package com.sma2.sma2.DataAccess;

import android.support.annotation.IdRes;

import com.sma2.sma2.ExerciseLogic.Exercise;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Property;

@Entity
public class ExerciseDA {
    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String exerciseName;
    private String instructionTextPath;
    private String instructionVideoPath;
    private String fragmentClassName;
    private String exerciseType;
    @Property
    private String shortDescription;
    @Property
    private String shortInstructions;


    @Keep
    public ExerciseDA(Exercise exercise) {
        this.id = null;
        this.exerciseName = exercise.getName();
        this.instructionTextPath = exercise.getInstructionTextPath().toString();
        this.instructionVideoPath = exercise.getInstructionVideoPath().toString();
        this.exerciseType = exercise.getExerciseType();
        this.shortDescription = exercise.getShortDescription();
        this.shortInstructions = exercise.getShortInstructions();
        this.fragmentClassName = Exercise.getStringFromFragment(exercise.getFragmentClass());

    }

    @Generated(hash = 532267431)
    public ExerciseDA(Long id, @NotNull String exerciseName, String examplePath,
            String instructionsPath, String exerciseType) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.examplePath = examplePath;
        this.instructionsPath = instructionsPath;
        this.exerciseType = exerciseType;
    }
    @Generated(hash = 846666550)
    public ExerciseDA() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getExerciseName() {
        return this.exerciseName;
    }
    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }
    public String getExamplePath() {
        return this.examplePath;
    }
    public void setExamplePath(String examplePath) {
        this.examplePath = examplePath;
    }
    public String getInstructionsPath() {
        return this.instructionsPath;
    }
    public void setInstructionsPath(String instructionsPath) {
        this.instructionsPath = instructionsPath;
    }
    public String getExerciseType() {
        return this.exerciseType;
    }
    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

}

