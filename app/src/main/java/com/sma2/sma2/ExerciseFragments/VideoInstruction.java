package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

import com.sma2.sma2.ExerciseLogic.Exercise;
import com.sma2.sma2.R;


public class VideoInstruction extends Fragment {
    private Exercise mExercise;

    public VideoInstruction() {
        // Required empty public constructor
    }

    public static VideoInstruction newInstance(Uri VideoInstrPath) {
        VideoInstruction fragment = new VideoInstruction();
        Bundle args = new Bundle();
        args.putString("Instructions", String.valueOf(VideoInstrPath));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video_instruction, container, false);

        // Set Video based on based Exercise Information
        VideoView instructionVideo = view.findViewById(R.id.instructionVideo);
        instructionVideo.setVideoURI(Uri.parse(getArguments().getString("Instructions")));

        // Set On Click Back handler
        Button instrButton = view.findViewById(R.id.btnbackvideoinstr);
        instrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

        return view;
    }

}
