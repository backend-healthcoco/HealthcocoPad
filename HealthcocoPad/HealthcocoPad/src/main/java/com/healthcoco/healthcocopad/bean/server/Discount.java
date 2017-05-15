package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.UnitType;
import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by Shreshtha on 24-03-2017.
 */
@Parcel
public class Discount extends SugarRecord {
    private double value;
    private UnitType unit = UnitType.INR;

    public UnitType getUnit() {
        return unit;
    }
    public void setUnit(UnitType unit) {
        this.unit = unit;
    }
    public double getValue() {
        return value;
    }
    public void setValue(double value) {
        this.value = value;
    }
}
