package com.healthcoco.healthcocoplus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.User;
import com.healthcoco.healthcocoplus.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class PatientRegistrationFragment extends HealthCocoFragment implements View.OnClickListener {
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_registration, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        ((CommonOpenUpActivity) mActivity).initSaveButton(this);
    }

    @Override
    public void init() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
            initViews();
            initListeners();
//            initAutoTvAdapter(autotvBloodGroup, AutoCompleteTextViewType.BLOOD_GROUP, BLOOD_GROUPS);
//            initAutoTvAdapter(autotvCountry, AutoCompleteTextViewType.COUNTRY, (ArrayList<Object>) (ArrayList<?>) new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.array_countries))));
//            initDefaultData();
//            initData();
        }
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {

    }

    @Override
    public void onClick(View v) {

    }
}
