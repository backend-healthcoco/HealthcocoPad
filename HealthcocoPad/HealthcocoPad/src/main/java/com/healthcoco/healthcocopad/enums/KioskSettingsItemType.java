package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 14/01/16.
 */
public enum KioskSettingsItemType {

    ADD_VIDEO(R.string.add_patient_video, 0, 0),
    CHANGE_PIN(R.string.change_pin, 0, 0);

    private final int searchHintId;
    private final int titleId;
    private int noDataId;

    KioskSettingsItemType(int titleId, int searchHintId, int noDataId) {
        this.titleId = titleId;
        this.searchHintId = searchHintId;
        this.noDataId = noDataId;
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

}
