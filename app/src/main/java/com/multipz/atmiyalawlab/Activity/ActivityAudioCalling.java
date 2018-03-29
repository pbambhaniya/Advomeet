package com.multipz.atmiyalawlab.Activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.multipz.atmiyalawlab.Model.CallListModel;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.Util.AppController;
import com.multipz.atmiyalawlab.Util.Application;
import com.multipz.atmiyalawlab.Util.AudioPlayer;
import com.multipz.atmiyalawlab.Util.Config;
import com.multipz.atmiyalawlab.Util.Constant_method;
import com.multipz.atmiyalawlab.Util.Shared;
import com.multipz.atmiyalawlab.Util.Toaster;
import com.multipz.atmiyalawlab.VideoCalling.BaseActivity;
import com.multipz.atmiyalawlab.VideoCalling.CallScreenActivity;
import com.multipz.atmiyalawlab.VideoCalling.SinchService;
import com.sinch.android.rtc.PushPair;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.calling.CallListener;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ActivityAudioCalling extends BaseActivity {
    private Call call;
    private SinchClient sinchClient;
    private TextView btncall;
    private Button btnAnswer, btnDeline, btn_end_Call;
    private String callerId = "1", lawyer_id, lawyer_name = "", in_Call = "", RemoteUserID = "";
    private String recipientId = "2";
    /*.applicationKey("7ca125a7-3adb-44f8-b50b-726ba05eedc3")
                   .applicationSecret("mlWTSgH4I0a4GaKyXfuctA==")
                   .environmentHost("sandbox.sinch.com")*/
    private AudioPlayer mAudioPlayer;
    Animation myAnim;
    static final String CALL_START_TIME = "callStartTime";
    static final String ADDED_LISTENER = "addedListener";
    private long mCallStart = 0;
    private boolean mAddedListener = false;
    private boolean mVideoViewsAdded = false;
    private LinearLayout lnr_incomming_call, lnr_end_Call;
    private TextView mCallDuration;
    private TextView mCallState;
    private TextView calling_name;
    private int communication_type_id;
    private String profile_img, fname;
    private Context context;
    private Shared shared;
    private Timer mTimer;
    private RelativeLayout rel_root;
    private ActivityAudioCalling.UpdateCallDurationTask mDurationTask;
    private String param = "";


    private class UpdateCallDurationTask extends TimerTask {

        @Override
        public void run() {
            ActivityAudioCalling.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    updateCallDuration();
                }
            });
        }
    }


    private void updateCallDuration() {
        if (mCallStart > 0) {
            mCallDuration.setText(formatTimespan(System.currentTimeMillis() - mCallStart));
        }
    }

    private String formatTimespan(long timespan) {
        long totalSeconds = timespan / 1000;
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }


    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong(CALL_START_TIME, mCallStart);
        savedInstanceState.putBoolean(ADDED_LISTENER, mAddedListener);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mCallStart = savedInstanceState.getLong(CALL_START_TIME);
        mAddedListener = savedInstanceState.getBoolean(ADDED_LISTENER);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                + WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

        setContentView(R.layout.activity_audio_calling);

        if (savedInstanceState == null) {
            mCallStart = System.currentTimeMillis();
        }
        context = this;
        shared = new Shared(context);

        //asking for permissions here
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE}, 100);
        }

        mAudioPlayer = new AudioPlayer(this);
        Intent i = getIntent();
        communication_type_id = i.getIntExtra("communication_type_id", 0);
        profile_img = getIntent().getStringExtra("profile_img");
        fname = getIntent().getStringExtra("fname");
        // lawyer_id = getIntent().getStringExtra("lawyer_id");


        in_Call = getIntent().getStringExtra("in_Call");
        lawyer_id = getIntent().getStringExtra("lawyer_id");
        RemoteUserID = getIntent().getStringExtra("RemoteUserID");
        shared.setAnswercall(RemoteUserID);

        reference();

        sinchClient = Sinch.getSinchClientBuilder()
                .context(this)
                .userId(shared.getUserId() + "***" + shared.getUserName())
                .applicationKey(Config.applicationKey)
                .applicationSecret(Config.applicationSecret)
                .environmentHost(Config.environmentHost)
                .build();

        sinchClient.setSupportCalling(true);
        sinchClient.startListeningOnActiveConnection();
        sinchClient.start();
        // sinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());

        init();

    }

    private void init() {

        if (RemoteUserID != null) {
            mAudioPlayer.playRingtone();
            mAudioPlayer.stopProgressTone();

            String res[] = RemoteUserID.split("\\*\\*\\*");
            String incommingUserName = res[1];
            calling_name.setText(incommingUserName);
        } else {
            calling_name.setText(fname);
        }

        if (in_Call != null && in_Call.contentEquals("AudioCall")) {
            lnr_incomming_call.setVisibility(View.VISIBLE);
        } else {
            lnr_end_Call.setVisibility(View.VISIBLE);
            //MakeCall();
        }


        btnDeline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SinchService.currentAudioCall != null) {
                    call.hangup();
                    mAudioPlayer.stopProgressTone();
                    setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
                } else {
                    mAudioPlayer.stopProgressTone();
                    finish();
                }
            }
        });

        btn_end_Call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (call != null) {
                    call.hangup();
                    mAudioPlayer.stopProgressTone();
                    setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
                } else {
                    mAudioPlayer.stopProgressTone();
                    finish();
                }
            }
        });

        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (SinchService.currentAudioCall != null) {
                    mAudioPlayer.stopRingtone();
                    //call = sinchClient.getCallClient().callUser(shared.getAnswercall());
                    SinchService.currentAudioCall.answer();
                    SinchService.currentAudioCall.addCallListener(new SinchCallListener());
                    lnr_incomming_call.setVisibility(View.GONE);
                } else {
                    SinchService.currentAudioCall.hangup();
                    mAudioPlayer.stopProgressTone();
                }
            }
        });


    }

    private void reference() {
        btnAnswer = (Button) findViewById(R.id.btnAnswer);
        btnDeline = (Button) findViewById(R.id.btnDeline);
        btncall = (TextView) findViewById(R.id.btncall);
        lnr_incomming_call = (LinearLayout) findViewById(R.id.lnr_incomming_call);
        lnr_end_Call = (LinearLayout) findViewById(R.id.lnr_end_Call);
        btn_end_Call = (Button) findViewById(R.id.btn_end_Call);
        mCallDuration = (TextView) findViewById(R.id.mCallDuration);
        calling_name = (TextView) findViewById(R.id.calling_name);
        rel_root = (RelativeLayout) findViewById(R.id.rel_root);
        Application.setFontDefault(rel_root);


    }

    private void MakeCall() {
        if (sinchClient.isStarted()) {
            if (call == null) {
                call = sinchClient.getCallClient().callUser(lawyer_id);
                call.addCallListener(new SinchCallListener());
            } else {
                call.hangup();
                mAudioPlayer.stopProgressTone();
            }
        }
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        if (in_Call != null && in_Call.contentEquals("AudioCall")) {
            lnr_incomming_call.setVisibility(View.VISIBLE);
        } else {
            lnr_end_Call.setVisibility(View.VISIBLE);
            MakeCall();
        }


        //MakeCall();
    }

    @Override
    protected void onDestroy() {
        if (getSinchServiceInterface() != null) {
            getSinchServiceInterface().stopClient();
        }
        super.onDestroy();
    }

    //stop the timer when call is ended
    @Override
    public void onStop() {
        super.onStop();
        mDurationTask.cancel();
        mTimer.cancel();
    }

    //start the timer for the call duration here
    @Override
    public void onStart() {
        super.onStart();
        mTimer = new Timer();
        mDurationTask = new ActivityAudioCalling.UpdateCallDurationTask();
        mTimer.schedule(mDurationTask, 0, 500);
    }


    public class SinchCallListener implements CallListener {
        @Override
        public void onCallEnded(Call endedCall) {
            call = null;
            btncall.setText("Call Dismissed");
            setVolumeControlStream(AudioManager.USE_DEFAULT_STREAM_TYPE);
            mAudioPlayer.stopProgressTone();
            mAudioPlayer.stopRingtone();
            lnr_end_Call.setVisibility(View.VISIBLE);
            finish();

            /*Log.d("call", "" + endedCall.getDetails());
            Log.d("call", "" + endedCall.getState());
            Log.d("call", "" + endedCall.getCallId());
*/
            CommunicationHistory(endedCall);
        }

        @Override
        public void onCallEstablished(Call establishedCall) {
            mCallDuration.setVisibility(View.VISIBLE);
            btncall.setText("connected");
            mAudioPlayer.stopProgressTone();
            mAudioPlayer.stopRingtone();
            lnr_end_Call.setVisibility(View.VISIBLE);
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            mCallStart = System.currentTimeMillis();
            CommunicationHistory(establishedCall);


        }

        @Override
        public void onCallProgressing(Call progressingCall) {
            btncall.setText("Ringing...");
            mAudioPlayer.playProgressTone();
            setVolumeControlStream(AudioManager.STREAM_VOICE_CALL);
            lnr_end_Call.setVisibility(View.VISIBLE);

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
            //call.addCallListener(new SinchCallListener());
            btncall.setText("Hang Up");
            mAudioPlayer.playRingtone();
            mAudioPlayer.stopProgressTone();

           /* Intent i = new Intent(ActivityAudioCalling.this, ActivityAudioCalling.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);*/
        }

    }

    private void CommunicationHistory(final Call establish) {
        String tag_string_req = "string_req";
        String url = Config.BASE_URL;
//        rel_progress.setVisibility(View.VISIBLE);
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
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                //  dialog.hide();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                try {
                    JSONObject main = new JSONObject();
                    main.put("action", Config.communicationHistory);
                    JSONObject body = new JSONObject();
                    body.put("ah_users_id", shared.getUserId());
                    body.put("communication_id", communication_type_id);

                    //long millisecond = Long.parseLong(String.valueOf(establish.getDetails().getStartedTime()));
                    Time sdate = new Time(establish.getDetails().getStartedTime());
                    Time edate = new Time(establish.getDetails().getEndedTime());
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                    String StartTime = formatter.format(sdate);
                    String EndTime = formatter.format(edate);

                    body.put("start_time", StartTime);
                    body.put("end_time", EndTime);
                    body.put("callid", establish.getCallId());
                    body.put("callstate", establish.getState());
                    body.put("callestablishtime", establish.getDetails().getEstablishedTime());
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
