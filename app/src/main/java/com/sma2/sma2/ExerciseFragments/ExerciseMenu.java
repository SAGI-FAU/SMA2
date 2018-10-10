package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sma2.sma2.R;


public class ExerciseMenu extends Fragment {

    private OnExerciseMenuInteraction mListener;
    private Button redoButton;
    private Button doneButton;

    public ExerciseMenu() {
        // Required empty public constructor
    }

    public static ExerciseMenu newInstance() {
        ExerciseMenu fragment = new ExerciseMenu();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_menu, container, false);
        redoButton = view.findViewById(R.id.btnRedoExSV);
        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMenuInteractionInteraction(true);
            }
        });
        doneButton = view.findViewById(R.id.btnDoneExSV);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onMenuInteractionInteraction(false);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExerciseMenuInteraction) {
            mListener = (OnExerciseMenuInteraction) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExerciseMenuInteraction listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnExerciseMenuInteraction {
        void onMenuInteractionInteraction(boolean redo);
    }
}
