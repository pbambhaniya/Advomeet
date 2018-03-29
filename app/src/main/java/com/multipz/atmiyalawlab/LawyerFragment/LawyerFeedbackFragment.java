package com.multipz.atmiyalawlab.LawyerFragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Adapter.FeedBackAdapter;
import com.multipz.atmiyalawlab.Model.FeedBackModel;
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
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class LawyerFeedbackFragment extends Fragment {

    RecyclerView listview_getfeedback;
    FeedBackAdapter adapter;
    Context context;
    private List<FeedBackModel> list = new ArrayList<>();
    //    ImageView img_back;
    String param;
    Shared shared;
    private RelativeLayout rel_root, rel_progress;
    ProgressDialog dialog;
    private CircularProgressView progressBar1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lawyer_feedback, container, false);

        context = getActivity();
        shared = new Shared(context);

        reference(view);
        init();
        return view;
    }


    private void reference(View view) {
        listview_getfeedback = (RecyclerView) view.findViewById(R.id.listview_getfeedback);
        progressBar1 = (CircularProgressView) view.findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) view.findViewById(R.id.rel_progress);
        rel_root = (RelativeLayout) view.findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) view.findViewById(R.id.rel_root));
        progressBar1.startAnimation();

    }

    private void init() {
//        img_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onBackPressed();
//            }
//        });
        if (Constant_method.checkConn(context)) {
            getFeedback();
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }
    }

    class itemInClickListener implements FeedBackAdapter.ClickListener {
        @Override
        public void onItemClick(View view, int position) {
            FeedBackModel model = list.get(position);

        }
    }

    private void getFeedback() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;


        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    String msg = object.getString("msg");
                    if (status.contentEquals("1")) {

                        JSONArray array = object.getJSONArray("data");

                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            FeedBackModel model = new FeedBackModel();
                            model.setFeedback_id(obj.getInt("feedback_id"));
                            model.setAh_user_id(obj.getInt("ah_user_id"));
                            model.setFull_name(obj.getString("full_name"));
                            model.setProfile_img(obj.getString("profile_img"));
                            model.setRetting(obj.getString("retting"));
                            model.setMessage(obj.getString("message"));
                            model.setFeedback_on(obj.getString("feedback_on"));
                            list.add(model);
                        }
                        adapter = new FeedBackAdapter(context, list);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        listview_getfeedback.setLayoutManager(mLayoutManager);
                        listview_getfeedback.setAdapter(adapter);
                    } else if (status.contentEquals("0")) {

                        Toaster.getToast(context, msg);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.feedbackList);
                    JSONObject body = new JSONObject();
                    body.put("ah_lawyer_id", 18);
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
