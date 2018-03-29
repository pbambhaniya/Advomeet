package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JudgementDetailActivity extends AppCompatActivity {
    String tid;
    Context context;
    WebView webview_judgement;
    ProgressDialog dialog;
    String param;
    ImageView img_back;
    private CircularProgressView progressBar1;
    RelativeLayout rel_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judgement_detail);
        context = this;
        ref();
        init();


    }

    private void ref() {
        webview_judgement = (WebView) findViewById(R.id.webview_judgement);
        img_back = (ImageView) findViewById(R.id.img_back);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
    }

    private void init() {

        if (Constant_method.checkConn(context)) {
            getJudgementDeatil();
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }


        tid = getIntent().getStringExtra("tid");
//        Toaster.getToast(context, tid);

        WebSettings settings = webview_judgement.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDisplayZoomControls(true);
        settings.setBuiltInZoomControls(true);
        webview_judgement.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getJudgementDeatil() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;

        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String msg = "", status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object1 = jsonObject.getJSONObject("body");
                    msg = object1.getString("msg");
                    if (status.contentEquals("1")) {
                        rel_progress.setVisibility(View.GONE);
                        JSONArray data = object1.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject j = data.getJSONObject(i);
                            String judgement_detail_id = j.getString("judgement_detail_id");
                            String judgement_id = j.getString("judgement_id");
                            String docsource = j.getString("docsource");
                            String doc = j.getString("doc");
                            String publishdate = j.getString("publishdate");
                            String tid = j.getString("tid");
                            webview_judgement.loadData(String.format("%s", doc), "text/html", "UTF-8");
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
                    object.put("action", Config.judjment_detail);
                    JSONObject body = new JSONObject();
                    body.put("tid", tid);
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
