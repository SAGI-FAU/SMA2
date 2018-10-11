package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sma2.sma2.R;


public class ExOneFingerTapping extends ExerciseFragment {

    public ExOneFingerTapping() {
        // Required empty public constructor
    }

    public static ExOneFingerTapping newInstance(String param1, String param2) {
        ExOneFingerTapping fragment = new ExOneFingerTapping();
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
