package com.healthcoco.healthcocopad.enums;

public enum NutrientCategoryType {

    GENERAL("GENERAL"),
    CARBOHYDRATE("CARBOHYDRATE"),
    VITAMINS("VITAMINS"),
    LIPIDS("LIPIDS"),
    PROTEIN_AMINOACIDS("PROTEIN_AMINOACIDS"),
    MINERALS("MINERALS"),
    OTHERS("OTHERS");


    private String nutrientCategoryType;

    NutrientCategoryType(String nutrientCategoryType) {
        this.nutrientCategoryType = nutrientCategoryType;
    }

    public String getNutrientCategoryType() {
        return nutrientCategoryType;
    }

}
