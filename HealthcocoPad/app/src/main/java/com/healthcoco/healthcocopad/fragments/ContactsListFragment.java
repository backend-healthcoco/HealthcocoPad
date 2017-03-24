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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.activities.HomeActivity;
import com.healthcoco.healthcocopad.adapter.ContactsListAdapter;
import com.healthcoco.healthcocopad.adapter.FilterGroupListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.GroupsListDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.PatientNumberSearchDialogFragment;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.ChangeViewType;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.FilterItemType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.ContactsItemOptionsListener;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.OnFilterItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;
import com.healthcoco.healthcocopad.views.FontAwesomeButton;
import com.healthcoco.healthcocopad.views.GridViewLoadMore;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shreshtha on 31-01-2017.
 */
public class ContactsListFragment extends HealthCocoFragment implements
        LoadMorePageListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, TextWatcher,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, CommonListDialogItemClickListener, LocalDoInBackgroundListenerOptimised,
        ContactsItemOptionsListener, View.OnTouchListener, OnFilterItemClickListener {
    public static final String TAG_IS_IN_HOME_ACTIVITY = "isInHomeActivity";
    //required if contacts list is not in HomeScreen
    public static final String INTENT_FINISH_CONTACTS_LIST_SCREEN = "com.healthcoco.FINISH_CONTACTS_LIST_SCREEN";

    //required to receive an event when filter is selected
    public static final String INTENT_FILTER_TYPE = "com.healthcoco.FILTER_TYPE";
    public static final String INTENT_GET_CONTACT_LIST_LOCAL = "com.healthcoco.CONTACT_LIST_LOCAL";
    public static final String INTENT_REFRESH_CONTACTS_LIST_FROM_SERVER = "com.healthcoco.healthcocopad.fragments.ContactsListFragment.REFRESH_CONTACTS_LIST_FROM_SERVER";
    //variables need for pagination
    public static final int MAX_SIZE = 12;
    private static final String TAG_RECEIVERS_REGISTERED = "tagReceiversRegistered";
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private int currentPageNumber;
    private boolean isInitialLoading = true;


    //other variables
    private ProgressBar progressLoading;
    private GridViewLoadMore gvContacts;
    private ListViewLoadMore lvContacts;
    private ContactsListAdapter contactsListAdapter;
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
    private LinearLayout containerFilterFragment;
    private ChangeViewType changeViewType = ChangeViewType.GRID_VIEW;
    //For Filter Layout
    public static final String INTENT_REFRESH_FRAGMENT = "com.healthcoco.FILTER_TYPE";
    private ArrayList<UserGroups> groupsList;
    private ListViewLoadMore lvGroups;
    private ImageButton btAddNewGroup;
    private TextView tvNoGroups;
    private FilterGroupListAdapter filterAdapter;
    private TextView tvAllPatients;
    private TextView tvRecentlyVisited;
    private TextView tvMostVisited;
    private TextView tvRecentlyAdded;
    private SwipeRefreshLayout swipeRefreshFilterLayout;
    private String selectedFilterTitle;
    private FontAwesomeButton btAdvanceSearch;
    private LinearLayout parentEditSearch;
    private LinearLayout childEditSearch;
    private ImageButton btCancel;
    private CustomAutoCompleteTextView editSearchDropdown;
    private EditText editSearch;
    private TextView btSearch;
    private ArrayList<Object> ADVANCED_SEARCH_OPTION = new ArrayList<Object>() {
        {
            add("Patient Name");
            add("Patient Id");
            add("Mobile Number");
            add("Email");
            add("Blood Group");
            add("Profession");
            add("Reference");
        }
    };

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
            if (user != null && !Util.isNullOrBlank(user.getUniqueId())) {
                setSelectedItem(FilterItemType.ALL_PATIENTS);
            }
            getGroupsListFromLocal();
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
        initAdapters();
        initAutoTvAdapter(editSearchDropdown, AutoCompleteTextViewType.ADVANCE_SEARCH_OPTION, ADVANCED_SEARCH_OPTION);
    }

    private void initAutoTvAdapter(AutoCompleteTextView autoCompleteTextView, final AutoCompleteTextViewType autoCompleteTextViewType, ArrayList<Object> list) {
        try {
            if (!Util.isNullOrEmptyList(list)) {
                final AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.spinner_drop_down_item_grey_background,
                        list, autoCompleteTextViewType);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setDropDownAnchor(autoCompleteTextView.getId());
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (autoCompleteTextViewType) {
                            case ADVANCE_SEARCH_OPTION:
                                onDialogItemClicked(CommonListDialogType.ADVANCED_SEARCH_OPTION, adapter.getSelectedObject(position));
                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDialogItemClicked(CommonListDialogType commonListDialogType, Object object) {
        switch (commonListDialogType) {
            case ADVANCED_SEARCH_OPTION:
                if (object instanceof String) {
                    String text = (String) object;
//                    initEditSearchView(text, this, true);
//                editSearchDropdown.setText(bloodGroup.getBloodGroup());
                }
                break;
        }
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
        containerFilterFragment = (LinearLayout) view.findViewById(R.id.container_filter_fragment);
        tvNoPatients = (TextView) view.findViewById(R.id.tv_no_patients);
        btAddNewPatient = (FloatingActionButton) view.findViewById(R.id.bt_add_patient);
        btAdvanceSearch = (FontAwesomeButton) view.findViewById(R.id.bt_advance_search);
        parentEditSearch = (LinearLayout) view.findViewById(R.id.parent_edit_search);
        childEditSearch = (LinearLayout) view.findViewById(R.id.child_edit_search);
        btCancel = (ImageButton) view.findViewById(R.id.bt_cancel);
        editSearchDropdown = (CustomAutoCompleteTextView) view.findViewById(R.id.edit_search_dropdown);
        editSearch = (EditText) view.findViewById(R.id.edit_search);
        btSearch = (TextView) view.findViewById(R.id.bt_search);
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

        //For Filter Layout
        lvGroups = (ListViewLoadMore) view.findViewById(R.id.lv_groups);
        tvNoGroups = (TextView) view.findViewById(R.id.tv_no_groups);
        btAddNewGroup = (ImageButton) view.findViewById(R.id.bt_add_to_group);
        tvAllPatients = (TextView) view.findViewById(R.id.tv_all_patients);
        tvRecentlyVisited = (TextView) view.findViewById(R.id.tv_recently_visited);
        tvMostVisited = (TextView) view.findViewById(R.id.tv_most_visited);
        tvRecentlyAdded = (TextView) view.findViewById(R.id.tv_recently_added);
        swipeRefreshFilterLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshFilterLayout.setColorSchemeResources(R.color.blue_action_bar);
        childEditSearch.setVisibility(View.GONE);
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

        //For Filter Layout
        swipeRefreshFilterLayout.setOnRefreshListener(this);
        lvGroups.setSwipeRefreshLayout(swipeRefreshLayout);
        btAddNewGroup.setOnClickListener(this);
        tvAllPatients.setOnClickListener(this);
        tvRecentlyVisited.setOnClickListener(this);
        tvMostVisited.setOnClickListener(this);
        tvRecentlyAdded.setOnClickListener(this);
        lvGroups.setOnTouchListener(this);
        btAdvanceSearch.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btSearch.setOnClickListener(this);
    }

    private void initFilterGroupAdapter() {
        filterAdapter = new FilterGroupListAdapter(mActivity, this);
        lvGroups.setAdapter(filterAdapter);
    }

    @Override
    public void onAddToGroupClicked(RegisteredPatientDetailsUpdated selecetdPatient) {
        openDialogFragment(new GroupsListDialogFragment(), HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST, CommonOpenUpFragmentType.GROUPS, selecetdPatient.getUserId());
        contactsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCallClicked(RegisteredPatientDetailsUpdated selecetdPatient) {
        if (!Util.isNullOrBlank(selecetdPatient.getMobileNumber()))
            mActivity.showCallConfirmationAlert(selecetdPatient.getMobileNumber());
        else
            Util.showToast(mActivity, R.string.no_mobile_number_found);
        contactsListAdapter.notifyDataSetChanged();
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
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_DETAIL.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_PRESCRIPTION.ordinal());
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST);
        contactsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemContactDetailClicked(RegisteredPatientDetailsUpdated selecetdPatient) {
        if (selecetdPatient.getPatient() != null && !Util.isNullOrBlank(selecetdPatient.getPatient().getPatientId())) {
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = selecetdPatient.getUserId();
            if (isInHomeActivity()) {
                openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_DETAIL, null,
                        HealthCocoConstants.REQUEST_CODE_CONTACTS_DETAIL);
                contactsListAdapter.notifyDataSetChanged();
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
    public void onEditClicked(RegisteredPatientDetailsUpdated patientDetailsUpdated) {
        Intent intent = new Intent();
        intent.putExtra(HealthCocoConstants.TAG_UNIQUE_ID, patientDetailsUpdated.getForeignPatientId());
        intent.putExtra(HealthCocoConstants.TAG_MOBILE_NUMBER, patientDetailsUpdated.getMobileNumber());
        intent.putExtra(HealthCocoConstants.TAG_IS_EDIT_PATIENT, true);
        mActivity.openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_REGISTRATION, intent,
                HealthCocoConstants.REQUEST_CODE_CONTACTS_DETAIL);
    }

    @Override
    public void onQueueClicked(RegisteredPatientDetailsUpdated selecetdPatient) {
//        mActivity.showLoading(false);
//        WebDataServiceImpl.getInstance(mApp).addPatientToQueue(WebServiceType.ADD_PATIENT_TO_QUEUE, RegisteredPatientDetailsUpdated.class, selecetdPatient, this, this);
    }

    @Override
    public ChangeViewType getChangedViewType() {
        return changeViewType;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_patient:
                if (!isInHomeActivity) {
                    mActivity.openAddUpdateNameDialogFragment(null, AddUpdateNameDialogType.ADD_PATIENT_MOBILE_NUMBER, this, user, "", 0);
                } else {
                    openPatientMobileNumberDialogFragment();
                    contactsListAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.container_middle_action:
                if (changeViewType == ChangeViewType.GRID_VIEW)
                    changeView(ChangeViewType.LIST_VIEW);
                else
                    changeView(ChangeViewType.GRID_VIEW);
//                if (lvContacts.getVisibility() == View.GONE) {
//                    changeViewType = ChangeViewType.LIST_VIEW;
//
//                } else if (containerLv.getVisibility() == View.VISIBLE) {
//                    gvContacts.setVisibility(View.VISIBLE);
//                    containerLv.setVisibility(View.GONE);
//                    changeViewType = ChangeViewType.GRID_VIEW;
//
//                }
                break;
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
            case R.id.bt_advance_search:
                parentEditSearch.setVisibility(View.GONE);
                childEditSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_cancel:
                childEditSearch.setVisibility(View.GONE);
                parentEditSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_search:
                break;
            default:
                break;
        }
    }

    private void changeView(ChangeViewType nextViewToChange) {
        this.changeViewType = nextViewToChange;
        lvContacts.setVisibility(View.GONE);
        gvContacts.setVisibility(View.GONE);
        containerFilterFragment.setVisibility(View.GONE);
        switch (nextViewToChange) {
            case LIST_VIEW:
//                contactsListAdapter = new ContactsListAdapter(mActivity, this, changeViewType);
//                lvContacts.setAdapter(contactsListAdapter);
                notifyAdapter(new ArrayList<RegisteredPatientDetailsUpdated>(patientsListHashMap.values()));
                lvContacts.setVisibility(View.VISIBLE);
                containerFilterFragment.setVisibility(View.VISIBLE);
                ((HomeActivity) mActivity).disableFilterButton();
                break;
            case GRID_VIEW:
                gvContacts.setVisibility(View.VISIBLE);
                ((HomeActivity) mActivity).enableFilterButton();
                break;
        }
    }

    private void openPatientMobileNumberDialogFragment() {
        PatientNumberSearchDialogFragment patientNumberSearchDialogFragment = new PatientNumberSearchDialogFragment();
        patientNumberSearchDialogFragment.setTargetFragment(this, HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST);
        patientNumberSearchDialogFragment.show(mFragmentManager, patientNumberSearchDialogFragment.getClass().getSimpleName());
    }

    private void initAdapters() {
        initFilterGroupAdapter();
    initContactsListAdapter();}

    private void initContactsListAdapter() {
        contactsListAdapter = new ContactsListAdapter(mActivity, this);
        gvContacts.setAdapter(contactsListAdapter);
        lvContacts.setAdapter(contactsListAdapter);
    }

    private void notifyAdapter(List<RegisteredPatientDetailsUpdated> patientsList) {
        View visibleView = getChangedView(changeViewType);
        LogUtils.LOGD(TAG, "Success notifyAdapter :" + patientsList.size());
        if (!Util.isNullOrEmptyList(patientsList)) {
            tvNoPatients.setVisibility(View.GONE);
            visibleView.setVisibility(View.VISIBLE);
        } else {
            tvNoPatients.setVisibility(View.VISIBLE);
            visibleView.setVisibility(View.GONE);
        }
        progressLoading.setVisibility(View.GONE);
        contactsListAdapter.setListData(patientsList);
        contactsListAdapter.notifyDataSetChanged();
    }

    private View getChangedView(ChangeViewType changeViewType) {
        switch (changeViewType) {
            case LIST_VIEW:
                return lvContacts;
            default:
                return gvContacts;
        }
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
                case GET_GROUPS:
                    if (response.isDataFromLocal()) {
                        groupsList = (ArrayList<UserGroups>) (ArrayList<?>) response
                                .getDataList();
                        if (!Util.isNullOrEmptyList(groupsList)) {
                            LogUtils.LOGD(TAG, "Success onResponse list Size in page " + groupsList.size());
                        }
                        notifyFilterAdapter(groupsList);
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_GROUPS_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    swipeRefreshFilterLayout.setRefreshing(false);
                    break;
                case ADD_PATIENT_TO_QUEUE:
                    if (response.getData() != null && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                        RegisteredPatientDetailsUpdated selecetdPatient = (RegisteredPatientDetailsUpdated) response.getData();
                        mActivity.showAddedToQueueAlert(selecetdPatient.getFirstName());
                        contactsListAdapter.notifyDataSetChanged();
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
        } else if (requestCode == HealthCocoConstants.REQUEST_CODE_GROUPS_LIST) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_GROUP) {
                getGroupsListFromLocal();
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
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null)
            user = doctor.getUser();
        //Get groupsList
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.USER_GROUP);
        WebDataServiceImpl.getInstance(mApp).getGroupsList(WebServiceType.GET_GROUPS, UserGroups.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), latestUpdatedTime, null, this, this);
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

    //For Filter Layout
    public void getGroupsListFromLocal() {
        notifyFilterAdapter(null);
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null)
            user = doctor.getUser();
        LocalDataServiceImpl.getInstance(mApp).getUserGroups(WebServiceType.GET_GROUPS, null, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
    }

    private void notifyFilterAdapter(ArrayList<UserGroups> list) {
        if (!Util.isNullOrEmptyList(list)) {
            Collections.sort(list, ComparatorUtil.groupDateComparator);
            lvGroups.setVisibility(View.VISIBLE);
            tvNoGroups.setVisibility(View.GONE);
            filterAdapter.setListData(list);
            filterAdapter.notifyDataSetChanged();
        } else {
            lvGroups.setVisibility(View.GONE);
            tvNoGroups.setVisibility(View.VISIBLE);
        }
    }

    private void refreshHomeScreenTitle(String title) {
        this.selectedFilterTitle = title;
        ((HomeActivity) mActivity).setActionbarTitle(title);
    }

    public void setSelectedItem(FilterItemType filterItemType) {
        clearPreviuosFilters(null);
        switch (filterItemType) {
            case RECENTLY_VISITED:
                tvRecentlyVisited.setSelected(true);
                break;
            case RECENTLY_ADDED:
                tvRecentlyAdded.setSelected(true);
                break;
            case ALL_PATIENTS:
                tvAllPatients.setSelected(true);
                break;
            case MOST_VISITED:
                tvMostVisited.setSelected(true);
                break;
        }
    }

    private void clearPreviuosFilters(String groupId) {
        try {
            tvAllPatients.setSelected(false);
            tvRecentlyVisited.setSelected(false);
            tvMostVisited.setSelected(false);
            tvRecentlyAdded.setSelected(false);
            filterAdapter.setSelectedGroupId(groupId);
            filterAdapter.notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onGroupItemClicked(UserGroups group) {
        int selectedGroupPosition = groupsList.indexOf(group);
        LogUtils.LOGD(TAG, "Selected Group Position " + selectedGroupPosition);
        clearPreviuosFilters(group.getUniqueId());
        senBroadCastToContactsFragment(FilterItemType.GROUP_ITEM, group.getUniqueId());
        refreshHomeScreenTitle(group.getName());
    }

    private void senBroadCastToContactsFragment(FilterItemType itemType, Object intentData) {
        try {
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
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                disableDrawer(true);
                break;
            case MotionEvent.ACTION_UP:
                disableDrawer(false);
                break;

        }
        return false;
    }

    private void disableDrawer(boolean disable) {
        ((HomeActivity) mActivity).disableRightDrawer(disable);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_GROUPS_LIST:
                LocalDataServiceImpl.getInstance(mApp).addUserGroupsList((ArrayList<UserGroups>) (ArrayList<?>) response
                        .getDataList());
            case GET_GROUPS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getUserGroups(WebServiceType.GET_GROUPS, null, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), null, null);
                break;
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
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getSearchedPatientsListPageWise(WebServiceType.GET_CONTACTS, user.getUniqueId(),
                                user.getForeignHospitalId(), user.getForeignLocationId(),
                                currentPageNumber, MAX_SIZE, getSearchEditTextValue(), null, null);
                break;
            case SORT_LIST_BY_GROUP:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getPatientsListWithGroup(WebServiceType.GET_CONTACTS, user.getUniqueId(),
                                user.getForeignHospitalId(), user.getForeignLocationId(), selectedGroupId,
                                false, currentPageNumber, MAX_SIZE, null, null);
                break;
            case SORT_LIST_BY_RECENTLY_ADDED:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getPatientsListByFilterType(WebServiceType.GET_CONTACTS, user.getUniqueId(),
                                user.getForeignHospitalId(), user.getForeignLocationId(), filterType,
                                false, currentPageNumber, MAX_SIZE, null, null);
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

    public String getSelectedFilterTitle() {
        return selectedFilterTitle;
    }
}