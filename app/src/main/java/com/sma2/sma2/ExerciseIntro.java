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
    private String mExerciseName;
    private Uri mVideoPath;
    private Uri mInstructionPath;
    private int mScheduledExerciseId;

    public static ExerciseIntro newInstance(String exerciseName, Uri videoPath, Uri instructionPath, int scheduledExerciseId) {
        ExerciseIntro fragment = new ExerciseIntro();
        Bundle args = new Bundle();
        args.putString("NAME", exerciseName);
        args.putString("VIDEO_PATH", videoPath.toString());
        args.putString("INSTRUCTION_PATH", instructionPath.toString());
        args.putInt("SCHEDULED_EXERCISE_ID", scheduledExerciseId);
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
            mScheduledExerciseId = getArguments().getInt("SCHEDULED_EXERCISE_ID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_intro, container, false);

        // TODO: Handle the button click for the test instructions

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
                    mStartClickedCallback.onExerciseStartClicked(mScheduledExerciseId);
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

    public interface OnStartClickedListener {
        void onExerciseStartClicked(int scheduledExerciseId);
    }
}
