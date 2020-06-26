package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sma2.sma2.DataAccess.ExerciseSessionManager;
import com.sma2.sma2.DataAccess.ScheduledExercise;
import com.sma2.sma2.R;

import java.util.ArrayList;
import java.util.List;


public class SessionOverview extends Fragment {
    ArrayList<ScheduledExercise> mScheduledExercises;

    private OnSessionControlListener mListener;

    public SessionOverview() {}

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
            startButton.setText(R.string.session_finish);
        } else if (ExerciseSessionManager.getSessionStarted(mScheduledExercises)) {
            startButton.setText(R.string.session_continue);
        } else {
            startButton.setText(R.string.session_start);
        }

        // Set On Click handler for Start Button
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mListener != null) {
                    if (ExerciseSessionManager.getSessionCompleted(mScheduledExercises)) {
                        mListener.onSessionFinishedClicked();
                    } else {
                        mListener.onNextExerciseClicked();
                    }
                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSessionControlListener) {
            mListener = (OnSessionControlListener) context;
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

    public interface OnSessionControlListener {
        void onNextExerciseClicked();
        void onSessionFinishedClicked();
    }
}
