package com.healthcoco.healthcocopad.enums;

import com.android.volley.Request.Method;

/**
 * Created by Shreshtha on 20-01-2017.
 */
public enum WebServiceType {
    FRAGMENT_INITIALISATION(0, null),
    GET_PATIENT_VISIT_DETAIL(0, null),
    LOCAL_STRING_SAVE(0, null),
    GET_PATIENTS_COUNT(0, ""),
    ADD_PRESCRIPTION(Method.POST, "v1/prescription/prescriptionHandheld/add/"),

    GET_TREATMENT(Method.GET, "v1/treatment/"),

    ADD_TREATMENT(Method.POST, "v1/treatment/add"),

    GET_TREATMENT_LIST_BOTH_SOLR(Method.GET, "v1/solr/treatment/SERVICE/BOTH/?"),

    GET_TREATMENT_LIST_FEATURED(Method.GET, "v1/treatment/getServicesBySpeciality"),

    GET_TREATMENT_PDF_URL(Method.GET, "v1/treatment/download/"),

    DISCARD_TREATMENT(Method.DELETE, "v1/treatment/"),

    SEND_EMAIL_TREATMENT(Method.GET, "v1/treatment/"),

    ADD_TREATMENT_SERVICE(Method.POST, "v1/treatment/service/add"),

    GET_VIDEO(Method.GET, "v1/video/get"),

    ADD_VIDEO(Method.POST, "v1/video/add"),

    GET_PRESCRIPTION(Method.GET, "v1/prescription/"),
    GET_REPORTS_UPDATED(Method.GET, "v1/records/"),
    GET_CLINICAL_NOTES(Method.GET, "v1/clinicalNotes/"),
    GET_APPOINTMENT(Method.GET, "v1/appointment/"),

    GET_INVOICE(Method.GET, "v1/billing/invoice/BOTH"),

    GET_INVOICE_PDF_URL(Method.GET, "v1/billing/downloadInvoice/"),

    ADD_INVOICE(Method.POST, "v1/billing/invoice/add"),

    SEND_EMAIL_INVOICE(Method.GET, "v1/billing/invoice/"),

    DISCARD_INVOICE(Method.DELETE, "v1/billing/invoice/"),

    GET_RECEIPT(Method.GET, "v1/billing/receipt/"),

    GET_RECEIPT_PDF_URL(Method.GET, "v1/billing/downloadReceipt/"),

    DISCARD_RECEIPT(Method.DELETE, "v1/billing/receipt/"),

    GET_AMOUNT(Method.GET, "v1/billing/amount/"),

    ADD_RECEIPT(Method.POST, "v1/billing/receipt/add"),

    SEND_EMAIL_RECEIPT(Method.GET, "v1/billing/receipt/"),

//GET /v1/records/{recordId}/view  GET_RECORDS_BY_ID

    GET_RECORD_BY_RECORD_ID(Method.GET, "v1/records/"),

    GET_APPOINTMENT_BY_APPOINTMENT_ID(Method.GET, "v1/appointment/"),

    GET_CONTACTS(Method.GET, "v1/contacts/handheld/?"),

    LOGIN(Method.POST, "v1/login/user?"),

    IS_LOCATION_ADMIN(Method.POST, "v1/login/isLocationAdmin"),

    ADD_EDIT_PIN(Method.POST, "v1/login/pin/addEdit"),

    GET_DRUG_UNIT(Method.GET, "v1/prescription/DRUGSTRENGTHUNIT/BOTH/?discarded=false&doctorId="),

    GET_DRUG_TYPE(Method.GET, "v1/prescription/DRUGTYPE/BOTH/?"),

    //    ADD_DRUG(Method.POST, "v1/prescription/drug/add/"),
    ADD_DRUG(Method.POST, "v1/prescription/favouriteDrug/add/"),

    GET_DRUGS_LIST(Method.GET, "v1/prescription/DRUG_CUSTOM/BOTH/?discarded=false&doctorId="),
    GET_DRUGS_LIST_CUSTOM(Method.GET, "v1/prescription/DRUGS/CUSTOM/?"),
    GET_DRUGS_LIST_SOLR(Method.GET, "v1/solr/prescription/searchDrug/BOTH/?"),

