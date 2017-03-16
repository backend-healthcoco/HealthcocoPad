package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import java.io.Serializable;

/**
 * Created by neha on 11/12/15.
 */
public class Disease extends SugarRecord implements Serializable{
    @Unique
    private String uniqueId;
    private String description;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private Boolean discarded;
    private String disease;
    private String createdBy;
    private Long createdTime;
    private Long updatedTime;
    protected boolean isAssigned;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

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

    public boolean isAssigned() {
        return isAssigned;
    }

    public void setIsAssigned(boolean isAssigned) {
        this.isAssigned = isAssigned;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }
}
