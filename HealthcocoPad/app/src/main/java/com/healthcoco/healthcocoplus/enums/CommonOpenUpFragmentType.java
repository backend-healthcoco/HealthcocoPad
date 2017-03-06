package com.healthcoco.healthcocoplus.enums;

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
    PRIVACY_POLICY(R.string.privacy_policy), INITIAL_SYNC(0), FEEDBACK(R.string.help_us_to_improve),
    ADD_EDIT_CLINIC_HOURS(R.string.visiting_hours), ADD_EDIT_CLINIC_CONTACT(R.string.clinic_contact), ADD_EDIT_AWARDS_AND_PUBLICATION(R.string.awards_and_publication),
    ADD_EDIT_CLINIC_IMAGE(R.string.clinic_photos),
    GROUPS(R.string.groups),
    ADD_EDIT_EDUCATION_DETAIL(R.string.education), ADD_EDIT_REGISTRATION_DETAIL(R.string.registration_details), ADD_EDIT_DOCTOR_PROFILE_CONTACT_DETAIL(R.string.contact_details),
    ADD_EDIT_DOCTOR_PROFILE_CLINIC_HOURS(R.string.clinic_hours), ADD_EDIT_DOCTOR_APPOINTMENT_DETAIL(R.string.appointment_details),
    ADD_EDIT_DOCTOR_PROFILE_DETAILS(R.string.profile), NOTES(R.string.notes), ADD_EDIT_EXPERIENCE_DETAIL(R.string.experience), ADD_EDIT_PROFESSIONAL_MEMBERSHIP_DETAIL(R.string.professional_membership),
    ADD_EDIT_PROFESSSIONAL_STATEMENT_DETAIL(R.string.professional_statement), ADD_EDIT_CLINIC_ADDRESS(R.string.address_details), SETTING_UI_PERMISSION(R.string.ui_permission),
    SETTINGS_PATIENT(R.string.patient), SETTINGS_CLINICAL_NOTES(R.string.clinical_notes),
    SETTINGS_HISTORY(R.string.history_small), SETTINGS_PRESCRIPTION(R.string.prescriptions), SETTINGS_TEMPLATE(R.string.templates),
    SETTINGS_BILLING(R.string.billing), SETTING_SMS(R.string.sms), SETTING_ABOUT_US(R.string.about_us),
    SETTINGS_UI_PERMISSION_PRESCRIPTION(R.string.prescription_ui_permission_details),
    SETTINGS_UI_PERMISSION_CLINICAL_NOTES(R.string.clinical_notes_ui_permission_details),
    PATIENT_NUMBER_SEARCH(R.string.patient_mobile_number),
    SETTINGS_UI_PERMISSION_VISITS(R.string.visits_ui_permission_details), ADD_NEW_PRESCRIPTION(R.string.new_prescription),
    SETTINGS_UI_PERMISSION_PATIENT_TAB(R.string.patient_tab_ui_permission_details),
    PATIENT_REGISTRATION(R.string.new_contact);

    private final int titleId;

    CommonOpenUpFragmentType(int titleId) {
        this.titleId = titleId;
    }

    public int getTitleId() {
        return titleId;
    }
}

