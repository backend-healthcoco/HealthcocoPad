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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.Events;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddEventDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.AddEventsFromScreenType;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DoctorListPopupWindowListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.popupwindow.EventDetailsPopupWindow;
import com.healthcoco.healthcocopad.popupwindow.HealthcocoPopupWindow;
import com.healthcoco.healthcocopad.recyclerview.EndlessRecyclerViewScrollListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import static com.healthcoco.healthcocopad.enums.AddEventsFromScreenType.EVENTS_LIST_ADD_NEW;
import static com.healthcoco.healthcocopad.enums.AppointmentStatusType.CONFIRM;

/**
 * Created by Prashant on 28-05-2018.
 */


public class EventFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised,
        View.OnClickListener, HealthcocoTextWatcherListener, DoctorListPopupWindowListener, HealthcocoRecyclerViewItemClickListener {

    public static final int MAX_SIZE = 10;

    public static final String INTENT_GET_EVENT_LIST_LOCAL = "com.healthcoco.EVENT_LIST_LOCAL";
    public static final String INTENT_GET_EVENT_LIST_SERVER = "com.healthcoco.EVENT_LIST_SERVER";


    //    public static final int MAX_COUNT = 10;
    public static final int MAX_NUMBER_OF_EVENTS = 50;
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    public ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList;
    public List<RegisteredDoctorProfile> registeredDoctorProfileList;
    Long latestUpdatedTime = 0l;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved = true;
    private User user;
    private User loggedInUser;
    private long selectedMonthDayYearInMillis;
    private long curentMonthDayYearInMillis;
    private LinkedHashMap<String, RegisteredDoctorProfile> clinicDoctorListHashMap = new LinkedHashMap<>();
    private HashMap<String, Events> eventsHashMap = new HashMap<>();
    private HealthcocoPopupWindow doctorsListPopupWindow;
    private boolean isSingleDoctor = false;
    private boolean isInitialLoading = true;
    private boolean receiversRegistered;
    private RecyclerView eventRecyclerView;
    private HealthcocoRecyclerViewAdapter mAdapter;
    private ArrayList<Events> eventsList = new ArrayList<>();
    BroadcastReceiver eventListReceiverLocal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                getListFromLocal(true);
            }
        }
    };
    private TextView tvNoEventsFound;
    private TextView tvDoctorName;
    private LinearLayout lvDoctorName;
    private LinearLayout layoutSwitchView;
    private ImageButton btPreviousDate;
    private ImageButton btNextDate;
    private ImageButton btRefresh;
    private ImageButton btMenu;
    private TextView tvQueue;
    private TextView tvOneDay;
    private TextView tvThreeDay;
    private TextView tvSelectedDate;
    private FloatingActionButton floatingActionButton;
    private View.OnClickListener clickListener;
    private AppointmentStatusType appointmentStatusType = CONFIRM;
    private AddEventsFromScreenType screenType = EVENTS_LIST_ADD_NEW;


    public EventFragment(Activity activity) {
        super();
        clickListener = (View.OnClickListener) activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_calendar_event, container, false);
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
        initAdapter();
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void initViews() {
        eventRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_events);
        tvNoEventsFound = (TextView) view.findViewById(R.id.tv_no_events_found);
        btMenu = (ImageButton) view.findViewById(R.id.bt_menu);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        lvDoctorName = (LinearLayout) view.findViewById(R.id.lv_doctor_name);
        layoutSwitchView = (LinearLayout) view.findViewById(R.id.layout_select_day);
        btPreviousDate = (ImageButton) view.findViewById(R.id.bt_previuos_date);
        btNextDate = (ImageButton) view.findViewById(R.id.bt_next_date);
        btRefresh = (ImageButton) view.findViewById(R.id.bt_refresh);
        tvSelectedDate = (TextView) view.findViewById(R.id.tv_selected_date);
        tvQueue = (TextView) view.findViewById(R.id.bt_queue);
        tvOneDay = (TextView) view.findViewById(R.id.bt_one_day);
        tvThreeDay = (TextView) view.findViewById(R.id.bt_three_day);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_bt_add_event);

        layoutSwitchView.setVisibility(View.GONE);

