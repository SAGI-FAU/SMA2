package com.sma2.sma2.DataAccess;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.ToOne;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.NotNull;

import java.util.Date;

@Entity
public class MedicineDA {
    @Id
    private Long id;

    private long patientDAId;

    private boolean deleted;

    @NotNull
    private Date insertDate;

    @Property
    private String medicineName;
    @Property
    private int dose;
    @Property
    int intakeTime;

    @ToOne(joinProperty = "patientDAId")
    private PatientDA patientDA;

    /** Used to resolve relations */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    /** Used for active entity operations. */
    @Generated(hash = 1858501363)
    private transient MedicineDADao myDao;

    @Generated(hash = 1591221393)
    public MedicineDA(Long id, long patientDAId, boolean deleted, @NotNull Date insertDate,
            String medicineName, int dose, int intakeTime) {
        this.id = id;
        this.patientDAId = patientDAId;
        this.deleted = deleted;
        this.insertDate = insertDate;
        this.medicineName = medicineName;
        this.dose = dose;
        this.intakeTime = intakeTime;
    }

    @Generated(hash = 1697102487)
    public MedicineDA() {
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

    public String getMedicineName() {
        return this.medicineName;
    }

    public void setMedicineName(String medicineName) {
        this.medicineName = medicineName;
    }

    public int getDose() {
        return this.dose;
    }

    public void setDose(int dose) {
        this.dose = dose;
    }

    public int getIntakeTime() {
        return this.intakeTime;
    }

    public void setIntakeTime(int intakeTime) {
        this.intakeTime = intakeTime;
    }

    @Generated(hash = 1484351063)
    private transient Long patientDA__resolvedKey;

    /** To-one relationship, resolved on first access. */
    @Generated(hash = 1943062614)
    public PatientDA getPatientDA() {
        long __key = this.patientDAId;
        if (patientDA__resolvedKey == null
                || !patientDA__resolvedKey.equals(__key)) {
            final DaoSession daoSession = this.daoSession;
            if (daoSession == null) {
                throw new DaoException("Entity is detached from DAO context");
            }
            PatientDADao targetDao = daoSession.getPatientDADao();
            PatientDA patientDANew = targetDao.load(__key);
            synchronized (this) {
                patientDA = patientDANew;
                patientDA__resolvedKey = __key;
            }
        }
        return patientDA;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1672758692)
    public void setPatientDA(@NotNull PatientDA patientDA) {
        if (patientDA == null) {
            throw new DaoException(
                    "To-one property 'patientDAId' has not-null constraint; cannot set to-one to null");
        }
        synchronized (this) {
            this.patientDA = patientDA;
            patientDAId = patientDA.getUserId();
            patientDA__resolvedKey = patientDAId;
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

    public Date getInsertDate() {
        return this.insertDate;
    }

    public void setInsertDate(Date insertDate) {
        this.insertDate = insertDate;
    }

    public boolean getDeleted() {
        return this.deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    /** called by internal mechanisms, do not call yourself. */
    @Generated(hash = 1154463162)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getMedicineDADao() : null;
    }
}
