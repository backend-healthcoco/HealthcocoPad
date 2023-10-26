package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.google.gson.Gson;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.HealthcocoFCMListener;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.NotificationResponseDataAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.NotificationResponse;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.HistoryFilterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 29-04-2017.
 */

public class NotificationResponseDataFragment extends HealthCocoFragment implements
        CommonEMRItemClickListener, LocalDoInBackgroundListenerOptimised {
    public static final String INTENT_GET_NOTIFICATION_APPOINTMENT_LIST_LOCAL = "com.healthcoco.NOTIFICATION_APPOINTMENT_LIST_LOCAL";
    private ListView lvNotificationResponseData;
    private NotificationResponse notificationResponse;
    BroadcastReceiver appointmentListReceiverLocal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                getData();
                return;
            }
        }
    };
    private User user;
    private NotificationResponseDataAdapter adapter;
    private boolean receiversRegistered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_notification_response_data, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = mActivity.getIntent();
        String notificationResponseData = Parcels.unwrap(intent.getParcelableExtra(HealthcocoFCMListener.TAG_NOTIFICATION_RESPONSE));
        if (!Util.isNullOrBlank(notificationResponseData))
            notificationResponse = new Gson().fromJson(notificationResponseData, NotificationResponse.class);
        if (notificationResponse != null) init();
    }

    @Override
    public void init() {
        ((CommonOpenUpActivity) mActivity).initActionbarTitle(notificationResponse.getNotificationType().getTitleId());
        initViews();
        initListeners();
        initAdapter();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        lvNotificationResponseData = (ListView) view.findViewById(R.id.lv_notification_response_data);
    }

    @Override
    public void initListeners() {

    }

    private void initAdapter() {
        adapter = new NotificationResponseDataAdapter(mActivity, this);
        lvNotificationResponseData.setAdapter(adapter);
    }

    private void notifyAdapter(Object object) {
        ArrayList<NotificationResponse> list = new ArrayList<>();
        if (object != null) {
            notificationResponse.setData(object);
            list.add(notificationResponse);
        }
        if (!Util.isNullOrEmptyList(list)) {
            adapter.setListData(list);
            adapter.notifyDataSetChanged();
        } else {
            mActivity.finish();
        }
    }

    @Override
    public void showLoading(boolean showLoading) {
        if (showLoading)
            mActivity.showLoading(false);
        else
            mActivity.hideLoading();
    }

    @Override
    public RegisteredPatientDetailsUpdated getSelectedPatient() {
        return null;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String getLoginedUser() {
        return null;
    }

    @Override
    public void openEmrScreen(HistoryFilterType historyFilterType) {

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
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        getData();
                        return;
                    }
                    break;
                case GET_RECORD_BY_RECORD_ID:
                    if (response.getData() != null && response.getData() instanceof Records) {
                        Records record = (Records) response.getData();
                        notificationResponse.setData(record);
                    }
                    notifyAdapter(response.getData());
                    break;
                case GET_APPOINTMENT_BY_APPOINTMENT_ID:
                    if (response.getData() != null && response.getData() instanceof CalendarEvents) {
                        CalendarEvents record = (CalendarEvents) response.getData();
                        notificationResponse.setData(record);
                    }
                    notifyAdapter(response.getData());
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void getData() {
        try {
            switch (notificationResponse.getNotificationType()) {
                case REPORTS:
                    //break n finish the activity if id is null
                    if (Util.isNullOrBlank(notificationResponse.getRi()))
                        break;
                    WebDataServiceImpl.getInstance(mApp).getNotificationResponseDataDetail(notificationResponse, this, this);
                    return;
                case APPOINTMENT:
                    //break n finish the activity if id is null
                    if (Util.isNullOrBlank(notificationResponse.getAi()))
                        break;
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).getNotificationResponseDataDetail(notificationResponse, this, this);
                    return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        mActivity.finish();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null) {
                    user = doctor.getUser();
                }
                return volleyResponseBean;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Util.sendBroadcast(mApp, ContactsListFragment.INTENT_GET_CONTACT_LIST_LOCAL);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(appointmentListReceiverLocal);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for appointment list refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_NOTIFICATION_APPOINTMENT_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(appointmentListReceiverLocal, filter);
            receiversRegistered = true;
        }
    }
}