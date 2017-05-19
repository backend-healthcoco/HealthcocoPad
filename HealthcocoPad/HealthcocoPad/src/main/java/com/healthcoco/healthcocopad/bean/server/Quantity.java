package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.QuantityEnum;
import com.orm.SugarRecord;

import org.parceler.Parcel;

/**
 * Created by Shreshtha on 24-03-2017.
 */
@Parcel
public class Quantity extends SugarRecord {
    private int value;
    private QuantityEnum type;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public QuantityEnum getType() {
        return type;
    }

    public void setType(QuantityEnum type) {
        this.type = type;
    }

}
