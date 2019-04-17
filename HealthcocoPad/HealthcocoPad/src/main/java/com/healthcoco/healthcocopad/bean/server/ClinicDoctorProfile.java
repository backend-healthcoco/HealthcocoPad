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
public class ClinicDoctorProfile extends SugarRecord {
    @Unique
    private String uniqueId;
    private String title;
    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String emailAddress;
    private String imageUrl;
    private String thumbnailUrl;
    private String colorCode;
    private String coverImageUrl;
    private String coverThumbnailImageUrl;
    private String locationId;
    @Ignore
    private List<String> specialities;
    private String specialitiesJsonString;
    private Long createdTime;
    private Long updatedTime;
    private UserState userState;
    private Boolean isActive;
    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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

    public String getCoverImageUrl() {
        return coverImageUrl;
    }

    public void setCoverImageUrl(String coverImageUrl) {
        this.coverImageUrl = coverImageUrl;
    }

    public String getCoverThumbnailImageUrl() {
        return coverThumbnailImageUrl;
    }

    public void setCoverThumbnailImageUrl(String coverThumbnailImageUrl) {
        this.coverThumbnailImageUrl = coverThumbnailImageUrl;
    }

    public List<String> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<String> specialities) {
        this.specialities = specialities;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public UserState getUserState() {
        return userState;
    }

    public void setUserState(UserState userState) {
        this.userState = userState;
    }

    public String getSpecialitiesJsonString() {
        return specialitiesJsonString;
    }

    public void setSpecialitiesJsonString(String specialitiesJsonString) {
        this.specialitiesJsonString = specialitiesJsonString;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFirstNameWithTitle() {
        if (Util.isNullOrBlank(title))
            title = "Dr.";
        else if (!title.trim().contains("."))
            title = title + ".";
        return title + " " + firstName;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
