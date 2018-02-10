package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by Prashant on 06/02/2018.
 */
@Parcel
public class PatientDetails extends SugarRecord {

    private Boolean showName;
    private Boolean showPID;
    private Boolean showMobileNumber;
    private Boolean showDOB;
    private Boolean showGender;
    private Boolean showReferedBy;
    private Boolean showDate;
    private Boolean showBloodGroup;
    private Boolean showResourceId;
    private Boolean showHospitalId;
    private Boolean showCity;
    private Style style;

    public Boolean getShowName() {
        return showName;
    }

    public void setShowName(Boolean showName) {
        this.showName = showName;
    }

    public Boolean getShowPID() {
        return showPID;
    }

    public void setShowPID(Boolean showPID) {
        this.showPID = showPID;
    }

    public Boolean getShowMobileNumber() {
        return showMobileNumber;
    }

    public void setShowMobileNumber(Boolean showMobileNumber) {
        this.showMobileNumber = showMobileNumber;
    }

    public Boolean getShowDOB() {
        return showDOB;
    }

    public void setShowDOB(Boolean showDOB) {
        this.showDOB = showDOB;
    }

    public Boolean getShowGender() {
        return showGender;
    }

    public void setShowGender(Boolean showGender) {
        this.showGender = showGender;
    }

    public Boolean getShowReferedBy() {
        return showReferedBy;
    }

    public void setShowReferedBy(Boolean showReferedBy) {
        this.showReferedBy = showReferedBy;
    }

    public Boolean getShowDate() {
        return showDate;
    }

    public void setShowDate(Boolean showDate) {
        this.showDate = showDate;
    }

    public Boolean getShowBloodGroup() {
        return showBloodGroup;
    }

    public void setShowBloodGroup(Boolean showBloodGroup) {
        this.showBloodGroup = showBloodGroup;
    }

    public Boolean getShowResourceId() {
        return showResourceId;
    }

    public void setShowResourceId(Boolean showResourceId) {
        this.showResourceId = showResourceId;
    }

    public Boolean getShowHospitalId() {
        return showHospitalId;
    }

    public void setShowHospitalId(Boolean showHospitalId) {
        this.showHospitalId = showHospitalId;
    }

    public Boolean getShowCity() {
        return showCity;
    }

    public void setShowCity(Boolean showCity) {
        this.showCity = showCity;
    }

    public Style getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = style;
    }
}
