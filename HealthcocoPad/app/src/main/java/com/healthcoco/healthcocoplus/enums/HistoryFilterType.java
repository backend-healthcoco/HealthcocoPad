package com.healthcoco.healthcocoplus.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 14-03-2017.
 */
public enum HistoryFilterType {
    MEDICAL_HISTORY("", R.string.past_history),
    FAMILY_HISTORY("", R.string.family_history);

    private final String data;
    private final int actionbarId;

    HistoryFilterType(String data, int actionbarId) {
        this.data = data;
        this.actionbarId = actionbarId;
    }

    public String getData() {
        return data;
    }

    public int getActionbarId() {
        return actionbarId;
    }
}
