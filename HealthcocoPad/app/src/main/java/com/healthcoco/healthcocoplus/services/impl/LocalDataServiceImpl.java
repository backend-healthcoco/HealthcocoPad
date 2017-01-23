package com.healthcoco.healthcocoplus.services.impl;

import com.healthcoco.healthcocoplus.HealthCocoApplication;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.utilities.Util;

/**
 * Created by Shreshtha on 23-01-2017.
 */
@SuppressWarnings("unchecked")

public class LocalDataServiceImpl {
    private static final String TAG = LocalDataServiceImpl.class.getSimpleName();
    private static LocalDataServiceImpl mInstance;
    private static HealthCocoApplication mApp;

    private LocalDataServiceImpl() {
    }

    public static LocalDataServiceImpl getInstance(HealthCocoApplication application) {
        if (mInstance == null) {
            mInstance = new LocalDataServiceImpl();
            mApp = application;
        }
        Util.checkNetworkStatus(mApp.getApplicationContext());
        return mInstance;
    }

    /**
     * Delete all records
     */
    private void clearDoctor() {
        LoginResponse.deleteAll(LoginResponse.class);
    }

    public void addDoctor(LoginResponse doctor) {
        if (doctor.getUser() != null) {
            // setting user
            if (!Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                doctor.setForeignUserId(doctor.getUser().getUniqueId());
            }
            clearDoctor();
            doctor.save();
        }
    }
}