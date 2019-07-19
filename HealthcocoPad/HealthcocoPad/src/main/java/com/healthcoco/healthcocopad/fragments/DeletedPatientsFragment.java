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
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.ContactsListAdapter;
import com.healthcoco.healthcocopad.adapter.DeleteContactsListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsNew;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.ContactsItemOptionsForDeletedListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.GridViewLoadMore;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

public class DeletedPatientsFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        GsonRequest.ErrorListener, SwipeRefreshLayout.OnRefreshListener, ContactsItemOptionsForDeletedListener,
        HealthcocoTextWatcherListener, LoadMorePageListener {

    public static final String INTENT_REFRESH_CONTACTS_LIST_FROM_LOCAL = "com.healthcoco.healthcocoplus.fragments.DeletedPatientsFragment.REFRESH_CONTACTS_LIST_FROM_LOCAL";

    private User user;
    public static final int MAX_SIZE = 15;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private boolean isInitialLoading = true;
    //other variables
    private ProgressBar progressLoading;
    private GridViewLoadMore lvContacts;
    private DeleteContactsListAdapter adapter;
    private TextView tvNoPatients;

    private LinkedHashMap<String, RegisteredPatientDetailsUpdated> patientsListHashMap = new LinkedHashMap<>();
    private LinkedHashMap<String, RegisteredPatientDetailsNew> patientDetailsNewLinkedHashMap = new LinkedHashMap<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton btAddNewPatient;
    private AsyncTask<VolleyResponseBean, VolleyResponseBean, VolleyResponseBean> asynTaskGetPatients;
    private String selectedGroupId;
    private String lastTextSearched = "";
    private boolean isResetList = true;
    private boolean isEditTextSearching;
    private User selectedUser;
    private boolean isLoadingFromSearch;
    private boolean isEndOfListAchievedServer = true;
    private boolean receiversRegistered;
    private EditText editSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_deleted_patients, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
    }

    @Override
    public void initViews() {
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        lvContacts = (GridViewLoadMore) view.findViewById(R.id.gv_contacts);
        tvNoPatients = (TextView) view.findViewById(R.id.tv_no_patients);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        editSearch = (EditText) view.findViewById(R.id.edit_search);
//        GridLayoutManager gridLayoutManagerVertical = new GridLayoutManager(mActivity,
//                3, //The number of Columns in the grid
//                LinearLayoutManager.VERTICAL,
//                false);
//        int spacingInPixelsVerticalBlogs = getResources().getDimensionPixelSize(R.dimen.item_spacing_grid_item);
//        lvContacts.addItemDecoration(new VerticalRecyclerViewItemDecoration(spacingInPixelsVerticalBlogs));
//        lvContacts.setLayoutManager(gridLayoutManagerVertical);
//        lvContacts.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
//        lvContacts.addOnScrollListener(new EndlessRecyclerViewScrollListener((LinearLayoutManager) lvContacts.getLayoutManager()) {
//            @Override
//            public void onLoadMore(int current_page) {
//                if (!isEndOfListAchieved && !isInitialLoading && !isLoadingFromSearch) {
//                    PAGE_NUMBER++;
//                    getListFromLocal(false);
//                }
//            }
//        });
    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading && !isLoadingFromSearch) {
            PAGE_NUMBER++;
            getListFromLocal(false);
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return false;
    }

    @Override
    public void initListeners() {
        initEditSearchView(R.string.name_mobile_number, new HealthcocoTextWatcher(editSearch, this), true);
        swipeRefreshLayout.setOnRefreshListener(this);
        lvContacts.setLoadMoreListener(this);
        lvContacts.setSwipeRefreshLayout(swipeRefreshLayout);
    }

    private void initAdapter() {
        adapter = new DeleteContactsListAdapter(mActivity, this);
        lvContacts.setAdapter(adapter);
//        adapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.CONTACT_LIST_FOR_DELETED, this);
//        lvContacts.setAdapter(adapter);
    }

    private void notifyAdapter(List<RegisteredPatientDetailsUpdated> patientsList) {
//        LogUtils.LOGD(TAG, "Success notifyAdapter :" + patientsList.size());
//        if (!Util.isNullOrEmptyList(patientsList)) {
//            lvContacts.setVisibility(View.VISIBLE);
//            tvNoPatients.setVisibility(View.GONE);
//        } else {
//            lvContacts.setVisibility(View.GONE);
//            tvNoPatients.setVisibility(View.VISIBLE);
//        }
//        progressLoading.setVisibility(View.GONE);
//        adapter.setListData(patientsList);
//        adapter.notifyDataSetChanged();
    }

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
            case GET_PATIENTS_NEW:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getSearchedPatientsListNewPageWise(WebServiceType.GET_CONTACTS_NEW, user,
                                PAGE_NUMBER, MAX_SIZE, lastTextSearched, BooleanTypeValues.TRUE, null, null);
                break;
            case GET_PATIENTS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getSearchedPatientsListPageWise(WebServiceType.GET_CONTACTS, user,
                                PAGE_NUMBER, MAX_SIZE, lastTextSearched, BooleanTypeValues.TRUE, null, null);
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

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null && !Util.isNullOrBlank(user.getUniqueId())
                            && !Util.isNullOrBlank(user.getForeignHospitalId())
                            && !Util.isNullOrBlank(user.getForeignLocationId())) {
                        getListFromLocal(true);
                        return;
                    }
                    break;
                case GET_CONTACTS:
                    if (response.isDataFromLocal()) {
                        ArrayList<RegisteredPatientDetailsUpdated> responseList = (ArrayList<RegisteredPatientDetailsUpdated>) (ArrayList<?>) response
                                .getDataList();
                        isInitialLoading = false;
                        mActivity.hideProgressDialog();
                        formHashMapAndRefresh(responseList);
                        if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                            isEndOfListAchieved = true;
                        else isEndOfListAchieved = false;
                        response.setIsFromLocalAfterApiSuccess(true);
                    }
                    break;
                case GET_CONTACTS_NEW:
                    if (response.isDataFromLocal()) {
                        ArrayList<RegisteredPatientDetailsNew> responseList = (ArrayList<RegisteredPatientDetailsNew>) (ArrayList<?>) response
                                .getDataList();
                        isInitialLoading = false;
                        formHashMapAndRefreshNew(responseList);
                        if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                            isEndOfListAchieved = true;
                        else isEndOfListAchieved = false;
                        response.setIsFromLocalAfterApiSuccess(true);
                    }
                    break;
            }
            notifyAdapter(new ArrayList<RegisteredPatientDetailsUpdated>(patientsListHashMap.values()));
            isInitialLoading = false;
            mActivity.hideLoading();
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void formHashMapAndRefreshNew(ArrayList<RegisteredPatientDetailsNew> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (RegisteredPatientDetailsNew patientDetails :
                    responseList) {
                patientDetailsNewLinkedHashMap.put(patientDetails.getUserId(), patientDetails);
            }
        }
        notifyAdapterUpdate(new ArrayList<RegisteredPatientDetailsNew>(patientDetailsNewLinkedHashMap.values()));
    }

    private void notifyAdapterUpdate(List<RegisteredPatientDetailsNew> patientsList) {
        LogUtils.LOGD(TAG, "Success notifyAdapter :" + patientsList.size());
        if (!Util.isNullOrEmptyList(patientsList)) {
            lvContacts.setVisibility(View.VISIBLE);
            tvNoPatients.setVisibility(View.GONE);
        } else {
            lvContacts.setVisibility(View.GONE);
            tvNoPatients.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData(patientsList);
        adapter.notifyDataSetChanged();
    }

    private void formHashMapAndRefresh(ArrayList<RegisteredPatientDetailsUpdated> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (RegisteredPatientDetailsUpdated patientDetails :
                    responseList) {
                patientsListHashMap.put(patientDetails.getUserId(), patientDetails);
            }
        }
        notifyAdapter(new ArrayList<RegisteredPatientDetailsUpdated>(patientsListHashMap.values()));
    }

    public void getListFromLocal(boolean initialLoading) {
        this.isInitialLoading = initialLoading;
        isEditTextSearching = false;
        if (isInitialLoading) {
            PAGE_NUMBER = 0;
            progressLoading.setVisibility(View.GONE);
        } else {
            progressLoading.setVisibility(View.VISIBLE);
        }
        asynTaskGetPatients = new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PATIENTS_NEW, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        resetListAndPagingAttributes();
        getListFromLocal(false);
    }

    @Override
    public boolean isPositionVisible(int position) {
        return false;
    }

    @Override
    public void onDeletePatientClicked(RegisteredPatientDetailsUpdated selecetdPatient) {

    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            LogUtils.LOGD(TAG, "onResume " + receiversRegistered);
            //            broadcast to get contacts list from Local
            IntentFilter contactsListLocalIntentFilter = new IntentFilter();
            contactsListLocalIntentFilter.addAction(INTENT_REFRESH_CONTACTS_LIST_FROM_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(contactsListFromLocalReceiver, contactsListLocalIntentFilter);

            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(contactsListFromLocalReceiver);
        LogUtils.LOGD(TAG, "onDestroy " + receiversRegistered);
    }

    BroadcastReceiver contactsListFromLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            clearSearchEditText();
            resetListAndPagingAttributes();
            getListFromLocal(false);
        }
    };

    private void resetListAndPagingAttributes() {
        if (patientDetailsNewLinkedHashMap != null)
            patientDetailsNewLinkedHashMap.clear();
        isResetList = true;
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
    }

    @Override
    public void afterTextChange(View v, String s) {
        isEditTextSearching = true;
        String searchedText = String.valueOf(s).toLowerCase(Locale.ENGLISH).trim();
        if (!lastTextSearched.equalsIgnoreCase(searchedText)) {
            resetListAndPagingAttributes();
            LogUtils.LOGD(TAG, "TextChange afterTextChange");
            isLoadingFromSearch = true;
            getListFromLocal(false);
        }
        lastTextSearched = searchedText;
    }

}
