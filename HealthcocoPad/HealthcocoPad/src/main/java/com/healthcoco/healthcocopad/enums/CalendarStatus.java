package com.healthcoco.healthcocopad.enums;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 01-Mar-18.
 */

public enum CalendarStatus {
    SCHEDULED("SCHEDULED", new ArrayList<Object>() {
        {
            add("WAITING");
            add("ENGAGED");
            add("CHECKED_OUT");
        }
    }), WAITING("WAITING", new ArrayList<Object>() {
        {
            add("SCHEDULED");
            add("ENGAGED");
            add("CHECKED_OUT");
        }
    }), ENGAGED("ENGAGED", new ArrayList<Object>() {
        {
            add("SCHEDULED");
            add("WAITING");
            add("CHECKED_OUT");
        }
    }), CHECKED_OUT("CHECKED_OUT", new ArrayList<Object>() {
        {
            add("SCHEDULED");
            add("WAITING");
            add("ENGAGED");
        }
    });
    private final ArrayList<Object> list;
    private String value;

    CalendarStatus(String value, ArrayList<Object> list) {
        this.list = list;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public ArrayList<Object> getList() {
        return list;
    }
}
