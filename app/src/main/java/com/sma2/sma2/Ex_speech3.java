package com.sma2.sma2;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Ex_speech3 extends AppCompatActivity implements View.OnClickListener {

    private String exercise3;

    private int x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        x= (int)(Math.random()*((3))+1);
        switch (x) {
            case 1:
                exercise3 = getApplicationContext().getString(R.string.monologue);
                break;
            case 2:
                exercise3 = getApplicationContext().getString(R.string.readtext);
                break;
            case 3:
                exercise3 = getApplicationContext().getString(R.string.image);
                break;
        }
        setContentView(R.layout.activity_record_speech_other);
        setListeners();
    }

    private void setListeners() {
        final TextView mTextView = findViewById(R.id.textView_other_speech);
        findViewById(R.id.button_instructions3).setOnClickListener(this);
        findViewById(R.id.button_record3).setOnClickListener(this);
        mTextView.setText(exercise3);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_instructions3:
                //TODO: Implement method to play the audio or visual instructions
                break;
            case R.id.button_record3:
                open_exercise();
                break;
        }

    }

    public void open_exercise(){

        Intent intent_ex1;

        switch (x) {
            case 1:
                intent_ex1 =new Intent(this, Record_speech_image.class);
                intent_ex1.putExtra("EXERCISE","monologue");
                startActivity(intent_ex1);
                break;
            case 2:
                intent_ex1 =new Intent(this, Record_speech_image.class);
                intent_ex1.putExtra("EXERCISE","readtext");
                startActivity(intent_ex1);
                break;
            case 3:
                intent_ex1 =new Intent(this, Record_speech_image.class);
                intent_ex1.putExtra("EXERCISE","image");
                startActivity(intent_ex1);
                break;
        }
    }
}



