package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import java.util.ArrayList;
import java.util.List;

public class LocationAndAccessControl extends SugarRecord {
    @Unique
    private String uniqueId;

    private String locationName;

    private String country;

    private String state;

    private String city;

    private String locationPhoneNumber;

    private String postalCode;

    private String websiteUrl;
    @Ignore
    private List<ClinicImage> images;

    private String logoUrl;

    private String logoThumbnailUrl;

    private String hospitalId;

    private Double latitude;

    private Double longitude;

    private String tagLine;

    private String landmarkDetails;

    private String locationEmailAddress;
    @Ignore
    private List<String> specialization;

    private String streetAddress;

    private String locality;

    private String mobileNumber;

    private String alternateNumber;
    @Ignore
    private List<DoctorWorkingSchedule> workingSchedules;

    private boolean isTwentyFourSevenOpen;

    private Boolean isLab = false;

    private Boolean isMobileNumberOptional = false;
    @Ignore
    private ArrayList<Role> roles;

    protected String foreignHospitalId;
    protected String doctorId;
//    @Ignore
//    private Location location;
//    protected String locationId;
//
//    @Ignore
//    private AccessControl accessControl;
//    private String foreignAccessControlId;

//    public Location getLocation() {
//        return location;
//    }
//
//    public void setLocation(Location location) {
//        this.location = location;
//    }
//
//    public AccessControl getAccessControl() {
//        return accessControl;
//    }
//
//    public void setAccessControl(AccessControl accessControl) {
//        this.accessControl = accessControl;
//    }


    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocationPhoneNumber() {
        return locationPhoneNumber;
    }

    public void setLocationPhoneNumber(String locationPhoneNumber) {
        this.locationPhoneNumber = locationPhoneNumber;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public void setWebsiteUrl(String websiteUrl) {
        this.websiteUrl = websiteUrl;
    }

    public List<ClinicImage> getImages() {
        return images;
    }

    public void setImages(List<ClinicImage> images) {
        this.images = images;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    public String getLogoThumbnailUrl() {
        return logoThumbnailUrl;
    }

    public void setLogoThumbnailUrl(String logoThumbnailUrl) {
        this.logoThumbnailUrl = logoThumbnailUrl;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getTagLine() {
        return tagLine;
    }

    public void setTagLine(String tagLine) {
        this.tagLine = tagLine;
    }

    public String getLandmarkDetails() {
        return landmarkDetails;
    }

    public void setLandmarkDetails(String landmarkDetails) {
        this.landmarkDetails = landmarkDetails;
    }

    public String getLocationEmailAddress() {
        return locationEmailAddress;
    }

    public void setLocationEmailAddress(String locationEmailAddress) {
        this.locationEmailAddress = locationEmailAddress;
    }

    public List<String> getSpecialization() {
        return specialization;
    }

    public void setSpecialization(List<String> specialization) {
        this.specialization = specialization;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getAlternateNumber() {
        return alternateNumber;
    }

    public void setAlternateNumber(String alternateNumber) {
        this.alternateNumber = alternateNumber;
    }

    public List<DoctorWorkingSchedule> getWorkingSchedules() {
        return workingSchedules;
    }

    public void setWorkingSchedules(List<DoctorWorkingSchedule> workingSchedules) {
        this.workingSchedules = workingSchedules;
    }

    public boolean isTwentyFourSevenOpen() {
        return isTwentyFourSevenOpen;
    }

    public void setIsTwentyFourSevenOpen(boolean isTwentyFourSevenOpen) {
        this.isTwentyFourSevenOpen = isTwentyFourSevenOpen;
    }

    public Boolean getIsLab() {
        return isLab;
    }

    public void setIsLab(Boolean isLab) {
        this.isLab = isLab;
    }

    public Boolean getMobileNumberOptional() {
        return isMobileNumberOptional;
    }

    public void setMobileNumberOptional(Boolean mobileNumberOptional) {
        isMobileNumberOptional = mobileNumberOptional;
    }

    public ArrayList<Role> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<Role> roles) {
        this.roles = roles;
    }

    public String getForeignHospitalId() {
        return foreignHospitalId;
    }

    public void setForeignHospitalId(String foreignHospitalId) {
        this.foreignHospitalId = foreignHospitalId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

//    public String getLocationId() {
//        return locationId;
//    }
//
//    public void setLocationId(String locationId) {
//        this.locationId = locationId;
//    }
//
//    public String getForeignAccessControlId() {
//        return foreignAccessControlId;
//    }
//
//    public void setForeignAccessControlId(String foreignAccessControlId) {
//        this.foreignAccessControlId = foreignAccessControlId;
//    }
}
