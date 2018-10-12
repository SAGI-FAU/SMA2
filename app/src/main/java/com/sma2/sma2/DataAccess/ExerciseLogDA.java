package com.sma2.sma2.DataAccess;

import java.util.Date;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.DaoException;

@Entity
public class ExerciseLogDA {
    @Id
    private Long id;

    private long exerciseDAId;

    @ToOne(joinProperty="exerciseDAId")
    private ExerciseDA exerciseDA;

    @NotNull
    private Date completionDate;
    private String resultPath;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1383097960)
    private transient ExerciseLogDADao myDao;

    @Generated(hash = 196987570)
    private transient Long exerciseDA__resolvedKey;
    @Generated(hash = 306138469)
    public ExerciseLogDA(Long id, long exerciseDAId, @NotNull Date completionDate,
            String resultPath) {
        this.id = id;
        this.exerciseDAId = exerciseDAId;
        this.completionDate = completionDate;
        this.resultPath = resultPath;
    }
    @Generated(hash = 1299941058)
    public ExerciseLogDA() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getCompletionDate() {
        return this.completionDate;
    }
    public void setCompletionDate(Date completionDate) {
        this.completionDate = completionDate;
    }
    public String getResultPath() {
        return this.resultPath;
    }
    public void setResultPath(String resultPath) {
        this.resultPath = resultPath;
    }
    public long getExerciseDAId() {
        return this.exerciseDAId;
    }
    public void setExerciseDAId(long exerciseDAId) {
        this.exerciseDAId = exerciseDAId;
    }
    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1577826720)
    public ExerciseDA getExerciseDA() {
        long __key = this.exerciseDAId;
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
    @Generated(hash = 1314555598)
    public void setExerciseDA(@NotNull ExerciseDA exerciseDA) {
        if (exerciseDA == null) {
            throw new DaoException(
                    "To-one property 'exerciseDAId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.exerciseDA = exerciseDA;
            exerciseDAId = exerciseDA.getId();
            exerciseDA__resolvedKey = exerciseDAId;
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
    @Generated(hash = 1810242894)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getExerciseLogDADao() : null;
    }

}

