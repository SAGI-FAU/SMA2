package com.sma2.sma2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Ex_mov_hands extends AppCompatActivity implements View.OnClickListener {

    private String exercise_mov_hands;

    private int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        x= (int)(Math.random()*((4))+1);
        switch (x) {
            case 1:
                exercise_mov_hands = getApplicationContext().getString(R.string.rest_tremor);
                break;
            case 2:
                exercise_mov_hands = getApplicationContext().getString(R.string.kinetic_tremor);
                break;
            case 3:
                exercise_mov_hands = getApplicationContext().getString(R.string.pronation_supination);
                break;
            case 4:
                exercise_mov_hands = getApplicationContext().getString(R.string.ball_balance);
        }
        setContentView(R.layout.activity_record_hand_tremor);
        setListeners();
    }

    private void setListeners() {
        final TextView mTextView = findViewById(R.id.textView_hand_name);
        findViewById(R.id.button_instructions_hand).setOnClickListener(this);
        findViewById(R.id.button_record_hand).setOnClickListener(this);
        mTextView.setText(exercise_mov_hands);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_instructions_hand:
                //TODO: Implement method to play the audio or visual instructions
                break;
            case R.id.button_record_hand:
                open_exercise();
                break;
        }

    }

    public void open_exercise(){

        Intent intent_ex1;

        switch (x) {
            case 1:
                intent_ex1 =new Intent(this, Record_movement.class);
                intent_ex1.putExtra("EXERCISE","rest_tremor");
                startActivity(intent_ex1);
                break;
            case 2:
                intent_ex1 =new Intent(this, Record_movement.class);
                intent_ex1.putExtra("EXERCISE","kinetic_tremor");
                startActivity(intent_ex1);
                break;
            case 3:
                intent_ex1 =new Intent(this, Record_movement.class);
                intent_ex1.putExtra("EXERCISE","pronation_supination");
                startActivity(intent_ex1);
                break;
            case 4:
                intent_ex1 =new Intent(this, Record_mov_ball_balance.class);
                intent_ex1.putExtra("EXERCISE","ball_balance");
                startActivity(intent_ex1);
                break;
        }
    }
}



