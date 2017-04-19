package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.AddVisitSuggestionsListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddNewDrugDialogFragment;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewDrugListener;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.healthcoco.healthcocopad.enums.WebServiceType.FRAGMENT_INITIALISATION;

/**
 * Created by neha on 15/04/17.
 */

public class AddVisitSuggestionsFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised,
        LoadMorePageListener, View.OnClickListener, AddNewDrugListener, AdapterView.OnItemClickListener {
    public static final String INTENT_LOAD_DATA = "com.healthcoco.healthcocopad.fragments.AddVisitSuggestionsFragment.LOAD_DATA";

    public static final int MAX_SIZE = 25;
    public static final String TAG_SUGGESTIONS_TYPE = "suggestionType";
    public static final String TAG_SEARCHED_TERM = "searchedTerm";
    public static int PAGE_NUMBER = 0;
    private ListViewLoadMore lvSuggestionsList;
    private boolean isEndOfListAchieved = false;
    private List<Object> suggestionsList = new ArrayList<>();
    private AddVisitSuggestionsListAdapter adapterSolr;
    private ProgressBar progressLoading;
    private boolean isLoadingFromSearch;
    private String lastTextSearched;
    private boolean isInitialLoading = true;
    private ImageButton btAddNew;
    private User user;
    private boolean receiversRegistered;
    private SuggestionType suggestionType;
    private TextView tvHeaderTitle;
    private String searchedTerm;
    private LinearLayout parentEditSearchView;
    private EditText editTextSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_visit_suggestions, container, false);
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
        showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        parentEditSearchView = (LinearLayout) view.findViewById(R.id.parent_edit_search_view);
        tvHeaderTitle = (TextView) view.findViewById(R.id.tv_header_title);
        lvSuggestionsList = (ListViewLoadMore) view.findViewById(R.id.lv_suggestions_list);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        btAddNew = (ImageButton) view.findViewById(R.id.bt_add_new);
    }

    @Override
    public void initListeners() {
        editTextSearch = initEditSearchView(R.string.search);
        AddVisitsFragment addVisitsFragment = (AddVisitsFragment) mFragmentManager.findFragmentByTag(AddVisitsFragment.class.getSimpleName());
        if (addVisitsFragment != null)
            editTextSearch.setOnFocusChangeListener(addVisitsFragment.getFocusChangeListener());
        btAddNew.setOnClickListener(this);
        lvSuggestionsList.setOnItemClickListener(this);
    }

    private void initAdapters() {
        adapterSolr = new AddVisitSuggestionsListAdapter(mActivity);
        lvSuggestionsList.setAdapter(adapterSolr);
    }

    private void notifyAdapter(List<Object> list) {
        if (!Util.isNullOrEmptyList(list)) {
            LogUtils.LOGD(TAG, "onResponse DrugsList notifyAdapter " + list.size());
            lvSuggestionsList.setVisibility(View.VISIBLE);
        } else {
            lvSuggestionsList.setVisibility(View.GONE);
        }
        adapterSolr.setListData(list);
        adapterSolr.notifyDataSetChanged();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        ArrayList<Object> responseList = null;
        if (response != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
//                    if (user != null)
//                        getSuggestionListFromServer(true, PAGE_NUMBER, MAX_SIZE, "");
                    break;
                case GET_COMPLAINT_SUGGESTIONS:
                case GET_OBSERVATION_SUGGESTIONS:
                case GET_INVESTIGATION_SUGGESTIONS:
                case GET_DIAGNOSIS_SUGGESTIONS:
                case GET_DRUGS_LIST_SOLR:
                case GET_DIAGNOSTIC_TESTS_SOLR:
                    responseList = response.getDataList();
                    break;

                default:
                    break;
            }
        }
        if (suggestionsList == null)
            suggestionsList = new ArrayList<>();
        if (isLoadingFromSearch) {
            suggestionsList.clear();
            notifyAdapter(suggestionsList);
        }
        if (!Util.isNullOrEmptyList(responseList)) {
            suggestionsList.addAll(responseList);
        }
        if (Util.isNullOrEmptyList(responseList) || responseList.size() < MAX_SIZE || Util.isNullOrEmptyList(responseList))
            isEndOfListAchieved = true;

        notifyAdapter(suggestionsList);
        mActivity.hideLoading();
        progressLoading.setVisibility(View.GONE);
        isLoadingFromSearch = false;
        isInitialLoading = false;
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
                volleyResponseBean.setWebServiceType(FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                }
                break;
            case GET_COMPLAINT_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_COMPLAINT_SUGGESTIONS,
                        ComplaintSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_OBSERVATION_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_OBSERVATION_SUGGESTIONS,
                        ObservationSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_INVESTIGATION_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_INVESTIGATION_SUGGESTIONS,
                        InvestigationSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_DIAGNOSIS_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_DIAGNOSIS_SUGGESTIONS,
                        DiagnosisSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
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

    private void resetListAndPagingAttributes() {
        if (suggestionsList != null)
            suggestionsList.clear();
        PAGE_NUMBER = 0;
        isEndOfListAchieved = false;
        isLoadingFromSearch = false;
        lvSuggestionsList.resetPreLastPosition(0);
        notifyAdapter(suggestionsList);
    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved && !isInitialLoading && !isLoadingFromSearch) {
            PAGE_NUMBER = PAGE_NUMBER + 1;
            refreshData(false);
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_add_new:
                AddNewDrugDialogFragment newDrugDialogFragment = new AddNewDrugDialogFragment(this);
                newDrugDialogFragment.show(mActivity.getSupportFragmentManager(),
                        newDrugDialogFragment.getClass().getSimpleName());
                break;
        }
    }

    @Override
    public void onSaveClicked(Drug drug) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            LogUtils.LOGD(TAG, "onResume " + receiversRegistered);

            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_LOAD_DATA);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(loadDataReceiver, filter);
            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(loadDataReceiver);
    }


    BroadcastReceiver loadDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                int ordinal = intent.getIntExtra(TAG_SUGGESTIONS_TYPE, -1);
                searchedTerm = intent.getStringExtra(TAG_SEARCHED_TERM);
                suggestionType = SuggestionType.values()[ordinal];
                if (suggestionType != null) {
                    resetListAndPagingAttributes();
                    refreshData(true);
                }
            }
        }
    };

    //
