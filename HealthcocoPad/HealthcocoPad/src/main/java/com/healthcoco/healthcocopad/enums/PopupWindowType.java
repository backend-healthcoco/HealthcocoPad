package com.healthcoco.healthcocopad.enums;

import java.util.ArrayList;

/**
 * Created by neha on 03/05/17.
 */

public enum PopupWindowType {
    TIME_SLOTS,

    PAYMENT_TYPE(new ArrayList<Object>() {{
        add(ModeOfPaymentType.CASH.getType());
        add(ModeOfPaymentType.CARD.getType());
        add(ModeOfPaymentType.WALLET.getType());
    }}),

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

    APPOINTMENT_SLOT(new ArrayList<Object>() {{
        add(AppointmentSlotsType.SLOT1);
        add(AppointmentSlotsType.SLOT2);
        add(AppointmentSlotsType.SLOT3);
        add(AppointmentSlotsType.SLOT4);
        add(AppointmentSlotsType.SLOT5);
        add(AppointmentSlotsType.SLOT6);
        add(AppointmentSlotsType.SLOT7);
        add(AppointmentSlotsType.SLOT8);
        add(AppointmentSlotsType.SLOT9);
        add(AppointmentSlotsType.SLOT10);
        add(AppointmentSlotsType.SLOT11);
        add(AppointmentSlotsType.SLOT12);
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
