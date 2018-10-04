package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class RecipeResponse extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(RecipeResponse.class.getSimpleName());
    protected String quantityJsonString;
    protected String calariesJsonString;

    protected String equivalentMeasurementsJsonString;
    protected String mealTimingJsonString;

    @Unique
    private String uniqueId;
    private String name;
    private String direction;
    private Boolean nutrientValueAtRecipeLevel;
    @Ignore
    private List<Ingredient> ingredients;
    @Ignore
    private List<Nutrients> nutrients;
    @Ignore
    private MealQuantity quantity;
    @Ignore
    private MealQuantity calaries;
    @Ignore
    private List<EquivalentQuantities> equivalentMeasurements;
    @Ignore
    private List<String> mealTiming;


    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public List<Nutrients> getNutrients() {
        return nutrients;
    }

    public void setNutrients(List<Nutrients> nutrients) {
        this.nutrients = nutrients;
    }

    public MealQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(MealQuantity quantity) {
        this.quantity = quantity;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Boolean getNutrientValueAtRecipeLevel() {
        return nutrientValueAtRecipeLevel;
    }

    public void setNutrientValueAtRecipeLevel(Boolean nutrientValueAtRecipeLevel) {
        this.nutrientValueAtRecipeLevel = nutrientValueAtRecipeLevel;
    }

    public String getQuantityJsonString() {
        return quantityJsonString;
    }

    public void setQuantityJsonString(String quantityJsonString) {
        this.quantityJsonString = quantityJsonString;
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


    public String getEquivalentMeasurementsJsonString() {
        return equivalentMeasurementsJsonString;
    }

    public void setEquivalentMeasurementsJsonString(String equivalentMeasurementsJsonString) {
        this.equivalentMeasurementsJsonString = equivalentMeasurementsJsonString;
    }


    public String getMealTimingJsonString() {
        return mealTimingJsonString;
    }

    public void setMealTimingJsonString(String mealTimingJsonString) {
        this.mealTimingJsonString = mealTimingJsonString;
    }


    public List<EquivalentQuantities> getEquivalentMeasurements() {
        return equivalentMeasurements;
    }

    public void setEquivalentMeasurements(List<EquivalentQuantities> equivalentMeasurements) {
        this.equivalentMeasurements = equivalentMeasurements;
    }

    public List<String> getMealTiming() {
        return mealTiming;
    }

    public void setMealTiming(List<String> mealTiming) {
        this.mealTiming = mealTiming;
    }
}