//        resetCaledarView(CalendarViewType.QUEUE);
    }

    @Override
    public void initListeners() {
        btPreviousDate.setOnClickListener(this);
        btNextDate.setOnClickListener(this);
        floatingActionButton.setOnClickListener(this);
        btRefresh.setOnClickListener(this);
        tvSelectedDate.setOnClickListener(this);
        tvQueue.setOnClickListener(this);
        tvOneDay.setOnClickListener(this);
        tvThreeDay.setOnClickListener(this);
        tvSelectedDate.addTextChangedListener(new HealthcocoTextWatcher(tvSelectedDate, this));

        btMenu.setOnClickListener(clickListener);

    }

    public void getEventsList(boolean showLoading) {

        if (isEndOfListAchieved) {
            latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.EVENTS, selectedMonthDayYearInMillis);
            if (showLoading)
                if (latestUpdatedTime > 0l) {
                    mActivity.showLoading(false);
                } else {
                    mActivity.showProgressDialog();
                }
        }
        WebDataServiceImpl.getInstance(mApp).getEvents(Events.class,
                user.getForeignLocationId(),
                DateTimeUtil.getFirstDayOfMonthMilli(selectedMonthDayYearInMillis), DateTimeUtil.getLastDayOfMonthMilli(selectedMonthDayYearInMillis),
                latestUpdatedTime, PAGE_NUMBER, MAX_NUMBER_OF_EVENTS, this, this);
    }


    private void resetListAndPagingAttributes() {
        PAGE_NUMBER = 0;
        isEndOfListAchieved = true;
        eventsHashMap.clear();
        eventsList.clear();
    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        eventRecyclerView.setLayoutManager(layoutManager);
        eventRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.EVENT_LIST, this);
        mAdapter.setListData((ArrayList<Object>) (Object) eventsList);
        eventRecyclerView.setAdapter(mAdapter);

        eventRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMore(current_page);
            }
        });
    }


    private void fromHashMapAndRefresh(ArrayList<Events> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (Events events :
                    responseList) {
                if (Util.isNullOrBlank(events.getDoctorName()))
                    events.setDoctorName("Dr. " + user.getFirstName());
                eventsHashMap.put(events.getUniqueId(), events);
            }
        }
        notifyAdapter(new ArrayList<>(eventsHashMap.values()));
    }

    private void notifyAdapter(ArrayList<Events> eventsArrayList) {
        if (!Util.isNullOrEmptyList(eventsArrayList)) {
            eventRecyclerView.setVisibility(View.VISIBLE);
            tvNoEventsFound.setVisibility(View.GONE);
            Collections.sort(eventsArrayList, ComparatorUtil.eventsFromToTimeComparator);
            eventsList.clear();
            eventsList.addAll(eventsArrayList);
            mAdapter.notifyDataSetChanged();
//            tvCount.setText(Util.getValidatedValue(eventsArrayList.size()));
        } else {
            eventRecyclerView.setVisibility(View.GONE);
            tvNoEventsFound.setVisibility(View.VISIBLE);
//            tvCount.setText(R.string.zero);
        }
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideProgressDialog();
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        super.onErrorResponse(volleyResponseBean, errorMessage);
        mActivity.hideProgressDialog();
        getListFromLocal(true);
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
                        getEventsList(true);
                        return;
                    }
                    break;
                case GET_EVENTS:
                    if (response.isDataFromLocal()) {
                        ArrayList<Events> responseDataList = (ArrayList<Events>) (ArrayList<?>) response.getDataList();
                        LogUtils.LOGD(TAG, "getSectionedDataListCalendar  loading onResponse");
                        if (Util.isNullOrEmptyList(responseDataList) || responseDataList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseDataList))
                            isEndOfListAchieved = true;
                        else isEndOfListAchieved = false;
                        if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            LogUtils.LOGD(TAG, "getSectionedDataListCalendar  loading Complete");
                            isInitialLoading = false;
                            fromHashMapAndRefresh(responseDataList);
                            return;
                        }
                        fromHashMapAndRefresh(responseDataList);
                        return;
                    } else {
                        if (Util.isNullOrEmptyList(response.getDataList()) || response.getDataList().size() < MAX_NUMBER_OF_EVENTS || Util.isNullOrEmptyList(response.getDataList())) {
                            isEndOfListAchieved = true;
                            mActivity.updateProgressDialog(10, 10);
                        } else {
                            PAGE_NUMBER = PAGE_NUMBER + 1;
                            isEndOfListAchieved = false;
                            mActivity.updateProgressDialog(10, 1);
                        }
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_EVENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }

                case ADD_EVENT:
                    if (!isEndOfListAchieved) {
                        getEventsList(true);
                    } else {
                        mActivity.hideProgressDialog();
                        getListFromLocal(true);
                        isInitialLoading = false;
                        mActivity.hideLoading();
                    }
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
                LocalDataServiceImpl.getInstance(mApp).
                        addRegisterDoctorResponse((ArrayList<RegisteredDoctorProfile>)
                                (ArrayList<?>) response.getDataList(), user.getForeignLocationId());
                break;
            case GET_EVENTS:
                if (user != null)
                    volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).
                            getEventsListResponseDayWise(WebServiceType.GET_EVENTS, appointmentStatusType,
                                    clinicDoctorProfileList, user.getForeignHospitalId(),
                                    user.getForeignLocationId(), selectedMonthDayYearInMillis,
                                    PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case ADD_EVENTS:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.ADD_EVENT);

                LocalDataServiceImpl.getInstance(mApp).addEventsList(
                        (ArrayList<Events>) (ArrayList<?>) response
                                .getDataList(), registeredDoctorProfileList);
                return volleyResponseBean;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    private void getListFromLocal(boolean initialLoading) {
        if (initialLoading) {
            showLoadingOverlay(true);
            resetListAndPagingAttributes();
        } else {
            showLoadingOverlay(false);
        }
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_EVENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    private void formHashMapAndRefresh(List<RegisteredDoctorProfile> responseList) {
        clinicDoctorProfileList = (ArrayList<RegisteredDoctorProfile>) responseList;
        if (responseList.size() > 1) {
            lvDoctorName.setVisibility(View.VISIBLE);
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
                tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, selectedMonthDayYearInMillis));
                break;
            case R.id.bt_previuos_date:
                long previousDateTimeInMillis = DateTimeUtil.getPreviousDate(selectedMonthDayYearInMillis);
                selectedMonthDayYearInMillis = previousDateTimeInMillis;
                tvSelectedDate.setText(DateTimeUtil.getFormattedDateTime(DATE_FORMAT_USED_IN_THIS_SCREEN, selectedMonthDayYearInMillis));
                break;
            case R.id.fl_bt_add_event:
                bookWalkInAppointment();
                break;
            case R.id.bt_refresh:
                resetListAndPagingAttributes();
                getEventsList(true);
                break;
            case R.id.tv_selected_date:
                openDatePickerDialog(tvSelectedDate);
                break;
            case R.id.bt_one_day:
                resetCaledarView(CalendarViewType.ONE_DAY);
                break;
            case R.id.bt_three_day:
                resetCaledarView(CalendarViewType.THREE_DAY);
                break;
            case R.id.fl_bt_today:
                selectedMonthDayYearInMillis = DateTimeUtil.getCurrentDateLong();
                tvSelectedDate.setText(DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN));
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
                    if (!isInitialLoading) {
                        if (DateTimeUtil.isCurrentMonthSelected(curentMonthDayYearInMillis, selectedMonthDayYearInMillis)) {
                            getListFromLocal(true);
                        } else {
                            curentMonthDayYearInMillis = selectedMonthDayYearInMillis;
                            resetListAndPagingAttributes();
                            getEventsList(true);
                        }
                    }
                } else {
                    tvSelectedDate.setText(R.string.today);
                }
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
        getListFromLocal(true);
    }

    @Override
    public void onEmptyListFound() {

    }


    public void bookWalkInAppointment() {
        AddEventDialogFragment addEventDialogFragment = new AddEventDialogFragment();
        Bundle bundle = new Bundle();
//        bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, calendarEvents.getAppointmentId());
        bundle.putParcelable(BookAppointmentDialogFragment.TAG_FROM_SCREEN_TYPE, Parcels.wrap(screenType.ordinal()));
        addEventDialogFragment.setArguments(bundle);
        addEventDialogFragment.setTargetFragment(addEventDialogFragment, PatientAppointmentDetailFragment.REQUEST_CODE_APPOINTMENTS_LIST);
        addEventDialogFragment.show(mActivity.getSupportFragmentManager(), addEventDialogFragment.getClass().getSimpleName());

    }

    private void openDatePickerDialog(final TextView textView) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(selectedMonthDayYearInMillis);
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


    private void resetCaledarView(CalendarViewType calendarViewType) {
        switch (calendarViewType) {
            case ONE_DAY:
                tvQueue.setSelected(false);
                tvOneDay.setSelected(true);
                tvThreeDay.setSelected(false);
//                horizontalScrollView.setVisibility(View.GONE);
//                layoutWeekView.setVisibility(View.VISIBLE);
//                calendarFragment.changeCalendarViewType(calendarViewType);
                break;
//
            case THREE_DAY:
                tvQueue.setSelected(false);
                tvOneDay.setSelected(false);
                tvThreeDay.setSelected(true);
//                horizontalScrollView.setVisibility(View.GONE);
//                layoutWeekView.setVisibility(View.VISIBLE);
//                calendarFragment.changeCalendarViewType(calendarViewType);
                break;
        }

    }


    private void loadMore(int current_page) {
        current_page--;
        PAGE_NUMBER = current_page;
        if (!isEndOfListAchieved)
            getListFromLocal(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for appointment list refresh
            IntentFilter filterListLocal = new IntentFilter();
            filterListLocal.addAction(INTENT_GET_EVENT_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(eventListReceiverLocal, filterListLocal);
            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(eventListReceiverLocal);
    }

    @Override
    public void onItemClicked(Object object) {
        Events events = (Events) object;
        EventDetailsPopupWindow eventDetailsPopupWindow = new EventDetailsPopupWindow(mActivity, null, events);
        eventDetailsPopupWindow.setOutsideTouchable(true);
        eventDetailsPopupWindow.setContentView(eventDetailsPopupWindow.getPopupView());
//        y = x + y;
        eventDetailsPopupWindow.showOptionsWindow(eventRecyclerView);

    }
}
