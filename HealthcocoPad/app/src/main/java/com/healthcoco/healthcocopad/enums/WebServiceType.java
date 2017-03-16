package com.healthcoco.healthcocopad.enums;

import com.android.volley.Request.Method;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;

/**
 * Created by Shreshtha on 20-01-2017.
 */
public enum WebServiceType {
    FRAGMENT_INITIALISATION(0, null),
    GET_PATIENT_VISIT_DETAIL(0, null),
    LOCAL_STRING_SAVE(0, null),
    ADD_PRESCRIPTION(Method.POST, "prescription/prescriptionHandheld/add/"),

    GET_PRESCRIPTION(Method.GET, "prescription/"),
    GET_REPORTS_UPDATED(Method.GET, "records/"),
    GET_CLINICAL_NOTES(Method.GET, "clinicalNotes/"),
    GET_APPOINTMENT(Method.GET, "appointment/"),
//GET /v1/records/{recordId}/view  GET_RECORDS_BY_ID

    GET_RECORD_BY_RECORD_ID(Method.GET, "records/"),

    GET_CONTACTS(Method.GET,
            "contacts/handheld/?discarded=true"),

    LOGIN(Method.POST, "login/user?"),

    GET_DRUG_UNIT(Method.GET, "prescription/DRUGSTRENGTHUNIT/BOTH/?discarded=false&doctorId="),

    GET_DRUG_TYPE(Method.GET, "prescription/DRUGTYPE/BOTH/?"),

    ADD_DRUG(Method.POST, "prescription/drug/add/"),

    GET_DRUGS_LIST(Method.GET, "prescription/DRUG_CUSTOM/BOTH/?discarded=false&doctorId="),
    GET_DRUGS_LIST_CUSTOM(Method.GET, "prescription/DRUGS/CUSTOM/?"),
    GET_DRUGS_LIST_SOLR(Method.GET, "solr/prescription/searchDrug/BOTH/?"),

    GET_DRUG_DOSAGE(Method.GET, "prescription/DRUGDOSAGE/BOTH/?"),

    GET_DURATION_UNIT(Method.GET,
            "prescription/DRUGDURATIONUNIT/BOTH/?"),

    GET_DIRECTION(Method.GET,
            "prescription/DRUGDIRECTION/BOTH/?"),

    ADD_TEMPLATE(Method.POST, "prescription/templateHandheld/add/"),
    TEMP_REGISTER_CUSTOMER(Method.POST, "api/v1/user/register"),

    GET_TEMPLATES_LIST(Method.GET, "prescription/templates/?"),

    ADD_RECORD(Method.POST, "records/add/"),
    ADD_RECORD_MULTIPART(Method.POST, "records/add/"),

//    GET_HISTORY_LIST(Method.GET, "history/getPatientHistory/"),

    GET_HISTORY_LIST(Method.GET, "history/"),

    GET_GROUPS(Method.GET, "contacts/groups/"),

    ASSIGN_GROUP(Method.POST, "contacts/patient/addgroup/"),

    ADD_NEW_GROUP(Method.POST, "contacts/group/add/"),

    UPDATE_GROUP(Method.PUT, "contacts/group/"),

    DELETE_GROUP(Method.DELETE, "contacts/group/"),

    UPDATE_TEMPLATE(Method.PUT, "prescription/template/"),

    DELETE_TEMPLATE(Method.DELETE, "prescription/template/"),

    DISCARD_REPORT(Method.DELETE, "records/"),

    ADD_CLINICAL_NOTES(Method.POST, "clinicalNotes/add/"),

    DISCARD_CLINICAL_NOTES(Method.DELETE, "clinicalNotes/"),

    UPDATE_CLINICAL_NOTE(Method.PUT, "clinicalNotes/"),

    GET_DIAGRAMS_LIST(Method.GET, "clinicalNotes/DIAGRAMS/GLOBAL/?"),

    ADD_DIAGRAM(Method.POST, "clinicalNotes/diagram/add/"),

    ADD_TO_HISTORY_PRESCRIPTION(Method.GET, "history/prescription/"),