    GET_DRUG_DOSAGE(Method.GET, "v1/prescription/DRUGDOSAGE/BOTH/?"),

    GET_DURATION_UNIT(Method.GET, "v1/prescription/DRUGDURATIONUNIT/BOTH/?"),

    GET_DIRECTION(Method.GET, "v1/prescription/DRUGDIRECTION/BOTH/?"),

    ADD_TEMPLATE(Method.POST, "v1/prescription/templateHandheld/add/"),
    TEMP_REGISTER_CUSTOMER(Method.POST, "v1/api/v1/user/register"),

    GET_TEMPLATES_LIST(Method.GET, "v1/prescription/templates/?"),

    ADD_RECORD(Method.POST, "v1/records/add/"),

//    ADD_FILE_RECORDS(Method.POST, "v1/records/upload/file"),

    ADD_RECORD_MULTIPART(Method.POST, "v1/records/addMultipart"),

//    GET_HISTORY_LIST(Method.GET, "v1/history/getPatientHistory/"),

    GET_DISEASE_HISTORY_LIST(Method.GET, "v1/history/"),
    GET_HISTORY_LIST(Method.GET, "v1/history/getHistory"),
    GET_GROUPS(Method.GET, "v1/contacts/groups/"),

    ASSIGN_GROUP(Method.POST, "v1/contacts/patient/addgroup/"),

    ADD_NEW_GROUP(Method.POST, "v1/contacts/group/add/"),

    UPDATE_GROUP(Method.PUT, "v1/contacts/group/"),

    DELETE_GROUP(Method.DELETE, "v1/contacts/group/"),

    UPDATE_TEMPLATE(Method.PUT, "v1/prescription/template/"),

    DELETE_TEMPLATE(Method.DELETE, "v1/prescription/template/"),

    DISCARD_REPORT(Method.DELETE, "v1/records/"),

    ADD_CLINICAL_NOTES(Method.POST, "v1/clinicalNotes/add/"),

    DISCARD_CLINICAL_NOTES(Method.DELETE, "v1/clinicalNotes/"),

    UPDATE_CLINICAL_NOTE(Method.PUT, "v1/clinicalNotes/"),

    GET_DIAGRAMS_LIST(Method.GET, "v1/clinicalNotes/DIAGRAMS/GLOBAL/?"),

    ADD_DIAGRAM(Method.POST, "v1/clinicalNotes/diagram/add/"),

    ADD_TO_HISTORY_PRESCRIPTION(Method.GET, "v1/history/prescription/"),

    ADD_TO_HISTORY_REPORT(Method.GET, "v1/history/report/"),

    ADD_TO_HISTORY_CLINICAL_NOTE(Method.GET, "v1/history/clinicalNotes/"),

    REMOVE_HISTORY_CLINICAL_NOTE(Method.GET, "v1/history/removeClinicalNotes/"),

    REMOVE_HISTORY_PRESCRIPTION(Method.GET, "v1/history/removePrescription/"),

    REMOVE_HISTORY_REPORT(Method.GET, "v1/history/removeReports/"),

    DISCARD_PRESCRIPTION(Method.DELETE, "v1/prescription/"),

    //    Clinical note Add Suggession
    ADD_COMPLAINT_SUGGESTIONS(Method.POST, "v1/clinicalNotes/complaint/add/"),

    ADD_DIAGNOSIS_SUGGESTIONS(Method.POST, "v1/clinicalNotes/diagnosis/add/"),

    ADD_EARS_EXAM_SUGGESTIONS(Method.POST, "v1/clinicalNotes/earsExam/add/"),

    ADD_ECG_DETAILS_SUGGESTIONS(Method.POST, "v1/clinicalNotes/ecgDetails/add/"),

    ADD_ECHO_SUGGESTIONS(Method.POST, "v1/clinicalNotes/echo/add/"),

    ADD_EYE_OBSERVATION_SUGGESTIONS(Method.POST, "v1/clinicalNotes/eyeObservation/add/"),

    ADD_GENERAL_EXAM_SUGGESTIONS(Method.POST, "v1/clinicalNotes/generalExam/add/"),

