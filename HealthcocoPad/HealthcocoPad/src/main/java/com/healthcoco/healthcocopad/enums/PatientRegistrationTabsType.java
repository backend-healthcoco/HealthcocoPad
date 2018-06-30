package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Prashant on 25/06/2018.
 */

public enum PatientRegistrationTabsType {

    SEARCH_PATIENT(0, "Search Patient", R.string.search_patient, R.drawable.ic_profile),
    BASIC_DETAILS(1, "Basic Details", R.string.basic_details, R.drawable.ic_profile),
    MORE_DETAIlS(2, "More Details", R.string.more_details, R.drawable.ic_profile);

    private int textId = 0;
    private int drawableId = 0;
    private int tabPosition;
    private String stringValue;

    PatientRegistrationTabsType(int tabPosition, String stringValue, int textId, int drawableId) {
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
