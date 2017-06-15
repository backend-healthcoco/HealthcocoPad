package com.healthcoco.healthcocopad.bean.request;


import com.healthcoco.healthcocopad.bean.WorkingHours;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.CreatedByType;

import org.parceler.Parcel;

/**
 * Created by neha on 26/11/16.
 */
@Parcel
public class AppointmentRequestToSend {
    private String locationId;
    private String hospitalId;
    private String doctorId;
    private String patientId;

    private Boolean notifyDoctorByEmail;
    private Boolean notifyDoctorBySms;
    private Boolean notifyPatientByEmail;
    private Boolean notifyPatientBySms;
    private String localPatientName;
    private String mobileNumber;

    private String uniqueId;
    protected boolean isAddedOnSuccess;
    protected Long createdTime;
    protected Long updatedTime;
    private String explanation;
    private CreatedByType createdBy;
    private CreatedByType cancelledBy;
    private Long fromDate;
    private Long toDate;
    private WorkingHours time;
    private String appointmentId;
    private AppointmentStatusType state;

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

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public Boolean getNotifyDoctorByEmail() {
        return notifyDoctorByEmail;
    }

    public void setNotifyDoctorByEmail(Boolean notifyDoctorByEmail) {
        this.notifyDoctorByEmail = notifyDoctorByEmail;
    }

    public Boolean getNotifyDoctorBySms() {
        return notifyDoctorBySms;
    }

    public void setNotifyDoctorBySms(Boolean notifyDoctorBySms) {
        this.notifyDoctorBySms = notifyDoctorBySms;
    }

    public Boolean getNotifyPatientByEmail() {
        return notifyPatientByEmail;
    }

    public void setNotifyPatientByEmail(Boolean notifyPatientByEmail) {
        this.notifyPatientByEmail = notifyPatientByEmail;
    }

    public Boolean getNotifyPatientBySms() {
        return notifyPatientBySms;
    }

    public void setNotifyPatientBySms(Boolean notifyPatientBySms) {
        this.notifyPatientBySms = notifyPatientBySms;
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

    public boolean isAddedOnSuccess() {
        return isAddedOnSuccess;
    }

    public void setIsAddedOnSuccess(boolean isAddedOnSuccess) {
        this.isAddedOnSuccess = isAddedOnSuccess;
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

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public CreatedByType getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedByType createdBy) {
        this.createdBy = createdBy;
    }

    public CreatedByType getCancelledBy() {
        return cancelledBy;
    }

    public void setCancelledBy(CreatedByType cancelledBy) {
        this.cancelledBy = cancelledBy;
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

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public AppointmentStatusType getState() {
        return state;
    }

    public void setState(AppointmentStatusType state) {
        this.state = state;
    }
}
