package com.sma2.sma2.DataAccess;

import android.support.annotation.IdRes;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class ExerciseDA {
    @Id private  Long id;

    @NotNull
    private String exerciseName;
    private String examplePath;
    private String instructionsPath;
    private String exerciseType;

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

