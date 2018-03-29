package com.multipz.atmiyalawlab.Activity;

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
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.User.ActivityDashboardUser;
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

public class ActivityChangePassword extends AppCompatActivity {

    private RelativeLayout rel_root, rel_progress, btn_change_password;
    private LinearLayout layout_edit;
    private EditText edt_current_password, edt_new_password, edt_confirm_password;
    private TextView btnchangePassword;
    private ImageView imgClose;
    private String password = "", current_password = "", param = "";
    private Shared shared;
    private Context context;
    ProgressDialog dialog;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

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
        btn_change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_current_password.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.currentPassword);
                } else if (edt_new_password.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.newPassword);
                } else if (edt_confirm_password.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.confirmPassword);
                } else if (edt_new_password.getText().toString().contentEquals(edt_confirm_password.getText().toString())) {
                    current_password = edt_current_password.getText().toString();
                    password = edt_confirm_password.getText().toString();
                    if (Constant_method.checkConn(context)) {
                        ChangePassword(current_password, password);
                    } else {
                        Toaster.getToast(context, ErrorMessage.NoInternet);
                    }
                } else {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.confirmPasswordNotMatch);
                }
            }
        });


    }

    private void ChangePassword(final String current_password, final String password) {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rel_progress.setVisibility(View.GONE);
                String msg = "", status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (shared.getUsertype().contentEquals("L")) {
                        if (status.contentEquals("1")) {
                            Toaster.getToast(context, "" + msg);
                            Intent intent = new Intent(ActivityChangePassword.this, ActivityDashboard.class);
                            startActivity(intent);
                        } else {
                            rel_progress.setVisibility(View.GONE);
                            Toaster.getToast(context, "" + msg);
                        }
                    } else if (shared.getUsertype().contentEquals("U")) {
                        if (status.contentEquals("1")) {
                            Toaster.getToast(context, "" + msg);
                            Intent intent = new Intent(ActivityChangePassword.this, ActivityDashboardUser.class);
                            startActivity(intent);
                        } else {
                            rel_progress.setVisibility(View.GONE);
                            Toaster.getToast(context, "" + msg);
                        }
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
                    object.put("action", Config.changePassword);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("oldpassword", current_password);
                    body.put("newpassword", password);
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

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        layout_edit = (LinearLayout) findViewById(R.id.layout_edit);
        edt_current_password = (EditText) findViewById(R.id.edt_current_password);
        edt_new_password = (EditText) findViewById(R.id.edt_new_password);
        edt_confirm_password = (EditText) findViewById(R.id.edt_confirm_password);
        btnchangePassword = (TextView) findViewById(R.id.btnchangePassword);
        imgClose = (ImageView) findViewById(R.id.imgClose);
        btn_change_password = (RelativeLayout) findViewById(R.id.btn_change_password);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI(rel_root, ActivityChangePassword.this);
    }
}
