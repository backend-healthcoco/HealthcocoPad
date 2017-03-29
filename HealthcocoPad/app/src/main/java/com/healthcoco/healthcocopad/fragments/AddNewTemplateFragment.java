package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.Bundle;
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
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.adapter.SelectedTemplateDrugItemsListAdapter;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.Duration;
import com.healthcoco.healthcocopad.bean.server.GenericName;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;
import com.healthcoco.healthcocopad.listeners.SelectDrugItemClickListener;
import com.healthcoco.healthcocopad.listeners.SelectedDrugsListItemListener;
import com.healthcoco.healthcocopad.listeners.TemplateListItemListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Shreshtha on 27-03-2017.
 */
public class AddNewTemplateFragment extends HealthCocoFragment implements TabHost.OnTabChangeListener,
        ViewPager.OnPageChangeListener, View.OnClickListener, SelectDrugItemClickListener, SelectedDrugsListItemListener, TemplateListItemListener {

    private ViewPager viewPager;
    private TabHost tabhost;
    private ArrayList<Fragment> fragmentsList;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private String visitId;
    private VisitDetails visitDetails;
    private TemplateListFragment templateListFragment;
    private DrugListFragment drugListFragment;
    private EditText etDuration;
    private EditText etTemplateName;
    private LinkedHashMap<String, Drug> drugsListHashMap = new LinkedHashMap<>();
    private SelectedTemplateDrugItemsListAdapter adapter;
    private ListView lvTemplates;

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
    }

    private void initTabsFragmentsList() {
        tabhost.setup();
        fragmentsList = new ArrayList<>();
        // init fragment 1
        drugListFragment = new DrugListFragment(this);
        // init fragment 2
        templateListFragment = new TemplateListFragment(this);
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

    @Override
    public void ondrugItemClick(SelectDrugItemType drugItemType, Object object) {
        addSelectedDrug(drugItemType, object);
    }

    public void addSelectedDrug(SelectDrugItemType selectDrugItemType, Object object) {
        Drug selectedDrug = new Drug();
        String drugId = "";
        String drugName = "";
        String drugType = "";
        String drugTypeId = "";
        String directionId = "";
        String durationUnitId = "";
        String durationtext = "";
        String dosage = "";
        String instructions = "";
        String drugCode = "";
        String doctorId = "";
        String hospitalId = "";
        String locationId = "";
        String patientId = "";
        List<GenericName> genericNames = null;
        switch (selectDrugItemType) {
            case ALL_DRUGS:
                DrugsListSolrResponse drugsSolr = (DrugsListSolrResponse) object;
                drugId = drugsSolr.getUniqueId();
                drugName = drugsSolr.getDrugName();
                drugType = drugsSolr.getDrugType();
                drugTypeId = drugsSolr.getDrugTypeId();
                drugCode = drugsSolr.getDrugCode();
                doctorId = drugsSolr.getDoctorId();
                hospitalId = drugsSolr.getHospitalId();
                locationId = drugsSolr.getLocationId();
                if (!Util.isNullOrEmptyList(drugsSolr.getDirection()))
                    directionId = drugsSolr.getDirection().get(0).getUniqueId();
                if (drugsSolr.getDuration() != null && drugsSolr.getDuration().getDurationUnit() != null) {
                    durationtext = drugsSolr.getDuration().getValue();
                    durationUnitId = drugsSolr.getDuration().getDurationUnit().getUniqueId();
                }
                dosage = drugsSolr.getDosage();
                genericNames = drugsSolr.getGenericNames();
                break;
        }

        selectedDrug.setDrugName(drugName);
        selectedDrug.setUniqueId(drugId);
        selectedDrug.setDoctorId(doctorId);
        selectedDrug.setHospitalId(hospitalId);
        selectedDrug.setLocationId(locationId);
        selectedDrug.setGenericNames(genericNames);

        DrugType drugTypeObj = new DrugType();
        drugTypeObj.setUniqueId(drugTypeId);
        drugTypeObj.setType(drugType);
        selectedDrug.setDrugType(drugTypeObj);

        selectedDrug.setDrugId(drugId);
        selectedDrug.setDrugCode(drugCode);
        selectedDrug.setDosage(dosage);
        selectedDrug.setDirection(getDirectionsListFromLocal(directionId));
        selectedDrug.setDuration(getDurationAndUnit(durationtext, durationUnitId));
        selectedDrug.setInstructions(instructions);
        if (selectedDrug != null) {
            addDrug(selectedDrug);
        }
    }

    public void addDrug(Drug drug) {
        if (drug != null) {
            drugsListHashMap.put(drug.getUniqueId(), drug);
//            notifyAdapter(new ArrayList<Drug>(drugsListHashMap.values()));
        }
    }

    /**
     * Any change in this method,change should also be done in AddNewPrescription,AddDrugDetailFragment's getDurationAndUnit() method
     */
    private Duration getDurationAndUnit(String durationText, String durationUnitId) {
        Duration duration = new Duration();
        duration.setValue(durationText);
        duration.setDurationUnit(LocalDataServiceImpl.getInstance(mApp).getDurationUnit(durationUnitId));
        return duration;
    }

    /**
     * Any change in this method,change should also be done in AddNewPrescription,AddDrugDetailFragment's getDirectionsListFromLocal() method
     */
    private List<DrugDirection> getDirectionsListFromLocal(String directionId) {
        List<DrugDirection> list = new ArrayList<>();
        DrugDirection direction = LocalDataServiceImpl.getInstance(mApp).getDrugDirection(directionId);
        if (direction != null)
            list.add(direction);
        return list;
    }

    @Override
    public void onDeleteItemClicked(Drug drug) {

    }

    @Override
    public void onDrugItemClicked(Drug drug) {

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
        addTemplate(template);
    }

    private void addTemplate(TempTemplate template) {

    }

    @Override
    public boolean isFromSettingsScreen() {
        return false;
    }
}
