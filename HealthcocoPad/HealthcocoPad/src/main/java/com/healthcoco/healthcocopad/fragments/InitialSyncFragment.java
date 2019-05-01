package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.HomeActivity;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.DefaultSyncServiceType;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 25-01-2017.
 */
public class InitialSyncFragment extends HealthCocoFragment {
    public static final int MAX_SIZE = 50;
    public static final String INTENT_ACTION_INITIAL_SYNC = "com.healthcoco.INTENT_ACTION_INITIAL_SYNC";
    public static int PAGE_NUMBER = 0;
    public static boolean isPageLoading = false;
    public static long MAX_COUNT = 0;
    private boolean isFromLoginSignup;
    private TextView tvLoadingTitle;
    private ProgressBar progressLoading;
    private int progressUpdateValue;
    BroadcastReceiver updateProgressReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                int syncServiceTypeOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_SYNC_SERVICE_TYPE, -1);
                if (syncServiceTypeOrdinal != -1) {
                    DefaultSyncServiceType syncServiceType = DefaultSyncServiceType.values()[syncServiceTypeOrdinal];
                    LogUtils.LOGD(TAG, "Initial Sync service " + syncServiceType);
                    setProgress(syncServiceType);
                    if (syncServiceType == DefaultSyncServiceType.SYNC_COMPLETE) {
//                        ContactsListFragment tempContactsListFragment = (ContactsListFragment) mFragmentManager.findFragmentByTag(TemplatesListFragment.class.getSimpleName());
//                        if (tempContactsListFragment != null) {
//                            tempContactsListFragment.getHistoryListFromLocal(true, 0);
//                        } else
                        mActivity.finish();
                        Util.sendBroadcast(mApp, HomeActivity.INTENT_SYNC_SUCCESS);
                    }
                }
            }
        }
    };
    private int totalSyncServices;
    private LinearLayout parent;

    public InitialSyncFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_initial_sync_login_signup, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = mActivity.getIntent();
        totalSyncServices = intent.getIntExtra(HealthCocoConstants.TAG_TOTAL_SYNC_SERVICES, 0);
        isFromLoginSignup = intent.getBooleanExtra(HealthCocoConstants.TAG_IS_FROM_LOGIN_SIGNUP, false);
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            User user = doctor.getUser();
            init();
            mActivity.initDefaultData(user);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(INTENT_ACTION_INITIAL_SYNC);
        mActivity.registerReceiver(updateProgressReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mActivity.unregisterReceiver(updateProgressReceiver);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        setProgressUpdateValue(totalSyncServices);
    }

    @Override
    public void initViews() {
//        parent = (LinearLayout) view.findViewById(R.id.parent);
//        View viewToAdd;
//        if (isFromLoginSignup) {
//            viewToAdd = mActivity.getLayoutInflater().inflate(R.layout.fragment_initial_sync_login_signup, null);
//        } else {
//            viewToAdd = mActivity.getLayoutInflater().inflate(R.layout.fragment_initial_sync_translucent_background, null);
//        }
//        viewToAdd.setLayoutParams(new LinearLayout.LayoutParams(ScreenDimensions.SCREEN_WIDTH, ScreenDimensions.SCREEN_HEIGHT));
//        parent.addView(viewToAdd);

        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        tvLoadingTitle = (TextView) view.findViewById(R.id.tv_loading_title);
        LogUtils.LOGD(TAG, "isFromLoginSignup " + isFromLoginSignup);
    }

    @Override
    public void initListeners() {

    }

    public int setProgress(DefaultSyncServiceType syncServiceType) {
        int finalProgress = 0;
        try {
            switch (syncServiceType) {
                case GET_CONTACTS:
//                case GET_GROUPS:
                    int percentage = 0;
                    if (MAX_COUNT > 0) {
                        float receivedData = (PAGE_NUMBER * MAX_SIZE);
                        float amount = receivedData / MAX_COUNT;
                        percentage = (int) (amount * 100);
                        if (receivedData > (MAX_COUNT - MAX_SIZE))
                            percentage = 100;
                        LogUtils.LOGD(TAG, "RecievedData : " + receivedData + " Amount : " + amount + " Precentage : " + percentage);
                    }
                    tvLoadingTitle.setText(String.format(getResources().getString(syncServiceType.getLoadingTitle(), String.valueOf(percentage) + "%%")));
                    return finalProgress;
            }
            if (!isPageLoading) {
                finalProgress = progressLoading.getProgress() + progressUpdateValue;
                progressLoading.setProgress(finalProgress);
                try {
                    // if %s is present
                    tvLoadingTitle.setText(String.format(getResources().getString(syncServiceType.getLoadingTitle(), "")));
                } catch (Exception e) {
                    //otherwise will setText directly
                    tvLoadingTitle.setText(syncServiceType.getLoadingTitle());
                }
                LogUtils.LOGD(TAG, "Initial Sync finalProgress " + finalProgress);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalProgress;
    }

    public void setProgressUpdateValue(int totalSyncServices) {
        progressUpdateValue = Math.round(((float) progressLoading.getMax() / totalSyncServices));
        LogUtils.LOGD(TAG, "Progress Max Value " + progressUpdateValue);
    }

    public int getProgress() {
        if (progressLoading != null)
            return progressLoading.getProgress();
        return 0;
    }
}
