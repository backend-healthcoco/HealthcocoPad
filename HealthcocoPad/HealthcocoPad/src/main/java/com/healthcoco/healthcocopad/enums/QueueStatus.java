package com.healthcoco.healthcocopad.enums;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 01-Mar-18.
 */

public enum QueueStatus {

    SCHEDULED("SCHEDULED"),
    WAITING("WAITING"),
    ENGAGED("ENGAGED"),
    CHECKED_OUT("CHECKED_OUT");

    private String value;

    QueueStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
