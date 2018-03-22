package com.healthcoco.healthcocopad.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.AppointmentStatusType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.QueueListitemlistener;
import com.healthcoco.healthcocopad.recyclerview.EndlessRecyclerViewScrollListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Collections;

import static com.healthcoco.healthcocopad.enums.AppointmentStatusType.ALL;
import static com.healthcoco.healthcocopad.enums.CalendarStatus.ENGAGED;
import static com.healthcoco.healthcocopad.enums.CalendarStatus.SCHEDULED;
import static com.healthcoco.healthcocopad.enums.CalendarStatus.WAITING;
import static com.healthcoco.healthcocopad.fragments.EngagedQueueFragment.INTENT_REFRESH_ENGAGED_QUEUE_DATA;

/**
 * Created by Prashant on 01-03-2018.
 */


public class WaitingQueueFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised, SwipeRefreshLayout.OnRefreshListener, QueueListitemlistener {
    public static final String INTENT_REFRESH_WAITING_QUEUE_DATA = "com.healthcoco.REFRESH_WAITING_QUEUE_DATA";

    public static final int MAX_SIZE = 10;
    public static final int MAX_NUMBER_OF_EVENTS = 30;
    public ArrayList<ClinicDoctorProfile> clinicDoctorProfileList = null;
    CalendarEvents calendarEvents;
    private ArrayList<CalendarEvents> calendarEventsList = new ArrayList<>();
    private RecyclerView scheduledQueueRecyclerView;
    private TextView tvNoEventsFound;
    private TextView tvTitle;
    private TextView tvCount;

    private SwipeRefreshLayout swipeRefreshLayout;
    private HealthcocoRecyclerViewAdapter mAdapter;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private boolean isInitialLoading = true;
    BroadcastReceiver refreshQueueDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getListFromLocal(false);
        }
    };
    private ProgressBar progressLoading;
    private AppointmentStatusType appointmentStatusType = ALL;
    private User user;
    private long selectedMonthDayYearInMillis;
    private boolean receiversRegistered;

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
        tvNoEventsFound = (TextView) view.findViewById(R.id.tv_no_events_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvCount = (TextView) view.findViewById(R.id.tv_count);

        tvTitle.setText(R.string.reschedule);

    }

    @Override
    public void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);

    }

    private void initAdapter() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        scheduledQueueRecyclerView.setLayoutManager(layoutManager);
        scheduledQueueRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.APOINTMENT_QUEUE, this);
        mAdapter.setListData((ArrayList<Object>) (Object) calendarEventsList);
        scheduledQueueRecyclerView.setAdapter(mAdapter);

        scheduledQueueRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMore(current_page);
            }
        });
    }

    private void loadMore(int current_page) {
        current_page--;
        PAGE_NUMBER = current_page;
        if (!isEndOfListAchieved)
            getListFromLocal(false);
    }

    private void notifyAdapter(ArrayList<CalendarEvents> responseList) {
        swipeRefreshLayout.setRefreshing(false);
        if (!Util.isNullOrEmptyList(responseList)) {
            scheduledQueueRecyclerView.setVisibility(View.VISIBLE);
            tvNoEventsFound.setVisibility(View.GONE);
            Collections.sort(responseList, ComparatorUtil.calendarEventsFromToTimeComparator);

            calendarEventsList.addAll(responseList);
            progressLoading.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        } else {
            scheduledQueueRecyclerView.setVisibility(View.GONE);
            tvNoEventsFound.setVisibility(View.VISIBLE);
        }
    }

    private void getListFromLocal(boolean initialLoading) {
        if (initialLoading) {
            swipeRefreshLayout.setRefreshing(false);
            showLoadingOverlay(true);
            resetListAndPagingAttributes();
        } else {
            swipeRefreshLayout.setRefreshing(true);
            showLoadingOverlay(false);
        }
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_CALENDAR_EVENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void resetListAndPagingAttributes() {
        isInitialLoading = true;
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
        scheduledQueueRecyclerView.invalidate();
        calendarEventsList.clear();
        //        mAdapter.notifyItemRangeChanged(0, mAdapter.getItemCount());
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case GET_CALENDAR_EVENTS:
                    if (response.isDataFromLocal()) {
                        ArrayList<CalendarEvents> responseDataList = (ArrayList<CalendarEvents>) (ArrayList<?>) response.getDataList();
                        LogUtils.LOGD(TAG, "getSectionedDataListCalendar  loading onResponse");
                        if (Util.isNullOrEmptyList(responseDataList) || responseDataList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseDataList))
                            isEndOfListAchieved = true;
                        else isEndOfListAchieved = false;
                        if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            LogUtils.LOGD(TAG, "getSectionedDataListCalendar  loading Complete");
                            progressLoading.setVisibility(View.GONE);
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
                case CHANGE_APPOINTMENT_STATUS:
                    mActivity.hideLoading();
                    if (response.getData() != null) {
                        CalendarEvents responseData = (CalendarEvents) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addCalendarEventsUpdated(responseData);
                        calendarEventsList.remove(calendarEvents);
//                        mAdapter.notifyDataSetChanged();
                        notifyAdapter(calendarEventsList);
                        Util.sendBroadcast(mApp, INTENT_REFRESH_ENGAGED_QUEUE_DATA);
                    }
                    break;
                default:
                    break;
            }
        }

        isInitialLoading = false;
        swipeRefreshLayout.setRefreshing(false);

        showLoadingOverlay(false);

    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null) {
                    user = doctor.getUser();
                }
                return volleyResponseBean;
            case GET_CALENDAR_EVENTS:
                long selectedMonthInMillis = DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(QueueFragment.DATE_FORMAT_FOR_THIS_SCREEN, DateTimeUtil.getFormattedDateTime(QueueFragment.DATE_FORMAT_FOR_THIS_SCREEN, selectedMonthDayYearInMillis));
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).
                        getCalendarEventsListResponsePageWise(WebServiceType.GET_CALENDAR_EVENTS, appointmentStatusType,
                                clinicDoctorProfileList, user.getForeignHospitalId(),
                                user.getForeignLocationId(), WAITING.getValue(), selectedMonthInMillis,
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

    public void reFreshQueue(long selectedMonthDayYearInMillis, ArrayList<ClinicDoctorProfile> clinicDoctorProfileList) {
        this.selectedMonthDayYearInMillis = selectedMonthDayYearInMillis;
        this.clinicDoctorProfileList = clinicDoctorProfileList;
        getListFromLocal(true);
    }

    @Override
    public void onRefresh() {
        calendarEventsList.clear();
        getListFromLocal(true);
    }

    @Override
    public void onCheckInClicked(Object object) {
        calendarEvents = (CalendarEvents) object;
        if (calendarEvents != null) {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).changeAppointmentStatus(CalendarEvents.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), calendarEvents.getPatientId(), calendarEvents.getAppointmentId(), ENGAGED.getValue(), this, this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for prescription list refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_REFRESH_WAITING_QUEUE_DATA);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshQueueDetailsReceiver, filter);
            receiversRegistered = true;


        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
//        sendBroadcasts(null);
        mActivity.hideLoading();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshQueueDetailsReceiver);
    }
}
