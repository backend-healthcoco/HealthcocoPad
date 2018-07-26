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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.InvestigationNotesListAdapter;
import com.healthcoco.healthcocopad.adapter.TreatmentListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.InvestigationNote;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.HistoryFilterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.listeners.TreatmentListItemClickListeners;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.healthcoco.healthcocopad.fragments.AddEditNormalInvestigationFragment.TAG_INVESTIGATION_NOTE_ID;

/**
 * Created by Prashant on 07-03-2017.
 */
public class PatientInvestigationDetailFragment extends HealthCocoFragment implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, LocalDoInBackgroundListenerOptimised, CommonEMRItemClickListener, LoadMorePageListener, SelectedTreatmentItemClickListener {

    public static final String TAG_INVESTIGATION_DATA = "treatment_data";
    public static final String INTENT_GET_INVESTIGATION_LIST_LOCAL = "com.healthcoco.INVESTIGATION_LIST_LOCAL";

    //variables need for pagination
    public static final int MAX_SIZE = 10;
    private static final int REQUEST_CODE_INVESTIGATION_NOTE_LIST = 170;
    public ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList;
    List<InvestigationNote> investigationNoteList;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private int currentPageNumber;
    private boolean isInitialLoading;
    private HashMap<String, InvestigationNote> investigationNoteHashMap = new HashMap<>();
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvNoInvestigationFound;
    private ListViewLoadMore lvInvestigation;
    private User user;

