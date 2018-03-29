package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Adapter.MyAppointmentLawyerAdapter;
import com.multipz.atmiyalawlab.Model.MyAppointmentList;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.User.ActivityBookAppointment;
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

public class ActivityMyAppointmentListlawyerSide extends AppCompatActivity implements ItemClickListener {


    private ImageView img_back;
    private RecyclerView listviewMyAppointmentlist;
    private ProgressDialog dialog;
    private String param = "";
    private ArrayList<MyAppointmentList> alist;
    private Context context;
    private Shared shared;
    ArrayList<MyAppointmentList> tempList = new ArrayList<>();
    private MyAppointmentLawyerAdapter adapter;
    private int reqStatus = 1;
    TextView txt_done, txt_confirm, txt_pending;
    private RelativeLayout rel_root, rel_progress;
    private View viewpending, viewRejected;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_appointment_listlawyer_side);
        context = this;
        shared = new Shared(context);

        alist = new ArrayList<>();
        reference();
        init();

    }

    void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        listviewMyAppointmentlist = (RecyclerView) findViewById(R.id.listviewMyAppointmentlist);
        txt_confirm = (TextView) findViewById(R.id.txt_confirm);
        txt_pending = (TextView) findViewById(R.id.txt_pending);
        txt_done = (TextView) findViewById(R.id.txt_done);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        viewpending = (View) findViewById(R.id.viewpending);
        viewRejected = (View) findViewById(R.id.viewRejected);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        viewRejected.setVisibility(View.VISIBLE);
        txt_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAppointmentList(alist, 1);

                txt_pending.setBackgroundResource(R.drawable.bg_btn_sub_solid_myaccount_left);
                txt_confirm.setBackgroundColor(Color.parseColor("#00000000"));
                txt_done.setBackgroundColor(Color.parseColor("#00000000"));
                txt_pending.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_done.setTextColor(getResources().getColor(R.color.colorPrimary));
                txt_confirm.setTextColor(getResources().getColor(R.color.colorPrimary));

            }

        });

        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAppointmentList(alist, 2);
                txt_confirm.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                txt_pending.setBackgroundColor(Color.parseColor("#00000000"));
                txt_done.setBackgroundColor(Color.parseColor("#00000000"));
                txt_confirm.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_done.setTextColor(getResources().getColor(R.color.colorPrimary));
                txt_pending.setTextColor(getResources().getColor(R.color.colorPrimary));
            }
        });


        txt_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAppointmentList(alist, 3);

                txt_done.setBackgroundResource(R.drawable.bg_btn_sub_solid_myaccount_right);
                txt_confirm.setBackgroundColor(Color.parseColor("#00000000"));
                txt_pending.setBackgroundColor(Color.parseColor("#00000000"));
                txt_done.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_pending.setTextColor(getResources().getColor(R.color.colorPrimary));
                txt_confirm.setTextColor(getResources().getColor(R.color.colorPrimary));
                viewpending.setVisibility(View.VISIBLE);

            }
        });

        if (Constant_method.checkConn(context)) {
            MyAppointmentList();
        } else {
            Toaster.getToast(getApplicationContext(), ErrorMessage.NoInternet);
        }

    }

    private void MyAppointmentList() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
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
                        rel_progress.setVisibility(View.GONE);

                        JSONArray jsonArray = object.getJSONArray("data");
                        alist.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            MyAppointmentList model = new MyAppointmentList();
                            model.setAppointment_id(obj.getInt("appointment_id"));
                            model.setAh_users_id(obj.getInt("ah_users_id"));
                            model.setFull_name(obj.getString("full_name"));
                            model.setProfile_img(obj.getString("profile_img"));
                            model.setSubject(obj.getString("subject"));
                            model.setDescription(obj.getString("description"));
                            model.setAppointment_date(obj.getString("appointment_date"));
                            model.setStart_time(obj.getString("start_time"));
                            model.setEnd_time(obj.getString("end_time"));
                            model.setStatus_id(obj.getInt("status_id"));
                            model.setStatus_name(obj.getString("status_name"));
                            model.setIs_payment(obj.getInt("is_payment"));
                            alist.add(model);
                        }
                        getAppointmentList(alist, reqStatus);
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
                Log.d("error", "" + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject object = new JSONObject();
                    object.put("action", Config.myAppointmentList);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("type", shared.getUsertype());
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


    private void getAppointmentList(ArrayList<MyAppointmentList> AppointList, int status) {
        tempList.clear();
        if (status == 1) {
            for (MyAppointmentList m : AppointList) {
                if (m.getStatus_id() == 1) {
                    tempList.add(m);
                    setAdapter();
                } else {
                    setAdapter();

                }
            }
            //     adapter.setClickListener(new ActivityCasesList.itemInClickListener());
        } else if (status == 2) {
            for (MyAppointmentList m : AppointList) {
                if (m.getStatus_id() == 2) {
                    tempList.add(m);
                    setAdapter();
                } else {
                    setAdapter();

                }
            }
        } else if (status == 3) {
            for (MyAppointmentList m : AppointList) {
                if (m.getStatus_id() == 3) {
                    tempList.add(m);
                    setAdapter();
                } else {
                    setAdapter();
                }
            }
        }


    }

    private void setAdapter() {

        if (tempList != null) {

            if (tempList.size() > 0) {
                adapter = new MyAppointmentLawyerAdapter(tempList, context);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                listviewMyAppointmentlist.setLayoutManager(mLayoutManager);
                listviewMyAppointmentlist.setItemAnimator(new DefaultItemAnimator());
                listviewMyAppointmentlist.setAdapter(adapter);
                adapter.setClickListener(this);
            } else {
                adapter = new MyAppointmentLawyerAdapter(tempList, context);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                listviewMyAppointmentlist.setLayoutManager(mLayoutManager);
                listviewMyAppointmentlist.setItemAnimator(new DefaultItemAnimator());
                listviewMyAppointmentlist.setAdapter(adapter);
            }
        }

    }

    @Override
    public void itemClicked(View view, int position) {
        MyAppointmentList model = tempList.get(position);
        int appointment_id = model.getAppointment_id();
        if (view.getId() == R.id.txtshowdetail) {
            Intent i = new Intent(ActivityMyAppointmentListlawyerSide.this, ActivityBookAppointment.class);
            i.putExtra("image", model.getProfile_img());
            i.putExtra("fullname", model.getFull_name());
            i.putExtra("subject", model.getSubject());
            i.putExtra("desription", model.getDescription());
            i.putExtra("date", model.getAppointment_date());
            i.putExtra("stime", model.getStart_time());
            i.putExtra("etime", model.getEnd_time());
            i.putExtra("appointment_id", model.getAppointment_id());
            startActivity(i);
        } else if (view.getId() == R.id.txtCancel) {
            popupReject(appointment_id);
        } else if (view.getId() == R.id.txtReject) {
            //  popRejected(appointment_id);
        } else if (view.getId() == R.id.txtApprove) {
            popApprove(appointment_id);
        }
    }


    private void CancelAppoinment(final int appointment_id, final int status) {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
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
                        Toaster.getToast(getApplicationContext(), "" + msg);
                        Intent intent = new Intent(ActivityMyAppointmentListlawyerSide.this, ActivityMyAppointmentListlawyerSide.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else if (status.contentEquals("0")) {
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
                    object.put("action", Config.updateAppointmentStatus);
                    JSONObject body = new JSONObject();
                    body.put("appointment_id", appointment_id);
                    body.put("ah_users_id", shared.getUserId());
                    body.put("status_id", status);
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


    void popupReject(final int appointment_id) {


        LayoutInflater inflater = ActivityMyAppointmentListlawyerSide.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_assign_case, null);
        TextView txt_des = (TextView) c.findViewById(R.id.head_desc);
        TextView txt_Ok = (TextView) c.findViewById(R.id.txt_Ok);
        TextView txt_Cancel = (TextView) c.findViewById(R.id.txt_Cancel);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityMyAppointmentListlawyerSide.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        txt_des.setText(Config.RejectApproveRequest);
        txt_Ok.setText("Yes");
        txt_Cancel.setText("Cancel");

        txt_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant_method.checkConn(context)) {
                    CancelAppoinment(appointment_id, 3);
                    dialog.dismiss();
                } else {
                    Toaster.getToast(context, ErrorMessage.NoInternet);
                }
            }
        });

        txt_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

    void popRejected(final int appointment_id) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ActivityMyAppointmentListlawyerSide.this);
        alertDialog.setMessage("Are you sure you want rejected?");
        // Setting Positive "Yes" Button
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                CancelAppoinment(appointment_id, 3);
                dialog.cancel();
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }


    void popApprove(final int appointment_id) {

        LayoutInflater inflater = ActivityMyAppointmentListlawyerSide.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_assign_case, null);
        TextView txt_des = (TextView) c.findViewById(R.id.head_desc);
        TextView txt_Ok = (TextView) c.findViewById(R.id.txt_Ok);
        TextView txt_Cancel = (TextView) c.findViewById(R.id.txt_Cancel);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityMyAppointmentListlawyerSide.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        txt_des.setText("Are you sure want Approve?");
        txt_Ok.setText("Accept");
        txt_Cancel.setText("Decline");

        txt_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant_method.checkConn(context)) {
                    CancelAppoinment(appointment_id, 2);
                    dialog.dismiss();
                } else {
                    Toaster.getToast(context, ErrorMessage.NoInternet);
                }
            }
        });

        txt_Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


    }

}
