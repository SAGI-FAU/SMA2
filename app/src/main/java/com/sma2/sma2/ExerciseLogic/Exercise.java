package com.sma2.sma2.ExerciseLogic;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.app.Fragment;

import com.sma2.sma2.DataAccess.ExerciseDA;


public class Exercise implements Parcelable {
    private String name;
    private String exerciseType;
    private String shortDescription;
    private String shortInstructions;
    private Uri instructionVideoPath;
    private Uri instructionTextPath;
    private Class<? extends Fragment> fragmentClass;
    
    public Exercise(ExerciseDA exerciseDA) {
        super();
        this.name = exerciseDA.getExerciseName();
        this.instructionTextPath.parse(exerciseDA.getInstructionTextPath());
        this.instructionVideoPath.parse(exerciseDA.getInstructionVideoPath());
        this.fragmentClass = getFragmentFromString(exerciseDA.getFragmentClassName());
    }

    public Exercise(String name,
                    String exerciseType,
                    String shortDescription,
                    String shortInstructions,
                    Uri instructionVideoPath,
                    Uri instructionTextPath,
                    Class<? extends Fragment> fragmentClass){
        super();
        this.name = name;
        this.exerciseType = exerciseType;
        this.shortDescription = shortDescription;
        this.shortInstructions = shortInstructions;
        this.instructionVideoPath = instructionVideoPath;
        this.instructionTextPath = instructionTextPath;
        this.fragmentClass = fragmentClass;
    }

    public Exercise(String name,
                    String exerciseType,
                    String shortDescription,
                    String shortInstructions,
                    Uri instructionVideoPath,
                    Uri instructionTextPath,
                    String fragmentClassString){
        this(
                name,
                exerciseType,
                shortDescription,
                shortInstructions,
                instructionVideoPath,
                instructionTextPath,
                getFragmentFromString(fragmentClassString));
    }

    public Exercise(Parcel serializedExercise) {
        super();
        this.name = serializedExercise.readString();
        this.instructionVideoPath = Uri.parse(serializedExercise.readString());
        this.instructionTextPath = Uri.parse(serializedExercise.readString());
        this.fragmentClass = getFragmentFromString(serializedExercise.readString());

    }

    static private Class<? extends Fragment> getFragmentFromString(String fragmentClassString) {
        Class<? extends Fragment> fragmentClass;
        try {
            fragmentClass = Class.forName(fragmentClassString).asSubclass(Fragment.class);
        } catch (Exception e) {
            throw new RuntimeException("The specified Fragment class for this exercise is invalid.");
        }
        return fragmentClass;
    }

    static private String getStringFromFragment(Class<? extends Fragment> fragmentClass) {
        return fragmentClass.getName();
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Exercise createFromParcel(Parcel serializedExercise) {
            return new Exercise(serializedExercise);
        }

        public Exercise[] newArray(int size) {
            return new Exercise[size];
        }
    };

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

    public String getExerciseType() {
        return exerciseType;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getShortInstructions() {
        return shortInstructions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.name);
        parcel.writeString(this.exerciseType);
        parcel.writeString(this.shortDescription);
        parcel.writeString(this.shortInstructions);
        parcel.writeString(this.instructionTextPath.toString());
        parcel.writeString(this.instructionVideoPath.toString());
        parcel.writeString(getStringFromFragment(this.fragmentClass));
    }
}
