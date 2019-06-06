package com.healthcoco.healthcocopad.bean.server;

import android.graphics.Bitmap;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class RegisteredPatientDetailsNew extends SugarRecord {
    @Unique
    private String backendPatientId;
    private String uniqueId;
    private String localPatientName;
    private String firstName;
    private String imageUrl;
    @Ignore
    private Bitmap profileImageBitmap;
    private String userId;
    private String mobileNumber;
    @Ignore
    private List<String> groupIds;
    protected String groupIdsJsonString;
    private String doctorId;
    private String locationId;
    private String hospitalId;
    private String pid;
    private String colorCode;
    private Long updatedTime;
    private Long createdTime;
    private String thumbnailUrl;
    private boolean isPatientDiscarded;
    private String pnum;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public List<String> getGroupIds() {
        return groupIds;
    }

    public void setGroupIds(List<String> groupIds) {
        this.groupIds = groupIds;
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

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getColorCode() {
        if (colorCode == null)
            return "#0077b5";
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    public Long getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Long updatedTime) {
        this.updatedTime = updatedTime;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public boolean isPatientDiscarded() {
        return isPatientDiscarded;
    }

    public void setPatientDiscarded(boolean patientDiscarded) {
        isPatientDiscarded = patientDiscarded;
    }

    public String getPnum() {
        return pnum;
    }

    public void setPnum(String pnum) {
        this.pnum = pnum;
    }

    public String getBackendPatientId() {
        return backendPatientId;
    }

    public void setBackendPatientId(String backendPatientId) {
        this.backendPatientId = backendPatientId;
    }

    public String getGroupIdsJsonString() {
        return groupIdsJsonString;
    }

    public void setGroupIdsJsonString(String groupIdsJsonString) {
        this.groupIdsJsonString = groupIdsJsonString;
    }

    public Bitmap getProfileImageBitmap() {
        return profileImageBitmap;
    }

    public void setProfileImageBitmap(Bitmap profileImageBitmap) {
        this.profileImageBitmap = profileImageBitmap;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }
}
