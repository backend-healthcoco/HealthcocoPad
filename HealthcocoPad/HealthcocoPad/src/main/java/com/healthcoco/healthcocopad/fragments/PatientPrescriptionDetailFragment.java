package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.PrescriptionListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.ClinicDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.HistoryFilterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
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
import java.util.List;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class PatientPrescriptionDetailFragment extends HealthCocoFragment implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, LocalDoInBackgroundListenerOptimised, CommonEMRItemClickListener, LoadMorePageListener {

    public static final String INTENT_GET_PRESCRIPTION_LIST = "com.healthcoco.PRESCRIPTION_LIST";
    public static final String INTENT_GET_PRESCRIPTION_LIST_LOCAL = "com.healthcoco.PRESCRIPTION_LIST_LOCAL";
    public static final String INTENT_GET_PRESCRIPTION_USING_PRESCRIPTION_ID = "com.healthcoco.PRESCRIPTION_USING_PRESCRIPTION_ID";

    //variables need for pagination
    public static final int MAX_SIZE = 10;
    private static final int REQUEST_CODE_PRESCRIPTION_LIST = 121;
    public FloatingActionButton floatingActionButton;
    BroadcastReceiver prescriptionUsingPrescriptionIdReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
//            if (intent != null && intent.hasExtra(AddNewPrescriptionFragment.TAG_PRESCRIPTION_ID)) {
//                String prescriptionId = intent.getStringExtra(AddNewPrescriptionFragment.TAG_PRESCRIPTION_ID);
//                Prescription prescripton = LocalDataServiceImpl.getInstance(mApp).getPrescription(prescriptionId);
//                prescriptionsList.put(prescripton.getUniqueId(), prescripton);
//                notifyAdapter(new ArrayList<>(prescriptionsList.values()));
//            }
            sendBroadcasts();
        }
    };
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private int currentPageNumber;
    private boolean isInitialLoading;
    private HashMap<String, Prescription> prescriptionsList = new HashMap<>();
    private TextView tvNoPrescriptionFound;
    private ListViewLoadMore lvPrescription;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private ProgressBar progressLoading;
    private boolean isOTPVerified = false;
    BroadcastReceiver prescriptionListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            resetListAndPagingAttributes();
            getPrescription(true);
        }
    };
    private PrescriptionListAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean receiversRegistered = false;
    private PatientDetailTabType detailTabType;
    private boolean isLoading;
    BroadcastReceiver prescriptionListReceiverLocal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            boolean showLoading = false;
            if (intent != null && intent.hasExtra(SHOW_LOADING)) {
                showLoading = intent.getBooleanExtra(SHOW_LOADING, false);
            }
            resetListAndPagingAttributes();
            getListFromLocal(showLoading, isOtpVerified(), PAGE_NUMBER);
            sendBroadcasts();
        }
    };
    private String loginedUser;
    private ArrayList<ClinicDoctorProfile> clinicDoctorProfileList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_prescription_deatil, container, false);
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

    private void initAdapter() {
        adapter = new PrescriptionListAdapter(mActivity, this);
        lvPrescription.setAdapter(adapter);
    }

    public void getListFromLocal(boolean initialLoading, boolean isOTPVerified, int PAGE_NUMBER) {
        this.isInitialLoading = initialLoading;
        this.currentPageNumber = PAGE_NUMBER;
        this.isOTPVerified = isOTPVerified;
        isLoading = true;
        if (initialLoading) {
            showLoadingOverlay(true);
            progressLoading.setVisibility(View.GONE);
        } else {
            progressLoading.setVisibility(View.VISIBLE);
        }
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PRESCRIPTION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getPrescription(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.PRESCRIPTION);
        WebDataServiceImpl.getInstance(mApp).getEmrListGeneralMethod(Prescription.class, WebServiceType.GET_PRESCRIPTION,
                isOTPVerified, true, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                selectedPatient.getUserId(), latestUpdatedTime, this, this);
    }

    @Override
    public void initViews() {
        lvPrescription = (ListViewLoadMore) view.findViewById(R.id.lv_prescription);
        tvNoPrescriptionFound = (TextView) view.findViewById(R.id.tv_no_prescriptions_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_bt_add_rx);
    }

    @Override
    public void initListeners() {
        lvPrescription.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_options:
                break;
            case R.id.fl_bt_add_rx:
                openAddNewPrescriptionScreen();
                break;
            default:
                break;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PRESCRIPTION_LIST) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_PRESCIPTION) {
                lvPrescription.smoothScrollToPosition(0);
                getPrescription(true);
                Util.showToast(mActivity, "Prescription Saved");
            }
        }
    }

    private void notifyAdapter(List<Prescription> prescriptionsList) {
        if (!Util.isNullOrEmptyList(prescriptionsList)) {
            Collections.sort(prescriptionsList, ComparatorUtil.prescriptionDateComparator);
            lvPrescription.setVisibility(View.VISIBLE);
            tvNoPrescriptionFound.setVisibility(View.GONE);
        } else {
            lvPrescription.setVisibility(View.GONE);
            tvNoPrescriptionFound.setVisibility(View.VISIBLE);
        }
        adapter.setListData(prescriptionsList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showLoading(boolean showLoading) {
        showLoadingOverlay(showLoading);
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
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        progressLoading.setVisibility(View.GONE);
        showLoadingOverlay(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case GET_PRESCRIPTION:
                    if (response.isDataFromLocal()) {
                        ArrayList<Prescription> responseList = (ArrayList<Prescription>) (ArrayList<?>) response
                                .getDataList();
                        prescriptionsList.clear();
                        formHashMapAndRefresh(responseList);
                        if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                            isEndOfListAchieved = true;
                        else isEndOfListAchieved = false;
                        if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            getPrescription(true);
                            return;
                        }
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        response.setIsFromLocalAfterApiSuccess(true);
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PRESCRIPTION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        if (!Util.isNullOrEmptyList(prescriptionsList))
                            LogUtils.LOGD(TAG, "Success onResponse prescriptionsList Size Total" + prescriptionsList.size() + " isDataFromLocal " + response.isDataFromLocal());
                        return;
                    }
                    if (!response.isUserOnline())
                        onNetworkUnavailable(response.getWebServiceType());
                    progressLoading.setVisibility(View.GONE);
                    isLoading = false;
                    showLoadingOverlay(false);
                    isInitialLoading = false;
                    break;
                default:
                    break;
            }
        }
        showLoadingOverlay(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void formHashMapAndRefresh(ArrayList<Prescription> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (Prescription prescription :
                    responseList) {
                prescriptionsList.put(prescription.getUniqueId(), prescription);
            }
        }
        notifyAdapter(new ArrayList<Prescription>(prescriptionsList.values()));
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_PRESCRIPTION:
                LocalDataServiceImpl.getInstance(mApp).addPrescriptionsList(
                        (ArrayList<Prescription>) (ArrayList<?>) response
                                .getDataList());
            case GET_PRESCRIPTION:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getPrescriptionsListResponse(WebServiceType.GET_PRESCRIPTION,
                        isOTPVerified, clinicDoctorProfileList, user.getForeignLocationId(), user.getForeignHospitalId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, null, null);
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
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for prescription list refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_PRESCRIPTION_LIST);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(prescriptionListReceiver, filter);

            //receiver for prescription list local refresh
            IntentFilter filter2 = new IntentFilter();
            filter2.addAction(INTENT_GET_PRESCRIPTION_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(prescriptionListReceiverLocal, filter2);

            //receiver for prescription using prescriptionId
            IntentFilter filter3 = new IntentFilter();
            filter3.addAction(INTENT_GET_PRESCRIPTION_USING_PRESCRIPTION_ID);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(prescriptionUsingPrescriptionIdReceiver, filter3);

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
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(prescriptionListReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(prescriptionListReceiverLocal);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(prescriptionUsingPrescriptionIdReceiver);
    }

    private void sendBroadcasts() {
        Util.sendBroadcasts(mApp, new ArrayList<String>() {{
//            add(HistoryFragment.INTENT_GET_HISTORY_LIST);
//            add(PatientVisitDetailFragment.INTENT_GET_VISITS_LIST);
        }});
    }

    @Override
    public void onRefresh() {
        getPrescription(false);
    }

    @Override
    public RegisteredPatientDetailsUpdated getSelectedPatient() {
        return selectedPatient;
    }

    public void setSelectedPatient(RegisteredPatientDetailsUpdated selectedPatient) {
        this.selectedPatient = selectedPatient;
    }

    @Override
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String getLoginedUser() {
        return loginedUser;
    }

    @Override
    public void openEmrScreen(HistoryFilterType historyFilterType) {

    }

    public void refreshData(PatientDetailTabType detailTabType, ArrayList<ClinicDoctorProfile> clinicDoctorProfileList) {
        this.clinicDoctorProfileList = clinicDoctorProfileList;
        getListFromLocal(true, isOtpVerified(), PAGE_NUMBER);
        this.detailTabType = detailTabType;
    }

    public void openAddNewPrescriptionScreen() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_VISITS.ordinal());
        intent.putExtra(CommonOpenUpPatientDetailFragment.TAG_PATIENT_DETAIL_TAB_TYPE, detailTabType);
        startActivityForResult(intent, REQUEST_CODE_PRESCRIPTION_LIST);
    }

    public void setUserData(User user, String loginedUser, RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        this.selectedPatient = registeredPatientDetailsUpdated;
        this.user = user;
        this.loginedUser = loginedUser;
    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isLoading) {
            PAGE_NUMBER = PAGE_NUMBER + 1;
            LogUtils.LOGD(TAG, "LoadMore PAGE_NUMBER " + PAGE_NUMBER);
            getListFromLocal(false, isOtpVerified(), PAGE_NUMBER);
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }

    private void resetListAndPagingAttributes() {
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
        currentPageNumber = 0;
        lvPrescription.resetPreLastPosition(0);
    }
}