    ADD_HOLTER_SUGGESTIONS(Method.POST, "v1/clinicalNotes/holter/add/"),

    ADD_INDICATION_OF_USG_SUGGESTIONS(Method.POST, "v1/clinicalNotes/indicationOfUSG/add/"),

    ADD_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS(Method.POST, "v1/clinicalNotes/indirectLarygoscopyExam/add/"),

    ADD_INVESTIGATION_SUGGESTIONS(Method.POST, "v1/clinicalNotes/investigation/add/"),

    ADD_MENSTRUAL_HISTORY_SUGGESTIONS(Method.POST, "v1/clinicalNotes/menstrualHistory/add/"),

    ADD_NECK_EXAM_SUGGESTIONS(Method.POST, "v1/clinicalNotes/neckExam/add/"),

    ADD_NOSE_EXAM_SUGGESTIONS(Method.POST, "v1/clinicalNotes/noseExam/add/"),

    ADD_NOTES_SUGGESTIONS(Method.POST, "v1/clinicalNotes/notes/add/"),

    ADD_OBSERVATION_SUGGESTIONS(Method.POST, "v1/clinicalNotes/observation/add/"),

    ADD_OBSTETRIC_HISTORY_SUGGESTIONS(Method.POST, "v1/clinicalNotes/obstetricHistory/add/"),

    ADD_GENERAL_EXAMINATION_SUGGESTIONS(Method.POST, "v1/clinicalNotes/generalExam/add"),

    ADD_SYSTEMIC_EXAMINATION_SUGGESTIONS(Method.POST, "v1/clinicalNotes/systemExam/add"),

    ADD_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS(Method.POST, "v1/clinicalNotes/oralCavityThroatExam/add/"),

    ADD_PA_SUGGESTIONS(Method.POST, "v1/clinicalNotes/pa/add/"),

    ADD_PC_EARS_SUGGESTIONS(Method.POST, "v1/clinicalNotes/pcears/add/"),

    ADD_PC_NOSE_SUGGESTIONS(Method.POST, "v1/clinicalNotes/pcnose/add/"),

    ADD_PC_ORAL_CAVITY_SUGGESTIONS(Method.POST, "v1/clinicalNotes/pcoralCavity/add/"),

    ADD_PC_THROAT_SUGGESTIONS(Method.POST, "v1/clinicalNotes/pcthroat/add/"),

    ADD_PRESENT_COMPLAINT_SUGGESTIONS(Method.POST, "v1/clinicalNotes/presentComplaint/add/"),

    ADD_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS(Method.POST, "v1/clinicalNotes/presentComplaintHistory/add/"),

    ADD_PROCEDURE_NOTE_SUGGESTIONS(Method.POST, "v1/clinicalNotes/procedureNote/add/"),

    ADD_PROVISIONAL_DIAGNOSIS_SUGGESTIONS(Method.POST, "v1/clinicalNotes/provisionalDiagnosis/add/"),

    ADD_PS_SUGGESTIONS(Method.POST, "v1/clinicalNotes/ps/add/"),

    ADD_PV_SUGGESTIONS(Method.POST, "v1/clinicalNotes/pv/add/"),

    ADD_SYSTEM_EXAM_SUGGESTIONS(Method.POST, "v1/clinicalNotes/systemExam/add/"),

    ADD_XRAY_DETAILS_SUGGESTIONS(Method.POST, "v1/clinicalNotes/xRayDetails/add/"),

    ADD_ADVICE_SUGGESTIONS(Method.POST, "v1/prescription/advice/"),

    GET_PRESENT_COMPLAINT_SUGGESTIONS(Method.GET, "v1/clinicalNotes/PRESENT_COMPLAINT/"),

    GET_COMPLAINT_SUGGESTIONS(Method.GET, "v1/clinicalNotes/COMPLAINTS/"),

    GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS(Method.GET, "v1/clinicalNotes/HISTORY_OF_PRESENT_COMPLAINT/"),

    GET_MENSTRUAL_HISTORY_SUGGESTIONS(Method.GET, "v1/clinicalNotes/MENSTRUAL_HISTORY/"),

    GET_OBSTETRIC_HISTORY_SUGGESTIONS(Method.GET, "v1/clinicalNotes/OBSTETRIC_HISTORY/"),

