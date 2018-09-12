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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
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
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Prashant on 14/07/2018.
 */

public class AddEditMedicalInformationFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, HistoryDiseaseIdsListener, CompoundButton.OnCheckedChangeListener, RadioGroup.OnCheckedChangeListener {

    public static ArrayList<Object> QUANTITY_ALCOHOL = new ArrayList<Object>() {{
        add("ml");
        add("gms");
        add("unit");
    }};
    public static ArrayList<Object> FREQUENCY_ALCOHOL = new ArrayList<Object>() {{
        add("per Day");
        add("per Week");
        add("per Month");
        add("per Year");
    }};

    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;

    private LinearLayout containerPastHistory;
    private LinearLayout containerFamilyHistory;
    private LinearLayout layoutSmoking;
    private LinearLayout layoutAlcohol;
    private LinearLayout layoutTobacco;
    private LinearLayout layoutReason;
    private TextView tvNoDataText;

    private Button addPastHistory;
    private Button addFamilyHistory;

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
    private EditText editReason;

    private CustomAutoCompleteTextView tvQuantitySmoking;
    private CustomAutoCompleteTextView tvQuantityAlcohol;
    private CustomAutoCompleteTextView tvQuantityTobacco;
    private CustomAutoCompleteTextView tvFrequencySmoking;
    private CustomAutoCompleteTextView tvFrequencyAlcohol;
    private CustomAutoCompleteTextView tvFrequencyTobacco;

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
        initData();
    }

    @Override
    public void initViews() {

//        editFromTimeWorkingHrs = (TextView) view.findViewById(R.id.edit_working_hrs_from_time);
        containerPastHistory = (LinearLayout) view.findViewById(R.id.container_past_history);
        containerFamilyHistory = (LinearLayout) view.findViewById(R.id.container_family_history);
        addPastHistory = (Button) view.findViewById(R.id.bt_add_past_history);
        addFamilyHistory = (Button) view.findViewById(R.id.bt_add_family_history);

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
        editReason = (EditText) view.findViewById(R.id.edit_reason);
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
    }

    @Override
    public void initListeners() {
        addPastHistory.setOnClickListener(this);
        addFamilyHistory.setOnClickListener(this);

        cbSmoking.setOnCheckedChangeListener(this);
        cbAlcohol.setOnCheckedChangeListener(this);
        cbTobacco.setOnCheckedChangeListener(this);

        radioHospitalized.setOnCheckedChangeListener(this);
    }

    public void initData() {

        initAutoTvAdapter(tvQuantitySmoking, AutoCompleteTextViewType.QUANTITY, QUANTITY_ALCOHOL);
        initAutoTvAdapter(tvQuantityAlcohol, AutoCompleteTextViewType.QUANTITY, QUANTITY_ALCOHOL);
        initAutoTvAdapter(tvQuantityTobacco, AutoCompleteTextViewType.QUANTITY, QUANTITY_ALCOHOL);
        initAutoTvAdapter(tvFrequencySmoking, AutoCompleteTextViewType.FREQUENCY, FREQUENCY_ALCOHOL);
        initAutoTvAdapter(tvFrequencyAlcohol, AutoCompleteTextViewType.FREQUENCY, FREQUENCY_ALCOHOL);
        initAutoTvAdapter(tvFrequencyTobacco, AutoCompleteTextViewType.FREQUENCY, FREQUENCY_ALCOHOL);

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

    private void initAutoTvAdapter(AutoCompleteTextView autoCompleteTextView, final AutoCompleteTextViewType autoCompleteTextViewType, ArrayList<Object> list) {
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
                        switch (autoCompleteTextViewType) {
                            case QUANTITY:
                                break;
                            case FREQUENCY:
                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
