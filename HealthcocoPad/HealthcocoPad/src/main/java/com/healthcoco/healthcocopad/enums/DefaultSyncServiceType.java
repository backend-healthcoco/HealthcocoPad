package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 25-01-2017.
 */

public enum DefaultSyncServiceType {
    GET_DRUG_DOSAGE(R.string.syncing_groups),
    GET_CONTACTS(R.string.syncing_contacts),
    GET_DOCTOR_PROFILE(R.string.syncing_doctor_profile),
    GET_CLINIC_PROFILE(R.string.syncing_clinic_profile),
    GET_DURATION_UNIT(R.string.syncing_duration_unit),
    GET_DIRECTION(R.string.syncing_direction),
    GET_DRUG_TYPE(R.string.syncing_drug_type),
    GET_GROUPS(R.string.syncing_groups),
    GET_CITIES(R.string.syncing_cities),
    GET_SPECIALITIES(R.string.syncing_specialities),
    GET_PROFESSION(R.string.syncing_profession),
    GET_REFERENCE(R.string.syncing_reffered_by),
    GET_BLOOD_GROUP(R.string.syncing_blood_groups),
    //    //    GET_CALENDAR_EVENTS(R.string.syncing_calendar_events),
    GET_PRESENT_COMPLAINT_SUGGESTIONS(R.string.syncing_present_complaint_suggestions),
    GET_COMPLAINT_SUGGESTIONS(R.string.syncing_complaint_suggestions),
    GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS(R.string.syncing_history_of_present_complaint_suggestions),
    GET_MENSTRUAL_HISTORY_SUGGESTIONS(R.string.syncing_menstrual_history_suggestions),
    GET_OBSTETRIC_HISTORY_SUGGESTIONS(R.string.syncing_obstetric_history_suggestions),
    GET_GENERAL_EXAMINATION_SUGGESTIONS(R.string.syncing_general_examination_suggestions),
    GET_SYSTEMIC_EXAMINATION_SUGGESTIONS(R.string.syncing_systemic_examination_suggestions),
    GET_OBSERVATION_SUGEGSTIONS(R.string.syncing_observation_suggestions),
    GET_INVESTIGATION_SUGGESTIONS(R.string.syncing_investigation_suggestions),
    GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS(R.string.syncing_provisional_diagnosis_suggestions),
    GET_DIAGNOSIS_SUGGESTIONS(R.string.syncing_diagnosis_suggestions),
    GET_ECG_DETAILS_SUGGESTIONS(R.string.syncing_ecg_details_suggestions),
    GET_ECHO_SUGGESTIONS(R.string.syncing_echo_suggestions),
    GET_X_RAY_DETAILS_SUGGESTIONS(R.string.syncing_xray_details_suggestions),
    GET_HOLTER_SUGGESTIONS(R.string.syncing_holter_suggestions),
    GET_PA_SUGGESTIONS(R.string.syncing_pa_suggestions),
    GET_PV_SUGGESTIONS(R.string.syncing_pv_suggestions),
    GET_PS_SUGGESTIONS(R.string.syncing_ps_suggestions),
    GET_INDICATION_OF_USG_SUGGESTIONS(R.string.syncing_indication_of_usg_suggestions),
    GET_NOTES_SUGGESTIONS(R.string.syncing_notes_suggestions),
    GET_EAR_EXAM_SUGGESTIONS(R.string.syncing_ear_exam_suggestions),
    GET_NECK_EXAM_SUGGESTIONS(R.string.syncing_neck_exam_suggestions),
    GET_NOSE_EXAM_SUGGESTIONS(R.string.syncing_nose_exam_suggestions),
    GET_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS(R.string.syncing_indirect_larygoscopy_exam_suggestions),
    GET_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS(R.string.syncing_oral_cavity_throat_exam_suggestions),
    GET_PC_EARS_SUGGESTIONS(R.string.syncing_pc_ear_suggestions),
    GET_PC_NOSE_SUGGESTIONS(R.string.syncing_pc_nose_suggestions),
    GET_PC_ORAL_CAVITY_SUGGESTIONS(R.string.syncing_pc_oral_cavity_suggestions),
    GET_PC_THROAT_SUGGESTIONS(R.string.syncing_pc_throat_suggestions),
    GET_PROCEDURE_NOTE_SUGGESTIONS(R.string.syncing_procedures_suggestions),
    GET_SEARCH_ADVICE(R.string.syncing_advice_suggestions),

    GET_HISTORY(R.string.syncing_history),

    GET_TEMPLATES(R.string.syncing_templates),

    GET_BOTH_UI_PERMISSIONS(R.string.syncing_ui_Permissions),

    //    GET_EDUCATION_QUALIFICATION(R.string.syncing_education_qualification),
//    GET_INSTITUTES(R.string.syncing_institutes),
//    GET_MEDICAL_COUNCIL(R.string.syncing_medical_councils),
    SYNC_COMPLETE(R.string.sync_complete);

    private int loadingTitleId;

    DefaultSyncServiceType(int loadingTitleId) {
        this.loadingTitleId = loadingTitleId;
    }

    public static DefaultSyncServiceType getSyncType(WebServiceType webServiceType) {
        DefaultSyncServiceType syncServiceType = GET_DRUG_DOSAGE;
        switch (webServiceType) {
            case GET_DRUG_DOSAGE:
                return GET_DRUG_DOSAGE;
            case GET_CONTACTS:
                return GET_CONTACTS;
            case GET_DOCTOR_PROFILE:
                return GET_DOCTOR_PROFILE;
            case GET_CLINIC_PROFILE:
                return GET_CLINIC_PROFILE;
            case GET_DURATION_UNIT:
                return GET_DURATION_UNIT;
            case GET_DIRECTION:
                return GET_DIRECTION;
            case GET_DRUG_TYPE:
                return GET_DRUG_TYPE;
            case GET_GROUPS:
                return GET_GROUPS;
            case GET_CITIES:
                return GET_CITIES;
            case GET_SPECIALITIES:
                return GET_SPECIALITIES;
            case GET_PROFESSION:
                return GET_PROFESSION;
            case GET_REFERENCE:
                return GET_REFERENCE;
            case GET_BLOOD_GROUP:
                return GET_BLOOD_GROUP;
            case GET_HISTORY_LIST:
                return GET_HISTORY;
            case GET_TEMPLATES_LIST:
                return GET_TEMPLATES;
            case GET_BOTH_PERMISSIONS_FOR_DOCTOR:
                return GET_BOTH_UI_PERMISSIONS;
        }
        return syncServiceType;
    }

    public int getLoadingTitle() {
        return loadingTitleId;
    }
}
