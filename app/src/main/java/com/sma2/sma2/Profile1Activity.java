package com.sma2.sma2;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Profile1Activity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_username;
    TextView tv_userid;
    EditText et_date;
    RadioGroup rg_gender, rg_hand, rg_smoker;
    UserData userData;
    Calendar C = Calendar.getInstance();
    int year, month, day;
    DatePickerDialog datePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);
        userData = (UserData) getIntent().getSerializableExtra("UserData");
        initialized();
    }

    private void initialized() {
        tv_username = findViewById(R.id.username_create);
        tv_userid = findViewById(R.id.userid_create);
        et_date = findViewById(R.id.age_create);
        tv_username.setText(userData.getUsername());
        tv_userid.setText(userData.getUserId());
        findViewById(R.id.button_continue).setOnClickListener(this);
        findViewById(R.id.button_back1).setOnClickListener(this);
        et_date.setOnClickListener(this);
        year = C.get(Calendar.YEAR);
        month = C.get(Calendar.MONTH);
        day = C.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_continue:
                String date_string = et_date.getText().toString();
                if(!date_string.isEmpty()) {
                    rg_gender = findViewById(R.id.gender_radio);
                    rg_hand = findViewById(R.id.hand_radio);
                    rg_smoker = findViewById(R.id.smoker_radio);
                    String gender = ((RadioButton) findViewById(rg_gender.getCheckedRadioButtonId())).getText().toString();
                    String hand_string = ((RadioButton) findViewById(rg_hand.getCheckedRadioButtonId())).getText().toString();
                    String smoker_string = ((RadioButton) findViewById(rg_smoker.getCheckedRadioButtonId())).getText().toString();
                    Date date = null;
                    try {
                        date = new SimpleDateFormat("dd/MM/yyyy").parse(date_string);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    userData.setBirthday(date);
                    userData.setGender(gender);
                    switch (hand_string) {
                        case "Right":
                            userData.setHand(0);
                            break;
                        case "Left":
                            userData.setHand(1);
                            break;
                    }

                    switch (smoker_string) {
                        case "Yes":
                            userData.setSmoker(true);
                            break;
                        case "No":
                            userData.setSmoker(false);
                            break;
                    }

                    Intent intent = new Intent(Profile1Activity.this,Profile2Activity.class);
                    intent.putExtra("UserData", userData);
                    startActivity(intent);
                }else{
                    Toast.makeText(this,R.string.brith_date_error,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_back1:
                onBackPressed();
                break;
            case R.id.age_create:
                datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                        mMonth+=1;
                        et_date.setText(mDay+"/"+mMonth+"/"+mYear);
                    }
                },year,month,day);
            datePickerDialog.show();
        }
    }
}
