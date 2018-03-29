package com.multipz.atmiyalawlab.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.google.gson.Gson;
import com.multipz.atmiyalawlab.Adapter.CourtAdapter;
import com.multipz.atmiyalawlab.Adapter.DiaryAdapter;
import com.multipz.atmiyalawlab.Adapter.SeeallNewsAdapter;
import com.multipz.atmiyalawlab.Model.DiaryModel;
import com.multipz.atmiyalawlab.Model.ModelCourt;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.HideSoftKeyboard;
import com.multipz.atmiyalawlab.Util.ItemClickListener;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class ActivityAdvocateDiaryDetail extends AppCompatActivity implements ItemClickListener {
    private ImageView img_back, img_setting;
    private RelativeLayout img_notification, rel_root, rel_progress;

    private RecyclerView advodairylist;
    private FloatingActionButton fabAddAdvoDairy;
    private ProgressDialog dialog;
    private TextView txtMonthTitle;
    String param;
    // DiaryAdapter adapter;
    // ArrayList<DiaryModel> list;
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMMM- yyyy", Locale.getDefault());
    Context context;
    private Shared shared;
    private DiaryAdapter adapter;
    private CompactCalendarView dairy_Calender;
    private ArrayList<DiaryModel> list;
    private CircularProgressView progressBar1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advocate_diary_detail);

        context = this;
        shared = new Shared(context);
        reference();
        init();
    }

    private void init() {
        txtMonthTitle.setText(dateFormatForMonth.format(dairy_Calender.getFirstDayOfCurrentMonth()));
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        fabAddAdvoDairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ActivityAdvocateDiaryDetail.this, ActivityAddTaskDairy.class));
            }
        });

        dairy_Calender.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                String strDateFormat = "yyyy-MM-dd";
                SimpleDateFormat sdf = new SimpleDateFormat(strDateFormat);
                String dairydate = sdf.format(dateClicked);
                if (Constant_method.checkConn(context)) {
                    getDairyNote(dairydate);
                } else {
                    Toaster.getToast(context, ErrorMessage.NoInternet);
                }

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                txtMonthTitle.setText(dateFormatForMonth.format(firstDayOfNewMonth));

            }
        });
    }

    private void reference() {
        list = new ArrayList<>();
        img_back = (ImageView) findViewById(R.id.img_back);
        advodairylist = (RecyclerView) findViewById(R.id.advodairylist);
        fabAddAdvoDairy = (FloatingActionButton) findViewById(R.id.fabAddAdvoDairy);
        txtMonthTitle = (TextView) findViewById(R.id.txtMonthTitle);
        dairy_Calender = (CompactCalendarView) findViewById(R.id.dairy_Calender);
        dairy_Calender.setUseThreeLetterAbbreviation(true);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        progressBar1 = (CircularProgressView) findViewById(R.id.progressBar1);
        rel_progress = (RelativeLayout) findViewById(R.id.rel_progress);
        progressBar1.startAnimation();


        Application.setFontDefault((RelativeLayout) findViewById(R.id.rel_root));
        HideSoftKeyboard.setupUI((RelativeLayout) findViewById(R.id.rel_root), ActivityAdvocateDiaryDetail.this);

        gotoToday();

    }

    public void gotoToday() {
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String today = fmt.format(date);

        if (Constant_method.checkConn(context)) {
            getDairyNote(today);
        } else {
            Toaster.getToast(context, ErrorMessage.NoInternet);
        }

        dairy_Calender.setCurrentDate(Calendar.getInstance(Locale.getDefault()).getTime());

    }

    private void getDairyNote(final String dated) {
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
                    list.clear();
                    if (status.contentEquals("1")) {
                        rel_progress.setVisibility(View.GONE);
                        JSONArray data = object.getJSONArray("data");
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            DiaryModel model = new DiaryModel();
                            model.setAh_advocate_dairy_id(obj.getString("ah_advocate_dairy_id"));
                            model.setName(obj.getString("name"));
                            model.setDescription(obj.getString("description"));
                            model.setDate(obj.getString("date"));
                            model.setTime(obj.getString("time"));
                            list.add(model);

                        }
                        adapter = new DiaryAdapter(list, context);
                        RecyclerView.LayoutManager mLayoutManagers = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        advodairylist.setLayoutManager(mLayoutManagers);
                        advodairylist.setItemAnimator(new DefaultItemAnimator());
                        advodairylist.setAdapter(adapter);
                        advodairylist.setNestedScrollingEnabled(false);
                        adapter.setClickListener(ActivityAdvocateDiaryDetail.this);

                    } else {
                        rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(getApplicationContext(), msg);
                        adapter = new DiaryAdapter(list, context);
                        RecyclerView.LayoutManager mLayoutManagers = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                        advodairylist.setLayoutManager(mLayoutManagers);
                        advodairylist.setItemAnimator(new DefaultItemAnimator());
                        advodairylist.setAdapter(adapter);
                        advodairylist.setNestedScrollingEnabled(false);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar1.setVisibility(View.GONE);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.getDairyData);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("date", dated);
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
    public void itemClicked(View view, int position) {

        DiaryModel model = list.get(position);
        if (view.getId() == R.id.viewDairyDetail) {
            Intent i = new Intent(ActivityAdvocateDiaryDetail.this, ActivityAddTaskDairy.class);
            i.putExtra("upadateDaity", true);
            i.putExtra("ah_advocate_dairy_id", model.getAh_advocate_dairy_id());
            i.putExtra("name", model.getName());
            i.putExtra("des", model.getDescription());
            i.putExtra("date", model.getDate());
            i.putExtra("time", model.getTime());
            startActivity(i);
            //  PopupViewDairyData(model.getName(), model.getDescription());
        }
    }


    private void PopupViewDairyData(String title, String description) {

        LayoutInflater inflater = ActivityAdvocateDiaryDetail.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_dairy_description, null);

        TextView txtDairyTitle = (TextView) c.findViewById(R.id.txtDairyTitle);
        ImageView img_close = (ImageView) c.findViewById(R.id.img_close);
        TextView txtDescription = (TextView) c.findViewById(R.id.txtDescription);
        LinearLayout rel_root = (LinearLayout) c.findViewById(R.id.rel_root);
        Application.setFontDefault((LinearLayout) c.findViewById(R.id.rel_root));
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityAdvocateDiaryDetail.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txtDairyTitle.setText(title);
        txtDescription.setText(description);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }
}

