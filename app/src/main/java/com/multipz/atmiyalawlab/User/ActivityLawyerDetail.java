package com.multipz.atmiyalawlab.User;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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
import com.multipz.atmiyalawlab.Activity.ActivityAudioCalling;
import com.multipz.atmiyalawlab.Activity.ActivityAvailabeTimeSlot;
import com.multipz.atmiyalawlab.Activity.ActivityChatWithUser;
import com.multipz.atmiyalawlab.Activity.ActivityChatting;
import com.multipz.atmiyalawlab.Activity.ActivityLawyerBookAppointment;
import com.multipz.atmiyalawlab.Adapter.CourtLawyerDetailAdapter;
import com.multipz.atmiyalawlab.Adapter.Lawyer_language_detail_Adapter;
import com.multipz.atmiyalawlab.Adapter.ReviewsLawyerDetailAdapter;
import com.multipz.atmiyalawlab.Adapter.SpecializationAdapter;
import com.multipz.atmiyalawlab.Adapter.UserAvalibilityAdapter;
import com.multipz.atmiyalawlab.Model.CourtModel;
import com.multipz.atmiyalawlab.Model.LanguageModel;
import com.multipz.atmiyalawlab.Model.ModelReviewsLawyerDetail;
import com.multipz.atmiyalawlab.Model.SelectAvailabilityModel;
import com.multipz.atmiyalawlab.Model.SpecializationModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;
import com.multipz.atmiyalawlab.VideoCalling.LoginUserActivity;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityLawyerDetail extends AppCompatActivity implements ItemClickListener {

    private RelativeLayout rel_root, rel_book_appointment, header_langauage;
    private ImageView img_back, img_like_bookmark;
    private CircleImageView img_lawyer;
    private TextView txt_lawyer_name, txt_city, txt_experiense, txt_availabilty, txt_ratting, txt_total_case, btn_call, btn_message, btn_video_call, txttotalrating;
    private LinearLayout txtSeeAll_Feedback;
    private RecyclerView rv_courts, rel_review, rv_avalibility, rv_specilization, rv_language;
    private ArrayList<CourtModel> courtList;
    private ArrayList<ModelReviewsLawyerDetail> reviewlist;
    private ArrayList<SelectAvailabilityModel> avalibilitylist;
    SpecializationAdapter sp_adapter;
    private ArrayList<SpecializationModel> specializationlist;
    private ReviewsLawyerDetailAdapter rivewdapter;
    private UserAvalibilityAdapter avalibilityAdapter;
    private CourtLawyerDetailAdapter adapter;
    private NestedScrollView scrollmain;
    private ProgressDialog dialog;
    String lawyer_id = "", param;
    Context context;
    Shared shared;
    private ArrayList<LanguageModel> languagelist;
    Lawyer_language_detail_Adapter lan_adapter;
    String ah_lawyer_id, fname, experience, profile_img, city_name, avg_rating, is_bookmark, total_rating_count, total_case_assign, is_available, statusid, user_status;
    private CircularProgressView progressBar1;
    RelativeLayout rel_progress;
    private boolean is_selected_call = false, isIs_selected_callchat = false, isIs_selected_callVideo = false, is_available_now = false, is_Appointment = false, is_showfeedback = true;
    private int communication_type_id_Call = 0, communication_type_id_video = 0, communication_type_id_Chat = 0, communication_id = 0;
    String price, pricechat, pricechatvideo, priceappointment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_detail);
        context = this;
        shared = new Shared(context);
        courtList = new ArrayList<>();
        reviewlist = new ArrayList<>();
        avalibilitylist = new ArrayList<>();
        specializationlist = new ArrayList<>();
        languagelist = new ArrayList<>();

        lawyer_id = getIntent().getStringExtra("lawyer_id");
        reference();
        init();


    }

    private void reference() {
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        scrollmain = (NestedScrollView) findViewById(R.id.scrollmain);

        header_langauage = (RelativeLayout) findViewById(R.id.header_langauage);
        img_back = (ImageView) findViewById(R.id.img_back);
        img_like_bookmark = (ImageView) findViewById(R.id.img_like_bookmark);
        img_lawyer = (CircleImageView) findViewById(R.id.img_lawyer);
        txt_lawyer_name = (TextView) findViewById(R.id.txt_lawyer_name);
        txt_city = (TextView) findViewById(R.id.txt_city);
        txt_experiense = (TextView) findViewById(R.id.txt_experiense);
        txt_availabilty = (TextView) findViewById(R.id.txt_availabilty);
        txt_ratting = (TextView) findViewById(R.id.txt_ratting);
        txt_total_case = (TextView) findViewById(R.id.txt_total_case);
        btn_call = (TextView) findViewById(R.id.btn_call);
        btn_message = (TextView) findViewById(R.id.btn_message);
        btn_video_call = (TextView) findViewById(R.id.btn_video_call);
        rv_avalibility = (RecyclerView) findViewById(R.id.rv_avalibility);

        txttotalrating = (TextView) findViewById(R.id.txttotalrating);
        rv_courts = (RecyclerView) findViewById(R.id.rv_courts);
        rel_review = (RecyclerView) findViewById(R.id.rel_review);
        rv_specilization = (RecyclerView) findViewById(R.id.rv_specilization);
        rv_language = (RecyclerView) findViewById(R.id.rv_language);
        txtSeeAll_Feedback = (LinearLayout) findViewById(R.id.txtSeeAll_Feedback);
        rel_book_appointment = (RelativeLayout) findViewById(R.id.rel_book_appointment);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();
        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));

    }

    private void init() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityLawyerDetail.this, ActivityChatWithUser.class);
                startActivity(i);
            }
        });
        btn_video_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityLawyerDetail.this, LoginUserActivity.class);
                startActivity(i);

            }
        });


        img_like_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (Constant_method.checkConn(context)) {
                    addBookmark();
                } else {
                    Toaster.getToast(context, ErrorMessage.NoInternet);
                }
            }
        });

        if (Constant_method.checkConn(context)) {
            getlawyerDetail();
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }
        rel_book_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shared.getUsertype().contentEquals("U")) {
                    Intent i = new Intent(ActivityLawyerDetail.this, ActivityLawyerBookAppointment.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.putExtra("lawyer_id", ah_lawyer_id);
                    i.putExtra("price", priceappointment);
                    startActivity(i);
                } else {
                    Intent i = new Intent(ActivityLawyerDetail.this, ActivityDashboardUser.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }


            }
        });

        btn_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityLawyerDetail.this, ActivityAvailabeTimeSlot.class);
                i.putExtra("communication_type_id", communication_type_id_Call);
                shared.setCommunication_type_id(communication_type_id_Call);
                i.putExtra("profile_img", profile_img);
                i.putExtra("fname", fname);
                i.putExtra("communication_id", communication_id);
                i.putExtra("price", price);
                i.putExtra("lawyer_id", ah_lawyer_id);
                startActivity(i);
            }
        });

        btn_message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityLawyerDetail.this, ActivityAvailabeTimeSlot.class);
                i.putExtra("communication_type_id", communication_type_id_Chat);
                shared.setCommunication_type_id(communication_type_id_Chat);
                i.putExtra("profile_img", profile_img);
                i.putExtra("fname", fname);
                i.putExtra("communication_id", communication_id);
                i.putExtra("price", pricechat);
                i.putExtra("lawyer_id", ah_lawyer_id);
                startActivity(i);
            }
        });

        btn_video_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityLawyerDetail.this, ActivityAvailabeTimeSlot.class);
                i.putExtra("communication_type_id", communication_type_id_video);
                shared.setCommunication_type_id(communication_type_id_video);
                i.putExtra("profile_img", profile_img);
                i.putExtra("fname", fname);
                i.putExtra("communication_id", communication_id);
                i.putExtra("price", pricechatvideo);
                i.putExtra("lawyer_id", ah_lawyer_id);
                startActivity(i);
            }
        });

        txtSeeAll_Feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityLawyerDetail.this, ActivityGiveFeedback.class);
                i.putExtra("lawyer_id", lawyer_id);
                i.putExtra("profile_img", profile_img);
                i.putExtra("lawyer_name", txt_lawyer_name.getText().toString());
                startActivity(i);

            }
        });

    }

    private void addBookmark() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                String status;
                String msg = "";
                boolean is_bookmark;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    is_bookmark = object.getBoolean("is_bookmark");
                    if (status.contentEquals("1")) {
                        if (is_bookmark) {
                            img_like_bookmark.setBackground(getResources().getDrawable(R.mipmap.ic_bookmark_select));
                        } else {
                            img_like_bookmark.setBackground(getResources().getDrawable(R.mipmap.ic_pro_bookmark));
                        }
                        Toaster.getToast(context, "" + msg);
                    } else if (status.contentEquals("0")) {
                        Toaster.getToast(context, "" + msg);
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
                    main.put("action", Config.bookmarkLawyer);
                    JSONObject body = new JSONObject();
                    body.put("ah_lawyer_id", lawyer_id);
                    body.put("ah_users_id", shared.getUserId());
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


    private void getlawyerDetail() {

        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                scrollmain.setVisibility(View.VISIBLE);
                String msg = "", status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        rel_progress.setVisibility(View.GONE);
                        JSONObject dataobj = object.getJSONObject("data");
                        JSONArray array = dataobj.getJSONArray("basicdetail");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.getJSONObject(i);
                            ah_lawyer_id = obj.getString("ah_lawyer_id");
                            fname = obj.getString("full_name");
                            experience = obj.getString("experience");
                            profile_img = obj.getString("profile_img");
                            city_name = obj.getString("city_name");
                            avg_rating = obj.getString("avg_rating");


                            total_rating_count = obj.getString("total_rating_count");
                            total_case_assign = obj.getString("total_case_assign");
                            is_available = obj.getString("is_available");
                            is_bookmark = obj.getString("is_bookmark");

                            is_available_now = obj.getBoolean("is_available_now");
                            statusid = obj.getString("statusid");
                            user_status = obj.getString("user_status");
                            String experienceinyear = obj.getString("experienceinyear");
                            String experienceinmonth = obj.getString("experienceinmonth");
                            is_showfeedback = obj.getBoolean("is_showfeedback");
                            if (is_showfeedback) {
                                txtSeeAll_Feedback.setVisibility(View.VISIBLE);
                            } else {
                                txtSeeAll_Feedback.setVisibility(View.GONE);
                            }

                            txt_lawyer_name.setText(fname);
                            txt_city.setText(city_name);
                            txt_experiense.setText("Experience -" + experienceinyear + "." + experienceinmonth + " Year");
                            if (avg_rating.contentEquals("null")) {
                                txt_ratting.setText("0");
                            } else {
                                txt_ratting.setText(avg_rating);
                            }


                            txt_total_case.setText("Total Rating : " + total_case_assign);
                            txttotalrating.setText("Total Case : " + total_rating_count);


                            if (profile_img.contentEquals("")) {
                                Picasso.with(context).load(profile_img).placeholder(R.drawable.user).into(img_lawyer);
                            } else {
                                Picasso.with(context).load(profile_img).into(img_lawyer);
                            }
//                            list.add(model);
                            if (is_bookmark.contentEquals("true")) {
                                img_like_bookmark.setBackground(getResources().getDrawable(R.mipmap.ic_bookmark_select));
                            } else {
                                img_like_bookmark.setBackground(getResources().getDrawable(R.mipmap.ic_pro_bookmark));
                            }
                        }

//                        adapter = new SearchUserAdapter(context, list);
//                        rv_seach.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
//                        rv_seach.setAdapter(adapter);
//                        adapter.setClickListener(SearchActivity.this);
//                        rv_seach.setNestedScrollingEnabled(false);

                        JSONArray court = dataobj.getJSONArray("court");

                        for (int i = 0; i < court.length(); i++) {
                            JSONObject c = court.getJSONObject(i);
                            CourtModel model = new CourtModel();
                            model.setAh_lawyer_court_id(c.getString("ah_lawyer_court_id"));
                            model.setCourt_name(c.getString("court_name"));
                            model.setAh_court_id(c.getString("ah_court_id"));
                            courtList.add(model);

                        }
                        adapter = new CourtLawyerDetailAdapter(context, courtList);
                        rv_courts.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        rv_courts.setAdapter(adapter);
                        adapter.setClickListener(ActivityLawyerDetail.this);
                        rv_courts.setNestedScrollingEnabled(false);


                        JSONArray specialization = dataobj.getJSONArray("specialization");
                        //     shared.putString("laywer_type", specialization.toString());
                        for (int s = 0; s < specialization.length(); s++) {
                            JSONObject specialization_detail = specialization.getJSONObject(s);
                            SpecializationModel model = new SpecializationModel();
                            model.setAh_lawyer_specialist_id(specialization_detail.getString("ah_lawyer_specialist_id"));
                            model.setAh_lawyer_type_id(specialization_detail.getString("ah_lawyer_type_id"));
                            model.setLawyer_type(specialization_detail.getString("lawyer_type"));
                            specializationlist.add(model);
                        }
                        sp_adapter = new SpecializationAdapter(specializationlist, context);
                        rv_specilization.setAdapter(sp_adapter);
                        rv_specilization.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
                        rv_specilization.setNestedScrollingEnabled(false);
                        sp_adapter.setClickListener(ActivityLawyerDetail.this);

                        JSONArray review = dataobj.getJSONArray("reviews");
                        for (int j = 0; j < review.length(); j++) {
                            JSONObject r = review.getJSONObject(j);
                            ModelReviewsLawyerDetail model = new ModelReviewsLawyerDetail();
                            model.setAh_feedback_id(r.getString("ah_feedback_id"));
                            model.setAh_lawyer_id(r.getString("ah_lawyer_id"));
                            model.setAh_users_id(r.getString("ah_users_id"));
                            model.setFull_name(r.getString("full_name"));
                            model.setRate(r.getString("rate"));
                            model.setMessage(r.getString("message"));
                            model.setFeedback_date(r.getString("feedback_date"));
                            reviewlist.add(model);
                        }
                        rivewdapter = new ReviewsLawyerDetailAdapter(context, reviewlist);
                        rel_review.setAdapter(rivewdapter);
                        rel_review.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        rel_review.setNestedScrollingEnabled(false);
                        rivewdapter.setClickListener(ActivityLawyerDetail.this);


                        JSONArray avalibility = dataobj.getJSONArray("availability");
                        shared.putString(Config.availability_today, avalibility.toString());

                        for (int a = 0; a < avalibility.length(); a++) {
                            JSONObject objects = avalibility.getJSONObject(a);
                            SelectAvailabilityModel model = new SelectAvailabilityModel();
                            model.setAh_lawyer_availability_id(objects.getString("ah_lawyer_availability_id"));
                            model.setAh_day_id(objects.getString("ah_day_id"));
                            model.setStart_time(objects.getString("start_time"));
                            model.setEnd_time(objects.getString("end_time"));
                            model.setDay_name(objects.getString("day_name"));
                            avalibilitylist.add(model);
                        }

                        avalibilityAdapter = new UserAvalibilityAdapter(context, avalibilitylist);
                        rv_avalibility.setAdapter(avalibilityAdapter);
                        rv_avalibility.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                        avalibilityAdapter.setClickListener(ActivityLawyerDetail.this);
                        rv_avalibility.setNestedScrollingEnabled(false);


                        JSONArray language = dataobj.getJSONArray("language");
                        if (language.length() > 0) {
                            for (int l = 0; l < language.length(); l++) {
                                JSONObject lan = language.getJSONObject(l);
                                LanguageModel model = new LanguageModel();
                                model.setAh_lawyer_language_id(lan.getString("ah_lawyer_language_id"));
                                model.setAh_language_id(lan.getString("ah_language_id"));
                                model.setLanguage_name(lan.getString("language_name"));
                                languagelist.add(model);

//                            if (language.length() - 1 == l) {
//                                language_name = language_name + lan.getString("language_name");
//                            } else {
//                                language_name = language_name + lan.getString("language_name") + ", ";
//
//                            }
//                            txt_language.setText(language_name);
                            }

                            header_langauage.setVisibility(View.VISIBLE);
                            lan_adapter = new Lawyer_language_detail_Adapter(context, languagelist);
                            rv_language.setAdapter(lan_adapter);
                            rv_language.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                            lan_adapter.setClickListener(ActivityLawyerDetail.this);
                            rv_language.setNestedScrollingEnabled(false);
                        } else {
                            header_langauage.setVisibility(View.GONE);
                        }


                        JSONObject communication = dataobj.getJSONObject("communication");
                        JSONObject objCall = communication.getJSONObject("call");
                        communication_id = objCall.getInt("communication_id");
                        communication_type_id_Call = objCall.getInt("communication_type_id");
                        String communication_type = objCall.getString("communication_type");
                        price = objCall.getString("price");
                        int price_type_id = objCall.getInt("price_type_id");
                        String price_type = objCall.getString("price_type");
                        String op_expression = objCall.getString("op_expression");
                        String is_multiply_diff = objCall.getString("is_multiply_diff");
                        is_selected_call = objCall.getBoolean("is_selected");


                        JSONObject chat = communication.getJSONObject("chat");
                        communication_id = chat.getInt("communication_id");
                        communication_type_id_Chat = chat.getInt("communication_type_id");
                        String communication_typechat = chat.getString("communication_type");
                        pricechat = chat.getString("price");
                        int price_type_idchat = chat.getInt("price_type_id");
                        String price_typechat = chat.getString("price_type");
                        String op_expressionchat = chat.getString("op_expression");
                        String is_multiply_diffchat = chat.getString("is_multiply_diff");
                        isIs_selected_callchat = chat.getBoolean("is_selected");

                        JSONObject video = communication.getJSONObject("video");
                        communication_id = video.getInt("communication_id");
                        communication_type_id_video = video.getInt("communication_type_id");
                        String communication_typechatvideo = video.getString("communication_type");
                        pricechatvideo = video.getString("price");
                        int price_type_idchatvideo = video.getInt("price_type_id");
                        String price_typechatvideo = video.getString("price_type");
                        String op_expressionchatvideo = video.getString("op_expression");
                        String is_multiply_diffchatvideo = video.getString("is_multiply_diff");
                        isIs_selected_callVideo = video.getBoolean("is_selected");

                        JSONObject appointment = communication.getJSONObject("appointment");
                        is_Appointment = appointment.getBoolean("is_selected");
                        priceappointment = appointment.getString("price");

                        if (!is_Appointment) {
                            rel_book_appointment.setVisibility(View.GONE);

                        } else {
                            rel_book_appointment.setVisibility(View.VISIBLE);
                        }

                        if (is_available_now) {
                            //visible
                            if (is_selected_call) {
                                btn_call.setVisibility(View.VISIBLE);
                            } else {
                                btn_call.setVisibility(View.GONE);
                            }

                            if (isIs_selected_callchat) {
                                btn_message.setVisibility(View.VISIBLE);
                            } else {
                                btn_message.setVisibility(View.GONE);
                            }
                            if (isIs_selected_callVideo) {
                                btn_video_call.setVisibility(View.VISIBLE);
                            } else {
                                btn_video_call.setVisibility(View.GONE);
                            }

//                            btn_call.setVisibility(View.VISIBLE);
//                            btn_message.setVisibility(View.VISIBLE);
//                            btn_video_call.setVisibility(View.VISIBLE);

                        } else {
                            //gone
                            btn_call.setVisibility(View.GONE);
                            btn_message.setVisibility(View.GONE);
                            btn_video_call.setVisibility(View.GONE);
                        }

                    } else {
//                        dialog.dismiss();
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
//                dialog.hide();
                rel_progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.getLawyerDetails);
                    JSONObject body = new JSONObject();
                    body.put("ah_lawyer_id", lawyer_id);
                    body.put("ah_users_id", shared.getUserId());
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

    @Override
    public void itemClicked(View View, int position) {

    }
}