    GET_GENERAL_EXAMINATION_SUGGESTIONS(Method.GET, "v1/clinicalNotes/GENERAL_EXAMINATION/"),

    GET_SYSTEMIC_EXAMINATION_SUGGESTIONS(Method.GET, "v1/clinicalNotes/SYSTEMIC_EXAMINATION/"),

    GET_OBSERVATION_SUGGESTIONS(Method.GET, "v1/clinicalNotes/OBSERVATIONS/"),

    GET_INVESTIGATION_SUGGESTIONS(Method.GET, "v1/clinicalNotes/INVESTIGATIONS/"),

    GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS(Method.GET, "v1/clinicalNotes/PROVISIONAL_DIAGNOSIS/"),

    GET_DIAGNOSIS_SUGGESTIONS(Method.GET, "v1/clinicalNotes/DIAGNOSIS/"),

    GET_ECG_SUGGESTIONS(Method.GET, "v1/clinicalNotes/ECG/"),

    GET_ECHO_SUGGESTIONS(Method.GET, "v1/clinicalNotes/ECHO/"),

    GET_XRAY_SUGGESTIONS(Method.GET, "v1/clinicalNotes/XRAY/"),

    GET_HOLTER_SUGGESTIONS(Method.GET, "v1/clinicalNotes/HOLTER/"),

    GET_PA_SUGGESTIONS(Method.GET, "v1/clinicalNotes/PA/"),

    GET_PS_SUGGESTIONS(Method.GET, "v1/clinicalNotes/PS/"),

    GET_PV_SUGGESTIONS(Method.GET, "v1/clinicalNotes/PV/"),

    GET_INDICATION_OF_USG_SUGGESTIONS(Method.GET, "v1/clinicalNotes/INDICATION_OF_USG/"),

    GET_NOTES_SUGGESTIONS(Method.GET, "v1/clinicalNotes/NOTES/"),

    GET_EAR_EXAM_SUGGESTIONS(Method.GET, "v1/clinicalNotes/EARS_EXAM/"),

    GET_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS(Method.GET, "v1/clinicalNotes/INDIRECT_LAGYROSCOPY_EXAM/"),

    GET_NECK_EXAM_SUGGESTIONS(Method.GET, "v1/clinicalNotes/NECK_EXAM/"),

    GET_NOSE_EXAM_SUGGESTIONS(Method.GET, "v1/clinicalNotes/NOSE_EXAM/"),

    GET_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS(Method.GET, "v1/clinicalNotes/ORAL_CAVITY_THROAT_EXAM/"),

    GET_PC_EARS_SUGGESTIONS(Method.GET, "v1/clinicalNotes/PC_EARS/"),

    GET_PC_NOSE_SUGGESTIONS(Method.GET, "v1/clinicalNotes/PC_NOSE/"),

    GET_PC_ORAL_CAVITY_SUGGESTIONS(Method.GET, "v1/clinicalNotes/PC_ORAL_CAVITY/"),

    GET_PC_THROAT_SUGGESTIONS(Method.GET, "v1/clinicalNotes/PC_THROAT/"),

    GET_PROCEDURE_NOTE_SUGGESTIONS(Method.GET, "v1/clinicalNotes/PROCEDURE_NOTE/"),

    GET_DISEASE_LIST(Method.GET, "v1/history/diseases/BOTH/"),

    GET_MEDICAL_AND_FAMILY_HISTORY(Method.GET, "v1/history/getMedicalAndFamilyHistory/"),

    ADD_FAMILY_HISTORY(Method.POST, "v1/history/familyHistory/"),

    ADD_MEDICAL_HISTORY(Method.POST, "v1/history/medicalHistory/"),

    ADD_DISEASE(Method.POST, "v1/history/disease/add/"),

    // GET http://example.com/api/v1/clinicalNotes/clinicalNotesId/doctorId/locationId/hospitalId/emailAddress/mail/
    SEND_EMAIL_CLINICAL_NOTES(Method.GET, "v1/clinicalNotes/"),

    // GET http://example.com/api/v1/records/recordId/doctorId/locationId/hospitalId/emailAddress/mail/
    SEND_EMAIL_REPORTS(Method.GET, "v1/records/"),

