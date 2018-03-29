package com.multipz.atmiyalawlab.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AudioPlayer;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Shared;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActivitySinchAudioCalling extends AppCompatActivity {

    private Call call;
    private SinchClient sinchClient;
    private Button end_call;
    private String callerId = "", lawyer_id = "";
    private AudioPlayer mAudioPlayer;
    private TextView txtstates, txt_profile_name;
    private CircleImageView img_profile;
    private Context context;
    private Shared shared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      /*  getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                +WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                +WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);*/
        setContentView(R.layout.activity_sinch_audio_calling);
        context = this;
        shared = new Shared(context);

        lawyer_id = getIntent().getStringExtra("lawyer_id");

        //asking for permissions here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE}, 100);
        }

        mAudioPlayer = new AudioPlayer(this);
        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(shared.getUserId())
                .applicationKey(Config.applicationKey)
                .applicationSecret(Config.applicationSecret)
                .environmentHost(Config.environmentHost)
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();


        sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        end_call = (Button) findViewById(R.id.end_call);
        txtstates = (TextView) findViewById(R.id.txtstates);
        txt_profile_name = (TextView) findViewById(R.id.txt_profile_name);
        img_profile = (CircleImageView) findViewById(R.id.img_profile);


        end_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (end_call.getText().toString().contentEquals("End call")) {
                    if (call != null) {
                        call.hangup();
                        mAudioPlayer.stopProgressTone();
                        setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
                        finish();
                    } else {
                        finish();
                    }
                } else {
                    if (call == null) {
                        call = sinchClient.getCallClient().callUser(lawyer_id);
                        call.addCallListener(new SinchCallListener());
                        // end_call.setText("Hang Up");
                    } else {
                        call.hangup();
                        mAudioPlayer.stopProgressTone();
                    }

                }


            }
        });

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                button.clearAnimation();
                if (call == null) {
                    call = sinchClient.getCallClient().callUser(recipientId);
                    call.addCallListener(new SinchCallListener());
                    btncall.setText("Hang Up");
                } else {
                    call.hangup();
                    mAudioPlayer.stopProgressTone();
                }
            }
        });*/
        if (sinchClient.isStarted()) {
            Calling();
        }

        ///    Calling();
    }

    private void Calling() {

        if (call == null) {
            call = sinchClient.getCallClient().callUser(lawyer_id);
            call.addCallListener(new SinchCallListener());
            // btncall.setText("Hang Up");
        } else {
            call.hangup();
            mAudioPlayer.stopProgressTone();
        }
    }


    private class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            //   btncall.setText("Call Dismissed");
            txtstates.setText("");

            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            mAudioPlayer.stopProgressTone();
            mAudioPlayer.stopRingtone();
/*            Log.d("Stated Calli Id", endedCall.getCallId());
            Log.d("Get Detail", "" + endedCall.getDetails().getStartedTime());
            Log.d("Remote ID", "" + endedCall.getRemoteUserId());
            Log.d("Get States", "" + endedCall.getState());*/

        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            txtstates.setText("connected");
            mAudioPlayer.stopProgressTone();
            mAudioPlayer.stopRingtone();
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            end_call.setText("End Call");
        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            txtstates.setText("ringing");
            mAudioPlayer.playProgressTone();
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
        }

        @Override
        public void onShouldSendPushNotification(Call call, List<PushPair> pushPairs) {

        }
    }

    private class SinchCallClientListener implements CallClientListener {
        @Override
        public void onIncomingCall(CallClient callClient, Call incomingCall) {
            call = incomingCall;
            call.answer();
            call.addCallListener(new SinchCallListener());
            txtstates.setText("Hang Up");
            mAudioPlayer.playRingtone();
            mAudioPlayer.stopProgressTone();
            Intent i = new Intent(ActivitySinchAudioCalling.this, ActivityAudioCalling.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
        }

    }

}
