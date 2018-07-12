package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Prashant on 25/06/2018.
 */

public enum DoctorAndClinicTabsType {

    DOCTORS(0, "Doctors", R.string.doctor, R.drawable.ic_profile),
    PHOTOS(1, "Photos", R.string.clinic_photos, R.drawable.ic_profile),
    ABOUT(2, "About", R.string.about, R.drawable.ic_profile);

    private int textId = 0;
    private int drawableId = 0;
    private int tabPosition;
    private String stringValue;

    DoctorAndClinicTabsType(int tabPosition, String stringValue, int textId, int drawableId) {
        this.textId = textId;
        this.drawableId = drawableId;
        this.tabPosition = tabPosition;
        this.stringValue = stringValue;
    }

    public int getTextId() {
        return textId;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getTabPosition() {
        return tabPosition;
    }

    public String getStringValue() {
        return stringValue;
    }
}
