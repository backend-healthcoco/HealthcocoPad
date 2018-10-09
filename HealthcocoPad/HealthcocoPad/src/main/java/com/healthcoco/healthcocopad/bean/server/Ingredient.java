package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant on 24/09/2018.
 */

@Parcel
public class Ingredient extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(Ingredient.class.getSimpleName());

    protected String foreignMealId;
    protected String equivalentMeasurementsJsonString;
    protected String calariesJsonString;
    protected String carbohydreateJsonString;
    protected String fatJsonString;
    protected String proteinJsonString;
    protected String fiberJsonString;
    protected String customUniqueId;

    private String uniqueId;
    private String name;
    private double value;
    private QuantityType type;
    @Ignore
    private List<EquivalentQuantities> equivalentMeasurements;
    @Ignore
    private MealQuantity calaries;
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

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public QuantityType getType() {
        return type;
    }

    public void setType(QuantityType type) {
        this.type = type;
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
}
