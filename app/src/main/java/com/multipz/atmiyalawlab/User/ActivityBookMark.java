package com.multipz.atmiyalawlab.User;

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
import com.multipz.atmiyalawlab.Adapter.BookMarkTopLawyerAdapter;
import com.multipz.atmiyalawlab.Model.BookMarkModel;

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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityBookMark extends AppCompatActivity {
    RecyclerView rv_bookmark;
    ProgressDialog dialog;
    Context context;
    Shared shared;
    String param;
    ArrayList<BookMarkModel> list;
    ImageView img_back;
    BookMarkTopLawyerAdapter adapter;
    private RelativeLayout rel_root, rel_progress;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_mark);
        context = this;
        shared = new Shared(context);

        list = new ArrayList<>();
        rv_bookmark = (RecyclerView) findViewById(R.id.rv_bookmark);
        img_back = (ImageView) findViewById(R.id.img_back);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (Constant_method.checkConn(context)) {
            BokmarkLawyerList();
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }
    }

    private void BokmarkLawyerList() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        //    dialog.show();
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
                        JSONArray jsonArray = object.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            rel_progress.setVisibility(View.GONE);
                            JSONObject bookmark = jsonArray.getJSONObject(i);
                            BookMarkModel model = new BookMarkModel();
                            model.setAh_lawyer_id(bookmark.getString("ah_lawyer_id"));
                            model.setFull_name(bookmark.getString("full_name"));
                            model.setExperienceinyear(bookmark.getString("experienceinyear"));
                            model.setExperienceinmonth(bookmark.getString("experienceinmonth"));
                            model.setExperience(bookmark.getString("experience"));
                            model.setProfile_img(bookmark.getString("profile_img"));
                            model.setCity_name(bookmark.getString("city_name"));
                            model.setAvg_rating(bookmark.getString("avg_rating"));
                            model.setTotal_case_assign(bookmark.getString("total_case_assign"));
                            model.setTotal_rating_count(bookmark.getString("total_rating_count"));
                            model.setIs_available(bookmark.getString("is_available"));
                            model.setIs_bookmark(bookmark.getString("is_bookmark"));
                            model.setStatusid(bookmark.getString("statusid"));
                            model.setUser_status(bookmark.getString("user_status"));
                            list.add(model);


                        }

                        adapter = new BookMarkTopLawyerAdapter(context, list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        rv_bookmark.setLayoutManager(mLayoutManager);
                        rv_bookmark.setItemAnimator(new DefaultItemAnimator());
                        rv_bookmark.setAdapter(adapter);


                    } else if (status.contentEquals("0")){
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
              //  dialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.bookmarkLawyerList);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
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
}