package com.healthcoco.healthcocopad.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AppointmentRequestToSend;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.CreatedByType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;


/**
 * Created by Prashant on 14/07/2018.
 */

public class AddEditMeasurementFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, HealthcocoTextWatcherListener {

    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private EditText editWeight;
    private EditText editHeight;
    private EditText editBmi;
    private EditText editBodyAge;
    private EditText editWaistCircumference;
    private EditText editHipCircumference;
    private EditText editWaistHipRatio;
    private EditText editBodyFat;
    private EditText editBmr;
    private EditText editVfat;
    private EditText editWholeBodySfat;
    private EditText editWholeBodyMuscles;
    private EditText editArmsBodySfat;
    private EditText editArmsBodyMuscles;
    private EditText editTrunkBodySfat;
    private EditText editTrunkBodyMuscles;
    private EditText editLegsBodySfat;
    private EditText editLegsBodyMuscles;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_body_measurement, container, false);
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

        editWeight = (EditText) view.findViewById(R.id.edit_weight);
        editHeight = (EditText) view.findViewById(R.id.edit_height);
        editBmi = (EditText) view.findViewById(R.id.edit_bmi);
        editBodyAge = (EditText) view.findViewById(R.id.edit_body_age);
        editWaistCircumference = (EditText) view.findViewById(R.id.edit_waist_circumference);
        editHipCircumference = (EditText) view.findViewById(R.id.edit_hip_circumference);
        editWaistHipRatio = (EditText) view.findViewById(R.id.edit_waist_hip_ratio);
        editBodyFat = (EditText) view.findViewById(R.id.edit_body_fat);
        editBmr = (EditText) view.findViewById(R.id.edit_bmr);
        editVfat = (EditText) view.findViewById(R.id.edit_v_fat);
        editWholeBodySfat = (EditText) view.findViewById(R.id.edit_whole_body_s_fat);
        editWholeBodyMuscles = (EditText) view.findViewById(R.id.edit_whole_body_muscles);
        editArmsBodySfat = (EditText) view.findViewById(R.id.edit_arms_body_s_fat);
        editArmsBodyMuscles = (EditText) view.findViewById(R.id.edit_arms_body_muscles);
        editTrunkBodySfat = (EditText) view.findViewById(R.id.edit_trunks_body_s_fat);
        editTrunkBodyMuscles = (EditText) view.findViewById(R.id.edit_trunks_body_muscles);
        editLegsBodySfat = (EditText) view.findViewById(R.id.edit_legs_body_s_fat);
        editLegsBodyMuscles = (EditText) view.findViewById(R.id.edit_legs_body_muscles);
    }

    @Override
    public void initListeners() {
        editHeight.addTextChangedListener(new HealthcocoTextWatcher(editHeight, this));
        editWeight.addTextChangedListener(new HealthcocoTextWatcher(editWeight, this));
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
            case R.id.tv_selected_date:
                break;
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
//        String selectedDate = String.valueOf(tvSelectedDate.getText()).trim();

        if (Util.isNullOrBlank(msg))
            addMeasurement();
        else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addMeasurement() {

        Util.getValidatedValueOrNull(editWeight);
        Util.getValidatedValueOrNull(editHeight);
        Util.getValidatedValueOrNull(editBmi);
        Util.getValidatedValueOrNull(editBodyAge);
        Util.getValidatedValueOrNull(editWaistCircumference);
        Util.getValidatedValueOrNull(editHipCircumference);
        Util.getValidatedValueOrNull(editWaistHipRatio);
        Util.getValidatedValueOrNull(editBodyFat);
        Util.getValidatedValueOrNull(editBmr);
        Util.getValidatedValueOrNull(editVfat);
        Util.getValidatedValueOrNull(editWholeBodySfat);
        Util.getValidatedValueOrNull(editWholeBodyMuscles);
        Util.getValidatedValueOrNull(editArmsBodySfat);
        Util.getValidatedValueOrNull(editArmsBodyMuscles);
        Util.getValidatedValueOrNull(editTrunkBodySfat);
        Util.getValidatedValueOrNull(editTrunkBodyMuscles);
        Util.getValidatedValueOrNull(editLegsBodySfat);
        Util.getValidatedValueOrNull(editLegsBodyMuscles);
    }

    @Override
    public void afterTextChange(View v, String s) {
        switch (v.getId()) {
            case R.id.edit_height:
                if (!Util.isNullOrBlank(s)
                        && !Util.isNullOrBlank(editWeight.getText().toString())
                        && !Util.isNullOrBlank(s)) {
                    float weight = Float.parseFloat(editWeight.getText().toString());
                    float height = Float.parseFloat(s);
                    //BMI = weight in KG / square of (height in metre)
                    float bmiValue = Util.calculateBMI(weight, Float.parseFloat(s) / 100);

                    editBmi.setText(Util.getFormattedFloatNumber(bmiValue));
                } else {
                    editBmi.setText("");
                }
                break;
            case R.id.edit_weight:
                if (!Util.isNullOrBlank(s)
                        && !Util.isNullOrBlank(editHeight.getText().toString())
                        && !Util.isNullOrBlank(s)) {
                    float weight = Float.parseFloat(s);
                    float height = Float.parseFloat(editHeight.getText().toString());
                    //BMI = weight in KG / square of (height in metre)
                    float bmiValue = Util.calculateBMI(weight, height / 100);
                    editBmi.setText(Util.getFormattedFloatNumber(bmiValue));

                } else {
                    editBmi.setText("");
                }
                break;
        }
    }
}
