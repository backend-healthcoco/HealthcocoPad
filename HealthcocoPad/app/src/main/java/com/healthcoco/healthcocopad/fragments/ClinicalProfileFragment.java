package com.healthcoco.healthcocopad.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.ClinicImageToSend;
import com.healthcoco.healthcocopad.bean.server.ClinicDetailResponse;
import com.healthcoco.healthcocopad.bean.server.ClinicImage;
import com.healthcoco.healthcocopad.bean.server.ClinicWorkingSchedule;
import com.healthcoco.healthcocopad.bean.server.FileDetails;
import com.healthcoco.healthcocopad.bean.server.Location;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Role;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.WorkingHours;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.custom.ScrollViewWithHeader;
import com.healthcoco.healthcocopad.dialogFragment.AddEditClinicAddressDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddEditClinicContactDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.AddEditClinicHoursDialogFragment;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.DialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.MapType;
import com.healthcoco.healthcocopad.enums.OptionsType;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.BitmapUtil;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ScreenDimensions;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class ClinicalProfileFragment extends HealthCocoFragment
        implements View.OnClickListener, Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, ViewPager.OnPageChangeListener, CommonOptionsDialogItemClickListener, OnMapReadyCallback {
    public static final String INTENT_GET_CLINIC_PROFILE_DETAILS = "com.healthcoco.REFRESH_CLINIC_PROFILE";
    public static final int MAX_DOCTORS_LIST_COUNT = 3;
    public static final int REQUEST_CODE_CLINIC_PROFILE_IMAGE = 105;
    private ScrollViewWithHeader svContainer;
    private TextView tvClinicname;
    private TextView tvHeader;
    private FrameLayout topLayout;
    private TextViewFontAwesome btEditClinicImage;
    private TextViewFontAwesome btEditClinicHours;
    private TextViewFontAwesome btEditClinicAddress;
    private TextViewFontAwesome btEditContact;
    private User user;
    private ClinicDetailResponse clinicDetailResponse;
    private TextView tvClinicAddress;
    private LinearLayout containerFromToTimeMon;
    private TextView tvnofromToTimeMon;
    private LinearLayout containerFromToTimeTue;
    private TextView tvnofromToTimeTue;
    private LinearLayout containerFromToTimeWed;
    private TextView tvnofromToTimeWed;
    private LinearLayout containerFromToTimeThu;
    private TextView tvnofromToTimeThu;
    private LinearLayout containerFromToTimeFri;
    private TextView tvnofromToTimeFri;
    private LinearLayout containerFromToTimeSat;
    private TextView tvnofromToTimeSat;
    private LinearLayout containerFromToTimeSun;
    private TextView tvnofromToTimeSun;
    private TextView tvClinicNumbers;
    private TextView tvWebsite;
    private TextView tvEmailAddress;
    private ViewPager viewPager;
    private LinearLayout containerBullets;
    private LinearLayout containerClinicOpen247;
    private LinearLayout containerClinicHours;
    private TextView tvLogoLabel;
    private Uri cameraImageUri;
    private ImageView ivLogoImage;
    private FrameLayout parentlogo;
    private ProgressBar progressLoadingLogo;
    private SupportMapFragment mapFragment;
    private GoogleMap googleMap;
    private View btEnlargedMap;
    private List<Role> rolesList;
    private boolean isEditEnabled;
    private boolean receiversRegistered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_clinical_profile, container, false);
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
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        initViews();
        initListeners();
        initScrollViewAndTopContainerHeight();
    }

    private void initMapFragment() {
        mapFragment = new SupportMapFragment();
        mFragmentManager.beginTransaction().add(R.id.container_map_fragment, mapFragment, mapFragment.getClass().getSimpleName()).commit();
    }

    private void getDataFromLocal() {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_LOCATION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void initViews() {
        svContainer = (ScrollViewWithHeader) view
                .findViewById(R.id.sv_container);
        tvHeader = (TextView) view.findViewById(R.id.tv_header);
        topLayout = (FrameLayout) view.findViewById(R.id.top_layout);

        tvClinicname = (TextView) view.findViewById(R.id.tv_clinic_name);
        tvClinicAddress = (TextView) view.findViewById(R.id.tv_address);
        containerBullets = (LinearLayout) view.findViewById(R.id.container_bullets);
        containerClinicOpen247 = (LinearLayout) view.findViewById(R.id.container_clinc_open_24_7);
        containerClinicHours = (LinearLayout) view.findViewById(R.id.container_clinic_hours);
        //clinic Hours view
        containerFromToTimeMon = (LinearLayout) view.findViewById(R.id.container_from_to_time_mon);
        tvnofromToTimeMon = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_mon);
        containerFromToTimeTue = (LinearLayout) view.findViewById(R.id.container_from_to_time_tue);
        tvnofromToTimeTue = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_tue);
        containerFromToTimeWed = (LinearLayout) view.findViewById(R.id.container_from_to_time_wed);
        tvnofromToTimeWed = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_wed);
        containerFromToTimeThu = (LinearLayout) view.findViewById(R.id.container_from_to_time_thu);
        tvnofromToTimeThu = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_thu);
        containerFromToTimeFri = (LinearLayout) view.findViewById(R.id.container_from_to_time_fri);
        tvnofromToTimeFri = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_fri);
        containerFromToTimeSat = (LinearLayout) view.findViewById(R.id.container_from_to_time_sat);
        tvnofromToTimeSat = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_sat);
        containerFromToTimeSun = (LinearLayout) view.findViewById(R.id.container_from_to_time_sun);
        tvnofromToTimeSun = (TextView) view.findViewById(R.id.tv_no_from_to_time_found_sun);


        //Clinic Contact views
        progressLoadingLogo = (ProgressBar) view.findViewById(R.id.progress_loading);
        tvClinicNumbers = (TextView) view.findViewById(R.id.tv_clinic_numbers);
        tvWebsite = (TextView) view.findViewById(R.id.tv_website);
        tvEmailAddress = (TextView) view.findViewById(R.id.tv_clinic_email_address);

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);

        btEditClinicImage = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_clinic_image);
        btEditClinicHours = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_clinic_hours);
        btEditClinicAddress = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_address);
        btEditContact = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_contact);

        //logo views
        tvLogoLabel = (TextView) view.findViewById(R.id.tv_logo_label);
        ivLogoImage = (ImageView) view.findViewById(R.id.iv_logo_image);
        parentlogo = (FrameLayout) view.findViewById(R.id.parent_logo);
        btEnlargedMap = (View) view.findViewById(R.id.bt_enlarged_map);
    }

    @Override
    public void initListeners() {
        btEditClinicImage.setOnClickListener(this);
        btEditClinicHours.setOnClickListener(this);
        btEditClinicAddress.setOnClickListener(this);
        btEditContact.setOnClickListener(this);
        viewPager.addOnPageChangeListener(this);
        parentlogo.setOnClickListener(this);
        btEnlargedMap.setOnClickListener(this);
    }

    private void initScrollViewAndTopContainerHeight() {
        //init ScrollView
        svContainer.addFixedHeader(tvHeader);
        svContainer.setHeaderVisiblilty(tvHeader.getId(), false);
        svContainer.addChildHeaders(tvClinicname);
        svContainer.setHeaderVisiblilty(tvClinicname.getId(), true);
        svContainer.build();

        //init top layout height
        ViewGroup.LayoutParams layoutParamsWidget = topLayout.getLayoutParams();
        layoutParamsWidget.height = (int) (ScreenDimensions.SCREEN_HEIGHT * 0.30);
        topLayout.setLayoutParams(layoutParamsWidget);
    }

    private void initData(ClinicDetailResponse clinicDetailResponse) {
        if (clinicDetailResponse != null) {
            this.clinicDetailResponse = clinicDetailResponse;
            Location location = clinicDetailResponse.getLocation();
            if (location != null) {
                tvClinicname.setText(Util.getValidatedValue(location.getLocationName()));
                tvClinicAddress.setText(location.getFormattedClinicAddress(mActivity));

                if (location.getTwentyFourSevenOpen() != null && location.getTwentyFourSevenOpen()) {
                    containerClinicHours.setVisibility(View.GONE);
                    containerClinicOpen247.setVisibility(View.VISIBLE);
                } else {
                    containerClinicHours.setVisibility(View.VISIBLE);
                    containerClinicOpen247.setVisibility(View.GONE);
                    if (!Util.isNullOrEmptyList(location.getClinicWorkingSchedules())) {
                        addWorkingSchedules(location.getClinicWorkingSchedules());
                    } else {
                        clearWorkingSchedules();
                    }
                }
                tvClinicNumbers.setText(getClinicNumbers(location));
                tvWebsite.setText(Util.getValidatedValueOrDash(mActivity, location.getWebsiteUrl()));

                tvEmailAddress.setText(Util.getValidatedValueOrDash(mActivity, location.getLocationEmailAddress()));

                DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivLogoImage, location.getLogoThumbnailUrl());

                if (googleMap == null)
                    mapFragment.getMapAsync(this);
            }
        }
    }

    private void clearWorkingSchedules() {
        containerFromToTimeMon.removeAllViews();
        containerFromToTimeTue.removeAllViews();
        containerFromToTimeWed.removeAllViews();
        containerFromToTimeThu.removeAllViews();
        containerFromToTimeFri.removeAllViews();
        containerFromToTimeSat.removeAllViews();
        containerFromToTimeSun.removeAllViews();

        containerFromToTimeMon.setVisibility(View.GONE);
        containerFromToTimeTue.setVisibility(View.GONE);
        containerFromToTimeWed.setVisibility(View.GONE);
        containerFromToTimeThu.setVisibility(View.GONE);
        containerFromToTimeFri.setVisibility(View.GONE);
        containerFromToTimeSat.setVisibility(View.GONE);
        containerFromToTimeSun.setVisibility(View.GONE);

        tvnofromToTimeMon.setVisibility(View.VISIBLE);
        tvnofromToTimeTue.setVisibility(View.VISIBLE);
        tvnofromToTimeWed.setVisibility(View.VISIBLE);
        tvnofromToTimeThu.setVisibility(View.VISIBLE);
        tvnofromToTimeFri.setVisibility(View.VISIBLE);
        tvnofromToTimeSat.setVisibility(View.VISIBLE);
        tvnofromToTimeSun.setVisibility(View.VISIBLE);
    }

    private void initViewPagerAdapter() {
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
        ContactsDetailViewPagerAdapter viewPagerAdapter = new ContactsDetailViewPagerAdapter(
                mActivity.getSupportFragmentManager());
        viewPagerAdapter.setFragmentsList(fragmentsList);
        viewPager.setAdapter(viewPagerAdapter);
    }

    private void getClinicDetails() {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getClinicDetails(ClinicDetailResponse.class, user.getForeignLocationId(), this, this);
    }

    private String getClinicNumbers(Location location) {
        String numbers = "";
        if (!Util.isNullOrBlank(location.getClinicNumber()))
            numbers = location.getClinicNumber();
        if (!Util.isNullOrEmptyList(location.getAlternateClinicNumbers())) {
            for (String alternateNumber :
                    location.getAlternateClinicNumbers()) {
                if (!Util.isNullOrBlank(alternateNumber)) {
                    numbers = addCharacterToText("\n", numbers);
                    numbers = numbers + alternateNumber;
                }
            }
        }
        if (!Util.isNullOrBlank(location.getLocationPhoneNumber())) {
            numbers = addCharacterToText("\n", numbers);
            numbers = numbers + location.getLocationPhoneNumber();
        }
        if (Util.isNullOrBlank(numbers))
            return getResources().getString(R.string.no_text_dash);
        return numbers;
    }

    private String addCharacterToText(String character, String numbers) {
        if (!Util.isNullOrBlank(numbers))
            numbers = numbers + character;
        return numbers;
    }

    private void addWorkingSchedules(List<ClinicWorkingSchedule> list) {
        for (ClinicWorkingSchedule schedule :
                list) {
            switch (schedule.getWorkingDay()) {
                case MONDAY:
                    addWorkingHoursFromToTime(containerFromToTimeMon, tvnofromToTimeMon, schedule.getWorkingHours());
                    break;
                case TUESDAY:
                    addWorkingHoursFromToTime(containerFromToTimeTue, tvnofromToTimeTue, schedule.getWorkingHours());
                    break;
                case WEDNESDAY:
                    addWorkingHoursFromToTime(containerFromToTimeWed, tvnofromToTimeWed, schedule.getWorkingHours());
                    break;
                case THURSDAY:
                    addWorkingHoursFromToTime(containerFromToTimeThu, tvnofromToTimeThu, schedule.getWorkingHours());
                    break;
                case FRIDAY:
                    addWorkingHoursFromToTime(containerFromToTimeFri, tvnofromToTimeFri, schedule.getWorkingHours());
                    break;
                case SATURDAY:
                    addWorkingHoursFromToTime(containerFromToTimeSat, tvnofromToTimeSat, schedule.getWorkingHours());
                    break;
                case SUNDAY:
                    addWorkingHoursFromToTime(containerFromToTimeSun, tvnofromToTimeSun, schedule.getWorkingHours());
                    break;
            }
        }
    }

    private void addWorkingHoursFromToTime(LinearLayout container, TextView noFromToTime, List<WorkingHours> list) {
        container.removeAllViews();
        if (!Util.isNullOrEmptyList(list)) {
            container.setVisibility(View.VISIBLE);
            noFromToTime.setVisibility(View.GONE);
            for (WorkingHours hours :
                    list) {
                if (hours.getFromTime() != null && hours.getToTime() != null) {
                    LinearLayout subItemFromToTime = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.sub_item_from_to_clinic_profile, null);
                    TextView tvTimefrom = (TextView) subItemFromToTime.findViewById(R.id.tv_time_from);
                    TextView tvTimeTo = (TextView) subItemFromToTime.findViewById(R.id.tv_time_to);
                    tvTimefrom.setText(DateTimeUtil.getFormattedTime(0, Math.round(hours.getFromTime())));
                    tvTimeTo.setText(DateTimeUtil.getFormattedTime(0, Math.round(hours.getToTime())));
                    container.addView(subItemFromToTime);
                }
            }
        } else {
            container.setVisibility(View.GONE);
            noFromToTime.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        Util.checkNetworkStatus(mActivity);
        if (!HealthCocoConstants.isNetworkOnline)
            onNetworkUnavailable(null);
        switch (v.getId()) {
            case R.id.bt_edit_clinic_image:
                if (clinicDetailResponse != null)
                    mActivity.openCommonOpenUpActivity(CommonOpenUpFragmentType.ADD_EDIT_CLINIC_IMAGE, HealthCocoConstants.TAG_UNIQUE_ID, clinicDetailResponse.getUniqueId(), 0);
                break;
            case R.id.bt_edit_clinic_hours:
                if (clinicDetailResponse != null) {
                    openDialogFragment(new AddEditClinicHoursDialogFragment(), AddEditClinicHoursDialogFragment.TAG_CLINIC_HOURS, clinicDetailResponse, REQUEST_CODE_CLINIC_PROFILE_IMAGE, CommonOpenUpFragmentType.ADD_EDIT_CLINIC_HOURS);
                }
                break;
            case R.id.bt_edit_address:
                if (clinicDetailResponse != null) {
                    openDialogFragment(new AddEditClinicAddressDialogFragment(), AddEditClinicAddressDialogFragment.TAG_CLINIC_ADDRESS, clinicDetailResponse, REQUEST_CODE_CLINIC_PROFILE_IMAGE, CommonOpenUpFragmentType.ADD_EDIT_CLINIC_ADDRESS);
                }
                break;
            case R.id.bt_edit_contact:
                if (clinicDetailResponse != null) {
                    openDialogFragment(new AddEditClinicContactDialogFragment(), AddEditClinicContactDialogFragment.TAG_CONTACT, clinicDetailResponse, REQUEST_CODE_CLINIC_PROFILE_IMAGE, CommonOpenUpFragmentType.ADD_EDIT_CLINIC_CONTACT);
                }
                break;
            case R.id.bt_add_session:
                LogUtils.LOGD(TAG, "AddSession Clicked");
                if (clinicDetailResponse != null) addSubItemSession();
                break;
            case R.id.parent_logo:
                LogUtils.LOGD(TAG, "logo Clicked");
                if (clinicDetailResponse != null) {
                    if (isEditEnabled) {
                        if (clinicDetailResponse.getLocation() != null
                                && !Util.isNullOrBlank(clinicDetailResponse.getLocation().getLogoThumbnailUrl()))
                            openDialogFragment(DialogType.SELECT_LOGO_IMAGE, this);
                        else
                            openDialogFragment(DialogType.SELECT_IMAGE, this);
                    } else onOptionsItemSelected(OptionsType.PREVIEW);
                }
                break;
            case R.id.bt_enlarged_map:
                if (clinicDetailResponse != null && !Util.isNullOrBlank(clinicDetailResponse.getUniqueId()))
                    openMapViewActivity(CommonOpenUpFragmentType.ENLARGED_MAP_VIEW_FRAGMENT, clinicDetailResponse.getUniqueId(), MapType.CLINIC_PROFILE, 0);
                    break;
        }
    }

    private void addSubItemSession() {

    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        mActivity.hideLoading();
        progressLoadingLogo.setVisibility(View.GONE);
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
        progressLoadingLogo.setVisibility(View.GONE);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        enableEditViews();
                        getDataFromLocal();
                        initMapFragment();
                        return;
                    }
                    break;
                case GET_CLINIC_PROFILE:
                    if (response.isDataFromLocal()) {
                        initData((ClinicDetailResponse) response.getData());
                        if (!response.isFromLocalAfterApiSuccess() && response.isUserOnline()) {
                            getClinicDetails();
                            return;
                        }
                    } else if (response.getData() != null) {
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_LOCATION, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                        response.setIsFromLocalAfterApiSuccess(true);
                        return;
                    }
                    if (!response.isUserOnline())
                        onNetworkUnavailable(response.getWebServiceType());
                    break;
                case ADD_CLINIC_LOGO:
                    Util.showToast(mActivity, R.string.clinic_logo_updated_successfully);
                    break;
            }
        }
        initViewPagerAdapter();
        mActivity.hideLoading();
        progressLoadingLogo.setVisibility(View.GONE);
    }

    private void enableEditViews() {
        ArrayList<RoleType> roleTypeList = new ArrayList<>();
        if (!Util.isNullOrEmptyList(rolesList)) {
            for (Role role :
                    rolesList) {
                roleTypeList.add(role.getRole());
            }
        }
        if (roleTypeList.contains(RoleType.SUPER_ADMIN)
                || roleTypeList.contains(RoleType.LOCATION_ADMIN)
                || roleTypeList.contains(RoleType.HOSPITAL_ADMIN)) {
            setEditViewsVisibility(View.VISIBLE);
            isEditEnabled = true;
        } else {
            setEditViewsVisibility(View.INVISIBLE);
            isEditEnabled = false;
        }
    }

    private void setEditViewsVisibility(int visibility) {
        btEditClinicImage.setVisibility(visibility);
        btEditClinicAddress.setVisibility(visibility);
        btEditClinicHours.setVisibility(visibility);
        btEditContact.setVisibility(visibility);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case ADD_LOCATION:
                LocalDataServiceImpl.getInstance(mApp).
                        addClinicDetailResponse((ClinicDetailResponse) response.getData());
            case GET_LOCATION:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getClinicDetailsResponse(WebServiceType.GET_CLINIC_PROFILE, user.getForeignLocationId(), null, null);
                break;
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                    rolesList = LocalDataServiceImpl.getInstance(mApp).getRoles(user.getForeignLocationId(), user.getForeignHospitalId());
                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == HealthCocoConstants.REQUEST_CODE_CAMERA && cameraImageUri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), cameraImageUri);
                    if (bitmap != null) {
                        addClinicLogo(cameraImageUri.getPath(), bitmap);
                    }
                } else if (requestCode == HealthCocoConstants.REQUEST_CODE_GALLERY && data.getData() != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), data.getData());
                    if (bitmap != null) {
                        addClinicLogo(data.getData().getPath(), bitmap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_CLINIC_PROFILE_IMAGE) {
            Location location = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_CLINIC_PROFILE));
            clinicDetailResponse.setLocation(location);
            switch (resultCode) {
                case HealthCocoConstants.RESULT_CODE_ADD_EDIT_CLINIC_ADDRESS:
                    initData(clinicDetailResponse);
                    break;
                case HealthCocoConstants.RESULT_CODE_ADD_EDIT_CLINIC_CONTACT:
                    initData(clinicDetailResponse);
                    break;
                case HealthCocoConstants.RESULT_CODE_ADD_EDIT_CLINIC_HOURS:
                    initData(clinicDetailResponse);
                    break;
            }
            initViewPagerAdapter();
        }
    }

    private void addClinicLogo(String filePath, Bitmap originalBitmap) {
        originalBitmap = ImageUtil.getRotatedBitmapIfRequiredFromPath(filePath, originalBitmap);
        //croping bitmap to show on image as per its dimensions
        Bitmap bitmap = BitmapUtil.scaleCenterCrop(originalBitmap, Util.getValidatedWidth(ivLogoImage.getWidth()), Util.getValidatedHeight(ivLogoImage.getHeight()));
        progressLoadingLogo.setVisibility(View.VISIBLE);
        if (bitmap != null) ivLogoImage.setImageBitmap(bitmap);
        ClinicImageToSend imageToSend = new ClinicImageToSend();
        imageToSend.setId(clinicDetailResponse.getUniqueId());
        //passing original bitmap to server
        imageToSend.setImage(getFileDetails(originalBitmap));
        WebDataServiceImpl.getInstance(mApp).addClinicLogo(ClinicImage.class, imageToSend, this, this);
    }

    private FileDetails getFileDetails(Bitmap bitmap) {
        FileDetails fileDetails = new FileDetails();
        fileDetails.setFileEncoded(ImageUtil.encodeTobase64(bitmap));
        fileDetails.setFileExtension(ImageUtil.DEFAULT_IMAGE_EXTENSION);
        fileDetails.setFileName(ImageUtil.DEFAULT_CLINIC_LOGO_NAME);
        fileDetails.setBitmap(bitmap);
        return fileDetails;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        setBulletSelected(position, containerBullets);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onOptionsItemSelected(Object object) {
        OptionsType optionsType = (OptionsType) object;
        if (optionsType != null) {
            LogUtils.LOGD(TAG, getResources().getString(optionsType.getStringId()));
            switch (optionsType) {
                case CAMERA:
                    cameraImageUri = mActivity.openCamera(this, "clinicLogoImage");
                    break;
                case GALLERY:
                    mActivity.openGallery(this);
                    break;
                case PREVIEW:
                    if (clinicDetailResponse != null && clinicDetailResponse.getLocation() != null
                            && !Util.isNullOrBlank(clinicDetailResponse.getLocation().getLogoUrl()))
                        mActivity.openEnlargedImageDialogFragment(clinicDetailResponse.getLocation().getLogoUrl());
                    break;
            }
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (clinicDetailResponse != null && clinicDetailResponse.getLocation() != null) {
            Location location = clinicDetailResponse.getLocation();
            if (googleMap != null && location.getLatitude() != null && location.getLongitude() != null) {
                // create marker
                MarkerOptions marker = new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(location.getLocationName());
                // adding marker
                googleMap.addMarker(marker);
                moveCamerToLatLong(googleMap, location.getLatitude(), location.getLongitude());
                googleMap.getUiSettings().setAllGesturesEnabled(false);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_GET_CLINIC_PROFILE_DETAILS);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshClinicProfileReceiver, filter);
            receiversRegistered = true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(refreshClinicProfileReceiver);
    }

    BroadcastReceiver refreshClinicProfileReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
            if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                user = doctor.getUser();
                getClinicDetails();
            }
        }
    };
}
