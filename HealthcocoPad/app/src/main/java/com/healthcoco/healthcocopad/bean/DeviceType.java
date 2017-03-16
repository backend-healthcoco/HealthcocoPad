package com.healthcoco.healthcocopad.bean;

/**
 * Created by Shreshtha on 20-01-2017.
 */
public enum  DeviceType {
    ANDROID("ANDROID"), IOS("IOS"), WINDOWS("WINDOWS");

    private final String text;

    DeviceType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
