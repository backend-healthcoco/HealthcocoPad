package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.server.VaccineCustomResponse;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.VaccineRequest;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VaccineResponse;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.VaccineStatus;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.SelectVaccinationListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.VaccinationListViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class UpdateVaccineFragment extends HealthCocoFragment implements SelectVaccinationListener,
        View.OnClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    private VaccineCustomResponse vaccineCustomResponse;
    private RecyclerView rvVaccineList;
    private CheckBox cbSelectAllVaccine;
    private Button btUpdateVaccine;
    private User user;
    private HealthcocoRecyclerViewAdapter adapter;
    private ArrayList<String> rateCardIdsList;
    private LinkedHashMap<String, VaccineRequest> requestLinkedHashMapForValidate = new LinkedHashMap<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_vaccine, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
        }
        init();
    }

    @Override
    public void init() {
        Intent intent = mActivity.getIntent();
        vaccineCustomResponse = Parcels.unwrap(intent.getParcelableExtra(VaccinationListViewHolder.TAG_VACCINE_DATA));
        initViews();
        initListeners();
        initAdapters();
    }

    private void initAdapters() {
        adapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.UPDATE_VACCINE, this);
        rvVaccineList.setAdapter(adapter);
        adapter.setListData((ArrayList<Object>) (Object) vaccineCustomResponse.getVaccineResponse());
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initViews() {
        rvVaccineList = (RecyclerView) view.findViewById(R.id.rv_vaccine_list);
//        int spacingInPixelsVerticalBlogs = getResources().getDimensionPixelSize(R.dimen.item_spacing_vaccination_list_item);
        //initialsing adapter for Health Blogs
        rvVaccineList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
//        rvVaccineList.addItemDecoration(new VerticalRecyclerViewItemDecoration(spacingInPixelsVerticalBlogs));
        cbSelectAllVaccine = (CheckBox) view.findViewById(R.id.cb_select_all_vaccine);
        btUpdateVaccine = (Button) view.findViewById(R.id.bt_update_vaccine);
    }

    @Override
    public void initListeners() {
        cbSelectAllVaccine.setOnClickListener(this);
        btUpdateVaccine.setOnClickListener(this);
    }

    private void setSelectAllSelected(boolean isSelected) {
        for (VaccineResponse vaccineResponse :
                vaccineCustomResponse.getVaccineResponse()) {
            isVaccinationClicked(isSelected, vaccineResponse);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.cb_select_all_vaccine) {
            setSelectAllSelected(cbSelectAllVaccine.isChecked());
            adapter.notifyDataSetChanged();

        } else if (id == R.id.bt_update_vaccine) {
            validateData();
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        mActivity.showLoading(false);
        UpdateBrandsFragment updateBrandsFragment = (UpdateBrandsFragment) getFragmentManager().findFragmentByTag(UpdateBrandsFragment.class.getSimpleName());
        if (updateBrandsFragment != null) {
            for (VaccineRequest vaccineRequest :
                    requestLinkedHashMapForValidate.values()) {
                if (!Util.isNullOrBlank(updateBrandsFragment.getVaccineBrandResponse(vaccineRequest.getVaccineId())))
                    vaccineRequest.setVaccineBrandId(updateBrandsFragment.getVaccineBrandResponse(vaccineRequest.getVaccineId()));
            }
        }
        if (!Util.isNullOrEmptyList(rateCardIdsList) && rateCardIdsList.size() > 0) {
            for (VaccineRequest vaccineRequest :
                    requestLinkedHashMapForValidate.values()) {
                if (vaccineRequest.getGivenDate() == null)
                    msg = getResources().getString(R.string.please_select_given_date);
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
        showLoadingOverlay(true);
        WebDataServiceImpl.getInstance(mApp).addEditVaccinationList(Boolean.class, WebServiceType.ADD_EDIT_MULTIPLE_VACCINATION,
                new ArrayList<VaccineRequest>(requestLinkedHashMapForValidate.values()), this, this);
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        Util.showToast(mActivity, errorMsg);
        showLoadingOverlay(false);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        showLoadingOverlay(false);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
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
                break;
            default:
                break;
        }
        showLoadingOverlay(false);
        mActivity.hideLoading();
    }

    @Override
    public void isVaccinationClicked(boolean isChecked, VaccineResponse response) {
        if (Util.isNullOrEmptyList(rateCardIdsList))
            rateCardIdsList = new ArrayList<>();
        try {
            VaccineRequest vaccineRequest = new VaccineRequest();
            ReflectionUtil.copy(vaccineRequest, response);
            vaccineRequest.setStatus(VaccineStatus.GIVEN);
            vaccineRequest.setDoctorId(user.getUniqueId());
            vaccineRequest.setLocationId(user.getForeignLocationId());
            vaccineRequest.setHospitalId(user.getForeignHospitalId());
            if (isChecked) {
                requestLinkedHashMapForValidate.put(response.getUniqueId(), vaccineRequest);
                if (!rateCardIdsList.contains(response.getUniqueId()))
                    rateCardIdsList.add(response.getUniqueId());
            } else {
                requestLinkedHashMapForValidate.remove(vaccineRequest.getUniqueId());
                if (rateCardIdsList.contains(response.getUniqueId()))
                    rateCardIdsList.remove(response.getUniqueId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isVaccinationSelected(String uniqueId) {
        if (!Util.isNullOrEmptyList(rateCardIdsList))
            return rateCardIdsList.contains(uniqueId);
        return false;
    }

    @Override
    public void setSelectAll(boolean isSelected) {
        if (rateCardIdsList.size() == vaccineCustomResponse.getVaccineResponse().size())
            cbSelectAllVaccine.setChecked(true);
        else cbSelectAllVaccine.setChecked(false);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == HealthCocoConstants.RESULT_CODE_UPDATA_BRAND_GROUP) {
            if (requestCode == HealthCocoConstants.REQUEST_CODE_UPDATA_BRAND_GROUP) {
                mActivity.finish();
            }
        }
    }

    public void setSelectedVaccineDate(ArrayList<String> vaccineIdsList) {
        for (VaccineResponse vaccineResponse : vaccineCustomResponse.getVaccineResponse()) {
            if (vaccineResponse != null && !Util.isNullOrBlank(vaccineResponse.getVaccineId())) {
                if (vaccineIdsList.contains(vaccineResponse.getVaccineId())) {
                    vaccineResponse.setGivenDate(DateTimeUtil.getCurrentDateLong());
                    vaccineResponse.setSelected(true);
                }
            }
        }
        adapter.setListData((ArrayList<Object>) (Object) vaccineCustomResponse.getVaccineResponse());
        adapter.notifyDataSetChanged();
    }
}
