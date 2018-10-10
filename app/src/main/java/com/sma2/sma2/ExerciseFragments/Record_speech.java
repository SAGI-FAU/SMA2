package com.sma2.sma2.ExerciseFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.sma2.sma2.ExerciseFragments.Ex_speech2;
import com.sma2.sma2.ExerciseFragments.Ex_speech3;
import com.sma2.sma2.ExerciseFragments.Ex_walking;
import com.sma2.sma2.R;


public class Record_speech extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_capture_speech_all_tasks);

        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.button_continue4).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_continue4:
                open_exercise();
                break;
        }

    }

    public void open_exercise(){


        Intent intent_prev = getIntent();
        String Exercise = intent_prev.getStringExtra("EXERCISE");
        Intent intent_ex1;
        if (Exercise.equals("@string/a_ex")){
            intent_ex1 =new Intent(this, Ex_speech2.class);
            startActivity(intent_ex1);
        }
        else if (Exercise.equals("@string/ddk_ex")){
            intent_ex1 =new Intent(this, Ex_speech3.class);
            startActivity(intent_ex1);
        }
        else{
            intent_ex1 =new Intent(this, Ex_walking.class);
            startActivity(intent_ex1);
        }
    }


}
