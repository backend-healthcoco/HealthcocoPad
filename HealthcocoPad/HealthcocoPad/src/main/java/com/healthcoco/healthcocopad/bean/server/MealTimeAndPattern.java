package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.Duration;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class MealTimeAndPattern extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(MealTimeAndPattern.class.getSimpleName());

    protected String foreignAssessmentId;

    @Ignore
    private List<Meal> food;
    @Ignore
    private MealTimeType timeType;
    private int fromTime;
    private int toTime;

    public String getForeignAssessmentId() {
        return foreignAssessmentId;
    }

    public void setForeignAssessmentId(String foreignAssessmentId) {
        this.foreignAssessmentId = foreignAssessmentId;
    }

    public List<Meal> getFood() {
        return food;
    }

    public void setFood(List<Meal> food) {
        this.food = food;
    }

    public MealTimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(MealTimeType timeType) {
        this.timeType = timeType;
    }

    public int getFromTime() {
        return fromTime;
    }

    public void setFromTime(int fromTime) {
        this.fromTime = fromTime;
    }

    public int getToTime() {
        return toTime;
    }

    public void setToTime(int toTime) {
        this.toTime = toTime;
    }
}
