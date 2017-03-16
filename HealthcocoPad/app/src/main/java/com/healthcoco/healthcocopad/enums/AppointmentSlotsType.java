package com.healthcoco.healthcocopad.enums;

/**
 * Created by neha on 06/02/16.
 */
public enum AppointmentSlotsType {
    SLOT1(15, TimeUnitType.MINS), SLOT2(30, TimeUnitType.MINS), SLOT3(45, TimeUnitType.MINS),
    SLOT4(60, TimeUnitType.MINS), SLOT5(75, TimeUnitType.MINS), SLOT6(90, TimeUnitType.MINS),
    SLOT7(105, TimeUnitType.MINS), SLOT8(120, TimeUnitType.MINS);
    private final float time;
    private final TimeUnitType units;

    AppointmentSlotsType(float time, TimeUnitType units) {
        this.time = time;
        this.units = units;
    }

    public float getTime() {
        return time;
    }

    public TimeUnitType getUnits() {
        return units;
    }
}
