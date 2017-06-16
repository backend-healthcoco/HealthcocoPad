package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.Duration;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by neha on 17/12/15.
 */
@Parcel
public class DrugsListSolrResponse extends SugarRecord {
    @Unique
    private String uniqueId;
    private String description;
    private String hospitalId;
    private String doctorId;
    private String locationId;
    private String drugType;
    private Boolean discarded;
    private Long updatedTime;
    private String drugName;
    private String drugCode;
    private String drugTypeId;
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

    public String getDrugType() {
        return drugType;
    }

    public void setDrugType(String drugType) {
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

    public String getDrugTypeId() {
        return drugTypeId;
    }

    public void setDrugTypeId(String drugTypeId) {
        this.drugTypeId = drugTypeId;
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
