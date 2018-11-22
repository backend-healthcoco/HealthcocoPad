package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.enums.AddictionType;
import com.healthcoco.healthcocopad.enums.ConsumeTimeType;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcel;

/**
 * Created by Prashant on 15/11/18.
 */
@Parcel
public class Addiction {

    private AddictionType type;
    private String alcoholType;
    private Integer noOfTime;
    private MealQuantity quantity;
    private ConsumeTimeType consumeTime;

    public AddictionType getType() {
        return type;
    }

    public void setType(AddictionType type) {
        this.type = type;
    }

    public String getAlcoholType() {
        return alcoholType;
    }

    public void setAlcoholType(String alcoholType) {
        this.alcoholType = alcoholType;
    }

    public Integer getNoOfTime() {
        return noOfTime;
    }

    public void setNoOfTime(Integer noOfTime) {
        this.noOfTime = noOfTime;
    }

    public MealQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(MealQuantity quantity) {
        this.quantity = quantity;
    }

    public ConsumeTimeType getConsumeTime() {
        return consumeTime;
    }

    public void setConsumeTime(ConsumeTimeType consumeTime) {
        this.consumeTime = consumeTime;
    }
}
