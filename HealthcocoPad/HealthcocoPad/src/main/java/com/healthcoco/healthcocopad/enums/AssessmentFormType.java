package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by Prashant on 18/09/2018.
 */
public enum AssessmentFormType {

    PERSONAL_DETAILS("PERSONAL_DETAILS", R.string.personal_details, R.drawable.img_registration, CommonOpenUpFragmentType.PERSONAL_DETAILS),
    MEASUREMENT("MEASUREMENT", R.string.measurements, R.drawable.img_know, CommonOpenUpFragmentType.MEASUREMENT),
    FOOD_AND_EXERCISE("FOOD_AND_EXERCISE", R.string.food_and_exercise, R.drawable.img_video, CommonOpenUpFragmentType.FOOD_AND_EXERCISE),
    LIFE_SYLE("LIFE_SYLE", R.string.life_style, R.drawable.img_feedback, CommonOpenUpFragmentType.LIFE_SYLE),
    MEDICAL_INFORMATION("MEDICAL_INFORMATION", R.string.medical_information, R.drawable.img_appointmnt, CommonOpenUpFragmentType.MEDICAL_INFORMATION);

    private int titleId;
    private int drawableId;
    private String value;
    private CommonOpenUpFragmentType openUpFragmentType;

    AssessmentFormType(String value, int titleId, int drawableId, CommonOpenUpFragmentType openUpFragmentType) {
        this.value = value;
        this.titleId = titleId;
        this.drawableId = drawableId;
        this.openUpFragmentType = openUpFragmentType;
    }

    public String getValue() {
        return value;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public CommonOpenUpFragmentType getOpenUpFragmentType() {
        return openUpFragmentType;
    }
}
