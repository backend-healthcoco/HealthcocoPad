package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.Exercise;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class PatientFoodAndExcercise extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(PatientFoodAndExcercise.class.getSimpleName());

    @Unique
    private String uniqueId;

    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String patientId;
    private String assessmentId;

    @Ignore
    private List<String> foodPrefer;
    @Ignore
    private List<MealTimeAndPattern> mealTimeAndPattern;
    @Ignore
    private List<FoodCravingRequest> foodCravings;
    @Ignore
    private List<Exercise> exercise;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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

    public String getAssessmentId() {
        return assessmentId;
    }

    public void setAssessmentId(String assessmentId) {
        this.assessmentId = assessmentId;
    }

    public List<String> getFoodPrefer() {
        return foodPrefer;
    }

    public void setFoodPrefer(List<String> foodPrefer) {
        this.foodPrefer = foodPrefer;
    }

    public List<MealTimeAndPattern> getMealTimeAndPattern() {
        return mealTimeAndPattern;
    }

    public void setMealTimeAndPattern(List<MealTimeAndPattern> mealTimeAndPattern) {
        this.mealTimeAndPattern = mealTimeAndPattern;
    }

    public List<FoodCravingRequest> getFoodCravings() {
        return foodCravings;
    }

    public void setFoodCravings(List<FoodCravingRequest> foodCravings) {
        this.foodCravings = foodCravings;
    }

    public List<Exercise> getExercise() {
        return exercise;
    }

    public void setExercise(List<Exercise> exercise) {
        this.exercise = exercise;
    }
}
