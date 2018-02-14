package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.DrugInteractions;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Prescription;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewPrescriptionListener;
import com.healthcoco.healthcocopad.listeners.AddTemplatesListener;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectDrugItemClickListener;
import com.healthcoco.healthcocopad.listeners.SelectedDrugsListItemListener;
import com.healthcoco.healthcocopad.listeners.TemplateListItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.FontAwesomeButton;
import com.healthcoco.healthcocopad.views.ScrollViewWithHeaderNewPrescriptionLayout;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.healthcoco.healthcocopad.fragments.AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_DATA;

/**
 * Created by Shreshtha on 27-03-2017.
 */
public class AddNewTemplateFragment extends HealthCocoFragment implements TabHost.OnTabChangeListener,
        ViewPager.OnPageChangeListener, View.OnClickListener, SelectDrugItemClickListener,
        SelectedDrugsListItemListener, AddTemplatesListener, GsonRequest.ErrorListener,
        Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised,
        AddNewPrescriptionListener, HealthcocoTextWatcherListener {

    private ViewPager viewPager;
    private TabHost tabhost;
    private ArrayList<Fragment> fragmentsList;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TemplateListFragment templateListFragment;
    private DrugListFragment drugListFragment;
    private EditText etDuration;
    private EditText etTemplateName;
    private List<Prescription> prescriptionList;
    private RelativeLayout tvHeader;
    //    private LinearLayout tvHeaderOne;
    private RelativeLayout tvHeaderTwo;
    private Button btHeaderInteraction;
    private Button btHeaderTwoInteraction;
    private ScrollViewWithHeaderNewPrescriptionLayout svContainer;
    private TextView tvNoDrugLabselected;
    private EditText etHeaderTwoDuration;
    private String templateId;
    private TempTemplate template;
    private SelectedDrugItemsListFragment selectedDrugItemsListFragment;
    private LinearLayout parentDrugsList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragemnt_add_new_template, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = mActivity.getIntent();
        templateId = intent.getStringExtra(HealthCocoConstants.TAG_TEMPLATE_ID);
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(TAG_PRESCRIPTION_DATA)) {
            prescriptionList = Parcels.unwrap(bundle.getParcelable(TAG_PRESCRIPTION_DATA));
        }
        init();
        showLoadingOverlay(true);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initTabsFragmentsList();
        initViewPagerAdapter();
        if (!Util.isNullOrBlank(templateId))
            initData(templateId);
        initScrollView();
        initSelectedDrugsListFragment();
    }

    private void initData(String templateId) {
        template = LocalDataServiceImpl.getInstance(mApp).getTemplate(templateId);
        if (!Util.isNullOrBlank(template.getName()))
            etTemplateName.setText(template.getName());
    }

    private void initSelectedDrugsListFragment() {
        Bundle bundle = new Bundle();
        selectedDrugItemsListFragment = new SelectedDrugItemsListFragment(this);
        bundle.putParcelable(AddEditNormalVisitPrescriptionFragment.TAG_PRESCRIPTION_DATA, Parcels.wrap(prescriptionList));
        selectedDrugItemsListFragment.setArguments(bundle);
        mFragmentManager.beginTransaction().add(R.id.layout_selected_drugs_list_fragment, selectedDrugItemsListFragment, selectedDrugItemsListFragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        etDuration = (EditText) view.findViewById(R.id.et_duration);
        etTemplateName = (EditText) view.findViewById(R.id.et_template_name);
        svContainer = (ScrollViewWithHeaderNewPrescriptionLayout) view.findViewById(R.id.sv_container);
        tvHeader = (RelativeLayout) view.findViewById(R.id.tv_header);
//        tvHeaderOne = (LinearLayout) view.findViewById(R.id.tv_header_one);
        tvHeaderTwo = (RelativeLayout) view.findViewById(R.id.tv_header_two);
        btHeaderInteraction = (FontAwesomeButton) tvHeader.findViewById(R.id.bt_header_interaction);
        btHeaderTwoInteraction = (FontAwesomeButton) tvHeaderTwo.findViewById(R.id.bt_header_two_interaction);
        tvNoDrugLabselected = (TextView) view.findViewById(R.id.tv_no_drug_lab_selected);
        etHeaderTwoDuration = (EditText) view.findViewById(R.id.et_header_two_duration);
        parentDrugsList = (LinearLayout) view.findViewById(R.id.parent_drugs_list);
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
//        svContainer.addChildHeaders(tvHeaderOne);
        svContainer.addChildHeaders(tvHeaderTwo);
        svContainer.build(mActivity, this);
        tvHeader.setVisibility(View.GONE);
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
        switch (v.getId()) {
            case R.id.container_right_action:
                validateData();
                break;
            case R.id.bt_header_interaction:
            case R.id.bt_header_two_interaction:
                confirmDrugInteractionsAlert();
                LogUtils.LOGD(TAG, "Interactions Clicked");
                break;
        }
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

    private void validateData() {
        String msg = null;
        EditText selectedEditText = null;
        String templateName = Util.getValidatedValueOrNull(etTemplateName);
        if (Util.isNullOrBlank(templateName)) {
            selectedEditText = etTemplateName;
            msg = getResources().getString(R.string.please_enter_template_name);
        } else if (Util.isNullOrEmptyList(selectedDrugItemsListFragment.getDrugsList())) {
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
            case GET_DRUG_INTERACTIONS:
                if (!Util.isNullOrEmptyList(response.getDataList()))
                    showDrugInteractionsAlert((ArrayList<DrugInteractions>) (ArrayList<?>) response.getDataList());
                else
                    Util.showAlert(mActivity, R.string.title_no_interactions_found, R.string.msg_no_interactions_found);
                break;
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

    private void showDrugInteractionsAlert(ArrayList<DrugInteractions> interactionsList) {
        String formattedString = "";
        for (DrugInteractions drugInteractions :
                interactionsList) {
            formattedString = formattedString + drugInteractions.getText() + "\n";
        }
        Util.showAlert(mActivity, formattedString);
    }

    @Override
    public void ondrugItemClick(SelectDrugItemType drugItemType, Object object) {
        System.out.println("drug" + drugItemType);
        selectedDrugItemsListFragment.addSelectedDrug(drugItemType, object);
        if (object != null && selectedDrugItemsListFragment != null) {
            svContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    svContainer.removeOnLayoutChangeListener(this);
                    Log.d(TAG, "list got updated, do what ever u want");
                    svContainer.requestChildFocus(selectedDrugItemsListFragment.getLastChildView(), selectedDrugItemsListFragment.getLastChildView());

                }
            });
        }
    }

    @Override
    public void setDrugsListparentVisibility(boolean isVisible) {
        if (isVisible) {
            tvNoDrugLabselected.setVisibility(View.GONE);
            parentDrugsList.setVisibility(View.VISIBLE);
            svContainer.setHeaderVisiblilty(tvHeaderTwo, true);
        } else {
            parentDrugsList.setVisibility(View.GONE);
            svContainer.setHeaderVisiblilty(tvHeaderTwo, false);
        }
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
    public void onItemClicked(TempTemplate template) {
        System.out.println("onItemClicked" + template);
        selectedDrugItemsListFragment.addDrugsList(template.getItems());
        if (template != null && selectedDrugItemsListFragment != null) {
            svContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    svContainer.removeOnLayoutChangeListener(this);
                    Log.d(TAG, "list got updated, do what ever u want");
                    svContainer.requestChildFocus(selectedDrugItemsListFragment.getLastChildView(), selectedDrugItemsListFragment.getLastChildView());

                }
            });
        }
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
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
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
