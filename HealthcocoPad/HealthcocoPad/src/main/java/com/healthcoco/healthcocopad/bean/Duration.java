package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.bean.server.DrugDurationUnit;

import org.parceler.Parcel;

@Parcel
public class Duration {
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

    @Override
    public String toString() {
        return "Duration [value=" + value + ", durationUnit=" + durationUnit + "]";
    }
}
