package com.healthcoco.healthcocoplus.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocoplus.enums.PatientProfileScreenType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.views.FontAwesomeButton;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class PatientProfileDetailFragment extends HealthCocoFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised {

    private FontAwesomeButton btEditPatientProfilePastHistory;
    private FontAwesomeButton btEditPatientProfileFamilyHistory;
    private FontAwesomeButton btEditPatientProfilePersonalHistory;
    private FontAwesomeButton btEditPatientProfileDruAndAllergy;
    private FontAwesomeButton btEditPatientProfileGroups;
    private FontAwesomeButton btEditPatientProfilePatientData;
    private FontAwesomeButton btEditPatientProfileNotes;
    private LinearLayout containerPastHistory;
    private LinearLayout containerEditPastHistory;
    private LinearLayout containerFamilyHistory;
    private LinearLayout containerEditFamilyHistory;
    private LinearLayout containerPersonalHistory;
    private LinearLayout containerEditPersonalHistory;
    private LinearLayout containeDrugAndAllergy;
    private LinearLayout containerEditDrugAndAllergy;
    private LinearLayout containerGroups;
    private LinearLayout containerEditGroups;
    private LinearLayout containerPatientData;
    private LinearLayout containerEditPatientData;
    private LinearLayout containerNotes;
    private LinearLayout containerEditNotes;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_profile_deatil, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {
        btEditPatientProfilePastHistory = (FontAwesomeButton) view.findViewById(R.id.bt_edit_patient_profile_past_history);
        btEditPatientProfileFamilyHistory = (FontAwesomeButton) view.findViewById(R.id.bt_edit_patient_profile_family_history);
        btEditPatientProfilePersonalHistory = (FontAwesomeButton) view.findViewById(R.id.bt_edit_patient_profile_personal_history);
        btEditPatientProfileDruAndAllergy = (FontAwesomeButton) view.findViewById(R.id.bt_edit_patient_profile_drug_and_allergy);
        btEditPatientProfileGroups = (FontAwesomeButton) view.findViewById(R.id.bt_edit_patient_profile_groups);
        btEditPatientProfilePatientData = (FontAwesomeButton) view.findViewById(R.id.bt_edit_patient_profile_patient_data);
        btEditPatientProfileNotes = (FontAwesomeButton) view.findViewById(R.id.bt_edit_patient_profile_notes);
        containerPastHistory = (LinearLayout) view.findViewById(R.id.container_past_history);
        containerEditPastHistory = (LinearLayout) view.findViewById(R.id.container_edit_past_history);
        containerFamilyHistory = (LinearLayout) view.findViewById(R.id.container_family_history);
        containerEditFamilyHistory = (LinearLayout) view.findViewById(R.id.container_edit_family_history);
        containerPersonalHistory = (LinearLayout) view.findViewById(R.id.container_personal_history);
        containerEditPersonalHistory = (LinearLayout) view.findViewById(R.id.container_edit_personal_history);
        containeDrugAndAllergy = (LinearLayout) view.findViewById(R.id.container_drug_and_allergy);
        containerEditDrugAndAllergy = (LinearLayout) view.findViewById(R.id.container_edit_drug_and_allergy);
        containerGroups = (LinearLayout) view.findViewById(R.id.container_groups);
        containerEditGroups = (LinearLayout) view.findViewById(R.id.container_edit_groups);
        containerPatientData = (LinearLayout) view.findViewById(R.id.container_patient_data);
        containerEditPatientData = (LinearLayout) view.findViewById(R.id.container_edit_patient_data);
        containerNotes = (LinearLayout) view.findViewById(R.id.container_notes);
        containerEditNotes = (LinearLayout) view.findViewById(R.id.container_edit_notes);
    }

    @Override
    public void initListeners() {
        btEditPatientProfilePastHistory.setOnClickListener(this);
        btEditPatientProfileFamilyHistory.setOnClickListener(this);
        btEditPatientProfilePersonalHistory.setOnClickListener(this);
        btEditPatientProfileDruAndAllergy.setOnClickListener(this);
        btEditPatientProfileGroups.setOnClickListener(this);
        btEditPatientProfilePatientData.setOnClickListener(this);
        btEditPatientProfileNotes.setOnClickListener(this);
    }

    private void initData() {
//        if (selectedPatient != null) {
//            ivContactProfile.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (!Util.isNullOrBlank(selectedPatient.getImageUrl()))
//                        mActivity.openEnlargedImageDialogFragment(selectedPatient.getImageUrl());
//                }
//            });
//            tvPatientName.setText(selectedPatient.getLocalPatientName());
//            tvGenderAge.setText(selectedPatient.getPid());
//            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_DOCTOR_PROFILE, selectedPatient, null, ivContactProfile, tvInitialAlphabet);
//        } else {
//            mActivity.showLoading(false);
//            WebDataServiceImpl.getInstance(mApp).getPatientProfile(RegisteredPatientDetailsUpdated.class, HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
//        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit_patient_profile_past_history:
                break;
            case R.id.bt_edit_patient_profile_family_history:
                break;
            case R.id.bt_edit_patient_profile_personal_history:
                break;
            case R.id.bt_edit_patient_profile_drug_and_allergy:
                break;
            case R.id.bt_edit_patient_profile_groups:
            case R.id.bt_edit_patient_profile_patient_data:
            case R.id.bt_edit_patient_profile_notes:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    openRegistrationFragment(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                else
                    onNetworkUnavailable(null);
                break;
        }
    }

    public void openRegistrationFragment(String patientUniqueId) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_REGISTRATION.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_UNIQUE_ID, patientUniqueId);
        intent.putExtra(HealthCocoConstants.TAG_IS_EDIT_PATIENT, true);
        mActivity.openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_REGISTRATION, intent,
                HealthCocoConstants.REQUEST_CODE_CONTACTS_DETAIL);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId()) && selectedPatient != null && !Util.isNullOrBlank(selectedPatient.getUserId())) {
                    user = doctor.getUser();
                }
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        super.onResponse(response);
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    initData();
                    break;
                case GET_PATIENT_PROFILE:
                    if (response.getData() != null && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                        selectedPatient = (RegisteredPatientDetailsUpdated) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addPatient(selectedPatient);
                        initData();
                    }
                    break;
            }
        }
        mActivity.hideLoading();
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
    }
}
