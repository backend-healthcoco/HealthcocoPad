package com.healthcoco.healthcocopad.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ReportsListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.NextReviewOnDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.UploadReportDialogFragment;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.DialogType;
import com.healthcoco.healthcocopad.enums.HistoryFilterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.OptionsType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class PatientReportsDetailFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised,
        View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, CommonEMRItemClickListener, CommonOptionsDialogItemClickListener {
    public static final String INTENT_GET_REPORTS_LIST = "com.healthcoco.GET_REPORTS_LIST";
    public static final String INTENT_GET_REPORTS_LIST_LOCAL = "com.healthcoco.GET_REPORTS_LIST_LOCAL";
    public static final String INTENT_GET_REPORTS_LIST_USING_ID = "com.healthcoco.GET_REPORTS_LIST_USING_ID";

    private static final String[] ACCEPT_MIME_TYPES = {
            "image/*", "application/pdf", "text/*",
            "application/rtf", "application/msword", "application/vnd.ms-powerpoint"
    };
    private static final int REQUEST_CODE_REPORTS_LIST = 188;
    private static final int REQUEST_CODE_UPLOAD_REPORT = 124;

    private ListViewLoadMore lvReports;
    private TextView tvNoReportsFound;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private ArrayList<Records> recordsList;
    private Uri reportImageUri;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isOTPVerified = false;
    private ReportsListAdapter adapter;
    private ProgressBar progressLoading;
    private boolean receiversRegistered = false;
    private boolean isInitialLoading;
    private FloatingActionButton floatingActionButton;
    private PatientDetailTabType detailTabType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_reports_deatil, container, false);
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
    }

    public void getListFromLocal(boolean showLoading, boolean isOTPVerified, User user) {
        if (user != null) {
            this.user = user;
            isInitialLoading = showLoading;
            this.isOTPVerified = isOTPVerified;
            if (showLoading) {
                showLoadingOverlay(true);
            }
            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_RECORDS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void getRecords(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.RECORDS);
        WebDataServiceImpl.getInstance(mApp).getEmrListGeneralMethod(Records.class, WebServiceType.GET_REPORTS_UPDATED, isOTPVerified, true, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                HealthCocoConstants.SELECTED_PATIENTS_USER_ID, latestUpdatedTime, this, this);
    }

    @Override
    public void initViews() {
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_bt_add_report);
        lvReports = (ListViewLoadMore) view.findViewById(R.id.lv_reports);
        tvNoReportsFound = (TextView) view.findViewById(R.id.tv_no_reports_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void initListeners() {
        floatingActionButton.setOnClickListener(this);
        lvReports.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initAdapter() {
        adapter = new ReportsListAdapter(mActivity, this);
        lvReports.setAdapter(adapter);
    }

    private void notifyAdapter(ArrayList<Records> recordsList) {
        if (!Util.isNullOrEmptyList(recordsList)) {
            Collections.sort(recordsList, ComparatorUtil.reportsDateComparator);
            lvReports.setVisibility(View.VISIBLE);
            tvNoReportsFound.setVisibility(View.GONE);
        } else {
            lvReports.setVisibility(View.GONE);
            tvNoReportsFound.setVisibility(View.VISIBLE);
        }
        adapter.setListData(recordsList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_bt_add_report:
                openDialogFragment(new UploadReportDialogFragment(), REQUEST_CODE_UPLOAD_REPORT);
//                openDialogFragment(DialogType.SELECT_IMAGE, this);

//                try {
//                    String action = "";
//                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT)
//                        action = Intent.ACTION_GET_CONTENT;
//                    else
//                        action = Intent.ACTION_OPEN_DOCUMENT;
//                    Intent intent = new Intent(action);
//                    intent.addCategory(Intent.CATEGORY_OPENABLE);
//                    intent.setType("*/*");
//                    intent.putExtra(Intent.EXTRA_MIME_TYPES, ACCEPT_MIME_TYPES);
//                    startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_FILE);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onOptionsItemSelected(Object object) {
        OptionsType optionsType = (OptionsType) object;
        if (optionsType != null) {
            LogUtils.LOGD(TAG, getResources().getString(optionsType.getStringId()));
            switch (optionsType) {
                case CAMERA:
                    reportImageUri = mActivity.openCamera(this, "reportImage");
                    break;
                case GALLERY:
                    mActivity.openGallery(this);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == HealthCocoConstants.REQUEST_CODE_CAMERA && reportImageUri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), reportImageUri);
                    if (bitmap != null) {
//                        openAddRecordDetailActivity(CommonOpenUpFragmentType.ADD_RECORD_DETAIL, reportImageUri, requestCode);
                    }
                } else if (requestCode == HealthCocoConstants.REQUEST_CODE_GALLERY && data.getData() != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), data.getData());
                    if (bitmap != null) {
//                        openAddRecordDetailActivity(CommonOpenUpFragmentType.ADD_RECORD_DETAIL, data.getData(), requestCode);
                    }
                } else if (requestCode == HealthCocoConstants.REQUEST_CODE_FILE && data.getData() != null) {
//                    openAddRecordDetailActivity(CommonOpenUpFragmentType.ADD_RECORD_DETAIL, data.getData(), requestCode);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_REPORTS_LIST) {
//            if (resultCode == HealthCocoConstants.RESULT_CODE_REPORT_DETAIL) {
//                lvReports.smoothScrollToPosition(0);
//                getRecords(true);
//            }
        }
    }

    private void openAddRecordDetailActivity(CommonOpenUpFragmentType fragmentType, Uri intentData, int requestCode) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
//        intent.putExtra(HealthCocoConstants.TAG_IMAGE_URI, intentData);
//        intent.putExtra(HealthCocoConstants.TAG_REQUEST_CODE, requestCode);
        startActivityForResult(intent, REQUEST_CODE_REPORTS_LIST);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        progressLoading.setVisibility(View.GONE);
        showLoadingOverlay(false);
        Util.showToast(mActivity, errorMsg);
        swipeRefreshLayout.setRefreshing(false);
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
        switch (response.getWebServiceType()) {
            case GET_REPORTS_UPDATED:
                if (response.isDataFromLocal()) {
                    recordsList = (ArrayList<Records>) (ArrayList<?>) response
                            .getDataList();
                    if (!Util.isNullOrEmptyList(recordsList))
                        LogUtils.LOGD(TAG, "Success onResponse recordsList Size " + recordsList.size() + " isDataFromLocal " + response.isDataFromLocal());
                    notifyAdapter(recordsList);
                    if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getRecords(true);
                        return;
                    }
                } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_RECORDS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    response.setIsFromLocalAfterApiSuccess(true);
                    return;
                }
                showLoadingOverlay(false);
                progressLoading.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_RECORDS:
                LocalDataServiceImpl.getInstance(mApp).addRecordsList((ArrayList<Records>) (ArrayList<?>) response.getDataList());
            case GET_RECORDS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getRecordsList(WebServiceType.GET_REPORTS_UPDATED, isOTPVerified, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, null, null);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
    }

    @Override
    public void showLoading(boolean showLoading) {
        showLoadingOverlay(showLoading);
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
    public void openEmrScreen(HistoryFilterType historyFilterType) {

    }

    @Override
    public void onRefresh() {
        getRecords(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for reports list refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_REPORTS_LIST);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(reportsListReceiver, filter);

            //receiver for reports list refresh from local
            IntentFilter filter1 = new IntentFilter();
            filter1.addAction(INTENT_GET_REPORTS_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(reportsListReceiverLocal, filter1);

            IntentFilter filter2 = new IntentFilter();
            filter2.addAction(INTENT_GET_REPORTS_LIST_USING_ID);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(reportsListUsingIdReceiver, filter2);

            receiversRegistered = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(reportsListReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(reportsListReceiverLocal);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(reportsListUsingIdReceiver);
    }

    BroadcastReceiver reportsListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getRecords(true);
        }
    };
    BroadcastReceiver reportsListReceiverLocal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            boolean showLoading = false;
            if (intent != null && intent.hasExtra(SHOW_LOADING)) {
                showLoading = intent.getBooleanExtra(SHOW_LOADING, false);
            }
            getListFromLocal(showLoading, isOtpVerified(), user);
            sendBroadcasts();
        }
    };
    BroadcastReceiver reportsListUsingIdReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(UploadReportDialogFragment.TAG_REPORT_ID)) {
                String reportId = intent.getStringExtra(UploadReportDialogFragment.TAG_REPORT_ID);
                Records records = LocalDataServiceImpl.getInstance(mApp).getRecord(reportId, HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                if (records != null) recordsList.add(records);
                notifyAdapter(recordsList);
            }
            sendBroadcasts();
        }
    };

    private void sendBroadcasts() {
        Util.sendBroadcasts(mApp, new ArrayList<String>() {{
//            add(HistoryFragment.INTENT_GET_HISTORY_LIST);
            add(PatientVisitDetailFragment.INTENT_GET_VISITS_LIST_FROM_LOCAL);
        }});
    }

//    @Override
//    public void onDialogItemClicked(CommonListDialogType commonListDialogType, Object object) {
//        switch (commonListDialogType) {
//            case FILE_TYPES:
//                if (object instanceof FileTypes) {
//                    FileTypes fileType = (FileTypes) object;
//                    Intent mediaIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                    mediaIntent.setType(fileType.getMimeType());
//                    startActivityForResult(mediaIntent, HealthCocoConstants.REQUEST_CODE_FILE);
//                }
//                break;
//        }
//    }

    public void setUser(User user) {
        this.user = user;
    }

    public void refreshData(PatientDetailTabType detailTabType) {
        getListFromLocal(true, isOtpVerified(), user);
        this.detailTabType = detailTabType;
    }

    public void setUserData(User user, RegisteredPatientDetailsUpdated selectedPatient) {
        this.user = user;
        this.selectedPatient = selectedPatient;
    }
}
