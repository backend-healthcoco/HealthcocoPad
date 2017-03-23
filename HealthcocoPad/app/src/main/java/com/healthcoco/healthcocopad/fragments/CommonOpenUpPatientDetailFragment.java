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
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

import static com.healthcoco.healthcocopad.R.id.container_right_action;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class CommonOpenUpPatientDetailFragment extends HealthCocoFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener, LocalDoInBackgroundListenerOptimised {
    private TabHost tabhost;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragmentsList = new ArrayList<>();
    private CommonOpenUpFragmentType fragmentType;
    //    private HorizontalScrollView mHorizontalScroll;
    private FragmentTransaction transaction;
    private Toolbar toolbar;
    private TextView tvInitialAlphabet;
    private TextView tvPatientName;
    private TextView tvGenderAge;
    private ImageView ivContactProfile;
    private LinearLayout patientProfileLayout;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private User user;
    private FloatingActionButton floatingActionButton;
    private int tabOrdinal;
    private String selectedFilterTitle;

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

    private void initTabs() {
        fragmentsList.clear();
        if (mViewPager.getAdapter() != null)
            mViewPager.getAdapter().notifyDataSetChanged();
        for (PatientDetailTabType detailTabType :
                PatientDetailTabType.values()) {
            tabhost.addTab(getTabSpec(detailTabType, detailTabType.getFragment()));
        }
    }

    private TabHost.TabSpec getTabSpec(PatientDetailTabType detailTabType, Fragment fragment) {
        fragmentsList.add(fragment);
        View view1 = mActivity.getLayoutInflater().inflate(R.layout.tab_indicator_home, null);
        ImageView ivIcon = (ImageView) view1.findViewById(R.id.iv_image);
        TextView tvText = (TextView) view1.findViewById(R.id.tv_text);
        ivIcon.setImageResource(detailTabType.getDrawableId());
        tvText.setText(detailTabType.getTextId());
        return tabhost.newTabSpec(fragment.getClass().getSimpleName()).setIndicator(view1).setContent(new DummyTabFactory(mActivity));
    }

    @Override
    public void initViews() {
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabhost.setup();
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
//        mHorizontalScroll = (HorizontalScrollView) view.findViewById(R.id.h_Scroll_View);
        tvInitialAlphabet = (TextView) view.findViewById(R.id.tv_initial_aplhabet);
        tvPatientName = (TextView) view.findViewById(R.id.tv_name);
        tvGenderAge = (TextView) view.findViewById(R.id.tv_patient_id);
        ivContactProfile = (ImageView) view.findViewById(R.id.iv_image);
        patientProfileLayout = (LinearLayout) view.findViewById(R.id.patient_profile_layout);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.bt_add_patient);
        ((CommonOpenUpActivity) mActivity).showRightAction(false);
    }

    @Override
    public void initListeners() {
        mViewPager.addOnPageChangeListener(this);
        tabhost.setOnTabChangedListener(this);
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initViewPagerAdapter() {
        mViewPager.setOffscreenPageLimit(fragmentsList.size());
        CommonViewPagerAdapter viewPagerAdapter = new CommonViewPagerAdapter(
                mActivity.getSupportFragmentManager());
        viewPagerAdapter.setFragmentsList(fragmentsList);
        mViewPager.setAdapter(viewPagerAdapter);
    }

    private void initFragment(int ordinal) {
        tabOrdinal = getArguments().getInt(HealthCocoConstants.TAG_TAB_TYPE);
        fragmentType = CommonOpenUpFragmentType.values()[ordinal];
        switch (fragmentType) {
            case PATIENT_DETAIL_PROFILE:
                openFragment(ActionbarType.TITLE, R.string.patient_profile, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, ActionbarLeftRightActionTypeDrawables.NO_LEFT_RIGHT_ACTION, new PatientProfileDetailFragment());
                break;
        }
        if (tabOrdinal == 5)
            tabhost.setCurrentTab(PatientDetailTabType.PATIENT_DETAIL_PRESCRIPTION.getTabPosition());
        else
            tabhost.setCurrentTab(PatientDetailTabType.PATIENT_DETAIL_PROFILE.getTabPosition());
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
        PatientDetailTabType patientDetailTabType = null;
        switch (tabhost.getCurrentTab()) {
            case PatientDetailTabType.POSITION_PROFILE_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_PROFILE;
                hidePatientDetailLayout();
                hideFloatingButton();
                refreshHomeScreenTitle(getResources().getString(R.string.patient_profile));
                break;
            case PatientDetailTabType.POSITION_VISIT_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_VISIT;
                showPatientDetailLayout();
                showFloatingButton();
                refreshHomeScreenTitle(getResources().getString(R.string.visits));
                break;
            case PatientDetailTabType.POSITION_CLINICAL_NOTES_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_CLINICAL_NOTES;
                showPatientDetailLayout();
                showFloatingButton();
                refreshHomeScreenTitle(getResources().getString(R.string.clinical_notes));
                break;
            case PatientDetailTabType.POSITION_IMPORTANT_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_IMPORTANT;
                showPatientDetailLayout();
                hideFloatingButton();
                refreshHomeScreenTitle(getResources().getString(R.string.important));
                break;
            case PatientDetailTabType.POSITION_REPORTS_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_REPORTS;
                showPatientDetailLayout();
                showFloatingButton();
                refreshHomeScreenTitle(getResources().getString(R.string.reports));
                break;
            case PatientDetailTabType.POSITION_PRESCRIPTION_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_PRESCRIPTION;
                showPatientDetailLayout();
                showFloatingButton();
                refreshHomeScreenTitle(getResources().getString(R.string.prescriptions));
                break;
            case PatientDetailTabType.POSITION_APPOINTMENT_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_APPOINTMENT;
                showPatientDetailLayout();
                showFloatingButton();
                refreshHomeScreenTitle(getResources().getString(R.string.appointment));
                break;
            case PatientDetailTabType.POSITION_TREATMENT_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_TREATMENT;
                showPatientDetailLayout();
                showFloatingButton();
                refreshHomeScreenTitle(getResources().getString(R.string.treatment));
                break;
        }
    }

    private void refreshHomeScreenTitle(String title) {
        this.selectedFilterTitle = title;
        ((CommonOpenUpActivity) mActivity).initActionbarTitle(title);
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
            tvGenderAge.setText(selectedPatient.getPid());
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
        }
    }

    public void showPatientDetailLayout() {
        if (patientProfileLayout.getVisibility() == View.GONE)
            patientProfileLayout.setVisibility(View.VISIBLE);
    }

    public void hidePatientDetailLayout() {
        if (patientProfileLayout.getVisibility() == View.VISIBLE)
            patientProfileLayout.setVisibility(View.GONE);
    }

    public void showFloatingButton() {
        if (floatingActionButton.getVisibility() == View.GONE)
            floatingActionButton.setVisibility(View.VISIBLE);
    }

    public void hideFloatingButton() {
        if (floatingActionButton.getVisibility() == View.VISIBLE)
            floatingActionButton.setVisibility(View.GONE);
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

    public RegisteredPatientDetailsUpdated getSelectedPatient() {
        return selectedPatient;
    }

    public User getUser() {
        return user;
    }
}
