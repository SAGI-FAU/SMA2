package com.sma2.apkinson.ExerciseFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.sma2.apkinson.DataAccess.FeatureDataService;
import com.sma2.apkinson.FeatureExtraction.Movement.CSVFileReader;
import com.sma2.apkinson.FeatureExtraction.Movement.MovementProcessing;
import com.sma2.apkinson.R;
import com.sma2.apkinson.SignalRecording.CSVFileWriter;
import com.sma2.apkinson.SignalRecording.MovementRecorder;

import java.io.File;
import java.util.Date;


public class Ex_balance_Rec extends ExerciseFragment implements ButtonFragment.OnButtonInteractionListener {
    private MovementRecorder recorder;
    private static long START_COUNTDOWN = 3;
    private static long EXERCISE_TIME = 30;
    private final int SAMPLING_FREQUENCY = 10000;
    private String countdown_finished_txt;
    private long countdownStart;
    private CountDownTimer timer;
    private TextView countdownTextView;
    private boolean countdownIsRunning = false;
    private MovementProcessing MovementProcessor=new MovementProcessing();
    private CSVFileReader FileReader;
    FeatureDataService FeatureDataService;
    SharedPreferences sharedPref;
    public Ex_balance_Rec() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        FileReader = new CSVFileReader(getActivity().getApplicationContext());
        View view = inflater.inflate(R.layout.fragment_ex_balance, container, false);
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
            }
        }
    }

    private void startInitialCountdownTimer() {
        countdownIsRunning = true;
        countdownStart = System.currentTimeMillis();
        timer = new CountDownTimer(START_COUNTDOWN * 1000, 1000) {
            public void onTick(long millisUntilFinished) {
                int newTime =  Math.round(millisUntilFinished / 1000);
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


    private void EvaluateFeatures(){
        String Route= CSVFileWriter.getpath();
        String final_route = Route +"/" + recorder.getFileName();
        float balance = MovementProcessor.UppTremor(FileReader,final_route);
        File file = new File(final_route);
        Date lastModDate = new Date(file.lastModified());
        FeatureDataService.save_feature(FeatureDataService.posture_name, lastModDate, balance);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("New Area Movement", true);
        editor.apply();

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

                try {
                    EvaluateFeatures();
                }
                catch (Exception e){
                    Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.failed),Toast.LENGTH_SHORT).show();
                }

                mListener.onExerciseFinished(recorder.getFileName());
                MediaPlayer mp = MediaPlayer.create(getContext(), R.raw.bell);
                mp.start();
                Vibrator vib = (Vibrator) getContext().getSystemService(Context.VIBRATOR_SERVICE);
                vib.vibrate(1000);
            }
        }.start();
    }
}
