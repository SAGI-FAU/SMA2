package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;


public abstract class ExerciseFragment extends Fragment {
    protected OnExerciseCompletedListener mListener;
    protected boolean recording = false;
    protected String filePath;
    protected String exercise;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExerciseCompletedListener) {
            mListener = (OnExerciseCompletedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExerciseCompletedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnExerciseCompletedListener {
        void onExerciseFinished(String filePath);
    }
}

