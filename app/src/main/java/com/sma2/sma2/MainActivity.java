package com.sma2.sma2;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.sma2.sma2.DataAccess.DaoMaster;
import com.sma2.sma2.DataAccess.DaoSession;
import com.sma2.sma2.DataAccess.PatientDA;
import com.sma2.sma2.DataAccess.PatientDADao;

import org.greenrobot.greendao.database.Database;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int MY_PERMISSIONS_REQUEST_RECORD_AUDIO, MY_PERMISSIONS_REQUEST_WRITE_STORAGE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main_menu);
        setContentView(R.layout.activity_main_menu);
        setListeners();
        ask_permissions();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();

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
            case R.id.txtProfile:
                //TODO: Implement transition to new activity
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
                break;
            case R.id.txtResults:
                //TODO: Implement transition to new activity
                break;
        }
    }

    public void open_settings(){
        Intent intent_settings =new Intent(this, SettingsActivity.class);
        startActivity(intent_settings);
    }

    public void open_exercises(){
        Intent intent_exercises =new Intent(this, ExercisesActivity.class);
        startActivity(intent_exercises);
    }


    public void ask_permissions(){
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.RECORD_AUDIO)) {
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.RECORD_AUDIO},
                        MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
            }
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
            }
        }
    }

}
