package com.healthcoco.healthcocopad.enums;

public enum MealType {

    RECIPE("RECIPE"), INGREDIENT("INGREDIENT");

    private String mealType;

    MealType(String mealType) {
        this.mealType = mealType;
    }

    public String getMealType() {
        return mealType;
    }

}
