package com.healthcoco.healthcocopad.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.ViewPagerAdapterTabLayout;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.HealthcocoBlogResponse;
import com.healthcoco.healthcocopad.enums.FeedListType;
import com.healthcoco.healthcocopad.enums.PatientRegistrationTabsType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.BlogFragmentRefreshListener;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 22-Sep-17.
 */

public class FeedsFragment extends HealthCocoFragment implements
        Response.Listener<VolleyResponseBean>, TabLayout.OnTabSelectedListener, BlogFragmentRefreshListener {
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ArrayList<FeedListType> feedListTypes;
    private ViewPagerAdapterTabLayout viewPagerAdapter;
    private static final int TAB_HEALTH = 0;
    private static final int TAB_FAVORITE = 1;
    private int ordinal;
    private CommonHealthFeedsFragment myHealthsFragment;
    private CommonHealthFeedsFragment favouriteFeedsFragment;
    private boolean receiversRegistered;

    enum TabType {
        HEALTH_FEEDS(R.string.health, WebServiceType.GET_All_HEALTH_BLOGS),
        FAVOURITE_FEEDS(R.string.favorite, WebServiceType.GET_FAV_HEALTH_BLOGS);

        public static final int TAB_POSITION_HEALTH_FEEDS = 0;
        public static final int TAB_POSITION_FAVOURITE_FEEDS = 1;
        public static final String TAG_TAB_TYPE = "tabType";
        private final int textId;
        private final WebServiceType webServiceType;

        TabType(int textId, WebServiceType webServiceType) {
            this.textId = textId;
            this.webServiceType = webServiceType;
        }

        public int getTextId() {
            return textId;
        }

        public WebServiceType getWebServiceType() {
            return webServiceType;
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_health_feeds, container, false);
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
        Bundle bundle = getArguments();
        initViews();
        initListeners();
        initTabsFragmentsList();
//        getBlogDataFromServer(false, WebServiceType.GET_All_HEALTH_BLOGS);
    }

    private void getBlogDataFromServer(boolean showLoading, WebServiceType webServiceType) {
        if (showLoading) {
            mActivity.showLoading(false);
        }
        String selectedPatientUserId = "";
        WebDataServiceImpl.getInstance(mApp).getBlogsList(webServiceType, HealthcocoBlogResponse.class, selectedPatientUserId, this, this);
    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager_feeds);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
    }

    @Override
    public void initListeners() {
        tabLayout.addOnTabSelectedListener(this);
    }

    private void initTabsFragmentsList() {
//        if (!Util.isNullOrEmptyList(feedListTypes)) {
            for (FeedListType feedListType :
                    FeedListType.values()) {

                if (feedListType != null) {
                    switch (feedListType) {
                        case HEALTH:
                            myHealthsFragment = new CommonHealthFeedsFragment();
                            addFragment(feedListType, myHealthsFragment, feedListType.getTitleId());
                            break;
                        case FAVORITE:
                            favouriteFeedsFragment = new CommonHealthFeedsFragment();
                            addFragment(feedListType, favouriteFeedsFragment, feedListType.getTitleId());
                            break;
                    }
                }
//            }
        }
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    private void addFragment(FeedListType feedListType, HealthCocoFragment fragment, int tabIndicatorId) {
        if (viewPagerAdapter == null)
            viewPagerAdapter = new ViewPagerAdapterTabLayout(mFragmentManager);
        Bundle bundle = new Bundle();
        bundle.putInt(TabType.TAG_TAB_TYPE, feedListType.ordinal());
        fragment.setArguments(bundle);
        viewPagerAdapter.addFragment(fragment, getResources().getString(tabIndicatorId));
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
//        if (response.getWebServiceType() != null) {
//            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
//            HealthBlogResponse allHealthBlogResponse = null;
//            if (response.getData() != null && response.getData() instanceof HealthBlogResponse) {
//                allHealthBlogResponse = (HealthBlogResponse) response.getData();
//            }
//            switch (response.getWebServiceType()) {
//                case GET_All_HEALTH_BLOGS:
//                    myHealthsFragment.refreshData(allHealthBlogResponse);
//                    break;
//                case GET_FAV_HEALTH_BLOGS:
//                    favouriteFeedsFragment.refreshData(allHealthBlogResponse);
//                    break;
//            }
//        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        int position = tab.getPosition();
        if (position == 1) {
            getBlogDataFromServer(false, WebServiceType.GET_FAV_HEALTH_BLOGS);
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void refreshBlog() {
        int position = tabLayout.getSelectedTabPosition();
        FeedListType feedListType = FeedListType.values()[position];
        switch (feedListType) {
            case HEALTH:
                getBlogDataFromServer(false, WebServiceType.GET_All_HEALTH_BLOGS);
                break;
            case FAVORITE:
                getBlogDataFromServer(false, WebServiceType.GET_FAV_HEALTH_BLOGS);
                break;
        }
    }
}
