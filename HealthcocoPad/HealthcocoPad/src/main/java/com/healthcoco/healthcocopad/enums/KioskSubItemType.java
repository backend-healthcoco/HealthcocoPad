package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 22/06/18.
 */
public enum KioskSubItemType {

    PATIENT_REGISTER("PATIENT_REGISTER", R.string.patient_registration, R.drawable.img_registration, R.string.settings_no_groups_added),
    DOCTOR_AND_CLINIC("DOCTOR_AND_CLINIC", R.string.about_doctor_and_clinic, R.drawable.img_know, R.string.settings_no_groups_added),
    VIDEO("VIDEO", R.string.videos, R.drawable.img_video, R.string.settings_no_groups_added),
    FEEDBACK("FEEDBACK", R.string.feedback, R.drawable.img_feedback, R.string.settings_no_groups_added),
    FOLLOW_UP_APPOINTMENT("FOLLOW_UP_APPOINTMENT", R.string.appointment, R.drawable.img_consent_form, R.string.settings_no_groups_added),
    BLOGS("BLOGS", R.string.blogs, R.drawable.img_consent_form, R.string.settings_no_groups_added);


    private int titleId;
    private int drawableId;
    private int hintId;
    private String value;

    KioskSubItemType(String value, int titleId, int drawableId, int hintId) {
        this.value = value;
        this.titleId = titleId;
        this.drawableId = drawableId;
        this.hintId = hintId;
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

    public int getHintId() {
        return hintId;
    }
}
