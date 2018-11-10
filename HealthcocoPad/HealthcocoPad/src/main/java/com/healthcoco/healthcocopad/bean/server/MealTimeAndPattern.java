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
    private List<DietPlanRecipeItem> food;
    @Ignore
    private MealTimeType timeType;
    private Float fromTime;
    private Float toTime;

    public String getForeignAssessmentId() {
        return foreignAssessmentId;
    }

    public void setForeignAssessmentId(String foreignAssessmentId) {
        this.foreignAssessmentId = foreignAssessmentId;
    }

    public List<DietPlanRecipeItem> getFood() {
        return food;
    }

    public void setFood(List<DietPlanRecipeItem> food) {
        this.food = food;
    }

    public MealTimeType getTimeType() {
        return timeType;
    }

    public void setTimeType(MealTimeType timeType) {
        this.timeType = timeType;
    }

    public Float getFromTime() {
        return fromTime;
    }

    public void setFromTime(Float fromTime) {
        this.fromTime = fromTime;
    }

    public Float getToTime() {
        return toTime;
    }

    public void setToTime(Float toTime) {
        this.toTime = toTime;
    }
}
