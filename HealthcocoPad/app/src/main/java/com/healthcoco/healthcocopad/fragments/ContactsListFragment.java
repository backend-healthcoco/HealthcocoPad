package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Profession;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.GroupsListDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.PatientNumberSearchDialogFragment;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.AdvanceSearchOptionsType;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.ChangeViewType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.FilterItemType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.RecordType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.ContactsItemOptionsListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;
import com.healthcoco.healthcocopad.views.FontAwesomeButton;
import com.healthcoco.healthcocopad.views.GridViewLoadMore;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shreshtha on 31-01-2017.
 */
public class ContactsListFragment extends HealthCocoFragment implements
        LoadMorePageListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised,
        ContactsItemOptionsListener, HealthcocoTextWatcherListener {
    public static final String TAG_IS_IN_HOME_ACTIVITY = "isInHomeActivity";
    //required if contacts list is not in HomeScreen
    public static final String INTENT_FINISH_CONTACTS_LIST_SCREEN = "com.healthcoco.FINISH_CONTACTS_LIST_SCREEN";

    //required to receive an event when filter is selected
    public static final String INTENT_FILTER_TYPE = "com.healthcoco.FILTER_TYPE";
    public static final String INTENT_GET_CONTACT_LIST_LOCAL = "com.healthcoco.CONTACT_LIST_LOCAL";
    public static final String INTENT_REFRESH_CONTACTS_LIST_FROM_SERVER = "com.healthcoco.healthcocopad.fragments.ContactsListFragment.REFRESH_CONTACTS_LIST_FROM_SERVER";
    public static final String INTENT_REFRESH_GROUPS_LIST_FROM_SERVER = "com.healthcoco.healthcocopad.fragments.ContactsListFragment.REFRESH_GROUPS_LIST_FROM_SERVER";

    //variables need for pagination
    public static final int MAX_SIZE = 16;
    private static final String TAG_RECEIVERS_REGISTERED = "tagReceiversRegistered";
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private boolean isInitialLoading = true;
    private static Integer REQUEST_CODE_CONTACTS_DETAIL = 101;
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
    private boolean receiversRegistered = false;
    private boolean isInHomeActivity = true;
    private boolean isEditTextSearching;
    private LinearLayout containerFilterFragment;
    private ChangeViewType changeViewType = ChangeViewType.GRID_VIEW;
    private ArrayList<UserGroups> groupsList;
    private FontAwesomeButton btAdvanceSearch;
    private LinearLayout parentEditSearch;
    private LinearLayout childEditSearch;
    private ImageButton btCancel;
    private CustomAutoCompleteTextView autotvSearchType;
    private EditText editSearch;
    private TextView btSearch;
    private boolean isOnLoadMore = false;
    private EditText editAdvanceSearchText;
    private CustomAutoCompleteTextView autoTvAdvanceSearchText;
    private AdvanceSearchOptionsType selectedSearchType;
    private List<Profession> professionsList;
    private List<Reference> referenceList;


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
        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapters();
        initAutoTvAdapter(autotvSearchType, AutoCompleteTextViewType.ADVANCE_SEARCH_OPTION, AdvanceSearchOptionsType.getSearchedptionsTypeValues());
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
        autotvSearchType = (CustomAutoCompleteTextView) view.findViewById(R.id.autotv_search_type);
        editSearch = (EditText) view.findViewById(R.id.edit_search);
        editSearch.setTag(AdvanceSearchOptionsType.GENERAL_SEARCH);
        editAdvanceSearchText = (EditText) view.findViewById(R.id.edit_advance_search_text);
        autoTvAdvanceSearchText = (CustomAutoCompleteTextView) view.findViewById(R.id.autotv_advance_search_text);
        btSearch = (TextView) view.findViewById(R.id.bt_search);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
//        swipeRefreshLayout.setColorSchemeResources(R.color.blue_action_bar);

        if (!isInHomeActivity) {
            initEditSearchView(R.string.name_mobile_number, new HealthcocoTextWatcher(editSearch, this), true);
            btAddNewPatient.setVisibility(View.GONE);
//            btAddNewPatient.setVisibility(View.GONE);
        } else {
            initEditSearchView(R.string.name_mobile_number, new HealthcocoTextWatcher(editSearch, this), false);
//            btAddNewPatient.setVisibility(View.VISIBLE);
        }

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
        editAdvanceSearchText.addTextChangedListener(new HealthcocoTextWatcher(editAdvanceSearchText, this));
        autoTvAdvanceSearchText.addTextChangedListener(new HealthcocoTextWatcher(autoTvAdvanceSearchText, this));

        //For Filter Layout
        btAdvanceSearch.setOnClickListener(this);
        btCancel.setOnClickListener(this);
        btSearch.setOnClickListener(this);
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
            //            broadcast to get groups list from server
            IntentFilter groupsListServerIntent = new IntentFilter();
            groupsListServerIntent.addAction(INTENT_REFRESH_GROUPS_LIST_FROM_SERVER);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshGroupsListFromServerReceiver, groupsListServerIntent);
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
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshGroupsListFromServerReceiver);
        LogUtils.LOGD(TAG, "onDestroy " + receiversRegistered);
    }

    private void initAutoTvAdapter(AutoCompleteTextView autoCompleteTextView, final AutoCompleteTextViewType autoCompleteTextViewType, List<Object> list) {
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
                        Object tag = view.getTag();
                        if (tag != null) {
                            LogUtils.LOGD(TAG, "ItemClicked " + tag);
                            if (tag instanceof AdvanceSearchOptionsType) {
                                handleSearchOptionTypeSelected((AdvanceSearchOptionsType) tag);
                            }
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleSearchOptionTypeSelected(AdvanceSearchOptionsType advanceSearchOptionsType) {
        editAdvanceSearchText.setTag(advanceSearchOptionsType);
        autoTvAdvanceSearchText.setTag(advanceSearchOptionsType);
        //clearing text on SearchType changed
        editAdvanceSearchText.setText("");
        autoTvAdvanceSearchText.setText("");
        //setting hint based on selectedSearchOptionType
        editAdvanceSearchText.setHint(advanceSearchOptionsType.getHintId());
        autoTvAdvanceSearchText.setHint(advanceSearchOptionsType.getHintId());
        switch (advanceSearchOptionsType) {
            case EMAIL:
            case PATIENT_ID:
            case MOBILE_NUMBER:
            case PATIENT_NAME:
                editAdvanceSearchText.setVisibility(View.VISIBLE);
                autoTvAdvanceSearchText.setVisibility(View.GONE);
                break;
            case REFERENCE:
            case PROFESSION:
            case BLOOD_GROUP:
                editAdvanceSearchText.setVisibility(View.GONE);
                autoTvAdvanceSearchText.setVisibility(View.VISIBLE);
                mActivity.showLoading(false);
                new LocalDataBackgroundtaskOptimised(mActivity, advanceSearchOptionsType.getLocalBackgroundTaskType(), this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    private void initFilterFragment() {
        FilterFragment filterFragment = new FilterFragment();
        FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container_filter_fragment, filterFragment, filterFragment.getClass().getSimpleName());
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onAddToGroupClicked(RegisteredPatientDetailsUpdated selecetdPatient) {
        GroupsListDialogFragment dialogFragment = new GroupsListDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(GroupsListDialogFragment.TAG_SELECTED_PATIENT_ID, Parcels.wrap(selecetdPatient.getUserId()));
        bundle.putParcelable(GroupsListDialogFragment.TAG_GROUP_IDS_LIST, Parcels.wrap(selecetdPatient.getGroupIds()));
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(this, REQUEST_CODE_CONTACTS_DETAIL);
        dialogFragment.show(mFragmentManager, dialogFragment.getClass().getSimpleName());
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
//        HealthCocoConstants.SELECTED_PATIENTS_USER_ID = selecetdPatient.getUserId();
//        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
//        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.PATIENT_DETAIL.ordinal());
//        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, PatientDetailTabType.PATIENT_DETAIL_PRESCRIPTION.ordinal());
//        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_CONTACTS_LIST);
//        contactsListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemContactDetailClicked(RegisteredPatientDetailsUpdated selecetdPatient) {
        if (selecetdPatient.getPatient() != null && !Util.isNullOrBlank(selecetdPatient.getPatient().getPatientId())) {
            HealthCocoConstants.SELECTED_PATIENTS_USER_ID = selecetdPatient.getUserId();
            if (isInHomeActivity()) {
                openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_DETAIL, null, REQUEST_CODE_CONTACTS_DETAIL);
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
                REQUEST_CODE_CONTACTS_DETAIL);
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
        hideKeyboard(view);
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
                mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_NEW_GROUP, AddUpdateNameDialogType.GROUPS, this, user, "", REQUEST_CODE_CONTACTS_DETAIL);
                break;
            case R.id.bt_advance_search:
                clearSearchResults();
                parentEditSearch.setVisibility(View.GONE);
                childEditSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_cancel:
                clearSearchResults();
                childEditSearch.setVisibility(View.GONE);
                parentEditSearch.setVisibility(View.VISIBLE);
                break;
            case R.id.bt_search:
                break;
            default:
                break;
        }
    }

    private void clearSearchResults() {
        hideKeyboard(view);
        editAdvanceSearchText.setTag(null);
        autoTvAdvanceSearchText.setTag(null);
//        resetListAndPagingAttributes();
        editSearch.setText("");
        autoTvAdvanceSearchText.setText("");
        autotvSearchType.setText("");
        editAdvanceSearchText.setText("");
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
        patientNumberSearchDialogFragment.setTargetFragment(this, REQUEST_CODE_CONTACTS_DETAIL);
        patientNumberSearchDialogFragment.show(mFragmentManager, patientNumberSearchDialogFragment.getClass().getSimpleName());
    }

    private void initAdapters() {
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
                case FRAGMENT_INITIALISATION:
                    if (user != null && !Util.isNullOrBlank(user.getUniqueId())
                            && !Util.isNullOrBlank(user.getForeignHospitalId())
                            && !Util.isNullOrBlank(user.getForeignLocationId())) {
                        initFilterFragment();
                        getListFromLocal(true);
                        return;
                    }
                    break;
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
                        response.setIsFromLocalAfterApiSuccess(true);
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PATIENTS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    isEditTextSearching = false;
                    isInitialLoading = false;
                    notifyAdapter(new ArrayList<RegisteredPatientDetailsUpdated>(patientsListHashMap.values()));
                    if (!isOnLoadMore) {
                        refreshMenuFragmentContactsCount();
                    }
                    break;
                case ADD_PATIENT_TO_QUEUE:
                    if (response.getData() != null && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                        RegisteredPatientDetailsUpdated selecetdPatient = (RegisteredPatientDetailsUpdated) response.getData();
                        mActivity.showAddedToQueueAlert(selecetdPatient.getFirstName());
                        contactsListAdapter.notifyDataSetChanged();
                    }
                    break;
                case GET_PROFESSION:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        initAutoTvAdapter(autoTvAdvanceSearchText, AutoCompleteTextViewType.PROFESSION, response.getDataList());
                    }
                    break;
                case GET_REFERENCE:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        initAutoTvAdapter(autoTvAdvanceSearchText, AutoCompleteTextViewType.REFERENCE, response.getDataList());
                    }
                    break;
                case GET_HARDCODED_BLOOD_GROUPS:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        initAutoTvAdapter(autoTvAdvanceSearchText, AutoCompleteTextViewType.BLOOD_GROUP, response.getDataList());
                    }
                    break;
                case UPDATE_PATIENT:
                    filterList(filterType);
                    break;
                case GET_GROUPS:
                    if (!response.isDataFromLocal() && !Util.isNullOrEmptyList(response.getDataList())) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_GROUPS_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    Util.sendBroadcast(mApp, FilterFragment.INTENT_REFRESH_GROUPS_LIST_LOCAL);
                    break;
                default:
                    break;
            }
        }
        progressLoading.setVisibility(View.GONE);
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
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
        isEditTextSearching = false;
        gvContacts.resetPreLastPosition(0);
        notifyAdapter(new ArrayList<RegisteredPatientDetailsUpdated>(patientsListHashMap.values()));
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
        if (requestCode == REQUEST_CODE_CONTACTS_DETAIL) {
            LogUtils.LOGD(TAG, "Contacts List onActivityResult ");
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_PRESCIPTION) {
                openCommonOpenUpActivity(CommonOpenUpFragmentType.CONTACTS_DETAIL, null, REQUEST_CODE_CONTACTS_DETAIL);
            } else if (resultCode == HealthCocoConstants.RESULT_CODE_GROUPS_LIST) {
                filterList(filterType);
            } else if (resultCode == HealthCocoConstants.RESULT_CODE_SEARCH_NUMBER_RESULTS) {
                mActivity.finish();
            }
        }
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
                onClick(btCancel);
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
            getListFromLocal(false);
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
                filterType = FilterItemType.ALL_PATIENTS;
                getListFromLocal(true);
            }
        }
    };
    BroadcastReceiver finishContactsListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            mActivity.finish();
        }
    };
    BroadcastReceiver refreshGroupsListFromServerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getGroupsListFromServer();
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
        getListFromLocal(false);
    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading && !isEditTextSearching) {
            isOnLoadMore = true;
            PAGE_NUMBER++;
            getListFromLocal(false);
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }

    public void getListFromLocal(boolean initialLoading) {
        this.isInitialLoading = initialLoading;
        if (isInitialLoading) {
            filterType = FilterItemType.ALL_PATIENTS;
            mActivity.showLoading(false);
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
            Intent intent = new Intent(FilterFragment.INTENT_FILTER_REFRESH_SELECTED_FILTER_TYPE);
            intent.putExtra(FilterFragment.TAG_ORDINAL, filterType.ordinal());
            intent.putExtra(FilterFragment.TAG_GROUP_ID, selectedGroupId);
            LocalBroadcastManager.getInstance(mApp.getApplicationContext()).sendBroadcast(intent);

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
            case ADD_GROUPS_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).addUserGroupsList((ArrayList<UserGroups>) (ArrayList<?>) response
                        .getDataList());
                break;
            case ADD_DOCTOR_PROFILE:
                if (response.getData() != null)
                    LocalDataServiceImpl.getInstance(mApp).
                            addDoctorProfile((DoctorProfile) response.getData());
                break;
            case ADD_PATIENTS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).
                        addPatientsList((ArrayList<RegisteredPatientDetailsUpdated>) (ArrayList<?>) response.getDataList());
            case GET_PATIENTS:
            case SORT_LIST_BY_RECENTLY_ADDED:
            case SEARCH_PATIENTS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getSearchedPatientsListPageWise(WebServiceType.GET_CONTACTS, user,
                                PAGE_NUMBER, MAX_SIZE, filterType, selectedSearchType, getSearchEditTextValue(selectedSearchType), null, null);
                break;
            case SORT_LIST_BY_GROUP:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getPatientsListWithGroup(WebServiceType.GET_CONTACTS, user, selectedGroupId,
                                false, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
//            case SORT_LIST_BY_RECENTLY_ADDED:
//                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
//                        .getPatientsListByFilterType(WebServiceType.GET_CONTACTS, user.getUniqueId(),
//                                user.getForeignHospitalId(), user.getForeignLocationId(), filterType,
//                                false, currentPageNumber, MAX_SIZE, null, null);
//            break;
            case CLEAR_PATIENTS:
                LocalDataServiceImpl.getInstance(mApp).clearPatientsList();
                break;
            case GET_REFERENCE_ACTIVATED_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getReferenceList(WebServiceType.GET_REFERENCE, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), BooleanTypeValues.FALSE, RecordType.BOTH, null, null);
                break;
            case GET_PROFESSION_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getProfessionList(WebServiceType.GET_PROFESSION, null, null);
                break;
            case GET_BLOOD_GROUP:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getHardcodedBloodGroupsList(null, null);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    private String getSearchEditTextValue(AdvanceSearchOptionsType selectedSearchType) {
        if (selectedSearchType != null)
            switch (selectedSearchType) {
                case GENERAL_SEARCH:
                    return getSearchEditTextValue();
                case PATIENT_NAME:
                case MOBILE_NUMBER:
                case EMAIL:
                case PATIENT_ID:
                    return Util.getValidatedValueOrNull(editAdvanceSearchText);
                case REFERENCE:
                case PROFESSION:
                case BLOOD_GROUP:
                    return Util.getValidatedValueOrNull(autoTvAdvanceSearchText);
            }
        return "";
    }

    @Override
    public void afterTextChange(View v, String s) {
        isEditTextSearching = true;
        Object tag = v.getTag();
        if (tag != null && tag instanceof AdvanceSearchOptionsType) {
            selectedSearchType = (AdvanceSearchOptionsType) tag;
            LogUtils.LOGD(TAG, "Tag : " + v.getTag());
            filterType = FilterItemType.SEARCH_PATIENT;
            String searchedText = String.valueOf(s).toLowerCase(Locale.ENGLISH).trim();
            if (!lastTextSearched.equalsIgnoreCase(searchedText)) {
                resetListAndPagingAttributes();
                cancelPreviuosPagingRequests();
                LogUtils.LOGD(TAG, "TextChange afterTextChange");
                getListFromLocal(false);
            }
            lastTextSearched = searchedText;
        }
    }

    private void getGroupsListFromServer() {
        //Get groupsList
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.USER_GROUP);
        WebDataServiceImpl.getInstance(mApp).getGroupsList(WebServiceType.GET_GROUPS, UserGroups.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), latestUpdatedTime, null, this, this);
    }
}