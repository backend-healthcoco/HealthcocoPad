package com.healthcoco.healthcocoplus.bean.server;

import com.healthcoco.healthcocoplus.enums.WeekDayNameType;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;
import com.orm.dsl.Unique;

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

    protected String foreignLocationId;
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
}
