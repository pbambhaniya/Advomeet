package com.multipz.atmiyalawlab.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Activity.ActivityDashboard;
import com.multipz.atmiyalawlab.Adapter.MyEarningAdapter;
import com.multipz.atmiyalawlab.Model.ModelMyEarnData;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAccount extends Fragment {

    private TextView txtEarning, tctMySubscriptionPlan;
    private RelativeLayout rel_root, rel_erning, rel_subscription_plan, rel_progress;
    private RecyclerView listShowEarningPLan;
    private ArrayList<ModelMyEarnData> earnList;
    private MyEarningAdapter adapter;
    private Context mContext;
    private ProgressDialog dialog;
    private Shared shared;
    private String param = "";
    private CircularProgressView progressBar1;

    private TextView txtDate, txtmonthlyPlan, txtRuppes, txtdescrption;

    public MyAccount() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_account, container, false);
        mContext = this.getActivity();
        shared = new Shared(mContext);
        reference(view);
        init();
        return view;
    }

    private void reference(View view) {
        earnList = new ArrayList<>();

        txtEarning = (TextView) view.findViewById(R.id.txtEarning);
        tctMySubscriptionPlan = (TextView) view.findViewById(R.id.tctMySubscriptionPlan);
        listShowEarningPLan = (RecyclerView) view.findViewById(R.id.listShowEarningPLan);
        rel_erning = (RelativeLayout) view.findViewById(R.id.rel_erning);
        rel_subscription_plan = (RelativeLayout) view.findViewById(R.id.rel_subscription_plan);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtmonthlyPlan = (TextView) view.findViewById(R.id.txtmonthlyPlan);
        txtRuppes = (TextView) view.findViewById(R.id.txtRuppes);
        txtdescrption = (TextView) view.findViewById(R.id.txtdescrption);
        rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) view.findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) view.findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));
    }

    private void init() {
        txtEarning.setBackgroundResource(R.drawable.bg_btn_null_app_border_myaccount);
        txtEarning.setTextColor(Color.parseColor("#FFFFFF"));

        txtEarning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityDashboard.txt_renew.setVisibility(View.GONE);
                rel_erning.setVisibility(View.VISIBLE);
                rel_subscription_plan.setVisibility(View.GONE);
                txtEarning.setBackgroundResource(R.drawable.bg_btn_sub_solid_myaccount_left);
                txtEarning.setTextColor(getResources().getColor(R.color.colorWhite));
                tctMySubscriptionPlan.setBackgroundResource(R.drawable.bg_btn_right_null_myaccount);
                tctMySubscriptionPlan.setTextColor(getResources().getColor(R.color.colorPrimary));

                if (earnList.size() > 0) {
                    adapter = new MyEarningAdapter(mContext, earnList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                    listShowEarningPLan.setLayoutManager(mLayoutManager);
                    listShowEarningPLan.setItemAnimator(new DefaultItemAnimator());
                    listShowEarningPLan.setAdapter(adapter);
                } else {
                    if (Constant_method.checkConn(mContext)) {
                        getMyEaring();
                    } else {
                        Toaster.getToast(mContext, ErrorMessage.NoInternet);
                    }
                }


            }
        });

        tctMySubscriptionPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //  ActivityDashboard.txt_renew.setVisibility(View.VISIBLE);
                rel_erning.setVisibility(View.GONE);
                rel_subscription_plan.setVisibility(View.VISIBLE);
                tctMySubscriptionPlan.setBackgroundResource(R.drawable.bg_btn_sub_solid_myaccount_right);
                tctMySubscriptionPlan.setTextColor(getResources().getColor(R.color.colorWhite));
                txtEarning.setBackgroundResource(R.drawable.bg_btn_left_null_myaccount);
                txtEarning.setTextColor(getResources().getColor(R.color.colorPrimary));

                if (Constant_method.checkConn(mContext)) {
                    getLawyerCurrentPlan();
                } else {
                    Toast.makeText(mContext, ErrorMessage.NoInternet, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if (Constant_method.checkConn(mContext)) {
            getMyEaring();
        } else {
            Toaster.getToast(mContext, ErrorMessage.NoInternet);
        }

    }

    private void getLawyerCurrentPlan() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rel_progress.setVisibility(View.GONE);
                String msg, status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");

                    if (status.contentEquals("1")) {
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i <= data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            int ah_users_id = obj.getInt("ah_users_id");
                            int ah_user_subscription_id = obj.getInt("ah_user_subscription_id");
                            boolean is_subscribe = obj.getBoolean("is_subscribe");
                            boolean is_verified = obj.getBoolean("is_verified");
                            int remainig_days = obj.getInt("remainig_days");
                            boolean is_expired = obj.getBoolean("is_expired");
                            if (is_expired) {
                                ActivityDashboard.txt_renew.setVisibility(View.VISIBLE);
                            } else {
                                ActivityDashboard.txt_renew.setVisibility(View.GONE);
                            }

                            boolean is_paid = obj.getBoolean("is_paid");
                            String startdate = obj.getString("startdate");
                            String enddate = obj.getString("enddate");
                            txtDate.setText(enddate);
                            String totalamount = obj.getString("totalamount");
                            int ah_subscription_type_id = obj.getInt("ah_subscription_type_id");
                            String subscription_type = obj.getString("subscription_type");
                            int ah_subscription_master_id = obj.getInt("ah_subscription_master_id");
                            String subscription_plan = obj.getString("subscription_plan");
                            txtmonthlyPlan.setText(subscription_plan);
                            int validity = obj.getInt("validity");
                            String price = obj.getString("price");
                            txtRuppes.setText(Config.ruppessSymbol + price);
                            String description = obj.getString("description");
                            txtdescrption.setText(description);

                        }


                    } else if (status.contentEquals("0")) {
//                        dialog.dismiss();
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(mContext, "" + msg);
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
                    main.put("action", Config.getLawyersCurrentPlan);
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


    private void getMyEaring() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rel_progress.setVisibility(View.GONE);
                String msg, status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        JSONArray data = object.getJSONArray("data");
                        earnList.clear();
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            ModelMyEarnData model = new ModelMyEarnData();
                            model.setCommunication_id(obj.getInt("communication_id"));
                            model.setCommunication_type_id(obj.getInt("communication_type_id"));
                            model.setCommunication_type(obj.getString("communication_type"));
                            model.setAh_users_id(obj.getInt("ah_users_id"));
                            model.setFull_name(obj.getString("full_name"));
                            model.setProfile_img(obj.getString("profile_img"));
                            model.setAmount(obj.getString("amount"));
                            model.setStart_time(obj.getString("start_time"));
                            model.setEnd_time(obj.getString("end_time"));
                            model.setUpdatedon(obj.getString("updatedon"));
                            earnList.add(model);
                        }

                        adapter = new MyEarningAdapter(mContext, earnList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
                        listShowEarningPLan.setLayoutManager(mLayoutManager);
                        listShowEarningPLan.setItemAnimator(new DefaultItemAnimator());
                        listShowEarningPLan.setAdapter(adapter);

                    } else if (status.contentEquals("0")) {
//                        dialog.dismiss();
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(mContext, "" + msg);
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
                    main.put("action", Config.myEarnings);
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

}