    // GET http://example.com/api/v1/prescription/prescriptionId/doctorId/locationId/hospitalId/emailAddress/mail/
    SEND_EMAIL_PRESCRIPTION(Method.GET, "v1/prescription/"),

    // GET http://example.com/api/v1/patientVisit/email/visitId/emailAddress/
    SEND_EMAIL_VISIT(Method.GET, "v1/patientVisit/email/"),

    //  GET http://example.com/api/v1/prescription/prescriptionId/doctorId/locationId/hospitalId/mobileNumber/sms/
    SEND_SMS_PRESCRIPTION(Method.GET, "v1/prescription/"),

    // GET http://example.com/api/v1/patientVisit/visitId/doctorId/locationId/hospitalId/mobileNumber/sms/
    SEND_SMS_VISIT(Method.GET, "v1/patientVisit/"),

    //    GET http://example.com/api/v1/patientVisit/doctorId/locationId/hospitalId/patientId/
    GET_PATIENT_VISIT(Method.GET, "v1/patientVisit/"),

    DISCARD_PATIENT_VISIT(Method.DELETE, "v1/patientVisit/"),

    UPDATE_PRESCRIPTION(Method.PUT, "v1/prescription/"),

    SIGN_UP(Method.POST, "v1/signup/doctorHandheld/"),

    SIGN_UP_CONTINUE_VERIFICATION(Method.POST, "v1/signup/doctorHandheldContinue/"),

    RESET_PASSWORD(Method.POST, "v1/forgotPassword/forgotPasswordDoctor/"),

    GET_SPECIALITIES(Method.GET, "v1/doctorProfile/getSpecialities/?"),

    DISCARD_PATIENT(Method.DELETE, "v1/register/patient/"),

    REGISTER_PATIENT(Method.POST, "v1/register/patient/"),

    UPDATE_PATIENT(Method.PUT, "v1/register/patient/"),

    SEARCH_PATIENTS(Method.GET, "v1/register/existing_patients/"),

    GET_CITIES(Method.GET, "v1/appointment/cities/?"),

    GET_PATIENT_PROFILE(Method.GET, "v1/register/getpatientprofile/"),

    GET_REFERENCE(Method.GET, "v1/register/reference/BOTH/?"),

    ADD_REFERENCE(Method.POST, "v1/register/referrence/add/"),

    UPDATE_REFERENCE(Method.PUT, "v1/register/referrence/add/"),

    DELETE_REFERENCE(Method.DELETE, "v1/register/referrence/"),

    GET_BLOOD_GROUP(Method.GET, "v1/register/settings/bloodGroup/"),

    GET_PROFESSION(Method.GET, "v1/register/settings/profession/"),

    //    DELETEhttp://example.com/api/v1/prescription/drugDirection/drugDirectionId/delete/
    DELETE_DIRECTION(Method.DELETE, "v1/prescription/drugDirection/"),

    //    DELETEhttp://example.com/api/v1/prescription/drug/drugId/delete/
    DELETE_DRUG(Method.DELETE, "v1/prescription/drug/"),

    //    DELETEhttp://example.com/api/v1/prescription/drugDosage/drugDosageId/delete/
    DELETE_DRUG_DOSAGE(Method.DELETE, "v1/prescription/drugDosage/"),


    DELETE_DISEASE(Method.DELETE, "v1/history/disease/"),
    DELETE_TREATMENT(Method.DELETE, "v1/treatment/service/"),

