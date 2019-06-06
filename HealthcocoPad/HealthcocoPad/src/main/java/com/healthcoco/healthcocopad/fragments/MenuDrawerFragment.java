package com.healthcoco.healthcocopad.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.HomeActivity;
import com.healthcoco.healthcocopad.bean.MenuItem;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.MenuClinicListDialogFragment;
import com.healthcoco.healthcocopad.drawer.MenuListAdapter;
import com.healthcoco.healthcocopad.enums.AccountPackageType;
import com.healthcoco.healthcocopad.enums.BooleanTypeValues;
import com.healthcoco.healthcocopad.enums.FragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Shreshtha on 28-01-2017.
 */

public class MenuDrawerFragment extends HealthCocoFragment implements View.OnClickListener, AdapterView.OnItemClickListener, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean> {
    public static final String INTENT_REFRESH_DOCTOR_PROFILE = "com.healthcoco.healthcocopad.fragments.REFRESH_DOCTOR_PROFILE";
    public static final String INTENT_REFRESH_PATIENT_COUNT = "com.healthcoco.healthcocopad.fragments.REFRESH_PATIENT_COUNT";
    public static String SELECTED_LOCATION_ID = "";
    private LinkedHashMap<FragmentType, MenuItem> menuListHashMap = new LinkedHashMap<>();
    BroadcastReceiver refreshDoctorReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getDoctorProfile();
        }
    };
    private LinearLayout itemProfileHeader;
    private ListView lvMenuList;
    private ArrayList<FragmentType> list;
    private TextView tvProfileName;
    private TextView tvEmailId;
    private ProgressBar progressLoading;
    private ImageView ivImage;
    private MenuListAdapter menuListAdapter;
    private DoctorProfile doctorProfile;
    private TextView tvInitialAlphabet;
    private TextView tvClinicName;
    private int selectedPosition;
    private FragmentType selectedFragmentType;
    private boolean receiversRegistered;
    private LinearLayout manageClinicLayout;
    private List<DoctorClinicProfile> clinicProfile;
    private boolean isKiosk;
    private User user;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_master, null);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for filter refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_REFRESH_DOCTOR_PROFILE);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshDoctorReceiver, filter);

            //receiver for filter refresh
            IntentFilter filter1 = new IntentFilter();
            filter1.addAction(INTENT_REFRESH_PATIENT_COUNT);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshPatientCountReceiver, filter1);

            receiversRegistered = true;
        }
    }

    private void getDoctorProfile() {
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DOCTOR_PROFILE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initMenuListAdapter();
//        openFragment(FragmentType.CONTACTS);
//        ((HomeActivity) mActivity).initContactsFragment();
    }

    private void initMenuListAdapter() {
        menuListAdapter = new MenuListAdapter(mActivity);
        lvMenuList.setAdapter(menuListAdapter);
    }

    private void openFragment(FragmentType fragmentType) {
        ((HomeActivity) mActivity).closePaneLayout(fragmentType);
    }

    @Override
    public void initViews() {
        ivImage = (ImageView) view.findViewById(R.id.iv_image);
        lvMenuList = (ListView) view.findViewById(R.id.lvMenuList);
        tvEmailId = (TextView) view.findViewById(R.id.tv_email_id);
        tvClinicName = (TextView) view.findViewById(R.id.tv_clinic_name);
        tvProfileName = (TextView) view.findViewById(R.id.tv_profile_name);
        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);
        itemProfileHeader = (LinearLayout) view.findViewById(R.id.item_profile_header);
        manageClinicLayout = (LinearLayout) view.findViewById(R.id.manage_clinic_layout);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
    }

    @Override
    public void initListeners() {
        ivImage.setOnClickListener(this);
        tvClinicName.setOnClickListener(this);
        itemProfileHeader.setOnClickListener(this);
        manageClinicLayout.setOnClickListener(this);
        lvMenuList.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.item_profile_header:
                openFragment(FragmentType.PROFILE);
                ((HomeActivity) mActivity).initFragment(FragmentType.PROFILE);
                break;
            case R.id.tv_clinic_name:
                if (clinicProfile.size() > 1) {
                    openClinicListDialogFragment();
                }
                break;
            case R.id.iv_image:
                if (doctorProfile != null && !Util.isNullOrBlank(doctorProfile.getImageUrl())) {
                    mActivity.openEnlargedImageDialogFragment(doctorProfile.getImageUrl());
                }
                break;
            case R.id.manage_clinic_layout:
                openClinicListDialogFragment();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        selectedFragmentType = list.get(position);
        selectedPosition = position;
        if (selectedFragmentType == null) {
            selectedFragmentType = FragmentType.CONTACTS;
        }
        openFragment(selectedFragmentType);
        ((HomeActivity) mActivity).initFragment(selectedFragmentType);
    }

    public void setMenuSelection(FragmentType fragmentType) {
        if (menuListAdapter != null)
            menuListAdapter.setSetSelectedPosition(fragmentType);
    }

    public void initData(DoctorProfile doctorProfile) {
        if (doctorProfile != null) {
            this.doctorProfile = doctorProfile;
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_MENU, doctorProfile, progressLoading, ivImage, tvInitialAlphabet);
            String title = doctorProfile.getTitle(false);
            if (Util.isNullOrBlank(title))
                title = getResources().getString(R.string.dr);
            tvProfileName.setText(title + Util.getValidatedValue(doctorProfile.getFirstName()));

            tvEmailId.setText(Util.getValidatedValue(doctorProfile.getEmailAddress()));
//            tvClinicName.setText(Util.getValidatedValue(doctorProfile.getClinicProfile().get(selectedPosition).getLocationName()));
            clinicProfile = doctorProfile.getClinicProfile();
            initSelectedLocationId(getSelectedLocationId());
            refreshMenuListAdapter();
            if (selectedFragmentType != null && lvMenuList.findViewWithTag(selectedFragmentType) != null) {
                View viewItem = lvMenuList.findViewWithTag(selectedFragmentType);
                viewItem.setSelected(true);
            }
            lvMenuList.setItemChecked(selectedPosition, true);
            if (clinicProfile.size() == 1)
                tvClinicName.setCompoundDrawables(null, null, null, null);

        }
