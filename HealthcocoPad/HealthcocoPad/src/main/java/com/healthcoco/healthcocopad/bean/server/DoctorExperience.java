package com.healthcoco.healthcocopad.bean.server;

import android.os.Parcel;
import android.os.Parcelable;

import com.healthcoco.healthcocopad.enums.DoctorExperienceUnit;
import com.orm.SugarRecord;
import com.orm.annotation.Unique;

public class DoctorExperience extends SugarRecord implements Parcelable {
    @Unique
    protected String foreignUniqueId;
    private Integer experience;

    private DoctorExperienceUnit period;
    protected String periodValue;

    public DoctorExperience() {
    }

    protected DoctorExperience(Parcel in) {
        foreignUniqueId = in.readString();
        experience = in.readInt();
        periodValue = in.readString();
    }

    public static final Creator<DoctorExperience> CREATOR = new Creator<DoctorExperience>() {
        @Override
        public DoctorExperience createFromParcel(Parcel in) {
            return new DoctorExperience(in);
        }

        @Override
        public DoctorExperience[] newArray(int size) {
            return new DoctorExperience[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(foreignUniqueId);
        dest.writeInt(experience);
        dest.writeString(periodValue);
    }
}
