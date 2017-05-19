package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.WeekDayNameType;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class WorkingSchedule extends SugarRecord {
    private WeekDayNameType workingDay;

    @Ignore
    private List<WorkingHours> workingHours;

    protected String foreignLocationId;
    protected String doctorId;
    @Unique
    protected String customUniqueId;

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
}
