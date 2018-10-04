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

    APPOINTMENT_STATUS_TYPE(new ArrayList<Object>() {{
        add(CalendarStatus.SCHEDULED.getValue());
        add(CalendarStatus.WAITING.getValue());
        add(CalendarStatus.ENGAGED.getValue());
        add(CalendarStatus.CHECKED_OUT.getValue());
    }}),

    DOCTOR_LIST,

    DOCTOR_CLINIC_PROFILE,

    SERVING_TYPE,

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
    }}),

    QUANTITY_TYPE(new ArrayList<Object>() {
        {
            add("0.25");
            add("0.5");
            add("0.75");
            add("1");
            add("1.5");
            add("2");
            add("2.5");
            add("3");
            add("4");
            add("5");
            add("6");
            add("7");
            add("8");
            add("9");
            add("10");
            add("15");
            add("20");
            add("25");
            add("30");
            add("35");
            add("40");
            add("45");
            add("50");
            add("60");
            add("70");
            add("80");
            add("90");
            add("100");
            add("150");
            add("200");
            add("250");
            add("300");
            add("350");
            add("400");
            add("450");
            add("500");
            add("600");
            add("700");
            add("800");
            add("900");
            add("1000");
            add("1500");
            add("2000");
        }
    });

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
