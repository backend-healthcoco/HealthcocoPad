package com.healthcoco.healthcocopad.bean;


import com.healthcoco.healthcocopad.enums.FeedbackType;

import java.util.ArrayList;

/**
 * Created by neha on 15/01/18.
 */

public class AppointmentFeedback {
    private String uniqueId;
    private String locationId;
    private String doctorId;
    private String patientId;
    private String hospitalId;
    private String localeId;
    private String appointmentId;
    private String prescriptionId;
    private Boolean isRecommended;
    private Boolean isAppointmentStartedOnTime;
    private String howLateWasAppointmentInMinutes;
    private Integer overallExperience;
    private String reasonOfVisit;
    private String experience;
    private String reply;
    private Boolean isAnonymous;
    private String adminUpdatedExperience;
    private Boolean isMedicationOnTime;
    private FeedbackType feedbackType;
    private ArrayList<QuestionAnswers> questionAnswers;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
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

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getLocaleId() {
        return localeId;
    }

    public void setLocaleId(String localeId) {
        this.localeId = localeId;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public boolean isRecommended() {
        return isRecommended;
    }

    public void setRecommended(boolean recommended) {
        isRecommended = recommended;
    }

    public boolean isAppointmentStartedOnTime() {
        return isAppointmentStartedOnTime;
    }

    public void setAppointmentStartedOnTime(boolean appointmentStartedOnTime) {
        isAppointmentStartedOnTime = appointmentStartedOnTime;
    }

    public int getOverallExperience() {
        return overallExperience;
    }

    public void setOverallExperience(int overallExperience) {
        this.overallExperience = overallExperience;
    }

    public String getReasonOfVisit() {
        return reasonOfVisit;
    }

    public void setReasonOfVisit(String reasonOfVisit) {
        this.reasonOfVisit = reasonOfVisit;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public boolean isAnonymous() {
        return isAnonymous;
    }

    public void setAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public String getAdminUpdatedExperience() {
        return adminUpdatedExperience;
    }

    public void setAdminUpdatedExperience(String adminUpdatedExperience) {
        this.adminUpdatedExperience = adminUpdatedExperience;
    }

    public boolean isMedicationOnTime() {
        return isMedicationOnTime;
    }

    public void setMedicationOnTime(boolean medicationOnTime) {
        isMedicationOnTime = medicationOnTime;
    }

    public FeedbackType getFeedbackType() {
        return feedbackType;
    }

    public void setFeedbackType(FeedbackType feedbackType) {
        this.feedbackType = feedbackType;
    }

    public ArrayList<QuestionAnswers> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(ArrayList<QuestionAnswers> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public String getHowLateWasAppointmentInMinutes() {
        return howLateWasAppointmentInMinutes;
    }

    public void setHowLateWasAppointmentInMinutes(String howLateWasAppointmentInMinutes) {
        this.howLateWasAppointmentInMinutes = howLateWasAppointmentInMinutes;
    }
}
