package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.enums.DeviceType;
import com.healthcoco.healthcocopad.enums.UserState;

import org.parceler.Parcel;

import java.util.ArrayList;

/**
 * Created by neha on 17/07/16.
 */
@Parcel
public class DoctorContactUs {
    private String uniqueId;
    private String title;
    private String firstName;
    private String userName;
    private String gender;
    private String emailAddress;
    private String mobileNumber;
    private String city;
    private ArrayList<String> specialities;
    private UserState contactState;
    private Boolean isVerified;
    private Boolean toList;
    private DeviceType deviceType;
    private String mrCode;

    public DoctorContactUs() {
    }

    public DoctorContactUs(String title, String firstName, String gender, String mobileNumber, String emailAddress, ArrayList<String> specialities, String city, DeviceType deviceType) {
        this.title = title;
        this.firstName = firstName;
        this.gender = gender;
        this.emailAddress = emailAddress;
        this.mobileNumber = mobileNumber;
        this.city = city;
        this.specialities = specialities;
        this.deviceType = deviceType;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public ArrayList<String> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(ArrayList<String> specialities) {
        this.specialities = specialities;
    }

    public UserState getContactState() {
        return contactState;
    }

    public void setContactState(UserState contactState) {
        this.contactState = contactState;
    }

    public boolean isVerified() {
        return isVerified;
    }

    public void setIsVerified(boolean isVerified) {
        this.isVerified = isVerified;
    }

    public boolean isToList() {
        return toList;
    }

    public void setToList(boolean toList) {
        this.toList = toList;
    }


    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    public String getMrCode() {
        return mrCode;
    }

    public void setMrCode(String mrCode) {
        this.mrCode = mrCode;
    }
}
