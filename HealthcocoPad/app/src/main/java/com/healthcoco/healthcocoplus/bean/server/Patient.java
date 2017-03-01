package com.healthcoco.healthcocoplus.bean.server;

import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.util.ArrayList;
import java.util.List;

public class Patient extends SugarRecord {
    @Unique
    private String patientId;

    private String bloodGroup;

    private String profession;
    @Ignore
    private List<Relations> relations;

    private String emailAddress;

    private String doctorId;

    private String addressId;

    private String secMobile;

    private String adhaarId;

    private String panCardNumber;

    private String drivingLicenseId;

    private String insuranceId;

    private String insuranceName;
    @Ignore
    private ArrayList<String> notes;
    @Ignore
    protected List<NotesTable> notesTableList;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public List<Relations> getRelations() {
        return relations;
    }

    public void setRelations(List<Relations> relations) {
        this.relations = relations;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getSecMobile() {
        return secMobile;
    }

    public void setSecMobile(String secMobile) {
        this.secMobile = secMobile;
    }

    public String getAdhaarId() {
        return adhaarId;
    }

    public void setAdhaarId(String adhaarId) {
        this.adhaarId = adhaarId;
    }

    public String getPanCardNumber() {
        return panCardNumber;
    }

    public void setPanCardNumber(String panCardNumber) {
        this.panCardNumber = panCardNumber;
    }

    public String getDrivingLicenseId() {
        return drivingLicenseId;
    }

    public void setDrivingLicenseId(String drivingLicenseId) {
        this.drivingLicenseId = drivingLicenseId;
    }

    public String getInsuranceId() {
        return insuranceId;
    }

    public void setInsuranceId(String insuranceId) {
        this.insuranceId = insuranceId;
    }

    public String getInsuranceName() {
        return insuranceName;
    }

    public void setInsuranceName(String insuranceName) {
        this.insuranceName = insuranceName;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    @Override
    public String toString() {
        return "Patient [patientId=" + patientId + ", bloodGroup=" + bloodGroup + ", profession=" + profession
                + ", emailAddress=" + emailAddress + ", doctorId=" + doctorId
                + ", addressId=" + addressId + ", secMobile=" + secMobile + ", adhaarId=" + adhaarId
                + ", panCardNumber=" + panCardNumber + ", drivingLicenseId=" + drivingLicenseId + ", insuranceId="
                + insuranceId + ", insuranceName=" + insuranceName + ", notes=" + notes + "]";
    }

    public List<NotesTable> getNotesTableList() {
        return notesTableList;
    }

    public void setNotesTableList(List<NotesTable> notesTableList) {
        this.notesTableList = notesTableList;
    }
}
