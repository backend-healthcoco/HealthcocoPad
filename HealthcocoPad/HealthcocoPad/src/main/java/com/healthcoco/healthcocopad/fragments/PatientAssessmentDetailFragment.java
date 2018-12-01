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
import com.healthcoco.healthcocopad.adapter.AssessmentListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AssessmentPersonalDetail;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DietItemClickListeners;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class PatientAssessmentDetailFragment extends HealthCocoFragment implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, LocalDoInBackgroundListenerOptimised, LoadMorePageListener, DietItemClickListeners {

    public static final String INTENT_GET_INVOICE_LIST = "com.healthcoco.INVOICE_LIST";
    public static final String INTENT_GET_INVOICE_LIST_LOCAL = "com.healthcoco.INVOICE_LIST_LOCAL";

    //variables need for pagination
    public static final int MAX_SIZE = 10;
    private static final int REQUEST_CODE_ASSESSMENT = 130;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private int currentPageNumber;
    private boolean isInitialLoading;
    private HashMap<String, AssessmentPersonalDetail> personalDetailHashMap = new HashMap<>();
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvNoAssessmentFound;
    private ListViewLoadMore lvAssessment;
    private User user;
    BroadcastReceiver invoiceListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            resetListAndPagingAttributes();
            getAssessment(true);
        }
    };
    private boolean isOTPVerified = false;
    private boolean isLoading;
    private String loginedUser;
    private ProgressBar progressLoading;
    BroadcastReceiver invoiceListLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            boolean showLoading = false;
            if (intent != null && intent.hasExtra(SHOW_LOADING)) {
                showLoading = intent.getBooleanExtra(SHOW_LOADING, false);
            }
            resetListAndPagingAttributes();
            getListFromLocal(showLoading, isOtpVerified(), PAGE_NUMBER);
        }
    };
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private AssessmentListAdapter adapter;
    private PatientDetailTabType detailTabType;
    private boolean receiversRegistered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_assessment_deatil, container, false);
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
        adapter = new AssessmentListAdapter(mActivity, this);
        lvAssessment.setAdapter(adapter);
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
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_ASSESSMENT, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getAssessment(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.ASSESSMENT);
        WebDataServiceImpl.getInstance(mApp).getAssessment(AssessmentPersonalDetail.class, true, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                HealthCocoConstants.SELECTED_PATIENTS_USER_ID, latestUpdatedTime, this, this);
    }

    @Override
    public void initViews() {
        lvAssessment = (ListViewLoadMore) view.findViewById(R.id.lv_assessment);
        tvNoAssessmentFound = (TextView) view.findViewById(R.id.tv_no_assessment_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_bt_add_assessment);
    }

    @Override
    public void initListeners() {
        lvAssessment.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fl_bt_add_assessment:
                openAddAssessmentFragment();
                break;
            default:
                break;
        }

    }

    private void notifyAdapter(ArrayList<AssessmentPersonalDetail> personalDetailArrayList) {
        if (!Util.isNullOrEmptyList(personalDetailArrayList)) {
            Collections.sort(personalDetailArrayList, ComparatorUtil.assessmentDateComparator);
            lvAssessment.setVisibility(View.VISIBLE);
            tvNoAssessmentFound.setVisibility(View.GONE);
        } else {
            lvAssessment.setVisibility(View.GONE);
            tvNoAssessmentFound.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData(personalDetailArrayList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    private void openAddAssessmentFragment() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_ASSESSMENT.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_IS_FROM_ASSESSMENT, true);
        intent.putExtra(HealthCocoConstants.TAG_PATIENT_ID, HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
        startActivityForResult(intent, REQUEST_CODE_ASSESSMENT);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ASSESSMENT) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ASSESSMENT) {
                lvAssessment.smoothScrollToPosition(0);
                getAssessment(true);
                Util.showToast(mActivity, R.string.assessment_saved);
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        Util.showToast(mActivity, errorMsg);
        progressLoading.setVisibility(View.GONE);
        showLoadingOverlay(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        showLoadingOverlay(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case GET_PATIENT_ASSESSMENT:
                    if (response.isDataFromLocal()) {
                        ArrayList<AssessmentPersonalDetail> responseList = (ArrayList<AssessmentPersonalDetail>) (ArrayList<?>) response
                                .getDataList();
                        personalDetailHashMap.clear();
                        formHashMapAndRefresh(responseList);
                        if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                            isEndOfListAchieved = true;
                        else isEndOfListAchieved = false;
                        if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            getAssessment(true);
                            return;
                        }
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_ASSESSMENT, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    showLoadingOverlay(false);
                    progressLoading.setVisibility(View.GONE);
                    isInitialLoading = false;
                    break;
                default:
                    break;
            }
        }
        showLoadingOverlay(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void formHashMapAndRefresh(ArrayList<AssessmentPersonalDetail> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (AssessmentPersonalDetail personalDetail :
                    responseList) {
                personalDetailHashMap.put(personalDetail.getUniqueId(), personalDetail);
            }
        }
        notifyAdapter(new ArrayList<>(personalDetailHashMap.values()));
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_ASSESSMENT:
                LocalDataServiceImpl.getInstance(mApp).addAssessmentDetailList((ArrayList<AssessmentPersonalDetail>) (ArrayList<?>) response.getDataList());
            case GET_ASSESSMENT:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getAssessmentList(WebServiceType.GET_PATIENT_ASSESSMENT, user.getUniqueId(), user.getForeignLocationId(),
                        user.getForeignHospitalId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onRefresh() {
        getAssessment(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for prescription list refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_INVOICE_LIST);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(invoiceListReceiver, filter);
            //receiver for prescription list refresh from local
            IntentFilter filter2 = new IntentFilter();
            filter2.addAction(INTENT_GET_INVOICE_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(invoiceListLocalReceiver, filter2);
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
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(invoiceListLocalReceiver);
    }

    public void refreshData(PatientDetailTabType detailTabType) {
        getListFromLocal(true, isOtpVerified(), PAGE_NUMBER);
        this.detailTabType = detailTabType;
    }

    public void setUserData(User user, String loginedUser, RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        this.selectedPatient = registeredPatientDetailsUpdated;
        this.user = user;
        this.user.setUniqueId(loginedUser);
    }


    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading) {
            PAGE_NUMBER++;
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
        lvAssessment.resetPreLastPosition(0);
    }


    @Override
    public void onEditDietClicked(Object object) {
        AssessmentPersonalDetail assessmentPersonalDetail = (AssessmentPersonalDetail) object;
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_ASSESSMENT.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_ASSESSMENT_ID, assessmentPersonalDetail.getUniqueId());
        intent.putExtra(HealthCocoConstants.TAG_PATIENT_ID, assessmentPersonalDetail.getPatientId());
        startActivityForResult(intent, REQUEST_CODE_ASSESSMENT);
    }
}
