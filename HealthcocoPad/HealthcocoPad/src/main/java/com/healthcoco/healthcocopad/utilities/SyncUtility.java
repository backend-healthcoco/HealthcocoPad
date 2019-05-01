package com.healthcoco.healthcocopad.utilities;

import android.os.AsyncTask;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.bean.UiPermissionsBoth;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.BloodGroup;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.CityResponse;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotesDynamicField;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.server.Location;
import com.healthcoco.healthcocopad.bean.server.PatientCount;
import com.healthcoco.healthcocopad.bean.server.Profession;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.Specialities;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandResponse;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.ContactsListFragment;
import com.healthcoco.healthcocopad.fragments.MenuDrawerFragment;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 07-02-2017.
 */
public class SyncUtility implements Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised {
    private static final String TAG = SyncUtility.class.getSimpleName();
    private HealthCocoActivity mActivity;
    private HealthCocoApplication mApp;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    Long latestUpdatedTimeContact = 0l;
    private long MAX_COUNT;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved = true;

    public SyncUtility(HealthCocoApplication mApp, HealthCocoActivity mActivity, User user, RegisteredPatientDetailsUpdated selectedPatient) {
        this.mApp = mApp;
        this.mActivity = mActivity;
        this.user = user;
        this.selectedPatient = selectedPatient;
    }

    public void updateMasterTableOnSpecialityChange() {
        LocalDataServiceImpl.getInstance(mApp).clearMasterDataOnSpecialityChange();
        syncClinicalNotesData();
    }

    public void syncClinicalNotesData() {
        getComplaintSuggestions();
    }

