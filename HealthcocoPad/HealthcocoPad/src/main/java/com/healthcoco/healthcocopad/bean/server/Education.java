package com.healthcoco.healthcocopad.bean.server;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;

public class Education extends SugarRecord implements Parcelable {
    public static final Creator<Education> CREATOR = new Creator<Education>() {
        @Override
        public Education createFromParcel(Parcel in) {
            return new Education(in);
        }

        @Override
        public Education[] newArray(int size) {
            return new Education[size];
        }
    };
    protected String foreignUniqueId;
    private String qualification;
    private String collegeUniversity;
    private Integer yearOfPassing;

    public Education() {
    }

    protected Education(Parcel in) {
        qualification = in.readString();
        collegeUniversity = in.readString();
        foreignUniqueId = in.readString();
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getCollegeUniversity() {
        return collegeUniversity;
    }

    public void setCollegeUniversity(String collegeUniversity) {
        this.collegeUniversity = collegeUniversity;
    }

    public Integer getYearOfPassing() {
        return yearOfPassing;
    }

    public void setYearOfPassing(Integer yearOfPassing) {
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
        dest.writeString(qualification);
        dest.writeString(collegeUniversity);
        dest.writeString(foreignUniqueId);
    }
}
