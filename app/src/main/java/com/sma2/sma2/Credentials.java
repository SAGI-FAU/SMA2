package com.sma2.sma2;

import java.io.Serializable;

public class Credentials implements Serializable {
    String username;
    String userid;

    public Credentials(String username, String userid) {
        this.username = username;
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
