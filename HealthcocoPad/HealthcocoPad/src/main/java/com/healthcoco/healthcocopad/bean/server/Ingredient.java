package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

import java.util.List;

/**
 * Created by Prashant on 24/09/2018.
 */

@Parcel
public class Ingredient extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(Ingredient.class.getSimpleName());

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
    private double value;
    private QuantityType type;
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
    @Ignore
    private MealQuantity calories;
    @Ignore
    private List<Nutrients> generalNutrients;
    @Ignore
    private List<Nutrients> carbNutrients;
    @Ignore
    private List<Nutrients> lipidNutrients;
    @Ignore
    private List<Nutrients> proteinAminoAcidNutrients;
    @Ignore
    private List<Nutrients> vitaminNutrients;
    @Ignore
    private List<Nutrients> mineralNutrients;
    @Ignore
    protected MealQuantity caloriesTemp;
    @Ignore
    private List<Nutrients> otherNutrients;
    protected double caloriesPerHundredUnit;
    @Ignore
    protected MealQuantity fatTemp;
    @Ignore
    protected MealQuantity proteinTemp;
    @Ignore
    protected MealQuantity carbohydreateTemp;
    @Ignore
    protected MealQuantity fiberTemp;
    @Ignore
    protected MealQuantity currentQuantity;
    protected double fatPerHundredUnit;
    protected double proteinPerHundredUnit;
    protected double carbohydreatePerHundredUnit;
    protected double fiberPerHundredUnit;
    @Ignore
    protected MealQuantity tempQuantity;

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

    public MealQuantity getCaloriesTemp() {
        return caloriesTemp;
    }

    public void setCaloriesTemp(MealQuantity caloriesTemp) {
        this.caloriesTemp = caloriesTemp;
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

    public double getCaloriesPerHundredUnit() {
        return caloriesPerHundredUnit;
    }

    public void setCaloriesPerHundredUnit(double caloriesPerHundredUnit) {
        this.caloriesPerHundredUnit = caloriesPerHundredUnit;
    }

    public MealQuantity getTempQuantity() {
        return tempQuantity;
    }

    public void setTempQuantity(MealQuantity tempQuantity) {
        this.tempQuantity = tempQuantity;
    }

    public MealQuantity getCurrentQuantity() {
        return currentQuantity;
    }

    public void setCurrentQuantity(MealQuantity currentQuantity) {
        this.currentQuantity = currentQuantity;
    }

    public List<Nutrients> getGeneralNutrients() {
        return generalNutrients;
    }

    public void setGeneralNutrients(List<Nutrients> generalNutrients) {
        this.generalNutrients = generalNutrients;
    }

    public List<Nutrients> getCarbNutrients() {
        return carbNutrients;
    }

    public void setCarbNutrients(List<Nutrients> carbNutrients) {
        this.carbNutrients = carbNutrients;
    }

    public List<Nutrients> getLipidNutrients() {
        return lipidNutrients;
    }

    public void setLipidNutrients(List<Nutrients> lipidNutrients) {
        this.lipidNutrients = lipidNutrients;
    }

    public List<Nutrients> getProteinAminoAcidNutrients() {
        return proteinAminoAcidNutrients;
    }

    public void setProteinAminoAcidNutrients(List<Nutrients> proteinAminoAcidNutrients) {
        this.proteinAminoAcidNutrients = proteinAminoAcidNutrients;
    }

    public List<Nutrients> getVitaminNutrients() {
        return vitaminNutrients;
    }

    public void setVitaminNutrients(List<Nutrients> vitaminNutrients) {
        this.vitaminNutrients = vitaminNutrients;
    }

    public List<Nutrients> getMineralNutrients() {
        return mineralNutrients;
    }

    public void setMineralNutrients(List<Nutrients> mineralNutrients) {
        this.mineralNutrients = mineralNutrients;
    }

    public List<Nutrients> getOtherNutrients() {
        return otherNutrients;
    }

    public void setOtherNutrients(List<Nutrients> otherNutrients) {
        this.otherNutrients = otherNutrients;
    }
}
