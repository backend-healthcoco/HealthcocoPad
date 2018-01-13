package com.healthcoco.healthcocopad.bean;

import com.healthcoco.healthcocopad.enums.DoctorExperienceUnit;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

@org.parceler.Parcel
public class DoctorExperience extends SugarRecord {
    @Unique
    protected String foreignUniqueId;
    protected String periodValue;
    private Integer experience;
    private DoctorExperienceUnit period;

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

    public String getForeignUniqueId() {
        return foreignUniqueId;
    }

    public void setForeignUniqueId(String foreignUniqueId) {
        this.foreignUniqueId = foreignUniqueId;
    }

    public String getPeriodValue() {
        return periodValue;
    }

    public void setPeriodValue(String periodValue) {
        this.periodValue = periodValue;
    }

}
