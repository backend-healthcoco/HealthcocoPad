package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.adapter.SelectedTemplateDrugItemsListAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.Duration;
import com.healthcoco.healthcocopad.bean.server.GenericName;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewPrescriptionListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectDrugItemClickListener;
import com.healthcoco.healthcocopad.listeners.SelectedDrugsListItemListener;
import com.healthcoco.healthcocopad.listeners.TemplateListItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.FontAwesomeButton;
import com.healthcoco.healthcocopad.views.ScrollViewWithHeaderNewPrescriptionLayout;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import static com.healthcoco.healthcocopad.fragments.AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_DATA;

/**
 * Created by Shreshtha on 27-03-2017.
 */
public class AddNewTemplateFragment extends HealthCocoFragment implements TabHost.OnTabChangeListener,
        ViewPager.OnPageChangeListener, View.OnClickListener, SelectDrugItemClickListener,
        SelectedDrugsListItemListener, TemplateListItemListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised, AddNewPrescriptionListener, HealthcocoTextWatcherListener {

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
    private LinkedHashMap<String, DrugItem> drugsListHashMap = new LinkedHashMap<>();
    private SelectedTemplateDrugItemsListAdapter adapter;
    private ListView lvTemplates;
    private Prescription prescription;
    private List<Prescription> prescriptionList;
    private RelativeLayout tvHeader;
    private LinearLayout tvHeaderOne;
    private RelativeLayout tvHeaderTwo;
    private Button btHeaderInteraction;
    private Button btHeaderTwoInteraction;
    private ScrollViewWithHeaderNewPrescriptionLayout svContainer;
    private ImageButton btClear;
    private TextView tvNoDrugLabselected;
    private EditText etHeaderTwoDuration;
    private String templateId;
    private TempTemplate template;
    private SelectedDrugItemsListFragment selectedDrugItemsListFragment;

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
        Intent intent = mActivity.getIntent();
        templateId = intent.getStringExtra(HealthCocoConstants.TAG_TEMPLATE_ID);
        if (!Util.isNullOrBlank(templateId))
            initData(templateId);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(TAG_PRESCRIPTION_DATA)) {
            prescriptionList = Parcels.unwrap(bundle.getParcelable(TAG_PRESCRIPTION_DATA));
        }
        showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initData(templateId);
        initTabsFragmentsList();
        initViewPagerAdapter();
        initScrollView();
    }

    private void initData(String templateId) {
//        template = LocalDataServiceImpl.getInstance(mApp).getTemplate(templateId);
//        if (!Util.isNullOrBlank(template.getName()))
//            etTemplateName.setText(template.getName());
        initSelectedDrugsListFragment();
        prePopulateVisitDetails(prescriptionList);
    }

    private void initSelectedDrugsListFragment() {
        Bundle bundle = new Bundle();
        selectedDrugItemsListFragment = new SelectedDrugItemsListFragment(this);
        bundle.putParcelable(AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_DATA, Parcels.wrap(prescriptionList));
        selectedDrugItemsListFragment.setArguments(bundle);
        mFragmentManager.beginTransaction().add(R.id.layout_selected_drugs_list_fragment, selectedDrugItemsListFragment, selectedDrugItemsListFragment.getClass().getSimpleName()).commit();
    }

