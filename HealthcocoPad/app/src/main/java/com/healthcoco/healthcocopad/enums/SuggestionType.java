package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 15/04/17.
 */

public enum SuggestionType {
    DRUGS(true, R.string.all_drugs),
    COMPLAINTS(false, R.string.complaints),
    OBSERVATION(false, R.string.observations),
    INVESTIGATION(false, R.string.investigations),
    DIAGNOSIS(false, R.string.diagnosis);

    private final boolean isFromServerList;
    private final int headerTitleId;

    SuggestionType(boolean isFromServerList, int headerTitleId) {
        this.isFromServerList = isFromServerList;
        this.headerTitleId = headerTitleId;
    }

    public boolean isFromServerList() {
        return isFromServerList;
    }

    public int getHeaderTitleId() {
        return headerTitleId;
    }

}
