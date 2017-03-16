package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

public class Investigation extends SugarRecord {

    @Unique
    private String uniqueId;

    protected String foreignClinicalNotesId;

    private String investigation;

    private String doctorId;

    private String locationId;

    private String hospitalId;

    private Long createdTime;
    private Long updatedTime;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getForeignClinicalNotesId() {
        return foreignClinicalNotesId;
    }

    public void setForeignClinicalNotesId(String foreignClinicalNotesId) {
        this.foreignClinicalNotesId = foreignClinicalNotesId;
    }

    public String getInvestigation() {
        return investigation;
    }

    public void setInvestigation(String investigation) {
        this.investigation = investigation;
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
}
