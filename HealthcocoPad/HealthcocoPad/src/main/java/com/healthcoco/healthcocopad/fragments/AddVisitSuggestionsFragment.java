package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.healthcoco.healthcocopad.bean.server.AdviceSuggestion;
import com.healthcoco.healthcocopad.bean.server.ComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.EarsExamSuggestions;
import com.healthcoco.healthcocopad.bean.server.EcgDetailSuggestions;
import com.healthcoco.healthcocopad.bean.server.EchoSuggestions;
import com.healthcoco.healthcocopad.bean.server.GeneralExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.HistoryPresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.HolterSuggestions;
import com.healthcoco.healthcocopad.bean.server.IndicationOfUsgSuggestions;
import com.healthcoco.healthcocopad.bean.server.IndirectLarygoscopyExamSuggestions;
import com.healthcoco.healthcocopad.bean.server.InvestigationSuggestions;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MenstrualHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.NeckExamSuggestions;
import com.healthcoco.healthcocopad.bean.server.NoseExamSuggestions;
import com.healthcoco.healthcocopad.bean.server.NotesSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObservationSuggestions;
import com.healthcoco.healthcocopad.bean.server.ObstetricHistorySuggestions;
import com.healthcoco.healthcocopad.bean.server.OralCavityThroatExamSuggestions;
import com.healthcoco.healthcocopad.bean.server.PaSuggestions;
import com.healthcoco.healthcocopad.bean.server.PcEarsSuggestions;
import com.healthcoco.healthcocopad.bean.server.PcNoseSuggestions;
import com.healthcoco.healthcocopad.bean.server.PcOralCavitySuggestions;
import com.healthcoco.healthcocopad.bean.server.PcThroatSuggestions;
import com.healthcoco.healthcocopad.bean.server.PresentComplaintSuggestions;
import com.healthcoco.healthcocopad.bean.server.ProcedureNoteSuggestions;
import com.healthcoco.healthcocopad.bean.server.ProvisionalDiagnosisSuggestions;
import com.healthcoco.healthcocopad.bean.server.PsSuggestions;
import com.healthcoco.healthcocopad.bean.server.PvSuggestions;
import com.healthcoco.healthcocopad.bean.server.SystemicExaminationSuggestions;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.XrayDetailSuggestions;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddNewDrugDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddNewSuggestionDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddNewTreatmentDialogFragment;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.SuggestionType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewDrugListener;
import com.healthcoco.healthcocopad.listeners.AddNewSuggestionListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.GridViewLoadMore;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.healthcoco.healthcocopad.enums.WebServiceType.FRAGMENT_INITIALISATION;

/**
 * Created by neha on 15/04/17.
 */

public class AddVisitSuggestionsFragment extends HealthCocoFragment implements TextWatcher,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised,
        LoadMorePageListener, View.OnClickListener, AddNewDrugListener, AdapterView.OnItemClickListener, View.OnTouchListener, HealthcocoTextWatcherListener, AddNewSuggestionListener {

    public static final String INTENT_LOAD_DATA = "com.healthcoco.healthcocopad.fragments.AddVisitSuggestionsFragment.LOAD_DATA";

    public static final int MAX_SIZE = 25;
    public static final String TAG_SUGGESTIONS_TYPE = "suggestionType";
    public static final String TAG_SEARCHED_TERM = "searchedTerm";
    public static int PAGE_NUMBER = 0;
    private GridViewLoadMore gvSuggestionsList;
    private boolean isEndOfListAchieved = false;
    private List<Object> suggestionsList = new ArrayList<>();
    private AddVisitSuggestionsListAdapter adapterSolr;
    private ProgressBar progressLoading;
    private boolean isLoadingFromSearch;
    private String lastTextSearched;
    private boolean isInitialLoading = true;
    private LinearLayout btAddNew;
    private User user;
    private boolean receiversRegistered;
    private SuggestionType suggestionType;
    private TextView tvHeaderTitle;
    private String searchedTerm;
    private LinearLayout parentEditSearchView;
    private EditText editTextSearch;
    private TextView tvNoDrugs;
    private boolean visitToggleStateFromPreferences;
    private ListViewLoadMore lvSuggestionsList;
    BroadcastReceiver loadDataReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null) {
                int ordinal = intent.getIntExtra(TAG_SUGGESTIONS_TYPE, -1);
                searchedTerm = intent.getStringExtra(TAG_SEARCHED_TERM);
                suggestionType = SuggestionType.values()[ordinal];
                btAddNew.setVisibility(View.VISIBLE);
                if (suggestionType != null) {
                    isLoadingFromSearch = true;
                    resetListAndPagingAttributes();
                    refreshData(false);
                }
            }
        }
    };
    private AddEditNormalVisitsFragment addEditNormalVisitsFragment;
    private AddClinicalNotesVisitNormalFragment addClinicalNotesVisitNormalFragment;

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
        visitToggleStateFromPreferences = Util.getVisitToggleStateFromPreferences(mActivity);
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
        gvSuggestionsList = (GridViewLoadMore) view.findViewById(R.id.gv_suggestions_list);
        lvSuggestionsList = (ListViewLoadMore) view.findViewById(R.id.lv_suggestions_list);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        btAddNew = (LinearLayout) view.findViewById(R.id.bt_add_new);
        tvNoDrugs = (TextView) view.findViewById(R.id.tv_no_drugs);
