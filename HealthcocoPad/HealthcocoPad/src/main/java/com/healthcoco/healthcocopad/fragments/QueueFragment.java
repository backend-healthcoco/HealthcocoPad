package com.healthcoco.healthcocopad.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.ClinicDetailResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Prashant on 01-03-2018.
 */


public class QueueFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised {

    public static final String DATE_FORMAT_FOR_HEADER_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    public static final String DATE_FORMAT_SELECTED_FOR_THIS_SCREEN = "MMM yyyy";
    public static final String MONTH_FORMAT_FOR_THIS_SCREEN = "MMM yyyy";
    public static final String DATE_FORMAT_FOR_THIS_SCREEN = "dd MMM yyyy";

    public static final int MAX_SIZE = 10;
    public static final int MAX_NUMBER_OF_EVENTS = 30;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private User user;
    private long selectedMonthDayYearInMillis;

    private ProgressBar progressLoading;
    private ArrayList<CalendarEvents> calenderEventList = null;
    private AppointmentStatusType appointmentStatusType = AppointmentStatusType.ALL;


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
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void initViews() {
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
    }

    @Override
    public void initListeners() {
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
        progressLoading.setVisibility(View.GONE);
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
//                        onDateSet(null, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                        return;
                    }
                    break;
                case GET_CALENDAR_EVENTS:
                    if (response.isDataFromLocal()) {
                        calenderEventList = (ArrayList<CalendarEvents>) (ArrayList<?>) response.getDataList();
                        LogUtils.LOGD(TAG, "getSectionedDataListCalendar  loading onResponse");
                        if (Util.isNullOrEmptyList(calenderEventList) || calenderEventList.size() < MAX_SIZE || Util.isNullOrEmptyList(calenderEventList))
                            isEndOfListAchieved = true;
                        else isEndOfListAchieved = false;
//                        if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
//                            LogUtils.LOGD(TAG, "getSectionedDataListCalendar  loading Complete");
//                            isInitialLoading = false;
//                            refreshList(true, calenderEventList);
//                            return;
//                        }
//                        refreshList(false, calenderEventList);
                        return;
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_CALENDAR_EVENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    } else {
//                        getListFromLocal(false);
                    }
                    break;

                default:
                    break;
            }
        }

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
//                    try {
//                        loggedInUser = new User();
//                        ReflectionUtil.copy(loggedInUser, doctor.getUser());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        loggedInUser = doctor.getUser();
//                    }
//                    user = doctor.getUser();
//                    getSelectedDoctorClinic();
//                    clinicProfileList = doctorProfile.getClinicProfile();
//                    selectedClinicProfile = LocalDataServiceImpl.getInstance(mApp).getClinicResponseDetails(0, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId());
                }
                return volleyResponseBean;
//            case ADD_CLINIC_PROFILE:
//                LocalDataServiceImpl.getInstance(mApp).
//                        addClinicDetailResponse((ClinicDetailResponse) response.getData());
//            case GET_CLINIC_PROFILE:
//                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getClinicDetailsResponse(WebServiceType.GET_CLINIC_PROFILE, 0, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), null, null);
//                break;
            case ADD_CALENDAR_EVENTS:
                LocalDataServiceImpl.getInstance(mApp).addCalendarEventsList(
                        (ArrayList<CalendarEvents>) (ArrayList<?>) response
                                .getDataList());
            case GET_CALENDAR_EVENTS:
                long selectedMonthInMillis = DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_FOR_THIS_SCREEN, DateTimeUtil.getFormattedDateTime(DATE_FORMAT_FOR_THIS_SCREEN, selectedMonthDayYearInMillis));
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).
                        getCalendarEventsListResponsePageWise(WebServiceType.GET_CALENDAR_EVENTS, appointmentStatusType,
                                user.getUniqueId(), user.getForeignHospitalId(),
                                user.getForeignLocationId(), selectedMonthInMillis,
                                PAGE_NUMBER, MAX_SIZE, null, null);
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
}
