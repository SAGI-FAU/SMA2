package com.sma2.sma2.ExerciseLogic;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class ScheduledExercise implements Parcelable {
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


    public ScheduledExercise(Parcel serializedExercise) {
        this.exercise = serializedExercise.readParcelable(Exercise.class.getClassLoader());
        this.sessionId = serializedExercise.readInt();
        this.completionDate = serializedExercise.readLong();
        this.resultPath = Uri.parse(serializedExercise.readString());

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ScheduledExercise createFromParcel(Parcel serializedExercise) {
            return new ScheduledExercise(serializedExercise);
        }

        public ScheduledExercise[] newArray(int size) {
            return new ScheduledExercise[size];
        }
    };

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

    public boolean getCompleted() {
        if (completionDate == -1) {
            return false;
        }
        return true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.exercise, i);
        parcel.writeInt(this.sessionId);
        parcel.writeLong(this.completionDate);
        parcel.writeString(this.resultPath.toString());

    }
}
