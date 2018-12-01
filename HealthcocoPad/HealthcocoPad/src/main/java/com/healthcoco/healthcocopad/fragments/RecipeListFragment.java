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
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RecipeResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewDrugListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.RecipeItemListener;
import com.healthcoco.healthcocopad.listeners.SelectedRecipeItemClickListener;
import com.healthcoco.healthcocopad.recyclerview.EndlessRecyclerViewScrollListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.FontAwesomeButton;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType.ADD_RECIPE;

/**
 * Created by Prashant on 28-03-2017.
 */
public class RecipeListFragment extends HealthCocoFragment implements View.OnClickListener,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener,
        TextWatcher, LocalDoInBackgroundListenerOptimised,
        SwipeRefreshLayout.OnRefreshListener, RecipeItemListener {

    //variables need for pagination
    public static final int MAX_SIZE = 25;
    private SelectedRecipeItemClickListener selectedRecipeItemClickListener;
    private int PAGE_NUMBER = 0;
    private boolean isEndOfListAchieved;
    private boolean isLoadingFromSearch;
    private boolean isInitialLoading;
    //other variables
    private ProgressBar progressLoadingCenter;
    private ProgressBar progressLoading;
    private RecyclerView recipeRecyclerView;
    private HealthcocoRecyclerViewAdapter mAdapter;
    private ImageButton btAddRecipe;
    private List<RecipeResponse> recipeListSolr = new ArrayList<RecipeResponse>();
    private TextView tvNoRecipe;
    private User user;
    private String lastTextSearched;
    private boolean isResponseReceived = false;
    private FontAwesomeButton btAddNew;

    public RecipeListFragment() {
    }

    public RecipeListFragment(SelectedRecipeItemClickListener selectedRecipeItemClickListener) {
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
        recipeRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_recipe);
        tvNoRecipe = (TextView) view.findViewById(R.id.tv_no_recipe);
        btAddRecipe = (ImageButton) view.findViewById(R.id.bt_add_recipe);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        progressLoadingCenter = (ProgressBar) view.findViewById(R.id.progress_loading_center);
        btAddNew = (FontAwesomeButton) view.findViewById(R.id.bt_add_new);
        initEditSearchView(R.string.search_recipe, this, this);

        btAddRecipe.setVisibility(View.VISIBLE);
        recipeRecyclerView.setVisibility(View.VISIBLE);

        if (selectedRecipeItemClickListener != null) {
            btAddRecipe.setVisibility(View.VISIBLE);
            btAddNew.setVisibility(View.GONE);
        } else {
            btAddRecipe.setVisibility(View.GONE);
            btAddNew.setVisibility(View.VISIBLE);
        }

        tvNoRecipe.setVisibility(View.GONE);
    }


    @Override
    public void initListeners() {
        btAddRecipe.setOnClickListener(this);
        btAddNew.setOnClickListener(this);
    }

    private void initAdapters() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        recipeRecyclerView.setLayoutManager(layoutManager);
        recipeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        if (selectedRecipeItemClickListener != null)
            mAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.RECIPE_ITEM_SOLR, selectedRecipeItemClickListener);
        else
            mAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.RECIPE_ITEM_SOLR, (RecipeItemListener) this);

        mAdapter.setListData((ArrayList<Object>) (Object) recipeListSolr);
        recipeRecyclerView.setAdapter(mAdapter);

        recipeRecyclerView.addOnScrollListener(new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                loadMore(current_page);
            }
        });

    }

    private void notifyAdapter(List<RecipeResponse> recipeResponseList) {
        if (!Util.isNullOrEmptyList(recipeResponseList)) {
            recipeRecyclerView.setVisibility(View.VISIBLE);
            tvNoRecipe.setVisibility(View.GONE);
        } else {
            recipeRecyclerView.setVisibility(View.GONE);
            tvNoRecipe.setVisibility(View.VISIBLE);
        }
        progressLoading.setVisibility(View.GONE);
        mAdapter.setListData((ArrayList<Object>) (Object) recipeResponseList);
        mAdapter.notifyDataSetChanged();
    }


    private void getRecipeList(boolean isInitialLoading, int pageNum, int size, String searchTerm) {
        if (user != null) {
            mApp.cancelPendingRequests(String.valueOf(WebServiceType.GET_RECIPE_LIST_SOLR));
            if (isInitialLoading) {
                progressLoadingCenter.setVisibility(View.VISIBLE);
                progressLoading.setVisibility(View.GONE);
            } else
                progressLoading.setVisibility(View.VISIBLE);
            isResponseReceived = false;
            WebDataServiceImpl.getInstance(mApp).getRecipeListSolr(RecipeResponse.class,
                    WebServiceType.GET_RECIPE_LIST_SOLR, pageNum, size, searchTerm, this, this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_recipe:
            case R.id.bt_add_new:
                openAddNewRecipeFragment();
                break;
        }
    }

    private void openAddNewRecipeFragment() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, ADD_RECIPE.ordinal());
        startActivity(intent);
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
                    mActivity.hideLoading();
                    getRecipeList(true, PAGE_NUMBER, MAX_SIZE, "");
                    return;
                }
                break;
            case GET_RECIPE_LIST_SOLR:
                ArrayList<RecipeResponse> list = (ArrayList<RecipeResponse>) (ArrayList<?>) response.getDataList();
                LogUtils.LOGD(TAG, "onResponse Treatment Size " + recipeListSolr.size() + " isDataFromLocal " + response.isDataFromLocal());
                isResponseReceived = true;
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
                getRecipeList(false, PAGE_NUMBER, MAX_SIZE, search);
            } else
                Util.showToast(mActivity, R.string.user_offline);

        }
        lastTextSearched = search;
    }


    public void loadMore(int current_page) {
        if (isResponseReceived)
            if (!isEndOfListAchieved && !isInitialLoading && !isLoadingFromSearch) {
                PAGE_NUMBER++;
                getRecipeList(false, PAGE_NUMBER, MAX_SIZE, getSearchEditTextValue());
            }
    }


    @Override
    public void onRefresh() {
        getRecipeList(true, PAGE_NUMBER, MAX_SIZE, getSearchEditTextValue());
    }

    @Override
    public void onEditClick(Object object) {
        RecipeResponse recipeResponse = (RecipeResponse) object;
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, ADD_RECIPE.ordinal());
        if (recipeResponse != null) {
            intent.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(recipeResponse));
        }
        startActivity(intent);
    }

    @Override
    public void onItemClicked(Object object) {
        RecipeResponse recipeResponse = (RecipeResponse) object;
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, ADD_RECIPE.ordinal());
        if (recipeResponse != null) {
            intent.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(recipeResponse));
            intent.putExtra(HealthCocoConstants.TAG_IS_FROM_RECIPE, true);
        }
        startActivity(intent);
    }

}
