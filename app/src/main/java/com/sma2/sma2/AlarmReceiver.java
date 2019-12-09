package com.sma2.sma2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {


    String TAG = "AlarmReceiver";




    @Override
    public void onReceive(Context context, Intent intent) {

        Notifications notifications =new Notifications(context);
        if (intent.getAction() != null && context != null) {

            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                notifications.setReminder(context, AlarmReceiver.class,
                        9, 0);
                return;

            }

        }
        String Title_notification=context.getResources().getString(R.string.Title_notif);
        String content_notification=context.getResources().getString(R.string.Content_notif);

        //Trigger the notification
        notifications.notifyUser(Title_notification, content_notification);

    }

}