//    @Override
//    public void afterTextChanged(Editable s) {
//        String search = String.valueOf(s).toUpperCase(Locale.ENGLISH);
//        if (lastTextSearched == null || !lastTextSearched.equalsIgnoreCase(search)) {
//            //sorting solar drugs list from server
//            Util.checkNetworkStatus(mActivity);
//            if (HealthCocoConstants.isNetworkOnline) {
//                PAGE_NUMBER = 0;
//                isLoadingFromSearch = true;
//                isEndOfListAchieved = false;
//                resetListAndPagingAttributes();
//                LogUtils.LOGD(TAG, "TextChange afterTextChange");
//                refreshData(false);
//            } else
//                Util.showToast(mActivity, R.string.user_offline);
//        }
//        lastTextSearched = search;
//    }
    private void refreshData(boolean isInitialLoading) {
        refreshViewsAndTitle();
        mApp.cancelPendingRequests(String.valueOf(WebServiceType.GET_DRUGS_LIST_SOLR));
        if (isInitialLoading) {
            mActivity.showLoading(false);
            progressLoading.setVisibility(View.GONE);
        } else
            progressLoading.setVisibility(View.VISIBLE);
        if (suggestionType.isFromServerList())
            getSuggestionListFromServer();
        else
            getListFromLocal();
    }

    private void refreshViewsAndTitle() {
        tvHeaderTitle.setText(suggestionType.getHeaderTitleId());
        switch (suggestionType) {
            case LAB_TESTS:
            case DRUGS:
                parentEditSearchView.setVisibility(View.VISIBLE);
                break;
            default:
                parentEditSearchView.setVisibility(View.GONE);
                break;
        }
    }

    public void getSuggestionListFromServer() {
        switch (suggestionType) {
            case DRUGS:
                WebDataServiceImpl.getInstance(mApp).getDrugsListSolr(DrugsListSolrResponse.class, PAGE_NUMBER, MAX_SIZE, user.getUniqueId(), user.getForeignHospitalId(), user.getForeignLocationId(), getSearchEditTextValue(), this, this);
                break;
            case LAB_TESTS:
                WebDataServiceImpl.getInstance(mApp).getDiagnosticTestsFromSolr(DiagnosticTest.class, user.getForeignLocationId(), user.getForeignHospitalId(), PAGE_NUMBER, MAX_SIZE, getSearchEditTextValue(), this, this);
                break;
        }
    }

    private void getListFromLocal() {
        switch (suggestionType) {
            case COMPLAINTS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_COMPLAINT_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case OBSERVATION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_OBSERVATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case INVESTIGATION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_INVESTIGATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case DIAGNOSIS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DIAGNOSIS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            default:
                onResponse(null);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(AddVisitsFragment.INTENT_ON_SUGGESTION_ITEM_CLICK);
        intent.putExtra(TAG_SUGGESTIONS_TYPE, suggestionType.ordinal());
        intent.putExtra(AddVisitsFragment.TAG_SELECTED_SUGGESTION_OBJECT, Parcels.wrap(adapterSolr.getItem(position)));
        LocalBroadcastManager.getInstance(mApp).sendBroadcast(intent);
    }

    public void refreshTagOfEditText(SuggestionType suggestionType) {
        editTextSearch = initEditSearchView(suggestionType.getSearchHintId());
        editTextSearch.setText("");
        editTextSearch.setTag(suggestionType);
        Util.requesFocus(editTextSearch);
    }

    public SuggestionType getSelectedSuggestionType() {
        return suggestionType;
    }
}

