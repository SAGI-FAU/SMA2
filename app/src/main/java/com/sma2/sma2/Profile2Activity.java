package com.sma2.sma2;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDataService;
import com.sma2.sma2.DataAccess.PatientDA;

import java.util.ArrayList;
import java.util.Calendar;

import static com.sma2.sma2.Utility.Helpers.hideKeyboard;

public class Profile2Activity extends AppCompatActivity implements View.OnClickListener {
    TextView TimeDignosis;
    Spinner spinner;
    Spinner intaketime_spinner;
    PatientDA patientData;
    ArrayList<MedicineDA> medicineDataArrayList;
    String medicine_name;
    int dose, hour_intake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        TimeDignosis=findViewById(R.id.time_diagnosis);
        ConstraintLayout layout = findViewById(R.id.layout_p2);
        layout.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View view, MotionEvent ev)
            {
                hideKeyboard(view);
                return false;
            }
        });
        patientData = (PatientDA) getIntent().getSerializableExtra("PatientData");
        MedicineDataService ds = new MedicineDataService(getApplicationContext());
        ds.getAllCurrentMedictation();
        medicineDataArrayList = new ArrayList<>(ds.getAllCurrentMedictation());
        initialized();
    }

    private void initialized() {
        spinner = findViewById(R.id.spinnerEducation);
        intaketime_spinner = findViewById(R.id.spinnerIntake);


        Resources r = getResources();
        String[] categories = new String[]{r.getString(R.string.none_item),r.getString(R.string.elem_item),r.getString(R.string.high_item),r.getString(R.string.under_item),r.getString(R.string.post)};
        String[] hours = new String[]{"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
        ArrayAdapter<String> intake_adapter =  new ArrayAdapter<> (this,android.R.layout.simple_spinner_item,hours);
        ArrayAdapter<String> adapter =  new ArrayAdapter<> (this,android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        intake_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);

        int EducationLevel=patientData.getEducational_level();
        if (EducationLevel>=0){
            spinner.setSelection(EducationLevel);
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                patientData.setEducational_level(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        if (patientData.getYear_diag() != 0) {
            int YearDiagnosis = patientData.getYear_diag();
            TimeDignosis.setText(String.valueOf(YearDiagnosis));
        }

        intaketime_spinner.setAdapter(intake_adapter);
        intaketime_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                hour_intake = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        findViewById(R.id.button_save_med).setOnClickListener(this);
        findViewById(R.id.button_back1).setOnClickListener(this);
        findViewById(R.id.button_continue2).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        medicine_name = ((TextView) findViewById(R.id.med_name)).getText().toString();
        String temp_dose = ((TextView) findViewById(R.id.med_dose)).getText().toString();
        switch (view.getId()){
            case R.id.button_save_med:
                if(valid_medical_data(temp_dose)){
                    if(!AddMedicine()){
                        Toast.makeText(this,R.string.add_medice_error,Toast.LENGTH_SHORT).show();
                    }
                    Log.d("debug","Cualquier: Debug");
                }
                break;
            case R.id.button_back1:
                onBackPressed();
                break;
            case R.id.button_continue2:
                String time_diagnosis = TimeDignosis.getText().toString();
                if(!time_diagnosis.isEmpty()){
                    patientData.setYear_diag(Integer.valueOf(time_diagnosis));
                    patientData.setYear_diag(Integer.valueOf(time_diagnosis));
                    Intent intent = new Intent(Profile2Activity.this,Profile3Activity.class);
                    intent.putExtra("PatientData", patientData);
                    startActivity(intent);
                    patientData.setYear_diag(Integer.valueOf(time_diagnosis));
                }else{
                    Toast.makeText(this,R.string.time_diagnosis_error,Toast.LENGTH_SHORT).show();
                }
        }
    }

    private boolean AddMedicine() {
//        PatientDataService pds = new PatientDataService(getApplicationContext());
        MedicineDataService mds = new MedicineDataService(getApplicationContext());

        long patientid = patientData.getUserId();
        MedicineDA da = new MedicineDA(medicine_name, dose, hour_intake, patientid);
        if(medicineDataArrayList.contains(da)){
            Toast.makeText(this,R.string.add_medice_error,Toast.LENGTH_SHORT).show();
            return false;
        }else{
            medicineDataArrayList.add(da);
            mds.saveMedicine(da);
            Toast.makeText(this,R.string.medicine_added,Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    private boolean valid_medical_data(String temp_dose) {
        if(medicine_name.isEmpty()) {
            Toast.makeText(this, R.string.medicine_empty, Toast.LENGTH_SHORT).show();
            return false;
        } else if (temp_dose.isEmpty()){
            Toast.makeText(this,R.string.dose_empty,Toast.LENGTH_SHORT).show();
            return false;
        }else{
            dose = Integer.valueOf(temp_dose);
            return true;
        }
    }
}
