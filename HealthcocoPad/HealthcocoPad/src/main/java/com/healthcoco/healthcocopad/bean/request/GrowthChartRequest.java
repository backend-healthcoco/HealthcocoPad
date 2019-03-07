package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.BloodPressure;
import com.healthcoco.healthcocopad.bean.DOB;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class GrowthChartRequest {
    @Unique
    private String uniqueId;
    private String patientId;
    private String doctorId;
    private String locationId;
    private String hospitalId;

    private Double height;
    private Double weight;
    private Double bmi;
    private Double skullCircumference;
    private String progress;
    private String temperature;
    @Ignore
    private BloodPressure bloodPressure;
    private String bloodSugarF;
    private String bloodSugarPP;
    private String bmd;
    @Ignore
    private DOB age;
    private boolean discarded;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

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

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getBmi() {
        return bmi;
    }

    public void setBmi(Double bmi) {
        this.bmi = bmi;
    }

    public Double getSkullCircumference() {
        return skullCircumference;
    }

    public void setSkullCircumference(Double skullCircumference) {
        this.skullCircumference = skullCircumference;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public BloodPressure getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(BloodPressure bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public String getBloodSugarF() {
        return bloodSugarF;
    }

    public void setBloodSugarF(String bloodSugarF) {
        this.bloodSugarF = bloodSugarF;
    }

    public String getBloodSugarPP() {
        return bloodSugarPP;
    }

    public void setBloodSugarPP(String bloodSugarPP) {
        this.bloodSugarPP = bloodSugarPP;
    }

    public String getBmd() {
        return bmd;
    }

    public void setBmd(String bmd) {
        this.bmd = bmd;
    }

    public DOB getAge() {
        return age;
    }

    public void setAge(DOB age) {
        this.age = age;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }
}
