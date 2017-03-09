package com.healthcoco.healthcocoplus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class PatientVisitDetailFragment extends HealthCocoFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_visit_deatil, container, false);
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

    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {

    }
}
