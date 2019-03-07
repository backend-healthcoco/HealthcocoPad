package com.healthcoco.healthcocopad.enums;

public enum BrandGroupType {
    SIX_IN_ONE("SIX IN ONE"),

    FIVE_IN_ONE("FIVE IN ONE"),

    FOUR_IN_ONE("FOUR IN ONE");

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    private String value;

    BrandGroupType(String value) {
        this.value = value;
    }
}
