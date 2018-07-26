package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class InvestigationNote extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(InvestigationNote.class.getSimpleName());


    private Long createdTime;
    private Long updatedTime;
    @Unique
    private String uniqueId;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private Boolean inHistory;
    private Boolean deleted;
    private String patientId;
    private boolean discarded;
    private String createdBy;
    private String investigationId;

    private String absoluteEosinophilCount;
    private String haemoglobin;
    private String totalWcbCount;
    private String haematocrit;
    private String neutrophils;
    private String lymphocytes;
    private String eosinophils;
    private String monocytes;
    private String basophils;
    private String rbcRedBloodCells;
    private String erythrocyteSedimentationrate;
    private String rbcs;
    private String wbcs;
    private String platelets;
    private String haemoparasites;
    private String impression;
    private String meanCorpuscularVolume;
    private String meanCorpuscularHaemoglobin;
    private String meanCorpuscularHaemoglobinConcentration;
    private String fastingBloodSugar;
    private String fastingUrineSugar;
    private String postPrandialBloodSugar;
    private String postPrandialUrineSugar;
    private String glycosylatedHaemoglobin;
    private String meanBloodGlucose;
    private String rbs;
    private String randomUrineSugar;
    private String ketone;
    private String protein;
    private String totalCholesterol;
    private String serumHdlCholesterol;
    private String serumTriglycerides;
    private String serumLdlCholesterol;
    private String serumVdlCholesterol;
    private String totalCholesterolHdlRatio;
    private String ldlCholesterol;
    private String trigkycerides;
    private String nonHdlCholesterol;
    private String bloodUrea;
    private String serumCreatinine;
    private String serumSodium;
    private String serumBilirubinTotal;
    private String serumBilirubinDirect;
    private String bilirubinIndirect;

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

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Boolean getInHistory() {
        return inHistory;
    }

    public void setInHistory(Boolean inHistory) {
        this.inHistory = inHistory;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }


    public String getAbsoluteEosinophilCount() {
        return absoluteEosinophilCount;
    }

    public void setAbsoluteEosinophilCount(String absoluteEosinophilCount) {
        this.absoluteEosinophilCount = absoluteEosinophilCount;
    }

    public String getHaemoglobin() {
        return haemoglobin;
    }

    public void setHaemoglobin(String haemoglobin) {
        this.haemoglobin = haemoglobin;
    }

    public String getTotalWcbCount() {
        return totalWcbCount;
    }

    public void setTotalWcbCount(String totalWcbCount) {
        this.totalWcbCount = totalWcbCount;
    }

    public String getHaematocrit() {
        return haematocrit;
    }

    public void setHaematocrit(String haematocrit) {
        this.haematocrit = haematocrit;
    }

    public String getNeutrophils() {
        return neutrophils;
    }

    public void setNeutrophils(String neutrophils) {
        this.neutrophils = neutrophils;
    }

    public String getLymphocytes() {
        return lymphocytes;
    }

    public void setLymphocytes(String lymphocytes) {
        this.lymphocytes = lymphocytes;
    }

    public String getEosinophils() {
        return eosinophils;
    }

    public void setEosinophils(String eosinophils) {
        this.eosinophils = eosinophils;
    }

    public String getMonocytes() {
        return monocytes;
    }

    public void setMonocytes(String monocytes) {
        this.monocytes = monocytes;
    }

    public String getBasophils() {
        return basophils;
    }

    public void setBasophils(String basophils) {
        this.basophils = basophils;
    }

    public String getRbcRedBloodCells() {
        return rbcRedBloodCells;
    }

    public void setRbcRedBloodCells(String rbcRedBloodCells) {
        this.rbcRedBloodCells = rbcRedBloodCells;
    }

    public String getErythrocyteSedimentationrate() {
        return erythrocyteSedimentationrate;
    }

    public void setErythrocyteSedimentationrate(String erythrocyteSedimentationrate) {
        this.erythrocyteSedimentationrate = erythrocyteSedimentationrate;
    }

    public String getRbcs() {
        return rbcs;
    }

    public void setRbcs(String rbcs) {
        this.rbcs = rbcs;
    }

    public String getWbcs() {
        return wbcs;
    }

    public void setWbcs(String wbcs) {
        this.wbcs = wbcs;
    }

    public String getPlatelets() {
        return platelets;
    }

    public void setPlatelets(String platelets) {
        this.platelets = platelets;
    }

    public String getHaemoparasites() {
        return haemoparasites;
    }

    public void setHaemoparasites(String haemoparasites) {
        this.haemoparasites = haemoparasites;
    }

    public String getImpression() {
        return impression;
    }

    public void setImpression(String impression) {
        this.impression = impression;
    }

    public String getMeanCorpuscularVolume() {
        return meanCorpuscularVolume;
    }

    public void setMeanCorpuscularVolume(String meanCorpuscularVolume) {
        this.meanCorpuscularVolume = meanCorpuscularVolume;
    }

    public String getMeanCorpuscularHaemoglobin() {
        return meanCorpuscularHaemoglobin;
    }

    public void setMeanCorpuscularHaemoglobin(String meanCorpuscularHaemoglobin) {
        this.meanCorpuscularHaemoglobin = meanCorpuscularHaemoglobin;
    }

    public String getMeanCorpuscularHaemoglobinConcentration() {
        return meanCorpuscularHaemoglobinConcentration;
    }

    public void setMeanCorpuscularHaemoglobinConcentration(String meanCorpuscularHaemoglobinConcentration) {
        this.meanCorpuscularHaemoglobinConcentration = meanCorpuscularHaemoglobinConcentration;
    }

    public String getFastingBloodSugar() {
        return fastingBloodSugar;
    }

    public void setFastingBloodSugar(String fastingBloodSugar) {
        this.fastingBloodSugar = fastingBloodSugar;
    }

    public String getFastingUrineSugar() {
        return fastingUrineSugar;
    }

    public void setFastingUrineSugar(String fastingUrineSugar) {
        this.fastingUrineSugar = fastingUrineSugar;
    }

    public String getPostPrandialBloodSugar() {
        return postPrandialBloodSugar;
    }

    public void setPostPrandialBloodSugar(String postPrandialBloodSugar) {
        this.postPrandialBloodSugar = postPrandialBloodSugar;
    }

    public String getPostPrandialUrineSugar() {
        return postPrandialUrineSugar;
    }

    public void setPostPrandialUrineSugar(String postPrandialUrineSugar) {
        this.postPrandialUrineSugar = postPrandialUrineSugar;
    }

    public String getGlycosylatedHaemoglobin() {
        return glycosylatedHaemoglobin;
    }

    public void setGlycosylatedHaemoglobin(String glycosylatedHaemoglobin) {
        this.glycosylatedHaemoglobin = glycosylatedHaemoglobin;
    }

    public String getMeanBloodGlucose() {
        return meanBloodGlucose;
    }

    public void setMeanBloodGlucose(String meanBloodGlucose) {
        this.meanBloodGlucose = meanBloodGlucose;
    }

    public String getRbs() {
        return rbs;
    }

    public void setRbs(String rbs) {
        this.rbs = rbs;
    }

    public String getRandomUrineSugar() {
        return randomUrineSugar;
    }

    public void setRandomUrineSugar(String randomUrineSugar) {
        this.randomUrineSugar = randomUrineSugar;
    }

    public String getKetone() {
        return ketone;
    }

    public void setKetone(String ketone) {
        this.ketone = ketone;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getTotalCholesterol() {
        return totalCholesterol;
    }

    public void setTotalCholesterol(String totalCholesterol) {
        this.totalCholesterol = totalCholesterol;
    }

    public String getSerumHdlCholesterol() {
        return serumHdlCholesterol;
    }

    public void setSerumHdlCholesterol(String serumHdlCholesterol) {
        this.serumHdlCholesterol = serumHdlCholesterol;
    }

    public String getSerumTriglycerides() {
        return serumTriglycerides;
    }

    public void setSerumTriglycerides(String serumTriglycerides) {
        this.serumTriglycerides = serumTriglycerides;
    }

    public String getSerumLdlCholesterol() {
        return serumLdlCholesterol;
    }

    public void setSerumLdlCholesterol(String serumLdlCholesterol) {
        this.serumLdlCholesterol = serumLdlCholesterol;
    }

    public String getSerumVdlCholesterol() {
        return serumVdlCholesterol;
    }

    public void setSerumVdlCholesterol(String serumVdlCholesterol) {
        this.serumVdlCholesterol = serumVdlCholesterol;
    }

    public String getTotalCholesterolHdlRatio() {
        return totalCholesterolHdlRatio;
    }

    public void setTotalCholesterolHdlRatio(String totalCholesterolHdlRatio) {
        this.totalCholesterolHdlRatio = totalCholesterolHdlRatio;
    }

    public String getLdlCholesterol() {
        return ldlCholesterol;
    }

    public void setLdlCholesterol(String ldlCholesterol) {
        this.ldlCholesterol = ldlCholesterol;
    }

    public String getTrigkycerides() {
        return trigkycerides;
    }

    public void setTrigkycerides(String trigkycerides) {
        this.trigkycerides = trigkycerides;
    }

    public String getNonHdlCholesterol() {
        return nonHdlCholesterol;
    }

    public void setNonHdlCholesterol(String nonHdlCholesterol) {
        this.nonHdlCholesterol = nonHdlCholesterol;
    }

    public String getBloodUrea() {
        return bloodUrea;
    }

    public void setBloodUrea(String bloodUrea) {
        this.bloodUrea = bloodUrea;
    }

    public String getSerumCreatinine() {
        return serumCreatinine;
    }

    public void setSerumCreatinine(String serumCreatinine) {
        this.serumCreatinine = serumCreatinine;
    }

    public String getSerumSodium() {
        return serumSodium;
    }

    public void setSerumSodium(String serumSodium) {
        this.serumSodium = serumSodium;
    }

    public String getSerumBilirubinTotal() {
        return serumBilirubinTotal;
    }

    public void setSerumBilirubinTotal(String serumBilirubinTotal) {
        this.serumBilirubinTotal = serumBilirubinTotal;
    }

    public String getSerumBilirubinDirect() {
        return serumBilirubinDirect;
    }

    public void setSerumBilirubinDirect(String serumBilirubinDirect) {
        this.serumBilirubinDirect = serumBilirubinDirect;
    }

    public String getBilirubinIndirect() {
        return bilirubinIndirect;
    }

    public void setBilirubinIndirect(String bilirubinIndirect) {
        this.bilirubinIndirect = bilirubinIndirect;
    }

    public String getInvestigationId() {
        return investigationId;
    }

    public void setInvestigationId(String investigationId) {
        this.investigationId = investigationId;
    }
}
