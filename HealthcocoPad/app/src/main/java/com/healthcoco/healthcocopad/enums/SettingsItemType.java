package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 14/01/16.
 */
public enum SettingsItemType {
    //    PATIENT(R.string.patient, R.string.search_patient, R.string.settings_no_patient_added, ClassType.PATIENT),
//    CLINICAL_NOTES(R.string.clinical_notes, R.string.search_clinical_notes, R.string.no_clinical_note, ClassType.CLINICAL_NOTES),
//    HISTORY(R.string.history_small, R.string.search_history, R.string.settings_no_history_added, ClassType.HISTORY),
//    PRESCRIPTION(R.string.prescriptions, R.string.search_prescription, R.string.settings_no_prescription, ClassType.PRESCRIPTION),
//    TEMPLATE(R.string.templates, R.string.search_template, R.string.no_templates, ClassType.TEMPLATE),
    UI_PERMISSION(R.string.ui_permission, R.string.search_ui_permission, R.string.settings_no_ui_permission, ClassType.UI_PERMISSION),
    //    BILLING(R.string.billing, R.string.search_billing, R.string.settings_no_billing, ClassType.BILLING),
//    SMS(R.string.sms, 0, R.string.settings_no_sms, null),
//    EMAIL(R.string.email, 0, 0, null),
//    PRINT(R.string.print, 0, 0, null),
//    ID_CREATION(R.string.id_creation, 0, 0, null),
    ABOUT(R.string.about_us, 0, 0, null),
    SIGN_OUT(R.string.sign_out, 0, 0, null);

    private final int searchHintId;
    private final int titleId;
    private final ClassType classType;
    private int noDataId;

    SettingsItemType(int titleId, int searchHintId, int noDataId, ClassType classType) {
        this.titleId = titleId;
        this.searchHintId = searchHintId;
        this.noDataId = noDataId;
        this.classType = classType;
    }

    public int getSearchHintId() {
        return searchHintId;
    }

    public int getNoDataText() {
        return noDataId;
    }

    public int getTitleId() {
        return titleId;
    }

    public ClassType getClassType() {
        return classType;
    }
}
