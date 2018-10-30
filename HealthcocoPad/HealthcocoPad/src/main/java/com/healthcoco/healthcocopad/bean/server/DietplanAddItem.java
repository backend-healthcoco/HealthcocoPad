package com.healthcoco.healthcocopad.bean.server;

import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class DietplanAddItem extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(DietplanAddItem.class.getSimpleName());

    protected String foreignDietId;
    protected String calariesJsonString;
    @Ignore
    private MealTimeType mealTiming;
    @Ignore
    private List<DietPlanRecipeItem> recipes;
    private String note;
    @Ignore
    private MealQuantity calories;

    private Float toTime;

    protected double calTotal;
    protected double fatTotal;
    protected double proteinTotal;
    protected double carbohydreateTotal;
    protected double fiberTotal;

    @Ignore
    protected List<Nutrients> generalNutrients;
    @Ignore
    protected List<Nutrients> carbNutrients;
    @Ignore
    protected List<Nutrients> lipidNutrients;
    @Ignore
    protected List<Nutrients> proteinAminoAcidNutrients;
    @Ignore
    protected List<Nutrients> vitaminNutrients;
    @Ignore
    protected List<Nutrients> mineralNutrients;
    @Ignore
    protected List<Nutrients> otherNutrients;


    public String getForeignDietId() {
        return foreignDietId;
    }

    public void setForeignDietId(String foreignDietId) {
        this.foreignDietId = foreignDietId;
    }

    public MealTimeType getMealTiming() {
        return mealTiming;
    }

    public void setMealTiming(MealTimeType mealTiming) {
        this.mealTiming = mealTiming;
    }

    public List<DietPlanRecipeItem> getRecipes() {
        return recipes;
    }

    public void setRecipes(List<DietPlanRecipeItem> recipes) {
        this.recipes = recipes;
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

    public String getCalariesJsonString() {
        return calariesJsonString;
    }

    public void setCalariesJsonString(String calariesJsonString) {
        this.calariesJsonString = calariesJsonString;
    }

    public double getFatTotal() {
        return fatTotal;
    }

    public void setFatTotal(double fatTotal) {
        this.fatTotal = fatTotal;
    }

    public double getProteinTotal() {
        return proteinTotal;
    }

    public void setProteinTotal(double proteinTotal) {
        this.proteinTotal = proteinTotal;
    }

    public double getCarbohydreateTotal() {
        return carbohydreateTotal;
    }

    public void setCarbohydreateTotal(double carbohydreateTotal) {
        this.carbohydreateTotal = carbohydreateTotal;
    }

    public double getFiberTotal() {
        return fiberTotal;
    }

    public void setFiberTotal(double fiberTotal) {
        this.fiberTotal = fiberTotal;
    }

    public double getCalTotal() {
        return calTotal;
    }

    public void setCalTotal(double calTotal) {
        this.calTotal = calTotal;
    }

    public Float getToTime() {
        return toTime;
    }

    public void setToTime(Float toTime) {
        this.toTime = toTime;
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
