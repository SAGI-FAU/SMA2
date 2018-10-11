package com.sma2.sma2.ExerciseFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import com.sma2.sma2.R;

public class Ex_walking extends AppCompatActivity implements View.OnClickListener {

    private String exercise_walking;

    private int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        x= (int)(Math.random()*((3))+1);
        switch (x) {
            case 1:
                exercise_walking = getApplicationContext().getString(R.string.two_x_ten);
                break;
            case 2:
                exercise_walking = getApplicationContext().getString(R.string.four_x_ten);
                break;
            case 3:
                exercise_walking = getApplicationContext().getString(R.string.two_min_walk);
                break;
        }
        setContentView(R.layout.activity_record_walking);
        setListeners();
    }

    private void setListeners() {
        final TextView mTextView = findViewById(R.id.textView_walk_name);
        findViewById(R.id.button_instructions_walk).setOnClickListener(this);
        findViewById(R.id.button_record_walk).setOnClickListener(this);
        mTextView.setText(exercise_walking);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_instructions_walk:
                //TODO: Implement method to play the audio or visual instructions
                break;
            case R.id.button_record_walk:
                open_exercise();
                break;
        }

    }

    public void open_exercise(){

        Intent intent_ex1;

        switch (x) {
            case 1:
                intent_ex1 =new Intent(this, Record_movement.class);
                intent_ex1.putExtra("EXERCISE","2x10");
                startActivity(intent_ex1);
                break;
            case 2:
                intent_ex1 =new Intent(this, Record_movement.class);
                intent_ex1.putExtra("EXERCISE","4x10");
                startActivity(intent_ex1);
                break;
            case 3:
                intent_ex1 =new Intent(this, Record_movement.class);
                intent_ex1.putExtra("EXERCISE","2min_walk");
                startActivity(intent_ex1);
                break;
        }
    }
}



