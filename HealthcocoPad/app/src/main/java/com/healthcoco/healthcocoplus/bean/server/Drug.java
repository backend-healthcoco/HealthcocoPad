package com.healthcoco.healthcocoplus.bean.server;

import com.healthcoco.healthcocoplus.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.io.Serializable;

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
