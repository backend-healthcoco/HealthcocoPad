package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.adapter.SelectedTemplateDrugItemsListAdapter;
import com.healthcoco.healthcocopad.bean.DrugInteractions;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.PrescriptionRequest;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewPrescriptionListener;
import com.healthcoco.healthcocopad.listeners.DiagnosticTestItemListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectDrugItemClickListener;
import com.healthcoco.healthcocopad.listeners.SelectedDrugsListItemListener;
import com.healthcoco.healthcocopad.listeners.TemplateListItemListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.FontAwesomeButton;
import com.healthcoco.healthcocopad.views.ScrollViewWithHeaderNewPrescriptionLayout;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Shreshtha on 02-03-2017.
 */
public class AddEditNormalVisitPrescriptionFragment extends HealthCocoFragment implements
        TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, View.OnClickListener,
        SelectDrugItemClickListener, SelectedDrugsListItemListener, TemplateListItemListener,
        DiagnosticTestItemListener, AddNewPrescriptionListener, LocalDoInBackgroundListenerOptimised {

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
    private RelativeLayout parentLayoutDrugList;
    private LinearLayout parentLayoutDrugs;
    private LinearLayout containerAdviceSuggestionsList;
    private AddVisitSuggestionsFragment addVisitSuggestionsFragment;
    private EditText editAdvice;
    private LinearLayout containerDiagnosticTests;
    private ScrollViewWithHeaderNewPrescriptionLayout svContainer;
    private RelativeLayout tvHeader;
    private LinearLayout tvHeaderOne;
    private RelativeLayout tvHeaderTwo;
    private LinearLayout parentDiagnosticTestList;
    private LinearLayout parentDrugsList;
    private boolean hidePatientDetailHeader;
    private LinearLayout layoutPatientImageGender;
    private String prescriptionId;
    private TextView tvNoDrugLabselected;
    private boolean isDiagnosticsListPresent;
    private boolean isDrugItemPresent;
    private Button btHeaderInteraction;
    private Button btHeaderTwoInteraction;
    private SelectedDrugItemsListFragment selectedDrugItemsListFragment;
    private Prescription selectedPrescription;
    private LinkedHashMap<String, DiagnosticTest> selectedDiagnosticTestsList = new LinkedHashMap<>();

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
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initScrollView();
        initTabsFragmentsList();
        initViewPagerAdapter();
        initSuggestionsFragment();
        initSelectedDrugsListFragment();
    }

    private void initSelectedDrugsListFragment() {
        selectedDrugItemsListFragment = new SelectedDrugItemsListFragment(this);
        mFragmentManager.beginTransaction().add(R.id.layout_selected_drugs_list_fragment, selectedDrugItemsListFragment, selectedDrugItemsListFragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        etDuration = (EditText) view.findViewById(R.id.et_duration);
        editAdvice = (EditText) view.findViewById(R.id.edit_advice);
        etTemplateName = (EditText) view.findViewById(R.id.et_template_name);
        svContainer = (ScrollViewWithHeaderNewPrescriptionLayout) view.findViewById(R.id.sv_container);
        tvHeader = (RelativeLayout) view.findViewById(R.id.tv_header);
        tvHeaderOne = (LinearLayout) view.findViewById(R.id.tv_header_one);
        tvHeaderTwo = (RelativeLayout) view.findViewById(R.id.tv_header_two);
        parentLayoutDrugList = (RelativeLayout) view.findViewById(R.id.parent_layout_drugList);
        containerDiagnosticTests = (LinearLayout) view.findViewById(R.id.container_diagnostic_tests);
        parentDiagnosticTestList = (LinearLayout) view.findViewById(R.id.parent_diagnostic_tests_list);
        parentDrugsList = (LinearLayout) view.findViewById(R.id.parent_drugs_list);
        containerAdviceSuggestionsList = (LinearLayout) view.findViewById(R.id.container_advice_suggestions_list);
        btHeaderInteraction = (FontAwesomeButton) tvHeader.findViewById(R.id.bt_header_interaction);
        btHeaderTwoInteraction = (FontAwesomeButton) tvHeaderTwo.findViewById(R.id.bt_header_two_interaction);
        tvNoDrugLabselected = (TextView) view.findViewById(R.id.tv_no_drug_lab_selected);
    }

    private void initSuggestionsFragment() {
        addVisitSuggestionsFragment = new AddVisitSuggestionsFragment();
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.add(R.id.container_suggestions_list, addVisitSuggestionsFragment, addVisitSuggestionsFragment.getClass().getSimpleName());
        transaction.commit();
//        addVisitSuggestionsFragment.refreshTagOfEditText(SuggestionType.ADVICE);
    }

    @Override
    public void initListeners() {
        tabhost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);
        editAdvice.setOnClickListener(this);
        btHeaderTwoInteraction.setOnClickListener(this);
        btHeaderInteraction.setOnClickListener(this);
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

    private void initData() {
        initSelectedDrugsListFragment();
        if (selectedPrescription != null)
            notifyDiagnosticsList(Prescription.getDiagnosticTestsList(selectedPrescription.getDiagnosticTests()));
        else notifyDiagnosticsList(null);
    }

    private void notifyDiagnosticsList(ArrayList<DiagnosticTest> diagnosticTests) {
        containerDiagnosticTests.removeAllViews();
        if (!Util.isNullOrEmptyList(diagnosticTests)) {
            tvNoDrugLabselected.setVisibility(View.GONE);
            isDiagnosticsListPresent = true;
            parentDiagnosticTestList.setVisibility(View.VISIBLE);
            svContainer.setHeaderVisiblilty(tvHeaderOne, true);
            for (DiagnosticTest diagnosticTest : diagnosticTests) {
                LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.item_selected_diagnostic, null);
                TextView tvTestName = (TextView) linearLayout.findViewById(R.id.tv_test_name);
                ImageButton btDeleteDiagnosticTest = (ImageButton) linearLayout.findViewById(R.id.bt_delete_diagnostic_test);
                tvTestName.setText(Util.getValidatedValue(diagnosticTest.getTestName()));
                btDeleteDiagnosticTest.setTag(diagnosticTest);
                btDeleteDiagnosticTest.setOnClickListener(this);
                containerDiagnosticTests.addView(linearLayout);
            }
        } else {
            if (!isDrugItemPresent)
                tvNoDrugLabselected.setVisibility(View.VISIBLE);
            parentDiagnosticTestList.setVisibility(View.GONE);
            svContainer.setHeaderVisiblilty(tvHeaderOne, false);
            if (selectedDrugItemsListFragment != null)
                selectedDrugItemsListFragment.notifyList();
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
            case R.id.bt_delete_diagnostic_test:
                confirmDeleteDrugAlert(v, R.string.confirm_remove_diagnostic_test);
                break;
            case R.id.bt_header_interaction:
            case R.id.bt_header_two_interaction:
                confirmDrugInteractionsAlert();
                LogUtils.LOGD(TAG, "Interactions Clicked");
                break;
            default:
                break;
        }
    }

    private void confirmDeleteDrugAlert(final View view, final int messageId) {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(messageId);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    switch (view.getId()) {
                        case R.id.bt_delete_diagnostic_test:
                            DiagnosticTest diagnosticTest = (DiagnosticTest) view.getTag();
                            selectedDiagnosticTestsList.remove(diagnosticTest.getUniqueId());
                            notifyDiagnosticsList(new ArrayList<DiagnosticTest>(selectedDiagnosticTestsList.values()));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    private void confirmDrugInteractionsAlert() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(R.string.confirm);
        alertBuilder.setMessage(getResources().getString(
                R.string.confirm_drug_interaction));
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                getDrugInteractionsResponse();
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    private void getDrugInteractionsResponse() {
        mActivity.showLoading(true);
        WebDataServiceImpl.getInstance(mApp).getDrugsInteractionResponse(DrugInteractions.class, selectedPatient.getUserId(), selectedDrugItemsListFragment.getDrugInteractionRequestsList(), this, this);
    }

    public int getBlankPrescriptionMsg() {
        int msgId = R.string.alert_add_drugs;
        if (!Util.isNullOrEmptyList(selectedDrugItemsListFragment.getModifiedDrugsList()) || !Util.isNullOrEmptyList(selectedDiagnosticTestsList))
            return 0;
        return msgId;
    }

    private void addPrescription() {
        mActivity.showLoading(false);
        PrescriptionRequest prescription = getPrescriptionRequestDetails();
//        if (Util.isNullOrBlank(prescriptionId))
//            prescription.setVisitId(Util.getVisitId(VisitIdType.PRESCRIPTION));
//        WebDataServiceImpl.getInstance(mApp).addPrescription(Prescription.class, prescription, this, this);
    }

    public PrescriptionRequest getPrescriptionRequestDetails() {
        PrescriptionRequest prescription = new PrescriptionRequest();
        LogUtils.LOGD(TAG, "Selected patient " + selectedPatient.getLocalPatientName());
        prescription.setPatientId(selectedPatient.getUserId());
        prescription.setDoctorId(user.getUniqueId());
        prescription.setLocationId(user.getForeignLocationId());
        prescription.setHospitalId(user.getForeignHospitalId());
        prescription.setItems(selectedDrugItemsListFragment.getModifiedDrugsList());
        prescription.setDiagnosticTests(getLabTestsList());
        if (!Util.isNullOrBlank(prescriptionId))
            prescription.setUniqueId(prescriptionId);
        return prescription;
    }

    @Override
    public void ondrugItemClick(SelectDrugItemType drugItemType, Object object) {
        System.out.println("drug" + drugItemType);
        selectedDrugItemsListFragment.addSelectedDrug(drugItemType, object);
    }

    public List<DiagnosticTest> getLabTestsList() {
        if (!Util.isNullOrEmptyList(selectedDiagnosticTestsList))
            return new ArrayList<>(selectedDiagnosticTestsList.values());
        return null;
    }

    @Override
    public void setDrugsListparentVisibility(boolean isVisible) {
        if (isVisible) {
            isDrugItemPresent = true;
            tvNoDrugLabselected.setVisibility(View.GONE);
            parentDrugsList.setVisibility(View.VISIBLE);
            svContainer.setHeaderVisiblilty(tvHeaderTwo, true);
        } else {
            isDrugItemPresent = false;
            parentDrugsList.setVisibility(View.GONE);
            svContainer.setHeaderVisiblilty(tvHeaderTwo, false);
            if (!isDiagnosticsListPresent) {
                tvNoDrugLabselected.setVisibility(View.VISIBLE);
                svContainer.showHeader(false);
            }
        }
    }

    @Override
    public void onDeleteItemClicked(DrugItem drug) {
        System.out.println("onDeleteItemClicked" + drug);
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
        System.out.println("onPrescriptionClicked" + template);
    }

    @Override
    public void onEditClicked(TempTemplate template) {
        System.out.println("onEditClicked" + template);
    }

    @Override
    public void onDeleteClicked(TempTemplate template) {
        System.out.println("onDeleteClicked" + template);
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
    public void onAddClicked(DiagnosticTest diagnosticTest) {
        System.out.println("onAddClicked" + diagnosticTest);
    }

    @Override
    public void onAddedClicked(DiagnosticTest diagnosticTest) {
        System.out.println("onAddedClicked" + diagnosticTest);
    }

    @Override
    public DiagnosticTest getDiagnosticTest(String uniqueId) {
        return null;
    }

    @Override
    public void onDeleteItemClicked(DiagnosticTest diagnosticTest) {
        System.out.println("onDeleteItemClicked" + diagnosticTest);
    }

    @Override
    public void onDiagnosticTestClicked(DiagnosticTest diagnosticTest) {
        System.out.println("onDiagnosticTestClicked" + diagnosticTest);
        selectedDiagnosticTestsList.put(diagnosticTest.getUniqueId(), diagnosticTest);
        notifyDiagnosticsList(new ArrayList<DiagnosticTest>(selectedDiagnosticTestsList.values()));
        selectedDrugItemsListFragment.notifyList();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null)
                    user = doctor.getUser();
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                if (!Util.isNullOrBlank(prescriptionId))
                    selectedPrescription = LocalDataServiceImpl.getInstance(mApp).getPrescription(prescriptionId);
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
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = null;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        } else {
            errorMsg = getResources().getString(R.string.error);
        }
        Util.showToast(mActivity, errorMsg);
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case FRAGMENT_INITIALISATION:
                if (user != null && selectedPatient != null) {
                    initData();
                }
                break;
            case ADD_PRESCRIPTION:
            case UPDATE_PRESCRIPTION:
//                if (response.getData() != null && response.getData() instanceof Prescription) {
//                    Prescription prescription = (Prescription) response.getData();
//                    LocalDataServiceImpl.getInstance(mApp).addPrescription(prescription);
//                    Util.setVisitId(VisitIdType.PRESCRIPTION, prescription.getVisitId());
//                    sendBroadcasts(prescription.getUniqueId());
//                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_PRESCIPTION, null);
//                    ((CommonOpenUpActivity) mActivity).finish();
//                }
                break;
            case GET_DRUG_INTERACTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    showDrugInteractionsAlert((ArrayList<DrugInteractions>) (ArrayList<?>) response.getDataList());
                else
                    Util.showAlert(mActivity, R.string.title_no_interactions_found, R.string.msg_no_interactions_found);
                break;
            default:
                break;
        }
        mActivity.hideLoading();
    }

    private void showDrugInteractionsAlert(ArrayList<DrugInteractions> interactionsList) {
        String formattedString = "";
        for (DrugInteractions drugInteractions :
                interactionsList) {
            formattedString = formattedString + drugInteractions.getText() + "\n";
        }
        Util.showAlert(mActivity, formattedString);
    }
}
