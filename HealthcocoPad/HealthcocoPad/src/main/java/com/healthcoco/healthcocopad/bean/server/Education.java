package com.healthcoco.healthcocopad.bean.server;

import android.os.Parcel;

import com.orm.SugarRecord;

@org.parceler.Parcel
public class Education extends SugarRecord {
    private String qualification;

    private String collegeUniversity;

    private Integer yearOfPassing;
    protected String foreignUniqueId;

    public Education() {
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
}
