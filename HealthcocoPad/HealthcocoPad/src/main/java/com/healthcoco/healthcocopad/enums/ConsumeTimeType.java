package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

public enum ConsumeTimeType {

    DAILY("DAILY", R.string.daily),
    WEEKLY("WEEKLY", R.string.weekly),
    MONTHLY("MONTHLY", R.string.monthly),
    YEARLY("YEARLY", R.string.yearly);

    private String timeType;
    private int timeTitle;

    ConsumeTimeType(String timeType, int timeTitle) {
        this.timeType = timeType;
        this.timeTitle = timeTitle;
    }

    public String getTimeType() {
        return timeType;
    }

    public int getTimeTitle() {
        return timeTitle;
    }
}
