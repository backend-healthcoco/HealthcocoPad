package com.healthcoco.healthcocopad.enums;

import com.healthcoco.healthcocopad.R;

/**
 * Created by Shreshtha on 23-01-2017.
 */
public enum CommonOpenUpFragmentType {
    LOGIN_SIGN_UP(0),
    CONTINUE_SIGN_UP(0),
    PATIENT_DETAIL(0),
    CONTACTS_DETAIL(0),
    TERMS_OF_SERVICE(R.string.terms_of_service),
    PRIVACY_POLICY(R.string.privacy_policy),
    INITIAL_SYNC(0), FEEDBACK(R.string.help_us_to_improve),
    ADD_EDIT_CLINIC_HOURS(R.string.visiting_hours), ADD_EDIT_CLINIC_CONTACT(R.string.clinic_contact), ADD_EDIT_AWARDS_AND_PUBLICATION(R.string.awards_and_publication),
    ADD_EDIT_CLINIC_IMAGE(R.string.clinic_photos),
    GROUPS(R.string.groups),
    ADD_EDIT_EDUCATION_DETAIL(R.string.education),
    ADD_EDIT_REGISTRATION_DETAIL(R.string.registration_details),
    ADD_EDIT_DOCTOR_PROFILE_CONTACT_DETAIL(R.string.contact_details),
    ADD_EDIT_DOCTOR_PROFILE_CLINIC_HOURS(R.string.clinic_hours),
    ADD_EDIT_DOCTOR_APPOINTMENT_DETAIL(R.string.appointment_details),
    ADD_EDIT_DOCTOR_PROFILE_DETAILS(R.string.profile), NOTES(R.string.notes),
    ADD_EDIT_EXPERIENCE_DETAIL(R.string.experience), ADD_EDIT_PROFESSIONAL_MEMBERSHIP_DETAIL(R.string.professional_membership),
    ADD_EDIT_PROFESSSIONAL_STATEMENT_DETAIL(R.string.professional_statement), ADD_EDIT_CLINIC_ADDRESS(R.string.address_details), SETTING_UI_PERMISSION(R.string.ui_permission),
    SETTINGS_PATIENT(R.string.patient), SETTINGS_CLINICAL_NOTES(R.string.clinical_notes),
    SETTINGS_HISTORY(R.string.history_small), SETTINGS_PRESCRIPTION(R.string.prescriptions), SETTINGS_TEMPLATE(R.string.templates),
    SETTINGS_BILLING(R.string.billing), SETTING_SMS(R.string.sms), SETTING_ABOUT_US(R.string.about_us),
    SETTINGS_UI_PERMISSION_PRESCRIPTION(R.string.prescription_ui_permission_details),
    SETTINGS_UI_PERMISSION_CLINICAL_NOTES(R.string.clinical_notes_ui_permission_details),
    PATIENT_NUMBER_SEARCH(R.string.patient_mobile_number),
    SETTINGS_UI_PERMISSION_VISITS(R.string.visits_ui_permission_details),
    ADD_NEW_PRESCRIPTION(R.string.new_prescription),
    SETTINGS_UI_PERMISSION_PATIENT_TAB(R.string.patient_tab_ui_permission_details),
    PATIENT_REGISTRATION(R.string.new_contact),
    //For Patient Detail
    PATIENT_DETAIL_PROFILE(R.string.profile),
    PATIENT_DETAIL_VISIT(R.string.visits),
    PATIENT_DETAIL_CLINICAL_NOTES(R.string.clinical_notes),
    PATIENT_DETAIL_IMPORTANT(R.string.important),
    PATIENT_DETAIL_REPORTS(R.string.reports),
    PATIENT_DETAIL_PRESCRIPTION(R.string.prescriptions),
    PATIENT_DETAIL_APPOINTMENT(R.string.appointment),
    PATIENT_DETAIL_TREATMENT(R.string.treatment),
    HISTORY_DISEASE_LIST(R.string.past_history),
    ADD_NEW_TEMPLATE(R.string.new_template),
    NOTIFICATION_RESPONSE_DATA(R.string.notification_details),
    ADD_VISITS(0),
    SELECT_DIAGRAM(R.string.diagrams),
    SELECTED_DIAGRAM_DETAIL(0), BOOK_APPOINTMENT(R.string.book),
    CONTACTS_LIST(R.string.my_patients),
    ENLARGED_MAP_VIEW_FRAGMENT(R.string.location),
    ADD_CLINICAL_NOTE(R.string.edit_clinical_note),;

    private final int titleId;


    CommonOpenUpFragmentType(int titleId) {
        this.titleId = titleId;
    }

    public int getTitleId() {
        return titleId;
    }
}

