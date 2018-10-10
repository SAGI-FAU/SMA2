package com.sma2.sma2.ExerciseFragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.sma2.sma2.SignalRecording.TappingRecorder;
import com.sma2.sma2.Tapping1;

import com.sma2.sma2.R;
import com.sma2.sma2.ThanksActivity;



public class Tapping2 extends AppCompatActivity implements View.OnClickListener {
    TappingRecorder tappingrecorder;
    private float time2;
    private String timeStr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tappingrecorder=TappingRecorder.getInstance(this);
        tappingrecorder.prepare("Tapping2");
        tappingrecorder.TapWriter("Tap Time (ms)"  + "\t" + "Button" +"\n\r");
        tappingrecorder.ErrorWriter("Distance to bug1"  + "\t"+"Distance to bug2" +"\n\r");

        setContentView(R.layout.activity_capture_tapping_2);
        setListeners();
    }

    private void setListeners() {

        final TextView mTextField = findViewById(R.id.accgraph_chrono4);
        findViewById(R.id.tapButton_2_1).setOnClickListener(this);
        findViewById(R.id.tapButton_2_2).setOnClickListener(this);



        new CountDownTimer(10000, 1000) {
            // Here, it is computed the difference between the last time in which
            // you push the button and the time you are pushing the button
            float timef = SystemClock.currentThreadTimeMillis(); //Last time

            public void onTick(long millisUntilFinished) {
                mTextField.setText(Long.toString(millisUntilFinished / 1000));
                time2 = SystemClock.currentThreadTimeMillis()-timef; // The difference between times
                timeStr = String.valueOf(time2);
            }

            public void onFinish() {
                mTextField.setText(getApplicationContext().getString(R.string.done));
                tappingrecorder.CloseTappingDocument();
                tappingrecorder.CloseErrorDocument();

                open_exercise();
            }
        }.start();

    }


    @Override
    public void onClick(View view) {
        //TODO: onclick




        final ConstraintLayout background = (ConstraintLayout) findViewById(R.id.backgroundTapping2);
        background.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                ImageButton tap1 = findViewById(R.id.tapButton_2_1);
                ImageButton tap2 = findViewById(R.id.tapButton_2_2);

                ConstraintLayout.LayoutParams params_bugDif= (ConstraintLayout.LayoutParams) tap1.getLayoutParams();
                ConstraintLayout.LayoutParams params_bugDif2= (ConstraintLayout.LayoutParams) tap2.getLayoutParams();

                //Capturing the button position

                float yTap1=(float) params_bugDif.topMargin;
                float xTap1=(float) params_bugDif.leftMargin;
                float yTap2=(float) params_bugDif2.topMargin;
                float xTap2=(float) params_bugDif2.leftMargin;

                //Screen position
                float yScreen=(float) ev.getY();
                float xScreen=(float) ev.getX();


                //The euclidean distance between the touch screen position
                // and the button to measure the error
                double distanceTouchButton1= Math.sqrt(Math.pow((xTap1-xScreen),2)+Math.pow((yTap1-yScreen),2)) ;
                double distanceTouchButton2= Math.sqrt(Math.pow((xTap2-xScreen),2)+Math.pow((yTap2-yScreen),2)) ;

                tappingrecorder.ErrorWriter(Double.toString(distanceTouchButton1)  + "\t"+Double.toString(distanceTouchButton2)+ "\n\r");


                return true;
            }
        });





        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        ImageButton tap1 = findViewById(R.id.tapButton_2_1);
        ImageButton tap2 = findViewById(R.id.tapButton_2_2);

        switch (view.getId()){
            case R.id.tapButton_2_1:

            vib.vibrate(100);
            change_button_position(tap1,tap2);
                tappingrecorder.TapWriter(timeStr  + '\t'+ "0"  + "\n\r"); // Writing the time between taps

                tappingrecorder.ErrorWriter("0" + "\t"+ "0"+"\n\r"); // Writing 0 if there is not error

                break;
            case R.id.tapButton_2_2:

            vib.vibrate(100);
            change_button_position(tap2,tap1);

                tappingrecorder.TapWriter(timeStr  + '\t'+ "1"  + "\n\r"); // Writing the time between taps

                tappingrecorder.ErrorWriter("0" + "\t"+ "0"+"\n\r"); // Writing 0 if there is not error

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
