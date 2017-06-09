package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.server.AppointmentRequest;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import java.util.List;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class PrescriptionRequest {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(Prescription.class.getSimpleName());
    @Unique
    private String uniqueId;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String createdBy;
    private String patientId;
    private String name;
    private Long createdTime;
    private Long updatedTime;
    private List<DrugItem> items;
    private List<DiagnosticTest> diagnosticTests;

    private Boolean discarded;
    private String visitId;
    private String advice;
    @Ignore
    private AppointmentRequest appointmentRequest;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setItems(List<DrugItem> items) {
        this.items = items;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
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

    public List<DrugItem> getItems() {
        return items;
    }

    public List<DiagnosticTest> getDiagnosticTests() {
        return diagnosticTests;
    }

    public AppointmentRequest getAppointmentRequest() {
        return appointmentRequest;
    }

    public void setAppointmentRequest(AppointmentRequest appointmentRequest) {
        this.appointmentRequest = appointmentRequest;
    }
}
