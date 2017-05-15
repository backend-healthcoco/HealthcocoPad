package com.healthcoco.healthcocopad.bean.server;


import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class UserGroups extends SugarRecord {
    @Unique
    private String uniqueId;

    private String name;

    private String description;

    private String doctorId;

    private String locationId;

    private String hospitalId;

    private int count = 0;

    private Boolean discarded = false;
    private Long createdTime;
    private Long updatedTime;
    protected boolean foreignIsAssignedAnyPatient;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

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

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public boolean isForeignIsAssignedAnyPatient() {
        return foreignIsAssignedAnyPatient;
    }

    public void setForeignIsAssignedAnyPatient(boolean foreignIsAssignedAnyPatient) {
        this.foreignIsAssignedAnyPatient = foreignIsAssignedAnyPatient;
    }
}
