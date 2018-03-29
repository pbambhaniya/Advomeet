package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
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
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityPolicy extends AppCompatActivity {

    private RelativeLayout rel_root, rel_progress;
    private ImageView img_back;
    private TextView txt_name_title;
    private WebView wb_about;
    private ProgressDialog pDialog;
    private Context context;
    private String param = "";
    private int policyID = 0;
    private CircularProgressView progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_policy);
        context = this;
        reference();
        init();
    }

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_name_title = (TextView) findViewById(R.id.txt_name_title);
        wb_about = (WebView) findViewById(R.id.wb_about);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {
        WebSettings settings = wb_about.getSettings();
        settings.setJavaScriptEnabled(true);
        wb_about.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        Intent i = getIntent();
        policyID = i.getIntExtra("policyID", 0);
        if (policyID == 2) {
            txt_name_title.setText("About Us");
            if (Constant_method.checkConn(context)) {
                getPageContent(policyID);
            } else {
                Toaster.getToast(context, ErrorMessage.NoInternet);
            }

        } else if (policyID == 3) {
            txt_name_title.setText("Help & FAQ");
            wb_about.loadUrl("http://192.168.0.81:8083/uploads/faq.html");

        }
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    private void getPageContent(final int policyID) {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Loading...");
        //  pDialog.show();

        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                rel_progress.setVisibility(View.GONE);

                String Message, status;
                try {
                    JSONObject object = new JSONObject(response);
                    status = object.getString("status");
                    JSONObject o = object.getJSONObject("body");
                    Message = o.getString("msg");
                    if (status.contentEquals("1")) {
                        JSONArray jsonArray = o.getJSONArray("data");
                        for (int i1 = 0; i1 < jsonArray.length(); i1++) {
                            JSONObject c = jsonArray.getJSONObject(i1);
                            String ah_policy_id = c.getString("ah_policy_id");
                            String ah_policy_type_id = c.getString("ah_policy_type_id");
                            String policy_type = c.getString("policy_type");
                            String policy_description = c.getString("policy_description");
                            wb_about.loadData(String.format("%s", policy_description), "text/html", "UTF-8");
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
