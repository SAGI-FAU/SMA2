package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.sma2.sma2.DataAccess.FeatureDataService;
import com.sma2.sma2.FeatureExtraction.Movement.CSVFileReader;
import com.sma2.sma2.FeatureExtraction.Movement.MovementProcessing;
import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.CSVFileWriter;
import com.sma2.sma2.SignalRecording.MovementRecorder;

import java.io.File;
import java.util.Date;


public class Ex_Circling_Rec extends ExerciseFragment implements ButtonFragment.OnButtonInteractionListener {
    private MovementRecorder recorder;
    private static long START_COUNTDOWN = 3;
    private static long EXERCISE_TIME = 30;
    private final int SAMPLING_FREQUENCY = 10000;
    private String countdown_finished_txt;
    private long countdownStart;
    private CountDownTimer timer;
    private TextView countdownTextView;
    private boolean countdownIsRunning = false;

    private CSVFileReader FileReader;
    private MovementProcessing MovementProcessor = new MovementProcessing();
    FeatureDataService FeatureDataService;
    SharedPreferences sharedPref;
    public Ex_Circling_Rec() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FileReader = new CSVFileReader(getActivity().getApplicationContext());
        try {
            recorder = new MovementRecorder(this.getContext(), SAMPLING_FREQUENCY, mExercise.getName());
            recorder.registerListeners();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_circling, container, false);
        ButtonFragment buttonFragment = new ButtonFragment();
        buttonFragment.setmListener(this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        sharedPref =PreferenceManager.getDefaultSharedPreferences(getActivity());

        transaction.replace(R.id.frameExSV, buttonFragment);
        transaction.commit();
        countdown_finished_txt = getResources().getString(R.string.start2);
        countdownTextView = view.findViewById(R.id.countdownTimerTextView);
        countdownTextView.setText(String.valueOf(START_COUNTDOWN));
        FeatureDataService=new FeatureDataService(getActivity().getApplicationContext());
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            recorder.stop();
            recorder = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onButtonInteraction(boolean start) {
        if (start) {
            recorder.startLogging();
            startInitialCountdownTimer();
        } else {
            if(countdownIsRunning) {
                countdownIsRunning = false;
                timer.cancel();
                countdownTextView.setText(String.valueOf(START_COUNTDOWN));
            } else {
                try {
                    recorder.stop();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                EvaluateFeatures();

                mListener.onExerciseFinished(recorder.getFileName());
            }
        }
    }

    private void EvaluateFeatures() {
        String Route= CSVFileWriter.getpath();
        String final_route = Route + recorder.getFileName();
        float UppRegularity = MovementProcessor.UppRegularity(FileReader,final_route);
        File file = new File(final_route);
        Date lastModDate = new Date(file.lastModified());

        if(mExercise.getId() == 23){
            String name=FeatureDataService.regularity_circles_right_name;
            FeatureDataService.save_feature(name, lastModDate, UppRegularity);
        }
        else if(mExercise.getId() == 24){
            String name=FeatureDataService.regularity_circles_left_name;
            FeatureDataService.save_feature(name, lastModDate, UppRegularity);
        }

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("New Area Movement", true);
        editor.apply();

    }

    private void startInitialCountdownTimer() {
        countdownIsRunning = true;
        countdownStart = System.currentTimeMillis();
        timer = new CountDownTimer(START_COUNTDOWN * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                int newTime = Math.round(millisUntilFinished / 1000);
                countdownTextView.setText(String.valueOf(newTime));
            }
            public void onFinish() {
                countdownIsRunning = false;
                this.cancel();
                //countdownTextView.setText(countdown_finished_txt);
                MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.bell);
                mp.start();
                Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(1000);
                recorder.startLogging();
                startSecondCountdownTimer();

            }
        }.start();

    }


    private void startSecondCountdownTimer() {
        countdownIsRunning = true;
        countdownStart = System.currentTimeMillis();
        timer = new CountDownTimer(EXERCISE_TIME* 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                int newTime =  Math.round(millisUntilFinished / 1000);
                countdownTextView.setText(String.valueOf(newTime));
            }
            public void onFinish() {
                countdownIsRunning = false;
                this.cancel();
                //countdownTextView.setText(countdown_finished_txt);

                EvaluateFeatures();
                mListener.onExerciseFinished(recorder.getFileName());
                MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.bell);
                mp.start();
                Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(1000);
            }
        }.start();
    }

}
