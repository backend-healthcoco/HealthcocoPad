package com.healthcoco.healthcocopad.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.ClinicDetailResponse;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DoctorListPopupWindowListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.popupwindow.HealthcocoPopupWindow;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import static com.healthcoco.healthcocopad.enums.AppointmentStatusType.ALL;

/**
 * Created by Prashant on 01-03-2018.
 */


public class QueueFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised, View.OnClickListener, HealthcocoTextWatcherListener, DoctorListPopupWindowListener {

    public static final String DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    public static final String DATE_FORMAT_SELECTED_FOR_THIS_SCREEN = "MMM yyyy";
    public static final String MONTH_FORMAT_FOR_THIS_SCREEN = "MMM yyyy";
    public static final String DATE_FORMAT_FOR_THIS_SCREEN = "dd MMM yyyy";
    public static final int MAX_SIZE = 10;
    public static final int MAX_NUMBER_OF_EVENTS = 30;
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    public ArrayList<ClinicDoctorProfile> clinicDoctorProfileList;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private User user;
    private User loggedInUser;
    private long selectedMonthDayYearInMillis;
    private ArrayList<CalendarEvents> calenderEventList = null;
    private AppointmentStatusType appointmentStatusType = ALL;
    private LinkedHashMap<String, ClinicDoctorProfile> clinicDoctorListHashMap = new LinkedHashMap<>();
    private ClinicDetailResponse selectedClinicProfile;
    private HealthcocoPopupWindow doctorsListPopupWindow;
    private boolean isSingleDoctor = false;
    private boolean isInitialLoading = true;

    private TextView tvDoctorName;
    private LinearLayout lvDoctorName;
    private ImageButton btPreviousDate;
    private ImageButton btNextDate;
    private TextView tvSelectedDate;
    private HorizontalScrollView horizontalScrollView;

    private ScheduledQueueFragment scheduledQueueFragment;
    private WaitingQueueFragment waitingQueueFragment;
    private EngagedQueueFragment engagedQueueFragment;
    private CheckedOutQueueFragment checkedOutQueueFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_queue, container, false);
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
        initFragments();
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initFragments() {
        scheduledQueueFragment = new ScheduledQueueFragment();
        mFragmentManager.beginTransaction().add(R.id.layout_scheduled_queue, scheduledQueueFragment, scheduledQueueFragment.getClass().getSimpleName()).commit();

        waitingQueueFragment = new WaitingQueueFragment();
        mFragmentManager.beginTransaction().add(R.id.layout_waiting_queue, waitingQueueFragment, waitingQueueFragment.getClass().getSimpleName()).commit();

        engagedQueueFragment = new EngagedQueueFragment();
        mFragmentManager.beginTransaction().add(R.id.layout_engaged_queue, engagedQueueFragment, engagedQueueFragment.getClass().getSimpleName()).commit();

        checkedOutQueueFragment = new CheckedOutQueueFragment();
        mFragmentManager.beginTransaction().add(R.id.layout_checked_out_queue, checkedOutQueueFragment, checkedOutQueueFragment.getClass().getSimpleName()).commit();

    }


    @Override
    public void initViews() {
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        lvDoctorName = (LinearLayout) view.findViewById(R.id.lv_doctor_name);
        btPreviousDate = (ImageButton) view.findViewById(R.id.bt_previuos_date);
        btNextDate = (ImageButton) view.findViewById(R.id.bt_next_date);
        tvSelectedDate = (TextView) view.findViewById(R.id.tv_selected_date);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.scrollview_horizontal);
        horizontalScrollView.scrollTo(0, 0); // scroll to application top

    }

    @Override
    public void initListeners() {
        btPreviousDate.setOnClickListener(this);
        btNextDate.setOnClickListener(this);
        tvSelectedDate.addTextChangedListener(new HealthcocoTextWatcher(tvSelectedDate, this));

    }

    public void getCalendarEventsList(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.CALENDAR_EVENTS);
        WebDataServiceImpl.getInstance(mApp).getCalendarEvents(CalendarEvents.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), selectedMonthDayYearInMillis, latestUpdatedTime, this, this);
    }


    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        showLoadingOverlay(false);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        Calendar calendar = DateTimeUtil.getCalendarInstance();
                        if (selectedClinicProfile != null)
                            formHashMapAndRefresh(selectedClinicProfile.getDoctors());
                        else
                            refreshDoctorsList();
