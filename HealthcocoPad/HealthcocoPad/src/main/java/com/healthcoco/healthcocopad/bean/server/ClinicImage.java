package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.FileDetails;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;
@org.parceler.Parcel

public class ClinicImage extends SugarRecord {
    private String imageUrl;

    private String thumbnailUrl;
    private String logoURL;

    private String logoThumbnailURL;

    private int counter;

    protected String foreignLocationId;
    @Unique
    protected String customUniqueId;
    protected FileDetails fileDetails;

    public ClinicImage() {
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getCounter() {
        return counter;
    }

    public void setCounter(int counter) {
        this.counter = counter;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getForeignLocationId() {
        return foreignLocationId;
    }

    public void setForeignLocationId(String foreignLocationId) {
        this.foreignLocationId = foreignLocationId;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

    public FileDetails getFileDetails() {
        return fileDetails;
    }

    public void setFileDetails(FileDetails fileDetails) {
        this.fileDetails = fileDetails;
    }

    public String getLogoThumbnailURL() {
        return logoThumbnailURL;
    }

    public void setLogoThumbnailURL(String logoThumbnailURL) {
        this.logoThumbnailURL = logoThumbnailURL;
    }

    public String getLogoURL() {
        return logoURL;
    }

    public void setLogoURL(String logoURL) {
        this.logoURL = logoURL;
    }
}
