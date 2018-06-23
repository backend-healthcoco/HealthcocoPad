package com.healthcoco.healthcocopad.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.KioskSubItemType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;

import java.util.ArrayList;

/**
 * Created by Prashant on 23-06-18.
 */

public class KioskHomeFragment extends HealthCocoFragment implements
        View.OnClickListener, LocalDoInBackgroundListenerOptimised, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {


    private KioskSubItemType kioskSubItemType;
    private RecyclerView optionsRecyclerView;
    private HealthcocoRecyclerViewAdapter mAdapter;
    private ArrayList<KioskSubItemType> subItemTypeArrayList = new ArrayList<>();
    private boolean receiversRegistered;
    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kiosk_home, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
    }

    private void initAdapter() {
//        adapter = new CommTabContentGridAdapter(mActivity);
//        gvOptions.setAdapter(adapter);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false);
        optionsRecyclerView.setLayoutManager(layoutManager);
        optionsRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.KIOSK_SUB_ITEM, this);
        mAdapter.setListData((ArrayList<Object>) (Object) subItemTypeArrayList);
        optionsRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void initViews() {
        optionsRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_options);
    }

    @Override
    public void initListeners() {
//        gvOptions.setOnItemClickListener(this);
    }


    public void initData() {
      /*  if (doctorProfile != null) {
            DoctorClinicProfile doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(doctorProfile.getDoctorId(), MenuFragment.SELECTED_LOCATION_ID);
            if (doctorClinicProfile != null) {
                if (doctorClinicProfile.getClinic()) {
                    if (!Util.isNullOrEmptyList(doctorProfile.getParentSpecialities())) {
                        // For only Dentist
                        if (doctorProfile.getParentSpecialities().contains("Dentist")) {
                            if (doctorClinicProfile.getLab() && !doctorClinicProfile.getParent())
                                adapter.setListData(TabSubItemsType.getForDentistsLabTabSubItemsTypeList());
                            else
                                adapter.setListData(TabSubItemsType.getDentistTabSubItemsTypeList());
                        } else {
                            if (doctorClinicProfile.getLab() && !doctorClinicProfile.getParent()) {
                                adapter.setListData(TabSubItemsType.getForLabTabSubItemsTypeList());
                            } else
                                adapter.setListData(TabSubItemsType.getNormalTabSubItemsTypeList());
                        }
                    } else
                        adapter.setListData(TabSubItemsType.getNormalTabSubItemsTypeList());
                } else if (doctorClinicProfile.getLab() && !doctorClinicProfile.getParent()) {
                    adapter.setListData(TabSubItemsType.getForLabTabSubItemsTypeList());
                } else {
                    // For only parent lab
                    adapter.setListData(TabSubItemsType.getNormalTabSubItemsTypeList());
                }
            }
            adapter.notifyDataSetChanged();
        }*/
        subItemTypeArrayList.add(KioskSubItemType.PATIENT_REGISTRATION);
        subItemTypeArrayList.add(KioskSubItemType.KNOW_YOUR_DOCTOR);
        subItemTypeArrayList.add(KioskSubItemType.VIDEOS);
        subItemTypeArrayList.add(KioskSubItemType.FEEDBACK);
        subItemTypeArrayList.add(KioskSubItemType.HEALTHCOCO_BLOG);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        initData();
                        return;
                    }
                    break;
            }
        }
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null) {
                    user = doctor.getUser();
                }
                return volleyResponseBean;
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
    public void onClick(View v) {

    }
}