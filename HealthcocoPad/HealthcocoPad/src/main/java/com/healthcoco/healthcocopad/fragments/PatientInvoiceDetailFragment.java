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
import com.healthcoco.healthcocopad.adapter.InvoiceListAdapter;
import com.healthcoco.healthcocopad.adapter.TreatmentListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.HistoryFilterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.InvoiceItemClickListeners;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.TreatmentListItemClickListeners;
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
import java.util.List;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class PatientInvoiceDetailFragment extends HealthCocoFragment implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, LocalDoInBackgroundListenerOptimised, LoadMorePageListener, InvoiceItemClickListeners {

    public static final String TAG_FROM_INVOICE_SCREEN = "fromInvoiceScreen";
    public static final String TAG_INVOICE_DATA = "invoice_data";
    public static final String INTENT_GET_INVOICE_LIST = "com.healthcoco.INVOICE_LIST";
    public static final String INTENT_GET_INVOICE_LIST_LOCAL = "com.healthcoco.INVOICE_LIST_LOCAL";
    public static final String INTENT_GET_INVOICE_USING_INVOICE_ID = "com.healthcoco.INVOICE_USING_INVOICE_ID";

    //variables need for pagination
    public static final int MAX_SIZE = 10;
    private static final int REQUEST_CODE_INVOICE_LIST = 130;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private int currentPageNumber;
    private boolean isInitialLoading;
    private HashMap<String, Invoice> invoiceHashMap = new HashMap<>();
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvNoInvoiceFound;
    private ListViewLoadMore lvInvoice;
    private User user;
    BroadcastReceiver invoiceListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            resetListAndPagingAttributes();
            getInvoice(true);
        }
    };
    private boolean isOTPVerified = false;
    private boolean isLoading;
    private String loginedUser;
    private ProgressBar progressLoading;
    BroadcastReceiver invoiceListLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            boolean showLoading = false;
            if (intent != null && intent.hasExtra(SHOW_LOADING)) {
                showLoading = intent.getBooleanExtra(SHOW_LOADING, false);
            }
            resetListAndPagingAttributes();
            getListFromLocal(showLoading, isOtpVerified(), PAGE_NUMBER);
            sendBroadcasts();
        }
    };
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton floatingActionButton;
    private InvoiceListAdapter adapter;
    private PatientDetailTabType detailTabType;
    private boolean receiversRegistered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_invoice_deatil, container, false);
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
        adapter = new InvoiceListAdapter(mActivity, this);
        lvInvoice.setAdapter(adapter);
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
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_INVOICE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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

    @Override
    public void initViews() {
        lvInvoice = (ListViewLoadMore) view.findViewById(R.id.lv_invoice);
        tvNoInvoiceFound = (TextView) view.findViewById(R.id.tv_no_invoice_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_bt_add_invoice);
    }

    @Override
    public void initListeners() {
        lvInvoice.setSwipeRefreshLayout(swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        floatingActionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.fl_bt_add_invoice:
                openAddInvoiceFragment();
                break;
            default:
                break;
        }

    }

    private void notifyAdapter(ArrayList<Invoice> invoiceArrayList) {
        if (!Util.isNullOrEmptyList(invoiceArrayList)) {
            Collections.sort(invoiceArrayList, ComparatorUtil.invoiceDateComparator);
            lvInvoice.setVisibility(View.VISIBLE);
            tvNoInvoiceFound.setVisibility(View.GONE);
        } else {
            lvInvoice.setVisibility(View.GONE);
            tvNoInvoiceFound.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData(invoiceArrayList);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    private void openAddInvoiceFragment() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_INVOICE.ordinal());
        intent.putExtra(CommonOpenUpPatientDetailFragment.TAG_PATIENT_DETAIL_TAB_TYPE, detailTabType);
        startActivityForResult(intent, REQUEST_CODE_INVOICE_LIST);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INVOICE_LIST) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_INVOICE) {
                lvInvoice.smoothScrollToPosition(0);
                getInvoice(true);
                Util.showToast(mActivity, "Invoice Saved");
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
                case GET_INVOICE:
                    if (response.isDataFromLocal()) {
                        ArrayList<Invoice> responseList = (ArrayList<Invoice>) (ArrayList<?>) response
                                .getDataList();
                        invoiceHashMap.clear();
                        formHashMapAndRefresh(responseList);
                        if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                            isEndOfListAchieved = true;
                        else isEndOfListAchieved = false;
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
                    isInitialLoading = false;
                    break;
                default:
                    break;
            }
        }
        showLoadingOverlay(false);
        swipeRefreshLayout.setRefreshing(false);
    }

    private void formHashMapAndRefresh(ArrayList<Invoice> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (Invoice invoice :
                    responseList) {
                invoiceHashMap.put(invoice.getUniqueId(), invoice);
            }
        }
        notifyAdapter(new ArrayList<>(invoiceHashMap.values()));
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_INVOICE:
                LocalDataServiceImpl.getInstance(mApp).addInvoiceList((ArrayList<Invoice>) (ArrayList<?>) response.getDataList());
            case GET_INVOICE:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getInvoiceList(WebServiceType.GET_INVOICE, user.getUniqueId(), user.getForeignLocationId(),
                        user.getForeignHospitalId(), HealthCocoConstants.SELECTED_PATIENTS_USER_ID, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onRefresh() {
        getInvoice(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for prescription list refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_INVOICE_LIST);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(invoiceListReceiver, filter);
            //receiver for prescription list refresh from local
            IntentFilter filter2 = new IntentFilter();
            filter2.addAction(INTENT_GET_INVOICE_LIST_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(invoiceListLocalReceiver, filter2);
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
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(invoiceListLocalReceiver);
    }

    private void sendBroadcasts() {
        Util.sendBroadcasts(mApp, new ArrayList<String>() {{
//            add(HistoryFragment.INTENT_GET_HISTORY_LIST);
            add(PatientVisitDetailFragment.INTENT_GET_VISITS_LIST);
        }});
    }


    public void refreshData(PatientDetailTabType detailTabType) {
        getListFromLocal(true, isOtpVerified(), PAGE_NUMBER);
        this.detailTabType = detailTabType;
    }

    public void setUserData(User user, String loginedUser, RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated) {
        this.selectedPatient = registeredPatientDetailsUpdated;
        this.user = user;
        this.user.setUniqueId(loginedUser);
    }


    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading) {
            PAGE_NUMBER++;
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
        lvInvoice.resetPreLastPosition(0);
    }


    @Override
    public void onPayInvoiceClicked(Invoice invoice) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_RECEIPT.ordinal());
        intent.putExtra(TAG_INVOICE_DATA, Parcels.wrap(invoice));
        intent.putExtra(TAG_FROM_INVOICE_SCREEN, true);
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_INVOICE);
    }

    @Override
    public void onEditInvoiceClicked(Invoice invoice) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_INVOICE.ordinal());
        if (invoice != null)
            intent.putExtra(AddInvoiceFragment.TAG_INVOICE_ID, Parcels.wrap(invoice));
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_INVOICE);
    }
}
