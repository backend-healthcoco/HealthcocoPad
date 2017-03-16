package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import java.io.Serializable;

public class DrugDirection extends SugarRecord implements Serializable {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(DrugDirection.class.getSimpleName());

    @Unique
    private String uniqueId;

    private String direction;

    private String doctorId;

    private String locationId;

    private String hospitalId;

    private Long createdTime;
    private Long updatedTime;
    protected Boolean discarded;

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Boolean getDiscarded() {
        if (discarded == null)
            return false;
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }
}
