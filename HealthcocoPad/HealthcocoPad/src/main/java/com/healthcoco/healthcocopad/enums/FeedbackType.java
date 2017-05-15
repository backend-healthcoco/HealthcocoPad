package com.healthcoco.healthcocopad.enums;

/**
 * Created by neha on 23/01/16.
 */
public enum FeedbackType {
    HELP_US("HELP_US"), REFERRER("REFERRER"),
    PRESCRIPTION("PRESCRIPTION"), APPOINTMENT("APPOINTMENT"),
    DOCTOR("DOCTOR"), LAB("LAB"), RECOMMENDATION("RECOMMENDATION"),
    REPORT("REPORT");

    private String type;

    private FeedbackType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
