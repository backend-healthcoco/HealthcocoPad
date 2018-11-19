package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

public enum AddictionType {

    TOBACCO("TOBACCO", R.string.tobacco),
    SMOCKING("SMOCKING", R.string.smoking),
    ALCOHOL("ALCOHOL", R.string.alcohol),
    OTHER("OTHER", R.string.others);

    private String addictionType;
    private int addictionTitle;

    AddictionType(String addictionType, int addictionTitle) {
        this.addictionType = addictionType;
        this.addictionTitle = addictionTitle;
    }

    public String getAddictionType() {
        return addictionType;
    }

    public int getAddictionTitle() {
        return addictionTitle;
    }
}
