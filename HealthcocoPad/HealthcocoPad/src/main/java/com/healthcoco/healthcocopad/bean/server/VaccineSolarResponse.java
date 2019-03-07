package com.healthcoco.healthcocopad.bean.server;

import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class VaccineSolarResponse {
    @Unique
    private String uniqueId;
    private String name;
    private String longName;
    private String duration;
    private Integer periodTime;

    public Integer getPeriodTime() {
        return periodTime;
    }

    public void setPeriodTime(Integer periodTime) {
        this.periodTime = periodTime;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
