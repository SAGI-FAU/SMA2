package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.sma2.sma2.DataAccess.Exercise;


public abstract class ExerciseFragment extends Fragment {
    protected OnExerciseCompletedListener mListener;
    protected boolean recording = false;
    protected String filePath;
    protected Exercise mExercise;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    public ExerciseFragment newInstance(Exercise exercise) {
        ExerciseFragment fragment;
        try {
            fragment = this.getClass().asSubclass(ExerciseFragment.class).newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Exercise not correctly implemented");
        }
        Bundle args = new Bundle();
        args.putParcelable("EXERCISE", exercise);

        if (fragment  instanceof ExReadText){
            String name=exercise.getName();
            String[] separated = name.split(" ");
            String NumSent=separated[1];
            args.putInt("sentence", Integer.valueOf(NumSent)-1);

        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mExercise = getArguments().getParcelable("EXERCISE");
        }
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

