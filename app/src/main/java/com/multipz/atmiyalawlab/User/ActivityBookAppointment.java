package com.multipz.atmiyalawlab.User;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.atmiyalawlab.Activity.ActivityLawyerBookAppointment;
import com.multipz.atmiyalawlab.Activity.ActivityMyAppointmentList;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityBookAppointment extends AppCompatActivity implements PaymentResultListener {

    private ImageView img_back;
    private CircleImageView img_registration;
    private TextView txtdate, txttime, txtagenda, txtdesc, txtusername, txtPaidUnPaid, txtPrice;
    private String image = "", fullname = "", subject = "", desription = "", date = "", stime = "", etime = "", price = "";
    private Context context;
    private Shared shared;
    private LinearLayout lnr_pay_and_reschedule;
    private RelativeLayout rel_root;
    private int appointment_id = 0, status_id = 0, is_payment = 0;
    private Button txtreschedule_appoinement;
    private String param = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);
        context = this;
        shared = new Shared(context);

        reference();
        init();
    }

    void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_registration = (CircleImageView) findViewById(R.id.img_registration);
        txtdate = (TextView) findViewById(R.id.txtdate);
        txttime = (TextView) findViewById(R.id.txttime);
        txtagenda = (TextView) findViewById(R.id.txtagenda);
        txtdesc = (TextView) findViewById(R.id.txtdesc);
        txtusername = (TextView) findViewById(R.id.txtusername);
        txtPaidUnPaid = (TextView) findViewById(R.id.txtPaidUnPaid);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        lnr_pay_and_reschedule = (LinearLayout) findViewById(R.id.lnr_pay_and_reschedule);
        txtreschedule_appoinement = (Button) findViewById(R.id.txtreschedule_appoinement);
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    void init() {

        if (shared.getUsertype().contentEquals("L")) {
            lnr_pay_and_reschedule.setVisibility(View.GONE);
        }


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        Intent i = getIntent();
        image = i.getStringExtra("image");
        Picasso.with(context).load(image).into(img_registration);
        fullname = i.getStringExtra("fullname");
        subject = i.getStringExtra("subject");
        desription = i.getStringExtra("desription");
        date = i.getStringExtra("date");
        stime = i.getStringExtra("stime");
        etime = i.getStringExtra("etime");
        status_id = i.getIntExtra("status_id", 0);
        appointment_id = i.getIntExtra("appointment_id", 0);
        is_payment = i.getIntExtra("is_payment", 0);
        price = i.getStringExtra("price");
        shared.setAppointment_id(appointment_id);


        if (status_id == 1) {
            txtPaidUnPaid.setVisibility(View.VISIBLE);
            txtPaidUnPaid.setBackground(getResources().getDrawable(R.drawable.bg_btn_cancel_red));
            txtPaidUnPaid.setText("Cancel");
        } else if (status_id == 2) {
            if (is_payment == 1) {
                txtPaidUnPaid.setVisibility(View.VISIBLE);
                txtPaidUnPaid.setBackground(getResources().getDrawable(R.drawable.bg_btn_cancel_red));
                txtPaidUnPaid.setText("Cancel");
            } else if (is_payment == 0) {
                txtPaidUnPaid.setVisibility(View.VISIBLE);
                txtPrice.setText(Config.ruppessSymbol + price);
            }
        }


        txtusername.setText(fullname);
        txtdate.setText(date);
        txtagenda.setText(subject);
        txtdesc.setText(desription);
        txttime.setText(stime + " To " + etime);

        txtPaidUnPaid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (txtPaidUnPaid.getText().toString().contentEquals("Cancel")) {
                    popCancelDailog(appointment_id);
                } else {
                    startPayment();
                }

            }
        });


        txtreschedule_appoinement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityBookAppointment.this, ActivityLawyerBookAppointment.class);
                i.putExtra("reschedule", true);
                i.putExtra("appointment_id", appointment_id);
                i.putExtra("subject", subject);
                i.putExtra("description", desription);
                i.putExtra("date", date);
                i.putExtra("stime", stime);
                i.putExtra("etime", etime);
                i.putExtra("price", price);
                startActivity(i);

            }
        });

    }

    void popCancelDailog(final int appointment_id) {

        LayoutInflater inflater = ActivityBookAppointment.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_assign_case, null);
        TextView txt_des = (TextView) c.findViewById(R.id.head_desc);
        TextView txt_Ok = (TextView) c.findViewById(R.id.txt_Ok);
        TextView txt_Cancel = (TextView) c.findViewById(R.id.txt_Cancel);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityBookAppointment.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        txt_des.setText(Config.cancelRequest);

        txt_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant_method.checkConn(context)) {
                    CancelAppoinment(appointment_id, 4);
                    dialog.cancel();
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

    private void CancelAppoinment(final int appointment_id, final int status_id) {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;

        // rel_progress.setVisibility(View.VISIBLE);

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
                        Toaster.getToast(getApplicationContext(), "" + msg);
                        Intent intent = new Intent(ActivityBookAppointment.this, ActivityMyAppointmentList.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else if (status.contentEquals("0")) {
                        //rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(getApplicationContext(), "" + msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // dialog.dismiss();
                Log.d("error", "" + error);
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
                    body.put("status_id", status_id);
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


    private void makeAppoinmentPayment(final String razorpayPaymentID) {

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
                        Intent i = new Intent(ActivityBookAppointment.this, ActivityDashboardUser.class);
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
                    main.put("action", Config.makeAppointmentPayment);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("appointment_id", shared.getAppointment_id());
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

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        Double rupess = Double.parseDouble(price);

        final Activity activity = this;

        final Checkout co = new Checkout();

        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.application_name));
            options.put("description", "Appointment Charges");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", rupess * 100);
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

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            if (Constant_method.checkConn(context)) {
                makeAppoinmentPayment(razorpayPaymentID);
            } else {
                Toaster.getToast(context, ErrorMessage.NoInternet);
            }


        } catch (Exception e) {
            Log.e("", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e("", "Exception in onPaymentError", e);
        }

    }
}
