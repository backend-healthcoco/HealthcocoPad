package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.enums.DoctorExperienceUnit;

@org.parceler.Parcel
public class DoctorExperience {
    private Integer experience;

    private DoctorExperienceUnit period;
    protected String periodValue;

    public DoctorExperience() {
    }

    public String getFormattedExperience() {
        String formattedExperience = experience + " " + getPeriod().getExperiencePeriod();
        return formattedExperience;
    }

    public Integer getExperience() {
        if (experience == null)
            experience = 0;
        return experience;
    }

    public void setExperience(Integer experience) {
        this.experience = experience;
    }

    public DoctorExperienceUnit getPeriod() {
        return period;
    }

    public void setPeriod(DoctorExperienceUnit period) {
        this.period = period;
    }

    public String getPeriodValue() {
        return periodValue;
    }

    public void setPeriodValue(String periodValue) {
        this.periodValue = periodValue;
    }
}
