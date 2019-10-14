package com.sma2.sma2.ExerciseFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sma2.sma2.DataAccess.FeatureDataService;
import com.sma2.sma2.FeatureExtraction.Tapping.FeatureTapping;
import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.TappingRecorder;

import java.io.File;
import java.util.Date;


public class ExTwoFingerTapping extends ExerciseFragment implements View.OnClickListener{

    private TappingRecorder tappingrecorder;
    private TextView chronoText;
    private ConstraintLayout background;
    private ImageButton tapButton_left, tapButton_right;
    private int screenHeight, screenWidth;
    private long lastTime;
    private CountDownTimer timer;
    private static final long TOTAL_TIME =  10000;
    SharedPreferences sharedPref;
    FeatureDataService FeatureDataService;
    FeatureTapping featureTapping;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_two_finger_tapping, container, false);
        tappingrecorder = TappingRecorder.getInstance(getContext());
        try {
            tappingrecorder.TapHeaderWriter(mExercise.getName(), 1);
        }catch (Exception e) {
            Log.e("Tapping2HeaderWriter", e.toString());
        }
        sharedPref =PreferenceManager.getDefaultSharedPreferences(getActivity());

        filePath = tappingrecorder.TappingFileName();
        chronoText = view.findViewById(R.id.chronoTapTwo);
        chronoText.setText(String.valueOf((float) TOTAL_TIME / 1000));
        background = view.findViewById(R.id.backgroundExTapTwo);
        view.findViewById(R.id.bugTapTwoLeft).setOnClickListener(this);
        view.findViewById(R.id.bugTapTwoRight).setOnClickListener(this);

        tapButton_left = view.findViewById(R.id.bugTapTwoLeft);
        tapButton_left.setOnClickListener(this);
        tapButton_right = view.findViewById(R.id.bugTapTwoRight);
        tapButton_right.setOnClickListener(this);
        getDisplayDimensions();
        setListener();
        timer = setTimer();
        change_button_position(tapButton_left, 0);
        change_button_position(tapButton_right, 1);
        FeatureDataService=new FeatureDataService(getActivity().getApplicationContext());
        featureTapping=new FeatureTapping(getActivity().getApplicationContext());

        return view;
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setListener() {
        background.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    ConstraintLayout.LayoutParams params_bugDif_left = (ConstraintLayout.LayoutParams) tapButton_left.getLayoutParams();
                    ConstraintLayout.LayoutParams params_bugDif_right = (ConstraintLayout.LayoutParams) tapButton_right.getLayoutParams();


                    //Capturing the button position
                    String[] data = new String[4];
                    float yTap1=(float) params_bugDif_left.topMargin;
                    float xTap1=(float) params_bugDif_left.leftMargin;
                    float yTap2=(float) params_bugDif_right.topMargin;
                    float xTap2=(float) params_bugDif_right.leftMargin;

                    //Screen position
                    float yScreen = ev.getY();
                    float xScreen = ev.getX();

                    //The euclidean distance between the touch screen position
                    // and the button to measure the error
                    double distanceTouchButton_left= Math.sqrt(Math.pow((xTap1-xScreen),2)+Math.pow((yTap1-yScreen),2)) ;
                    double distanceTouchButton_right= Math.sqrt(Math.pow((xTap2-xScreen),2)+Math.pow((yTap2-yScreen),2)) ;

                    data[0]="0";
                    data[1]= String.valueOf(System.currentTimeMillis()-lastTime); // The difference between times
                    data[2]=Double.toString(distanceTouchButton_left);
                    data[3]=Double.toString(distanceTouchButton_right);
                    lastTime = System.currentTimeMillis(); //Last Tap time

                    tappingrecorder.TapWriter(data);

                    return true;
                }
                return true;
            }
        });
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
                    tappingrecorder.CloseTappingDocument();
                }catch (Exception e) {
                    Log.e("Tapping2CloseWriter", e.toString());
                }
                mListener.onExerciseFinished(filePath);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean("New Area Tapping", true);
                editor.apply();
                EvaluateFeatures();

            }
        };
    }

    private void getDisplayDimensions() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }


    public void change_button_position(ImageButton imageButton, int bugFlag){
        ConstraintLayout.LayoutParams params_bug= (ConstraintLayout.LayoutParams)  imageButton.getLayoutParams();

        int bugHeight = imageButton.getHeight();
        int bugWidth = imageButton.getWidth();

        int y = (int)(Math.random()*((screenHeight - 2*bugHeight)));

        int x;
        //Depends on the button, the position is different, Right or Left
        if (bugFlag==0){
            //Lady Bug in the Left
            x=(int)(Math.random()*((screenWidth - 2*bugWidth)/2));
        }else{
            //Lady Bug in the Right
            x=(int)(Math.random()*((screenWidth - 2*bugWidth)/2))+(screenWidth)/2;
        }
        if(bugHeight == 0 && bugWidth == 0){
            x = (int)(Math.random()*((screenWidth * 0.8)));
            y = (int)(Math.random()*((screenHeight * 0.8)));
        }

        //Changing the button position
        params_bug.topMargin=y;
        params_bug.leftMargin=x;

        params_bug.setMarginStart(x);
        imageButton.setLayoutParams(params_bug);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClick(View view) {
        Vibrator vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        String[] data = new String[4];
        if (!recording){
            recording = true;
            lastTime = System.currentTimeMillis();
            timer.start();
            vib.vibrate(200);
            change_button_position(tapButton_left, 0);
            change_button_position(tapButton_right, 1);
        } else {

            switch (view.getId()){

                //Case Left Finger
                case R.id.bugTapTwoLeft:
                    vib.vibrate(200);
                    change_button_position(tapButton_left,0);

                    data[0]="1";
                    data[1]=String.valueOf(System.currentTimeMillis()-lastTime); // The difference between times;
                    data[2]="0";
                    data[3]="0";
                    lastTime = System.currentTimeMillis(); //Last Tap time
                    tappingrecorder.TapWriter(data);
                    break;

                //Case Right Finger
                case R.id.bugTapTwoRight:
                    vib.vibrate(200);
                    change_button_position(tapButton_right,1);
                    data = new String[4];
                    data[0]="2";
                    data[1]=String.valueOf(System.currentTimeMillis()-lastTime); // The difference between times;
                    data[2]="0";
                    data[3]="0";
                    lastTime = System.currentTimeMillis(); //Last Tap time

                    tappingrecorder.TapWriter(data);
                    break;
            }
        }
    }

    private void EvaluateFeatures() {
        File file = new File(filePath);
        Date lastModDate = new Date(file.lastModified());

        float[] features=featureTapping.feat_tapping_two(filePath);
        FeatureDataService.save_feature(FeatureDataService.perc_tapping2_name, lastModDate, features[0]);
        FeatureDataService.save_feature(FeatureDataService.veloc_tapping2_name, lastModDate, features[1]);
        FeatureDataService.save_feature(FeatureDataService.precision_tapping2_name, lastModDate, features[2]);

    }


    @Override
    public void onDetach() {
        super.onDetach();
        timer.cancel();
    }


}
