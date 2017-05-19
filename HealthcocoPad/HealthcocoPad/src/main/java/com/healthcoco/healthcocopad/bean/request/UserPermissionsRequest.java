package com.healthcoco.healthcocopad.bean.request;

/**
 * Created by neha on 13/04/17.
 */

public class UserPermissionsRequest {
    private String uniqueId;
    private AssignedUiPermissionsRequest uiPermissions;
    private String doctorId;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public AssignedUiPermissionsRequest getUiPermissions() {
        return uiPermissions;
    }

    public void setUiPermissions(AssignedUiPermissionsRequest uiPermissions) {
        this.uiPermissions = uiPermissions;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
