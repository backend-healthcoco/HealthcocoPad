package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.parceler.Parcel;

@Parcel
public class Diagnoses extends SugarRecord {

    @Unique
    private String uniqueId;

    protected String foreignClinicalNotesId;

    private String diagnosis;

    private String doctorId;

    private String locationId;

    private String hospitalId;
    private Long createdTime;
    private Long updatedTime;

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

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

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
