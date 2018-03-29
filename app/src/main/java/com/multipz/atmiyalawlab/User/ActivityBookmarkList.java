package com.multipz.atmiyalawlab.User;

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
import com.multipz.atmiyalawlab.Adapter.BookMarkAdapter;
import com.multipz.atmiyalawlab.Model.LawyerListModel;
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
import java.util.List;
import java.util.Map;

public class ActivityBookmarkList extends AppCompatActivity implements ItemClickListener {


    ImageView img_back;
    RecyclerView rv_bookmark;
    BookMarkAdapter adapter;
    private List<LawyerListModel> list = new ArrayList<>();
    private String param = "";
    private ProgressDialog dialog;
    private RelativeLayout rel_root, rel_progress;
    private Shared shared;
    Context context;
    private CircularProgressView progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark_list);
        context = this;
        reference();


    }

    private void reference() {

        context = this;
        shared = new Shared(context);
        img_back = (ImageView) findViewById(R.id.img_back);
        rv_bookmark = (RecyclerView) findViewById(R.id.rv_bookmark);
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

        if (Constant_method.checkConn(context)) {
            getTopLawyerList();
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }


    }

    private void getTopLawyerList() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(ActivityBookmarkList.this);
        dialog.setMessage("Loading...");
        //  dialog.show();
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
                        JSONArray jsonArray = object.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            LawyerListModel model = new LawyerListModel();
                            model.setAh_users_id(obj.getString("ah_lawyer_id"));
                            //  model.setAh_lawyer_id(obj.getInt("lawyer_id"));
                            model.setFull_name(obj.getString("full_name"));
                            model.setExperienceinyear(obj.getString("experienceinyear"));
                            model.setExperienceinmonth(obj.getString("experienceinmonth"));
                            model.setProfile_img(obj.getString("profile_img"));
                            model.setCity_name(obj.getString("city_name"));
                            model.setAvg_rating(obj.getString("avg_rating"));
                            model.setTotal_rating_count(obj.getString("total_rating_count"));
                            model.setTotal_case_assign(obj.getString("total_case_assign"));
                            model.setIs_available(obj.getString("is_available"));
                            list.add(model);

                        }
                        adapter = new BookMarkAdapter(context, list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        rv_bookmark.setLayoutManager(mLayoutManager);
                        rv_bookmark.setItemAnimator(new DefaultItemAnimator());
                        rv_bookmark.setAdapter(adapter);
                        adapter.setClickListener(ActivityBookmarkList.this);

                    } else if (status.contentEquals("0")) {
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(getApplicationContext(), "" + msg);
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
                    object.put("action", Config.getTopLawyersList);

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
    public void itemClicked(View View, int position) {

        LawyerListModel model = list.get(position);
        if (shared.getUsertype().contentEquals("U")) {
            Intent i = new Intent(ActivityBookmarkList.this, ActivityLawyerDetail.class);
            i.putExtra("lawyer_id", model.getAh_users_id());
            startActivity(i);
        }


    }
}
