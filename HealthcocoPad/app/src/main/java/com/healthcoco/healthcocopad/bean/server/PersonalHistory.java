package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by Shreshtha on 16-03-2017.
 */
@Parcel
public class PersonalHistory extends SugarRecord {
    private String diet;
    private String addictions;
    private String bowelHabit;
    private String bladderHabit;
    private String doctorId;

    private String locationId;

    private String hospitalId;

    private String patientId;

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

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getDiet() {
        return diet;
    }

    public void setDiet(String diet) {
        this.diet = diet;
    }

    public String getAddictions() {
        return addictions;
    }

    public void setAddictions(String addictions) {
        this.addictions = addictions;
    }

    public String getBowelHabit() {
        return bowelHabit;
    }

    public void setBowelHabit(String bowelHabit) {
        this.bowelHabit = bowelHabit;
    }

    public String getBladderHabit() {
        return bladderHabit;
    }

    public void setBladderHabit(String bladderHabit) {
        this.bladderHabit = bladderHabit;
    }
}
