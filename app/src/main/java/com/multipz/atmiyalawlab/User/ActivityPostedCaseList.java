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
import android.widget.Toast;

import com.multipz.atmiyalawlab.Activity.ActivityCasesList;
import com.multipz.atmiyalawlab.Adapter.PostCaseAdapter;
import com.multipz.atmiyalawlab.Model.CasesModel;
import com.multipz.atmiyalawlab.Model.PostCaseModel;
import com.multipz.atmiyalawlab.R;
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
import java.util.List;

public class ActivityPostedCaseList extends AppCompatActivity implements MyAsyncTask.AsyncInterface, ItemClickListener {

    private RelativeLayout rel_root;
    private RecyclerView listviewpostcase_list;
    private Context context;
    private Shared shared;
    private String param = "";
    private PostCaseAdapter adapter;
    List<CasesModel> list = new ArrayList<>();
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posted_case_list);
        context = this;
        shared = new Shared(context);

        reference();
        init();
    }

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        listviewpostcase_list = (RecyclerView) findViewById(R.id.listviewpostcase_list);
        img_back = (ImageView) findViewById(R.id.img_back);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        if (Constant_method.checkConn(context)) {
            getPostedCaselist();
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }
    }

    private void getPostedCaselist() {
        param = "{\"action\":\"" + Config.userPostedCaseList + "\",\"body\":{\"ah_users_id\":" + shared.getUserId() + "}}";
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, ActivityPostedCaseList.this, param, 1);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
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

                    for (int i = 0; i < aa.length(); i++) {
                        JSONObject objects = aa.getJSONObject(i);
                        CasesModel model = new CasesModel();
                        model.setCase_id(objects.getInt("case_id"));
                        model.setCity_id(objects.getInt("city_id"));
                        model.setCity_name(objects.getString("city_name"));
                        model.setTitle(objects.getString("title"));
                        model.setDescription(objects.getString("description"));
                        model.setCase_type_id(objects.getInt("case_type_id"));
                        model.setCase_type(objects.getString("case_type"));
                        model.setStatus_id(objects.getInt("status_id"));
                        model.setCase_status(objects.getString("case_status"));
//                        model.setCase_request_id(objects.getInt("case_request_id"));
                        model.setCased_by_user_id(objects.getInt("cased_by_user_id"));
                        model.setCased_by_name(objects.getString("cased_by_name"));
                        //  model.setProfile_img(objects.getString("profile_img"));
                        model.setCreated_on(objects.getString("created_on"));
                        list.add(model);
                    }
                    adapter = new PostCaseAdapter(list, context);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                    listviewpostcase_list.setLayoutManager(mLayoutManager);
                    listviewpostcase_list.setItemAnimator(new DefaultItemAnimator());
                    listviewpostcase_list.setAdapter(adapter);
                    listviewpostcase_list.setNestedScrollingEnabled(false);
                    adapter.setClickListener(this);

                } else if (status.contentEquals("0")) {
                    Toaster.getToast(getApplicationContext(), Message);
                    pd.dismiss();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void itemClicked(View View, int position) {
        CasesModel model = list.get(position);
        String fullname = model.getCased_by_name();
        String img = model.getProfile_img();
        String lawyer_type = model.getCase_type();

        int case_id = model.getCase_id();
        Intent intent = new Intent(ActivityPostedCaseList.this, ActivityCaseList.class);
        intent.putExtra("description", model.getDescription());
        intent.putExtra("createdate", model.getCreated_on());
        intent.putExtra("case_id", case_id);
        startActivity(intent);
    }
}
