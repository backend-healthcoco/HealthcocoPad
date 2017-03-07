package com.healthcoco.healthcocoplus.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoFragment;
import com.healthcoco.healthcocoplus.adapter.CommonViewPagerAdapter;
import com.healthcoco.healthcocoplus.custom.DummyTabFactory;
import com.healthcoco.healthcocoplus.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocoplus.enums.PatientDetailTabType;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 03-03-2017.
 */
public class CommonOpenUpPatientDetailFragment extends HealthCocoFragment implements View.OnClickListener, ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {
    private TabHost tabhost;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragmentsList = new ArrayList<>();
    private CommonOpenUpFragmentType fragmentType;
    private HorizontalScrollView mHorizontalScroll;

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
    }

    private void initFragment(int ordinal) {
        fragmentType = CommonOpenUpFragmentType.values()[ordinal];
        switch (fragmentType) {
            case PATIENT_DETAIL_PROFILE:
                break;
        }
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initTabs();
        initViewPagerAdapter();
//        initFragment(CommonOpenUpFragmentType.PATIENT_DETAIL_PROFILE.ordinal());
        tabhost.setCurrentTab(PatientDetailTabType.PATIENT_DETAIL_PROFILE.getTabPosition());
//        mActivity.showLoading(false);
//        new LocalDataBackgroundtaskOptimised(mActivity, GET_FRAGMENT_INITIALISATION_DATA, this, this, this).execute();
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
        mHorizontalScroll = (HorizontalScrollView) view.findViewById(R.id.h_Scroll_View);
    }

    @Override
    public void initListeners() {
        mViewPager.addOnPageChangeListener(this);
        tabhost.setOnTabChangedListener(this);
    }

    private void initViewPagerAdapter() {
        mViewPager.setOffscreenPageLimit(fragmentsList.size());
        CommonViewPagerAdapter viewPagerAdapter = new CommonViewPagerAdapter(
                mActivity.getSupportFragmentManager());
        viewPagerAdapter.setFragmentsList(fragmentsList);
        mViewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        View tabView = tabhost.getTabWidget().getChildAt(position);
        if (tabView != null) {
            final int width = mHorizontalScroll.getWidth();
            final int scrollPos = tabView.getLeft() - (width - tabView.getWidth()) / 2;
            mHorizontalScroll.scrollTo(scrollPos, 0);
        } else {
            mHorizontalScroll.scrollBy(positionOffsetPixels, 0);
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
    public void onTabChanged(String tabId) {
        mViewPager.setCurrentItem(tabhost.getCurrentTab());
        PatientDetailTabType patientDetailTabType = null;
        switch (tabhost.getCurrentTab()) {
            case PatientDetailTabType.POSITION_PROFILE_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_PROFILE;
                break;
            case PatientDetailTabType.POSITION_VISIT_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_VISIT;
                break;
            case PatientDetailTabType.POSITION_CLINICAL_NOTES_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_CLINICAL_NOTES;
                break;
            case PatientDetailTabType.POSITION_IMPORTANT_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_IMPORTANT;
                break;
            case PatientDetailTabType.POSITION_REPORTS_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_REPORTS;
                break;
            case PatientDetailTabType.POSITION_PRESCRIPTION_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_PRESCRIPTION;
                break;
            case PatientDetailTabType.POSITION_APPOINTMENT_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_APPOINTMENT;
                break;
            case PatientDetailTabType.POSITION_TREATMENT_TAB:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL_TREATMENT;
                break;
            case PatientDetailTabType.POSITION_PATIENT_DETAIL:
                patientDetailTabType = PatientDetailTabType.PATIENT_DETAIL;
                break;
            case PatientDetailTabType.POSITION_PATIENT:
                patientDetailTabType = PatientDetailTabType.PATIENT;
                break;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
