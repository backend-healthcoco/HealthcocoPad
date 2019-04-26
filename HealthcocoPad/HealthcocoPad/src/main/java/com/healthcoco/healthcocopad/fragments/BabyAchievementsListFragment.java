package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.BabyAchievementsResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.BabyAchievementsListItemListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.recyclerview.EndlessRecyclerViewScrollListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

public class BabyAchievementsListFragment extends HealthCocoFragment implements
        SwipeRefreshLayout.OnRefreshListener, LocalDoInBackgroundListenerOptimised, BabyAchievementsListItemListener {

    public static final String INTENT_REFRESH_REQUEST_LIST_FROM_SERVER = "com.healthcoco.android.fragments.BabyAchievementsListFragment.INTENT_REFRESH_REQUEST_LIST_FROM_SERVER";
    public static final String TAG_BABY_ACHIEVEMENTS_DATA = "babyAchievementsData";
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private RecyclerView rvBabyAchievementsList;
    private ProgressBar progressLoading;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HealthcocoRecyclerViewAdapter adapter;
    private boolean isInitialLoading;
    private boolean isEndOfListAchieved;
    private boolean receiversRegistered;
    private LinkedHashMap<String, BabyAchievementsResponse> babyAchievementsResponseLinkedHashMap = new LinkedHashMap<>();
    public static final int MAX_SIZE = 10;
    private int PAGE_NUMBER = 0;
    private TextView tvNoBabyAchievementFound;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_baby_achievements_list, container, false);
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
        rvBabyAchievementsList = (RecyclerView) view.findViewById(R.id.rv_list);
//        int spacingInPixelsVerticalBlogs = getResources().getDimensionPixelSize(R.dimen.item_spacing_baby_achievements_list_item);
        //initialsing adapter for Health Blogs
        rvBabyAchievementsList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
//        rvBabyAchievementsList.addItemDecoration(new VerticalRecyclerViewItemDecoration(spacingInPixelsVerticalBlogs));
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        tvNoBabyAchievementFound = (TextView) view.findViewById(R.id.tv_no_baby_achievements_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);
        rvBabyAchievementsList.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) rvBabyAchievementsList.getLayoutManager()) {
            @Override
            public void onLoadMore(int current_page) {
                if (!isEndOfListAchieved && !isInitialLoading) {
                    PAGE_NUMBER++;
                    getListFromLocal(false);
                }
            }
        });
    }

    private void initAdapters() {
        adapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.BABY_ACHIEVEMENTS, this);
        rvBabyAchievementsList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setUserData(User user, RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        this.selectedPatient = registeredPatientDetailsUpdated;
        this.user = user;
    }

    public void getListFromLocal(boolean showLoading) {
        if (user != null) {
            isInitialLoading = showLoading;
            if (showLoading) {
                showLoadingOverlay(true);
            }
            if (isInitialLoading) {
                resetListAndPagingAttributes();
                progressLoading.setVisibility(View.GONE);
            } else {
                progressLoading.setVisibility(View.VISIBLE);
            }
            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_BABY_ACHIEVEMENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void getBabyAchievementsList(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.GET_BABY_ACHIEVEMENTS);
        WebDataServiceImpl.getInstance(mApp).getBabyAchievementsList(BabyAchievementsResponse.class, WebServiceType.GET_BABY_ACHIEVEMENTS,
                selectedPatient.getUserId(), latestUpdatedTime, this, this);
    }

    private void notifyAdapter(ArrayList<BabyAchievementsResponse> list) {
        if (!Util.isNullOrEmptyList(list)) {
            Collections.sort(list, ComparatorUtil.babyAchievementsDateComparator);
            rvBabyAchievementsList.setVisibility(View.VISIBLE);
            tvNoBabyAchievementFound.setVisibility(View.GONE);
        } else {
            rvBabyAchievementsList.setVisibility(View.GONE);
            tvNoBabyAchievementFound.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData((ArrayList<Object>) (Object) list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        resetListAndPagingAttributes();
        getBabyAchievementsList(false);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = mActivity.getResources().getString(R.string.error);
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

    private void resetListAndPagingAttributes() {
        isInitialLoading = true;
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case GET_BABY_ACHIEVEMENTS:
                if (response.isDataFromLocal()) {
                    ArrayList<BabyAchievementsResponse> responseList = (ArrayList<BabyAchievementsResponse>) (ArrayList<?>) response
                            .getDataList();
                    formHashMapAndRefresh(responseList);
                    if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                        isEndOfListAchieved = true;
                    else isEndOfListAchieved = false;

                    if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getBabyAchievementsList(true);
                        return;
                    }
                } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_BABY_ACHIEVEMENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    response.setIsFromLocalAfterApiSuccess(true);
                    return;
                }
//                showLoadingOverlay(false);
                progressLoading.setVisibility(View.GONE);
                isInitialLoading = false;
                break;
            default:
                break;
        }
        showLoadingOverlay(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void formHashMapAndRefresh(ArrayList<BabyAchievementsResponse> responseArrayList) {
        if (!Util.isNullOrEmptyList(responseArrayList)) {
            for (BabyAchievementsResponse babyAchievementsResponse :
                    responseArrayList) {
                babyAchievementsResponseLinkedHashMap.put(babyAchievementsResponse.getUniqueId(), babyAchievementsResponse);
            }
        }
        notifyAdapter(new ArrayList<BabyAchievementsResponse>(babyAchievementsResponseLinkedHashMap.values()));
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_BABY_ACHIEVEMENTS:
                LocalDataServiceImpl.getInstance(mApp).addBabyAchievementsList((ArrayList<BabyAchievementsResponse>) (ArrayList<?>) response.getDataList());
            case GET_BABY_ACHIEVEMENTS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getBabyAchievementsList(WebServiceType.GET_BABY_ACHIEVEMENTS, selectedPatient.getUserId(), PAGE_NUMBER, MAX_SIZE, null, null);
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
            getBabyAchievementsList(true);
        }
    };

    @Override
    public void updateBabyAchievements(BabyAchievementsResponse babyAchievementsResponse) {
        openDialogFragment(new UpdateBabyAchievementsFragment(), TAG_BABY_ACHIEVEMENTS_DATA, babyAchievementsResponse, 0);
    }
}
