package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import java.io.Serializable;

public class Notes extends SugarRecord implements Serializable {

    @Unique
    private String uniqueId;

    protected String foreignClinicalNotesId;

    private String note;

    private String doctorId;

    private String locationId;

    private String hospitalId;

    private Boolean inHistory;

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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


    public String getForeignClinicalNotesId() {
        return foreignClinicalNotesId;
    }

    public void setForeignClinicalNotesId(String foreignClinicalNotesId) {
        this.foreignClinicalNotesId = foreignClinicalNotesId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Boolean getInHistory() {
        return inHistory;
    }

    public void setInHistory(Boolean inHistory) {
        this.inHistory = inHistory;
    }
}
