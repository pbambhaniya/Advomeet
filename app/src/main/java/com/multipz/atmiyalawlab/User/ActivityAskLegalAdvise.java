package com.multipz.atmiyalawlab.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Activity.ActivityDashboard;
import com.multipz.atmiyalawlab.Activity.ActivityDrafiting;
import com.multipz.atmiyalawlab.Activity.ActivityLawyerProfile;
import com.multipz.atmiyalawlab.Adapter.DraftingTypeAdapter;
import com.multipz.atmiyalawlab.Model.ModelDraftingType;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.MyAsyncTask;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityAskLegalAdvise extends AppCompatActivity {

    private ImageView img_back;
    private EditText edt_title_about_case, edt_desc;
    private RelativeLayout rel_root, btn_ask_question;
    private Context context;
    private Shared shared;
    private String title = "", desc = "";
    private ProgressDialog dialog;
    private CircularProgressView progressBar1;
    RelativeLayout rel_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_legal_advise);
        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        edt_title_about_case = (EditText) findViewById(R.id.edt_title_about_case);
        edt_desc = (EditText) findViewById(R.id.edt_desc);
        btn_ask_question = (RelativeLayout) findViewById(R.id.btn_ask_question);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI((RelativeLayout) findViewById(R.id.rel_root), ActivityAskLegalAdvise.this);


    }

    void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_ask_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_title_about_case.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.title);
                } else if (edt_desc.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterDescription);
                } else {
                    title = edt_title_about_case.getText().toString();
                    desc = edt_desc.getText().toString();
                    if (Constant_method.checkConn(context)) {
                        AskLegelQuestion();
                    } else {
                        Toaster.getToast(context, ErrorMessage.NoInternet);
                    }

                }
            }
        });
    }

    private void AskLegelQuestion() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(context);
        dialog.setMessage("Loading...");
        //   dialog.show();
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
//                        Toaster.getToast(getApplicationContext(), "" + msg);
                        edt_title_about_case.setText("");
                        edt_desc.setText("");
                        popUpAlertLogout(msg);
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
                rel_progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.askLegalAdvise);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("subject", title);
                    body.put("description", desc);
                    main.put("body", body);
                    params.put("json", main.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void popUpAlertLogout(String msg) {
        LayoutInflater inflater = ActivityAskLegalAdvise.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_submit, null);
        TextView txt_des = (TextView) c.findViewById(R.id.txt_des);
        LinearLayout lnr_ok = (LinearLayout) c.findViewById(R.id.lnr_ok);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityAskLegalAdvise.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txt_des.setText(msg);
        lnr_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(ActivityAskLegalAdvise.this, ActivityDashboardUser.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            }
        });

    }
}
