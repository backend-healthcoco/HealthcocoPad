package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.bean.server.AssignedUserUiPermissions;

/**
 * Created by neha on 01/02/17.
 */
public class UserPermissionsResponse {
    private String uniqueId;
    private Long createdTime;
    private Long updatedTime;
    private String createdBy;
    private AssignedUserUiPermissions uiPermissions;
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

    public AssignedUserUiPermissions getUiPermissions() {
        return uiPermissions;
    }

    public void setUiPermissions(AssignedUserUiPermissions uiPermissions) {
        this.uiPermissions = uiPermissions;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
