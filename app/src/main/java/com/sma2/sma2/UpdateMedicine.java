package com.sma2.sma2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class UpdateMedicine extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ejm_data_medicine> list_medic;
    RecyclerView recycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_medicine);
        recycler=findViewById(R.id.recycler_update);
        list_medic=new ArrayList<>();
        llenar_medicine();
        Adapter_update_medic adapter=new Adapter_update_medic(list_medic, this);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);
        setListeners();
    }

    private void llenar_medicine() {
        list_medic.add(new ejm_data_medicine("Medicine 1",25,17));
        list_medic.add(new ejm_data_medicine("Medicine 2",250,11));
    }

    private void setListeners() {
        findViewById(R.id.button_add_medicine).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_add_medicine:
                add_medicine();
                break;
        }
    }

    private void add_medicine() {
        Intent intent_add_medicine =new Intent(UpdateMedicine.this, Modif_medicine.class);
        startActivity(intent_add_medicine);
    }
}
