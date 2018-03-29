package com.multipz.atmiyalawlab.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.MyAsyncTask;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityAssignCase extends AppCompatActivity implements MyAsyncTask.AsyncInterface {
    private ImageView img_back;
    private CircleImageView imglawyerAccept;
    private TextView txtAcceptRequest, txtdecline, txtFullname, txtDescription;
    private String ah_users_id;
    private int idstatus = 0, case_request_id = 0, status_id = 0;
    private String param = "", fullname = "", desc = "", img = "", location = "";
    private Context context;
    private Shared shared;
    private RelativeLayout rel_root, rel_progress;
    private LinearLayout lnr_btn_accept_cancel;
    TextView txt_location;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_case);
        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        imglawyerAccept = (CircleImageView) findViewById(R.id.imglawyerAccept);
        txtAcceptRequest = (TextView) findViewById(R.id.txtAcceptRequest);
        txtdecline = (TextView) findViewById(R.id.txtdecline);
        txtFullname = (TextView) findViewById(R.id.txtFullname);
        txtDescription = (TextView) findViewById(R.id.txtDescription);
        txt_location = (TextView) findViewById(R.id.txt_location);
        lnr_btn_accept_cancel = (LinearLayout) findViewById(R.id.lnr_btn_accept_cancel);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
    }

    private void init() {

        fullname = getIntent().getStringExtra("fullname");
        case_request_id = getIntent().getIntExtra("case_request_id", 0);
        status_id = getIntent().getIntExtra("status_id", 0);
        desc = getIntent().getStringExtra("desc");
        img = getIntent().getStringExtra("img");
        location = getIntent().getStringExtra("location");

        if (status_id == 1) {
            lnr_btn_accept_cancel.setVisibility(View.VISIBLE);
        } else if (status_id == 2) {
            lnr_btn_accept_cancel.setVisibility(View.GONE);
        }


        Picasso.with(context).load(img).into(imglawyerAccept);
        txtFullname.setText(fullname);
        txtDescription.setText(desc);
        txt_location.setText(location);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        txtAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpAlertAccept(2);

            }
        });

        txtdecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpAlertDecline(3);

            }
        });
    }

    private void LawyerAcceptPostCase(int idstatus) {
        try {
            JSONObject main = new JSONObject();
            main.put("action", Config.updateCaseRequest);
            JSONObject user = new JSONObject();
            user.put("ah_users_id", shared.getUserId());
            user.put("case_request_id", case_request_id);//shared.getUserId()
            user.put("status_id", idstatus);
            main.put("body", user);
            param = main.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (Constant_method.checkConn(getApplicationContext())) {
            MyAsyncTask myAsyncTask = new MyAsyncTask(Config.BASE_URL, ActivityAssignCase.this, param, 2);
            myAsyncTask.execute();
        } else {
            Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
        }
    }

    public void popUpAlertAccept(final int status_id) {

        LayoutInflater inflater = ActivityAssignCase.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_assign_case, null);
        TextView txt_des = (TextView) c.findViewById(R.id.head_desc);

        TextView txt_Ok = (TextView) c.findViewById(R.id.txt_Ok);
        TextView txt_Cancel = (TextView) c.findViewById(R.id.txt_Cancel);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityAssignCase.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        txt_des.setText(R.string.case_accept_popup);

        txt_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant_method.checkConn(context)) {
                    LawyerAcceptPostCase(status_id);
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

    public void popUpAlertDecline(final int status_id) {

        LayoutInflater inflater = ActivityAssignCase.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_assign_case, null);
        TextView txt_des = (TextView) c.findViewById(R.id.head_desc);
        TextView txt_Ok = (TextView) c.findViewById(R.id.txt_Ok);
        TextView txt_Cancel = (TextView) c.findViewById(R.id.txt_Cancel);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityAssignCase.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        txt_des.setText(R.string.case_decline_popup);

        txt_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant_method.checkConn(context)) {
                    LawyerAcceptPostCase(status_id);
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

    @Override
    public void onResponseService(String response, int flag, ProgressDialog pd) {
        String Message = "", status;
        if (flag == 2) {
            JSONObject object = null;
            try {
                object = new JSONObject(response);
                status = object.getString("status");
                JSONObject o = object.getJSONObject("body");
                Message = o.getString("msg");
                if (status.contentEquals("1")) {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), "" + Message);
                    Intent i = new Intent(ActivityAssignCase.this, ActivityCasesList.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                } else {
                    pd.dismiss();
                    Toaster.getToast(getApplicationContext(), "" + Message);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
