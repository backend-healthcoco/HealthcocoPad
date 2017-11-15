package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.request.ClinicalNoteToSend;
import com.healthcoco.healthcocopad.bean.request.PrescriptionRequest;
import com.healthcoco.healthcocopad.enums.VisitedForType;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import java.util.List;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class VisitDetails extends SugarRecord {
    protected Long updatedTime;
    @Unique
    private String uniqueId;
    private String doctorId;
    private String locationId;
    private String patientId;
    private String hospitalId;
    @Ignore
    private List<Prescription> prescriptions;
    @Ignore
    private List<ClinicalNotes> clinicalNotes;
    @Ignore
    private List<Records> records;
    @Ignore
    private List<Treatments> patientTreatments;
    @Ignore
    private List<VisitedForType> visitedFor;
    private Long visitedTime;
    private Long createdTime;
    @Ignore
    private ClinicalNoteToSend clinicalNote;
    @Ignore
    private PrescriptionRequest prescription;

    private String uniqueEmrId;

    @Ignore
    private RegisteredPatientDetailsUpdated selectedPatient;

    @Ignore
    private AppointmentRequest appointmentRequest;

    private String createdBy;
    private String visitId;
    private String appointmentId;
    private Boolean discarded;

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

    public List<Prescription> getPrescriptions() {
        return prescriptions;
    }

    public void setPrescriptions(List<Prescription> prescriptions) {
        this.prescriptions = prescriptions;
    }

    public List<ClinicalNotes> getClinicalNotes() {
        return clinicalNotes;
    }

    public void setClinicalNotes(List<ClinicalNotes> clinicalNotes) {
        this.clinicalNotes = clinicalNotes;
    }

    public List<Treatments> getPatientTreatments() {
        return patientTreatments;
    }

    public void setPatientTreatments(List<Treatments> patientTreatments) {
        this.patientTreatments = patientTreatments;
    }

    public List<Records> getRecords() {
        return records;
    }

    public void setRecords(List<Records> records) {
        this.records = records;
    }

    public List<VisitedForType> getVisitedFor() {
        return visitedFor;
    }

    public void setVisitedFor(List<VisitedForType> visitedFor) {
        this.visitedFor = visitedFor;
    }

    public Long getVisitedTime() {
        return visitedTime;
    }

    public void setVisitedTime(Long visitedTime) {
        this.visitedTime = visitedTime;
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

    public ClinicalNoteToSend getClinicalNote() {
        return clinicalNote;
    }

    public void setClinicalNote(ClinicalNoteToSend clinicalNote) {
        this.clinicalNote = clinicalNote;
    }

    public PrescriptionRequest getPrescription() {
        return prescription;
    }

    public void setPrescription(PrescriptionRequest prescription) {
        this.prescription = prescription;
    }

    public String getUniqueEmrId() {
        return uniqueEmrId;
    }

    public void setUniqueEmrId(String uniqueEmrId) {
        this.uniqueEmrId = uniqueEmrId;
    }

    public RegisteredPatientDetailsUpdated getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(RegisteredPatientDetailsUpdated selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public AppointmentRequest getAppointmentRequest() {
        return appointmentRequest;
    }

    public void setAppointmentRequest(AppointmentRequest appointmentRequest) {
        this.appointmentRequest = appointmentRequest;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }
}
