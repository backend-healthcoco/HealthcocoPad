package com.healthcoco.healthcocopad.bean;

@org.parceler.Parcel
public class WorkingHours {
    private Float fromTime;
    private Float toTime;

    public WorkingHours() {

    }

    public WorkingHours(Float fromTime, Float toTime) {
        this.fromTime = fromTime;
        this.toTime = toTime;
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
}
