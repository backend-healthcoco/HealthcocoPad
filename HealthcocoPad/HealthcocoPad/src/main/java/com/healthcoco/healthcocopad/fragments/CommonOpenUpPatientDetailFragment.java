package com.healthcoco.healthcocopad.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
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

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.CommonViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.ActionbarType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientDetailTabType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonOpenUpPatientDetailListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

import static com.healthcoco.healthcocopad.R.id.container_right_action;
import static com.healthcoco.healthcocopad.R.id.fl_bt_add;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class CommonOpenUpPatientDetailFragment extends HealthCocoFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener,
        LocalDoInBackgroundListenerOptimised, CommonOpenUpPatientDetailListener {
    public static final String INTENT_REFRESH_PATIENT_PROFILE = "com.healthcoco.REFRESH_PATIENT_PROFILE";
    public static final String TAG_PATIENT_DETAIL_TAB_TYPE = "detailTabType";
    private TabHost tabhost;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragmentsList = new ArrayList<>();
    private CommonOpenUpFragmentType fragmentType;
    //    private HorizontalScrollView mHorizontalScroll;
    private FragmentTransaction transaction;
    private Toolbar toolbar;
    private TextView tvInitialAlphabet;
    private TextView tvPatientName;
    private TextView tvPatientId;
    private ImageView ivContactProfile;
    private LinearLayout patientProfileLayout;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    public FloatingActionButton floatingActionButton;
    private int tabOrdinal;

    //flags to refresh fragment when clicked for the first time
    private PatientProfileDetailFragment profileFragment;
    private PatientVisitDetailFragment visitsFragment;
    private PatientAppointmentDetailFragment appointmentFragment;
    private PatientClinicalNotesDetailFragment clinicalNotesDetailFragment;
    private PatientPrescriptionDetailFragment prescriptionDetailFragment;

    private boolean isProfileTabClicked = true;
    private boolean isVisitsTabClicked = false;
    private boolean isAppointmentTabClicked = false;
    private boolean isPrescriptionTabClicked = false;
    private boolean isClinicalNotesTabClicked = false;
    private TextView tvGenderDate;

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
    }

    @Override
    public void initViews() {
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabhost.setup();
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
//        mHorizontalScroll = (HorizontalScrollView) view.findViewById(R.id.h_Scroll_View);
        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);
        tvPatientName = (TextView) view.findViewById(R.id.tv_name);
        tvPatientId = (TextView) view.findViewById(R.id.tv_patient_id);
        tvGenderDate = (TextView) view.findViewById(R.id.tv_patient_gender);
        ivContactProfile = (ImageView) view.findViewById(R.id.iv_image);
        patientProfileLayout = (LinearLayout) view.findViewById(R.id.patient_profile_layout);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fl_bt_add);
        ((CommonOpenUpActivity) mActivity).showRightAction(false);
    }

    @Override
    public void initListeners() {
        mViewPager.addOnPageChangeListener(this);
        tabhost.setOnTabChangedListener(this);
        floatingActionButton.setOnClickListener(this);
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initTabs() {
        fragmentsList.clear();
        if (mViewPager.getAdapter() != null)
            mViewPager.getAdapter().notifyDataSetChanged();
        for (PatientDetailTabType detailTabType :
                PatientDetailTabType.values()) {
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
                case PATIENT_DETAIL_CLINICAL_NOTES:
                    clinicalNotesDetailFragment = new PatientClinicalNotesDetailFragment();
                    healthcocoFragment = clinicalNotesDetailFragment;
                    break;
//                case PATIENT_DETAIL_IMPORTANT:
//                    healthcocoFragment = new PatientImportantDetailFragment();
//                    break;
//                case PATIENT_DETAIL_REPORTS:
//                    healthcocoFragment = new PatientReportsDetailFragment();
//                    break;
                case PATIENT_DETAIL_PRESCRIPTION:
                    prescriptionDetailFragment = new PatientPrescriptionDetailFragment();
                    healthcocoFragment = prescriptionDetailFragment;
                    break;
                case PATIENT_DETAIL_APPOINTMENT:
                    appointmentFragment = new PatientAppointmentDetailFragment();
                    healthcocoFragment = appointmentFragment;
                    break;
//                case PATIENT_DETAIL_TREATMENT:
//                    healthcocoFragment = new PatientTreatmentDetailFragment();
//                    break;
            }
            if (healthcocoFragment != null)
                tabhost.addTab(getTabSpec(detailTabType, healthcocoFragment));
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

    private void openFragment(ActionbarType actionbarType, int actionBarTitle, ActionbarLeftRightActionTypeDrawables leftAction, ActionbarLeftRightActionTypeDrawables rightAction, HealthCocoFragment fragment) {
        Bundle bundle = new Bundle();
        initActionBar(actionbarType, actionBarTitle, leftAction, rightAction);
        bundle.putInt(HealthCocoConstants.TAG_FRAGMENT_NAME, fragmentType.ordinal());
        fragment.setArguments(bundle);
        transaction.add(R.id.layout_fragment_common_open_up, fragment, fragment.getClass().getSimpleName());
        transaction.commit();
    }

    public void initActionBar(ActionbarType actionbarType, int title, ActionbarLeftRightActionTypeDrawables leftAction, ActionbarLeftRightActionTypeDrawables rightAction) {
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
//        View tabView = tabhost.getTabWidget().getChildAt(position);
//        if (tabView != null) {
//            final int width = mHorizontalScroll.getWidth();
//            final int scrollPos = tabView.getLeft() - (width - tabView.getWidth()) / 2;
//            mHorizontalScroll.scrollTo(scrollPos, 0);
//            tabhost.refreshDrawableState();
//        } else {
//            mHorizontalScroll.scrollBy(positionOffsetPixels, 0);
//        }
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
        if (patientDetailTabType != null) {
            setPatientDetailHeaderVisibility(patientDetailTabType.getPatientDetailHeaderVisibility());
            setFloatingButtonVisibility(patientDetailTabType.getFloatingButtonVisibility());
            switch (patientDetailTabType) {
                case PATIENT_DETAIL_PROFILE:
                    if (!isProfileTabClicked) {
                        profileFragment.refreshData();
                        isProfileTabClicked = true;
                    }
                    break;
                case PATIENT_DETAIL_VISIT:
                    if (!isVisitsTabClicked) {
                        visitsFragment.refreshData(user, selectedPatient, PatientDetailTabType.PATIENT_DETAIL_VISIT);
                        isVisitsTabClicked = true;
                    }
                case PATIENT_DETAIL_APPOINTMENT:
                    if (!isAppointmentTabClicked) {
                        appointmentFragment.refreshData(user, selectedPatient);
                        isAppointmentTabClicked = true;
                    }
                    break;
                case PATIENT_DETAIL_CLINICAL_NOTES:
                    if (!isClinicalNotesTabClicked) {
                        clinicalNotesDetailFragment.refreshData(user, selectedPatient, PatientDetailTabType.PATIENT_DETAIL_CLINICAL_NOTES);
                        isClinicalNotesTabClicked = true;
                    }
                    break;
                case PATIENT_DETAIL_PRESCRIPTION:
                    if (!isPrescriptionTabClicked) {
                        prescriptionDetailFragment.refreshData(user, selectedPatient, PatientDetailTabType.PATIENT_DETAIL_PRESCRIPTION
                        );
                        isPrescriptionTabClicked = true;
                    }
                    break;
            }
        }
        ((CommonOpenUpActivity) mActivity).initActionbarTitle(patientDetailTabType.getActionBarTitleId());
    }

    private void initData() {
        if (selectedPatient != null) {
            ivContactProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Util.isNullOrBlank(selectedPatient.getImageUrl()))
                        mActivity.openEnlargedImageDialogFragment(selectedPatient.getImageUrl());
                }
            });
            tvPatientName.setText(selectedPatient.getLocalPatientName());
            tvPatientId.setText(selectedPatient.getPid());
            String formattedGenderAge = Util.getFormattedGenderAge(selectedPatient);
            if (!Util.isNullOrBlank(formattedGenderAge)) {
                tvGenderDate.setVisibility(View.VISIBLE);
                tvGenderDate.setText(formattedGenderAge);
            } else {
                tvGenderDate.setVisibility(View.GONE);
                tvGenderDate.setText("");
            }
            DownloadImageFromUrlUtil.loadImageWithInitialAlphabet(mActivity, PatientProfileScreenType.IN_PATIENT_DEATIL_SCREEN_EXCEPT_PROFILE, selectedPatient, null, ivContactProfile, tvInitialAlphabet);
        } else {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).getPatientProfile(RegisteredPatientDetailsUpdated.class, HealthCocoConstants.SELECTED_PATIENTS_USER_ID, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case container_right_action:
                openGlobalRecordAccessDialogFragment();
                break;
            case fl_bt_add:
                int ordinal = Integer.parseInt(tabhost.getCurrentTabTag());
                PatientDetailTabType patientDetailTabType = PatientDetailTabType.values()[ordinal];
                switch (patientDetailTabType) {
                    case PATIENT_DETAIL_VISIT:
                        visitsFragment.openAddVisitFragment();
                        break;
                    case PATIENT_DETAIL_APPOINTMENT:
                        appointmentFragment.openAddNewAppointmentScreen();
                        break;
                    case PATIENT_DETAIL_CLINICAL_NOTES:
                        clinicalNotesDetailFragment.openAddNewClinicalNotesScreen();
                        break;
                    case PATIENT_DETAIL_PRESCRIPTION:
//                        prescriptionDetailFragment.openAddNewClinicalNotesScreen();
                        break;
                }
                break;
        }
    }

    public void setPatientDetailHeaderVisibility(int visibility) {
        patientProfileLayout.setVisibility(visibility);
    }

    public void setFloatingButtonVisibility(int visibility) {
        floatingActionButton.setVisibility(visibility);
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
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId()) && selectedPatient != null && !Util.isNullOrBlank(selectedPatient.getUserId())) {
                    user = doctor.getUser();
                }
                break;
        }
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
                        initTabs();
                        initViewPagerAdapter();
                        initData();
                        checkPatientStatus(user, selectedPatient);
                    }
                    break;
                case GET_PATIENT_PROFILE:
                    if (response.getData() != null && response.getData() instanceof RegisteredPatientDetailsUpdated) {
                        selectedPatient = (RegisteredPatientDetailsUpdated) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addPatient(selectedPatient);
                        initData();
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
    }

    @Override
    public User getUser() {
        return user;
    }

    @Override
    public RegisteredPatientDetailsUpdated getSelectedPatientDetails() {
        return selectedPatient;
    }

}
