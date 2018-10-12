package com.sma2.sma2.ExerciseFragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.MovementRecorder;

import java.io.IOException;


public class ExFreeWalking extends ExerciseFragment implements ButtonFragment.OnButtonInteractionListener{
    private static long COUNTDOWN = 10;
    private static long WALK_TIME = 10;
    private static final int SAMPLING_RATE = 100;
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
        ButtonFragment buttonFragment = new ButtonFragment();
        buttonFragment.setmListener(this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.replace(R.id.frameExWalking, buttonFragment);
        transaction.commit();
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
                long newTime = Math.round(millisUntilFinished / 1000);
                txtWalking.setText(String.valueOf(newTime));
            }
            public void onFinish() {
                this.cancel();
                txtWalking.setText(String.valueOf(COUNTDOWN));
                if((System.currentTimeMillis() - countdownStart) < COUNTDOWN * 1000){
                    return;
                } else {
                    buttonFrame.setClickable(false);
                    buttonFrame.setVisibility(View.INVISIBLE);
                    CountDownTimer walkingTimer = getWalkingTimer();
                    MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.bell);
                    mp.start();
                    try {
                        recorder = new MovementRecorder(getContext(), SAMPLING_RATE, mExercise.getName());
                    } catch (IOException e){
                        Log.e("MovementRecorder", e.toString());
                    }
                    filePath = recorder.getFileName();
                    recorder.registerListeners();
                    recorder.startLogging();
                    View decorView = getActivity().getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
                    decorView.setSystemUiVisibility(uiOptions);
                    walkingTimer.start();
                }
            }
        }.start();

    }

    private CountDownTimer getWalkingTimer() {
        return new CountDownTimer(WALK_TIME * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long newTime = Math.round(millisUntilFinished / 1000);
                txtWalking.setText(String.valueOf(newTime));
            }
            public void onFinish() {
                try {
                    recorder.stop();
                } catch (IOException e) {
                    Log.e("MovementRecorder", e.toString());
                }
                MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.bell);
                mp.start();
                View decorView = getActivity().getWindow().getDecorView();
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(uiOptions);
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
