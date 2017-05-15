package com.healthcoco.healthcocopad.bean.server;

import android.os.Parcel;
import android.os.Parcelable;

import com.healthcoco.healthcocopad.enums.TimeUnitType;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

public class AppointmentSlot extends SugarRecord implements Parcelable {
    @Unique
    protected String foreignUniqueId;
    private Float time;

    private TimeUnitType timeUnit;

    public AppointmentSlot() {
    }

    protected AppointmentSlot(Parcel in) {
        foreignUniqueId = in.readString();
        time = in.readFloat();
        timeUnit = TimeUnitType.values()[in.readInt()];
    }

    public static final Creator<AppointmentSlot> CREATOR = new Creator<AppointmentSlot>() {
        @Override
        public AppointmentSlot createFromParcel(Parcel in) {
            return new AppointmentSlot(in);
        }

        @Override
        public AppointmentSlot[] newArray(int size) {
            return new AppointmentSlot[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foreignUniqueId);
        dest.writeFloat(time);
        dest.writeInt(timeUnit.ordinal());
    }
}
