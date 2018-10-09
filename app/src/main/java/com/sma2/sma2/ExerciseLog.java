package com.sma2.sma2;

import android.net.Uri;

import java.util.Date;

public class ExerciseLog {
    private Exercise exercise;
    private int sessionId;
    private Date completionDate;
    private Uri resultPath;

    public ExerciseLog(Exercise exercise, int sessionId, Date completionDate, Uri resultPath) {
        this.exercise = exercise;
        this.sessionId = sessionId;
        this.completionDate = completionDate;
        this.resultPath = resultPath;
    }

    public Exercise getExercise() {
        return exercise;
    }

    public int getSessionId() {
        return sessionId;
    }

    public Date getCompletionDate() {
        return completionDate;
    }

    public Uri getResultPath() {
        return resultPath;
    }

    public void complete(Uri resultPath) {
        // get the current date, modify completion data, modify result path and persist to the database
    }
}
