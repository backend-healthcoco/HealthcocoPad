package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.HomeActivity;
import com.healthcoco.healthcocopad.adapter.FilterGroupListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.FilterItemType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.OnFilterItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Shreshtha on 31-01-2017.
 */
public class FilterFragment extends HealthCocoFragment implements
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener,
        View.OnClickListener, OnFilterItemClickListener, SwipeRefreshLayout.OnRefreshListener,
        LocalDoInBackgroundListenerOptimised {
    public static final String INTENT_REFRESH_GROUPS_LIST_LOCAL = "com.healthcoco.REFRESH_GROUPS_LIST_LOCAL";
    public static final String INTENT_FILTER_REFRESH_SELECTED_FILTER_TYPE = "com.healthcoco.healthcocopad.fragments.FilterFragment.REFRESH_SELECTED_FILTER_TYPE";

    public static final String TAG_ORDINAL = "ordinal";
    public static final String TAG_GROUP_ID = "group_id";

    private User user;
    private ArrayList<UserGroups> groupsList;
    private ListViewLoadMore lvGroups;
    private ImageButton btAddNewGroup;
    private TextView tvNoGroups;
    private FilterGroupListAdapter adapter;
    private TextView tvAllPatients;
    private TextView tvRecentlyVisited;
    private TextView tvMostVisited;
    private TextView tvRecentlyAdded;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean receiversRegistered;
    private String selectedFilterTitle;
    private String selectedGroupId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_filter, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        lvGroups = (ListViewLoadMore) view.findViewById(R.id.lv_groups);
        tvNoGroups = (TextView) view.findViewById(R.id.tv_no_groups);
        btAddNewGroup = (ImageButton) view.findViewById(R.id.bt_add_to_group);
        tvAllPatients = (TextView) view.findViewById(R.id.tv_all_patients);
        tvRecentlyVisited = (TextView) view.findViewById(R.id.tv_recently_visited);
        tvMostVisited = (TextView) view.findViewById(R.id.tv_most_visited);
        tvRecentlyAdded = (TextView) view.findViewById(R.id.tv_recently_added);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_action_bar);
    }

    @Override
    public void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);
        lvGroups.setSwipeRefreshLayout(swipeRefreshLayout);
        btAddNewGroup.setOnClickListener(this);
        tvAllPatients.setOnClickListener(this);
        tvRecentlyVisited.setOnClickListener(this);
        tvMostVisited.setOnClickListener(this);
        tvRecentlyAdded.setOnClickListener(this);
    }

    private void initAdapter() {
        adapter = new FilterGroupListAdapter(mActivity, this);
        lvGroups.setAdapter(adapter);
    }

    public void getGroupsListFromLocal() {
        notifyAdapter(null);
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null)
            user = doctor.getUser();
        LocalDataServiceImpl.getInstance(mApp).getUserGroups(WebServiceType.GET_GROUPS, null, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null && !Util.isNullOrBlank(user.getUniqueId())
                            && !Util.isNullOrBlank(user.getForeignHospitalId())
                            && !Util.isNullOrBlank(user.getForeignLocationId())) {
                        if (user != null && !Util.isNullOrBlank(user.getUniqueId())) {
                            getGroupsListFromLocal();
                            return;
                        }
                    }
                    break;
                case GET_GROUPS:
                    if (response.isDataFromLocal()) {
                        groupsList = (ArrayList<UserGroups>) (ArrayList<?>) response
                                .getDataList();
                        notifyAdapter(groupsList);
                    }
                    break;
            }
        }

        swipeRefreshLayout.setRefreshing(false);
        mActivity.hideLoading();
    }

    private void notifyAdapter(ArrayList<UserGroups> list) {
        if (!Util.isNullOrEmptyList(list)) {
            Collections.sort(list, ComparatorUtil.groupDateComparator);
            lvGroups.setVisibility(View.VISIBLE);
            tvNoGroups.setVisibility(View.GONE);
            adapter.setListData(list);
            adapter.notifyDataSetChanged();
        } else {
            lvGroups.setVisibility(View.GONE);
            tvNoGroups.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        swipeRefreshLayout.setRefreshing(false);
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        swipeRefreshLayout.setRefreshing(false);
        mActivity.hideLoading();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_to_group:
                mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_NEW_GROUP, AddUpdateNameDialogType.GROUPS, this, user, "", HealthCocoConstants.REQUEST_CODE_GROUPS_LIST);
                break;
            case R.id.tv_all_patients:
                senBroadCastToContactsFragment(FilterItemType.ALL_PATIENTS, null);
                setSelectedItem(FilterItemType.ALL_PATIENTS);
                refreshHomeScreenTitle(getResources().getString(R.string.my_patients));
                break;
            case R.id.tv_recently_visited:
                senBroadCastToContactsFragment(FilterItemType.RECENTLY_VISITED, null);
                setSelectedItem(FilterItemType.RECENTLY_VISITED);
                refreshHomeScreenTitle(getResources().getString(R.string.recently_visited));
                break;
            case R.id.tv_most_visited:
                senBroadCastToContactsFragment(FilterItemType.MOST_VISITED, null);
                setSelectedItem(FilterItemType.MOST_VISITED);
                refreshHomeScreenTitle(getResources().getString(R.string.most_visited));
                break;
            case R.id.tv_recently_added:
                senBroadCastToContactsFragment(FilterItemType.RECENTLY_ADDED, null);
                setSelectedItem(FilterItemType.RECENTLY_ADDED);
                refreshHomeScreenTitle(getResources().getString(R.string.recently_added));
                break;
        }
    }

    private void refreshHomeScreenTitle(String title) {
        this.selectedFilterTitle = title;
        ((HomeActivity) mActivity).setActionbarTitle(title);
    }

    public void setSelectedItem(FilterItemType filterItemType) {
        clearPreviuosFilters();
        switch (filterItemType) {
            case RECENTLY_VISITED:
                tvRecentlyVisited.setSelected(true);
                break;
            case RECENTLY_ADDED:
                tvRecentlyAdded.setSelected(true);
                break;
            case SEARCH_PATIENT:
            case ALL_PATIENTS:
                tvAllPatients.setSelected(true);
                break;
            case MOST_VISITED:
                tvMostVisited.setSelected(true);
                break;
            case GROUP_ITEM:
                adapter.setSelectedGroupId(selectedGroupId);
                adapter.notifyDataSetChanged();
        }
    }

    private void clearPreviuosFilters() {
        try {
            tvAllPatients.setSelected(false);
            tvRecentlyVisited.setSelected(false);
            tvMostVisited.setSelected(false);
            tvRecentlyAdded.setSelected(false);
            adapter.setSelectedGroupId(null);
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGroupItemClicked(UserGroups group) {
        int selectedGroupPosition = groupsList.indexOf(group);
        LogUtils.LOGD(TAG, "Selected Group Position " + selectedGroupPosition);
        selectedGroupId = group.getUniqueId();
        setSelectedItem(FilterItemType.GROUP_ITEM);
        senBroadCastToContactsFragment(FilterItemType.GROUP_ITEM, group.getUniqueId());
        refreshHomeScreenTitle(group.getName());
    }

    private void senBroadCastToContactsFragment(FilterItemType itemType, Object intentData) {
        try {
            ((HomeActivity) mActivity).closeDrawer();
            Intent intent = new Intent();
            switch (itemType) {
                case GROUP_ITEM:
                    intent.putExtra(HealthCocoConstants.TAG_GROUP_ID, (String) intentData);
                    break;
            }
            intent.putExtra(HealthCocoConstants.TAG_ORDINAL, itemType.ordinal());
            intent.setAction(ContactsListFragment.INTENT_FILTER_TYPE);
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        Util.sendBroadcast(mApp, ContactsListFragment.INTENT_REFRESH_GROUPS_LIST_FROM_SERVER);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for refreshing groups
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_REFRESH_GROUPS_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshListner, filter);

            //receiver for refreshing groups
            IntentFilter filter1 = new IntentFilter();
            filter1.addAction(INTENT_FILTER_REFRESH_SELECTED_FILTER_TYPE);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshSelectedFilterTypeReceiver, filter1);
            receiversRegistered = true;
            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshListner);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshSelectedFilterTypeReceiver);
    }

    BroadcastReceiver refreshListner = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getGroupsListFromLocal();
        }
    };

    BroadcastReceiver refreshSelectedFilterTypeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                FilterItemType selectedFilterType = FilterItemType.values()[intent.getIntExtra(TAG_ORDINAL, -1)];
                selectedGroupId = intent.getStringExtra(TAG_GROUP_ID);
                if (selectedFilterType != null)
                    setSelectedItem(selectedFilterType);
            }
        }
    };

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                }
                break;
            case GET_GROUPS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getUserGroups(WebServiceType.GET_GROUPS, null, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), null, null);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    public String getSelectedFilterTitle() {
        return selectedFilterTitle;
    }
}
