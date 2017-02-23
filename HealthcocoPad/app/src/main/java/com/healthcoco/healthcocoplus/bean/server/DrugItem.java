package com.healthcoco.healthcocoplus.bean.server;

import com.healthcoco.healthcocoplus.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.io.Serializable;
import java.util.List;

public class DrugItem extends SugarRecord implements Serializable {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(DrugItem.class.getSimpleName());

    protected String foreignTableId;
    protected String foreignTableKey;
    private String dosage;
    private String instructions;
    private String drugId;
    @Ignore
    private Duration duration;
    protected String foreignDurationId;

    @Ignore
    private List<DrugDirection> direction;
    @Ignore
    private Drug drug;
    protected String foreignDrugId;
    private String doctorId;
    @Unique
    protected String customUniqueId;

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

    public String getForeignDrugId() {
        return foreignDrugId;
    }

    public void setForeignDrugId(String foreignDrugId) {
        this.foreignDrugId = foreignDrugId;
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
}
