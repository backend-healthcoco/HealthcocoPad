package com.healthcoco.healthcocopad.enums;

/**
 * Created by neha on 06/02/16.
 */
public enum TimeUnitType {
    MINS("Mins");
    private String stringToDisplay;

    TimeUnitType(String stringToDisplay) {
        this.stringToDisplay = stringToDisplay;
    }

    public String getValueToDisplay() {
        return stringToDisplay;
    }
}
