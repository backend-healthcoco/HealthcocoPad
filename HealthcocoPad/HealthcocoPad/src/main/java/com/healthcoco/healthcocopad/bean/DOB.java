package com.healthcoco.healthcocopad.bean;


import com.orm.SugarRecord;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

@Parcel
public class DOB extends SugarRecord {
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
}
