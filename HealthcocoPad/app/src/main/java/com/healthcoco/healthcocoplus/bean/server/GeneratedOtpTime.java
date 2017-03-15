package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

/**
 * Created by neha on 11/02/16.
 */
public class GeneratedOtpTime extends SugarRecord {
    @Unique
    private String patientId;

    private long lastGeneratedTime;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public long getLastGeneratedTime() {
        return lastGeneratedTime;
    }

    public void setLastGeneratedTime(long lastGeneratedTime) {
        this.lastGeneratedTime = lastGeneratedTime;
    }
}
