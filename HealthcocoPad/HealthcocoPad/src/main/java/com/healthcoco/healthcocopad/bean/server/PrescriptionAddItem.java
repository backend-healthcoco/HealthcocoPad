package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.Duration;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class PrescriptionAddItem extends SugarRecord {
    private static final String GENERIC_NAME_SEPARATOR = " + ";
    public static String TABLE_NAME = " " + StringUtil.toSQLName(PrescriptionAddItem.class.getSimpleName());
    protected String foreignDrugTypeId;
    protected boolean isDrugFromGetDrugsList;
    @Unique
    private String drugId;
    @Ignore
    private DrugType drugType;
    private String drugName;
    private String explanation;
    @Ignore
    private Duration duration;
    private String dosage;
    @Ignore
    private DrugDosage dosageTime;
    @Ignore
    private List<DrugDirection> direction;
    private String instructions;

    public String getForeignDrugTypeId() {
        return foreignDrugTypeId;
    }

    public void setForeignDrugTypeId(String foreignDrugTypeId) {
        this.foreignDrugTypeId = foreignDrugTypeId;
    }

    public boolean isDrugFromGetDrugsList() {
        return isDrugFromGetDrugsList;
    }

    public void setDrugFromGetDrugsList(boolean drugFromGetDrugsList) {
        isDrugFromGetDrugsList = drugFromGetDrugsList;
    }

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public DrugType getDrugType() {
        return drugType;
    }

    public void setDrugType(DrugType drugType) {
        this.drugType = drugType;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public DrugDosage getDosageTime() {
        return dosageTime;
    }

    public void setDosageTime(DrugDosage dosageTime) {
        this.dosageTime = dosageTime;
    }

    public List<DrugDirection> getDirection() {
        return direction;
    }

    public void setDirection(List<DrugDirection> direction) {
        this.direction = direction;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getFormattedDrugName() {
        String drugName = getDrugName();
        if (getDrugType() != null) {
            String type = getDrugType().getType();
            drugName = type + " " + drugName;
        }
        return drugName;
    }

}
