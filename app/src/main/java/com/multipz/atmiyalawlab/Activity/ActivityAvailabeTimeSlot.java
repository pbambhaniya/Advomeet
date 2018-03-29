package com.multipz.atmiyalawlab.Activity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.atmiyalawlab.Adapter.UserAvalibilityAdapter;
import com.multipz.atmiyalawlab.Model.SelectAvailabilityModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.User.ActivityMakePayment;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ActivityAvailabeTimeSlot extends AppCompatActivity {

    private EditText edtStartTime, edtEndTime;
    private String format = "", startTime = "", endTime = "", fname = "", profile_img = "", Timehours = "";
    private Button btn_make_payment;
    private int communication_type_id;
    String price, pricechat, pricechatvideo;
    private RecyclerView recyler_setTimeslot;
    private Shared shared;
    private Context context;
    private ArrayList<SelectAvailabilityModel> avalibilitylist, templist;
    String param;
    String lawyer_id;
    ImageView img_back;
    TextView txt_rate;
    private UserAvalibilityAdapter avalibilityAdapter;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_availabe_time_slot);
        context = this;
        shared = new Shared(context);
        communication_type_id = getIntent().getIntExtra("communication_type_id", 0);
        shared.setCommunication_type_id(communication_type_id);

        price = getIntent().getStringExtra("price");
        lawyer_id = getIntent().getStringExtra("lawyer_id");
        fname = getIntent().getStringExtra("fname");
        profile_img = getIntent().getStringExtra("profile_img");

        reference();
        init();
    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        edtStartTime = (EditText) findViewById(R.id.edtStartTime);
        edtEndTime = (EditText) findViewById(R.id.edtEndTime);
        txt_rate = (TextView) findViewById(R.id.txt_rate);
        btn_make_payment = (Button) findViewById(R.id.btn_make_payment);
        recyler_setTimeslot = (RecyclerView) findViewById(R.id.recyler_setTimeslot);
        txt_rate.setText("Lawyer rate  " + price);
    }


    private void init() {
        edtStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final Calendar mcurrentTime = Calendar.getInstance();
                final int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                final int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(ActivityAvailabeTimeSlot.this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
                    @SuppressLint("NewApi")
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTime = hour + ":" + minute /*+ ":" + "00"*/;
                        // startTime = edtStartTime.getText().toString();

                        getTimeFormate(selectedHour, selectedMinute, view);
                        getTimeFormateEndTime((timePicker.getCurrentHour() + 1), (timePicker.getCurrentMinute()), view);
                        endTime = hour + 1 + ":" + minute;
                    }
                }, hour, minute, false);//Yes 24 hour time
                mTimePicker.show();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btn_make_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (startTime.contentEquals("")) {
                    Toaster.getToast(context, ErrorMessage.startTime);
                } else {
                    if (Constant_method.checkConn(context)) {
                        getCommunicationRequest();
                    } else {
                        Toaster.getToast(context, ErrorMessage.NoInternet);
                    }

                }
//                Intent i = new Intent(ActivityAvailabeTimeSlot.this, ActivityMakePayment.class);
//                startActivity(i);
            }
        });
        getTodayAvailable();
    }


    private void getTodayAvailable() {
        templist = new ArrayList<>();
        avalibilitylist = new ArrayList<SelectAvailabilityModel>();
        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.availability_today, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject objects = jsonArray.getJSONObject(i);
                SelectAvailabilityModel model = new SelectAvailabilityModel();
                model.setAh_lawyer_availability_id(objects.getString("ah_lawyer_availability_id"));
                model.setAh_day_id(objects.getString("ah_day_id"));
                model.setStart_time(objects.getString("start_time"));
                model.setEnd_time(objects.getString("end_time"));
                model.setDay_name(objects.getString("day_name"));
                avalibilitylist.add(model);
            }
            getFilterToday(avalibilitylist);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getFilterToday(ArrayList<SelectAvailabilityModel> alist) {

        templist.clear();
        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();
        String day = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date.getTime());

        for (SelectAvailabilityModel model : alist) {
            if (model.getDay_name().contentEquals(day)) {
                templist.add(model);
            }
        }
        avalibilityAdapter = new UserAvalibilityAdapter(context, templist);
        recyler_setTimeslot.setAdapter(avalibilityAdapter);
        recyler_setTimeslot.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyler_setTimeslot.setNestedScrollingEnabled(false);


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
            Timehours = pad(hourOfDay) + ":" + pad(selectedMinute);
        } else if (view.getId() == R.id.edtEndTime) {
            edtEndTime.setText(pad(hourOfDay) + ":" + pad(selectedMinute) + " " + format);
        }

    }

    private void getTimeFormateEndTime(int hourOfDay, int selectedMinute, View view) {
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
        edtEndTime.setText(pad(hourOfDay) + ":" + pad(selectedMinute) + " " + format);
    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    private void getCommunicationRequest() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String status;
                String msg = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");

                    if (status.contentEquals("1")) {
                        Toaster.getToast(context, "" + msg);
                        String communication_id = object.getString("communication_id");
                        shared.setCommunication_id(communication_id);
                        Intent i = new Intent(ActivityAvailabeTimeSlot.this, ActivityMakePayment.class);
                        i.putExtra("lawyer_id", lawyer_id);
                        i.putExtra("price", price);
                        i.putExtra("fname", fname);
                        i.putExtra("profile_img", profile_img);
                        i.putExtra("startTime", Timehours);
                        i.putExtra("communication_type_id", shared.getCommunication_type_id());
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);

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
                Log.d("Error", "" + error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.communicationRequest);
                    JSONObject body = new JSONObject();
                    body.put("requested_user_id", shared.getUserId());
                    body.put("communication_type_id", communication_type_id);
                    body.put("lawyer_id", lawyer_id);
                    body.put("start_time", startTime);
                    body.put("end_time", endTime);
                    body.put("amount", price);
                    body.put("othercharges", "0");
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
