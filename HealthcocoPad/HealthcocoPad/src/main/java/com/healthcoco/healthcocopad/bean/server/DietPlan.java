package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;

@Parcel
public class DietPlan extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(DietPlan.class.getSimpleName());
    protected String calariesJsonString;
    @Unique
    private String uniqueId;
    private Long createdTime;
    private Long updatedTime;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String patientId;
    private Boolean discarded;
    private String createdBy;
    private String uniquePlanId;
    @Ignore
    private ArrayList<DietplanAddItem> items;
    @Ignore
    private MealQuantity calories;

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

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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

    public ArrayList<DietplanAddItem> getItems() {
        return items;
    }

    public void setItems(ArrayList<DietplanAddItem> items) {
        this.items = items;
    }

    public MealQuantity getCalories() {
        return calories;
    }

    public void setCalories(MealQuantity calories) {
        this.calories = calories;
    }

    public String getCalariesJsonString() {
        return calariesJsonString;
    }

    public void setCalariesJsonString(String calariesJsonString) {
        this.calariesJsonString = calariesJsonString;
    }

    public String getUniquePlanId() {
        return uniquePlanId;
    }

    public void setUniquePlanId(String uniquePlanId) {
        this.uniquePlanId = uniquePlanId;
    }
}
