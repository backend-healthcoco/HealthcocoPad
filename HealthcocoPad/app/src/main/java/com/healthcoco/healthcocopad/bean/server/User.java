package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.DOB;
import com.healthcoco.healthcocopad.bean.UIPermissions;
import com.healthcoco.healthcocopad.enums.UserState;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

/**
 * Created by Shreshtha on 23-01-2017.
 */

public class User extends SugarRecord {
    @Unique
    private String uniqueId;

    private String firstName;

    private String lastName;

    private String middleName;

    // private String password;
    private String emailAddress;

    private String mobileNumber;

    private String gender;
    @Ignore
    private DOB dob;
    protected String dobJsonString;

    private String secPhoneNumber;

    private String imageUrl;
    private char[] password;
    private String username;

    protected String foreignHospitalId;

    protected String foreignLocationId;
    private UserState userState;
    private String colorCode;
    private UIPermissions uiPermissions;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    /*
     * public String getPassword() { return password; } public void
     * setPassword(String password) { this.password = password; }
     */
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

    public DOB getDob() {
        return dob;
    }

    public void setDob(DOB dob) {
        this.dob = dob;
    }

    public String getSecPhoneNumber() {
        return secPhoneNumber;
    }

    public void setSecPhoneNumber(String secPhoneNumber) {
        this.secPhoneNumber = secPhoneNumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getForeignHospitalId() {
        return foreignHospitalId;
    }

    public void setForeignHospitalId(String foreignHospitalId) {
        this.foreignHospitalId = foreignHospitalId;
    }

    public String getForeignLocationId() {
        return foreignLocationId;
    }

    public void setForeignLocationId(String foreignLocationId) {
        this.foreignLocationId = foreignLocationId;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public UIPermissions getUiPermissions() {
        return uiPermissions;
    }

    public void setUiPermissions(UIPermissions uiPermissions) {
        this.uiPermissions = uiPermissions;
    }

    public String getDobJsonString() {
        return dobJsonString;
    }

    public void setDobJsonString(String dobJsonString) {
        this.dobJsonString = dobJsonString;
    }
}
