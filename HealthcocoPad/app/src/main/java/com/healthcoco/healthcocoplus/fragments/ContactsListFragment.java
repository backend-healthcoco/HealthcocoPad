package com.healthcoco.healthcocoplus.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.adapter.ContactsListAdapter;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.DoctorProfile;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocoplus.enums.FilterItemType;
import com.healthcoco.healthcocoplus.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.listeners.ContactsItemOptionsListener;
import com.healthcoco.healthcocoplus.listeners.LoadMorePageListener;
import com.healthcoco.healthcocoplus.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.views.GridViewLoadMore;
import com.healthcoco.healthcocoplus.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Shreshtha on 31-01-2017.
 */
public class ContactsListFragment extends HealthCocoFragment implements LoadMorePageListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, TextWatcher, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised, ContactsItemOptionsListener {
    public static final String TAG_IS_IN_HOME_ACTIVITY = "isInHomeActivity";
    public static final String INTENT_REFRESH_CONTACTS_LIST_FROM_SERVER = "com.healthcoco.healthcocoplus.fragments.ContactsListFragment.REFRESH_CONTACTS_LIST_FROM_SERVER";
    private boolean receiversRegistered = false;
    private boolean isInHomeActivity = true;
    private User user;
    private ProgressBar progressLoading;
    private GridViewLoadMore gvContacts;
    private TextView tvNoPatients;
    private FloatingActionButton btAddNewPatient;
    private SwipeRefreshLayout swipeRefreshLayout;
    private Button btAdvanceSearch;
    private boolean isEditTextSearching;
    private FilterItemType filterType;
    private String lastTextSearched;
    private LinearLayout child_edit_search;
    private LinearLayout parent_edit_search;
    //variables need for pagination
    public static final int MAX_SIZE = 10;
    private static final String TAG_RECEIVERS_REGISTERED = "tagReceiversRegistered";
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private int currentPageNumber;
    private boolean isInitialLoading = true;
    private AsyncTask<VolleyResponseBean, VolleyResponseBean, VolleyResponseBean> asynTaskGetPatients;
    private ContactsListAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contacts_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState != null) {
            LogUtils.LOGD(TAG, "onSaveInstanceState " + receiversRegistered);
            outState.putBoolean(TAG_RECEIVERS_REGISTERED, receiversRegistered);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            receiversRegistered = savedInstanceState.getBoolean(TAG_RECEIVERS_REGISTERED);
            LogUtils.LOGD(TAG, "onActivityCreated " + receiversRegistered);
        }

        Intent intent = mActivity.getIntent();
        isInHomeActivity = intent.getBooleanExtra(TAG_IS_IN_HOME_ACTIVITY, true);

        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            init();
            syncDoctorProfile();
            getListFromLocal(true, 0);
        }
    }

    private void syncDoctorProfile() {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getDoctorProfile(DoctorProfile.class, user.getUniqueId(), null, null, this, this);
    }

    public void getListFromLocal(boolean initialLoading, int pageNum) {
        this.isInitialLoading = initialLoading;
        isEditTextSearching = false;
        this.currentPageNumber = pageNum;
        if (isInitialLoading) {
            filterType = FilterItemType.ALL_PATIENTS;
            mActivity.showLoading(false);
            this.currentPageNumber = 0;
            progressLoading.setVisibility(View.GONE);
        } else {
            progressLoading.setVisibility(View.VISIBLE);
        }

        LocalBackgroundTaskType localBackgroundTaskType = LocalBackgroundTaskType.GET_PATIENTS;
        if (filterType != null) {
            localBackgroundTaskType = filterType.getLocalBackgroundTaskType();
//            refreshFilterItemSelection(filterType);
        }
        asynTaskGetPatients = new LocalDataBackgroundtaskOptimised(mActivity, localBackgroundTaskType, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
    }

    private void initAdapter() {
        adapter = new ContactsListAdapter(mActivity, this);
        gvContacts.setAdapter(adapter);
    }

    @Override
    public void initViews() {
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        gvContacts = (GridViewLoadMore) view.findViewById(R.id.gv_contacts);
        tvNoPatients = (TextView) view.findViewById(R.id.tv_no_patients);
        btAddNewPatient = (FloatingActionButton) view.findViewById(R.id.bt_add);
        btAdvanceSearch = (Button) view.findViewById(R.id.bt_advance_search);
        child_edit_search = (LinearLayout) view.findViewById(R.id.child_edit_search);
        parent_edit_search = (LinearLayout) view.findViewById(R.id.parent_edit_search);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setColorSchemeResources(R.color.blue_action_bar);

        if (!isInHomeActivity) {
            initEditSearchView(R.string.name_mobile_number, this, true);
            btAddNewPatient.setVisibility(View.GONE);
//            btAddNewPatient.setVisibility(View.GONE);
        } else {
            initEditSearchView(R.string.name_mobile_number, this, false);
//            btAddNewPatient.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void initListeners() {
        gvContacts.setLoadMoreListener(this);
        gvContacts.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        btAddNewPatient.setOnClickListener(this);
        btAdvanceSearch.setOnClickListener(this);
//        child_edit_search.setOnClickListener(this);
    }

    @Override
    public void loadMore() {

    }

    @Override
    public boolean isEndOfListAchieved() {
        return false;
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_add:
                if (!isInHomeActivity) {
//                    openAddUpdateNameDialogFragment(null, AddUpdateNameDialogType.ADD_PATIENT_MOBILE_NUMBER, this, user, "", 0);
                } else {
//                    openPatientMobileNumberDialogFragment();
//                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.bt_advance_search:
                if (child_edit_search.getVisibility() == View.GONE && parent_edit_search.getVisibility() == View.VISIBLE) {
                    child_edit_search.setVisibility(View.VISIBLE);
                    parent_edit_search.setVisibility(View.GONE);
                } else {

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        isEditTextSearching = true;
//        filterType = FilterItemType.SEARCH_PATIENT;
//        String searchedText = String.valueOf(s).toLowerCase(Locale.ENGLISH).trim();
//        if (!lastTextSearched.equalsIgnoreCase(searchedText)) {
////            resetListAndPagingAttributes();
////            cancelPreviuosPagingRequests();
//            LogUtils.LOGD(TAG, "TextChange afterTextChange");
//            getListFromLocal(false, PAGE_NUMBER);
//        }
//        lastTextSearched = searchedText;
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        if (volleyResponseBean != null && volleyResponseBean.getWebServiceType() != null) {
            switch (volleyResponseBean.getWebServiceType()) {
                case GET_DOCTOR_PROFILE:
                    init();
                    return;
            }
        }
        mActivity.hideLoading();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        if (webServiceType != null) {
            switch (webServiceType) {
                case GET_DOCTOR_PROFILE:
                    init();
                    return;
            }
        }
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case GET_DOCTOR_PROFILE:
                    if (response.getData() != null && response.getData() instanceof DoctorProfile && !response.isDataFromLocal()) {
                        MenuDrawerFragment menuFragment = (MenuDrawerFragment) mActivity.getSupportFragmentManager().findFragmentByTag(MenuDrawerFragment.class.getSimpleName());
                        if (menuFragment != null) {
                            menuFragment.initData((DoctorProfile) response.getData());
                        }
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DOCTOR_PROFILE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    }
//                    mActivity.initGCM();
                    return;
            }

//    public FilterItemType getSelectedFilterType() {
//        return filterType;
//    }
        }
        if (isInitialLoading) {
            syncDoctorProfile();
//            mActivity.refreshMenuFragment(user);
//            refreshMenuFragmentContactsCount();
        }
        isInitialLoading = false;
        progressLoading.setVisibility(View.GONE);
//        notifyAdapter(new ArrayList<RegisteredPatientDetailsUpdated>(patientsListHashMap.values()));
        mActivity.hideLoading();
        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_DOCTOR_PROFILE:
                if (response.getData() != null)
                    LocalDataServiceImpl.getInstance(mApp).
                            addDoctorProfile((DoctorProfile) response.getData());
                break;
            case ADD_PATIENTS:
//                LocalDataServiceImpl.getInstance(mApp).
//                        addPatientsList((ArrayList<RegisteredPatientDetailsUpdated>) (ArrayList<?>) response.getDataList());
//                filterList(filterType);
                break;
            case GET_PATIENTS:
            case SEARCH_PATIENTS:
//                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
//                        .getPatientsListPageWise(WebServiceType.GET_CONTACTS, user.getUniqueId(),
//                                user.getForeignHospitalId(), user.getForeignLocationId(),
//                                 currentPageNumber, MAX_SIZE, null, null);
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getSearchedPatientsListPageWise(WebServiceType.GET_CONTACTS, user.getUniqueId(),
                                user.getForeignHospitalId(), user.getForeignLocationId(),
                                currentPageNumber, MAX_SIZE, getSearchEditTextValue(), null, null);
                break;
//            case SEARCH_PATIENTS:
//                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
//                        .getSearchedPatientsListPageWise(WebServiceType.GET_CONTACTS, user.getUniqueId(),
//                                user.getForeignHospitalId(), user.getForeignLocationId(), currentPageNumber, MAX_SIZE, getSearchEditTextValue(), null, null);
//            break;
//            case SORT_LIST_BY_GROUP:
//                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
//                        .getPatientsListWithGroup(WebServiceType.GET_CONTACTS, user.getUniqueId(),
//                                user.getForeignHospitalId(), user.getForeignLocationId(), selectedGroupId,
//                                false, currentPageNumber, MAX_SIZE, null, null);
//                break;
//            case SORT_LIST_BY_RECENTLY_ADDED:
//                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
//                        .getPatientsListByFilterType(WebServiceType.GET_CONTACTS, user.getUniqueId(),
//                                user.getForeignHospitalId(), user.getForeignLocationId(), filterType,
//                                false, currentPageNumber, MAX_SIZE, null, null);
//                break;
//            case CLEAR_PATIENTS:
//                LocalDataServiceImpl.getInstance(mApp).clearPatientsList();
//                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }


    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    //    /**
//     * exception will be caught if filter fragments views are not initialised yet
//     *
//     * @param filterType
//     */
//    private void refreshFilterItemSelection(FilterItemType filterType) {
//        try {
//            FilterFragment filterFragment = (FilterFragment) mFragmentManager.findFragmentByTag(FilterFragment.class.getSimpleName());
//            if (filterFragment != null)
//                filterFragment.setSelectedItem(filterType);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    public void setSelectedItem(FilterItemType filterItemType) {
//        clearPreviuosFilters(null);
//        switch (filterItemType) {
//            case RECENTLY_VISITED:
//                tvRecentlyVisited.setSelected(true);
//                break;
//            case RECENTLY_ADDED:
//                tvRecentlyAdded.setSelected(true);
//                break;
//            case ALL_PATIENTS:
//                tvAllPatients.setSelected(true);
//                break;
//            case MOST_VISITED:
//                tvMostVisited.setSelected(true);
//                break;
//        }
//    }
    public FilterItemType getSelectedFilterType() {
        return filterType;
    }

    @Override
    public void onAddToGroupClicked(RegisteredPatientDetailsUpdated selecetdPatient) {

    }

    @Override
    public void onCallClicked(RegisteredPatientDetailsUpdated selecetdPatient) {

    }

    @Override
    public boolean isPositionVisible(int position) {
        return false;
    }

    @Override
    public void onAddPrescriptionClicked(RegisteredPatientDetailsUpdated selecetdPatient) {

    }

    @Override
    public void onItemContactDetailClicked(RegisteredPatientDetailsUpdated selecetdPatient) {

    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return null;
    }

    @Override
    public boolean isInHomeActivity() {
        return false;
    }
}