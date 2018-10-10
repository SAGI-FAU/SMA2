package com.sma2.sma2.ExerciseFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.SpeechRecorder;

import java.io.File;


public class ExAudioRec extends ExerciseFragment implements View.OnClickListener, ButtonFragment.OnButtonInteractionListener{
    private Button startButton;
    private Button doneButton;
    private Button redoButton;
    private ProgressBar volumeBar;
    private SpeechRecorder recorder;

    public ExAudioRec() {
        // Required empty public constructor
    }

    public static ExAudioRec newInstance() {
        ExAudioRec fragment = new ExAudioRec();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_audio_rec, container, false);
        exercise = getArguments().getString("exercise");
        doneButton = view.findViewById(R.id.btnDoneExSV);
        doneButton.setOnClickListener(this);
        redoButton = view.findViewById(R.id.btnRedoExSV);
        redoButton.setOnClickListener(this);
        volumeBar = view.findViewById(R.id.volumeExSV);
        recorder = SpeechRecorder.getInstance(getActivity().getApplicationContext(), new VolumeHandler(volumeBar));

        ButtonFragment buttonFragment = new ButtonFragment();
        buttonFragment.setmListener(this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.replace(R.id.frameExSV, buttonFragment);
        transaction.commit();
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        recorder.release();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        //Record or Stop button pressed
        if (id == startButton.getId()){
            //Start recording
            if (!recording){
                filePath = recorder.prepare("exerciseID" + "_" + "patientID");
                recorder.record();
                recording = true;
                startButton.setText(R.string.stop);
            //Stop recording
            } else {
                recorder.stopRecording();
                recording = false;
                startButton.setText(R.string.start);
                startButton.setVisibility(View.INVISIBLE);
                doneButton.setVisibility(View.VISIBLE);
                redoButton.setVisibility(View.VISIBLE);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        volumeBar.setProgress(0);
                    }
                });
            }
        //Exercise done
        } else if (id == doneButton.getId()){
            mListener.onExerciseFinished(filePath);
        //Redo exercise
        } else if (id == redoButton.getId()){
            doneButton.setVisibility(View.INVISIBLE);
            redoButton.setVisibility(View.INVISIBLE);
            startButton.setVisibility(View.VISIBLE);
            Thread deleteFile = new Thread() {
                @Override
                public void run() {
                    File file = new File(filePath);
                    int attempts =  0;
                    while (!file.exists() && attempts < 10){
                        try {
                            sleep(100);
                            attempts++;
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    if (attempts < 10){
                        file.delete();
                    } else {
                        Log.w("AUDIO_FILE", "No file could be deleted");
                    }
                }
            };
            deleteFile.start();
        }
    }

    @Override
    public void onButtonInteraction(boolean start) {
        if(start){
            filePath = recorder.prepare("exerciseID" + "_" + "patientID");
            recorder.record();
            recording = true;
        } else {
            if (!recording){
                return;
            }
            recorder.stopRecording();
            recording = false;
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    volumeBar.setProgress(0);
                }
            });
        }
    }

    private static class VolumeHandler extends Handler{
        ProgressBar volumeBar;

        public VolumeHandler(ProgressBar bar){
            volumeBar = bar;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            final int currentVolume = (int) bundle.getDouble("Volume");
            post(new Runnable() {
                @Override
                public void run() {
                    volumeBar.setProgress(currentVolume);
                }
            });
        }
    }
}
