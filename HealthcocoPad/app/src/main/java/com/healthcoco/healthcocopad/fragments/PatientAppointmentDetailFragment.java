package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.adapter.AppointmentsListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.BookAppointmentDialogFragment;
import com.healthcoco.healthcocopad.enums.BookAppointmentFromScreenType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class PatientAppointmentDetailFragment extends HealthCocoFragment implements SwipeRefreshLayout.OnRefreshListener, LocalDoInBackgroundListenerOptimised,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, LoadMorePageListener {
    public static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "dd MMM yyyy";
    public static final String INTENT_GET_APPOINTMENT_LIST_LOCAL = "com.healthcoco.APPOINTMENT_LIST_LOCAL";

    //variables need for pagination
    public static final int MAX_SIZE = 10;
    public static final int REQUEST_CODE_APPOINTMENTS_LIST = 111;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private boolean isInitialLoading;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ListViewLoadMore lvAppointment;
    private TextView tvNoAppointmentsFound;
    private ProgressBar progressLoading;
    private AppointmentsListAdapter adapter;
    private boolean isOTPVerified = false;
    private HashMap<String, CalendarEvents> appointmentsListHashMap = new HashMap<String, CalendarEvents>();
    private boolean receiversRegistered;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_appointment_deatil, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for appointment list refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_APPOINTMENT_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(appointmentListReceiverLocal, filter);
            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(appointmentListReceiverLocal);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
    }

    @Override
    public void initViews() {
        lvAppointment = (ListViewLoadMore) view.findViewById(R.id.lv_appointment);
        tvNoAppointmentsFound = (TextView) view.findViewById(R.id.tv_no_appointments_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void initListeners() {
        lvAppointment.setSwipeRefreshLayout(swipeRefreshLayout);
        lvAppointment.setLoadMoreListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initAdapter() {
        adapter = new AppointmentsListAdapter(mActivity);
        lvAppointment.setAdapter(adapter);
    }

    private void notifyAdapter(ArrayList<CalendarEvents> list) {
        if (!Util.isNullOrEmptyList(list)) {
            Collections.sort(list, ComparatorUtil.appointmentFromDateComparator);
            lvAppointment.setVisibility(View.VISIBLE);
            tvNoAppointmentsFound.setVisibility(View.GONE);
        } else {
            lvAppointment.setVisibility(View.GONE);
            tvNoAppointmentsFound.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData(CalendarEvents.getSectionedDataAppointment(list));
        adapter.notifyDataSetChanged();
    }

    public void getListFromLocal(User user) {
        this.user = user;
        showLoadingOverlay(true);
        getListFromLocal(true);
    }

    private void getListFromLocal(boolean initialLoading) {
        this.isInitialLoading = initialLoading;
        showLoadingOverlay(isInitialLoading);
        if (isInitialLoading) {
            if (isInitialLoading)
                mActivity.showLoading(false);
            progressLoading.setVisibility(View.GONE);
        } else
            progressLoading.setVisibility(View.VISIBLE);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_APPOINTMENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void resetListAndPagingAttributes() {
        isInitialLoading = true;
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
        lvAppointment.resetPreLastPosition(0);
    }

    public void getAppointmentsList(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.APPOINTMENT);
        WebDataServiceImpl.getInstance(mApp).getEmrListGeneralMethod(CalendarEvents.class, WebServiceType.GET_APPOINTMENT, isOTPVerified, true, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                selectedPatient.getUserId(), latestUpdatedTime, this, this);
    }

    public void openAddNewAppointmentScreen() {
        openDialogFragment(new BookAppointmentDialogFragment(), BookAppointmentDialogFragment.TAG_FROM_SCREEN_TYPE,
                BookAppointmentFromScreenType.APPOINTMENTS_LIST_ADD_NEW.ordinal(), REQUEST_CODE_APPOINTMENTS_LIST);
    }

    @Override
    public void onRefresh() {
        getAppointmentsList(false);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        swipeRefreshLayout.setRefreshing(false);
        progressLoading.setVisibility(View.GONE);
        showLoadingOverlay(false);
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        progressLoading.setVisibility(View.GONE);
        showLoadingOverlay(false);
        mActivity.hideLoading();
        swipeRefreshLayout.setRefreshing(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case GET_APPOINTMENT:
                    if (response.isDataFromLocal()) {
                        ArrayList<CalendarEvents> responseList = (ArrayList<CalendarEvents>) (ArrayList<?>) response
                                .getDataList();
                        formHashMapAndRefresh(responseList);
                        if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                            isEndOfListAchieved = true;
                        else isEndOfListAchieved = false;
                        if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            getAppointmentsList(true);
                            return;
                        }
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        ArrayList<CalendarEvents> responseList = (ArrayList<CalendarEvents>) (ArrayList<?>) response
                                .getDataList();
                        if (responseList.size() <= 10)
                            formHashMapAndRefresh(responseList);
                        response.setIsFromLocalAfterApiSuccess(true);
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_APPOINTMENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    swipeRefreshLayout.setRefreshing(false);
                    progressLoading.setVisibility(View.GONE);
                    showLoadingOverlay(false);
                    mActivity.hideLoading();
                    isInitialLoading = false;
                    break;
                default:
                    break;
            }
        }
        showLoadingOverlay(false);
        mActivity.hideLoading();
        swipeRefreshLayout.setRefreshing(false);
    }

    private void formHashMapAndRefresh(ArrayList<CalendarEvents> responseList) {
        if (!Util.isNullOrEmptyList(responseList))
            for (CalendarEvents appointment :
                    responseList) {
                appointmentsListHashMap.put(appointment.getUniqueId(), appointment);
            }
        notifyAdapter(new ArrayList<CalendarEvents>(appointmentsListHashMap.values()));
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_APPOINTMENTS:
                LocalDataServiceImpl.getInstance(mApp).addAppointmentsList(
                        (ArrayList<CalendarEvents>) (ArrayList<?>) response
                                .getDataList());
            case GET_APPOINTMENTS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getAppointmentsListResponsePageWise(WebServiceType.GET_APPOINTMENT, isOTPVerified,
                        user.getUniqueId(), user.getForeignHospitalId(), user.getForeignLocationId(),
                        HealthCocoConstants.SELECTED_PATIENTS_USER_ID, PAGE_NUMBER, MAX_SIZE, null, null);
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
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading) {
            PAGE_NUMBER = PAGE_NUMBER + 1;
            getListFromLocal(false);
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }

    BroadcastReceiver appointmentListReceiverLocal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            boolean showLoading = false;
            String appointmentId = "";
            if (intent != null) {
                if (intent.hasExtra(SHOW_LOADING))
                    showLoading = intent.getBooleanExtra(SHOW_LOADING, false);
                if (!Util.isNullOrBlank(appointmentId)) {
                    CalendarEvents appointment = LocalDataServiceImpl.getInstance(mApp).getAppointment(appointmentId);
                    if (appointment != null) {
                        appointmentsListHashMap.put(appointment.getUniqueId(), appointment);
                        notifyAdapter(new ArrayList<CalendarEvents>(appointmentsListHashMap.values()));
                        return;
                    }
                }
            }
            getListFromLocal(showLoading);
        }
    };

    public void refreshData(User user, RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        this.selectedPatient = registeredPatientDetailsUpdated;
        this.user = user;
        getListFromLocal(true);
    }
}
