package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 15/04/17.
 */

public enum SuggestionType {
    DRUGS(true, R.string.search_drug, R.string.all_drugs),
    LAB_TESTS(true, R.string.search_lab_test, R.string.lab_tests),
    COMPLAINTS(false, R.string.complaints),
    OBSERVATION(false, R.string.observations),
    INVESTIGATION(false, R.string.investigations),
    DIAGNOSIS(false, R.string.diagnosis),
    PRESENT_COMPLAINT(false, R.string.present_complaint),
    HISTORY_OF_PRESENT_COMPLAINT(false, R.string.history_of_present_complaints),
    MENSTRUAL_HISTORY(false, R.string.menstrual_history),
    OBSTETRIC_HISTORY(false, R.string.obstetric_history),
    GENERAL_EXAMINATION(false, R.string.general_examination),
    SYSTEMIC_EXAMINATION(false, R.string.systemic_examination),
    PROVISIONAL_DIAGNOSIS(false, R.string.provisional_diagnosis),
    ECG_DETAILS(false, R.string.ecg_details),
    ECHO(false, R.string.echo),
    X_RAY_DETAILS(false, R.string.xray_details),
    HOLTER(false, R.string.holter),
    PA(false, R.string.pa),
    PV(false, R.string.pv),
    PS(false, R.string.ps),
    INDICATION_OF_USG(false, R.string.indication_of_usg),
    NOTES(false, R.string.notes);

    private final boolean isFromServerList;
    private final int headerTitleId;
    private int searchHintId = R.string.search;

    SuggestionType(boolean isFromServerList, int headerTitleId) {
        this.isFromServerList = isFromServerList;
        this.headerTitleId = headerTitleId;
    }


    SuggestionType(boolean isFromServerList, int searchHintId, int headerTitleId) {
        this.isFromServerList = isFromServerList;
        this.searchHintId = searchHintId;
        this.headerTitleId = headerTitleId;
    }

    public boolean isFromServerList() {
        return isFromServerList;
    }

    public int getHeaderTitleId() {
        return headerTitleId;
    }

    public int getSearchHintId() {
        return searchHintId;
    }
}
