package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by Prashant on 21/02/2018.
 */
@Parcel
public class DischargeSummaryDynamicFields extends SugarRecord {

    private String operationNotes;
    private String labourNotes;
    private String babyNotes;
    private String cement;
    private String implant;
    private String operationName;

    public String getOperationNotes() {
        return operationNotes;
    }

    public void setOperationNotes(String operationNotes) {
        this.operationNotes = operationNotes;
    }

    public String getLabourNotes() {
        return labourNotes;
    }

    public void setLabourNotes(String labourNotes) {
        this.labourNotes = labourNotes;
    }

    public String getBabyNotes() {
        return babyNotes;
    }

    public void setBabyNotes(String babyNotes) {
        this.babyNotes = babyNotes;
    }

    public String getCement() {
        return cement;
    }

    public void setCement(String cement) {
        this.cement = cement;
    }

    public String getImplant() {
        return implant;
    }

    public void setImplant(String implant) {
        this.implant = implant;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
