package com.multipz.atmiyalawlab.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.atmiyalawlab.Activity.ActivityAudioCalling;
import com.multipz.atmiyalawlab.Activity.ActivityChatWithUser;
import com.multipz.atmiyalawlab.Activity.ActivityDrafiting;
import com.multipz.atmiyalawlab.Activity.ActivityLoginScreen;
import com.multipz.atmiyalawlab.Adapter.DraftingTypeAdapter;
import com.multipz.atmiyalawlab.Model.ModelDraftingType;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;
import com.multipz.atmiyalawlab.VideoCalling.LoginUserActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

public class ActivityMakePayment extends AppCompatActivity implements PaymentResultListener {
    private static final String TAG = ActivityMakePayment.class.getSimpleName();
    private int communication_type_id;
    private String profile_img, fname, lawyer_id = "", price = "", startTime = "", starthours = "", startMinute = "";
    private TextView txtCallname, txtPrice;
    Button button;
    private Context context;
    private Shared shared;
    private int start_hours, curr_hours, start_minute, curr_minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_payment);
        Checkout.preload(getApplicationContext());
        context = this;
        shared = new Shared(context);


        reference();
        init();
    }

    private void reference() {
        txtCallname = (TextView) findViewById(R.id.txtCallname);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
    }

    private void init() {

        Intent i = getIntent();
        communication_type_id = i.getIntExtra("communication_type_id", 0);
        profile_img = getIntent().getStringExtra("profile_img");
        fname = getIntent().getStringExtra("fname");
        lawyer_id = getIntent().getStringExtra("lawyer_id");
        price = getIntent().getStringExtra("price");
        startTime = getIntent().getStringExtra("startTime");

        String[] starthrs = startTime.split(":");

        starthours = starthrs[0];
        startMinute = starthrs[1];

        start_hours = Integer.parseInt(starthours);
        start_minute = Integer.parseInt(startMinute);
        curr_hours = new Time(System.currentTimeMillis()).getHours();
        curr_minute = new Time(System.currentTimeMillis()).getMinutes();
        getTimeFormateEndTime(curr_hours, curr_minute);


        button = (Button) findViewById(R.id.btn_pay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });
        txtPrice.setText(price);
/*

        Log.d("s_h", "" + start_hours);
        Log.d("s_m", "" + start_minute);
        Log.d("c_h", "" + curr_hours);
        Log.d("c_m", "" + curr_minute);

*/

    }

    private void getTimeFormateEndTime(int hourOfDay, int selectedMinute) {
        if (hourOfDay == 0) {
            hourOfDay += 12;
            //  format = "AM";
        } else if (hourOfDay == 12) {
            //format = "PM";
        } else if (hourOfDay > 12) {
            hourOfDay -= 12;
            //format = "PM";
        } else {
            // format = "AM";
        }
        curr_hours = Integer.parseInt(pad(hourOfDay));

    }

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            //  Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            if (Constant_method.checkConn(context)) {
                Communicationpayment(razorpayPaymentID);
                // makeAppoinmentPayment(razorpayPaymentID);
            } else {
                Toaster.getToast(context, ErrorMessage.NoInternet);
            }


        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }

    @SuppressWarnings("unused")
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }

    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        double ruppes = Double.parseDouble(price);
        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.application_name));
            options.put("description", "");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", getResources().getDrawable(R.mipmap.ic_app_logo));
            options.put("currency", "INR");
            options.put("amount", ruppes * 100);
            JSONObject preFill = new JSONObject();
            preFill.put("email", "multipz.paresh@gmail.com");
            preFill.put("contact", "8758689113");
            options.put("prefill", preFill);
            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    private void Communicationpayment(final String razorpayPaymentID) {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
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
                        // redirectToDashboard();
                        /*if (shared.getCommunication_type_id() == 2) {
                            Intent intent = new Intent(ActivityMakePayment.this, ActivityChatWithUser.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }*/
                        checkTime(start_hours, start_minute, curr_hours, curr_minute);
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

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.communicationPayment);
                    JSONObject body = new JSONObject();
                    body.put("communication_id", shared.getCommunication_id());
                    body.put("requested_user_id", shared.getUserId());
                    body.put("payment_id", razorpayPaymentID);
                    body.put("payment_status", "Success");
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

    void checkTime(int st_hours, int st_minute, int crr_hours, int crr_minute) {
        if (st_hours == crr_hours) {
            if (crr_minute <= st_minute) {
                Intent intent = new Intent(ActivityMakePayment.this, ActivityCallProfile.class);
                intent.putExtra("lawyer_id", lawyer_id /*+ "***" + fname*/);
                intent.putExtra("lawyer_profile", profile_img);
                intent.putExtra("lawyer_name", fname);
                //intent.putExtra("communication_type_id", communication_type_id);
                intent.putExtra("communication_type_id", shared.getCommunication_type_id());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                redirectToDashboard();
            }
        } else {
            redirectToDashboard();
        }

    }

    void redirectToDashboard() {
        if (shared.getCommunication_type_id() == 1) {
            Intent i = new Intent(ActivityMakePayment.this, ActivityDashboardUser.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //  i.putExtra("communication_type_id", communication_type_id);
            i.putExtra("communication_type_id", shared.getCommunication_type_id());
            i.putExtra("profile_img", profile_img);
            i.putExtra("fname", fname);
            startActivity(i);
        } else if (shared.getCommunication_type_id() == 2) {
            Intent i = new Intent(ActivityMakePayment.this, ActivityDashboardUser.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.putExtra("communication_type_id", communication_type_id);
            i.putExtra("communication_type_id", shared.getCommunication_type_id());
            i.putExtra("profile_img", profile_img);
            i.putExtra("fname", fname);
            startActivity(i);
        } else if (shared.getCommunication_type_id() == 3) {
            Intent i = new Intent(ActivityMakePayment.this, ActivityDashboardUser.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            //i.putExtra("communication_type_id", communication_type_id);
            i.putExtra("communication_type_id", shared.getCommunication_type_id());
            i.putExtra("profile_img", profile_img);
            i.putExtra("fname", fname);
            startActivity(i);
        }
    }

}
