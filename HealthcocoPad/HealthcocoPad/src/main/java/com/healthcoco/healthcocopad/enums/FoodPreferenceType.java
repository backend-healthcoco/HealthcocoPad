package com.healthcoco.healthcocopad.enums;

public enum FoodPreferenceType {

    VEG_GRAIN("VEG_GRAIN"), MILK("MILK"), EGG("EGG"), FISH("FISH"), MEAT("MEAT"), SEAFOOD("SEAFOOD"), HONEY("HONEY");

    private String foodPreferenceType;

    FoodPreferenceType(String foodPreferenceType) {
        this.foodPreferenceType = foodPreferenceType;
    }

    public String getFoodPreferenceType() {
        return foodPreferenceType;
    }

}
