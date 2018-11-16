package com.sma2.sma2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDataService;
import com.sma2.sma2.DataAccess.PatientDA;
import com.sma2.sma2.DataAccess.PatientDataService;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ejm_data_medicine> list_medic;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        TextView UsernameText = findViewById(R.id.textView_use_name);
        TextView BirthdayText = findViewById(R.id.textView_user_birthday);
        TextView SessionsText = findViewById(R.id.textView_user_sessions);

        recycler=findViewById(R.id.recyclerMedicine);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        list_medic=new ArrayList<>();
        ad_medicine();
        Adapter_data_medicine adapter=new Adapter_data_medicine(list_medic);
        recycler.setAdapter(adapter);


        PatientDataService PatientData= new PatientDataService(this);
        Long NumPatients=PatientData.countPatients();
        if (NumPatients>0){
            PatientDA patient=PatientData.getPatient();
            UsernameText.setText(patient.getUsername());
            Date Birthday=patient.getBirthday();
            BirthdayText.setText(DateFormat.getDateInstance().format(Birthday));
            SessionsText.setText(String.valueOf(patient.getSessionCount()));
        }
        else{
            UsernameText.setText(this.getText(R.string.name));
            Date Birthday=Calendar.getInstance().getTime();
            BirthdayText.setText(DateFormat.getDateInstance().format(Birthday));
            SessionsText.setText(String.valueOf(0));
        }


        setListeners();
    }

    private void ad_medicine() {
        //TODO: this is a test for RecyclerView

        MedicineDataService MedicineData=new MedicineDataService(this);

        List<MedicineDA> Medicine=MedicineData.getAllCurrentMedictation();

        MedicineDA CurrentMed;
        for (int i = 0; i < Medicine.size(); i++) {
            CurrentMed=Medicine.get(i);

            list_medic.add(new ejm_data_medicine(CurrentMed.getId(), CurrentMed.getMedicineName(),CurrentMed.getDose(),CurrentMed.getIntakeTime()));
        }
    }

    private void setListeners() {
        findViewById(R.id.button_update_medicine).setOnClickListener(this);
        findViewById(R.id.button_back).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_update_medicine:
                open_update();
                break;
            case R.id.button_back:
                open_main();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        open_main();
        finish();
        super.onBackPressed();
    }

    private void open_update() {
        Intent intent_update =new Intent(this, UpdateMedicine.class);
        startActivity(intent_update);
    }

    public void open_main(){
        Intent intent_main =new Intent(this, MainActivityMenu.class);
        startActivity(intent_main);
    }
}
