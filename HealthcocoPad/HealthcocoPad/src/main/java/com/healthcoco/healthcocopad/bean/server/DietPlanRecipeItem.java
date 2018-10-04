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
public class DietPlanRecipeItem extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(DietPlanRecipeItem.class.getSimpleName());
    protected String quantityJsonString;
    protected String calariesJsonString;
    @Unique
    private String uniqueId;
    @Ignore
    private MealQuantity quantity;
    private String name;
    @Ignore
    private List<Ingredient> ingredients;
    @Ignore
    private List<Nutrients> nutrients;
    private String direction;
    private String note;
    @Ignore
    private MealQuantity calaries;

    protected double calariesPerHundredUnit;
    @Ignore
    protected MealQuantity currentQuantity;
    protected String equivalentMeasurementsJsonString;
    protected String mealTimingJsonString;
    @Ignore
    protected List<Nutrients> nutrientPerHundredUnit;
    protected Boolean nutrientValueAtRecipeLevel;
    @Ignore
    protected List<EquivalentQuantities> equivalentMeasurements;


    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
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

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
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

    public double getCalariesPerHundredUnit() {
        return calariesPerHundredUnit;
    }

    public void setCalariesPerHundredUnit(double calariesPerHundredUnit) {
        this.calariesPerHundredUnit = calariesPerHundredUnit;
    }

    public MealQuantity getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(MealQuantity currentQuantity) {
        this.currentQuantity = currentQuantity;
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

    public List<Nutrients> getNutrientPerHundredUnit() {
        return nutrientPerHundredUnit;
    }

    public void setNutrientPerHundredUnit(List<Nutrients> nutrientPerHundredUnit) {
        this.nutrientPerHundredUnit = nutrientPerHundredUnit;
    }

    public Boolean getNutrientValueAtRecipeLevel() {
        return nutrientValueAtRecipeLevel;
    }

    public void setNutrientValueAtRecipeLevel(Boolean nutrientValueAtRecipeLevel) {
        this.nutrientValueAtRecipeLevel = nutrientValueAtRecipeLevel;
    }

    public List<EquivalentQuantities> getEquivalentMeasurements() {
        return equivalentMeasurements;
    }

    public void setEquivalentMeasurements(List<EquivalentQuantities> equivalentMeasurements) {
        this.equivalentMeasurements = equivalentMeasurements;
    }
}
