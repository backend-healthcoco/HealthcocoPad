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
import com.healthcoco.healthcocopad.adapter.DietPlanListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DietPlan;
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
import com.healthcoco.healthcocopad.listeners.InvoiceItemClickListeners;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Prashant on 10-10-2018.
 */
public class PatientDietPlanDetailFragment extends HealthCocoFragment implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, LocalDoInBackgroundListenerOptimised, LoadMorePageListener, DietItemClickListeners {

    public static final String INTENT_GET_DIET_PLAN_LIST = "com.healthcoco.DIET_PLAN_LIST";
    public static final String INTENT_GET_DIET_PLAN_LIST_LOCAL = "com.healthcoco.INVOICE_LIST_LOCAL";

    //variables need for pagination
    public static final int MAX_SIZE = 10;
    public static final String TAG_DIET_PLAN_DATA = "dietPlanData";
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private int currentPageNumber;
    private boolean isInitialLoading;
    private HashMap<String, DietPlan> dietPlanHashMap = new HashMap<>();
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvNoDietPlanFound;
    private ListViewLoadMore lvDietPlan;
    private User user;
    BroadcastReceiver dietPlanListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            resetListAndPagingAttributes();
            getDietPlanList(true);
        }
    };
    private boolean isOTPVerified = false;
    private boolean isLoading;
    private String loginedUser;
    private ProgressBar progressLoading;
    BroadcastReceiver dietPlanListLocalReceiver = new BroadcastReceiver() {
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
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private DietPlanListAdapter adapter;
    private PatientDetailTabType detailTabType;
    private boolean receiversRegistered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_diet_plan_deatil, container, false);
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
        adapter = new DietPlanListAdapter(mActivity, this);
        lvDietPlan.setAdapter(adapter);
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
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DIET_PLAN, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void getDietPlanList(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.DIET_PLAN);
        WebDataServiceImpl.getInstance(mApp).getDietPlan(DietPlan.class, false, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                HealthCocoConstants.SELECTED_PATIENTS_USER_ID, latestUpdatedTime, this, this);
    }

    @Override
    public void initViews() {
        lvDietPlan = (ListViewLoadMore) view.findViewById(R.id.lv_diet_plan);
        tvNoDietPlanFound = (TextView) view.findViewById(R.id.tv_no_diet_plan_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_bt_add_diet_plan);
    }

    @Override
    public void initListeners() {
        lvDietPlan.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fl_bt_add_diet_plan:
                openAddDietPlanFragment();
                break;
            default:
                break;
        }

    }

    private void notifyAdapter(ArrayList<DietPlan> dietPlanArrayList) {
        if (!Util.isNullOrEmptyList(dietPlanArrayList)) {
            Collections.sort(dietPlanArrayList, ComparatorUtil.dietPlanComparator);
            lvDietPlan.setVisibility(View.VISIBLE);
            tvNoDietPlanFound.setVisibility(View.GONE);
        } else {
            lvDietPlan.setVisibility(View.GONE);
            tvNoDietPlanFound.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData(dietPlanArrayList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    private void openAddDietPlanFragment() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_DIET.ordinal());
//        intent.putExtra(CommonOpenUpPatientDetailFragment.TAG_PATIENT_DETAIL_TAB_TYPE, detailTabType);
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_ADD_DIET);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_ADD_DIET) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_DIET) {
                lvDietPlan.smoothScrollToPosition(0);
                getDietPlanList(true);
                Util.showToast(mActivity, "Diet Plan Saved");
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
                case GET_DIET_PLAN:
//                    if (response.isDataFromLocal()) {
                    ArrayList<DietPlan> responseList = (ArrayList<DietPlan>) (ArrayList<?>) response
                            .getDataList();
                    dietPlanHashMap.clear();
                    formHashMapAndRefresh(responseList);
                    if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                        isEndOfListAchieved = true;
                    else isEndOfListAchieved = false;
                    if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
//                            getDietPlanList(true);
                        isInitialLoading = false;
                        return;
                    }
                    /*} else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DIET_PLAN, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }*/
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

    private void formHashMapAndRefresh(ArrayList<DietPlan> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (DietPlan dietPlan :
                    responseList) {
                dietPlanHashMap.put(dietPlan.getUniqueId(), dietPlan);
            }
        }
        notifyAdapter(new ArrayList<>(dietPlanHashMap.values()));
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
          /*  case ADD_INVOICE:
                LocalDataServiceImpl.getInstance(mApp).addInvoiceList((ArrayList<Invoice>) (ArrayList<?>) response.getDataList());
            case GET_INVOICE:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getInvoiceList(WebServiceType.GET_INVOICE, user.getUniqueId(), user.getForeignLocationId(),
                        user.getForeignHospitalId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
   */
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onRefresh() {
        getDietPlanList(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for prescription list refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_DIET_PLAN_LIST);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(dietPlanListReceiver, filter);
            //receiver for prescription list refresh from local
            IntentFilter filter2 = new IntentFilter();
            filter2.addAction(INTENT_GET_DIET_PLAN_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(dietPlanListLocalReceiver, filter2);
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
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(dietPlanListLocalReceiver);
    }

    private void sendBroadcasts() {
      /*  Util.sendBroadcasts(mApp, new ArrayList<String>() {{
//            add(HistoryFragment.INTENT_GET_HISTORY_LIST);
            add(PatientVisitDetailFragment.INTENT_GET_VISITS_LIST);
        }});*/
    }


    public void refreshData(PatientDetailTabType detailTabType) {
        getListFromLocal(true, isOtpVerified(), PAGE_NUMBER);
        getDietPlanList(true);
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
        lvDietPlan.resetPreLastPosition(0);
    }

    @Override
    public void onEditDietClicked(Object object) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_DIET.ordinal());
        if (object != null) {
            DietPlan dietPlan = (DietPlan) object;
            intent.putExtra(TAG_DIET_PLAN_DATA, Parcels.wrap(dietPlan));
        }
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_ADD_DIET);
    }
}
