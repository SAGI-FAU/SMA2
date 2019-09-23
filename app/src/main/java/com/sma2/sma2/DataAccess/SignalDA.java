package com.sma2.sma2.DataAccess;

import com.sma2.sma2.ExerciseLogic.DaoSession;
import com.sma2.sma2.ExerciseLogic.Exercise;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import com.sma2.sma2.ExerciseLogic.ExerciseDao;

@Entity
public class SignalDA {
    @Id
    private Long id;
    private long patientDAId;
    private long exerciseID;

    @NotNull
    private String signalPath;
    private String exerciseName;
    private Date recordingTime;
    private int sessionNumber;


    @ToOne(joinProperty = "patientDAId")
    private PatientDA patient;


    @ToOne(joinProperty = "exerciseID")
    private Exercise exercise;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 457749002)
    private transient SignalDADao myDao;

    @Generated(hash = 391381774)
    private transient Long patient__resolvedKey;

    @Keep
    public SignalDA(String exerciseName, String signalPath){
        this.id=null;
        this.patientDAId=1;
        this.signalPath = signalPath;
        this.exerciseName=exerciseName;
        this.recordingTime = new Date();
        this.sessionNumber = 0; // TODO: Get the session number

    }

    @Generated(hash = 1545681166)
    public SignalDA(Long id, long patientDAId, long exerciseID, @NotNull String signalPath,
            String exerciseName, Date recordingTime, int sessionNumber) {
        this.id = id;
        this.patientDAId = patientDAId;
        this.exerciseID = exerciseID;
        this.signalPath = signalPath;
        this.exerciseName = exerciseName;
        this.recordingTime = recordingTime;
        this.sessionNumber = sessionNumber;
    }

    @Generated(hash = 896497034)
    public SignalDA() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getPatientDAId() {
        return this.patientDAId;
    }

    public void setPatientDAId(long patientDAId) {
        this.patientDAId = patientDAId;
    }


    public long getExerciseID() {
        return this.exerciseID;
    }

    public void setExerciseID(long exerciseID) {
        this.exerciseID = exerciseID;
    }


    public String getSignalPath() {
        return this.signalPath;
    }

    public void setSignalPath(String signalPath) {
        this.signalPath = signalPath;
    }

    public String getExerciseName() {
        return this.exerciseName;
    }

    public void setExerciseName(String exerciseName) {
        this.exerciseName = exerciseName;
    }

    public Date getRecordingTime() {
        return this.recordingTime;
    }

    public void setRecordingTime(Date recordingTime) {
        this.recordingTime = recordingTime;
    }

    public int getSessionNumber() {
        return this.sessionNumber;
    }

    public void setSessionNumber(int sessionNumber) {
        this.sessionNumber = sessionNumber;
    }

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 266209956)
    public PatientDA getPatient() {
        long __key = this.patientDAId;
        if (patient__resolvedKey == null || !patient__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PatientDADao targetDao = daoSession.getPatientDADao();
            PatientDA patientNew = targetDao.load(__key);
            synchronized (this) {
                patient = patientNew;
                patient__resolvedKey = __key;
            }
        }
        return patient;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 700590261)
    public void setPatient(@NotNull PatientDA patient) {
        if (patient == null) {
            throw new DaoException(
                    "To-one property 'patientDAId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.patient = patient;
            patientDAId = patient.getUserId();
            patient__resolvedKey = patientDAId;
        }
    }


    @Generated(hash = 1987934211)
    private transient Long exercise__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1642099129)
    public Exercise getExercise() {
        long __key = this.exerciseID;
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
    @Generated(hash = 1823297237)
    public void setExercise(@NotNull Exercise exercise) {
        if (exercise == null) {
            throw new DaoException(
                    "To-one property 'exerciseID' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.exercise = exercise;
            exerciseID = exercise.getId();
            exercise__resolvedKey = exerciseID;
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
    @Generated(hash = 1305790658)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getSignalDADao() : null;
    }

}
