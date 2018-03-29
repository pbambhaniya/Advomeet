package com.multipz.atmiyalawlab.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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
import com.multipz.atmiyalawlab.Adapter.CallListAdapter;
import com.multipz.atmiyalawlab.Model.BookMarkModel;
import com.multipz.atmiyalawlab.Model.CallListModel;
import com.multipz.atmiyalawlab.Model.MyAppointmentList;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityCallList extends AppCompatActivity implements ItemClickListener {
    RecyclerView rv_call_list;

    ProgressDialog dialog;
    Context context;
    Shared shared;
    String param;
    ArrayList<CallListModel> list;
    ImageView img_back;
    ArrayList<CallListModel> tempCallList = new ArrayList<>();
    CallListAdapter adapter;

    //    private RelativeLayout rel_root, rel_progress;
//    private CircularProgressView progressBar1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_list);
        context = this;
        shared = new Shared(context);
        list = new ArrayList<>();
        ref();
        Init();
    }

    private void ref() {

        rv_call_list = (RecyclerView) findViewById(R.id.rv_call_list);
        img_back = (ImageView) findViewById(R.id.img_back);
        getCallList();
    }

    private void Init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void getCallList() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        //    dialog.show();
//        rel_progress.setVisibility(View.VISIBLE);
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
                        list.clear();
                        JSONArray jsonArray = object.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
//                            rel_progress.setVisibility(View.GONE);
                            CallListModel model = new CallListModel();
                            JSONObject bookmark = jsonArray.getJSONObject(i);
                            model.setCommunication_id(bookmark.getString("communication_id"));
                            model.setCommunication_type_id(bookmark.getString("communication_type_id"));
                            model.setCommunication_type(bookmark.getString("communication_type"));
                            model.setComm_date(bookmark.getString("comm_date"));
                            model.setRequested_user_id(bookmark.getString("requested_user_id"));
                            model.setLawyer_id(bookmark.getString("lawyer_id"));
                            model.setLawyer_name(bookmark.getString("lawyer_name"));
                            model.setProfile_img(bookmark.getString("profile_img"));
                            model.setStart_time(bookmark.getString("start_time"));
                            model.setEnd_time(bookmark.getString("end_time"));
                            model.setAmount(bookmark.getString("amount"));
                            model.setOthercharges(bookmark.getString("othercharges"));
                            model.setCreated_date(bookmark.getString("created_date"));
                            model.setStatus(bookmark.getString("status"));
                            model.setPayment_id(bookmark.getString("payment_id"));
                            model.setPayment_status(bookmark.getString("payment_status"));
                            list.add(model);

                        }
                        getcallandvedioList(list);

                    } else if (status.contentEquals("0")) {
//                        rel_progress.setVisibility(View.GONE);
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
                    main.put("action", Config.activeCallSession);
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

    private void getcallandvedioList(ArrayList<CallListModel> tempList) {
        tempCallList.clear();
        for (CallListModel model : tempList) {
            if (!model.getCommunication_type_id().contentEquals("2")) {
                tempCallList.add(model);
            }
        }
        adapter = new CallListAdapter(context, tempCallList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        rv_call_list.setLayoutManager(mLayoutManager);
        rv_call_list.setItemAnimator(new DefaultItemAnimator());
        rv_call_list.setAdapter(adapter);
        adapter.setClickListener(ActivityCallList.this);

    }

    @Override
    public void itemClicked(View View, int position) {
        CallListModel model = list.get(position);
        Intent i = new Intent(ActivityCallList.this, ActivityCallProfile.class);
        i.putExtra("lawyer_id", model.getLawyer_id());
        i.putExtra("lawyer_name", model.getLawyer_name());
        i.putExtra("lawyer_profile", model.getProfile_img());
        i.putExtra("communication_type_id", model.getCommunication_type_id().toString());
        shared.setCommunication_type_id(Integer.parseInt(model.getCommunication_type_id()));
        startActivity(i);

    }
}


