package com.multipz.atmiyalawlab.User;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
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
import com.bumptech.glide.Glide;
import com.multipz.atmiyalawlab.Activity.ActivityChatWithUser;
import com.multipz.atmiyalawlab.Activity.ActivityDashboard;
import com.multipz.atmiyalawlab.Activity.ActivityLawyerProfile;
import com.multipz.atmiyalawlab.Activity.ActivityLawyerSetting;
import com.multipz.atmiyalawlab.Activity.ActivityMyAppointmentList;
import com.multipz.atmiyalawlab.Activity.ActivityNotification;
import com.multipz.atmiyalawlab.Activity.ActivityPolicy;
import com.multipz.atmiyalawlab.Activity.ActivityPolicyAndTermCondition;
import com.multipz.atmiyalawlab.Activity.ActivitySelectUser;
import com.multipz.atmiyalawlab.Fragment.Dashboard;
import com.multipz.atmiyalawlab.Fragment.MyAccountUserFragment;
import com.multipz.atmiyalawlab.Fragment.UserDashboard;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.ErrorMessage;
import com.multipz.atmiyalawlab.Util.MyAsyncTask;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;
import com.multipz.atmiyalawlab.VideoCalling.BaseActivity;
import com.multipz.atmiyalawlab.VideoCalling.SinchService;
import com.sinch.android.rtc.SinchError;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityDashboardUser extends BaseActivity implements SinchService.StartFailedListener {
    FragmentTransaction transaction;
    private CircleImageView img_registration;
    private TextView txt_full_name, txt_email, txtlogout, txt_name;
    private LinearLayout lnr_home, lnr_my_Account, lnr_find_lawyers, lnr_bookmark, lnr_pasted_cases, lnr_chat, lnr_about_us, lnr_policy, lnr_help, lnr_helandFAQ, lnr_myAppointment, lnr_call_list, lnr_question;
    private Shared shared;
    private Context context;
    private String param = "";
    private DrawerLayout drawer;
    private ImageView img_setting, img_edit;
    private RelativeLayout img_notification, btn_logout;
    private int exitDashboardfragment = 0;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);
        context = this;
        shared = new Shared(context);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


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
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else {
                    drawer.openDrawer(GravityCompat.START);
                }
            }
        });
        reference();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        // navigationView.setNavigationItemSelectedListener(this);

        setToolbarName(getResources().getString(R.string.dashboard));
        drawer.closeDrawer(GravityCompat.START);
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.containerUser, new UserDashboard()).commit();
        lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drawer.closeDrawer(GravityCompat.START);
                transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.container, new UserDashboard()).commit();
                exitDashboardfragment = 1;
            }
        });


        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                popUpAlertLogout();
            }
        });

        lnr_find_lawyers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboardUser.this, ActivitySearchLawyer.class);
                startActivity(intent);
            }
        });

        lnr_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboardUser.this, ActivityBookMark.class);
                startActivity(intent);
            }
        });

        img_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityDashboardUser.this, ActivityUserSetting.class);
                startActivity(intent);

            }
        });

        img_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityDashboardUser.this, ActivityNotification.class);
                startActivity(intent);
            }
        });

        lnr_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboardUser.this, ActivityChatWithUser.class);
                startActivity(intent);
            }
        });

        lnr_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboardUser.this, ActivityPolicyAndTermCondition.class);
                startActivity(intent);
            }
        });

        lnr_about_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboardUser.this, ActivityPolicy.class);
                intent.putExtra("policyID", 2);
                startActivity(intent);

            }
        });

        lnr_helandFAQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboardUser.this, ActivityPolicy.class);
                intent.putExtra("policyID", 3);
                startActivity(intent);

            }
        });
        lnr_call_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboardUser.this, ActivityCallList.class);
                startActivity(intent);
            }
        });
        img_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboardUser.this, ActivityLawyerProfile.class);
                startActivity(intent);

            }
        });

        lnr_pasted_cases.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboardUser.this, ActivityPostedCaseList.class);
                startActivity(intent);
            }
        });

        lnr_my_Account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setToolbarName(getResources().getString(R.string.my_account));
                openFragment(new MyAccountUserFragment(), getResources().getString(R.string.my_account));
                exitDashboardfragment = 2;
            }
        });
        lnr_myAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboardUser.this, ActivityMyAppointmentList.class);
                startActivity(intent);
            }
        });
        lnr_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openFragment(new UserDashboard(), getResources().getString(R.string.dashboard));
            }
        });
        lnr_question.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                Intent intent = new Intent(ActivityDashboardUser.this, ActivityAskQuestionView.class);
                startActivity(intent);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE}, 100);
        }
        getUserData();

    }

    public void setToolbarName(String name) {
        txt_name.setText(name);
    }

    void reference() {
        img_setting = (ImageView) findViewById(R.id.img_setting);
        img_notification = (RelativeLayout) findViewById(R.id.img_notification);
        img_registration = (CircleImageView) findViewById(R.id.img_registration);
        txt_full_name = (TextView) findViewById(R.id.txt_full_name);
        txt_email = (TextView) findViewById(R.id.txt_email);
        txtlogout = (TextView) findViewById(R.id.txtlogout);
        lnr_home = (LinearLayout) findViewById(R.id.lnr_home);
        lnr_my_Account = (LinearLayout) findViewById(R.id.lnr_my_Account);
        lnr_find_lawyers = (LinearLayout) findViewById(R.id.lnr_find_lawyers);
        lnr_bookmark = (LinearLayout) findViewById(R.id.lnr_bookmark);
        lnr_pasted_cases = (LinearLayout) findViewById(R.id.lnr_pasted_cases);
        lnr_chat = (LinearLayout) findViewById(R.id.lnr_chat);
        lnr_helandFAQ = (LinearLayout) findViewById(R.id.lnr_helandFAQ);
        lnr_about_us = (LinearLayout) findViewById(R.id.lnr_about_us);
        lnr_policy = (LinearLayout) findViewById(R.id.lnr_policy);
        lnr_call_list = (LinearLayout) findViewById(R.id.lnr_call_list);
        lnr_question = (LinearLayout) findViewById(R.id.lnr_question);
        lnr_help = (LinearLayout) findViewById(R.id.lnr_help);
        img_edit = (ImageView) findViewById(R.id.img_edit);
        txt_name = (TextView) findViewById(R.id.txt_name);
        lnr_myAppointment = (LinearLayout) findViewById(R.id.lnr_myAppointment);
        btn_logout = (RelativeLayout) findViewById(R.id.btn_logout);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        Application.setFontDefault((DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (exitDashboardfragment == 2) {
                openFragment(new UserDashboard(), getResources().getString(R.string.dashboard));
                exitDashboardfragment = 0;
            } else {
                exitDashboardfragment = 0;
                popupDialogExitApporNot();
            }


        }
    }

    void popupDialogExitApporNot() {

        LayoutInflater inflater = ActivityDashboardUser.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.popup_assign_case, null);
        TextView txt_des = (TextView) c.findViewById(R.id.head_desc);
        TextView txt_Ok = (TextView) c.findViewById(R.id.txt_Ok);
        TextView txt_Cancel = (TextView) c.findViewById(R.id.txt_Cancel);

        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(ActivityDashboardUser.this);
        builder.setView(c);

        final android.support.v7.app.AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        txt_des.setText(ErrorMessage.popupExit);

        txt_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Constant_method.checkConn(context)) {
                    ActivityDashboardUser.this.finish();
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
        //rel_progress.setVisibility(View.VISIBLE);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // rel_progress.setVisibility(View.GONE);

                String msg = "", status = "";
                try {
                    JSONObject object = new JSONObject(response);
                    status = object.getString("status");
                    JSONObject o = object.getJSONObject("body");
                    msg = o.getString("msg");
                    if (status.contentEquals("1")) {
                        shared.setlogin(false);
                        shared.logout();
                        Intent intent = new Intent(ActivityDashboardUser.this, ActivitySelectUser.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        getSinchServiceInterface().stopClient();
                    } else if (status.contentEquals("0")) {
                        //  rel_progress.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // rel_progress.setVisibility(View.GONE);
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
//                String param = "{\"action\":\"" + Config.getcountry + "\"}";
                params.put("json", param);
                return params;
            }
        };

        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    public void popUpAlertLogout() {
        LayoutInflater inflater = ActivityDashboardUser.this.getLayoutInflater();
        View c = inflater.inflate(R.layout.logout_dialog, null);

        TextView txt_Cancel = (TextView) c.findViewById(R.id.txt_Cancel);
        TextView txt_Ok = (TextView) c.findViewById(R.id.txt_Ok);
        AlertDialog.Builder builder = new AlertDialog.Builder(ActivityDashboardUser.this);
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

    @Override
    protected void onResume() {
        super.onResume();
        getUserData();
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
                    Glide.with(context).load(R.drawable.user).into(img_registration);
                } else {
                    Glide.with(context).load(profile_img).into(img_registration);
                }
                txt_full_name.setText(f_name);
                txt_email.setText(email);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    void openFragment(Fragment fragment, String title) {
        setToolbarName(title);
        try {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.containerUser, fragment).commit();
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
