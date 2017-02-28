package com.healthcoco.healthcocoplus.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 27-02-2017.
 */
public enum UIPermissionsItemType {
    PRESCRIPTION_UI_PERMISSION(R.string.prescriptions),
    CLINICAL_NOTES(R.string.clinical_notes),
    VISITS(R.string.visits),
    PATIENT_TAB_PERMISSION(R.string.patient_tab_permission);

    private final int titleId;

    UIPermissionsItemType(int titleId) {
        this.titleId = titleId;
    }

    public int getTitleId() {
        return titleId;
    }

}
