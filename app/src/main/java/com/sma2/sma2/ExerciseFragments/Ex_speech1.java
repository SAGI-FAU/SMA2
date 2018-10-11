package com.sma2.sma2.ExerciseFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.sma2.sma2.R;

public class Ex_speech1 extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_record_speech_a);
        setListeners();
    }

    private void setListeners() {
        findViewById(R.id.button_instructions1).setOnClickListener(this);
        findViewById(R.id.button_record1).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_instructions1:
                //TODO: Implement method to play the audio or visual instructions
                break;
            case R.id.button_record1:
                open_exercise();
                break;
        }

    }

    public void open_exercise(){

        Intent intent_ex1 =new Intent(this, Record_speech.class);
        intent_ex1.putExtra("EXERCISE","@string/a_ex");
        startActivity(intent_ex1);
    }


}
