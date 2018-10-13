package com.sma2.sma2.ExerciseFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.TappingRecorder;


public class ExOneFingerTapping extends ExerciseFragment implements View.OnClickListener {
    private TappingRecorder tappingrecorder;
    private TextView chronoText;
    private ConstraintLayout background;
    private ImageButton tapButton;
    int screenHeight, screenWidth;
    long startTime;

    public ExOneFingerTapping() {
        // Required empty public constructor
    }

    public static ExOneFingerTapping newInstance(String param1, String param2) {
        ExOneFingerTapping fragment = new ExOneFingerTapping();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_one_finger_tapping, container, false);
        tappingrecorder = TappingRecorder.getInstance(getContext());
        try {
            tappingrecorder.TapHeaderWriter(mExercise.getName(), 0);
        }catch (Exception e) {
            Log.e("Tapping1HeaderWriter", e.toString());
        }

        filePath = tappingrecorder.TappingFileName();
        chronoText = view.findViewById(R.id.chronoTapOne);
        background = view.findViewById(R.id.backgroundExTapOne);
        view.findViewById(R.id.bugTapOne).setOnClickListener(this);
        tapButton = view.findViewById(R.id.bugTapOne);
        tapButton.setOnClickListener(this);
        getDisplayDimensions();
        setListener();
        setTimer();

        return view;
    }

    private void setListener() {
        background.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                    ConstraintLayout.LayoutParams params_bugDif = (ConstraintLayout.LayoutParams) tapButton.getLayoutParams();
                    //Capturing the button position
                    String[] data = new String[3];
                    float yTap1 = (float) params_bugDif.topMargin;
                    float xTap1 = (float) params_bugDif.leftMargin;

                    //Screen position
                    float yScreen = ev.getY();
                    float xScreen = ev.getX();


                    //The euclidean distance between the touch screen position
                    // and the button to measure the error
                    double distanceTouchButton = Math.sqrt(Math.pow((xTap1 - xScreen), 2) + Math.pow((yTap1 - yScreen), 2));

                    data[0] = "0";
                    data[1] = String.valueOf(System.currentTimeMillis() - startTime);
                    data[2] = Double.toString(distanceTouchButton);
                    startTime = System.currentTimeMillis(); //To update the initial time


                    tappingrecorder.TapWriter(data);
                    return true;
                }
                return true;
            }
        });
    }

    private void setTimer() {
        new CountDownTimer(10000, 100) {

            // Here, it is computed the difference between the last time in which
            // you push the button and the time you are pushing the button
            //float timef = SystemClock.currentThreadTimeMillis(); //Last time

            public void onTick(long millisUntilFinished) {
                double newTime = (double) Math.round(millisUntilFinished / 100) / 10;
                chronoText.setText(String.valueOf(newTime));
                //time2 = SystemClock.currentThreadTimeMillis()-timef; // The difference between times
                //timeStr = String.valueOf(time2);
            }
            public void onFinish() {
                chronoText.setText(getActivity().getApplicationContext().getString(R.string.done));
                try {
                    tappingrecorder.CloseTappingDocument();
                }catch (Exception e) {
                    Log.e("Tapping1CloseWriter", e.toString());
                }
                mListener.onExerciseFinished(filePath);
            }
        }.start();
        startTime = System.currentTimeMillis();
    }

    private void getDisplayDimensions() {
        Display display = getActivity().getWindowManager().getDefaultDisplay();

        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onClick(View view) {
        String [] data= new String[3];
        Vibrator vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        vib.vibrate(200);
        data[0]="1";
        data[1]=String.valueOf(System.currentTimeMillis() - startTime);
        data[2]="0";
        startTime = System.currentTimeMillis();
        tappingrecorder.TapWriter(data);
        change_button_position();
    }

    public void change_button_position(){
        ConstraintLayout.LayoutParams params_bug = (ConstraintLayout.LayoutParams)  tapButton.getLayoutParams();
        int bugHeight = tapButton.getHeight();
        int bugWidth = tapButton.getWidth();
        int x = (int)(Math.random()*((screenWidth - bugWidth)));
        int y = (int)(Math.random()*((screenHeight - bugHeight)));
        params_bug.topMargin=y;
        params_bug.leftMargin=x;
        params_bug.setMarginStart(x);
        tapButton.setLayoutParams(params_bug);
    }
}
