package com.multipz.atmiyalawlab.VideoCalling;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.multipz.atmiyalawlab.Activity.ActivityAudioCalling;
import com.multipz.atmiyalawlab.Util.AudioPlayer;
import com.multipz.atmiyalawlab.Util.Config;
import com.sinch.android.rtc.AudioController;
import com.sinch.android.rtc.ClientRegistration;
import com.sinch.android.rtc.Sinch;
import com.sinch.android.rtc.SinchClient;
import com.sinch.android.rtc.SinchClientListener;
import com.sinch.android.rtc.SinchError;
import com.sinch.android.rtc.calling.Call;
import com.sinch.android.rtc.calling.CallClient;
import com.sinch.android.rtc.calling.CallClientListener;
import com.sinch.android.rtc.video.VideoController;

/**
 * Created by Admin on 10-02-2018.
 */

public class SinchService extends Service {

    private static final String APP_KEY = Config.applicationKey;
    private static final String APP_SECRET = Config.applicationSecret;
    private static final String ENVIRONMENT = Config.environmentHost;
   /* private static final String APP_KEY = Config.applicationKey;
    private static final String APP_SECRET = Config.applicationSecret;
    private static final String ENVIRONMENT = Config.environmentHost;*/

    public static final String CALL_ID = "call_ID";
    static final String TAG = SinchService.class.getSimpleName();

    private AudioPlayer mAudioPlayer;
    private SinchServiceInterface mSinchServiceInterface = new SinchServiceInterface();
    private SinchClient mSinchClient;
    private String mUserId;
    public static Call currentAudioCall = null;
    private StartFailedListener mListener;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        if (mSinchClient != null && mSinchClient.isStarted()) {
            mSinchClient.terminate();
        }
        super.onDestroy();
    }

    private void start(String userName) {
        if (mSinchClient == null) {
            mUserId = userName;
            try {
                mSinchClient = Sinch.getSinchClientBuilder().context(getApplicationContext()).userId(userName)
                        .applicationKey(APP_KEY)
                        .applicationSecret(APP_SECRET)
                        .environmentHost(ENVIRONMENT).build();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mSinchClient.setSupportCalling(true);
            mSinchClient.startListeningOnActiveConnection();
            mSinchClient.addSinchClientListener(new MySinchClientListener());
            mSinchClient.getCallClient().addCallClientListener(new SinchCallClientListener());
            mSinchClient.start();
        }
    }

    private void stop() {
        if (mSinchClient != null) {
            mSinchClient.terminate();
            mSinchClient = null;
        }
    }

    private boolean isStarted() {
        return (mSinchClient != null && mSinchClient.isStarted());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mSinchServiceInterface;
    }

    public class SinchServiceInterface extends Binder {

        public Call callUserVideo(String userId) {
            return mSinchClient.getCallClient().callUserVideo(userId);
        }

        public String getUserName() {
            return mUserId;
        }

        public boolean isStarted() {
            return SinchService.this.isStarted();
        }

        public void startClient(String userName) {
            start(userName);
        }

        public void stopClient() {
            stop();
        }

        public void setStartListener(StartFailedListener listener) {
            mListener = listener;
        }

        public Call getCall(String callId) {
            return mSinchClient.getCallClient().getCall(callId);
        }

        public VideoController getVideoController() {
            if (!isStarted()) {
                return null;
            }
            return mSinchClient.getVideoController();
        }

        public AudioController getAudioController() {
            if (!isStarted()) {
                return null;
            }
            return mSinchClient.getAudioController();
        }
    }

    public interface StartFailedListener {

        void onStartFailed(SinchError error);

        void onStarted();
    }

    private class MySinchClientListener implements SinchClientListener {

        @Override
        public void onClientFailed(SinchClient client, SinchError error) {
            if (mListener != null) {
                mListener.onStartFailed(error);
            }
            mSinchClient.terminate();
            mSinchClient = null;
        }

        @Override
        public void onClientStarted(SinchClient client) {
            Log.d(TAG, "SinchClient started");
            if (mListener != null) {
                mListener.onStarted();
            }
        }

        @Override
        public void onClientStopped(SinchClient client) {
            Log.d(TAG, "SinchClient stopped");
        }

        @Override
        public void onLogMessage(int level, String area, String message) {
            switch (level) {
                case Log.DEBUG:
                    Log.d(area, message);
                    break;
                case Log.ERROR:
                    Log.e(area, message);
                    break;
                case Log.INFO:
                    Log.i(area, message);
                    break;
                case Log.VERBOSE:
                    Log.v(area, message);
                    break;
                case Log.WARN:
                    Log.w(area, message);
                    break;
            }
        }

        @Override
        public void onRegistrationCredentialsRequired(SinchClient client,
                                                      ClientRegistration clientRegistration) {
        }
    }

    private class SinchCallClientListener implements CallClientListener {

        @Override
        public void onIncomingCall(CallClient callClient, Call call) {
            Log.d("LogVideo", "" + call.getDetails().isVideoOffered());
            if (call.getDetails().isVideoOffered()) {
                Intent intent = new Intent(SinchService.this, IncommingActivity.class);
                intent.putExtra(CALL_ID, call.getCallId());
//            Call call = getSinchServiceInterface().callUserVideo(lawyer_id + "***" + lawyer_name);
                intent.putExtra("RemoteUserID", call.getRemoteUserId());
                intent.putExtra("communication_type_id ", 3);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                SinchService.this.startActivity(intent);
            } else {
                if (currentAudioCall == null) {
                    currentAudioCall = call;
                    Intent intent = new Intent(SinchService.this, ActivityAudioCalling.class);
                    intent.putExtra(CALL_ID, call.getCallId());
                    intent.putExtra("in_Call", "AudioCall");
                    intent.putExtra("RemoteUserID", call.getRemoteUserId());
                    intent.putExtra("currentAudioCall", currentAudioCall.toString());
                    //intent.putExtra("lawyer_id", call.getRemoteUserId());
//            Call call = getSinchServiceInterface().callUserVideo(lawyer_id + "***" + lawyer_name);
                    //  intent.putExtra(CALL_ID, call.getRemoteUserId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    SinchService.this.startActivity(intent);
                }

            }

        }
    }

}
