package com.healthcoco.healthcocopad.bean.request;


import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.CreatedByType;

import org.parceler.Parcel;

/**
 * Created by Prashant on 29/05/18.
 */
@Parcel
public class EventRequest {

    protected boolean isAddedOnSuccess;
    protected Long createdTime;
    protected Long updatedTime;
    private String locationId;
    private String hospitalId;
    private String doctorId;
    private String patientId;
    private String localPatientName;
    private String mobileNumber;
    private String uniqueId;
    private String subject;
    private String explanation;
    private Long fromDate;
    private Long toDate;
    private WorkingHours time;
    private AppointmentStatusType state;
    private boolean isCalenderBlocked;
    private boolean isPatientRequired;
    private boolean isAllDayEvent;


    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public boolean isAddedOnSuccess() {
        return isAddedOnSuccess;
    }

    public void setAddedOnSuccess(boolean addedOnSuccess) {
        isAddedOnSuccess = addedOnSuccess;
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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getLocalPatientName() {
        return localPatientName;
    }

    public void setLocalPatientName(String localPatientName) {
        this.localPatientName = localPatientName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Long getFromDate() {
        return fromDate;
    }

    public void setFromDate(Long fromDate) {
        this.fromDate = fromDate;
    }

    public Long getToDate() {
        return toDate;
    }

    public void setToDate(Long toDate) {
        this.toDate = toDate;
    }

    public WorkingHours getTime() {
        return time;
    }

    public void setTime(WorkingHours time) {
        this.time = time;
    }

    public AppointmentStatusType getState() {
        return state;
    }

    public void setState(AppointmentStatusType state) {
        this.state = state;
    }

    public boolean isCalenderBlocked() {
        return isCalenderBlocked;
    }

    public void setCalenderBlocked(boolean calenderBlocked) {
        isCalenderBlocked = calenderBlocked;
    }

    public boolean isPatientRequired() {
        return isPatientRequired;
    }

    public void setPatientRequired(boolean patientRequired) {
        isPatientRequired = patientRequired;
    }

    public boolean isAllDayEvent() {
        return isAllDayEvent;
    }

    public void setAllDayEvent(boolean allDayEvent) {
        isAllDayEvent = allDayEvent;
    }
}
