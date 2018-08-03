package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.LeaveListAdapter;
import com.healthcoco.healthcocopad.adapter.ReferDoctorListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Leave;
import com.healthcoco.healthcocopad.bean.server.ReferDoctor;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddLeaveDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddReferalDialogFragment;
import com.healthcoco.healthcocopad.enums.HistoryFilterType;
import com.healthcoco.healthcocopad.enums.LeaveFromScreenType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Prashant on 07-03-2017.
 */
public class PatientReferalDetailFragment extends HealthCocoFragment implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, LocalDoInBackgroundListenerOptimised, CommonEMRItemClickListener, LoadMorePageListener, SelectedTreatmentItemClickListener {


    //variables need for pagination
    public static final int MAX_SIZE = 10;
    public ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private int currentPageNumber;
    private boolean isInitialLoading;
    private HashMap<String, ReferDoctor> referDactorHashMap = new HashMap<>();
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvNoReferalFound;
    private ListViewLoadMore lvReferal;
    private User user;

    private boolean isOTPVerified = false;
    private boolean isLoading;
    private String loginedUser;
    private ProgressBar progressLoading;

    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private ReferDoctorListAdapter adapter;
    private boolean receiversRegistered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_referal_deatil, container, false);
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
        adapter = new ReferDoctorListAdapter(mActivity, this, this);
        lvReferal.setAdapter(adapter);
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
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_LEAVE_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    @Override
    public void initViews() {
        lvReferal = (ListViewLoadMore) view.findViewById(R.id.lv_referal);
        tvNoReferalFound = (TextView) view.findViewById(R.id.tv_no_referal_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_bt_add_referal);
    }

    @Override
    public void initListeners() {
        lvReferal.setLoadMoreListener(this);
        lvReferal.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fl_bt_add_referal:
                openAddReferalFragment();
                break;
            default:
                break;
        }

    }

    private void notifyAdapter(ArrayList<ReferDoctor> referDoctorArrayList) {
        if (!Util.isNullOrEmptyList(referDoctorArrayList)) {
            Collections.sort(referDoctorArrayList, ComparatorUtil.referDcotorDateComparator);
            lvReferal.setVisibility(View.VISIBLE);
            tvNoReferalFound.setVisibility(View.GONE);
        } else {
            lvReferal.setVisibility(View.GONE);
            tvNoReferalFound.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData(referDoctorArrayList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    private void openAddReferalFragment() {
        AddReferalDialogFragment dialogFragment = new AddReferalDialogFragment();
        Bundle bundle = new Bundle();
//        bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, calendarEvents.getAppointmentId());
        bundle.putParcelable(AddLeaveDialogFragment.TAG_FROM_SCREEN_TYPE, Parcels.wrap(LeaveFromScreenType.ADD_NEW.ordinal()));
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(this, HealthCocoConstants.REQUEST_CODE_LEAVE);
        dialogFragment.show(mActivity.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_LEAVE) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_NEW_LEAVE) {
                lvReferal.smoothScrollToPosition(0);
                getListFromLocal(true, isOTPVerified, 0);
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
                case GET_LEAVE_LIST:
                    ArrayList<ReferDoctor> responseList = (ArrayList<ReferDoctor>) (ArrayList<?>) response
                            .getDataList();
                    if (isInitialLoading) referDactorHashMap.clear();
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

    private void formHashMapAndRefresh(ArrayList<ReferDoctor> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (ReferDoctor referDoctor :
                    responseList) {
                referDactorHashMap.put(referDoctor.getUniqueId(), referDoctor);
            }
        }
        notifyAdapter(new ArrayList<>(referDactorHashMap.values()));
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_LEAVE_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getReferDoctorList(WebServiceType.GET_LEAVE_LIST, clinicDoctorProfileList, user.getForeignLocationId(),
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
    public void onPause() {
        super.onPause();
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
        if (object != null && object instanceof ReferDoctor) {
            ReferDoctor referDoctor = (ReferDoctor) object;
            AddReferalDialogFragment dialogFragment = new AddReferalDialogFragment();
            Bundle bundle = new Bundle();
            bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, referDoctor.getUniqueId());
            bundle.putParcelable(AddLeaveDialogFragment.TAG_FROM_SCREEN_TYPE, Parcels.wrap(LeaveFromScreenType.RESCHEDULE.ordinal()));
            dialogFragment.setArguments(bundle);
            dialogFragment.setTargetFragment(this, HealthCocoConstants.REQUEST_CODE_LEAVE);
            dialogFragment.show(mActivity.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
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
        lvReferal.resetPreLastPosition(0);
    }

}
