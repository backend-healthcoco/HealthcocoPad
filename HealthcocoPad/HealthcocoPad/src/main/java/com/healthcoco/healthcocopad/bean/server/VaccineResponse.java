package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.DOB;
import com.healthcoco.healthcocopad.enums.VaccineRoute;
import com.healthcoco.healthcocopad.enums.VaccineStatus;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class VaccineResponse extends SugarRecord {
    @Unique
    private String uniqueId;
    private Long createdTime;
    private Long updatedTime;
    private String name;
    private Long dueDate;
    private VaccineStatus status = VaccineStatus.GIVEN;
    private VaccineRoute route;
    private String bodySite;
    private String vaccineId;
    private Integer dosage;
    private Long givenDate;
    @Ignore
    private VaccineBrand vaccineBrand;
    protected String vaccineBrandJsonString;
    @Ignore
    private DOB age;
    protected String ageJsonString;
    private String patientId;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String note;
    private String duration;
    private Integer periodTime;
    private String createdBy;

    private boolean selected;

    public Integer getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(Integer periodTime) {
        this.periodTime = periodTime;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDueDate() {
        return dueDate;
    }

    public void setDueDate(Long dueDate) {
        this.dueDate = dueDate;
    }

    public VaccineStatus getStatus() {
        return status;
    }

    public void setStatus(VaccineStatus status) {
        this.status = status;
    }

    public VaccineRoute getRoute() {
        return route;
    }

    public void setRoute(VaccineRoute route) {
        this.route = route;
    }

    public String getBodySite() {
        return bodySite;
    }

    public void setBodySite(String bodySite) {
        this.bodySite = bodySite;
    }

    public Integer getDosage() {
        return dosage;
    }

    public void setDosage(Integer dosage) {
        this.dosage = dosage;
    }

    public Long getGivenDate() {
        return givenDate;
    }

    public void setGivenDate(Long givenDate) {
        this.givenDate = givenDate;
    }

    public VaccineBrand getVaccineBrand() {
        return vaccineBrand;
    }

    public void setVaccineBrand(VaccineBrand vaccineBrand) {
        this.vaccineBrand = vaccineBrand;
    }

    public DOB getAge() {
        return age;
    }

    public void setAge(DOB age) {
        this.age = age;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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

    public String getVaccineBrandJsonString() {
        return vaccineBrandJsonString;
    }

    public void setVaccineBrandJsonString(String vaccineBrandJsonString) {
        this.vaccineBrandJsonString = vaccineBrandJsonString;
    }

    public String getVaccineId() {
        return vaccineId;
    }

    public void setVaccineId(String vaccineId) {
        this.vaccineId = vaccineId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAgeJsonString() {
        return ageJsonString;
    }

    public void setAgeJsonString(String ageJsonString) {
        this.ageJsonString = ageJsonString;
    }
}
