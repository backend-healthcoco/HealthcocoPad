package com.healthcoco.healthcocoplus.fragments;

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

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.activities.HomeActivity;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.enums.DefaultSyncServiceType;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

/**
 * Created by Shreshtha on 25-01-2017.
 */
public class InitialSyncFragment extends HealthCocoFragment {
    private boolean isFromLoginSignup;
    private TextView tvLoadingTitle;
    private ProgressBar progressLoading;
    private int progressUpdateValue;
    public static final String INTENT_ACTION_INITIAL_SYNC = "com.healthcoco.INTENT_ACTION_INITIAL_SYNC";
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

    public int setProgress(int title) {
        int finalProgress = 0;
        try {
            finalProgress = progressLoading.getProgress() + progressUpdateValue;
            progressLoading.setProgress(finalProgress);
            tvLoadingTitle.setText(title);
            LogUtils.LOGD(TAG, "Initial Sync finalProgress " + finalProgress);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return finalProgress;
    }

    public void setProgressUpdateValue(int totalSyncServices) {
        progressUpdateValue = progressLoading.getMax() / totalSyncServices;
        LogUtils.LOGD(TAG, "Progress Max Value " + progressUpdateValue);
    }

    public int getProgress() {
        if (progressLoading != null)
            return progressLoading.getProgress();
        return 0;
    }

    BroadcastReceiver updateProgressReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                int syncServiceTypeOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_SYNC_SERVICE_TYPE, -1);
                if (syncServiceTypeOrdinal != -1) {
                    DefaultSyncServiceType syncServiceType = DefaultSyncServiceType.values()[syncServiceTypeOrdinal];
                    LogUtils.LOGD(TAG, "Initial Sync service " + syncServiceType);
                    setProgress(syncServiceType.getLoadingTitle());
                    if (syncServiceType == DefaultSyncServiceType.SYNC_COMPLETE) {
//                        ContactsListFragment tempContactsListFragment = (ContactsListFragment) mFragmentManager.findFragmentByTag(TemplatesListFragment.class.getSimpleName());
//                        if (tempContactsListFragment != null) {
//                            tempContactsListFragment.getListFromLocal(true, 0);
//                        } else
                        mActivity.finish();
                        Util.sendBroadcast(mApp, HomeActivity.INTENT_SYNC_SUCCESS);
                    }
                }
            }
        }
    };
}
