package com.healthcoco.healthcocopad.bean.request;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by neha on 23/11/15.
 */
@Parcel
public class AssignGroupRequest {
    private ArrayList<String> groupIds;
    private String patientId;
    private String doctorId;
    private String locationId;
    private String hospitalId;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public ArrayList<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(ArrayList<String> groupIds) {
        this.groupIds = groupIds;
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
}
