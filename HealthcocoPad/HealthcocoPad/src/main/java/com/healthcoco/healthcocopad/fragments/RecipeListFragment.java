package com.healthcoco.healthcocopad.fragments;

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
import com.healthcoco.healthcocopad.adapter.RecipeListSolrAdapter;
import com.healthcoco.healthcocopad.adapter.TreatmentsListSolrAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RecipeResponse;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddNewTreatmentDialogFragment;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewDrugListener;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Prashant on 28-03-2017.
 */
public class RecipeListFragment extends HealthCocoFragment implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener,
        TextWatcher, LocalDoInBackgroundListenerOptimised,
        LoadMorePageListener, SwipeRefreshLayout.OnRefreshListener, AddNewDrugListener {

    //variables need for pagination
    public static final int MAX_SIZE = 25;
    SelectedTreatmentItemClickListener treatmentItemClickListener;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private boolean isLoadingFromSearch;
    private boolean isInitialLoading;
    //other variables
    private ProgressBar progressLoading;
    private ListViewLoadMore lvRecipes;
    private RecipeListSolrAdapter adapter;
    private ImageButton btAddNewTemplate;
    private List<RecipeResponse> recipeListSolr = new ArrayList<RecipeResponse>();
    private TextView tvNoRecipe;
    private User user;
    private String lastTextSearched;

    public RecipeListFragment() {
    }

    public RecipeListFragment(SelectedTreatmentItemClickListener treatmentItemClickListener) {
        this.treatmentItemClickListener = treatmentItemClickListener;
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
        lvRecipes = (ListViewLoadMore) view.findViewById(R.id.lv_recipe);
        tvNoRecipe = (TextView) view.findViewById(R.id.tv_no_recipe);
        btAddNewTemplate = (ImageButton) view.findViewById(R.id.bt_add_recipe);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        initEditSearchView(R.string.search_recipe, this, this);

        btAddNewTemplate.setVisibility(View.VISIBLE);
        lvRecipes.setVisibility(View.VISIBLE);
    }


    @Override
    public void initListeners() {
        btAddNewTemplate.setOnClickListener(this);
        lvRecipes.setLoadMoreListener(this);
    }

    private void initAdapters() {
        adapter = new RecipeListSolrAdapter(mActivity, treatmentItemClickListener);
        lvRecipes.setAdapter(adapter);
    }

    private void notifyAdapter(List<RecipeResponse> recipeResponseList) {
        if (!Util.isNullOrEmptyList(recipeResponseList)) {
            lvRecipes.setVisibility(View.VISIBLE);
            tvNoRecipe.setVisibility(View.GONE);
        } else {
            lvRecipes.setVisibility(View.GONE);
            tvNoRecipe.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        adapter.setListData(recipeResponseList);
        adapter.notifyDataSetChanged();
    }

    private void getRecipeList(boolean isInitialLoading, int pageNum, int size, String searchTerm) {
        if (user != null) {
            mApp.cancelPendingRequests(String.valueOf(WebServiceType.GET_RECIPE_LIST_SOLR));
            if (isInitialLoading) {
                mActivity.showLoading(false);
                progressLoading.setVisibility(View.GONE);
            } else
                progressLoading.setVisibility(View.VISIBLE);

            WebDataServiceImpl.getInstance(mApp).getRecipeListSolr(RecipeResponse.class,
                    WebServiceType.GET_RECIPE_LIST_SOLR, pageNum, size, searchTerm, this, this);
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
                    getRecipeList(true, PAGE_NUMBER, MAX_SIZE, "");
                    return;
                }
                break;
            case GET_RECIPE_LIST_SOLR:
                ArrayList<RecipeResponse> list = (ArrayList<RecipeResponse>) (ArrayList<?>) response.getDataList();
                LogUtils.LOGD(TAG, "onResponse Treatment Size " + recipeListSolr.size() + " isDataFromLocal " + response.isDataFromLocal());
                if (recipeListSolr == null)
                    recipeListSolr = new ArrayList<>();
                if (isLoadingFromSearch) {
                    recipeListSolr.clear();
                    notifyAdapter(recipeListSolr);
                }
                if (!Util.isNullOrEmptyList(list)) {
                    recipeListSolr.addAll(list);
                }
                if (Util.isNullOrEmptyList(list) || list.size() < MAX_SIZE || Util.isNullOrEmptyList(list))
                    isEndOfListAchieved = true;

                notifyAdapter(recipeListSolr);
                mActivity.hideLoading();
                progressLoading.setVisibility(View.GONE);
                isLoadingFromSearch = false;
                isInitialLoading = false;
                break;
            default:
                break;
        }
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
                getRecipeList(false, PAGE_NUMBER, MAX_SIZE, search);
            } else
                Util.showToast(mActivity, R.string.user_offline);

        }
        lastTextSearched = search;
    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading && !isLoadingFromSearch) {
            PAGE_NUMBER++;
            getRecipeList(false, PAGE_NUMBER, MAX_SIZE, getSearchEditTextValue());
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }


    @Override
    public void onRefresh() {
        getRecipeList(true, PAGE_NUMBER, MAX_SIZE, getSearchEditTextValue());
    }

    @Override
    public void onSaveClicked(Object treatmentService) {
        lvRecipes.smoothScrollToPosition(0);
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
        isLoadingFromSearch = false;
        clearSearchEditText();
        getRecipeList(false, PAGE_NUMBER, MAX_SIZE, "");
    }
}
