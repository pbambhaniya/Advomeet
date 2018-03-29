package com.multipz.atmiyalawlab.Activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.multipz.atmiyalawlab.Model.CommunicationModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.User.ActivityChangeContactNo;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ActivityLawyerSetting extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {


    private LinearLayout lnr_availabilty, lnr_Courts, lnr_Practice_Area, lnr_language, lnr_change_pass, lnr_rate_us, lnr_contact_medium, lnr_change_con_no;
    private ImageView img_back;
    private RelativeLayout rel_root, rel_progress;
    String param;
    Context context;
    Shared shared;
    AlertDialog dialog;
    CheckBox cb_chat, cb_voice_call, cb_video_call, cb_book_appoinment;
    EditText et_chat, et_video_call, et_voice_call, et_book_appoinment;
    String c_chat, e_chat_price, c_call, e_call_price, C_appoinment, e_appoinment_price, C_video_call, e_video_call_price;
    boolean isSelectChat, isSelectCall, IsSelectAppoinment, IsSelectVideoCall;
    String com_chat_id, chat_com_type_id, com_call_id, call_com_type_id, com_video_id, video_com_type_id, com_appointment_id, appointment_com_type_id;
    private CircularProgressView progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_setting);
        context = this;
        shared = new Shared(context);
        reference();
        init();

    }

    private void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        lnr_change_pass = (LinearLayout) findViewById(R.id.lnr_change_pass);

        lnr_language = (LinearLayout) findViewById(R.id.lnr_language);
        lnr_Courts = (LinearLayout) findViewById(R.id.lnr_Courts);
        lnr_Practice_Area = (LinearLayout) findViewById(R.id.lnr_Practice_Area);
        lnr_availabilty = (LinearLayout) findViewById(R.id.lnr_availabilty);
        lnr_change_con_no = (LinearLayout) findViewById(R.id.lnr_change_con_no);
        lnr_contact_medium = (LinearLayout) findViewById(R.id.lnr_contact_medium);
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
        lnr_availabilty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLawyerSetting.this, SelectAvailabilityActivity.class);
                startActivity(intent);
            }
        });

        lnr_change_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLawyerSetting.this, ActivityChangePassword.class);
                startActivity(intent);
            }
        });

        lnr_language.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLawyerSetting.this, ActivityLawyerLanguage.class);
                startActivity(intent);
            }
        });
        lnr_Courts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLawyerSetting.this, ActivityLawyerCourt.class);
                startActivity(intent);
            }
        });
        lnr_Practice_Area.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLawyerSetting.this, ActivityPracticeArea.class);
                startActivity(intent);
            }
        });
        lnr_change_con_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityLawyerSetting.this, ActivityChangeContactNo.class);
                startActivity(intent);
            }
        });
        lnr_contact_medium.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupContactMedium();
            }
        });
    }

    public void PopupContactMedium() {
        LayoutInflater inflater = ActivityLawyerSetting.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_contact_medium, null);

        LinearLayout lnr_submit = (LinearLayout) c.findViewById(R.id.lnr_submit);
        cb_chat = (CheckBox) c.findViewById(R.id.cb_chat);
        cb_chat.setButtonDrawable(R.drawable.check_box);
        cb_voice_call = (CheckBox) c.findViewById(R.id.cb_voice_call);
        cb_voice_call.setButtonDrawable(R.drawable.check_box);
        cb_video_call = (CheckBox) c.findViewById(R.id.cb_video_call);
        cb_video_call.setButtonDrawable(R.drawable.check_box);
        cb_book_appoinment = (CheckBox) c.findViewById(R.id.cb_book_appoinment);
        cb_book_appoinment.setButtonDrawable(R.drawable.check_box);

        et_chat = (EditText) c.findViewById(R.id.et_chat);
        et_voice_call = (EditText) c.findViewById(R.id.et_voice_call);
        et_video_call = (EditText) c.findViewById(R.id.et_video_call);
        et_book_appoinment = (EditText) c.findViewById(R.id.et_book_appoinment);

        cb_voice_call.setOnCheckedChangeListener(this);
        cb_chat.setOnCheckedChangeListener(this);
        cb_video_call.setOnCheckedChangeListener(this);
        cb_book_appoinment.setOnCheckedChangeListener(this);

        getSetting();
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityLawyerSetting.this);
        builder.setView(c);

        dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        lnr_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chat = et_chat.getText().toString();
                String call = et_voice_call.getText().toString();
                String video_call = et_video_call.getText().toString();
                String appoinment = et_book_appoinment.getText().toString();

                if (et_chat.getText().toString().contentEquals("") || Double.parseDouble(chat.toString()) <= 50.00) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterprice);
                } else if (et_voice_call.getText().toString().contentEquals("") || Double.parseDouble(call.toString()) <= 50.00) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterprice);
                } else if (et_video_call.getText().toString().contentEquals("") || Double.parseDouble(video_call.toString()) <= 50.00) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterprice);
                } else if (et_book_appoinment.getText().toString().contentEquals("") || Double.parseDouble(appoinment.toString()) <= 50.00) {
                    Toaster.getToast(getApplicationContext(), ErrorMessage.enterprice);
                } else {
                    if (Constant_method.checkConn(context)) {
                        getModifyCom();
                        dialog.dismiss();
                    } else {
                        Toaster.getToast(context, ErrorMessage.NoInternet);
                    }
                }
            }
        });
    }


    private void getSetting() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String status = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    status = jsonObject.getString("status");
                    JSONObject object = jsonObject.getJSONObject("body");
                    String msg = object.getString("msg");
                    if (status.contentEquals("1")) {
                        rel_progress.setVisibility(View.GONE);
                        JSONObject dataobj = object.getJSONObject("data");
                        JSONObject comunication = dataobj.getJSONObject("communication");
                        shared.putString(Config.Communication, comunication.toString());

                        CommunicationModel model = new CommunicationModel();
                        JSONObject chat = comunication.getJSONObject("chat");
                        model.setCommunication_id(chat.getString("communication_id"));
                        model.setCommunication_type_id(chat.getString("communication_type_id"));
                        model.setCommunication_type(chat.getString("communication_type"));
                        model.setPrice(chat.getString("price"));
                        model.setPrice_type_id(chat.getString("price_type_id"));
                        model.setPrice_type(chat.getString("price_type"));
                        model.setOp_expression(chat.getString("op_expression"));
                        model.setIs_multiply_diff(chat.getString("is_multiply_diff"));
                        model.setIs_selected(chat.getBoolean("is_selected"));

                        com_chat_id = chat.getString("communication_id");
                        chat_com_type_id = chat.getString("communication_type_id");

                        cb_chat.setText(model.getCommunication_type());
                        cb_chat.setChecked(model.isIs_selected());
                        et_chat.setVisibility(View.VISIBLE);
                        et_chat.setText(model.getPrice());

                        if (model.isIs_selected()) {
                            et_chat.setEnabled(true);
                        } else {
                            et_chat.setEnabled(false);
                        }

                        JSONObject call = comunication.getJSONObject("call");
                        model.setCommunication_id(call.getString("communication_id"));
                        model.setCommunication_type_id(call.getString("communication_type_id"));
                        model.setCommunication_type(call.getString("communication_type"));
                        model.setPrice(call.getString("price"));
                        model.setPrice_type_id(call.getString("price_type_id"));
                        model.setPrice_type(call.getString("price_type"));
                        model.setOp_expression(call.getString("op_expression"));
                        model.setIs_multiply_diff(call.getString("is_multiply_diff"));
                        model.setIs_selected(call.getBoolean("is_selected"));


                        com_call_id = call.getString("communication_id");
                        call_com_type_id = call.getString("communication_type_id");
                        cb_voice_call.setText(model.getCommunication_type());
                        cb_voice_call.setChecked(model.isIs_selected());
                        et_voice_call.setVisibility(View.VISIBLE);
                        et_voice_call.setText(model.getPrice());


                        if (model.isIs_selected()) {
                            et_voice_call.setEnabled(true);
                        } else {
                            et_voice_call.setEnabled(false);
                        }

                        JSONObject video = comunication.getJSONObject("video");
                        model.setCommunication_id(video.getString("communication_id"));
                        model.setCommunication_type_id(video.getString("communication_type_id"));
                        model.setCommunication_type(video.getString("communication_type"));
                        model.setPrice(video.getString("price"));
                        model.setPrice_type_id(video.getString("price_type_id"));
                        model.setPrice_type(video.getString("price_type"));
                        model.setOp_expression(video.getString("op_expression"));
                        model.setIs_multiply_diff(video.getString("is_multiply_diff"));
                        model.setIs_selected(video.getBoolean("is_selected"));

                        com_video_id = video.getString("communication_id");
                        video_com_type_id = video.getString("communication_type_id");

                        cb_video_call.setText(model.getCommunication_type());
                        cb_video_call.setChecked(model.isIs_selected());
                        et_video_call.setVisibility(View.VISIBLE);
                        et_video_call.setText(model.getPrice());


                        if (model.isIs_selected()) {
                            et_video_call.setEnabled(true);
                        } else {
                            et_video_call.setEnabled(false);
                        }

                        JSONObject appointment = comunication.getJSONObject("appointment");
                        model.setCommunication_id(appointment.getString("communication_id"));
                        model.setCommunication_type_id(appointment.getString("communication_type_id"));
                        model.setCommunication_type(appointment.getString("communication_type"));
                        model.setPrice(appointment.getString("price"));
                        model.setPrice_type_id(appointment.getString("price_type_id"));
                        model.setPrice_type(appointment.getString("price_type"));
                        model.setOp_expression(appointment.getString("op_expression"));
                        model.setIs_multiply_diff(appointment.getString("is_multiply_diff"));
                        model.setIs_selected(appointment.getBoolean("is_selected"));

                        com_appointment_id = appointment.getString("communication_id");
                        appointment_com_type_id = appointment.getString("communication_type_id");

                        cb_book_appoinment.setText(model.getCommunication_type());
                        cb_book_appoinment.setChecked(model.isIs_selected());
                        et_book_appoinment.setVisibility(View.VISIBLE);
                        et_book_appoinment.setText(model.getPrice());

                        if (model.isIs_selected()) {
                            et_book_appoinment.setEnabled(true);
                        } else {
                            et_book_appoinment.setEnabled(false);
                        }
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
//                dialog.dismiss();
                rel_progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.getSetting);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("type", shared.getUsertype());
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                String param = "{\"action\":\"getcourt\"}";
                params.put("json", param);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean isChecked) {
        if (v.getId() == R.id.cb_chat) {
            if (isChecked) {
                c_chat = cb_chat.getText().toString();
                isSelectChat = true;
                e_chat_price = et_chat.getText().toString();
                et_chat.setEnabled(true);


            } else {
                isSelectChat = false;
                et_chat.setEnabled(false);
            }

        } else if (v.getId() == R.id.cb_voice_call) {
            if (isChecked) {
                c_call = cb_voice_call.getText().toString();
                isSelectCall = true;
                e_call_price = et_voice_call.getText().toString();
                et_voice_call.setEnabled(true);

            } else {
                isSelectCall = false;
                et_voice_call.setEnabled(false);

            }

        } else if (v.getId() == R.id.cb_book_appoinment) {

            if (isChecked) {
                C_appoinment = cb_book_appoinment.getText().toString();
                IsSelectAppoinment = true;
                e_appoinment_price = et_book_appoinment.getText().toString();
                et_book_appoinment.setEnabled(true);

            } else {
                IsSelectAppoinment = false;
                et_book_appoinment.setEnabled(false);
            }

        } else if (v.getId() == R.id.cb_video_call) {

            if (isChecked) {
                C_video_call = cb_video_call.getText().toString();
                IsSelectVideoCall = true;
                e_video_call_price = et_video_call.getText().toString();
                et_video_call.setEnabled(true);

            } else {
                IsSelectVideoCall = false;
                et_video_call.setEnabled(false);
            }
        }
    }

    private void getModifyCom() {
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
                    Toaster.getToast(context, msg);
                    if (status.contentEquals("1")) {
                        rel_progress.setVisibility(View.GONE);
                        JSONObject dataobj = object.getJSONObject("data");
                        JSONObject comunication = dataobj.getJSONObject("communication");
                        shared.putString(Config.Communication, comunication.toString());

                        CommunicationModel model = new CommunicationModel();
                        JSONObject chat = comunication.getJSONObject("chat");
                        model.setCommunication_id(chat.getString("communication_id"));
                        model.setCommunication_type_id(chat.getString("communication_type_id"));
                        model.setCommunication_type(chat.getString("communication_type"));
                        model.setPrice(chat.getString("price"));
                        model.setPrice_type_id(chat.getString("price_type_id"));
                        model.setPrice_type(chat.getString("price_type"));
                        model.setOp_expression(chat.getString("op_expression"));
                        model.setIs_multiply_diff(chat.getString("is_multiply_diff"));
                        model.setIs_selected(chat.getBoolean("is_selected"));

                        com_chat_id = chat.getString("communication_id");
                        chat_com_type_id = chat.getString("communication_type_id");

                        cb_chat.setText(model.getCommunication_type());
                        cb_chat.setChecked(model.isIs_selected());
                        et_chat.setVisibility(View.VISIBLE);
                        et_chat.setText(model.getPrice());

                        if (model.isIs_selected()) {
                            et_chat.setEnabled(true);
                        } else {
                            et_chat.setEnabled(false);
                        }

                        JSONObject call = comunication.getJSONObject("call");
                        model.setCommunication_id(call.getString("communication_id"));
                        model.setCommunication_type_id(call.getString("communication_type_id"));
                        model.setCommunication_type(call.getString("communication_type"));
                        model.setPrice(call.getString("price"));
                        model.setPrice_type_id(call.getString("price_type_id"));
                        model.setPrice_type(call.getString("price_type"));
                        model.setOp_expression(call.getString("op_expression"));
                        model.setIs_multiply_diff(call.getString("is_multiply_diff"));
                        model.setIs_selected(call.getBoolean("is_selected"));


                        com_call_id = call.getString("communication_id");
                        call_com_type_id = call.getString("communication_type_id");
                        cb_voice_call.setText(model.getCommunication_type());
                        cb_voice_call.setChecked(model.isIs_selected());
                        et_voice_call.setVisibility(View.VISIBLE);
                        et_voice_call.setText(model.getPrice());


                        if (model.isIs_selected()) {
                            et_voice_call.setEnabled(true);
                        } else {
                            et_voice_call.setEnabled(false);
                        }

                        JSONObject video = comunication.getJSONObject("video");
                        model.setCommunication_id(video.getString("communication_id"));
                        model.setCommunication_type_id(video.getString("communication_type_id"));
                        model.setCommunication_type(video.getString("communication_type"));
                        model.setPrice(video.getString("price"));
                        model.setPrice_type_id(video.getString("price_type_id"));
                        model.setPrice_type(video.getString("price_type"));
                        model.setOp_expression(video.getString("op_expression"));
                        model.setIs_multiply_diff(video.getString("is_multiply_diff"));
                        model.setIs_selected(video.getBoolean("is_selected"));

                        com_video_id = video.getString("communication_id");
                        video_com_type_id = video.getString("communication_type_id");

                        cb_video_call.setText(model.getCommunication_type());
                        cb_video_call.setChecked(model.isIs_selected());
                        et_video_call.setVisibility(View.VISIBLE);
                        et_video_call.setText(model.getPrice());


                        if (model.isIs_selected()) {
                            et_video_call.setEnabled(true);
                        } else {
                            et_video_call.setEnabled(false);
                        }

                        JSONObject appointment = comunication.getJSONObject("appointment");
                        model.setCommunication_id(appointment.getString("communication_id"));
                        model.setCommunication_type_id(appointment.getString("communication_type_id"));
                        model.setCommunication_type(appointment.getString("communication_type"));
                        model.setPrice(appointment.getString("price"));
                        model.setPrice_type_id(appointment.getString("price_type_id"));
                        model.setPrice_type(appointment.getString("price_type"));
                        model.setOp_expression(appointment.getString("op_expression"));
                        model.setIs_multiply_diff(appointment.getString("is_multiply_diff"));
                        model.setIs_selected(appointment.getBoolean("is_selected"));

                        com_appointment_id = appointment.getString("communication_id");
                        appointment_com_type_id = appointment.getString("communication_type_id");

                        cb_book_appoinment.setText(model.getCommunication_type());
                        cb_book_appoinment.setChecked(model.isIs_selected());
                        et_book_appoinment.setVisibility(View.VISIBLE);
                        et_book_appoinment.setText(model.getPrice());

                        if (model.isIs_selected()) {
                            et_book_appoinment.setEnabled(true);
                        } else {
                            et_book_appoinment.setEnabled(false);
                        }

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
//                dialog.dismiss();
                rel_progress.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.modifyCommunicationDetail);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    JSONObject communication = new JSONObject();
                    body.put("communication", communication);
                    JSONObject call = new JSONObject();
                    call.put("communication_id", com_call_id);
                    call.put("communication_type_id", "1");
                    call.put("communication_type", "Call");
                    call.put("price", et_voice_call.getText().toString());
                    call.put("price_type_id", "2");
                    call.put("price_type", "PerHour");
                    call.put("op_expression", "");
                    call.put("is_multiply_diff", "Y");
                    call.put("is_selected", isSelectCall);
                    communication.put("call", call);

                    JSONObject chat = new JSONObject();
                    chat.put("communication_id", com_chat_id);
                    chat.put("communication_type_id", "2");
                    chat.put("communication_type", "chat");
                    chat.put("price", et_chat.getText().toString());
                    chat.put("price_type_id", "2");
                    chat.put("price_type", "PerHour");
                    chat.put("op_expression", "");
                    chat.put("is_multiply_diff", "Y");
                    chat.put("is_selected", isSelectChat);
                    communication.put("chat", chat);

                    JSONObject video = new JSONObject();
                    video.put("communication_id", com_video_id);
                    video.put("communication_type_id", "3");
                    video.put("communication_type", "Video Call");
                    video.put("price", et_video_call.getText().toString());
                    video.put("price_type_id", "2");
                    video.put("price_type", "PerHour");
                    video.put("op_expression", "");
                    video.put("is_multiply_diff", "Y");
                    video.put("is_selected", IsSelectVideoCall);
                    communication.put("video", video);

                    JSONObject appointment = new JSONObject();
                    appointment.put("communication_id", com_appointment_id);
                    appointment.put("communication_type_id", "4");
                    appointment.put("communication_type", "Appointment");
                    appointment.put("price", et_book_appoinment.getText().toString());
                    appointment.put("price_type_id", "2");
                    appointment.put("price_type", "PerHour");
                    appointment.put("op_expression", "");
                    appointment.put("is_multiply_diff", "Y");
                    appointment.put("is_selected", IsSelectAppoinment);
                    communication.put("appointment", appointment);
                    main.put("body", body);
                    param = main.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


//                String param = "{\"action\":\"getcourt\"}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}

