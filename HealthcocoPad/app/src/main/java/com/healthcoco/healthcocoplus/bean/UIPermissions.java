package com.healthcoco.healthcocoplus.bean;

import com.healthcoco.healthcocoplus.enums.ClinicalNotesPermissionType;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

import java.util.ArrayList;

/**
 * Created by neha on 01/02/17.
 */
public class UIPermissions extends SugarRecord {
    private String doctorId;
    @Ignore
    private ArrayList<String> tabPermissions;
    private String tabPermissionsString;
    @Ignore
    private ArrayList<String> patientVisitPermissions;
    private String patientVisitPermissionsString;

    @Ignore
    private ArrayList<ClinicalNotesPermissionType> clinicalNotesPermissions;
    private String clinicalNotesPermissionsString;
    @Ignore
    private ArrayList<String> prescriptionPermissions;
    private String prescriptionPermissionsString;

    @Ignore
    private ArrayList<String> profilePermissions;
    private String profilePermissionsString;

    public String getTabPermissionsString() {
        return tabPermissionsString;
    }

    public void setTabPermissionsString(String tabPermissionsString) {
        this.tabPermissionsString = tabPermissionsString;
    }

    public String getPatientVisitPermissionsString() {
        return patientVisitPermissionsString;
    }

    public void setPatientVisitPermissionsString(String patientVisitPermissionsString) {
        this.patientVisitPermissionsString = patientVisitPermissionsString;
    }

    public String getClinicalNotesPermissionsString() {
        return clinicalNotesPermissionsString;
    }

    public void setClinicalNotesPermissionsString(String clinicalNotesPermissionsString) {
        this.clinicalNotesPermissionsString = clinicalNotesPermissionsString;
    }

    public String getPrescriptionPermissionsString() {
        return prescriptionPermissionsString;
    }

    public void setPrescriptionPermissionsString(String prescriptionPermissionsString) {
        this.prescriptionPermissionsString = prescriptionPermissionsString;
    }

    public String getProfilePermissionsString() {
        return profilePermissionsString;
    }

    public void setProfilePermissionsString(String profilePermissionsString) {
        this.profilePermissionsString = profilePermissionsString;
    }

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

    public ArrayList<ClinicalNotesPermissionType> getClinicalNotesPermissions() {
        return clinicalNotesPermissions;
    }

    public void setClinicalNotesPermissions(ArrayList<ClinicalNotesPermissionType> clinicalNotesPermissions) {
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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }
}