//        swipeRefreshLayout.setRefreshing(false);
    }

    private void refreshMenuListAdapter() {
        if (!Util.isNullOrBlank(SELECTED_LOCATION_ID)) {
            DoctorClinicProfile doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(doctorProfile.getDoctorId(), SELECTED_LOCATION_ID);
            if (doctorClinicProfile != null) {
                refreshSelectedDoctorClinicProfileDetails(doctorClinicProfile);
                if (doctorClinicProfile.getIskiosk() != null)
                    isKiosk = doctorClinicProfile.getIskiosk();

                if (doctorClinicProfile.getPackageType() != null) {
                    AccountPackageType packageType = doctorClinicProfile.getPackageType();
                    menuListHashMap = packageType.getMenuItemList();
                    if (!Util.isNullOrEmptyList(menuListHashMap)) {

                        if (doctorProfile != null) {
                            if (!Util.isNullOrEmptyList(doctorProfile.getSpecialities()) && doctorProfile.getSpecialities().contains("Dentist")) {
                                if (packageType == AccountPackageType.ADVANCE || packageType == AccountPackageType.PRO)
                                    menuListHashMap.put(FragmentType.VIDEOS, new MenuItem(FragmentType.VIDEOS, "", false));
                            } else
                                menuListHashMap.remove(FragmentType.VIDEOS);
                        }
                        if (isKiosk)
                            menuListHashMap.put(FragmentType.KIOSK, new MenuItem(FragmentType.KIOSK, "", false));
                        else
                            menuListHashMap.remove(FragmentType.KIOSK);

                        list = new ArrayList<>();
                        list.addAll(menuListHashMap.keySet());
                    }
                } else {
                    list = new ArrayList<>();
                    list.add(FragmentType.CONTACTS);
                    list.add(FragmentType.SETTINGS);
                }

                notifyMenuListAdapter(new ArrayList<>(menuListHashMap.values()));
            }
        }
    }


    private String getSelectedLocationId() {
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            User user = doctor.getUser();
            return user.getForeignLocationId();
        }
        return null;
    }

    private void initSelectedLocationId(String selectedLocationId) {
        SELECTED_LOCATION_ID = selectedLocationId;
        if (Util.isNullOrBlank(SELECTED_LOCATION_ID) &&
                doctorProfile != null && !Util.isNullOrEmptyList(doctorProfile.getClinicProfile())) {
            SELECTED_LOCATION_ID = doctorProfile.getClinicProfile().get(0).getLocationId();
        }
    }

    private void refreshSelectedDoctorClinicProfileDetails(DoctorClinicProfile doctorClinicProfile) {
        SELECTED_LOCATION_ID = doctorClinicProfile.getLocationId();
        if (Util.isNullOrBlank(SELECTED_LOCATION_ID) &&
                doctorProfile != null && !Util.isNullOrEmptyList(doctorProfile.getClinicProfile())) {
            SELECTED_LOCATION_ID = doctorProfile.getClinicProfile().get(0).getLocationId();
        }
        if (!Util.isNullOrBlank(SELECTED_LOCATION_ID)) {
            tvClinicName.setVisibility(View.VISIBLE);
            DoctorClinicProfile clinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(doctorProfile.getDoctorId(), SELECTED_LOCATION_ID);
            tvClinicName.setText(clinicProfile.getLocationName());
            LocalDataServiceImpl.getInstance(mApp).updatedSelectedLocationDetails(doctorProfile.getUserId(), clinicProfile);
        }
    }

    private void openClinicListDialogFragment() {
        openDialogFragment(new MenuClinicListDialogFragment(clinicProfile), HealthCocoConstants.REQUEST_CODE_MENU_CLINIC_LIST);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_MENU_CLINIC_LIST) {
            if (resultCode == Activity.RESULT_OK) {
                Util.sendBroadcast(mApp, ContactsListFragment.INTENT_REFRESH_CONTACTS_LIST_FROM_SERVER, true);
                Util.sendBroadcast(mApp, ContactsListFragment.INTENT_GET_CLINIC_PROFILE, true);
                Util.sendBroadcast(mApp, MyClinicFragment.INTENT_GET_DOCTOR_CLINIC_PROFILE, true);
                openFragment(FragmentType.CONTACTS);
                initSelectedLocationId(SELECTED_LOCATION_ID);
            }
        }
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case GET_DOCTOR_PROFILE:
                    if (response.isDataFromLocal()) {
                        initData((DoctorProfile) response.getData());
                    }
                    break;
                case GET_PATIENTS_COUNT:
                    String notifNo = "";
                    if (response.getData() != null && response.getData() instanceof String) {
                        notifNo = (String) response.getData();
                    }
                    if (!Util.isNullOrEmptyList(menuListHashMap)) {
                        MenuItem menuItem = menuListHashMap.get(FragmentType.CONTACTS);
                        if (menuItem != null) {
                            menuItem.setNotifNo(notifNo);
                            menuItem.setShowLoader(false);
                            menuListHashMap.put(FragmentType.CONTACTS, menuItem);
                            notifyMenuListAdapter(new ArrayList<>(menuListHashMap.values()));
                        }
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void notifyMenuListAdapter(ArrayList<MenuItem> list) {
        menuListAdapter.setListData(list);
        menuListAdapter.notifyDataSetChanged();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {
            case GET_DOCTOR_PROFILE:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileResponse(WebServiceType.GET_DOCTOR_PROFILE, doctorProfile.getDoctorId(), null, null);
                break;
            case GET_PATIENT_COUNT:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getListSize(LocalTabelType.REGISTERED_PATIENTS_DETAILS_NEW, BooleanTypeValues.FALSE, null, null);
                break;
        }
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    public void refreshMenuItem() {
        menuListAdapter.notifyDataSetChanged();
    }

    BroadcastReceiver refreshPatientCountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            refreshPatientsCOunt();
        }
    };

    private void refreshPatientsCOunt() {
        MenuItem menuItem = menuListHashMap.get(FragmentType.CONTACTS);
        if (menuItem != null)
            menuItem.setShowLoader(true);
        notifyMenuListAdapter(new ArrayList<MenuItem>(menuListHashMap.values()));
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_PATIENT_COUNT, MenuDrawerFragment.this, MenuDrawerFragment.this, MenuDrawerFragment.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshPatientCountReceiver);
    }

}
