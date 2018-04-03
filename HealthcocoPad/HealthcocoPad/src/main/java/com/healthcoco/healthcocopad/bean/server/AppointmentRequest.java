package com.healthcoco.healthcocopad.bean.server;


import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.CreatedByType;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

/**
 * Created by neha on 26/11/16.
 */
@Parcel
public class AppointmentRequest extends SugarRecord {
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
    @Ignore
    private WorkingHours time;
    protected String workingHoursJson;
    private String appointmentId;
    private AppointmentStatusType state;
    //    private boolean isRescheduled;
//    private boolean isCalenderBlocked;
//    private boolean isFeedbackAvailable;
//    private boolean isAllDayEvent;
//    private String doctorName;
//    private String locationName;
//    private String clinicAddress;
//    private String clinicNumber;
    private String visitId;

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

    public void setAddedOnSuccess(boolean addedOnSuccess) {
        isAddedOnSuccess = addedOnSuccess;
    }

//    public boolean isRescheduled() {
//        return isRescheduled;
//    }
//
//    public void setRescheduled(boolean rescheduled) {
//        isRescheduled = rescheduled;
//    }
//
//    public boolean isCalenderBlocked() {
//        return isCalenderBlocked;
//    }
//
//    public void setCalenderBlocked(boolean calenderBlocked) {
//        isCalenderBlocked = calenderBlocked;
//    }
//
//    public boolean isFeedbackAvailable() {
//        return isFeedbackAvailable;
//    }
//
//    public void setFeedbackAvailable(boolean feedbackAvailable) {
//        isFeedbackAvailable = feedbackAvailable;
//    }
//
//    public boolean isAllDayEvent() {
//        return isAllDayEvent;
//    }
//
//    public void setAllDayEvent(boolean allDayEvent) {
//        isAllDayEvent = allDayEvent;
//    }
//
//    public String getDoctorName() {
//        return doctorName;
//    }
//
//    public void setDoctorName(String doctorName) {
//        this.doctorName = doctorName;
//    }
//
//    public String getLocationName() {
//        return locationName;
//    }
//
//    public void setLocationName(String locationName) {
//        this.locationName = locationName;
//    }
//
//    public String getClinicAddress() {
//        return clinicAddress;
//    }
//
//    public void setClinicAddress(String clinicAddress) {
//        this.clinicAddress = clinicAddress;
//    }
//
//    public String getClinicNumber() {
//        return clinicNumber;
//    }
//
//    public void setClinicNumber(String clinicNumber) {
//        this.clinicNumber = clinicNumber;
//    }

    public String getVisitId() {
        return visitId;
    }

    public void setVisitId(String visitId) {
        this.visitId = visitId;
    }

    public String getWorkingHoursJson() {
        return workingHoursJson;
    }

    public void setWorkingHoursJson(String workingHoursJson) {
        this.workingHoursJson = workingHoursJson;
    }
}