    ADD_TO_HISTORY_REPORT(Method.GET, "history/report/"),

    ADD_TO_HISTORY_CLINICAL_NOTE(Method.GET, "history/clinicalNotes/"),

    REMOVE_HISTORY_CLINICAL_NOTE(Method.GET, "history/removeClinicalNotes/"),

    REMOVE_HISTORY_PRESCRIPTION(Method.GET, "history/removePrescription/"),

    REMOVE_HISTORY_REPORT(Method.GET, "history/removeReports/"),

    DISCARD_PRESCRIPTION(Method.DELETE, "prescription/"),

    GET_COMPLAINT_SUGGESTIONS(Method.GET, "clinicalNotes/COMPLAINTS/BOTH/" + HealthCocoConstants.PARAM_DISCARDED_TRUE),

    GET_OBSERVATION_SUGGESTIONS(Method.GET, "clinicalNotes/OBSERVATIONS/BOTH/" + HealthCocoConstants.PARAM_DISCARDED_TRUE),

    GET_INVESTIGATION_SUGGESTIONS(Method.GET, "clinicalNotes/INVESTIGATIONS/BOTH/" + HealthCocoConstants.PARAM_DISCARDED_TRUE),

    GET_DIAGNOSIS_SUGGESTIONS(Method.GET, "clinicalNotes/DIAGNOSIS/BOTH/" + HealthCocoConstants.PARAM_DISCARDED_TRUE),

    GET_DISEASE_LIST(Method.GET, "history/diseases/BOTH/"),

    GET_MEDICAL_AND_FAMILY_HISTORY(Method.GET, "history/getMedicalAndFamilyHistory/"),

    ADD_FAMILY_HISTORY(Method.POST, "history/familyHistory/"),

    ADD_MEDICAL_HISTORY(Method.POST, "history/medicalHistory/"),

    ADD_DISEASE(Method.POST, "history/disease/add/"),

    // GET http://example.com/api/v1/clinicalNotes/clinicalNotesId/doctorId/locationId/hospitalId/emailAddress/mail/
    SEND_EMAIL_CLINICAL_NOTES(Method.GET, "clinicalNotes/"),

    // GET http://example.com/api/v1/records/recordId/doctorId/locationId/hospitalId/emailAddress/mail/
    SEND_EMAIL_REPORTS(Method.GET, "records/"),

    // GET http://example.com/api/v1/prescription/prescriptionId/doctorId/locationId/hospitalId/emailAddress/mail/
    SEND_EMAIL_PRESCRIPTION(Method.GET, "prescription/"),

    // GET http://example.com/api/v1/patientVisit/email/visitId/emailAddress/
    SEND_EMAIL_VISIT(Method.GET, "patientVisit/email/"),

    //  GET http://example.com/api/v1/prescription/prescriptionId/doctorId/locationId/hospitalId/mobileNumber/sms/
    SEND_SMS_PRESCRIPTION(Method.GET, "prescription/"),

    // GET http://example.com/api/v1/patientVisit/visitId/doctorId/locationId/hospitalId/mobileNumber/sms/
    SEND_SMS_VISIT(Method.GET, "patientVisit/"),

    //    GET http://example.com/api/v1/patientVisit/doctorId/locationId/hospitalId/patientId/
    GET_PATIENT_VISIT(Method.GET, "patientVisit/"),

    UPDATE_PRESCRIPTION(Method.PUT, "prescription/"),

    SIGN_UP(Method.POST, "signup/doctorHandheld/"),

    SIGN_UP_CONTINUE_VERIFICATION(Method.POST, "signup/doctorHandheldContinue/"),

    RESET_PASSWORD(Method.POST, "forgotPassword/forgotPasswordDoctor/"),

    GET_SPECIALITIES(Method.GET, "doctorProfile/getSpecialities/?"),

    REGISTER_PATIENT(Method.POST, "register/patient/"),

    UPDATE_PATIENT(Method.PUT, "register/patient/"),

