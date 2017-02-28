package com.healthcoco.healthcocoplus.enums;


import com.healthcoco.healthcocopad.R;

public enum ClinicalNotesPermissionType {

//    VITAL_SIGNS(R.string.vital_signs, true, R.layout.layout_vital_signs_add_clinical_notes),
//    PRESENT_COMPLAINT(R.string.present_complaint, false, R.layout.layout_item_add_clinical_note_permision),
//    COMPLAINT(R.string.complaints, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.COMPLAINT, 11),
//    HISTORY_OF_PRESENT_COMPLAINT(R.string.history_of_present_complaints, false, R.layout.layout_item_add_clinical_note_permision),
//    MENSTRUAL_HISTORY(R.string.menstrual_history, false, R.layout.layout_item_add_clinical_note_permision),
//    OBSTETRIC_HISTORY(R.string.obstetric_history, false, R.layout.layout_item_add_clinical_note_permision),
//    GENERAL_EXAMINATION(R.string.general_examination, false, R.layout.layout_item_add_clinical_note_permision),
//    SYSTEMIC_EXAMINATION(R.string.systemic_examination, false, R.layout.layout_item_add_clinical_note_permision),
//    OBSERVATION(R.string.observations, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.OBSERVATION, 12),
//    INVESTIGATIONS(R.string.investigations, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.INVESTIGATION, 13),
//    PROVISIONAL_DIAGNOSIS(R.string.provisional_diagnosis, false, R.layout.layout_item_add_clinical_note_permision),
//    DIAGNOSIS(R.string.diagnosis, false, R.layout.layout_item_add_clinical_note_permision, AutoCompleteTextViewType.DIAGNOSIS, 14),
//    ECG_DETAILS(R.string.ecg_details, false, R.layout.layout_item_add_clinical_note_permision),
//    ECHO(R.string.echo, false, R.layout.layout_item_add_clinical_note_permision),
//    X_RAY_DETAILS(R.string.xray_details, false, R.layout.layout_item_add_clinical_note_permision),
//    HOLTER(R.string.holter, false, R.layout.layout_item_add_clinical_note_permision),
//    PA(R.string.pa, false, R.layout.layout_item_add_clinical_note_permision),
//    PV(R.string.pv, false, R.layout.layout_item_add_clinical_note_permision),
//    PS(R.string.ps, false, R.layout.layout_item_add_clinical_note_permision),
//    INDICATION_OF_USG(R.string.indication_of_usg, false, R.layout.layout_item_add_clinical_note_permision),
//    NOTES(R.string.notes, false, R.layout.layout_item_add_clinical_note_permision),
//    DIAGRAM(R.string.diagrams, true, R.layout.layout_item_add_clinical_note_permision)
 ;
    private final boolean isDifferentView;
    private final int layoutId;
    private AutoCompleteTextViewType autoCompleteTextViewType = null;
    private int autotvId = 0;
    private int textId;

    private ClinicalNotesPermissionType(int textId, boolean isDifferentLayout, int layoutId) {
        this.textId = textId;
        this.isDifferentView = isDifferentLayout;
        this.layoutId = layoutId;
    }

    private ClinicalNotesPermissionType(int textId, boolean isDifferentLayout, int layoutId, AutoCompleteTextViewType autoCompleteTextViewType, int autotvId) {
        this.textId = textId;
        this.isDifferentView = isDifferentLayout;
        this.layoutId = layoutId;
        this.autotvId = autotvId;
        this.autoCompleteTextViewType = autoCompleteTextViewType;
    }

    public int getTextId() {
        return textId;
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
}
