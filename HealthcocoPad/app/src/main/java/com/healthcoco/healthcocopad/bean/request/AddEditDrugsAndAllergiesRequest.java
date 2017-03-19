package com.healthcoco.healthcocopad.bean.request;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by shreshtha on 3/19/2017.
 */
@Parcel
public class AddEditDrugsAndAllergiesRequest {
    private String hospitalId;
    private String doctorId;
    private String locationId;
    private String patientId;
    private String allergies;
    private List<String> drugIds;

    public String getUniqueId() {
        return hospitalId;
    }

    public void setUniqueId(String hospitalId) {
        this.hospitalId = hospitalId;
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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public List<String> getDrugIds() {
        return drugIds;
    }

    public void setDrugIds(List<String> drugIds) {
        this.drugIds = drugIds;
    }
}
