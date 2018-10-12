package com.sma2.sma2.ExerciseFragments;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sma2.sma2.R;
import com.sma2.sma2.SignalRecording.MovementRecorder;


public class ExMovementRec extends ExerciseFragment implements ButtonFragment.OnButtonInteractionListener {
    private MovementRecorder recorder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_move_rec, container, false);
        ButtonFragment buttonFragment = new ButtonFragment();
        buttonFragment.setmListener(this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();

        transaction.replace(R.id.frameExSV, buttonFragment);
        transaction.commit();
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
        //recorder.release();
    }

    @Override
    public void onButtonInteraction(boolean start) {
        if (start) {
            recorder.startLogging();
        } else {
            try {
                recorder.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mListener.onExerciseFinished(recorder.getFileName());
        }
    }
}
