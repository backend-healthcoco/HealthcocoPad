package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.DOB;
import com.healthcoco.healthcocopad.enums.VaccineRoute;
import com.healthcoco.healthcocopad.enums.VaccineStatus;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class VaccineRequest {
    @Unique
    private String uniqueId;
    private String name;
    private Long dueDate;
    private VaccineStatus status;
    private VaccineRoute route;
    private String bodySite;
    private Integer dosage;
    private Long givenDate;

    @Ignore
    private DOB age;
    private String patientId;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String note;
    private String vaccineId;
    private String vaccineBrandId;
    private String duration;
    private Integer periodTime;

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

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(int periodTime) {
        this.periodTime = periodTime;
    }
}
