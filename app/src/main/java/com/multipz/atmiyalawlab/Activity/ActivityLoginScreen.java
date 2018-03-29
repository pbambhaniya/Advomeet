package com.multipz.atmiyalawlab.Activity;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.multipz.atmiyalawlab.Adapter.BannerAdapter;
import com.multipz.atmiyalawlab.Adapter.JudgementAdapter;
import com.multipz.atmiyalawlab.Adapter.LawyerTopAdapter;
import com.multipz.atmiyalawlab.Adapter.NewsAdapter;
import com.multipz.atmiyalawlab.Fragment.Dashboard;
import com.multipz.atmiyalawlab.Model.BannerModel;
import com.multipz.atmiyalawlab.Model.JudjmentModel;
import com.multipz.atmiyalawlab.Model.NewsModel;
import com.multipz.atmiyalawlab.Model.TopLawyerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.User.ActivityDashboardUser;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.ProgressLoader;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityLoginScreen extends AppCompatActivity {

    private RelativeLayout rel_root, btn_login, rel_progress;
    private EditText edt_number, edt_password;
    private ImageView imgright, imgright_password, img_back;
    private TextView forgot_password, btn_signin;
    private String mobile_no, password;
    private Context context;
    private ProgressDialog dialog;
    private CircleImageView imgsetuser;
    private String param = "";
    Shared shared;
    private CircularProgressView progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        context = this;
        shared = new Shared(context);

        reference();
        init();

        if (shared.getUsertype().contentEquals("U")) {
            imgsetuser.setBackground(getResources().getDrawable(R.drawable.user_default));
        } else if (shared.getUsertype().contentEquals("L")) {
            imgsetuser.setBackground(getResources().getDrawable(R.drawable.lawyer_default));

        }


    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        btn_login = (RelativeLayout) findViewById(R.id.btn_login);
        edt_number = (EditText) findViewById(R.id.edt_number);
        edt_password = (EditText) findViewById(R.id.edt_password);
        imgright = (ImageView) findViewById(R.id.imgright);
        imgright_password = (ImageView) findViewById(R.id.imgright_password);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        btn_signin = (TextView) findViewById(R.id.btn_signin);
        imgsetuser = (CircleImageView) findViewById(R.id.imgsetuser);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();


        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI(rel_root, ActivityLoginScreen.this);

    }


    @SuppressLint("WrongConstant")
    private void init() {
        //ProgressLoader.PLoader(context, progressBar1);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        imgright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edt_password.requestFocus();

            }
        });
        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityLoginScreen.this, ActivityVerifyMobileNo.class);
                startActivity(i);

            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobile_no = edt_number.getText().toString();
                password = edt_password.getText().toString();
                if (mobile_no.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterMobile);
                } else if (mobile_no.toString().length() <= 9) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterValidMobile);
                } else if (password.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterPassword);
                } else {
                    if (Constant_method.checkConn(context)) {
                        getLoginApiCall();
                    } else {
                        Toast.makeText(context, ErrorMessage.NoInternet, Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    private void getLoginApiCall() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                dialog.dismiss();
                String msg = "", status = "";
                rel_progress.setVisibility(View.GONE);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        JSONArray jsonArray = object.getJSONArray("data");
                        shared.putString(Config.Logindata, jsonArray.toString());
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
                            shared.setUsertype(type);
                            shared.setlogin(true);
                            if (shared.getUsertype().contentEquals("L")) {
                             /*   Intent intent = new Intent(ActivityLoginScreen.this, ActivityDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);*/
                                getDashboardApi();
                            } else if (shared.getUsertype().contentEquals("U")) {
                                Intent intent = new Intent(ActivityLoginScreen.this, ActivityDashboardUser.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }

                        }
                        // Toaster.getToast(getApplicationContext(), "" + msg);
                    } else if (status.contentEquals("0")) {
                        Toaster.getToast(getApplicationContext(), "" + msg);
//                        dialog.dismiss();
                        rel_progress.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
//                dialog.dismiss();
                rel_progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject object = new JSONObject();
                    object.put("action", Config.login);
                    JSONObject body = new JSONObject();
                    body.put("mobile_number", mobile_no);
                    body.put("password", password);
                    body.put("type", shared.getUsertype());
                    body.put("notification_token", FirebaseInstanceId.getInstance().getToken());
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


    private void getDashboardApi() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        //  dialog.show();
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rel_progress.setVisibility(View.GONE);
                String msg, status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        JSONObject data = object.getJSONObject("data");
                        JSONArray subscription = data.getJSONArray("subscription");
                        for (int s = 0; s <= subscription.length(); s++) {
                            JSONObject subObj = subscription.getJSONObject(s);
                            int ah_user_subscription_id = subObj.getInt("ah_user_subscription_id");
                            boolean is_subscribe = subObj.getBoolean("is_subscribe");
                            shared.setIsSubsciribe(is_subscribe);
                            if (!is_subscribe) {
                                Intent i = new Intent(ActivityLoginScreen.this, ActivitySubscriptionPlan.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i);
                            } else {
                                Intent intent = new Intent(ActivityLoginScreen.this, ActivityDashboard.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    } else if (status.contentEquals("0")) {
                        Toaster.getToast(getApplicationContext(), msg);
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
                    JSONObject body = new JSONObject();
                    object.put("action", Config.dashboard);
                    body.put("ah_users_id", shared.getUserId());
                    body.put("type", shared.getUsertype());
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





