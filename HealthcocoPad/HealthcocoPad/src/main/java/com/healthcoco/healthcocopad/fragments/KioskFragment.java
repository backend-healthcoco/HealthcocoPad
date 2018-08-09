package com.healthcoco.healthcocopad.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.KioskActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.FeedbackType;
import com.healthcoco.healthcocopad.enums.KioskScreenType;
import com.healthcoco.healthcocopad.enums.KioskSubItemType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;

import java.util.ArrayList;

import static com.healthcoco.healthcocopad.enums.KioskSubItemType.DOCTOR_AND_CLINIC;
import static com.healthcoco.healthcocopad.enums.KioskSubItemType.PATIENT_REGISTER;
import static com.healthcoco.healthcocopad.enums.KioskSubItemType.VIDEO;

/**
 * Created by Prashant on 23-06-18.
 */

public class KioskFragment extends HealthCocoFragment implements
        View.OnClickListener, LocalDoInBackgroundListenerOptimised, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, HealthcocoRecyclerViewItemClickListener {


    private KioskSubItemType kioskSubItemType;
    private RecyclerView optionsRecyclerView;
    private LinearLayout layoutLeft;
    private Button btLock;
    private TextView tvClinicName;
    private HealthcocoRecyclerViewAdapter mAdapter;
    private ArrayList<KioskSubItemType> subItemTypeArrayList = new ArrayList<>();
    private boolean receiversRegistered;
    private User user;
    private DoctorClinicProfile doctorClinicProfile;
    private PatientRegistrationDetailsListener registrationDetailsListener;

    public KioskFragment(PatientRegistrationDetailsListener registrationDetailsListener) {
        super();
        this.registrationDetailsListener = registrationDetailsListener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_kiosk, container, false);
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
        layoutLeft = (LinearLayout) view.findViewById(R.id.layout_left);
        btLock = (Button) view.findViewById(R.id.bt_kiosk_lock);
        tvClinicName = (TextView) view.findViewById(R.id.tv_clinic_name);
    }

    @Override
    public void initListeners() {
        layoutLeft.setOnClickListener(this);
        btLock.setOnClickListener(this);
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
        subItemTypeArrayList.add(PATIENT_REGISTER);
        subItemTypeArrayList.add(DOCTOR_AND_CLINIC);
        subItemTypeArrayList.add(VIDEO);
        subItemTypeArrayList.add(KioskSubItemType.FEEDBACK);
        subItemTypeArrayList.add(KioskSubItemType.BLOGS);
        subItemTypeArrayList.add(KioskSubItemType.FOLLOW_UP_APPOINTMENT);

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        initData();
                        if (doctorClinicProfile != null)
                            setClinicName();
                        return;
                    }
                    break;
            }
        }
    }

    private void setClinicName() {
        if (doctorClinicProfile != null && doctorClinicProfile.getLocationName() != null)
            tvClinicName.setText(doctorClinicProfile.getLocationName());
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
                    doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
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
        switch (v.getId()) {
            case R.id.layout_left:
                break;
            case R.id.bt_kiosk_lock:
                registrationDetailsListener.readyToMoveNext(KioskScreenType.PINVIEW.ordinal(), false);
                break;
        }
    }

    @Override
    public void onItemClicked(Object object) {
        if (object != null) {
            int ordinal = (int) object;
            KioskSubItemType subItemType = KioskSubItemType.values()[ordinal];
            switch (subItemType) {
                case PATIENT_REGISTER:
                    openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_REGISTRATION_TABS, HealthCocoConstants.TAG_MOBILE_NUMBER, null);
                    break;
                case DOCTOR_AND_CLINIC:
                    openCommonOpenUpActivity(CommonOpenUpFragmentType.ABOUT_DOCTOR, "ABOUT_DOCTOR", null);
                    break;
                case VIDEO:
                    openCommonOpenUpActivity(CommonOpenUpFragmentType.EDUCATION_VIDEO, "VIDEO", null);
                    break;
                case FEEDBACK:
                    openCommonOpenUpActivity(CommonOpenUpFragmentType.FEEDBACK_DOCTOR, AppointmentFeedbackFragment.TAG_FEEDBACK_TYPE, FeedbackType.DOCTOR.ordinal(), 0);
                    break;
                case BLOGS:
                    openCommonOpenUpActivity(CommonOpenUpFragmentType.BLOGS, "BLOGS", null);
                    break;
                case FOLLOW_UP_APPOINTMENT:
                    openCommonOpenUpActivity(CommonOpenUpFragmentType.FOLLOW_UP_APPOINTMENT, "FOLLOW_UP_APPOINTMENT", null);
                    break;
            }
        }
    }

}