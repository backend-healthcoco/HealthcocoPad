package com.healthcoco.healthcocopad.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.QueueRecyclerViewAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.healthcoco.healthcocopad.enums.AppointmentStatusType.ALL;

/**
 * Created by Prashant on 01-03-2018.
 */


public class ScheduledQueueFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised {
    public static final int MAX_SIZE = 10;
    public static final int MAX_NUMBER_OF_EVENTS = 30;
    private List<CalendarEvents> calendarEventsList = new ArrayList<>();
    private RecyclerView scheduledQueueRecyclerView;
    private QueueRecyclerViewAdapter mAdapter;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private boolean isInitialLoading = true;

    private AppointmentStatusType appointmentStatusType = ALL;
    private User user;
    private long selectedMonthDayYearInMillis;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_scheduled_queue, container, false);
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
        scheduledQueueRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_scheduled_queue);
    }

    @Override
    public void initListeners() {
    }

    private void initAdapter() {
        mAdapter = new QueueRecyclerViewAdapter(mActivity, calendarEventsList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        scheduledQueueRecyclerView.setLayoutManager(layoutManager);
        scheduledQueueRecyclerView.setItemAnimator(new DefaultItemAnimator());
        scheduledQueueRecyclerView.setAdapter(mAdapter);
    }

    private void notifyAdapter(ArrayList<CalendarEvents> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            scheduledQueueRecyclerView.setVisibility(View.VISIBLE);
//            tvNoEventsFound.setVisibility(View.GONE);
            Collections.sort(responseList, ComparatorUtil.calendarEventsFromToTimeComparator);
        } else {
            scheduledQueueRecyclerView.setVisibility(View.GONE);
//            tvNoEventsFound.setVisibility(View.VISIBLE);
        }
        calendarEventsList.addAll(responseList);
//        progressLoading.setVisibility(View.GONE);
        mAdapter.notifyDataSetChanged();
    }

    private void getListFromLocal(boolean initialLoading) {
        if (isInitialLoading) {
//            swipeRefreshLayout.setRefreshing(false);
            showLoadingOverlay(true);
            resetListAndPagingAttributes();
        } else {
//            swipeRefreshLayout.setRefreshing(true);
            showLoadingOverlay(false);
        }
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_CALENDAR_EVENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void resetListAndPagingAttributes() {
        isInitialLoading = true;
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
//        scheduledQueueRecyclerView.resetPreLastPosition(0);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                    }
                    break;
                case GET_CALENDAR_EVENTS:
                    if (response.isDataFromLocal()) {
                        ArrayList<CalendarEvents> responseDataList = (ArrayList<CalendarEvents>) (ArrayList<?>) response.getDataList();
                        LogUtils.LOGD(TAG, "getSectionedDataListCalendar  loading onResponse");
                        if (Util.isNullOrEmptyList(responseDataList) || responseDataList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseDataList))
                            isEndOfListAchieved = true;
                        else isEndOfListAchieved = false;
                        if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            LogUtils.LOGD(TAG, "getSectionedDataListCalendar  loading Complete");
                            isInitialLoading = false;
                            notifyAdapter(responseDataList);
                            return;
                        }
                        notifyAdapter(responseDataList);
                        return;
                    } else {
                        getListFromLocal(false);
                    }
                    break;
                default:
                    break;
            }
        }

        isInitialLoading = false;
//        swipeRefreshLayout.setRefreshing(false);
//        progressLoading.setVisibility(View.GONE);

        showLoadingOverlay(false);

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
                }
                return volleyResponseBean;
            case GET_CALENDAR_EVENTS:
                long selectedMonthInMillis = DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(QueueFragment.DATE_FORMAT_FOR_THIS_SCREEN, DateTimeUtil.getFormattedDateTime(QueueFragment.DATE_FORMAT_FOR_THIS_SCREEN, selectedMonthDayYearInMillis));
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
