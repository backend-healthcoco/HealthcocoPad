package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.GrowthChartResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.GrowthChartListItemListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.recyclerview.EndlessRecyclerViewScrollListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.FontAwesomeButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class GrowthChartListFragment extends HealthCocoFragment implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, LocalDoInBackgroundListenerOptimised, GrowthChartListItemListener {

    public static final String INTENT_REFRESH_REQUEST_LIST_FROM_SERVER = "com.healthcoco.android.fragments.GrowthChartListFragment.INTENT_REFRESH_REQUEST_LIST_FROM_SERVER";
    public static final String TAG_GROWTH_CHART_DATA = "growthChartData";
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private RecyclerView rvGrowthChartList;
    private ProgressBar progressLoading;
    private LinearLayout containerNoDataFound;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HealthcocoRecyclerViewAdapter adapter;
    private boolean isInitialLoading;
    private boolean isEndOfListAchieved;
    private boolean receiversRegistered;
    private FontAwesomeButton btAddGrowthChart;
    private LinkedHashMap<String, GrowthChartResponse> growthChartResponseLinkedHashMap = new LinkedHashMap<>();
    public static final int MAX_SIZE = 10;
    private int PAGE_NUMBER = 0;
    private GrowthChartResponse growthChartResponse;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_growth_chart_list, container, false);
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
        initAdapters();
    }

    @Override
    public void initViews() {
        rvGrowthChartList = (RecyclerView) view.findViewById(R.id.rv_list);
//        int spacingInPixelsVerticalBlogs = getResources().getDimensionPixelSize(R.dimen.item_spacing_growth_chart_list_item);
        //initialsing adapter for Health Blogs
        rvGrowthChartList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
//        rvGrowthChartList.addItemDecoration(new VerticalRecyclerViewItemDecoration(spacingInPixelsVerticalBlogs));
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        btAddGrowthChart = (FontAwesomeButton) view.findViewById(R.id.bt_add);
//        containerNoDataFound = initNoDataFoundView(NoDataFoundType.NO_GROWTH_CHART);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);
        rvGrowthChartList.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) rvGrowthChartList.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                if (!isEndOfListAchieved && !isInitialLoading) {
                    PAGE_NUMBER++;
                    getListFromLocal(false);
                }
            }
        });
        btAddGrowthChart.setOnClickListener(this);
    }

    private void initAdapters() {
        adapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.GROWTH_CHART, this);
        rvGrowthChartList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setUserData(User user, RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        this.selectedPatient = registeredPatientDetailsUpdated;
        this.user = user;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.ADD_GROWTH_CHART, null, null, HealthCocoConstants.REQUEST_CODE_GROWTH_CHART_LIST);
                break;
        }
    }

    public void getListFromLocal(boolean showLoading) {
        if (user != null) {
            isInitialLoading = showLoading;
            if (showLoading) {
                showLoadingOverlay(true);
            }
            if (isInitialLoading) {
                progressLoading.setVisibility(View.GONE);
            } else {
                progressLoading.setVisibility(View.VISIBLE);
            }
            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_GROWTH_CHART, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void getGrowthChartList(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.GET_GROWTH_CHART);
        WebDataServiceImpl.getInstance(mApp).getGrowthChartList(GrowthChartResponse.class, WebServiceType.GET_GROWTH_CHART, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                selectedPatient.getUserId(), latestUpdatedTime, this, this);
    }

    private void notifyAdapter(ArrayList<GrowthChartResponse> list) {
        if (!Util.isNullOrEmptyList(list)) {
            Collections.sort(list, ComparatorUtil.growthChartDateComparator);
            rvGrowthChartList.setVisibility(View.VISIBLE);
            containerNoDataFound.setVisibility(View.GONE);
        } else {
            rvGrowthChartList.setVisibility(View.GONE);
            containerNoDataFound.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData((ArrayList<Object>) (Object) list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        getListFromLocal(true);
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

    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case GET_GROWTH_CHART:
                if (response.isDataFromLocal()) {
                    ArrayList<GrowthChartResponse> responseList = (ArrayList<GrowthChartResponse>) (ArrayList<?>) response
                            .getDataList();
                    formHashMapAndRefresh(responseList);
                    if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                        isEndOfListAchieved = true;
                    else isEndOfListAchieved = false;

                    if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getGrowthChartList(true);
                        return;
                    }
                } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_GROWTH_CHART, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    response.setIsFromLocalAfterApiSuccess(true);
                    return;
                }
                showLoadingOverlay(false);
                progressLoading.setVisibility(View.GONE);
                isInitialLoading = false;
                break;
            case DISCARD_GROWTH_CHART:
                if (response.getData() instanceof Boolean) {
                    boolean isDataSuccess = (boolean) response.getData();
                    if (isDataSuccess)
                        growthChartResponse.setDiscarded(true);
                    LocalDataServiceImpl.getInstance(mApp).addGrowthChartResponse(growthChartResponse);
                    growthChartResponseLinkedHashMap.clear();
                    getGrowthChartList(true);
                }
                break;
            default:
                break;
        }
        showLoadingOverlay(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void formHashMapAndRefresh(ArrayList<GrowthChartResponse> responseArrayList) {
        if (!Util.isNullOrEmptyList(responseArrayList)) {
            for (GrowthChartResponse growthChartResponse :
                    responseArrayList) {
                growthChartResponseLinkedHashMap.put(growthChartResponse.getUniqueId(), growthChartResponse);
            }
        }
        notifyAdapter(new ArrayList<GrowthChartResponse>(growthChartResponseLinkedHashMap.values()));
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_GROWTH_CHART:
                LocalDataServiceImpl.getInstance(mApp).addGrowthChartList((ArrayList<GrowthChartResponse>) (ArrayList<?>) response.getDataList());
            case GET_GROWTH_CHART:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getGrowthChartList(WebServiceType.GET_GROWTH_CHART, user.getUniqueId(), user.getForeignLocationId(),
                        user.getForeignHospitalId(), selectedPatient.getUserId(), PAGE_NUMBER, MAX_SIZE, null, null);
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.LOGD(TAG, "receiversRegistered " + receiversRegistered);
        if (!receiversRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_REFRESH_REQUEST_LIST_FROM_SERVER);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshRequestsListFromServerReceiver, filter);
        }
        receiversRegistered = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshRequestsListFromServerReceiver);
    }

    BroadcastReceiver refreshRequestsListFromServerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getListFromLocal(true);
        }
    };

    @Override
    public void editGrowthChart(GrowthChartResponse growthChartResponse) {
        openCommonOpenUpActivityUsingParcel(CommonOpenUpFragmentType.ADD_GROWTH_CHART,
                new String[]{TAG_GROWTH_CHART_DATA}, new Object[]{growthChartResponse});
    }

    @Override
    public void discardGrowthChart(GrowthChartResponse growthChartResponse) {
        if (HealthCocoConstants.isNetworkOnline) {
            int msgId = R.string.confirm_discard_growth_chart_message;
            int titleId = R.string.confirm_discard_growth_chart_title;
            showConfirmationAlert(mActivity.getResources().getString(titleId), mActivity.getResources().getString(msgId), growthChartResponse);
        }
    }

    private void showConfirmationAlert(String title, String msg, final GrowthChartResponse growthChartResponse) {
        if (Util.isNullOrBlank(title))
            title = mActivity.getResources().getString(R.string.confirm);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(msg);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                discardGrowthChartRequest(growthChartResponse);
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    private void discardGrowthChartRequest(GrowthChartResponse growthChartResponse) {
        this.growthChartResponse = growthChartResponse;
        showLoadingOverlay(false);
        WebDataServiceImpl.getInstance(mApp).discardGrowthChart(Boolean.class, WebServiceType.DISCARD_GROWTH_CHART, growthChartResponse.getUniqueId(), this, this);
    }
}
