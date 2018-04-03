package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.WeekDayNameType;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import java.util.List;

/**
 * Created by neha on 17/02/16.
 */
@org.parceler.Parcel
public class DoctorWorkingSchedule extends SugarRecord {
    protected String workingHoursJson;
    protected String locationId;
    protected String doctorId;
    @Unique
    protected String uniqueId;
    private WeekDayNameType workingDay;
    @Ignore
    private List<WorkingHours> workingHours;

    public DoctorWorkingSchedule() {
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public List<WorkingHours> getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(List<WorkingHours> workingHours) {
        this.workingHours = workingHours;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
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

    public String getWorkingHoursJson() {
        return workingHoursJson;
    }

    public void setWorkingHoursJson(String workingHoursJson) {
        this.workingHoursJson = workingHoursJson;
    }
}