//    private void initAdapters() {
//        adapter = new SelectedTemplateDrugItemsListAdapter(mActivity, this);
//        lvTemplates.setAdapter(adapter);
//    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        etDuration = (EditText) view.findViewById(R.id.et_duration);
        etTemplateName = (EditText) view.findViewById(R.id.et_template_name);
        svContainer = (ScrollViewWithHeaderNewPrescriptionLayout) view.findViewById(R.id.sv_container);
        tvHeader = (RelativeLayout) view.findViewById(R.id.tv_header);
        tvHeaderOne = (LinearLayout) view.findViewById(R.id.tv_header_one);
        tvHeaderTwo = (RelativeLayout) view.findViewById(R.id.tv_header_two);
        btHeaderInteraction = (FontAwesomeButton) tvHeader.findViewById(R.id.bt_header_interaction);
        btHeaderTwoInteraction = (FontAwesomeButton) tvHeaderTwo.findViewById(R.id.bt_header_two_interaction);
        tvNoDrugLabselected = (TextView) view.findViewById(R.id.tv_no_drug_lab_selected);
        etHeaderTwoDuration = (EditText) view.findViewById(R.id.et_header_two_duration);
    }

    @Override
    public void initListeners() {
        tabhost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);
        btHeaderTwoInteraction.setOnClickListener(this);
        btHeaderInteraction.setOnClickListener(this);
        etDuration.addTextChangedListener(new HealthcocoTextWatcher(etDuration, this));
        etHeaderTwoDuration.addTextChangedListener(new HealthcocoTextWatcher(etHeaderTwoDuration, this));
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initScrollView() {
        svContainer.addFixedHeader(tvHeader);
        svContainer.addChildHeaders(tvHeaderOne);
        svContainer.addChildHeaders(tvHeaderTwo);
        svContainer.build(mActivity, this);
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

    public void prePopulateVisitDetails(List<Prescription> prescriptionList) {
        if (!Util.isNullOrEmptyList(prescriptionList)) {
            for (Prescription prescription :
                    prescriptionList) {
                //initialising DiagnosticTests(LabTests)
//                if (!Util.isNullOrEmptyList(prescription.getDiagnosticTests())) {
//                    notifyDiagnosticsList(prescription.getDiagnosticTestsList(prescription.getDiagnosticTests()));
//                }
//
//                if (!Util.isNullOrBlank(prescription.getAdvice())) {
//                    editAdvice.setText(prescription.getAdvice());
//                }
//                if (!isFromClone)
//                    prescriptionId = prescription.getUniqueId();
            }
        }
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
        switch (v.getId()) {
            case R.id.container_right_action:
                validateData();
                break;
        }
    }

    private void validateData() {
        String msg = null;
        EditText selectedEditText = null;
        String templateName = Util.getValidatedValueOrNull(etTemplateName);
        if (Util.isNullOrBlank(templateName)) {
            selectedEditText = etTemplateName;
            msg = getResources().getString(R.string.please_enter_template_name);
        } else if (Util.isNullOrEmptyList(drugsListHashMap)) {
            msg = getResources().getString(R.string.please_add_drugs_for_template) + "\"" + templateName + "\"";
        }
        if (!Util.isNullOrBlank(msg)) {
            Util.showErrorOnEditText(selectedEditText);
            Util.showToast(mActivity, msg);
        } else {
            addTemplate(templateName);
        }
    }

    private void addTemplate(String templateName) {
        TempTemplate template = new TempTemplate();
        template.setDoctorId(user.getUniqueId());
        template.setLocationId(user.getForeignLocationId());
        template.setHospitalId(user.getForeignHospitalId());
        template.setName(templateName);
        template.setUniqueId(templateId);
        template.setItems(selectedDrugItemsListFragment.getModifiedDrugsList());

        if (!Util.isNullOrBlank(template.getUniqueId())) {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).updateTemplate(TempTemplate.class, template, this, this);
        } else {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).addTempLate(TempTemplate.class, template, this, this);
        }
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = errorMessage;
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case ADD_TEMPLATE:
                Util.showToast(mActivity, String.format(getResources().getString(R.string.success_template_added), Util.getValidatedValueOrBlankWithoutTrimming(etTemplateName)));
                if (response.isValidData(response) && response.getData() instanceof TempTemplate) {
                    TempTemplate template = (TempTemplate) response.getData();
                    LocalDataServiceImpl.getInstance(mApp).addTemplate(template);
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_NEW_TEMPLATE, new Intent().putExtra(TemplateListFragment.TAG_TEMPLATE_ID, template.getUniqueId()));
                    ((CommonOpenUpActivity) mActivity).finish();
                }
                break;
            case UPDATE_TEMPLATE:
                Util.showToast(mActivity, String.format(getResources().getString(R.string.success_template_updated), Util.getValidatedValueOrBlankWithoutTrimming(etTemplateName)));
                if (response.isValidData(response) && response.getData() instanceof TempTemplate) {
                    TempTemplate template = (TempTemplate) response.getData();
                    LocalDataServiceImpl.getInstance(mApp).addTemplate(template);
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_NEW_TEMPLATE, new Intent().putExtra(TemplateListFragment.TAG_TEMPLATE_ID, template.getUniqueId()));
                    ((CommonOpenUpActivity) mActivity).finish();
                }
                break;
        }
        mActivity.hideLoading();
    }

    private List<DrugItem> getDrugsItemList() {
        if (!Util.isNullOrEmptyList(drugsListHashMap)) {
            List<DrugItem> modifiedList = new ArrayList<DrugItem>(drugsListHashMap.values());
            for (DrugItem drugItem :
                    modifiedList) {
                if (drugItem.getDrug() != null) {
                    drugItem.setDrugId(drugItem.getDrug().getUniqueId());
                    drugItem.setDrug(null);
                }
            }
            return modifiedList;
        }
        return null;
    }

    @Override
    public void ondrugItemClick(SelectDrugItemType drugItemType, Object object) {
        System.out.println("drug" + drugItemType);
        selectedDrugItemsListFragment.addSelectedDrug(drugItemType, object);
    }

    private void notifyAdapter(ArrayList<DrugItem> drugsListMap) {
        adapter.setListData(drugsListMap);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setDrugsListparentVisibility(boolean isVisible) {

    }

    @Override
    public void onDeleteItemClicked(DrugItem drug) {

    }

    @Override
    public void onDrugItemClicked(DrugItem drug) {

    }

    @Override
    public String getDurationUnit() {
        return null;
    }

    @Override
    public boolean isDurationSet() {
        return false;
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
        System.out.println("onItemClicked" + template);
        selectedDrugItemsListFragment.addDrugsList(template.getItems());
    }

    @Override
    public boolean isFromSettingsScreen() {
        return false;
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
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void afterTextChange(View v, String s) {
        if (v instanceof EditText) {
            if (v.getId() == R.id.et_duration || (v.getId() == R.id.et_header_two_duration)) {
                setDurationUnitToAll(s);
            }
        }
    }

    private void setDurationUnitToAll(String unit) {
        selectedDrugItemsListFragment.modifyDurationUnit(unit);
    }
}
