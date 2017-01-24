package com.healthcoco.healthcocoplus.services.impl;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoApplication;
import com.healthcoco.healthcocoplus.bean.VolleyResponseBean;
import com.healthcoco.healthcocoplus.bean.server.CityResponse;
import com.healthcoco.healthcocoplus.bean.server.LoginResponse;
import com.healthcoco.healthcocoplus.bean.server.Specialities;
import com.healthcoco.healthcocoplus.enums.LocalTabelType;
import com.healthcoco.healthcocoplus.enums.WebServiceType;
import com.healthcoco.healthcocoplus.services.GsonRequest;
import com.healthcoco.healthcocoplus.utilities.DateTimeUtil;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.util.ArrayList;
import java.util.List;

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

    public long getLatestUpdatedTime(LocalTabelType localTabelType) {
        Long latestUpdatedTime = 0l;
        switch (localTabelType) {
            case CITIES:
                List<CityResponse> tempCitiesList = CityResponse.find(CityResponse.class, null, null, null, "updated_time DESC", "1");
                if (!Util.isNullOrEmptyList(tempCitiesList))
                    latestUpdatedTime = tempCitiesList.get(0).getUpdatedTime();
                break;
        }
        if (latestUpdatedTime == null)
            latestUpdatedTime = 0l;
        LogUtils.LOGD(TAG, "Latest Updated Time for " + localTabelType + " : " + DateTimeUtil.getFormatedDateAndTime(latestUpdatedTime) + " , " + latestUpdatedTime);
        return latestUpdatedTime;
    }

    public void addSpecialities(ArrayList<Specialities> specialitiesResponse) {
        Specialities.saveInTx(specialitiesResponse);
    }

    public VolleyResponseBean getSpecialitiesListVolleyResponse(WebServiceType webServiceType, Response.Listener<VolleyResponseBean> responseListener, GsonRequest.ErrorListener errorListener) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setWebServiceType(webServiceType);
        volleyResponseBean.setDataFromLocal(true);
        volleyResponseBean.setUserOnline(HealthCocoConstants.isNetworkOnline);
        try {
            List<Specialities> list = getSpecialitiesListObject();
            volleyResponseBean.setDataList(getObjectsListFromMap(list));
            if (responseListener != null)
                responseListener.onResponse(volleyResponseBean);
        } catch (Exception e) {
            e.printStackTrace();
            showErrorLocal(volleyResponseBean, errorListener);
        }
        return volleyResponseBean;
    }

    public List<Specialities> getSpecialitiesListObject() {
        return Specialities.listAll(Specialities.class);
    }

    private ArrayList<Object> getObjectsListFromMap(List<?> list) {
        if (list != null) {
            ArrayList<Object> listNew = new ArrayList<Object>();
            listNew.addAll(list);
            return listNew;
        }
        return null;
    }

    private void showErrorLocal(VolleyResponseBean volleyResponseBean, GsonRequest.ErrorListener errorListener) {
        if (errorListener != null)
            errorListener.onErrorResponse(volleyResponseBean, mApp.getResources().getString(R.string
                    .error_local_data));
        else
            volleyResponseBean.setErrMsg(mApp.getResources().getString(R.string
                    .error_local_data));
    }
}