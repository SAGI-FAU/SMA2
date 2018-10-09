package com.sma2.sma2.ExerciseFragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.SpeechRecorder;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ExSustainedVowel.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ExSustainedVowel#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExSustainedVowel extends Fragment implements View.OnClickListener{

    private OnFragmentInteractionListener mListener;
    private Button startButton;
    private Button doneButton;
    private Button redoButton;
    private ProgressBar volumeBar;
    private SpeechRecorder recorder;
    private boolean recording = false;
    private String filePath;

    public ExSustainedVowel() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ExSustainedVowel.
     */
    // TODO: Rename and change types and number of parameters
    public static ExSustainedVowel newInstance() {
        ExSustainedVowel fragment = new ExSustainedVowel();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_sustained_vowel, container, false);
        startButton = view.findViewById(R.id.btnStartExSV);
        startButton.setOnClickListener(this);
        doneButton = view.findViewById(R.id.btnDoneExSV);
        doneButton.setOnClickListener(this);
        redoButton = view.findViewById(R.id.btnRedoExSV);
        redoButton.setOnClickListener(this);
        volumeBar = view.findViewById(R.id.volumeExSV);
        recorder = SpeechRecorder.getInstance(getActivity().getApplicationContext(), new VolumeHandler(volumeBar));
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onExerciseFinished(String filePath);
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
