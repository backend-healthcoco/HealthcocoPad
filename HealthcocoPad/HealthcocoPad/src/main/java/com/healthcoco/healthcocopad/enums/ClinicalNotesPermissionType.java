package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

public enum ClinicalNotesPermissionType {
    VITAL_SIGNS("VITAL_SIGNS", R.string.vital_signs, R.string.write_vital_signs, true, R.layout.layout_vital_signs_add_clinical_notes),
    PRESENT_COMPLAINT("PRESENT_COMPLAINT", R.string.present_complaint, R.string.write_present_complaint, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PRESENT_COMPLAINT, 11, SuggestionType.PRESENT_COMPLAINT),
    COMPLAINT("COMPLAINT", R.string.complaints, R.string.write_complaints, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.COMPLAINT, 12, SuggestionType.COMPLAINTS),
    HISTORY_OF_PRESENT_COMPLAINT("HISTORY_OF_PRESENT_COMPLAINT", R.string.history_of_present_complaints, R.string.write_history_of_present_complaints, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.HISTORY_OF_PRESENT_COMPLAINT, 13, SuggestionType.HISTORY_OF_PRESENT_COMPLAINT),
    MENSTRUAL_HISTORY("MENSTRUAL_HISTORY", R.string.menstrual_history, R.string.write_menstrual_history, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.MENSTRUAL_HISTORY, 14, SuggestionType.MENSTRUAL_HISTORY),
    OBSTETRIC_HISTORY("OBSTETRIC_HISTORY", R.string.obstetric_history, R.string.write_obstetric_history, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.OBSTETRIC_HISTORY, 15, SuggestionType.OBSTETRIC_HISTORY),
    GENERAL_EXAMINATION("GENERAL_EXAMINATION", R.string.general_examination, R.string.write_general_examination, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.GENERAL_EXAMINATION, 16, SuggestionType.GENERAL_EXAMINATION),
    SYSTEMIC_EXAMINATION("SYSTEMIC_EXAMINATION", R.string.systemic_examination, R.string.write_systemic_examination, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.SYSTEMIC_EXAMINATION, 17, SuggestionType.SYSTEMIC_EXAMINATION),
    OBSERVATION("OBSERVATION", R.string.observations, R.string.write_observations, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.OBSERVATION, 18, SuggestionType.OBSERVATION),
    INVESTIGATIONS("INVESTIGATIONS", R.string.investigations, R.string.write_investigations, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.INVESTIGATION, 19, SuggestionType.INVESTIGATION),
    PROVISIONAL_DIAGNOSIS("PROVISIONAL_DIAGNOSIS", R.string.provisional_diagnosis, R.string.write_provisional_diagnosis, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PROVISIONAL_DIAGNOSIS, 20, SuggestionType.PROVISIONAL_DIAGNOSIS),
    DIAGNOSIS("DIAGNOSIS", R.string.diagnosis, R.string.write_diagnosis, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.DIAGNOSIS, 21, SuggestionType.DIAGNOSIS),
    ECG("ECG", R.string.ecg_details, R.string.write_ecg_details, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.ECG_DETAILS, 23, SuggestionType.ECG_DETAILS),
    ECHO("ECHO", R.string.echo, R.string.write_echo, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.ECHO, 10, SuggestionType.ECHO),
    XRAY("XRAY", R.string.xray_details, R.string.write_xray_details, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.X_RAY_DETAILS, 25, SuggestionType.X_RAY_DETAILS),
    HOLTER("HOLTER", R.string.holter, R.string.write_holter, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.HOLTER, 26, SuggestionType.HOLTER),
    PA("PA", R.string.pa, R.string.write_pa, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PA, 27, SuggestionType.PA),
    PV("PV", R.string.pv, R.string.write_pv, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PV, 28, SuggestionType.PV),
    PS("PS", R.string.ps, R.string.write_ps, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PS, 30, SuggestionType.PS),
    INDICATION_OF_USG("INDICATION_OF_USG", R.string.indication_of_usg, R.string.write_indication_of_usg, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.INDICATION_OF_USG, 31, SuggestionType.INDICATION_OF_USG),
    PAIN_SCALE("PAIN_SCALE", R.string.pain_scale, R.string.pain_scale, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PAIN_SCALE, 32, SuggestionType.PAIN_SCALE),
    EARS_EXAM("EARS_EXAM", R.string.ears_exam, R.string.ears_exam, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PS, 33, SuggestionType.EARS_EXAM),
    ORAL_CAVITY_THROAT_EXAM("ORAL_CAVITY_THROAT_EXAM", R.string.oral_cavity_throat_exam, R.string.oral_cavity_throat_exam, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.ORAL_CAVITY_THROAT_EXAM, 34, SuggestionType.ORAL_CAVITY_THROAT_EXAM),
    PROCEDURES("PROCEDURES", R.string.procedures, R.string.procedures, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PROCEDURES, 35, SuggestionType.PROCEDURES),
    PAST_HISTORY("PAST_HISTORY", R.string.past_history, R.string.past_history, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PAST_HISTORY, 36, SuggestionType.PAST_HISTORY),
    PC_THROAT("PC_THROAT", R.string.pc_throat, R.string.pc_throat, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PC_THROAT, 37, SuggestionType.PC_THROAT),
    FAMILY_HISTORY("FAMILY_HISTORY", R.string.family_history, R.string.family_history, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.FAMILY_HISTORY, 38, SuggestionType.FAMILY_HISTORY),
    INDIRECT_LARYGOSCOPY_EXAM("INDIRECT_LARYGOSCOPY_EXAM", R.string.indirect_larygoscopy_exam, R.string.indirect_larygoscopy_exam, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.INDIRECT_LARYGOSCOPY_EXAM, 39, SuggestionType.INDIRECT_LARYGOSCOPY_EXAM),
    PERSONAL_HISTORY("PERSONAL_HISTORY", R.string.personal_history, R.string.personal_history, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PERSONAL_HISTORY, 40, SuggestionType.PERSONAL_HISTORY),
    PC_ORAL_CAVITY("PC_ORAL_CAVITY", R.string.pc_oral_cavity, R.string.pc_oral_cavity, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PC_ORAL_CAVITY, 41, SuggestionType.PC_ORAL_CAVITY),
    PC_NOSE("PC_NOSE", R.string.pc_nose, R.string.pc_nose, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PC_NOSE, 42, SuggestionType.PC_NOSE),
    PC_EARS("PC_EARS", R.string.pc_ears, R.string.pc_ears, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.PC_EARS, 43, SuggestionType.PC_EARS),
    GENERAL_HISTORY("GENERAL_HISTORY", R.string.general_history, R.string.general_history, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.GENERAL_HISTORY, 44, SuggestionType.GENERAL_HISTORY),
    NOSE_EXAM("NOSE_EXAM", R.string.nose_exam, R.string.nose_exam, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.NOSE_EXAM, 45, SuggestionType.NOSE_EXAM),
    NECK_EXAM("NECK_EXAM", R.string.neck_exam, R.string.neck_exam, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.NECK_EXAM, 46, SuggestionType.NECK_EXAM),
    USG_GENDER_COUNT("USG_GENDER_COUNT", R.string.usg_gender_count, R.string.usg_gender_count, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.USG_GENDER_COUNT, 47, SuggestionType.USG_GENDER_COUNT),
    LMP("LMP", R.string.lmp, R.string.lmp, false, R.layout.layout_item_add_myscript_clinical_note_permision, AutoCompleteTextViewType.LMP, 48, SuggestionType.LMP),
    NOTES("NOTES", R.string.notes, R.string.write_note, false, R.layout.layout_item_add_myscript_clinical_note_permision),
    DIAGRAM("DIAGRAM", R.string.diagrams, R.string.diagrams, true, R.layout.layout_item_add_myscript_clinical_note_permision),
    TOBACCO("TOBACCO", R.string.tobacco, R.string.tobacco, false, R.layout.layout_item_add_myscript_clinical_note_permision),
    ALCOHOL("ALCOHOL", R.string.alcohol, R.string.alcohol, false, R.layout.layout_item_add_myscript_clinical_note_permision),
    SMOKING("SMOKING", R.string.smoking, R.string.smoking, false, R.layout.layout_item_add_myscript_clinical_note_permision),
    DIET("DIET", R.string.diet, R.string.diet, false, R.layout.layout_item_add_myscript_clinical_note_permision),
    OCCUPATION("OCCUPATION", R.string.occupation, R.string.occupation, false, R.layout.layout_item_add_myscript_clinical_note_permision),
    DRUGS("DRUGS", R.string.drugs, R.string.drugs, false, R.layout.layout_item_add_myscript_clinical_note_permision),
    MEDICINE("MEDICINE", R.string.medicine, R.string.medicine, false, R.layout.layout_item_add_myscript_clinical_note_permision),
    ALLERGIES("ALLERGIES", R.string.allergie, R.string.allergies, false, R.layout.layout_item_add_myscript_clinical_note_permision),
    SURGICAL("SURGICAL", R.string.surgical, R.string.surgical, false, R.layout.layout_item_add_myscript_clinical_note_permision);
    private final boolean isDifferentView;
    private final int layoutId;
    private final String value;
    private SuggestionType suggestionType;
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
        this.suggestionType = suggestionType;
        this.suggestionType = suggestionType;
    }

    private ClinicalNotesPermissionType(String value, int textId, int hintId, boolean isDifferentLayout, int layoutId, AutoCompleteTextViewType autoCompleteTextViewType, int autotvId, SuggestionType suggestionType) {
        this.value = value;
        this.textId = textId;
        this.isDifferentView = isDifferentLayout;
        this.layoutId = layoutId;
        this.autotvId = autotvId;
        this.autoCompleteTextViewType = autoCompleteTextViewType;
        this.hintId = hintId;
        this.suggestionType = suggestionType;
    }

    public static ClinicalNotesPermissionType getClinicalNotesPermissionType(String permission) {
        for (ClinicalNotesPermissionType clinicalNotesPermissionType :
                values()) {
            if (permission.equals(clinicalNotesPermissionType.getValue()))
                return clinicalNotesPermissionType;
        }
        return null;
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

    public String getValue() {
        return value;
    }

    public SuggestionType getSuggestionType() {
        return suggestionType;
    }

    public void setSuggestionType(SuggestionType suggestionType) {
        this.suggestionType = suggestionType;
    }
}
