package com.healthcoco.healthcocopad.bean.request;

import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.bean.server.Nutrients;
import com.healthcoco.healthcocopad.utilities.StringUtil;
import com.orm.SugarRecord;
import com.orm.annotation.Ignore;
import com.orm.annotation.Unique;

import org.parceler.Parcel;

import java.util.List;

@Parcel
public class RecipeRequest extends SugarRecord {
    public static String TABLE_NAME = " " + StringUtil.toSQLName(RecipeRequest.class.getSimpleName());

    @Unique
    private String uniqueId;
    private String locationId;
    private String doctorId;
    private String hospitalId;
    @Ignore
    private MealQuantity quantity;
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
    @Ignore
    private List<EquivalentQuantities> equivalentMeasurements;
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
    private List<Nutrients> otherNutrients;

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(String doctorId) {
        this.doctorId = doctorId;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public MealQuantity getQuantity() {
        return quantity;
    }

    public void setQuantity(MealQuantity quantity) {
        this.quantity = quantity;
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

    public List<EquivalentQuantities> getEquivalentMeasurements() {
        return equivalentMeasurements;
    }

    public void setEquivalentMeasurements(List<EquivalentQuantities> equivalentMeasurements) {
        this.equivalentMeasurements = equivalentMeasurements;
    }

    public MealQuantity getCalories() {
        return calories;
    }

    public void setCalories(MealQuantity calories) {
        this.calories = calories;
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
