package com.healthcoco.healthcocoplus.bean.server;

import com.healthcoco.healthcocoplus.enums.HistoryFilterType;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * Created by neha on 11/12/15.
 */
public class MedicalFamilyHistoryResponse extends SugarRecord {
    private String doctorId;
    private String locationId;
    private String specialNotes;
    private String patientId;
    private String hospitalId;
    @Ignore
    List<MedicalFamilyHistoryDetails> medicalhistory;
    @Ignore
    List<MedicalFamilyHistoryDetails> familyhistory;
    private String createdBy;
    @Unique
    private String uniqueId;

    protected HistoryFilterType historyFilterType;

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

    public String getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(String specialNotes) {
        this.specialNotes = specialNotes;
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

    public List<MedicalFamilyHistoryDetails> getMedicalhistory() {
        return medicalhistory;
    }

    public void setMedicalhistory(List<MedicalFamilyHistoryDetails> medicalhistory) {
        this.medicalhistory = medicalhistory;
    }

    public List<MedicalFamilyHistoryDetails> getFamilyhistory() {
        return familyhistory;
    }

    public void setFamilyhistory(List<MedicalFamilyHistoryDetails> familyhistory) {
        this.familyhistory = familyhistory;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public HistoryFilterType getHistoryFilterType() {
        return historyFilterType;
    }

    public void setHistoryFilterType(HistoryFilterType historyFilterType) {
        this.historyFilterType = historyFilterType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }
}
