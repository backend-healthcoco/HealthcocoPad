package com.healthcoco.healthcocopad.enums;

/**
 * Created by neha on 14/04/17.
 */

public enum PrescriptionPermissionType {
    GENERIC_DRUGS("GENERIC_DRUGS"), ALLDRUGS("ALLDRUGS"), LAB("LAB"), OPTHO_RX("OPTHO_RX"),
    MYDRUGS("MYDRUGS"), ADVICE("ADVICE");

    PrescriptionPermissionType(String value) {
        this.value = value;
    }

    public static PrescriptionPermissionType getPrescriptionPermissionType(String permission) {
        for (PrescriptionPermissionType prescriptionPermissionType :
                values()) {
            if (permission.equals(prescriptionPermissionType.getValue()))
                return prescriptionPermissionType;
        }
        return null;
    }
    private final String value;

    public String getValue() {
        return value;
    }
}
