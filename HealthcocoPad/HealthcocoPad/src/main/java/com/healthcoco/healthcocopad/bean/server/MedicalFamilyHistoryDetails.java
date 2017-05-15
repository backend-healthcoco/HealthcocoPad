package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.HistoryFilterType;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

/**
 * Created by neha on 11/12/15.
 */
@Parcel
public class MedicalFamilyHistoryDetails extends SugarRecord {
    @Unique
    private String uniqueId;
    private String disease;
    private String description;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String createdBy;
    private String createdTime;
    private String updatedTime;
    private String explanation;
    private Boolean discarded;
    private HistoryFilterType historyFilterType;

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

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(String updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public HistoryFilterType getHistoryFilterType() {
        return historyFilterType;
    }

    public void setHistoryFilterType(HistoryFilterType historyFilterType) {
        this.historyFilterType = historyFilterType;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }
}