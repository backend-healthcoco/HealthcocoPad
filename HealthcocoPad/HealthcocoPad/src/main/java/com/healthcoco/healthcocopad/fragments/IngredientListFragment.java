package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.IngredientResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddNewTreatmentDialogFragment;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewDrugListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectedRecipeItemClickListener;
import com.healthcoco.healthcocopad.recyclerview.EndlessRecyclerViewScrollListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Prashant on 28-03-2017.
 */
public class IngredientListFragment extends HealthCocoFragment implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener,
        TextWatcher, LocalDoInBackgroundListenerOptimised,
        SwipeRefreshLayout.OnRefreshListener, AddNewDrugListener {

    //variables need for pagination
    public static final int MAX_SIZE = 15;
    private SelectedRecipeItemClickListener selectedRecipeItemClickListener;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private boolean isLoadingFromSearch;
    private boolean isInitialLoading;
    //other variables
    private ProgressBar progressLoading;
    private ProgressBar progressLoadingCenter;
    private RecyclerView ingredientRecyclerView;
    private HealthcocoRecyclerViewAdapter mAdapter;
    private ImageButton btAddNewTemplate;
    private List<IngredientResponse> ingredientListSolr = new ArrayList<IngredientResponse>();
    private TextView tvNoIngredient;
    private User user;
    private String lastTextSearched;
    private boolean isResponseReceived = false;

    public IngredientListFragment() {
    }

    public IngredientListFragment(SelectedRecipeItemClickListener selectedRecipeItemClickListener) {
        this.selectedRecipeItemClickListener = selectedRecipeItemClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recipe_solar_list, container, false);
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
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        ingredientRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_recipe);
        tvNoIngredient = (TextView) view.findViewById(R.id.tv_no_recipe);
        btAddNewTemplate = (ImageButton) view.findViewById(R.id.bt_add_recipe);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        progressLoadingCenter = (ProgressBar) view.findViewById(R.id.progress_loading_center);
        initEditSearchView(R.string.search_ingredient, this, this);

        btAddNewTemplate.setVisibility(View.VISIBLE);
        ingredientRecyclerView.setVisibility(View.VISIBLE);

        tvNoIngredient.setVisibility(View.GONE);
        tvNoIngredient.setText(R.string.no_ingredient_added);
    }


    @Override
    public void initListeners() {
        btAddNewTemplate.setOnClickListener(this);
    }

    private void initAdapters() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        ingredientRecyclerView.setLayoutManager(layoutManager);
        ingredientRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.INGREDIENT_ITEM_SOLR, selectedRecipeItemClickListener);
        mAdapter.setListData((ArrayList<Object>) (Object) ingredientListSolr);
        ingredientRecyclerView.setAdapter(mAdapter);

        ingredientRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMore(current_page);
            }
        });

    }

    private void notifyAdapter(List<IngredientResponse> ingredientResponseList) {
        if (!Util.isNullOrEmptyList(ingredientResponseList)) {
            ingredientRecyclerView.setVisibility(View.VISIBLE);
            tvNoIngredient.setVisibility(View.GONE);
        } else {
            ingredientRecyclerView.setVisibility(View.GONE);
            tvNoIngredient.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        mAdapter.setListData((ArrayList<Object>) (Object) ingredientResponseList);
        mAdapter.notifyDataSetChanged();
    }


    private void getIngredientList(boolean isInitialLoading, int pageNum, int size, String searchTerm) {
        if (user != null) {
            mApp.cancelPendingRequests(String.valueOf(WebServiceType.GET_RECIPE_LIST_SOLR));
            if (isInitialLoading) {
                progressLoadingCenter.setVisibility(View.VISIBLE);
                progressLoading.setVisibility(View.GONE);
            } else
                progressLoading.setVisibility(View.VISIBLE);
            isResponseReceived = false;
            WebDataServiceImpl.getInstance(mApp).getListSolr(IngredientResponse.class,
                    WebServiceType.GET_INGREDINET_LIST_SOLR, pageNum, size, searchTerm, this, this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_treatment:
                openAddNewTreatmentsFragment();
                break;
        }
    }

    private void openAddNewTreatmentsFragment() {

        AddNewTreatmentDialogFragment addNewTreatmentDetailFragment = new AddNewTreatmentDialogFragment(this);
        addNewTreatmentDetailFragment.show(mActivity.getSupportFragmentManager(),
                addNewTreatmentDetailFragment.getClass().getSimpleName());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = errorMessage;
        }
        progressLoadingCenter.setVisibility(View.GONE);
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
                    mActivity.hideLoading();
                    getIngredientList(true, PAGE_NUMBER, MAX_SIZE, "");
                    return;
                }
                break;
            case GET_INGREDINET_LIST_SOLR:
                ArrayList<IngredientResponse> list = (ArrayList<IngredientResponse>) (ArrayList<?>) response.getDataList();
                LogUtils.LOGD(TAG, "onResponse Treatment Size " + ingredientListSolr.size() + " isDataFromLocal " + response.isDataFromLocal());
                isResponseReceived = true;
                if (ingredientListSolr == null)
                    ingredientListSolr = new ArrayList<>();
                if (isLoadingFromSearch) {
                    ingredientListSolr.clear();
                    notifyAdapter(ingredientListSolr);
                }
                if (!Util.isNullOrEmptyList(list)) {
                    ingredientListSolr.addAll(list);
                }
                if (Util.isNullOrEmptyList(list) || list.size() < MAX_SIZE || Util.isNullOrEmptyList(list))
                    isEndOfListAchieved = true;

                notifyAdapter(ingredientListSolr);
                progressLoadingCenter.setVisibility(View.GONE);
                progressLoading.setVisibility(View.GONE);
                isLoadingFromSearch = false;
                isInitialLoading = false;
                break;
            default:
                break;
        }
        progressLoadingCenter.setVisibility(View.GONE);
        mActivity.hideLoading();
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

            Util.checkNetworkStatus(mActivity);
            if (HealthCocoConstants.isNetworkOnline) {
                PAGE_NUMBER = 0;
                isLoadingFromSearch = true;
                isEndOfListAchieved = false;
                getIngredientList(false, PAGE_NUMBER, MAX_SIZE, search);
            } else
                Util.showToast(mActivity, R.string.user_offline);

        }
        lastTextSearched = search;
    }


    public void loadMore(int current_page) {
        if (isResponseReceived)
            if (!isEndOfListAchieved && !isInitialLoading && !isLoadingFromSearch) {
                PAGE_NUMBER++;
                getIngredientList(false, PAGE_NUMBER, MAX_SIZE, getSearchEditTextValue());
            }
    }


    @Override
    public void onRefresh() {
        getIngredientList(true, PAGE_NUMBER, MAX_SIZE, getSearchEditTextValue());
    }

    @Override
    public void onSaveClicked(Object treatmentService) {
        ingredientRecyclerView.smoothScrollToPosition(0);
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
        isLoadingFromSearch = false;
        clearSearchEditText();
        getIngredientList(false, PAGE_NUMBER, MAX_SIZE, "");
    }
}