    private boolean isOTPVerified = false;
    private boolean isLoading;
    private String loginedUser;
    private ProgressBar progressLoading;
    BroadcastReceiver investigationListLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            boolean showLoading = false;
            if (intent != null && intent.hasExtra(SHOW_LOADING)) {
                showLoading = intent.getBooleanExtra(SHOW_LOADING, false);
            }
            resetListAndPagingAttributes();
            getListFromLocal(showLoading, isOtpVerified(), PAGE_NUMBER);
        }
    };
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private InvestigationNotesListAdapter adapter;
    private PatientDetailTabType detailTabType;
    private boolean receiversRegistered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_investigation_deatil, container, false);
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

    private void initAdapter() {
        adapter = new InvestigationNotesListAdapter(mActivity, this, this);
        lvInvestigation.setAdapter(adapter);
    }

    public void getListFromLocal(boolean initialLoading, boolean isOTPVerified, int PAGE_NUMBER) {
        this.isInitialLoading = initialLoading;
        this.currentPageNumber = PAGE_NUMBER;
        this.isOTPVerified = isOTPVerified;
        isLoading = true;
        if (initialLoading) {
            showLoadingOverlay(true);
            progressLoading.setVisibility(View.GONE);
        } else {
            progressLoading.setVisibility(View.VISIBLE);
        }
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_INVESTIGATION_NOTE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

  /*  public void getTreatment(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.TREATMENT);
        WebDataServiceImpl.getInstance(mApp).getTreatment(Treatments.class, WebServiceType.GET_TREATMENT, true, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                HealthCocoConstants.SELECTED_PATIENTS_USER_ID, latestUpdatedTime, this, this);
    }*/

    @Override
    public void initViews() {
        lvInvestigation = (ListViewLoadMore) view.findViewById(R.id.lv_investigation);
        tvNoInvestigationFound = (TextView) view.findViewById(R.id.tv_no_investigation_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_bt_add_investigation);
    }

    @Override
    public void initListeners() {
        lvInvestigation.setLoadMoreListener(this);
        lvInvestigation.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_bt_add_investigation:
                openAddInvestigationFragment();
                break;
            default:
                break;
        }

    }

    private void notifyAdapter(ArrayList<InvestigationNote> investigationNoteArrayList) {
        if (!Util.isNullOrEmptyList(investigationNoteArrayList)) {
            Collections.sort(investigationNoteArrayList, ComparatorUtil.investigationNoteDateComparator);
            lvInvestigation.setVisibility(View.VISIBLE);
            tvNoInvestigationFound.setVisibility(View.GONE);
        } else {
            lvInvestigation.setVisibility(View.GONE);
            tvNoInvestigationFound.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData(investigationNoteArrayList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    private void openAddInvestigationFragment() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_INVESTIGATION_NOTE.ordinal());
        startActivityForResult(intent, REQUEST_CODE_INVESTIGATION_NOTE_LIST);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INVESTIGATION_NOTE_LIST) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_NEW_IVESTIGATION) {
                lvInvestigation.smoothScrollToPosition(0);
                getListFromLocal(true, isOTPVerified, 0);
                Util.showToast(mActivity, getString(R.string.investigation_saved));
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

    @SuppressWarnings("unchecked")
    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case GET_INVESTIGATION_NOTE:
                    ArrayList<InvestigationNote> responseList = (ArrayList<InvestigationNote>) (ArrayList<?>) response
                            .getDataList();
                    if (isInitialLoading) investigationNoteHashMap.clear();
                    formHashMapAndRefresh(responseList);
                    if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                        isEndOfListAchieved = true;
                    else isEndOfListAchieved = false;
                    showLoadingOverlay(false);
                    progressLoading.setVisibility(View.GONE);
                    isInitialLoading = false;
                    isLoading = false;
                    break;
                default:
                    break;
            }
        }
        showLoadingOverlay(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void formHashMapAndRefresh(ArrayList<InvestigationNote> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (InvestigationNote investigationNote :
                    responseList) {
                investigationNoteHashMap.put(investigationNote.getUniqueId(), investigationNote);
            }
        }
        notifyAdapter(new ArrayList<>(investigationNoteHashMap.values()));
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_INVESTIGATION_NOTE:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getInvestigationList(WebServiceType.GET_INVESTIGATION_NOTE, clinicDoctorProfileList, user.getForeignLocationId(),
                        user.getForeignHospitalId(), selectedPatient.getUserId(), PAGE_NUMBER, MAX_SIZE, null, null);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onRefresh() {
        getListFromLocal(true, isOTPVerified, 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            IntentFilter filter2 = new IntentFilter();
            filter2.addAction(INTENT_GET_INVESTIGATION_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(investigationListLocalReceiver, filter2);
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
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(investigationListLocalReceiver);
    }

    @Override
    public void showLoading(boolean showLoading) {

    }

    @Override
    public RegisteredPatientDetailsUpdated getSelectedPatient() {
        return selectedPatient;
    }


    @Override
    public void onTreatmentItemClick(Object object) {
        if (object != null && object instanceof InvestigationNote) {
            InvestigationNote note = (InvestigationNote) object;
            Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
            intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_INVESTIGATION_NOTE.ordinal());
            intent.putExtra(TAG_INVESTIGATION_NOTE_ID, note.getUniqueId());
            startActivityForResult(intent, REQUEST_CODE_INVESTIGATION_NOTE_LIST);
        }
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public String getLoginedUser() {
        return loginedUser;
    }

    @Override
    public void openEmrScreen(HistoryFilterType historyFilterType) {

    }

    public void refreshData(PatientDetailTabType detailTabType, ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList) {
        this.clinicDoctorProfileList = clinicDoctorProfileList;
        getListFromLocal(true, isOtpVerified(), PAGE_NUMBER);
        this.detailTabType = detailTabType;
    }

    public void setUserData(User user, String loginedUser, RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        this.selectedPatient = registeredPatientDetailsUpdated;
        this.user = user;
        this.loginedUser = loginedUser;
    }


    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isLoading) {
            PAGE_NUMBER = PAGE_NUMBER + 1;
            LogUtils.LOGD(TAG, "LoadMore PAGE_NUMBER " + PAGE_NUMBER);
            getListFromLocal(false, isOtpVerified(), PAGE_NUMBER);
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }

    private void resetListAndPagingAttributes() {
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
        currentPageNumber = 0;
        lvInvestigation.resetPreLastPosition(0);
    }

}
