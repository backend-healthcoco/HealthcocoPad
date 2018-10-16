package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Prashant on 24/09/2018.
 */

@Parcel
public class IngredientResponse extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(IngredientResponse.class.getSimpleName());

    protected String foreignMealId;
    protected String equivalentMeasurementsJsonString;
    protected String caloriesJsonString;
    protected String carbohydreateJsonString;
    protected String fatJsonString;
    protected String proteinJsonString;
    protected String fiberJsonString;
    protected String customUniqueId;

    private String uniqueId;
    private String name;
    @Ignore
    private MealQuantity quantity;
    private String note;
    @Ignore
    private MealQuantity calories;
    @Ignore
    private List<EquivalentQuantities> equivalentMeasurements;
    @Ignore
    private MealQuantity fat;
    @Ignore
    private MealQuantity protein;
    @Ignore
    private MealQuantity carbohydreate;
    @Ignore
    private MealQuantity fiber;

    public String getForeignMealId() {
        return foreignMealId;
    }

    public void setForeignMealId(String foreignMealId) {
        this.foreignMealId = foreignMealId;
    }

    public String getCustomUniqueId() {
        return customUniqueId;
    }

    public void setCustomUniqueId(String customUniqueId) {
        this.customUniqueId = customUniqueId;
    }

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

    public List<EquivalentQuantities> getEquivalentMeasurements() {
        return equivalentMeasurements;
    }

    public void setEquivalentMeasurements(List<EquivalentQuantities> equivalentMeasurements) {
        this.equivalentMeasurements = equivalentMeasurements;
    }

    public String getEquivalentMeasurementsJsonString() {
        return equivalentMeasurementsJsonString;
    }

    public void setEquivalentMeasurementsJsonString(String equivalentMeasurementsJsonString) {
        this.equivalentMeasurementsJsonString = equivalentMeasurementsJsonString;
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

    public MealQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(MealQuantity quantity) {
        this.quantity = quantity;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
