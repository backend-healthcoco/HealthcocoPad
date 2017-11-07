package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 24-03-2017.
 */
@Parcel
public class TreatmentService extends SugarRecord {
    protected String fieldsRequiredJsonString;
    @Unique
    private String uniqueId;
    private String customUniqueId;
    private String treatmentItemId;
    private Long createdTime;
    private Long updatedTime;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private Boolean discarded;
    private String createdBy;
    private String name;
    private String speciality;
    private String treatmentCode;
    private String category;
    private double cost = 0.0;
    private Integer rankingCount;
    @Ignore
    private ArrayList<String> fieldsRequired;

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

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public String getTreatmentCode() {
        return treatmentCode;
    }

    public void setTreatmentCode(String treatmentCode) {
        this.treatmentCode = treatmentCode;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Integer getRankingCount() {
        return rankingCount;
    }

    public void setRankingCount(Integer rankingCount) {
        this.rankingCount = rankingCount;
    }

    public ArrayList<String> getFieldsRequired() {
        return fieldsRequired;
    }

    public void setFieldsRequired(ArrayList<String> fieldsRequired) {
        this.fieldsRequired = fieldsRequired;
    }

    public String getFieldsRequiredJsonString() {
        return fieldsRequiredJsonString;
    }

    public void setFieldsRequiredJsonString(String fieldsRequiredJsonString) {
        this.fieldsRequiredJsonString = fieldsRequiredJsonString;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

    public String getTreatmentItemId() {
        return treatmentItemId;
    }

    public void setTreatmentItemId(String treatmentItemId) {
        this.treatmentItemId = treatmentItemId;
        this.customUniqueId = this.treatmentItemId + uniqueId;
    }
}
