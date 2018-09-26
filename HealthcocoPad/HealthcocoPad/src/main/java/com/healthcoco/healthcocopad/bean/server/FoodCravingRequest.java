package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

/**
 * Created by Prashant on 24/09/2018.
 */

@Parcel
public class FoodCravingRequest extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(FoodCravingRequest.class.getSimpleName());

    protected String foreignAssessmentId;

    @Ignore
    private Meal food;
    private int noOfTime;

    public String getForeignAssessmentId() {
        return foreignAssessmentId;
    }

    public void setForeignAssessmentId(String foreignAssessmentId) {
        this.foreignAssessmentId = foreignAssessmentId;
    }

    public Meal getFood() {
        return food;
    }

    public void setFood(Meal food) {
        this.food = food;
    }

    public int getNoOfTime() {
        return noOfTime;
    }

    public void setNoOfTime(int noOfTime) {
        this.noOfTime = noOfTime;
    }
}
