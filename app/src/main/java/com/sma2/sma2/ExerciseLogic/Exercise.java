package com.sma2.sma2.ExerciseLogic;

import android.net.Uri;
import android.support.v4.app.Fragment;


public class Exercise {
    private String name;
    private Uri instructionVideoPath;
    private Uri instructionTextPath;
    private Class<? extends Fragment> fragmentClass;

    public Exercise(String name, Uri instructionVideoPath, Uri instructionTextPath, Class<? extends Fragment> fragmentClass){
        super();
        this.name = name;
        this.instructionVideoPath = instructionVideoPath;
        this.instructionTextPath = instructionTextPath;
        this.fragmentClass = fragmentClass;
    }

    public Exercise(String name, Uri instructionVideoPath, Uri instructionTextPath, String fragmentClassString){
        super();

        Class<? extends Fragment> fragmentClass;
        try {
            fragmentClass = Class.forName(fragmentClassString).asSubclass(Fragment.class);
        } catch (Exception e) {
            throw new RuntimeException("The specified Fragment class for this exercise is invalid.");
        }

        this.name = name;
        this.instructionVideoPath = instructionVideoPath;
        this.instructionTextPath = instructionTextPath;
        this.fragmentClass = fragmentClass;
    }

    public String getName() {
        return name;
    }

    public Uri getInstructionVideoPath() {
        return instructionVideoPath;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public Uri getInstructionTextPath() {
        return instructionTextPath;
    }
}