    DELETE_COMPLAINT(Method.DELETE, "v1/clinicalNotes/complaint/"),
    DELETE_PRESENT_COMPLAINT(Method.DELETE, "v1/clinicalNotes/presentComplaint/"),
    DELETE_HISTORY_OF_PRESENT_COMPLAINT(Method.DELETE, "v1/clinicalNotes/presentComplaintHistory/"),
    DELETE_MENSTRUAL_HISTORY(Method.DELETE, "v1/clinicalNotes/menstrualHistory/"),
    DELETE_OBSTETRIC_HISTORY(Method.DELETE, "v1/clinicalNotes/obstetricHistory/"),
    DELETE_GENERAL_EXAMINATION(Method.DELETE, "v1/clinicalNotes/generalExam/"),
    DELETE_SYSTEMIC_EXAMINATION(Method.DELETE, "v1/clinicalNotes/systemExam/"),
    DELETE_OBSERVATION(Method.DELETE, "v1/clinicalNotes/observation/"),
    DELETE_INVESTIGATION(Method.DELETE, "v1/clinicalNotes/investigation/"),
    DELETE_PROVISIONAL_DIAGNOSIS(Method.DELETE, "v1/clinicalNotes/provisionalDiagnosis/"),
    DELETE_DIAGNOSIS(Method.DELETE, "v1/clinicalNotes/diagnosis/"),
    DELETE_ECG(Method.DELETE, "v1/clinicalNotes/ecgDetails/"),
    DELETE_ECHO(Method.DELETE, "v1/clinicalNotes/echo/"),
    DELETE_XRAY(Method.DELETE, "v1/clinicalNotes/xRayDetails/"),
    DELETE_HOLTER(Method.DELETE, "v1/clinicalNotes/holter/"),
    DELETE_PA(Method.DELETE, "v1/clinicalNotes/pa/"),
    DELETE_PS(Method.DELETE, "v1/clinicalNotes/ps/"),
    DELETE_PV(Method.DELETE, "v1/clinicalNotes/pv/"),
    DELETE_INDICATION_OF_USG(Method.DELETE, "v1/clinicalNotes/presentIndicationOfUSG/"),
    DELETE_NOTES(Method.DELETE, "v1/clinicalNotes/notes/"),
    DELETE_PROCEDURE_NOTE(Method.DELETE, "v1/clinicalNotes/procedureNote/"),

    ADD_PRESCRIPTION_APPOINTMENT_FEEDBACK(Method.POST, "v1/feedback/addEditPatientFeedback"),

    ADD_DIRECTION(Method.POST, "v1/prescription/drugDirection/add/"),

    ADD_DOSAGE(Method.POST, "v1/prescription/drugDosage/add/"),

    ADD_CUSTOM_HISTORY(Method.POST, "v1/history/disease/add/"),

    ADD_FEEDBACK(Method.POST, "v1/register/feedback/add/"),

    GET_CLINIC_PROFILE(Method.GET, "v1/appointment/clinic/"),

    GET_REGISTER_DOCTOR(Method.GET, "v1/register/users/"),

    ADD_UPDATE_CLINIC_ADDRESS(Method.POST, "v1/register/settings/updateClinicAddress/"),

    ADD_UPDATE_CLINIC_CONTACT(Method.POST, "v1/register/settings/updateClinicProfileHandheld/"),

    ADD_UPDATE_CLINIC_HOURS(Method.POST, "v1/register/settings/updateClinicTiming"),

    GET_EDUCATION_QUALIFICATION(Method.GET, "v1/doctorProfile/getEducationQualifications/"),

    GET_EDUCATION_QUALIFICATION_SOLR(Method.GET, "v1/solr/master/educationQualification/?"),

    GET_COLLEGE_UNIVERSITY_INSTITUES(Method.GET, "v1/doctorProfile/getEducationInstitutes/"),

    GET_COLLEGE_UNIVERSITY_INSTITUES_SOLR(Method.GET, "v1/solr/master/educationInstitute/?"),

    GET_MEDICAL_COUNCILS(Method.GET, "v1/doctorProfile/getMedicalCouncils/"),

    GET_MEDICAL_COUNCILS_SOLR(Method.GET, "v1/solr/master/medicalCouncil/?"),

    ADD_UPDATE_EDUCATION(Method.POST, "v1/doctorProfile/addEditEducation/"),

    ADD_UPDATE_REGISTRATION_DETAIL(Method.POST, "v1/doctorProfile/addEditRegistrationDetail/"),

    GET_DOCTOR_PROFILE(Method.GET, "v1/doctorProfile/"),

    UPDATE_DOCTOR_PROFILE(Method.POST, "v1/doctorProfile/addEditMultipleData/"),

    ADD_UPDATE_DOCTOR_CLINIC_HOURS(Method.POST, "v1/doctorProfile/clinicProfile/addEditVisitingTime/"),

