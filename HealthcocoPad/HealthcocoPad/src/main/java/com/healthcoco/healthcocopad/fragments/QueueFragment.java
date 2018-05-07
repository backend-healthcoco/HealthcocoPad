package com.healthcoco.healthcocopad.fragments;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.calendar.CalendarFragment;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType;
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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

import static com.healthcoco.healthcocopad.enums.AppointmentStatusType.ALL;
import static com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType.APPOINTMENTS_QUEUE_ADD_NEW;

/**
 * Created by Prashant on 01-03-2018.
 */


public class QueueFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised, View.OnClickListener, HealthcocoTextWatcherListener, DoctorListPopupWindowListener {

    public static final String INTENT_GET_APPOINTMENT_LIST_LOCAL = "com.healthcoco.APPOINTMENT_LIST_LOCAL";
    public static final String INTENT_GET_APPOINTMENT_LIST_SERVER = "com.healthcoco.APPOINTMENT_LIST_SERVER";

    public static final String DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    public static final int MAX_SIZE = 10;
    public static final int MAX_NUMBER_OF_EVENTS = 30;
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    public ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList;
    public List<RegisteredDoctorProfile> registeredDoctorProfileList;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private User user;
    private User loggedInUser;
    private long selectedMonthDayYearInMillis;
    BroadcastReceiver appointmentListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                getCalendarEventsList(true);
            }
        }
    };
    private long curentMonthDayYearInMillis;
    private ArrayList<CalendarEvents> calenderEventList = null;
    private AppointmentStatusType appointmentStatusType = ALL;
    private LinkedHashMap<String, RegisteredDoctorProfile> clinicDoctorListHashMap = new LinkedHashMap<>();
    private HealthcocoPopupWindow doctorsListPopupWindow;
    private boolean isSingleDoctor = false;
    private boolean isInitialLoading = true;
    private boolean receiversRegistered;
    private TextView tvDoctorName;
    private LinearLayout lvDoctorName;
    private ImageButton btPreviousDate;
    private ImageButton btNextDate;
    private ImageButton btRefresh;
    private ImageButton btMenu;
    private TextView tvSelectedDate;
    private FloatingActionButton floatingActionButton;
    private HorizontalScrollView horizontalScrollView;
    private FrameLayout layoutWeekView;
    private View.OnClickListener clickListener;
    private ScheduledQueueFragment scheduledQueueFragment;
    private WaitingQueueFragment waitingQueueFragment;
    private EngagedQueueFragment engagedQueueFragment;
    private CheckedOutQueueFragment checkedOutQueueFragment;
    BroadcastReceiver appointmentListReceiverLocal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                refreshQueueData();
            }
        }
    };
    private CalendarFragment calendarFragment;
    private BookAppointmentFromScreenType screenType = APPOINTMENTS_QUEUE_ADD_NEW;

    public QueueFragment(Activity activity) {
        super();
        clickListener = (View.OnClickListener) activity;
    }


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

        calendarFragment = new CalendarFragment();
        mFragmentManager.beginTransaction().add(R.id.layout_weekview, calendarFragment, calendarFragment.getClass().getSimpleName()).commit();

    }


    @Override
    public void initViews() {
        btMenu = (ImageButton) view.findViewById(R.id.bt_menu);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        lvDoctorName = (LinearLayout) view.findViewById(R.id.lv_doctor_name);
        btPreviousDate = (ImageButton) view.findViewById(R.id.bt_previuos_date);
        btNextDate = (ImageButton) view.findViewById(R.id.bt_next_date);
        btRefresh = (ImageButton) view.findViewById(R.id.bt_refresh);
        tvSelectedDate = (TextView) view.findViewById(R.id.tv_selected_date);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_bt_add_appointment);
        layoutWeekView = (FrameLayout) view.findViewById(R.id.layout_weekview);
        horizontalScrollView = (HorizontalScrollView) view.findViewById(R.id.scrollview_horizontal);
        horizontalScrollView.scrollTo(0, 0); // scroll to application top


        horizontalScrollView.setVisibility(View.VISIBLE);
        layoutWeekView.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        btPreviousDate.setOnClickListener(this);
        btNextDate.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        btRefresh.setOnClickListener(this);
        tvSelectedDate.setOnClickListener(this);
        tvSelectedDate.addTextChangedListener(new HealthcocoTextWatcher(tvSelectedDate, this));

        btMenu.setOnClickListener(clickListener);

    }

    public void getCalendarEventsList(boolean showLoading) {
        if (showLoading)
            mActivity.showLoading(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.CALENDAR_EVENTS, selectedMonthDayYearInMillis);
        WebDataServiceImpl.getInstance(mApp).getCalendarEvents(CalendarEvents.class, registeredDoctorProfileList, user.getForeignLocationId(), user.getForeignHospitalId(), selectedMonthDayYearInMillis, latestUpdatedTime, this, this);
    }


    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        super.onErrorResponse(volleyResponseBean, errorMessage);
        refreshQueueData();
        refreshCalendarData();
        isInitialLoading = false;
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        Calendar calendar = DateTimeUtil.getCalendarInstance();
                        if (!Util.isNullOrEmptyList(registeredDoctorProfileList))
                            formHashMapAndRefresh(registeredDoctorProfileList);
                        else
                            refreshDoctorsList();
