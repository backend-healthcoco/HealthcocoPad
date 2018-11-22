package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.Addiction;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddMedicalFamilyHistoryRequest;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugsAndAllergies;
import com.healthcoco.healthcocopad.bean.server.FoodAndAllergies;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MealTimeAndPattern;
import com.healthcoco.healthcocopad.bean.server.PatientMedicalHistory;
import com.healthcoco.healthcocopad.bean.server.PrescriptionAddItem;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddEditDrugAndAllergyDetailDialogFragment;
import com.healthcoco.healthcocopad.enums.AddictionType;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.ConsumeTimeType;
import com.healthcoco.healthcocopad.enums.HistoryFilterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Prashant on 14/07/2018.
 */

public class AddEditMedicalInformationFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    private static final int REQUEST_CODE_PATIENT_ASSESSMENT = 111;

    public static ArrayList<Object> QUANTITY = new ArrayList<Object>() {{
        add(QuantityType.MG);
        add(QuantityType.G);
        add(QuantityType.MILI_LITRE);
        add(QuantityType.LITRE);
        add(QuantityType.GLASS);
        add(QuantityType.QTY);
    }};
    public static ArrayList<Object> FREQUENCY = new ArrayList<Object>() {{
        add(ConsumeTimeType.DAILY);
        add(ConsumeTimeType.WEEKLY);
        add(ConsumeTimeType.MONTHLY);
        add(ConsumeTimeType.YEARLY);
    }};

    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;

    private LinearLayout containerExistingDisease;
    private LinearLayout containerPastHistory;
    private LinearLayout containerFamilyHistory;
    private LinearLayout containerDrugAllergy;
    private LinearLayout containerFoodAllergy;
    private LinearLayout containerExistingMedication;
    private LinearLayout layoutSmoking;
    private LinearLayout layoutAlcohol;
    private LinearLayout layoutTobacco;
    private LinearLayout layoutReason;
    private TextView tvNoDataText;

    private Button addPastHistory;
    private Button addFamilyHistory;
    private Button addExistingDisease;
    private Button addDrugAllergy;
    private Button addFoodAllergy;
    private Button addExistingMedication;

    private CheckBox cbSmoking;
    private CheckBox cbAlcohol;
    private CheckBox cbTobacco;
    private RadioGroup radioInsulin;
    private RadioGroup radioHospitalized;
    private RadioGroup radioStress;
    private EditText editQuantitySmoking;
    private EditText editQuantityAlcohol;
    private EditText editQuantityTobacco;
    private EditText editFrequencySmoking;
    private EditText editFrequencyAlcohol;
    private EditText editFrequencyTobacco;
    private EditText editTypeSmoking;
    private EditText editTypeAlcohol;
    private EditText editTypeTobacco;
    private EditText editReason;
    private EditText editFoodAllergy;

    private CustomAutoCompleteTextView tvQuantitySmoking;
    private CustomAutoCompleteTextView tvQuantityAlcohol;
    private CustomAutoCompleteTextView tvQuantityTobacco;
    private CustomAutoCompleteTextView tvFrequencySmoking;
    private CustomAutoCompleteTextView tvFrequencyAlcohol;
    private CustomAutoCompleteTextView tvFrequencyTobacco;

    private TextView tvAllergy;
    private LinearLayout containerAllergy;
    private LinearLayout containerDrugs;
    private LinearLayout containerDrugsDetail;
    private TextView tvNoDrugsAndAllergyData;

    private List<String> pastHistoryList = new ArrayList<String>();
    private List<String> familyHistoryList = new ArrayList<String>();
    private List<String> existingDiseaseList = new ArrayList<String>();

    private List<PrescriptionAddItem> existingMedicationList = new ArrayList<>();

    private DrugsAndAllergies drugsAndAllergies;
    private DrugsAndAllergies existingMedication;
    private MealTimeAndPattern mealTimeAndPattern;

    private String assessmentId;
    private String patientId;
    private PatientMedicalHistory patientMedicalHistory;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_medical_information, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();

        Intent intent = mActivity.getIntent();
        if (intent != null) {
            assessmentId = intent.getStringExtra(HealthCocoConstants.TAG_ASSESSMENT_ID);
            patientId = intent.getStringExtra(HealthCocoConstants.TAG_PATIENT_ID);
        }

        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
        initData();
    }

    @Override
    public void initViews() {

//        editFromTimeWorkingHrs = (TextView) view.findViewById(R.id.edit_working_hrs_from_time);
        containerPastHistory = (LinearLayout) view.findViewById(R.id.container_past_history);
        containerFamilyHistory = (LinearLayout) view.findViewById(R.id.container_family_history);
        containerExistingDisease = (LinearLayout) view.findViewById(R.id.container_disease);
        containerDrugAllergy = (LinearLayout) view.findViewById(R.id.container_drug_allergy);
        containerFoodAllergy = (LinearLayout) view.findViewById(R.id.container_food_allergy);
        containerExistingMedication = (LinearLayout) view.findViewById(R.id.container_existing_medication);
        addPastHistory = (Button) view.findViewById(R.id.bt_add_past_history);
        addFamilyHistory = (Button) view.findViewById(R.id.bt_add_family_history);
        addExistingDisease = (Button) view.findViewById(R.id.bt_add_disease);
        addDrugAllergy = (Button) view.findViewById(R.id.bt_add_drug_allergy);
        addFoodAllergy = (Button) view.findViewById(R.id.bt_add_food_allergy);
        addExistingMedication = (Button) view.findViewById(R.id.bt_add_existing_medication);

        cbSmoking = (CheckBox) view.findViewById(R.id.cb_smoking);
        cbAlcohol = (CheckBox) view.findViewById(R.id.cb_alcohol);
        cbTobacco = (CheckBox) view.findViewById(R.id.cb_tobacco);
        radioInsulin = (RadioGroup) view.findViewById(R.id.rg_insulin);
        radioHospitalized = (RadioGroup) view.findViewById(R.id.rg_hospitalized);
        radioStress = (RadioGroup) view.findViewById(R.id.rg_stress);
        editQuantitySmoking = (EditText) view.findViewById(R.id.edit_quantity_smoking);
        editQuantityAlcohol = (EditText) view.findViewById(R.id.edit_quantity_alcohol);
        editQuantityTobacco = (EditText) view.findViewById(R.id.edit_quantity_tobacco);
        editFrequencySmoking = (EditText) view.findViewById(R.id.edit_frequency_smoking);
        editFrequencyAlcohol = (EditText) view.findViewById(R.id.edit_frequency_alcohol);
        editFrequencyTobacco = (EditText) view.findViewById(R.id.edit_frequency_tobacco);
        editTypeSmoking = (EditText) view.findViewById(R.id.edit_type_smoking);
        editTypeAlcohol = (EditText) view.findViewById(R.id.edit_type_alcohol);
        editTypeTobacco = (EditText) view.findViewById(R.id.edit_type_tobacco);
        editReason = (EditText) view.findViewById(R.id.edit_reason);
        editFoodAllergy = (EditText) view.findViewById(R.id.edit_food_allergy);
        layoutSmoking = (LinearLayout) view.findViewById(R.id.layout_smoking);
        layoutAlcohol = (LinearLayout) view.findViewById(R.id.layout_alcohol);
        layoutTobacco = (LinearLayout) view.findViewById(R.id.layout_tobacco);
        layoutReason = (LinearLayout) view.findViewById(R.id.layout_reason);

        tvQuantitySmoking = (CustomAutoCompleteTextView) view.findViewById(R.id.tv_quantity_smoking);
        tvQuantityAlcohol = (CustomAutoCompleteTextView) view.findViewById(R.id.tv_quantity_alcohol);
        tvQuantityTobacco = (CustomAutoCompleteTextView) view.findViewById(R.id.tv_quantity_tobacco);
        tvFrequencySmoking = (CustomAutoCompleteTextView) view.findViewById(R.id.tv_frequency_smoking);
        tvFrequencyAlcohol = (CustomAutoCompleteTextView) view.findViewById(R.id.tv_frequency_alcohol);
        tvFrequencyTobacco = (CustomAutoCompleteTextView) view.findViewById(R.id.tv_frequency_tobacco);

        //DrugsAndAllergyViews
        tvAllergy = (TextView) containerDrugAllergy.findViewById(R.id.tv_allergy);
        containerAllergy = (LinearLayout) containerDrugAllergy.findViewById(R.id.container_allergy);
        containerDrugs = (LinearLayout) containerDrugAllergy.findViewById(R.id.container_drugs);
        containerDrugsDetail = (LinearLayout) containerDrugAllergy.findViewById(R.id.container_drugs_detail);
        tvNoDrugsAndAllergyData = (TextView) containerDrugAllergy.findViewById(R.id.tv_no_data);

//        set default value
        tvFrequencySmoking.setTag(ConsumeTimeType.DAILY);
        tvFrequencyAlcohol.setTag(ConsumeTimeType.DAILY);
        tvFrequencyTobacco.setTag(ConsumeTimeType.DAILY);
        tvQuantitySmoking.setTag(QuantityType.G);
        tvQuantityAlcohol.setTag(QuantityType.G);
        tvQuantityTobacco.setTag(QuantityType.G);

    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);

        addPastHistory.setOnClickListener(this);
        addFamilyHistory.setOnClickListener(this);
        addExistingDisease.setOnClickListener(this);
        addDrugAllergy.setOnClickListener(this);
        addFoodAllergy.setOnClickListener(this);
        addExistingMedication.setOnClickListener(this);

        cbSmoking.setOnCheckedChangeListener(this);
        cbAlcohol.setOnCheckedChangeListener(this);
        cbTobacco.setOnCheckedChangeListener(this);

        radioHospitalized.setOnCheckedChangeListener(this);
    }


    private void getPatientMedicalHistory() {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getPatientAssessmentDetails(PatientMedicalHistory.class,
                WebServiceType.GET_PATIENT_MEDICL_HISTORY, assessmentId, this, this);
    }

    public void initData() {
        if (patientMedicalHistory != null) {
            if (!Util.isNullOrEmptyList(patientMedicalHistory.getFamilyhistory())) {
                familyHistoryList = patientMedicalHistory.getFamilyhistory();
                initPastHistory(familyHistoryList, containerFamilyHistory);
            }

            if (!Util.isNullOrEmptyList(patientMedicalHistory.getMedicalhistory())) {
                pastHistoryList = patientMedicalHistory.getMedicalhistory();
                initPastHistory(pastHistoryList, containerPastHistory);
            }
            if (!Util.isNullOrEmptyList(patientMedicalHistory.getDiesease())) {
                existingDiseaseList = patientMedicalHistory.getDiesease();
                initPastHistory(existingDiseaseList, containerExistingDisease);
            }

            if (patientMedicalHistory.getDrugsAndAllergies() != null) {
                drugsAndAllergies = patientMedicalHistory.getDrugsAndAllergies();
                initDrugsAndAllergyHistory(drugsAndAllergies);
            }

            if (!Util.isNullOrEmptyList(patientMedicalHistory.getAddiction())) {
                for (Addiction addiction : patientMedicalHistory.getAddiction()) {
                    if (addiction.getAddictionType() != null)
                        switch (addiction.getAddictionType()) {
                            case SMOCKING:
                                initAddictionData(addiction, cbSmoking, editTypeSmoking, editQuantitySmoking, tvQuantitySmoking, editFrequencySmoking, tvFrequencySmoking);
                                break;
                            case ALCOHOL:
                                initAddictionData(addiction, cbAlcohol, editTypeAlcohol, editQuantityAlcohol, tvQuantityAlcohol, editFrequencyAlcohol, tvFrequencyAlcohol);
                                break;
                            case TOBACCO:
                                initAddictionData(addiction, cbTobacco, editTypeTobacco, editQuantityTobacco, tvQuantityTobacco, editFrequencyTobacco, tvFrequencyTobacco);
                                break;
//                            case OTHER:
                        }
                }
            }

            if (patientMedicalHistory.getStress() != null) {
                if (patientMedicalHistory.getStress()) {
                    RadioButton stress = (RadioButton) radioStress.findViewWithTag(getString(R.string.yes));
                    if (stress != null) {
                        stress.setChecked(true);
                    }
                }
            }
            if (patientMedicalHistory.getEverHospitalize() != null) {
                if (patientMedicalHistory.getEverHospitalize()) {
                    RadioButton hospitalized = (RadioButton) radioHospitalized.findViewWithTag(getString(R.string.yes));
                    if (hospitalized != null) {
                        hospitalized.setChecked(true);
                    }
                    if (!Util.isNullOrEmptyList(patientMedicalHistory.getReasons())) {
                        for (String reason : patientMedicalHistory.getReasons())
                            editReason.setText(reason);
                    }
                }
            }
            if (patientMedicalHistory.getFoodAndAllergies() != null) {
                if (!Util.isNullOrEmptyList(patientMedicalHistory.getFoodAndAllergies().getFoods()))
                    addFood(containerFoodAllergy, patientMedicalHistory.getFoodAndAllergies().getFoods());
                if (!Util.isNullOrBlank(patientMedicalHistory.getFoodAndAllergies().getAllergies()))
                    editFoodAllergy.setText(patientMedicalHistory.getFoodAndAllergies().getAllergies());
            }
            if (!Util.isNullOrEmptyList(patientMedicalHistory.getExistingMedication())) {
                initExistingMedication(patientMedicalHistory.getExistingMedication());
            }
        }

    }

    private void initAddictionData(Addiction addiction, CheckBox cbAddiction, EditText
            editType, EditText editQuantity,
                                   CustomAutoCompleteTextView tvQuantity, EditText
                                           editFrequency, CustomAutoCompleteTextView tvFrequency) {
        cbAddiction.setChecked(true);
        if (!Util.isNullOrBlank(addiction.getAlcoholType())) {
            editType.setText(addiction.getAlcoholType());
        }
        if (addiction.getQuantity() != null) {
            if (!Util.isNullOrZeroNumber(addiction.getQuantity().getValue()))
                editQuantity.setText(Util.getValidatedValue(addiction.getQuantity().getValue()));
            if (addiction.getQuantity().getType() != null) {
                tvQuantity.setText(addiction.getQuantity().getType().getUnit());
                tvQuantity.setTag(addiction.getQuantity().getType());
            }
        }
        if (addiction.getConsumeTime() != null) {
            tvFrequency.setText(getString(addiction.getConsumeTime().getTimeTitle()));
            tvFrequency.setTag(addiction.getAddictionType());
        }
        if (!Util.isNullOrZeroNumber(addiction.getNoOfTime())) {
            editFrequency.setText(Util.getValidatedValue(addiction.getNoOfTime()));
        }
    }

    private void initAdapter() {

        initAutoTvAdapter(tvQuantitySmoking, AutoCompleteTextViewType.QUANTITY, QUANTITY);
        initAutoTvAdapter(tvQuantityAlcohol, AutoCompleteTextViewType.QUANTITY, QUANTITY);
        initAutoTvAdapter(tvQuantityTobacco, AutoCompleteTextViewType.QUANTITY, QUANTITY);
        initAutoTvAdapter(tvFrequencySmoking, AutoCompleteTextViewType.FREQUENCY, FREQUENCY);
        initAutoTvAdapter(tvFrequencyAlcohol, AutoCompleteTextViewType.FREQUENCY, FREQUENCY);
        initAutoTvAdapter(tvFrequencyTobacco, AutoCompleteTextViewType.FREQUENCY, FREQUENCY);

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
                        getPatientMedicalHistory();
                    }
                    break;
                case GET_PATIENT_MEDICL_HISTORY:
                    if (response.isValidData(response) && response.getData() instanceof PatientMedicalHistory) {
                        patientMedicalHistory = (PatientMedicalHistory) response.getData();
                        if (patientMedicalHistory != null) {
                            initData();
                        }
                    }
                    break;
                case ADD_PATIENT_MEDICL_HISTORY:
                    if (response.isValidData(response) && response.getData() instanceof PatientMedicalHistory) {
                        PatientMedicalHistory data = (PatientMedicalHistory) response.getData();
//                        LocalDataServiceImpl.getInstance(mApp).addPatientFoodAndExercise(data);
                        mActivity.hideLoading();

                        mActivity.setResult(HealthCocoConstants.RESULT_CODE_REGISTRATION);
                        ((CommonOpenUpActivity) mActivity).finish();
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
                openHisoryFragment(HistoryFilterType.MEDICAL_HISTORY);
                break;
            case R.id.bt_add_family_history:
                openHisoryFragment(HistoryFilterType.FAMILY_HISTORY);
                break;
            case R.id.bt_add_disease:
                openHisoryFragment(HistoryFilterType.DISEASE);
                break;
            case R.id.bt_add_drug_allergy:
                openDrugAndAllergyDialog(false, drugsAndAllergies);
                break;
            case R.id.bt_add_existing_medication:
                openDrugAndAllergyDialog(true, existingMedication);
                break;
            case R.id.bt_add_food_allergy:
                openFoodAndAllergy();
                break;
            case R.id.container_right_action:
                validateData();
                break;
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
//        String selectedDate = String.valueOf(tvSelectedDate.getText()).trim();

        if (Util.isNullOrBlank(msg)) {
            addPatientMedicalInformation();
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addPatientMedicalInformation() {

        mActivity.showLoading(false);

        PatientMedicalHistory medicalHistory = new PatientMedicalHistory();

        if (patientMedicalHistory != null) {
            medicalHistory.setPatientId(patientMedicalHistory.getPatientId());
            medicalHistory.setAssessmentId(patientMedicalHistory.getAssessmentId());
            medicalHistory.setDoctorId(patientMedicalHistory.getDoctorId());
            medicalHistory.setLocationId(patientMedicalHistory.getLocationId());
            medicalHistory.setHospitalId(patientMedicalHistory.getHospitalId());
        } else {
            if (!Util.isNullOrBlank(patientId))
                medicalHistory.setPatientId(patientId);
            if (!Util.isNullOrBlank(assessmentId))
                medicalHistory.setAssessmentId(assessmentId);
            medicalHistory.setDoctorId(user.getUniqueId());
            medicalHistory.setLocationId(user.getForeignLocationId());
            medicalHistory.setHospitalId(user.getForeignHospitalId());
        }

        if (!Util.isNullOrEmptyList(familyHistoryList))
            medicalHistory.setFamilyhistory(familyHistoryList);

        if (!Util.isNullOrEmptyList(pastHistoryList))
            medicalHistory.setMedicalhistory(pastHistoryList);

        if (drugsAndAllergies != null)
            medicalHistory.setDrugsAndAllergies(drugsAndAllergies);

        RadioButton stress = (RadioButton) radioStress.findViewWithTag(getString(R.string.yes));
        if (stress.isChecked()) {
            medicalHistory.setStress(true);
        } else
            medicalHistory.setStress(false);

        medicalHistory.setAddiction(getSelectedAddiction());

        if (!Util.isNullOrEmptyList(existingDiseaseList))
            medicalHistory.setDiesease(existingDiseaseList);

        RadioButton hopitalized = (RadioButton) radioHospitalized.findViewWithTag(getString(R.string.yes));
        if (hopitalized.isChecked()) {
            medicalHistory.setEverHospitalize(true);
            if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(editReason))) {
                List<String> reasonList = new ArrayList<>();
                reasonList.add(Util.getValidatedValueOrNull(editReason));
                medicalHistory.setReasons(reasonList);
            }
        } else
            medicalHistory.setEverHospitalize(false);

        if (mealTimeAndPattern != null) {
            FoodAndAllergies foodAndAllergies = new FoodAndAllergies();
            if (!Util.isNullOrEmptyList(mealTimeAndPattern.getFood())) {
                foodAndAllergies.setFoods(mealTimeAndPattern.getFood());
            }
            if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(editFoodAllergy)))
                foodAndAllergies.setAllergies(Util.getValidatedValueOrNull(editFoodAllergy));

            medicalHistory.setFoodAndAllergies(foodAndAllergies);
        }

        if (!Util.isNullOrEmptyList(existingMedicationList))
            medicalHistory.setExistingMedication(existingMedicationList);

        WebDataServiceImpl.getInstance(mApp).addPatientAssessmentInfo(PatientMedicalHistory.class,
                WebServiceType.ADD_PATIENT_MEDICL_HISTORY, medicalHistory, this, this);

    }

    private List<Addiction> getSelectedAddiction() {
        List<Addiction> addictionList = new ArrayList<>();

        if (cbSmoking.isChecked()) {
            addictionList.add(getAddictionData(AddictionType.SMOCKING, editQuantitySmoking, tvQuantitySmoking,
                    editFrequencySmoking, tvFrequencySmoking, editTypeSmoking));
        }
        if (cbAlcohol.isChecked()) {
            addictionList.add(getAddictionData(AddictionType.ALCOHOL, editQuantityAlcohol, tvQuantityAlcohol,
                    editFrequencyAlcohol, tvFrequencyAlcohol, editTypeAlcohol));
        }
        if (cbTobacco.isChecked()) {
            addictionList.add(getAddictionData(AddictionType.TOBACCO, editQuantityTobacco, tvQuantityTobacco,
                    editFrequencyTobacco, tvFrequencyTobacco, editTypeTobacco));
        }

        return addictionList;
    }

    private Addiction getAddictionData(AddictionType addictionType, EditText editQuantity, CustomAutoCompleteTextView tvQuantity,
                                       EditText editFrequency, CustomAutoCompleteTextView tvFrequency, EditText editType) {

        Addiction addiction = new Addiction();
        addiction.setAddictionType(addictionType);

        if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(editQuantity))) {
//                addiction.setAddictionType()
            MealQuantity quantity = new MealQuantity();
            quantity.setValue(Util.getValidatedDoubleValue(editQuantity));
            quantity.setType((QuantityType) tvQuantity.getTag());
            addiction.setQuantity(quantity);
        }
        if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(editFrequency))) {
//                addiction.setAddictionType()
            addiction.setNoOfTime(Util.getValidatedIntegerValue(editFrequency));
            addiction.setConsumeTime((ConsumeTimeType) tvFrequency.getTag());
        }
        if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(editType)))
            addiction.setAlcoholType(Util.getValidatedValueOrNull(editType));

        return addiction;
    }

    private void openHisoryFragment(HistoryFilterType historyFilterType) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        switch (historyFilterType) {
            case MEDICAL_HISTORY:
                intent.putExtra(HealthCocoConstants.TAG_DISEASE_IDS_LIST, new ArrayList<>(pastHistoryList));
                break;
            case FAMILY_HISTORY:
                intent.putExtra(HealthCocoConstants.TAG_DISEASE_IDS_LIST, new ArrayList<>(familyHistoryList));
                break;
            case DISEASE:
                intent.putExtra(HealthCocoConstants.TAG_DISEASE_IDS_LIST, new ArrayList<>(existingDiseaseList));
                break;
        }
        intent.putExtra(HealthCocoConstants.TAG_HISTORY_FILTER_TYPE, historyFilterType.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_IS_FROM_ASSESSMENT, true);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.HISTORY_DISEASE_LIST.ordinal());
        startActivityForResult(intent, REQUEST_CODE_PATIENT_ASSESSMENT);
    }


    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.cb_smoking:
                if (isChecked) {
                    Util.toggleLayoutView(mActivity, layoutSmoking, true);
                } else {
                    Util.toggleLayoutView(mActivity, layoutSmoking, false);
                }
                break;
            case R.id.cb_alcohol:
                if (isChecked) {
                    Util.toggleLayoutView(mActivity, layoutAlcohol, true);
                } else {
                    Util.toggleLayoutView(mActivity, layoutAlcohol, false);
                }
                break;
            case R.id.cb_tobacco:
                if (isChecked) {
                    Util.toggleLayoutView(mActivity, layoutTobacco, true);
                } else {
                    Util.toggleLayoutView(mActivity, layoutTobacco, false);
                }
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        RadioButton radioButton = (RadioButton) radioHospitalized.findViewWithTag(getString(R.string.yes));
        if (radioButton.isChecked()) {
            Util.toggleLayoutView(mActivity, layoutReason, true);
        } else {
            Util.toggleLayoutView(mActivity, layoutReason, false);
        }
    }

    private void initAutoTvAdapter(AutoCompleteTextView autoCompleteTextView,
                                   final AutoCompleteTextViewType autoCompleteTextViewType, ArrayList<Object> list) {
        try {
            if (!Util.isNullOrEmptyList(list)) {
                final AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.spinner_drop_down_item_grey_background,
                        list, autoCompleteTextViewType);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setDropDownAnchor(autoCompleteTextView.getId());
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        onQuantityItemClicked(autoCompleteTextViewType,parent,view,position,id);
                        switch (autoCompleteTextViewType) {
                            case QUANTITY:
                                Object quantity = QUANTITY.get(position);
                                view.setTag(quantity);
                                break;
                            case FREQUENCY:
                                Object frequency = FREQUENCY.get(position);
                                view.setTag(frequency);
                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PATIENT_ASSESSMENT)
            switch (resultCode) {
                case HealthCocoConstants.RESULT_CODE_PAST_HISTORY:
                    if (data != null) {
                        AddMedicalFamilyHistoryRequest familyHistoryRequest = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        int filterTypeOrdinal = data.getIntExtra(HealthCocoConstants.TAG_HISTORY_FILTER_TYPE, 0);
                        HistoryFilterType filterType = HistoryFilterType.values()[filterTypeOrdinal];
                        if (familyHistoryRequest != null) {
                            if (!Util.isNullOrEmptyList(familyHistoryRequest.getAddDiseases())) {
                                if (filterType != null) {
                                    switch (filterType) {
                                        case MEDICAL_HISTORY:
                                            pastHistoryList = familyHistoryRequest.getAddDiseases();
                                            initPastHistory(pastHistoryList, containerPastHistory);
                                            break;
                                        case FAMILY_HISTORY:
                                            familyHistoryList = familyHistoryRequest.getAddDiseases();
                                            initPastHistory(familyHistoryList, containerFamilyHistory);
                                            break;
                                        case DISEASE:
                                            existingDiseaseList = familyHistoryRequest.getAddDiseases();
                                            initPastHistory(existingDiseaseList, containerExistingDisease);
                                            break;
                                    }
                                }
                            }
                        }
                    }
                    break;
                case HealthCocoConstants.RESULT_CODE_DRUGS_AND_ALLERGIES_DETAIL:
                    if (data != null) {
                        drugsAndAllergies = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        if (drugsAndAllergies != null)
                            initDrugsAndAllergyHistory(drugsAndAllergies);
                    }
                    break;
                case HealthCocoConstants.RESULT_CODE_EXISTING_MEDICATION:
                    if (data != null) {
                        existingMedication = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        if (existingMedication != null) {
                            if (!Util.isNullOrEmptyList(existingMedication.getDrugs())) {
                                existingMedicationList.clear();
                                for (Drug drug : existingMedication.getDrugs()) {
                                    PrescriptionAddItem prescriptionAddItem = new PrescriptionAddItem();
                                    try {
                                        ReflectionUtil.copy(prescriptionAddItem, drug);
                                        prescriptionAddItem.setDrugId(drug.getUniqueId());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    existingMedicationList.add(prescriptionAddItem);
                                }
                                initExistingMedication(existingMedicationList);
                            }
                        }
                    }
                    break;
                case HealthCocoConstants.RESULT_CODE_ADD_MEAL:
                    if (data != null) {
                        mealTimeAndPattern = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        if (mealTimeAndPattern != null) {
                            if (!Util.isNullOrEmptyList(mealTimeAndPattern.getFood()))
                                addFood(containerFoodAllergy, mealTimeAndPattern.getFood());
                        }
                    }
                    break;
            }
    }


    private void initPastHistory(List<String> historyList, LinearLayout containerHistory) {
        containerHistory.removeAllViews();
        if (!Util.isNullOrEmptyList(historyList)) {
            for (String history :
                    historyList) {
                TextView tvGroupName = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
                tvGroupName.setText(history);
                containerHistory.addView(tvGroupName);
            }
        } else {
            showNoHistoryText(R.string.edit_to_assign_past_history);
            containerHistory.addView(tvNoDataText);
        }
    }

    private void showNoHistoryText(int textId) {
        tvNoDataText = (TextView) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_groups_notes_text, null);
        tvNoDataText.setText(textId);
    }

    public void openDrugAndAllergyDialog(boolean isFromMedication, DrugsAndAllergies andAllergies) {
        AddEditDrugAndAllergyDetailDialogFragment dialogFragment = new AddEditDrugAndAllergyDetailDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean(HealthCocoConstants.TAG_IS_FROM_ASSESSMENT, true);
        if (isFromMedication)
            bundle.putBoolean(HealthCocoConstants.TAG_DRUG_DETAILS, true);
        if (andAllergies != null)
            bundle.putParcelable(AddEditDrugAndAllergyDetailDialogFragment.TAG_DRUGS_AND_ALLERGIES_ASSESSMENT, Parcels.wrap(andAllergies));
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(this, REQUEST_CODE_PATIENT_ASSESSMENT);
        dialogFragment.show(mActivity.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
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
                    LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_drugs_text, null);
                    TextView drugName = (TextView) linearLayout.findViewById(R.id.drug_name);
                    TextView genericName = (TextView) linearLayout.findViewById(R.id.generic_name);
                    drugName.setText(drug.getFormattedDrugName());
                    genericName.setText(drug.getFormattedGenricName());
                    containerDrugsDetail.addView(linearLayout);
                }
            } else {
                containerDrugs.setVisibility(View.GONE);
            }
        } else {
            tvNoDrugsAndAllergyData.setVisibility(View.VISIBLE);
            containerAllergy.setVisibility(View.GONE);
            containerDrugs.setVisibility(View.GONE);
        }
        containerDrugAllergy.setVisibility(View.VISIBLE);
    }

    public void openFoodAndAllergy() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.SELECT_RECIPES.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, MealTimeType.BREAKFAST.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_IS_FROM_ASSESSMENT, true);
        if (mealTimeAndPattern != null)
            intent.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(mealTimeAndPattern));
        startActivityForResult(intent, REQUEST_CODE_PATIENT_ASSESSMENT, null);

    }

    private void addFood(LinearLayout containerLayout, List<DietPlanRecipeItem> mealList) {
        containerLayout.removeAllViews();
        if (!Util.isNullOrEmptyList(mealList)) {
            editFoodAllergy.setVisibility(View.VISIBLE);
            for (DietPlanRecipeItem recipeItem : mealList) {

                LinearLayout layoutRecipe = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_sub_item_selected_recipe, null);
                TextView tvRecipeTitle = (TextView) layoutRecipe.findViewById(R.id.tv_ingredient_name);
                TextView tvRecipeQuantity = (TextView) layoutRecipe.findViewById(R.id.tv_quantity_ingredient);
                TextView tvRecipeCalarie = (TextView) layoutRecipe.findViewById(R.id.tv_serving_ingredient);
                LinearLayout containerIngredients = (LinearLayout) layoutRecipe.findViewById(R.id.container_ingredient);
                boolean isIngredientValue = false;

                tvRecipeTitle.setText(recipeItem.getName());

                if (recipeItem.getNutrientValueAtRecipeLevel() != null)
                    isIngredientValue = recipeItem.getNutrientValueAtRecipeLevel();


                if (isIngredientValue) {
                    tvRecipeQuantity.setVisibility(View.GONE);
                    tvRecipeCalarie.setVisibility(View.GONE);
                }

                if (recipeItem.getQuantity() != null) {
                    if (recipeItem.getQuantity().getType() != null) {
                        String quantityType = recipeItem.getQuantity().getType().getUnit();
                        if (!Util.isNullOrZeroNumber(recipeItem.getQuantity().getValue())) {
                            tvRecipeQuantity.setText(Util.getValidatedValue(recipeItem.getQuantity().getValue()) + quantityType);
                        }
                    }
                }
                if (recipeItem.getCalories() != null) {
                    if (!Util.isNullOrZeroNumber(recipeItem.getCalories().getValue())) {
                        tvRecipeCalarie.setText(Util.getValidatedValue(recipeItem.getCalories().getValue()) + mActivity.getString(R.string.cal_orange) /*+ calaries.getType().getQuantityType()*/);
                    }
                }


                containerIngredients.removeAllViews();

                if (!Util.isNullOrEmptyList(recipeItem.getIngredients())) {
                    containerIngredients.setVisibility(View.VISIBLE);
                    for (Ingredient ingredient : recipeItem.getIngredients()) {
                        if (ingredient != null) {
                            LinearLayout layoutSubItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_sub_item_selected_ingredient, null);
                            TextView tvItemTitle = (TextView) layoutSubItemPermission.findViewById(R.id.tv_ingredient_name);
                            TextView tvItemQuantity = (TextView) layoutSubItemPermission.findViewById(R.id.tv_quantity_ingredient);
                            TextView tvItemCalarie = (TextView) layoutSubItemPermission.findViewById(R.id.tv_serving_ingredient);
                            TextViewFontAwesome btItemDelete = (TextViewFontAwesome) layoutSubItemPermission.findViewById(R.id.bt_delete_ingredient);
                            btItemDelete.setOnClickListener(this);
                            btItemDelete.setVisibility(View.GONE);

                            tvItemTitle.setText(ingredient.getName());
                            if (ingredient.getQuantity() != null)
                                if (ingredient.getQuantity().getType() != null) {
                                    String type = ingredient.getQuantity().getType().getUnit();
                                    if (!Util.isNullOrZeroNumber(ingredient.getQuantity().getValue())) {
                                        tvItemQuantity.setText(Util.getValidatedValue(ingredient.getQuantity().getValue()) + type);
                                    }
                                }
                            if (ingredient.getCalories() != null) {
                                if (!Util.isNullOrZeroNumber(ingredient.getCalories().getValue())) {
                                    tvItemCalarie.setText(Util.getValidatedValue(ingredient.getCalories().getValue()) + mActivity.getString(R.string.cal_orange));
                                }
                            }
                            containerIngredients.addView(layoutSubItemPermission);
                        }
                    }
                } else {
                    containerIngredients.setVisibility(View.GONE);
                }
                containerLayout.addView(layoutRecipe);
            }
        }
    }

    private void initExistingMedication(List<PrescriptionAddItem> existingMedicationList) {
        containerExistingMedication.removeAllViews();
        for (PrescriptionAddItem item : existingMedicationList) {
            LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.sub_item_profile_detail_drugs_text, null);
            TextView drugName = (TextView) linearLayout.findViewById(R.id.drug_name);
            TextView genericName = (TextView) linearLayout.findViewById(R.id.generic_name);
            drugName.setText(item.getFormattedDrugName());
            genericName.setText(item.getDosage());
            containerExistingMedication.addView(linearLayout);
        }
    }


}
