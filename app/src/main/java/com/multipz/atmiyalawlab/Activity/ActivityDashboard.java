package com.multipz.atmiyalawlab.Activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.multipz.atmiyalawlab.Fragment.Dashboard;
import com.multipz.atmiyalawlab.Fragment.MyAccount;
import com.multipz.atmiyalawlab.LawyerFragment.LawyerFeedbackFragment;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;
import com.multipz.atmiyalawlab.VideoCalling.BaseActivity;
import com.multipz.atmiyalawlab.VideoCalling.SinchService;
import com.sinch.android.rtc.SinchError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityDashboard extends BaseActivity implements SinchService.StartFailedListener {
    FragmentTransaction transaction;
    TextView txtlogout, txt_full_name, txt_email;
    Context context;
    Shared shared;
    private LinearLayout lnr_home, lnr_my_Account, lnr_chat, lnr_cases, lnr_feedback, lnr_about_us, lnr_policy, lnr_help, lnr_helandFAQ, lnr_my_appointment;
    DrawerLayout drawer;
    ImageView img_edit;
    CircleImageView img_registration;
    String img, name, email;
    public static TextView txt_name_title_lawyer;
    ImageView img_setting;
    public static TextView txtExpire;
    public static RelativeLayout rel_toolbar, myaccount, img_notification, rel_progress;
    public static TextView txt_renew;
    private String param = "";
    private int policyID = 0;
    private int exitDashboardfragment = 0;
    private CircularProgressView progressBar1;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context = this;
        shared = new Shared(context);

        Init();
        ref();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
        toggle.setDrawerIndicatorEnabled(false);

        toggle.setHomeAsUpIndicator(R.drawable.ic_menu_new);
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //navigationView.setNavigationItemSelectedListener(this);

        drawer.closeDrawer(GravityCompat.START);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, new Dashboard()).commit();

        setToolbarName(getResources().getString(R.string.dashboard));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE}, 100);
        }
    }


    private void ref() {
        txtlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                popUpAlertLogout();


            }
        });
        txtExpire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToolbarName(getResources().getString(R.string.my_account));
                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new MyAccount()).commit();
                exitDashboardfragment = 2;
            }
        });


        lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToolbarName(getResources().getString(R.string.dashboard));
                drawer.closeDrawer(GravityCompat.START);
                myaccount.setVisibility(View.GONE);
                rel_toolbar.setVisibility(View.VISIBLE);

                openFragment(new Dashboard(), getResources().getString(R.string.dashboard));
                /*transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new Dashboard()).commit();*/
                exitDashboardfragment = 1;
            }
        });
        lnr_my_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToolbarName(getResources().getString(R.string.my_account));
                txt_renew.setVisibility(View.GONE);
                drawer.closeDrawer(GravityCompat.START);
                myaccount.setVisibility(View.VISIBLE);
                rel_toolbar.setVisibility(View.GONE);
                openFragment(new MyAccount(), getResources().getString(R.string.my_account));
                /*transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new MyAccount()).commit();*/
                exitDashboardfragment = 2;

            }
        });
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboard.this, ActivityLawyerProfile.class);
                startActivity(intent);

            }
        });
        lnr_cases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboard.this, ActivityCasesList.class);
                startActivity(intent);
//                openFragment(new LawyercasesFragment(),"Cases");
            }
        });

        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityDashboard.this, ActivityLawyerSetting.class);
                startActivity(intent);
            }
        });
        lnr_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboard.this, ActivityChatWithUser.class);
                startActivity(intent);

            }
        });

        lnr_feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
