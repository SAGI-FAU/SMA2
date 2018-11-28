package com.sma2.sma2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sma2.sma2.DataAccess.MedicineDA;
import com.sma2.sma2.DataAccess.MedicineDataService;

import java.util.ArrayList;
import java.util.List;

public class UpdateMedicine extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ejm_data_medicine> list_medic;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medicine);
        recycler=findViewById(R.id.recycler_update);
        list_medic=new ArrayList<>();
        ad_medicine();
        Adapter_update_medic adapter=new Adapter_update_medic(list_medic, this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        setListeners();
    }

    private void ad_medicine() {
        MedicineDataService MedicineData=new MedicineDataService(this);
        List<MedicineDA> Medicine=MedicineData.getAllCurrentMedictation();
        MedicineDA CurrentMed;
        for (int i = 0; i < Medicine.size(); i++) {
            CurrentMed=Medicine.get(i);
            list_medic.add(new ejm_data_medicine(CurrentMed.getId(), CurrentMed.getMedicineName(),CurrentMed.getDose(),CurrentMed.getIntakeTime()));
        }
    }

    private void setListeners() {
        findViewById(R.id.button_add_medicine).setOnClickListener(this);
        findViewById(R.id.button_back1).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_medicine:
                add_medicine();
                break;
            case R.id.button_back1:
                Intent intent=new Intent(UpdateMedicine.this, ProfileActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void add_medicine() {
        Intent intent_add_medicine =new Intent(UpdateMedicine.this, Modif_medicine.class);
        intent_add_medicine.putExtra("update",false);
        startActivity(intent_add_medicine);
    }
}
