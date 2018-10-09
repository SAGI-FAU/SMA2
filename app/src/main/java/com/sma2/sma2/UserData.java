package com.sma2.sma2;

import java.io.Serializable;
import java.util.Date;

public class UserData implements Serializable {
    private String username;
    private String userId;
    private Date birthday;
    private String gender;
    private int hand;
    private int smoker;
    private int Educational_level;
    private int year_diag;
    private String other_disorder;
    private float weight;
    private int height;

    public UserData(String username, String userId) {
        this.username = username;
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getHand() {
        return hand;
    }

    public void setHand(int hand) {
        this.hand = hand;
    }

    public int getSmoker() {
        return smoker;
    }

    public void setSmoker(int smoker) {
        this.smoker = smoker;
    }

    public int getEducational_level() {
        return Educational_level;
    }

    public void setEducational_level(int educational_level) {
        Educational_level = educational_level;
    }

    public int getYear_diag() {
        return year_diag;
    }

    public void setYear_diag(int year_diag) {
        this.year_diag = year_diag;
    }

    public String getOther_disorder() {
        return other_disorder;
    }

    public void setOther_disorder(String other_disorder) {
        this.other_disorder = other_disorder;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
