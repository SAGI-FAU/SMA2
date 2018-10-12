package com.sma2.sma2;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sma2.sma2.ExerciseFragments.ButtonFragment;
import com.sma2.sma2.ExerciseFragments.ExerciseFragment;
import com.sma2.sma2.SignalRecording.MovementRecorder;


public class ExFreeWalking extends ExerciseFragment implements ButtonFragment.OnButtonInteractionListener{
    private static long COUNTDOWN = 10;
    private static long WALK_TIME = 120;
    private TextView txtWalking;
    private CountDownTimer timer;
    private MovementRecorder recorder;
    private long countdownStart;
    private FrameLayout buttonFrame;


    public ExFreeWalking() {
        // Required empty public constructor
    }

    public static ExFreeWalking newInstance(String param1, String param2) {
        ExFreeWalking fragment = new ExFreeWalking();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_free_walking, container, false);
        txtWalking = view.findViewById(R.id.txtWalking);
        txtWalking.setText(String.valueOf(COUNTDOWN));
        buttonFrame = view.findViewById(R.id.frameExWalking);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void startTimer() {
        countdownStart = System.currentTimeMillis();
        timer = new CountDownTimer(COUNTDOWN * 1000, 100) {
            public void onTick(long millisUntilFinished) {
                double newTime = (double) Math.round(millisUntilFinished / 100) / 10;
                txtWalking.setText(String.valueOf(newTime));
            }
            public void onFinish() {
                this.cancel();
                txtWalking.setText(String.valueOf(COUNTDOWN));
                if((System.currentTimeMillis() - countdownStart) < COUNTDOWN){
                    return;
                } else {
                    buttonFrame.setClickable(false);
                    buttonFrame.setVisibility(View.INVISIBLE);
                    CountDownTimer walkingTimer = getWalkingTimer();
                    MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.bell);
                    mp.start();
                    //recorder = new MovementRecorder(getContext(), )
                }
            }
        }.start();

    }

    private CountDownTimer getWalkingTimer() {
        return new CountDownTimer(WALK_TIME * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long newTime = Math.round(millisUntilFinished / 1000);
                txtWalking.setText(String.valueOf(WALK_TIME - newTime));
            }
            public void onFinish() {
                txtWalking.setText(getString(R.string.done));
                mListener.onExerciseFinished(filePath);
            }
        };
    }

    @Override
    public void onButtonInteraction(boolean start) {
        if(start){
            startTimer();
        } else {
            timer.onFinish();
        }
    }
}
