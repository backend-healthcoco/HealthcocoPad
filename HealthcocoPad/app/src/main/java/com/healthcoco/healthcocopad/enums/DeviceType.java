package com.healthcoco.healthcocopad.enums;

/**
 * Created by neha on 14/06/16.
 */
public enum DeviceType {
    ANDROID("ANDROID"), IOS("IOS"), WINDOWS("WINDOWS");

    private final String text;

    DeviceType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
