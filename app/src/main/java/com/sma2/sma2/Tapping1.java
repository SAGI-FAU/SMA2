package com.sma2.sma2;


import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.SystemClock;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.content.Context;
import android.os.Vibrator;

import com.sma2.sma2.SignalRecording.TappingRecorder;

public class Tapping1 extends AppCompatActivity implements View.OnClickListener {

    String data="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_capture_tapping_1);
        setListeners();
    }

    private void setListeners() {

        findViewById(R.id.tapButton_1).setOnClickListener(this);
        final TextView mTextField = findViewById(R.id.accgraph_chrono3);


        new CountDownTimer(10000, 1000) {
            float timef = SystemClock.currentThreadTimeMillis();

            public void onTick(long millisUntilFinished) {
                mTextField.setText(Long.toString(millisUntilFinished / 1000));

                float time2 = SystemClock.currentThreadTimeMillis()-timef;
                String timeStr = String.valueOf(time2);
                Log.e("values",timeStr);




            }

            public void onFinish() {
                mTextField.setText(getApplicationContext().getString(R.string.done));

                open_exercise();
            }
        }.start();

    }


    @Override
    public void onClick(View view) {


        ImageButton tap1 = findViewById(R.id.tapButton_1);





        if (view.getId()==R.id.tapButton_1){

            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            vib.vibrate(100);


            TappingRecorder tappingrecorder= new TappingRecorder();
            tappingrecorder.Record("Tapping1",data);


            change_button_position(tap1);

        }


    }

    public void open_exercise(){
        Intent intent_ex1;
        intent_ex1 =new Intent(this, ThanksActivity.class);
        startActivity(intent_ex1);
    }


    public void change_button_position(ImageButton imageButton){
        ConstraintLayout.LayoutParams params_bug= (ConstraintLayout.LayoutParams)  imageButton.getLayoutParams();
        int y= (int)(Math.random()*((800)));
        int x= (int)(Math.random()*((600)));

        params_bug.topMargin=y;
        params_bug.leftMargin=x;
        params_bug.setMarginStart(x);
        imageButton.setLayoutParams(params_bug);

    }

}
