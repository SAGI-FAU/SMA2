package com.sma2.apkinson.ExerciseFragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.fragment.app.Fragment;

import com.sma2.apkinson.DataAccess.Exercise;
import com.sma2.apkinson.R;

public class ExerciseInstructions extends Fragment {

    OnStartClickedListener mStartClickedCallback;
    private Exercise mExercise;

    MediaController mediaController;

    public ExerciseInstructions() {}

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

        // Set Exercise Information
        TextView exerciseTitle = view.findViewById(R.id.exerciseTitle);
        TextView exerciseInstruction = view.findViewById(R.id.exerciseInstruction);
        TextView exerciseDescription = view.findViewById(R.id.exerciseDescription);
        exerciseTitle.setText(mExercise.getName());
        exerciseDescription.setText(mExercise.getShortDescription());
        exerciseInstruction.setText(mExercise.getShortInstructions());

        // Set Instruction Video
        final VideoView videoView = view.findViewById(R.id.videoView);
        String videoptah=mExercise.getInstructionVideoPath().getPath();
        Log.d("VIDEO_PATH", mExercise.getInstructionVideoPath().getPath());
        if(!mExercise.getInstructionVideoPath().getPath().equals("None")) {
            videoView.setVideoURI(mExercise.getInstructionVideoPath());
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mediaPlayer.setOnVideoSizeChangedListener(new MediaPlayer.OnVideoSizeChangedListener() {
                        @Override
                        public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i1) {
                            mediaController = new MediaController(getContext(), false);
                            videoView.setMediaController(mediaController);
                            mediaController.setAnchorView(videoView);
                            mediaController.show(0);
                            mediaController.setAlpha(0.6f);
                        }
                    });
                }
            });
            videoView.seekTo(1);
        }

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
