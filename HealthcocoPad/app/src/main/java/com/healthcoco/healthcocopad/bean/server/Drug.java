package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import java.io.Serializable;
import java.util.List;

public class Drug extends SugarRecord implements Serializable {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(Drug.class.getSimpleName());
    @Unique
    private String uniqueId;
    private String explanation;
    private String doctorId;
    private String hospitalId;
    private String locationId;
    @Ignore
    private DrugType drugType;
    protected String foreignDrugTypeId;
    private Boolean discarded;
    private String drugCode;
    private String genericId;
    private String companyName;
    private String packSize;
    private String mrp;
    private String drugName;
    private Long createdTime;
    private Long updatedTime;
    protected boolean isDrugFromGetDrugsList;
    @Ignore
    private DrugDosage dosageTime;
    @Ignore
    private DrugDirection direction;
    private String dosage;
    @Ignore
    private Duration duration;
    private String categories;
    private int rankingCount;
    @Ignore
    private List<GenericName> genericNames;
    private String genericNamesJsonString;

    public List<GenericName> getGenericNames() {
        return genericNames;
    }

    public void setGenericNames(List<GenericName> genericNames) {
        this.genericNames = genericNames;
    }

    public String getGenericNamesJsonString() {
        return genericNamesJsonString;
    }

    public void setGenericNamesJsonString(String genericNamesJsonString) {
        this.genericNamesJsonString = genericNamesJsonString;
    }

    public DrugDosage getDosageTime() {
        return dosageTime;
    }

    public void setDosageTime(DrugDosage dosageTime) {
        this.dosageTime = dosageTime;
    }

    public DrugDirection getDirection() {
        return direction;
    }

    public void setDirection(DrugDirection direction) {
        this.direction = direction;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getRankingCount() {
        return rankingCount;
    }

    public void setRankingCount(int rankingCount) {
        this.rankingCount = rankingCount;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public DrugType getDrugType() {
        return drugType;
    }

    public void setDrugType(DrugType drugType) {
        this.drugType = drugType;
    }

    public String getForeignDrugTypeId() {
        return foreignDrugTypeId;
    }

    public void setForeignDrugTypeId(String foreignDrugTypeId) {
        this.foreignDrugTypeId = foreignDrugTypeId;
    }

    public Boolean getDiscarded() {
        if (discarded == null)
            return false;
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getGenericId() {
        return genericId;
    }

    public void setGenericId(String genericId) {
        this.genericId = genericId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public String getMrp() {
        return mrp;
    }

    public void setMrp(String mrp) {
        this.mrp = mrp;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
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

    public boolean isDrugFromGetDrugsList() {
        return isDrugFromGetDrugsList;
    }

    public void setIsDrugFromGetDrugsList(boolean isDrugFromGetDrugsList) {
        this.isDrugFromGetDrugsList = isDrugFromGetDrugsList;
    }
}
