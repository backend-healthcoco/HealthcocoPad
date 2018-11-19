package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Prashant on 24/09/2018.
 */

@Parcel
public class FoodAndAllergies extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(FoodAndAllergies.class.getSimpleName());

    protected String foreignAssessmentId;
    protected String recipeItemId;

    @Ignore
    private List<DietPlanRecipeItem> foods;
    private String allergies;

    public String getForeignAssessmentId() {
        return foreignAssessmentId;
    }

    public void setForeignAssessmentId(String foreignAssessmentId) {
        this.foreignAssessmentId = foreignAssessmentId;
    }

    public List<DietPlanRecipeItem> getFoods() {
        return foods;
    }

    public void setFoods(List<DietPlanRecipeItem> foods) {
        this.foods = foods;
    }

    public String getAllergies() {
        return allergies;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String getRecipeItemId() {
        return recipeItemId;
    }

    public void setRecipeItemId(String recipeItemId) {
        this.recipeItemId = recipeItemId;
    }
}
