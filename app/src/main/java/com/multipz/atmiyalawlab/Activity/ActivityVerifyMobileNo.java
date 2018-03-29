package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

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

public class ActivityVerifyMobileNo extends AppCompatActivity {

    private RelativeLayout rel_root, btn_submit, rel_progress;
    private EditText edt_verify_mobile_no;
    private String mobile;
    private Context context;
    private ProgressDialog dialog;
    private String param;
    Shared shared;
    private String android_id;
    String ah_otp_id, OTP, expirein;
    private CircularProgressView progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_otp);
        context = this;
        shared = new Shared(context);
        reference();
        init();

    }

    private void reference() {
        android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        edt_verify_mobile_no = (EditText) findViewById(R.id.edt_verify_mobile_no);
        btn_submit = (RelativeLayout) findViewById(R.id.btn_submit);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI(rel_root, ActivityVerifyMobileNo.this);

    }

    private void init() {
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile = edt_verify_mobile_no.getText().toString();
                if (mobile.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterMobile);
                } else if (mobile.toString().length() <= 9) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterValidMobile);
                } else if (Constant_method.checkConn(context)) {
                    VerifyMobileNo();
                } else {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.NoInternet);
                }

            }
        });

    }

    private void VerifyMobileNo() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;

        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rel_progress.setVisibility(View.GONE);

                int status;
                String msg = null;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (status == 1) {

                        String userid = object.getString("ah_users_id");
                        ah_otp_id = object.getString("ah_otp_id");
                        OTP = object.getString("OTP");
                        expirein = object.getString("expirein");
                        shared.setUserId(userid);
                        Intent intent = new Intent(ActivityVerifyMobileNo.this, ActivityVerifyOTP.class);
                        intent.putExtra("mobileno", mobile);
                        intent.putExtra("otp_id", ah_otp_id);
                        intent.putExtra("Otp", OTP);
                        intent.putExtra("expirein", expirein);
                        startActivity(intent);
                        Toaster.getToast(context, "" + msg);
                    } else {
                        Toaster.getToast(context, "" + msg);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                rel_progress.setVisibility(View.GONE);
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
                    body.put("service_type", "G");
                    body.put("ah_users_id", "");
                    body.put("ah_otp_id", "");
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
}
