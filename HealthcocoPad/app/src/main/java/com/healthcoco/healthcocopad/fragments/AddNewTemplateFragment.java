package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Shreshtha on 27-03-2017.
 */
public class AddNewTemplateFragment extends HealthCocoFragment implements TabHost.OnTabChangeListener,
        ViewPager.OnPageChangeListener, View.OnClickListener {

    private ViewPager viewPager;
    private TabHost tabhost;
    private ArrayList<Fragment> fragmentsList;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private String visitId;
    private VisitDetails visitDetails;
    private TemplateListFragment templateListFragment;
    private DrugListFragment drugListFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragemnt_add_new_template, container, false);
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
        initData();
        initAdapters();
        getDataFromIntent();
        initTabsFragmentsList();
        initViewPagerAdapter();
//        showLoadingOverlay(true);
//        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    private void getDataFromIntent() {
        Intent intent = mActivity.getIntent();
        selectedPatient = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_COMMON_OPENUP_INTENT_DATA));
    }

    private void initData() {

    }

    private void initAdapters() {

    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
    }

    @Override
    public void initListeners() {
        tabhost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    private void initTabsFragmentsList() {
        tabhost.setup();
        fragmentsList = new ArrayList<>();
        // init fragment 1
        drugListFragment = new DrugListFragment();
        // init fragment 2
        templateListFragment = new TemplateListFragment();
//        Bundle bundle = new Bundle();
//        bundle.putBoolean(AddNewPrescriptionFragment.TAG_HIDE_PATIENT_DETAIL_HEADER, true);
//        templateListFragment.setArguments(bundle);
        addFragment(drugListFragment, R.string.drugs, false);
        addFragment(templateListFragment, R.string.templates, true);
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
        viewPager.setOffscreenPageLimit(fragmentsList.size());
        ContactsDetailViewPagerAdapter viewPagerAdapter = new ContactsDetailViewPagerAdapter(
                mActivity.getSupportFragmentManager());
        viewPagerAdapter.setFragmentsList(fragmentsList);
        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onTabChanged(String tabId) {
        viewPager.setCurrentItem(tabhost.getCurrentTab());
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

    public RegisteredPatientDetailsUpdated getSelectedPatient() {
        return selectedPatient;
    }

    @Override
    public void onClick(View v) {

    }

}
