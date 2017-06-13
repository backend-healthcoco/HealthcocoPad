package com.healthcoco.healthcocopad.enums;

import java.util.ArrayList;

/**
 * Created by neha on 03/05/17.
 */

public enum PopupWindowType {
    TIME_SLOTS;
    private final ArrayList<Object> list;

    PopupWindowType(ArrayList<Object> list) {
        this.list = list;
    }

    PopupWindowType() {
        this.list = null;
    }

    public ArrayList<Object> getList() {
        return list;
    }
}
