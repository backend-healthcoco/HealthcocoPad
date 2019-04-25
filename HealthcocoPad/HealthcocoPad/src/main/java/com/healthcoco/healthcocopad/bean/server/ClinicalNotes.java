package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class ClinicalNotes extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(ClinicalNotes.class.getSimpleName());

    @Unique
    private String uniqueId;
    private String complaint;
    private String observation;
    private String investigation;
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
    private String priorConsultations;

    @Ignore
    private AppointmentRequest appointmentRequest;
    private String appointmentId;

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

    public List<Diagram> getDiagrams() {
        return diagrams;
    }

    public void setDiagrams(List<Diagram> diagrams) {
        this.diagrams = diagrams;
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

    public String getProcedureNote() {
        return procedureNote;
    }

    public void setProcedureNote(String procedureNote) {
        this.procedureNote = procedureNote;
    }

    public String getPriorConsultations() {
        return priorConsultations;
    }

    public void setPriorConsultations(String priorConsultations) {
        this.priorConsultations = priorConsultations;
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
                && Util.isNullOrBlank(diagnosis) && Util.isNullOrBlank(procedureNote)
                && Util.isNullOrBlank(getxRayDetails)
                && Util.isNullOrBlank(pcNose)
                && Util.isNullOrBlank(pcOralCavity)
                && Util.isNullOrBlank(pcThroat)
                && Util.isNullOrBlank(pcEars)
                && Util.isNullOrBlank(noseExam)
                && Util.isNullOrBlank(oralCavityThroatExam)
                && Util.isNullOrBlank(indirectLarygoscopyExam)
                && Util.isNullOrBlank(neckExam)
                && Util.isNullOrBlank(earsExam)
                && Util.isNullOrBlank(pastHistory)
                && Util.isNullOrBlank(familyHistory)
                && Util.isNullOrBlank(personalHistoryTobacco)
                && Util.isNullOrBlank(personalHistoryAlcohol)
                && Util.isNullOrBlank(personalHistorySmoking)
                && Util.isNullOrBlank(personalHistoryDiet)
                && Util.isNullOrBlank(personalHistoryOccupation)
                && Util.isNullOrBlank(generalHistoryDrugs)
                && Util.isNullOrBlank(generalHistoryMedicine)
                && Util.isNullOrBlank(generalHistoryAllergies)
                && Util.isNullOrBlank(generalHistorySurgical)
                && Util.isNullOrBlank(painScale)
                && Util.isNullOrEmptyList(diagrams)
                )
            return true;
        return false;
    }
}
