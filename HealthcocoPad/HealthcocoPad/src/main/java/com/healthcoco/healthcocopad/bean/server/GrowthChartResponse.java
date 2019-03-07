package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.BloodPressure;
import com.healthcoco.healthcocopad.bean.DOB;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class GrowthChartResponse extends SugarRecord {
    @Unique
    private String uniqueId;
    private Long createdTime;
    private Long updatedTime;
    private String createdBy;
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
    protected String bloodPressureJsonString;
    private String bloodSugarF;
    private String bloodSugarPP;
    private String bmd;
    @Ignore
    private DOB age;
    protected String ageJsonString;
    private boolean discarded;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

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

    public String getBloodPressureJsonString() {
        return bloodPressureJsonString;
    }

    public void setBloodPressureJsonString(String bloodPressureJsonString) {
        this.bloodPressureJsonString = bloodPressureJsonString;
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

    public String getAgeJsonString() {
        return ageJsonString;
    }

    public void setAgeJsonString(String ageJsonString) {
        this.ageJsonString = ageJsonString;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }
}
