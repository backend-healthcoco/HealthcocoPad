package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

public enum ContactListItemOptionType {
    EDIT_MOBILE_NUMBER(R.string.edit_mobile_number, R.drawable.ic_edit_mobile_number),
    DELETE_PATIENT(R.string.delete_patient, R.drawable.ic_delete_patient);

    private int value;
    private int drawableId;

    ContactListItemOptionType(int value, int drawableId) {
        this.value = value;
        this.drawableId = drawableId;
    }

    public int getValue() {
        return value;
    }

    public int getDrawableId() {
        return drawableId;
    }
}
