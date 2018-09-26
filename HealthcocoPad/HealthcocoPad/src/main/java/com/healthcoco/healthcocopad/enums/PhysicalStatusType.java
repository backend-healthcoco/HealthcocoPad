package com.healthcoco.healthcocopad.enums;

public enum PhysicalStatusType {
    PREGNANT("PREGNANT"), LACTATING("LACTATING"), WORKING("WORKING");

    private String physicalStatusType;

    PhysicalStatusType(String physicalStatusType) {
        this.physicalStatusType = physicalStatusType;
    }

    public String getPhysicalStatusType() {
        return physicalStatusType;
    }

}
