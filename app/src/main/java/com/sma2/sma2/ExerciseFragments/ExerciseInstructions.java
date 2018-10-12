package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

import com.sma2.sma2.ExerciseLogic.Exercise;
import com.sma2.sma2.ExerciseLogic.ScheduledExercise;
import com.sma2.sma2.R;

public class ExerciseInstructions extends Fragment {

    OnStartClickedListener mStartClickedCallback;
    private Exercise mExercise;


    public static ExerciseInstructions newInstance(Exercise exercise) {
        ExerciseInstructions fragment = new ExerciseInstructions();
        Bundle args = new Bundle();
        args.putParcelable("EXERCISE", exercise);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_instructions, container, false);

        // TODO: Handle the button click for the test instructions

        // Set Title based Exercise Information
        TextView exerciseTitle = view.findViewById(R.id.exerciseTitle);
        exerciseTitle.setText(mExercise.getName());

        // Set Short description based Exercise Information
        TextView exerciseDescription = view.findViewById(R.id.exerciseDescription);
        exerciseDescription.setText(mExercise.getShortDescription());

        // Set Short instructions based Exercise Information
        TextView exerciseShortInstructions = view.findViewById(R.id.shortInstructions);
        exerciseShortInstructions.setText(mExercise.getShortInstructions());

        // Set Video based on based Exercise Information
        VideoView instructionVideo = view.findViewById(R.id.instructionVideo);
        instructionVideo.setVideoURI(mExercise.getInstructionVideoPath());

        // Set On Click handler for Start Button
        Button startButton = view.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStartClickedCallback != null) {
                    mStartClickedCallback.onExerciseStartClicked();
                }
            }

        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ExerciseInstructions.OnStartClickedListener) {
            mStartClickedCallback = (ExerciseInstructions.OnStartClickedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnStartClickedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mStartClickedCallback = null;
    }

    public interface OnStartClickedListener {
        void onExerciseStartClicked();
    }
}
