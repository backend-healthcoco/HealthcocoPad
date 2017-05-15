package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public class Specialities extends SugarRecord {
    @Unique
    private String uniqueId;
    private String speciality;
    private String superSpeciality;
    private String createdBy;
    private Long createdTime;
    private Long updatedTime;

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getSuperSpeciality() {
        return superSpeciality;
    }

    public void setSuperSpeciality(String superSpeciality) {
        this.superSpeciality = superSpeciality;
    }
}
