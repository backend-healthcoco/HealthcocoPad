package com.healthcoco.healthcocopad.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.server.Patient;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.enums.PatientRegistrationTabsType;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomViewPager;

import java.util.ArrayList;

/**
 * Created by Prashant on 25-06-18.
 */

public class PatientRegistrationTabsFragment extends HealthCocoFragment implements ViewPager.OnPageChangeListener,
        View.OnClickListener, PatientRegistrationDetailsListener, TabHost.OnTabChangeListener {

    PatientNumberSearchFragment searchFragment;
    PatientProfileFragment basicDetailsFragment;
    PatientAssessmentFragment patientAssessmentFragment;
    private TabHost tabhost;
    private CustomViewPager mViewPager;
    private ImageButton btCross;
    private Button btSave;
    private TextView tvTitle;
    private ArrayList<Fragment> fragmentsList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_patient_registration_tabs, container, false);
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
        initViews();
        initListeners();
        initTabs();
        initViewPagerAdapter();
    }

    public void initViews() {
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabhost.setup();
        mViewPager = (CustomViewPager) view.findViewById(R.id.viewpager_home_tabs);

        btCross = (ImageButton) view.findViewById(R.id.bt_cross);
        btSave = (Button) view.findViewById(R.id.bt_save);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);

        btCross.setImageResource(R.drawable.selector_actionbar_back);
        tvTitle.setText(getString(R.string.patient_registration));
    }

    public void initListeners() {
        mViewPager.addOnPageChangeListener(this);
        btSave.setOnClickListener(this);
        tabhost.setOnTabChangedListener(this);
        btCross.setOnClickListener(this);
    }

    private void initTabs() {
        fragmentsList.clear();
        tabhost.clearAllTabs();
        if (mViewPager.getAdapter() != null)
            mViewPager.getAdapter().notifyDataSetChanged();
        for (PatientRegistrationTabsType registrationTabsType :
                PatientRegistrationTabsType.values()) {
            switch (registrationTabsType) {
                case SEARCH_PATIENT:
                    searchFragment = new PatientNumberSearchFragment(this);
                    addFragment(registrationTabsType, searchFragment);
                    break;
                case BASIC_DETAILS:
                    basicDetailsFragment = new PatientProfileFragment(this);
                    addFragment(registrationTabsType, basicDetailsFragment);
                    break;
                case MORE_DETAIlS:
                    patientAssessmentFragment = new PatientAssessmentFragment();
                    addFragment(registrationTabsType, patientAssessmentFragment);
                    break;
            }

        }

    }

    private void addFragment(PatientRegistrationTabsType registrationTabsType, Fragment fragment) {
        tabhost.addTab(getTabSpec(registrationTabsType, fragment));
        tabhost.getTabWidget().getChildTabViewAt(registrationTabsType.getTabPosition()).setEnabled(false);
    }

    private TabHost.TabSpec getTabSpec(PatientRegistrationTabsType registrationTabsType, Fragment fragment) {
        fragmentsList.add(fragment);
        View view1 = mActivity.getLayoutInflater().inflate(R.layout.tab_indicator_add_visit, null);
        TextView tvTabText = (TextView) view1.findViewById(R.id.tv_tab_text);
        tvTabText.setText(registrationTabsType.getStringValue());
        return tabhost.newTabSpec(String.valueOf(registrationTabsType)).setIndicator(view1).setContent(new DummyTabFactory(mActivity));
    }

    private void initViewPagerAdapter() {
        mViewPager.setOffscreenPageLimit(fragmentsList.size());
        ContactsDetailViewPagerAdapter viewPagerAdapter = new ContactsDetailViewPagerAdapter(mActivity.getSupportFragmentManager());
        viewPagerAdapter.setFragmentsList(fragmentsList);
        mViewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        PatientRegistrationTabsType registrationTabsType = PatientRegistrationTabsType.values()[position];
        switch (registrationTabsType) {
            case SEARCH_PATIENT:
                btSave.setVisibility(View.GONE);
                break;
            case BASIC_DETAILS:
                btSave.setVisibility(View.VISIBLE);
                btSave.setText(getString(R.string.save));
                break;
            case MORE_DETAIlS:
                btSave.setVisibility(View.VISIBLE);
                btSave.setText(getString(R.string.save));
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        tabhost.setCurrentTab(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v) {
        mActivity.hideSoftKeyboard();
        switch (v.getId()) {
            case R.id.bt_save:
                nextButtonOnClick();
                break;
            case R.id.bt_cross:
                prevButtonOnClick();
                break;

        }
    }

    private void nextButtonOnClick() {
        int currentItem = mViewPager.getCurrentItem();
        PatientRegistrationTabsType registrationTabsType = PatientRegistrationTabsType.values()[currentItem];
        switch (registrationTabsType) {
            case SEARCH_PATIENT:
                Util.showToast(mActivity, R.string.please_enter_valid_mobile_no);
                break;
            case BASIC_DETAILS:
                basicDetailsFragment.validateData();
                break;
            case MORE_DETAIlS:
//                basicDetailsFragment.validateData();
                break;
        }
    }

    public void prevButtonOnClick() {
        int currentItem = mViewPager.getCurrentItem();
        PatientRegistrationTabsType registrationTabsType = PatientRegistrationTabsType.values()[currentItem];
        switch (registrationTabsType) {
            case SEARCH_PATIENT:
                ((CommonOpenUpActivity) mActivity).finishThisActivity();
                break;
            case BASIC_DETAILS:
                scrollToPrev();
                break;
            case MORE_DETAIlS:
                ((CommonOpenUpActivity) mActivity).finishThisActivity();
                break;
        }
    }

    private void scrollToNext() {
        mViewPager.setCurrentItem((mViewPager.getCurrentItem() + 1), true);
    }

    private void scrollToPrev() {
        mViewPager.setCurrentItem((mViewPager.getCurrentItem() - 1), true);
    }

    @Override
    public void readyToMoveNext(Object object, boolean isEditPatient) {
        int currentItem = mViewPager.getCurrentItem();
        PatientRegistrationTabsType registrationTabsType = PatientRegistrationTabsType.values()[currentItem];
        switch (registrationTabsType) {
            case SEARCH_PATIENT:
                basicDetailsFragment.initDataFromPreviousFragment(object, isEditPatient);
                break;
            case BASIC_DETAILS:
                patientAssessmentFragment.initDataFromPreviousFragment(object, isEditPatient);
                break;
        }
        scrollToNext();

    }

    @Override
    public boolean isFromPatientRegistarion() {
        return true;
    }

    @Override
    public void onTabChanged(String tabId) {
    }
}
