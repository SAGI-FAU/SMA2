package com.sma2.sma2.ExerciseFragments;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;


import com.sma2.sma2.SignalRecording.TappingRecorder;

import com.sma2.sma2.R;
import com.sma2.sma2.ThanksActivity;

import java.io.File;
import java.io.FileNotFoundException;


public class Tapping2 extends AppCompatActivity implements View.OnClickListener {
    TappingRecorder tappingrecorder;
    private float time2;
    private String timeStr;
    private String [] data= new String[4];
    public String TappingFileName;
    int screenHeight, screenWidth;
    long timef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;

        tappingrecorder = TappingRecorder.getInstance(this);

        try {
            tappingrecorder.TapHeaderWriter("Two finger tapping", 1);
        } catch (Exception e) {
            Log.e("Tapping2HeaderWriter", e.toString());
        }
        TappingFileName=tappingrecorder.TappingFileName();


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

            public void onTick(long millisUntilFinished) {
                mTextField.setText(Long.toString(millisUntilFinished / 1000));
                time2 = System.currentTimeMillis()-timef; // The difference between times
                timeStr = String.valueOf(time2);
            }

            public void onFinish() {
                mTextField.setText(getApplicationContext().getString(R.string.done));
                try{
                    tappingrecorder.CloseTappingDocument();
                }catch (Exception e) {
                    Log.e("Tapping2CloseWriter", e.toString());
                }


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

                data[0]="0";
                data[1]=timeStr;
                data[2]=Double.toString(distanceTouchButton1);
                data[3]=Double.toString(distanceTouchButton2);
                timef = System.currentTimeMillis(); //Last time

                tappingrecorder.TapWriter(data);



                return true;
            }
        });





        Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        ImageButton tap1 = findViewById(R.id.tapButton_2_1);
        ImageButton tap2 = findViewById(R.id.tapButton_2_2);

        switch (view.getId()){

            //Case Left Finger
            case R.id.tapButton_2_1:

            vib.vibrate(100);
            change_button_position(tap1,0);
                data[0]="1";
                data[1]=timeStr;
                data[2]="0";
                data[3]="0";
                timef = System.currentTimeMillis(); //Last time


                tappingrecorder.TapWriter(data);

                break;

            //Case Right Finger
            case R.id.tapButton_2_2:

            vib.vibrate(100);
            change_button_position(tap2,1);
                data[0]="2";
                data[1]=timeStr;
                data[2]="0";
                data[3]="0";
                timef = System.currentTimeMillis(); //Last time


                tappingrecorder.TapWriter(data);

                break;
        }

    }

    public void open_exercise(){
        Intent intent_ex1;
        intent_ex1 =new Intent(this, ThanksActivity.class);
        startActivity(intent_ex1);
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;


    }

    public static int getScreenHeight() {


        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public void change_button_position(ImageButton imageButton, int bugFlag){
        ConstraintLayout.LayoutParams params_bug= (ConstraintLayout.LayoutParams)  imageButton.getLayoutParams();




        int bugHeight = imageButton.getHeight();
        int bugWidth = imageButton.getWidth();
        screenHeight=getScreenHeight();
        screenWidth=getScreenWidth();

        int y = (int)(Math.random()*((screenHeight - 2*bugHeight)));


        int x;

        if (bugFlag==0){
            x=(int)(Math.random()*((screenWidth - 2*bugWidth)/2));


        }else{
            x=(int)(Math.random()*(int)((screenWidth - 2*bugWidth)/2))+
                    (int)(screenWidth)/2;



        }



        params_bug.topMargin=y;
        params_bug.leftMargin=x;

        params_bug.setMarginStart(x);
        imageButton.setLayoutParams(params_bug);


    }




}

