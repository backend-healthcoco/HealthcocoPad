package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.bean.server.AssignedUserUiPermissions;

import org.parceler.Parcel;

/**
 * Created by neha on 13/04/17.
 */

@Parcel
public class UiPermissionsBoth {
    private AssignedUserUiPermissions doctorPermissions;
    private UIPermissions allPermissions;
    private String doctorId;

    public UIPermissions getAllPermissions() {
        return allPermissions;
    }

    public void setAllPermissions(UIPermissions allPermissions) {
        this.allPermissions = allPermissions;
    }

    public AssignedUserUiPermissions getDoctorPermissions() {
        return doctorPermissions;
    }

    public void setDoctorPermissions(AssignedUserUiPermissions doctorPermissions) {
        this.doctorPermissions = doctorPermissions;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
