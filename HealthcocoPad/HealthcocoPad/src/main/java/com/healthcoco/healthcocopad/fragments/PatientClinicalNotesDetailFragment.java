package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ClinicalNotesListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.HistoryFilterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class PatientClinicalNotesDetailFragment extends HealthCocoFragment implements
        SwipeRefreshLayout.OnRefreshListener, LocalDoInBackgroundListenerOptimised,
        CommonEMRItemClickListener {

    public static final String INTENT_GET_CLINICAL_NOTES_LIST = "com.healthcoco.CLINICAL_NOTES_LIST";
    public static final String INTENT_GET_CLINICAL_NOTES_LIST_LOCAL = "com.healthcoco.CLINICAL_NOTES_LIST_LOCAL";
    public static final String INTENT_GET_CLINICAL_NOTE_USING_ID = "com.healthcoco.GET_CLINICAL_NOTE_USING_ID";
    private static final int REQUEST_CODE_CLINICAL_NOTES = 111;

    private ListViewLoadMore lvClinicalNotes;
    private HashMap<String, ClinicalNotes> clinicalNotesList = new HashMap<>();
    private TextView tvNoNotesAdded;
    private User user;
    private ProgressBar progressLoading;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean isOTPVerified = false;
    private ClinicalNotesListAdapter adapter;
    private boolean receiversRegistered = false;
    private Button btAddClinicalNote;
    private boolean isInitialLoading;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private PatientDetailTabType detailTabType;

    public PatientClinicalNotesDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_clinical_notes_deatil, container, false);
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

    public void getListFromLocal(boolean showLoading, boolean isOTPVerified, User user) {
        if (user != null) {
            this.user = user;
            isInitialLoading = showLoading;
            this.isOTPVerified = isOTPVerified;
            if (showLoading) {
                showLoadingOverlay(true);
            }
            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_CLINICAL_NOTES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void getClinicalNotes(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.CLINAL_NOTE);
        WebDataServiceImpl.getInstance(mApp).getEmrListGeneralMethod(ClinicalNotes.class, WebServiceType.GET_CLINICAL_NOTES, isOTPVerified, true, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                HealthCocoConstants.SELECTED_PATIENTS_USER_ID, latestUpdatedTime, this, this);
    }

    @Override
    public void initViews() {
        lvClinicalNotes = (ListViewLoadMore) view.findViewById(R.id.lv_clinical_notes);
        tvNoNotesAdded = (TextView) view.findViewById(R.id.tv_no_clinical_notes_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        btAddClinicalNote = (Button) view.findViewById(R.id.bt_add);
    }

    @Override
    public void initListeners() {
        lvClinicalNotes.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void notifyAdapter(ArrayList<ClinicalNotes> clinicalNotesList) {
        if (!Util.isNullOrEmptyList(clinicalNotesList)) {
            Collections.sort(clinicalNotesList, ComparatorUtil.clinicalNotesDateComparator);
            lvClinicalNotes.setVisibility(View.VISIBLE);
            tvNoNotesAdded.setVisibility(View.GONE);
        } else {
            lvClinicalNotes.setVisibility(View.GONE);
            tvNoNotesAdded.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData(clinicalNotesList);
        adapter.notifyDataSetChanged();
        adapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        adapter = new ClinicalNotesListAdapter(mActivity, this);
        lvClinicalNotes.setAdapter(adapter);
    }

    private void openAddClinicalNotesFragment(String clinicalNoteId) {
//        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
//        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_CLINICAL_NOTE.ordinal());
//        if (!Util.isNullOrBlank(clinicalNoteId))
//            intent.putExtra(AddClinicalNoteFragment.TAG_CLINICAL_NOTE_ID, clinicalNoteId);
//        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_CLINICAL_NOTES);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CLINICAL_NOTES) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_CLINICAL_NOTE) {
                Util.showToast(mActivity, "Clinical Note Saved");
            }
        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
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

    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case GET_CLINICAL_NOTES:
                if (response.isDataFromLocal()) {
                    ArrayList<ClinicalNotes> responseList = (ArrayList<ClinicalNotes>) (ArrayList<?>) response
                            .getDataList();
                    formHashMapAndRefresh(responseList);
                    if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getClinicalNotes(true);
                        return;
                    }
                } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_CLINAL_NOTE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    response.setIsFromLocalAfterApiSuccess(true);
                    return;
                }
                showLoadingOverlay(false);
                progressLoading.setVisibility(View.GONE);
                break;
            default:
                break;
        }
        showLoadingOverlay(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void formHashMapAndRefresh(ArrayList<ClinicalNotes> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (ClinicalNotes clinicalNotes :
                    responseList) {
                clinicalNotesList.put(clinicalNotes.getUniqueId(), clinicalNotes);
            }
        }
        notifyAdapter(new ArrayList<>(clinicalNotesList.values()));
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_CLINAL_NOTE:
                LocalDataServiceImpl.getInstance(mApp).addClinicalNotesList((ArrayList<ClinicalNotes>) (ArrayList<?>) response.getDataList());
            case GET_CLINICAL_NOTES:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getClinicalNotesList(WebServiceType.GET_CLINICAL_NOTES, isOTPVerified, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, null, null);
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
    }

    @Override
    public void showLoading(boolean showLoading) {
        showLoadingOverlay(showLoading);
    }

    @Override
    public RegisteredPatientDetailsUpdated getSelectedPatient() {
        return null;
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public void openEmrScreen(HistoryFilterType historyFilterType) {

    }

    @Override
    public void onRefresh() {
        getClinicalNotes(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for prescription list refresh
            IntentFilter filter1 = new IntentFilter();
            filter1.addAction(INTENT_GET_CLINICAL_NOTES_LIST);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(clinicalNotesListReceiver, filter1);

            //receiver for prescription list refresh from local
            IntentFilter filter2 = new IntentFilter();
            filter2.addAction(INTENT_GET_CLINICAL_NOTES_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(clinicalNotesListLocalReceiver, filter2);

            IntentFilter filter3 = new IntentFilter();
            filter3.addAction(INTENT_GET_CLINICAL_NOTE_USING_ID);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(clinicalNoteUsingIdLocalReceiver, filter3);

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
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(clinicalNotesListReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(clinicalNotesListLocalReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(clinicalNoteUsingIdLocalReceiver);
    }

    BroadcastReceiver clinicalNotesListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getClinicalNotes(true);
        }
    };

    BroadcastReceiver clinicalNoteUsingIdLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(AddEditNormalVisitClinicalNotesFragment.TAG_CLINICAL_NOTE_ID)) {
//                String clinicalNoteId = intent.getStringExtra(AddEditNormalVisitClinicalNotesFragment.TAG_CLINICAL_NOTE_ID);
//                ClinicalNotes clinicalNotes = LocalDataServiceImpl.getInstance(mApp).getClinicalNote(clinicalNoteId, HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
//                if (clinicalNotes != null)
//                    clinicalNotesList.put(clinicalNotes.getUniqueId(), clinicalNotes);
//                notifyAdapter(new ArrayList<>(clinicalNotesList.values()));

            }
            sendBroadcasts();
        }
    };
    BroadcastReceiver clinicalNotesListLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            boolean showLoading = false;
            if (intent != null && intent.hasExtra(SHOW_LOADING)) {
                showLoading = intent.getBooleanExtra(SHOW_LOADING, false);
            }
            getListFromLocal(showLoading, isOtpVerified(), user);
            sendBroadcasts();
        }
    };

    private void sendBroadcasts() {
        Util.sendBroadcasts(mApp, new ArrayList<String>() {{
//            add(HistoryFragment.INTENT_GET_HISTORY_LIST);
            add(PatientVisitDetailFragment.INTENT_GET_VISITS_LIST_FROM_LOCAL);
        }});
    }

    public void refreshData(PatientDetailTabType detailTabType) {
        getListFromLocal(true, true, user);
        this.detailTabType = detailTabType;
    }

    public void openAddNewClinicalNotesScreen() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_VISITS.ordinal());
        intent.putExtra(CommonOpenUpPatientDetailFragment.TAG_PATIENT_DETAIL_TAB_TYPE, detailTabType);
        startActivity(intent);
    }

    public void setUserData(User user, RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        this.selectedPatient = registeredPatientDetailsUpdated;
        this.user = user;
    }
}
