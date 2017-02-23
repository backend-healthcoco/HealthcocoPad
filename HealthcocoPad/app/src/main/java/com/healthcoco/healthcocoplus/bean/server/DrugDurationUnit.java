package com.healthcoco.healthcocoplus.bean.server;

import com.healthcoco.healthcocoplus.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import java.io.Serializable;

public class DrugDurationUnit extends SugarRecord implements Serializable {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(DrugDurationUnit.class.getSimpleName());

    @Unique
    private String uniqueId;

    private String unit;

    private String doctorId;

    private String locationId;

    private String hospitalId;

    private Long createdTime;
    private Long updatedTime;
    protected Boolean discarded;

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
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
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }
}
