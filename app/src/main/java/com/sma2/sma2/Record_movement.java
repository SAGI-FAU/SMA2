package com.sma2.sma2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;


public class Record_movement extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_capture_movement);
        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.button_continue_walking).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_continue_walking:
                open_exercise();
                break;
        }

    }

    public void open_exercise(){

        Intent intent_prev = getIntent();
        String Exercise = intent_prev.getStringExtra("EXERCISE");
        Intent intent_ex1;
        if (Exercise.equals("2x10") || Exercise.equals("4x10") || Exercise.equals("2min_walk")){
            intent_ex1 =new Intent(this, Ex_mov_hands.class);
            startActivity(intent_ex1);
        }
        else if (Exercise.equals("rest_tremor") || Exercise.equals("kinetic_tremor") || Exercise.equals("pronation_supination")){
            intent_ex1 =new Intent(this, Ex_tapping.class);
            startActivity(intent_ex1);
        }

    }


}
