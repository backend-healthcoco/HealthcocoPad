package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.DOB;
import com.healthcoco.healthcocopad.enums.UserState;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

/**
 * Created by neha on 14/01/16.
 */
@Parcel
public class AlreadyRegisteredPatientsResponse extends SugarRecord {
    @Unique
    private String userId;
    private String userName;
    private String imageUrl;
    private String thumbnailUrl;
    private String mobileNumber;
    private String emailAddress;
    @Ignore
    private DOB dob;
    private String colorCode;
    private String firstName;
    private String middleName;
    private String lastName;
    private String title;
    private String gender;
    private UserState userState;
    private Boolean isPartOfClinic;
    private String secPhoneNumber;
    protected String imageFilePath;
    private String localPatientName;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
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

    public DOB getDob() {
        return dob;
    }

    public void setDob(DOB dob) {
        this.dob = dob;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getFirstName() {
        if (Util.isNullOrBlank(firstName))
            return localPatientName;
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public Boolean getIsPartOfClinic() {
        return isPartOfClinic;
    }

    public void setIsPartOfClinic(Boolean isPartOfClinic) {
        this.isPartOfClinic = isPartOfClinic;
    }

    public String getSecPhoneNumber() {
        return secPhoneNumber;
    }

    public void setSecPhoneNumber(String secPhoneNumber) {
        this.secPhoneNumber = secPhoneNumber;
    }

    public String getImageFilePath() {
        return imageFilePath;
    }

    public void setImageFilePath(String imageFilePath) {
        this.imageFilePath = imageFilePath;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLocalPatientName() {
        return localPatientName;
    }

    public void setLocalPatientName(String localPatientName) {
        this.localPatientName = localPatientName;
    }
}
