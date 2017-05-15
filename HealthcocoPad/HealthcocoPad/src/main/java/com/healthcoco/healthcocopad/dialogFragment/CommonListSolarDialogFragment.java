package com.healthcoco.healthcocopad.dialogFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.adapter.CommonListDialogSolrAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.CollegeUniversityInstitute;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.EducationQualification;
import com.healthcoco.healthcocopad.bean.server.ForeignProfessionalMemberships;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MedicalCouncil;
import com.healthcoco.healthcocopad.bean.server.ProfessionalMembership;
import com.healthcoco.healthcocopad.bean.server.Reference;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ListViewLoadMore;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by neha on 03/02/16.
 */
public class CommonListSolarDialogFragment extends HealthCocoDialogFragment implements TextWatcher, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LoadMorePageListener, View.OnKeyListener, TextView.OnEditorActionListener, LocalDoInBackgroundListenerOptimised {
    private static final int MAX_SIZE = 25;
    private int PAGE_NUMBER = 0;
    private List<Object> list;
    private CommonListDialogType commonListDialogType;
    private CommonListDialogItemClickListener commonListDialogItemClickListener;
    private ListViewLoadMore listView;
    private CommonListDialogSolrAdapter mAdapter;
    private TextView tvNoResultFound;
    private ProgressBar progressLoading;
    private boolean isLoadingFromSearch;
    private boolean isEndOfListAchieved;
    private EditText editSearch;
    private LinearLayout loadingOverlay;
    private String lastTextSearched;
    private User user;

    public CommonListSolarDialogFragment() {
    }

    public CommonListSolarDialogFragment(CommonListDialogItemClickListener commonListDialogItemClickListener, CommonListDialogType commonListDialogType) {
        this.commonListDialogItemClickListener = commonListDialogItemClickListener;
        this.commonListDialogType = commonListDialogType;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_common_list_solr, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        setDialogToMatchParent();
        init();
    }


    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
        loadingOverlay.setVisibility(View.VISIBLE);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        listView = (ListViewLoadMore) view.findViewById(R.id.lv_list);
        tvNoResultFound = (TextView) view.findViewById(R.id.tv_no_result_found);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
        loadingOverlay = (LinearLayout) view.findViewById(R.id.loading_overlay);
        listView.setMinimumHeight(ScreenDimensions.SCREEN_HEIGHT);

