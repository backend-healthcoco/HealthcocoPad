package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;
@Parcel
public class Prescription extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(Prescription.class.getSimpleName());
    @Unique
    private String uniqueId;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String createdBy;
    private String patientId;
    private String prescriptionCode;
    private String name;
    private Long createdTime;
    private Long updatedTime;
    @Ignore
    private List<DrugItem> items;
    //required to get prescription in Response
    @Ignore
    private List<DiagnosticTest> diagnosticTests;
    private String diagnosticTestsIdsJsonString;

    private Boolean discarded;
    private Boolean inHistory;
    private String visitId;
    private String uniqueEmrId;
    private String advice;

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public Long getUpdatedTime() {
        if (updatedTime != null)
            return updatedTime;
        return 0L;
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

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getPrescriptionCode() {
        return prescriptionCode;
    }

    public void setPrescriptionCode(String prescriptionCode) {
        this.prescriptionCode = prescriptionCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<DrugItem> getItems() {
        return items;
    }

    public void setItems(List<DrugItem> items) {
        this.items = items;
    }

    public Boolean getDiscarded() {
        if (discarded == null)
            discarded = false;
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public Boolean getInHistory() {
        if (inHistory == null)
            inHistory = false;
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

    public String getUniqueEmrId() {
        return uniqueEmrId;
    }

    public void setUniqueEmrId(String uniqueEmrId) {
        this.uniqueEmrId = uniqueEmrId;
    }

    public List<DiagnosticTest> getDiagnosticTests() {
        return diagnosticTests;
    }

    public void setDiagnosticTests(List<DiagnosticTest> diagnosticTests) {
        this.diagnosticTests = diagnosticTests;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    public String getDiagnosticTestsIdsJsonString() {
        return diagnosticTestsIdsJsonString;
    }

    public void setDiagnosticTestsIdsJsonString(String diagnosticTestsIdsJsonString) {
        this.diagnosticTestsIdsJsonString = diagnosticTestsIdsJsonString;
    }
}
