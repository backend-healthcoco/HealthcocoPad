package com.healthcoco.healthcocopad.bean.server;

import org.parceler.Parcel;

/**
 * Created by Shreshtha on 14-07-2017.
 */
@Parcel
public class TreatmentFields {
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
