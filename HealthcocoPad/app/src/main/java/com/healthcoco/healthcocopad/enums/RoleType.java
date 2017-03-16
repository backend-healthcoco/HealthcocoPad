package com.healthcoco.healthcocopad.enums;

/**
 * Created by neha on 14/06/16.
 */
public enum RoleType {
    SUPER_ADMIN("SUPER_ADMIN"), HOSPITAL_ADMIN("HOSPITAL_ADMIN"),
    LOCATION_ADMIN("LOCATION_ADMIN"), DOCTOR("DOCTOR"),
    PATIENT("PATIENT"),ADMIN("ADMIN"),
    STAFF("STAFF");

    private final String text;

    RoleType(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }
}
