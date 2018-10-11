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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_text_instructions, container, false);

        MarkdownView txtinstr = view.findViewById(R.id.textInstrView);
        txtinstr.loadMarkdown(getResources().getString(R.string.a_ex));
        return view;
    }

}
