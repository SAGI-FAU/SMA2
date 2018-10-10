package com.sma2.sma2;

import android.net.Uri;

import java.util.Date;

public class ScheduledExercise {
    private Exercise exercise;
    private int sessionId;
    private long completionDate;
    private Uri resultPath;

    public ScheduledExercise(Exercise exercise, int sessionId, long completionDate, Uri resultPath) {
        this.exercise = exercise;
        this.sessionId = sessionId;
        this.completionDate = completionDate;
        this.resultPath = resultPath;
    }

    public ScheduledExercise(Exercise exercise, int sessionId) {
        this.exercise = exercise;
        this.sessionId = sessionId;
        this.completionDate = -1;
        this.resultPath = null;
    }

    public void complete(Uri resultPath) {
        this.completionDate = new Date().getTime();
        this.resultPath = resultPath;
        // TODO: Handle Database writing
    }

    public Exercise getExercise() {
        return exercise;
    }

    public int getSessionId() {
        return sessionId;
    }

    public long getCompletionDate() {
        return completionDate;
    }

    public Uri getResultPath() {
        return resultPath;
    }
}
