package com.healthcoco.healthcocopad.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toolbar;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.ClinicDetailResponse;
import com.healthcoco.healthcocopad.bean.server.ClinicImage;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.Location;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.DoctorAndClinicTabsType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientRegistrationTabsType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomViewPager;

import java.util.ArrayList;

/**
 * Created by Prashant on 25-06-18.
 */

public class AboutDoctorFragment extends HealthCocoFragment implements ViewPager.OnPageChangeListener,
        View.OnClickListener, PatientRegistrationDetailsListener, TabHost.OnTabChangeListener, LocalDoInBackgroundListenerOptimised {
    private DoctorListFragment doctorListFragment;
    private ClinicPhotosFragment clinicPhotosFragment;
    private AboutClinicFragment aboutClinicFragment;
    private TabHost tabhost;
    private CustomViewPager mViewPager;
    private ArrayList<Fragment> fragmentsList = new ArrayList<>();
    private User user;
    private DoctorProfile doctorProfile;
    private ClinicDetailResponse clinicDetailResponse;
    private ViewPager viewPager;
    private LinearLayout containerBullets;
    private ContactsDetailViewPagerAdapter imageViewPagerAdapter;
    private TextView tvClinicName;
    private TextView tvClinicAddress;
    private ImageView ivLogoImage;
    private FrameLayout parentlogo;
    private TextView tvLogoLabel;
    private CollapsingToolbarLayout collapsingToolbarLayout;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about_doctor, container, false);
        super.onCreateView(inflater, container, savedInstanceState);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        init();
