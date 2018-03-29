package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.atmiyalawlab.Adapter.CasesAdapter;
import com.multipz.atmiyalawlab.Model.CasesModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.MyAsyncTask;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ActivityCasesList extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    TextView txt_assigned, txt_accepted, txt_pending;
    ImageView img_back;
    RecyclerView listviewCases;
    private String param = "";
    private ArrayList<CasesModel> caseList;
    private CasesAdapter adapter;
    private Context context;
    private int reqStatus = 1;
    ArrayList<CasesModel> tempList = new ArrayList<>();
    private RelativeLayout rel_root;
    private Shared shared;
    private View viewPending, viewAccepted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cases_list);

        context = this;
        shared = new Shared(context);
        caseList = new ArrayList<>();

        reference();
        init();
    }

    private void init() {
        txt_pending.setTextColor(getResources().getColor(R.color.delete));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        getLawyerCases();


        txt_pending.setBackgroundResource(R.drawable.bg_btn_null_app_border_myaccount);
        txt_pending.setTextColor(Color.parseColor("#FFFFFF"));
        txt_pending.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                reqStatus = 1;
                getCaseList(caseList, reqStatus);
              /*  txt_pending.setTextColor(getResources().getColor(R.color.delete));
                txt_accepted.setTextColor(getResources().getColor(R.color.colorWhite));
                //  txt_assigned.setTextColor(getResources().getColor(R.color.colorWhite));*/

                txt_pending.setBackgroundResource(R.drawable.bg_btn_sub_solid_myaccount_left);
                txt_pending.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_accepted.setBackgroundResource(R.drawable.bg_btn_right_null_myaccount);
                txt_accepted.setTextColor(getResources().getColor(R.color.colorPrimary));


            }
        });
        txt_accepted.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                reqStatus = 2;
                getCaseList(caseList, reqStatus);
               /* txt_accepted.setTextColor(getResources().getColor(R.color.colorGreen));
                txt_pending.setTextColor(getResources().getColor(R.color.colorWhite));*/

                txt_accepted.setBackgroundResource(R.drawable.bg_btn_sub_solid_myaccount_right);
                txt_accepted.setTextColor(getResources().getColor(R.color.colorWhite));
                txt_pending.setBackgroundResource(R.drawable.bg_btn_left_null_myaccount);
                txt_pending.setTextColor(getResources().getColor(R.color.colorPrimary));


                //   txt_assigned.setTextColor(getResources().getColor(R.color.colorWhite));
            }
        });


    }

    private void reference() {

        listviewCases = (RecyclerView) findViewById(R.id.listviewCases);
        txt_accepted = (TextView) findViewById(R.id.txt_accepted);
        txt_pending = (TextView) findViewById(R.id.txt_pending);
        img_back = (ImageView) findViewById(R.id.img_back);
        // viewPending = (View) findViewById(R.id.viewPending);
        //  viewAccepted = (View) findViewById(R.id.viewAccepted);

        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));


    }

    private void getLawyerCases() {
        param = "{\"action\":\"" + Config.lawyerCaseList + "\",\"body\":{\"ah_users_id\":" + shared.getUserId() + "}}";
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, ActivityCasesList.this, param, 1);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), ErrorMessage.NoInternet, Toast.LENGTH_SHORT).show();
        }
    }

    class itemInClickListener implements CasesAdapter.ClickListener {

        @Override
        public void onItemClick(View view, int position) {
            CasesModel model = tempList.get(position);
            Intent intent = new Intent(ActivityCasesList.this, ActivityAssignCase.class);
            intent.putExtra("fullname", model.getCased_by_name());
            intent.putExtra("case_request_id", model.getCase_request_id());
            intent.putExtra("status_id", model.getStatus_id());
            intent.putExtra("desc", model.getDescription());
            intent.putExtra("img", model.getProfile_img());
            intent.putExtra("location", model.getCity_name());
            startActivity(intent);
        }
    }

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        JSONObject object = null;
        if (flag == 1) {
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    JSONArray aa = o.getJSONArray("data");
                    caseList.clear();
                    for (int i = 0; i < aa.length(); i++) {
                        JSONObject objects = aa.getJSONObject(i);
                        CasesModel model = new CasesModel();
                        model.setTitle(objects.getString("title"));
                        model.setCase_request_id(objects.getInt("case_request_id"));
                        model.setCase_id(objects.getInt("case_id"));
                        model.setCity_id(objects.getInt("city_id"));
                        model.setCity_name(objects.getString("city_name"));
                        model.setDescription(objects.getString("description"));
                        model.setCase_type_id(objects.getInt("case_type_id"));
                        model.setCase_type(objects.getString("case_type"));
                        model.setStatus_id(objects.getInt("status_id"));
                        model.setCase_status(objects.getString("case_status"));
                        model.setCased_by_user_id(objects.getInt("cased_by_user_id"));
                        model.setCased_by_name(objects.getString("cased_by_name"));
                        model.setProfile_img(objects.getString("profile_img"));
                        model.setCreated_on(objects.getString("created_on"));
                        caseList.add(model);
                    }
                    getCaseList(caseList, reqStatus);
                } else if (status.contentEquals("0")) {
                    Toaster.getToast(getApplicationContext(), Message);
                    pd.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void getCaseList(ArrayList<CasesModel> caseList, int status) {
        tempList.clear();
        if (status == 1) {
            for (CasesModel m : caseList) {
                if (m.getStatus_id() == 1) {
                    tempList.add(m);
                    setAdapter();
                } else {
                    setAdapter();
                }
            }
            // adapter.setClickListener(new itemInClickListener());
        } else if (status == 2) {
            for (CasesModel m : caseList) {
                if (m.getStatus_id() == 2) {
                    tempList.add(m);
                    setAdapter();
                } else {
                    setAdapter();
                }
            }
        }/* else if (status == 4) {
            for (CasesModel m : caseList) {
                if (m.getIdstatus() == 4) {
                    tempList.add(m);
                    setAdapter();
                } else {
                    setAdapter();
                }
            }
        }*/
        if (adapter != null) {
            adapter.setClickListener(new itemInClickListener());
        }

    }

    private void setAdapter() {
        adapter = new CasesAdapter(context, tempList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        listviewCases.setLayoutManager(mLayoutManager);
        listviewCases.setItemAnimator(new DefaultItemAnimator());
        listviewCases.setAdapter(adapter);
    }

}
