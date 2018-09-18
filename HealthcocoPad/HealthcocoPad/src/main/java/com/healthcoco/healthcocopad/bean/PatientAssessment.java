package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcel;

/**
 * Created by Prashant on 18-09-2018.
 */
@Parcel
public class PatientAssessment {

    private String title;
    private int totalField;
    private int filledField;
    private String patientId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getTotalField() {
        return totalField;
    }

    public void setTotalField(int totalField) {
        this.totalField = totalField;
    }

    public int getFilledField() {
        return filledField;
    }

    public void setFilledField(int filledField) {
        this.filledField = filledField;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }
}
