package com.sma2.sma2.ExerciseFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sma2.sma2.ExerciseFragments.Ex_walking;
import com.sma2.sma2.R;


public class Record_speech_image extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_capture_speech_image);
        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.button_continue_image).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_continue_image:
                open_exercise();
                break;
        }

    }

    public void open_exercise(){
        Intent intent_ex1 =new Intent(this, Ex_walking.class);
        startActivity(intent_ex1);

    }


}
