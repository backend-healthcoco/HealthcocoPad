package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.PersonalFamilyHistoryDiseaseAdapter;
import com.healthcoco.healthcocopad.bean.PersonalHistory;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddMedicalFamilyHistoryRequest;
import com.healthcoco.healthcocopad.bean.request.RegisterNewPatientRequest;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.bean.server.HistoryDetailsResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.MedicalFamilyHistoryItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class PatientMedicalDeatilsFragment extends HealthCocoFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, MedicalFamilyHistoryItemListener {

    RegisterNewPatientRequest patientDetails = new RegisterNewPatientRequest();
    private GridView gvFamilyHistory;
    private PersonalFamilyHistoryDiseaseAdapter adapter;
    private ArrayList<Disease> diseaseList;
    private TextView tvNoFamilyHistory;
    private ArrayList<String> addFamilyDiseasesList = new ArrayList<String>();
    private ArrayList<String> removeFamilyDiseasesList = new ArrayList<String>();
    private ArrayList<String> familyDiseaseIdsList = new ArrayList<String>();
    private User user;
    private boolean isEditPatient;
    private EditText editDiet;
    private EditText editAddiction;
    private EditText editBowelHabits;
    private EditText editBladderHabits;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_medical_history, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();

            initViews();
            initListeners();
            initAdapter();
        }
    }

    private void getListFromLocal() {
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DISEASE_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void initViews() {
        gvFamilyHistory = (GridView) view.findViewById(R.id.gv_family_history_list);
        tvNoFamilyHistory = (TextView) view.findViewById(R.id.tv_no_family_history);
        editDiet = (EditText) view.findViewById(R.id.edit_diet);
        editAddiction = (EditText) view.findViewById(R.id.edit_addiction);
        editBowelHabits = (EditText) view.findViewById(R.id.edit_bowel_habits);
        editBladderHabits = (EditText) view.findViewById(R.id.edit_bladder_habits);

    }

    @Override
    public void initListeners() {
    }

    private void initAdapter() {
        adapter = new PersonalFamilyHistoryDiseaseAdapter(mActivity, this);
        adapter.setListData(diseaseList);
        gvFamilyHistory.setAdapter(adapter);
    }

    private void notifyAdapter(ArrayList<Disease> list) {
        if (!Util.isNullOrEmptyList(list)) {
            Collections.sort(list, ComparatorUtil.diseaseDateComparator);
            gvFamilyHistory.setVisibility(View.VISIBLE);
            tvNoFamilyHistory.setVisibility(View.GONE);
            adapter.setListData(list);
            adapter.notifyDataSetChanged();
        } else {
            gvFamilyHistory.setVisibility(View.GONE);
            tvNoFamilyHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void addMedicalFamilyHistory() {
        mActivity.showLoading(false);
        AddMedicalFamilyHistoryRequest addMedicalFamilyHistoryRequest = new AddMedicalFamilyHistoryRequest();
        addMedicalFamilyHistoryRequest.setHospitalId(user.getForeignHospitalId());
        addMedicalFamilyHistoryRequest.setLocationId(user.getForeignLocationId());
        addMedicalFamilyHistoryRequest.setDoctorId(user.getUniqueId());
        addMedicalFamilyHistoryRequest.setAddDiseases(addFamilyDiseasesList);
        addMedicalFamilyHistoryRequest.setRemoveDiseases(removeFamilyDiseasesList);
        addMedicalFamilyHistoryRequest.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        WebServiceType webServiceType = null;
      /*  LogUtils.LOGD(TAG, "FilterType : " + String.valueOf(filterType));
        switch (filterType) {
            case MEDICAL_HISTORY:
                webServiceType = WebServiceType.ADD_MEDICAL_HISTORY;
                break;
            case FAMILY_HISTORY:
                webServiceType = WebServiceType.ADD_FAMILY_HISTORY;
                break;
        }*/
        WebDataServiceImpl.getInstance(mApp).addMedicalFamilyHistory(webServiceType, Boolean.class, addMedicalFamilyHistoryRequest, this, this);

    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case GET_DISEASE_LIST:
                if (response.isDataFromLocal()) {
                    diseaseList = (ArrayList<Disease>) (ArrayList<?>) response.getDataList();
                    LogUtils.LOGD(TAG, "Success onResponse diseaseList Size " + diseaseList.size() + " isDataFromLocal " + response.isDataFromLocal());
                }
                notifyAdapter(diseaseList);
                mActivity.hideLoading();
                break;
            case UPDATE_PATIENT:
                if (response.isValidData(response) && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                    RegisteredPatientDetailsUpdated patientDetails = (RegisteredPatientDetailsUpdated) response.getData();
                    refreshContactsData(patientDetails);
                    mActivity.hideLoading();
                    //will directly refresh PatientDetailSCreen on its onActivityResult
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_REGISTRATION, new Intent().putExtra(HealthCocoConstants.TAG_PATIENT_PROFILE, Parcels.wrap(patientDetails)));
                    ((CommonOpenUpActivity) mActivity).finish();
                    return;
                }
                break;
            case REGISTER_PATIENT:
                if (response.isValidData(response) && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                    RegisteredPatientDetailsUpdated patientDetails = (RegisteredPatientDetailsUpdated) response.getData();
                    LogUtils.LOGD(TAG, "REGISTER_PATIENT SYNC_COMPLETE" + patientDetails.getLocalPatientName());
                    LocalDataServiceImpl.getInstance(mApp).addPatient(patientDetails);
                    refreshContactsData(patientDetails);
                    mActivity.hideLoading();
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_REGISTRATION);
                    ((CommonOpenUpActivity) mActivity).finish();
                    return;
                }
                break;
            default:
                break;
        }
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_DISEASE_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getDiseaseList(WebServiceType.GET_DISEASE_LIST, null, BooleanTypeValues.FALSE, null, 0, null, null);
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
        if (!Util.isNullOrBlank(volleyResponseBean.getErrMsg()))
            onErrorResponse(volleyResponseBean, "");
        else
            onResponse(volleyResponseBean);
    }


    @Override
    public void onAddDiseaseClicked(String diseaseId) {
        if (!addFamilyDiseasesList.contains(diseaseId))
            addFamilyDiseasesList.add(diseaseId);
        if (removeFamilyDiseasesList.contains(diseaseId)) {
            removeFamilyDiseasesList.remove(diseaseId);
            if (familyDiseaseIdsList.contains(diseaseId))
                familyDiseaseIdsList.remove(diseaseId);
        }
    }

    @Override
    public void onRemoveDiseaseClicked(String diseaseId) {
        if (!removeFamilyDiseasesList.contains(diseaseId)) {
            removeFamilyDiseasesList.add(diseaseId);
            if (familyDiseaseIdsList.contains(diseaseId))
                familyDiseaseIdsList.remove(diseaseId);
        }
        if (addFamilyDiseasesList.contains(diseaseId))
            addFamilyDiseasesList.remove(diseaseId);
    }

    @Override
    public boolean isDiseaseAdded(String uniqueId) {
        if (addFamilyDiseasesList.contains(uniqueId) || familyDiseaseIdsList.contains(uniqueId))
            return true;
        return false;
    }

    public void initDataFromPreviousFragment(Object object, boolean isEditPatient) {
        this.isEditPatient = isEditPatient;
        patientDetails = (RegisterNewPatientRequest) object;

        if (patientDetails.getFamilyMedicalHistoryHandler() != null) {
            familyDiseaseIdsList = (ArrayList<String>) patientDetails.getFamilyMedicalHistoryHandler().getAddDiseases();
        }
        getListFromLocal();
        if (patientDetails.getPersonalHistoryAddRequest() != null)
            setPersonalHistoryData(patientDetails.getPersonalHistoryAddRequest());
    }

    private void setPersonalHistoryData(PersonalHistory personalHistory) {
        String diet = personalHistory.getDiet();
        String addictions = personalHistory.getAddictions();
        String bowelHabit = personalHistory.getBowelHabit();
        String bladderHabit = personalHistory.getBladderHabit();
        editDiet.setText(Util.getValidatedValue(diet));
        editAddiction.setText(Util.getValidatedValue(addictions));
        editBowelHabits.setText(Util.getValidatedValue(bowelHabit));
        editBladderHabits.setText(Util.getValidatedValue(bladderHabit));
    }

    public void validateData() {
        mActivity.showLoading(false);

        patientDetails.setPersonalHistoryAddRequest(addEditPerosnalHistoryDetails());


        AddMedicalFamilyHistoryRequest addFamilyHistoryRequest = new AddMedicalFamilyHistoryRequest();
        addFamilyHistoryRequest.setHospitalId(user.getForeignHospitalId());
        addFamilyHistoryRequest.setLocationId(user.getForeignLocationId());
        addFamilyHistoryRequest.setDoctorId(user.getUniqueId());
        addFamilyHistoryRequest.setAddDiseases(addFamilyDiseasesList);
        addFamilyHistoryRequest.setRemoveDiseases(removeFamilyDiseasesList);
        if (isEditPatient)
            addFamilyHistoryRequest.setPatientId(patientDetails.getUserId());

        patientDetails.setFamilyMedicalHistoryHandler(addFamilyHistoryRequest);


        if (isEditPatient) {
            WebDataServiceImpl.getInstance(mApp).updatePatient(RegisteredPatientDetailsUpdated.class, patientDetails, this, this);
        } else {
            WebDataServiceImpl.getInstance(mApp).registerNewPatient(RegisteredPatientDetailsUpdated.class, patientDetails, this, this);
        }

    }

    private PersonalHistory addEditPerosnalHistoryDetails() {

        String diet = Util.getValidatedValueOrNull(editDiet);
        String addiction = Util.getValidatedValueOrNull(editAddiction);
        String bowelHabit = Util.getValidatedValueOrNull(editBowelHabits);
        String bladderHabit = Util.getValidatedValueOrNull(editBladderHabits);

        PersonalHistory personalHistory = new PersonalHistory();
        personalHistory.setDiet(diet);
        personalHistory.setAddictions(addiction);
        personalHistory.setBowelHabit(bowelHabit);
        personalHistory.setBladderHabit(bladderHabit);
        personalHistory.setDoctorId(user.getUniqueId());
        personalHistory.setLocationId(user.getForeignLocationId());
        personalHistory.setHospitalId(user.getForeignHospitalId());
        if (isEditPatient)
            personalHistory.setPatientId(patientDetails.getUserId());

        return personalHistory;
    }

    private void refreshContactsData(RegisteredPatientDetailsUpdated patientDetails) {
        LocalDataServiceImpl.getInstance(mApp).addPatient(patientDetails);
        mActivity.syncContacts(false, user);
        Util.sendBroadcast(mApp, ContactsListFragment.INTENT_GET_CONTACT_LIST_LOCAL);
    }
}
