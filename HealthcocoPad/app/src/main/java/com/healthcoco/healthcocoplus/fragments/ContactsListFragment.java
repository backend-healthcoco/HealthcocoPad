package com.healthcoco.healthcocoplus.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocoplus.activities.HomeActivity;
import com.healthcoco.healthcocoplus.adapter.ContactsListAdapter;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.DoctorProfile;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocoplus.enums.ChangeViewType;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.enums.FilterItemType;
import com.healthcoco.healthcocoplus.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocoplus.enums.LocalTabelType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.listeners.ContactsItemOptionsListener;
import com.healthcoco.healthcocoplus.listeners.LoadMorePageListener;
import com.healthcoco.healthcocoplus.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.views.GridViewLoadMore;
import com.healthcoco.healthcocoplus.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shreshtha on 31-01-2017.
 */
public class ContactsListFragment extends HealthCocoFragment implements
        LoadMorePageListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, TextWatcher,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised,
        ContactsItemOptionsListener {
    public static final String TAG_IS_IN_HOME_ACTIVITY = "isInHomeActivity";
    //required if contacts list is not in HomeScreen
    public static final String INTENT_FINISH_CONTACTS_LIST_SCREEN = "com.healthcoco.FINISH_CONTACTS_LIST_SCREEN";

    //required to receive an event when filter is selected
    public static final String INTENT_FILTER_TYPE = "com.healthcoco.FILTER_TYPE";
    public static final String INTENT_GET_CONTACT_LIST_LOCAL = "com.healthcoco.CONTACT_LIST_LOCAL";
    public static final String INTENT_REFRESH_CONTACTS_LIST_FROM_SERVER = "com.healthcoco.healthcocoplus.fragments.ContactsListFragment.REFRESH_CONTACTS_LIST_FROM_SERVER";
    //variables need for pagination
    public static final int MAX_SIZE = 10;
    private static final String TAG_RECEIVERS_REGISTERED = "tagReceiversRegistered";
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private int currentPageNumber;
    private boolean isInitialLoading = true;


    //other variables
    private ProgressBar progressLoading;
    private GridViewLoadMore gvContacts;
    private ListViewLoadMore lvContacts;
    private ContactsListAdapter adapter;
    private TextView tvNoPatients;
    private LinkedHashMap<String, RegisteredPatientDetailsUpdated> patientsListHashMap = new LinkedHashMap<>();
    private User user;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton btAddNewPatient;
    private AsyncTask<VolleyResponseBean, VolleyResponseBean, VolleyResponseBean> asynTaskGetPatients;
    private String selectedGroupId;
    private FilterItemType filterType;
    private String lastTextSearched = "";
    private boolean isResetList = true;
    private boolean receiversRegistered = false;
    private boolean isInHomeActivity = true;
    private boolean isEditTextSearching;
    private LinearLayout containerLv;
    private ChangeViewType changeViewType = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            LogUtils.LOGD(TAG, "onResume " + receiversRegistered);
            //receiver for filter refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_FILTER_TYPE);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(filterReceiver, filter);

            //receiver for contacts list refresh
            IntentFilter contactsListLocal = new IntentFilter();
            contactsListLocal.addAction(INTENT_GET_CONTACT_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(contactsListLocalReceiver, contactsListLocal);


//            broadcast to finish this activity
            IntentFilter finishContactIntentFilter = new IntentFilter();
            finishContactIntentFilter.addAction(INTENT_FINISH_CONTACTS_LIST_SCREEN);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(finishContactsListReceiver, finishContactIntentFilter);

            //            broadcast to get contacts list from server
            IntentFilter contactsListServerIntentFilter = new IntentFilter();
            contactsListServerIntentFilter.addAction(INTENT_REFRESH_CONTACTS_LIST_FROM_SERVER);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(contactsListFromServerReceiver, contactsListServerIntentFilter);

            receiversRegistered = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mActivity.hideLoading();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(filterReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(contactsListLocalReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(finishContactsListReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(contactsListFromServerReceiver);
        LogUtils.LOGD(TAG, "onDestroy " + receiversRegistered);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
    }

    public void getContactsList(boolean showLoading) {
        if (showLoading)
            mActivity.showLoading(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.REGISTERED_PATIENTS_DETAILS);
        WebDataServiceImpl.getInstance(mApp).getContactsList(RegisteredPatientDetailsUpdated.class, user.getUniqueId(),
                user.getForeignHospitalId(), user.getForeignLocationId(), latestUpdatedTime, this, this);
    }

    @Override
    public void initViews() {
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        gvContacts = (GridViewLoadMore) view.findViewById(R.id.gv_contacts);
        lvContacts = (ListViewLoadMore) view.findViewById(R.id.lv_contacts);
        containerLv = (LinearLayout) view.findViewById(R.id.container_lv);
        tvNoPatients = (TextView) view.findViewById(R.id.tv_no_patients);
        btAddNewPatient = (FloatingActionButton) view.findViewById(R.id.bt_add_patient);

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
        lvContacts.setLoadMoreListener(this);
        gvContacts.setSwipeRefreshLayout(swipeRefreshLayout);
        lvContacts.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        btAddNewPatient.setOnClickListener(this);
        mActivity.initChangeViewButton(this);
    }

    @Override
    public void onAddToGroupClicked(RegisteredPatientDetailsUpdated selecetdPatient) {
//        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
//        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.GROUPS.ordinal());
//        intent.putExtra(HealthCocoConstants.TAG_SELECTED_USER_ID, selecetdPatient.getUserId());
//        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST);
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onCallClicked(RegisteredPatientDetailsUpdated selecetdPatient) {
        if (!Util.isNullOrBlank(selecetdPatient.getMobileNumber()))
            mActivity.showCallConfirmationAlert(selecetdPatient.getMobileNumber());
        else
            Util.showToast(mActivity, R.string.no_mobile_number_found);
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean isPositionVisible(int position) {
        int firstVisiblePosition = gvContacts.getFirstVisiblePosition();
        int lastVisiblePosition = gvContacts.getLastVisiblePosition();
        if (position > firstVisiblePosition && position < lastVisiblePosition)
            return true;
        return false;
    }

    @Override
    public void onAddPrescriptionClicked(RegisteredPatientDetailsUpdated selecetdPatient) {
        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = selecetdPatient.getUserId();
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_NEW_PRESCRIPTION.ordinal());
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemContactDetailClicked(RegisteredPatientDetailsUpdated selecetdPatient) {
        if (selecetdPatient.getPatient() != null && !Util.isNullOrBlank(selecetdPatient.getPatient().getPatientId())) {
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = selecetdPatient.getUserId();
            if (isInHomeActivity()) {
                openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_DETAIL, null,
                        HealthCocoConstants.REQUEST_CODE_CONTACTS_DETAIL);
                adapter.notifyDataSetChanged();
            } else {
//                Util.sendBroadcast(mApp, BookAppointmentFragment.INTENT_REFRESH_SELECTED_PATIENT);
//                ((CommonOpenUpActivity) mActivity).setResult(HealthCocoConstants.RESULT_CODE_CONTACTS_LIST);
                ((CommonOpenUpActivity) mActivity).finish();
            }
        }
    }

    @Override
    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    @Override
    public boolean isInHomeActivity() {
        return isInHomeActivity;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                if (!isInHomeActivity) {
//                    openAddUpdateNameDialogFragment(null, AddUpdateNameDialogType.ADD_PATIENT_MOBILE_NUMBER, this, user, "", 0);
                } else {
                    openPatientMobileNumberDialogFragment();
                    adapter.notifyDataSetChanged();
                }
                break;
            case R.id.container_middle_action:
                if (containerLv.getVisibility() == View.GONE) {
                    changeViewType = ChangeViewType.LIST_VIEW;
                    adapter = new ContactsListAdapter(mActivity, this, changeViewType);
                    lvContacts.setAdapter(adapter);
                    notifyAdapter(new ArrayList<RegisteredPatientDetailsUpdated>(patientsListHashMap.values()));
                    gvContacts.setVisibility(View.GONE);
                    containerLv.setVisibility(View.VISIBLE);
                    ((HomeActivity) mActivity).disableFilterButton();
                } else if (containerLv.getVisibility() == View.VISIBLE) {
                    gvContacts.setVisibility(View.VISIBLE);
                    containerLv.setVisibility(View.GONE);
                    changeViewType = ChangeViewType.GRID_VIEW;
                    ((HomeActivity) mActivity).enableFilterButton();
                }
                break;
            default:
                break;
        }
    }

    private void openPatientMobileNumberDialogFragment() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_NUMBER_SEARCH.ordinal());
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST);
    }

    private void initAdapter() {
        changeViewType = ChangeViewType.GRID_VIEW;
        adapter = new ContactsListAdapter(mActivity, this, changeViewType);
        gvContacts.setAdapter(adapter);
    }

    private void notifyAdapter(List<RegisteredPatientDetailsUpdated> patientsList) {
        LogUtils.LOGD(TAG, "Success notifyAdapter :" + patientsList.size());
        if (!Util.isNullOrEmptyList(patientsList)) {
            tvNoPatients.setVisibility(View.GONE);
            if (containerLv.getVisibility() == View.VISIBLE) {
                gvContacts.setVisibility(View.GONE);
                changeViewType = ChangeViewType.LIST_VIEW;
            } else {
                gvContacts.setVisibility(View.VISIBLE);
                containerLv.setVisibility(View.GONE);
                changeViewType = ChangeViewType.GRID_VIEW;
            }
        } else {
            tvNoPatients.setVisibility(View.VISIBLE);
            gvContacts.setVisibility(View.GONE);
            containerLv.setVisibility(View.GONE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData(patientsList);
        adapter.notifyDataSetChanged();
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
                    mActivity.initGCM();
                    return;
                case GET_CONTACTS:
                    if (response.isDataFromLocal()) {
                        ArrayList<RegisteredPatientDetailsUpdated> responseList = (ArrayList<RegisteredPatientDetailsUpdated>) (ArrayList<?>) response
                                .getDataList();
                        formHashMapAndRefresh(responseList);
                        if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                            isEndOfListAchieved = true;
                        else isEndOfListAchieved = false;
                        if (isInitialLoading && !isEditTextSearching && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            getContactsList(true);
                            return;
                        }
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        ArrayList<RegisteredPatientDetailsUpdated> responseList = (ArrayList<RegisteredPatientDetailsUpdated>) (ArrayList<?>) response
                                .getDataList();
                        if (responseList.size() <= 10)
                            formHashMapAndRefresh(responseList);
                        response.setIsFromLocalAfterApiSuccess(true);
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PATIENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    break;
                default:
                    break;
            }
        }
        if (isInitialLoading) {
            syncDoctorProfile();
            mActivity.refreshMenuFragment(user);
            refreshMenuFragmentContactsCount();
        }
        isInitialLoading = false;
        progressLoading.setVisibility(View.GONE);
        notifyAdapter(new ArrayList<RegisteredPatientDetailsUpdated>(patientsListHashMap.values()));
        mActivity.hideLoading();
        swipeRefreshLayout.setRefreshing(false);
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

    private void resetListAndPagingAttributes() {
        if (patientsListHashMap != null)
            patientsListHashMap.clear();
        isResetList = true;
        PAGE_NUMBER = 0;
        currentPageNumber = 0;
        isEndOfListAchieved = false;
        gvContacts.resetPreLastPosition(0);
    }

    private void refreshMenuFragmentContactsCount() {
        MenuDrawerFragment menuFragment = (MenuDrawerFragment) mFragmentManager.findFragmentByTag(MenuDrawerFragment.class.getSimpleName());
        if (menuFragment != null) {
            menuFragment.refreshMenuItem();
        }
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
                LocalDataServiceImpl.getInstance(mApp).
                        addPatientsList((ArrayList<RegisteredPatientDetailsUpdated>) (ArrayList<?>) response.getDataList());
                filterList(filterType);
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
            case SORT_LIST_BY_GROUP:
//                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
//                        .getPatientsListWithGroup(WebServiceType.GET_CONTACTS, user.getUniqueId(),
//                                user.getForeignHospitalId(), user.getForeignLocationId(), selectedGroupId,
//                                false, currentPageNumber, MAX_SIZE, null, null);
                break;
            case SORT_LIST_BY_RECENTLY_ADDED:
//                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
//                        .getPatientsListByFilterType(WebServiceType.GET_CONTACTS, user.getUniqueId(),
//                                user.getForeignHospitalId(), user.getForeignLocationId(), filterType,
//                                false, currentPageNumber, MAX_SIZE, null, null);
                break;
            case CLEAR_PATIENTS:
                LocalDataServiceImpl.getInstance(mApp).clearPatientsList();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST) {
            LogUtils.LOGD(TAG, "Contacts List onActivityResult ");
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_PRESCIPTION) {
                openCommonOpenUpActivity(CommonOpenUpFragmentType.CONTACTS_DETAIL, null,
                        HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST);
            } else if (resultCode == HealthCocoConstants.RESULT_CODE_GROUPS_LIST) {
                filterList(filterType);
            } else if (resultCode == HealthCocoConstants.RESULT_CODE_SEARCH_NUMBER_RESULTS) {
                mActivity.finish();
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        isEditTextSearching = true;
        filterType = FilterItemType.SEARCH_PATIENT;
        String searchedText = String.valueOf(s).toLowerCase(Locale.ENGLISH).trim();
        if (!lastTextSearched.equalsIgnoreCase(searchedText)) {
            resetListAndPagingAttributes();
            cancelPreviuosPagingRequests();
            LogUtils.LOGD(TAG, "TextChange afterTextChange");
            getListFromLocal(false, PAGE_NUMBER);
        }
        lastTextSearched = searchedText;
    }

    @Override
    public void onRefresh() {
        getContactsList(false);
//        mActivity.syncGroups(user);
    }

    BroadcastReceiver filterReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                int filterItemTypeOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_ORDINAL, 0);
                final FilterItemType itemType = FilterItemType.values()[filterItemTypeOrdinal];
                if (itemType != null) {
                    if (itemType == FilterItemType.REFRESH_CONTACTS) {
                        getContactsList(true);
                    } else {
                        sortList(intent, itemType);
                    }
                }

            }
        }
    };
    BroadcastReceiver contactsListLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            resetListAndPagingAttributes();
            getListFromLocal(false, 0);
        }
    };
    BroadcastReceiver contactsListFromServerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
            if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                clearSearchEditText();
                user = doctor.getUser();
                resetListAndPagingAttributes();
                getListFromLocal(true, 0);
            }
        }
    };
    BroadcastReceiver finishContactsListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            mActivity.finish();
        }
    };

    private void sortList(Intent intent, FilterItemType itemType) {
        selectedGroupId = null;
        switch (itemType) {
            case ALL_PATIENTS:
            case RECENTLY_VISITED:
                filterType = FilterItemType.RECENTLY_VISITED;
                break;
            case MOST_VISITED:
                filterType = FilterItemType.MOST_VISITED;
                break;
            case RECENTLY_ADDED:
                filterType = FilterItemType.RECENTLY_ADDED;
                break;
            case GROUP_ITEM:
                if (intent != null) {
                    String groupId = intent.getStringExtra(HealthCocoConstants.TAG_GROUP_ID);
                    selectedGroupId = groupId;
                }
        }
        filterList(itemType);
    }

    private void filterList(FilterItemType itemType) {
        resetListAndPagingAttributes();
        this.filterType = itemType;
        switch (itemType) {
            case ALL_PATIENTS:
                break;
            case RECENTLY_VISITED:
                break;
            case MOST_VISITED:
                break;
            case RECENTLY_ADDED:
                filterType = FilterItemType.RECENTLY_ADDED;
                break;
            case GROUP_ITEM:
                if (Util.isNullOrBlank(selectedGroupId)) {
                    filterType = FilterItemType.ALL_PATIENTS;
                }
        }
        new LocalDataBackgroundtaskOptimised(mActivity, filterType.getLocalBackgroundTaskType(), this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved) {
            PAGE_NUMBER++;
            getListFromLocal(false, PAGE_NUMBER);
        }

    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
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
            refreshFilterItemSelection(filterType);
        }
        asynTaskGetPatients = new LocalDataBackgroundtaskOptimised(mActivity, localBackgroundTaskType, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * exception will be caught if filter fragments views are not initialised yet
     *
     * @param filterType
     */
    private void refreshFilterItemSelection(FilterItemType filterType) {
        try {
            FilterFragment filterFragment = (FilterFragment) mFragmentManager.findFragmentByTag(FilterFragment.class.getSimpleName());
            if (filterFragment != null)
                filterFragment.setSelectedItem(filterType);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelPreviuosPagingRequests() {
        if (asynTaskGetPatients != null && (asynTaskGetPatients.getStatus() == AsyncTask.Status.RUNNING || asynTaskGetPatients.getStatus() == AsyncTask.Status.PENDING)) {
            LogUtils.LOGD(TAG, "Cancelling previous asyncTask " + asynTaskGetPatients.getStatus());
            asynTaskGetPatients.cancel(true);
        }
    }

    public FilterItemType getSelectedFilterType() {
        return filterType;
    }
}