package com.sma2.sma2.ExerciseFragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.sma2.sma2.R;

import us.feras.mdv.MarkdownView;

public class LongInstruction extends Fragment {

    public LongInstruction(){}

    public static LongInstruction newInstance(String InstrPath)
    {
        LongInstruction fragment = new LongInstruction();

        Bundle args = new Bundle();
        args.putString("Instructions", InstrPath);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_long_instruction, container, false);

        MarkdownView txtinstr = view.findViewById(R.id.textInstrView);
        txtinstr.loadMarkdown(getArguments().getString("Instructions"));


        // Set On Click handler for Text instructions
        Button instrButton = view.findViewById(R.id.btnbackinstr);
        instrButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return view;
    }


}
