package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.server.VaccineCustomResponse;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandResponse;
import com.healthcoco.healthcocopad.bean.server.VaccineResponse;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.VaccinationListViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class UpdateVaccinationCommonFragment extends HealthCocoFragment implements View.OnClickListener {
    private Button btBrands;
    private Button btVaccine;
    private VaccineCustomResponse vaccineResponse;
    private ArrayList<String> vacccineIds = new ArrayList<>();
    private UpdateVaccineFragment updateVaccineFragment;
    private UpdateBrandsFragment updateBrandsFragment;
    private LinearLayout containerVaccine;
    private LinearLayout containerBrands;
    private LinkedHashMap<String, ArrayList<VaccineBrandResponse>> vaccineIdsList;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_vaccination, container, false);
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
        Intent intent = mActivity.getIntent();
        vaccineResponse = Parcels.unwrap(intent.getParcelableExtra(VaccinationListViewHolder.TAG_VACCINE_DATA));
        initViews();
        initListeners();

        updateVaccineFragment = new UpdateVaccineFragment();
        updateBrandsFragment = new UpdateBrandsFragment();
        openFragment(updateVaccineFragment, containerVaccine);
        openFragment(updateBrandsFragment, containerBrands);
//        getVaccinationBrandList();
    }

    private void getVaccinationBrandList() {
        if (!Util.isNullOrEmptyList(vaccineResponse.getVaccineResponse())) {
            ArrayList<String> vacccineIds = new ArrayList<>();
            for (VaccineResponse vaccineResponse :
                    vaccineResponse.getVaccineResponse()) {
                vacccineIds.add(vaccineResponse.getVaccineId());
            }
            WebDataServiceImpl.getInstance(mApp).getVaccinationBrandList(VaccineBrandResponse.class, WebServiceType.GET_VACCINATION_BRAND, vacccineIds, this, this);
        }
    }

    @Override
    public void initViews() {
//        ((CommonOpenUpActivity) mActivity).initActionbarTitle();
        btBrands = view.findViewById(R.id.bt_brands);
        btVaccine = view.findViewById(R.id.bt_vaccine);
        containerVaccine = view.findViewById(R.id.container_vaccine);
        containerBrands = view.findViewById(R.id.container_brands);
        setBrandVaccineState(true);
    }

    private void setBrandVaccineState(boolean isSelected) {
        if (isSelected) {
            btBrands.setSelected(false);
            btVaccine.setSelected(true);
            ((CommonOpenUpActivity) mActivity).initActionbarTitle(mActivity.getResources().getString(R.string.vaccine) + " - " + vaccineResponse.getDuration());
        } else {
            btBrands.setSelected(true);
            btVaccine.setSelected(false);
            ((CommonOpenUpActivity) mActivity).initActionbarTitle(mActivity.getResources().getString(R.string.brands) + " - " + vaccineResponse.getDuration());
        }
    }

    @Override
    public void initListeners() {
        btBrands.setOnClickListener(this);
        btVaccine.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bt_brands) {
            if (containerVaccine.getVisibility() == View.VISIBLE) {
                containerBrands.setVisibility(View.VISIBLE);
                containerVaccine.setVisibility(View.GONE);
            }
            setBrandVaccineState(false);

        } else if (id == R.id.bt_vaccine) {
            if (containerBrands.getVisibility() == View.VISIBLE) {
                containerVaccine.setVisibility(View.VISIBLE);
                containerBrands.setVisibility(View.GONE);
                if (!Util.isNullOrEmptyList(vaccineIdsList)) {
                    updateVaccineFragment.setSelectedVaccineDate(new ArrayList<>(vaccineIdsList.keySet()));
                }
            }
            setBrandVaccineState(true);
        }
    }

    public void openVaccineFragment(boolean b, LinkedHashMap<String, ArrayList<VaccineBrandResponse>> vaccineIdsList) {
        this.vaccineIdsList = vaccineIdsList;
        onClick(btVaccine);
    }

    public void openFragment(Fragment fragment, LinearLayout containerVaccine) {
        FragmentTransaction fragmentTransaction = mActivity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(containerVaccine.getId(), fragment, fragment.getClass().getSimpleName());
//        Bundle bundle = new Bundle();
//        bundle.putParcelable(VaccinationListViewHolder.TAG_VACCINE_DATA, Parcels.wrap(vaccineResponse));
//        fragment.setArguments(bundle);
        fragmentTransaction.commitAllowingStateLoss();
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
}
