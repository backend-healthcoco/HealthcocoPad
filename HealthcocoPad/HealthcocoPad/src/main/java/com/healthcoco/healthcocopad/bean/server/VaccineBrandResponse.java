package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class VaccineBrandResponse extends SugarRecord {

    @Unique
    private String uniqueId;
    private Long createdTime;
    private Long updatedTime;
    private String vaccineId;
    private String vaccineBrandId;
    private boolean isActive;
    @Ignore
    private VaccineBrand vaccineBrand;
    protected String vaccineBrandJsonString;
    @Ignore
    private VaccineResponse vaccine;

    protected boolean isSelected;

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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

    public String getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(String vaccineId) {
        this.vaccineId = vaccineId;
    }

    public String getVaccineBrandId() {
        return vaccineBrandId;
    }

    public void setVaccineBrandId(String vaccineBrandId) {
        this.vaccineBrandId = vaccineBrandId;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public VaccineBrand getVaccineBrand() {
        return vaccineBrand;
    }

    public void setVaccineBrand(VaccineBrand vaccineBrand) {
        this.vaccineBrand = vaccineBrand;
    }

    public String getVaccineBrandJsonString() {
        return vaccineBrandJsonString;
    }

    public void setVaccineBrandJsonString(String vaccineBrandJsonString) {
        this.vaccineBrandJsonString = vaccineBrandJsonString;
    }

    public VaccineResponse getVaccine() {
        return vaccine;
    }

    public void setVaccine(VaccineResponse vaccine) {
        this.vaccine = vaccine;
    }
}
