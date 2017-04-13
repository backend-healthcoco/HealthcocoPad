package com.healthcoco.healthcocopad.bean.request;

import java.util.ArrayList;

/**
 * Created by neha on 13/04/17.
 */

public class AssignedUiPermissionsRequest {
    private ArrayList<String> tabPermissions;
    private ArrayList<String> patientVisitPermissions;
    private ArrayList<String> clinicalNotesPermissions;
    private ArrayList<String> prescriptionPermissions;
    private ArrayList<String> profilePermissions;

    public ArrayList<String> getTabPermissions() {
        return tabPermissions;
    }

    public void setTabPermissions(ArrayList<String> tabPermissions) {
        this.tabPermissions = tabPermissions;
    }

    public ArrayList<String> getPatientVisitPermissions() {
        return patientVisitPermissions;
    }

    public void setPatientVisitPermissions(ArrayList<String> patientVisitPermissions) {
        this.patientVisitPermissions = patientVisitPermissions;
    }

    public ArrayList<String> getClinicalNotesPermissions() {
        return clinicalNotesPermissions;
    }

    public void setClinicalNotesPermissions(ArrayList<String> clinicalNotesPermissions) {
        this.clinicalNotesPermissions = clinicalNotesPermissions;
    }

    public ArrayList<String> getPrescriptionPermissions() {
        return prescriptionPermissions;
    }

    public void setPrescriptionPermissions(ArrayList<String> prescriptionPermissions) {
        this.prescriptionPermissions = prescriptionPermissions;
    }

    public ArrayList<String> getProfilePermissions() {
        return profilePermissions;
    }

    public void setProfilePermissions(ArrayList<String> profilePermissions) {
        this.profilePermissions = profilePermissions;
    }
}
