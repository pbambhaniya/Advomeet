package com.multipz.atmiyalawlab.Activity;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.multipz.atmiyalawlab.Adapter.BookMarkAdapter;
import com.multipz.atmiyalawlab.Adapter.CourtLawyerDetailAdapter;
import com.multipz.atmiyalawlab.Adapter.ReviewsLawyerDetailAdapter;
import com.multipz.atmiyalawlab.Adapter.UserAvalibilityAdapter;
import com.multipz.atmiyalawlab.Model.CourtModel;
import com.multipz.atmiyalawlab.Model.LawyerListModel;
import com.multipz.atmiyalawlab.Model.ModelReviewsLawyerDetail;
import com.multipz.atmiyalawlab.Model.SelectAvailabilityModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.User.ActivityAcceptCaseLaywerList;
import com.multipz.atmiyalawlab.User.ActivityAskLegalAdvise;
import com.multipz.atmiyalawlab.User.ActivityBookmarkList;
import com.multipz.atmiyalawlab.User.ActivityDashboardUser;
import com.multipz.atmiyalawlab.User.ActivityLawyerDetail;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ActivityLawyerBookAppointment extends AppCompatActivity {

    private ImageView img_back;
    private EditText edt_select_date, edtStartTime, edtEndTime, edtaboutagenda, edt_describe_description;
    private RelativeLayout rel_root, rel_book_now, rel_progress;
    private int mYear, mMonth, mDay;
    Context context;
    private Shared shared;
    private String param = "", daysID = "", appdate = "", subject = "", description = "", startTime = "", endTime = "", format = "", date, price = "";
    private ProgressDialog dialog;
    private String lawyer_id;
    private TextView txtPrice;
    private int appointment_id = 0;
    private boolean reschedule;
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_book_appointment);
        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        edt_select_date = (EditText) findViewById(R.id.edt_select_date);
        edtStartTime = (EditText) findViewById(R.id.edtStartTime);
        edtEndTime = (EditText) findViewById(R.id.edtEndTime);
        edtaboutagenda = (EditText) findViewById(R.id.edtaboutagenda);
        edt_describe_description = (EditText) findViewById(R.id.edt_describe_description);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        rel_book_now = (RelativeLayout) findViewById(R.id.rel_book_now);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI((RelativeLayout) findViewById(R.id.rel_root), ActivityLawyerBookAppointment.this);


    }

    void init() {

        lawyer_id = getIntent().getStringExtra("lawyer_id");
        appointment_id = getIntent().getIntExtra("appointment_id", 0);
        reschedule = getIntent().getBooleanExtra("reschedule", false);
        subject = getIntent().getStringExtra("subject");
        description = getIntent().getStringExtra("description");
        date = getIntent().getStringExtra("date");
        startTime = getIntent().getStringExtra("stime");
        endTime = getIntent().getStringExtra("etime");
        price = getIntent().getStringExtra("price");
        txtPrice.setText(Config.ruppessSymbol + "" + price);
        if (reschedule) {
            edt_select_date.setText(date);
            edtStartTime.setText(startTime);
            edtEndTime.setText(endTime);
            edtaboutagenda.setText(subject);
            edt_describe_description.setText(description);
            txtPrice.setText(price);
        }


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        edt_select_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_select_date.getText().toString().contentEquals("")) {
                    final Calendar c = Calendar.getInstance();
                    mYear = c.get(Calendar.YEAR);
                    mMonth = c.get(Calendar.MONTH);
                    mDay = c.get(Calendar.DAY_OF_MONTH);
                } else {
                    String[] s = edt_select_date.getText().toString().split("-");
                    mYear = Integer.parseInt(s[0]);
                    mMonth = Integer.parseInt(s[1]) - 1;
                    mDay = Integer.parseInt(s[2]);
                }

                DatePickerDialog datePickerDialog = new DatePickerDialog(ActivityLawyerBookAppointment.this,
                        R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String day = "";
                        int month = monthOfYear + 1;
                        if (dayOfMonth > 0 && dayOfMonth <= 9) {
                            day = "0" + dayOfMonth;
                        } else {
                            day = "" + dayOfMonth;
                        }
                        if (month > 0 && month <= 9) {
                            edt_select_date.setText(year + "-0" + (monthOfYear + 1) + "-" + day);
                        } else {
                            edt_select_date.setText(year + "-" + (monthOfYear + 1) + "-" + day);
                        }

                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        edtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivityLawyerBookAppointment.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime = hour + ":" + minute /*+ ":" + "00"*/;
                        // startTime = edtStartTime.getText().toString();
                        getTimeFormate(selectedHour, selectedMinute, view);

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        edtEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivityLawyerBookAppointment.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int selectedMinute) {
                        endTime = hourOfDay + ":" + selectedMinute /*+ ":" + "00"*/;
                        //  endTime = edtEndTime.getText().toString();
                        getTimeFormate(hourOfDay, selectedMinute, view);

                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        rel_book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_select_date.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.selectDay);
                } else if (edtStartTime.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.startTime);
                } else if (edtEndTime.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.endTime);
                } else if (edtaboutagenda.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.entersubject);
                } else if (edt_describe_description.getText().toString().contentEquals("")) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterDescription);
                } else {
                    appdate = edt_select_date.getText().toString();
                    description = edt_describe_description.getText().toString();
                    subject = edtaboutagenda.getText().toString();
                    if (Constant_method.checkConn(context)) {
                        BookAppointment();
                    } else {
                        Toaster.getToast(getApplicationContext(), ErrorMessage.NoInternet);
                    }
                }


            }
        });
    }

    private void getTimeFormate(int hourOfDay, int selectedMinute, View view) {
        if (hourOfDay == 0) {
            hourOfDay += 12;
            format = "AM";
        } else if (hourOfDay == 12) {
            format = "PM";
        } else if (hourOfDay > 12) {
            hourOfDay -= 12;
            format = "PM";
        } else {
            format = "AM";
        }
        if (view.getId() == R.id.edtStartTime) {
            edtStartTime.setText(pad(hourOfDay) + ":" + pad(selectedMinute) + " " + format);
        } else if (view.getId() == R.id.edtEndTime) {
            edtEndTime.setText(pad(hourOfDay) + ":" + pad(selectedMinute) + " " + format);
        }

    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    private void BookAppointment() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        // dialog.show();
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
                        edt_select_date.setText("");
                        edtStartTime.setText("");
                        edtEndTime.setText("");
                        edtaboutagenda.setText("");
                        edt_describe_description.setText("");
                        popUpAlertLogout();
                    } else {
                        dialog.dismiss();
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
                Log.d("errr", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.bookAppointment);
                    JSONObject body = new JSONObject();
                    if (!reschedule) {
                        body.put("appointment_id", "");
                        body.put("ah_lawyer_id", lawyer_id);
                    } else {
                        body.put("appointment_id", appointment_id);
                        body.put("ah_lawyer_id", lawyer_id);
                    }
                    body.put("ah_users_id", shared.getUserId());
                    body.put("subject", subject);
                    body.put("description", description);
                    body.put("start_time", startTime);
                    body.put("appointment_date", appdate);
                    body.put("end_time", endTime);
                    body.put("price", price);
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

    public void popUpAlertLogout() {
        LayoutInflater inflater = ActivityLawyerBookAppointment.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_submit, null);
        TextView txt_des = (TextView) c.findViewById(R.id.txt_des);
        LinearLayout lnr_ok = (LinearLayout) c.findViewById(R.id.lnr_ok);
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityLawyerBookAppointment.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txt_des.setText(getResources().getString(R.string.addappoinment));
        lnr_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

    }
}
