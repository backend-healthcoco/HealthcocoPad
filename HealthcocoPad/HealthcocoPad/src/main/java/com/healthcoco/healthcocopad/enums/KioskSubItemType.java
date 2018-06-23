package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 14/01/16.
 */
public enum KioskSubItemType {

    PATIENT_REGISTRATION("PATIENT_REGISTRATION", R.string.patient, R.drawable.ic_profile, R.string.settings_no_groups_added),
    KNOW_YOUR_DOCTOR("KNOW_YOUR_DOCTOR", R.string.patient, R.drawable.ic_profile, R.string.settings_no_groups_added),
    VIDEOS("VIDEOS", R.string.patient, R.drawable.ic_profile, R.string.settings_no_groups_added),
    FEEDBACK("FEEDBACK", R.string.patient, R.drawable.ic_profile, R.string.settings_no_groups_added),
    HEALTHCOCO_BLOG("HEALTHCOCO_BLOG", R.string.patient, R.drawable.ic_profile, R.string.settings_no_groups_added);


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
