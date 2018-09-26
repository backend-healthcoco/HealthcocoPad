package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.Address;
import com.healthcoco.healthcocopad.bean.DOB;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class PatientMeasurementInfo extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(PatientMeasurementInfo.class.getSimpleName());
    protected String waistHipRatioJsonString;
    protected String wholeBodyJsonString;
    protected String armBodyJsonString;
    protected String trunkBodyJsonString;
    protected String legBodyJsonString;
    @Unique
    private String uniqueId;
    private Long adminCreatedTime;
    private Long createdTime;
    private Long updatedTime;
    private String createdBy;
    private String patientId;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String assessmentId;
    private double weightInKG;
    private double heightInCM;
    private double bmi;
    private int bodyAge;
    @Ignore
    private Ratio waistHipRatio;
    private double bodyFat;
    private int bmr;
    private double vfat;
    @Ignore
    private BodyContent wholeBody;
    @Ignore
    private BodyContent armBody;
    @Ignore
    private BodyContent trunkBody;
    @Ignore
    private BodyContent legBody;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public Long getAdminCreatedTime() {
        return adminCreatedTime;
    }

    public void setAdminCreatedTime(Long adminCreatedTime) {
        this.adminCreatedTime = adminCreatedTime;
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

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public double getWeightInKG() {
        return weightInKG;
    }

    public void setWeightInKG(double weightInKG) {
        this.weightInKG = weightInKG;
    }

    public double getHeightInCM() {
        return heightInCM;
    }

    public void setHeightInCM(double heightInCM) {
        this.heightInCM = heightInCM;
    }

    public double getBmi() {
        return bmi;
    }

    public void setBmi(double bmi) {
        this.bmi = bmi;
    }

    public int getBodyAge() {
        return bodyAge;
    }

    public void setBodyAge(int bodyAge) {
        this.bodyAge = bodyAge;
    }

    public Ratio getWaistHipRatio() {
        return waistHipRatio;
    }

    public void setWaistHipRatio(Ratio waistHipRatio) {
        this.waistHipRatio = waistHipRatio;
    }

    public String getWaistHipRatioJsonString() {
        return waistHipRatioJsonString;
    }

    public void setWaistHipRatioJsonString(String waistHipRatioJsonString) {
        this.waistHipRatioJsonString = waistHipRatioJsonString;
    }

    public double getBodyFat() {
        return bodyFat;
    }

    public void setBodyFat(double bodyFat) {
        this.bodyFat = bodyFat;
    }

    public int getBmr() {
        return bmr;
    }

    public void setBmr(int bmr) {
        this.bmr = bmr;
    }

    public double getVfat() {
        return vfat;
    }

    public void setVfat(double vfat) {
        this.vfat = vfat;
    }

    public BodyContent getWholeBody() {
        return wholeBody;
    }

    public void setWholeBody(BodyContent wholeBody) {
        this.wholeBody = wholeBody;
    }

    public String getWholeBodyJsonString() {
        return wholeBodyJsonString;
    }

    public void setWholeBodyJsonString(String wholeBodyJsonString) {
        this.wholeBodyJsonString = wholeBodyJsonString;
    }

    public BodyContent getArmBody() {
        return armBody;
    }

    public void setArmBody(BodyContent armBody) {
        this.armBody = armBody;
    }

    public String getArmBodyJsonString() {
        return armBodyJsonString;
    }

    public void setArmBodyJsonString(String armBodyJsonString) {
        this.armBodyJsonString = armBodyJsonString;
    }

    public BodyContent getTrunkBody() {
        return trunkBody;
    }

    public void setTrunkBody(BodyContent trunkBody) {
        this.trunkBody = trunkBody;
    }

    public String getTrunkBodyJsonString() {
        return trunkBodyJsonString;
    }

    public void setTrunkBodyJsonString(String trunkBodyJsonString) {
        this.trunkBodyJsonString = trunkBodyJsonString;
    }

    public BodyContent getLegBody() {
        return legBody;
    }

    public void setLegBody(BodyContent legBody) {
        this.legBody = legBody;
    }

    public String getLegBodyJsonString() {
        return legBodyJsonString;
    }

    public void setLegBodyJsonString(String legBodyJsonString) {
        this.legBodyJsonString = legBodyJsonString;
    }
}
