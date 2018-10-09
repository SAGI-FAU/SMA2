package com.sma2.sma2;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Profile2Activity extends AppCompatActivity implements View.OnClickListener {

    Spinner spinner;
    UserData userData;
    ArrayList<MedicalData> medicalDataArrayList;
    String medicine_name;
    int dose;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);
        userData = (UserData) getIntent().getSerializableExtra("UserData");
        medicalDataArrayList = new ArrayList<MedicalData>(5);
        initialized();
    }

    private void initialized() {
        spinner = findViewById(R.id.spinnerEducation);
        Resources r = getResources();
        String[] categories = new String[]{r.getString(R.string.none_item),r.getString(R.string.elem_item),r.getString(R.string.high_item),r.getString(R.string.under_item),r.getString(R.string.post)};
        ArrayAdapter<String> adapter =  new ArrayAdapter<String> (this,android.R.layout.simple_spinner_item,categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                userData.setEducational_level(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        findViewById(R.id.button_save_med).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        medicine_name = ((TextView) findViewById(R.id.med_name)).getText().toString();
        String temp_dose = ((TextView) findViewById(R.id.med_dose)).getText().toString();
        switch (view.getId()){
            case R.id.button_save_med:
                if(valid_medical_data(temp_dose)){
                    if(!AddMedice()){
                        Toast.makeText(this,R.string.add_medice_error,Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private boolean AddMedice() {
        if(medicalDataArrayList.contains(new MedicalData(medicine_name,dose))){
            return false;
        }else{
            medicalDataArrayList.add(new MedicalData(medicine_name,dose));
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
