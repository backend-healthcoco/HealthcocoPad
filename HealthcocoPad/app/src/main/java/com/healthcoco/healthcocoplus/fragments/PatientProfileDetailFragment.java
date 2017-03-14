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
import com.healthcoco.healthcocoplus.HealthCocoDialogFragment;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.Address;
import com.healthcoco.healthcocoplus.bean.server.HistoryDetailsResponse;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.MedicalFamilyHistoryDetails;
import com.healthcoco.healthcocoplus.bean.server.MedicalFamilyHistoryResponse;
import com.healthcoco.healthcocoplus.bean.server.NotesTable;
import com.healthcoco.healthcocoplus.bean.server.Patient;
import com.healthcoco.healthcocoplus.bean.server.Reference;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.bean.server.UserGroups;
import com.healthcoco.healthcocoplus.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocoplus.dialogFragment.AddEditPersonalHistoryDetailDialogFragment;
import com.healthcoco.healthcocoplus.enums.BooleanTypeValues;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.enums.HistoryFilterType;
import com.healthcoco.healthcocoplus.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocoplus.enums.LocalTabelType;
import com.healthcoco.healthcocoplus.enums.PatientProfileScreenType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.listeners.HistoryDiseaseIdsListener;
import com.healthcoco.healthcocoplus.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.ComparatorUtil;
import com.healthcoco.healthcocoplus.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.views.FontAwesomeButton;

