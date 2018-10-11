package com.sma2.sma2.ExerciseFragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sma2.sma2.R;
import com.sma2.sma2.ThanksActivity;


public class Sliding extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_capture_sliding);
        setListeners();
    }

    private void setListeners() {

        final TextView mTextField = findViewById(R.id.accgraph_chrono5);
        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                mTextField.setText(getApplicationContext().getString(R.string.done));

                open_exercise();
            }
        }.start();


        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar2);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                ImageView imageView_limit=(ImageView) findViewById(R.id.imageView_limit);
                ConstraintLayout.LayoutParams params_bug= (ConstraintLayout.LayoutParams)  imageView_limit.getLayoutParams();
                int xSeek=seekBar.getProgress(); //The SeekBar percentage
                //int xSeek=seekBar.get; //The SeekBar position
                int xBar=params_bug.getMarginStart(); // The indicator bar position
                ReachingTheBar(xSeek, xBar);
                Log.e("seek",Integer.toString(xSeek));
                Log.e("seek",Integer.toString(xBar));





            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




    }




    @Override
    public void onClick(View view) {
        //TODO: onclick
    }

    public void open_exercise(){
        Intent intent_ex1;
        intent_ex1 =new Intent(this, ThanksActivity.class);
        startActivity(intent_ex1);
    }

    public void ReachingTheBar(int xSeek, int xBar){
        if(xSeek==xBar){
            Log.e("seek","holi");

            ImageView imageView_limit=(ImageView) findViewById(R.id.imageView_limit);
            ConstraintLayout.LayoutParams params_bug= (ConstraintLayout.LayoutParams)  imageView_limit.getLayoutParams();
            int xRandomBar= (int)(Math.random()*((22))+12);
            params_bug.setMarginStart(xRandomBar); // The indicator bar position

        }
    }


}
