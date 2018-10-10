package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sma2.sma2.ExerciseLogic.ExerciseSessionManager;
import com.sma2.sma2.ExerciseLogic.ScheduledExercise;
import com.sma2.sma2.R;

import java.util.ArrayList;
import java.util.List;


public class SessionOverview extends Fragment {
    ArrayList<ScheduledExercise> mScheduledExercises;

    private OnSessionStartListener mSessionStartClickedCallback;

    public static SessionOverview newInstance(List<ScheduledExercise> scheduledExerciseList) {
        SessionOverview fragment = new SessionOverview();
        Bundle args = new Bundle();
        args.putParcelableArrayList("SCHEDULED_EXERCISES", (ArrayList<ScheduledExercise>) scheduledExerciseList);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mScheduledExercises = getArguments().getParcelableArrayList("SCHEDULED_EXERCISES");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_session_overview, container, false);

        // Attach Adapter for the exercise overview
        Context context = view.getContext();
        RecyclerView recyclerView = view.findViewById(R.id.scheduledExerciseList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(new SessionOverviewRecyclerViewAdapter(mScheduledExercises));

        // Change Text of Start Button based on Context
        Button startButton = view.findViewById(R.id.sessionStart);
        if (ExerciseSessionManager.getSessionCompleted(mScheduledExercises)) {
            startButton.setText("Back To Main Menu");
        } else if (ExerciseSessionManager.getSessionStarted(mScheduledExercises)) {
            startButton.setText("Start Next Exercise");
        } else {
            startButton.setText("Start First Exercise");
        }

        // Set On Click handler for Start Button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSessionStartClickedCallback != null) {
                    mSessionStartClickedCallback.onSessionStartClicked();
                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSessionStartListener) {
            mSessionStartClickedCallback = (OnSessionStartListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSessionStartClickedCallback = null;
    }

    public interface OnSessionStartListener {
        void onSessionStartClicked();
    }
}
