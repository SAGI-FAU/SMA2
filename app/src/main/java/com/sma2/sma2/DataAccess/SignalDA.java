package com.sma2.sma2.DataAccess;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.ToOne;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;

@Entity
public class SignalDA {
    @Id
    private Long id;

    private long patientDAId;
    private long exerciseDAID;

    @NotNull
    private String signalPath;
    private Date recordingTime;
    private Integer sessionCount;


    @ToOne(joinProperty = "patientDAId")
    private PatientDA patient;

    @ToOne(joinProperty = "exerciseDAID")
    private  ExerciseDA exerciseDA;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 457749002)
    private transient SignalDADao myDao;

    @Generated(hash = 835431146)
    public SignalDA(Long id, long patientDAId, long exerciseDAID,
            @NotNull String signalPath, Date recordingTime, Integer sessionCount) {
        this.id = id;
        this.patientDAId = patientDAId;
        this.exerciseDAID = exerciseDAID;
        this.signalPath = signalPath;
        this.recordingTime = recordingTime;
        this.sessionCount = sessionCount;
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

    public long getExerciseDAID() {
        return this.exerciseDAID;
    }

    public void setExerciseDAID(long exerciseDAID) {
        this.exerciseDAID = exerciseDAID;
    }

    public String getSignalPath() {
        return this.signalPath;
    }

    public void setSignalPath(String signalPath) {
        this.signalPath = signalPath;
    }

    public Date getRecordingTime() {
        return this.recordingTime;
    }

    public void setRecordingTime(Date recordingTime) {
        this.recordingTime = recordingTime;
    }

    public Integer getSessionCount() {
        return this.sessionCount;
    }

    public void setSessionCount(Integer sessionCount) {
        this.sessionCount = sessionCount;
    }

    @Generated(hash = 391381774)
    private transient Long patient__resolvedKey;

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

    @Generated(hash = 196987570)
    private transient Long exerciseDA__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 2056666445)
    public ExerciseDA getExerciseDA() {
        long __key = this.exerciseDAID;
        if (exerciseDA__resolvedKey == null
                || !exerciseDA__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            ExerciseDADao targetDao = daoSession.getExerciseDADao();
            ExerciseDA exerciseDANew = targetDao.load(__key);
            synchronized (this) {
                exerciseDA = exerciseDANew;
                exerciseDA__resolvedKey = __key;
            }
        }
        return exerciseDA;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 701245847)
    public void setExerciseDA(@NotNull ExerciseDA exerciseDA) {
        if (exerciseDA == null) {
            throw new DaoException(
                    "To-one property 'exerciseDAID' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.exerciseDA = exerciseDA;
            exerciseDAID = exerciseDA.getId();
            exerciseDA__resolvedKey = exerciseDAID;
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
