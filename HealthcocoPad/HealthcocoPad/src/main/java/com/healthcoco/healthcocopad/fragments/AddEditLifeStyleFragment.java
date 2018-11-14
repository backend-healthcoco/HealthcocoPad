package com.healthcoco.healthcocopad.fragments;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.UIPermissionItemGridAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Patient;
import com.healthcoco.healthcocopad.bean.server.PatientFoodAndExcercise;
import com.healthcoco.healthcocopad.bean.server.PatientLifeStyle;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.SleepPattern;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.bean.server.WorkingSchedule;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddWorkingHrsDialogFragment;
import com.healthcoco.healthcocopad.enums.LifeStyleType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.SleepTimeType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.enums.WeekDayNameType;
import com.healthcoco.healthcocopad.listeners.CommonUiPermissionsListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by Prashant on 14/07/2018.
 */

public class AddEditLifeStyleFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener, CommonUiPermissionsListener, RadioGroup.OnCheckedChangeListener {

    private final int EDIT_ID = 100;
    private final int DELETE_ID = 101;
    public static final String TIME_FORMAT = "hh:mm aaa";
    public static final int DEFAULT_TIME_INTERVAL = 15;
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    TimePickerDialog datePickerDialog;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private final ArrayList<String> ALL_WORKING_DAY = new ArrayList<String>() {{
        add("Sunday");
        add("Monday");
        add("Tuesday");
        add("Wednesday");
        add("Thursday");
        add("Friday");
        add("Saturday");
    }};
    private TextView tvFromTimeSleepingDay;
    private TextView tvToTimeSleepingDay;
    private TextView tvFromTimeSleepingNight;
    private EditText editMobileUsage;
    private EditText editTravellingPeriod;
    private EditText editTvViewTime;
    private EditText editSocialMeadia;
    private TextView tvToTimeSleepingNight;
    private EditText editSleepTimeDay;
    private EditText editSleepTimeNight;
    private EditText editTvBedroom;
    private EditText editLaptopBedroom;
    private TextView tvFromTimeSocialMedia;
    private TextView tvToTimeSocialMedia;
    private TextView tvFromTimeTvWaching;
    private TextView tvToTimeTvWaching;
    private TextView tvFromTimeMobileUsage;
    private CheckBox cbDay;
    private CheckBox cbNight;
    private LinearLayout layoutSleepingDay;
    private LinearLayout layoutSleepingNight;
    private TextView tvToTimeMobileUsage;
    private LinearLayout layoutTvBedroom;
    private LinearLayout layoutLaptopBedroom;
    private LinearLayout layoutAddWorkingHrs;
    private LinearLayout layoutWorkingHrs;
    private LinearLayout layoutAddTravellingPeriod;
    private RadioGroup radioTvBedroom;
    private RadioGroup radioLaptopBedroom;
    private RadioGroup radioLifeStyle;
    private GridView gvWorkingDay;
    private UIPermissionItemGridAdapter adapter;
    private ArrayList<String> selectedWorkingDays = new ArrayList<>();
    private LinearLayout layoutTravellingPeriod;
    private LinkedHashMap<String, WorkingHours> workingHrsHashMap = new LinkedHashMap<>();
    private LinkedHashMap<String, WorkingHours> travellingTimeHashMap = new LinkedHashMap<>();
    private String assessmentId;
    private String patientId;
    private PatientLifeStyle patientLifeStyle;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_life_style, container, false);
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
//            if (assessmentId != null)
//                measurementInfo = LocalDataServiceImpl.getInstance(mApp).getPatientMeasurementInfo(assessmentId);
//            if (measurementInfo != null)
//                initMeasurementInfo();
        }

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

        tvFromTimeSleepingDay = (TextView) view.findViewById(R.id.tv_sleeping_from_time_day);
        tvToTimeSleepingDay = (TextView) view.findViewById(R.id.tv_sleeping_to_time_day);
        tvFromTimeSleepingNight = (TextView) view.findViewById(R.id.tv_sleeping_from_time_night);
        tvToTimeSleepingNight = (TextView) view.findViewById(R.id.tv_sleeping_to_time_night);
        editTravellingPeriod = (EditText) view.findViewById(R.id.edit_travelling_peroid);
        editMobileUsage = (EditText) view.findViewById(R.id.edit_mobile_usage);
        editTvViewTime = (EditText) view.findViewById(R.id.edit_tv_view_time);
        editSocialMeadia = (EditText) view.findViewById(R.id.edit_social_media);
        tvFromTimeTvWaching = (TextView) view.findViewById(R.id.tv_tv_view_from_time);
        tvToTimeTvWaching = (TextView) view.findViewById(R.id.tv_tv_view_to_time);
        tvFromTimeSocialMedia = (TextView) view.findViewById(R.id.tv_social_media_from_time);
        tvToTimeSocialMedia = (TextView) view.findViewById(R.id.tv_social_media_to_time);
        tvFromTimeMobileUsage = (TextView) view.findViewById(R.id.tv_mobile_usage_from_time);
        tvToTimeMobileUsage = (TextView) view.findViewById(R.id.tv_mobile_usage_to_time);
        editSleepTimeDay = (EditText) view.findViewById(R.id.edit_sleeping_time_day);
        editSleepTimeNight = (EditText) view.findViewById(R.id.edit_sleeping_time_night);
        editTvBedroom = (EditText) view.findViewById(R.id.edit_tv_in_bedroom);
        editLaptopBedroom = (EditText) view.findViewById(R.id.edit_laptop_in_bedroom);
        cbDay = (CheckBox) view.findViewById(R.id.cb_day);
        cbNight = (CheckBox) view.findViewById(R.id.cb_night);
        radioTvBedroom = (RadioGroup) view.findViewById(R.id.rg_tv_in_bedroom);
        radioLaptopBedroom = (RadioGroup) view.findViewById(R.id.rg_laptop_in_bedroom);
        radioLifeStyle = (RadioGroup) view.findViewById(R.id.rg_life_style);
        layoutSleepingDay = (LinearLayout) view.findViewById(R.id.layout_sleep_pattern_day);
        layoutSleepingNight = (LinearLayout) view.findViewById(R.id.layout_sleep_pattern_night);
        layoutTvBedroom = (LinearLayout) view.findViewById(R.id.layout_tv_in_bedroom);
        layoutLaptopBedroom = (LinearLayout) view.findViewById(R.id.layout_laptop_in_bedroom);
        layoutWorkingHrs = (LinearLayout) view.findViewById(R.id.layout_working_hours);
        layoutAddWorkingHrs = (LinearLayout) view.findViewById(R.id.layout_add_working_hrs);
        layoutTravellingPeriod = (LinearLayout) view.findViewById(R.id.layout_travelling_period);
        layoutAddTravellingPeriod = (LinearLayout) view.findViewById(R.id.layout_add_travelling_period);

        gvWorkingDay = (GridView) view.findViewById(R.id.gv_working_day);

        view.findViewById(R.id.rb_sedentary).setTag(LifeStyleType.SENDETARY);
        view.findViewById(R.id.rb_moderate).setTag(LifeStyleType.MODERATE);
        view.findViewById(R.id.rb_heavy).setTag(LifeStyleType.HEAVY);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);

        cbDay.setOnCheckedChangeListener(this);
        cbNight.setOnCheckedChangeListener(this);

        radioLaptopBedroom.setOnCheckedChangeListener(this);
        radioTvBedroom.setOnCheckedChangeListener(this);

        tvFromTimeSleepingDay.setOnClickListener(this);
        tvToTimeSleepingDay.setOnClickListener(this);
        tvFromTimeSleepingNight.setOnClickListener(this);
        tvToTimeSleepingNight.setOnClickListener(this);
        tvFromTimeSocialMedia.setOnClickListener(this);
        tvToTimeSocialMedia.setOnClickListener(this);
        tvFromTimeTvWaching.setOnClickListener(this);
        tvToTimeTvWaching.setOnClickListener(this);
        tvFromTimeMobileUsage.setOnClickListener(this);
        tvToTimeMobileUsage.setOnClickListener(this);

        layoutAddWorkingHrs.setOnClickListener(this);
        layoutAddTravellingPeriod.setOnClickListener(this);
    }


    private void getPatientLifeStyle() {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getPatientAssessmentDetails(PatientLifeStyle.class,
                WebServiceType.GET_PATIENT_LIFE_STYLE, assessmentId, this, this);
    }

    public void initData() {
        if (patientLifeStyle != null) {
            if (!Util.isNullOrEmptyList(patientLifeStyle.getWorkingschedules())) {
                for (WorkingSchedule workingSchedule : patientLifeStyle.getWorkingschedules()) {
                    if (workingSchedule.getWorkingDay() != null) {
                        WeekDayNameType workingDay = workingSchedule.getWorkingDay();
                        selectedWorkingDays.add(getString(workingDay.getWeekDayNameId()));
                    }
                    if (!Util.isNullOrEmptyList(workingSchedule.getWorkingHours())) {
                        for (WorkingHours workingHours : workingSchedule.getWorkingHours()) {
                            workingHrsHashMap.put(workingHours.getCustomUniqueId(), workingHours);
                        }
                        addWorkingHours(new ArrayList<>(workingHrsHashMap.values()), layoutWorkingHrs);
                    }
                }
                notifyAdapter(ALL_WORKING_DAY);
            }

            if (!Util.isNullOrEmptyList(patientLifeStyle.getTrivalingPeriod()))
                for (WorkingSchedule workingSchedule : patientLifeStyle.getTrivalingPeriod()) {
                    if (!Util.isNullOrEmptyList(workingSchedule.getWorkingHours())) {
                        for (WorkingHours workingHours : workingSchedule.getWorkingHours()) {
                            travellingTimeHashMap.put(workingHours.getCustomUniqueId(), workingHours);
                        }
                        addWorkingHours(new ArrayList<>(travellingTimeHashMap.values()), layoutTravellingPeriod);
                    }
                }

            if (!Util.isNullOrEmptyList(patientLifeStyle.getSleepPatterns())) {
                for (SleepPattern sleepPattern : patientLifeStyle.getSleepPatterns()) {
                    if (sleepPattern.getTimeType() != null) {
                        switch (sleepPattern.getTimeType()) {
                            case DAY:
                                cbDay.setChecked(true);
                                setFromTimeAndToTime(sleepPattern, editSleepTimeDay, tvFromTimeSleepingDay, tvToTimeSleepingDay);
                                break;
                            case NIGHT:
                                cbNight.setChecked(true);
                                setFromTimeAndToTime(sleepPattern, editSleepTimeNight, tvFromTimeSleepingNight, tvToTimeSleepingNight);
                                break;
                        }
                    }
                }
            }
            if (patientLifeStyle.getTvViewTime() != null) {
                setFromTimeAndToTime(patientLifeStyle.getTvViewTime(), editTvViewTime, tvFromTimeTvWaching, tvToTimeTvWaching);
            }
            if (patientLifeStyle.getMobileUsage() != null) {
                setFromTimeAndToTime(patientLifeStyle.getMobileUsage(), editMobileUsage, tvFromTimeMobileUsage, tvToTimeMobileUsage);
            }
            if (patientLifeStyle.getLoptopUseTime() != null) {
                setFromTimeAndToTime(patientLifeStyle.getLoptopUseTime(), editSocialMeadia, tvFromTimeSocialMedia, tvToTimeSocialMedia);
            }

            if (patientLifeStyle.getTvInBedRoom() != null) {
                if (patientLifeStyle.getTvInBedRoom()) {
                    RadioButton radioTv = (RadioButton) radioTvBedroom.findViewWithTag(getString(R.string.yes));
                    if (radioTv != null) {
                        radioTv.setChecked(true);
                    }
                    if (!Util.isNullOrZeroNumber(patientLifeStyle.getTvInBedRoomForMinute())) {
                        editTvBedroom.setText(getNoOfHours(patientLifeStyle.getTvInBedRoomForMinute()));
                    }
                }
            }
            if (patientLifeStyle.getLaptopInBedRoom() != null) {
                if (patientLifeStyle.getLaptopInBedRoom()) {
                    RadioButton radioTv = (RadioButton) radioLaptopBedroom.findViewWithTag(getString(R.string.yes));
                    if (radioTv != null) {
                        radioTv.setChecked(true);
                    }
                    if (!Util.isNullOrZeroNumber(patientLifeStyle.getLaptopInBedRoomForMinute())) {
                        editLaptopBedroom.setText(getNoOfHours(patientLifeStyle.getLaptopInBedRoomForMinute()));
                    }
                }
            }

            if (patientLifeStyle.getType() != null) {
                RadioButton radioTv = (RadioButton) radioLifeStyle.findViewWithTag(patientLifeStyle.getType());
                if (radioTv != null) {
                    radioTv.setChecked(true);
                }

            }
        }
    }

    private void setFromTimeAndToTime(SleepPattern sleepPattern, EditText editTime, TextView tvFromTime, TextView tvToTime) {
        if (!Util.isNullOrZeroNumber(sleepPattern.getNoOfminute()))
            editTime.setText(getNoOfHours(sleepPattern.getNoOfminute()));
        if (sleepPattern.getHours() != null) {
            if (!Util.isNullOrZeroNumber(sleepPattern.getHours().getFromTime()))
                tvFromTime.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) sleepPattern.getHours().getFromTime())));
            if (!Util.isNullOrZeroNumber(sleepPattern.getHours().getToTime()))
                tvToTime.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) sleepPattern.getHours().getToTime())));
        }
    }

    private String getNoOfHours(Integer noOfMinute) {
        int hours = noOfMinute / 60; //since both are ints, you get an int
        int minutes = noOfMinute % 60;
        return hours + "." + minutes;
    }

    private void initAdapter() {
        adapter = new UIPermissionItemGridAdapter(mActivity, this);
        gvWorkingDay.setAdapter(adapter);
        notifyAdapter(ALL_WORKING_DAY);
    }

    private void notifyAdapter(List<String> list) {
        adapter.setListData(list);
        adapter.notifyDataSetChanged();
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
                        if (!Util.isNullOrBlank(assessmentId)) {
                            getPatientLifeStyle();
                        }
                    }
                    break;
                case GET_PATIENT_LIFE_STYLE:
                    if (response.isValidData(response) && response.getData() instanceof PatientLifeStyle) {
                        patientLifeStyle = (PatientLifeStyle) response.getData();
                        if (patientLifeStyle != null) {
                            initData();
                        }
                    }
                    break;
                case ADD_PATIENT_LIFE_STYLE:
                    if (response.isValidData(response) && response.getData() instanceof PatientLifeStyle) {
                        PatientLifeStyle data = (PatientLifeStyle) response.getData();
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
            case R.id.tv_sleeping_from_time_day:
            case R.id.tv_sleeping_to_time_day:
            case R.id.tv_sleeping_from_time_night:
            case R.id.tv_sleeping_to_time_night:
            case R.id.tv_tv_view_from_time:
            case R.id.tv_tv_view_to_time:
            case R.id.tv_social_media_from_time:
            case R.id.tv_social_media_to_time:
            case R.id.tv_mobile_usage_from_time:
            case R.id.tv_mobile_usage_to_time:
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.layout_add_working_hrs:
                openAddWorkingHrsDialog(null, false);
                break;
            case R.id.layout_add_travelling_period:
                openAddWorkingHrsDialog(null, true);
                break;
            case EDIT_ID:
                WorkingHours workingHours = (WorkingHours) v.getTag();
                if (workingHours != null) {
                    if (workingHrsHashMap.containsKey(workingHours.getCustomUniqueId()))
                        openAddWorkingHrsDialog(workingHours, false);
                    if (travellingTimeHashMap.containsKey(workingHours.getCustomUniqueId()))
                        openAddWorkingHrsDialog(workingHours, true);
                }
                break;
            case DELETE_ID:
                String tag = (String) v.getTag();
                if (tag != null) {
                    if (workingHrsHashMap.containsKey(tag)) {
                        workingHrsHashMap.remove(tag);
                        addWorkingHours(new ArrayList<>(workingHrsHashMap.values()), layoutWorkingHrs);
                    }
                    if (travellingTimeHashMap.containsKey(tag)) {
                        travellingTimeHashMap.remove(tag);
                        addWorkingHours(new ArrayList<>(travellingTimeHashMap.values()), layoutTravellingPeriod);
                    }
                }
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
            addLifeStyle();
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addLifeStyle() {
        mActivity.showLoading(false);

        PatientLifeStyle lifeStyle = new PatientLifeStyle();

        if (patientLifeStyle != null) {
            lifeStyle.setPatientId(patientLifeStyle.getPatientId());
            lifeStyle.setAssessmentId(patientLifeStyle.getAssessmentId());
            lifeStyle.setDoctorId(patientLifeStyle.getDoctorId());
            lifeStyle.setLocationId(patientLifeStyle.getLocationId());
            lifeStyle.setHospitalId(patientLifeStyle.getHospitalId());

        } else {
            if (!Util.isNullOrBlank(patientId))
                lifeStyle.setPatientId(patientId);
            if (!Util.isNullOrBlank(assessmentId))
                lifeStyle.setAssessmentId(assessmentId);
            lifeStyle.setDoctorId(user.getUniqueId());
            lifeStyle.setLocationId(user.getForeignLocationId());
            lifeStyle.setHospitalId(user.getForeignHospitalId());
        }


        if (!Util.isNullOrEmptyList(workingHrsHashMap))
            lifeStyle.setWorkingschedules(getSelectedWorkinSchedule());

        lifeStyle.setSleepPatterns(getSleepPattern());

        if (!Util.isNullOrEmptyList(travellingTimeHashMap))
            lifeStyle.setTrivalingPeriod(getSelectedTravelingPeriod());

        lifeStyle.setSocialMediaTime(getMinutes(editSocialMeadia));

        RadioButton radioTv = (RadioButton) radioTvBedroom.findViewWithTag(getString(R.string.yes));
        if (radioTv.isChecked()) {
            lifeStyle.setTvInBedRoom(true);
            lifeStyle.setTvInBedRoomForMinute(getMinutes(editTvBedroom));
        } else {
            lifeStyle.setTvInBedRoom(false);
        }

        RadioButton radioLaptop = (RadioButton) radioLaptopBedroom.findViewWithTag(getString(R.string.yes));
        if (radioLaptop.isChecked()) {
            lifeStyle.setLaptopInBedRoom(true);
            lifeStyle.setLaptopInBedRoomForMinute(getMinutes(editLaptopBedroom));
        } else
            lifeStyle.setLaptopInBedRoom(false);


        SleepPattern tvViewPattern = new SleepPattern();
        if (!Util.isNullOrBlank(String.valueOf(editTvViewTime.getText()))) {
            tvViewPattern.setNoOfminute(getMinutes(editTvViewTime));
        } else {
            WorkingHours workingHours = new WorkingHours();
            if (!Util.isNullOrBlank(String.valueOf(tvFromTimeTvWaching.getText())))
                workingHours.setFromTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvFromTimeTvWaching.getTag()));
            if (!Util.isNullOrBlank(String.valueOf(tvToTimeTvWaching.getText())))
                workingHours.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvToTimeTvWaching.getTag()));
            tvViewPattern.setHours(workingHours);
        }
        lifeStyle.setTvViewTime(tvViewPattern);

        SleepPattern socialMedia = new SleepPattern();
        if (!Util.isNullOrBlank(String.valueOf(editSocialMeadia.getText()))) {
            socialMedia.setNoOfminute(getMinutes(editSocialMeadia));
        } else {
            WorkingHours workingHours = new WorkingHours();
            if (!Util.isNullOrBlank(String.valueOf(tvFromTimeSocialMedia.getText())))
                workingHours.setFromTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvFromTimeSocialMedia.getTag()));
            if (!Util.isNullOrBlank(String.valueOf(tvToTimeSocialMedia.getText())))
                workingHours.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvToTimeSocialMedia.getTag()));
            socialMedia.setHours(workingHours);
        }
        lifeStyle.setLoptopUseTime(socialMedia);

        SleepPattern mobileUsePattern = new SleepPattern();
        if (!Util.isNullOrBlank(String.valueOf(editMobileUsage.getText()))) {
            mobileUsePattern.setNoOfminute(getMinutes(editMobileUsage));
        } else {
            WorkingHours workingHours = new WorkingHours();
            if (!Util.isNullOrBlank(String.valueOf(tvFromTimeMobileUsage.getText())))
                workingHours.setFromTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvFromTimeMobileUsage.getTag()));
            if (!Util.isNullOrBlank(String.valueOf(tvToTimeMobileUsage.getText())))
                workingHours.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvToTimeMobileUsage.getTag()));
            mobileUsePattern.setHours(workingHours);
        }
        lifeStyle.setMobileUsage(mobileUsePattern);

        View checkedRadioButton = view.findViewById(radioLifeStyle.getCheckedRadioButtonId());
        if (checkedRadioButton != null) {
            lifeStyle.setType((LifeStyleType) checkedRadioButton.getTag());
        }

        WebDataServiceImpl.getInstance(mApp).addPatientAssessmentInfo(PatientLifeStyle.class,
                WebServiceType.ADD_PATIENT_LIFE_STYLE, lifeStyle, this, this);
    }

    private List<WorkingSchedule> getSelectedTravelingPeriod() {
        List<WorkingSchedule> workingScheduleList = new ArrayList<>();

        WorkingSchedule workingSchedule = new WorkingSchedule();
        workingSchedule.setWorkingHours(new ArrayList<>(travellingTimeHashMap.values()));
        workingScheduleList.add(workingSchedule);

        return workingScheduleList;
    }

    private List<WorkingSchedule> getSelectedWorkinSchedule() {
        List<WorkingSchedule> workingScheduleList = new ArrayList<>();
        for (String selectedDay : selectedWorkingDays) {
            WorkingSchedule workingSchedule = new WorkingSchedule();
            WeekDayNameType weekDayNameType = WeekDayNameType.valueOf(selectedDay.toUpperCase());
            if (weekDayNameType != null) {
                workingSchedule.setWorkingDay(weekDayNameType);
                workingSchedule.setWorkingHours(new ArrayList<>(workingHrsHashMap.values()));

                workingScheduleList.add(workingSchedule);
            }
        }
        return workingScheduleList;
    }

    private List<SleepPattern> getSleepPattern() {
        List<SleepPattern> sleepPatternList = new ArrayList<>();

        if (cbDay.isChecked()) {
            SleepPattern sleepPattern = new SleepPattern();
            if (!Util.isNullOrBlank(String.valueOf(editSleepTimeDay.getText()))) {
                sleepPattern.setNoOfminute(getMinutes(editSleepTimeDay));
            } else {
                WorkingHours workingHours = new WorkingHours();
                if (!Util.isNullOrBlank(String.valueOf(tvFromTimeSleepingDay.getText())))
                    workingHours.setFromTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvFromTimeSleepingDay.getTag()));
                if (!Util.isNullOrBlank(String.valueOf(tvToTimeSleepingDay.getText())))
                    workingHours.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvToTimeSleepingDay.getTag()));
                sleepPattern.setHours(workingHours);
            }
            sleepPattern.setTimeType(SleepTimeType.DAY);
            sleepPatternList.add(sleepPattern);
        }
        if (cbNight.isChecked()) {
            SleepPattern sleepPattern = new SleepPattern();
            if (!Util.isNullOrBlank(String.valueOf(editSleepTimeNight.getText()))) {
                sleepPattern.setNoOfminute(getMinutes(editSleepTimeNight));
            } else {
                WorkingHours workingHours = new WorkingHours();
                if (!Util.isNullOrBlank(String.valueOf(tvFromTimeSleepingNight.getText())))
                    workingHours.setFromTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvFromTimeSleepingNight.getTag()));
                if (!Util.isNullOrBlank(String.valueOf(tvToTimeSleepingNight.getText())))
                    workingHours.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvToTimeSleepingNight.getTag()));
                sleepPattern.setHours(workingHours);
            }
            sleepPattern.setTimeType(SleepTimeType.NIGHT);
            sleepPatternList.add(sleepPattern);
        }
        return sleepPatternList;
    }

    private Integer getMinutes(EditText editSleepTimeDay) {
        int minutes = 0;
        String hrs = Util.getValidatedValueOrNull(editSleepTimeDay);
        if (!Util.isNullOrBlank(hrs)) {
            if (hrs.contains(".")) {
                String[] split = hrs.split("\\.");
                if (split.length > 1) {
                    int hr = Integer.parseInt(split[0]);
                    int min = Integer.parseInt(split[1]);
                    minutes = (hr * 60) + min;
                }
            } else {
                minutes = Integer.parseInt(hrs) * 60;
            }
        }
        return minutes;
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


    @Override
    public boolean isAssigned(String permission) {
        return selectedWorkingDays.contains(permission);
    }

    @Override
    public void assignPermission(String permission) {
        if (!selectedWorkingDays.contains(permission))
            selectedWorkingDays.add(permission);
    }

    @Override
    public void removePermission(String permission) {
        if (selectedWorkingDays.contains(permission))
            selectedWorkingDays.remove(permission);
    }


    private void openTimePickerDialog(final String selectedFromTime, final TextView tvToTime) {
        String defaultPickerTime = selectedFromTime;
        String textTime = Util.getValidatedValueOrNull(tvToTime);
        boolean isTextShown = false;
        if (!Util.isNullOrBlank(textTime)) {
            isTextShown = true;
            defaultPickerTime = textTime;
        }
        final Calendar calendar = DateTimeUtil.getCalendarInstanceFromFormattedTime(TIME_FORMAT, defaultPickerTime, isTextShown, DEFAULT_TIME_INTERVAL);

        datePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                long selectedFromDateTimeMillis = getSelectedFromDateTime(hourOfDay, minute);
                int msg = 0;
//                if (!DateTimeUtil.selectedTimeIsGreaterThanTime(selectedFromDateTimeMillis, DateTimeUtil.getCurrentDateLong(DATE_FORMAT_USED_IN_THIS_SCREEN + "" + TIME_FORMAT))) {
//                    msg = R.string.time_to_should_be_greater_than_current_time;
//                }
                if (msg == 0) {
                    LogUtils.LOGD(TAG, "Time lesser");
                    tvToTime.setText(DateTimeUtil.getFormattedDateTime(TIME_FORMAT, selectedFromDateTimeMillis));
                    tvToTime.setTag(selectedFromDateTimeMillis);
                } else {
                    openTimePickerDialog(selectedFromTime, tvToTime);
                    Util.showToast(mActivity, msg);
                    LogUtils.LOGD(TAG, "Time greater");
                }
            }

        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        if (!datePickerDialog.isShowing()) {
            datePickerDialog.show();
        }
    }

    private long getSelectedFromDateTime(int hourOfDay, int minute) {
        Calendar calendar1 = DateTimeUtil.getCalendarInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar1.set(Calendar.MINUTE, minute);
        return calendar1.getTimeInMillis();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (radioGroup.getId()) {
            case R.id.rg_tv_in_bedroom:
                RadioButton radioButton = (RadioButton) radioTvBedroom.findViewWithTag(getString(R.string.yes));
                if (radioButton.isChecked()) {
                    Util.toggleLayoutView(mActivity, layoutTvBedroom, true);
                } else {
                    Util.toggleLayoutView(mActivity, layoutTvBedroom, false);
                }
                break;
            case R.id.rg_laptop_in_bedroom:
                RadioButton radioButton1 = (RadioButton) radioLaptopBedroom.findViewWithTag(getString(R.string.yes));
                if (radioButton1.isChecked()) {
                    Util.toggleLayoutView(mActivity, layoutLaptopBedroom, true);
                } else {
                    Util.toggleLayoutView(mActivity, layoutLaptopBedroom, false);
                }
                break;
        }
    }

    private void openAddWorkingHrsDialog(WorkingHours workingHours, boolean isTravelingPeriod) {

        AddWorkingHrsDialogFragment workingHrsDialogFragment = new AddWorkingHrsDialogFragment();
        Bundle bundle = new Bundle();
        if (workingHours != null) {
            bundle.putParcelable(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(workingHours));
        }
        bundle.putBoolean(HealthCocoConstants.TAG_IS_TRAVELLING_PERIOD, isTravelingPeriod);
        workingHrsDialogFragment.setArguments(bundle);

        workingHrsDialogFragment.setTargetFragment(this, HealthCocoConstants.REQUEST_CODE_ADD_WORKING_HRS);
        workingHrsDialogFragment.show(mActivity.getSupportFragmentManager(),
                workingHrsDialogFragment.getClass().getSimpleName());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK)
            switch (requestCode) {
                case HealthCocoConstants.REQUEST_CODE_ADD_WORKING_HRS:
                    if (data != null) {
                        WorkingHours hours = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        boolean isTravelingPeriod = data.getBooleanExtra(HealthCocoConstants.TAG_IS_TRAVELLING_PERIOD, false);
                        if (hours != null) {
                            if (!isTravelingPeriod) {
                                workingHrsHashMap.put(hours.getCustomUniqueId(), hours);
                                addWorkingHours(new ArrayList<>(workingHrsHashMap.values()), layoutWorkingHrs);
                            } else {
                                travellingTimeHashMap.put(hours.getCustomUniqueId(), hours);
                                addWorkingHours(new ArrayList<>(travellingTimeHashMap.values()), layoutTravellingPeriod);
                            }
                        }
                    }
                    break;

            }

    }

    private void addWorkingHours(ArrayList<WorkingHours> workingHoursList, LinearLayout
            containerWorkingHrs) {
        containerWorkingHrs.removeAllViews();
        if (!Util.isNullOrEmptyList(workingHoursList)) {
            containerWorkingHrs.setVisibility(View.VISIBLE);
            for (WorkingHours workingHours : workingHoursList) {

                LinearLayout layoutEquivalentQuantity = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_item_working_hours, null);
                TextView tvFromTime = (TextView) layoutEquivalentQuantity.findViewById(R.id.tv_from_time);
                TextView tvToTime = (TextView) layoutEquivalentQuantity.findViewById(R.id.tv_to_time);

                TextViewFontAwesome btDelete = (TextViewFontAwesome) layoutEquivalentQuantity.findViewById(R.id.bt_delete);
                TextViewFontAwesome btEdit = (TextViewFontAwesome) layoutEquivalentQuantity.findViewById(R.id.bt_edit);

                tvFromTime.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) workingHours.getFromTime())));
                tvToTime.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) workingHours.getToTime())));


                btEdit.setId(EDIT_ID);
                btEdit.setTag(workingHours);
                btEdit.setOnClickListener(this);

                btDelete.setId(DELETE_ID);
                btDelete.setTag(workingHours.getCustomUniqueId());
                btDelete.setOnClickListener(this);

                containerWorkingHrs.addView(layoutEquivalentQuantity);
            }
        } else
            containerWorkingHrs.setVisibility(View.GONE);
    }

}
