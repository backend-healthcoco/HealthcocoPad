package com.healthcoco.healthcocopad.enums;

public enum MealTimeType {

    EARLY_MORNING("EARLY_MORNING"), BREAKFAST("BREAKFAST"), MID_MORNING("MID_MORNING"), LUNCH("LUNCH"), SNACK("SNACK"),
    DINNER("DINNER");

    private String mealTime;

    MealTimeType(String mealTime) {
        this.mealTime = mealTime;
    }

    public String getMealTime() {
        return mealTime;
    }

}
