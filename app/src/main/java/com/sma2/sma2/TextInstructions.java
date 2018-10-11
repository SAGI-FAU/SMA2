package com.sma2.sma2;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import us.feras.mdv.MarkdownView;


public class TextInstructions extends Fragment {

    public TextInstructions(){}

    public static TextInstructions newInstance(String InstrPath)
    {
        TextInstructions fragment = new TextInstructions();

        Bundle args = new Bundle();
        args.putString("InstructionPath", InstrPath);
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
        View view = inflater.inflate(R.layout.fragment_text_instructions, container, false);

        MarkdownView txtinstr = view.findViewById(R.id.textInstrView);
        txtinstr.loadMarkdown(getArguments().getString("InstructionPath"));
        return view;
    }

}
