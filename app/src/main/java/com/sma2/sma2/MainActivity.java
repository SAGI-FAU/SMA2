package com.sma2.sma2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);
        tv_username = findViewById(R.id.username);
        tv_userid = findViewById(R.id.userid);
        bt_create = findViewById(R.id.button_create);
         setListeners();

    }

    private void setListeners() {
        /*findViewById(R.id.btnProfile).setOnClickListener(this);
        findViewById(R.id.btnSettings).setOnClickListener(this);
        findViewById(R.id.btnExercises).setOnClickListener(this);
        findViewById(R.id.btnResults).setOnClickListener(this);
        findViewById(R.id.txtProfile).setOnClickListener(this);
        findViewById(R.id.txtSettings).setOnClickListener(this);
        findViewById(R.id.txtExercises).setOnClickListener(this);
        findViewById(R.id.txtResults).setOnClickListener(this);
*/
        tv_username.setOnClickListener(this);
        tv_userid.setOnClickListener(this);
        bt_create.setOnClickListener(this);
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
        Intent intent_exercises =new Intent(this, Start_exercises_Activity.class);
        startActivity(intent_exercises);
    }

    public void open_profile1(){
        Intent intent_profile1 = new Intent(this,Profile1Activity.class);
        intent_profile1.putExtra("UserData", userData);
        startActivity(intent_profile1);
    }
}
