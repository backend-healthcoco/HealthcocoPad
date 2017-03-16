package com.healthcoco.healthcocopad.bean.server;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

public class DoctorRegistrationDetail extends SugarRecord implements Parcelable{
    private String medicalCouncil;

    private String registrationId;

    private int yearOfPassing;
    protected String foreignUniqueId;

    public DoctorRegistrationDetail() {
    }

    protected DoctorRegistrationDetail(Parcel in) {
        medicalCouncil = in.readString();
        registrationId = in.readString();
        yearOfPassing = in.readInt();
        foreignUniqueId = in.readString();
    }

    public static final Creator<DoctorRegistrationDetail> CREATOR = new Creator<DoctorRegistrationDetail>() {
        @Override
        public DoctorRegistrationDetail createFromParcel(Parcel in) {
            return new DoctorRegistrationDetail(in);
        }

        @Override
        public DoctorRegistrationDetail[] newArray(int size) {
            return new DoctorRegistrationDetail[size];
        }
    };

    public String getMedicalCouncil() {
        return medicalCouncil;
    }

    public void setMedicalCouncil(String medicalCouncil) {
        this.medicalCouncil = medicalCouncil;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public int getYearOfPassing() {
        return yearOfPassing;
    }

    public void setYearOfPassing(int yearOfPassing) {
        this.yearOfPassing = yearOfPassing;
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
        dest.writeString(medicalCouncil);
        dest.writeString(registrationId);
        dest.writeInt(yearOfPassing);
        dest.writeString(foreignUniqueId);
    }
}
