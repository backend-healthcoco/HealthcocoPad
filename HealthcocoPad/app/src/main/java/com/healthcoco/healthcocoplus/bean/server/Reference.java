package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class Reference extends SugarRecord {
    @Unique
    private String uniqueId;

    private String reference;

    private String description;

    private String doctorId;

    private String locationId;

    private String hospitalId;
    private Long createdTime;
    private Long updatedTime;
    private Boolean discarded;
    protected boolean isFromContactsList;

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
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

    @Override
    public String toString() {
        return "Reference [id=" + id + ", reference=" + reference + ", description=" + description + ", doctorId=" + doctorId + ", locationId=" + locationId
                + ", hospitalId=" + hospitalId + "]";
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Boolean getDiscarded() {
        if (discarded == null)
            return false;
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public boolean isFromContactsList() {
        return isFromContactsList;
    }

    public void setIsFromContactsList(boolean isFromContactsList) {
        this.isFromContactsList = isFromContactsList;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }
}
