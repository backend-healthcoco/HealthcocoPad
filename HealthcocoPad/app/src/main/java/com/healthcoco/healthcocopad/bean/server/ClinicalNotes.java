package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import java.util.List;

public class ClinicalNotes extends SugarRecord {
    @Unique
    private String uniqueId;
    private String complaint;
    private String observation;
    private String investigation;
    @Ignore
    private List<Diagnoses> diagnoses;
    private String diagnosis;

    private Long createdTime;
    private Long updatedTime;
    @Ignore
    private List<Diagram> diagrams;
    private String note;

    private String doctorId;

    private String locationId;

    private String hospitalId;

    private Boolean inHistory;

    private Boolean deleted;

    private String patientId;
    private Boolean discarded;
    private String createdBy;
    private String visitId;
    private String uniqueEmrId;
    @Ignore
    private VitalSigns vitalSigns;
    private String provisionalDiagnosis;
    private String generalExam;
    private String systemExam;
    private String presentComplaint;
    private String presentComplaintHistory;
    private String menstrualHistory;
    private String obstetricHistory;
    private String indicationOfUSG;
    private String pv;
    private String pa;
    private String ps;
    private String ecgDetails;
    private String xRayDetails;
    private String echo;
    private String holter;

    public String getProvisionalDiagnosis() {
        return provisionalDiagnosis;
    }

    public void setProvisionalDiagnosis(String provisionalDiagnosis) {
        this.provisionalDiagnosis = provisionalDiagnosis;
    }

    public String getGeneralExam() {
        return generalExam;
    }

    public void setGeneralExam(String generalExam) {
        this.generalExam = generalExam;
    }

    public String getSystemExam() {
        return systemExam;
    }

    public void setSystemExam(String systemExam) {
        this.systemExam = systemExam;
    }

    public String getPresentComplaint() {
        return presentComplaint;
    }

    public void setPresentComplaint(String presentComplaint) {
        this.presentComplaint = presentComplaint;
    }

    public String getPresentComplaintHistory() {
        return presentComplaintHistory;
    }

    public void setPresentComplaintHistory(String presentComplaintHistory) {
        this.presentComplaintHistory = presentComplaintHistory;
    }

    public String getMenstrualHistory() {
        return menstrualHistory;
    }

    public void setMenstrualHistory(String menstrualHistory) {
        this.menstrualHistory = menstrualHistory;
    }

    public String getObstetricHistory() {
        return obstetricHistory;
    }

    public void setObstetricHistory(String obstetricHistory) {
        this.obstetricHistory = obstetricHistory;
    }

    public String getIndicationOfUSG() {
        return indicationOfUSG;
    }

    public void setIndicationOfUSG(String indicationOfUSG) {
        this.indicationOfUSG = indicationOfUSG;
    }

    public String getPv() {
        return pv;
    }

    public void setPv(String pv) {
        this.pv = pv;
    }

    public String getPa() {
        return pa;
    }

    public void setPa(String pa) {
        this.pa = pa;
    }

    public String getPs() {
        return ps;
    }

    public void setPs(String ps) {
        this.ps = ps;
    }

    public String getEcgDetails() {
        return ecgDetails;
    }

    public void setEcgDetails(String ecgDetails) {
        this.ecgDetails = ecgDetails;
    }

    public String getxRayDetails() {
        return xRayDetails;
    }

    public void setxRayDetails(String xRayDetails) {
        this.xRayDetails = xRayDetails;
    }

    public String getEcho() {
        return echo;
    }

    public void setEcho(String echo) {
        this.echo = echo;
    }

    public String getHolter() {
        return holter;
    }

    public void setHolter(String holter) {
        this.holter = holter;
    }

    public String getComplaint() {
        return complaint;
    }

    public void setComplaint(String complaint) {
        this.complaint = complaint;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public String getInvestigation() {
        return investigation;
    }

    public void setInvestigation(String investigation) {
        this.investigation = investigation;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }
//    public List<Complaint> getComplaints() {
//        return complaints;
//    }
//
//    public void setComplaints(List<Complaint> complaints) {
//        this.complaints = complaints;
//    }
//
//    public List<Observation> getObservations() {
//        return observations;
//    }
//
//    public void setObservations(List<Observation> observations) {
//        this.observations = observations;
//    }
//
//    public List<Investigation> getInvestigations() {
//        return investigations;
//    }
//
//    public void setInvestigations(List<Investigation> investigations) {
//        this.investigations = investigations;
//    }

    public List<Diagnoses> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<Diagnoses> diagnoses) {
        this.diagnoses = diagnoses;
    }

    public List<Diagram> getDiagrams() {
        return diagrams;
    }

    public void setDiagrams(List<Diagram> diagrams) {
        this.diagrams = diagrams;
    }

//    public List<Notes> getNotes() {
//        return notes;
//    }
//
//    public void setNotes(List<Notes> notes) {
//        this.notes = notes;
//    }

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

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
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

    public Boolean getInHistory() {
        return inHistory;
    }

    public void setInHistory(Boolean inHistory) {
        this.inHistory = inHistory;
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

    public String getUniqueEmrId() {
        return uniqueEmrId;
    }

    public void setUniqueEmrId(String uniqueEmrId) {
        this.uniqueEmrId = uniqueEmrId;
    }

    public VitalSigns getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(VitalSigns vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public boolean areAllFieldsNull() {
        if (vitalSigns != null
                && Util.isNullOrBlank(indicationOfUSG)
                && Util.isNullOrBlank(ps)
                && Util.isNullOrBlank(pv)
                && Util.isNullOrBlank(pa)
                && Util.isNullOrBlank(holter)
                && Util.isNullOrBlank(echo)
                && Util.isNullOrBlank(xRayDetails)
                && Util.isNullOrBlank(ecgDetails)
                && Util.isNullOrBlank(presentComplaint)
                && Util.isNullOrBlank(complaint)
                && Util.isNullOrBlank(presentComplaintHistory)
                && Util.isNullOrBlank(menstrualHistory)
                && Util.isNullOrBlank(obstetricHistory)
                && Util.isNullOrBlank(generalExam)
                && Util.isNullOrBlank(systemExam)
                && Util.isNullOrBlank(note)
                && Util.isNullOrBlank(provisionalDiagnosis)
                && Util.isNullOrBlank(diagnosis)
                && Util.isNullOrEmptyList(diagrams)
                )
            return true;
        return false;
    }
}
