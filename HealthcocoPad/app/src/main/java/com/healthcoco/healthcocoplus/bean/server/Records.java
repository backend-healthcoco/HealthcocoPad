package com.healthcoco.healthcocoplus.bean.server;

import android.graphics.Bitmap;

import com.healthcoco.healthcocoplus.enums.RecordState;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

public class Records extends SugarRecord {
    @Unique
    private String uniqueId;

    private String doctorId;

    private String locationId;

    private String hospitalId;

    private String recordsUrl;

    private String recordsLabel;

    private String recordsType;

    private String explanation;

    private Boolean inHistory;

    private Boolean discarded;

    private String createdBy;
    private String patientId;
    private Long createdTime;
    private Long updatedTime;

    private String uploadedByLocation;
    private String visitId;
    private String uniqueEmrId;
//    @Ignore
//    private FileDetails fileDetails;
    @Ignore
    private Bitmap recordsImageBitmap;
    @Ignore
    private String loadedBitmapsRecordUrl;

    private RecordState recordsState;
    private String prescribedByDoctorId;
    private String prescribedByLocationId;
    private String prescribedByHospitalId;
    private String diagnosticTestId;

    public String getPrescribedByDoctorId() {
        return prescribedByDoctorId;
    }

    public void setPrescribedByDoctorId(String prescribedByDoctorId) {
        this.prescribedByDoctorId = prescribedByDoctorId;
    }

    public String getPrescribedByLocationId() {
        return prescribedByLocationId;
    }

    public void setPrescribedByLocationId(String prescribedByLocationId) {
        this.prescribedByLocationId = prescribedByLocationId;
    }

    public String getPrescribedByHospitalId() {
        return prescribedByHospitalId;
    }

    public void setPrescribedByHospitalId(String prescribedByHospitalId) {
        this.prescribedByHospitalId = prescribedByHospitalId;
    }

    public String getDiagnosticTestId() {
        return diagnosticTestId;
    }

    public void setDiagnosticTestId(String diagnosticTestId) {
        this.diagnosticTestId = diagnosticTestId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getRecordsUrl() {
        return recordsUrl;
    }

    public void setRecordsUrl(String recordsUrl) {
        this.recordsUrl = recordsUrl;
    }

    public String getRecordsLabel() {
        return recordsLabel;
    }

    public void setRecordsLabel(String recordsLabel) {
        this.recordsLabel = recordsLabel;
    }

    public String getRecordsType() {
        return recordsType;
    }

    public void setRecordsType(String recordsType) {
        this.recordsType = recordsType;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
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

//    public FileDetails getFileDetails() {
//        return fileDetails;
//    }

//    public void setFileDetails(FileDetails fileDetails) {
//        this.fileDetails = fileDetails;
//    }

    public Boolean getInHistory() {
        return inHistory;
    }

    public void setInHistory(Boolean inHistory) {
        this.inHistory = inHistory;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUploadedByLocation() {
        return uploadedByLocation;
    }

    public void setUploadedByLocation(String uploadedByLocation) {
        this.uploadedByLocation = uploadedByLocation;
    }

    public String getUniqueEmrId() {
        return uniqueEmrId;
    }

    public void setUniqueEmrId(String uniqueEmrId) {
        this.uniqueEmrId = uniqueEmrId;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Bitmap getRecordsImageBitmap() {
        return recordsImageBitmap;
    }

    public void setRecordsImageBitmap(Bitmap recordsImageBitmap) {
        this.recordsImageBitmap = recordsImageBitmap;
    }

    public String getLoadedBitmapsRecordUrl() {
        return loadedBitmapsRecordUrl;
    }

    public void setLoadedBitmapsRecordUrl(String loadedBitmapsRecordUrl) {
        this.loadedBitmapsRecordUrl = loadedBitmapsRecordUrl;
    }

    public RecordState getRecordsState() {
        return recordsState;
    }

    public void setRecordsState(RecordState recordsState) {
        this.recordsState = recordsState;
    }
}
