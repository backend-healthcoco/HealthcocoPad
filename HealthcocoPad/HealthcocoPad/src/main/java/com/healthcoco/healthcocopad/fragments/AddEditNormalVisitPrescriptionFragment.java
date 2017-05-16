package com.healthcoco.healthcocopad.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.adapter.SelectedTemplateDrugItemsListAdapter;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;
import com.healthcoco.healthcocopad.listeners.DiagnosticTestItemListener;
import com.healthcoco.healthcocopad.listeners.SelectDrugItemClickListener;
import com.healthcoco.healthcocopad.listeners.SelectedDrugsListItemListener;
import com.healthcoco.healthcocopad.listeners.TemplateListItemListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by Shreshtha on 02-03-2017.
 */
public class AddEditNormalVisitPrescriptionFragment extends HealthCocoFragment implements
        TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, View.OnClickListener,
        SelectDrugItemClickListener, SelectedDrugsListItemListener, TemplateListItemListener, DiagnosticTestItemListener {

    private ViewPager viewPager;
    private TabHost tabhost;
    private ArrayList<Fragment> fragmentsList;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private String visitId;
    private VisitDetails visitDetails;
    private TemplateListFragment templateListFragment;
    private DiagnosticTestListFragment diagnosticTestListFragment;
    private DrugListFragment drugListFragment;
    private EditText etDuration;
    private EditText etTemplateName;
    private LinkedHashMap<String, DrugItem> drugsListHashMap = new LinkedHashMap<>();
    private SelectedTemplateDrugItemsListAdapter adapter;
    private ListView lvTemplates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_normal_prescription_visit, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapters();
        initTabsFragmentsList();
        initViewPagerAdapter();
    }

    private void initAdapters() {
        adapter = new SelectedTemplateDrugItemsListAdapter(mActivity, this);
        lvTemplates.setAdapter(adapter);
    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        etDuration = (EditText) view.findViewById(R.id.et_duration);
        etTemplateName = (EditText) view.findViewById(R.id.et_template_name);
        lvTemplates = (ListView) view.findViewById(R.id.lv_templates);
    }

    @Override
    public void initListeners() {
        tabhost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initTabsFragmentsList() {
        tabhost.setup();
        fragmentsList = new ArrayList<>();
        // init fragment 1
        drugListFragment = new DrugListFragment(this);
        // init fragment 2
        templateListFragment = new TemplateListFragment(this);
        // init fragment 3
        diagnosticTestListFragment = new DiagnosticTestListFragment(this);
        addFragment(drugListFragment, R.string.drugs, false);
        addFragment(templateListFragment, R.string.templates, true);
        addFragment(diagnosticTestListFragment, R.string.lab_test, true);
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

    @Override
    public void ondrugItemClick(SelectDrugItemType drugItemType, Object object) {

    }

    @Override
    public void onDeleteItemClicked(DrugItem drug) {

    }

    @Override
    public void onDrugItemClicked(DrugItem drug) {

    }

    @Override
    public MyScriptAddVisitsFragment getAddVisitFragment() {
        return null;
    }

    @Override
    public void onPrescriptionClicked(TempTemplate template) {

    }

    @Override
    public void onEditClicked(TempTemplate template) {

    }

    @Override
    public void onDeleteClicked(TempTemplate template) {

    }

    @Override
    public void onItemClicked(TempTemplate template) {

    }

    @Override
    public boolean isFromSettingsScreen() {
        return false;
    }

    @Override
    public void onAddClicked(DiagnosticTest diagnosticTest) {

    }

    @Override
    public void onAddedClicked(DiagnosticTest diagnosticTest) {

    }

    @Override
    public DiagnosticTest getDiagnosticTest(String uniqueId) {
        return null;
    }

    @Override
    public void onDeleteItemClicked(DiagnosticTest diagnosticTest) {

    }
}
