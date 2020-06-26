package com.sma2.apkinson.DataAccess;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class PatientDA implements Serializable {
    private static final long serialVersionUID = -7060210544600464481L;

    @Id(autoincrement = true)
    private Long userId;
    @Property
    private String govtId;
    @Property
    private String username;
    @Property
    private Date birthday;
    @Property
    private String gender;
    @Property
    int hand;
    @Property
    private boolean smoker;
    @Property
    int educational_level;
    @Property
    private int year_diag;
    @Property
    private String other_disorder;
    @Property
    private float weight;
    @Property
    private int height;
    @Property
    private int sessionCount;

    @Generated(hash = 736211304)
    public PatientDA(Long userId, String govtId, String username, Date birthday,
            String gender, int hand, boolean smoker, int educational_level,
            int year_diag, String other_disorder, float weight, int height,
            int sessionCount) {
        this.userId = userId;
        this.govtId = govtId;
        this.username = username;
        this.birthday = birthday;
        this.gender = gender;
        this.hand = hand;
        this.smoker = smoker;
        this.educational_level = educational_level;
        this.year_diag = year_diag;
        this.other_disorder = other_disorder;
        this.weight = weight;
        this.height = height;
        this.sessionCount = sessionCount;
    }
    @Generated(hash = 1204534041)
    public PatientDA() {
    }

    public PatientDA(String username, String govtId) {
        this.username = username;
        this.govtId = govtId;
    }

    public String getUsername() {
        return this.username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public Date getBirthday() {
        return this.birthday;
    }
    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
    public String getGender() {
        return this.gender;
    }
    public void setGender(String gender) {
        this.gender = gender;
    }
    public int getHand() {
        return this.hand;
    }
    public void setHand(int hand) {
        this.hand = hand;
    }
    public boolean getSmoker() {
        return this.smoker;
    }
    public void setSmoker(boolean smoker) {
        this.smoker = smoker;
    }
    public int getEducational_level() {
        return this.educational_level;
    }
    public void setEducational_level(int educational_level) {
        this.educational_level = educational_level;
    }
    public int getYear_diag() {
        return this.year_diag;
    }
    public void setYear_diag(int year_diag) {
        this.year_diag = year_diag;
    }
    public String getOther_disorder() {
        return this.other_disorder;
    }
    public void setOther_disorder(String other_disorder) {
        this.other_disorder = other_disorder;
    }
    public float getWeight() {
        return this.weight;
    }
    public void setWeight(float weight) {
        this.weight = weight;
    }
    public int getHeight() {
        return this.height;
    }
    public void setHeight(int height) {
        this.height = height;
    }
    public Long getUserId() {
        return this.userId;
    }
    public void setUserId(long userId) {
        this.userId = userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public int getSessionCount() {
        return this.sessionCount;
    }
    public void setSessionCount(int sessionCount) {
        this.sessionCount = sessionCount;
    }
    public String getGovtId() {
        return this.govtId;
    }
    public void setGovtId(String govtId) {
        this.govtId = govtId;
    }

}
