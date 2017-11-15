package com.healthcoco.healthcocopad.bean.server;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

public class DoctorExperienceDetail extends SugarRecord implements Parcelable {
    public static final Creator<DoctorExperienceDetail> CREATOR = new Creator<DoctorExperienceDetail>() {
        @Override
        public DoctorExperienceDetail createFromParcel(Parcel in) {
            return new DoctorExperienceDetail(in);
        }

        @Override
        public DoctorExperienceDetail[] newArray(int size) {
            return new DoctorExperienceDetail[size];
        }
    };
    protected String foreignUniqueId;
    private String organization;
    private String city;
    private int fromValue;
    private int toValue;

    public DoctorExperienceDetail() {
    }

    protected DoctorExperienceDetail(Parcel in) {
        foreignUniqueId = in.readString();
        organization = in.readString();
        city = in.readString();
        fromValue = in.readInt();
        toValue = in.readInt();
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getFromValue() {
        return fromValue;
    }

    public void setFromValue(int fromValue) {
        this.fromValue = fromValue;
    }

    public int getToValue() {
        return toValue;
    }

    public void setToValue(int toValue) {
        this.toValue = toValue;
    }

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foreignUniqueId);
        dest.writeString(organization);
        dest.writeString(city);
        dest.writeInt(fromValue);
        dest.writeInt(toValue);
    }
}