//        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initTabs();
        initViewPagerAdapter();
    }

    public void initViews() {
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabhost.setup();
        mViewPager = (CustomViewPager) view.findViewById(R.id.viewpager_home_tabs);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        containerBullets = (LinearLayout) view.findViewById(R.id.container_bullets);
        tvClinicName = (TextView) view.findViewById(R.id.tv_clinic_name);
        tvClinicAddress = (TextView) view.findViewById(R.id.tv_clinic_address);

        tvLogoLabel = (TextView) view.findViewById(R.id.tv_logo_label);
        ivLogoImage = (ImageView) view.findViewById(R.id.iv_logo_image);
        parentlogo = (FrameLayout) view.findViewById(R.id.parent_logo);


        mActivity.setSupportActionBar((android.support.v7.widget.Toolbar) view.findViewById(R.id.toolbar));
        mActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle("");
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
    }

    public void initListeners() {
        mViewPager.addOnPageChangeListener(this);
        tabhost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);
    }


    private void initTabs() {
        fragmentsList.clear();
        tabhost.clearAllTabs();
        if (mViewPager.getAdapter() != null)
            mViewPager.getAdapter().notifyDataSetChanged();
        else {
            doctorListFragment = new DoctorListFragment();
            addFragment(DoctorAndClinicTabsType.DOCTORS, doctorListFragment);

            clinicPhotosFragment = new ClinicPhotosFragment();
            addFragment(DoctorAndClinicTabsType.PHOTOS, clinicPhotosFragment);

            aboutClinicFragment = new AboutClinicFragment();
            addFragment(DoctorAndClinicTabsType.ABOUT, aboutClinicFragment);
        }
    }

    private void addFragment(DoctorAndClinicTabsType doctorAndClinicTabsType, Fragment fragment) {
        tabhost.addTab(getTabSpec(doctorAndClinicTabsType, fragment));
//        tabhost.getTabWidget().getChildTabViewAt(registrationTabsType.getTabPosition()).setEnabled(false);
    }

    private TabHost.TabSpec getTabSpec(DoctorAndClinicTabsType doctorAndClinicTabsType, Fragment fragment) {
        fragmentsList.add(fragment);
        View view1 = mActivity.getLayoutInflater().inflate(R.layout.tab_indicator_about_doctor, null);
        TextView tvTabText = (TextView) view1.findViewById(R.id.tv_tab_text);
        tvTabText.setText(doctorAndClinicTabsType.getStringValue());
        return tabhost.newTabSpec(String.valueOf(doctorAndClinicTabsType)).setIndicator(view1).setContent(new DummyTabFactory(mActivity));
    }

    private void initViewPagerAdapter() {
        mViewPager.setOffscreenPageLimit(fragmentsList.size());
        ContactsDetailViewPagerAdapter viewPagerAdapter = new ContactsDetailViewPagerAdapter(mActivity.getSupportFragmentManager());
        viewPagerAdapter.setFragmentsList(fragmentsList);
        mViewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        View tabView = tabhost.getTabWidget().getChildAt(position);
    }

    @Override
    public void onPageSelected(int position) {
        tabhost.setCurrentTab(position);
        setBulletSelected(position, containerBullets);
    }


    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    @Override
    public void readyToMoveNext(Object object, boolean isEditPatient) {

    }

    @Override
    public void onTabChanged(String tabId) {
        mViewPager.setCurrentItem(tabhost.getCurrentTab());
    }


    private void initImageViewPagerAdapter() {
        containerBullets.removeAllViews();
        ArrayList<Fragment> fragmentsList = new ArrayList<>();
        if (clinicDetailResponse != null && clinicDetailResponse.getLocation() != null
                && !Util.isNullOrEmptyList(clinicDetailResponse.getLocation().getImages())) {
            Location location = clinicDetailResponse.getLocation();
            for (ClinicImage clinicImage :
                    location.getImages()) {
                if (!Util.isNullOrBlank(clinicImage.getImageUrl())) {
                    Bundle bundle = new Bundle();
                    bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, clinicImage.getCustomUniqueId());
                    ClinicImageFragment fragment = new ClinicImageFragment();
                    fragment.setArguments(bundle);
                    fragmentsList.add(fragment);
                    addBullet(containerBullets, R.layout.bullet_white_translucent_font_awesom);
                }
            }
            viewPager.setOffscreenPageLimit(fragmentsList.size());
            setBulletSelected(0, containerBullets);
        }
        imageViewPagerAdapter = new ContactsDetailViewPagerAdapter(
                mActivity.getSupportFragmentManager());
        imageViewPagerAdapter.setFragmentsList(fragmentsList);
        viewPager.setAdapter(imageViewPagerAdapter);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        getDoctorProfileFromLocal();
                        return;
                    }
                    break;
                case GET_DOCTOR_PROFILE:
                    if (response.getData() != null) {
                        if (response.isDataFromLocal()) {
                            doctorProfile = (DoctorProfile) response.getData();
                            initData(doctorProfile);
                            mActivity.refreshMenuFragment(doctorProfile);
                            if (doctorProfile == null) {
                                WebDataServiceImpl.getInstance(mApp).getDoctorProfile(DoctorProfile.class, user.getUniqueId(), null, null, this, this);
                                return;
                            }
                        } else {
                            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DOCTOR_PROFILE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        }
                    } else {
                        WebDataServiceImpl.getInstance(mApp).getDoctorProfile(DoctorProfile.class, user.getUniqueId(), null, null, this, this);
                        return;
                    }
                    break;
            }
        }
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
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_DOCTOR_PROFILE:
                LocalDataServiceImpl.getInstance(mApp).
                        addDoctorProfile((DoctorProfile) response.getData());
            case GET_DOCTOR_PROFILE:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileResponse(WebServiceType.GET_DOCTOR_PROFILE, user.getUniqueId(), null, null);
                break;
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                    if (user != null)
                        clinicDetailResponse = LocalDataServiceImpl.getInstance(mApp).getClinicResponseDetails(user.getForeignLocationId());
                }
                return volleyResponseBean;
        }
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    private void getDoctorProfileFromLocal() {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised
                (mActivity, LocalBackgroundTaskType.GET_DOCTOR_PROFILE, this,
                        this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void initData(DoctorProfile doctorProfile) {
        this.doctorProfile = doctorProfile;
//        initImageViewPagerAdapter();
        if (aboutClinicFragment != null) {
            if (clinicDetailResponse != null) {
                Location location = clinicDetailResponse.getLocation();
                if (location != null) {
                    refreshClinicProfileData(location);
                    aboutClinicFragment.refreshClinicContacDetail(location);
                }
            }
            aboutClinicFragment.initData(doctorProfile);
        }

    }

    private void refreshClinicProfileData(Location location) {
        if (location != null) {
            tvClinicName.setText(Util.getValidatedValue(location.getLocationName()));
            tvClinicAddress.setText(location.getFormattedClinicAddress(mActivity));
            DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivLogoImage, location.getLogoThumbnailUrl());
            parentlogo.setVisibility(View.VISIBLE);
            collapsingToolbarLayout.setTitle(Util.getValidatedValue(location.getLocationName()));
            mActivity.hideLoading();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mActivity.finish();
                return true;
//            case R.id.item_share:
//                mActivity.shareText(mActivity.getResources().getString(R.string.share_healthcoco_subject),
//                        mActivity.getResources().getString(R.string.share_healthcoco_message)
//                                + mActivity.getResources().getString(R.string.link_bit_share));
//                return false;
//            case R.id.item_fav:
//                RegisteredPatientDetails patientDetails2 = ((CommonOpenUpActivity) mActivity).getSelectedRegisteredPatientDetails();
//                if (patientDetails2 != null)
//                    performFavoritesOperation();
//                else {
//                    openLoginSignupActivity(HealthCocoConstants.REQUEST_CODE_DOCTOR_PROFILE);
//                    Util.showToast(mActivity, R.string.please_login_first);
//                }
//                break;
            default:
                break;
        }
        return false;
    }
}
