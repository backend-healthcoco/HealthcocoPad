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
    DIAGNOSIS(false, R.string.diagnosis);

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
