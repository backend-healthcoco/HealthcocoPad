package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.Duration;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class DrugItem extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(DrugItem.class.getSimpleName());

    protected String foreignTableId;
    protected String foreignTableKey;
    private String dosage;
    private String instructions;
    private String drugId;
    @Ignore
    private Duration duration;
    protected String durationJsonString;
    protected String foreignDurationId;

    @Ignore
    private List<DrugDirection> direction;
    protected String directionJsonString;
    @Ignore
    private Drug drug;
    private String doctorId;
    @Unique
    protected String customUniqueId;

    private String drugName;
    @Ignore
    private DrugType drugType;

    public List<GenericName> getGenericNames() {
        return genericNames;
    }

    public void setGenericNames(List<GenericName> genericNames) {
        this.genericNames = genericNames;
    }

    @Ignore
    protected List<GenericName> genericNames;

    public String getGenericNamesJsonString() {
        return genericNamesJsonString;
    }

    public void setGenericNamesJsonString(String genericNamesJsonString) {
        this.genericNamesJsonString = genericNamesJsonString;
    }

    protected String genericNamesJsonString;
    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Drug getDrug() {
        return drug;
    }

    public void setDrug(Drug drug) {
        this.drug = drug;
    }

    public List<DrugDirection> getDirection() {
        return direction;
    }

    public void setDirection(List<DrugDirection> direction) {
        this.direction = direction;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public String getForeignDurationId() {
        return foreignDurationId;
    }

    public void setForeignDurationId(String foreignDurationId) {
        this.foreignDurationId = foreignDurationId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

    public String getForeignTableKey() {
        return foreignTableKey;
    }

    public void setForeignTableKey(String foreignTableKey) {
        this.foreignTableKey = foreignTableKey;
    }

    public String getForeignTableId() {
        return foreignTableId;
    }

    public void setForeignTableId(String foreignTableId) {
        this.foreignTableId = foreignTableId;
    }

    public String getDurationJsonString() {
        return durationJsonString;
    }

    public void setDurationJsonString(String durationJsonString) {
        this.durationJsonString = durationJsonString;
    }

    public String getDirectionJsonString() {
        return directionJsonString;
    }

    public void setDirectionJsonString(String directionJsonString) {
        this.directionJsonString = directionJsonString;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public DrugType getDrugType() {
        return drugType;
    }

    public void setDrugType(DrugType drugType) {
        this.drugType = drugType;
    }
}
