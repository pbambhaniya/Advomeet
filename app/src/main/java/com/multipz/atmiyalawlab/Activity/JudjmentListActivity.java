package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Adapter.JudgementDetailAdapter;
import com.multipz.atmiyalawlab.Model.JudjmentModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JudjmentListActivity extends AppCompatActivity implements ItemClickListener {

    RelativeLayout rel_root;
    RecyclerView rv_judgement;
    ImageView img_back;
    Context context;
    Shared shared;
    ArrayList<JudjmentModel> mlistJudgement;
    ProgressDialog dialog;
    String param;
    JudgementDetailAdapter judgementAdapter;
    JudjmentModel model;
    private CircularProgressView progressBar1;
    RelativeLayout rel_progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_judjment_list);
        context = this;
        shared = new Shared(context);
        ref();
        init();
    }

    private void init() {
        if (Constant_method.checkConn(context)) {
            getJudgementDetail();
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


    private void ref() {
        mlistJudgement = new ArrayList<>();
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        rv_judgement = (RecyclerView) findViewById(R.id.rv_judgement);
        img_back = (ImageView) findViewById(R.id.img_back);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void getJudgementDetail() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
//                dialog.dismiss();
                rel_progress.setVisibility(View.GONE);
                String msg = "", status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object1 = jsonObject.getJSONObject("body");
                    msg = object1.getString("msg");
                    if (status.contentEquals("1")) {
                        JSONArray data = object1.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject j = data.getJSONObject(i);
                            model = new JudjmentModel();
                            model.setJudgement_id(j.getString("judgement_id"));
                            model.setNumcites(j.getString("numcites"));
                            model.setTitle(j.getString("title"));
                            model.setDoctype(j.getString("doctype"));
                            model.setFragment(j.getString("fragment"));
                            model.setDocsource(j.getString("docsource"));
                            model.setDocsize(j.getString("docsize"));
                            model.setPublishdate(j.getString("publishdate"));
                            model.setTid(j.getString("tid"));
                            mlistJudgement.add(model);
                        }
                        judgementAdapter = new JudgementDetailAdapter(mlistJudgement, context);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        rv_judgement.setLayoutManager(mLayoutManager);
                        rv_judgement.setItemAnimator(new DefaultItemAnimator());
                        rv_judgement.setAdapter(judgementAdapter);
                        judgementAdapter.setClickListener(JudjmentListActivity.this);

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
                Log.d("error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject object = new JSONObject();
                    object.put("action", Config.judjment);
                    JSONObject body = new JSONObject();
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

    @Override
    public void itemClicked(View view, int position) {
        if (view.getId() == R.id.lnr_judgement)
            model = mlistJudgement.get(position);
        String tid = model.getTid();
        Intent intent = new Intent(JudjmentListActivity.this, JudgementDetailActivity.class);
        intent.putExtra("tid", tid);
        startActivity(intent);
    }
}
