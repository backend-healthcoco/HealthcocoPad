package com.healthcoco.healthcocopad.bean.request;


import com.healthcoco.healthcocopad.bean.server.AppointmentRequest;
import com.healthcoco.healthcocopad.bean.server.Complaint;
import com.healthcoco.healthcocopad.bean.server.Diagnoses;
import com.healthcoco.healthcocopad.bean.server.Investigation;
import com.healthcoco.healthcocopad.bean.server.Notes;
import com.healthcoco.healthcocopad.bean.server.Observation;
import com.healthcoco.healthcocopad.bean.server.VitalSigns;
import com.orm.annotation.Ignore;

import java.util.List;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class ClinicalNoteToSend {
    private String uniqueId;
    private List<Complaint> complaints;
    private List<Observation> observations;
    private List<Investigation> investigations;
    private List<Diagnoses> diagnoses;
    private VitalSigns vitalSigns;
    private Long createdTime;
    private Long updatedTime;
    private List<String> diagrams;
    private List<Notes> notes;


    private String doctorId;

    private String locationId;

    private String hospitalId;

    private Boolean inHistory;

    private Boolean deleted;

    private String patientId;
    private Boolean discarded;
    private String createdBy;
    private String visitId;

    private String complaint;
    private String observation;
    private String investigation;
    private String diagnosis;
    private String note;

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
    private String procedureNote;
    private String getxRayDetails;
    private String pcNose;
    private String pcOralCavity;
    private String pcThroat;
    private String pcEars;
    private String noseExam;
    private String oralCavityThroatExam;
    private String indirectLarygoscopyExam;
    private String neckExam;
    private String earsExam;
    private String pastHistory;
    private String familyHistory;
    private String personalHistoryTobacco;
    private String personalHistoryAlcohol;
    private String personalHistorySmoking;
    private String personalHistoryDiet;
    private String personalHistoryOccupation;
    private String generalHistoryDrugs;
    private String generalHistoryMedicine;
    private String generalHistoryAllergies;
    private String generalHistorySurgical;
    private String painScale;
    @Ignore
    private AppointmentRequest appointmentRequest;

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

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
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

    public List<Complaint> getComplaints() {
        return complaints;
    }

    public void setComplaints(List<Complaint> complaints) {
        this.complaints = complaints;
    }

    public List<Observation> getObservations() {
        return observations;
    }

    public void setObservations(List<Observation> observations) {
        this.observations = observations;
    }

    public List<Investigation> getInvestigations() {
        return investigations;
    }

    public void setInvestigations(List<Investigation> investigations) {
        this.investigations = investigations;
    }

    public List<Diagnoses> getDiagnoses() {
        return diagnoses;
    }

    public void setDiagnoses(List<Diagnoses> diagnoses) {
        this.diagnoses = diagnoses;
    }

    public List<Notes> getNotes() {
        return notes;
    }

    public void setNotes(List<Notes> notes) {
        this.notes = notes;
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

    public List<String> getDiagrams() {
        return diagrams;
    }

    public void setDiagrams(List<String> diagrams) {
        this.diagrams = diagrams;
    }

    public VitalSigns getVitalSigns() {
        return vitalSigns;
    }

    public void setVitalSigns(VitalSigns vitalSigns) {
        this.vitalSigns = vitalSigns;
    }

    public AppointmentRequest getAppointmentRequest() {
        return appointmentRequest;
    }

    public void setAppointmentRequest(AppointmentRequest appointmentRequest) {
        this.appointmentRequest = appointmentRequest;
    }

    public String getProcedureNote() {
        return procedureNote;
    }

    public void setProcedureNote(String procedureNote) {
        this.procedureNote = procedureNote;
    }

    public String getGetxRayDetails() {
        return getxRayDetails;
    }

    public void setGetxRayDetails(String getxRayDetails) {
        this.getxRayDetails = getxRayDetails;
    }

    public String getPcNose() {
        return pcNose;
    }

    public void setPcNose(String pcNose) {
        this.pcNose = pcNose;
    }

    public String getPcOralCavity() {
        return pcOralCavity;
    }

    public void setPcOralCavity(String pcOralCavity) {
        this.pcOralCavity = pcOralCavity;
    }

    public String getPcThroat() {
        return pcThroat;
    }

    public void setPcThroat(String pcThroat) {
        this.pcThroat = pcThroat;
    }

    public String getPcEars() {
        return pcEars;
    }

    public void setPcEars(String pcEars) {
        this.pcEars = pcEars;
    }

    public String getNoseExam() {
        return noseExam;
    }

    public void setNoseExam(String noseExam) {
        this.noseExam = noseExam;
    }

    public String getOralCavityThroatExam() {
        return oralCavityThroatExam;
    }

    public void setOralCavityThroatExam(String oralCavityThroatExam) {
        this.oralCavityThroatExam = oralCavityThroatExam;
    }

    public String getIndirectLarygoscopyExam() {
        return indirectLarygoscopyExam;
    }

    public void setIndirectLarygoscopyExam(String indirectLarygoscopyExam) {
        this.indirectLarygoscopyExam = indirectLarygoscopyExam;
    }

    public String getNeckExam() {
        return neckExam;
    }

    public void setNeckExam(String neckExam) {
        this.neckExam = neckExam;
    }

    public String getEarsExam() {
        return earsExam;
    }

    public void setEarsExam(String earsExam) {
        this.earsExam = earsExam;
    }

    public String getPastHistory() {
        return pastHistory;
    }

    public void setPastHistory(String pastHistory) {
        this.pastHistory = pastHistory;
    }

    public String getFamilyHistory() {
        return familyHistory;
    }

    public void setFamilyHistory(String familyHistory) {
        this.familyHistory = familyHistory;
    }

    public String getPersonalHistoryTobacco() {
        return personalHistoryTobacco;
    }

    public void setPersonalHistoryTobacco(String personalHistoryTobacco) {
        this.personalHistoryTobacco = personalHistoryTobacco;
    }

    public String getPersonalHistoryAlcohol() {
        return personalHistoryAlcohol;
    }

    public void setPersonalHistoryAlcohol(String personalHistoryAlcohol) {
        this.personalHistoryAlcohol = personalHistoryAlcohol;
    }

    public String getPersonalHistorySmoking() {
        return personalHistorySmoking;
    }

    public void setPersonalHistorySmoking(String personalHistorySmoking) {
        this.personalHistorySmoking = personalHistorySmoking;
    }

    public String getPersonalHistoryDiet() {
        return personalHistoryDiet;
    }

    public void setPersonalHistoryDiet(String personalHistoryDiet) {
        this.personalHistoryDiet = personalHistoryDiet;
    }

    public String getPersonalHistoryOccupation() {
        return personalHistoryOccupation;
    }

    public void setPersonalHistoryOccupation(String personalHistoryOccupation) {
        this.personalHistoryOccupation = personalHistoryOccupation;
    }

    public String getGeneralHistoryDrugs() {
        return generalHistoryDrugs;
    }

    public void setGeneralHistoryDrugs(String generalHistoryDrugs) {
        this.generalHistoryDrugs = generalHistoryDrugs;
    }

    public String getGeneralHistoryMedicine() {
        return generalHistoryMedicine;
    }

    public void setGeneralHistoryMedicine(String generalHistoryMedicine) {
        this.generalHistoryMedicine = generalHistoryMedicine;
    }

    public String getGeneralHistoryAllergies() {
        return generalHistoryAllergies;
    }

    public void setGeneralHistoryAllergies(String generalHistoryAllergies) {
        this.generalHistoryAllergies = generalHistoryAllergies;
    }

    public String getGeneralHistorySurgical() {
        return generalHistorySurgical;
    }

    public void setGeneralHistorySurgical(String generalHistorySurgical) {
        this.generalHistorySurgical = generalHistorySurgical;
    }

    public String getPainScale() {
        return painScale;
    }

    public void setPainScale(String painScale) {
        this.painScale = painScale;
    }
}
