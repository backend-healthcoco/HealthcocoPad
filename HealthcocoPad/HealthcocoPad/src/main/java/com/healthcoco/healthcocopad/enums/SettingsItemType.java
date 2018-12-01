package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 14/01/16.
 */
public enum SettingsItemType {
    //    PATIENT(R.string.patient, R.string.search_patient, R.string.settings_no_groups_added, ClassType.PATIENT),
    GROUPS(R.string.patient, R.string.search_group_name, R.string.settings_no_groups_added, ClassType.GROUPS),
    //    REFERENCE(R.string.referred_by_settings, R.string.search_referred_by, R.string.settings_no_reffered_by_added, ClassType.REFERENCE),
    CLINICAL_NOTE(R.string.clinical_notes, R.string.search_clinical_notes, R.string.settings_no_clinical_notes_added, ClassType.CLINICAL_NOTES),
    HISTORY(R.string.history_small, R.string.search_history, R.string.settings_no_history_added, ClassType.HISTORY),
    //    DIRECTION(R.string.direction, R.string.search_disease, R.string.settings_no_direction_added, ClassType.DIRECTIONS),
//    FREQUENCY(R.string.frequency, R.string.search_frequency, R.string.settings_no_frequency_added, ClassType.FREQUENCY_DOSAGE),
//    DRUG(R.string.prescriptions, R.string.search_drug, R.string.settings_no_drug_added, ClassType.DRUG),
    PRESCRIPTION(R.string.prescriptions, R.string.search_prescription, R.string.settings_no_prescription, ClassType.PRESCRIPTION),
    TEMPLATES(R.string.templates, R.string.search_template, R.string.no_templates_added, ClassType.TEMPLATE),
    TREATMENT(R.string.treatment, R.string.search_treatment, R.string.no_treatment_added, ClassType.TREATMENT),
    //    HELP(R.string.help, 0, 0, null),
//    RATE_US(R.string.rate_us, 0, 0, null),
    //    PATIENT(R.string.patient, R.string.search_patient, R.string.settings_no_patient_added, ClassType.PATIENT),
//    CLINICAL_NOTES(R.string.clinical_notes, R.string.search_clinical_notes, R.string.no_clinical_note, ClassType.CLINICAL_NOTES),
//    HISTORY(R.string.history_small, R.string.search_history, R.string.settings_no_history_added, ClassType.HISTORY),
//    TEMPLATE(R.string.templates, R.string.search_template, R.string.no_templates, ClassType.TEMPLATE),
    UI_PERMISSION(R.string.ui_permission, R.string.search_ui_permission, R.string.settings_no_ui_permission, ClassType.UI_PERMISSION),
    PRINT(R.string.print, 0, 0, null),
    KIOSK(R.string.kiosk_setting, 0, 0, null),
    RECIPE(R.string.recipe_list, 0, 0, null),
    INGREDIENT(R.string.add_ingredient, 0, 0, null),
    SYNC(R.string.sync, 0, 0, null),
    //    BILLING(R.string.billing, R.string.search_billing, R.string.settings_no_billing, ClassType.BILLING),
    //    SMS(R.string.sms, 0, R.string.settings_no_sms, null),
//    EMAIL(R.string.email, 0, 0, null),
//    PRINT(R.string.print, 0, 0, null),
//    ID_CREATION(R.string.id_creation, 0, 0, null),
    HELP_IMPROVE(R.string.help_us_to_improve, 0, 0, null),
    //    SYNC_CONTACT(R.string.contact_sync, 0, 0, null),
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
