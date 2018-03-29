package com.multipz.atmiyalawlab.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Activity.ActivityDashboard;
import com.multipz.atmiyalawlab.Activity.ActivityLoginScreen;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityChangeContactNo extends AppCompatActivity {
    private ImageView imgClose;
    private EditText edt_old_contact_no, edt_new_Contact_no, edt_otp;
    private TextView btnSendOtp, btnChange;
    private LinearLayout layout_hide_show_otp;
    private ProgressDialog dialog;
    private String param = "", oldCno = "", newCno = "";
    private boolean IsOTPVerify = false;
    private RelativeLayout rel_root, rel_progress, sendotp;
    private Shared shared;
    private Context context;
    private int ah_otp_id = 0;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_contact_no);
        context = this;
        shared = new Shared(context);

        reference();
        init();
    }

    private void init() {

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_old_contact_no.getText().toString().contentEquals("") || edt_old_contact_no.getText().toString().length() <= 9) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.oldno);
                } else if (!edt_old_contact_no.getText().toString().contentEquals(shared.getMobileno())) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterValidMobile);
                } else if (edt_new_Contact_no.getText().toString().contentEquals("") || edt_new_Contact_no.getText().toString().length() <= 9) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.newno);
                } else {
                    oldCno = edt_old_contact_no.getText().toString().trim();
                    newCno = edt_new_Contact_no.getText().toString().trim();
                    if (Constant_method.checkConn(context)) {
                        ChangeContactNo("G");
                    } else {
                        Toaster.getToast(context, ErrorMessage.NoInternet);
                    }

                }
            }
        });


        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_otp.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterOTP);
                } else {
                    if (IsOTPVerify) {
                        if (Constant_method.checkConn(context)) {
                            ChangeContactNo("V");
                        } else {
                            Toaster.getToast(context, ErrorMessage.NoInternet);
                        }
                    }
                }

            }
        });
    }

    private void reference() {
        imgClose = (ImageView) findViewById(R.id.imgClose);
        edt_old_contact_no = (EditText) findViewById(R.id.edt_old_contact_no);
        edt_new_Contact_no = (EditText) findViewById(R.id.edt_new_Contact_no);
        edt_otp = (EditText) findViewById(R.id.edt_otp);
        btnSendOtp = (TextView) findViewById(R.id.btnSendOtp);
        btnChange = (TextView) findViewById(R.id.btnChange);
        layout_hide_show_otp = (LinearLayout) findViewById(R.id.layout_hide_show_otp);
        sendotp = (RelativeLayout) findViewById(R.id.sendotp);
        rel_root = (RelativeLayout) findViewById(R.id.root_layout);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        Application.setFontDefault((RelativeLayout) findViewById(R.id.root_layout));
        HideSoftKeyboard.setupUI((RelativeLayout) findViewById(R.id.root_layout), ActivityChangeContactNo.this);

    }

    private void ChangeContactNo(final String stype) {
        String tag_string_req = "string_req";

        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        //dialog.show();
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    String msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        rel_progress.setVisibility(View.GONE);

                        if (IsOTPVerify) {
                            JSONArray jsonArray = object.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject obj = jsonArray.getJSONObject(i);
                                String user_id = obj.getString("ah_users_id");
                                shared.setUserId(user_id);
                                String m_no = obj.getString("mobile_number");
                                shared.setMobileno(m_no);
                                String f_name = obj.getString("name");
                                shared.setUserName(f_name);
                                String email = obj.getString("email");
                                shared.setUserEmail(email);
                                String gender = obj.getString("gender");
                                String type = obj.getString("type");
                                if (type.contentEquals("L")) {
                                    Intent intent = new Intent(ActivityChangeContactNo.this, ActivityDashboard.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                } else if (type.contentEquals("U")) {
                                    Intent intent = new Intent(ActivityChangeContactNo.this, ActivityDashboardUser.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                }

                            }
                            Toaster.getToast(getApplicationContext(), ErrorMessage.numberChanged);

                        } else {
                            rel_progress.setVisibility(View.GONE);
                            IsOTPVerify = true;
                            String ah_users_id = object.getString("ah_users_id");
                            ah_otp_id = object.getInt("ah_otp_id");
                            String OTP = object.getString("OTP");
                            layout_hide_show_otp.setVisibility(View.VISIBLE);
                            edt_otp.setText(OTP);
                            Toaster.getToast(getApplicationContext(), msg);
                        }

                    } else {
                        dialog.dismiss();
                        Toaster.getToast(getApplicationContext(), msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                dialog.hide();
                Toaster.getToast(getApplicationContext(), "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.ChangeContactNo);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("mobile_number", newCno);
                    if (!IsOTPVerify) {
                        body.put("ah_otp_id", "");
                    } else {
                        body.put("ah_otp_id", ah_otp_id);
                    }
                    body.put("service_type", stype);
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("json", param);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
