package com.healthcoco.healthcocopad.bean.server;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

public class ClinicImage extends SugarRecord implements Parcelable {
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

    protected ClinicImage(Parcel in) {
        imageUrl = in.readString();
        thumbnailUrl = in.readString();
        logoURL = in.readString();
        logoThumbnailURL = in.readString();
        counter = in.readInt();
        foreignLocationId = in.readString();
        customUniqueId = in.readString();
    }

    public static final Creator<ClinicImage> CREATOR = new Creator<ClinicImage>() {
        @Override
        public ClinicImage createFromParcel(Parcel in) {
            return new ClinicImage(in);
        }

        @Override
        public ClinicImage[] newArray(int size) {
            return new ClinicImage[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imageUrl);
        dest.writeString(thumbnailUrl);
        dest.writeString(logoURL);
        dest.writeString(logoThumbnailURL);
        dest.writeInt(counter);
        dest.writeString(foreignLocationId);
        dest.writeString(customUniqueId);
    }
}
