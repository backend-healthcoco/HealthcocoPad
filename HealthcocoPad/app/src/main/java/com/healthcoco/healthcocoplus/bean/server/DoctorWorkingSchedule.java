package com.healthcoco.healthcocoplus.bean.server;

import android.os.Parcel;
import android.os.Parcelable;

import com.healthcoco.healthcocoplus.enums.WeekDayNameType;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

import java.util.List;

/**
 * Created by neha on 17/02/16.
 */
public class DoctorWorkingSchedule extends SugarRecord implements Parcelable {
    private WeekDayNameType workingDay;

    @Ignore
    private List<WorkingHours> workingHours;

    protected String foreignLocationId;
    protected String doctorId;
    @Unique
    protected String customUniqueId;

    public DoctorWorkingSchedule() {
    }

    protected DoctorWorkingSchedule(Parcel in) {
        workingHours = in.createTypedArrayList(WorkingHours.CREATOR);
        foreignLocationId = in.readString();
        doctorId = in.readString();
        customUniqueId = in.readString();
        workingDay = WeekDayNameType.values()[in.readInt()];
    }

    public static final Creator<DoctorWorkingSchedule> CREATOR = new Creator<DoctorWorkingSchedule>() {
        @Override
        public DoctorWorkingSchedule createFromParcel(Parcel in) {
            return new DoctorWorkingSchedule(in);
        }

        @Override
        public DoctorWorkingSchedule[] newArray(int size) {
            return new DoctorWorkingSchedule[size];
        }
    };

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

    public List<WorkingHours> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(List<WorkingHours> workingHours) {
        this.workingHours = workingHours;
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

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(workingHours);
        dest.writeString(foreignLocationId);
        dest.writeString(doctorId);
        dest.writeString(customUniqueId);
        dest.writeInt(workingDay.ordinal());
    }
}
