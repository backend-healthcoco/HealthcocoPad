package com.healthcoco.healthcocopad.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.PersonalFamilyHistoryDiseaseAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddMedicalFamilyHistoryRequest;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.HistoryFilterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddDiseaseListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.MedicalFamilyHistoryItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.ComparatorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.FontAwesomeButton;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

/**
 * Created by neha on 11/12/15.
 */
public class DiseaseListFragment extends HealthCocoFragment implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, TextWatcher, MedicalFamilyHistoryItemListener, AddDiseaseListener {
    public static final String TAG_FILTER_TYPE = "filterType";
    public static final int REQUEST_CODE_DISEASED_LIST = 136;
    public static final String TAG_DISEASES_LIST = "diseasesList";
    private GridView lvDiseaseList;
    //    private ListView lvDiseaseList;
    private PersonalFamilyHistoryDiseaseAdapter adapter;
    private ArrayList<Disease> diseaseList;
    private TextView tvNoDiseaseHistory;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private ArrayList<String> addDiseasesList = new ArrayList<String>();
    private ArrayList<String> removeDiseasesList = new ArrayList<String>();
    private ArrayList<String> diseaseIdsList;
    private HistoryFilterType filterType;
    private User user;
    private FontAwesomeButton btAddNewHistory;
    private boolean isFromAssessment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_disease_list, container, false);
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
        Intent intent = mActivity.getIntent();
        int filterTypeOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_HISTORY_FILTER_TYPE, 0);
        isFromAssessment = intent.getBooleanExtra(HealthCocoConstants.TAG_IS_FROM_ASSESSMENT, false);
        diseaseIdsList = intent.getStringArrayListExtra(HealthCocoConstants.TAG_DISEASE_IDS_LIST);
        filterType = HistoryFilterType.values()[filterTypeOrdinal];
        if (filterType != null)
            ((CommonOpenUpActivity) mActivity).initActionbarTitle(filterType.getActionbarId());
        initViews();
        initListeners();
        initAdapter();
