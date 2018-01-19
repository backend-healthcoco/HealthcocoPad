package com.healthcoco.healthcocopad.enums;

/**
 * Created by neha on 06/02/16.
 */
public enum AppointmentSlotsType {
    SLOT1(5, TimeUnitType.MINS),
    SLOT2(10, TimeUnitType.MINS),
    SLOT3(15, TimeUnitType.MINS),
    SLOT4(20, TimeUnitType.MINS),
    SLOT5(25, TimeUnitType.MINS),
    SLOT6(30, TimeUnitType.MINS),
    SLOT7(45, TimeUnitType.MINS),
    SLOT8(60, TimeUnitType.MINS),
    SLOT9(75, TimeUnitType.MINS),
    SLOT10(90, TimeUnitType.MINS),
    SLOT11(105, TimeUnitType.MINS),
    SLOT12(120, TimeUnitType.MINS);
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
