package com.sma2.sma2;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDataService;

import java.util.List;

public class MainActivityMenu extends AppCompatActivity implements View.OnClickListener {
    int PERMISSION_ALL = 1;
    String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.RECORD_AUDIO
    };
    private AlertDialog noPermissionsAlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        noPermissionsAlertDialog= createAlertDialog();
        ask_permissions();

        // create alarm notifications to make exercises
        Notifications notifications=new Notifications(this);
        int hour=9;
        int minutes=0;
        notifications.setReminder(this,AlarmReceiver.class, hour, minutes);


        // create alarm notifications to take the medicine
        MedicineDataService MedicineData=new MedicineDataService(this);
        List<MedicineDA> Medicine=MedicineData.getAllCurrentMedictation();
        MedicineDA CurrentMed;
        for (int i = 0; i < Medicine.size(); i++) {
            CurrentMed=Medicine.get(i);
            notifications.setReminder(this,AlarmReceiverMedicine.class, CurrentMed.getIntakeTime(), 0);
            }

        setListeners();



    }

    private void setListeners() {
        findViewById(R.id.btnProfile).setOnClickListener(this);
        findViewById(R.id.btnSettings).setOnClickListener(this);
        findViewById(R.id.btnExercises).setOnClickListener(this);
        findViewById(R.id.btnResults).setOnClickListener(this);
        findViewById(R.id.txtProfile).setOnClickListener(this);
        findViewById(R.id.txtSettings).setOnClickListener(this);
        findViewById(R.id.txtExercises).setOnClickListener(this);
        findViewById(R.id.txtResults).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnProfile:
                open_profile();
                break;
            case R.id.txtProfile:
                open_profile();
                break;
            case R.id.btnSettings:
                open_settings();
                break;
            case R.id.txtSettings:
                open_settings();
                break;
            case R.id.btnExercises:
                open_exercises();
                break;
            case R.id.txtExercises:
                open_exercises();
                break;
            case R.id.btnResults:
                open_results();
                break;
            case R.id.txtResults:
                open_results();
                break;

        }
    }

    public void open_settings() {
        Intent intent_settings = new Intent(MainActivityMenu.this, SettingsActivity.class);
        startActivity(intent_settings);
    }

    public void open_exercises() {
        if(!hasPermissions(this, PERMISSIONS)){
            noPermissionsAlertDialog.show();
            return;
        }
        Intent intent_exercises = new Intent(MainActivityMenu.this, ExercisesActivity.class);
        startActivity(intent_exercises);
    }

    public void open_profile(){
        Intent intent_profile =new Intent(MainActivityMenu.this, ProfileActivity.class);
        startActivity(intent_profile);
    }
    public void open_results(){
        Intent intent_results =new Intent(MainActivityMenu.this, ResultsActivity.class);
        startActivity(intent_results);
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public void ask_permissions() {
        if(!hasPermissions(this, PERMISSIONS)){
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_ALL);
        }
    }

    private AlertDialog createAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);

        // set dialog message
        alertDialogBuilder
                .setTitle(getResources().getString(R.string.missing_permissions_title))
                .setMessage(getResources().getString(R.string.missing_permissions_text))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.OK),new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                       noPermissionsAlertDialog.dismiss();
                    }
                });
        return alertDialogBuilder.create();
    }
}
