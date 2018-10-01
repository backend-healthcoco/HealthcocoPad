package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class DietplanAddItem extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(DietplanAddItem.class.getSimpleName());

    protected String foreignDietId;
    protected String calariesJsonString;
    @Ignore
    private MealTimeType mealTiming;
    @Ignore
    private List<DietPlanRecipeItem> recipes;
    private String note;
    @Ignore
    private MealQuantity calaries;

    public String getForeignDietId() {
        return foreignDietId;
    }

    public void setForeignDietId(String foreignDietId) {
        this.foreignDietId = foreignDietId;
    }

    public MealTimeType getMealTiming() {
        return mealTiming;
    }

    public void setMealTiming(MealTimeType mealTiming) {
        this.mealTiming = mealTiming;
    }

    public List<DietPlanRecipeItem> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<DietPlanRecipeItem> recipes) {
        this.recipes = recipes;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public MealQuantity getCalaries() {
        return calaries;
    }

    public void setCalaries(MealQuantity calaries) {
        this.calaries = calaries;
    }

    public String getCalariesJsonString() {
        return calariesJsonString;
    }

    public void setCalariesJsonString(String calariesJsonString) {
        this.calariesJsonString = calariesJsonString;
    }
}