    ADD_UPDATE_GENERAL_INFO(Method.POST, "v1/doctorProfile/clinicProfile/addEditGeneralInfo/"),

    //    POSThttp://example.com/api/v1/doctorProfile/addEditContact/
    ADD_UPDATE_DOCTOR_CONTACT(Method.POST, "v1/doctorProfile/addEditContact/"),

    ADD_CLINIC_IMAGE(Method.POST, "v1/register/settings/clinicImage/add/"),

    //    POSThttp://example.com/api/v1/register/settings/changeClinicLogo/
    ADD_CLINIC_LOGO(Method.POST, "v1/register/settings/changeClinicLogo/"),

    // DELETEhttp://example.com/api/v1/register/settings/clinicImage/locationId/counter/delete/
    DELETE_CLINIC_IMAGE(Method.DELETE, "v1/register/settings/clinicImage/"),


    ADD_PRINT_SETTINGS(Method.POST, "v1/printSettings/add/"),

    GET_PRINT_SETTINGS(Method.GET, "v1/printSettings"),

    //    GET http://example.com/api/v1/dynamicUI/getDataPermissionForDoctor/doctorId/
    GET_DATA_PERMISSION(Method.GET, "v1/dynamicUI/getDataPermissionForDoctor/"),

    //    GET http://example.com/api/v1/register/patientStatus/patientId/doctorId/locationId/hospitalId/
    GET_PATIENT_STATUS(Method.GET, "v1/register/patientStatus/"),

    //    GET http://example.com/api/v1/otp/doctorId/locationId/hospitalId/patientId/generate/
    GENERATE_OTP(Method.GET, "v1/otp/"),

    //    GET http://example.com/api/v1/otp/doctorId/locationId/hospitalId/patientId/otpNumber/verify/
    VERIFY_OTP(Method.GET, "v1/otp/"),

    GET_DIAGNOSTIC_TESTS_SOLR(Method.GET, "v1/solr/prescription/searchDiagnosticTest/BOTH/?"),

    GET_SEARCH_ADVICE_SOLR(Method.GET, "v1/solr/prescription/searchAdvice/BOTH/?"),

    ADD_DIAGNOSTIC_TESTS(Method.POST, "v1/prescription/diagnosticTest/addEdit/?"),

    //    GET http://example.com/v1/appointment/getTimeSlots/doctorId/locationId/date/
    GET_APPOINTMENT_TIME_SLOTS(Method.GET, "v1/appointment/getTimeSlots/"),

    GET_CALENDAR_EVENTS(Method.GET, "v1/appointment"),

    ADD_APPOINTMENT(Method.POST, "v1/appointment/"),

    GET_EVENTS(Method.GET, "v1/appointment/event/get"),

    ADD_EVENT(Method.POST, "v1/appointment/event/"),

    SEND_REMINDER(Method.GET, "v1/appointment/sendReminder/patient/"),

    CHANGE_APPOINTMENT_STATUS(Method.GET, "v1/appointment/changeStatus/"),

    GET_PATIENT_CARD_PDF_URL(Method.POST, "v1/appointment/downloadpatientCard"),

    //    post /v1/notification/device/add
    SEND_GCM_REGISTRATION_ID(Method.POST, "v1/notification/device/add"),

    //    POST /v1/signup/submitDoctorContact
    DOCTOR_CONTACT_US(Method.POST, "v1/signup/submitDoctorContact"),

    //    POST /v1/version/check
    VERSION_CONTROL_CHECK(Method.POST, "v1/version/check"),

    GET_PRESCRIPTION_PDF_URL(Method.GET, "v1/prescription/download/"),
    GET_REPORT_PDF_URL(Method.GET, "v1/records/download/"),
    GET_CLINICAL_NOTES_PDF_URL(Method.GET, "v1/clinicalNotes/download/"),
    ADD_VISIT(Method.POST, "v1/patientVisit/add/"),
    GET_VISIT_PDF_URL(Method.GET, "v1/patientVisit/download/"),
    ADD_DOCTOR(Method.POST, "v1/register/user/"),
    //    records/{recordId}/{recordsState}/changeState
    CHANGE_RECORD_STATE(Method.GET, "v1/records/"),

