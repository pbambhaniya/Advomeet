package com.multipz.atmiyalawlab.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Activity.ActivityDashboard;
import com.multipz.atmiyalawlab.Activity.ActivityLawyerProfile;
import com.multipz.atmiyalawlab.Adapter.SpinnerAdapter;
import com.multipz.atmiyalawlab.Model.SpinnerModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityPostYourCase extends AppCompatActivity {

    ImageView img_back, img_setting;
    RelativeLayout img_notification, rel_root, rel_progress;
    private Spinner spn_category, sp_city, sp_speclalist, sp_state;
    private EditText edt_description, edt_title;
    private TextView btnSave;
    private ArrayList<SpinnerModel> object_city, object_specilization, object_state;
    private Shared shared;
    private Context context;
    private String param = "";
    ProgressDialog dialog;
    String title, des, city_id, ah_lawyer_type_id, state_id;
    private CircularProgressView progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_your_case);
        context = this;
        shared = new Shared(context);

        object_city = new ArrayList<>();
        object_specilization = new ArrayList<>();

        reference();
        init();

    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        sp_speclalist = (Spinner) findViewById(R.id.sp_speclalist);
        sp_city = (Spinner) findViewById(R.id.sp_city);
        sp_state = (Spinner) findViewById(R.id.sp_state);
        edt_description = (EditText) findViewById(R.id.edt_description);
        btnSave = (TextView) findViewById(R.id.btnSave);
        edt_title = (EditText) findViewById(R.id.edt_title);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();


        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI((RelativeLayout) findViewById(R.id.rel_root), ActivityPostYourCase.this);

        getCity();
        getstate();
        getSpecilization();
    }

    private void getstate() {
        object_state = new ArrayList<SpinnerModel>();
        object_state.add(new SpinnerModel("Select State", "Select State"));

        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.State, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject state = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(state.getString("ah_state_id"));
                spinnerModel.setName(state.getString("state_name"));
                object_state.add(spinnerModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sp_state.setAdapter(new SpinnerAdapter(this, object_state));

    }

    private void getCity() {
        object_city = new ArrayList<SpinnerModel>();
        object_city.add(new SpinnerModel("Select City", "Select City"));

        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.City, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject state = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(state.getString("ah_city_id"));
                spinnerModel.setName(state.getString("city_name"));
                object_city.add(spinnerModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sp_city.setAdapter(new SpinnerAdapter(this, object_city));

    }

    private void init() {

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                city_id = object_city.get(sp_city.getSelectedItemPosition()).getid();
                ah_lawyer_type_id = object_specilization.get(sp_speclalist.getSelectedItemPosition()).getid();
                state_id = object_state.get(sp_state.getSelectedItemPosition()).getid();
                title = edt_title.getText().toString();
                des = edt_description.getText().toString();

                if (ah_lawyer_type_id.contentEquals("Specialization")) {
                    Toaster.getToast(context, ErrorMessage.specialtion);
                } else if (state_id.contentEquals("Select State")) {
                    Toaster.getToast(context, ErrorMessage.enterstate);
                } else if (city_id.contentEquals("Select City")) {
                    Toaster.getToast(context, ErrorMessage.entercity);
                } else if (title.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.title);
                } else if (des.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.enterDescription);
                } else {
                    if (Constant_method.checkConn(context)) {
                        getCasePost();
                    } else {
                        Toaster.getToast(context, ErrorMessage.NoInternet);
                    }
                }
            }
        });
    }

    private void getCasePost() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
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
                        popUp(msg);
//                        Intent intent=new Intent(ActivityPostYourCase.this, UserDashboard.class);
//                        startActivity(intent);
                    } else {
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
                rel_progress.setVisibility(View.GONE);

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.casePost);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("ah_lawyer_type_id", ah_lawyer_type_id);
                    body.put("city_id", city_id);
                    body.put("title", title);
                    body.put("description", des);
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

    private void getSpecilization() {
        object_specilization = new ArrayList<SpinnerModel>();
        object_specilization.add(new SpinnerModel("Specialization", "Specialization"));

        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.specialization, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject state = jsonArray.getJSONObject(i);
                SpinnerModel spinnerModel = new SpinnerModel();
                spinnerModel.setid(state.getString("ah_lawyer_type_id"));
                spinnerModel.setName(state.getString("lawyer_type"));
                object_specilization.add(spinnerModel);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sp_speclalist.setAdapter(new SpinnerAdapter(this, object_specilization));

    }

    public void popUp(String msg) {
        LayoutInflater inflater = ActivityPostYourCase.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_submit, null);
        TextView txt_des = (TextView) c.findViewById(R.id.txt_des);
        LinearLayout lnr_ok = (LinearLayout) c.findViewById(R.id.lnr_ok);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityPostYourCase.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txt_des.setText(msg);
        lnr_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityPostYourCase.this, ActivityDashboardUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }
}
