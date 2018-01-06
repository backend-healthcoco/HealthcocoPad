package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by neha on 22/11/15.
 */
@Parcel
public class DrugItemRecentlyPrescribed extends SugarRecord {
    @Unique
    private String drugId;
    private String drugName;
    private String drugType;
    private String drugTypeId;
    private String dosageText;
    private Boolean discarded;

    // accepts foreign durationUnitId
    private String durationUnitId;
    private String durationText;
    private String durationUnit;

    private String directionsId;
    private String directionsText;
    private String instructions;
    private Long createdTime;
    private String genericNamesString;
    @Ignore
    private List<GenericName> genericNames;

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public String getDirectionsId() {
        return directionsId;
    }

    public void setDirectionsId(String directionsId) {
        this.directionsId = directionsId;
    }

    public String getDurationText() {
        return durationText;
    }

    public void setDurationText(String durationText) {
        this.durationText = durationText;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public String getDrugId() {
        return drugId;
    }

    public void setDrugId(String drugId) {
        this.drugId = drugId;
    }

    public String getDosageText() {
        return dosageText;
    }

    public void setDosageText(String dosageText) {
        this.dosageText = dosageText;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    public String getDirectionsText() {
        return directionsText;
    }

    public void setDirectionsText(String directionsText) {
        this.directionsText = directionsText;
    }

    public String getDrugTypeId() {
        return drugTypeId;
    }

    public void setDrugTypeId(String drugTypeId) {
        this.drugTypeId = drugTypeId;
    }

    public String getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(String durationUnit) {
        this.durationUnit = durationUnit;
    }

    public String getDurationUnitId() {
        return durationUnitId;
    }

    public void setDurationUnitId(String durationUnitId) {
        this.durationUnitId = durationUnitId;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public List<GenericName> getGenericNames() {
        return genericNames;
    }

    public void setGenericNames(List<GenericName> genericNames) {
        this.genericNames = genericNames;
    }

    public String getGenericNamesString() {
        return genericNamesString;
    }

    public void setGenericNamesString(String genericNamesString) {
        this.genericNamesString = genericNamesString;
    }
}