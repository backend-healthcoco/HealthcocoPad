package com.healthcoco.healthcocoplus.bean.server;

import android.os.Parcel;
import android.os.Parcelable;

import com.healthcoco.healthcocoplus.enums.WeekDayNameType;
import com.orm.SugarRecord;
import com.orm.dsl.Unique;

public class WorkingHours extends SugarRecord implements Parcelable {
    private Float fromTime;
    private Float toTime;
    protected WeekDayNameType workingDay;
    protected String foreignLocationId;
    protected String doctorId;
    @Unique
    protected String customUniqueId;
    private String foreignTableId;

    public WorkingHours() {

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

    public static final Creator<WorkingHours> CREATOR = new Creator<WorkingHours>() {
        @Override
        public WorkingHours createFromParcel(Parcel in) {
            return new WorkingHours(in);
        }

        @Override
        public WorkingHours[] newArray(int size) {
            return new WorkingHours[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(foreignTableId);
        dest.writeString(foreignLocationId);
        dest.writeString(doctorId);
        if (workingDay != null)
            dest.writeInt(workingDay.ordinal());
        dest.writeString(customUniqueId);
        dest.writeFloat(fromTime);
        dest.writeFloat(toTime);
    }

    public String getForeignTableId() {
        return foreignTableId;
    }

    public void setForeignTableId(String foreignTableId) {
        this.foreignTableId = foreignTableId;
    }
}
