package com.healthcoco.healthcocopad.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.SelectDrugListSolrAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddNewDrugDialogFragment;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewDrugListener;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectDrugItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shreshtha on 28-03-2017.
 */
public class DrugListFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, TextWatcher, LocalDoInBackgroundListenerOptimised, LoadMorePageListener, SelectDrugItemClickListener, View.OnClickListener, AddNewDrugListener {
    public static final int MAX_SIZE = 25;
    public static int PAGE_NUMBER = 0;
    private ListViewLoadMore lvAllDrugsList;
    private TextView tvNoDrugHistory;
    private boolean isEndOfListAchieved = false;
    private List<DrugsListSolrResponse> drugsListSolr = new ArrayList<DrugsListSolrResponse>();
    private SelectDrugListSolrAdapter adapterSolr;
    private ProgressBar progressLoading;
    private boolean isLoadingFromSearch;
    private String lastTextSearched;
    private boolean isInitialLoading = true;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private EditText editSearch;
    private ImageButton btAddDrug;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragemnt_drugs_list, container, false);
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
        initData();
        initViews();
        initListeners();
        initAdapters();
        showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void initData() {
        AddNewTemplateFragment newTemplateFragment = (AddNewTemplateFragment) getFragmentManager().findFragmentByTag(AddNewTemplateFragment.class.getSimpleName());
        if (newTemplateFragment != null) {
            selectedPatient = newTemplateFragment.getSelectedPatient();
        }
    }

    @Override
    public void initViews() {
        lvAllDrugsList = (ListViewLoadMore) view.findViewById(R.id.lv_drugs_list);
        tvNoDrugHistory = (TextView) view.findViewById(R.id.tv_no_drug_history);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        editSearch = (EditText) view.findViewById(R.id.edit_search);
        btAddDrug = (ImageButton) view.findViewById(R.id.bt_add_drug);
    }

    @Override
    public void initListeners() {
        btAddDrug.setOnClickListener(this);
        initEditSearchView(R.string.search_drug, this, this);
    }

    private void initAdapters() {
        adapterSolr = new SelectDrugListSolrAdapter(mActivity, this, this);
        lvAllDrugsList.setAdapter(adapterSolr);
    }

    public void getDrugsList(boolean isInitialLoading, int pageNum, int size, String searchTerm) {
        mApp.cancelPendingRequests(String.valueOf(WebServiceType.GET_DRUGS_LIST_SOLR));
        if (isInitialLoading) {
            mActivity.showLoading(false);
            progressLoading.setVisibility(View.GONE);
        } else
            progressLoading.setVisibility(View.VISIBLE);
        WebDataServiceImpl.getInstance(mApp).getDrugsListSolr(DrugsListSolrResponse.class, pageNum, size, selectedPatient.getDoctorId(), selectedPatient.getHospitalId(), selectedPatient.getLocationId(), searchTerm, this, this);
    }

    private void notifyAdapterSolr(List<DrugsListSolrResponse> list) {
        if (!Util.isNullOrEmptyList(list)) {
            LogUtils.LOGD(TAG, "onResponse DrugsList notifyAdapter " + list.size());
//            Collections.sort(list, ComparatorUtil.dateComparatorDrugsSolrlist);
            lvAllDrugsList.setVisibility(View.VISIBLE);
            tvNoDrugHistory.setVisibility(View.GONE);
        } else {
            lvAllDrugsList.setVisibility(View.GONE);
            tvNoDrugHistory.setVisibility(View.VISIBLE);
        }
        adapterSolr.setListData(list);
        adapterSolr.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String search = String.valueOf(s).toUpperCase(Locale.ENGLISH);
        if (lastTextSearched == null || !lastTextSearched.equalsIgnoreCase(search)) {
            //sorting solar drugs list from server
            Util.checkNetworkStatus(mActivity);
            if (HealthCocoConstants.isNetworkOnline) {
                PAGE_NUMBER = 0;
                isLoadingFromSearch = true;
                isEndOfListAchieved = false;
                getDrugsList(false, PAGE_NUMBER, MAX_SIZE, search);
            } else
                Util.showToast(mActivity, R.string.user_offline);
        }
        lastTextSearched = search;
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    getDrugsList(true, PAGE_NUMBER, MAX_SIZE, "");
                    break;
                case GET_DRUGS_LIST_SOLR:
                    ArrayList<DrugsListSolrResponse> list = (ArrayList<DrugsListSolrResponse>) (ArrayList<?>) response.getDataList();
                    LogUtils.LOGD(TAG, "onResponse DrugsList Size " + drugsListSolr.size() + " isDataFromLocal " + response.isDataFromLocal());
                    if (drugsListSolr == null)
                        drugsListSolr = new ArrayList<>();
                    if (isLoadingFromSearch) {
                        drugsListSolr.clear();
                        notifyAdapterSolr(drugsListSolr);
                    }
                    if (!Util.isNullOrEmptyList(list)) {
                        drugsListSolr.addAll(list);
                    }
                    if (Util.isNullOrEmptyList(list) || list.size() < MAX_SIZE || Util.isNullOrEmptyList(list))
                        isEndOfListAchieved = true;

                    notifyAdapterSolr(drugsListSolr);
                    mActivity.hideLoading();
                    progressLoading.setVisibility(View.GONE);
                    isLoadingFromSearch = false;
                    isInitialLoading = false;
                    break;
                default:
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {

        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else if (!Util.isNullOrBlank(errorMessage)) {
            errorMsg = errorMessage;
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        mActivity.hideLoading();
        progressLoading.setVisibility(View.GONE);
        Util.showToast(mActivity, errorMsg);

    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        progressLoading.setVisibility(View.GONE);
        mActivity.hideLoading();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading && !isLoadingFromSearch) {
            PAGE_NUMBER = PAGE_NUMBER + 1;
            getDrugsList(false, PAGE_NUMBER, MAX_SIZE, getSearchEditTextValue());
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }

    @Override
    public void ondrugItemClick(SelectDrugItemType drugItemType, Object object) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_drug:
                AddNewDrugDialogFragment newDrugDialogFragment = new AddNewDrugDialogFragment(this);
                newDrugDialogFragment.show(mActivity.getSupportFragmentManager(),
                        newDrugDialogFragment.getClass().getSimpleName());
                break;
        }
    }

    @Override
    public void onSaveClicked(Drug drug) {

    }
}
