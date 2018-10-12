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
    protected String carbohydreateJsonString;
    protected String fatJsonString;
    protected String proteinJsonString;
    protected String fiberJsonString;
    @Unique
    private String uniqueId;
    @Ignore
    private MealQuantity quantity;
    private String name;
    @Ignore
    private List<Ingredient> ingredients;
    private String direction;
    private String note;
    @Ignore
    private MealQuantity calaries;
    @Ignore
    protected MealQuantity currentQuantity;
    protected String equivalentMeasurementsJsonString;
    protected Boolean nutrientValueAtRecipeLevel;
    @Ignore
    protected List<EquivalentQuantities> equivalentMeasurements;
    @Ignore
    private MealQuantity fat;
    @Ignore
    private MealQuantity protein;
    @Ignore
    private MealQuantity carbohydreate;
    @Ignore
    private MealQuantity fiber;

    @Ignore
    protected MealQuantity calariesTemp;
    @Ignore
    protected MealQuantity fatTemp;
    @Ignore
    protected MealQuantity proteinTemp;
    @Ignore
    protected MealQuantity carbohydreateTemp;
    @Ignore
    protected MealQuantity fiberTemp;

    protected double fatPerHundredUnit;
    protected double proteinPerHundredUnit;
    protected double carbohydreatePerHundredUnit;
    protected double fiberPerHundredUnit;
    protected double calariesPerHundredUnit;
    @Ignore
    protected MealQuantity tempQuantity;

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

    public MealQuantity getTempQuantity() {
        return tempQuantity;
    }

    public void setTempQuantity(MealQuantity tempQuantity) {
        this.tempQuantity = tempQuantity;
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

    public double getFatPerHundredUnit() {
        return fatPerHundredUnit;
    }

    public void setFatPerHundredUnit(double fatPerHundredUnit) {
        this.fatPerHundredUnit = fatPerHundredUnit;
    }

    public double getProteinPerHundredUnit() {
        return proteinPerHundredUnit;
    }

    public void setProteinPerHundredUnit(double proteinPerHundredUnit) {
        this.proteinPerHundredUnit = proteinPerHundredUnit;
    }

    public double getCarbohydreatePerHundredUnit() {
        return carbohydreatePerHundredUnit;
    }

    public void setCarbohydreatePerHundredUnit(double carbohydreatePerHundredUnit) {
        this.carbohydreatePerHundredUnit = carbohydreatePerHundredUnit;
    }

    public double getFiberPerHundredUnit() {
        return fiberPerHundredUnit;
    }

    public void setFiberPerHundredUnit(double fiberPerHundredUnit) {
        this.fiberPerHundredUnit = fiberPerHundredUnit;
    }

    public MealQuantity getCalariesTemp() {
        return calariesTemp;
    }

    public void setCalariesTemp(MealQuantity calariesTemp) {
        this.calariesTemp = calariesTemp;
    }

    public MealQuantity getFatTemp() {
        return fatTemp;
    }

    public void setFatTemp(MealQuantity fatTemp) {
        this.fatTemp = fatTemp;
    }

    public MealQuantity getProteinTemp() {
        return proteinTemp;
    }

    public void setProteinTemp(MealQuantity proteinTemp) {
        this.proteinTemp = proteinTemp;
    }

    public MealQuantity getCarbohydreateTemp() {
        return carbohydreateTemp;
    }

    public void setCarbohydreateTemp(MealQuantity carbohydreateTemp) {
        this.carbohydreateTemp = carbohydreateTemp;
    }

    public MealQuantity getFiberTemp() {
        return fiberTemp;
    }

    public void setFiberTemp(MealQuantity fiberTemp) {
        this.fiberTemp = fiberTemp;
    }
}
