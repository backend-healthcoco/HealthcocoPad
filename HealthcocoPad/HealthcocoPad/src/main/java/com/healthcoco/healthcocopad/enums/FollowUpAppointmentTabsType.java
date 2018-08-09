package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Prashant on 25/06/2018.
 */

public enum FollowUpAppointmentTabsType {

    SEARCH_PATIENT(0, R.string.search_patient, R.drawable.ic_profile),
    SELECT_APPOINTMENT(1, R.string.select_appointment, R.drawable.ic_profile),
    CONFIRM_APPOINTMENT(2, R.string.confirm_appointment, R.drawable.ic_profile);

    private int textId = 0;
    private int drawableId = 0;
    private int tabPosition;

    FollowUpAppointmentTabsType(int tabPosition, int textId, int drawableId) {
        this.textId = textId;
        this.drawableId = drawableId;
        this.tabPosition = tabPosition;
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

}