import java.util.ArrayList;
import java.util.Collections;

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
    private LinearLayout mainContainerPastHistory;
    private LinearLayout mainContainerFamilyHistory;
    private LinearLayout containerPersonalHistory;
    private LinearLayout containerEditPersonalHistory;
    private LinearLayout containeDrugAndAllergy;
    private LinearLayout containerEditDrugAndAllergy;
    private LinearLayout mainContainerGroups;
    private LinearLayout mainContainerNotes;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private TextView tvInitialAlphabet;
    private TextView tvPatientName;
    private TextView tvGenderAge;
    private ImageView ivContactProfile;
    private LinearLayout mainLayoutProfile;
    private ArrayList<String> medicalDiseaseIdsList = new ArrayList<String>();
    private ArrayList<String> familyDiseaseIdsList = new ArrayList<String>();
    private boolean isMedicalFamilyHistoryLoaded;
    private boolean isHistoryLoaded;
    private ArrayList<HistoryDetailsResponse> historyList;
    private boolean isInitialLoading;
    private boolean isOtpVerified = false;
    private HistoryDiseaseIdsListener addDiseaseIdsListener;
    private MedicalFamilyHistoryResponse medicalHistoryResponse;

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
        ((CommonOpenUpActivity) mActivity).initFloatingActionButton(this);
        hideAllMainLayouts();
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
        mainContainerPastHistory = (LinearLayout) view.findViewById(R.id.container_past_history);
        mainContainerFamilyHistory = (LinearLayout) view.findViewById(R.id.container_family_history);
        containerPersonalHistory = (LinearLayout) view.findViewById(R.id.container_personal_history);
        containerEditPersonalHistory = (LinearLayout) view.findViewById(R.id.container_edit_personal_history);
        containeDrugAndAllergy = (LinearLayout) view.findViewById(R.id.container_drug_and_allergy);
        containerEditDrugAndAllergy = (LinearLayout) view.findViewById(R.id.container_edit_drug_and_allergy);
        mainContainerGroups = (LinearLayout) view.findViewById(R.id.container_groups);
        mainContainerNotes = (LinearLayout) view.findViewById(R.id.container_notes);
        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);
        tvPatientName = (TextView) view.findViewById(R.id.tv_name);
        tvGenderAge = (TextView) view.findViewById(R.id.tv_patient_id);
        ivContactProfile = (ImageView) view.findViewById(R.id.iv_image);
        mainLayoutProfile = (LinearLayout) view.findViewById(R.id.main_layout_profile);
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

    private void hideAllMainLayouts() {
        mainLayoutProfile.setVisibility(View.VISIBLE);
        mainContainerGroups.setVisibility(View.GONE);
        mainContainerNotes.setVisibility(View.GONE);
        mainContainerPastHistory.setVisibility(View.GONE);
        mainContainerFamilyHistory.setVisibility(View.GONE);
    }

    private void initData() {
        if (selectedPatient != null) {
            ivContactProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Util.isNullOrBlank(selectedPatient.getImageUrl()))
                        mActivity.openEnlargedImageDialogFragment(selectedPatient.getImageUrl());
                }
            });
            tvPatientName.setText(selectedPatient.getLocalPatientName());
            tvGenderAge.setText(selectedPatient.getPid());
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENT_DEATIL_PROFILE, selectedPatient, null, ivContactProfile, tvInitialAlphabet);
        } else {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).getPatientProfile(RegisteredPatientDetailsUpdated.class, HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
        }
        isOtpVerified();
        initProfileData();
        initGroups();
        initNotes();
    }

    private void initProfileData() {
        LinearLayout containerMobile = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_mobile);
        TextView tvBloodGroup = (TextView) mainLayoutProfile.findViewById(R.id.tv_blood_group);
        LinearLayout containerBloodGroup = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_blood_group);
        TextView tvProfession = (TextView) mainLayoutProfile.findViewById(R.id.tv_profession);
        LinearLayout containerProfession = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_profession);
        TextView tvReferredBy = (TextView) mainLayoutProfile.findViewById(R.id.tv_referred_by);
        LinearLayout containerReferredBy = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_referred_by);
        TextView tvAadharId = (TextView) mainLayoutProfile.findViewById(R.id.tv_aadhar_id);
        LinearLayout containerAadharId = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_aadhar_id);
        TextView tvPamNumber = (TextView) mainLayoutProfile.findViewById(R.id.tv_pan_number);
        LinearLayout containerPanNumber = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_pan_number);
        TextView tvDrivingLicence = (TextView) mainLayoutProfile.findViewById(R.id.tv_driving_license);
        LinearLayout containerDrivingLicense = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_driving_license);
        LinearLayout containerSecondaryMobile = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_secondary_mobile);
        LinearLayout containerEmail = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_email);
        LinearLayout containerStreetAddress = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_street_address);
        LinearLayout containerLocality = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_locality);
        LinearLayout containerCity = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_city);
        LinearLayout containerCountry = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_country);
        LinearLayout containerPinCode = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_pin_code);
        boolean isViewVisible = false;
        containerBloodGroup.setVisibility(View.GONE);
        containerProfession.setVisibility(View.GONE);
        containerReferredBy.setVisibility(View.GONE);
        containerAadharId.setVisibility(View.GONE);
        containerPanNumber.setVisibility(View.GONE);
        containerDrivingLicense.setVisibility(View.GONE);
        //initialising mobile data
        if (!Util.isNullOrBlank(selectedPatient.getMobileNumber())) {
            containerMobile.setVisibility(View.VISIBLE);
            TextView tvMobileno = (TextView) containerMobile.findViewById(R.id.tv_mobile);
            tvMobileno.setText(selectedPatient.getMobileNumber());
            isViewVisible = true;
        } else
            containerMobile.setVisibility(View.GONE);

        if (selectedPatient.getPatient() != null) {
            Patient patient = selectedPatient.getPatient();
            if (!Util.isNullOrBlank(patient.getBloodGroup())) {
                containerBloodGroup.setVisibility(View.VISIBLE);
                tvBloodGroup.setText(patient.getBloodGroup());
            }
            if (!Util.isNullOrBlank(patient.getProfession())) {
                containerProfession.setVisibility(View.VISIBLE);
                tvProfession.setText(patient.getProfession());
            }
            if (!Util.isNullOrBlank(patient.getAdhaarId())) {
                containerAadharId.setVisibility(View.VISIBLE);
                tvAadharId.setText(patient.getAdhaarId());
            }
            if (!Util.isNullOrBlank(patient.getPanCardNumber())) {
                containerPanNumber.setVisibility(View.VISIBLE);
                tvPamNumber.setText(patient.getPanCardNumber());
            }
            if (!Util.isNullOrBlank(patient.getDrivingLicenseId())) {
                containerDrivingLicense.setVisibility(View.VISIBLE);
                tvDrivingLicence.setText(patient.getDrivingLicenseId());
            }
        }
        if (selectedPatient.getReferredBy() != null && !Util.isNullOrBlank(selectedPatient.getReferredBy().getReference())) {
            Reference reference = selectedPatient.getReferredBy();
            containerReferredBy.setVisibility(View.VISIBLE);
            tvReferredBy.setText(reference.getReference());
        }
        //initialising Email Address
        if (selectedPatient.getPatient() != null) {
            Patient patient = selectedPatient.getPatient();
            if (!Util.isNullOrBlank(patient.getEmailAddress())) {
                containerEmail.setVisibility(View.VISIBLE);
                TextView tvEmail = (TextView) containerEmail.findViewById(R.id.tv_email);
                tvEmail.setText(selectedPatient.getPatient().getEmailAddress());
                isViewVisible = true;
            } else
                containerEmail.setVisibility(View.GONE);

            //initialising secondary mobile data
            if (!Util.isNullOrBlank(patient.getSecMobile())) {
                containerSecondaryMobile.setVisibility(View.VISIBLE);
                TextView tvSecondaryMobile = (TextView) containerSecondaryMobile.findViewById(R.id.tv_secondary_mobile);
                tvSecondaryMobile.setText(patient.getSecMobile());
                isViewVisible = true;
            } else
                containerSecondaryMobile.setVisibility(View.GONE);
        }

        //initialising address data
        if (selectedPatient.getAddress() != null) {
            Address address = selectedPatient.getAddress();

            //setting Street Address
            if (!Util.isNullOrBlank(address.getStreetAddress())) {
                containerStreetAddress.setVisibility(View.VISIBLE);
                TextView tvStreetAddress = (TextView) containerStreetAddress.findViewById(R.id.tv_street_address);
                tvStreetAddress.setText(address.getStreetAddress());
                isViewVisible = true;
            } else
                containerStreetAddress.setVisibility(View.GONE);

            //setting locality
            if (!Util.isNullOrBlank(address.getLocality())) {
                containerLocality.setVisibility(View.VISIBLE);
                TextView tvLocality = (TextView) containerLocality.findViewById(R.id.tv_locality);
                tvLocality.setText(address.getLocality());
                isViewVisible = true;
            } else
                containerLocality.setVisibility(View.GONE);

            //setting city
            if (!Util.isNullOrBlank(address.getCity())) {
                containerCity.setVisibility(View.VISIBLE);
                TextView tvCity = (TextView) containerCity.findViewById(R.id.tv_city);
                tvCity.setText(address.getCity());
                isViewVisible = true;
            } else
                containerCity.setVisibility(View.GONE);

            //setting country
            if (!Util.isNullOrBlank(address.getCountry())) {
                containerCountry.setVisibility(View.VISIBLE);
                TextView tvCountry = (TextView) containerCountry.findViewById(R.id.tv_country);
                tvCountry.setText(address.getCountry());
                isViewVisible = true;
            } else
                containerCountry.setVisibility(View.GONE);
            //setting country
            if (!Util.isNullOrBlank(address.getPostalCode())) {
                containerPinCode.setVisibility(View.VISIBLE);
                TextView tvPincode = (TextView) containerPinCode.findViewById(R.id.tv_pin_code);
                tvPincode.setText(address.getPostalCode());
                isViewVisible = true;
            } else
                containerPinCode.setVisibility(View.GONE);
        } else {
            containerCity.setVisibility(View.GONE);
            containerLocality.setVisibility(View.GONE);
            containerStreetAddress.setVisibility(View.GONE);
            containerCountry.setVisibility(View.GONE);
            containerPinCode.setVisibility(View.GONE);
        }
        mainLayoutProfile.setVisibility(View.VISIBLE);
    }

    private void initHistory(MedicalFamilyHistoryResponse medicalHistoryResponse) {
        LinearLayout containerGroups = (LinearLayout) mainContainerPastHistory.findViewById(R.id.container_past_history);
        containerGroups.removeAllViews();
        if (medicalHistoryResponse != null && (!Util.isNullOrEmptyList(medicalHistoryResponse.getMedicalhistory()))) {
            for (MedicalFamilyHistoryDetails familyHistoryResponse :
                    medicalHistoryResponse.getMedicalhistory()) {
                TextView tvGroupName = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                tvGroupName.setText(familyHistoryResponse.getDisease());
                containerGroups.addView(tvGroupName);
            }
        } else {
            TextView tvGroupName = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
            tvGroupName.setText(R.string.edit_to_assign_past_history);
            containerGroups.addView(tvGroupName);
        }
        mainContainerGroups.setVisibility(View.VISIBLE);
    }

    private void initGroups() {
        LinearLayout containerGroups = (LinearLayout) mainContainerGroups.findViewById(R.id.container_groups);
        containerGroups.removeAllViews();
        if (!Util.isNullOrEmptyList(selectedPatient.getGroups())) {
            for (UserGroups group :
                    selectedPatient.getGroups()) {
                TextView tvGroupName = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                tvGroupName.setText(group.getName());
                containerGroups.addView(tvGroupName);
            }
        } else {
            TextView tvGroupName = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
            tvGroupName.setText(R.string.edit_to_assign_groups);
            containerGroups.addView(tvGroupName);
        }
        mainContainerGroups.setVisibility(View.VISIBLE);
    }

    private void initNotes() {
        LinearLayout containerNotes = (LinearLayout) mainContainerNotes.findViewById(R.id.container_notes);
        containerNotes.removeAllViews();
        if (selectedPatient.getPatient() != null && !Util.isNullOrEmptyList(selectedPatient.getPatient().getNotesTableList())) {
            for (NotesTable note :
                    selectedPatient.getPatient().getNotesTableList()) {
                TextView tvNote = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                tvNote.setText(note.getNote());
                containerNotes.addView(tvNote);
            }
        } else {
            TextView tvNote = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
            tvNote.setText(R.string.edit_to_add_notes);
            containerNotes.addView(tvNote);
        }

        mainContainerNotes.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit_patient_profile_past_history:
                openHisoryFragment(HistoryFilterType.MEDICAL_HISTORY);
//                if (!medicalDiseaseIdsList.contains(medicalHistoryResponse.getUniqueId()))
//                    medicalDiseaseIdsList.add(medicalHistoryResponse.getUniqueId());
                break;
            case R.id.bt_edit_patient_profile_family_history:
                openHisoryFragment(HistoryFilterType.FAMILY_HISTORY);
//                if (!familyDiseaseIdsList.contains(medicalHistoryResponse.getUniqueId()))
//                    familyDiseaseIdsList.add(medicalHistoryResponse.getUniqueId());
                break;
            case R.id.bt_edit_patient_profile_personal_history:
                openDialogFragment(HealthCocoConstants.REQUEST_CODE_PATIENT_PROFILE, new AddEditPersonalHistoryDetailDialogFragment());
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

    public boolean isOtpVerified() {
        return super.isOtpVerified();
    }

    private void openHisoryFragment(HistoryFilterType historyFilterType) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        switch (historyFilterType) {
            case MEDICAL_HISTORY:
                intent.putExtra(HealthCocoConstants.TAG_DISEASE_IDS_LIST, medicalDiseaseIdsList);
                break;
            case FAMILY_HISTORY:
                intent.putExtra(HealthCocoConstants.TAG_DISEASE_IDS_LIST, familyDiseaseIdsList);
                break;
        }
        intent.putExtra(HealthCocoConstants.TAG_HISTORY_FILTER_TYPE, historyFilterType.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.HISTORY_DISEASE_LIST.ordinal());
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_HISTORY_LIST);
    }

    private void openDialogFragment(int requestCode, HealthCocoDialogFragment dialogFragment) {
        dialogFragment.setTargetFragment(this, requestCode);
        dialogFragment.show(mFragmentManager, dialogFragment.getClass().getSimpleName());
    }

    public void openRegistrationFragment(String patientUniqueId) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_REGISTRATION.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_UNIQUE_ID, patientUniqueId);
        intent.putExtra(HealthCocoConstants.TAG_IS_EDIT_PATIENT, true);
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_PATIENT_PROFILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_PATIENT_PROFILE) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_REGISTRATION) {
                mActivity.showLoading(false);
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
        }
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
            case ADD_MEDICAL_HISTORY:
                LocalDataServiceImpl.getInstance(mApp).addMedicalHistory(HealthCocoConstants.SELECTED_PATIENTS_USER_ID, (MedicalFamilyHistoryResponse) response.getData());
            case GET_MEDICAL_AND_FAMILY_HISTORY:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getMedicalFAmilyHistory(WebServiceType.GET_MEDICAL_AND_FAMILY_HISTORY, HealthCocoConstants.SELECTED_PATIENTS_USER_ID, null, null);
                break;
            case ADD_HISTORY_LIST:
                LocalDataServiceImpl.getInstance(mApp).addHistoryList(HealthCocoConstants.SELECTED_PATIENTS_USER_ID, (ArrayList<HistoryDetailsResponse>) (ArrayList<?>) response.getDataList());
            case GET_HISTORY_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getHistoryList(WebServiceType.GET_HISTORY_LIST, BooleanTypeValues.FALSE, isOtpVerified, user.getUniqueId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, null, null);
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
                case GET_HISTORY_LIST:
                    isHistoryLoaded = true;
                    if (response.isDataFromLocal()) {
                        historyList = (ArrayList<HistoryDetailsResponse>) (ArrayList<?>) response
                                .getDataList();
                        if (!Util.isNullOrEmptyList(historyList))
                            LogUtils.LOGD(TAG, "Success onResponse historyList Size " + historyList.size() + " isDataFromLocal " + response.isDataFromLocal());
                        notifyAdapter(historyList);
                        if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            getHistoryList(true);
                            return;
                        }
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_HISTORY_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    if (!isMedicalFamilyHistoryLoaded)
                        getMedicalFamilyHistoryFromLocal(false);
                    break;
                case GET_MEDICAL_AND_FAMILY_HISTORY:
                    isMedicalFamilyHistoryLoaded = true;
                    medicalHistoryResponse = new MedicalFamilyHistoryResponse();
                    if (response.getData() != null && response.getData() instanceof MedicalFamilyHistoryResponse) {
                        medicalHistoryResponse = (MedicalFamilyHistoryResponse) response.getData();
                    }
                    initHistory(medicalHistoryResponse);
                    addMedicalFamilyHistoryInLayout(medicalHistoryResponse);
                    if (response.isDataFromLocal() && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getMedicalFamilyHistory(false);
                        return;
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_MEDICAL_HISTORY, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void addMedicalFamilyHistoryInLayout(MedicalFamilyHistoryResponse medicalHistoryResponse) {

    }

    private void getMedicalFamilyHistoryFromLocal(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_MEDICAL_AND_FAMILY_HISTORY, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void notifyAdapter(ArrayList<HistoryDetailsResponse> historyList) {
        if (!Util.isNullOrEmptyList(historyList)) {
            Collections.sort(historyList, ComparatorUtil.historyDateComparator);

        }
    }

    public void getHistoryList(boolean showLoading) {
        try {
            if (user != null) {
                if (showLoading)
                    showLoadingOverlay(true);
                else showLoadingOverlay(false);
                Long updatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.HISTORY_DETAIL_RESPONSE);
                boolean isInHistory = false;
                if (updatedTime == 0) {
                    isInHistory = true;
                }
                WebDataServiceImpl.getInstance(mApp).getHistoryListUpdatedAPI(HistoryDetailsResponse.class, WebServiceType.GET_HISTORY_LIST, isOtpVerified, HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                        isInHistory, updatedTime, this, this);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getMedicalFamilyHistory(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        WebDataServiceImpl.getInstance(mApp).getMedicalFamilyHistory(MedicalFamilyHistoryResponse.class, WebServiceType.GET_MEDICAL_AND_FAMILY_HISTORY, HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        if (volleyResponseBean != null && volleyResponseBean.getWebServiceType() != null)
            switch (volleyResponseBean.getWebServiceType()) {
                case GET_HISTORY_LIST:
                    isHistoryLoaded = true;
                    break;
                case GET_MEDICAL_AND_FAMILY_HISTORY:
                    isMedicalFamilyHistoryLoaded = true;
                    break;
            }
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
