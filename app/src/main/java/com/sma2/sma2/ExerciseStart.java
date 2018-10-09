package com.sma2.sma2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class ExerciseStart extends Fragment {

    private OnSessionStartListener mSessionStartClickedCallback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise_start, container, false);

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
