package com.sma2.apkinson.ExerciseFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.fragment.app.FragmentTransaction;

import com.sma2.apkinson.R;
import com.sma2.apkinson.SignalRecording.SpeechRecorder;


public class ExImageDescription extends ExerciseFragment implements ButtonFragment.OnButtonInteractionListener {
    private SpeechRecorder recorder;
    private ProgressBar volumeBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_image_description, container, false);
        volumeBar = view.findViewById(R.id.volumeExID);
        recorder = SpeechRecorder.getInstance(getActivity().getApplicationContext(), new VolumeHandler(volumeBar));
        ImageView image = view.findViewById(R.id.imageID);
        image.setImageResource(R.drawable.cookie);
        ButtonFragment buttonFragment = new ButtonFragment();
        buttonFragment.setmListener(this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.replace(R.id.frameExID, buttonFragment);
        transaction.commit();
        return view;
    }



    @Override
    public void onDetach() {
        super.onDetach();
        recorder.release();
        mListener = null;
    }


    @Override
    public void onButtonInteraction(boolean start) {
        if(start){
            filePath = recorder.prepare(mExercise.getName());
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
            mListener.onExerciseFinished(filePath);
        }
    }

    private static class VolumeHandler extends Handler {
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
