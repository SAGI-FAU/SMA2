package com.sma2.sma2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDataService;
import com.sma2.sma2.DataAccess.PatientDA;
import com.sma2.sma2.DataAccess.PatientDataService;

public class Modif_medicine extends AppCompatActivity implements View.OnClickListener{

    TextView eMedicine,eDoses;
    String medicine_name,temp_dose;
    Spinner intaketime_spinner;
    int hour_intake;
    int band=0;
    Long id;
    Button delete;
    boolean update=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_medicine);
        eMedicine=findViewById(R.id.med_name);
        eDoses=findViewById(R.id.med_dose);
        delete=findViewById(R.id.button_delete);
        intaketime_spinner = findViewById(R.id.spinnerIntake);

        Bundle Data_medicine=this.getIntent().getExtras();

        if (Data_medicine!=null){
            update=(boolean)Data_medicine.get("update");
            eMedicine.setText(String.valueOf(Data_medicine.get("Medicine")));
            eDoses.setText(String.valueOf(Data_medicine.get("Doses")));
            id=(Long) Data_medicine.get("id");
            band=1;
        }

        if (band==0){
            delete.setVisibility(View.GONE);
        }
        String[] hours = new String[]{"00:00","01:00","02:00","03:00","04:00","05:00","06:00","07:00","08:00","09:00","10:00","11:00","12:00","13:00","14:00","15:00","16:00","17:00","18:00","19:00","20:00","21:00","22:00","23:00"};
        ArrayAdapter<String> intake_adapter =  new ArrayAdapter<> (this,android.R.layout.simple_spinner_item,hours);
        intake_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.button_cancel).setOnClickListener(this);
        findViewById(R.id.button_save).setOnClickListener(this);
        findViewById(R.id.button_delete).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        medicine_name = ((TextView) findViewById(R.id.med_name)).getText().toString();
        temp_dose = ((TextView) findViewById(R.id.med_dose)).getText().toString();
        MedicineDataService mds = new MedicineDataService(getApplicationContext());
        PatientDataService PatientData= new PatientDataService(this);
        Long NumPatients=PatientData.countPatients();
        Intent intent_update =new Intent(this, UpdateMedicine.class);

        switch (view.getId()){
            case R.id.button_cancel:
                startActivity(intent_update);
                break;
            case R.id.button_save:
                if (NumPatients>0){
                    PatientDA patient=PatientData.getPatient();
                    if (!update){
                    MedicineDA Medicine=new MedicineDA(medicine_name, Integer.valueOf(temp_dose), hour_intake, patient.getUserId());
                    mds.saveMedicine(Medicine);
                    }
                    else{
                        MedicineDA Medicine=   mds.getMedicineById(id);
                        Medicine.setMedicineName(medicine_name);
                        Medicine.setDose(Integer.valueOf(temp_dose));
                        Medicine.setIntakeTime(hour_intake);
                        Medicine.setPatientDAId(patient.getUserId());
                        mds.saveMedicine(Medicine);
                    }
                }

                startActivity(intent_update);
                break;
            case R.id.button_delete:

                MedicineDA Med=mds.getMedicineById(id);
                mds.delete(Med);

                startActivity(intent_update);
        }
    }
}
