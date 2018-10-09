package com.sma2.sma2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.sma2.sma2.ExerciseFragments.Ex_speech1;

public class Start_exercises_Activity extends AppCompatActivity implements View.OnClickListener {


    Intent intent = getIntent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);
        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.button_start).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_start:
                open_exercise();
                break;
        }

    }

    public void open_exercise(){
        Intent intent_ex1 =new Intent(this, Ex_speech1.class);
        startActivity(intent_ex1);
    }


}
