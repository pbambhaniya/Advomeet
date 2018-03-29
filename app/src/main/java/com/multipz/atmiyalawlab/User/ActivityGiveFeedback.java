package com.multipz.atmiyalawlab.User;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityGiveFeedback extends AppCompatActivity {

    private CircleImageView img_dp;
    private TextView txtassign_lawyer_name, txtDivorceLawyer, btn_give_feedback, btn_no_thanks;
    private RatingBar feedbackRating;
    private EditText edt_feedback_description;
    private RelativeLayout rel_root, rel_progress;
    private ImageView img_back;
    private String param = "";
    private Context mContext;
    private Shared shared;
    private float rating = 0;
    private String message = "", lawyer_id = "", profile_img = "", lawyer_name = "";
    private CircularProgressView progressBar1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_give_feedback);
        mContext = this;
        shared = new Shared(mContext);
        reference();
        init();


    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        img_dp = (CircleImageView) findViewById(R.id.img_dp);
        txtassign_lawyer_name = (TextView) findViewById(R.id.txtassign_lawyer_name);
        txtDivorceLawyer = (TextView) findViewById(R.id.txtDivorceLawyer);
        btn_give_feedback = (TextView) findViewById(R.id.btn_give_feedback);

        btn_no_thanks = (TextView) findViewById(R.id.btn_no_thanks);
        feedbackRating = (RatingBar) findViewById(R.id.feedbackRating);
        edt_feedback_description = (EditText) findViewById(R.id.edt_feedback_description);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI(rel_root, ActivityGiveFeedback.this);

    }

    private void init() {
        lawyer_id = getIntent().getStringExtra("lawyer_id");
        profile_img = getIntent().getStringExtra("profile_img");
        lawyer_name = getIntent().getStringExtra("lawyer_name");

        Glide.with(mContext).load(profile_img).into(img_dp);
        txtassign_lawyer_name.setText(lawyer_name);


        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btn_no_thanks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        feedbackRating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                rating = v;
            }
        });

        btn_give_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edt_feedback_description.getText().toString().contentEquals("")) {
                    Toaster.getToast(mContext, "Please enter description");
                } else {
                    message = edt_feedback_description.getText().toString();
                    getFeedback();
                }
            }
        });
    }


    void getFeedback() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
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
                        Intent i = new Intent(ActivityGiveFeedback.this, ActivityDashboardUser.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                    } else {
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(mContext, "" + msg);
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
                    main.put("action", Config.addFeedback);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("ah_lawyer_id", lawyer_id);
                    body.put("message", message);
                    body.put("rate", rating);
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
