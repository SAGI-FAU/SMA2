package com.sma2.sma2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class TextInstruction extends Fragment {
    private OnSessionInstrListener mSessionInstrClickedCallback;

    public TextInstruction() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_text_instruction, container, false);

        // Set On Click handler for Start Button
        Button instrButton = view.findViewById(R.id.textInstructions);
        instrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSessionInstrClickedCallback != null) {
                    mSessionInstrClickedCallback.onSessionInstrClicked();
                }
            }
        });

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnSessionInstrListener) {
            mSessionInstrClickedCallback = (OnSessionInstrListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mSessionInstrClickedCallback = null;
    }

    public interface OnSessionInstrListener {
        void onSessionInstrClicked();
    }
}
