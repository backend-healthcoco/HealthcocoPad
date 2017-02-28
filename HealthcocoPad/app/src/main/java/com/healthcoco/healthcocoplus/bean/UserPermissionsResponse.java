package com.healthcoco.healthcocoplus.bean;

import com.orm.SugarRecord;
import com.orm.dsl.Unique;

import org.parceler.Parcel;

/**
 * Created by neha on 01/02/17.
 */
@Parcel
public class UserPermissionsResponse extends SugarRecord {
    @Unique
    private String uniqueId;
    private Long createdTime;
    private Long updatedTime;
    private String createdBy;
    private UIPermissions uiPermissions;
    private String doctorId;

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

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public UIPermissions getUiPermissions() {
        return uiPermissions;
    }

    public void setUiPermissions(UIPermissions uiPermissions) {
        this.uiPermissions = uiPermissions;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
