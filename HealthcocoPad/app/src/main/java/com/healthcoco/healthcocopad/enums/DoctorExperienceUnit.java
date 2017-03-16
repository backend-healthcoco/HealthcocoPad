package com.healthcoco.healthcocopad.enums;

public enum DoctorExperienceUnit {
    MONTH("Month"), YEAR("Year"),YEARS("Years"),MONTHS("Months");

    private String period;

    DoctorExperienceUnit(String period) {
        this.period = period;
    }

    public String getExperiencePeriod() {
        return period;
    }

}