//                        onDateSetonDateSet(null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        refreshDoctorClinicText();
                        selectedMonthDayYearInMillis = calendar.getTimeInMillis();
                        curentMonthDayYearInMillis = selectedMonthDayYearInMillis;
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
                    } else {
                        refreshQueueData();
                        refreshCalendarData();
                        isInitialLoading = false;
                        mActivity.hideLoading();
                    }
                    break;
                case ADD_APPOINTMENT:
                    refreshQueueData();
                    refreshCalendarData();
                    isInitialLoading = false;
                    mActivity.hideLoading();
                    break;
                case GET_REGISTER_DOCTOR:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        registeredDoctorProfileList = (ArrayList<RegisteredDoctorProfile>) (ArrayList) response.getDataList();
                        formHashMapAndRefresh(registeredDoctorProfileList);
//                        initSelectedDoctorClinicData();
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_REGISTER_DOCTOR, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        mActivity.hideLoading();
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
        mActivity.showLoading(true);
        WebDataServiceImpl.getInstance(mApp).getRegisterDoctor(RegisteredDoctorProfile.class, user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
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
                    registeredDoctorProfileList = LocalDataServiceImpl.getInstance(mApp).getRegisterDoctorDetails(user.getForeignLocationId());
                }
                return volleyResponseBean;
            case ADD_REGISTER_DOCTOR:
                LocalDataServiceImpl.getInstance(mApp).addRegisterDoctorResponse((ArrayList<RegisteredDoctorProfile>) (ArrayList<?>) response.getDataList(), user.getForeignLocationId());
                break;
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

    private void refreshQueueData() {
        scheduledQueueFragment.reFreshQueue(selectedMonthDayYearInMillis, clinicDoctorProfileList);
        waitingQueueFragment.reFreshQueue(selectedMonthDayYearInMillis, clinicDoctorProfileList);
        engagedQueueFragment.reFreshQueue(selectedMonthDayYearInMillis, clinicDoctorProfileList);
        checkedOutQueueFragment.reFreshQueue(selectedMonthDayYearInMillis, clinicDoctorProfileList);

    }

    private void refreshCalendarData() {
        calendarFragment.reFreshCalendar(selectedMonthDayYearInMillis, clinicDoctorProfileList);
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    private void formHashMapAndRefresh(List<RegisteredDoctorProfile> responseList) {
        clinicDoctorProfileList = (ArrayList<RegisteredDoctorProfile>) responseList;
        if (responseList.size() > 1) {
            if (!Util.isNullOrEmptyList(responseList)) {
                for (RegisteredDoctorProfile registeredDoctorProfile :
                        responseList) {
                    clinicDoctorListHashMap.put(registeredDoctorProfile.getUserId(), registeredDoctorProfile);
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
            case R.id.fl_bt_add_appointment:
                bookWalkInAppointment();
                break;
            case R.id.bt_refresh:
                getCalendarEventsList(true);
                break;
            case R.id.tv_selected_date:
                openDatePickerDialog(tvSelectedDate);
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
                        if (DateTimeUtil.isCurrentMonthSelected(curentMonthDayYearInMillis, selectedMonthDayYearInMillis))
                            refreshQueueData();
                        else {
                            curentMonthDayYearInMillis = selectedMonthDayYearInMillis;
                            getCalendarEventsList(true);
                        }
                } else
                    tvSelectedDate.setText(R.string.today);
                break;
        }
    }

    @Override
    public void onDoctorSelected(ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList) {
        this.clinicDoctorProfileList = clinicDoctorProfileList;
        String doctorName = "";
        if (!Util.isNullOrEmptyList(clinicDoctorProfileList)) {
            if (clinicDoctorProfileList.size() == clinicDoctorListHashMap.size())
                tvDoctorName.setText(R.string.all_doctor);
            else {
                for (RegisteredDoctorProfile clinicDoctorProfile : clinicDoctorProfileList) {
                    doctorName = doctorName + clinicDoctorProfile.getFirstNameWithTitle() + ", ";
                }
                doctorName = doctorName.substring(0, doctorName.length() - 2);
                tvDoctorName.setText(doctorName);
            }
        }
        refreshQueueData();
        refreshCalendarData();
    }

    @Override
    public void onEmptyListFound() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for appointment list refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_APPOINTMENT_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(appointmentListReceiverLocal, filter);
            IntentFilter filter2 = new IntentFilter();
            filter.addAction(INTENT_GET_APPOINTMENT_LIST_SERVER);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(appointmentListReceiver, filter);
            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(appointmentListReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(appointmentListReceiverLocal);
    }


    public void bookWalkInAppointment() {
        BookAppointmentDialogFragment dialogFragment = new BookAppointmentDialogFragment();
        Bundle bundle = new Bundle();
//        bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, calendarEvents.getAppointmentId());
        bundle.putParcelable(BookAppointmentDialogFragment.TAG_FROM_SCREEN_TYPE, Parcels.wrap(screenType.ordinal()));
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(dialogFragment, PatientAppointmentDetailFragment.REQUEST_CODE_APPOINTMENTS_LIST);
        dialogFragment.show(mActivity.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());

    }

    private void openDatePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedMonthDayYearInMillis);
//        calendar = DateTimeUtil.setCalendarDefaultvalue(DateTimeUtil.DATE_FORMAT_WEEKDAY_DAY_MONTH_AS_TEXT_YEAR_DASH, calendar, textView);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedMonthDayYearInMillis = DateTimeUtil.getSelectedDate(year, monthOfYear, dayOfMonth, 0, 0, 0);
                textView.setText(DateTimeUtil.getFormattedTime(DATE_FORMAT_USED_IN_THIS_SCREEN, year, monthOfYear, dayOfMonth, 0, 0, 0));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker();
        datePickerDialog.show();
    }


}
