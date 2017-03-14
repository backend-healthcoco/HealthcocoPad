package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import org.parceler.Parcel;

import java.util.List;
public class HistoryDetailsResponse extends SugarRecord {

    private String uniqueId;
    @Unique
    protected String customUniqueId;
    private String doctorId;

    private String locationId;

    private String hospitalId;

    private String patientId;

    private String createdBy;
    @Ignore
    private List<GeneralData> generalRecords;
    @Ignore
    private List<String> specialNotes;
    protected Long updatedTime;
    protected Long createdTime;
    private Boolean discarded;

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

    public List<GeneralData> getGeneralRecords() {
        return generalRecords;
    }

    public void setGeneralRecords(List<GeneralData> generalRecords) {
        this.generalRecords = generalRecords;
    }

    public List<String> getSpecialNotes() {
        return specialNotes;
    }

    public void setSpecialNotes(List<String> specialNotes) {
        this.specialNotes = specialNotes;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getDiscarded() {
        if (discarded == null)
            return false;
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }
}