    SEARCH_PATIENTS(Method.GET, "register/existing_patients/"),

    GET_CITIES(Method.GET, "appointment/cities/?"),

    GET_PATIENT_PROFILE(Method.GET, "register/getpatientprofile/"),

    GET_REFERENCE(Method.GET, "register/reference/BOTH/?"),

    ADD_REFERENCE(Method.POST, "register/referrence/add/"),

    UPDATE_REFERENCE(Method.PUT, "register/referrence/add/"),

    DELETE_REFERENCE(Method.DELETE, "register/referrence/"),

    GET_BLOOD_GROUP(Method.GET, "register/settings/bloodGroup/"),

    GET_PROFESSION(Method.GET, "register/settings/profession/"),

    //    DELETEhttp://example.com/api/v1/prescription/drugDirection/drugDirectionId/delete/
    DELETE_DIRECTION(Method.DELETE, "prescription/drugDirection/"),

    //    DELETEhttp://example.com/api/v1/prescription/drug/drugId/delete/
    DELETE_DRUG(Method.DELETE, "prescription/drug/"),

    //    DELETEhttp://example.com/api/v1/prescription/drugDosage/drugDosageId/delete/
    DELETE_DRUG_DOSAGE(Method.DELETE, "prescription/drugDosage/"),


    DELETE_DISEASE(Method.DELETE, "history/disease/"),

    ADD_DIRECTION(Method.POST, "prescription/drugDirection/add/"),

    ADD_DOSAGE(Method.POST, "prescription/drugDosage/add/"),

    ADD_CUSTOM_HISTORY(Method.POST, "history/disease/add/"),

    ADD_FEEDBACK(Method.POST, "register/feedback/add/"),

    GET_CLINIC_PROFILE(Method.GET, "appointment/clinic/"),

    ADD_UPDATE_CLINIC_ADDRESS(Method.POST, "register/settings/updateClinicAddress/"),

    ADD_UPDATE_CLINIC_CONTACT(Method.POST, "register/settings/updateClinicProfileHandheld/"),

    ADD_UPDATE_CLINIC_HOURS(Method.POST, "register/settings/updateClinicTiming"),

    GET_EDUCATION_QUALIFICATION(Method.GET, "doctorProfile/getEducationQualifications/"),

    GET_EDUCATION_QUALIFICATION_SOLR(Method.GET, "solr/master/educationQualification/?"),

    GET_COLLEGE_UNIVERSITY_INSTITUES(Method.GET, "doctorProfile/getEducationInstitutes/"),

    GET_COLLEGE_UNIVERSITY_INSTITUES_SOLR(Method.GET, "solr/master/educationInstitute/?"),

    GET_MEDICAL_COUNCILS(Method.GET, "doctorProfile/getMedicalCouncils/"),

    GET_MEDICAL_COUNCILS_SOLR(Method.GET, "solr/master/medicalCouncil/?"),

    ADD_UPDATE_EDUCATION(Method.POST, "doctorProfile/addEditEducation/"),

    ADD_UPDATE_REGISTRATION_DETAIL(Method.POST, "doctorProfile/addEditRegistrationDetail/"),

    GET_DOCTOR_PROFILE(Method.GET, "doctorProfile/"),

    UPDATE_DOCTOR_PROFILE(Method.POST, "doctorProfile/addEditMultipleData/"),

    ADD_UPDATE_DOCTOR_CLINIC_HOURS(Method.POST, "doctorProfile/clinicProfile/addEditVisitingTime/"),

    ADD_UPDATE_GENERAL_INFO(Method.POST, "doctorProfile/clinicProfile/addEditGeneralInfo/"),

    //    POSThttp://example.com/api/v1/doctorProfile/addEditContact/
    ADD_UPDATE_DOCTOR_CONTACT(Method.POST, "doctorProfile/addEditContact/"),

    ADD_CLINIC_IMAGE(Method.POST, "register/settings/clinicImage/add/"),

    //    POSThttp://example.com/api/v1/register/settings/changeClinicLogo/
    ADD_CLINIC_LOGO(Method.POST, "register/settings/changeClinicLogo/"),