//                        onDateSetonDateSet(null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        refreshDoctorClinicText();
                        selectedMonthDayYearInMillis = calendar.getTimeInMillis();
                        tvSelectedDate.setText(DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN));
                        getCalendarEventsList(true);

                        return;
                    }
                    break;
                case GET_CALENDAR_EVENTS:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_CALENDAR_EVENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    break;
                case ADD_APPOINTMENT:
                    refreshData();
                    isInitialLoading = false;
                    break;
                case GET_CLINIC_PROFILE:
                    if (response.getData() != null) {
                        selectedClinicProfile = (ClinicDetailResponse) response.getData();
                        formHashMapAndRefresh(selectedClinicProfile.getDoctors());
//                        initSelectedDoctorClinicData();
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_CLINIC_PROFILE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);

                    }
                    if (!response.isUserOnline())
                        onNetworkUnavailable(response.getWebServiceType());
                    break;
                default:
                    break;
            }
        }

    }

    private void refreshDoctorsList() {
        showLoadingOverlay(true);
        WebDataServiceImpl.getInstance(mApp).getClinicDetails(ClinicDetailResponse.class, user.getForeignLocationId(), this, this);
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
                    try {
                        loggedInUser = new User();
                        ReflectionUtil.copy(loggedInUser, doctor.getUser());
                    } catch (Exception e) {
                        e.printStackTrace();
                        loggedInUser = doctor.getUser();
                    }
                    user = doctor.getUser();
                    selectedClinicProfile = LocalDataServiceImpl.getInstance(mApp).getClinicResponseDetails(user.getForeignLocationId());
                }
                return volleyResponseBean;
            case ADD_CALENDAR_EVENTS:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.ADD_APPOINTMENT);

                LocalDataServiceImpl.getInstance(mApp).addCalendarEventsList(
                        (ArrayList<CalendarEvents>) (ArrayList<?>) response
                                .getDataList());
                return volleyResponseBean;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    private void refreshData() {
        scheduledQueueFragment.reFreshQueue(selectedMonthDayYearInMillis, clinicDoctorProfileList);
        waitingQueueFragment.reFreshQueue(selectedMonthDayYearInMillis, clinicDoctorProfileList);
        engagedQueueFragment.reFreshQueue(selectedMonthDayYearInMillis, clinicDoctorProfileList);
        checkedOutQueueFragment.reFreshQueue(selectedMonthDayYearInMillis, clinicDoctorProfileList);

    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    private void formHashMapAndRefresh(List<ClinicDoctorProfile> responseList) {
        clinicDoctorProfileList = (ArrayList<ClinicDoctorProfile>) responseList;
        if (responseList.size() > 1) {
            if (!Util.isNullOrEmptyList(responseList)) {
                for (ClinicDoctorProfile clinicDoctorProfile :
                        responseList) {
                    clinicDoctorListHashMap.put(clinicDoctorProfile.getUniqueId(), clinicDoctorProfile);
                }
            }
//            notifyAdapter(new ArrayList<ClinicDoctorProfile>(clinicDoctorListHashMap.values()));
            if (doctorsListPopupWindow != null)
                doctorsListPopupWindow.notifyAdapter(new ArrayList<Object>(clinicDoctorListHashMap.values()));
            else
                mActivity.initDoctorListPopupWindows(tvDoctorName, PopupWindowType.DOCTOR_LIST, new ArrayList<Object>(clinicDoctorListHashMap.values()), this);
        } else {
            lvDoctorName.setVisibility(View.INVISIBLE);
            isSingleDoctor = true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next_date:
                long nextDateTimeInMillis = DateTimeUtil.getNextDate(selectedMonthDayYearInMillis);
                selectedMonthDayYearInMillis = nextDateTimeInMillis;
                tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, nextDateTimeInMillis));
                break;
            case R.id.bt_previuos_date:
                long previousDateTimeInMillis = DateTimeUtil.getPreviousDate(selectedMonthDayYearInMillis);
                selectedMonthDayYearInMillis = previousDateTimeInMillis;
                tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, previousDateTimeInMillis));
                break;
        }
    }


    private void refreshDoctorClinicText() {
        tvDoctorName.setText(R.string.all_doctor);
    }

    @Override
    public void afterTextChange(View v, String s) {
        switch (v.getId()) {
            case R.id.tv_selected_date:
                LogUtils.LOGD(TAG, "TextVieew Selected Date ");

                if (!DateTimeUtil.isCurrentDateSelected(DATE_FORMAT_USED_IN_THIS_SCREEN,
                        Util.getValidatedValueOrNull(tvSelectedDate))) {
                    if (!isInitialLoading)
                        refreshData();
                } else
                    tvSelectedDate.setText(R.string.today);
                break;
        }
    }

    @Override
    public void onDoctorSelected(ArrayList<ClinicDoctorProfile> clinicDoctorProfileList) {
        this.clinicDoctorProfileList = clinicDoctorProfileList;
        String doctorName = "";
        if (!Util.isNullOrEmptyList(clinicDoctorProfileList)) {
            if (clinicDoctorProfileList.size() == clinicDoctorListHashMap.size())
                tvDoctorName.setText(R.string.all_doctor);
            else {
                for (ClinicDoctorProfile clinicDoctorProfile : clinicDoctorProfileList) {
                    doctorName = doctorName + clinicDoctorProfile.getFirstNameWithTitle() + ", ";
                }
                doctorName = doctorName.substring(0, doctorName.length() - 2);
                tvDoctorName.setText(doctorName);
            }
        }
        refreshData();
    }

    @Override
    public void onEmptyListFound() {

    }
}
