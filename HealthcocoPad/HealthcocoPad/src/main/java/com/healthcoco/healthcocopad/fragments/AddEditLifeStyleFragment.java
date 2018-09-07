package com.healthcoco.healthcocopad.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;


/**
 * Created by Prashant on 14/07/2018.
 */

public class AddEditLifeStyleFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private EditText editFromTimeWorkingHrs;
    private EditText editToTimeWorkingHrs;
    private EditText editFromTimeSleepingDay;
    private EditText editToTimeSleepingDay;
    private EditText editFromTimeSleepingNight;
    private EditText editToTimeSleepingNight;
    private EditText editFromTimeTvWaching;
    private EditText editToTimeTvWaching;
    private EditText editFromTimeSocialMedia;
    private EditText editToTimeSocialMedia;
    private CheckBox cbDay;
    private CheckBox cbNight;
    private LinearLayout layoutSleepingDay;
    private LinearLayout layoutSleepingNight;
    private RadioGroup radioTvBedroom;
    private RadioGroup radioLaptopBedroom;
    private RadioGroup radioLifeStyle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_body_measurement, container, false);
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

        editFromTimeWorkingHrs = (EditText) view.findViewById(R.id.edit_working_hrs_from_time);
        editToTimeWorkingHrs = (EditText) view.findViewById(R.id.edit_working_hrs_to_time);
        editFromTimeSleepingDay = (EditText) view.findViewById(R.id.edit_sleeping_from_time_day);
        editToTimeSleepingDay = (EditText) view.findViewById(R.id.edit_sleeping_to_time_day);
        editFromTimeSleepingNight = (EditText) view.findViewById(R.id.edit_sleeping_from_time_night);
        editToTimeSleepingNight = (EditText) view.findViewById(R.id.edit_sleeping_to_time_night);
        editFromTimeTvWaching = (EditText) view.findViewById(R.id.edit_tv_view_from_time);
        editToTimeTvWaching = (EditText) view.findViewById(R.id.edit_tv_view_to_time);
        editFromTimeSocialMedia = (EditText) view.findViewById(R.id.edit_social_media_from_time);
        editToTimeSocialMedia = (EditText) view.findViewById(R.id.edit_social_media_to_time);
        cbDay = (CheckBox) view.findViewById(R.id.cb_day);
        cbNight = (CheckBox) view.findViewById(R.id.cb_night);
        radioTvBedroom = (RadioGroup) view.findViewById(R.id.rg_tv_in_bedroom);
        radioLaptopBedroom = (RadioGroup) view.findViewById(R.id.rg_laptop_in_bedroom);
        radioLifeStyle = (RadioGroup) view.findViewById(R.id.rg_life_style);
        layoutSleepingDay = (LinearLayout) view.findViewById(R.id.layout_sleep_pattern_day);
        layoutSleepingNight = (LinearLayout) view.findViewById(R.id.layout_sleep_pattern_night);

    }

    @Override
    public void initListeners() {
        cbDay.setOnCheckedChangeListener(this);
        cbNight.setOnCheckedChangeListener(this);
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

        if (Util.isNullOrBlank(msg)) {
            addLifeStyle();
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addLifeStyle() {

        Util.getValidatedValueOrNull(editFromTimeWorkingHrs);
        Util.getValidatedValueOrNull(editToTimeWorkingHrs);
        Util.getValidatedValueOrNull(editFromTimeSleepingDay);
        Util.getValidatedValueOrNull(editToTimeSleepingDay);
        Util.getValidatedValueOrNull(editFromTimeSleepingNight);
        Util.getValidatedValueOrNull(editToTimeSleepingNight);
        Util.getValidatedValueOrNull(editFromTimeTvWaching);
        Util.getValidatedValueOrNull(editToTimeTvWaching);
        Util.getValidatedValueOrNull(editFromTimeSocialMedia);
        Util.getValidatedValueOrNull(editToTimeSocialMedia);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.cb_day:
                if (isChecked) {
                    Util.toggleLayoutView(mActivity, layoutSleepingDay, true);
                } else {
                    Util.toggleLayoutView(mActivity, layoutSleepingDay, false);
                }
                break;
            case R.id.cb_night:
                if (isChecked) {
                    Util.toggleLayoutView(mActivity, layoutSleepingNight, true);
                } else {
                    Util.toggleLayoutView(mActivity, layoutSleepingNight, false);
                }
                break;
        }
    }
}
