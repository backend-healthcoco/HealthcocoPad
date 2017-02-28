package com.healthcoco.healthcocoplus.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.bean.UserPermissionsResponse;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocoplus.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocoplus.utilities.Util;

/**
 * Created by Shreshtha on 28-02-2017.
 */
public class PrescriptionUIPermissionFragment extends HealthCocoFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ui_permission_prescription, null);
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
        initViews();
        initListeners();

//        initAdapters();
//        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
//        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
//            user = doctor.getUser();
//        }
//        getUIPermissions();
    }

    private void getUIPermissions() {
//        if (user != null) {
//            WebDataServiceImpl.getInstance(mApp).getUIPermissions(UserPermissionsResponse.class, user.getUniqueId(), this, this);
//        }
    }

    @Override
    public void initViews() {

    }

    @Override
    public void initListeners() {

    }
}
