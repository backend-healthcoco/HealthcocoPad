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
    GET_PATIENTS_COUNT(0, ""),
    ADD_PRESCRIPTION(Method.POST, "prescription/prescriptionHandheld/add/"),

    GET_TREATMENT(Method.GET, "treatment/"),

    ADD_TREATMENT(Method.POST, "treatment/add"),

    GET_TREATMENT_LIST_BOTH_SOLR(Method.GET, "solr/treatment/SERVICE/BOTH/?"),

    GET_TREATMENT_LIST_FEATURED(Method.GET, "treatment/getServicesBySpeciality"),

    GET_TREATMENT_PDF_URL(Method.GET, "treatment/download/"),

    DISCARD_TREATMENT(Method.DELETE, "treatment/"),

    SEND_EMAIL_TREATMENT(Method.GET, "treatment/"),

    ADD_TREATMENT_SERVICE(Method.POST, "treatment/service/add"),

    GET_VIDEO(Method.GET, "video/get"),

    GET_PRESCRIPTION(Method.GET, "prescription/"),
    GET_REPORTS_UPDATED(Method.GET, "records/"),
    GET_CLINICAL_NOTES(Method.GET, "clinicalNotes/"),
    GET_APPOINTMENT(Method.GET, "appointment/"),

    GET_INVOICE(Method.GET, "billing/invoice/BOTH"),

    GET_INVOICE_PDF_URL(Method.GET, "billing/downloadInvoice/"),

    ADD_INVOICE(Method.POST, "billing/invoice/add"),

    SEND_EMAIL_INVOICE(Method.GET, "billing/invoice/"),

    DISCARD_INVOICE(Method.DELETE, "billing/invoice/"),

    GET_RECEIPT(Method.GET, "billing/receipt/"),

    GET_RECEIPT_PDF_URL(Method.GET, "billing/downloadReceipt/"),

    DISCARD_RECEIPT(Method.DELETE, "billing/receipt/"),

    GET_AMOUNT(Method.GET, "billing/amount/"),

    ADD_RECEIPT(Method.POST, "billing/receipt/add"),

    SEND_EMAIL_RECEIPT(Method.GET, "billing/receipt/"),

//GET /v1/records/{recordId}/view  GET_RECORDS_BY_ID

    GET_RECORD_BY_RECORD_ID(Method.GET, "records/"),

    GET_APPOINTMENT_BY_APPOINTMENT_ID(Method.GET, "appointment/"),

    GET_CONTACTS(Method.GET,
            "contacts/handheld/?discarded=true"),

    LOGIN(Method.POST, "login/user?"),

    GET_DRUG_UNIT(Method.GET, "prescription/DRUGSTRENGTHUNIT/BOTH/?discarded=false&doctorId="),

    GET_DRUG_TYPE(Method.GET, "prescription/DRUGTYPE/BOTH/?"),

    //    ADD_DRUG(Method.POST, "prescription/drug/add/"),
    ADD_DRUG(Method.POST, "prescription/favouriteDrug/add/"),

    GET_DRUGS_LIST(Method.GET, "prescription/DRUG_CUSTOM/BOTH/?discarded=false&doctorId="),
    GET_DRUGS_LIST_CUSTOM(Method.GET, "prescription/DRUGS/CUSTOM/?"),
    GET_DRUGS_LIST_SOLR(Method.GET, "solr/prescription/searchDrug/BOTH/?"),

    GET_DRUG_DOSAGE(Method.GET, "prescription/DRUGDOSAGE/BOTH/?"),

    GET_DURATION_UNIT(Method.GET,
            "prescription/DRUGDURATIONUNIT/BOTH/?"),

    GET_DIRECTION(Method.GET,
            "prescription/DRUGDIRECTION/BOTH/?"),

    ADD_TEMPLATE(Method.POST, "prescription/template/add/"),
    TEMP_REGISTER_CUSTOMER(Method.POST, "api/v1/user/register"),

    GET_TEMPLATES_LIST(Method.GET, "prescription/templates/?"),

    ADD_RECORD(Method.POST, "records/add/"),

//    ADD_FILE_RECORDS(Method.POST, "records/upload/file"),

    ADD_RECORD_MULTIPART(Method.POST, "records/addMultipart"),

