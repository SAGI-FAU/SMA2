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
    @Id private  Long id;

    @NotNull
    private String exerciseName;
    private String instructionTextPath;
    private String instructionVideoPath;
    private String fragmentClassName;
    private String exerciseType;
    @Property
    private String description;
    @Property
    private String instructions;

    @Keep
    public ExerciseDA(Exercise exercise) {
        this.exerciseName = exercise.getName();
        this.instructionTextPath = exercise.getInstructionTextPath().toString();
        this.instructionVideoPath = exercise.getInstructionVideoPath().toString();
        this.fragmentClassName = Exercise.getStringFromFragment(exercise.getFragmentClass());
        this.exerciseType = exercise.getExerciseType();
        this.description = exercise.getShortDescription();
        this.instructions = exercise.getShortInstructions();

    }

    @Generated(hash = 335302140)
    public ExerciseDA(Long id, @NotNull String exerciseName, String instructionTextPath, String instructionVideoPath, String fragmentClassName,
            String exerciseType, String description, String instructions) {
        this.id = id;
        this.exerciseName = exerciseName;
        this.instructionTextPath = instructionTextPath;
        this.instructionVideoPath = instructionVideoPath;
        this.fragmentClassName = fragmentClassName;
        this.exerciseType = exerciseType;
        this.description = description;
        this.instructions = instructions;
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

    public String getInstructionTextPath() {
        return this.instructionTextPath;
    }

    public void setInstructionTextPath(String instructionTextPath) {
        this.instructionTextPath = instructionTextPath;
    }

    public String getInstructionVideoPath() {
        return this.instructionVideoPath;
    }

    public void setInstructionVideoPath(String instructionVideoPath) {
        this.instructionVideoPath = instructionVideoPath;
    }

    public String getExerciseType() {
        return this.exerciseType;
    }

    public void setExerciseType(String exerciseType) {
        this.exerciseType = exerciseType;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFragmentClassName() {
        return this.fragmentClassName;
    }

    public void setFragmentClassName(String fragmentClassName) {
        this.fragmentClassName = fragmentClassName;
    }

    public String getInstructions() {
        return this.instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

}

