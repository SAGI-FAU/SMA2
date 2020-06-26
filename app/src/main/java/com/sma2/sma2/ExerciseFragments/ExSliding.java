package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.sma2.sma2.DataAccess.FeatureDataService;
import com.sma2.sma2.FeatureExtraction.Tapping.FeatureTapping;
import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.SlidingRecorder;

import java.io.File;
import java.util.Date;

public class ExSliding extends ExerciseFragment implements SeekBar.OnSeekBarChangeListener {
    private SlidingRecorder slidingrecorder;
    private TextView chronoText;
    private ImageView imageView_limit;
    private TextView textView_limit;
    private SeekBar bar_sliding;
    private int screenHeight, screenWidth;
    private long lastTime;
    private CountDownTimer timer;
    private static final long TOTAL_TIME =  10000;
    private int threshold=30;
    private int seekBarFlag=1;
    SharedPreferences sharedPref;
    FeatureTapping featureTapping;
    FeatureDataService FeatureDataService;
    final String PATH = Environment.getExternalStorageDirectory() + "/Apkinson/MOVEMENT/";



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_sliding, container, false);
        slidingrecorder = SlidingRecorder.getInstance(getContext());
        try {
            slidingrecorder.SlidingHeaderWriter(mExercise.getName());
        }catch (Exception e) {
            Log.e("SlidingHeaderWriter", e.toString());
        }

        filePath = slidingrecorder.SlidingFileName();
        chronoText = view.findViewById(R.id.chronoSliding);
        chronoText.setText(String.valueOf((float) TOTAL_TIME / 1000));
        bar_sliding = view.findViewById(R.id.bar_sliding);
        bar_sliding.setOnSeekBarChangeListener(this);
        imageView_limit=view.findViewById(R.id.imageView_limit);
        textView_limit=view.findViewById(R.id.textView_limit);
        getDisplayDimensions();
        timer = setTimer();
        sharedPref =PreferenceManager.getDefaultSharedPreferences(getActivity());
        FeatureDataService=new FeatureDataService(getActivity().getApplicationContext());
        featureTapping=new FeatureTapping(getActivity().getApplicationContext());

        return view;
    }


    private CountDownTimer setTimer() {
        return new CountDownTimer(TOTAL_TIME, 100) {

            // Here, it is computed the difference between the last time in which
            // you push the button and the time you are pushing the button

            public void onTick(long millisUntilFinished) {
                double newTime = (double) Math.round(millisUntilFinished / 100) / 10;
                chronoText.setText(String.valueOf(newTime));

            }
            public void onFinish() {
                chronoText.setText(getActivity().getApplicationContext().getString(R.string.done));
                try {
                    slidingrecorder.CloseSlidingDocument();
                }catch (Exception e) {
                    Log.e("SlidingCloseWriter", e.toString());
                }

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("New Area Tapping", true);
                editor.apply();

                try {
                    EvaluateFeatures();
                }
                catch (Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.failed),Toast.LENGTH_SHORT).show();
                }
                mListener.onExerciseFinished(filePath);

            }
        };
    }



    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {

        ConstraintLayout.LayoutParams params_bug= (ConstraintLayout.LayoutParams)  imageView_limit.getLayoutParams();

        float xSeek = seekBar.getMeasuredWidth() * seekBar.getProgress() /
                seekBar.getMax() - seekBar.getThumbOffset(); //To obtain the positions of the seekBar

        float xBar=params_bug.getMarginStart(); // The indicator bar position
        ReachingTheBar(xSeek, xBar);


    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

        if (seekBarFlag==1) {
            lastTime = System.currentTimeMillis();
            timer.start();
        }
        seekBarFlag=0; //To deactivate the timer
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onDetach() {
        super.onDetach();
        timer.cancel();
    }





    private void getDisplayDimensions() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }



    public void ReachingTheBar(Float xSeek, Float xBar){

        int textView_limitWidth = textView_limit.getWidth();

        if(Math.abs(xSeek-xBar)<threshold){
            String[] data = new String[2];
            data[0]=Float.toString(xBar);
            data[1]=String.valueOf(System.currentTimeMillis()-lastTime); // The difference between times
            lastTime = System.currentTimeMillis(); //Last Tap time


            slidingrecorder.SlidingWriter(data); //Writing the data
            Vibrator vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
            vib.vibrate(200);

            ConstraintLayout.LayoutParams params_line= (ConstraintLayout.LayoutParams)  imageView_limit.getLayoutParams();
            ConstraintLayout.LayoutParams params_text= (ConstraintLayout.LayoutParams)  textView_limit.getLayoutParams();

            int xRandomBar= (int)(Math.random()*(bar_sliding.getWidth()-bar_sliding.getMax()))+bar_sliding.getMinimumWidth();


            int xTextbar=xRandomBar-textView_limitWidth/2;

            if (xTextbar+textView_limitWidth/2>=screenWidth){
                xTextbar=xRandomBar-textView_limitWidth-8;
            }
            else if (xTextbar<=0){
                xTextbar=8;
            }

            params_line.setMarginStart(xRandomBar); // The indicator bar position
            params_line.leftMargin=xRandomBar;
            params_line.setMarginStart(xRandomBar);
            imageView_limit.setLayoutParams(params_line);

            params_text.setMarginStart(xRandomBar);
            params_text.leftMargin=xTextbar;
            params_text.setMarginStart(xTextbar);
            textView_limit.setLayoutParams(params_text);

        }
    }

    private void EvaluateFeatures() {
        File file = new File(PATH+filePath);
        Date lastModDate = new Date(file.lastModified());

        float features=featureTapping.feat_sliding(PATH+filePath);
        FeatureDataService.save_feature(FeatureDataService.perc_sliding_name, lastModDate, features);

    }



}
