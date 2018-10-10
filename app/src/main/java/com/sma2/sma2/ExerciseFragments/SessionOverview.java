package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sma2.sma2.R;


public class SessionOverview extends Fragment {

    private OnSessionStartListener mSessionStartClickedCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_session_overview, container, false);

        // Set On Click handler for Start Button
        Button startButton = view.findViewById(R.id.sessionStart);
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
