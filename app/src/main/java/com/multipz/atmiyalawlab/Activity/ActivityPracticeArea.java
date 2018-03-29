package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Adapter.PractiseAreaAdapter;
import com.multipz.atmiyalawlab.Model.PractiseAreaModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityPracticeArea extends AppCompatActivity implements ItemClickListener {

    ImageView img_back;
    RecyclerView rv_specilization;
    ProgressDialog dialog;
    PractiseAreaAdapter adapter;
    ArrayList<PractiseAreaModel> list;
    Context context;
    String param;
    private RelativeLayout rel_root;
    Shared shared;
    RelativeLayout rel_progress;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_area);

        context = this;
        shared = new Shared(context);
        list = new ArrayList<>();

        reference();
        init();
    }

    private void reference() {
        rv_specilization = (RecyclerView) findViewById(R.id.rv_specilization);
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {
        getSetting();
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getSetting() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int status;

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getInt("status");
                    if (status == 1) {
//                        dialog.dismiss();
                        rel_progress.setVisibility(View.GONE);
                        JSONObject object = jsonObject.getJSONObject("body");
                        String msg = object.getString("msg");
                        JSONObject dataobj = object.getJSONObject("data");
                        JSONArray account_details = dataobj.getJSONArray("account_details");
                        for (int i = 0; i < account_details.length(); i++) {
                            JSONObject c = account_details.getJSONObject(i);
                        }
                        JSONArray availability = dataobj.getJSONArray("availability");
                        for (int i = 0; i < availability.length(); i++) {
                            JSONObject a = availability.getJSONObject(i);
                        }

                        JSONArray specilization = dataobj.getJSONArray("specialization");
                        for (int i = 0; i < specilization.length(); i++) {
                            JSONObject a = specilization.getJSONObject(i);
                            PractiseAreaModel model = new PractiseAreaModel();
                            model.setAh_lawyer_specialist_id(a.getString("ah_lawyer_specialist_id"));
                            model.setAh_lawyer_type_id(a.getString("ah_lawyer_type_id"));
                            model.setLawyer_type(a.getString("lawyer_type"));
                            model.setIs_selected(a.getBoolean("is_selected"));
                            list.add(model);
                        }

                        JSONArray language = dataobj.getJSONArray("language");

                        for (int i = 0; i < language.length(); i++) {
                            JSONObject a = language.getJSONObject(i);

                        }
                        adapter = new PractiseAreaAdapter(context, list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        rv_specilization.setLayoutManager(mLayoutManager);
                        rv_specilization.setItemAnimator(new DefaultItemAnimator());
                        rv_specilization.setAdapter(adapter);
                        rv_specilization.setNestedScrollingEnabled(false);
                        //adapter.setClickListener(new LawyerLanguageActivity());
                        adapter.notifyDataSetChanged();

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
                    JSONObject main = new JSONObject();
                    main.put("action", Config.getSetting);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("type", shared.getUsertype());
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                String param = "{\"action\":\"getcourt\"}";
                params.put("json", param);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void itemClicked(View View, int position) {

    }
}