//    GET_HISTORY_LIST(Method.GET, "history/getPatientHistory/"),

    GET_DISEASE_HISTORY_LIST(Method.GET, "history/"),
    GET_HISTORY_LIST(Method.GET, "history/getHistory"),
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

    //    Clinical note Add Suggession
    ADD_COMPLAINT_SUGGESTIONS(Method.POST, "clinicalNotes/complaint/add/"),

    ADD_DIAGNOSIS_SUGGESTIONS(Method.POST, "clinicalNotes/diagnosis/add/"),

    ADD_EARS_EXAM_SUGGESTIONS(Method.POST, "clinicalNotes/earsExam/add/"),

    ADD_ECG_DETAILS_SUGGESTIONS(Method.POST, "clinicalNotes/ecgDetails/add/"),

    ADD_ECHO_SUGGESTIONS(Method.POST, "clinicalNotes/echo/add/"),

    ADD_EYE_OBSERVATION_SUGGESTIONS(Method.POST, "clinicalNotes/eyeObservation/add/"),

    ADD_GENERAL_EXAM_SUGGESTIONS(Method.POST, "clinicalNotes/generalExam/add/"),

    ADD_HOLTER_SUGGESTIONS(Method.POST, "clinicalNotes/holter/add/"),

    ADD_INDICATION_OF_USG_SUGGESTIONS(Method.POST, "clinicalNotes/indicationOfUSG/add/"),

    ADD_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS(Method.POST, "clinicalNotes/indirectLarygoscopyExam/add/"),

    ADD_INVESTIGATION_SUGGESTIONS(Method.POST, "clinicalNotes/investigation/add/"),

    ADD_MENSTRUAL_HISTORY_SUGGESTIONS(Method.POST, "clinicalNotes/menstrualHistory/add/"),

    ADD_NECK_EXAM_SUGGESTIONS(Method.POST, "clinicalNotes/neckExam/add/"),

    ADD_NOSE_EXAM_SUGGESTIONS(Method.POST, "clinicalNotes/noseExam/add/"),

    ADD_NOTES_SUGGESTIONS(Method.POST, "clinicalNotes/notes/add/"),

    ADD_OBSERVATION_SUGGESTIONS(Method.POST, "clinicalNotes/observation/add/"),

    ADD_OBSTETRIC_HISTORY_SUGGESTIONS(Method.POST, "clinicalNotes/obstetricHistory/add/"),

    ADD_GENERAL_EXAMINATION_SUGGESTIONS(Method.POST, "clinicalNotes/generalExam/add"),

    ADD_SYSTEMIC_EXAMINATION_SUGGESTIONS(Method.POST, "clinicalNotes/systemExam/add"),

    ADD_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS(Method.POST, "clinicalNotes/oralCavityThroatExam/add/"),

    ADD_PA_SUGGESTIONS(Method.POST, "clinicalNotes/pa/add/"),

    ADD_PC_EARS_SUGGESTIONS(Method.POST, "clinicalNotes/pcears/add/"),

    ADD_PC_NOSE_SUGGESTIONS(Method.POST, "clinicalNotes/pcnose/add/"),

    ADD_PC_ORAL_CAVITY_SUGGESTIONS(Method.POST, "clinicalNotes/pcoralCavity/add/"),

    ADD_PC_THROAT_SUGGESTIONS(Method.POST, "clinicalNotes/pcthroat/add/"),

    ADD_PRESENT_COMPLAINT_SUGGESTIONS(Method.POST, "clinicalNotes/presentComplaint/add/"),

    ADD_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS(Method.POST, "clinicalNotes/presentComplaintHistory/add/"),

    ADD_PROCEDURE_NOTE_SUGGESTIONS(Method.POST, "clinicalNotes/procedureNote/add/"),

    ADD_PROVISIONAL_DIAGNOSIS_SUGGESTIONS(Method.POST, "clinicalNotes/provisionalDiagnosis/add/"),

    ADD_PS_SUGGESTIONS(Method.POST, "clinicalNotes/ps/add/"),

    ADD_PV_SUGGESTIONS(Method.POST, "clinicalNotes/pv/add/"),

    ADD_SYSTEM_EXAM_SUGGESTIONS(Method.POST, "clinicalNotes/systemExam/add/"),

    ADD_XRAY_DETAILS_SUGGESTIONS(Method.POST, "clinicalNotes/xRayDetails/add/"),

    ADD_ADVICE_SUGGESTIONS(Method.POST, "prescription/advice/"),

    GET_PRESENT_COMPLAINT_SUGGESTIONS(Method.GET, "clinicalNotes/PRESENT_COMPLAINT/"),

    GET_COMPLAINT_SUGGESTIONS(Method.GET, "clinicalNotes/COMPLAINTS/"),

    GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS(Method.GET, "clinicalNotes/HISTORY_OF_PRESENT_COMPLAINT/"),

    GET_MENSTRUAL_HISTORY_SUGGESTIONS(Method.GET, "clinicalNotes/MENSTRUAL_HISTORY/"),

    GET_OBSTETRIC_HISTORY_SUGGESTIONS(Method.GET, "clinicalNotes/OBSTETRIC_HISTORY/"),

    GET_GENERAL_EXAMINATION_SUGGESTIONS(Method.GET, "clinicalNotes/GENERAL_EXAMINATION/"),

    GET_SYSTEMIC_EXAMINATION_SUGGESTIONS(Method.GET, "clinicalNotes/SYSTEMIC_EXAMINATION/"),

    GET_OBSERVATION_SUGGESTIONS(Method.GET, "clinicalNotes/OBSERVATIONS/"),

    GET_INVESTIGATION_SUGGESTIONS(Method.GET, "clinicalNotes/INVESTIGATIONS/"),

    GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS(Method.GET, "clinicalNotes/PROVISIONAL_DIAGNOSIS/"),

    GET_DIAGNOSIS_SUGGESTIONS(Method.GET, "clinicalNotes/DIAGNOSIS/"),

    GET_ECG_SUGGESTIONS(Method.GET, "clinicalNotes/ECG/"),

    GET_ECHO_SUGGESTIONS(Method.GET, "clinicalNotes/ECHO/"),

    GET_XRAY_SUGGESTIONS(Method.GET, "clinicalNotes/XRAY/"),

    GET_HOLTER_SUGGESTIONS(Method.GET, "clinicalNotes/HOLTER/"),

    GET_PA_SUGGESTIONS(Method.GET, "clinicalNotes/PA/"),

    GET_PS_SUGGESTIONS(Method.GET, "clinicalNotes/PS/"),

    GET_PV_SUGGESTIONS(Method.GET, "clinicalNotes/PV/"),

    GET_INDICATION_OF_USG_SUGGESTIONS(Method.GET, "clinicalNotes/INDICATION_OF_USG/"),

    GET_NOTES_SUGGESTIONS(Method.GET, "clinicalNotes/NOTES/"),

    GET_EAR_EXAM_SUGGESTIONS(Method.GET, "clinicalNotes/EARS_EXAM/"),

    GET_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS(Method.GET, "clinicalNotes/INDIRECT_LAGYROSCOPY_EXAM/"),

    GET_NECK_EXAM_SUGGESTIONS(Method.GET, "clinicalNotes/NECK_EXAM/"),

    GET_NOSE_EXAM_SUGGESTIONS(Method.GET, "clinicalNotes/NOSE_EXAM/"),

    GET_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS(Method.GET, "clinicalNotes/ORAL_CAVITY_THROAT_EXAM/"),

    GET_PC_EARS_SUGGESTIONS(Method.GET, "clinicalNotes/PC_EARS/"),

    GET_PC_NOSE_SUGGESTIONS(Method.GET, "clinicalNotes/PC_NOSE/"),

    GET_PC_ORAL_CAVITY_SUGGESTIONS(Method.GET, "clinicalNotes/PC_ORAL_CAVITY/"),

    GET_PC_THROAT_SUGGESTIONS(Method.GET, "clinicalNotes/PC_THROAT/"),

    GET_PROCEDURE_NOTE_SUGGESTIONS(Method.GET, "clinicalNotes/PROCEDURE_NOTE/"),

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

    DISCARD_PATIENT_VISIT(Method.DELETE, "patientVisit/"),

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
    DELETE_TREATMENT(Method.DELETE, "treatment/service/"),

    DELETE_COMPLAINT(Method.DELETE, "clinicalNotes/complaint/"),
    DELETE_PRESENT_COMPLAINT(Method.DELETE, "clinicalNotes/presentComplaint/"),
    DELETE_HISTORY_OF_PRESENT_COMPLAINT(Method.DELETE, "clinicalNotes/presentComplaintHistory/"),
    DELETE_MENSTRUAL_HISTORY(Method.DELETE, "clinicalNotes/menstrualHistory/"),
    DELETE_OBSTETRIC_HISTORY(Method.DELETE, "clinicalNotes/obstetricHistory/"),
    DELETE_GENERAL_EXAMINATION(Method.DELETE, "clinicalNotes/generalExam/"),
    DELETE_SYSTEMIC_EXAMINATION(Method.DELETE, "clinicalNotes/systemExam/"),
    DELETE_OBSERVATION(Method.DELETE, "clinicalNotes/observation/"),
    DELETE_INVESTIGATION(Method.DELETE, "clinicalNotes/investigation/"),
    DELETE_PROVISIONAL_DIAGNOSIS(Method.DELETE, "clinicalNotes/provisionalDiagnosis/"),
    DELETE_DIAGNOSIS(Method.DELETE, "clinicalNotes/diagnosis/"),
    DELETE_ECG(Method.DELETE, "clinicalNotes/ecgDetails/"),
    DELETE_ECHO(Method.DELETE, "clinicalNotes/echo/"),
    DELETE_XRAY(Method.DELETE, "clinicalNotes/xRayDetails/"),
    DELETE_HOLTER(Method.DELETE, "clinicalNotes/holter/"),
    DELETE_PA(Method.DELETE, "clinicalNotes/pa/"),
    DELETE_PS(Method.DELETE, "clinicalNotes/ps/"),
    DELETE_PV(Method.DELETE, "clinicalNotes/pv/"),
    DELETE_INDICATION_OF_USG(Method.DELETE, "clinicalNotes/presentIndicationOfUSG/"),
    DELETE_NOTES(Method.DELETE, "clinicalNotes/notes/"),
    DELETE_PROCEDURE_NOTE(Method.DELETE, "clinicalNotes/procedureNote/"),

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


    ADD_PRINT_SETTINGS(Method.POST, "printSettings/add/"),

    GET_PRINT_SETTINGS(Method.GET, "printSettings"),

    //    GET http://example.com/api/v1/dynamicUI/getDataPermissionForDoctor/doctorId/
    GET_DATA_PERMISSION(Method.GET, "dynamicUI/getDataPermissionForDoctor/"),

    //    GET http://example.com/api/v1/register/patientStatus/patientId/doctorId/locationId/hospitalId/
    GET_PATIENT_STATUS(Method.GET, "register/patientStatus/"),

    //    GET http://example.com/api/v1/otp/doctorId/locationId/hospitalId/patientId/generate/
    GENERATE_OTP(Method.GET, "otp/"),

    //    GET http://example.com/api/v1/otp/doctorId/locationId/hospitalId/patientId/otpNumber/verify/
    VERIFY_OTP(Method.GET, "otp/"),

    GET_DIAGNOSTIC_TESTS_SOLR(Method.GET, "solr/prescription/searchDiagnosticTest/BOTH/?"),

    GET_SEARCH_ADVICE_SOLR(Method.GET, "solr/prescription/searchAdvice/BOTH/?"),

    ADD_DIAGNOSTIC_TESTS(Method.POST, "prescription/diagnosticTest/addEdit/?"),

    //    GET http://example.com/v1/appointment/getTimeSlots/doctorId/locationId/date/
    GET_APPOINTMENT_TIME_SLOTS(Method.GET, "appointment/getTimeSlots/"),
    GET_CALENDAR_EVENTS(Method.GET, "appointment"),
    ADD_EVENT(Method.POST, "appointment/event/"),
    ADD_APPOINTMENT(Method.POST, "appointment/"),
    SEND_REMINDER(Method.GET, "appointment/sendReminder/patient/"),
    CHANGE_APPOINTMENT_STATUS(Method.GET, "appointment/changeStatus/"),

    GET_PATIENT_CARD_PDF_URL(Method.POST, "appointment/downloadpatientCard"),

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

    GET_BOTH_PERMISSIONS_FOR_DOCTOR(Method.GET, "dynamicUI/getBothPermissionsForDoctor/"),

    POST_UI_PERMISSIONS(Method.POST, "dynamicUI/postPermissions/"),

    ADD_PATIENT_TO_QUEUE(Method.POST, "appointment/queue/add"),

    ADD_UPDATE_PERSONAL_HISTORY_DETAIL(Method.POST, "history/assignPersonalHistory/"),

    ADD_UPDATE_DRUGS_AND_ALLERGIES_DETAIL(Method.POST, "history/assignDrugsAndAllergies/"),
    GET_DRUG_INTERACTIONS(Method.POST, "prescription/drugs/interaction?"),

    GET_HARDCODED_BLOOD_GROUPS(0, "");

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
