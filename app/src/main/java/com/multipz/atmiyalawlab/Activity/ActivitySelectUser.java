package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivitySelectUser extends AppCompatActivity {

    private RelativeLayout rel_root, rel_select_lawyer, rel_select_user;
    Shared shared;
    Context context;
    private ProgressDialog dialog;
    private String param = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        context = this;
        shared = new Shared(context);
        reference();
        init();

    }

    private void reference() {

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        rel_select_lawyer = (RelativeLayout) findViewById(R.id.rel_select_lawyer);
        rel_select_user = (RelativeLayout) findViewById(R.id.rel_select_user);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    private void init() {
        rel_select_lawyer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivitySelectUser.this, ActivityLoginScreen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                shared.setUsertype("L");
                startActivity(i);

            }
        });
        rel_select_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivitySelectUser.this, ActivityLoginScreen.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                shared.setUsertype("U");
                startActivity(i);
            }
        });
        if (Constant_method.checkConn(context)) {
            getConfigApi();
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }

    }


    private void getConfigApi() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(ActivitySelectUser.this);
        dialog.setMessage("Loading...");
        //  dialog.show();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                dialog.dismiss();
                int status;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONObject data = object.getJSONObject("data");

                        JSONArray country = data.getJSONArray("country");
                        shared.putString(Config.Country, country.toString());

                        JSONArray state = data.getJSONArray("state");
                        shared.putString(Config.State, state.toString());

                        JSONArray city = data.getJSONArray("city");
                        shared.putString(Config.City, city.toString());

                        JSONArray specialization = data.getJSONArray("specialization");
                        shared.putString(Config.specialization, specialization.toString());

                        JSONArray court = data.getJSONArray("court");
                        shared.putString(Config.court, court.toString());

                        JSONArray graduation = data.getJSONArray("graduation");
                        shared.putString(Config.graduation, graduation.toString());


                        JSONArray degree = data.getJSONArray("degree");
                        shared.putString(Config.degree, degree.toString());

                        JSONArray document = data.getJSONArray("document");
                        shared.putString(Config.document, document.toString());

                        JSONArray day = data.getJSONArray("day");
                        shared.putString(Config.day, day.toString());

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
                dialog.dismiss();
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