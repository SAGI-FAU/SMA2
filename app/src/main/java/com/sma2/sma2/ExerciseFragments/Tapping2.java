package com.sma2.sma2.ExerciseFragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sma2.sma2.R;
import com.sma2.sma2.ThanksActivity;


public class Tapping2 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_capture_tapping_2);
        setListeners();
    }

    private void setListeners() {

        final TextView mTextField = findViewById(R.id.accgraph_chrono4);
        findViewById(R.id.tapButton_2_1).setOnClickListener(this);
        findViewById(R.id.tapButton_2_2).setOnClickListener(this);

        new CountDownTimer(10000, 1000) {

            public void onTick(long millisUntilFinished) {
                mTextField.setText(Long.toString(millisUntilFinished / 1000));
            }

            public void onFinish() {
                mTextField.setText(getApplicationContext().getString(R.string.done));

                open_exercise();
            }
        }.start();

    }


    @Override
    public void onClick(View view) {
        //TODO: onclick



        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        ImageButton tap1 = findViewById(R.id.tapButton_2_1);
        ImageButton tap2 = findViewById(R.id.tapButton_2_2);

        switch (view.getId()){
            case R.id.tapButton_2_1:

            vib.vibrate(100);
            change_button_position(tap1,tap2);
            break;
            case R.id.tapButton_2_2:

            vib.vibrate(100);
            change_button_position(tap2,tap1);
            break;
        }

    }

    public void open_exercise(){
        Intent intent_ex1;
        intent_ex1 =new Intent(this, ThanksActivity.class);
        startActivity(intent_ex1);
    }

   /* public void change_button_position(ImageButton imageButton){
        ConstraintLayout.LayoutParams params_bug= (ConstraintLayout.LayoutParams)  imageButton.getLayoutParams();


        int y= (int)(Math.random()*((800)));
        int x= (int)(Math.random()*((600)));



        params_bug.topMargin=y;
        params_bug.leftMargin=x;
        params_bug.setMarginStart(x);
        imageButton.setLayoutParams(params_bug);

    }*/

    public void change_button_position(ImageButton imageButton, ImageButton imageButton2){
        ConstraintLayout.LayoutParams params_bug= (ConstraintLayout.LayoutParams)  imageButton.getLayoutParams();
        ConstraintLayout.LayoutParams params_bug2= (ConstraintLayout.LayoutParams)  imageButton2.getLayoutParams();


        //int y= (int)(Math.random()*((800)));
        //int x= (int)(Math.random()*((600)));
        int y= (int)(Math.random()*((800)));
        int x= (int)(Math.random()*((600)));

        int x2=params_bug2.getMarginStart();

        int y2=params_bug2.topMargin;
        Log.e("x2",Integer.toString(x));
        Log.e("y2",Integer.toString(y));
        Log.e("x2",Integer.toString(x2));
        Log.e("y2",Integer.toString(y2));



        if(Math.abs(x2-x)<120) {
            x = x + 240;

            if(x>800){
                x=x-480;
            }
            Log.e("x2","Here there is an increment");

        }
        /*else if(Math.abs(y2-y)<120) {

                y = y + 240;

            if(y>600){
                y=y-480;
            }

        }*/
        Log.e("x2","After");
        Log.e("x2",Integer.toString(x));


        params_bug.topMargin=y;
        params_bug.leftMargin=x;

        params_bug.setMarginStart(x);
        imageButton.setLayoutParams(params_bug);


    }




}

// TODO: Separate the buttons