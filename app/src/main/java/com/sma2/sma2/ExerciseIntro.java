package com.sma2.sma2;

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

public class ExerciseIntro extends Fragment {

    OnStartClickedListener mStartClickedCallback;
    OnInstrClickedListener mInstrClickedCallback;

    private String mExerciseName;
    private Uri mVideoPath;
    private Uri mInstructionPath;

    public static ExerciseIntro newInstance(String exerciseName, Uri videoPath, Uri instructionPath) {
        ExerciseIntro fragment = new ExerciseIntro();
        Bundle args = new Bundle();
        args.putString("NAME", exerciseName);
        args.putString("VIDEO_PATH", videoPath.toString());
        args.putString("INSTRUCTION_PATH", instructionPath.toString());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mExerciseName = getArguments().getString("NAME");
            mVideoPath = Uri.parse(getArguments().getString("VIDEO_PATH"));
            mInstructionPath = Uri.parse(getArguments().getString("INSTRUCTION_PATH"));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_intro, container, false);


        // Set On Click handler for Instructions Button
        Button instrButton = view.findViewById(R.id.textInstructions);
        instrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInstrClickedCallback != null) {
                    mInstrClickedCallback.onTextInstrClicked();
                }
            }
        });

        // Set Title based on Intend Information
        TextView exerciseTitle = view.findViewById(R.id.exerciseTitle);
        exerciseTitle.setText(mExerciseName);

        // Set Video based on Intend Information
        VideoView instructionVideo = view.findViewById(R.id.instructionVideo);
        instructionVideo.setVideoURI(mVideoPath);

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
        if (context instanceof ExerciseIntro.OnStartClickedListener) {
            mStartClickedCallback = (ExerciseIntro.OnStartClickedListener) context;
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

    public interface  OnInstrClickedListener{
        void onTextInstrClicked();
    }

    public interface OnStartClickedListener {
        void onExerciseStartClicked();
    }
}
