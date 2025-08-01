package com.healthcoco.healthcocopad.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.VaccineRequest;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VaccineSolarResponse;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.VaccineStatus;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SearchVaccineListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

public class AddVaccinationFragment extends HealthCocoFragment implements TextWatcher, View.OnClickListener, SearchVaccineListener, LocalDoInBackgroundListenerOptimised {

    private RecyclerView rvVaccineList;
    private HealthcocoRecyclerViewAdapter adapter;
    private User user;
    private String lastTextSearched = "";
    private LinkedHashMap<String, VaccineRequest> requestLinkedHashMapForValidate = new LinkedHashMap<>();
    private LinkedHashMap<String, VaccineSolarResponse> vaccineSolarResponseArrayList = new LinkedHashMap<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_vaccine, container, false);
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
        initViews();
        initListeners();
        initAdapters();
    }

    @Override
    public void initViews() {
        rvVaccineList = (RecyclerView) view.findViewById(R.id.rv_vaccines);
//        int spacingInPixelsVerticalBlogs = getResources().getDimensionPixelSize(R.dimen.item_spacing_vaccination_list_item);
        //initialsing adapter for Health Blogs
        rvVaccineList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
//        rvVaccineList.addItemDecoration(new VerticalRecyclerViewItemDecoration(spacingInPixelsVerticalBlogs));
    }

    @Override
    public void initListeners() {
        initEditSearchView(R.string.search_vaccination, this, this);
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initAdapters() {
        adapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.SEARCH_VACCINATION, this);
        rvVaccineList.setAdapter(adapter);
    }

    private void notifyAdapter(ArrayList<VaccineSolarResponse> list) {
        if (!Util.isNullOrEmptyList(list)) {
            rvVaccineList.setVisibility(View.VISIBLE);
        } else {
            rvVaccineList.setVisibility(View.GONE);
        }
        adapter.setListData((ArrayList<Object>) (Object) list);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        LogUtils.LOGD(TAG, "TextChange afterTextChange");
        String search = String.valueOf(s).toUpperCase(Locale.ENGLISH);
        if (lastTextSearched == null || !lastTextSearched.equalsIgnoreCase(search)) {
            //sorting custom treatment list
            LinkedHashMap<String, VaccineSolarResponse> tempList = new LinkedHashMap<>();
            if (!Util.isNullOrEmptyList(vaccineSolarResponseArrayList)) {
                if (search.length() == 0) {
                    tempList.putAll(vaccineSolarResponseArrayList);
                } else {
                    for (VaccineSolarResponse vaccineSolarResponse : vaccineSolarResponseArrayList.values()) {
                        if (!Util.isNullOrBlank(vaccineSolarResponse.getLongName())) {
                            if (vaccineSolarResponse.getLongName().toUpperCase(Locale.ENGLISH)
                                    .contains(search)) {
                                tempList.put(vaccineSolarResponse.getUniqueId(), vaccineSolarResponse);
                            }
                        }
                    }
                }
            }
            notifyAdapter(new ArrayList<VaccineSolarResponse>(tempList.values()));
        }
        lastTextSearched = search;
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null) {
                    user = doctor.getUser();
                }
                return volleyResponseBean;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        mActivity.hideLoading();
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        getVaccinationSolrList();
                    }
                    return;
                case GET_VACCINES_SOLR_LIST:
                    ArrayList<VaccineSolarResponse> dataList = (ArrayList<VaccineSolarResponse>) (ArrayList<?>) response.getDataList();
                    if (!Util.isNullOrEmptyList(dataList)) {
                        formHashMapAndNotifyAdpater(dataList);
                    }
                    break;
                case ADD_EDIT_MULTIPLE_VACCINATION:
                    if (response.isValidData(response)) {
                        if (response.getData() != null && response.getData() instanceof Boolean) {
                            if (response.getData() instanceof Boolean) {
                                boolean isDataSuccess = (boolean) response.getData();
                                if (isDataSuccess) {
                                    Util.sendBroadcast(mApp, VaccinationListFragment.INTENT_REFRESH_REQUEST_LIST_FROM_SERVER);
                                } else Util.showToast(mActivity, R.string.vaccines_not_updated);
                                ((CommonOpenUpActivity) mActivity).finish();
                            } else {
                                Util.showToast(mActivity, response.getErrMsg());
                            }
                        }
                    }
                    showLoadingOverlay(false);
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void formHashMapAndNotifyAdpater(ArrayList<VaccineSolarResponse> dataList) {
        if (!Util.isNullOrEmptyList(dataList)) {
            for (VaccineSolarResponse vaccineSolarResponse :
                    dataList) {
                vaccineSolarResponseArrayList.put(vaccineSolarResponse.getUniqueId(), vaccineSolarResponse);
            }
        }
        notifyAdapter(new ArrayList<VaccineSolarResponse>(vaccineSolarResponseArrayList.values()));
    }

    private void getVaccinationSolrList() {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getVaccinesSolrList(VaccineSolarResponse.class, WebServiceType.GET_VACCINES_SOLR_LIST, this, this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.container_right_action) {
            validateData();
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        mActivity.showLoading(false);
        if (!Util.isNullOrEmptyList(requestLinkedHashMapForValidate)) {
            for (VaccineRequest vaccineRequest : requestLinkedHashMapForValidate.values()) {
                if (vaccineRequest.getDueDate() == null)
                    msg = getResources().getString(R.string.please_select_due_date);
            }
            if (Util.isNullOrBlank(msg)) {
                sendUpdatedVaccineList();
            } else {
                mActivity.hideLoading();
                EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
            }
        } else {
            mActivity.hideLoading();
            Util.showAlert(mActivity, R.string.no_vaccine_are_selected);
        }

    }

    private void sendUpdatedVaccineList() {
//        showLoadingOverlay(false);
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addEditVaccinationList(Boolean.class, WebServiceType.ADD_EDIT_MULTIPLE_VACCINATION,
                new ArrayList<VaccineRequest>(requestLinkedHashMapForValidate.values()), this, this);
    }


    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void isVaccinationClicked(boolean isChecked, Long duration, VaccineStatus status, VaccineSolarResponse response) {
//        if (Util.isNullOrEmptyList(rateCardIdsList))
//            rateCardIdsList = new ArrayList<>();
        try {
            VaccineRequest vaccineRequest = new VaccineRequest();
            vaccineRequest.setPatientId(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
            vaccineRequest.setDoctorId(user.getUniqueId());
            vaccineRequest.setLocationId(user.getForeignLocationId());
            vaccineRequest.setHospitalId(user.getForeignHospitalId());
            vaccineRequest.setDueDate(duration);
            vaccineRequest.setName(response.getName());
            if (!Util.isNullOrBlank(response.getDuration()))
                vaccineRequest.setDuration(response.getDuration());
            else vaccineRequest.setDuration("Custom"/* + Util.generateRandomId()*/);
            if (response.getPeriodTime() != null)
                vaccineRequest.setPeriodTime(response.getPeriodTime());
            vaccineRequest.setVaccineId(response.getUniqueId());
            if (status == null) {
                if (duration != null && !Util.isNullOrZeroNumber(duration)) {
                    if (duration <= DateTimeUtil.getCurrentDateLong()) {
                        vaccineRequest.setStatus(VaccineStatus.GIVEN);
                        vaccineRequest.setGivenDate(duration);
                    } else
                        vaccineRequest.setStatus(VaccineStatus.PLANNED);
                }
            } else {
                vaccineRequest.setStatus(status);
                if (status == VaccineStatus.GIVEN) {
                    vaccineRequest.setGivenDate(duration);
                }
            }

            if (isChecked) {
                requestLinkedHashMapForValidate.put(response.getUniqueId(), vaccineRequest);
//                if (!rateCardIdsList.contains(response.getUniqueId()))
//                    rateCardIdsList.add(response.getUniqueId());
            } else {
                requestLinkedHashMapForValidate.remove(vaccineRequest.getUniqueId());
//                if (rateCardIdsList.contains(response.getUniqueId()))
//                    rateCardIdsList.remove(response.getUniqueId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
