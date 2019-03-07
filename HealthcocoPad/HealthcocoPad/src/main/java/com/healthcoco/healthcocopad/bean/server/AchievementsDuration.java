package com.healthcoco.healthcocopad.bean.server;

import com.orm.SugarRecord;

import org.parceler.Parcel;

@Parcel
public class AchievementsDuration extends SugarRecord {
    private String value;
    private DrugDurationUnit durationUnit;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DrugDurationUnit getDurationUnit() {
        return durationUnit;
    }

    public void setDurationUnit(DrugDurationUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    public String getFormattedDurationWithUnit() {
        if (durationUnit != null)
            return value + " " + durationUnit.getUnit();
        return "";
    }
}
