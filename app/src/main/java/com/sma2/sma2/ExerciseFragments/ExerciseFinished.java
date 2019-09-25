package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.sma2.sma2.DataAccess.ScheduledExercise;
import com.sma2.sma2.R;


public class ExerciseFinished extends Fragment {

    private ScheduledExercise mScheduledExercise;

    private OnExerciseActionListener mListener;

    public ExerciseFinished() {
        // Required empty public constructor
    }

    public static ExerciseFinished newInstance(ScheduledExercise scheduledExercise) {
        ExerciseFinished fragment = new ExerciseFinished();
        Bundle args = new Bundle();
        args.putParcelable("SCHEDULED_EXERCISE", scheduledExercise);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mScheduledExercise = getArguments().getParcelable("SCHEDULED_EXERCISE");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_finished, container, false);
        Button redoButton = view.findViewById(R.id.btnRedoExSV);
        redoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRedoButtonClicked();
            }
        });
        Button doneButton = view.findViewById(R.id.btnDoneExSV);
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onDoneButtonClicked();
            }
        });


        TextView tMessage=view.findViewById(R.id.motivationMessage);
        Animation animation2=AnimationUtils.loadAnimation(view.getContext(),R.anim.bounce);
        tMessage.startAnimation(animation2);

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnExerciseActionListener) {
            mListener = (OnExerciseActionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnExerciseActionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnExerciseActionListener {
        void onRedoButtonClicked();
        void onDoneButtonClicked();
    }
}
