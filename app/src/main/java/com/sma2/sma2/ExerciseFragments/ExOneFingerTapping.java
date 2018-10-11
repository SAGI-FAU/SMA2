package com.sma2.sma2.ExerciseFragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.os.Vibrator;
import android.support.constraint.ConstraintLayout;
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
    private float time2;
    private String timeStr;
    private TextView chronoText;
    private ConstraintLayout background;
    private ImageButton tapButton;
    int screenHeight, screenWidth;

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
        filePath = tappingrecorder.prepare("Tapping1");
        tappingrecorder.TapWriter("Tap Time (ms)"  + "\n\r");
        tappingrecorder.ErrorWriter("Distance to bug"  + "\n\r");
        chronoText = view.findViewById(R.id.chronoTapOne);
        background = view.findViewById(R.id.exOneTapBackground);
        view.findViewById(R.id.bugTapOne).setOnClickListener(this);
        tapButton = view.findViewById(R.id.bugTapOne);
        tapButton.setOnClickListener(this);
        getDisplayDimensions();
        setTimer();
        return view;
    }

    private void setTimer() {
        new CountDownTimer(10000, 1000) {

            // Here, it is computed the difference between the last time in which
            // you push the button and the time you are pushing the button
            float timef = SystemClock.currentThreadTimeMillis(); //Last time

            public void onTick(long millisUntilFinished) {
                chronoText.setText(Long.toString(millisUntilFinished / 1000));
                time2 = SystemClock.currentThreadTimeMillis()-timef; // The difference between times
                timeStr = String.valueOf(time2);
            }
            public void onFinish() {
                chronoText.setText(getActivity().getApplicationContext().getString(R.string.done));
                tappingrecorder.CloseTappingDocument();
                tappingrecorder.CloseErrorDocument();
                mListener.onExerciseFinished(filePath);
            }
        }.start();
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
        background.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent ev) {
                ConstraintLayout.LayoutParams params_bugDif= (ConstraintLayout.LayoutParams) tapButton.getLayoutParams();
                //Capturing the button position

                float yTap1=(float) params_bugDif.topMargin;
                float xTap1=(float) params_bugDif.leftMargin;

                //Screen position
                float yScreen= ev.getY();
                float xScreen= ev.getX();


                //The euclidean distance between the touch screen position
                // and the button to measure the error
                double distanceTouchButton= Math.sqrt(Math.pow((xTap1-xScreen),2)+Math.pow((yTap1-yScreen),2)) ;

                tappingrecorder.ErrorWriter(Double.toString(distanceTouchButton)  + "\n\r");
                return true;
            }
        });

        if (view.getId()==R.id.bugTapOne){
            Vibrator vib = (Vibrator) getActivity().getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
            // Vibrate for 500 milliseconds
            vib.vibrate(100);
            tappingrecorder.TapWriter(timeStr  + "\n\r"); // Writing the time between taps
            tappingrecorder.ErrorWriter("0" + "\n\r"); // Writing 0 if there is not error

            change_button_position();

        }
    }

    public void change_button_position(){
        ConstraintLayout.LayoutParams params_bug= (ConstraintLayout.LayoutParams)  tapButton.getLayoutParams();
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
