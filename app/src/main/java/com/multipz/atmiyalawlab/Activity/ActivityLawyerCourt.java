package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
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
import com.multipz.atmiyalawlab.Adapter.CourtAdapter;
import com.multipz.atmiyalawlab.Adapter.SettingCourtAdapter;
import com.multipz.atmiyalawlab.Model.CourtModel;
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

public class ActivityLawyerCourt extends AppCompatActivity implements ItemClickListener {
    ImageView img_back;
    RecyclerView rv_court;
    ProgressDialog dialog;
    SettingCourtAdapter adapter;
    ArrayList<CourtModel> list;
    Context context;
    String param;
    private RelativeLayout rel_root;
    Shared shared;
    private CircularProgressView progressBar1;
    private RelativeLayout rel_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_court);
        context = this;
        shared = new Shared(context);
        list = new ArrayList<>();

        reference();
        init();
    }

    private void reference() {
        rv_court = (RecyclerView) findViewById(R.id.rv_court);
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {
        if (Constant_method.checkConn(context)) {
            getLanguage();
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

    private void getLanguage() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
//        dialog.show();
        rel_progress.setVisibility(View.VISIBLE);

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String status = "";
                rel_progress.setVisibility(View.GONE);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    String msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        JSONObject dataobj = object.getJSONObject("data");
                        JSONArray account_details = dataobj.getJSONArray("account_details");
                        for (int i = 0; i < account_details.length(); i++) {
                            JSONObject c = account_details.getJSONObject(i);
                        }
                        JSONArray availability = dataobj.getJSONArray("availability");
                        for (int i = 0; i < availability.length(); i++) {
                            JSONObject a = availability.getJSONObject(i);
                        }

                        JSONArray language = dataobj.getJSONArray("language");

                        for (int i = 0; i < language.length(); i++) {
                            JSONObject a = language.getJSONObject(i);

                        }
                        JSONArray court = dataobj.getJSONArray("court");
                        for (int i = 0; i < court.length(); i++) {
                            JSONObject c = court.getJSONObject(i);
                            CourtModel model = new CourtModel();
                            model.setAh_lawyer_court_id(c.getString("ah_lawyer_court_id"));
                            model.setAh_court_id(c.getString("ah_court_id"));
                            model.setCourt_name(c.getString("court_name"));
                            model.setIs_selected(c.getBoolean("is_selected"));
                            list.add(model);

                        }
                        adapter = new SettingCourtAdapter(context, list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        rv_court.setLayoutManager(mLayoutManager);
                        rv_court.setItemAnimator(new DefaultItemAnimator());
                        rv_court.setAdapter(adapter);
                        rv_court.setNestedScrollingEnabled(false);
                        // adapter.setClickListener(new LawyerLanguageActivity());
                        adapter.notifyDataSetChanged();
                    } else if (status.contentEquals("0")) {
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(getApplicationContext(), msg);
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
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void itemClicked(View View, int position) {

    }
}