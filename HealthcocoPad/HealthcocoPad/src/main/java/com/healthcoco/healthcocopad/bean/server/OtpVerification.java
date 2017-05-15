package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by neha on 10/02/16.
 */
@Parcel
public class OtpVerification extends SugarRecord {
    private boolean isDataAvailableWithOtherDoctor;
    private boolean isPatientOTPVerified;
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

    public boolean isDataAvailableWithOtherDoctor() {
        return isDataAvailableWithOtherDoctor;
    }

    public void setIsDataAvailableWithOtherDoctor(boolean isDataAvailableWithOtherDoctor) {
        this.isDataAvailableWithOtherDoctor = isDataAvailableWithOtherDoctor;
    }

    public boolean isPatientOTPVerified() {
        return isPatientOTPVerified;
    }

    public void setIsPatientOTPVerified(boolean isPatientOTPVerified) {
        this.isPatientOTPVerified = isPatientOTPVerified;
    }
}
