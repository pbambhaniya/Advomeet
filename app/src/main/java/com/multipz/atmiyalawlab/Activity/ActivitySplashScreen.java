package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.google.firebase.iid.FirebaseInstanceId;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.User.ActivityDashboardUser;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class ActivitySplashScreen extends AppCompatActivity {

    private RelativeLayout rel_root;
    Shared shared;
    Context context;
    private String param = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        context = this;
        shared = new Shared(context);
        reference();
        try {
            init();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    private void init() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
//                    Intent i = new Intent(ActivitySplashScreen.this, ActivitySelectUser.class);
//                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(i);
                    if (!shared.isLoggedIn()) {
                        Intent i = new Intent(ActivitySplashScreen.this, ActivitySelectUser.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        if (shared.getUsertype().contentEquals("L")) {

                            if (shared.isSubsciption()) {//true
                                Intent i1 = new Intent(ActivitySplashScreen.this, ActivityDashboard.class);
                                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i1);
                                finish();
                            } else {
                                Intent i1 = new Intent(ActivitySplashScreen.this, ActivitySubscriptionPlan.class);
                                i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(i1);
                                finish();
                            }
                        } else if (shared.getUsertype().contentEquals("U")) {
                            Intent i1 = new Intent(ActivitySplashScreen.this, ActivityDashboardUser.class);
                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i1);
                            finish();

                        } else {
                            Intent i1 = new Intent(ActivitySplashScreen.this, ActivitySelectUser.class);
                            i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i1);
                            finish();
                        }

                    }
                }
            }
        });
        t.start();

    }

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

        getConfigApi();
    }


    private void getConfigApi() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONObject data = object.getJSONObject("data");

                        JSONArray subscription = data.getJSONArray("subscription");
                        shared.putString(Config.subscription, subscription.toString());

                        JSONArray price_type = data.getJSONArray("price_type");
                        shared.putString(Config.price_type, price_type.toString());

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // dialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject object = new JSONObject();
                    object.put("action", Config.config);
                    param = object.toString();
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
