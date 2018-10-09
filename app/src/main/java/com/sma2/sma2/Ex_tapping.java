package com.sma2.sma2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Ex_tapping extends AppCompatActivity implements View.OnClickListener {

    private String exercise_tapping;

    private int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        x= (int)(Math.random()*((3))+1);
        switch (x) {
            case 1:
                exercise_tapping = getApplicationContext().getString(R.string.tapping_1);
                break;
            case 2:
                exercise_tapping = getApplicationContext().getString(R.string.tapping_2);
                break;
            case 3:
                exercise_tapping = getApplicationContext().getString(R.string.sliding);
                break;
        }
        //TODO: discuss and improve how the three exercises will be distributed (randomly, one different in each session?)
        setContentView(R.layout.activity_capture_finger_tapping);
        setListeners();
    }

    private void setListeners() {
        final TextView mTextView = findViewById(R.id.textView_tapping_name);
        findViewById(R.id.button_instructions_tapping).setOnClickListener(this);
        findViewById(R.id.button_record_tapping).setOnClickListener(this);
        mTextView.setText(exercise_tapping);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_instructions_tapping:
                //TODO: Implement method to play the audio or visual instructions
                break;
            case R.id.button_record_tapping:
                open_exercise();
                break;
        }

    }

    public void open_exercise(){

        Intent intent_ex1;

        switch (x) {
            //TODO: screens for tapping exercises
            case 1:
                intent_ex1 =new Intent(this, Tapping1.class);
                intent_ex1.putExtra("EXERCISE","tapping1");
                startActivity(intent_ex1);
                break;
            case 2:
                intent_ex1 =new Intent(this, Tapping1.class);
                intent_ex1.putExtra("EXERCISE","tapping2");
                startActivity(intent_ex1);
                break;
            case 3:
                intent_ex1 =new Intent(this, Tapping1.class);
                intent_ex1.putExtra("EXERCISE","sliding");
                startActivity(intent_ex1);
                break;
        }
    }
}



