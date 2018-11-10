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
public class DietPlanRecipeItem extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(DietPlanRecipeItem.class.getSimpleName());
    protected String quantityJsonString;
    protected String caloriesJsonString;
    protected String carbohydreateJsonString;
    protected String fatJsonString;
    protected String proteinJsonString;
    protected String fiberJsonString;

    protected String foreignAssessmentId;

    private String uniqueId;
    @Ignore
    private MealQuantity quantity;
    @Ignore
    protected MealQuantity quantityTemp;
    private String name;
    @Ignore
    private List<Ingredient> ingredients;
    private String direction;
    private String note;
    @Ignore
    private MealQuantity fat;
    @Ignore
    private MealQuantity protein;
    @Ignore
    private MealQuantity carbohydreate;
    @Ignore
    private MealQuantity fiber;
    protected double caloriesPerHundredUnit;
    @Ignore
    protected MealQuantity currentQuantity;
    protected String equivalentMeasurementsJsonString;
    protected Boolean nutrientValueAtRecipeLevel;
    @Ignore
    protected List<EquivalentQuantities> equivalentMeasurements;
    @Ignore
    private MealQuantity calories;
    @Ignore
    private List<Nutrients> generalNutrients;
    @Ignore
    private List<Nutrients> carbNutrients;

    protected double fatPerHundredUnit;
    protected double proteinPerHundredUnit;
    protected double carbohydreatePerHundredUnit;
    protected double fiberPerHundredUnit;
    @Ignore
    private List<Nutrients> lipidNutrients;
    @Ignore
    private List<Nutrients> proteinAminoAcidNutrients;
    @Ignore
    private List<Nutrients> vitaminNutrients;
    @Ignore
    private List<Nutrients> mineralNutrients;
    @Ignore
    private List<Nutrients> otherNutrients;


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

    public double getCaloriesPerHundredUnit() {
        return caloriesPerHundredUnit;
    }

    public void setCaloriesPerHundredUnit(double caloriesPerHundredUnit) {
        this.caloriesPerHundredUnit = caloriesPerHundredUnit;
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

    public MealQuantity getQuantityTemp() {
        return quantityTemp;
    }

    public void setQuantityTemp(MealQuantity quantityTemp) {
        this.quantityTemp = quantityTemp;
    }

    public String getForeignAssessmentId() {
        return foreignAssessmentId;
    }

    public void setForeignAssessmentId(String foreignAssessmentId) {
        this.foreignAssessmentId = foreignAssessmentId;
    }
}
