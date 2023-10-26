package com.healthcoco.healthcocopad.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.ClinicImage;
import com.healthcoco.healthcocopad.bean.server.Location;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 12-05-2017.
 */

public class ClinicPhotosFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised,
        Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, HealthcocoRecyclerViewItemClickListener {
    private User user;
    private Location clinicDetail;
    private List<ClinicImage> clinicImageList = new ArrayList<>();
    private HealthcocoRecyclerViewAdapter mAdapter;

    private RecyclerView imageRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_clinic_photos, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
    }

    @Override
    public void initViews() {
        imageRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_images);
    }

    private void initAdapter() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mActivity, 5);
        imageRecyclerView.setLayoutManager(gridLayoutManager);
        imageRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.CLINIC_IMAGE, this);
        mAdapter.setListData((ArrayList<Object>) (Object) clinicImageList);
        imageRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void initListeners() {
    }

    private void initData(Location location) {
        try {
            clinicDetail = location;
            if (clinicDetail != null) {
                clinicImageList = clinicDetail.getImages();
                if (!Util.isNullOrEmptyList(clinicImageList))
                    initAdapter();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void notifyAdapter() {
        mAdapter.notifyDataSetChanged();
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
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_LOCATION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        return;
                    }
                    break;
                case GET_CLINIC_PROFILE:
                    if (response.isDataFromLocal() && response.getData() != null) {
                        initData((Location) response.getData());
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId()))
                    user = doctor.getUser();
                break;
            case GET_LOCATION:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getLocationResponse(WebServiceType.GET_CLINIC_PROFILE, user.getForeignLocationId(), null, null);
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
    public void onItemClicked(Object object) {
        if (object != null && object instanceof ClinicImage) {
            ClinicImage clinicImage = (ClinicImage) object;
            if (clinicImage != null && !Util.isNullOrBlank(clinicImage.getImageUrl())) {
                mActivity.openEnlargedImageDialogFragment(clinicImage.getImageUrl());
            }
        }
    }
}