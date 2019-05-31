package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Patient extends SugarRecord {
    protected String notesJsonString;
    @Unique
    private String backendPatientId;
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
    private boolean isPatientDiscarded;
    private String insuranceName;
    @Ignore
    private ArrayList<String> notes;

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

    public String getNotesJsonString() {
        return notesJsonString;
    }

    public void setNotesJsonString(String notesJsonString) {
        this.notesJsonString = notesJsonString;
    }

    public boolean isPatientDiscarded() {
        return isPatientDiscarded;
    }

    public void setPatientDiscarded(boolean patientDiscarded) {
        isPatientDiscarded = patientDiscarded;
    }

    public String getBackendPatientId() {
        return backendPatientId;
    }

    public void setBackendPatientId(String backendPatientId) {
        this.backendPatientId = backendPatientId;
    }
}
