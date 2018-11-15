package com.sma2.sma2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
        switch (view.getId()){
            case R.id.button_cancel:
                finish();
            case R.id.button_save:

                if (NumPatients>0){
                    PatientDA patient=PatientData.getPatient();
                    MedicineDA Medicine=new MedicineDA(medicine_name, Integer.valueOf(temp_dose), hour_intake, patient.getUserId());
                    mds.saveMedicine(Medicine);
                }

                finish();
                break;
            case R.id.button_delete:

                MedicineDA Med=mds.getMedicineById(id);
                mds.delete(Med);

                finish();
        }
    }
}