    ADD_UPDATE_ACHIEVEMENTS_DETAIL(Method.POST, "v1/doctorProfile/addEditAchievement/"),

    ADD_UPDATE_EXPERIENCE_DETAIL(Method.POST, "v1/doctorProfile/addEditExperienceDetail/"),

    ADD_UPDATE_PROFESSIONAL_MEMBERSHIP_DETAIL(Method.POST, "v1/doctorProfile/addEditProfessionalMembership/"),

    ADD_UPDATE_PROFESSIONAL_STATEMENT_DETAIL(Method.POST, "v1/doctorProfile/addEditProfessionalStatement/"),

    GET_PROFESSIONAL_MEMBERSHIP_SOLR(Method.GET, "v1/solr/master/professionalMembership/?"),

    GET_BOTH_PERMISSIONS_FOR_DOCTOR(Method.GET, "v1/dynamicUI/getBothPermissionsForDoctor/"),

    POST_UI_PERMISSIONS(Method.POST, "v1/dynamicUI/postPermissions/"),

    ADD_PATIENT_TO_QUEUE(Method.POST, "v1/appointment/queue/add"),

    ADD_UPDATE_PERSONAL_HISTORY_DETAIL(Method.POST, "v1/history/assignPersonalHistory/"),

    ADD_UPDATE_DRUGS_AND_ALLERGIES_DETAIL(Method.POST, "v1/history/assignDrugsAndAllergies/"),
    GET_DRUG_INTERACTIONS(Method.POST, "v1/prescription/drugs/interaction?"),

    GET_All_HEALTH_BLOGS(Method.GET, "v1/blogs/get?"),

    GET_FAV_HEALTH_BLOGS(Method.GET, "v1/blogs/getFovourite?"),

    GET_HEALTH_BLOG_BY_ID(Method.GET, "v1/blogs/getBlog/"),

    ADD_TO_FAVORITE(Method.GET, "v1/blogs/addTOFovourite/"),

    LIKE_THE_BLOG(Method.GET, "v1/blogs/likeTheBlog/"),

    ADD_REMOVE_FAVORITE(Method.GET, "v1/favourite/addRemove/"),

    GET_HARDCODED_BLOOD_GROUPS(0, "v1/"),

    GET_VACCINATION(Method.GET, "v1/paediatric/vaccine/getList"),

    ADD_EDIT_VACCINATION(Method.POST, "v1/paediatric/vaccine/addEdit"),

    ADD_EDIT_MULTIPLE_VACCINATION(Method.POST, "v1/paediatric/vaccine/addEditMultiple"),

    GET_VACCINATION_BRAND(Method.GET, "v1/paediatric/vaccine/getBrands"),

    GET_VACCINATION_BRAND_MULTIPLE(Method.GET, "v1/paediatric/vaccine/getMultipleBrands"),

    GET_VACCINES_SOLR_LIST(Method.GET, "v1/paediatric/vaccine/getMasterList"),

    GET_GROWTH_CHART(Method.GET, "v1/paediatric/growthChart/getList"),

    ADD_GROWTH_CHART(Method.POST, "v1/paediatric/growthChart/addEdit"),

    DISCARD_GROWTH_CHART(Method.GET, "v1/paediatric/growthChart/discard/"),

    GET_BABY_ACHIEVEMENTS(Method.GET, "v1/paediatric/achievement/getList/"),

    ADD_EDIT_BABY_ACHIEVEMENTS(Method.POST, "v1/paediatric/achievement/addEdit"),

    UPDATE_VACCINE_START_DATE(Method.GET, "v1/paediatric/vaccine/updateChart/"),

    DELETE_PATIENT(Method.DELETE, "v1/register/patient/"),

    UPDATE_PATINET_MOBILE_NUMBER(Method.GET, "v1/register/patient/"),

    UPDATE_PRINT_SETTING_FILE(Method.POST, "v1/printSettings/upload/file"),
    GET_CHECK_PNUM_EXIST(Method.GET, "v1/register/checkIfPnumExist/"),
    //V2 apis
    GET_CONTACTS_NEW(Method.GET, "v2/contacts/handheld/?");


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
