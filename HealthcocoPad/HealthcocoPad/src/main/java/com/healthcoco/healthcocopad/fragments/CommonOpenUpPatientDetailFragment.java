package com.healthcoco.healthcocopad.fragments;

import static com.healthcoco.healthcocopad.R.id.container_right_action;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.CommonViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AmountResponse;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredDoctorProfile;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AccountPackageType;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.ActionbarType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonOpenUpPatientDetailListener;
import com.healthcoco.healthcocopad.listeners.DoctorListPopupWindowListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.popupwindow.HealthcocoPopupWindow;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class CommonOpenUpPatientDetailFragment extends HealthCocoFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener,
        LocalDoInBackgroundListenerOptimised, CommonOpenUpPatientDetailListener, DoctorListPopupWindowListener {
    public static final String INTENT_REFRESH_PATIENT_PROFILE = "com.healthcoco.REFRESH_PATIENT_PROFILE";
    public static final String TAG_PATIENT_DETAIL_TAB_TYPE = "detailTabType";
    public static final String INTENT_REFRESH_AMOUNT_DETAILS = "com.healthcoco.REFRESH_AMOUNT_DETAILS";
    ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList = new ArrayList<>();
    private TabHost tabhost;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragmentsList = new ArrayList<>();
    private CommonOpenUpFragmentType fragmentType;
    //    private HorizontalScrollView mHorizontalScroll;
    private FragmentTransaction transaction;
    private Toolbar toolbar;
    private TextView tvInitialAlphabet;
    private TextView tvPatientName;
    private TextView tvDoctorName;
    private TextView tvPatientId;
    private TextView tvDueAmount;
    private ImageView ivContactProfile;
    private LinearLayout patientProfileLayout;
    private LinearLayout doctorNameLayout;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    BroadcastReceiver refreshAmountDetailsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            getPatientAmountDetails();
        }
    };
    private AmountResponse amountResponse;
    private String loginedUser;
    private List<RegisteredDoctorProfile> doctorProfileList;
    private LinkedHashMap<String, RegisteredDoctorProfile> clinicDoctorListHashMap = new LinkedHashMap<>();
    private int tabOrdinal;
    //    private TabLayout tabLayout;
    private DoctorProfile doctorProfile;
    //flags to refresh fragment when clicked for the first time
    private PatientProfileDetailFragment profileFragment;
    private PatientVisitDetailFragment visitsFragment;
    private PatientAppointmentDetailFragment appointmentFragment;
    private PatientClinicalNotesDetailFragment clinicalNotesDetailFragment;
    private PatientPrescriptionDetailFragment prescriptionDetailFragment;
    private PatientReportsDetailFragment reportsDetailFragment;
    private PatientTreatmentDetailFragment treatmentDetailFragment;
    private PatientInvoiceDetailFragment invoiceDetailFragment;
    private PatientReceiptDetailFragment receiptDetailFragment;
    private VaccinationListFragment vaccinationListFragment;
    private GrowthChartListFragment growthChartListFragment;
    private BabyAchievementsListFragment babyAchievementsListFragment;
    private HealthcocoPopupWindow doctorsListPopupWindow;
    private boolean isProfileTabClicked = true;
    private boolean isVisitsTabClicked = false;
    private boolean isAppointmentTabClicked = false;
    private boolean isPrescriptionTabClicked = false;
    private boolean isClinicalNotesTabClicked = false;
    private boolean isReportsTabClicked = false;
    private boolean isTreatmentTabClicked = false;
    private boolean isInvoiceTabClicked = false;
    private boolean isReceiptTabClicked = false;
    private boolean isVaccinationClickedOnce = false;
    private boolean isBabyAchievementsClickedOnce = false;
    private boolean isGrowthChartClickedOnce = false;

    private boolean isSingleDoctor = false;
    private TextView tvGenderDate;
    private int ordinal;
    private boolean isOTPVerified;
    private Boolean pidHasDate;
    private boolean receiversRegistered;
    private AccountPackageType packageType;
    private DoctorClinicProfile doctorClinicProfile;
    private LinearLayout middleAction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_common_open_up_patient_deatil, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        Intent intent = mActivity.getIntent();
        ordinal = intent.getIntExtra(HealthCocoConstants.TAG_TAB_TYPE, 0);
    }

    @Override
    public void initViews() {
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabhost.setup();
//        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
//        tabLayout.setupWithViewPager(mViewPager);
//        mHorizontalScroll = (HorizontalScrollView) view.findViewById(R.id.h_Scroll_View);
        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);
        tvPatientName = (TextView) view.findViewById(R.id.tv_name);
        tvPatientId = (TextView) view.findViewById(R.id.tv_patient_id);
        tvGenderDate = (TextView) view.findViewById(R.id.tv_patient_gender);
        tvDoctorName = (TextView) view.findViewById(R.id.tv_doctor_name);
        tvDueAmount = (TextView) view.findViewById(R.id.tv_due_amount);
        ivContactProfile = (ImageView) view.findViewById(R.id.iv_image);
        patientProfileLayout = (LinearLayout) view.findViewById(R.id.patient_profile_layout);
        doctorNameLayout = (LinearLayout) view.findViewById(R.id.layout_doctor_name);
        ((CommonOpenUpActivity) mActivity).showRightAction(false);
        middleAction = ((CommonOpenUpActivity) mActivity).showMiddleAction(false);
    }

    @Override
    public void initListeners() {
        mViewPager.addOnPageChangeListener(this);
        tabhost.setOnTabChangedListener(this);
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initTabs() {
        fragmentsList.clear();
        if (mViewPager.getAdapter() != null)
            mViewPager.getAdapter().notifyDataSetChanged();

        AccountPackageType packageType = doctorClinicProfile.getPackageType();
        ArrayList<PatientDetailTabType> tabItemList = null;
        if (doctorClinicProfile.isClinic()) {
            tabItemList = packageType.getTabItemList();
            if (!Util.isNullOrEmptyList(doctorProfile.getParentSpecialities())) {
//                if (doctorProfile.getParentSpecialities().contains("Dentist")) {
//                // For only Dentist
//                    if (tabItemList.contains(PatientDetailTabType.PATIENT_DETAIL_DENTAL_IMAGING_REPORT))
//                        tabItemList.remove(PatientDetailTabType.PATIENT_DETAIL_DENTAL_IMAGING_REPORT);
//                    if (packageType == AccountPackageType.ADVANCE || packageType == AccountPackageType.PRO)
//                        tabItemList.add(PatientDetailTabType.PATIENT_DETAIL_DENTAL_IMAGING_REPORT);
//                } else {
//                    tabItemList.remove(PatientDetailTabType.PATIENT_DETAIL_DENTAL_IMAGING_REPORT);
//                }

                // For only Peda
                if (doctorProfile.getParentSpecialities().contains("Pediatrician")) {
                    if (tabItemList.contains(PatientDetailTabType.PATIENT_DETAIL_VACCINATION))
                        tabItemList.remove(PatientDetailTabType.PATIENT_DETAIL_VACCINATION);
                    if (tabItemList.contains(PatientDetailTabType.PATIENT_DETAIL_GROWTH_CHART))
                        tabItemList.remove(PatientDetailTabType.PATIENT_DETAIL_GROWTH_CHART);
                    if (tabItemList.contains(PatientDetailTabType.PATIENT_DETAIL_BABY_ACHIEVEMENTS))
                        tabItemList.remove(PatientDetailTabType.PATIENT_DETAIL_BABY_ACHIEVEMENTS);
                    if (packageType == AccountPackageType.ADVANCE || packageType == AccountPackageType.PRO) {
                        tabItemList.add(PatientDetailTabType.PATIENT_DETAIL_VACCINATION);
                        tabItemList.add(PatientDetailTabType.PATIENT_DETAIL_GROWTH_CHART);
                        tabItemList.add(PatientDetailTabType.PATIENT_DETAIL_BABY_ACHIEVEMENTS);
                    }
                } else {
                    tabItemList.remove(PatientDetailTabType.PATIENT_DETAIL_VACCINATION);
                    tabItemList.remove(PatientDetailTabType.PATIENT_DETAIL_GROWTH_CHART);
                    tabItemList.remove(PatientDetailTabType.PATIENT_DETAIL_BABY_ACHIEVEMENTS);
                }
            }
        }
        ArrayList<PatientDetailTabType> newList = new ArrayList<>();
        if (user != null && user.getUiPermissions() != null && user.getUiPermissions() != null) {
            for (String permissionTypeString :
                    user.getUiPermissions().getTabPermissions()) {
                PatientDetailTabType commonOpenDetailType = PatientDetailTabType.getPatientDetailTabType(permissionTypeString);
                newList.add(commonOpenDetailType);
                if (newList.contains(PatientDetailTabType.PATIENT_DETAIL_INVOICE))
                    newList.add(PatientDetailTabType.PATIENT_DETAIL_RECEIPT);
                else if (newList.contains(PatientDetailTabType.PATIENT_DETAIL_RECEIPT))
                    newList.add(PatientDetailTabType.PATIENT_DETAIL_INVOICE);

                if (newList.contains(PatientDetailTabType.PATIENT_DETAIL_VACCINATION)) {
                    newList.add(PatientDetailTabType.PATIENT_DETAIL_GROWTH_CHART);
                    newList.add(PatientDetailTabType.PATIENT_DETAIL_BABY_ACHIEVEMENTS);
                }
            }
        }
        if (!Util.isNullOrEmptyList(tabItemList)) {
            Iterator<PatientDetailTabType> i = tabItemList.iterator();
            while (i.hasNext()) {
                PatientDetailTabType openDetailType = i.next();
                if (newList.contains(openDetailType)) {
                    switch (openDetailType) {
                        case PATIENT_DETAIL_REPORTS:
                        case PATIENT_DETAIL_CLINICAL_NOTES:
                        case PATIENT_DETAIL_PRESCRIPTION:
                        case PATIENT_DETAIL_RECEIPT:
                        case PATIENT_DETAIL_TREATMENT:
                        case PATIENT_DETAIL_INVOICE:
                        case PATIENT_DETAIL_VISIT:
                        case PATIENT_DETAIL_PROFILE:
                        case PATIENT_DETAIL_APPOINTMENT:
                        case PATIENT_DETAIL_VACCINATION:
                        case PATIENT_DETAIL_GROWTH_CHART:
                        case PATIENT_DETAIL_BABY_ACHIEVEMENTS:
                            break;
//                        case PATIENT_DETAIL_GROWTH_CHART:
//                        case PATIENT_DETAIL_BABY_ACHIEVEMENTS:
//                            break;
                    }
                } else {
                    switch (openDetailType) {
//                        case PATIENT_DETAIL_GROWTH_CHART:
//                        case PATIENT_DETAIL_BABY_ACHIEVEMENTS:
//                            break;
                    }
                }
            }
        }
        if (packageType != null) {
            for (PatientDetailTabType detailTabType :
                    packageType.getTabItemList()) {
                HealthCocoFragment healthcocoFragment = null;
                switch (detailTabType) {
                    case PATIENT_DETAIL_PROFILE:
                        profileFragment = new PatientProfileDetailFragment();
                        healthcocoFragment = profileFragment;
                        break;
                    case PATIENT_DETAIL_VISIT:
                        visitsFragment = new PatientVisitDetailFragment();
                        healthcocoFragment = visitsFragment;
                        break;
                    case PATIENT_DETAIL_APPOINTMENT:
                        appointmentFragment = new PatientAppointmentDetailFragment();
                        healthcocoFragment = appointmentFragment;
                        break;
                    case PATIENT_DETAIL_CLINICAL_NOTES:
                        clinicalNotesDetailFragment = new PatientClinicalNotesDetailFragment();
                        healthcocoFragment = clinicalNotesDetailFragment;
                        break;
                    case PATIENT_DETAIL_PRESCRIPTION:
                        prescriptionDetailFragment = new PatientPrescriptionDetailFragment();
                        healthcocoFragment = prescriptionDetailFragment;
                        break;
                    case PATIENT_DETAIL_REPORTS:
                        reportsDetailFragment = new PatientReportsDetailFragment();
                        healthcocoFragment = reportsDetailFragment;
                        break;
                    case PATIENT_DETAIL_TREATMENT:
                        treatmentDetailFragment = new PatientTreatmentDetailFragment();
                        healthcocoFragment = treatmentDetailFragment;
                        break;
                    case PATIENT_DETAIL_INVOICE:
                        invoiceDetailFragment = new PatientInvoiceDetailFragment();
                        healthcocoFragment = invoiceDetailFragment;
                        break;
                    case PATIENT_DETAIL_RECEIPT:
                        receiptDetailFragment = new PatientReceiptDetailFragment();
                        healthcocoFragment = receiptDetailFragment;
                        break;
                    case PATIENT_DETAIL_VACCINATION:
                        vaccinationListFragment = new VaccinationListFragment();
                        healthcocoFragment = vaccinationListFragment;
                        break;
                    case PATIENT_DETAIL_GROWTH_CHART:
                        growthChartListFragment = new GrowthChartListFragment();
                        healthcocoFragment = growthChartListFragment;
                        break;
                    case PATIENT_DETAIL_BABY_ACHIEVEMENTS:
                        babyAchievementsListFragment = new BabyAchievementsListFragment();
                        healthcocoFragment = babyAchievementsListFragment;
                        break;
                }
                if (healthcocoFragment != null)

//                if (user.getUiPermissions().getTabPermissions().contains(detailTabType) || (detailTabType == PatientDetailTabType.PATIENT_DETAIL_VISIT))
                    tabhost.addTab(getTabSpec(detailTabType, healthcocoFragment));
            }
        }
    }

    private TabHost.TabSpec getTabSpec(PatientDetailTabType detailTabType, Fragment fragment) {
        fragmentsList.add(fragment);
        View view1 = mActivity.getLayoutInflater().inflate(R.layout.tab_indicator_home, null);
        ImageView ivIcon = (ImageView) view1.findViewById(R.id.iv_image);
        TextView tvText = (TextView) view1.findViewById(R.id.tv_text);
        ivIcon.setImageResource(detailTabType.getDrawableId());
        tvText.setText(detailTabType.getTextId());
        return tabhost.newTabSpec(String.valueOf(detailTabType.ordinal())).setIndicator(view1).setContent(new DummyTabFactory(mActivity));
    }

    private void initViewPagerAdapter() {
        mViewPager.setOffscreenPageLimit(fragmentsList.size());
        CommonViewPagerAdapter viewPagerAdapter = new CommonViewPagerAdapter(
                mActivity.getSupportFragmentManager());
        viewPagerAdapter.setFragmentsList(fragmentsList);
        mViewPager.setAdapter(viewPagerAdapter);
    }

    private void openFragment(ActionbarType actionbarType,
                              int actionBarTitle, ActionbarLeftRightActionTypeDrawables
                                      leftAction, ActionbarLeftRightActionTypeDrawables rightAction, HealthCocoFragment fragment) {
        Bundle bundle = new Bundle();
        initActionBar(actionbarType, actionBarTitle, leftAction, rightAction);
        bundle.putInt(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        fragment.setArguments(bundle);
        transaction.add(R.id.layout_fragment_common_open_up, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void initActionBar(ActionbarType actionbarType,
                              int title, ActionbarLeftRightActionTypeDrawables
                                      leftAction, ActionbarLeftRightActionTypeDrawables rightAction) {
        if (actionbarType == ActionbarType.HIDDEN) {
            hideActionBar();
        } else {
            View actionbar = mActivity.getLayoutInflater().inflate(actionbarType.getActionBarLayoutId(), null);
            actionbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            toolbar.setContentInsetsAbsolute(0, 0);
            toolbar.addView(actionbar);
            LinearLayout containerLeftAction = (LinearLayout) actionbar.findViewById(R.id.container_left_action);
            if (leftAction != null && leftAction != ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION) {
                containerLeftAction.setVisibility(View.VISIBLE);
                View leftView = mActivity.getLayoutInflater().inflate(leftAction.getLayoutId(), null);
                if (leftView != null) {
                    if (leftAction.isDrawableBackground()) {
                        ImageButton imageButton = (ImageButton) leftView;
                        imageButton.setImageResource(leftAction.getDrawableTitleId());
                    } else {
                        TextView button = (TextView) leftView;
                        button.setText(leftAction.getDrawableTitleId());
                    }
                    containerLeftAction.addView(leftView);
                }
                containerLeftAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mActivity.finish();
                    }
                });
            } else
                containerLeftAction.setVisibility(View.GONE);

            LinearLayout containerRightAction = (LinearLayout) actionbar.findViewById(container_right_action);
            if (rightAction != null && rightAction != ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION) {
                containerRightAction.setVisibility(View.VISIBLE);
                View rightView = mActivity.getLayoutInflater().inflate(rightAction.getLayoutId(), null);
                if (rightView != null) {
                    if (rightAction.isDrawableBackground()) {
                        ImageButton imageButton = (ImageButton) rightView;
                        imageButton.setImageResource(rightAction.getDrawableTitleId());
                    } else {
                        TextView button = (TextView) rightView;
                        button.setText(rightAction.getDrawableTitleId());
                    }
                    containerRightAction.addView(rightView);
                }
            } else {
                containerRightAction.setVisibility(View.GONE);
            }
            if (title > 0) {
                TextView tvTitle = (TextView) toolbar.findViewById(R.id.tv_title);
                tvTitle.setText(title);
            }
        }
        setupUI(toolbar);
    }

    public void setupUI(final View view) {
        try {
            //Set up touch listener for non-text box views to hide keyboard.
            if (!(view instanceof EditText)) {
                view.setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(View v, MotionEvent event) {
                        Util.hideKeyboard(getContext(), view);
                        return false;
                    }
                });
            }
            //If a layout container, iterate over children and seed recursion.
            if (view instanceof ViewGroup) {
                for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                    View innerView = ((ViewGroup) view).getChildAt(i);
                    setupUI(innerView);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideActionBar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        View tabView = tabhost.getTabWidget().getChildAt(position);
        /*if (tabView != null) {
            final int width = mHorizontalScroll.getWidth();
            final int scrollPos = tabView.getLeft() - (width - tabView.getWidth()) / 2;
            mHorizontalScroll.scrollTo(scrollPos, 0);
            tabhost.refreshDrawableState();
        } else {
            mHorizontalScroll.scrollBy(positionOffsetPixels, 0);
        }*/
    }

    @Override
    public void onPageSelected(int position) {

        tabhost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabChanged(String tabId) {
        mViewPager.setCurrentItem(tabhost.getCurrentTab());
        LogUtils.LOGD(TAG, tabhost.getCurrentTabTag());
        int ordinal = Integer.parseInt(tabhost.getCurrentTabTag());
        PatientDetailTabType patientDetailTabType = PatientDetailTabType.values()[ordinal];
        if (!isSingleDoctor)
            doctorNameLayout.setVisibility(View.VISIBLE);
        else
            doctorNameLayout.setVisibility(View.INVISIBLE);
        tvDueAmount.setVisibility(View.GONE);
        if (patientDetailTabType != null) {
            setPatientDetailHeaderVisibility(patientDetailTabType.getPatientDetailHeaderVisibility());
            switch (patientDetailTabType) {
                case PATIENT_DETAIL_PROFILE:
                    ((CommonOpenUpActivity) mActivity).showMiddleAction(false);
                    if (!isProfileTabClicked) {
                        profileFragment.refreshData();
                        isProfileTabClicked = true;
                    }
                    break;
                case PATIENT_DETAIL_VISIT:
                    ((CommonOpenUpActivity) mActivity).showMiddleAction(false);
                    if (!isVisitsTabClicked) {
                        visitsFragment.refreshData(PatientDetailTabType.PATIENT_DETAIL_VISIT, clinicDoctorProfileList);
                        isVisitsTabClicked = true;
                    }
                    break;
                case PATIENT_DETAIL_APPOINTMENT:
                    ((CommonOpenUpActivity) mActivity).showMiddleAction(false);
                    doctorNameLayout.setVisibility(View.INVISIBLE);
                    if (!isAppointmentTabClicked) {
                        appointmentFragment.refreshData();
                        isAppointmentTabClicked = true;
                    }
                    break;
                case PATIENT_DETAIL_CLINICAL_NOTES:
                    ((CommonOpenUpActivity) mActivity).showMiddleAction(false);
                    if (!isClinicalNotesTabClicked) {
                        clinicalNotesDetailFragment.refreshData(PatientDetailTabType.PATIENT_DETAIL_CLINICAL_NOTES, clinicDoctorProfileList);
                        isClinicalNotesTabClicked = true;
                    }
                    break;
                case PATIENT_DETAIL_PRESCRIPTION:
                    ((CommonOpenUpActivity) mActivity).showMiddleAction(false);
                    if (!isPrescriptionTabClicked) {
                        prescriptionDetailFragment.refreshData(PatientDetailTabType.PATIENT_DETAIL_PRESCRIPTION, clinicDoctorProfileList);
                        isPrescriptionTabClicked = true;
                    }
                    break;
                case PATIENT_DETAIL_REPORTS:
                    ((CommonOpenUpActivity) mActivity).showMiddleAction(false);
                    doctorNameLayout.setVisibility(View.INVISIBLE);
                    if (!isReportsTabClicked) {
                        reportsDetailFragment.refreshData(PatientDetailTabType.PATIENT_DETAIL_REPORTS, clinicDoctorProfileList);
                        isReportsTabClicked = true;
                    }
                    break;
                case PATIENT_DETAIL_TREATMENT:
                    ((CommonOpenUpActivity) mActivity).showMiddleAction(false);
                    if (!isTreatmentTabClicked) {
                        treatmentDetailFragment.refreshData(PatientDetailTabType.PATIENT_DETAIL_TREATMENT, clinicDoctorProfileList);
                        isTreatmentTabClicked = true;
                    }
                    break;
                case PATIENT_DETAIL_INVOICE:
                    ((CommonOpenUpActivity) mActivity).showMiddleAction(false);
                    tvDueAmount.setVisibility(View.VISIBLE);
                    doctorNameLayout.setVisibility(View.INVISIBLE);
                    if (!isInvoiceTabClicked) {
                        invoiceDetailFragment.refreshData(PatientDetailTabType.PATIENT_DETAIL_INVOICE);
                        isInvoiceTabClicked = true;
                    }
                    break;
                case PATIENT_DETAIL_RECEIPT:
                    ((CommonOpenUpActivity) mActivity).showMiddleAction(false);
                    tvDueAmount.setVisibility(View.VISIBLE);
                    doctorNameLayout.setVisibility(View.INVISIBLE);
                    if (!isReceiptTabClicked) {
                        receiptDetailFragment.refreshData(PatientDetailTabType.PATIENT_DETAIL_RECEIPT);
                        isReceiptTabClicked = true;
                    }
                    break;
                case PATIENT_DETAIL_VACCINATION:
                    ((CommonOpenUpActivity) mActivity).showMiddleAction(true);
                    doctorNameLayout.setVisibility(View.INVISIBLE);
                    if (!isVaccinationClickedOnce) {
                        vaccinationListFragment.getListFromLocal(true);
                        isVaccinationClickedOnce = true;
                    }
                    break;
                case PATIENT_DETAIL_GROWTH_CHART:
                    ((CommonOpenUpActivity) mActivity).showMiddleAction(false);
                    doctorNameLayout.setVisibility(View.INVISIBLE);
                    if (!isGrowthChartClickedOnce) {
                        growthChartListFragment.getListFromLocal(true);
                        isGrowthChartClickedOnce = true;
                    }
                    break;
                case PATIENT_DETAIL_BABY_ACHIEVEMENTS:
                    ((CommonOpenUpActivity) mActivity).showMiddleAction(false);
                    doctorNameLayout.setVisibility(View.INVISIBLE);
                    if (!isBabyAchievementsClickedOnce) {
                        babyAchievementsListFragment.getListFromLocal(true);
                        isBabyAchievementsClickedOnce = true;
                    }
                    break;
            }
        }
        ((CommonOpenUpActivity) mActivity).initActionbarTitle(patientDetailTabType.getActionBarTitleId());
    }

    private void initData() {
        if (selectedPatient != null) {
            refreshHeaderData(selectedPatient);
            if (visitsFragment != null)
                visitsFragment.setUserData(user, loginedUser, selectedPatient);
            if (appointmentFragment != null)
                appointmentFragment.setUserData(user, selectedPatient);
            if (clinicalNotesDetailFragment != null)
                clinicalNotesDetailFragment.setUserData(user, loginedUser, selectedPatient);
            if (prescriptionDetailFragment != null)
                prescriptionDetailFragment.setUserData(user, loginedUser, selectedPatient);
            if (reportsDetailFragment != null)
                reportsDetailFragment.setUserData(user, selectedPatient);
            if (treatmentDetailFragment != null)
                treatmentDetailFragment.setUserData(user, loginedUser, selectedPatient);
            if (invoiceDetailFragment != null)
                invoiceDetailFragment.setUserData(user, loginedUser, selectedPatient);
            if (receiptDetailFragment != null)
                receiptDetailFragment.setUserData(user, loginedUser, selectedPatient);
            if (vaccinationListFragment != null) {
                vaccinationListFragment.setUserData(user, selectedPatient);
                vaccinationListFragment.initBottomPopupSheet(middleAction);
            }
            if (growthChartListFragment != null)
                growthChartListFragment.setUserData(user, selectedPatient);
            if (babyAchievementsListFragment != null)
                babyAchievementsListFragment.setUserData(user, selectedPatient);
        } else {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).getPatientProfile(RegisteredPatientDetailsUpdated.class, HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
        }
    }

    protected void refreshHeaderData(final RegisteredPatientDetailsUpdated selectedPatient) {
        ivContactProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Util.isNullOrBlank(selectedPatient.getImageUrl()))
                    mActivity.openEnlargedImageDialogFragment(selectedPatient.getImageUrl());
            }
        });
        tvPatientName.setText(selectedPatient.getLocalPatientName());
        if (pidHasDate != null) {
            if (!pidHasDate && (!Util.isNullOrBlank(selectedPatient.getPnum())))
                tvPatientId.setText(Util.getValidatedValue(selectedPatient.getPnum()));
            else
                tvPatientId.setText(Util.getValidatedValue(selectedPatient.getPid()));
        } else
            tvPatientId.setText(Util.getValidatedValue(selectedPatient.getPid()));
        String formattedGenderAge = Util.getFormattedGenderAge(selectedPatient);
        if (!Util.isNullOrBlank(formattedGenderAge)) {
            tvGenderDate.setVisibility(View.VISIBLE);
            tvGenderDate.setText(formattedGenderAge);
        } else {
            tvGenderDate.setVisibility(View.GONE);
            tvGenderDate.setText("");
        }
        DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENT_DEATIL_PROFILE, selectedPatient, null, ivContactProfile, tvInitialAlphabet);
        refreshHeaderDueAmountData(selectedPatient);
    }

    private void refreshHeaderDueAmountData(RegisteredPatientDetailsUpdated selectedPatient) {
        if (selectedPatient.getTotalDueAmount() < 0) {
            tvDueAmount.setText(String.valueOf(mActivity.getResources().getString(R.string.advance_amount)) + "\u20B9 " + Util.getFormattedDoubleNumber(selectedPatient.getTotalDueAmount()));
            tvDueAmount.setTextColor(mActivity.getResources().getColor(R.color.green_logo));
        } else if (selectedPatient.getTotalDueAmount() > 0) {
            tvDueAmount.setText(String.valueOf(mActivity.getResources().getString(R.string.due_amount)) + "\u20B9 " + Util.getFormattedDoubleNumber(selectedPatient.getTotalDueAmount()));
            tvDueAmount.setTextColor(mActivity.getResources().getColor(R.color.red_error));
        } else {
            tvDueAmount.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case container_right_action:
                openGlobalRecordAccessDialogFragment();
                break;
        }
    }

    public void setPatientDetailHeaderVisibility(int visibility) {
        patientProfileLayout.setVisibility(visibility);
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId()))
                    user = doctor.getUser();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    loginedUser = doctor.getUser().getUniqueId();
                    doctorProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileObject(user.getUniqueId());
                    doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
                    doctorProfileList = LocalDataServiceImpl.getInstance(mApp).getRegisterDoctorDetails(user.getForeignLocationId());
                }
                DoctorClinicProfile doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
                if (doctorClinicProfile != null && doctorClinicProfile.getPidHasDate() != null)
                    pidHasDate = doctorClinicProfile.getPidHasDate();

                if (doctorClinicProfile.getPackageType() != null)
                    packageType = doctorClinicProfile.getPackageType();

                break;
            case ADD_REGISTER_DOCTOR:
                LocalDataServiceImpl.getInstance(mApp).addRegisterDoctorResponse((ArrayList<RegisteredDoctorProfile>) (ArrayList<?>) response.getDataList(), user.getForeignLocationId());
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
    public void onResponse(VolleyResponseBean response) {
        super.onResponse(response);
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (selectedPatient != null) {
                        checkPatientStatus(user, selectedPatient);
                        if (!Util.isNullOrEmptyList(doctorProfileList))
                            formHashMapAndRefresh(doctorProfileList);
                        else
                            refreshDoctorsList();
                        initTabs();
                        initViewPagerAdapter();
                        initData();
                        refreshDoctorClinicText();
                        getPatientAmountDetails();
                        Long latestUpdatedTime = LocalDataServiceImpl.getInstance(mApp).getLatestUpdatedTime(user, LocalTabelType.VISIT_DETAILS);
                        if (latestUpdatedTime < 1)
                            if (visitsFragment != null)
                                visitsFragment.refreshData(PatientDetailTabType.PATIENT_DETAIL_VISIT, clinicDoctorProfileList);
                        if (ordinal != 0) {
                            PatientDetailTabType patientDetailTabType = PatientDetailTabType.values()[ordinal];
                            mViewPager.setCurrentItem(packageType.getTabItemList().indexOf(patientDetailTabType));
                        }
                    } else initData();
                    break;
                case GET_PATIENT_PROFILE:
                    if (response.getData() != null && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                        selectedPatient = (RegisteredPatientDetailsUpdated) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addPatient(selectedPatient);
                        if (selectedPatient != null) {
                            initTabs();
                            initViewPagerAdapter();
                            initData();
                            if (ordinal != 0) {
                                PatientDetailTabType patientDetailTabType = PatientDetailTabType.values()[ordinal];
                                mViewPager.setCurrentItem(packageType.getTabItemList().indexOf(patientDetailTabType));
                            }
                        }
                    }
                    break;
                case GET_REGISTER_DOCTOR:
                    if (!Util.isNullOrEmptyList(response.getDataList())) {
                        doctorProfileList = (ArrayList<RegisteredDoctorProfile>) (ArrayList) response.getDataList();
                        formHashMapAndRefresh(doctorProfileList);
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_REGISTER_DOCTOR, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);

                    }
                    if (!response.isUserOnline())
                        onNetworkUnavailable(response.getWebServiceType());
                    break;
                case GET_AMOUNT:
                    if (response.getData() != null && response.getData() instanceof AmountResponse) {
                        amountResponse = (AmountResponse) response.getData();
                        selectedPatient.setTotalDueAmount(amountResponse.getTotalDueAmount());
                        selectedPatient.setTotalRemainingAdvanceAmount(amountResponse.getTotalRemainingAdvanceAmount());
                        LocalDataServiceImpl.getInstance(mApp).addPatient(selectedPatient);
                        selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                        refreshHeaderDueAmountData(selectedPatient);
                    }
                    break;
            }
        }
        mActivity.hideLoading();
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

    private void getPatientAmountDetails() {
        Util.checkNetworkStatus(mActivity);
        if (HealthCocoConstants.isNetworkOnline) {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).getPatientAmountDetails(AmountResponse.class, WebServiceType.GET_AMOUNT, user.getForeignLocationId(), user.getForeignHospitalId(), selectedPatient.getUserId(), this, this);
        } else
            Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public RegisteredPatientDetailsUpdated getSelectedPatientDetails() {
        return selectedPatient;
    }

    public boolean isOtpVerified() {
        return isOTPVerified;
    }

    public Boolean isPidHasDate() {
        return pidHasDate;
    }

    private void refreshDoctorsList() {
        showLoadingOverlay(true);
        WebDataServiceImpl.getInstance(mApp).getRegisterDoctor(RegisteredDoctorProfile.class, user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
    }

    private void formHashMapAndRefresh(List<RegisteredDoctorProfile> responseList) {
        if (responseList.size() > 1) {
            if (!Util.isNullOrEmptyList(responseList)) {
                for (RegisteredDoctorProfile clinicDoctorProfile :
                        responseList) {
                    clinicDoctorListHashMap.put(clinicDoctorProfile.getUserId(), clinicDoctorProfile);
                }
            }
//        notifyAdapter(new ArrayList<ClinicDoctorProfile>(clinicDoctorListHashMap.values()));
            if (doctorsListPopupWindow != null)
                doctorsListPopupWindow.notifyAdapter(new ArrayList<Object>(clinicDoctorListHashMap.values()));
            else
                mActivity.initDoctorListPopupWindows(tvDoctorName, PopupWindowType.DOCTOR_LIST, new ArrayList<Object>(clinicDoctorListHashMap.values()), this);
        } else {
            doctorNameLayout.setVisibility(View.INVISIBLE);
            isSingleDoctor = true;
        }
    }


    private void resetAllFlags() {
        isProfileTabClicked = false;
        isVisitsTabClicked = false;
        isAppointmentTabClicked = false;
        isPrescriptionTabClicked = false;
        isClinicalNotesTabClicked = false;
        isReportsTabClicked = false;
        isTreatmentTabClicked = false;
    }

    @Override
    public void onDoctorSelected(ArrayList<RegisteredDoctorProfile> clinicDoctorProfileList) {
        this.clinicDoctorProfileList = clinicDoctorProfileList;
        String doctorName = "";
        if (!Util.isNullOrEmptyList(clinicDoctorProfileList)) {
            if (clinicDoctorProfileList.size() == clinicDoctorListHashMap.size())
                tvDoctorName.setText(R.string.all_doctor);
            else {
                for (RegisteredDoctorProfile clinicDoctorProfile : clinicDoctorProfileList) {
                    doctorName = doctorName + clinicDoctorProfile.getFirstNameWithTitle() + ", ";
                }
                doctorName = doctorName.substring(0, doctorName.length() - 2);
                tvDoctorName.setText(doctorName);
            }
        }
//        user.setUniqueId(doctorProfile.getUniqueId());
        resetAllFlags();
        onTabChanged(null);
    }

    @Override
    public void onEmptyListFound() {

    }

    private void refreshDoctorClinicText() {
        tvDoctorName.setText(R.string.all_doctor);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for prescription list refresh
            IntentFilter filter = new IntentFilter();
            filter.addAction(INTENT_REFRESH_AMOUNT_DETAILS);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(refreshAmountDetailsReceiver, filter);
            receiversRegistered = true;
        }
    }
}
