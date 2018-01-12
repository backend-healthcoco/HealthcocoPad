package com.healthcoco.healthcocopad.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.healthcoco.healthcocopad.enums.WeekDayNameType;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

@org.parceler.Parcel
public class WorkingHours extends SugarRecord {

    protected WeekDayNameType workingDay;
    protected String foreignLocationId;
    protected String doctorId;
    @Unique
    protected String customUniqueId;
    private Float fromTime;
    private Float toTime;
    private String foreignTableId;

    public WorkingHours() {

    }

    public WorkingHours(Float fromTime, Float toTime) {
        this.fromTime = fromTime;
        this.toTime = toTime;
    }

    protected WorkingHours(Parcel in) {
//        foreignTableId = in.readString();
        customUniqueId = in.readString();
        fromTime = in.readFloat();
        toTime = in.readFloat();
        foreignLocationId = in.readString();
        doctorId = in.readString();
        workingDay = WeekDayNameType.values()[in.readInt()];
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getForeignLocationId() {
        return foreignLocationId;
    }

    public void setForeignLocationId(String foreignLocationId) {
        this.foreignLocationId = foreignLocationId;
    }

    public WeekDayNameType getWorkingDay() {
        return workingDay;
    }

    public void setWorkingDay(WeekDayNameType workingDay) {
        this.workingDay = workingDay;
    }

    public Float getFromTime() {
        return fromTime;
    }

    public void setFromTime(Float fromTime) {
        this.fromTime = fromTime;
    }

    public Float getToTime() {
        return toTime;
    }

    public void setToTime(Float toTime) {
        this.toTime = toTime;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }


    public String getForeignTableId() {
        return foreignTableId;
    }

    public void setForeignTableId(String foreignTableId) {
        this.foreignTableId = foreignTableId;
    }

}
