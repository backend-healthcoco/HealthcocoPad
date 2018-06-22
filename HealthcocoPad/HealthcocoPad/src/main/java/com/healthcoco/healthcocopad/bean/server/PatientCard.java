package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class PatientCard extends SugarRecord {
    protected String notesJsonString;
    @Unique
    private String uniqueId;

    private String userId;

    private String firstName;

    private String localPatientName;

    private String userName;

    private String emailAddress;

    private String imageUrl;

    private String thumbnailUrl;

    private String bloodGroup;

    private String pid;

    private String pnum;

    private String gender;

    private String countryCode;

    private String mobileNumber;

    private String secPhoneNumber;

    private int count;

    private Long dateOfVisit;

    private String doctorId;

    private String locationId;

    private String hospitalId;

    private String doctorSepecificPatientId;

    private String colorCode;

    private Boolean isDataAvailableWithOtherDoctor = false;

    private Boolean isPatientOTPVerified = false;

    private String referredBy;

    @Ignore
    private User user;

    private String patientId;
    private String profession;
    @Ignore
    private List<Relations> relations;
    private String addressId;
    private String secMobile;
    private String adhaarId;
    private String panCardNumber;
    private String drivingLicenseId;
    private String insuranceId;
    private String insuranceName;
    private List<String> notes;

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
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

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public String getLocalPatientName() {
        return localPatientName;
    }

    public void setLocalPatientName(String localPatientName) {
        this.localPatientName = localPatientName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getSecPhoneNumber() {
        return secPhoneNumber;
    }

    public void setSecPhoneNumber(String secPhoneNumber) {
        this.secPhoneNumber = secPhoneNumber;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Long getDateOfVisit() {
        return dateOfVisit;
    }

    public void setDateOfVisit(Long dateOfVisit) {
        this.dateOfVisit = dateOfVisit;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
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

    public String getDoctorSepecificPatientId() {
        return doctorSepecificPatientId;
    }

    public void setDoctorSepecificPatientId(String doctorSepecificPatientId) {
        this.doctorSepecificPatientId = doctorSepecificPatientId;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public Boolean getIsDataAvailableWithOtherDoctor() {
        return isDataAvailableWithOtherDoctor;
    }

    public void setIsDataAvailableWithOtherDoctor(Boolean isDataAvailableWithOtherDoctor) {
        this.isDataAvailableWithOtherDoctor = isDataAvailableWithOtherDoctor;
    }

    public Boolean getIsPatientOTPVerified() {
        return isPatientOTPVerified;
    }

    public void setIsPatientOTPVerified(Boolean isPatientOTPVerified) {
        this.isPatientOTPVerified = isPatientOTPVerified;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getReferredBy() {
        return referredBy;
    }

    public void setReferredBy(String referredBy) {
        this.referredBy = referredBy;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getNotesJsonString() {
        return notesJsonString;
    }

    public void setNotesJsonString(String notesJsonString) {
        this.notesJsonString = notesJsonString;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }
}
