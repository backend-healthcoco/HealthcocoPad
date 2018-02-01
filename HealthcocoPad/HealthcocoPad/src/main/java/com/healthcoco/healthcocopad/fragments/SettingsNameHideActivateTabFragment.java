package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.enums.NameHideActivateType;
import com.healthcoco.healthcocopad.enums.SettingsItemType;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;


import java.util.ArrayList;


/**
 * Created by Prashant on 31-01-2018.
 */
public class SettingsNameHideActivateTabFragment extends HealthCocoFragment implements ViewPager.OnPageChangeListener, TabHost.OnTabChangeListener {
    private TabHost tabhost;
    private ViewPager mViewPager;
    private ArrayList<Fragment> fragmentsList = new ArrayList<>();
    private HorizontalScrollView mHorizontalScroll;
    private SettingsItemType settingsItemType;
    private FragmentTransaction transaction;

    //    private TabLayout tabLayout;
    private int typeOrdinal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_fragment_settings_hide_activate, container, false);
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
        Intent intent = mActivity.getIntent();
        typeOrdinal = intent.getIntExtra(HealthCocoConstants.TAG_NAME_EDIT_TYPE, -1);
        if (typeOrdinal > -1)
            settingsItemType = SettingsItemType.values()[typeOrdinal];

        initViews();
        initListeners();
        initTabsAndViewPagerFragments();
    }

    @Override
    public void initViews() {
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        tabhost.setup();
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
//        tabLayout.setupWithViewPager(mViewPager);
        mHorizontalScroll = (HorizontalScrollView) view.findViewById(R.id.h_Scroll_View);


    }

    @Override
    public void initListeners() {
        mViewPager.addOnPageChangeListener(this);
        tabhost.setOnTabChangedListener(this);
    }

    private void initTabsAndViewPagerFragments() {
        initTabsFragmentsList();
        initViewPagerAdapter();

    }


    private void initTabsFragmentsList() {
        tabhost.setup();
        fragmentsList = new ArrayList<>();
        if (settingsItemType != null) {
            switch (settingsItemType) {
                case GROUPS:

                    NameHideActivateTabFragment groupNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.GROUPS.ordinal());
                    addFragment(groupNameHideActivateTabFragment, R.string.groups, false);

                    NameHideActivateTabFragment referenceNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.REFERENCE.ordinal());
                    addFragment(referenceNameHideActivateTabFragment, R.string.reference, false);

                    break;

                case PRESCRIPTION:
                    NameHideActivateTabFragment directionNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.DIRECTION.ordinal());
                    addFragment(directionNameHideActivateTabFragment, R.string.direction, false);

                    NameHideActivateTabFragment frequencyNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.FREQUENCY.ordinal());
                    addFragment(frequencyNameHideActivateTabFragment, R.string.frequency, false);

                    NameHideActivateTabFragment drugNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.DRUG.ordinal());
                    addFragment(drugNameHideActivateTabFragment, R.string.my_drugs, false);

                    break;
                case CLINICAL_NOTE:

                    NameHideActivateTabFragment presentComplaintNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.PRESENT_COMPLAINT.ordinal());
                    NameHideActivateTabFragment complaintNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.COMPLAINT.ordinal());
                    NameHideActivateTabFragment historyOfPresentComplaintNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.HISTORY_OF_PRESENT_COMPLAINT.ordinal());
                    NameHideActivateTabFragment menstrualHistoryNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.MENSTRUAL_HISTORY.ordinal());
                    NameHideActivateTabFragment ObstetricHistoryNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.OBSTETRIC_HISTORY.ordinal());
                    NameHideActivateTabFragment generalExamNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.GENERAL_EXAMINATION.ordinal());
                    NameHideActivateTabFragment systemicExamNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.SYSTEMIC_EXAMINATION.ordinal());
                    NameHideActivateTabFragment observationNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.OBSERVATION.ordinal());
                    NameHideActivateTabFragment investigationNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.INVESTIGATIONS.ordinal());
                    NameHideActivateTabFragment provisionalNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.PROVISIONAL_DIAGNOSIS.ordinal());
                    NameHideActivateTabFragment diagnosisNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.DIAGNOSIS.ordinal());
                    NameHideActivateTabFragment ecgNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.ECG.ordinal());
                    NameHideActivateTabFragment echoNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.ECHO.ordinal());
                    NameHideActivateTabFragment xrayNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.XRAY.ordinal());
                    NameHideActivateTabFragment holterNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.HOLTER.ordinal());
                    NameHideActivateTabFragment paNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.PA.ordinal());
                    NameHideActivateTabFragment pvNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.PV.ordinal());
                    NameHideActivateTabFragment psNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.PS.ordinal());
                    NameHideActivateTabFragment indicationOfUsgNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.INDICATION_OF_USG.ordinal());
                    NameHideActivateTabFragment NotesNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.NOTES.ordinal());
                    NameHideActivateTabFragment ProcedureNoteNameHideActivateTabFragment = new NameHideActivateTabFragment(NameHideActivateType.PROCEDURE_NOTE.ordinal());


                    addFragment(presentComplaintNameHideActivateTabFragment, R.string.present_complaint, false);
                    addFragment(complaintNameHideActivateTabFragment, R.string.complaints, false);
                    addFragment(historyOfPresentComplaintNameHideActivateTabFragment, R.string.history_of_present_complaints, false);
                    addFragment(menstrualHistoryNameHideActivateTabFragment, R.string.menstrual_history, false);
                    addFragment(ObstetricHistoryNameHideActivateTabFragment, R.string.obstetric_history, false);
                    addFragment(generalExamNameHideActivateTabFragment, R.string.general_examination, false);
                    addFragment(systemicExamNameHideActivateTabFragment, R.string.systemic_examination, false);
                    addFragment(observationNameHideActivateTabFragment, R.string.observations, false);
                    addFragment(investigationNameHideActivateTabFragment, R.string.investigations, false);
                    addFragment(provisionalNameHideActivateTabFragment, R.string.provisional_diagnosis, false);
                    addFragment(diagnosisNameHideActivateTabFragment, R.string.diagnosis, false);
                    addFragment(ecgNameHideActivateTabFragment, R.string.ecg_details, false);
                    addFragment(echoNameHideActivateTabFragment, R.string.echo, false);
                    addFragment(xrayNameHideActivateTabFragment, R.string.xray_details, false);
                    addFragment(holterNameHideActivateTabFragment, R.string.holter, false);
                    addFragment(paNameHideActivateTabFragment, R.string.pa, false);
                    addFragment(pvNameHideActivateTabFragment, R.string.pv, false);
                    addFragment(psNameHideActivateTabFragment, R.string.ps, false);
                    addFragment(indicationOfUsgNameHideActivateTabFragment, R.string.indication_of_usg, false);
                    addFragment(NotesNameHideActivateTabFragment, R.string.notes, false);
                    addFragment(ProcedureNoteNameHideActivateTabFragment, R.string.procedures, false);

                    break;
            }

        }
    }


    private void addFragment(Fragment fragment, int tabIndicatorId, boolean isLastTab) {
        fragmentsList.add(fragment);
        tabhost.addTab(getTabSpec(fragment.getClass().getSimpleName(), tabIndicatorId, isLastTab));
    }

    private TabHost.TabSpec getTabSpec(String simpleName, int textId, boolean isLastTab) {
        View view1 = mActivity.getLayoutInflater().inflate(R.layout.tab_indicator_add_visit, null);
        TextView tvTabText = (TextView) view1.findViewById(R.id.tv_tab_text);
        tvTabText.setText(textId);
        return tabhost.newTabSpec(simpleName).setIndicator(view1).setContent(new DummyTabFactory(mActivity));
    }

    private void initViewPagerAdapter() {
        mViewPager.setOffscreenPageLimit(fragmentsList.size());
        ContactsDetailViewPagerAdapter viewPagerAdapter = new ContactsDetailViewPagerAdapter(
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
            tabhost.refreshDrawableState();
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
        String currentTabTag = tabhost.getCurrentTabTag();
    }


}