//                Intent intent = new Intent(ActivityDashboard.this, ActivityFeedbackLidt.class);
//                startActivity(intent);
                myaccount.setVisibility(View.GONE);
                rel_toolbar.setVisibility(View.VISIBLE);
                openFragment(new LawyerFeedbackFragment(), getResources().getString(R.string.feedback));
                exitDashboardfragment = 3;

            }
        });
        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityDashboard.this, ActivityNotification.class);
                startActivity(intent);
            }
        });
        lnr_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboard.this, ActivityPolicyAndTermCondition.class);
                startActivity(intent);
            }
        });

        lnr_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboard.this, ActivityPolicy.class);
                intent.putExtra("policyID", 2);
                startActivity(intent);

            }
        });

        lnr_helandFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboard.this, ActivityPolicy.class);
                intent.putExtra("policyID", 3);
                startActivity(intent);

            }
        });
        lnr_my_appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboard.this, ActivityMyAppointmentListlawyerSide.class);
                startActivity(intent);
            }
        });
       /* lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new Dashboard(), getResources().getString(R.string.dashboard));
            }
        });*/
    }

    public void setToolbarName(String name) {
        txt_name_title_lawyer.setText(name);

    }

    private void Init() {

        rel_toolbar = (RelativeLayout) findViewById(R.id.rel_toolbar);
        myaccount = (RelativeLayout) findViewById(R.id.myaccount);
        txtExpire = (TextView) findViewById(R.id.txtExpire);
        txt_renew = (TextView) findViewById(R.id.txt_renew);
        txtlogout = (TextView) findViewById(R.id.txtlogout);
        txt_name_title_lawyer = (TextView) findViewById(R.id.txt_name_title_lawyer);
        txt_email = (TextView) findViewById(R.id.txt_email);
        lnr_home = (LinearLayout) findViewById(R.id.lnr_home);
        lnr_my_Account = (LinearLayout) findViewById(R.id.lnr_my_Account);
        lnr_chat = (LinearLayout) findViewById(R.id.lnr_chat);
        lnr_cases = (LinearLayout) findViewById(R.id.lnr_cases);
        lnr_feedback = (LinearLayout) findViewById(R.id.lnr_feedback);
        lnr_about_us = (LinearLayout) findViewById(R.id.lnr_about_us);
        lnr_policy = (LinearLayout) findViewById(R.id.lnr_policy);
        lnr_help = (LinearLayout) findViewById(R.id.lnr_help);

        img_edit = (ImageView) findViewById(R.id.img_edit);
        txt_full_name = (TextView) findViewById(R.id.txt_full_name);
        img_registration = (CircleImageView) findViewById(R.id.img_registration);
        img_setting = (ImageView) findViewById(R.id.img_setting);
        img_notification = (RelativeLayout) findViewById(R.id.img_notification);
        lnr_helandFAQ = (LinearLayout) findViewById(R.id.lnr_helandFAQ);
        lnr_my_appointment = (LinearLayout) findViewById(R.id.lnr_my_appointment);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Application.setFontDefault(((DrawerLayout) findViewById(R.id.drawer_layout)));

        getUserData();

    }

    @Override
    public void onBackPressed() {

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            popupExit();
        }
    }

    void popupExit() {
        if (exitDashboardfragment == 2) {
            openFragment(new Dashboard(), getResources().getString(R.string.dashboard));
            myaccount.setVisibility(View.GONE);
            rel_toolbar.setVisibility(View.VISIBLE);
            exitDashboardfragment = 0;
        } else if (exitDashboardfragment == 3) {
            openFragment(new Dashboard(), getResources().getString(R.string.dashboard));
            exitDashboardfragment = 0;
        } else {
            exitDashboardfragment = 0;
            popupDialogExitApporNot();

        }
    }

    void popupDialogExitApporNot() {

        LayoutInflater inflater = ActivityDashboard.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_assign_case, null);
        TextView txt_des = (TextView) c.findViewById(R.id.head_desc);
        TextView txt_Ok = (TextView) c.findViewById(R.id.txt_Ok);
        TextView txt_Cancel = (TextView) c.findViewById(R.id.txt_Cancel);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityDashboard.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        txt_des.setText(ErrorMessage.popupExit);

        txt_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant_method.checkConn(context)) {
                    ActivityDashboard.this.finish();
                    dialog.dismiss();
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

    @Override
    protected void onResume() {
        super.onResume();
        //  getUserData();
    }

    private void getUserData() {
        try {
            JSONArray jsonArray = new JSONArray(shared.getString(Config.Logindata, "[{}]"));
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object1 = jsonArray.getJSONObject(i);
                String m_no = object1.getString("mobile_number");
                String f_name = object1.getString("name");
                String email = object1.getString("email");
                String profile_img = object1.getString("profile_img");
                if (profile_img.contentEquals("")) {
                    Picasso.with(context).load(R.drawable.user).into(img_registration);
                } else {
                    Picasso.with(context).load(profile_img).into(img_registration);
                }
                txt_full_name.setText(f_name);
                txt_email.setText(email);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void popUpAlertLogout() {
        LayoutInflater inflater = ActivityDashboard.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.logout_dialog, null);

        TextView txt_Cancel = (TextView) c.findViewById(R.id.txt_Cancel);
        TextView txt_Ok = (TextView) c.findViewById(R.id.txt_Ok);
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDashboard.this);
        builder.setView(c);

        final AlertDialog dialog = builder.create();
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        txt_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant_method.checkConn(context)) {
                    Logout();
                    dialog.dismiss();
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

    private void Logout() {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
        // rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  rel_progress.setVisibility(View.GONE);
                String msg = "", status = "";
                try {
                    JSONObject object = new JSONObject(response);
                    status = object.getString("status");
                    JSONObject o = object.getJSONObject("body");
                    msg = o.getString("msg");
                    if (status.contentEquals("1")) {
                        shared.setlogin(false);
                        shared.setIsSubsciribe(false);
                        shared.logout();
                        Intent intent = new Intent(ActivityDashboard.this, ActivitySelectUser.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getSinchServiceInterface().stopClient();
                        FirebaseInstanceId.getInstance().deleteInstanceId();
                    } else if (status.contentEquals("0")) {
                        //   rel_progress.setVisibility(View.GONE);
                        Toaster.getToast(getApplicationContext(), msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // rel_progress.setVisibility(View.GONE);
                Log.d("Error", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                try {
                    JSONObject object = new JSONObject();
                    object.put("action", Config.logout);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    object.put("body", body);
                    param = object.toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }


    void openFragment(Fragment fragment, String title) {
        setToolbarName(title);
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, fragment).commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onServiceConnected() {
        if (!getSinchServiceInterface().isStarted()) {
            getSinchServiceInterface().startClient(shared.getUserId() + "***" + shared.getUserName());
        }
        getSinchServiceInterface().setStartListener(this);
    }

    @Override
    public void onStartFailed(SinchError error) {
        Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    //Invoked when just after the service is connected with Sinch
    @Override
    public void onStarted() {
        //do what you want after success since service start.
    }
}
