package com.multipz.atmiyalawlab.Util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.multipz.atmiyalawlab.R;
import com.multipz.atmiyalawlab.User.ActivityDashboardUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Admin on 28-06-2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;
    public static final String NOTIFICATION_CHANNEL_ID = "1001";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d("", " Message Body: " + remoteMessage.getData());
        String msg = "" + remoteMessage.getData().get("basic_info");
        try {
            JSONArray array = new JSONArray(msg);
            for (int i = 0; i <= array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String lawyer_name = jsonObject.getString("lawyer_name");
                String lawyer_type = jsonObject.getString("lawyer_type");

                sendNotification(lawyer_type, lawyer_name);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void sendNotification(String lawyer_type, String lawyer_name) {
        Intent intent = new Intent(this, ActivityDashboardUser.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, PendingIntent.FLAG_ONE_SHOT);

        notificationBuilder = (NotificationCompat.Builder) new
                NotificationCompat.Builder(this)
                .setContentTitle(lawyer_name)
                .setContentText(lawyer_type)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(lawyer_type))
                .setAutoCancel(true)
                .setChannelId(NotificationChannelConstant.CHANNEL_ID)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);

        notificationManager =
                (NotificationManager)
                        getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationBuilder.setSmallIcon(R.drawable.ic_noti_app_logo);
            NotificationChannel channel = new NotificationChannel(NotificationChannelConstant.CHANNEL_ID,
                    NotificationChannelConstant.CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            channel.enableVibration(true);
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            try {
                notificationManager.createNotificationChannel(channel);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            notificationBuilder.setSmallIcon(R.mipmap.ic_app_logo);
        }
        notificationManager.notify(0, notificationBuilder.build());
    }

}
