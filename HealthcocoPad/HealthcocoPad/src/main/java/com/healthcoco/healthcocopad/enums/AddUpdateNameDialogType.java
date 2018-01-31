package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 17/01/16.
 */
public enum AddUpdateNameDialogType {
    PRESENT_COMPLAINT(R.string.present_complaint, R.string.write_a_present_complaint, R.string.please_enter_present_complaint),
    COMPLAINT(R.string.complaints, R.string.write_a_complaints, R.string.please_enter_complaints),
    HISTORY_OF_PRESENT_COMPLAINT(R.string.history_of_present_complaints, R.string.write_history_of_present_complaints, R.string.please_enter_history_of_present_complaints),
    MENSTRUAL_HISTORY(R.string.menstrual_history, R.string.write_menstrual_history, R.string.please_enter_menstrual_history),
    OBSTETRIC_HISTORY(R.string.obstetric_history, R.string.write_obstetric_history, R.string.please_enter_obstetric_history),
    GENERAL_EXAMINATION(R.string.general_examination, R.string.write_general_examination, R.string.please_enter_general_examination),
    SYSTEMIC_EXAMINATION(R.string.systemic_examination, R.string.write_systemic_examination, R.string.please_enter_systemic_examination),
    OBSERVATION(R.string.observations, R.string.write_a_complaints, R.string.please_enter_complaints),
    INVESTIGATIONS(R.string.investigations, R.string.write_observations, R.string.please_enter_observations),
    PROVISIONAL_DIAGNOSIS(R.string.provisional_diagnosis, R.string.write_provisional_diagnosis, R.string.please_enter_provisional_diagnosis),
    DIAGNOSIS(R.string.diagnosis, R.string.write_diagnosis, R.string.please_enter_diagnoses),
    ECG(R.string.ecg_details, R.string.write_ecg_details, R.string.please_enter_ecg_details),
    ECHO(R.string.echo, R.string.write_echo, R.string.please_enter_echo),
    XRAY(R.string.xray_details, R.string.write_xray_details, R.string.please_enter_xray_details),
    HOLTER(R.string.holter, R.string.write_holter, R.string.please_enter_holter),
    INDICATION_OF_USG(R.string.indication_of_usg, R.string.write_indication_of_usg, R.string.please_enter_indication_of_usg),
    NOTES(R.string.notes, R.string.write_note, R.string.please_enter_notes),
    PA(R.string.pa, R.string.write_pa, R.string.please_enter_pa),
    PS(R.string.ps, R.string.write_ps, R.string.please_enter_ps),
    PV(R.string.pv, R.string.write_pv, R.string.please_enter_pv),
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
