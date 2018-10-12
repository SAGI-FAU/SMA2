package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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


        // Set On Click handler for Text instructions
        Button instrButton = view.findViewById(R.id.textInstructions);
        instrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LongInstruction dialogFragment = LongInstruction.newInstance(mExercise.getShortInstructions());
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.exerciseContainer,dialogFragment);
                ft.commit();
            }
        });

        // Set Title based on Intend Information
        TextView exerciseTitle = view.findViewById(R.id.exerciseTitle);
        exerciseTitle.setText(mExercise.getName());

        // Set Video based on Intend Information
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
