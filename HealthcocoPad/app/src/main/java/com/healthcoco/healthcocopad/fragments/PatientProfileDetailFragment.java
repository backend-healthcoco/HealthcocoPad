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
import com.healthcoco.healthcocopad.bean.PersonalHistory;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugsAndAllergies;
import com.healthcoco.healthcocopad.bean.server.HistoryDetailsResponse;
import com.healthcoco.healthcocopad.bean.server.MedicalFamilyHistoryDetails;
import com.healthcoco.healthcocopad.bean.server.Patient;
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
public class PatientProfileDetailFragment extends HealthCocoFragment implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, HistoryDiseaseIdsListener {
    public static final String INTENT_GET_HISTORY_LIST = "com.healthcoco.HISTORY_LIST";
    public static final String INTENT_GET_MEDICAL_PERSONAL_FAMILY_LIST = "com.healthcoco.MEDICAL_PERSONAL_FAMILY_HISTORY";
    public static final String INTENT_GET_HISTORY_LIST_LOCAL = "com.healthcoco.HISTORY_LIST_LOCAL";
    public static final String TAG_PERSONAL_HISTORY = "patientHistory";
    public static final String DRUG_AND_ALLERGIES = "drugsAndAllegies";
    private static final int REQUEST_CODE_PATIENT_PROFILE = 111;
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
    private TextView tvPatientId;
    private ImageView ivContactProfile;
    private LinearLayout mainLayoutProfile;
    private ArrayList<String> medicalDiseaseIdsList = new ArrayList<String>();
    private ArrayList<String> familyDiseaseIdsList = new ArrayList<String>();
    private boolean isInitialLoading = true;
    private HistoryDetailsResponse historyDetailsResponse;
    private TextView tvDiet;
    private LinearLayout containerDiet;
    private TextView tvAddiction;
    private LinearLayout containerAddiction;
    private TextView tvBowelHabits;
    private LinearLayout containerBowelHabit;
    private TextView tvBladderHabit;
    private LinearLayout containerBladderHabit;
    private TextView tvNoPersonalHistoryData;
    private LinearLayout containerFamilyHistory;
    private TextView tvAllergy;
    private LinearLayout containerAllergy;
    private LinearLayout containerDrugs;
    private LinearLayout containerDrugsDetail;
    private TextView tvNoDrugsAndAllergyData;
    private LinearLayout containerPastHistory;
    private TextView tvNoDataText;
    private LinearLayout containerMobile;
    private TextView tvBloodGroup;
    private LinearLayout containerBloodGroup;
    private TextView tvProfession;
    private LinearLayout containerProfession;
    private TextView tvReferredBy;
    private LinearLayout containerReferredBy;
    private TextView tvAadharId;
    private LinearLayout containerAadharId;
    private TextView tvPamNumber;
    private LinearLayout containerPanNumber;
    private TextView tvDrivingLicence;
    private LinearLayout containerDrivingLicense;
    private LinearLayout containerSecondaryMobile;
    private LinearLayout containerEmail;
    private LinearLayout containerStreetAddress;
    private LinearLayout containerLocality;
    private LinearLayout containerCity;
    private LinearLayout containerCountry;
    private LinearLayout containerPinCode;
    private LinearLayout containerGroups;
    private LinearLayout containerNotes;
    private TextView tvGenderDate;

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
        hideAllMainLayouts();
        initListeners();
        getUserAndPatientDetails();
        refreshData();
    }

    private void getUserAndPatientDetails() {
        CommonOpenUpPatientDetailFragment patientDetailFragmentUpdated = (CommonOpenUpPatientDetailFragment) getFragmentManager().findFragmentByTag(CommonOpenUpPatientDetailFragment.class.getSimpleName());
        if (patientDetailFragmentUpdated != null) {
            selectedPatient = patientDetailFragmentUpdated.getSelectedPatientDetails();
            user = patientDetailFragmentUpdated.getUser();
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
        mainContainerGroups = (LinearLayout) view.findViewById(R.id.container_groups);
        mainContainerPersonalHistory = (LinearLayout) view.findViewById(R.id.container_personal_history);
        mainContainerDrugAndAllergy = (LinearLayout) view.findViewById(R.id.container_drug_and_allergy);
        mainContainerNotes = (LinearLayout) view.findViewById(R.id.container_notes);
        mainLayoutProfile = (LinearLayout) view.findViewById(R.id.main_layout_profile);
        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);
        tvPatientName = (TextView) view.findViewById(R.id.tv_name);
        tvPatientId = (TextView) view.findViewById(R.id.tv_patient_id);
        tvGenderDate = (TextView) view.findViewById(R.id.tv_patient_gender);
        ivContactProfile = (ImageView) view.findViewById(R.id.iv_image);
        //ProfileViews
        containerMobile = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_mobile);
        tvBloodGroup = (TextView) mainLayoutProfile.findViewById(R.id.tv_blood_group);
        containerBloodGroup = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_blood_group);
        tvProfession = (TextView) mainLayoutProfile.findViewById(R.id.tv_profession);
        containerProfession = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_profession);
        tvReferredBy = (TextView) mainLayoutProfile.findViewById(R.id.tv_referred_by);
        containerReferredBy = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_referred_by);
        tvAadharId = (TextView) mainLayoutProfile.findViewById(R.id.tv_aadhar_id);
        containerAadharId = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_aadhar_id);
        tvPamNumber = (TextView) mainLayoutProfile.findViewById(R.id.tv_pan_number);
        containerPanNumber = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_pan_number);
        tvDrivingLicence = (TextView) mainLayoutProfile.findViewById(R.id.tv_driving_license);
        containerDrivingLicense = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_driving_license);
        containerSecondaryMobile = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_secondary_mobile);
        containerEmail = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_email);
        containerStreetAddress = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_street_address);
        containerLocality = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_locality);
        containerCity = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_city);
        containerCountry = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_country);
        containerPinCode = (LinearLayout) mainLayoutProfile.findViewById(R.id.container_pin_code);

        containerGroups = (LinearLayout) mainContainerGroups.findViewById(R.id.container_groups);

        containerNotes = (LinearLayout) mainContainerNotes.findViewById(R.id.container_notes);
        //PersonalHistory Views
        tvDiet = (TextView) mainContainerPersonalHistory.findViewById(R.id.tv_diet);
        containerDiet = (LinearLayout) mainContainerPersonalHistory.findViewById(R.id.container_diet);
        tvAddiction = (TextView) mainContainerPersonalHistory.findViewById(R.id.tv_addictions);
        containerAddiction = (LinearLayout) mainContainerPersonalHistory.findViewById(R.id.container_addiction);
        tvBowelHabits = (TextView) mainContainerPersonalHistory.findViewById(R.id.tv_bowel_habits);
        containerBowelHabit = (LinearLayout) mainContainerPersonalHistory.findViewById(R.id.container_bowel_habits);
        tvBladderHabit = (TextView) mainContainerPersonalHistory.findViewById(R.id.tv_bladder_habits);
        containerBladderHabit = (LinearLayout) mainContainerPersonalHistory.findViewById(R.id.container_bladder_habits);
        tvNoPersonalHistoryData = (TextView) mainContainerPersonalHistory.findViewById(R.id.tv_no_data);
        //DrugsAndAllergyViews
        tvAllergy = (TextView) mainContainerDrugAndAllergy.findViewById(R.id.tv_allergy);
        containerAllergy = (LinearLayout) mainContainerDrugAndAllergy.findViewById(R.id.container_allergy);
        containerDrugs = (LinearLayout) mainContainerDrugAndAllergy.findViewById(R.id.container_drugs);
        containerDrugsDetail = (LinearLayout) mainContainerDrugAndAllergy.findViewById(R.id.container_drugs_detail);
        tvNoDrugsAndAllergyData = (TextView) mainContainerDrugAndAllergy.findViewById(R.id.tv_no_data);

        //PastHistoryViews
        containerPastHistory = (LinearLayout) mainContainerPastHistory.findViewById(R.id.container_past_history);
        showNoHistoryText(R.string.edit_to_assign_past_history);
        containerPastHistory.addView(tvNoDataText);

        //FamilyHistoryViews
        containerFamilyHistory = (LinearLayout) mainContainerFamilyHistory.findViewById(R.id.container_family_history);
        showNoHistoryText(R.string.edit_to_assign_family_history);
        containerFamilyHistory.addView(tvNoDataText);
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
        //ProfileView
        containerBloodGroup.setVisibility(View.GONE);
        containerProfession.setVisibility(View.GONE);
        containerReferredBy.setVisibility(View.GONE);
        containerAadharId.setVisibility(View.GONE);
        containerPanNumber.setVisibility(View.GONE);
        containerDrivingLicense.setVisibility(View.GONE);

        //PersonalHistory Views
        tvNoPersonalHistoryData.setVisibility(View.VISIBLE);
        containerDiet.setVisibility(View.GONE);
        containerAddiction.setVisibility(View.GONE);
        containerBowelHabit.setVisibility(View.GONE);
        containerBladderHabit.setVisibility(View.GONE);
        //DrugsAndAllergyViews
        tvNoDrugsAndAllergyData.setVisibility(View.VISIBLE);
        containerAllergy.setVisibility(View.GONE);
        containerDrugs.setVisibility(View.GONE);
    }

    public void initData() {
        if (selectedPatient != null) {
            ivContactProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Util.isNullOrBlank(selectedPatient.getImageUrl()))
                        mActivity.openEnlargedImageDialogFragment(selectedPatient.getImageUrl());
                }
            });
            tvPatientName.setText(selectedPatient.getLocalPatientName());
            tvPatientId.setText(selectedPatient.getPid());
            String formattedGenderAge = Util.getFormattedGenderAge(selectedPatient);
            if (!Util.isNullOrBlank(formattedGenderAge)) {
                tvGenderDate.setVisibility(View.VISIBLE);
                tvGenderDate.setText(formattedGenderAge);
            } else {
                tvGenderDate.setVisibility(View.GONE);
                tvGenderDate.setText("");
            }
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENT_DEATIL_PROFILE, selectedPatient, null, ivContactProfile, tvInitialAlphabet);
            initProfileData();
            initGroups();
            initNotes();
        }
    }

    private void initProfileData() {
        //initialising mobile data
        if (!Util.isNullOrBlank(selectedPatient.getMobileNumber())) {
            containerMobile.setVisibility(View.VISIBLE);
            TextView tvMobileno = (TextView) containerMobile.findViewById(R.id.tv_mobile);
            tvMobileno.setText(selectedPatient.getMobileNumber());
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
            } else
                containerEmail.setVisibility(View.GONE);

            //initialising secondary mobile data
            if (!Util.isNullOrBlank(patient.getSecMobile())) {
                containerSecondaryMobile.setVisibility(View.VISIBLE);
                TextView tvSecondaryMobile = (TextView) containerSecondaryMobile.findViewById(R.id.tv_secondary_mobile);
                tvSecondaryMobile.setText(patient.getSecMobile());
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
            } else
                containerStreetAddress.setVisibility(View.GONE);

            //setting locality
            if (!Util.isNullOrBlank(address.getLocality())) {
                containerLocality.setVisibility(View.VISIBLE);
                TextView tvLocality = (TextView) containerLocality.findViewById(R.id.tv_locality);
                tvLocality.setText(address.getLocality());
            } else
                containerLocality.setVisibility(View.GONE);

            //setting city
            if (!Util.isNullOrBlank(address.getCity())) {
                containerCity.setVisibility(View.VISIBLE);
                TextView tvCity = (TextView) containerCity.findViewById(R.id.tv_city);
                tvCity.setText(address.getCity());
            } else
                containerCity.setVisibility(View.GONE);

            //setting country
            if (!Util.isNullOrBlank(address.getCountry())) {
                containerCountry.setVisibility(View.VISIBLE);
                TextView tvCountry = (TextView) containerCountry.findViewById(R.id.tv_country);
                tvCountry.setText(address.getCountry());
            } else
                containerCountry.setVisibility(View.GONE);
            //setting country
            if (!Util.isNullOrBlank(address.getPostalCode())) {
                containerPinCode.setVisibility(View.VISIBLE);
                TextView tvPincode = (TextView) containerPinCode.findViewById(R.id.tv_pin_code);
                tvPincode.setText(address.getPostalCode());
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
        if (personalHistory != null && !personalHistory.areAllFieldsNull()) {

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
            tvNoPersonalHistoryData.setVisibility(View.GONE);
        } else {
            tvNoPersonalHistoryData.setVisibility(View.VISIBLE);
            containerDiet.setVisibility(View.GONE);
            containerAddiction.setVisibility(View.GONE);
            containerBowelHabit.setVisibility(View.GONE);
            containerBladderHabit.setVisibility(View.GONE);
        }
        mainContainerPersonalHistory.setVisibility(View.VISIBLE);
    }

    private void initDrugsAndAllergyHistory(DrugsAndAllergies drugsAndAllergies) {
        containerDrugsDetail.removeAllViews();
        tvNoDrugsAndAllergyData.setVisibility(View.VISIBLE);
        if (drugsAndAllergies != null) {
            if (!Util.isNullOrBlank(drugsAndAllergies.getAllergies())) {
                containerAllergy.setVisibility(View.VISIBLE);
                tvAllergy.setText(drugsAndAllergies.getAllergies());
                tvNoDrugsAndAllergyData.setVisibility(View.GONE);
            } else {
                containerAllergy.setVisibility(View.GONE);
            }
            if (!Util.isNullOrEmptyList(drugsAndAllergies.getDrugs())) {
                containerDrugs.setVisibility(View.VISIBLE);
                tvNoDrugsAndAllergyData.setVisibility(View.GONE);
                for (Drug drug : drugsAndAllergies.getDrugs()) {
                    TextView tvDrugs = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                    tvDrugs.setText(drug.getFormattedDrugName());
                    containerDrugsDetail.addView(tvDrugs);
                }
            } else {
                containerDrugs.setVisibility(View.GONE);
            }
        } else {
            tvNoDrugsAndAllergyData.setVisibility(View.VISIBLE);
            containerAllergy.setVisibility(View.GONE);
            containerDrugs.setVisibility(View.GONE);
        }
        mainContainerDrugAndAllergy.setVisibility(View.VISIBLE);
    }

    private void initPastHistory(List<MedicalFamilyHistoryDetails> medicalhistory, HistoryDiseaseIdsListener addDiseaseIdsListener) {
        containerPastHistory.removeAllViews();
        medicalDiseaseIdsList.clear();
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
        mainContainerPastHistory.setVisibility(View.VISIBLE);
    }

    private void initFamilyHistory(List<MedicalFamilyHistoryDetails> familyhistory, HistoryDiseaseIdsListener addDiseaseIdsListener) {
        containerFamilyHistory.removeAllViews();
        familyDiseaseIdsList.clear();
        if (!Util.isNullOrEmptyList(familyhistory)) {
            for (MedicalFamilyHistoryDetails familyHistoryDetails :
                    familyhistory) {
                TextView tvGroupName = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                tvGroupName.setText(familyHistoryDetails.getDisease());
                addDiseaseIdsListener.addDiseaseId(HistoryFilterType.FAMILY_HISTORY, familyHistoryDetails.getUniqueId());
                containerFamilyHistory.addView(tvGroupName);
            }
        } else {
            showNoHistoryText(R.string.edit_to_assign_family_history);
            containerFamilyHistory.addView(tvNoDataText);
        }
        mainContainerFamilyHistory.setVisibility(View.VISIBLE);
    }

    private void showNoHistoryText(int textId) {
        tvNoDataText = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
        tvNoDataText.setText(textId);
    }

    private void initGroups() {
        containerGroups.removeAllViews();
        if (!Util.isNullOrEmptyList(selectedPatient.getGroups())) {
            for (UserGroups group :
                    selectedPatient.getGroups()) {
                TextView tvGroupName = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                tvGroupName.setText(group.getName());
                containerGroups.addView(tvGroupName);
            }
        } else {
            showNoHistoryText(R.string.edit_to_assign_groups);
            containerGroups.addView(tvNoDataText);
        }
        mainContainerGroups.setVisibility(View.VISIBLE);
    }

    private void initNotes() {
        containerNotes.removeAllViews();
        if (selectedPatient.getPatient() != null && !Util.isNullOrEmptyList(selectedPatient.getPatient().getNotes())) {
            for (String note :
                    selectedPatient.getPatient().getNotes()) {
                TextView tvNote = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                tvNote.setText(note);
                containerNotes.addView(tvNote);
            }
        } else {
            showNoHistoryText(R.string.edit_to_add_notes);
            containerNotes.addView(tvNoDataText);
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
                openDialogFragment(new AddEditPersonalHistoryDetailDialogFragment(), AddEditPersonalHistoryDetailDialogFragment.TAG_PERSONAL_HISTORY, historyDetailsResponse, REQUEST_CODE_PATIENT_PROFILE, null);
                break;
            case R.id.bt_edit_patient_profile_drug_and_allergy:
                openDialogFragment(new AddEditDrugAndAllergyDetailDialogFragment(), AddEditDrugAndAllergyDetailDialogFragment.TAG_DRUGS_AND_ALLERGIES, historyDetailsResponse, REQUEST_CODE_PATIENT_PROFILE, null);
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
        startActivityForResult(intent, REQUEST_CODE_PATIENT_PROFILE);
    }

    public void openRegistrationFragment(String patientUniqueId) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_REGISTRATION.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_UNIQUE_ID, patientUniqueId);
        intent.putExtra(HealthCocoConstants.TAG_IS_EDIT_PATIENT, true);
        startActivityForResult(intent, REQUEST_CODE_PATIENT_PROFILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PATIENT_PROFILE) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_REGISTRATION) {
                selectedPatient = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_PATIENT_PROFILE));
                initGroups();
                initNotes();
                initProfileData();
            } else if (resultCode == HealthCocoConstants.RESULT_CODE_DOCTOR_PERSONAL_HISTORY_DETAIL) {
                PersonalHistory personalHistory = Parcels.unwrap(data.getParcelableExtra(TAG_PERSONAL_HISTORY));
                historyDetailsResponse = new HistoryDetailsResponse();
                historyDetailsResponse.setPersonalHistory(personalHistory);
                initPersonalHistory(personalHistory);
            } else if (resultCode == HealthCocoConstants.RESULT_CODE_DRUGS_AND_ALLERGIES_DETAIL) {
                DrugsAndAllergies drugsAndAllergies = Parcels.unwrap(data.getParcelableExtra(DRUG_AND_ALLERGIES));
                historyDetailsResponse = new HistoryDetailsResponse();
                historyDetailsResponse.setDrugsAndAllergies(drugsAndAllergies);
                initDrugsAndAllergyHistory(drugsAndAllergies);
            } else if (resultCode == HealthCocoConstants.RESULT_CODE_DISEASE_LIST) {
                if (data != null && data.hasExtra(DiseaseListFragment.TAG_FILTER_TYPE) && data.hasExtra(DiseaseListFragment.TAG_DISEASES_LIST)) {
                    getHistoryListFromServer(true);
                }
            }
        }
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_HISTORY_LIST:
                LocalDataServiceImpl.getInstance(mApp).addHistoryDetailResponse((HistoryDetailsResponse) response.getData());
                break;
            case GET_HISTORY_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getHistoryDetailResponse(WebServiceType.GET_HISTORY_LIST, BooleanTypeValues.FALSE, isOtpVerified(), user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, null, null);
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
                    historyDetailsResponse = (HistoryDetailsResponse) response.getData();
                    addHistoryDetailInLayout(historyDetailsResponse);
                    if (isInitialLoading && response.isDataFromLocal() && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getHistoryListFromServer(true);
                        return;
                    } else if (historyDetailsResponse != null) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_HISTORY_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    }
                    isInitialLoading = false;
                    break;
            }
        }
        mActivity.hideLoading();
    }

    public void getHistoryListFromServer(boolean showLoading) {
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

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
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
            getHistoryListFromServer(true);
        }
    };

    BroadcastReceiver historyListLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getHistoryListFromLocal(false);
        }
    };

    public void getHistoryListFromLocal(boolean showLoading) {
        if (user != null) {
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

    public void refreshData() {
        initData();
        getHistoryListFromLocal(true);
    }
}
