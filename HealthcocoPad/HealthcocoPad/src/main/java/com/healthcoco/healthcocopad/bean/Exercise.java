package com.healthcoco.healthcocopad.bean;

import android.graphics.Bitmap;

import com.healthcoco.healthcocopad.enums.ExcerciseType;

import org.parceler.Parcel;

@Parcel
public class Exercise {

    protected String assessmentId;

    private ExcerciseType type;
    private int minPerDay;
    private int timePerWeek;

    public ExcerciseType getType() {
        return type;
    }

    public void setType(ExcerciseType type) {
        this.type = type;
    }

    public int getMinPerDay() {
        return minPerDay;
    }

    public void setMinPerDay(int minPerDay) {
        this.minPerDay = minPerDay;
    }

    public int getTimePerWeek() {
        return timePerWeek;
    }

    public void setTimePerWeek(int timePerWeek) {
        this.timePerWeek = timePerWeek;
    }
}
