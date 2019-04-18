package com.healthcoco.healthcocopad.fragments;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VaccineCustomResponse;
import com.healthcoco.healthcocopad.bean.server.VaccineResponse;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.VaccineDuration;
import com.healthcoco.healthcocopad.enums.VaccineStatus;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PopupWindowListener;
import com.healthcoco.healthcocopad.listeners.VaccinationListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.SyncUtility;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class VaccinationListFragment extends HealthCocoFragment implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener, LocalDoInBackgroundListenerOptimised, PopupWindowListener, VaccinationListener, com.healthcoco.healthcocopad.popupwindow.PopupWindowListener {

    public static final String INTENT_REFRESH_REQUEST_LIST_FROM_SERVER = "com.healthcoco.android.fragments.VaccinationListFragment.INTENT_REFRESH_REQUEST_LIST_FROM_SERVER";
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private RecyclerView rvVaccineList;
    private ProgressBar progressLoading;
    private SwipeRefreshLayout swipeRefreshLayout;
    private HealthcocoRecyclerViewAdapter adapter;
    private boolean isInitialLoading;
    private LinkedHashMap<String, VaccineCustomResponse> customResponseLinkedHashMap;
    private boolean receiversRegistered;
    private int size = 5;
    private static LinkedHashMap<String, VaccineDuration> statusLinkedHashMap = new LinkedHashMap<>();
    private boolean forAll = true;
    private TextView tvNoVaccineFound;
    private FloatingActionButton fbAddVaccine;
    private TextView tvChangeDate;
    private boolean isGiven = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_vaccination_list, container, false);
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
        initAdapters();
    }

    @Override
    public void initViews() {
        rvVaccineList = (RecyclerView) view.findViewById(R.id.rv_list);
        rvVaccineList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        fbAddVaccine = (FloatingActionButton) view.findViewById(R.id.bt_add);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        tvNoVaccineFound = (TextView) view.findViewById(R.id.tv_no_vaccine_found);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        tvChangeDate = (TextView) view.findViewById(R.id.tv_change_date);
        tvChangeDate.setVisibility(View.VISIBLE);
    }

    @Override
    public void initListeners() {
        swipeRefreshLayout.setOnRefreshListener(this);
        fbAddVaccine.setOnClickListener(this);
        tvChangeDate.setOnClickListener(this);
    }

    private void initAdapters() {
        adapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.VACCINATION, this);
        rvVaccineList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void setUserData(User user, RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        this.selectedPatient = registeredPatientDetailsUpdated;
        this.user = user;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add:
                openCommonOpenUpActivity(CommonOpenUpFragmentType.ADD_VACCINATION, null, null, HealthCocoConstants.REQUEST_CODE_VACCINATION_LIST);
                break;
            case R.id.tv_change_date:
                openDatePickerDialog();
                break;
        }
    }

    private void openDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(calendar, Util.getValidatedValueOrNull(tvChangeDate));
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                tvChangeDate.setText(DateTimeUtil.getBirthDateFormat(dayOfMonth, monthOfYear + 1, year));
                changeDateOfVaccination((DateTimeUtil.getLongFromFormattedFormatString(DateTimeUtil.DATE_FORMAT_DAY_MONTH_YEAR_SLASH, DateTimeUtil.getBirthDateFormat(dayOfMonth, monthOfYear + 1, year))));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(DateTimeUtil.getLongFromFormattedFormatString(DateTimeUtil.DATE_FORMAT_DAY_MONTH_YEAR_SLASH, Util.getDOB(selectedPatient.getDob())));
        datePickerDialog.show();
    }

    private void changeDateOfVaccination(long vaccineStartDate) {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).updateVaccineStartDateList(Boolean.class, WebServiceType.UPDATE_VACCINE_START_DATE,
                selectedPatient.getUserId(), vaccineStartDate, this, this);
    }

    public void getListFromLocal(boolean showLoading) {
        if (user != null) {
            isInitialLoading = showLoading;
            if (showLoading) {
                showLoadingOverlay(true);
            }
            if (isInitialLoading) {
                progressLoading.setVisibility(View.GONE);
            } else {
                progressLoading.setVisibility(View.VISIBLE);
            }
            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_VACCINATION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void getVaccinationList(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.GET_VACCINATION);
        WebDataServiceImpl.getInstance(mApp).getVaccinationList(VaccineResponse.class, WebServiceType.GET_VACCINATION, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                HealthCocoConstants.SELECTED_PATIENTS_USER_ID, latestUpdatedTime, this, this);
    }

    private void notifyAdapter(ArrayList<VaccineCustomResponse> list) {
        if (isGiven)
            tvChangeDate.setVisibility(View.VISIBLE);
        else tvChangeDate.setVisibility(View.GONE);
        if (!Util.isNullOrEmptyList(list)) {
            Collections.sort(list, ComparatorUtil.vaccineDateComparator);
            rvVaccineList.setVisibility(View.VISIBLE);
            tvNoVaccineFound.setVisibility(View.GONE);
        } else {
            rvVaccineList.setVisibility(View.GONE);
            tvNoVaccineFound.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData((ArrayList<Object>) (Object) list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        getVaccinationList(false);
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
            case GET_VACCINATION:
                if (response.isDataFromLocal()) {
                    ArrayList<VaccineResponse> responseList = (ArrayList<VaccineResponse>) (ArrayList<?>) response
                            .getDataList();
                    ArrayList<VaccineCustomResponse> sectionedDataCustomTreatment = getSectionedDataCustomTreatment(responseList);
                    formHashMapAndRefresh(sectionedDataCustomTreatment);
                    if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getVaccinationList(true);
                        return;
                    }
                } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_VACCINATION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    response.setIsFromLocalAfterApiSuccess(true);
                    return;
                }
                new SyncUtility(mApp, mActivity, user, null).getVaccinesBrandsList();
                showLoadingOverlay(false);
                progressLoading.setVisibility(View.GONE);
                isInitialLoading = false;
                break;
            case UPDATE_VACCINE_START_DATE:
                if (response.getData() != null && response.getData() instanceof Boolean) {
                    boolean isDataSuccess = (boolean) response.getData();
                    if (isDataSuccess) {
                        Util.sendBroadcast(mApp, INTENT_REFRESH_REQUEST_LIST_FROM_SERVER);
                    }
                } else {
                    Util.showToast(mActivity, response.getErrMsg());
                }
                mActivity.hideLoading();
                break;
            default:
                break;
        }
        showLoadingOverlay(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void formHashMapAndRefresh(ArrayList<VaccineCustomResponse> responseArrayList) {
        customResponseLinkedHashMap = new LinkedHashMap<>();
        for (VaccineCustomResponse vaccineCustomResponse :
                responseArrayList) {
            customResponseLinkedHashMap.put(vaccineCustomResponse.getDuration(), vaccineCustomResponse);
            for (VaccineResponse vaccineResponse :
                    vaccineCustomResponse.getVaccineResponse()) {
                if (vaccineResponse.getStatus() == VaccineStatus.GIVEN)
                    isGiven = false;
            }
        }
        notifyAdapter(responseArrayList);
    }

    public static ArrayList<VaccineCustomResponse> getSectionedDataCustomTreatment(ArrayList<VaccineResponse> list) {
        ArrayList<VaccineCustomResponse> sksListData = new ArrayList<VaccineCustomResponse>();
        HashMap<String, HashMap<Long, ArrayList<VaccineResponse>>> headerServiceHashMap = new HashMap<>();
        ArrayList<VaccineResponse> responseArrayList = new ArrayList<>();
        HashMap<Long, ArrayList<VaccineResponse>> stringArrayListHashMap = new HashMap<>();
        for (VaccineResponse vaccineResponse :
                list) {
            String duration = vaccineResponse.getDuration();
//            if (!Util.isNullOrBlank(duration)) {
            if (headerServiceHashMap.containsKey(duration)) {
                stringArrayListHashMap = headerServiceHashMap.get(duration);
                responseArrayList = stringArrayListHashMap.get(vaccineResponse.getDueDate());
                if (!Util.isNullOrEmptyList(responseArrayList)) {
                    responseArrayList.add(vaccineResponse);
                    stringArrayListHashMap.put(vaccineResponse.getDueDate(), responseArrayList);
                } else {
                    responseArrayList = new ArrayList<>();
                    responseArrayList.add(vaccineResponse);
                    stringArrayListHashMap.put(vaccineResponse.getDueDate(), responseArrayList);
                }
            } else {
                responseArrayList = new ArrayList<>();
                responseArrayList.add(vaccineResponse);
                stringArrayListHashMap = new HashMap<>();
                stringArrayListHashMap.put(vaccineResponse.getDueDate(), responseArrayList);
            }
            headerServiceHashMap.put(duration, stringArrayListHashMap);
        }

        for (String duration :
                headerServiceHashMap.keySet()) {
            HashMap<Long, ArrayList<VaccineResponse>> longArrayListHashMap = headerServiceHashMap.get(duration);
            for (Long s :
                    longArrayListHashMap.keySet()) {
                VaccineCustomResponse vaccineCustomResponse = new VaccineCustomResponse();
                vaccineCustomResponse.setDuration(duration);
                vaccineCustomResponse.setVaccineResponse(longArrayListHashMap.get(s));
                vaccineCustomResponse.setDueDate(s);
                sksListData.add(vaccineCustomResponse);
            }
        }
        return sksListData;
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_VACCINATION:
                LocalDataServiceImpl.getInstance(mApp).addVaccinationList((ArrayList<VaccineResponse>) (ArrayList<?>) response.getDataList());
            case GET_VACCINATION:
                if (forAll)
                    volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getVaccinationList(WebServiceType.GET_VACCINATION, user.getUniqueId(), user.getForeignLocationId(),
                            user.getForeignHospitalId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, size, null, null);
                else
                    volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getVaccinationList(WebServiceType.GET_VACCINATION, user.getUniqueId(), user.getForeignLocationId(),
                            user.getForeignHospitalId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, size, statusLinkedHashMap, null, null);
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.LOGD(TAG, "receiversRegistered " + receiversRegistered);
        if (!receiversRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_REFRESH_REQUEST_LIST_FROM_SERVER);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshRequestsListFromServerReceiver, filter);
        }
        receiversRegistered = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshRequestsListFromServerReceiver);
    }

    BroadcastReceiver refreshRequestsListFromServerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getVaccinationList(true);
        }
    };

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case VACCINE_FILTER:
                if (object instanceof VaccineDuration) {
                    VaccineDuration filter = (VaccineDuration) object;
                    if (statusLinkedHashMap.containsKey(filter.getValue()))
                        statusLinkedHashMap.remove(filter.getValue());
                    else
                        statusLinkedHashMap.put(filter.getValue(), filter);
                    refreshCalendarEventsList(false);
                } else if (object instanceof String) {
                    statusLinkedHashMap.clear();
                    refreshCalendarEventsList(true);
                }
                break;
        }
    }

    @Override
    public void onEmptyListFound() {

    }

    private void refreshCalendarEventsList(boolean forAll) {
        this.forAll = forAll;
        getListFromLocal(true);
    }

    public static LinkedHashMap<String, VaccineDuration> getStatusLinkedHashMap() {
        return statusLinkedHashMap;
    }

    @Override
    public User getUser() {
        return user;
    }

    public void initBottomPopupSheet(View v) {
        initBottomSheetDialogWindows(v, PopupWindowType.VACCINE_FILTER, new ArrayList<Object>(PopupWindowType.VACCINE_FILTER.getList()), R.layout.layout_bottom_dialog_item_with_checkbox, true, this);
    }
}
