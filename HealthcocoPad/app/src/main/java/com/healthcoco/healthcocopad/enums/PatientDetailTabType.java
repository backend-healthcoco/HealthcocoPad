package com.healthcoco.healthcocopad.enums;


import com.healthcoco.healthcocopad.R;

/**
 * Created by neha on 14/10/16.
 */
public enum PatientDetailTabType {
    PATIENT_DETAIL_PROFILE(0, R.string.profile, R.drawable.selector_ic_profile_tab),
    PATIENT_DETAIL_VISIT(1, R.string.visits, R.drawable.selector_ic_visit_tab),
    PATIENT_DETAIL_CLINICAL_NOTES(2, R.string.clinical_notes, R.drawable.selector_ic_clinical_notes_tab),
    PATIENT_DETAIL_IMPORTANT(3, R.string.important, R.drawable.selector_ic_important_tab),
    PATIENT_DETAIL_REPORTS(4, R.string.reports, R.drawable.selector_ic_reports_tab),
    PATIENT_DETAIL_PRESCRIPTION(5, R.string.prescriptions, R.drawable.selector_ic_prescription_tab),
    PATIENT_DETAIL_APPOINTMENT(6, R.string.appointment, R.drawable.selector_ic_apoointment_tab),
    PATIENT_DETAIL_TREATMENT(7, R.string.treatment, R.drawable.selector_ic_treatment_tab);
//    PATIENT_DETAIL_BILLING(7, R.string.billing, R.drawable.selector_ic_treatment_tab, new PatientTreatmentDetailFragment());

    public static final int POSITION_PROFILE_TAB = 0;
    public static final int POSITION_VISIT_TAB = 1;
    public static final int POSITION_CLINICAL_NOTES_TAB = 2;
    public static final int POSITION_IMPORTANT_TAB = 3;
    public static final int POSITION_REPORTS_TAB = 4;
    public static final int POSITION_PRESCRIPTION_TAB = 5;
    public static final int POSITION_APPOINTMENT_TAB = 6;
    public static final int POSITION_TREATMENT_TAB = 7;

    private final int textId;
    private final int drawableId;
    private final int tabPosition;


    PatientDetailTabType(int tabPosition, int textId, int drawableId) {
        this.textId = textId;
        this.drawableId = drawableId;
        this.tabPosition = tabPosition;
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
}
