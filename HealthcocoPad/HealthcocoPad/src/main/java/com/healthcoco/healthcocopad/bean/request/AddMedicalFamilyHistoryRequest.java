package com.healthcoco.healthcocopad.bean.request;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by neha on 16/12/15.
 */
@Parcel
public class AddMedicalFamilyHistoryRequest {
    private String locationId;
    private String hospitalId;
    private String doctorId;
    private String patientId;
    List<String> addDiseases;
    List<String> removeDiseases;

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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public List<String> getAddDiseases() {
        return addDiseases;
    }

    public void setAddDiseases(List<String> addDiseases) {
        this.addDiseases = addDiseases;
    }

    public List<String> getRemoveDiseases() {
        return removeDiseases;
    }

    public void setRemoveDiseases(List<String> removeDiseases) {
        this.removeDiseases = removeDiseases;
    }
}
