package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.enums.MealType;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class Meal extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(Meal.class.getSimpleName());
    protected String foreignAssessmentId;
    protected MealTimeType foreignTimeType;
    protected String quantityJsonString;
    @Unique
    private String uniqueId;
    private String name;
    @Ignore
    private List<RecipeItem> ingredients;
    @Ignore
    private List<IngredientAddItem> nutrients;
    @Ignore
    private MealQuantity quantity;
    private String note;
    @Ignore
    private MealType type;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getForeignAssessmentId() {
        return foreignAssessmentId;
    }

    public void setForeignAssessmentId(String foreignAssessmentId) {
        this.foreignAssessmentId = foreignAssessmentId;
    }

    public MealTimeType getForeignTimeType() {
        return foreignTimeType;
    }

    public void setForeignTimeType(MealTimeType foreignTimeType) {
        this.foreignTimeType = foreignTimeType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<RecipeItem> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<RecipeItem> ingredients) {
        this.ingredients = ingredients;
    }

    public List<IngredientAddItem> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<IngredientAddItem> nutrients) {
        this.nutrients = nutrients;
    }

    public MealQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(MealQuantity quantity) {
        this.quantity = quantity;
    }

    public String getQuantityJsonString() {
        return quantityJsonString;
    }

    public void setQuantityJsonString(String quantityJsonString) {
        this.quantityJsonString = quantityJsonString;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public MealType getType() {
        return type;
    }

    public void setType(MealType type) {
        this.type = type;
    }
}
