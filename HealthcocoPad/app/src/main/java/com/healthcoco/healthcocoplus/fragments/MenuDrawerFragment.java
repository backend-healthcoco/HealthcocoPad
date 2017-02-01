package com.healthcoco.healthcocoplus.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.activities.HomeActivity;
import com.healthcoco.healthcocoplus.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocoplus.bean.server.DoctorProfile;
import com.healthcoco.healthcocoplus.dialogFragment.LoginDialogFragment;
import com.healthcoco.healthcocoplus.dialogFragment.MenuClinicListDialogFragment;
import com.healthcoco.healthcocoplus.drawer.MenuListAdapter;
import com.healthcoco.healthcocoplus.enums.FragmentType;
import com.healthcoco.healthcocoplus.enums.PatientProfileScreenType;
import com.healthcoco.healthcocoplus.utilities.HealthCocoConstants;
import com.healthcoco.healthcocoplus.utilities.Util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 28-01-2017.
 */

public class MenuDrawerFragment extends HealthCocoFragment implements View.OnClickListener, AdapterView.OnItemClickListener {
    public static final String INTENT_REFRESH_DOCTOR_PROFILE = "com.healthcoco.healthcocoplus.fragments.REFRESH_DOCTOR_PROFILE";
    public static String SELECTED_LOCATION_ID = "";

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
    private ListView lvClinicsList;
    private View view;
    private RelativeLayout profile_layout;
    private LinearLayout manage_clinic_layout;
    private List<DoctorClinicProfile> clinicProfile;

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

            receiversRegistered = true;
        }
    }

    BroadcastReceiver refreshDoctorReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
//            getDoctorProfile();
        }
    };

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initMenuListAdapter();
        openFragment(FragmentType.CONTACTS);
        ((HomeActivity) mActivity).initContactsFragment();
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
        manage_clinic_layout = (LinearLayout) view.findViewById(R.id.manage_clinic_layout);
        profile_layout = (RelativeLayout) view.findViewById(R.id.profile_layout);
        progressLoading = (ProgressBar) view.findViewById(R.id.progress_loading);
    }

    @Override
    public void initListeners() {
        ivImage.setOnClickListener(this);
        tvClinicName.setOnClickListener(this);
        itemProfileHeader.setOnClickListener(this);
        manage_clinic_layout.setOnClickListener(this);
        lvMenuList.setOnItemClickListener(this);
    }

    private void initMenuListAdapter() {
        list = new ArrayList<>();
        list.add(FragmentType.CONTACTS);
        list.add(FragmentType.CALENDAR);
        list.add(FragmentType.PROFILE);
        list.add(FragmentType.CLINIC_PROFILE);
        list.add(FragmentType.ISSUE_TRACKER);
        list.add(FragmentType.SYNC);
        list.add(FragmentType.HELP_IMPROVE);
        list.add(FragmentType.SETTINGS);
        menuListAdapter = new MenuListAdapter(mActivity);
        menuListAdapter.setListData(list);
        lvMenuList.setAdapter(menuListAdapter);
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
//            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_MENU, doctorProfile, progressLoading, ivImage, tvInitialAlphabet);
            String title = doctorProfile.getTitle(false);
            if (Util.isNullOrBlank(title))
                title = getResources().getString(R.string.dr);
            tvProfileName.setText(title + Util.getValidatedValue(doctorProfile.getFirstName()));

            tvEmailId.setText(Util.getValidatedValue(doctorProfile.getEmailAddress()));
            tvClinicName.setText(Util.getValidatedValue(doctorProfile.getClinicProfile().get(selectedPosition).getLocationName()));
            clinicProfile = doctorProfile.getClinicProfile();
//            notifyClinicListAdapter(doctorProfile.getClinicProfile());
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

    private void openClinicListDialogFragment() {
        Bundle args = new Bundle();
        MenuClinicListDialogFragment clinicListDialogFragment = new MenuClinicListDialogFragment(clinicProfile);
        clinicListDialogFragment.setArguments(args);
        clinicListDialogFragment.show(mFragmentManager, clinicListDialogFragment.getClass().getSimpleName());
    }
}
