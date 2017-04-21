package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

public enum ClinicalNotesPermissionType {

    VITAL_SIGNS("VITAL_SIGNS", R.string.vital_signs, R.string.write_vital_signs, true, R.layout.layout_vital_signs_add_clinical_notes),
    PRESENT_COMPLAINT("PRESENT_COMPLAINT", R.string.present_complaint, R.string.write_present_complaint, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.PRESENT_COMPLAINT, 11),
    COMPLAINT("COMPLAINT", R.string.complaints, R.string.write_complaints, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.COMPLAINT, 12),
    HISTORY_OF_PRESENT_COMPLAINT("HISTORY_OF_PRESENT_COMPLAINT", R.string.history_of_present_complaints, R.string.write_history_of_present_complaints, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.HISTORY_OF_PRESENT_COMPLAINT, 13),
    MENSTRUAL_HISTORY("MENSTRUAL_HISTORY", R.string.menstrual_history, R.string.write_menstrual_history, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.MENSTRUAL_HISTORY, 14),
    OBSTETRIC_HISTORY("OBSTETRIC_HISTORY", R.string.obstetric_history, R.string.write_obstetric_history, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.OBSTETRIC_HISTORY, 15),
    GENERAL_EXAMINATION("GENERAL_EXAMINATION", R.string.general_examination, R.string.write_general_examination, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.GENERAL_EXAMINATION, 16),
    SYSTEMIC_EXAMINATION("SYSTEMIC_EXAMINATION", R.string.systemic_examination, R.string.write_systemic_examination, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.SYSTEMIC_EXAMINATION, 17),
    OBSERVATION("OBSERVATION", R.string.observations, R.string.write_observations, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.OBSERVATION, 18),
    INVESTIGATIONS("INVESTIGATIONS", R.string.investigations, R.string.write_investigations, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.INVESTIGATION, 19),
    PROVISIONAL_DIAGNOSIS("PROVISIONAL_DIAGNOSIS", R.string.provisional_diagnosis, R.string.write_provisional_diagnosis, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.PROVISIONAL_DIAGNOSIS, 20),
    DIAGNOSIS("DIAGNOSIS", R.string.diagnosis, R.string.write_diagnosis, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.DIAGNOSIS, 21),
    ECG_DETAILS("ECG_DETAILS", R.string.ecg_details, R.string.write_ecg_details, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.ECG_DETAILS, 22),
    ECG("ECG", R.string.ecg_details, R.string.write_ecg_details, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.ECG_DETAILS, 23),
    ECHO("ECHO", R.string.echo, R.string.write_echo, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.ECHO, 10),
    X_RAY_DETAILS("X_RAY_DETAILS", R.string.xray_details, R.string.write_xray_details, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.X_RAY_DETAILS, 24),
    XRAY("XRAY", R.string.xray_details, R.string.write_xray_details, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.X_RAY_DETAILS, 25),
    HOLTER("HOLTER", R.string.holter, R.string.write_holter, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.HOLTER, 26),
    PA("PA", R.string.pa, R.string.write_pa, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.PA, 27),
    PV("PV", R.string.pv, R.string.write_pv, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.PV, 28),
    PS("PS", R.string.ps, R.string.write_ps, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.PS, 30),
    INDICATION_OF_USG("INDICATION_OF_USG", R.string.indication_of_usg, R.string.write_indication_of_usg, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.INDICATION_OF_USG, 31),
    NOTES("NOTES", R.string.notes, R.string.write_note, false, R.layout.layout_item_add_clinical_note_permision),
    DIAGRAM("DIAGRAM", R.string.diagrams, R.string.diagrams, true, R.layout.layout_item_add_clinical_note_permision);
    private final boolean isDifferentView;
    private final int layoutId;
    private final String value;
    private AutoCompleteTextViewType autoCompleteTextViewType = null;
    private int autotvId = 0;
    private int textId;
    private int hintId;

    private ClinicalNotesPermissionType(String value, int textId, int hintId, boolean isDifferentLayout, int layoutId) {
        this.value = value;
        this.textId = textId;
        this.isDifferentView = isDifferentLayout;
        this.layoutId = layoutId;
        this.hintId = hintId;
    }

    private ClinicalNotesPermissionType(String value, int textId, int hintId, boolean isDifferentLayout, int layoutId, AutoCompleteTextViewType autoCompleteTextViewType, int autotvId) {
        this.value = value;
        this.textId = textId;
        this.isDifferentView = isDifferentLayout;
        this.layoutId = layoutId;
        this.autotvId = autotvId;
        this.autoCompleteTextViewType = autoCompleteTextViewType;
        this.hintId = hintId;
    }

    public int getTextId() {
        return textId;
    }

    public int getHintId() {
        return hintId;
    }

    public boolean isDifferentView() {
        return isDifferentView;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public int getAutotvId() {
        return autotvId;
    }

    public AutoCompleteTextViewType getAutoCompleteTextViewType() {
        return autoCompleteTextViewType;
    }

    public static ClinicalNotesPermissionType getClinicalNotesPermissionType(String permission) {
        for (ClinicalNotesPermissionType clinicalNotesPermissionType :
                values()) {
            if (permission.equals(clinicalNotesPermissionType.getValue()))
                return clinicalNotesPermissionType;
        }
        return null;
    }

    public String getValue() {
        return value;
    }
}
