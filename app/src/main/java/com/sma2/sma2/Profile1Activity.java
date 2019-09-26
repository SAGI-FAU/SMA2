package com.sma2.sma2;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sma2.sma2.DataAccess.PatientDA;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.sma2.sma2.Utility.Helpers.hideKeyboard;

public class Profile1Activity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_username;
    //TextView tv_userid;
    TextView et_date;
    RadioGroup rg_gender, rg_hand, rg_smoker;
    PatientDA patientData;
    Calendar C = Calendar.getInstance();
    int year = 1970;
    int month = 1;
    int day = 1;
    DatePickerDialog datePickerDialog;
    Date date = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);
        patientData = (PatientDA) getIntent().getSerializableExtra("PatientData");
        ConstraintLayout layout = findViewById(R.id.layout_p1);
        et_date = findViewById(R.id.age_create);
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });
        try {
            date = new SimpleDateFormat("dd/MM/yyyy").parse(String.valueOf(day)+"/"+String.valueOf(month)+"/"+String.valueOf(year));
            et_date.setText(DateFormat.getDateInstance().format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        initialized();
    }

    private void initialized() {
        tv_username = findViewById(R.id.username_create);
        //tv_userid = findViewById(R.id.userid_create);
        et_date.setOnClickListener(this);

        tv_username.setText(patientData.getUsername());
        //tv_userid.setText(patientData.getGovtId());
        findViewById(R.id.button_continue).setOnClickListener(this);
        findViewById(R.id.button_back1).setOnClickListener(this);

        Date Birthdate=patientData.getBirthday();
        if (Birthdate!=null){
            et_date.setText(DateFormat.getDateInstance().format(Birthdate));
        }

        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                mMonth+=1;


                try {
                    date = new SimpleDateFormat("dd/MM/yyyy").parse(String.valueOf(mDay)+"/"+String.valueOf(mMonth)+"/"+String.valueOf(mYear));
                    et_date.setText(DateFormat.getDateInstance().format(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        },year,month,day);


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
                    RadioButton radio_button = findViewById(rg_gender.getCheckedRadioButtonId());
                    if (radio_button == null) {
                        Toast.makeText(getApplicationContext(), getString(R.string.fill_dialog), Toast.LENGTH_LONG).show();
                        return;
                    }
                    String gender = radio_button.getText().toString();
                    String hand_string = ((RadioButton) findViewById(rg_hand.getCheckedRadioButtonId())).getText().toString();
                    String smoker_string = ((RadioButton) findViewById(rg_smoker.getCheckedRadioButtonId())).getText().toString();
                    patientData.setBirthday(date);
                    patientData.setGender(gender);
                    switch (hand_string) {
                        case "Right":
                            patientData.setHand(0);
                            break;
                        case "Left":
                            patientData.setHand(1);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), getString(R.string.fill_dialog), Toast.LENGTH_LONG).show();
                            return;
                    }

                    switch (smoker_string) {
                        case "Yes":
                            patientData.setSmoker(true);
                            break;
                        case "No":
                            patientData.setSmoker(false);
                            break;
                        default:
                            Toast.makeText(getApplicationContext(), getString(R.string.fill_dialog), Toast.LENGTH_LONG).show();
                            return;
                    }

                    Intent intent = new Intent(Profile1Activity.this,Profile2Activity.class);
                    intent.putExtra("PatientData", patientData);
                    startActivity(intent);
                }else{
                    Toast.makeText(this,R.string.brith_date_error,Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.button_back1:
                onBackPressed();
                break;
            case R.id.age_create:
            datePickerDialog.show();
        }
    }
}