//        getDiseaseList();
    }

    private void getListFromLocal() {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DISEASE_LIST, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getDiseaseList() {
        mActivity.showLoading(false);
        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(LocalTabelType.DISEASE);
        WebDataServiceImpl.getInstance(mApp).getDiseaseList(Disease.class, user.getUniqueId(), latestUpdatedTime, diseaseIdsList, this, this);
    }

    @Override
    public void initViews() {
        lvDiseaseList = (GridView) view.findViewById(R.id.lv_disease_list);
//        lvDiseaseList = (ListView) view.findViewById(R.id.lv_disease_list);
        tvNoDiseaseHistory = (TextView) view.findViewById(R.id.tv_no_disease_history_found);
        btAddNewHistory = (FontAwesomeButton) view.findViewById(R.id.bt_advance_search);
        initEditSearchView(R.string.search_history, this, this);
        btAddNewHistory.setText(getResources().getString(R.string.icon_add_new_history));
    }

    @Override
    public void initListeners() {
        btAddNewHistory.setOnClickListener(this);
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initAdapter() {
        adapter = new PersonalFamilyHistoryDiseaseAdapter(mActivity, this);
        adapter.setListData(diseaseList);
        lvDiseaseList.setAdapter(adapter);
    }

    private void notifyAdapter(ArrayList<Disease> list) {
        if (!Util.isNullOrEmptyList(list)) {
            Collections.sort(list, ComparatorUtil.diseaseDateComparator);
            lvDiseaseList.setVisibility(View.VISIBLE);
            tvNoDiseaseHistory.setVisibility(View.GONE);
            adapter.setListData(list);
            adapter.notifyDataSetChanged();
        } else {
            lvDiseaseList.setVisibility(View.GONE);
            tvNoDiseaseHistory.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_advance_search:
                mActivity.openAddUpdateNameDialogFragment(WebServiceType.ADD_DISEASE,
                        AddUpdateNameDialogType.DISEASE, this, user, "", REQUEST_CODE_DISEASED_LIST);
                break;
            case R.id.container_right_action:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    addMedicalFamilyHistory();
                } else {
                    Util.showToast(mActivity, getResources().getString(R.string.user_offline));
                }
                break;
        }
    }

    private void addMedicalFamilyHistory() {
        if (filterType != null) {
            mActivity.showLoading(false);
            AddMedicalFamilyHistoryRequest addMedicalFamilyHistoryRequest = new AddMedicalFamilyHistoryRequest();
            addMedicalFamilyHistoryRequest.setHospitalId(user.getForeignHospitalId());
            addMedicalFamilyHistoryRequest.setLocationId(user.getForeignLocationId());
            addMedicalFamilyHistoryRequest.setDoctorId(user.getUniqueId());
            addMedicalFamilyHistoryRequest.setAddDiseases(addDiseasesList);
            addMedicalFamilyHistoryRequest.setRemoveDiseases(removeDiseasesList);
            addMedicalFamilyHistoryRequest.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
            WebServiceType webServiceType = null;
            LogUtils.LOGD(TAG, "FilterType : " + String.valueOf(filterType));
            switch (filterType) {
                case MEDICAL_HISTORY:
                    webServiceType = WebServiceType.ADD_MEDICAL_HISTORY;
                    break;
                case FAMILY_HISTORY:
                    webServiceType = WebServiceType.ADD_FAMILY_HISTORY;
                    break;
            }

            if (!isFromAssessment) {
                WebDataServiceImpl.getInstance(mApp).addMedicalFamilyHistory(webServiceType, Boolean.class, addMedicalFamilyHistoryRequest, this, this);
            } else {
                ArrayList<String> diseaseArrayList = new ArrayList<>();
                for (Disease disease : diseaseList) {
                    if (addDiseasesList.contains(disease.getUniqueId())) {
                        diseaseArrayList.add(disease.getDisease());
                    }
                }
                addMedicalFamilyHistoryRequest.setAddDiseases(diseaseArrayList);
                addMedicalFamilyHistoryRequest.setRemoveDiseases(addDiseasesList);
                Intent data = new Intent();
                data.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(addMedicalFamilyHistoryRequest));
                data.putExtra(HealthCocoConstants.TAG_HISTORY_FILTER_TYPE, filterType.ordinal());
                getActivity().setResult(HealthCocoConstants.RESULT_CODE_PAST_HISTORY, data);
                getActivity().finish();
            }
        }
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case FRAGMENT_INITIALISATION:
                if (user != null && selectedPatient != null) {
                    getListFromLocal();
                    return;
                }
                break;
            case GET_DISEASE_LIST:
                if (response.isDataFromLocal()) {
                    diseaseList = (ArrayList<Disease>) (ArrayList<?>) response.getDataList();
                    LogUtils.LOGD(TAG, "Success onResponse diseaseList Size " + diseaseList.size() + " isDataFromLocal " + response.isDataFromLocal());
                }
                notifyAdapter(diseaseList);
                mActivity.hideLoading();
                break;
            case ADD_MEDICAL_HISTORY:
            case ADD_FAMILY_HISTORY:
                if (response.getData() instanceof Boolean) {
                    boolean isSuccess = (boolean) response.getData();
                    LogUtils.LOGD(TAG, String.valueOf(response.getWebServiceType()) + isSuccess);
                    if (isSuccess) {
                        Intent intent = new Intent();
                        intent.putExtra(TAG_FILTER_TYPE, filterType.ordinal());
                        intent.putExtra(TAG_DISEASES_LIST, Parcels.wrap(addDiseasesList));
                        ((CommonOpenUpActivity) mActivity).setResult(HealthCocoConstants.RESULT_CODE_DISEASE_LIST, intent);
                        ((CommonOpenUpActivity) mActivity).finish();
                    }
                }
                break;
            default:
                break;
        }
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId()) && selectedPatient != null && !Util.isNullOrBlank(selectedPatient.getUserId())) {
                    user = doctor.getUser();
                }
                break;
            case ADD_DISEASE_LIST:
                LocalDataServiceImpl.getInstance(mApp).addDiseaseList((ArrayList<Disease>) (ArrayList<?>) response.getDataList());
            case GET_DISEASE_LIST:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp)
                        .getDiseaseList(WebServiceType.GET_DISEASE_LIST, null, BooleanTypeValues.FALSE, null, 0, null, null);
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
        if (!Util.isNullOrBlank(volleyResponseBean.getErrMsg()))
            onErrorResponse(volleyResponseBean, "");
        else
            onResponse(volleyResponseBean);
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
        ArrayList<Disease> tempList = new ArrayList<Disease>();
        if (!Util.isNullOrEmptyList(diseaseList)) {
            if (search.length() == 0) {
                tempList.addAll(diseaseList);
            } else {
                for (Disease disease : diseaseList) {
                    if (disease.getDisease().toLowerCase(Locale.ENGLISH)
                            .contains(search)) {
                        tempList.add(disease);
                    }
                }
            }
        }
        notifyAdapter(tempList);
    }

    @Override
    public void onAddDiseaseClicked(String diseaseId) {
        if (!addDiseasesList.contains(diseaseId))
            addDiseasesList.add(diseaseId);
        if (removeDiseasesList.contains(diseaseId)) {
            removeDiseasesList.remove(diseaseId);
            if (diseaseIdsList.contains(diseaseId))
                diseaseIdsList.remove(diseaseId);
        }
    }

    @Override
    public void onRemoveDiseaseClicked(String diseaseId) {
        if (!removeDiseasesList.contains(diseaseId)) {
            removeDiseasesList.add(diseaseId);
            if (diseaseIdsList.contains(diseaseId))
                diseaseIdsList.remove(diseaseId);
        }
        if (addDiseasesList.contains(diseaseId))
            addDiseasesList.remove(diseaseId);
    }

    @Override
    public void onAddDiseaseSuccess() {

    }

    @Override
    public boolean isDiseaseAdded(String uniqueId) {
        if (addDiseasesList.contains(uniqueId) || diseaseIdsList.contains(uniqueId))
            return true;
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_DISEASED_LIST) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_REFERENCE_LIST) {
                if (data != null && data.hasExtra(HealthCocoConstants.TAG_INTENT_DATA)) {
                    ArrayList<Disease> diseaseList = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                    if (this.diseaseList == null)
                        this.diseaseList = new ArrayList<>();
                    this.diseaseList.addAll(diseaseList);
                    notifyAdapter(this.diseaseList);
                    getListFromLocal();
                    return;
                }
            }
        }
    }
}
