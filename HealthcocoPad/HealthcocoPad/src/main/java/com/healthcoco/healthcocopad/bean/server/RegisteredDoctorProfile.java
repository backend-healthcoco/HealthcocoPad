package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.UserState;
import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by neha on 30/08/16.
 */
@Parcel
public class RegisteredDoctorProfile extends SugarRecord {
    @Unique
    private String userId;
    private String title;
    private String firstName;
    private String mobileNumber;
    private String emailAddress;
    private String imageUrl;
    private String thumbnailUrl;
    private String colorCode;
    private String webRole;
    private boolean isActivate;
    private boolean discarded;
    private boolean isSuperAdmin;
    private String registerNumber;
    private Long lastSession;
    private UserState userState;
    private String locationId;


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

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public String getWebRole() {
        return webRole;
    }

    public void setWebRole(String webRole) {
        this.webRole = webRole;
    }

    public boolean isActivate() {
        return isActivate;
    }

    public void setActivate(boolean activate) {
        isActivate = activate;
    }

    public boolean isDiscarded() {
        return discarded;
    }

    public void setDiscarded(boolean discarded) {
        this.discarded = discarded;
    }

    public boolean isSuperAdmin() {
        return isSuperAdmin;
    }

    public void setSuperAdmin(boolean superAdmin) {
        isSuperAdmin = superAdmin;
    }

    public String getRegisterNumber() {
        return registerNumber;
    }

    public void setRegisterNumber(String registerNumber) {
        this.registerNumber = registerNumber;
    }

    public Long getLastSession() {
        return lastSession;
    }

    public void setLastSession(Long lastSession) {
        this.lastSession = lastSession;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getFirstNameWithTitle() {
        if (Util.isNullOrBlank(title))
            title = "Dr.";
        else if (!title.trim().contains("."))
            title = title + ".";
        return title + " " + firstName;
    }
}
