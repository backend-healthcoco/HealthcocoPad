package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class RecipeResponse extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(RecipeResponse.class.getSimpleName());
    protected String quantityJsonString;
    protected String caloriesJsonString;
    protected String equivalentMeasurementsJsonString;
    protected String mealTimingJsonString;
    protected String carbohydreateJsonString;
    protected String fatJsonString;
    protected String proteinJsonString;
    protected String fiberJsonString;

    @Unique
    private String uniqueId;
    private String name;
    private String direction;
    private Boolean nutrientValueAtRecipeLevel;
    @Ignore
    private List<Ingredient> ingredients;
    @Ignore
    private MealQuantity quantity;
    @Ignore
    private MealQuantity calories;
    @Ignore
    private List<EquivalentQuantities> equivalentMeasurements;
    @Ignore
    private List<String> mealTiming;
    @Ignore
    private MealQuantity fat;
    @Ignore
    private MealQuantity protein;
    @Ignore
    private MealQuantity carbohydreate;
    @Ignore
    private MealQuantity fiber;

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

    public MealQuantity getCalories() {
        return calories;
    }

    public void setCalories(MealQuantity calories) {
        this.calories = calories;
    }

    public String getCaloriesJsonString() {
        return caloriesJsonString;
    }

    public void setCaloriesJsonString(String caloriesJsonString) {
        this.caloriesJsonString = caloriesJsonString;
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

    public String getCarbohydreateJsonString() {
        return carbohydreateJsonString;
    }

    public void setCarbohydreateJsonString(String carbohydreateJsonString) {
        this.carbohydreateJsonString = carbohydreateJsonString;
    }

    public String getFatJsonString() {
        return fatJsonString;
    }

    public void setFatJsonString(String fatJsonString) {
        this.fatJsonString = fatJsonString;
    }

    public String getProteinJsonString() {
        return proteinJsonString;
    }

    public void setProteinJsonString(String proteinJsonString) {
        this.proteinJsonString = proteinJsonString;
    }

    public String getFiberJsonString() {
        return fiberJsonString;
    }

    public void setFiberJsonString(String fiberJsonString) {
        this.fiberJsonString = fiberJsonString;
    }

    public MealQuantity getFat() {
        return fat;
    }

    public void setFat(MealQuantity fat) {
        this.fat = fat;
    }

    public MealQuantity getProtein() {
        return protein;
    }

    public void setProtein(MealQuantity protein) {
        this.protein = protein;
    }

    public MealQuantity getCarbohydreate() {
        return carbohydreate;
    }

    public void setCarbohydreate(MealQuantity carbohydreate) {
        this.carbohydreate = carbohydreate;
    }

    public MealQuantity getFiber() {
        return fiber;
    }

    public void setFiber(MealQuantity fiber) {
        this.fiber = fiber;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
