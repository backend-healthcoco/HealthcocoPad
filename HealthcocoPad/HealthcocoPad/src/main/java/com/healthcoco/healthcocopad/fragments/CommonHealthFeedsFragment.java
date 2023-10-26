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

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.CommonHealthFeedsAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.HealthcocoBlogResponse;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Shreshtha on 23-Sep-17.
 */

public class CommonHealthFeedsFragment extends HealthCocoFragment implements LoadMorePageListener, Response.Listener<VolleyResponseBean>, SwipeRefreshLayout.OnRefreshListener {
    public static final String INTENT_REFRESH_LOGIN = "com.healthcoco.android.fragments.CommonHealthFeedsFragment.INTENT_REFRESH_LOGIN";
    public static final String INTENT_REFRESH_FAV_BLOGS = "com.healthcoco.android.fragments.CommonHealthFeedsFragment.INTENT_REFRESH_FAV_BLOGS";
    public static final String INTENT_REFRESH_BLOGS = "com.healthcoco.android.fragments.CommonHealthFeedsFragment.INTENT_REFRESH_BLOGS";
    private static final int MAX_SIZE = 10;
    public static final String TAG_BLOG_UNIQUE_ID = "blogUniqueId";
    private ListViewLoadMore lvFeeds;
//    private LinearLayout containerNoDataFound;
    private CommonHealthFeedsAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressLoading;
    private LinkedHashMap<String, HealthcocoBlogResponse> list = new LinkedHashMap<>();
    private FeedsFragment.TabType tabType;
    private boolean receiversRegistered;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private boolean isInitialLoading;

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for refreshing data after login
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_REFRESH_LOGIN);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshLoginReceiver, filter);

            //receiver for refreshing data after favorites refresh
            IntentFilter refreshFavBlogsFilter = new IntentFilter();
            refreshFavBlogsFilter.addAction(INTENT_REFRESH_FAV_BLOGS);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshFavBlogsReceiver, refreshFavBlogsFilter);

            //receiver for refreshing data after favorites refresh
            IntentFilter refreshAllBlogsFilter = new IntentFilter();
            refreshAllBlogsFilter.addAction(INTENT_REFRESH_BLOGS);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshAllBlogsReceiver, refreshAllBlogsFilter);

            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshLoginReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshFavBlogsReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshAllBlogsReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_all_health_feeds, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        int ordinal = bundle.getInt(FeedsFragment.TabType.TAG_TAB_TYPE, -1);
        if (ordinal != -1) {
            tabType = FeedsFragment.TabType.values()[ordinal];
            init();
        }
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
        initData();
    }

    private void initData() {
        switch (tabType) {
            case HEALTH_FEEDS:
                getDataFromServer(true);
                break;
        }
    }

    private void getDataFromServer(boolean showLoading) {
        if (showLoading) {
            mActivity.showLoading(false);
            progressLoading.setVisibility(View.GONE);
        }
        String selectedPatientUserId = null;
//        selectedPatient = ((HomeActivity) mActivity).getSelectedRegisteredPatientDetails();
//        if (selectedPatient != null)
//            selectedPatientUserId = selectedPatient.getUserId();
        WebDataServiceImpl.getInstance(mApp).getBlogsList(tabType.getWebServiceType(), HealthcocoBlogResponse.class, selectedPatientUserId, this, this);
    }

    private void notifyAdapter(List<HealthcocoBlogResponse> list) {
        if (!Util.isNullOrEmptyList(list)) {
            lvFeeds.setVisibility(View.VISIBLE);
//            containerNoDataFound.setVisibility(View.GONE);
        } else {
//            if (!mActivity.isLoaderVisible()) {
            lvFeeds.setVisibility(View.GONE);
//            containerNoDataFound.setVisibility(View.VISIBLE);
//            }
        }
        mAdapter.setListData(list);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void initViews() {
        lvFeeds = (ListViewLoadMore) view.findViewById(R.id.lv_feeds);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
//        containerNoDataFound = initNoDataFoundView(tabType.getNoDataFoundType());
    }

    @Override
    public void initListeners() {
        lvFeeds.setLoadMoreListener(this);
        lvFeeds.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initAdapter() {
        mAdapter = new CommonHealthFeedsAdapter(mActivity);
        lvFeeds.setAdapter(mAdapter);
    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading) {
            progressLoading.setVisibility(View.VISIBLE);
            PAGE_NUMBER = PAGE_NUMBER + 1;
            getDataFromServer(false);
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return false;
    }

    @Override
    public void onRefresh() {
        resetListAndPagingAttributes();
        getDataFromServer(false);
    }

    public void refreshData(FeedsFragment.TabType tabType) {
        this.tabType = tabType;
        getDataFromServer(true);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));

            switch (response.getWebServiceType()) {
                case GET_FAV_HEALTH_BLOGS:
                case GET_All_HEALTH_BLOGS:
                    ArrayList<HealthcocoBlogResponse> blogsList = (ArrayList<HealthcocoBlogResponse>) (ArrayList<?>) response
                            .getDataList();
                    if (!Util.isNullOrEmptyList(blogsList) && blogsList.size() < MAX_SIZE || Util.isNullOrEmptyList(blogsList))
                        isEndOfListAchieved = true;

                    formHashMapAndRefresh(blogsList);
                    break;

                default:
                    break;
            }
        }
        mActivity.hideLoading();
        progressLoading.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void formHashMapAndRefresh(ArrayList<HealthcocoBlogResponse> responseList) {
        if (!Util.isNullOrEmptyList(responseList))
            for (HealthcocoBlogResponse object :
                    responseList) {
                HealthcocoBlogResponse healthcocoBlogResponse = (HealthcocoBlogResponse) object;
                if (!Util.isNullOrBlank(healthcocoBlogResponse.getUniqueId()))
                    list.put(healthcocoBlogResponse.getUniqueId(), object);
            }
        notifyAdapter(new ArrayList<>(list.values()));
    }

    private void resetListAndPagingAttributes() {
        if (list == null)
            list = new LinkedHashMap<>();
        list.clear();
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
    }

    BroadcastReceiver refreshLoginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            resetListAndPagingAttributes();
            getDataFromServer(true);
        }
    };

    BroadcastReceiver refreshFavBlogsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(TAG_BLOG_UNIQUE_ID)) {
                if (tabType == FeedsFragment.TabType.FAVOURITE_FEEDS) {
                    String uniqueId = intent.getStringExtra(TAG_BLOG_UNIQUE_ID);
                    if (!Util.isNullOrBlank(uniqueId))
                        list.remove(uniqueId);
                    notifyAdapter(new ArrayList<>(list.values()));
                }
            }
        }
    };

    BroadcastReceiver refreshAllBlogsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getDataFromServer(false);
        }
    };
}
