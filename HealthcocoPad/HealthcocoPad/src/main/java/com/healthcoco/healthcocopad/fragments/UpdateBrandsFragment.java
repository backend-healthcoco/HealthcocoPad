package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.VaccineCustomResponse;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VaccineBrand;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandAssociationRequest;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandResponse;
import com.healthcoco.healthcocopad.bean.server.VaccineResponse;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.BrandGroupType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectedBrandListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.UpdateBrandListViewHolderNew;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import static com.healthcoco.healthcocopad.viewholders.VaccinationListViewHolder.TAG_VACCINE_DATA;

public class UpdateBrandsFragment extends HealthCocoFragment implements View.OnClickListener, SelectedBrandListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised {
    public static final String TAG_BRAND_DATA = "brandData";
    public static final String TAG_SELECTED_BRAND_DATA = "selectedBrandData";
    private VaccineCustomResponse vaccineCustomResponse;
    private Button btNext;
    private RecyclerView rvBrandList;
    private HealthcocoRecyclerViewAdapter adapter;
    private ArrayList<String> vaccineId = new ArrayList<>();
    private LinkedHashMap<String, VaccineBrandAssociationRequest> brandAssociationRequestLinkedHashMap = new LinkedHashMap<>();
    private LinkedHashMap<String, VaccineBrandAssociationRequest> customBrandAssociationLinkedHashMap = new LinkedHashMap<>();
    private LinkedHashMap<String, VaccineBrandResponse> vaccineBrandHashmap = new LinkedHashMap<>();
    private LinkedHashMap<String, VaccineBrandResponse> brandLinkedHashMap = new LinkedHashMap<>();
    private LinkedHashMap<String, VaccineBrandAssociationRequest> associateGroupLinkedHashMap = new LinkedHashMap<>();
    private ArrayList<String> vaccineBrandIdsList;
    private LinkedHashMap<String, ArrayList<VaccineBrandResponse>> vaccineIdsList;
    private LinkedHashMap<String, ArrayList<String>> vaccineBrandList = new LinkedHashMap<>();
    private LinearLayout containerSixIOneBrandList;
    private LinearLayout containerSelectedBrandList;
    private LinearLayout parentContainerSelectedBrandList;
    private ArrayList<VaccineBrandResponse> responseList;
    private boolean isBrandGroupSelected;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_brands, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private User user;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        init();
    }

    @Override
    public void init() {
        Intent intent = mActivity.getIntent();
        vaccineCustomResponse = Parcels.unwrap(intent.getParcelableExtra(TAG_VACCINE_DATA));
        for (VaccineResponse vaccineResponse :
                vaccineCustomResponse.getVaccineResponse()) {
            vaccineId.add(vaccineResponse.getVaccineId());
            if (vaccineResponse.getVaccineBrand() != null) {
                if (Util.isNullOrEmptyList(vaccineBrandIdsList))
                    vaccineBrandIdsList = new ArrayList<>();
                vaccineBrandIdsList.add(vaccineResponse.getVaccineBrand().getUniqueId());
            }
        }
        initViews();
        initListeners();
        initAdapters();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void initViews() {
        rvBrandList = (RecyclerView) view.findViewById(R.id.rv_brands_list);
        containerSixIOneBrandList = (LinearLayout) view.findViewById(R.id.container_six_in_one_brands_list);
        parentContainerSelectedBrandList = (LinearLayout) view.findViewById(R.id.parent_container_selected_brands_list);
        containerSelectedBrandList = (LinearLayout) view.findViewById(R.id.container_selected_brands_list);
//        int spacingInPixelsVerticalBlogs = getResources().getDimensionPixelSize(R.dimen.item_spacing_vaccination_list_item);
        rvBrandList.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
//        rvBrandList.addItemDecoration(new VerticalRecyclerViewItemDecoration(spacingInPixelsVerticalBlogs));
        btNext = (Button) view.findViewById(R.id.bt_next);
        parentContainerSelectedBrandList.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        btNext.setOnClickListener(this);
    }

    private void initAdapters() {
        adapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.UPDATE_BRAND, this);
        rvBrandList.setAdapter(adapter);
        notifyAdapter(new ArrayList<>(brandAssociationRequestLinkedHashMap.values()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_next:
                if (!Util.isNullOrEmptyList(brandLinkedHashMap)) {
                    openCommonOpenUpActivityUsingParcel(CommonOpenUpFragmentType.UPDATE_VACCINE_BRAND_GROUP, HealthCocoConstants.REQUEST_CODE_UPDATA_BRAND_GROUP,
                            new String[]{UpdateVaccineBrandGroupFragment.TAG_VACCINE_BRAND_DATA, TAG_VACCINE_DATA, TAG_BRAND_DATA, TAG_SELECTED_BRAND_DATA},
                            new Object[]{brandLinkedHashMap, vaccineCustomResponse, responseList, vaccineBrandHashmap});
                } else {
                    UpdateVaccinationCommonFragment updateVaccinationCommonFragment = (UpdateVaccinationCommonFragment) getFragmentManager().findFragmentByTag(UpdateVaccinationCommonFragment.class.getSimpleName());
                    if (updateVaccinationCommonFragment != null)
                        updateVaccinationCommonFragment.openVaccineFragment(true, vaccineIdsList);
                }
                break;
            case R.id.iv_cancel_brand:
                VaccineBrand tag = (VaccineBrand) v.getTag();
                brandLinkedHashMap.remove(tag.getUniqueId());
                notifySelectedBrandContainer();
                break;
        }
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
            case FRAGMENT_INITIALISATION:
//                getVaccinationBrandList();
                new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_VACCINATION_BRAND, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                break;
            case GET_VACCINATION_BRAND:
                responseList = (ArrayList<VaccineBrandResponse>) (ArrayList<?>) response
                        .getDataList();
                if (!Util.isNullOrEmptyList(responseList)) {
                    ArrayList<VaccineBrandAssociationRequest> sectionedDataCustomTreatment = getSectionedDataCustomVaccineBrand(responseList);
                    formHashMapToNotifyAdapter(sectionedDataCustomTreatment);
                    formHashMapToNotifyContainers(responseList);
                }
                showLoadingOverlay(false);
                break;
            case GET_VACCINATION_BRAND_MULTIPLE:
                ArrayList<VaccineBrandAssociationRequest> brandAssociationRequests = (ArrayList<VaccineBrandAssociationRequest>) (ArrayList<?>) response
                        .getDataList();
                notifyAdapter(brandAssociationRequests);
                showLoadingOverlay(false);
                break;
            default:
                break;
        }
        showLoadingOverlay(false);
    }

    private void formHashMapToNotifyContainers(ArrayList<VaccineBrandResponse> list) {
        ArrayList<VaccineBrandAssociationRequest> sksListData = new ArrayList<VaccineBrandAssociationRequest>();
        HashMap<String, HashMap<String, ArrayList<VaccineBrandResponse>>> headerServiceHashMap = new HashMap<>();
        HashMap<String, ArrayList<VaccineBrandResponse>> responseArrayList = new HashMap<>();
        ArrayList<VaccineBrandResponse> arrayList = new ArrayList<>();
        //for from group data
        for (VaccineBrandResponse vaccineBrandResponse :
                list) {
            String fromGroup = vaccineBrandResponse.getVaccineBrand().getGroupFrom();
            if (!Util.isNullOrBlank(fromGroup)) {
                if (headerServiceHashMap.containsKey(fromGroup)) {
                    responseArrayList = headerServiceHashMap.get(fromGroup);
                    if (!Util.isNullOrEmptyList(responseArrayList)) {
                        if (responseArrayList.containsKey(vaccineBrandResponse.getVaccineBrandId())) {
                            arrayList = responseArrayList.get(vaccineBrandResponse.getVaccineBrandId());
                        } else {
                            arrayList = new ArrayList<>();
                            arrayList.add(vaccineBrandResponse);
                        }
                        responseArrayList.put(vaccineBrandResponse.getVaccineBrandId(), arrayList);
                    }
                } else {
                    responseArrayList = new HashMap<>();
                    arrayList = new ArrayList<>();
                    arrayList.add(vaccineBrandResponse);
                    responseArrayList.put(vaccineBrandResponse.getVaccineBrandId(), arrayList);
                }
                headerServiceHashMap.put(fromGroup, responseArrayList);
            }
        }
        for (String fromGroup :
                headerServiceHashMap.keySet()) {
            VaccineBrandAssociationRequest vaccineCustomResponse = new VaccineBrandAssociationRequest();
            vaccineCustomResponse.setName(fromGroup);
            HashMap<String, ArrayList<VaccineBrandResponse>> values = headerServiceHashMap.get(fromGroup);
            ArrayList<VaccineBrandResponse> vaccineBrandResponses = new ArrayList<>();
            for (String vaccineBrandId :
                    values.keySet()) {
                vaccineBrandResponses.addAll(values.get(vaccineBrandId));
            }
            vaccineCustomResponse.setVaccineBrandAssociationResponses(vaccineBrandResponses);
            sksListData.add(vaccineCustomResponse);
        }
        LinkedHashMap<String, VaccineBrandAssociationRequest> brandAssociationRequestLinkedHashMap = new LinkedHashMap<>();
        for (VaccineBrandAssociationRequest vaccineBrandAssociationRequest :
                sksListData) {
            brandAssociationRequestLinkedHashMap.put(vaccineBrandAssociationRequest.getName(), vaccineBrandAssociationRequest);
        }
        containerSixIOneBrandList.removeAllViews();
        isBrandGroupSelected = false;
        associateGroupLinkedHashMap = brandAssociationRequestLinkedHashMap;
        if (!Util.isNullOrEmptyList(brandAssociationRequestLinkedHashMap)) {
            for (BrandGroupType brandGroupType : BrandGroupType.values()) {
                VaccineBrandAssociationRequest vaccineBrandAssociationRequest =
                        brandAssociationRequestLinkedHashMap.get(brandGroupType.getValue());
                if (vaccineBrandAssociationRequest != null) {
                    UpdateBrandListViewHolderNew brandListViewHolder =
                            new UpdateBrandListViewHolderNew(mActivity, this);
                    View convertView = brandListViewHolder.getContentView();
                    brandListViewHolder.setData(vaccineBrandAssociationRequest);
                    brandListViewHolder.applyData();
                    containerSixIOneBrandList.addView(convertView);
                }
            }
        }
    }

    private void formHashMapToNotifyAdapter(ArrayList<VaccineBrandAssociationRequest> responseList) {
        for (VaccineBrandAssociationRequest vaccineBrandAssociationRequest :
                responseList) {
            brandAssociationRequestLinkedHashMap.put(vaccineBrandAssociationRequest.getName(), vaccineBrandAssociationRequest);
        }
        for (VaccineResponse vaccineResponse :
                vaccineCustomResponse.getVaccineResponse()) {
            if (vaccineResponse.getVaccineBrand() != null) {
                VaccineBrandAssociationRequest vaccineBrandAssociationRequest = brandAssociationRequestLinkedHashMap.get(vaccineResponse.getName());
                if (vaccineBrandAssociationRequest != null) {
                    for (VaccineBrandResponse vaccineBrandResponse :
                            vaccineBrandAssociationRequest.getVaccineBrandAssociationResponses()) {
                        if (vaccineResponse.getVaccineBrand().getUniqueId().equals(vaccineBrandResponse.getVaccineBrandId()))
                            vaccineBrandHashmap.put(vaccineBrandResponse.getVaccineId(), vaccineBrandResponse);
                    }
                }
            }
        }
        notifyAdapter(new ArrayList<>(brandAssociationRequestLinkedHashMap.values()));
    }

    private void notifyAdapter(ArrayList<VaccineBrandAssociationRequest> list) {
        if (!Util.isNullOrEmptyList(list)) {
            rvBrandList.setVisibility(View.VISIBLE);
        } else {
            rvBrandList.setVisibility(View.GONE);
        }

        adapter.setListData((ArrayList<Object>) (Object) list);
        adapter.notifyDataSetChanged();
    }

    public ArrayList<VaccineBrandAssociationRequest> getSectionedDataCustomVaccineBrand(ArrayList<VaccineBrandResponse> list) {
        ArrayList<VaccineBrandAssociationRequest> sksListData = new ArrayList<VaccineBrandAssociationRequest>();
        HashMap<String, ArrayList<VaccineBrandResponse>> headerServiceHashMap = new HashMap<>();
        ArrayList<VaccineBrandResponse> responseArrayList = new ArrayList<>();
        for (VaccineBrandResponse vaccineBrandResponse :
                list) {
            String vaccineName = vaccineBrandResponse.getVaccine().getName();
            if (!Util.isNullOrBlank(vaccineName)) {
                if (headerServiceHashMap.containsKey(vaccineName)) {
                    responseArrayList = headerServiceHashMap.get(vaccineName);
                    if (!Util.isNullOrEmptyList(responseArrayList)) {
                        responseArrayList.add(vaccineBrandResponse);
                    }
                } else {
                    responseArrayList = new ArrayList<>();
                    responseArrayList.add(vaccineBrandResponse);
                }
                ArrayList<String> strings = new ArrayList<>();
                for (VaccineBrandResponse brandResponse : responseArrayList) {
                    strings.add(brandResponse.getVaccineBrandId());
                }
                vaccineBrandList.put(vaccineName, strings);
                headerServiceHashMap.put(vaccineName, responseArrayList);
            }
        }

        for (String vaccineName :
                headerServiceHashMap.keySet()) {
            VaccineBrandAssociationRequest vaccineCustomResponse = new VaccineBrandAssociationRequest();
            vaccineCustomResponse.setName(vaccineName);
            vaccineCustomResponse.setVaccineBrandAssociationResponses(headerServiceHashMap.get(vaccineName));
            sksListData.add(vaccineCustomResponse);
        }
        return sksListData;
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
            case GET_VACCINATION_BRAND:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getVaccinationBrandsList(WebServiceType.GET_VACCINATION_BRAND, vaccineId, null, null);
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

    @Override
    public void isBrandSelect(boolean isChecked, String vaccineBrandId, VaccineBrandResponse vaccineBrandResponse) {
        if (Util.isNullOrEmptyList(vaccineBrandIdsList))
            vaccineBrandIdsList = new ArrayList<>();
        if (Util.isNullOrEmptyList(vaccineIdsList))
            vaccineIdsList = new LinkedHashMap<>();

        if (isChecked) {
            vaccineBrandHashmap.put(vaccineBrandResponse.getVaccineId(), vaccineBrandResponse);
            if (vaccineIdsList.containsKey(vaccineBrandResponse.getVaccineId())) {
                ArrayList<VaccineBrandResponse> vaccineBrandResponses = vaccineIdsList.get(vaccineBrandResponse.getVaccineId());
                for (VaccineBrandResponse s :
                        vaccineBrandResponses) {
                    vaccineBrandIdsList.remove(s.getVaccineBrandId());
                }
                vaccineBrandResponses.clear();
                vaccineBrandResponses.add(vaccineBrandResponse);
                vaccineIdsList.put(vaccineBrandResponse.getVaccineId(), vaccineBrandResponses);
            } else {
                ArrayList<VaccineBrandResponse> vaccineBrandResponses = vaccineIdsList.get(vaccineBrandResponse.getVaccineId());
                if (Util.isNullOrEmptyList(vaccineBrandResponses))
                    vaccineBrandResponses = new ArrayList<>();
                vaccineBrandResponses.add(vaccineBrandResponse);
                vaccineIdsList.put(vaccineBrandResponse.getVaccineId(), vaccineBrandResponses);
            }
            if (!vaccineBrandIdsList.contains(vaccineBrandResponse.getVaccineBrandId()))
                vaccineBrandIdsList.add(vaccineBrandResponse.getVaccineBrandId());

        } else {
            vaccineBrandHashmap.remove(vaccineBrandResponse.getVaccineId());
            if (vaccineBrandIdsList.contains(vaccineBrandResponse.getVaccineBrandId()))
                vaccineBrandIdsList.remove(vaccineBrandResponse.getVaccineBrandId());

            if (vaccineIdsList.containsKey(vaccineBrandResponse.getVaccineId()))
                vaccineIdsList.remove(vaccineBrandResponse.getVaccineId());
        }
        if (!rvBrandList.isComputingLayout()) {
            adapter.notifyDataSetChanged();
        }
    }

    private void notifySelectedBrandContainer() {
        containerSelectedBrandList.removeAllViews();
        for (VaccineBrandResponse brand :
                brandLinkedHashMap.values()) {
            LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.item_selected_brand_list, null);
            TextView brandTextView = layout.findViewById(R.id.tv_brand_name);
            ImageView ivCancelBrand = layout.findViewById(R.id.iv_cancel_brand);
            brandTextView.setText(brand.getVaccineBrand().getName());
            ivCancelBrand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VaccineBrandResponse tag = (VaccineBrandResponse) v.getTag();
                    brandLinkedHashMap.remove(tag.getVaccineBrand().getGroupFrom());
                    notifySelectedBrandContainer();
                    ArrayList<VaccineBrandAssociationRequest> sectionedDataCustomTreatment = getSectionedDataCustomVaccineBrand(responseList);
                    formHashMapToNotifyAdapter(sectionedDataCustomTreatment);
                    formHashMapToNotifyContainers(responseList);
                }
            });
            ivCancelBrand.setTag(brand);
            containerSelectedBrandList.addView(layout);
        }
        if (brandLinkedHashMap.size() > 0)
            parentContainerSelectedBrandList.setVisibility(View.VISIBLE);
        else parentContainerSelectedBrandList.setVisibility(View.GONE);
    }

    @Override
    public boolean isBrandSelected(String vaccineBrandId, String vaccineId) {
        if (vaccineBrandHashmap.containsKey(vaccineId)) {
            VaccineBrandResponse brandResponse = vaccineBrandHashmap.get(vaccineId);
            if (brandResponse.getVaccineBrandId().equals(vaccineBrandId))
                return true;
            else
                return false;
        }
       /* if (vaccineBrandHashmap.containsKey(vaccineId))
            if (!Util.isNullOrEmptyList(vaccineBrandIdsList))
                return vaccineBrandIdsList.contains(vaccineBrandId);*/
        return false;
    }

    @Override
    public void isBrandSelectForGroup(boolean isChecked, String vaccineBrandId, VaccineBrandResponse vaccineBrandResponse) {
        brandLinkedHashMap.clear();
        if (vaccineBrandResponse.getVaccineBrand().getGroupFrom() != null &&
                vaccineBrandResponse.getVaccineBrand().getGroupFrom().equals(BrandGroupType.FOUR_IN_ONE.getValue())) {
            brandLinkedHashMap.put(vaccineBrandResponse.getVaccineBrand().getGroupFrom(), vaccineBrandResponse);
            notifySelectedBrandContainer();
        } else if (vaccineBrandResponse.getVaccineBrand().getGroupFrom() != null &&
                vaccineBrandResponse.getVaccineBrand().getGroupFrom().equals(BrandGroupType.FIVE_IN_ONE.getValue())) {
            brandLinkedHashMap.put(vaccineBrandResponse.getVaccineBrand().getGroupFrom(), vaccineBrandResponse);
            notifySelectedBrandContainer();
        } else if (vaccineBrandResponse.getVaccineBrand().getGroupFrom() != null &&
                vaccineBrandResponse.getVaccineBrand().getGroupFrom().equals(BrandGroupType.SIX_IN_ONE.getValue())) {
            brandLinkedHashMap.put(vaccineBrandResponse.getVaccineBrand().getGroupFrom(), vaccineBrandResponse);
            notifySelectedBrandContainer();
        }
        removedSelectedVaccine(vaccineBrandResponse.getVaccineBrand().getUniqueId(),
                vaccineBrandResponse.getVaccineBrand().getGroupFrom());
    }

    @Override
    public boolean isBrandGroupSelected() {
        return isBrandGroupSelected;
    }

    private void removedSelectedVaccine(String uniqueId, String groupFrom) {
        customBrandAssociationLinkedHashMap.clear();
        for (String vaccine : vaccineBrandList.keySet()) {
            ArrayList<String> arrayList = vaccineBrandList.get(vaccine);
            if (!Util.isNullOrEmptyList(arrayList)) {
                if (!arrayList.contains(uniqueId)) {
                    VaccineBrandAssociationRequest request = brandAssociationRequestLinkedHashMap.get(vaccine);
                    customBrandAssociationLinkedHashMap.put(vaccine, request);
                }
            }
        }
        notifyCustomAdapter(new ArrayList<>(customBrandAssociationLinkedHashMap.values()), groupFrom, uniqueId);
    }

    private void notifyCustomAdapter(ArrayList<VaccineBrandAssociationRequest> list, String groupFrom, String uniqueId) {
        ArrayList<VaccineBrandAssociationRequest> brandAssociationRequests = new ArrayList<>();

        for (String vaccineBrandgroup :
                associateGroupLinkedHashMap.keySet()) {
            if (groupFrom.equalsIgnoreCase(vaccineBrandgroup)) {
                brandAssociationRequests.add(associateGroupLinkedHashMap.get(vaccineBrandgroup));
            }
        }
        containerSixIOneBrandList.removeAllViews();
        for (VaccineBrandAssociationRequest vaccineBrandAssociationRequest :
                brandAssociationRequests) {
            if (!Util.isNullOrEmptyList(vaccineBrandAssociationRequest.getVaccineBrandAssociationResponses())) {
                for (VaccineBrandResponse brandResponse : vaccineBrandAssociationRequest.getVaccineBrandAssociationResponses()) {
                    if (brandResponse != null) {
                        if (brandResponse.getVaccineBrandId().equalsIgnoreCase(uniqueId)) {
                            brandResponse.setSelected(true);
                        } else {
                            brandResponse.setSelected(false);
                        }
                    }
                }
            }
            isBrandGroupSelected = true;
            UpdateBrandListViewHolderNew brandListViewHolder = new UpdateBrandListViewHolderNew(mActivity, this);
            View convertView = brandListViewHolder.getContentView();
            brandListViewHolder.setData(vaccineBrandAssociationRequest);
            brandListViewHolder.applyData();
            containerSixIOneBrandList.addView(convertView);
        }

        if (!Util.isNullOrEmptyList(list)) {
            rvBrandList.setAdapter(adapter);
            adapter.setListData((ArrayList<Object>) (Object) list);
            adapter.notifyDataSetChanged();
        } else {
            rvBrandList.setVisibility(View.GONE);
        }
    }

    @Override
    public void isBrandCancel(String vaccineBrandId, VaccineBrandResponse vaccineBrandResponse) {
//        vaccineBrandHashmap.remove(vaccineBrandResponse.getVaccineBrandId());
//        notifySelectedBrandAdapter(new ArrayList<VaccineBrandResponse>(vaccineBrandHashmap.values()));
    }

    public String getVaccineBrandResponse(String uniqueId) {
        if (!Util.isNullOrEmptyList(vaccineBrandHashmap)) {
            if (vaccineBrandHashmap.get(uniqueId) != null)
                if (!Util.isNullOrBlank(vaccineBrandHashmap.get(uniqueId).getVaccineBrandId()))
                    return vaccineBrandHashmap.get(uniqueId).getVaccineBrandId();
        }
        return null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_UPDATA_BRAND_GROUP) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_UPDATA_BRAND_GROUP) {
                mActivity.finish();
            }
        }
    }
}
