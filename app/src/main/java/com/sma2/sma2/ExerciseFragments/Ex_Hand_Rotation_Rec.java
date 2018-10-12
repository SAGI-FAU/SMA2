package com.sma2.sma2.ExerciseFragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.MovementRecorder;


public class Ex_Hand_Rotation_Rec extends ExerciseFragment implements ButtonFragment.OnButtonInteractionListener {
    private MovementRecorder recorder;
    private static long START_COUNTDOWN = 3;
    private static long EXERCISE_TIME = 30;
    private long countdownStart;
    private CountDownTimer timer;
    private TextView countdownTextView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_hand_rotation_rec, container, false);
        ButtonFragment buttonFragment = new ButtonFragment();
        buttonFragment.setmListener(this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.replace(R.id.frameExSV, buttonFragment);
        transaction.commit();

        countdownTextView = view.findViewById(R.id.txtWalking);
        countdownTextView.setText(String.valueOf(START_COUNTDOWN));
        try {
            recorder = new MovementRecorder(this.getContext(), 10000, mExercise.getName());
            recorder.registerListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //recorder.stop();
    }

    @Override
    public void onButtonInteraction(boolean start) {
        if (start) {
            recorder.startLogging();
            startTimer();
        } else {
            try {
                recorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mListener.onExerciseFinished(recorder.getFileName());
        }
    }

    private void startTimer() {
        countdownStart = System.currentTimeMillis();
        timer = new CountDownTimer(START_COUNTDOWN * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                int newTime = (int) Math.round(millisUntilFinished / 1000);
                countdownTextView.setText(String.valueOf(newTime));
            }
            public void onFinish() {
                this.cancel();
                countdownTextView.setText("START!");
                MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.bell);
                mp.start();

            }
        }.start();

    }

    private CountDownTimer getWalkingTimer() {
        return new CountDownTimer(EXERCISE_TIME * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                long newTime = Math.round(millisUntilFinished / 1000);
                countdownTextView.setText(String.valueOf(EXERCISE_TIME - newTime));
            }
            public void onFinish() {
                countdownTextView.setText(getString(R.string.done));
                mListener.onExerciseFinished(filePath);
            }
        };
    }

}
