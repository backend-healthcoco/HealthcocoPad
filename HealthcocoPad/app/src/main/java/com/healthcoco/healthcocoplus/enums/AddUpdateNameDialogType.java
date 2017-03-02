package com.healthcoco.healthcocoplus.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 17/01/16.
 */
public enum AddUpdateNameDialogType {
    GROUPS(R.string.new_group, R.string.enter_group_name, R.string.please_enter_group_name),
    EMAIL(R.string.email_address, R.string.enter_email_address, R.string.please_enter_email_address),
    REFERENCE(R.string.new_reference, R.string.enter_reference, R.string.please_enter_reference),
    DIRECTION(R.string.new_direction, R.string.enter_direction, R.string.please_enter_direction),
    HISTORY(R.string.new_history, R.string.enter_history, R.string.please_enter_history),
    FREQUENCY(R.string.new_frequency, R.string.enter_frequency_dose, R.string.please_enter_frequency),
    DRUG(R.string.new_drug, R.string.enter_drug, R.string.please_enter_drug),
    DISEASE(R.string.new_disease, R.string.enter_disease, R.string.please_enter_disease_name),
    ADD_DIAGNOSTIC_TEST(R.string.new_test, R.string.test_name, R.string.please_enter_test_name),
    ADD_PATIENT_MOBILE_NUMBER(R.string.enter_patient_mobile_number, R.string.mobile_number, R.string.please_enter_mobile_no),
    ADD_NEW_PATIENT_NAME(R.string.enter_new_patient_name, R.string.patient_name, R.string.please_enter_patient_name),
    LOCAL_STRING_SAVE(R.string.new_note, R.string.write_a_note, R.string.please_write_note);
    private int alertId;
    private int titleId;
    private int hintId;

    AddUpdateNameDialogType(int titleId, int hintId, int alertId) {
        this.titleId = titleId;
        this.hintId = hintId;
        this.alertId = alertId;
    }

    public int getTitleId() {
        return titleId;
    }

    public int getHintId() {
        return hintId;
    }

    public int getAlertId() {
        return alertId;
    }
}
