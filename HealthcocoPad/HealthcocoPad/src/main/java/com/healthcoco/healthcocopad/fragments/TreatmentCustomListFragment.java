package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.TreatmentsCustomListAdapter;
import com.healthcoco.healthcocopad.adapter.TreatmentsListSolrAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.skscustomclasses.CustomListData;
import com.healthcoco.healthcocopad.skscustomclasses.SKSCustomListView;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shreshtha on 28-03-2017.
 */
public class TreatmentCustomListFragment extends HealthCocoFragment implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener,
        TextWatcher, LocalDoInBackgroundListenerOptimised,
        LoadMorePageListener, SwipeRefreshLayout.OnRefreshListener {

    //variables need for pagination
    public static final int MAX_SIZE = 25;
    SelectedTreatmentItemClickListener treatmentItemClickListener;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private boolean isLoadingFromSearch;
    private boolean isInitialLoading;
    //other variables
    private ProgressBar progressLoading;
    //    private ListViewLoadMore lvTreatments;
    private TreatmentsListSolrAdapter adapter;
    private TreatmentsCustomListAdapter customListAdapter;
    private ImageButton btAddNewTemplate;
    private List<TreatmentService> treatmentListSolr = new ArrayList<TreatmentService>();
    private TextView tvNoTreatments;
    private User user;
    private String lastTextSearched;
    private SKSCustomListView lvCustomTreatment;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArrayList<TreatmentService> customList;

    public TreatmentCustomListFragment() {
    }

    public TreatmentCustomListFragment(SelectedTreatmentItemClickListener treatmentItemClickListener) {
        this.treatmentItemClickListener = treatmentItemClickListener;
    }

    public static ArrayList<CustomListData> getSectionedDataCustomTreatment(ArrayList<TreatmentService> list) {
        ArrayList<CustomListData> sksListData = new ArrayList<CustomListData>();
        String currentHeader = "";
        ArrayList<TreatmentService> headerDataList = null;
        CustomListData currentCustomListData = null;
        if (!Util.isNullOrEmptyList(list)) {
            for (TreatmentService treatmentService : list) {
                headerDataList = new ArrayList<TreatmentService>();
                if (!currentHeader.equalsIgnoreCase(treatmentService.getCategory())) {
                    if (currentCustomListData != null && headerDataList != null) {
                        currentCustomListData.setHeaderDataList(headerDataList);
                        sksListData.add(currentCustomListData);
                    }
                    currentCustomListData = new CustomListData();
                    currentHeader = treatmentService.getCategory();
                    currentCustomListData.setHeader("" + currentHeader);
                    headerDataList.add(treatmentService);
                } else {
                    headerDataList.add(treatmentService);
                }
            }
            if (currentCustomListData != null) {
                currentCustomListData.setHeaderDataList(headerDataList);
                sksListData.add(currentCustomListData);
            }
        }
        return sksListData;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_treatment_solar_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = mActivity.getIntent();
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initSKSCustomListView();
        initAdapters();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
//        lvTreatments = (ListViewLoadMore) view.findViewById(R.id.lv_treatment);
        tvNoTreatments = (TextView) view.findViewById(R.id.tv_no_treatment);
        btAddNewTemplate = (ImageButton) view.findViewById(R.id.bt_add_treatment);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        LinearLayout layoutCustomList = (LinearLayout) view.findViewById(R.id.layout_custom_list);
        lvCustomTreatment = (SKSCustomListView) view.findViewById(R.id.lv_appointment);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        initEditSearchView(R.string.search_treatment, this, this);

        layoutCustomList.setVisibility(View.VISIBLE);
        btAddNewTemplate.setVisibility(View.GONE);
//        lvTreatments.setVisibility(View.GONE);

    }

    private void initSKSCustomListView() {
        lvCustomTreatment.setPinnedHeaderView(LayoutInflater.from(mActivity).inflate(R.layout.header_appointment_calendar_list, lvCustomTreatment, false));
    }

    @Override
    public void initListeners() {
        btAddNewTemplate.setOnClickListener(this);
//        lvTreatments.setLoadMoreListener(this);
        lvCustomTreatment.setSwipeRefreshLayout(swipeRefreshLayout);
        lvCustomTreatment.setLoadMoreListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    private void initAdapters() {
        customListAdapter = new TreatmentsCustomListAdapter(mActivity, treatmentItemClickListener);
        lvCustomTreatment.setAdapter(customListAdapter);
    }

    private void getTreatmentList(boolean isInitialLoading, int pageNum, int size, String searchTerm) {
        if (user != null) {
            mApp.cancelPendingRequests(String.valueOf(WebServiceType.GET_DRUGS_LIST_SOLR));
            if (isInitialLoading) {
                mActivity.showLoading(false);
                progressLoading.setVisibility(View.GONE);
            } else
                progressLoading.setVisibility(View.VISIBLE);
            WebDataServiceImpl.getInstance(mApp).getTreatmentsListSolr(TreatmentService.class, WebServiceType.GET_TREATMENT_LIST_FEATURED, pageNum, size, user.getUniqueId(), user.getForeignHospitalId(), user.getForeignLocationId(), searchTerm, this, this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.bt_add_treatment) {
            openAddNewTreatmentsFragment();
        }
    }

    private void openAddNewTreatmentsFragment() {
//        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
//        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_NEW_TREATMENT.ordinal());
//        this.startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_TREATMENT_LIST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == HealthCocoConstants.REQUEST_CODE_TREATMENT_LIST) {
//            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_NEW_TREATMENT_DETAIL) {
//                lvTreatments.smoothScrollToPosition(0);
//                PAGE_NUMBER = 0;
//                isEndOfListAchieved = false;
//                isLoadingFromSearch = false;
//                clearSearchEditText();
//                getTreatmentList(false, PAGE_NUMBER, MAX_COUNT, "");
//            }
//        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = errorMessage;
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
        onPostExecute(null);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case FRAGMENT_INITIALISATION:
                if (user != null) {
                    getTreatmentList(true, PAGE_NUMBER, MAX_SIZE, "");
                    return;
                }
                break;
            case GET_TREATMENT_LIST_FEATURED:
                customList = (ArrayList<TreatmentService>) (ArrayList<?>) response.getDataList();
                if (!Util.isNullOrEmptyList(customList)) {
                    formHeaderedList(customList);
                    notifyCustomTreatmentAdapter(customList);
                }
                swipeRefreshLayout.setRefreshing(false);
                progressLoading.setVisibility(View.GONE);
                mActivity.hideLoading();
                break;
            default:
                break;
        }
        mActivity.hideLoading();
    }

    private void formHeaderedList(ArrayList<TreatmentService> customList) {
        HashMap<String, ArrayList<TreatmentService>> headerServiceHashMap = new HashMap<>();
        ArrayList<TreatmentService> list = new ArrayList<>();
        for (TreatmentService treatmentService :
                customList) {
            String category = treatmentService.getCategory();
            if (!Util.isNullOrBlank(category)) {
                if (headerServiceHashMap.containsKey(category)) {
                    list = headerServiceHashMap.get(category);
                    list.add(treatmentService);
                } else {
                    list = new ArrayList<>();
                    list.add(treatmentService);
                }
                headerServiceHashMap.put(category, list);
            }
        }

        //printing
        for (String category :
                headerServiceHashMap.keySet()) {
            LogUtils.LOGD(TAG, "List" + category + " Size : " + headerServiceHashMap.get(category).size());
            for (TreatmentService treatmentService :
                    headerServiceHashMap.get(category)) {
                LogUtils.LOGD(TAG, "Name : " + treatmentService.getName());
            }
        }
    }

    private void notifyCustomTreatmentAdapter(ArrayList<TreatmentService> arrayLists) {
        if (!Util.isNullOrEmptyList(arrayLists)) {
            lvCustomTreatment.setVisibility(View.VISIBLE);
            tvNoTreatments.setVisibility(View.GONE);
        } else {
            lvCustomTreatment.setVisibility(View.GONE);
            tvNoTreatments.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        customListAdapter.setListData(getSectionedDataCustomTreatment(arrayLists));
        customListAdapter.notifyDataSetChanged();
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
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        LogUtils.LOGD(TAG, "TextChange beforeTextChanged");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        LogUtils.LOGD(TAG, "TextChange onTextChange");
    }

    @Override
    public void afterTextChanged(Editable s) {
        LogUtils.LOGD(TAG, "TextChange afterTextChange");
        String search = String.valueOf(s).toUpperCase(Locale.ENGLISH);
        if (lastTextSearched == null || !lastTextSearched.equalsIgnoreCase(search)) {
            //sorting custom treatment list
            ArrayList<TreatmentService> tempList = new ArrayList<TreatmentService>();
            if (!Util.isNullOrEmptyList(customList)) {
                if (search.length() == 0) {
                    tempList.addAll(customList);
                } else {
                    for (TreatmentService treatmentService : customList) {
                        if (treatmentService.getName().toUpperCase(Locale.ENGLISH)
                                .contains(search)) {
                            tempList.add(treatmentService);
                        }
                    }
                }
            }
            notifyCustomTreatmentAdapter(tempList);


        }
        lastTextSearched = search;
    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading && !isLoadingFromSearch) {
            PAGE_NUMBER++;
            getTreatmentList(false, PAGE_NUMBER, MAX_SIZE, getSearchEditTextValue());
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }

    @Override
    public void onRefresh() {
        getTreatmentList(true, PAGE_NUMBER, MAX_SIZE, getSearchEditTextValue());
    }
}
