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
    VACCINE_FILTER(new ArrayList<Object>() {
        {
            add(VaccineDuration.BIRTH);
            add(VaccineDuration.WEEKS_6);
            add(VaccineDuration.WEEKS_10);
            add(VaccineDuration.MONTHS_14);
            add(VaccineDuration.MONTHS_6);
            add(VaccineDuration.MONTHS_7);
            add(VaccineDuration.MONTHS_9);
            add(VaccineDuration.MONTHS_9_TO_12);
            add(VaccineDuration.MONTHS_12);
            add(VaccineDuration.MONTHS_15);
            add(VaccineDuration.MONTHS_16_TO_18);
            add(VaccineDuration.MONTH_18);
            add(VaccineDuration.YEARS_2);
            add(VaccineDuration.YEARS_4_To_6);
            add(VaccineDuration.YEARS_10_To_12);
            add(VaccineDuration.CUSTOM);
        }
    }),
    VACCINE_STATUS(new ArrayList<Object>() {
        {
            add(VaccineStatus.GIVEN);
            add(VaccineStatus.PLANNED);
        }
    }),BRAND_TYPE,
    ACHIEVEMENT_DURATION(new ArrayList<Object>() {{
        add("DAY(S)");
        add("MONTH(S)");
        add("WEEK(S)");
        add("YEAR(S)");
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
