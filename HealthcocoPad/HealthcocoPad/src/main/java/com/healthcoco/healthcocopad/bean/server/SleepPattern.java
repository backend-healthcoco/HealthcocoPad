package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.SleepTimeType;
import com.healthcoco.healthcocopad.enums.WeekDayNameType;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class SleepPattern extends SugarRecord {

    @Unique
    protected String customUniqueId;
    @Ignore
    private WorkingHours hours;

    private SleepTimeType timeType;

    private Integer noOfminute;

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

    public WorkingHours getHours() {
        return hours;
    }

    public void setHours(WorkingHours hours) {
        this.hours = hours;
    }

    public SleepTimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(SleepTimeType timeType) {
        this.timeType = timeType;
    }

    public Integer getNoOfminute() {
        return noOfminute;
    }

    public void setNoOfminute(Integer noOfminute) {
        this.noOfminute = noOfminute;
    }
}
