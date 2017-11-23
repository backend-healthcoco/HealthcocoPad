package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.AddVisitsActivity;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.PatientDetailVisitAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.custom.OptionsPopupWindow;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.OptionsTypePopupWindow;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.VisitedForType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import static com.healthcoco.healthcocopad.enums.WebServiceType.GET_PATIENT_VISIT;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class PatientVisitDetailFragment extends HealthCocoFragment implements Response.Listener<VolleyResponseBean>,
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, VisitDetailCombinedItemListener,
        SwipeRefreshLayout.OnRefreshListener,
        LoadMorePageListener, View.OnClickListener {
    public static final String INTENT_GET_VISITS_LIST_FROM_LOCAL = "com.healthcoco.healthcocopad.fragments.PatientVisitDetailFragment.VISITS_LIST_FROM_LOCAl";
    //variables need for pagination
    public static final int MAX_SIZE = 10;
    public static final String TAG_TOGGLE_STATE = "visitState";
    //other variables
    public static final String INTENT_GET_VISITS_LIST = "com.healthcoco.VISITS_LIST";
    private static final int REQUEST_CODE_VISITS_LIST = 100;
    public FloatingActionButton floatingActionButton;
    boolean isMyScriptVisitToggleState;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private boolean isInitialLoading;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvNoVisits;
    private ProgressBar progressLoading;
    private boolean receiversRegistered;
    private LinkedHashMap<String, VisitDetails> visitsListHashMap = new LinkedHashMap<>();
    private ListViewLoadMore lvVisits;
    private PatientDetailVisitAdapter patientsVisitAdapter;
    private boolean isLoading;
    private OptionsPopupWindow popupWindow;
    private SwipeRefreshLayout swipeRefreshLayout;
    private int currentPageNumber;
    BroadcastReceiver visitsListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            resetListAndPagingAttributes();
            getVisits(false);
        }
    };
    BroadcastReceiver visitsListLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            refreshListFromLocal();
            sendBroadcasts();
        }
    };
    private PatientDetailTabType detailTabType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_visit_deatil, container, false);
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
        initAdapter();
    }

    @Override
    public void initViews() {
        tvNoVisits = (TextView) view.findViewById(R.id.tv_no_visits_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        lvVisits = (ListViewLoadMore) view.findViewById(R.id.lv_visits);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_bt_add_visit);
    }

    @Override
    public void initListeners() {
        lvVisits.setLoadMoreListener(this);
        lvVisits.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    private void initAdapter() {
        patientsVisitAdapter = new PatientDetailVisitAdapter(mActivity, this);
        lvVisits.setAdapter(patientsVisitAdapter);
    }

    public void refreshData(PatientDetailTabType detailTabType) {
        getListFromLocal(true, 0);
        this.detailTabType = detailTabType;
    }

    public void getVisits(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.VISIT_DETAILS);
        WebDataServiceImpl.getInstance(mApp).getVisitsList(VisitDetails.class, GET_PATIENT_VISIT, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                selectedPatient.getUserId(), latestUpdatedTime, VisitedForType.TREATMENT.getVisitFor(), this, this);
    }

    private void initOptionsPopupWindow(OptionsTypePopupWindow optionsTypePopupWindow) {
        popupWindow = new OptionsPopupWindow(mActivity, optionsTypePopupWindow, this);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setContentView(popupWindow.getPopupView());
    }

    public void notifyVisitsAdapter(List<VisitDetails> visitsList) {
        if (!Util.isNullOrEmptyList(visitsList)) {
            Collections.sort(visitsList, ComparatorUtil.visitDateComparator);
            LogUtils.LOGD(TAG, "Size " + visitsList.size());
            lvVisits.setVisibility(View.VISIBLE);
            tvNoVisits.setVisibility(View.GONE);
        } else {
            lvVisits.setVisibility(View.GONE);
            tvNoVisits.setVisibility(View.VISIBLE);
        }
        patientsVisitAdapter.setListData(visitsList);
        patientsVisitAdapter.notifyDataSetChanged();
//        //setting menu options
//        if (visitsList.getVisitedFor().contains(VisitedForType.PRESCRIPTION))
//            initOptionsPopupWindow(OptionsTypePopupWindow.VISITS_PRESCRIPTION);
//        else
//            initOptionsPopupWindow(OptionsTypePopupWindow.VISITS_OTHERS);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        progressLoading.setVisibility(View.GONE);
        swipeRefreshLayout.setRefreshing(false);
        showLoadingOverlay(false);
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        swipeRefreshLayout.setRefreshing(false);
        showLoadingOverlay(false);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()) + " PAgeNum " + PAGE_NUMBER);
            switch (response.getWebServiceType()) {
                case GET_PATIENT_VISIT:
                    if (response.isDataFromLocal()) {
                        ArrayList<VisitDetails> responseList = (ArrayList<VisitDetails>) (ArrayList<?>) response
                                .getDataList();
                        formHashMapAndRefresh(responseList);
                        if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                            isEndOfListAchieved = true;
                        else isEndOfListAchieved = false;
                        if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            getVisits(true);
                            return;
                        }
                    } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                        response.setIsFromLocalAfterApiSuccess(true);
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_VISITS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        return;
                    }
                    if (!response.isUserOnline())
                        onNetworkUnavailable(response.getWebServiceType());
                    progressLoading.setVisibility(View.GONE);
                    isLoading = false;
                    showLoadingOverlay(false);
                    isInitialLoading = false;
                    break;
                case GET_VISIT_PDF_URL:
                    if (response.getData() != null && response.getData() instanceof String) {
                        mActivity.openEnlargedImageDialogFragment(true, (String) response.getData());
                    }
                    break;
                case SEND_SMS_VISIT:
                    Util.showToast(mActivity, mActivity.getResources().getString(R.string.sms_sent_to) + selectedPatient.getMobileNumber());
                    break;
                default:
                    break;
            }
        }
        progressLoading.setVisibility(View.GONE);
        showLoadingOverlay(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void formHashMapAndRefresh(ArrayList<VisitDetails> responseList) {
        if (!Util.isNullOrEmptyList(responseList))
            for (VisitDetails visitDetails : responseList) {
                visitsListHashMap.put(visitDetails.getUniqueId(), visitDetails);
            }
        notifyVisitsAdapter(new ArrayList<VisitDetails>(visitsListHashMap.values()));
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_VISITS:
                LocalDataServiceImpl.getInstance(mApp).addVisitsList((ArrayList<VisitDetails>) (ArrayList<?>) response.getDataList());
            case GET_VISITS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getVisitsListPageWise(GET_PATIENT_VISIT, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                        selectedPatient.getUserId(), PAGE_NUMBER, MAX_SIZE, null, null);
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
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for visits list refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_VISITS_LIST);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(visitsListReceiver, filter);

            IntentFilter filter1 = new IntentFilter();
            filter1.addAction(INTENT_GET_VISITS_LIST_FROM_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(visitsListLocalReceiver, filter1);

            receiversRegistered = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(visitsListReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(visitsListLocalReceiver);
    }

    private void sendBroadcasts() {
        Util.sendBroadcasts(mApp, new ArrayList<String>() {{
//            add(HistoryFragment.INTENT_GET_HISTORY_LIST);
            add(PatientClinicalNotesDetailFragment.INTENT_GET_CLINICAL_NOTES_LIST);
            add(PatientPrescriptionDetailFragment.INTENT_GET_PRESCRIPTION_LIST);
            add(PatientTreatmentDetailFragment.INTENT_GET_TREATMENT_LIST);
        }});
    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isLoading) {
            PAGE_NUMBER = PAGE_NUMBER + 1;
            LogUtils.LOGD(TAG, "LoadMore PAGE_NUMBER " + PAGE_NUMBER);
            getListFromLocal(false, PAGE_NUMBER);
        }
    }

    private void resetListAndPagingAttributes() {
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
        currentPageNumber = 0;
        lvVisits.resetPreLastPosition(0);
    }

    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }

    public void getListFromLocal(boolean showLoading, int pageNum) {
        this.isInitialLoading = showLoading;
        this.currentPageNumber = pageNum;
        isLoading = true;
        if (isInitialLoading) {
            showLoadingOverlay(true);
            progressLoading.setVisibility(View.GONE);
        } else {
            progressLoading.setVisibility(View.VISIBLE);
        }
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_VISITS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_VISITS_LIST) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_VISIT && data != null) {
//                    && data.hasExtra(VisitDetailFragment.TAG_VISIT_ID)) {
                refreshListFromLocal();
                // opens visitDetail screen once visit is added
//                String visitId = data.getStringExtra(VisitDetailFragment.TAG_VISIT_ID);
            }
        }
    }

    private void refreshListFromLocal() {
        resetListAndPagingAttributes();
        getListFromLocal(true, 0);
    }

    @Override
    public User getUser() {
        return null;
    }

    @Override
    public RegisteredPatientDetailsUpdated getSelectedPatient() {
        return null;
    }

    @Override
    public void doPrint(String visitId) {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline) {
            showLoadingOverlay(false);
            WebDataServiceImpl.getInstance(mApp).getPdfUrl(String.class, WebServiceType.GET_VISIT_PDF_URL, visitId, this, this);
        } else onNetworkUnavailable(null);
    }

    @Override
    public void sendSms(String mobileNumber) {
        showSmsAlert(mobileNumber);
    }

    @Override
    public void sendEmail(String emailAddress) {
        checkForEmailAndSend(emailAddress);
    }

    @Override
    public void editVisit(String visitId) {
        isMyScriptVisitToggleState = Util.getVisitToggleStateFromPreferences(mActivity);
        if (isMyScriptVisitToggleState)
            openCommonVisistActivity(CommonOpenUpFragmentType.ADD_VISITS, HealthCocoConstants.TAG_VISIT_ID, visitId, 0);
        else {
            Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
            intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_VISITS.ordinal());
            intent.putExtra(HealthCocoConstants.TAG_VISIT_ID, Parcels.wrap(visitId));
            intent.putExtra(CommonOpenUpPatientDetailFragment.TAG_PATIENT_DETAIL_TAB_TYPE, detailTabType);
            startActivity(intent);
        }
    }

    @Override
    public void setVisitHeader(View visitHeader) {

    }

    @Override
    public void saveAsTemplate(Prescription prescription) {
        LogUtils.LOGD(TAG, "save template");
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline)
            openNewTemplatesFragment(prescription);
        else onNetworkUnavailable(null);
    }

    private void openNewTemplatesFragment(Prescription prescription) {
        openCommonOpenUpActivity(CommonOpenUpFragmentType.ADD_NEW_TEMPLATE, prescription, REQUEST_CODE_VISITS_LIST);
    }

    @Override
    public void cloneVisit(String visitId) {
        Intent intent;
        isMyScriptVisitToggleState = Util.getVisitToggleStateFromPreferences(mActivity);
        if (isMyScriptVisitToggleState) {
            intent = new Intent(mActivity, AddVisitsActivity.class);
        } else {
            intent = new Intent(mActivity, CommonOpenUpActivity.class);
        }
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_VISITS.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_IS_FROM_CLONE, Parcels.wrap(true));
        intent.putExtra(HealthCocoConstants.TAG_VISIT_ID, Parcels.wrap(visitId));
        intent.putExtra(CommonOpenUpPatientDetailFragment.TAG_PATIENT_DETAIL_TAB_TYPE, detailTabType);
        startActivity(intent);
    }

    @Override
    public void openRecord(Records records) {
        if (!Util.isNullOrBlank(records.getRecordsUrl()))
            mActivity.openEnlargedImageDialogFragment(records.getRecordsUrl());
    }

    private void checkForEmailAndSend(String emailAddress) {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline)
            mActivity.openAddUpdateNameDialogFragment(WebServiceType.SEND_EMAIL_VISIT, AddUpdateNameDialogType.EMAIL, this, user, emailAddress, REQUEST_CODE_VISITS_LIST);
        else onNetworkUnavailable(null);
    }

    private void showSmsAlert(String uniqueId) {
        if (!Util.isNullOrBlank(selectedPatient.getMobileNumber()))
            showConfirmationAlert(uniqueId);
        else
            Util.showToast(mActivity, mActivity.getResources().getString(R.string.mobile_no_not_found) + selectedPatient.getLocalPatientName());
    }

    private void showConfirmationAlert(final String uniqueId) {
        if (popupWindow != null)
            popupWindow.dismiss();
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(mActivity.getResources().getString(R.string.confirm_sms_visit) + selectedPatient.getMobileNumber());
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                showLoadingOverlay(false);
                WebDataServiceImpl.getInstance(mApp).sendSms(WebServiceType.SEND_SMS_VISIT, uniqueId,
                        user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                        selectedPatient.getMobileNumber(), PatientVisitDetailFragment.this, PatientVisitDetailFragment.this);
                dialog.dismiss();
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

    private void openCommonVisistActivity(CommonOpenUpFragmentType fragmentType, String tag, Object intentData, int requestCode) {
        Intent intent = new Intent(mActivity, AddVisitsActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        if (!Util.isNullOrBlank(tag) && intentData != null)
            intent.putExtra(tag, Parcels.wrap(intentData));
        if (requestCode == 0)
            startActivity(intent);
        else
            startActivityForResult(intent, requestCode);
    }

    @Override
    public void onRefresh() {
        getVisits(false);
    }

    public void openAddVisitFragment() {
        isMyScriptVisitToggleState = Util.getVisitToggleStateFromPreferences(mActivity);
        if (isMyScriptVisitToggleState)
            openCommonVisistActivity(CommonOpenUpFragmentType.ADD_VISITS, null, null, 0);
        else {
            Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
            intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_VISITS.ordinal());
            intent.putExtra(CommonOpenUpPatientDetailFragment.TAG_PATIENT_DETAIL_TAB_TYPE, detailTabType);
            startActivity(intent);
        }
//        openCommonVisistActivity(CommonOpenUpFragmentType.ADD_VISITS, null, null, 0);
//        Util.addVisitToggleStateInPreference(mActivity,true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_bt_add_visit:
                openAddVisitFragment();
                break;
        }
    }

    public void setUserData(User user, RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        this.selectedPatient = registeredPatientDetailsUpdated;
        this.user = user;
    }
}
