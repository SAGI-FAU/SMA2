package com.sma2.sma2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.VideoView;

public class ExerciseIntro extends Fragment {

    private String mExerciseName;
    private Uri mVideoPath;
    private Uri mInstructionPath;

    OnStartClickedListener mStartClickedCallback;


    public static ExerciseIntro newInstance(String exerciseName, Uri videoPath, Uri instructionPath) {
        ExerciseIntro fragment = new ExerciseIntro();
        Bundle args = new Bundle();
        args.putString("NAME", exerciseName);
        args.putString("VIDEO_PATH", videoPath.toString());
        args.putString("INSTRUCTION_PATH", instructionPath.toString());
        fragment.setArguments(args);
        return fragment;
    }

    public interface OnStartClickedListener {
        void onExerciseStartClicked();
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
        return inflater.inflate(R.layout.fragment_exercise_intro, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d("TEST", mExerciseName);
        Log.d("TEST", mInstructionPath.toString());

        // Set Title based on Intend Information
        TextView exerciseTitle = view.findViewById(R.id.exerciseTitle);
        exerciseTitle.setText(mExerciseName);

        // Set Video based on Intend Information
        if (mInstructionPath.toString() != null) {
            VideoView instructionVideo = view.findViewById(R.id.instructionVideo);
            instructionVideo.setVideoURI(mVideoPath);
        } else {
            // Throw runtime error
        }

        // Set On Click handler for Start Button based on Intent
        Button startButton = view.findViewById(R.id.startButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStartClickedCallback.onExerciseStartClicked();
            }
        });
        super.onViewCreated(view, savedInstanceState);
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
}
