package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.enums.ExerciseType;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;

import org.parceler.Parcel;

@Parcel
public class Exercise extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(Exercise.class.getSimpleName());

    protected String foreignAssessmentId;

    private ExerciseType type;
    private int minPerDay;
    private int timePerWeek;

    public ExerciseType getType() {
        return type;
    }

    public void setType(ExerciseType type) {
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

    public String getForeignAssessmentId() {
        return foreignAssessmentId;
    }

    public void setForeignAssessmentId(String foreignAssessmentId) {
        this.foreignAssessmentId = foreignAssessmentId;
    }
}
