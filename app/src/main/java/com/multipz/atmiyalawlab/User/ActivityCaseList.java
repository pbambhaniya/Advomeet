package com.multipz.atmiyalawlab.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Activity.ActivityCasesList;
import com.multipz.atmiyalawlab.Activity.ActivityLawyerBookAppointment;
import com.multipz.atmiyalawlab.Adapter.AcceptedCaseLawyerListAdapter;
import com.multipz.atmiyalawlab.Adapter.BookMarkAdapter;
import com.multipz.atmiyalawlab.Adapter.PostCaseAdapter;
import com.multipz.atmiyalawlab.Model.CasesModel;
import com.multipz.atmiyalawlab.Model.LawyerListModel;
import com.multipz.atmiyalawlab.Model.PostCaseModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.MyAsyncTask;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActivityCaseList extends AppCompatActivity implements ItemClickListener {

    ImageView img_back;
    RecyclerView recyclerview;
    AcceptedCaseLawyerListAdapter adapter;
    private TextView txt_description, txt_date;
    List<LawyerListModel> list = new ArrayList<>();
    Context context;
    private String title = "", description = "", param = "", createdate = "";
    private int caseid = 0;
    private Shared shared;
    private RelativeLayout rel_root, rel_progress;
    private int case_id = 0;
    private ProgressDialog dialog;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_case_list);
        context = this;
        shared = new Shared(context);
        reference();
        init();

    }

    private void reference() {

        img_back = (ImageView) findViewById(R.id.img_back);
        recyclerview = (RecyclerView) findViewById(R.id.recyclerview);
        txt_description = (TextView) findViewById(R.id.txt_description);
        txt_date = (TextView) findViewById(R.id.txt_date);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {

        Intent i = getIntent();
        case_id = i.getIntExtra("case_id", 0);
        description = i.getStringExtra("description");
        createdate = i.getStringExtra("createdate");

        txt_description.setText(description);
        txt_date.setText("Create On " + createdate);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        if (Constant_method.checkConn(context)) {
            getTopLawyerList();
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }
    }

    private void getTopLawyerList() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(ActivityCaseList.this);
        dialog.setMessage("Loading...");
        //  dialog.show();
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rel_progress.setVisibility(View.GONE);
                String msg = "", status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        JSONArray jsonArray = object.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            LawyerListModel model = new LawyerListModel();
                            model.setCase_request_id(obj.getInt("case_request_id"));
                            model.setCase_id(obj.getInt("case_id"));
                            model.setCase_request_id(obj.getInt("case_request_status_id"));
                            model.setAh_lawyer_id(obj.getInt("ah_lawyer_id"));
                            //  model.setAh_users_id(obj.getString("ah_users_id"));
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
                        adapter = new AcceptedCaseLawyerListAdapter(context, list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        recyclerview.setLayoutManager(mLayoutManager);
                        recyclerview.setItemAnimator(new DefaultItemAnimator());
                        recyclerview.setAdapter(adapter);
                        adapter.setClickListener(ActivityCaseList.this);

                    } else if (status.contentEquals("0")) {
                        rel_progress.setVisibility(View.GONE);
                        popUpAlertNocase();
                        //   Toaster.getToast(getApplicationContext(), "" + msg);
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
                    JSONObject body = new JSONObject();
                    object.put("action", Config.acceptedCaseLawyerList);
                    body.put("case_id", case_id);
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
    public void itemClicked(View View, int position) {
        LawyerListModel model = list.get(position);
        String lawyer_id = String.valueOf(model.getAh_lawyer_id());
        Intent i = new Intent(ActivityCaseList.this, ActivityLawyerDetail.class);
        i.putExtra("lawyer_id", lawyer_id);
        startActivity(i);
    }

    public void popUpAlertNocase() {
        LayoutInflater inflater = ActivityCaseList.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_submit, null);
        TextView txt_des = (TextView) c.findViewById(R.id.txt_des);
        LinearLayout lnr_ok = (LinearLayout) c.findViewById(R.id.lnr_ok);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityCaseList.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txt_des.setText(getResources().getString(R.string.nocaselist));
        lnr_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

    }

}