//        btAddNew.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        if (visitToggleStateFromPreferences)
            editTextSearch = initEditSearchView(R.string.search, (View.OnClickListener) this);
        else {
            editTextSearch = initEditSearchView(R.string.search, this, this);
            addEditNormalVisitsFragment = (AddEditNormalVisitsFragment) mFragmentManager.findFragmentByTag(AddEditNormalVisitsFragment.class.getSimpleName());
            if (addEditNormalVisitsFragment != null)
                addClinicalNotesVisitNormalFragment = (AddClinicalNotesVisitNormalFragment) addEditNormalVisitsFragment.getCurrentTabFragment(0);
            else
                addClinicalNotesVisitNormalFragment = (AddClinicalNotesVisitNormalFragment) mFragmentManager.findFragmentByTag(AddClinicalNotesVisitNormalFragment.class.getSimpleName());
            if (addClinicalNotesVisitNormalFragment != null) {
                editTextSearch.setOnTouchListener(this);
                editTextSearch.addTextChangedListener(new HealthcocoTextWatcher(editTextSearch, this));
            }
        }
        btAddNew.setOnClickListener(this);
        gvSuggestionsList.setOnItemClickListener(this);
        lvSuggestionsList.setOnItemClickListener(this);
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
            } else
                Util.showToast(mActivity, R.string.user_offline);
        }
        lastTextSearched = search;
    }

    private void initAdapters() {
        adapterSolr = new AddVisitSuggestionsListAdapter(mActivity);
        gvSuggestionsList.setAdapter(adapterSolr);
        lvSuggestionsList.setAdapter(adapterSolr);
    }

    private void notifyAdapter(List<Object> list) {
        View visibleView = getChangedView(visitToggleStateFromPreferences);
        if (!Util.isNullOrEmptyList(list)) {
            LogUtils.LOGD(TAG, "onResponse DrugsList notifyAdapter " + list.size());
            visibleView.setVisibility(View.VISIBLE);
            tvNoDrugs.setVisibility(View.GONE);
        } else {
            visibleView.setVisibility(View.GONE);
            tvNoDrugs.setVisibility(View.VISIBLE);
        }
        adapterSolr.setListData(list);
        adapterSolr.notifyDataSetChanged();
    }

    private View getChangedView(boolean visitToggleStateFromPreferences) {
        if (visitToggleStateFromPreferences)
            return gvSuggestionsList;
        else return lvSuggestionsList;
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        ArrayList<Object> responseList = null;
        if (response != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    break;
                case GET_PRESENT_COMPLAINT_SUGGESTIONS:
                case GET_COMPLAINT_SUGGESTIONS:
                case GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS:
                case GET_MENSTRUAL_HISTORY_SUGGESTIONS:
                case GET_OBSTETRIC_HISTORY_SUGGESTIONS:
                case GET_GENERAL_EXAMINATION_SUGGESTIONS:
                case GET_SYSTEMIC_EXAMINATION_SUGGESTIONS:
                case GET_OBSERVATION_SUGGESTIONS:
                case GET_INVESTIGATION_SUGGESTIONS:
                case GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS:
                case GET_DIAGNOSIS_SUGGESTIONS:
                case GET_ECG_SUGGESTIONS:
                case GET_ECHO_SUGGESTIONS:
                case GET_XRAY_SUGGESTIONS:
                case GET_HOLTER_SUGGESTIONS:
                case GET_PA_SUGGESTIONS:
                case GET_PV_SUGGESTIONS:
                case GET_PS_SUGGESTIONS:
                case GET_INDICATION_OF_USG_SUGGESTIONS:
                case GET_NOTES_SUGGESTIONS:
                case GET_EAR_EXAM_SUGGESTIONS:
                case GET_NECK_EXAM_SUGGESTIONS:
                case GET_NOSE_EXAM_SUGGESTIONS:
                case GET_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS:
                case GET_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS:
                case GET_PC_EARS_SUGGESTIONS:
                case GET_PC_NOSE_SUGGESTIONS:
                case GET_PC_ORAL_CAVITY_SUGGESTIONS:
                case GET_PC_THROAT_SUGGESTIONS:
                case GET_PROCEDURE_NOTE_SUGGESTIONS:
                case GET_DRUGS_LIST_SOLR:
                case GET_DIAGNOSTIC_TESTS_SOLR:
                case GET_SEARCH_ADVICE_SOLR:
                    responseList = response.getDataList();
                    break;
                default:
                    break;
            }
        }
        if (suggestionsList == null)
            suggestionsList = new ArrayList<>();
        if (isLoadingFromSearch) {
//            suggestionsList.clear();
//            notifyAdapter(suggestionsList);
        }
        if (!Util.isNullOrEmptyList(responseList)) {
            suggestionsList.clear();
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
            case GET_PRESENT_COMPLAINT_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_PRESENT_COMPLAINT_SUGGESTIONS,
                        PresentComplaintSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
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
            case GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS,
                        HistoryPresentComplaintSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_MENSTRUAL_HISTORY_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_MENSTRUAL_HISTORY_SUGGESTIONS,
                        MenstrualHistorySuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_OBSTETRIC_HISTORY_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_OBSTETRIC_HISTORY_SUGGESTIONS,
                        ObstetricHistorySuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_GENERAL_EXAMINATION_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_GENERAL_EXAMINATION_SUGGESTIONS,
                        GeneralExaminationSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_SYSTEMIC_EXAMINATION_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_SYSTEMIC_EXAMINATION_SUGGESTIONS,
                        SystemicExaminationSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS,
                        ProvisionalDiagnosisSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_ECG_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_ECG_SUGGESTIONS,
                        EcgDetailSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_ECHO_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_ECHO_SUGGESTIONS,
                        EchoSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_XRAY_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_XRAY_SUGGESTIONS,
                        XrayDetailSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_HOLTER_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_HOLTER_SUGGESTIONS,
                        HolterSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_PA_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_PA_SUGGESTIONS,
                        PaSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_PV_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_PV_SUGGESTIONS,
                        PvSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_PS_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_PS_SUGGESTIONS,
                        PsSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_INDICATION_OF_USG_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_INDICATION_OF_USG_SUGGESTIONS,
                        IndicationOfUsgSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_NOTES_SUGGESTION:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_NOTES_SUGGESTIONS,
                        NotesSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_EAR_EXAM_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_EAR_EXAM_SUGGESTIONS,
                        EarsExamSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_NECK_EXAM_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_NECK_EXAM_SUGGESTIONS,
                        NeckExamSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_NOSE_EXAM_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_NOSE_EXAM_SUGGESTIONS,
                        NoseExamSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS,
                        IndirectLarygoscopyExamSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS,
                        OralCavityThroatExamSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_PC_EARS_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_PC_EARS_SUGGESTIONS,
                        PcEarsSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_PC_NOSE_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_PC_NOSE_SUGGESTIONS,
                        PcNoseSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_PC_ORAL_CAVITY_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_PC_ORAL_CAVITY_SUGGESTIONS,
                        PcOralCavitySuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_PC_THROAT_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_PC_THROAT_SUGGESTIONS,
                        PcThroatSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;
            case GET_PROCEDURE_NOTE_SUGGESTIONS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_PROCEDURE_NOTE_SUGGESTIONS,
                        ProcedureNoteSuggestions.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
                break;

            case GET_SEARCH_ADVICE_SOLR:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getSuggestionsListAsResponse(WebServiceType.GET_SEARCH_ADVICE_SOLR,
                        AdviceSuggestion.class, suggestionType, searchedTerm, PAGE_NUMBER, MAX_SIZE, null, null);
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
        gvSuggestionsList.resetPreLastPosition(0);
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
                if (suggestionType != null) {
                    switch (suggestionType) {
                        case DRUGS:
                            AddNewDrugDialogFragment newDrugDialogFragment = new AddNewDrugDialogFragment(this);
                            newDrugDialogFragment.show(mActivity.getSupportFragmentManager(),
                                    newDrugDialogFragment.getClass().getSimpleName());
                            break;

                        case COMPLAINTS:
                        case OBSERVATION:
                        case INVESTIGATION:
                        case DIAGNOSIS:
                        case PRESENT_COMPLAINT:
                        case HISTORY_OF_PRESENT_COMPLAINT:
                        case MENSTRUAL_HISTORY:
                        case OBSTETRIC_HISTORY:
                        case PROVISIONAL_DIAGNOSIS:
                        case GENERAL_EXAMINATION:
                        case SYSTEMIC_EXAMINATION:
                        case NOTES:
                        case ECG_DETAILS:
                        case ECHO:
                        case X_RAY_DETAILS:
                        case HOLTER:
                        case PA:
                        case PS:
                        case PV:
                        case INDICATION_OF_USG:
                        case EARS_EXAM:
                        case NOSE_EXAM:
                        case ORAL_CAVITY_THROAT_EXAM:
                        case INDIRECT_LARYGOSCOPY_EXAM:
                        case NECK_EXAM:
                        case PC_EARS:
                        case PC_NOSE:
                        case PC_ORAL_CAVITY:
                        case PC_THROAT:
                        case PROCEDURES:
                        case ADVICE:
                            openAddNewSuggestionDailogFragment();
                            break;
//                    case PRESENT_COMPLAINT:
//                        mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_DIAGNOSTIC_TESTS, AddUpdateNameDialogType.ADD_DIAGNOSTIC_TEST, this, user, null, HealthCocoConstants.RESULT_CODE_DIAGNOSTICS_TESTS);
                        default:
                            break;
                    }
                }
                break;
            case R.id.bt_clear:
                if (visitToggleStateFromPreferences) {
                    MyScriptAddVisitsFragment myScriptAddVisitsFragment = (MyScriptAddVisitsFragment) mFragmentManager.findFragmentByTag(MyScriptAddVisitsFragment.class.getSimpleName());
                    if (myScriptAddVisitsFragment != null)
                        myScriptAddVisitsFragment.onClearButtonClick();
                } else clearSearchEditText();
                break;
        }
    }

    @Override
    public void onSaveClicked(Object drug) {
        refreshData(true);
    }

    @Override
    public SuggestionType getSuggestionType() {
        return suggestionType;
    }

    private void openAddNewSuggestionDailogFragment() {

        AddNewSuggestionDialogFragment addNewSuggestionDialogFragment = new AddNewSuggestionDialogFragment(this);
        addNewSuggestionDialogFragment.show(mActivity.getSupportFragmentManager(),
                addNewSuggestionDialogFragment.getClass().getSimpleName());

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
                parentEditSearchView.setVisibility(View.VISIBLE);
                btAddNew.setVisibility(View.GONE);
                tvNoDrugs.setText(R.string.no_lab_test);
                break;
            case DRUGS:
                parentEditSearchView.setVisibility(View.VISIBLE);
                btAddNew.setVisibility(View.GONE);
                tvNoDrugs.setText(R.string.no_drug_history);
                break;
            default:
                if (visitToggleStateFromPreferences) {
                    parentEditSearchView.setVisibility(View.GONE);
                    btAddNew.setVisibility(View.VISIBLE);
                } else {
                    parentEditSearchView.setVisibility(View.VISIBLE);
//                    btAddNew.setVisibility(View.VISIBLE);
                    switch (suggestionType) {
                        case LAB_TESTS:
                            break;
                        case DRUGS:
                            btAddNew.setVisibility(View.VISIBLE);
                            break;
                        default:
                            btAddNew.setVisibility(View.VISIBLE);
                            break;
                    }
                }
                tvNoDrugs.setText(R.string.no_list_available);
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
            case PRESENT_COMPLAINT:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PRESENT_COMPLAINT_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case HISTORY_OF_PRESENT_COMPLAINT:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_HISTORY_OF_PRESENT_COMPLAINT_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case MENSTRUAL_HISTORY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_MENSTRUAL_HISTORY_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case OBSTETRIC_HISTORY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_OBSTETRIC_HISTORY_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case PROVISIONAL_DIAGNOSIS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PROVISIONAL_DIAGNOSIS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case GENERAL_EXAMINATION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_GENERAL_EXAMINATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case SYSTEMIC_EXAMINATION:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_SYSTEMIC_EXAMINATION_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case NOTES:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_NOTES_SUGGESTION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case ECG_DETAILS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_ECG_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case ECHO:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_ECHO_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case X_RAY_DETAILS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_XRAY_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case HOLTER:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_HOLTER_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case PA:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PA_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case PS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case PV:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PV_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case INDICATION_OF_USG:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_INDICATION_OF_USG_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case EARS_EXAM:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_EAR_EXAM_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case NOSE_EXAM:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_NOSE_EXAM_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case INDIRECT_LARYGOSCOPY_EXAM:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_INDIRECT_LARYGOSCOPY_EXAM_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case NECK_EXAM:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_NECK_EXAM_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case ORAL_CAVITY_THROAT_EXAM:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_ORAL_CAVITY_THROAT_EXAM_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case PC_EARS:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PC_EARS_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case PC_NOSE:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PC_NOSE_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case PC_ORAL_CAVITY:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PC_ORAL_CAVITY_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case PC_THROAT:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PC_THROAT_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case PROCEDURES:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PROCEDURE_NOTE_SUGGESTIONS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case ADVICE:
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_SEARCH_ADVICE_SOLR, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            default:
                onResponse(null);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent;
        if (visitToggleStateFromPreferences) {
            intent = new Intent(MyScriptAddVisitsFragment.INTENT_ON_SUGGESTION_ITEM_CLICK);
        } else {
            switch (suggestionType) {
                case ADVICE:
                    intent = new Intent(AddEditNormalVisitPrescriptionFragment.INTENT_ON_SUGGESTION_ITEM_CLICK);
                    break;
                default:
                    intent = new Intent(AddClinicalNotesVisitNormalFragment.INTENT_ON_SUGGESTION_ITEM_CLICK);
                    break;
            }
        }
        intent.putExtra(TAG_SUGGESTIONS_TYPE, suggestionType.ordinal());
        intent.putExtra(MyScriptAddVisitsFragment.TAG_SELECTED_SUGGESTION_OBJECT, Parcels.wrap(adapterSolr.getItem(position)));
        LocalBroadcastManager.getInstance(mApp).sendBroadcast(intent);
    }

    public void refreshTagOfEditText(SuggestionType suggestionType) {
        editTextSearch = initEditSearchView(suggestionType.getSearchHintId(), (View.OnClickListener) this);
        editTextSearch.setTag(suggestionType);
        editTextSearch.setText("");
        if (visitToggleStateFromPreferences) {
            MyScriptAddVisitsFragment myScriptAddVisitsFragment = (MyScriptAddVisitsFragment) mFragmentManager.findFragmentByTag(MyScriptAddVisitsFragment.class.getSimpleName());
            if (myScriptAddVisitsFragment != null)
                myScriptAddVisitsFragment.requestFocus(editTextSearch);
        }
        Util.requesFocus(editTextSearch);
    }

    @Override
    public void afterTextChange(View v, String s) {
        if (!s.isEmpty())
            addClinicalNotesVisitNormalFragment.refreshSuggestionsList(v, s);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                LogUtils.LOGD(TAG, "Action UP");
                break;
            case MotionEvent.ACTION_DOWN:
                addClinicalNotesVisitNormalFragment.requestFocus(v);
                LogUtils.LOGD(TAG, "Action DOWN");
                break;
        }
        return false;
    }
}

