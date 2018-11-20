package com.sma2.sma2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDataService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AlarmReceiverMedicine extends BroadcastReceiver {


    String TAG = "AlarmReceiver";



    @Override
    public void onReceive(Context context, Intent intent) {

        Notifications notifications =new Notifications(context);
        if (intent.getAction() != null && context != null) {

            if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {

                // Set the alarm here.
                Log.d(TAG, "onReceive: BOOT_COMPLETED");
                notifications.setReminder(context, AlarmReceiverMedicine.class,
                        9, 0);
                return;

            }

        }

        String Title_notification=context.getResources().getString(R.string.title_notif_med);
        String content_notification=context.getResources().getString(R.string.content_notif_med);

        MedicineDataService mds= new MedicineDataService(context);

        List<MedicineDA> ListMed= mds.getMedicineByTime(Calendar.getInstance().get(Calendar.HOUR_OF_DAY));
        String MedicineName;

        for (int i=0;i<ListMed.size();i++){
            MedicineDA CurrentMed=ListMed.get(i);
            MedicineName=CurrentMed.getMedicineName();
            content_notification=content_notification+ " "+MedicineName+", ";
        }


        //Trigger the notification
        notifications.notifyUser(Title_notification, content_notification);

    }

}