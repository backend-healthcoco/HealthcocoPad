package com.healthcoco.healthcocopad.enums;

import java.util.ArrayList;

/**
 * Created by neha on 03/05/17.
 */

public enum PopupWindowType {
    TIME_SLOTS,

    STATUS_TYPE(new ArrayList<Object>() {{
        add(PatientTreatmentStatus.NOT_STARTED);
        add(PatientTreatmentStatus.IN_PROGRESS);
        add(PatientTreatmentStatus.COMPLETED);
    }}),

    DOCTOR_LIST,

    DISCOUNT_TYPE(new ArrayList<Object>() {{
        add(UnitType.PERCENT);
        add(UnitType.INR);
    }}),

    MATERIAL_TYPE(new ArrayList<Object>() {{
        add("Ceramic-full ceramic/pfm");
        add("Ni Cr");
        add("Gold");
        add("Arylic");
        add("Ceramic-facing");
        add("Full Ceramic metal free");
        add("Zirconia");
    }});

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
