package com.multipz.atmiyalawlab.User;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.multipz.atmiyalawlab.Activity.ActivityAudioCalling;
import com.multipz.atmiyalawlab.Activity.ActivitySinchAudioCalling;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.VideoCalling.BaseActivity;
import com.multipz.atmiyalawlab.VideoCalling.CallScreenActivity;
import com.multipz.atmiyalawlab.VideoCalling.SinchService;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivityCallProfile extends BaseActivity implements SinchService.StartFailedListener {

    private ImageView img_back;
    private TextView txtname;
    private CircleImageView img_call_profile;
    private RelativeLayout btnMakeCall, btnMakeVideoCall;
    private String lawyer_id = "", lawyer_name = "", lawyer_profile = "";
    private String communication_type_id = "";
    private Context context;
    private Shared shared;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_profile);
        context = this;
        shared = new Shared(context);
        reference();
        init();


    }

    void reference() {
        img_back = (ImageView) findViewById(R.id.img_back);
        txtname = (TextView) findViewById(R.id.txtname);
        img_call_profile = (CircleImageView) findViewById(R.id.img_call_profile);
        btnMakeCall = (RelativeLayout) findViewById(R.id.btnMakeCall);
        btnMakeVideoCall = (RelativeLayout) findViewById(R.id.btnMakeVideoCall);
    }

    void init() {
        lawyer_id = getIntent().getStringExtra("lawyer_id");
        lawyer_name = getIntent().getStringExtra("lawyer_name");
        lawyer_profile = getIntent().getStringExtra("lawyer_profile");
        communication_type_id = getIntent().getStringExtra("communication_type_id");
        // shared.setCommunication_type_id(Integer.parseInt(communication_type_id));

        txtname.setText(lawyer_name);
        Picasso.with(context).load(lawyer_profile).into(img_call_profile);

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        btnMakeCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (shared.getCommunication_type_id() == 1) {
                    Intent i = new Intent(ActivityCallProfile.this, ActivityAudioCalling.class);
                    i.putExtra("lawyer_id", lawyer_id + "***" + lawyer_name);
                    i.putExtra("communication_type_id", shared.getCommunication_type_id());
                    i.putExtra("fname", lawyer_name);
                    startActivity(i);
                } else if (shared.getCommunication_type_id() == 3) {
                    Call call = getSinchServiceInterface().callUserVideo(lawyer_id + "***" + lawyer_name);
                    String callId = call.getCallId();
                    //String callId = call.getRemoteUserId();
                    Intent callScreen = new Intent(ActivityCallProfile.this, CallScreenActivity.class);
                    callScreen.putExtra("lawyer_id", lawyer_id + "***" + lawyer_name);
                    callScreen.putExtra("communication_type_id", shared.getCommunication_type_id());
                    callScreen.putExtra("lawyer_name", lawyer_name);
                    callScreen.putExtra(SinchService.CALL_ID, callId);
                    startActivity(callScreen);
                }
            }
        });


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
        //    Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStarted() {

    }

    @Override
    public void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
    }
}
