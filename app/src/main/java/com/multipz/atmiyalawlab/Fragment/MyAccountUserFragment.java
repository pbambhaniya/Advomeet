package com.multipz.atmiyalawlab.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
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
public class MyAccountUserFragment extends Fragment {

    private RelativeLayout rel_root, rel_progress;
    private RecyclerView listShowEarningPLan;
    private Context mContext;
    private Shared shared;
    private CircularProgressView progressBar1;
    private MyEarningAdapter adapter;
    private String param = "";
    private ArrayList<ModelMyEarnData> earnList;

    public MyAccountUserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_my_account_user, container, false);

        mContext = this.getActivity();
        shared = new Shared(mContext);
        earnList = new ArrayList<>();
        rel_root = (RelativeLayout) v.findViewById(R.id.rel_root);
        listShowEarningPLan = (RecyclerView) v.findViewById(R.id.listShowEarningPLan);

        progressBar1 = (CircularProgressView) v.findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) v.findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault(rel_root);
        init();

        return v;
    }

    void init() {

        if (Constant_method.checkConn(mContext)) {
            getMyEaring();
        } else {
            Toaster.getToast(mContext, ErrorMessage.NoInternet);
        }
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
                            model.setEnd_time(obj.getString("comm_date"));
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