    private void getComplaintSuggestions() {
        if (user != null) {
            ClinicalNotesDynamicField clinicalNotesDynamicField = LocalDataServiceImpl.getInstance(mApp).getClinicalNotesDynamicField();
            Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.COMPLAINT_SUGGESTIONS);
            WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ComplaintSuggestions.class, WebServiceType.GET_COMPLAINT_SUGGESTIONS, clinicalNotesDynamicField.getComplaint(), user.getUniqueId(),
                    latestUpdatedTime, this, this);
        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        if (Util.isSyncActive) {
            Util.isSyncActive = false;
            Util.sendBroadcast(mApp, ContactsListFragment.INTENT_REFRESH_PATIENT_COUNT);
        }
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        if (Util.isSyncActive) {
            Util.isSyncActive = false;
            Util.sendBroadcast(mApp, ContactsListFragment.INTENT_REFRESH_PATIENT_COUNT);
        }
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + response.getWebServiceType());
            switch (response.getWebServiceType()) {
                case GET_DRUG_DOSAGE:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DRUG_DOSAGE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    return;
                case GET_DOCTOR_PROFILE:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DOCTOR_PROFILE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    return;
                case GET_CLINIC_PROFILE:
                    if (response.getData() != null && response.getData() instanceof Location)
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_LOCATION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_DIAGRAMS_LIST:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DIAGRAMS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    }
                    break;
                case GET_DURATION_UNIT:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DURATION_UNIT, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    return;
                case GET_DIRECTION:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DIRECTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_DRUG_TYPE:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DRUG_TYPE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_GROUPS:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_GROUPS_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_CITIES:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_CITIES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_SPECIALITIES:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_SPECIALITIES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_PROFESSION:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PROFESSION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_REFERENCE:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_REFERENCE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_BLOOD_GROUP:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_BLOOD_GROUPS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_CALENDAR_EVENTS:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_CALENDAR_EVENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_EDUCATION_QUALIFICATION:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_EDUCATION_QUALIFICATION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_COLLEGE_UNIVERSITY_INSTITUES:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_INSTITUTES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_MEDICAL_COUNCILS:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_MEDICAL_COUNCILS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                //  syncing clinical notes suggestions data and art the end diagrams data
                case GET_COMPLAINT_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList()))
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_COMPLAINT_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    else
//                        getObservationSuggestions();
                        break;
                case GET_OBSERVATION_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList()))
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_OBSERVATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    else
//                        getInvestigationSuggestions();
                        break;
                case GET_INVESTIGATION_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList()))
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_INVESTIGATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    else
//                        getDiagnosisSuggestions();
                        break;
                case GET_DIAGNOSIS_SUGGESTIONS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList()))
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DIAGNOSIS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
//                    else syncDiagramsData();
                    break;
                ////////////// till here////////////
                case GET_DISEASE_LIST:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DISEASE_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_TEMPLATES_LIST:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_TEMPLATES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    break;
                case GET_VACCINATION_BRAND:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_VACCINATION_BRAND, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    }
                    break;
                case GET_BOTH_PERMISSIONS_FOR_DOCTOR:
                    if (response.getData() != null) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_BOTH_USER_UI_PERMISSIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    }
                    break;
                case GET_CONTACTS:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        if (Util.isNullOrZeroNumber(MAX_COUNT) || isTotalCountIsGreater())
                            updatePatientCount(response);
                        if (Util.isNullOrEmptyList(response.getDataList()) || response.getDataList().size() < ContactsListFragment.MAX_NUMBER_OF_CONTACT
                                || Util.isNullOrEmptyList(response.getDataList())) {
                            isEndOfListAchieved = true;
                        } else {
                            PAGE_NUMBER = PAGE_NUMBER + 1;
                        }
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PATIENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    } else {
                        if (Util.isSyncActive && !response.isFromLocalAfterApiSuccess()) {
                            Util.isSyncActive = false;
                            Util.sendBroadcast(mApp, ContactsListFragment.INTENT_REFRESH_PATIENT_COUNT);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_DRUG_DOSAGE:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addDrugDosageList((ArrayList<DrugDosage>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_DOCTOR_PROFILE:
                if (response.getData() != null)
                    LocalDataServiceImpl.getInstance(mApp).
                            addDoctorProfile((DoctorProfile) response.getData());
                break;
//            case ADD_DIAGRAMS:
//                LocalDataServiceImpl.getInstance(mApp).addDiagramsList(null, (ArrayList<Diagram>) (ArrayList<?>) response
//                        .getDataList());
//                break;
            case ADD_LOCATION:
                if (response.getData() != null)
                    LocalDataServiceImpl.getInstance(mApp).
                            addLocation((Location) response.getData());
                break;
            case ADD_DURATION_UNIT:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addDurationUnitList((ArrayList<DrugDurationUnit>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_DIRECTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addDirectionsList((ArrayList<DrugDirection>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_DRUG_TYPE:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addDrugType((ArrayList<DrugType>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_GROUPS_LIST:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addUserGroupsList((ArrayList<UserGroups>) (ArrayList<?>) response
                            .getDataList());
                break;
            case ADD_SPECIALITIES:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addSpecialities((ArrayList<Specialities>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_CITIES:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addCities((ArrayList<CityResponse>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_PROFESSION:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addProfessionsList((ArrayList<Profession>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_REFERENCE:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addReferenceList((ArrayList<Reference>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_BLOOD_GROUPS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addBloodGroups((ArrayList<BloodGroup>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_CALENDAR_EVENTS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addCalendarEventsList(
                            (ArrayList<CalendarEvents>) (ArrayList<?>) response
                                    .getDataList());
                break;
            case ADD_PATIENTS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addPatientsList((ArrayList<RegisteredPatientDetailsUpdated>) (ArrayList<?>) response.getDataList());
                response.setIsFromLocalAfterApiSuccess(true);
                if (!isEndOfListAchieved && !Util.isNullOrZeroNumber(MAX_COUNT)) {
                    syncContact();
                } else {
                    Util.isSyncActive = false;
                    resetListAndPagingAttributes();
                }
                Util.sendBroadcast(mApp, MenuDrawerFragment.INTENT_REFRESH_PATIENT_COUNT);
                Util.sendBroadcast(mApp, ContactsListFragment.INTENT_REFRESH_PATIENT_COUNT);
                break;
//            case ADD_EDUCATION_QUALIFICATION:
//                if (!Util.isNullOrEmptyList(response.getDataList()))
//                    LocalDataServiceImpl.getInstance(mApp).
//                            addEducationsList((ArrayList<EducationQualification>) (ArrayList<?>) response.getDataList());
//                break;
//            case ADD_INSTITUTES:
//                if (!Util.isNullOrEmptyList(response.getDataList()))
//                    LocalDataServiceImpl.getInstance(mApp).
//                            addCollegeUniversityInstituteList((ArrayList<CollegeUniversityInstitute>) (ArrayList<?>) response.getDataList());
//                break;
//            case ADD_MEDICAL_COUNCILS:
//                if (!Util.isNullOrEmptyList(response.getDataList()))
//                    LocalDataServiceImpl.getInstance(mApp).
//                            addMedicalCouncilList((ArrayList<MedicalCouncil>) (ArrayList<?>) response.getDataList());
//                break;
            case ADD_COMPLAINT_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_COMPLAINT_SUGGESTIONS, LocalTabelType.COMPLAINT_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_OBSERVATION_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_OBSERVATION_SUGGESTIONS, LocalTabelType.OBSERVATION_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_INVESTIGATION_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_INVESTIGATION_SUGGESTIONS, LocalTabelType.INVESTIGATION_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_DIAGNOSIS_SUGGESTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).
                            addSuggestionsList(WebServiceType.GET_DIAGNOSIS_SUGGESTIONS, LocalTabelType.DIAGNOSIS_SUGGESTIONS,
                                    response.getDataList(), null, null);
                break;
            case ADD_VACCINATION_BRAND:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    LocalDataServiceImpl.getInstance(mApp).addVaccinationBrandsList((ArrayList<VaccineBrandResponse>) (ArrayList<?>) response.getDataList());
                break;
            case ADD_BOTH_USER_UI_PERMISSIONS:
                if (response.getData() != null)
                    LocalDataServiceImpl.getInstance(mApp).
                            addBothUserUiPermissions((UiPermissionsBoth) response.getData());
                break;
//            case ADD_DISEASE_LIST:
//                if (!Util.isNullOrEmptyList(response.getDataList()))
//                    LocalDataServiceImpl.getInstance(mApp).addDiseaseList((ArrayList<Disease>) (ArrayList<?>) response.getDataList());
//                break;
//            case ADD_TEMPLATES:
//                if (!Util.isNullOrEmptyList(response.getDataList()))
//                    LocalDataServiceImpl.getInstance(mApp).addTemplatesList((ArrayList<TempTemplate>) (ArrayList<?>) response.getDataList());

        }
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(response.getWebServiceType());
        volleyResponseBean.setIsDataFromLocal(true);
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    public void getVaccinesBrandsList() {
        WebDataServiceImpl.getInstance(mApp).getVaccinationBrandList(VaccineBrandResponse.class, WebServiceType.GET_VACCINATION_BRAND, this, this);
    }

    public void getUIPermissions() {
        if (user != null) {
            WebDataServiceImpl.getInstance(mApp).getBothUIPermissionsForDoctor(UiPermissionsBoth.class, user.getUniqueId(), this, this);
        }
    }

    public void getContactsList() {
        Util.isSyncActive = true;
        MAX_COUNT = LocalDataServiceImpl.getInstance(mApp).getPatientCountLong(user);
        if (!Util.isNullOrZeroNumber(MAX_COUNT)) {
            isPaginationRequired();
            syncContact();
        } else {
            isEndOfListAchieved = false;
            latestUpdatedTimeContact = 0l;
            syncContact();
        }
    }

    private void syncContact() {
        if (isEndOfListAchieved) {
            latestUpdatedTimeContact = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.REGISTERED_PATIENTS_DETAILS);
        }
        WebDataServiceImpl.getInstance(mApp).getContactsList(RegisteredPatientDetailsUpdated.class, user.getUniqueId(),
                user.getForeignHospitalId(), user.getForeignLocationId(), latestUpdatedTimeContact, user, PAGE_NUMBER, ContactsListFragment.MAX_NUMBER_OF_CONTACT, null, this, this);
    }

    private boolean isPaginationRequired() {
        long count = LocalDataServiceImpl.getInstance(mApp).getListCount(user);
        if (count < MAX_COUNT) {
            PAGE_NUMBER = (int) (count / ContactsListFragment.MAX_NUMBER_OF_CONTACT);
            isEndOfListAchieved = false;
            latestUpdatedTimeContact = 0l;
            return true;
        } else
            return false;
    }

    private void resetListAndPagingAttributes() {
        PAGE_NUMBER = 0;
        isEndOfListAchieved = true;
    }

    private void updatePatientCount(VolleyResponseBean response) {
        long totalCount = 0;
        if (response.getData() != null && response.getData() instanceof Long)
            totalCount = (long) response.getData();
        else if (response.getData() != null && response.getData() instanceof Double) {
            Double data = (Double) response.getData();
            totalCount = Math.round(data);
        }
        if (!Util.isNullOrZeroNumber(totalCount)) {
            MAX_COUNT = totalCount;
            PatientCount patientCount = new PatientCount();
            patientCount.setDoctorId(user.getUniqueId());
            patientCount.setLocationId(user.getForeignLocationId());
            patientCount.setHospitalId(user.getForeignHospitalId());
            patientCount.setCount(totalCount);
            patientCount.setSyncCompleted(false);
            LocalDataServiceImpl.getInstance(mApp).
                    addPatientCount(patientCount);
        }
    }

    private boolean isTotalCountIsGreater() {
        long count = LocalDataServiceImpl.getInstance(mApp).getListCount(user);
        if (count <= MAX_COUNT)
            return false;
        else
            return true;
    }

}
