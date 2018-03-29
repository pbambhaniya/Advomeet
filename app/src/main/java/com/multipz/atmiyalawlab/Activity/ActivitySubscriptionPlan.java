package com.multipz.atmiyalawlab.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Adapter.GetSubsriptionPlanAdapter;
import com.multipz.atmiyalawlab.Adapter.RegSpecailizationAdapter;
import com.multipz.atmiyalawlab.Model.ModelSpecialization;
import com.multipz.atmiyalawlab.Model.SubscriptionPlanModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.User.ActivityDashboardUser;
import com.multipz.atmiyalawlab.User.ActivityMakePayment;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivitySubscriptionPlan extends AppCompatActivity implements ItemClickListener, PaymentResultListener {

    private RelativeLayout rel_root, rel_progress;
    private RecyclerView listsubscriptionplan;
    private Context mContext;
    private Shared shared;
    private ArrayList<SubscriptionPlanModel> mList;
    private CircularProgressView progressBar1;
    private GetSubsriptionPlanAdapter adapter;
    private ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_plan);
        Checkout.preload(getApplicationContext());
        mContext = this;
        shared = new Shared(mContext);
        mList = new ArrayList<>();
        reference();
        init();
    }

    void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        listsubscriptionplan = (RecyclerView) findViewById(R.id.listsubscriptionplan);
        img_back = (ImageView) findViewById(R.id.img_back);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault(rel_root);

    }

    void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SubscriptionPlan();
    }

    void SubscriptionPlan() {
        try {
            JSONArray array = new JSONArray(shared.getString(Config.subscription, "[{}]"));
            for (int i = 0; i < array.length(); i++) {
                JSONObject state = array.getJSONObject(i);
                SubscriptionPlanModel model = new SubscriptionPlanModel();
                model.setAh_subscription_id(state.getInt("ah_subscription_id"));
                model.setAh_subscription_type_id(state.getInt("ah_subscription_type_id"));
                model.setSubscription_type(state.getString("subscription_type"));
                model.setSubscription_plan(state.getString("subscription_plan"));
                model.setSubscription_details(state.getString("subscription_details"));
                model.setValidity(state.getInt("validity"));
                model.setPrice(state.getString("price"));
                mList.add(model);
            }
            adapter = new GetSubsriptionPlanAdapter(mContext, mList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            listsubscriptionplan.setLayoutManager(mLayoutManager);
            listsubscriptionplan.setAdapter(adapter);
            adapter.setClickListener(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void itemClicked(View View, int position) {
        SubscriptionPlanModel model = mList.get(position);
        shared.setAh_subscription_id(model.getAh_subscription_id());
        shared.setSubscription_price(model.getPrice());
        startPayment(model.getPrice());


    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {

            if (Constant_method.checkConn(mContext)) {
                upgarteAccount(razorpayPaymentID);

            } else {
                Toaster.getToast(mContext, ErrorMessage.NoInternet);
            }
        } catch (Exception e) {
            e.printStackTrace();
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


    public void startPayment(String ruppes) {

        double rupeess = Double.parseDouble(ruppes);

        final Activity activity = this;
        final Checkout co = new Checkout();
        try {
            JSONObject options = new JSONObject();
            options.put("name", getResources().getString(R.string.application_name));
            options.put("description", "");
            //You can omit the image option to fetch the image from dashboard
            options.put("image", getResources().getDrawable(R.mipmap.ic_app_logo));
            options.put("currency", "INR");
            options.put("amount", rupeess * 100);
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

    void upgarteAccount(final String paymentID) {
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
                        Intent i = new Intent(ActivitySubscriptionPlan.this, ActivityDashboard.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else if (status.contentEquals("0")) {
                        Toaster.getToast(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.upgradeAccount);
                    JSONObject body = new JSONObject();
                    body.put("ah_subscription_id", shared.getAh_subscription_id());
                    body.put("ah_users_id", shared.getUserId());
                    body.put("price", shared.getSubscription_price());
                    body.put("payment_id", paymentID);
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


}
