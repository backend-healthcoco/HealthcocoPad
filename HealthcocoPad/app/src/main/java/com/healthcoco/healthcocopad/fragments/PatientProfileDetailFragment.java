package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.Address;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugsAndAllergies;
import com.healthcoco.healthcocopad.bean.server.HistoryDetailsResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MedicalFamilyHistoryDetails;
import com.healthcoco.healthcocopad.bean.server.MedicalFamilyHistoryResponse;
import com.healthcoco.healthcocopad.bean.server.Patient;
import com.healthcoco.healthcocopad.bean.server.PersonalHistory;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddEditDrugAndAllergyDetailDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddEditPersonalHistoryDetailDialogFragment;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.HistoryFilterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HistoryDiseaseIdsListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.FontAwesomeButton;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class PatientProfileDetailFragment extends HealthCocoFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, HistoryDiseaseIdsListener {
    public static final String INTENT_GET_HISTORY_LIST = "com.healthcoco.HISTORY_LIST";
    public static final String INTENT_GET_MEDICAL_PERSONAL_FAMILY_LIST = "com.healthcoco.MEDICAL_PERSONAL_FAMILY_HISTORY";
    public static final String INTENT_GET_HISTORY_LIST_LOCAL = "com.healthcoco.HISTORY_LIST_LOCAL";
    public static final String INTENT_GET_MEDICAL_PERSONAL_FAMILY_LIST_LOCAL = "com.healthcoco.MEDICAL_PERSONAL_FAMILY_LIST_LOCAL";
    public static final String TAG_PERSONAL_HISTORY = "patientHistory";
    public static final String DRUG_AND_ALLERGIES = "drugsAndAllegies";
    private boolean receiversRegistered = false;
    private FontAwesomeButton btEditPatientProfilePastHistory;
    private FontAwesomeButton btEditPatientProfileFamilyHistory;
    private FontAwesomeButton btEditPatientProfilePersonalHistory;
    private FontAwesomeButton btEditPatientProfileDruAndAllergy;
    private FontAwesomeButton btEditPatientProfileGroups;
    private FontAwesomeButton btEditPatientProfilePatientData;
    private FontAwesomeButton btEditPatientProfileNotes;
    private LinearLayout mainContainerPastHistory;
    private LinearLayout mainContainerFamilyHistory;
    private LinearLayout mainContainerPersonalHistory;
    private LinearLayout mainContainerDrugAndAllergy;
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
    private HistoryDiseaseIdsListener addDiseaseIdsListener;
    private MedicalFamilyHistoryResponse medicalHistoryResponse;
    private HistoryDetailsResponse historyDetailsResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_profile_deatil, container, false);
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
        initViews();
        initListeners();
        hideAllMainLayouts();
        getUserAndPatientDetails();
        initData();
    }

    private void getUserAndPatientDetails() {
        CommonOpenUpPatientDetailFragment patientDetailFragmentUpdated = (CommonOpenUpPatientDetailFragment) getFragmentManager().findFragmentByTag(CommonOpenUpPatientDetailFragment.class.getSimpleName());
        if (patientDetailFragmentUpdated != null) {
            selectedPatient = patientDetailFragmentUpdated.getSelectedPatientDetails();
            user = patientDetailFragmentUpdated.getUser();
            patientDetailFragmentUpdated.initFloatingActionButton(this);
        }
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
        mainContainerPersonalHistory = (LinearLayout) view.findViewById(R.id.container_personal_history);
        mainContainerDrugAndAllergy = (LinearLayout) view.findViewById(R.id.container_drug_and_allergy);
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
            initProfileData();
            initGroups();
            initNotes();
        }
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

    private void addHistoryDetailInLayout(HistoryDetailsResponse historyDetailsResponses) {
        if (historyDetailsResponses != null) {
            initPastHistory(historyDetailsResponses.getMedicalhistory(), this);
            initFamilyHistory(historyDetailsResponses.getFamilyhistory(), this);
            initDrugsAndAllergyHistory(historyDetailsResponses.getDrugsAndAllergies());
            initPersonalHistory(historyDetailsResponses.getPersonalHistory());
        }
    }

    private void initPersonalHistory(PersonalHistory personalHistory) {
        TextView tvDiet = (TextView) mainContainerPersonalHistory.findViewById(R.id.tv_diet);
        LinearLayout containerDiet = (LinearLayout) mainContainerPersonalHistory.findViewById(R.id.container_diet);
        TextView tvAddiction = (TextView) mainContainerPersonalHistory.findViewById(R.id.tv_addictions);
        LinearLayout containerAddiction = (LinearLayout) mainContainerPersonalHistory.findViewById(R.id.container_addiction);
        TextView tvBowelHabits = (TextView) mainContainerPersonalHistory.findViewById(R.id.tv_bowel_habits);
        LinearLayout containerBowelHabit = (LinearLayout) mainContainerPersonalHistory.findViewById(R.id.container_bowel_habits);
        TextView tvBladderHabit = (TextView) mainContainerPersonalHistory.findViewById(R.id.tv_bladder_habits);
        LinearLayout containerBladderHabit = (LinearLayout) mainContainerPersonalHistory.findViewById(R.id.container_bladder_habits);
        TextView tvNoData = (TextView) mainContainerPersonalHistory.findViewById(R.id.tv_no_data);
        tvNoData.setVisibility(View.GONE);
        containerDiet.setVisibility(View.GONE);
        containerAddiction.setVisibility(View.GONE);
        containerBowelHabit.setVisibility(View.GONE);
        containerBladderHabit.setVisibility(View.GONE);
        if (personalHistory != null) {

            if (!Util.isNullOrBlank(personalHistory.getDiet())) {
                containerDiet.setVisibility(View.VISIBLE);
                tvDiet.setText(personalHistory.getDiet());
            }
            if (!Util.isNullOrBlank(personalHistory.getAddictions())) {
                containerAddiction.setVisibility(View.VISIBLE);
                tvAddiction.setText(personalHistory.getAddictions());
            }
            if (!Util.isNullOrBlank(personalHistory.getBowelHabit())) {
                containerBowelHabit.setVisibility(View.VISIBLE);
                tvBowelHabits.setText(personalHistory.getBowelHabit());
            }
            if (!Util.isNullOrBlank(personalHistory.getBladderHabit())) {
                containerBladderHabit.setVisibility(View.VISIBLE);
                tvBladderHabit.setText(personalHistory.getBladderHabit());
            }
        } else {
            containerDiet.setVisibility(View.GONE);
            containerAddiction.setVisibility(View.GONE);
            containerBowelHabit.setVisibility(View.GONE);
            containerBladderHabit.setVisibility(View.GONE);
            tvNoData.setVisibility(View.VISIBLE);
        }
        mainContainerPersonalHistory.setVisibility(View.VISIBLE);
    }

    private void initDrugsAndAllergyHistory(DrugsAndAllergies drugsAndAllergies) {
        TextView tvAllergy = (TextView) mainContainerDrugAndAllergy.findViewById(R.id.tv_allergy);
        LinearLayout containerAllergy = (LinearLayout) mainContainerDrugAndAllergy.findViewById(R.id.container_allergy);
        LinearLayout containerDrugs = (LinearLayout) mainContainerDrugAndAllergy.findViewById(R.id.container_drugs);
        LinearLayout containerDrugsDetail = (LinearLayout) mainContainerDrugAndAllergy.findViewById(R.id.container_drugs_detail);
        TextView tvNoData = (TextView) mainContainerDrugAndAllergy.findViewById(R.id.tv_no_data);

        containerDrugsDetail.removeAllViews();
        tvNoData.setVisibility(View.GONE);
        containerAllergy.setVisibility(View.GONE);
        containerDrugs.setVisibility(View.GONE);

        if (drugsAndAllergies != null) {
            if (!Util.isNullOrBlank(drugsAndAllergies.getAllergies())) {
                containerAllergy.setVisibility(View.VISIBLE);
                tvAllergy.setText(drugsAndAllergies.getAllergies());
            }
            if (!Util.isNullOrEmptyList(drugsAndAllergies.getDrugs())) {
                containerDrugs.setVisibility(View.VISIBLE);
                for (Drug drug : drugsAndAllergies.getDrugs()) {
                    TextView tvDrugs = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                    tvDrugs.setText(drug.getFormattedDrugName());
                    containerDrugsDetail.addView(tvDrugs);
                }
            }
        } else {
            tvNoData.setVisibility(View.VISIBLE);
            containerAllergy.setVisibility(View.GONE);
            containerDrugs.setVisibility(View.GONE);
        }
        mainContainerDrugAndAllergy.setVisibility(View.VISIBLE);
    }

    private void initPastHistory(List<MedicalFamilyHistoryDetails> medicalhistory, HistoryDiseaseIdsListener addDiseaseIdsListener) {
        LinearLayout containerPastHistory = (LinearLayout) mainContainerPastHistory.findViewById(R.id.container_past_history);
        containerPastHistory.removeAllViews();
        if (!Util.isNullOrEmptyList(medicalhistory)) {
            for (MedicalFamilyHistoryDetails familyHistoryDetails :
                    medicalhistory) {
                TextView tvGroupName = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                tvGroupName.setText(familyHistoryDetails.getDisease());
                addDiseaseIdsListener.addDiseaseId(HistoryFilterType.MEDICAL_HISTORY, familyHistoryDetails.getUniqueId());
                containerPastHistory.addView(tvGroupName);
            }
        } else {
            TextView tvGroupName = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
            tvGroupName.setText(R.string.edit_to_assign_past_history);
            containerPastHistory.addView(tvGroupName);
        }
        mainContainerPastHistory.setVisibility(View.VISIBLE);
    }

    private void initFamilyHistory(List<MedicalFamilyHistoryDetails> familyhistory, HistoryDiseaseIdsListener addDiseaseIdsListener) {
        LinearLayout containerFamilyHistory = (LinearLayout) mainContainerFamilyHistory.findViewById(R.id.container_family_history);
        containerFamilyHistory.removeAllViews();
        if (!Util.isNullOrEmptyList(familyhistory)) {
            for (MedicalFamilyHistoryDetails familyHistoryDetails :
                    familyhistory) {
                TextView tvGroupName = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                tvGroupName.setText(familyHistoryDetails.getDisease());
                addDiseaseIdsListener.addDiseaseId(HistoryFilterType.FAMILY_HISTORY, familyHistoryDetails.getUniqueId());
                containerFamilyHistory.addView(tvGroupName);
            }
        } else {
            TextView tvGroupName = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
            tvGroupName.setText(R.string.edit_to_assign_past_history);
            containerFamilyHistory.addView(tvGroupName);
        }
        mainContainerFamilyHistory.setVisibility(View.VISIBLE);
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
        if (selectedPatient.getPatient() != null && !Util.isNullOrEmptyList(selectedPatient.getPatient().getNotes())) {
            for (String note :
                    selectedPatient.getPatient().getNotes()) {
                TextView tvNote = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                tvNote.setText(note);
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
                break;
            case R.id.bt_edit_patient_profile_family_history:
                openHisoryFragment(HistoryFilterType.FAMILY_HISTORY);
                break;
            case R.id.bt_edit_patient_profile_personal_history:
                HistoryDetailsResponse detailsResponse = getDefaultIdDetails();
                detailsResponse.setPersonalHistory(historyDetailsResponse.getPersonalHistory());
                openDialogFragment(new AddEditPersonalHistoryDetailDialogFragment(), AddEditPersonalHistoryDetailDialogFragment.TAG_PERSONAL_HISTORY, detailsResponse, HealthCocoConstants.REQUEST_CODE_PATIENT_PROFILE, null);
                break;
            case R.id.bt_edit_patient_profile_drug_and_allergy:
                HistoryDetailsResponse detailsResponse1 = getDefaultIdDetails();
                detailsResponse1.setDrugsAndAllergies(historyDetailsResponse.getDrugsAndAllergies());
                openDialogFragment(new AddEditDrugAndAllergyDetailDialogFragment(), AddEditDrugAndAllergyDetailDialogFragment.TAG_DRUGS_AND_ALLERGIES, detailsResponse1, HealthCocoConstants.REQUEST_CODE_PATIENT_PROFILE, null);
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

    private HistoryDetailsResponse getDefaultIdDetails() {
        HistoryDetailsResponse detailsResponse = new HistoryDetailsResponse();
        detailsResponse.setDoctorId(selectedPatient.getDoctorId());
        detailsResponse.setLocationId(selectedPatient.getLocationId());
        detailsResponse.setHospitalId(selectedPatient.getHospitalId());
        detailsResponse.setPatientId(selectedPatient.getPatient().getPatientId());
        return detailsResponse;
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
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_PATIENT_PROFILE);
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
            } else if (resultCode == HealthCocoConstants.RESULT_CODE_DOCTOR_PERSONAL_HISTORY_DETAIL) {
                PersonalHistory personalHistory = Parcels.unwrap(data.getParcelableExtra(TAG_PERSONAL_HISTORY));
                historyDetailsResponse.setPersonalHistory(personalHistory);
                initPersonalHistory(personalHistory);
            } else if (resultCode == HealthCocoConstants.RESULT_CODE_DRUGS_AND_ALLERGIES_DETAIL) {
                DrugsAndAllergies drugsAndAllergies = Parcels.unwrap(data.getParcelableExtra(DRUG_AND_ALLERGIES));
                historyDetailsResponse.setDrugsAndAllergies(drugsAndAllergies);
                initDrugsAndAllergyHistory(drugsAndAllergies);
            } else if (resultCode == HealthCocoConstants.RESULT_CODE_DISEASE_LIST) {

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
                checkPatientStatus();
                getListFromLocal(true);
                break;
            case ADD_HISTORY_LIST:
                LocalDataServiceImpl.getInstance(mApp).addHistoryList(HealthCocoConstants.SELECTED_PATIENTS_USER_ID, (ArrayList<HistoryDetailsResponse>) (ArrayList<?>) response.getDataList());
            case GET_HISTORY_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getHistoryList(WebServiceType.GET_HISTORY_LIST, BooleanTypeValues.FALSE, isOtpVerified(), user.getUniqueId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, null, null);
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    public void checkPatientStatus() {
        checkPatientStatus(user, selectedPatient);
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
                    historyDetailsResponse = new HistoryDetailsResponse();
                    if (response.getData() != null && response.getData() instanceof HistoryDetailsResponse) {
                        historyDetailsResponse = (HistoryDetailsResponse) response.getData();
                    }
                    addHistoryDetailInLayout(historyDetailsResponse);
                    if (response.isDataFromLocal() && isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getHistoryList(true);
                        return;
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
//                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_HISTORY_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
//                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    break;
            }
        }
        mActivity.hideLoading();
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
                WebDataServiceImpl.getInstance(mApp).getHistoryListUpdatedAPI(HistoryDetailsResponse.class, WebServiceType.GET_HISTORY_LIST, isOtpVerified(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
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

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for history list refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_HISTORY_LIST);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(historyListReceiver, filter);

            //receiver for history  list refresh from local
            IntentFilter filter2 = new IntentFilter();
            filter2.addAction(INTENT_GET_HISTORY_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(historyListLocalReceiver, filter2);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(historyListReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(historyListLocalReceiver);
    }

    BroadcastReceiver historyListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (isHistoryLoaded) {
                isHistoryLoaded = false;
                getHistoryList(true);
            }
        }
    };

    BroadcastReceiver historyListLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getListFromLocal(false);
        }
    };

    public void getListFromLocal(boolean showLoading) {
        if (user != null) {
            this.user = user;
            isInitialLoading = showLoading;
            if (showLoading) {
                showLoadingOverlay(true);
            }
            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_HISTORY_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    @Override
    public void addDiseaseId(HistoryFilterType historyFilterType, String diseaseId) {
        switch (historyFilterType) {
            case MEDICAL_HISTORY:
                if (!medicalDiseaseIdsList.contains(diseaseId))
                    medicalDiseaseIdsList.add(diseaseId);
                break;
            case FAMILY_HISTORY:
                if (!familyDiseaseIdsList.contains(diseaseId))
                    familyDiseaseIdsList.add(diseaseId);
                break;
        }
    }
}
