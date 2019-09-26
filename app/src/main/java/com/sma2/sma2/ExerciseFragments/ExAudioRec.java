package com.sma2.sma2.ExerciseFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sma2.sma2.FeatureExtraction.Speech.features.RadarFeatures;
import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.SpeechRecorder;

public class ExAudioRec extends ExerciseFragment implements ButtonFragment.OnButtonInteractionListener{
    private ProgressBar volumeBar;
    private SpeechRecorder recorder;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_audio_rec, container, false);
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



    private void  evaluate_features(){
        if (mExercise.getId()==18||mExercise.getId()==19||mExercise.getId()==20){ // sustained vowel AH to compute jitter
            float jitt_perc = RadarFeatures.jitter(filePath);
            try {
                RadarFeatures.export_speech_feature(filePath, jitt_perc,"Jitter");
            }catch (Exception e) {
                Toast.makeText(getActivity(),R.string.jitter_failed,Toast.LENGTH_SHORT).show();

            }
            }
        else if (mExercise.getId()==11)
        {
            float vrate = RadarFeatures.voiceRate(filePath);
            try {
                RadarFeatures.export_speech_feature(filePath, vrate,"VRate");
            }catch (Exception e) {
                Toast.makeText(getActivity(),R.string.jitter_failed,Toast.LENGTH_SHORT).show();

            }
        }
    }

    private class VolumeHandler extends Handler{
        ProgressBar volumeBar;

        public VolumeHandler(ProgressBar bar){
            volumeBar = bar;
        }
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle bundle = msg.getData();
            final int currentVolume = (int) bundle.getDouble("Volume", 0);
            final String state = bundle.getString("State", "Empty");
            if (state.equals("Finished")){

                evaluate_features();

            }

            post(new Runnable() {
                @Override
                public void run() {
                    volumeBar.setProgress(currentVolume);
                }
            });
        }
    }
}
