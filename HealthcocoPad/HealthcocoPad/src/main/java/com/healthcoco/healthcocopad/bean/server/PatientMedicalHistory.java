package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.Addiction;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class PatientMedicalHistory extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(PatientMedicalHistory.class.getSimpleName());

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

    @Ignore
    private List<String> familyhistory;
    @Ignore
    private List<String> medicalhistory;
    @Ignore
    private DrugsAndAllergies drugsAndAllergies;
    @Ignore
    private List<String> specialNotes;

    private Boolean isStress;
    @Ignore
    private List<Addiction> addiction;
    @Ignore
    private List<String> diesease;

    private Boolean everHospitalize;

    @Ignore
    private List<String> reasons;
    private Integer noOfTime;
    @Ignore
    private FoodAndAllergies foodAndAllergies;
    @Ignore
    private List<PrescriptionAddItem> existingMedication;

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

    public List<String> getFamilyhistory() {
        return familyhistory;
    }

    public void setFamilyhistory(List<String> familyhistory) {
        this.familyhistory = familyhistory;
    }

    public List<String> getMedicalhistory() {
        return medicalhistory;
    }

    public void setMedicalhistory(List<String> medicalhistory) {
        this.medicalhistory = medicalhistory;
    }

    public DrugsAndAllergies getDrugsAndAllergies() {
        return drugsAndAllergies;
    }

    public void setDrugsAndAllergies(DrugsAndAllergies drugsAndAllergies) {
        this.drugsAndAllergies = drugsAndAllergies;
    }

    public List<String> getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(List<String> specialNotes) {
        this.specialNotes = specialNotes;
    }

    public Boolean getStress() {
        return isStress;
    }

    public void setStress(Boolean stress) {
        isStress = stress;
    }

    public List<Addiction> getAddiction() {
        return addiction;
    }

    public void setAddiction(List<Addiction> addiction) {
        this.addiction = addiction;
    }

    public List<String> getDiesease() {
        return diesease;
    }

    public void setDiesease(List<String> diesease) {
        this.diesease = diesease;
    }

    public Boolean getEverHospitalize() {
        return everHospitalize;
    }

    public void setEverHospitalize(Boolean everHospitalize) {
        this.everHospitalize = everHospitalize;
    }

    public List<String> getReasons() {
        return reasons;
    }

    public void setReasons(List<String> reasons) {
        this.reasons = reasons;
    }

    public Integer getNoOfTime() {
        return noOfTime;
    }

    public void setNoOfTime(Integer noOfTime) {
        this.noOfTime = noOfTime;
    }

    public FoodAndAllergies getFoodAndAllergies() {
        return foodAndAllergies;
    }

    public void setFoodAndAllergies(FoodAndAllergies foodAndAllergies) {
        this.foodAndAllergies = foodAndAllergies;
    }

    public List<PrescriptionAddItem> getExistingMedication() {
        return existingMedication;
    }

    public void setExistingMedication(List<PrescriptionAddItem> existingMedication) {
        this.existingMedication = existingMedication;
    }
}
