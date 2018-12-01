package com.healthcoco.healthcocopad.enums;


import android.view.View;

import com.healthcoco.healthcocopad.R;

import org.parceler.Parcel;

/**
 * Created by neha on 14/10/16.
 */
public enum PatientDetailTabType {
    PATIENT_DETAIL_PROFILE(0, R.string.profile, R.string.patient_profile, R.drawable.selector_ic_profile_tab, View.GONE, View.GONE, "PATIENT_PROFILE"),
    PATIENT_DETAIL_VISIT(1, R.string.visits, R.string.visits, R.drawable.selector_ic_visit_tab, View.VISIBLE, View.VISIBLE, "VISIT"),
    PATIENT_DETAIL_CLINICAL_NOTES(2, R.string.clinical_notes, R.string.clinical_notes, R.drawable.selector_ic_clinical_notes_tab, View.VISIBLE, View.VISIBLE, "CLINICAL_NOTES"),
    //    PATIENT_DETAIL_IMPORTANT(3, R.string.important,R.string.important, R.drawable.selector_ic_important_tab, View.GONE,View.VISIBLE),
    PATIENT_DETAIL_ASSESSMENT(3, R.string.assessment, R.string.assessment, R.drawable.selector_ic_receipt_tab, View.VISIBLE, View.VISIBLE, "ASSESSMENT"),
    PATIENT_DETAIL_REPORTS(4, R.string.reports, R.string.reports, R.drawable.selector_ic_reports_tab, View.VISIBLE, View.VISIBLE, "REPORTS"),
    PATIENT_DETAIL_PRESCRIPTION(5, R.string.prescriptions, R.string.prescriptions, R.drawable.selector_ic_prescription_tab, View.VISIBLE, View.VISIBLE, "PRESCRIPTION"),
    PATIENT_DETAIL_APPOINTMENT(6, R.string.appointment, R.string.appointment, R.drawable.selector_ic_apoointment_tab, View.VISIBLE, View.VISIBLE, "APPOINTMENT"),
    PATIENT_DETAIL_TREATMENT(7, R.string.treatment, R.string.treatment, R.drawable.selector_ic_treatment_tab, View.VISIBLE, View.VISIBLE, "TREATMENT"),
    PATIENT_DETAIL_INVOICE(8, R.string.invoice, R.string.invoice, R.drawable.selector_ic_invoice_tab, View.VISIBLE, View.VISIBLE, "BILLING"),
    PATIENT_DETAIL_RECEIPT(9, R.string.receipt, R.string.receipt, R.drawable.selector_ic_receipt_tab, View.VISIBLE, View.VISIBLE, "BILLING"),
    PATIENT_DETAIL_DIET_PLAN(10, R.string.diet_plan, R.string.diet_plan, R.drawable.selector_ic_clinical_notes_tab, View.VISIBLE, View.VISIBLE, "PATIENT_DETAIL_DIET_PLAN");
    //        PATIENT_DETAIL_BILLING(7, R.string.billing, R.drawable.selector_ic_treatment_tab, new PatientTreatmentDetailFragment());
    private final int textId;
    private final int drawableId;
    private final String value;
    private final int tabPosition;
    private final int actionBarTitleId;
    private final int floatingButtonVisibility;
    private final int patientDetailHeaderVisibility;


    PatientDetailTabType(int tabPosition, int textId, int actionBarTitleId, int drawableId, int floatingButtonVisibility, int patientDetailHeaderVisibility, String value) {
        this.textId = textId;
        this.drawableId = drawableId;
        this.tabPosition = tabPosition;
        this.actionBarTitleId = actionBarTitleId;
        this.floatingButtonVisibility = floatingButtonVisibility;
        this.patientDetailHeaderVisibility = patientDetailHeaderVisibility;
        this.value = value;
    }

    public int getPatientDetailHeaderVisibility() {
        return patientDetailHeaderVisibility;
    }

    public int getFloatingButtonVisibility() {
        return floatingButtonVisibility;
    }

    public int getDrawableId() {
        return drawableId;
    }

    public int getTextId() {
        return textId;
    }

    public int getTabPosition() {
        return tabPosition;
    }

    public int getActionBarTitleId() {
        return actionBarTitleId;
    }

    public String getValue() {
        return value;
    }

}
