package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.WorkingHours;
import com.healthcoco.healthcocopad.enums.WeekDayNameType;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by neha on 17/02/16.
 */
@Parcel
public class ClinicWorkingSchedule extends SugarRecord {
    private WeekDayNameType workingDay;

    @Ignore
    private List<WorkingHours> workingHours;
    protected String workingHoursJson;
    protected String locationId;
    @Unique
    protected String uniqueId;

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

    public String getWorkingHoursJson() {
        return workingHoursJson;
    }

    public void setWorkingHoursJson(String workingHoursJson) {
        this.workingHoursJson = workingHoursJson;
    }
}
