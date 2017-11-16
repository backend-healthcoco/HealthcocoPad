package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.UnitType;
import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by Shreshtha on 04-07-2017.
 */
@Parcel
public class UnitValue extends SugarRecord {
    private double value = 0.0;
    private UnitType unit;

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public UnitType getUnit() {
        return unit;
    }

    public void setUnit(UnitType unit) {
        this.unit = unit;
    }
}
