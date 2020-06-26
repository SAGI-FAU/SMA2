package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.sma2.sma2.R;

public class ButtonFragment extends Fragment {

    private OnButtonInteractionListener mListener;
    private Button startButton;
    private boolean running = false;

    public ButtonFragment() {
        // Required empty public constructor
    }

    public static ButtonFragment newInstance() {
        ButtonFragment fragment = new ButtonFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void setmListener(OnButtonInteractionListener listener){
        mListener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_button, container, false);
        startButton = view.findViewById(R.id.btnStartEx);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running){
                    startButton.setText(getString(R.string.stop));
                    running = true;
                } else {
                    startButton.setText(getString(R.string.start2));
                    running = false;
                }
                mListener.onButtonInteraction(running);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnButtonInteractionListener {
        void onButtonInteraction(boolean start);
    }
}
