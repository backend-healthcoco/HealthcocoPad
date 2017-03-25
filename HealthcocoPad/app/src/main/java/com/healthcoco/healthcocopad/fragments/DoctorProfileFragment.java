package com.healthcoco.healthcocopad.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddEditDoctorProfileDialogFragment;
import com.healthcoco.healthcocopad.enums.FragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DoctorProfileListener;
import com.healthcoco.healthcocopad.listeners.DownloadFileFromUrlListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.SyncUtility;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 31-01-2017.
 */
public class DoctorProfileFragment extends HealthCocoFragment implements GsonRequest.ErrorListener,
        LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, View.OnClickListener,
        ViewPager.OnPageChangeListener, DoctorProfileListener, DownloadFileFromUrlListener {
    public static final String INTENT_GET_DOCTOR_PROFILE_DETAILS = "DoctorProfileFragment.DOCTOR_PROFILE_DETAILS";
    public static final String INTENT_GET_DOCTOR_PROFILE_DETAILS_LOCAL = "DoctorProfileFragment.DOCTOR_PROFILE_DETAILS_LOCAL";
    public static final String TAG_DOCTOR_PROFILE = "doctorProfile";

    private TextView tabMyProfile;
    private TextView tabMyClinic;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragmentsList;
    private MyProfileFragment myProfileFragment;
    private MyClinicFragment myClinicFragment;
    private boolean isClickedOnce;
    private User user;
    private DoctorProfile doctorProfile;
    private TextView tvName;
    private TextViewFontAwesome btEdit;
    private ImageView ivDoctorCoverPhoto;
    private ImageView ivImage;
    private FrameLayout containerTop;
    private TextView tvSpecialities;
    private TextView tvInitialAlphabet;
    private TextView tvExperience;
    private TextView tvGenderDate;
    private boolean receiversRegistered;
    private boolean isInitialLoading = true;
    public static final int TAG_RESULT_CODE = 111;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_doctor_profile, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Util.checkNetworkStatus(mActivity);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised
                (mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, DoctorProfileFragment.this,
                        DoctorProfileFragment.this, DoctorProfileFragment.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        tvName = (TextView) view.findViewById(R.id.tv_name);
        ivDoctorCoverPhoto = (ImageView) view.findViewById(R.id.iv_doctor_cover_photo);
        ivImage = (ImageView) view.findViewById(R.id.iv_image);
        btEdit = (TextViewFontAwesome) view.findViewById(R.id.bt_edit);
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabMyProfile = (TextView) view.findViewById(R.id.tab_my_profile);
        tabMyClinic = (TextView) view.findViewById(R.id.tab_my_clinic);
        containerTop = (FrameLayout) view.findViewById(R.id.container_top);
        tvExperience = (TextView) view.findViewById(R.id.tv_experience);
        tvGenderDate = (TextView) view.findViewById(R.id.tv_gender_date);
        tvSpecialities = (TextView) view.findViewById(R.id.tv_specialities);
        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);
    }

    @Override
    public void initListeners() {
        btEdit.setOnClickListener(this);
        tabMyProfile.setOnClickListener(this);
        tabMyClinic.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
        ivImage.setOnClickListener(this);
    }

    private void initViewPager() {
        fragmentsList = new ArrayList<>();
        myProfileFragment = new MyProfileFragment(this);
        myClinicFragment = new MyClinicFragment(this);
        fragmentsList.add(myProfileFragment);
        fragmentsList.add(myClinicFragment);

        viewPager.setOffscreenPageLimit(fragmentsList.size());
        ContactsDetailViewPagerAdapter viewPagerAdapter = new ContactsDetailViewPagerAdapter(
                mActivity.getSupportFragmentManager());
        viewPagerAdapter.setFragmentsList(fragmentsList);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tab_my_profile:
                viewPager.setCurrentItem(0);
                break;
            case R.id.tab_my_clinic:
                if (!isClickedOnce) {
//                    myClinicFragment.refreshMapLocation();
                    isClickedOnce = true;
                }
                viewPager.setCurrentItem(1);
                break;
            case R.id.bt_edit:
                openDialogFragment(user.getUniqueId(), FragmentType.PROFILE);
                break;
            case R.id.iv_image:
                if (doctorProfile != null && !Util.isNullOrBlank(doctorProfile.getImageUrl()))
                    mActivity.openEnlargedImageDialogFragment(doctorProfile.getImageUrl());
                break;
        }
    }

    protected void openDialogFragment(String uniqueId, FragmentType profile) {
        Bundle bundle = new Bundle();
        bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, uniqueId);
        AddEditDoctorProfileDialogFragment editDoctorProfileDetailsFragment = new AddEditDoctorProfileDialogFragment(profile);
        editDoctorProfileDetailsFragment.setArguments(bundle);
        editDoctorProfileDetailsFragment.setTargetFragment(this,TAG_RESULT_CODE);
        editDoctorProfileDetailsFragment.show(mFragmentManager, editDoctorProfileDetailsFragment.getClass().getSimpleName());
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setSelectedTab(position);
    }

    private void setSelectedTab(int position) {
        if (position == 0) {
            tabMyProfile.setSelected(true);
            tabMyClinic.setSelected(false);
        } else if (position == 1) {
            tabMyProfile.setSelected(false);
            tabMyClinic.setSelected(true);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void getDoctorProfileFromLocal() {
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_DOCTOR_PROFILE, DoctorProfileFragment.this, DoctorProfileFragment.this, DoctorProfileFragment.this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
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
                        initViewPager();
                        setSelectedTab(0);
                        getDoctorProfileFromLocal();
                        return;
                    }
                    break;
                case GET_DOCTOR_PROFILE:
                    if (response.getData() != null)
                        if (response.isDataFromLocal()) {
                            DoctorProfile doctorProfileResponse = (DoctorProfile) response.getData();
                            initData(doctorProfileResponse);
                            mActivity.refreshMenuFragment(doctorProfile);
                            if (isInitialLoading && response.isUserOnline()) {
                                isInitialLoading = false;
                                WebDataServiceImpl.getInstance(mApp).getDoctorProfile(DoctorProfile.class, user.getUniqueId(), null, null, this, this);
                                return;
                            }
                        } else {
                            isInitialLoading = false;
                            new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_DOCTOR_PROFILE, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void loadImages() {
        if (doctorProfile != null) {
            DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivImage, doctorProfile.getThumbnailUrl());

            DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivDoctorCoverPhoto, doctorProfile.getCoverImageUrl());
        }
    }

    public void initData(DoctorProfile doctorProfile) {
        this.doctorProfile = doctorProfile;
        if (doctorProfile != null) {
            String title = doctorProfile.getTitle(false);
            if (Util.isNullOrBlank(title))
                title = getResources().getString(R.string.dr);
            tvName.setText(title + Util.getValidatedValue(doctorProfile.getFirstName()));

            if (!Util.isNullOrEmptyList(doctorProfile.getSpecialities())) {
                tvSpecialities.setVisibility(View.VISIBLE);
                getMergedSpecialities(doctorProfile.getSpecialities());
            } else {
                tvSpecialities.setVisibility(View.GONE);
                tvSpecialities.setText("");
            }
            if (doctorProfile.getExperience() != null && doctorProfile.getExperience().getExperience() != null) {
                tvExperience.setText(doctorProfile.getFormattedExperience(mActivity, false));
                tvExperience.setVisibility(View.VISIBLE);
            } else {
                tvExperience.setVisibility(View.GONE);
                tvExperience.setText("");
            }
            String formattedGenderAge = Util.getFormattedGenderAge(doctorProfile);
            if (!Util.isNullOrBlank(formattedGenderAge)) {
                tvGenderDate.setVisibility(View.VISIBLE);
                tvGenderDate.setText(formattedGenderAge);
            } else {
                tvGenderDate.setVisibility(View.GONE);
                tvGenderDate.setText("");
            }
        }
        if (myProfileFragment != null)
            myProfileFragment.initData(doctorProfile);
        if (myClinicFragment != null)
            myClinicFragment.initData(doctorProfile);
        loadImages();
    }

    private void getMergedSpecialities(List<String> list) {
        String mergedText = "";
        for (String speciality :
                list) {
            int index = list.indexOf(speciality);
            if (list.size() > 1 && index != list.size() - 1)
                speciality = speciality + ",";
            mergedText = mergedText + speciality;
        }
        tvSpecialities.setText(mergedText);
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
                }
                return volleyResponseBean;
        }
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onPostExecute(String filePath) {
        if (!Util.isNullOrBlank(filePath)) {
            if (!Util.isNullOrBlank(doctorProfile.getProfileImagePath()) && doctorProfile.getProfileImagePath().equalsIgnoreCase(filePath)) {
                int width = ivImage.getLayoutParams().width;
                LogUtils.LOGD(TAG, "Image SIze " + width);
                Bitmap bitmap = ImageUtil.getDecodedBitmapFromPath(filePath, width, width);
                if (bitmap != null) {
                    ivImage.setImageBitmap(bitmap);
                    ivImage.setVisibility(View.VISIBLE);
                }
            } else if (!Util.isNullOrBlank(doctorProfile.getCoverImagePath()) && doctorProfile.getCoverImagePath().equalsIgnoreCase(filePath)) {
                int width = ScreenDimensions.SCREEN_WIDTH;
                int height = (int) (ScreenDimensions.SCREEN_HEIGHT * 0.25);
                LogUtils.LOGD(TAG, "Image SIze " + width);
                Bitmap bitmap = ImageUtil.getDecodedBitmapFromPath(filePath, width, height);
                if (bitmap != null) {
                    ivDoctorCoverPhoto.setImageBitmap(bitmap);
                    ivDoctorCoverPhoto.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for getting doctor details from server
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_DOCTOR_PROFILE_DETAILS);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshDoctorProfileDetailsReceiver, filter);

            //receiver for getting doctor details from local
            IntentFilter filter2 = new IntentFilter();
            filter2.addAction(INTENT_GET_DOCTOR_PROFILE_DETAILS_LOCAL);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshDoctorProfileDetailsLocalReceiver, filter2);
            receiversRegistered = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshDoctorProfileDetailsReceiver);
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshDoctorProfileDetailsLocalReceiver);
    }

    BroadcastReceiver refreshDoctorProfileDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
        }
    };
    BroadcastReceiver refreshDoctorProfileDetailsLocalReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getDoctorProfileFromLocal();
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TAG_RESULT_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                new SyncUtility(mApp, mActivity, user, null).updateMasterTableOnSpecialityChange();
                getDoctorProfileFromLocal();
            }
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        getDoctorProfileFromLocal();
    }
}
