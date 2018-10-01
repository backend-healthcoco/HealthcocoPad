package com.healthcoco.healthcocopad.enums;

public enum MealTimeType {

    EARLY_MORNING("EARLY_MORNING"), BREAKFAST("BREAKFAST"), MID_MORNING("MID_MORNING"), LUNCH("LUNCH"), EVENING_SNACK("EVENING_SNACK"),
    DINNER("DINNER"), MID_NIGHT("MID_NIGHT");

    private String mealTime;

    MealTimeType(String mealTime) {
        this.mealTime = mealTime;
    }

    public String getMealTime() {
        return mealTime;
    }

}