    // DELETEhttp://example.com/api/v1/register/settings/clinicImage/locationId/counter/delete/
    DELETE_CLINIC_IMAGE(Method.DELETE, "register/settings/clinicImage/"),

    //    GET http://example.com/api/v1/register/patientStatus/patientId/doctorId/locationId/hospitalId/
    GET_PATIENT_STATUS(Method.GET, "register/patientStatus/"),

    //    GET http://example.com/api/v1/otp/doctorId/locationId/hospitalId/patientId/generate/
    GENERATE_OTP(Method.GET, "otp/"),

    //    GET http://example.com/api/v1/otp/doctorId/locationId/hospitalId/patientId/otpNumber/verify/
    VERIFY_OTP(Method.GET, "otp/"),
    GET_DIAGNOSTIC_TESTS_SOLR(Method.GET, "solr/prescription/searchDiagnosticTest/BOTH/?"),
    ADD_DIAGNOSTIC_TESTS(Method.POST, "prescription/diagnosticTest/addEdit/?"),

    //    GET http://example.com/v1/appointment/getTimeSlots/doctorId/locationId/date/
    GET_APPOINTMENT_TIME_SLOTS(Method.GET, "appointment/getTimeSlots/"),
    GET_CALENDAR_EVENTS(Method.GET, "appointment"),
    ADD_EVENT(Method.POST, "appointment/event/"),
    ADD_APPOINTMENT(Method.POST, "appointment/"),
    SEND_REMINDER(Method.GET, "appointment/sendReminder/patient/"),


    //    post /v1/notification/device/add
    SEND_GCM_REGISTRATION_ID(Method.POST, "notification/device/add"),

    //    POST /v1/signup/submitDoctorContact
    DOCTOR_CONTACT_US(Method.POST, "signup/submitDoctorContact"),

    //    POST /v1/version/check
    VERSION_CONTROL_CHECK(Method.POST, "version/check"),

    GET_PRESCRIPTION_PDF_URL(Method.GET, "prescription/download/"),
    GET_REPORT_PDF_URL(Method.GET, "records/download/"),
    GET_CLINICAL_NOTES_PDF_URL(Method.GET, "clinicalNotes/download/"),
    ADD_VISIT(Method.POST, "patientVisit/add/"),
    GET_VISIT_PDF_URL(Method.GET, "patientVisit/download/"),
    ADD_DOCTOR(Method.POST, "register/user/"),
    //    records/{recordId}/{recordsState}/changeState
    CHANGE_RECORD_STATE(Method.GET, "records/"),

    ADD_UPDATE_ACHIEVEMENTS_DETAIL(Method.POST, "doctorProfile/addEditAchievement/"),

    ADD_UPDATE_EXPERIENCE_DETAIL(Method.POST, "doctorProfile/addEditExperienceDetail/"),

    ADD_UPDATE_PROFESSIONAL_MEMBERSHIP_DETAIL(Method.POST, "doctorProfile/addEditProfessionalMembership/"),

    ADD_UPDATE_PROFESSIONAL_STATEMENT_DETAIL(Method.POST, "doctorProfile/addEditProfessionalStatement/"),

    GET_PROFESSIONAL_MEMBERSHIP_SOLR(Method.GET, "solr/master/professionalMembership/?"),

    GET_UI_PERMISSIONS_FOR_DOCTOR(Method.GET, "dynamicUI/getPermissionsForDoctor/"),

    GET_ALL_UI_PERMISSIONS(Method.GET, "dynamicUI/getAllPermissionsForDoctor/"),

    POST_UI_PERMISSIONS(Method.POST, "dynamicUI/postPermissions/"),
    ADD_PATIENT_TO_QUEUE(Method.POST, "appointment/queue/add");

    private int methodType;
    private String url;

    private WebServiceType(int methodType, String url) {
        this.methodType = methodType;
        this.url = url;
    }

    public int getMethodType() {
        return methodType;
    }

    public String getUrl() {
        return url;
    }
}
