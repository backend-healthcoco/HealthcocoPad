package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.DiagnosticTestsListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.CommonListSolarDialogFragment;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.DiagnosticTestItemListener;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by Shreshtha on 16-05-2017.
 */

public class DiagnosticTestListFragment extends HealthCocoFragment implements DiagnosticTestItemListener, View.OnClickListener,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised,
        CommonListDialogItemClickListener, AdapterView.OnItemClickListener, TextWatcher, LoadMorePageListener {

    private static final int MAX_SIZE = 25;
    public static final String TAG_ADDED_DIAGNOSTIC_TEST = "diagnosticTest";
    private static final int REQUEST_CODE_DIAGNOSTICS_TESTS = 101;
    private int PAGE_NUMBER = 0;
    private HashMap<String, DiagnosticTest> addedDiagnosticsHashMap = new HashMap<>();
    private DiagnosticTestsListAdapter mAdapterAssignedList;
    private User user;
    private CommonListSolarDialogFragment commonListDialog;
    private ListViewLoadMore lvLabTests;
    private TextView tvNoLabTests;
    private ProgressBar progressLoading;
    private EditText editSearch;
    private ImageButton btAddLabTests;
    private boolean isEndOfListAchieved = false;
    private boolean isLoadingFromSearch;
    private String lastTextSearched;
    private boolean isInitialLoading = true;
    private List<DiagnosticTest> addedList;
    private DiagnosticTestItemListener diagnosticTestItemListener;

    public DiagnosticTestListFragment(DiagnosticTestItemListener diagnosticTestItemListener) {
        this.diagnosticTestItemListener = diagnosticTestItemListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_diagnostics_list_updated, container, false);
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
        initAssignedDiagnosticsListAdapter();
        initData();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        lvLabTests = (ListViewLoadMore) view.findViewById(R.id.lv_lab_tests);
        tvNoLabTests = (TextView) view.findViewById(R.id.tv_no_lab_tests);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        editSearch = (EditText) view.findViewById(R.id.edit_search);
        btAddLabTests = (ImageButton) view.findViewById(R.id.bt_add_lab_tests);
    }

    @Override
    public void initListeners() {
        btAddLabTests.setOnClickListener(this);
        initEditSearchView(R.string.search_lab_test, this, this);
    }

    private void initData() {
        Intent intent = mActivity.getIntent();
        addedList = (List<DiagnosticTest>) intent.getSerializableExtra(HealthCocoConstants.TAG_DIAGNOSTICS_LIST);
        if (!Util.isNullOrEmptyList(addedList)) {
            for (DiagnosticTest diagnosticTest : addedList) {
                addedDiagnosticsHashMap.put(diagnosticTest.getUniqueId(), diagnosticTest);
            }
        }
        refreshTestAdded();
    }

    private void initAssignedDiagnosticsListAdapter() {
        mAdapterAssignedList = new DiagnosticTestsListAdapter(mActivity, this);
        lvLabTests.setAdapter(mAdapterAssignedList);
    }

    private void notifyActivatedAdapter(List activatedList) {
        if (!Util.isNullOrEmptyList(activatedList)) {
            tvNoLabTests.setVisibility(View.GONE);
            lvLabTests.setVisibility(View.VISIBLE);
        } else {
            lvLabTests.setVisibility(View.GONE);
            tvNoLabTests.setVisibility(View.VISIBLE);
        }
        mAdapterAssignedList.setListData(activatedList);
        mAdapterAssignedList.notifyDataSetChanged();
//        ListViewSizeHelper.getListViewSize(lvAssignedList);
    }

    private void getList(boolean isInitialLoading, int pageNum, int size, String searchTerm) {
        mApp.cancelPendingRequests(String.valueOf(WebServiceType.GET_DRUGS_LIST_SOLR));
        if (isInitialLoading) {
            mActivity.showLoading(false);
            progressLoading.setVisibility(View.GONE);
        } else
            progressLoading.setVisibility(View.VISIBLE);
        WebDataServiceImpl.getInstance(mApp).getDiagnosticTestsFromSolr(DiagnosticTest.class, user.getForeignLocationId(),
                user.getForeignHospitalId(), PAGE_NUMBER, MAX_SIZE, searchTerm, this, this);
    }

    @Override
    public void onAddClicked(DiagnosticTest diagnosticTest) {
        addedDiagnosticsHashMap.put(diagnosticTest.getUniqueId(), diagnosticTest);
        refreshTestAdded();
    }

    @Override
    public void onAddedClicked(DiagnosticTest diagnosticTest) {
        addedDiagnosticsHashMap.remove(diagnosticTest.getUniqueId());
        refreshTestAdded();
    }

    private void refreshTestAdded() {
        if (!Util.isNullOrEmptyList(addedDiagnosticsHashMap))
//            tvHeaderOne.setText(addedDiagnosticsHashMap.size() + mActivity.getResources().getString(R.string.tests_selected));
//        else
//            tvHeaderOne.setText("0" + mActivity.getResources().getString(R.string.tests_selected));
            notifyActivatedAdapter(new ArrayList(addedDiagnosticsHashMap.values()));
    }

    @Override
    public DiagnosticTest getDiagnosticTest(String uniqueId) {
        return addedDiagnosticsHashMap.get(uniqueId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_lab_tests:
                mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_DIAGNOSTIC_TESTS, AddUpdateNameDialogType.ADD_DIAGNOSTIC_TEST, this, user, null, HealthCocoConstants.RESULT_CODE_DIAGNOSTICS_TESTS);
                break;
            case R.id.bt_search:
                commonListDialog = openCommonListSolarDialogFragment(this, CommonListDialogType.DIAGNOSTIC_TESTS);
                break;
        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        refreshTestAdded();
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        refreshTestAdded();
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        getList(true, PAGE_NUMBER, MAX_SIZE, "");
                        return;
                    }
                    break;
                case GET_DIAGNOSTIC_TESTS_SOLR:
                    ArrayList<DiagnosticTest> listResponse = (ArrayList<DiagnosticTest>) (ArrayList<?>) response.getDataList();
                    LogUtils.LOGD(TAG, "onResponse solr list Size " + listResponse.size() + " isDataFromLocal " + response.isDataFromLocal());
                    if (addedList == null)
                        addedList = new ArrayList<>();
                    if (isLoadingFromSearch) {
                        addedList.clear();
                        notifyActivatedAdapter(addedList);
                    }
                    if (!Util.isNullOrEmptyList(listResponse)) {
                        addedList.addAll(listResponse);
                    }
                    if (Util.isNullOrEmptyList(listResponse) || listResponse.size() < MAX_SIZE || Util.isNullOrEmptyList(listResponse))
                        isEndOfListAchieved = true;
                    notifyActivatedAdapter(addedList);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DIAGNOSTICS_TESTS) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_DIAGNOSTICS_TESTS && data != null && data.hasExtra(TAG_ADDED_DIAGNOSTIC_TEST)) {
                DiagnosticTest diagnosticTest = (DiagnosticTest) data.getSerializableExtra(TAG_ADDED_DIAGNOSTIC_TEST);
                onAddClicked(diagnosticTest);
            }
        }
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
    public void onDialogItemClicked(CommonListDialogType commonListDialogType, Object object) {
        switch (commonListDialogType) {
            case DIAGNOSTIC_TESTS:
                if (object instanceof DiagnosticTest) {
                    DiagnosticTest diagnosticTest = (DiagnosticTest) object;
                    LogUtils.LOGD(TAG, "Selected diagnosticTest " + diagnosticTest.getTestName());
                    if (addedDiagnosticsHashMap.get(diagnosticTest.getUniqueId()) == null)
                        onAddClicked(diagnosticTest);
                    else
                        Util.showToast(mActivity, String.format(getResources().getString(R.string.lab_test_already_added), Util.getValidatedValue(diagnosticTest.getTestName())));
                }
                break;
        }
        if (commonListDialog != null)
            commonListDialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        autotvDiagnosticSearch.setText("");
//        DiagnosticTest diagnosticTest = diagnosticAutoTvAdapter.getItem(position);
//        onAddClicked(diagnosticTest);
    }

    @Override
    public void onDeleteItemClicked(DiagnosticTest diagnosticTest) {

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
                getList(false, PAGE_NUMBER, MAX_SIZE, search);
            } else
                Util.showToast(mActivity, R.string.user_offline);
        }
        lastTextSearched = search;
    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading && !isLoadingFromSearch) {
            PAGE_NUMBER = PAGE_NUMBER + 1;
            getList(false, PAGE_NUMBER, MAX_SIZE, getSearchEditTextValue());
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }
}