        if (commonListDialogType.isAddCustomAllowed()) {
            getSearchEditText().setImeOptions(EditorInfo.IME_ACTION_DONE);
            editSearch = initEditSearchView(commonListDialogType.getHint(), this, null, this, false);
        } else
            editSearch = initEditSearchView(commonListDialogType.getHint(), this, false);
        tvNoResultFound.setText(commonListDialogType.getNoResultFoundTextId());
        editSearch.setSelection(editSearch.getText().length());
        Util.setFocusToEditText(mActivity, editSearch);
    }

    @Override
    public void initListeners() {
        listView.setLoadMoreListener(this);
    }

    @Override
    public void initData() {
        getList(true, PAGE_NUMBER, MAX_SIZE, "");
    }

    private void initAdapter() {
        mAdapter = new CommonListDialogSolrAdapter(mActivity, commonListDialogItemClickListener, this);
        mAdapter.setListData(commonListDialogType, list);
        listView.setAdapter(mAdapter);
    }

    private void getList(boolean isInitialLoading, int pageNum, int size, String searchTerm) {
        WebServiceType webServiceType = null;
        Class<?> class1 = null;
        switch (commonListDialogType) {
            case QUALIFICATION:
                webServiceType = WebServiceType.GET_EDUCATION_QUALIFICATION_SOLR;
                class1 = EducationQualification.class;
                break;
            case COLLEGE_UNIVERSITY_INSTITUTE:
                webServiceType = WebServiceType.GET_COLLEGE_UNIVERSITY_INSTITUES_SOLR;
                class1 = CollegeUniversityInstitute.class;
                break;
            case MEDICAL_COUNCIL:
                webServiceType = WebServiceType.GET_MEDICAL_COUNCILS_SOLR;
                class1 = MedicalCouncil.class;
                break;
            case PROFESSIONAL_MEMBERSHIP:
                webServiceType = WebServiceType.GET_PROFESSIONAL_MEMBERSHIP_SOLR;
                class1 = ProfessionalMembership.class;
                break;
            case DIAGNOSTIC_TESTS:
                webServiceType = WebServiceType.GET_DIAGNOSTIC_TESTS_SOLR;
                class1 = DiagnosticTest.class;
                break;
        }

        mApp.cancelPendingRequests(String.valueOf(webServiceType));
        if (isInitialLoading) {
            loadingOverlay.setVisibility(View.VISIBLE);
            progressLoading.setVisibility(View.GONE);
        } else
            progressLoading.setVisibility(View.VISIBLE);
        WebDataServiceImpl.getInstance(mApp).getMasterDataFromSolr(webServiceType, class1, user.getForeignLocationId(), user.getForeignHospitalId(), pageNum, size, searchTerm, this, this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String search = String.valueOf(s).toLowerCase(Locale.ENGLISH);
        if (lastTextSearched == null || !lastTextSearched.equalsIgnoreCase(search)) {
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

    private void notifyAdapter(List<?> tempList) {
        if (!Util.isNullOrEmptyList(tempList)) {
            listView.setVisibility(View.VISIBLE);
            tvNoResultFound.setVisibility(View.GONE);
        } else {
            listView.setVisibility(View.GONE);
            tvNoResultFound.setVisibility(View.VISIBLE);
        }
        mAdapter.setListData(commonListDialogType, tempList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        Util.showToast(mActivity, errorMsg);
        loadingOverlay.setVisibility(View.INVISIBLE);
        progressLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        loadingOverlay.setVisibility(View.INVISIBLE);
        progressLoading.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    initData();
                    return;
                case GET_EDUCATION_QUALIFICATION_SOLR:
                case GET_COLLEGE_UNIVERSITY_INSTITUES_SOLR:
                case GET_MEDICAL_COUNCILS_SOLR:
                case GET_DIAGNOSTIC_TESTS_SOLR:
                case GET_PROFESSIONAL_MEMBERSHIP_SOLR:
                    ArrayList<Object> listResponse = response.getDataList();
                    LogUtils.LOGD(TAG, "onResponse solr list Size " + listResponse.size() + " isDataFromLocal " + response.isDataFromLocal());
                    if (isLoadingFromSearch && !Util.isNullOrEmptyList(list)) {
                        list.clear();
                    }
                    if (list == null)
                        list = new ArrayList<>();
                    list.addAll(listResponse);
                    if (list.size() < MAX_SIZE || Util.isNullOrEmptyList(list))
                        isEndOfListAchieved = true;
                    notifyAdapter(list);
                    progressLoading.setVisibility(View.GONE);
                    isLoadingFromSearch = false;
                    break;
                default:
                    break;
            }
        }
        loadingOverlay.setVisibility(View.GONE);
    }

    @Override
    public void loadMore() {
        if (!isEndOfListAchieved) {
            PAGE_NUMBER = PAGE_NUMBER + 1;
            getList(false, PAGE_NUMBER, MAX_SIZE, String.valueOf(editSearch.getText()));
        }
    }

    @Override
    public boolean isEndOfListAchieved() {
        return isEndOfListAchieved;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        return false;
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Object object = null;
            switch (commonListDialogType) {
                case QUALIFICATION:
                    EducationQualification educationQualification = new EducationQualification();
                    educationQualification.setName(getSearchEditTextValue());
                    object = educationQualification;
                    break;
                case COLLEGE_UNIVERSITY_INSTITUTE:
                    CollegeUniversityInstitute collegeUniversityInstitute = new CollegeUniversityInstitute();
                    collegeUniversityInstitute.setName(getSearchEditTextValue());
                    object = collegeUniversityInstitute;
                    break;
                case MEDICAL_COUNCIL:
                    MedicalCouncil medicalCouncil = new MedicalCouncil();
                    medicalCouncil.setMedicalCouncil(getSearchEditTextValue());
                    object = medicalCouncil;
                    break;
                case REFERRED_BY:
                    Reference reference = new Reference();
                    reference.setReference(getSearchEditTextValue());
                    object = reference;
                    break;
                case PROFESSIONAL_MEMBERSHIP:
                    ForeignProfessionalMemberships professionalMemberships = new ForeignProfessionalMemberships();
                    professionalMemberships.setProfessionalMemberships(getSearchEditTextValue());
                    object = professionalMemberships;
                    break;
            }
            if (object != null) {
                commonListDialogItemClickListener.onDialogItemClicked(commonListDialogType, object);
                dismiss();
            }
        }
        return false;
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
}
