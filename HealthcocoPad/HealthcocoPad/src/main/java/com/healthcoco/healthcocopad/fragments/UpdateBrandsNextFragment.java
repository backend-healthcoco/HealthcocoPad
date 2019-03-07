package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.VaccineCustomResponse;
import com.healthcoco.healthcocopad.viewholders.VaccinationListViewHolder;

import org.parceler.Parcels;

public class UpdateBrandsNextFragment extends HealthCocoFragment {
    private VaccineCustomResponse vaccineResponse;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_update_brands_next, container, false);
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
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {

    }
}
