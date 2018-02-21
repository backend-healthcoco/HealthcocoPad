package com.healthcoco.healthcocopad.bean.server;

import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

/**
 * Created by Prashant on 21/02/2018.
 */
@Parcel
public class DataPermissions {

    private Long adminCreatedTime;
    private Long createdTime;
    private Long updatedTime;
    private String createdBy;
    @Unique
    private String uniqueId;
    @Ignore
    private DataDynamicField dataDynamicField;
    private String doctorId;

    public Long getAdminCreatedTime() {
        return adminCreatedTime;
    }

    public void setAdminCreatedTime(Long adminCreatedTime) {
        this.adminCreatedTime = adminCreatedTime;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public DataDynamicField getDataDynamicField() {
        return dataDynamicField;
    }

    public void setDataDynamicField(DataDynamicField dataDynamicField) {
        this.dataDynamicField = dataDynamicField;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }
}
