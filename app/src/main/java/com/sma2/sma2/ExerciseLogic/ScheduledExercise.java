package com.sma2.sma2.ExerciseLogic;

import android.content.Context;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.converter.PropertyConverter;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.sma2.sma2.DataAccess.ScheduledExerciseDataService;
import com.sma2.sma2.DataAccess.DaoSession;

@Entity
public class ScheduledExercise implements Parcelable {
    @Id(autoincrement = true)
    private Long id;

    private long exerciseId;

    @ToOne(joinProperty = "exerciseId")
    private Exercise exercise;

    @NotNull
    private Integer sessionId;
    @Property
    private long completionDate;

    @Convert(converter = Exercise.UriConverter.class, columnType = String.class)
    private Uri resultPath;

    public static class UriConverter implements PropertyConverter<Uri, String> {
        @Override
        public Uri convertToEntityProperty(String databaseValue) {
            if (databaseValue == null) {
                return null;
            }
            return Uri.parse(databaseValue);
        }
        @Override
        public String convertToDatabaseValue(Uri entityProperty) {
            return entityProperty == null ? null : entityProperty.toString();
        }
    }
    @Keep
    public ScheduledExercise(Exercise exercise, int sessionId, long completionDate, Uri resultPath) {
        this.exercise = exercise;
        this.exerciseId = exercise.getId();
        this.sessionId = sessionId;
        this.completionDate = completionDate;
        this.resultPath = resultPath;
    }

    @Keep
    public ScheduledExercise(Exercise exercise, int sessionId) {
        this.id=null;
        this.exercise = exercise;
        this.exerciseId=exercise.getId();
        this.sessionId = sessionId;
        this.completionDate = -1;
        this.resultPath = Uri.parse("");
    }

    @Keep
    public ScheduledExercise(Parcel serializedExercise) {
        this.exercise = serializedExercise.readParcelable(Exercise.class.getClassLoader());
        this.sessionId = serializedExercise.readInt();
        this.completionDate = serializedExercise.readLong();
        this.resultPath = Uri.parse(serializedExercise.readString());

    }

    @Generated(hash = 792921268)
    public ScheduledExercise(Long id, long exerciseId, @NotNull Integer sessionId, long completionDate,
            Uri resultPath) {
        this.id = id;
        this.exerciseId = exerciseId;
        this.sessionId = sessionId;
        this.completionDate = completionDate;
        this.resultPath = resultPath;
    }

    @Generated(hash = 842542730)
    public ScheduledExercise() {
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public ScheduledExercise createFromParcel(Parcel serializedExercise) {
            return new ScheduledExercise(serializedExercise);
        }

        public ScheduledExercise[] newArray(int size) {
            return new ScheduledExercise[size];
        }
    };

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 47001928)
    private transient ScheduledExerciseDao myDao;

    @Generated(hash = 1987934211)
    private transient Long exercise__resolvedKey;

    public void complete(Uri resultPath) {
        this.completionDate = new Date().getTime();
        this.resultPath = resultPath;
    }

    public void save(Context context) {
        ScheduledExerciseDataService scheduledExerciseDataService = new ScheduledExerciseDataService(context);
        scheduledExerciseDataService.saveScheduledExercise(this);
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getExerciseId() {
        return this.exerciseId;
    }

    public void setExerciseId(long exerciseId) {
        this.exerciseId = exerciseId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public void setCompletionDate(long completionDate) {
        this.completionDate = completionDate;
    }

    public void setResultPath(Uri resultPath) {
        this.resultPath = resultPath;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1992784649)
    public Exercise getExercise() {
        long __key = this.exerciseId;
        if (exercise__resolvedKey == null || !exercise__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ExerciseDao targetDao = daoSession.getExerciseDao();
            Exercise exerciseNew = targetDao.load(__key);
            synchronized (this) {
                exercise = exerciseNew;
                exercise__resolvedKey = __key;
            }
        }
        return exercise;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1766473602)
    public void setExercise(@NotNull Exercise exercise) {
        if (exercise == null) {
            throw new DaoException(
                    "To-one property 'exerciseId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.exercise = exercise;
            exerciseId = exercise.getId();
            exercise__resolvedKey = exerciseId;
        }
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 610240580)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getScheduledExerciseDao() : null;
    }
}
