package com.sma2.sma2;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;


public class Exercise {
    private String name;
    private Uri instructionVideoPath;
    private Fragment fragmentClass;

    public Exercise(String name, Uri instructionVideoPath, Fragment fragmentClass){
        super();
        this.name = name;
        this.instructionVideoPath = instructionVideoPath;
        this.fragmentClass = fragmentClass;
    }

    public String getName() {
        return name;
    }

    public Uri getInstructionVideoPath() {
        return instructionVideoPath;
    }

    public Fragment getFragmentClass() {
        return fragmentClass;
    }
}
