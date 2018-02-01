package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.TemplatesListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.TemplateListItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

/**
 * Created by Shreshtha on 28-03-2017.
 */
public class TemplateListFragment extends HealthCocoFragment implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener,
        TextWatcher, LocalDoInBackgroundListenerOptimised,
        LoadMorePageListener, SwipeRefreshLayout.OnRefreshListener {

    public static final String TAG_TEMPLATE_ID = "templateId";
    public static final String INTENT_GET_TEMPLATES_LIST_LOCAL = "com.healthcoco.TEMPLATES_LIST_LOCAL";
    //variables need for pagination
    public static final int MAX_SIZE = 10;
    public static final String TAG_IS_FROM_SETTINGS = "isFromSettings";
    private static final int REQUEST_CODE_TEMPLATE_LIST = 121;
    BroadcastReceiver templatesListReceiverLocal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {

        }
    };
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private int currentPageNumber;
    private boolean isLoadingFromSearch;
    private boolean isInitialLoading;
    private TemplateListItemListener templateListItemListener;
    //other variables
    private ProgressBar progressLoading;
    private ListViewLoadMore lvTemplates;
    private TemplatesListAdapter adapter;
    private LinkedHashMap<String, TempTemplate> templatesList = new LinkedHashMap<>();
    private TextView tvNoTemplates;
    private User user;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageButton btAddNewTemplate;
    private boolean isFromAddNewTemplateFragment;
    private boolean isFromSettingsScreen;

    public TemplateListFragment() {
    }

    public TemplateListFragment(TemplateListItemListener templateListItemListener) {
        this.templateListItemListener = templateListItemListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragemnt_template_list, container, false);
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
        Intent intent = mActivity.getIntent();
        isFromAddNewTemplateFragment = intent.getBooleanExtra(HealthCocoConstants.TAG_IS_FROM_NEW_TEMPLATE_FRAGMENT, false);
        isFromSettingsScreen = intent.getBooleanExtra(TAG_IS_FROM_SETTINGS, false);

        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initListeners();
            initAdapters();
            getListFromLocal(true, 0, false);
        }
    }

    @Override
    public void initViews() {
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.blue_action_bar);
        lvTemplates = (ListViewLoadMore) view.findViewById(R.id.lv_templates);
        tvNoTemplates = (TextView) view.findViewById(R.id.tv_no_templates);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        btAddNewTemplate = (ImageButton) view.findViewById(R.id.bt_add_template);
        initEditSearchView(R.string.search_template, this, this);
        if (isFromAddNewTemplateFragment)
            btAddNewTemplate.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        lvTemplates.setLoadMoreListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        lvTemplates.setSwipeRefreshLayout(swipeRefreshLayout);
        btAddNewTemplate.setOnClickListener(this);
    }

    private void initAdapters() {
        adapter = new TemplatesListAdapter(mActivity, templateListItemListener, isFromSettingsScreen);
        lvTemplates.setAdapter(adapter);
    }

    private void notifyAdapter(ArrayList<TempTemplate> templatesList) {
        if (!Util.isNullOrEmptyList(templatesList)) {
            Collections.sort(templatesList, ComparatorUtil.templateListDateComparator);
            lvTemplates.setVisibility(View.VISIBLE);
            tvNoTemplates.setVisibility(View.GONE);
        } else {
            lvTemplates.setVisibility(View.GONE);
            tvNoTemplates.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData(templatesList);
        adapter.notifyDataSetChanged();
    }

    private void initTemplatesList(boolean showLoading) {
        if (showLoading)
            mActivity.showLoading(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.TEMPLATE);
        WebDataServiceImpl.getInstance(mApp).getTemplatesList(TempTemplate.class, user.getUniqueId(), latestUpdatedTime, this, this);
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
//        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case GET_TEMPLATES_LIST:
                if (response.isDataFromLocal()) {
                    ArrayList<TempTemplate> responseList = (ArrayList<TempTemplate>) (ArrayList<?>) response
                            .getDataList();
                    formHashMapAndRefresh(responseList);
                    if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
                        isEndOfListAchieved = true;
                    else isEndOfListAchieved = false;
                    if (isInitialLoading && !response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                        initTemplatesList(true);
                        return;
                    }
                } else if (!Util.isNullOrEmptyList(response.getDataList())) {
                    ArrayList<TempTemplate> responseList = (ArrayList<TempTemplate>) (ArrayList<?>) response
                            .getDataList();
                    if (responseList.size() <= 10)
                        formHashMapAndRefresh(responseList);
                    response.setIsFromLocalAfterApiSuccess(true);
                    new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_TEMPLATES, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    return;
                }
                isInitialLoading = false;
                swipeRefreshLayout.setRefreshing(false);
                progressLoading.setVisibility(View.GONE);
                mActivity.hideLoading();
                break;
            default:
                break;
        }
        swipeRefreshLayout.setRefreshing(false);
    }

    private void formHashMapAndRefresh(ArrayList<TempTemplate> responseList) {
        if (!Util.isNullOrEmptyList(responseList)) {
            for (TempTemplate template :
                    responseList) {
                templatesList.put(template.getUniqueId(), template);
            }
        }
        notifyAdapter(new ArrayList<>(templatesList.values()));
    }

    private void resetListAndPagingAttributes() {
        if (templatesList != null)
            templatesList.clear();
        PAGE_NUMBER = 0;
        currentPageNumber = 0;
        isEndOfListAchieved = false;
        lvTemplates.resetPreLastPosition(0);
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
        resetListAndPagingAttributes();
        getListFromLocal(false, PAGE_NUMBER, true);
    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading) {
            PAGE_NUMBER++;
            getListFromLocal(false, PAGE_NUMBER, false);
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_TEMPLATES:
                LocalDataServiceImpl.getInstance(mApp).addTemplatesList((ArrayList<TempTemplate>) (ArrayList<?>) response.getDataList());
            case GET_TEMPLATES_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getTemplatesListPageWise(WebServiceType.GET_TEMPLATES_LIST, user.getUniqueId(), false, 0l, currentPageNumber, MAX_SIZE, getSearchEditTextValue(), null, null);
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    private void getListFromLocal(boolean initialLoading, int pageNum, boolean loadingFromSearch) {
        this.isInitialLoading = initialLoading;
        this.isLoadingFromSearch = loadingFromSearch;
        this.currentPageNumber = pageNum;
        if (isInitialLoading || isLoadingFromSearch) {
            if (isInitialLoading)
                mActivity.showLoading(false);
            this.currentPageNumber = 0;
            progressLoading.setVisibility(View.GONE);
        } else
            progressLoading.setVisibility(View.VISIBLE);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_TEMPLATES_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onRefresh() {
        initTemplatesList(false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_template:
                openAddNewTemplatesFragment(null);
                break;
        }
    }

    private void openAddNewTemplatesFragment(String templateId) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_NEW_TEMPLATE.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TEMPLATE_ID, templateId);
        intent.putExtra(HealthCocoConstants.TAG_IS_FROM_NEW_TEMPLATE_FRAGMENT, true);
        this.startActivityForResult(intent, REQUEST_CODE_TEMPLATE_LIST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TEMPLATE_LIST) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_NEW_TEMPLATE) {
                if (data != null && data.hasExtra(TAG_TEMPLATE_ID)) {
                    clearSearchEditText();
                    String templateId = data.getStringExtra(TAG_TEMPLATE_ID);
                    TempTemplate template = LocalDataServiceImpl.getInstance(mApp).getTemplate(templateId);
                    if (template != null) {
                        templatesList.put(template.getUniqueId(), template);
                        notifyAdapter(new ArrayList<TempTemplate>(templatesList.values()));
                    }
                }
            }
        }
    }
}
