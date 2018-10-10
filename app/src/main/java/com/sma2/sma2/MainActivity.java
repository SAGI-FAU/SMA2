package com.sma2.sma2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sma2.sma2.DataAccess.DaoMaster;
import com.sma2.sma2.DataAccess.DaoSession;

import org.greenrobot.greendao.database.Database;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_username;
    TextView tv_userid;
    Button bt_create;
    String username;
    String userid;
    UserData userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("LoginPref",this.MODE_PRIVATE);
        int login = prefs.getInt("UserCreated",0);
        if(login == 1){
            Intent intent = new Intent(MainActivity.this,MainActivityMenu.class);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_main);
        tv_username = findViewById(R.id.username);
        tv_userid = findViewById(R.id.userid);
        bt_create = findViewById(R.id.button_create);
        setListeners();
      
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "apkinsondb");
        Database db = helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();

    }

    private void setListeners() {
        tv_username.setOnClickListener(this);
        tv_userid.setOnClickListener(this);
        bt_create.setOnClickListener(this);
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
            case R.id.button_create:
                username = tv_username.getText().toString();
                userid = tv_userid.getText().toString();
                if(validate_data()){
                    open_profile1();
                }
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

    private boolean validate_data() {
        if(username.isEmpty()) {
            Toast.makeText(this, R.string.user_empty, Toast.LENGTH_SHORT).show();
            return false;
        } else if (userid.isEmpty()){
            Toast.makeText(this,R.string.userid_empty,Toast.LENGTH_SHORT).show();
            return false;
        }else{
            userData = new UserData(username,userid);
            return true;
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

    public void open_profile1(){
        Intent intent_profile1 = new Intent(this,Profile1Activity.class);
        intent_profile1.putExtra("UserData", userData);
        startActivity(intent_profile1);
    }
}
