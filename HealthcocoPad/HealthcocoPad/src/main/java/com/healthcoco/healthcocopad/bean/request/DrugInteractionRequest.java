package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.Duration;
import com.healthcoco.healthcocopad.bean.server.GenericName;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import java.util.List;

/**
 * Created by neha on 04/02/17.
 */
public class DrugInteractionRequest {
    @Unique
    private String uniqueId;
    private String description;
    private String hospitalId;
    private String doctorId;
    private String locationId;
    private DrugType drugType;
    private Boolean discarded;
    private Long updatedTime;
    private String drugName;
    private String drugCode;
    @Ignore
    private Duration duration;
    @Ignore
    private List<DrugDirection> direction;
    private String dosage;

    @Ignore
    private List<GenericName> genericNames;

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

    public String getDrugCode() {
        return drugCode;
    }

    public void setDrugCode(String drugCode) {
        this.drugCode = drugCode;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
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

    public DrugType getDrugType() {
        return drugType;
    }

    public void setDrugType(DrugType drugType) {
        this.drugType = drugType;
    }

    public Boolean getDiscarded() {
        return discarded;
    }

    public void setDiscarded(Boolean discarded) {
        this.discarded = discarded;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getDrugName() {
        return drugName;
    }

    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    public List<DrugDirection> getDirection() {
        return direction;
    }

    public void setDirection(List<DrugDirection> direction) {
        this.direction = direction;
    }

    public List<GenericName> getGenericNames() {
        return genericNames;
    }

    public void setGenericNames(List<GenericName> genericNames) {
        this.genericNames = genericNames;
    }
}

