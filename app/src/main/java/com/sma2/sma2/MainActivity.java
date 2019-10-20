package com.sma2.sma2;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import android.content.SharedPreferences;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.sma2.sma2.DataAccess.DaoMaster;
import com.sma2.sma2.DataAccess.DaoSession;
import com.sma2.sma2.DataAccess.PatientDA;
import com.sma2.sma2.DataAccess.PatientDataService;
import com.sma2.sma2.DataAccess.ExerciseSessionManager;



import org.greenrobot.greendao.database.Database;

import java.util.Calendar;

import static com.sma2.sma2.Utility.Helpers.hideKeyboard;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int MY_PERMISSIONS_REQUEST_RECORD_AUDIO, MY_PERMISSIONS_REQUEST_WRITE_STORAGE;

    TextView tv_username;
    TextView tv_userid;
    Button bt_create;
    String username;
    String userid;
    PatientDA patientData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("LoginPref", this.MODE_PRIVATE);
        int login = prefs.getInt("UserCreated", 0);
        if (login == 1) {

            SharedPreferences sharedPreferences = getSharedPreferences("Last day", Context.MODE_PRIVATE);
            Calendar c = Calendar.getInstance();
            int thisDay = c.get(Calendar.DAY_OF_YEAR); // GET THE CURRENT DAY OF THE YEAR
            int lastDay = sharedPreferences.getInt("Last day", 0);

            if (lastDay!=thisDay){
                ExerciseSessionManager sessionManager = new ExerciseSessionManager();
                sessionManager.createExerciseSession(this);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("Last day", thisDay);
                editor.apply();
            }

            Intent intent = new Intent(MainActivity.this, MainActivityMenu.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_main);
        ConstraintLayout layout = findViewById(R.id.layout_signin);
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });
        tv_username = findViewById(R.id.username);
        tv_userid = findViewById(R.id.userid);
        bt_create = findViewById(R.id.button_create);
        tv_userid.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onClick(bt_create);
                    handled = true;
                }
                return handled;
            }
        });
        setListeners();
        ask_permissions();
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, getString(R.string.databasename));
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();
    }

    private void setListeners() {
        tv_username.setOnClickListener(this);
        tv_userid.setOnClickListener(this);
        bt_create.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_create:
                username = tv_username.getText().toString();
                userid = tv_userid.getText().toString();
                if (validate_data()) {
                    PatientDataService pds = new PatientDataService(getApplicationContext());
                    pds.savePatient(patientData);
                    patientData = pds.getPatient();
                    open_profile1();
                }
                break;

        }
    }

    private boolean validate_data() {
        if (username.isEmpty()) {
            Toast.makeText(this, R.string.user_empty, Toast.LENGTH_SHORT).show();
            return false;
        } else if (userid.isEmpty()) {
            Toast.makeText(this, R.string.userid_empty, Toast.LENGTH_SHORT).show();
            return false;
        } else {
            patientData = new PatientDA(username, userid);
            return true;
        }
    }


    public void ask_permissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
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

    public void open_profile1() {
        Intent intent_profile1 = new Intent(this, Profile1Activity.class);
        intent_profile1.putExtra("PatientData", patientData);
        startActivity(intent_profile1);
    }

}
