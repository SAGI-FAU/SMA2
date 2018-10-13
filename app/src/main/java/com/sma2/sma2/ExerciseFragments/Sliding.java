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
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.SlidingRecorder;
import com.sma2.sma2.SignalRecording.TappingRecorder;
import com.sma2.sma2.ThanksActivity;


public class Sliding extends AppCompatActivity implements View.OnClickListener {
    SlidingRecorder slidingrecorder;
    private long time2;
    private String timeStr;
    private String [] data= new String[2];
    public String SlidingFileName;
    private int seekBarFlag=1;
    long timef;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        slidingrecorder = SlidingRecorder.getInstance(this);

        try {
            slidingrecorder.SlidingHeaderWriter("Sliding");
        } catch (Exception e) {
            Log.e("SlidingHeaderWriter", e.toString());
        }
        SlidingFileName=slidingrecorder.SlidingFileName();
        
        setContentView(R.layout.activity_capture_sliding);
        setListeners();
    }

    private void setListeners() {



        final TextView mTextField = findViewById(R.id.accgraph_chrono5);


        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar2);


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                ImageView imageView_limit=(ImageView) findViewById(R.id.imageView_limit);
                ConstraintLayout.LayoutParams params_bug_seek= (ConstraintLayout.LayoutParams)  seekBar.getLayoutParams();

                ConstraintLayout.LayoutParams params_bug= (ConstraintLayout.LayoutParams)  imageView_limit.getLayoutParams();



                float xSeek = seekBar.getMeasuredWidth() * seekBar.getProgress() /
                        seekBar.getMax() - seekBar.getThumbOffset(); //To obtain the positions of the seekBar

                float xBar=params_bug.getMarginStart(); // The indicator bar position
                ReachingTheBar(xSeek, xBar);



            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

                if (seekBarFlag==1) {

                    new CountDownTimer(10000, 1000) {
                        // Here, it is computed the difference between the last time in which
                        // you push the button and the time you are pushing the button

                        public void onTick(long millisUntilFinished) {
                            double newTime = (double) Math.round(millisUntilFinished / 100) / 10;
                            mTextField.setText(Long.toString(millisUntilFinished / 1000));

                            time2 = System.currentTimeMillis()-timef; // The difference between times
                            timeStr = String.valueOf(time2);

                        }

                        public void onFinish() {
                            mTextField.setText(getApplicationContext().getString(R.string.done));
                            try{
                                slidingrecorder.CloseSlidingDocument();
                            }catch (Exception e) {
                                Log.e("SlidingCloseWriter", e.toString());
                            }

                            open_exercise();
                        }
                    }.start();
                    timef = System.currentTimeMillis(); //Last time

                }
                seekBarFlag=0; //To deactivate the timer



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

    public void ReachingTheBar(Float xSeek, Float xBar){

        SeekBar seekBar = (SeekBar) findViewById(R.id.seekBar2);

        if(Math.abs(xSeek-xBar)<30){
            data[0]=Float.toString(xBar);
            data[1]=String.valueOf(System.currentTimeMillis()-timef); //Last time
            timef = System.currentTimeMillis(); //Last time
            Log.e("timef",String.valueOf(timef));




            slidingrecorder.SlidingWriter(data);
            Vibrator vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(100);
            ImageView imageView_limit=(ImageView) findViewById(R.id.imageView_limit);
            ConstraintLayout.LayoutParams params_bug= (ConstraintLayout.LayoutParams)  imageView_limit.getLayoutParams();
            ConstraintLayout.LayoutParams params_seekBar= (ConstraintLayout.LayoutParams)  seekBar.getLayoutParams();

            int xRandomBar= (int)(Math.random()*(seekBar.getWidth()-seekBar.getMax()))+
                    seekBar.getMinimumWidth();
            params_bug.setMarginStart(xRandomBar); // The indicator bar position
            params_bug.leftMargin=xRandomBar;
            imageView_limit.setLayoutParams(params_bug);

        }
    }


}
