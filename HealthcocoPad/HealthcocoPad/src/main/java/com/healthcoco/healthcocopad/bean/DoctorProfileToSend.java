package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.bean.server.FileDetails;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Shreshtha on 04-02-2017.
 */
@Parcel
public class DoctorProfileToSend {
    private String title;

    private String firstName;

    private String emailAddress;

    private String mobileNumber;

    private String gender;

    private Integer experience;

    private List<String> speciality;
    private String doctorId;
    protected String profileImagePath;
    protected String coverImagePath;
    private FileDetails profileImage;
    private FileDetails coverImage;
    private DOB dob;

    private String locationId;
    private String hospitalId;
    private Boolean isActivate;
    private String registerNumber;
    private String roleId;
    private String userId;

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Boolean isActivate() {
        return isActivate;
    }

    public void setIsActivate(Boolean isActivate) {
        this.isActivate = isActivate;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public FileDetails getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(FileDetails profileImage) {
        this.profileImage = profileImage;
    }

    public FileDetails getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(FileDetails coverImage) {
        this.coverImage = coverImage;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<String> getSpeciality() {
        return speciality;
    }

    public void setSpeciality(List<String> speciality) {
        this.speciality = speciality;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public void setCoverImagePath(String coverImagePath) {
        this.coverImagePath = coverImagePath;
    }

    public Integer getExperience() {
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public DOB getDob() {
        return dob;
    }

    public void setDob(DOB dob) {
        this.dob = dob;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
