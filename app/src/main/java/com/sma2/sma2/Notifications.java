package com.sma2.sma2;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.sma2.sma2.Activities.SplashScreen;

import java.util.Calendar;

public class Notifications {


    private Context CONTEXT;

    private int DAILY_REMINDER_REQUEST_CODE;

    public Notifications(Context context){
        CONTEXT=context;
        DAILY_REMINDER_REQUEST_CODE=1;
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
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        // Create an explicit intent for an Activity in your app
        Intent intent = new Intent(CONTEXT, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(CONTEXT);
        stackBuilder.addParentStack(SplashScreen.class);
        stackBuilder.addNextIntent(intent);

        PendingIntent pendingIntent = PendingIntent.getActivity(CONTEXT, DAILY_REMINDER_REQUEST_CODE, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(CONTEXT, "ch10")
                .setSmallIcon(R.drawable.tulip_elevated_binary)
                .setContentTitle(title)
                .setContentText(text)
                .setSound(alarmSound)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        mBuilder.setAutoCancel(true);
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(CONTEXT);
        notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, mBuilder.build());
    }


    public void setReminder(Context context,Class<?> cls,int hour, int min)

    {

        Calendar calendar = Calendar.getInstance();
        Calendar setcalendar = Calendar.getInstance();
        setcalendar.set(Calendar.HOUR_OF_DAY, hour);
        setcalendar.set(Calendar.MINUTE, min);
        setcalendar.set(Calendar.SECOND, 0);

        // cancel already scheduled reminders
        cancelReminder(context,cls);

        if(setcalendar.before(calendar))
            setcalendar.add(Calendar.DATE,1);

        // Enable a receiver

        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);


        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE, intent1,
                PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(SplashScreen.ALARM_SERVICE);

        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, setcalendar.getTimeInMillis(),

                AlarmManager.INTERVAL_DAY, pendingIntent);

    }



    public void cancelReminder(Context context,Class<?> cls)

    {

        // Disable a receiver
        ComponentName receiver = new ComponentName(context, cls);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);

        Intent intent1 = new Intent(context, cls);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                DAILY_REMINDER_REQUEST_CODE, intent1, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) context.getSystemService(SplashScreen.ALARM_SERVICE);

        am.cancel(pendingIntent);

        pendingIntent.cancel();

    }

}


