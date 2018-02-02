package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 26/06/17.
 */

public enum NameHideActivateType {
    GROUPS(R.string.groups, R.string.search_group_name, R.string.settings_no_groups_added, ClassType.GROUPS),
    REFERENCE(R.string.referred_by_settings, R.string.search_referred_by, R.string.settings_no_reffered_by_added, ClassType.REFERENCE),
    HISTORY(R.string.history_small, R.string.search_history, R.string.settings_no_history_added, ClassType.HISTORY),
    TEMPLATES(R.string.templates, R.string.search_template, R.string.no_templates_added, ClassType.TEMPLATE),
    DIRECTION(R.string.direction, R.string.search_disease, R.string.settings_no_direction_added, ClassType.DIRECTIONS),
    TREATMENT(R.string.treatment, R.string.search_treatment, R.string.settings_no_treatment_added, ClassType.TREATMENT),
    FREQUENCY(R.string.frequency, R.string.search_frequency, R.string.settings_no_frequency_added, ClassType.FREQUENCY_DOSAGE),
    DRUG(R.string.drug, R.string.search_drug, R.string.settings_no_drug_added, ClassType.DRUG),

    PRESENT_COMPLAINT(R.string.present_complaint, R.string.search_present_complaint, R.string.settings_no_present_complaint_added, ClassType.PRESENT_COMPLAINT),
    COMPLAINT(R.string.complaints, R.string.search_complaints, R.string.settings_no_complaints_added, ClassType.COMPLAINT),
    HISTORY_OF_PRESENT_COMPLAINT(R.string.history_of_present_complaints, R.string.search_history_of_present_complaints, R.string.settings_no_history_of_present_complaints_added, ClassType.HISTORY_OF_PRESENT_COMPLAINT),
    MENSTRUAL_HISTORY(R.string.menstrual_history, R.string.search_menstrual_history, R.string.settings_no_menstrual_history_added, ClassType.MENSTRUAL_HISTORY),
    OBSTETRIC_HISTORY(R.string.obstetric_history, R.string.search_obstetric_history, R.string.settings_no_obstetric_history_added, ClassType.OBSTETRIC_HISTORY),
    GENERAL_EXAMINATION(R.string.general_examination, R.string.search_general_examination, R.string.settings_no_general_examination_added, ClassType.GENERAL_EXAMINATION),
    SYSTEMIC_EXAMINATION(R.string.systemic_examination, R.string.search_systemic_examination, R.string.settings_no_systemic_examination_added, ClassType.DRUG),
    OBSERVATION(R.string.observations, R.string.search_observations, R.string.settings_no_observations_added, ClassType.OBSERVATION),
    INVESTIGATIONS(R.string.investigations, R.string.search_investigations, R.string.settings_no_investigations_added, ClassType.INVESTIGATIONS),
    PROVISIONAL_DIAGNOSIS(R.string.provisional_diagnosis, R.string.search_provisional_diagnosis, R.string.settings_no_provisional_diagnosis_added, ClassType.PROVISIONAL_DIAGNOSIS),
    DIAGNOSIS(R.string.diagnosis, R.string.search_diagnosis, R.string.settings_no_diagnosis_added, ClassType.DIAGNOSIS),
    ECG(R.string.ecg_details, R.string.search_ecg_details, R.string.settings_no_ecg_details_added, ClassType.ECG),
    ECHO(R.string.echo, R.string.search_echo, R.string.settings_no_echo_added, ClassType.ECHO),
    XRAY(R.string.xray_details, R.string.search_xray_details, R.string.settings_no_xray_details_added, ClassType.XRAY),
    HOLTER(R.string.holter, R.string.search_holter, R.string.settings_no_holter_added, ClassType.HOLTER),
    PA(R.string.pa, R.string.search_pa, R.string.settings_no_pa_added, ClassType.PA),
    PV(R.string.pv, R.string.search_pv, R.string.settings_no_pv_added, ClassType.PV),
    PS(R.string.ps, R.string.search_ps, R.string.settings_no_ps_added, ClassType.PS),
    INDICATION_OF_USG(R.string.indication_of_usg, R.string.search_indication_of_usg, R.string.settings_no_indication_of_usg_added, ClassType.INDICATION_OF_USG),
    NOTES(R.string.note, R.string.search_note, R.string.settings_no_note_added, ClassType.NOTES),
    PROCEDURE_NOTE(R.string.procedures, R.string.search_procedure_note, R.string.settings_no_procedure_added, ClassType.NOTES);

    private final int searchHintId;
    private final int titleId;
    private final ClassType classType;
    private int noDataId;

    NameHideActivateType(int titleId, int searchHintId, int noDataId, ClassType classType) {
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
