package com.sma2.apkinson;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.sma2.apkinson.DataAccess.PatientDA;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.sma2.apkinson.Utility.Helpers.hideKeyboard;

public class Profile1Activity extends AppCompatActivity implements View.OnClickListener {

    TextView tv_username;
    TextView et_date;
    RadioGroup rg_gender, rg_hand, rg_smoker;
    PatientDA patientData;
    DatePickerDialog datePickerDialog;
    Date birthdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile1);
        patientData = (PatientDA) getIntent().getSerializableExtra("PatientData");
        ConstraintLayout layout = findViewById(R.id.layout_p1);
        et_date = findViewById(R.id.age_create);
        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent ev) {
                hideKeyboard(view);
                return false;
            }
        });

        tv_username = findViewById(R.id.username_create);
        tv_username.setText(patientData.getUsername());

        et_date.setText(getString(R.string.dataDummy));
        et_date.setOnClickListener(this);
        datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int mYear, int mMonth, int mDay) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(mYear, mMonth, mDay);
                birthdate = calendar.getTime();
                et_date.setText(DateFormat.getDateInstance().format(birthdate));
            }
        }, 1970, 1, 1);

        findViewById(R.id.button_continue).setOnClickListener(this);
        findViewById(R.id.button_back1).setOnClickListener(this);
    }

    private boolean parseData() {
        rg_gender = findViewById(R.id.gender_radio);
        rg_hand = findViewById(R.id.hand_radio);
        rg_smoker = findViewById(R.id.smoker_radio);

        int checkedGenderRadioButtonId = rg_gender.getCheckedRadioButtonId();
        if (checkedGenderRadioButtonId == -1) {
            return false;
        } else {
            RadioButton genderRadioButton = findViewById(checkedGenderRadioButtonId);
            patientData.setGender(genderRadioButton.getText().toString());
        }

        int checkedHandRadioButtonId = rg_hand.getCheckedRadioButtonId();
        switch (checkedHandRadioButtonId) {
            case R.id.rbRight:
                patientData.setHand(0);
                break;
            case R.id.rbLeft:
                patientData.setHand(1);
                break;
            default:
                return false;
        }

        int checkedSmokerRadioButtonId = rg_smoker.getCheckedRadioButtonId();
        switch (checkedSmokerRadioButtonId) {
            case R.id.rbYess:
                patientData.setSmoker(true);
                break;
            case R.id.rbNos:
                patientData.setSmoker(false);
                break;
            default:
                return false;
        }

        String date_string = et_date.getText().toString();
        if (date_string.isEmpty() || date_string.equals(getString(R.string.dataDummy))) {
            return false;
        } else {
            patientData.setBirthday(birthdate);
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_continue:
                boolean parseSuccess = parseData();
                if (!parseSuccess) {
                    Toast.makeText(getApplicationContext(), getString(R.string.fill_dialog), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(Profile1Activity.this, Profile2Activity.class);
                    intent.putExtra("PatientData", patientData);
                    startActivity(intent);
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
