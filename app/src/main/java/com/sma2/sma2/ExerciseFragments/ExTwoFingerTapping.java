package com.sma2.sma2.ExerciseFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sma2.sma2.R;


public class ExTwoFingerTapping extends ExerciseFragment {

    public ExTwoFingerTapping() {
        // Required empty public constructor
    }

    public static ExTwoFingerTapping newInstance() {
        ExTwoFingerTapping fragment = new ExTwoFingerTapping();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ex_one_finger_tapping, container, false);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
