package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.enums.QuantityType;

import org.parceler.Parcel;

@Parcel
public class EquivalentQuantities {

    private QuantityType servingType;
    private double value;
    private QuantityType type;


    public QuantityType getServingType() {
        return servingType;
    }

    public void setServingType(QuantityType servingType) {
        this.servingType = servingType;
    }

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
