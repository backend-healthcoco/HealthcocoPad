package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.enums.ExcerciseType;
import com.healthcoco.healthcocopad.enums.QuantityType;

import org.parceler.Parcel;

@Parcel
public class MealQuantity {

    private QuantityType type;
    private double value;

    public QuantityType getType() {
        return type;
    }

    public void setType(QuantityType type) {
        this.type = type;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }
}
