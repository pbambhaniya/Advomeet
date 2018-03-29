package com.multipz.atmiyalawlab.Activity;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityPolicyAndTermCondition extends AppCompatActivity {

    private TextView txt_term_condition, txt_privacy_policy;
    private WebView wb_policy;
    private ImageView img_back;
    private ProgressDialog pDialog;
    private Context context;
    private String param = "";
    private RelativeLayout rel_progress;
    private CircularProgressView progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy_and_term_condition);
        context = this;
        reference();
        init();

    }

    private void reference() {
        txt_term_condition = (TextView) findViewById(R.id.txt_term_condition);
        txt_privacy_policy = (TextView) findViewById(R.id.txt_privacy_policy);
        wb_policy = (WebView) findViewById(R.id.wb_policy);
        img_back = (ImageView) findViewById(R.id.img_back);

        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();


        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        if (Constant_method.checkConn(context)) {
            getPageContent(1);
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }

        txt_term_condition.setBackgroundResource(R.drawable.bg_btn_null_app_border_myaccount);
        txt_term_condition.setTextColor(Color.parseColor("#FFFFFF"));
        txt_term_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant_method.checkConn(context)) {
                    getPageContent(1);
                } else {
                    Toaster.getToast(context, ErrorMessage.NoInternet);
                }

                txt_term_condition.setBackgroundResource(R.drawable.bg_btn_sub_solid_myaccount_left);
                txt_term_condition.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_privacy_policy.setBackgroundResource(R.drawable.bg_btn_right_null_myaccount);
                txt_privacy_policy.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });
        txt_privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant_method.checkConn(context)) {
                    getPageContent(4);
                } else {
                    Toaster.getToast(context, ErrorMessage.NoInternet);
                }
                txt_privacy_policy.setBackgroundResource(R.drawable.bg_btn_sub_solid_myaccount_right);
                txt_privacy_policy.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_term_condition.setBackgroundResource(R.drawable.bg_btn_left_null_myaccount);
                txt_term_condition.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });

    }

    private void getPageContent(final int policyID) {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Message, status;
                try {
                    JSONObject object = new JSONObject(response);
                    status = object.getString("status");
                    JSONObject o = object.getJSONObject("body");
                    Message = o.getString("msg");
                    if (status.contentEquals("1")) {
                        rel_progress.setVisibility(View.GONE);
                        JSONArray jsonArray = o.getJSONArray("data");
                        for (int i1 = 0; i1 < jsonArray.length(); i1++) {
                            JSONObject c = jsonArray.getJSONObject(i1);
                            String ah_policy_id = c.getString("ah_policy_id");
                            String ah_policy_type_id = c.getString("ah_policy_type_id");
                            String policy_type = c.getString("policy_type");
                            String policy_description = c.getString("policy_description");
                            wb_policy.loadData(String.format("%s", policy_description), "text/html", "UTF-8");
                        }
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
                VolleyLog.d("", "Error: " + error.getMessage());
                pDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.getPolicy);
                    JSONObject user = new JSONObject();
                    user.put("ah_policy_type_id", policyID);
                    main.put("body", user);
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
