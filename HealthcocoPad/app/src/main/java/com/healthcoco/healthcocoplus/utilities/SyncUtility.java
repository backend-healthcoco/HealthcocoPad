package com.healthcoco.healthcocoplus.utilities;

import android.os.AsyncTask;

import com.android.volley.Response;
import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.HealthCocoApplication;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.BloodGroup;
import com.healthcoco.healthcocoplus.bean.server.CalendarEvents;
import com.healthcoco.healthcocoplus.bean.server.CityResponse;
import com.healthcoco.healthcocoplus.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocoplus.bean.server.DoctorProfile;
import com.healthcoco.healthcocoplus.bean.server.DrugDirection;
import com.healthcoco.healthcocoplus.bean.server.DrugDosage;
import com.healthcoco.healthcocoplus.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocoplus.bean.server.DrugType;
import com.healthcoco.healthcocoplus.bean.server.Location;
import com.healthcoco.healthcocoplus.bean.server.Profession;
import com.healthcoco.healthcocoplus.bean.server.Reference;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.bean.server.Specialities;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.bean.server.UserGroups;
import com.healthcoco.healthcocoplus.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocoplus.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocoplus.enums.LocalTabelType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;

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
            Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.COMPLAINT_SUGGESTIONS);
            WebDataServiceImpl.getInstance(mApp).getClinicalNoteSuggestionsList(ComplaintSuggestions.class, WebServiceType.GET_COMPLAINT_SUGGESTIONS, user.getUniqueId(),
                    latestUpdatedTime, this, this);
        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {

    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {

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
                case GET_CONTACTS:
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PATIENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
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
                    LocalDataServiceImpl.getInstance(mApp).
                            addPatientsList((ArrayList<RegisteredPatientDetailsUpdated>) (ArrayList<?>) response.getDataList());
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
}
