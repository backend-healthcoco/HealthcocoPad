package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 23-01-2017.
 */
public class LoginResponse extends SugarRecord {
    @Ignore
    private User user;
    @Unique
    protected String foreignUserId;

    @Ignore
    private List<Hospital> hospitals;

    private Boolean isTempPassword = false;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Hospital> getHospitals() {
        if (hospitals == null) {
            hospitals = new ArrayList<Hospital>();
        }
        return hospitals;
    }

    public void setHospitals(List<Hospital> hospitals) {
        this.hospitals = hospitals;
    }

    public Boolean getIsTempPassword() {
        return isTempPassword;
    }

    public void setIsTempPassword(Boolean isTempPassword) {
        this.isTempPassword = isTempPassword;
    }

    public String getForeignUserId() {
        return foreignUserId;
    }

    public void setForeignUserId(String foreignUserId) {
        this.foreignUserId = foreignUserId;
    }
}
