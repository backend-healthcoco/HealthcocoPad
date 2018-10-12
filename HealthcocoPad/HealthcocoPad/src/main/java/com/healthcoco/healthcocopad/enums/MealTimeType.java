package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

public enum MealTimeType {

    EARLY_MORNING("EARLY_MORNING", R.string.early_morning), BREAKFAST("BREAKFAST", R.string.breakfast),
    MID_MORNING("MID_MORNING", R.string.mid_morning), LUNCH("LUNCH", R.string.lunch), POST_LUNCH("POST_LUNCH", R.string.post_lunch),
    EVENING_SNACK("EVENING_SNACK", R.string.evening_snack), DINNER("DINNER", R.string.dinner),
    POST_DINNER("POST_DINNER", R.string.post_dinner), MID_NIGHT("MID_NIGHT", R.string.mid_night);

    private String mealTime;
    private int mealTitle;

    MealTimeType(String mealTime, int mealTitle) {
        this.mealTitle = mealTitle;
        this.mealTime = mealTime;
    }

    public String getMealTime() {
        return mealTime;
    }

    public int getMealTitle() {
        return mealTitle;
    }
}
