package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MedicalFamilyHistoryDetails;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.HistoryFilterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HistoryDiseaseIdsListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Prashant on 14/07/2018.
 */

public class AddEditMedicalInformationFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, HistoryDiseaseIdsListener {

    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;

    private LinearLayout containerPastHistory;
    private LinearLayout containerFamilyHistory;
    private TextView tvNoDataText;

    private Button addPastHistory;
    private Button addFamilyHistory;

    private ArrayList<String> pastHistoryIdsList = new ArrayList<String>();
    private ArrayList<String> familyHistoryIdsList = new ArrayList<String>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_medical_information, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
    }

    @Override
    public void initViews() {

//        editFromTimeWorkingHrs = (TextView) view.findViewById(R.id.edit_working_hrs_from_time);
        containerPastHistory = (LinearLayout) view.findViewById(R.id.container_past_history);
        containerFamilyHistory = (LinearLayout) view.findViewById(R.id.container_family_history);
        addPastHistory = (Button) view.findViewById(R.id.bt_add_past_history);
        addFamilyHistory = (Button) view.findViewById(R.id.bt_add_family_history);
    }

    @Override
    public void initListeners() {
        addPastHistory.setOnClickListener(this);
        addFamilyHistory.setOnClickListener(this);
    }

    public void initData() {
    }

    private void initAdapter() {
    }


    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null && !Util.isNullOrBlank(user.getUniqueId())) {
                        if (selectedPatient != null) {

                        }
                    }
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null) {
                    user = doctor.getUser();
                    selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
//                    doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
//                    registeredDoctorProfileList = LocalDataServiceImpl.getInstance(mApp).getRegisterDoctorDetails(user.getForeignLocationId());
                }
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_past_history:
                break;
            case R.id.bt_add_family_history:
                break;
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
//        String selectedDate = String.valueOf(tvSelectedDate.getText()).trim();

        if (Util.isNullOrBlank(msg)) {
            addLifeStyle();
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addLifeStyle() {

//        Util.getValidatedValueOrNull(editFromTimeWorkingHrs);
    }

    private void openHisoryFragment(HistoryFilterType historyFilterType) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        switch (historyFilterType) {
            case MEDICAL_HISTORY:
                intent.putExtra(HealthCocoConstants.TAG_DISEASE_IDS_LIST, pastHistoryIdsList);
                break;
            case FAMILY_HISTORY:
                intent.putExtra(HealthCocoConstants.TAG_DISEASE_IDS_LIST, familyHistoryIdsList);
                break;
        }
        intent.putExtra(HealthCocoConstants.TAG_HISTORY_FILTER_TYPE, historyFilterType.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.HISTORY_DISEASE_LIST.ordinal());
//        startActivityForResult(intent, REQUEST_CODE_PATIENT_PROFILE);
    }


    private void initPastHistory(List<MedicalFamilyHistoryDetails> medicalhistory, HistoryDiseaseIdsListener addDiseaseIdsListener) {
        containerPastHistory.removeAllViews();
        pastHistoryIdsList.clear();
        if (!Util.isNullOrEmptyList(medicalhistory)) {
            for (MedicalFamilyHistoryDetails familyHistoryDetails :
                    medicalhistory) {
                TextView tvGroupName = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                tvGroupName.setText(familyHistoryDetails.getDisease());
                addDiseaseIdsListener.addDiseaseId(HistoryFilterType.MEDICAL_HISTORY, familyHistoryDetails.getUniqueId());
                containerPastHistory.addView(tvGroupName);
            }
        } else {
            showNoHistoryText(R.string.edit_to_assign_past_history);
            containerPastHistory.addView(tvNoDataText);
        }
    }


    private void showNoHistoryText(int textId) {
        tvNoDataText = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
        tvNoDataText.setText(textId);
    }

    @Override
    public void addDiseaseId(HistoryFilterType historyFilterType, String diseaseId) {
        switch (historyFilterType) {
            case MEDICAL_HISTORY:
                if (!pastHistoryIdsList.contains(diseaseId))
                    pastHistoryIdsList.add(diseaseId);
                break;
            case FAMILY_HISTORY:
                if (!familyHistoryIdsList.contains(diseaseId))
                    familyHistoryIdsList.add(diseaseId);
                break;
        }
    }
}
