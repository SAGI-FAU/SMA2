package com.sma2.sma2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class Notifications {


    private Context CONTEXT;

    public Notifications(Context context){
        CONTEXT=context;
    }


    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ch10";
            String description = "channel notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("ch10", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = CONTEXT.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


    public void notifyUser(String title, String text) {

        createNotificationChannel();


        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(CONTEXT, MainActivityMenu.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(CONTEXT, 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(CONTEXT, "ch10")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        mBuilder.setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(CONTEXT);

        notificationManager.notify(10, mBuilder.build());

    }
}


