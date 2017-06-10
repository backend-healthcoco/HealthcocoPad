package com.healthcoco.healthcocopad.bean.server;

import android.os.Parcel;
import android.os.Parcelable;

import com.healthcoco.healthcocopad.enums.TimeUnitType;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

@org.parceler.Parcel
public class AppointmentSlot extends SugarRecord {
    @Unique
    protected String foreignUniqueId;
    private Float time;

    private TimeUnitType timeUnit;

    public AppointmentSlot() {
    }

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }

    public Float getTime() {
        return time;
    }

    public void setTime(Float time) {
        this.time = time;
    }

    public TimeUnitType getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnitType timeUnit) {
        this.timeUnit = timeUnit;
    }
}
