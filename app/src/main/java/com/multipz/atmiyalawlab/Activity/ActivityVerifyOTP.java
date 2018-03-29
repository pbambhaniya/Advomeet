package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityVerifyOTP extends AppCompatActivity {

    private RelativeLayout rel_root, btn_verify_otp, rel_progress;
    private TextView txt_mobile_number, btn_resend;
    private EditText edt_1, edt_2, edt_3, edt_4;
    String otp_id, Otp, expirein, mobile, android_id, param;
    ProgressDialog dialog;
    Context context;
    Shared shared;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp2);
        context = this;
        shared = new Shared(context);
        reference();
        init();

    }

    private void reference() {
        android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        txt_mobile_number = (TextView) findViewById(R.id.txt_mobile_number);
        btn_resend = (TextView) findViewById(R.id.btn_resend);
        edt_1 = (EditText) findViewById(R.id.edt_1);
        edt_2 = (EditText) findViewById(R.id.edt_2);
        edt_3 = (EditText) findViewById(R.id.edt_3);
        edt_4 = (EditText) findViewById(R.id.edt_4);
        btn_verify_otp = (RelativeLayout) findViewById(R.id.btn_verify_otp);

        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI(rel_root, ActivityVerifyOTP.this);


        otp_id = getIntent().getStringExtra("otp_id");
        Otp = getIntent().getStringExtra("Otp");

        String fisrt = Otp.substring(0, 1);
        edt_1.setText(fisrt);
        String Second = Otp.substring(1, 2);
        edt_2.setText(Second);
        String Third = Otp.substring(2, 3);
        edt_3.setText(Third);
        String four = Otp.substring(3, 4);
        edt_4.setText(four);
        expirein = getIntent().getStringExtra("expirein");
        mobile = getIntent().getStringExtra("mobileno");
        txt_mobile_number.setText("+91 " + mobile);


    }

    private void init() {
        edt_1.addTextChangedListener(new GenericTextWatcher(edt_1));
        edt_2.addTextChangedListener(new GenericTextWatcher(edt_2));
        edt_3.addTextChangedListener(new GenericTextWatcher(edt_3));
        edt_4.addTextChangedListener(new GenericTextWatcher(edt_4));


        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btn_verify_otp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant_method.checkConn(context)) {
                    getRegisterOTP();
                } else {
                    Toaster.getToast(context, ErrorMessage.NoInternet);
                }
            }
        });
    }


    private void getRegisterOTP() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;

        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    if (status.contentEquals("1")) {
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        Toaster.getToast(context, "" + msg);
                        String user_id = object.getString("ah_users_id");
                        String type = object.getString("type");
                        shared.setUserId(user_id);
                        Intent intent = new Intent(ActivityVerifyOTP.this, PersonalInformation.class);
                        intent.putExtra("mobile", mobile);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
// shared.putString(Config.country, jsonArray.toString());
                    } else if (status.contentEquals("0")) {
                        rel_progress.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();


                try {
                    JSONObject object = new JSONObject();
                    object.put("action", Config.registerOTP);
                    JSONObject body = new JSONObject();
                    body.put("mobile_number", mobile);
                    body.put("type", shared.getUsertype());
                    body.put("device_id", android_id);
                    body.put("notification_token", FirebaseInstanceId.getInstance().getToken());
                    body.put("service_type", "V");
                    body.put("ah_users_id", shared.getUserId());
                    body.put("ah_otp_id", otp_id);
                    object.put("body", body);
                    param = object.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                String param = "{\"action\":\"" + Config.getcountry + "\"}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    public class GenericTextWatcher implements TextWatcher {
        private View view;

        private GenericTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (view.getId()) {
                case R.id.edt_1:
                    if (text.length() == 1) {
                        edt_2.requestFocus();
                    }
                    break;
                case R.id.edt_2:
                    if (text.length() == 1) {
                        edt_3.requestFocus();
                    } else if (text.length() == 0) {
                        edt_1.requestFocus();
                    }
                    break;
                case R.id.edt_3:
                    if (text.length() == 1) {
                        edt_4.requestFocus();
                    } else if (text.length() == 0) {
                        edt_2.requestFocus();
                    }
                    break;
                case R.id.edt_4:
                    if (text.length() == 0) {
                        edt_3.requestFocus();
                    }
                    break;
            }
        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub


        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

}
