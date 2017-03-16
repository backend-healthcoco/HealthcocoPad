package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.server.Address;
import com.healthcoco.healthcocopad.bean.server.DOB;
import com.healthcoco.healthcocopad.bean.server.FileDetails;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.Relations;

import java.util.ArrayList;

/**
 * Created by neha on 16/01/16.
 */
public class RegisterNewPatientRequest {
    private String userId;
    private Address address;
    private String locationId;
    private String hospitalId;
    private String doctorId;
    private String mobileNumber;
    private String emailAddress;
    private String lastName;
    private String middleName;
    private String gender;
    private DOB dob;
    private String profession;
    private Reference referredBy;
    private Long dateOfVisit;
    private String pastHistoryId;
    private String medicalHistoryId;
    private String patientNumber;
    private String bloodGroup;
    private String secMobile;
    private String adhaarId;
    private String panCardNumber;
    private String drivingLicenseId;
    private String insuranceId;
    private String insuranceName;
    private ArrayList<Relations> relations;
    private FileDetails image;
    private ArrayList<String> notes;
    private ArrayList<String> groups;
    private Integer age;
    private String localPatientName;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public DOB getDob() {
        return dob;
    }

    public void setDob(DOB dob) {
        this.dob = dob;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public Reference getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(Reference referredBy) {
        this.referredBy = referredBy;
    }

    public Long getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(Long dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getPastHistoryId() {
        return pastHistoryId;
    }

    public void setPastHistoryId(String pastHistoryId) {
        this.pastHistoryId = pastHistoryId;
    }

    public String getMedicalHistoryId() {
        return medicalHistoryId;
    }

    public void setMedicalHistoryId(String medicalHistoryId) {
        this.medicalHistoryId = medicalHistoryId;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
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

    public ArrayList<Relations> getRelations() {
        return relations;
    }

    public void setRelations(ArrayList<Relations> relations) {
        this.relations = relations;
    }

    public FileDetails getImage() {
        return image;
    }

    public void setImage(FileDetails image) {
        this.image = image;
    }

    public ArrayList<String> getNotes() {
        return notes;
    }

    public void setNotes(ArrayList<String> notes) {
        this.notes = notes;
    }

    public ArrayList<String> getGroups() {
        return groups;
    }

    public void setGroups(ArrayList<String> groups) {
        this.groups = groups;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getLocalPatientName() {
        return localPatientName;
    }

    public void setLocalPatientName(String localPatientName) {
        this.localPatientName = localPatientName;
    }
}
