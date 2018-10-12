package com.sma2.sma2.ExerciseFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sma2.sma2.R;

import us.feras.mdv.MarkdownView;


public class TextInstruction extends Fragment {

    public TextInstruction(){}

    public static TextInstruction newInstance(String InstrPath)
    {
        TextInstruction fragment = new TextInstruction();

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
        View view = inflater.inflate(R.layout.fragment_text_instruction, container, false);

        MarkdownView txtinstr = view.findViewById(R.id.textInstrView);
        txtinstr.loadMarkdown(getArguments().getString("Instructions"));
        return view;
    }
}
