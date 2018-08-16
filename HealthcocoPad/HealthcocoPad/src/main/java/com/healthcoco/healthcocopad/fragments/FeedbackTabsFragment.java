package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
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
import com.healthcoco.healthcocopad.bean.request.RegisterNewPatientRequest;
import com.healthcoco.healthcocopad.bean.server.AlreadyRegisteredPatientsResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.dialogFragment.OtpVarificationFragment;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.FollowUpAppointmentTabsType;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationDetailsListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomViewPager;

import java.util.ArrayList;

/**
 * Created by Prashant on 25-06-18.
 */

public class FeedbackTabsFragment extends HealthCocoFragment implements ViewPager.OnPageChangeListener,
        View.OnClickListener, PatientRegistrationDetailsListener {

    private PatientNumberSearchFragment searchFragment;
    private AppointmentFeedbackFragment appointmentFeedbackFragment;
    private TabHost tabhost;
    private CustomViewPager mViewPager;
    private ImageButton btCross;
    private Button btSave;
    private TextView tvTitle;
    private ArrayList<Fragment> fragmentsList = new ArrayList<>();
    private String patientId;
    private String mobileNumber;
    private User user;

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
        LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
        if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
            user = doctor.getUser();
        }
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
        tvTitle.setText(getString(R.string.search_patient));

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
                    appointmentFeedbackFragment = new AppointmentFeedbackFragment();
                    addFragment(followUpAppointmentTabsType, appointmentFeedbackFragment);
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
        FollowUpAppointmentTabsType followUpAppointmentTabsType = FollowUpAppointmentTabsType.values()[position];
        switch (followUpAppointmentTabsType) {
            case SEARCH_PATIENT:
                btSave.setVisibility(View.INVISIBLE);
                break;
            case SELECT_APPOINTMENT:
                btSave.setVisibility(View.VISIBLE);
                tvTitle.setText(getString(R.string.feedback));
                btSave.setText(getString(R.string.submit));
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
        switch (v.getId()) {
            case R.id.bt_cross:
                prevButtonOnClick();
                break;
            case R.id.bt_save:
                nextButtonOnClick();
                break;
        }
    }

    private void nextButtonOnClick() {
        int currentItem = mViewPager.getCurrentItem();
        FollowUpAppointmentTabsType followUpAppointmentTabsType = FollowUpAppointmentTabsType.values()[currentItem];
        switch (followUpAppointmentTabsType) {
            case SEARCH_PATIENT:
//                scrollToNext();
                break;
            case SELECT_APPOINTMENT:
                appointmentFeedbackFragment.validateData();
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
        RegisterNewPatientRequest patientDetails = (RegisterNewPatientRequest) object;
        if (patientDetails != null) {
            if (!Util.isNullOrBlank(patientDetails.getUserId())) {
                initDataFromPreviousFragment(object, isEditPatient);
            } else {
                openCommonOpenUpActivity(CommonOpenUpFragmentType.PATIENT_REGISTRATION_TABS, HealthCocoConstants.TAG_MOBILE_NUMBER, patientDetails.getMobileNumber());
                mActivity.finish();
            }
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
        }
    }

    @Override
    public boolean isFromPatientRegistarion() {
        return false;
    }

    public void initDataFromPreviousFragment(Object object, boolean isEditPatient) {
        RegisterNewPatientRequest patientRequest = (RegisterNewPatientRequest) object;
        if (patientRequest != null) {
            if (!Util.isNullOrBlank(patientRequest.getUserId()))
                patientId = patientRequest.getUserId();

            if (!Util.isNullOrBlank(patientId)) {
                if (!Util.isNullOrBlank(patientId)) {
                    if (isEditPatient) {
                        RegisteredPatientDetailsUpdated patient = LocalDataServiceImpl.getInstance(mApp).getPatient(patientId, user.getForeignLocationId());
                        if (patient != null)
                            initPatientDetails(patient);
                        else {
                            AlreadyRegisteredPatientsResponse alreadyRegisteredPatient = LocalDataServiceImpl.getInstance(mApp).getALreadyRegisteredPatient(patientId);
                            if (alreadyRegisteredPatient != null) {
                                initPatientDetails(alreadyRegisteredPatient);
                            }
                        }
                    }
                }
            }
        }

    }

    private void initPatientDetails(Object patientDetails) {
        if (patientDetails instanceof RegisteredPatientDetailsUpdated) {
            RegisteredPatientDetailsUpdated registeredPatientDetailsUpdated = (RegisteredPatientDetailsUpdated) patientDetails;
            mobileNumber = Util.getValidatedValue(registeredPatientDetailsUpdated.getMobileNumber());

        } else if (patientDetails instanceof AlreadyRegisteredPatientsResponse) {
            AlreadyRegisteredPatientsResponse alreadyRegisteredPatient = (AlreadyRegisteredPatientsResponse) patientDetails;
            mobileNumber = Util.getValidatedValue(alreadyRegisteredPatient.getMobileNumber());
        }
        if (!Util.isNullOrBlank(mobileNumber))
            openVerifyOtpFragment();
    }


    private void openVerifyOtpFragment() {

        OtpVarificationFragment dialogFragment = new OtpVarificationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(HealthCocoConstants.TAG_UNIQUE_ID, patientId);
        bundle.putString(HealthCocoConstants.TAG_MOBILE_NUMBER, mobileNumber);
        dialogFragment.setArguments(bundle);
        dialogFragment.setTargetFragment(this, HealthCocoConstants.REQUEST_CODE_FEEDBACK_OTP);
        dialogFragment.show(mActivity.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_FEEDBACK_OTP) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_FEEDBACK_OTP) {
                appointmentFeedbackFragment.initDataFromPreviousFragment(patientId);
                scrollToNext();
            }
        }
    }

}
