package com.multipz.atmiyalawlab.Util;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.multipz.atmiyalawlab.Activity.ActivityDashboard;
import com.multipz.atmiyalawlab.R;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Admin on 14-03-2018.
 */

public class NotificationUtils {

    private Context mCtx;
    private static NotificationUtils mInstance;

    private NotificationUtils(Context context) {
        mCtx = context;
    }

    public static synchronized NotificationUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new NotificationUtils(context);
        }
        return mInstance;
    }

    public void displayNotification(String lawyer_name, String lawyer_type) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx, NotificationChannelConstant.CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_app_logo)
                        .setContentTitle(lawyer_name)
                        .setContentText(lawyer_type);


        /*
        *  Clicking on the notification will take us to this intent
        *  Right now we are using the MainActivity as this is the only activity we have in our application
        *  But for your project you can customize it as you want
        * */

        Intent resultIntent = new Intent(mCtx, ActivityDashboard.class);
        /*
        *  Now we will create a pending intent
        *  The method getActivity is taking 4 parameters
        *  All paramters are describing themselves
        *  0 is the request code (the second parameter)
        *  We can detect this code in the activity that will open by this we can get
        *  Which notification opened the activity
        * */
        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        /*
        *  Setting the pending intent to notification builder
        * */

        mBuilder.setContentIntent(pendingIntent);

        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);

        /*
        * The first parameter is the notification id
        * better don't give a literal here (right now we are giving a int literal)
        * because using this id we can modify it later
        * */
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(1, mBuilder.build());
        }
    }

}
