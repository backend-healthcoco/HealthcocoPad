package com.healthcoco.healthcocopad.enums;

public enum DoctorFacilityType {

    IBS("IBS"), CALL("CALL"), BOOK("BOOK");

    private String type;

    private DoctorFacilityType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
