package com.example.zhong.keeps;

import org.litepal.crud.DataSupport;

/**
 * Created by zhong on 17-12-22.
 */

public class UserInfo extends DataSupport {
    private String user;
    private String password;
    private Boolean online;

    public Boolean getOnline() {
        return online;
    }

    public void setOnline(Boolean online) {
        this.online = online;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
