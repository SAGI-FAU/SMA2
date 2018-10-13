package com.sma2.sma2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    ArrayList<ejm_data_medicine> list_medic;
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        recycler=findViewById(R.id.recyclerMedicine);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        list_medic=new ArrayList<>();
        ad_medicine();
        Adapter_data_medicine adapter=new Adapter_data_medicine(list_medic);
        recycler.setAdapter(adapter);
        setListeners();
    }

    private void ad_medicine() {
        //TODO: this is a test for RecyclerView
        list_medic.add(new ejm_data_medicine("Medicine 1",100,15));
        list_medic.add(new ejm_data_medicine("Medicine 2",250,18));
    }

    private void setListeners() {
        findViewById(R.id.button_update_medicine).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_update_medicine:
                open_update();
                break;
        }
    }

    private void open_update() {
        Intent intent_update =new Intent(this, UpdateMedicine.class);
        startActivity(intent_update);
    }
}
