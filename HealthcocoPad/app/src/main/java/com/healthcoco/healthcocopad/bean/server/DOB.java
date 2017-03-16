package com.healthcoco.healthcocopad.bean.server;

import android.os.Parcel;
import android.os.Parcelable;

import com.orm.SugarRecord;
import com.orm.annotation.Unique;

public class DOB extends SugarRecord implements Parcelable {
    @Unique
    protected String foreignUniqueId;
    private int days;

    private int months;

    private int years;

    public DOB() {
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public int getYears() {
        return years;
    }

    public void setYears(int years) {
        this.years = years;
    }

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(days);
        dest.writeString(foreignUniqueId);
        dest.writeInt(months);
        dest.writeInt(years);
    }

    // Creator
    public static final Creator CREATOR = new Creator
            () {
        public DOB createFromParcel(Parcel in) {
            return new DOB(in);
        }

        public DOB[] newArray(int size) {
            return new DOB[size];
        }
    };

    // "De-parcel object
    private DOB(Parcel in) {
        foreignUniqueId = in.readString();
        days = in.readInt();
        months = in.readInt();
        years = in.readInt();
    }
}
