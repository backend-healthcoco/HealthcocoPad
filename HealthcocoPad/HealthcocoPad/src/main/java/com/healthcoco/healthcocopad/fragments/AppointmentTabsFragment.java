package com.healthcoco.healthcocopad.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TabHost;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.request.RegisterNewPatientRequest;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.FollowUpAppointmentTabsType;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

/**
 * Created by Prashant on 25-06-18.
 */

public class AppointmentTabsFragment extends HealthCocoFragment implements ViewPager.OnPageChangeListener,
        View.OnClickListener, PatientRegistrationDetailsListener {

    private PatientNumberSearchFragment searchFragment;
    private FollowUpAppointmentFragment followUpAppointmentFragment;
    private ConfirmAppointmentFragment confirmAppointmentFragment;
    private TabHost tabhost;
    private ViewPager mViewPager;
    private ImageButton btCross;
    private Button btSave;
    private TextView tvTitle;
    private ArrayList<Fragment> fragmentsList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_appointment_tabs, container, false);
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
        mViewPager = (ViewPager) view.findViewById(R.id.viewpager_home_tabs);

        btCross = (ImageButton) view.findViewById(R.id.bt_cross);
        btSave = (Button) view.findViewById(R.id.bt_save);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);

        btCross.setImageResource(R.drawable.selector_actionbar_back);
        tvTitle.setText(getString(R.string.appointment));

        btSave.setVisibility(View.INVISIBLE);
    }

    public void initListeners() {
        mViewPager.addOnPageChangeListener(this);
        btSave.setOnClickListener(this);
        btCross.setOnClickListener(this);
    }

    private void initTabs() {
        fragmentsList.clear();
        tabhost.clearAllTabs();
        if (mViewPager.getAdapter() != null)
            mViewPager.getAdapter().notifyDataSetChanged();
        for (FollowUpAppointmentTabsType followUpAppointmentTabsType :
                FollowUpAppointmentTabsType.values()) {
            switch (followUpAppointmentTabsType) {
                case SEARCH_PATIENT:
                    searchFragment = new PatientNumberSearchFragment(this);
                    addFragment(followUpAppointmentTabsType, searchFragment);
                    break;
                case SELECT_APPOINTMENT:
                    followUpAppointmentFragment = new FollowUpAppointmentFragment(this);
                    addFragment(followUpAppointmentTabsType, followUpAppointmentFragment);
                    break;
                case CONFIRM_APPOINTMENT:
                    confirmAppointmentFragment = new ConfirmAppointmentFragment(this);
                    addFragment(followUpAppointmentTabsType, confirmAppointmentFragment);
                    break;
            }

        }

    }

    private void addFragment(FollowUpAppointmentTabsType followUpAppointmentTabsType, Fragment fragment) {
        tabhost.addTab(getTabSpec(followUpAppointmentTabsType, fragment));
        tabhost.getTabWidget().getChildTabViewAt(followUpAppointmentTabsType.getTabPosition()).setEnabled(false);
    }

    private TabHost.TabSpec getTabSpec(FollowUpAppointmentTabsType followUpAppointmentTabsType, Fragment fragment) {
        fragmentsList.add(fragment);
        View view1 = mActivity.getLayoutInflater().inflate(R.layout.tab_indicator_add_visit, null);
        TextView tvTabText = (TextView) view1.findViewById(R.id.tv_tab_text);
        tvTabText.setText(getString(followUpAppointmentTabsType.getTextId()));
        return tabhost.newTabSpec(String.valueOf(followUpAppointmentTabsType)).setIndicator(view1).setContent(new DummyTabFactory(mActivity));
    }

    private void initViewPagerAdapter() {
        mViewPager.setOffscreenPageLimit(fragmentsList.size());
        ContactsDetailViewPagerAdapter viewPagerAdapter = new ContactsDetailViewPagerAdapter(mActivity.getSupportFragmentManager());
        viewPagerAdapter.setFragmentsList(fragmentsList);
        mViewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

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
        int id = v.getId();

        if (id == R.id.bt_cross) {
            prevButtonOnClick();
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
        FollowUpAppointmentTabsType followUpAppointmentTabsType = FollowUpAppointmentTabsType.values()[currentItem];
        switch (followUpAppointmentTabsType) {
            case SEARCH_PATIENT:
                RegisterNewPatientRequest patientDetails = (RegisterNewPatientRequest) object;
                if (patientDetails != null) {
                    if (!Util.isNullOrBlank(patientDetails.getUserId())) {
                        followUpAppointmentFragment.initDataFromPreviousFragment(object, isEditPatient);
                        scrollToNext();
                    } else {
                        openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_REGISTRATION_TABS, HealthCocoConstants.TAG_MOBILE_NUMBER, patientDetails.getMobileNumber());
                        mActivity.finish();
                    }
                }

                break;
            case SELECT_APPOINTMENT:
                confirmAppointmentFragment.initDataFromPreviousFragment(object, isEditPatient);
                scrollToNext();
                break;
            case CONFIRM_APPOINTMENT:
//                otherDeatilsFragment.initDataFromPreviousFragment(object, isEditPatient);
                break;
        }
    }

    public void prevButtonOnClick() {
        int currentItem = mViewPager.getCurrentItem();
        FollowUpAppointmentTabsType followUpAppointmentTabsType = FollowUpAppointmentTabsType.values()[currentItem];
        switch (followUpAppointmentTabsType) {
            case SEARCH_PATIENT:
                ((CommonOpenUpActivity) mActivity).finishThisActivity();
                break;
            case SELECT_APPOINTMENT:
                scrollToPrev();
                break;
            case CONFIRM_APPOINTMENT:
                scrollToPrev();
                break;
        }
    }

    @Override
    public boolean isFromPatientRegistarion() {
        return false;
    }
}
