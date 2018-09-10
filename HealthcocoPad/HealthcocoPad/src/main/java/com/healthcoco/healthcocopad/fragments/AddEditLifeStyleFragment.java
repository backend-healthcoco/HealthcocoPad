package com.healthcoco.healthcocopad.fragments;

import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.UIPermissionItemGridAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonUiPermissionsListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Prashant on 14/07/2018.
 */

public class AddEditLifeStyleFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener, CommonUiPermissionsListener {

    public static final String TIME_FORMAT = "hh:mm aaa";
    public static final int DEFAULT_TIME_INTERVAL = 15;
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    TimePickerDialog datePickerDialog;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView editFromTimeWorkingHrs;
    private TextView editToTimeWorkingHrs;
    private TextView editFromTimeSleepingDay;
    private TextView editToTimeSleepingDay;
    private TextView editFromTimeSleepingNight;
    private TextView editToTimeSleepingNight;
    private EditText editMobileUsage;
    private EditText editTravellingPeriod;
    private EditText editTvViewTime;
    private EditText editSocialMeadia;
    //    private TextView editFromTimeSocialMedia;
//    private TextView editToTimeSocialMedia;
    //    private EditText editFromTimeTvWaching;
//    private EditText editToTimeTvWaching;
    private CheckBox cbDay;
    private CheckBox cbNight;
    private LinearLayout layoutSleepingDay;
    private LinearLayout layoutSleepingNight;
    private RadioGroup radioTvBedroom;
    private RadioGroup radioLaptopBedroom;
    private RadioGroup radioLifeStyle;
    private GridView gvWorkingDay;
    private UIPermissionItemGridAdapter adapter;
    private ArrayList<String> selectedWorkingDays = new ArrayList<>();
    private ArrayList<String> allWorkingDay = new ArrayList<String>() {{
        add("Sunday");
        add("Monday");
        add("Tuesday");
        add("Wednesday");
        add("Thursday");
        add("Friday");
        add("Saturday");
    }};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_life_style, container, false);
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

        editFromTimeWorkingHrs = (TextView) view.findViewById(R.id.edit_working_hrs_from_time);
        editToTimeWorkingHrs = (TextView) view.findViewById(R.id.edit_working_hrs_to_time);
        editFromTimeSleepingDay = (TextView) view.findViewById(R.id.edit_sleeping_from_time_day);
        editToTimeSleepingDay = (TextView) view.findViewById(R.id.edit_sleeping_to_time_day);
        editFromTimeSleepingNight = (TextView) view.findViewById(R.id.edit_sleeping_from_time_night);
        editToTimeSleepingNight = (TextView) view.findViewById(R.id.edit_sleeping_to_time_night);
        editTravellingPeriod = (EditText) view.findViewById(R.id.edit_travelling_peroid);
        editMobileUsage = (EditText) view.findViewById(R.id.edit_mobile_usage);
        editTvViewTime = (EditText) view.findViewById(R.id.edit_tv_view_time);
        editSocialMeadia = (EditText) view.findViewById(R.id.edit_social_media);
//        editFromTimeTvWaching = (EditText) view.findViewById(R.id.edit_tv_view_from_time);
//        editToTimeTvWaching = (EditText) view.findViewById(R.id.edit_tv_view_to_time);
//        editFromTimeSocialMedia = (TextView) view.findViewById(R.id.edit_social_media_from_time);
//        editToTimeSocialMedia = (TextView) view.findViewById(R.id.edit_social_media_to_time);
        cbDay = (CheckBox) view.findViewById(R.id.cb_day);
        cbNight = (CheckBox) view.findViewById(R.id.cb_night);
        radioTvBedroom = (RadioGroup) view.findViewById(R.id.rg_tv_in_bedroom);
        radioLaptopBedroom = (RadioGroup) view.findViewById(R.id.rg_laptop_in_bedroom);
        radioLifeStyle = (RadioGroup) view.findViewById(R.id.rg_life_style);
        layoutSleepingDay = (LinearLayout) view.findViewById(R.id.layout_sleep_pattern_day);
        layoutSleepingNight = (LinearLayout) view.findViewById(R.id.layout_sleep_pattern_night);

        gvWorkingDay = (GridView) view.findViewById(R.id.gv_working_day);

    }

    @Override
    public void initListeners() {
        cbDay.setOnCheckedChangeListener(this);
        cbNight.setOnCheckedChangeListener(this);

        editFromTimeWorkingHrs.setOnClickListener(this);
        editToTimeWorkingHrs.setOnClickListener(this);
        editFromTimeSleepingDay.setOnClickListener(this);
        editToTimeSleepingDay.setOnClickListener(this);
        editFromTimeSleepingNight.setOnClickListener(this);
        editToTimeSleepingNight.setOnClickListener(this);
//        editFromTimeSocialMedia.setOnClickListener(this);
//        editToTimeSocialMedia.setOnClickListener(this);
    }

    public void initData() {
    }

    private void initAdapter() {
        adapter = new UIPermissionItemGridAdapter(mActivity, this);
        gvWorkingDay.setAdapter(adapter);
        notifyAdapter(allWorkingDay);
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
            case R.id.edit_working_hrs_from_time:
            case R.id.edit_working_hrs_to_time:
            case R.id.edit_sleeping_from_time_day:
            case R.id.edit_sleeping_to_time_day:
            case R.id.edit_sleeping_from_time_night:
            case R.id.edit_sleeping_to_time_night:
                openTimePickerDialog(null, (TextView) v);
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
        Util.getValidatedValueOrNull(editTravellingPeriod);
        Util.getValidatedValueOrNull(editTvViewTime);
        Util.getValidatedValueOrNull(editMobileUsage);
        Util.getValidatedValueOrNull(editSocialMeadia);

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

}
