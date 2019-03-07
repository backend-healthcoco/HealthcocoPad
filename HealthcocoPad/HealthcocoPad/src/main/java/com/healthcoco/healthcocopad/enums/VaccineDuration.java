package com.healthcoco.healthcocopad.enums;

public enum VaccineDuration {
    BIRTH("Birth"),
    WEEKS_6("6 weeks"),
    WEEKS_10("10 weeks"),
    MONTHS_14("14 weeks"),
    MONTHS_6("6 months"),
    MONTHS_7("7 months"),
    MONTHS_9("9 months"),
    MONTHS_9_TO_12("9-12 months"),
    MONTHS_12("12 months"),
    MONTHS_15("15 months"),
    MONTHS_16_TO_18("16-18 months"),
    MONTH_18("18 months"),
    YEARS_2("2 years"),
    YEARS_4_To_6("4-6 years"),
    YEARS_10_To_12("10-12 years"),
    CUSTOM("Custom");

    VaccineDuration(String value) {
        this.value = value;
    }

    private String value;

    public String getValue() {
        return value;
    }
}
