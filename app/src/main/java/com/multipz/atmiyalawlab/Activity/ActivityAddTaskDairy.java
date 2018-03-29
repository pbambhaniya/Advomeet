package com.multipz.atmiyalawlab.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ActivityAddTaskDairy extends AppCompatActivity {
    private ImageView img_back;
    private EditText txt_start_date, txt_time;
    private EditText edt_name, edt_des;
    private Button btnAddTssk;
    private int hour, minutes;
    private int mYear, mMonth, mDay;
    String name = "", des = "", date = "", time = "", ah_advocate_dairy_id = "", param;
    Context context;
    Shared shared;
    boolean upadateDaity;
    private ProgressDialog dialog;
    private RelativeLayout rel_root, rel_progress;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_dairy);
        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    private void reference() {
        context = this;
        shared = new Shared(context);
        img_back = (ImageView) findViewById(R.id.img_back);
        txt_start_date = (EditText) findViewById(R.id.txt_start_date);
        txt_time = (EditText) findViewById(R.id.txt_time);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_des = (EditText) findViewById(R.id.edt_des);
        btnAddTssk = (Button) findViewById(R.id.btnAddTssk);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();

        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI(rel_root, ActivityAddTaskDairy.this);

        Intent i = getIntent();
        name = i.getStringExtra("name");
        des = i.getStringExtra("des");
        date = i.getStringExtra("date");
        time = i.getStringExtra("time");
        ah_advocate_dairy_id = i.getStringExtra("ah_advocate_dairy_id");
        upadateDaity = i.getBooleanExtra("upadateDaity", false);

        if (upadateDaity) {
            edt_name.setText(name);
            edt_des.setText(des);
            String dated = Constant_method.cu_datemonth(date);
            txt_start_date.setText(dated);
            String timed = Constant_method.current_time(time);
            txt_time.setText(timed);
            btnAddTssk.setText("Save task");
        }


    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnAddTssk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = edt_name.getText().toString().trim();
                des = edt_des.getText().toString().trim();
                date = txt_start_date.getText().toString();
                time = txt_time.getText().toString();

                if (name.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterName);
                } else if (des.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterDescription);
                } else if (date.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterDate);
                } else if (time.contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterTime);
                } else if (Constant_method.checkConn(context)) {
                    getAddDiary();
                } else {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.NoInternet);
                }
            }
        });


        txt_start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txt_start_date.getText().toString().contentEquals("")) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    String[] s = date.split("-");
                    mYear = Integer.parseInt(s[0]);
                    mMonth = Integer.parseInt(s[1]) - 1;
                    mDay = Integer.parseInt(s[2]);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityAddTaskDairy.this,
                        R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        int month = monthOfYear + 1;
                        if (month > 0 && month <= 9) {
                            date = year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth;
                            String dated = Constant_method.cu_datemonth(date);
                            txt_start_date.setText(dated);
                        } else {
                            date = year + "-" + (monthOfYear + 1) + "-" + dayOfMonth;
                            String dated = Constant_method.cu_datemonth(date);
                            txt_start_date.setText(dated);
                        }

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        txt_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (txt_time.getText().toString().contentEquals("")) {
                    final Calendar c = Calendar.getInstance();
                    hour = c.get(Calendar.HOUR_OF_DAY);
                    minutes = c.get(Calendar.MINUTE);
                } else {
                    String ti[] = time.split(":");
                    hour = Integer.parseInt(ti[0]);
                    minutes = Integer.parseInt(ti[1]);
                }
                // Launch Time Picker Dialog
                final TimePickerDialog timePickerDialog = new TimePickerDialog(ActivityAddTaskDairy.this,
                        R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        time = hourOfDay + ":" + minute + ":00";
                        String timed = Constant_method.current_time(time);
                        txt_time.setText(timed);
                    }
                }, hour, minutes, false);
                timePickerDialog.show();
            }
        });
    }

    private void getAddDiary() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;

        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                rel_progress.setVisibility(View.GONE);
                String status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    String msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        rel_progress.setVisibility(View.GONE);
//                        Toaster.getToast(getApplicationContext(), msg);
                        popUpAlertAdddiary(msg);

                    } else if (status.contentEquals("0")) {
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(getApplicationContext(), msg);
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
                    main.put("action", Config.addDairyNote);
                    JSONObject body = new JSONObject();

                    if (upadateDaity) {
                        body.put("ah_advocate_dairy_id", ah_advocate_dairy_id);
                    } else {
                        body.put("ah_advocate_dairy_id", "");
                    }
                    body.put("ah_users_id", shared.getUserId());
                    body.put("name", name);
                    body.put("description", des);
                    body.put("date", date);
                    body.put("time", time);
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

    public void popUpAlertAdddiary(String msg) {
        LayoutInflater inflater = ActivityAddTaskDairy.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_submit, null);
        TextView txt_des = (TextView) c.findViewById(R.id.txt_des);
        LinearLayout lnr_ok = (LinearLayout) c.findViewById(R.id.lnr_ok);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityAddTaskDairy.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txt_des.setText(msg);
        lnr_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityAddTaskDairy.this, ActivityAdvocateDiaryDetail.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        });

    }
}
