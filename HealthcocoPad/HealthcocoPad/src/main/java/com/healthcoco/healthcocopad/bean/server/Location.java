package com.healthcoco.healthcocopad.bean.server;

import android.content.Context;

import com.healthcoco.healthcocopad.utilities.Util;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author veeraj
 */
@Parcel
public class Location extends SugarRecord {
    @Unique
    private String uniqueId;

    private String clinicAddress;

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

    private List<String> specialization;

    private String streetAddress;

    private String locality;

    private String clinicNumber;
    @Ignore
    private ArrayList<String> alternateClinicNumbers;
    @Ignore
    private List<ClinicWorkingSchedule> clinicWorkingSchedules;

    private Boolean isLab;

    private Boolean isOnlineReportsAvailable;

    private Boolean isNABLAccredited;

    private Boolean isHomeServiceAvailable;
    private Boolean twentyFourSevenOpen;

    public Boolean getIsLab() {
        return isLab;
    }

    public void setIsLab(Boolean isLab) {
        this.isLab = isLab;
    }

    public Boolean getIsOnlineReportsAvailable() {
        return isOnlineReportsAvailable;
    }

    public void setIsOnlineReportsAvailable(Boolean isOnlineReportsAvailable) {
        this.isOnlineReportsAvailable = isOnlineReportsAvailable;
    }

    public Boolean getIsNABLAccredited() {
        return isNABLAccredited;
    }

    public void setIsNABLAccredited(Boolean isNABLAccredited) {
        this.isNABLAccredited = isNABLAccredited;
    }

    public Boolean getIsHomeServiceAvailable() {
        return isHomeServiceAvailable;
    }

    public void setIsHomeServiceAvailable(Boolean isHomeServiceAvailable) {
        this.isHomeServiceAvailable = isHomeServiceAvailable;
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

    public String getClinicNumber() {
        return clinicNumber;
    }

    public void setClinicNumber(String clinicNumber) {
        this.clinicNumber = clinicNumber;
    }

    public List<ClinicWorkingSchedule> getClinicWorkingSchedules() {
        return clinicWorkingSchedules;
    }

    public void setClinicWorkingSchedules(List<ClinicWorkingSchedule> clinicWorkingSchedules) {
        this.clinicWorkingSchedules = clinicWorkingSchedules;
    }


    public String getLogoThumbnailUrl() {
        return logoThumbnailUrl;
    }

    public void setLogoThumbnailUrl(String logoThumbnailUrl) {
        this.logoThumbnailUrl = logoThumbnailUrl;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public ArrayList<String> getAlternateClinicNumbers() {
        return alternateClinicNumbers;
    }

    public void setAlternateClinicNumbers(ArrayList<String> alternateClinicNumbers) {
        this.alternateClinicNumbers = alternateClinicNumbers;
    }

    public Boolean getTwentyFourSevenOpen() {
        return twentyFourSevenOpen;
    }

    public void setTwentyFourSevenOpen(Boolean twentyFourSevenOpen) {
        this.twentyFourSevenOpen = twentyFourSevenOpen;
    }

    public String getFormattedClinicAddress(Context context) {
        String address = "";
        if (!Util.isNullOrBlank(getClinicAddress()))
            address = getClinicAddress();

        if (Util.isNullOrBlank(address))
            return "";
        return address;
    }

    public String getClinicAddress() {
        return clinicAddress;
    }

    public void setClinicAddress(String clinicAddress) {
        this.clinicAddress = clinicAddress;
    }
}
