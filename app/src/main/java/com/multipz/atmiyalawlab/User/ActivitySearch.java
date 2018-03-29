package com.multipz.atmiyalawlab.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Adapter.SearchUserAdapter;
import com.multipz.atmiyalawlab.Model.SearchuserModel;
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

public class ActivitySearch extends AppCompatActivity implements ItemClickListener {
    String city, state, lawyer, city_id, lawyer_id;
    TextView txt_city, txt_state, txt_lawyer;
    RecyclerView rv_seach;
    ProgressDialog dialog;
    Context context;
    Shared shared;
    ArrayList<SearchuserModel> list;
    String param;
    SearchUserAdapter adapter;
    ImageView img_back, img_search;
    SearchuserModel model;
    private RelativeLayout rel_root, rel_progress;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        context = this;
        shared = new Shared(context);
        list = new ArrayList<>();

        txt_city = (TextView) findViewById(R.id.txt_city);
        txt_state = (TextView) findViewById(R.id.txt_state);

        txt_lawyer = (TextView) findViewById(R.id.txt_lawyer);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_search = (ImageView) findViewById(R.id.img_search);
        rv_seach = (RecyclerView) findViewById(R.id.rv_seach);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));


        city = getIntent().getStringExtra("city");
        state = getIntent().getStringExtra("state");
        lawyer = getIntent().getStringExtra("lawyer");

        city_id = getIntent().getStringExtra("city_id");
        lawyer_id = getIntent().getStringExtra("lawyer_id");

        txt_city.setText(city);
        txt_state.setText(state);
        txt_lawyer.setText(lawyer);

        if (Constant_method.checkConn(context)) {
            Searchlawyer();
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        img_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivitySearch.this, ActivitySearchLawyer.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void Searchlawyer() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        //     dialog.show();
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String msg = "", status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        rel_progress.setVisibility(View.GONE);
                        //Toaster.getToast(getApplicationContext(), msg);
                        JSONArray textarray = object.getJSONArray("data");
                        for (int i = 0; i < textarray.length(); i++) {
                            JSONObject text = textarray.getJSONObject(i);
                            model = new SearchuserModel();
                            model.setAh_lawyer_id(text.getString("ah_lawyer_id"));
                            model.setFull_name(text.getString("full_name"));
                            model.setExperience(text.getString("experience"));
                            model.setProfile_img(text.getString("profile_img"));
                            model.setCity_name(text.getString("city_name"));
                            model.setAvg_rating(text.getString("avg_rating"));
                            model.setTotal_rating_count(text.getString("total_rating_count"));
                            model.setTotal_case_assign(text.getString("total_case_assign"));
                            model.setIs_available(text.getString("is_available"));
                            model.setIs_bookmark(text.getString("is_bookmark"));
                            model.setIs_bookmark(text.getString("is_bookmark"));
                            model.setUser_status(text.getString("user_status"));
                            list.add(model);
                        }
                        adapter = new SearchUserAdapter(context, list);
                        rv_seach.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        rv_seach.setAdapter(adapter);
                        adapter.setClickListener(ActivitySearch.this);
                        rv_seach.setNestedScrollingEnabled(false);
                    } else {
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(context, "" + msg);
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
                    main.put("action", Config.searchAdvocate);
                    JSONObject body = new JSONObject();
                    body.put("ah_city_id", city_id);
                    body.put("ah_lawyer_type_id", lawyer_id);
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

    @Override
    public void itemClicked(View View, int position) {
        Intent intent = new Intent(ActivitySearch.this, ActivityLawyerDetail.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("lawyer_id", model.getAh_lawyer_id());
        startActivity(intent);
    }
}