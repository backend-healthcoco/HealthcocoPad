package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.SelectInvoiceListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectInvoiceItemClickListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by Prashant on 27-12-2017.
 */

public class SelectInvoiceFragment extends HealthCocoFragment
        implements SwipeRefreshLayout.OnRefreshListener, LocalDoInBackgroundListenerOptimised, AdapterView.OnItemClickListener {
    public static final String TAG_INVOICE_DATA = "invoice_data";
    private ListViewLoadMore lvInvoice;
    private TextView tvNoInvoiceAdded;
    private ProgressBar progressLoading;
    private SwipeRefreshLayout swipeRefreshLayout;
    private User user;
    private boolean isInitialLoading;
    private HashMap<String, Invoice> invoiceHashMap = new HashMap<>();
    private SelectInvoiceListAdapter adapter;
    private SelectInvoiceItemClickListener selectInvoiceItemClickListener;

    public SelectInvoiceFragment(SelectInvoiceItemClickListener selectInvoiceItemClickListener) {
        super();
        this.selectInvoiceItemClickListener = selectInvoiceItemClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_invoice_list, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
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
        initAdapter();
    }

    @Override
    public void initViews() {
        lvInvoice = (ListViewLoadMore) view.findViewById(R.id.lv_invoice);
        tvNoInvoiceAdded = (TextView) view.findViewById(R.id.tv_no_invoice_added);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
    }

    @Override
    public void initListeners() {
        lvInvoice.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
//        btAddInvoice.setOnClickListener(this);
        lvInvoice.setOnItemClickListener(this);
    }

    public void getListFromLocal(boolean showLoading, User user) {
        if (user != null) {
            this.user = user;
            isInitialLoading = showLoading;
            if (showLoading) {
                showLoadingOverlay(true);
            }
            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_INVOICE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }
    }

    public void getInvoice(boolean showLoading) {
        if (showLoading)
            showLoadingOverlay(true);
        else
            showLoadingOverlay(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.INVOICE);
        WebDataServiceImpl.getInstance(mApp).getInvoice(Invoice.class, WebServiceType.GET_INVOICE, true, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(),
                HealthCocoConstants.SELECTED_PATIENTS_USER_ID, latestUpdatedTime, this, this);
    }

    private void notifyAdapter(ArrayList<Invoice> invoiceArrayList) {
        if (!Util.isNullOrEmptyList(invoiceArrayList)) {
            Collections.sort(invoiceArrayList, ComparatorUtil.invoiceDateComparator);
            lvInvoice.setVisibility(View.VISIBLE);
            tvNoInvoiceAdded.setVisibility(View.GONE);
        } else {
            lvInvoice.setVisibility(View.GONE);
            tvNoInvoiceAdded.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData(invoiceArrayList);
        adapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        adapter = new SelectInvoiceListAdapter(mActivity);
        lvInvoice.setAdapter(adapter);
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
            case FRAGMENT_INITIALISATION:
                if (user != null) {
                    getListFromLocal(true, user);
                }
                break;
            case GET_INVOICE:
                if (response.isDataFromLocal()) {
                    ArrayList<Invoice> responseList = (ArrayList<Invoice>) (ArrayList<?>) response
                            .getDataList();
                    formHashMapAndRefresh(responseList);
                    if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        getInvoice(true);
                        return;
                    }
                } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_INVOICE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
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

    private void formHashMapAndRefresh(ArrayList<Invoice> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (Invoice invoice :
                    responseList) {
                if (!invoice.getDiscarded())
                    invoiceHashMap.put(invoice.getUniqueId(), invoice);
            }
        }
        notifyAdapter(new ArrayList<>(invoiceHashMap.values()));
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null)
                    user = doctor.getUser();
                break;
            case ADD_INVOICE:
                LocalDataServiceImpl.getInstance(mApp).addInvoiceList((ArrayList<Invoice>) (ArrayList<?>) response.getDataList());
            case GET_INVOICE:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getInvoiceSortedList(WebServiceType.GET_INVOICE, user.getUniqueId(), user.getForeignLocationId(),
                        user.getForeignHospitalId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, null, null);
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
    public void onRefresh() {
        getInvoice(false);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Invoice invoice = (Invoice) adapter.getItem(position);
        selectInvoiceItemClickListener.onInvoiceItemClick(invoice);
    }
}