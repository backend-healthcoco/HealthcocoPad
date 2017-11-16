package com.healthcoco.healthcocopad.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.TreatmentRequest;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Quantity;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.UnitValue;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.QuantityEnum;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;
import com.healthcoco.healthcocopad.enums.UnitType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectDrugItemClickListener;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ScrollViewWithHeaderNewPrescriptionLayout;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Aai on 09/11/2017.
 */


public class AddNewTreatmentFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised,
        View.OnClickListener, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, SelectDrugItemClickListener, SelectedTreatmentItemClickListener {
    public static final String INTENT_GET_MODIFIED_VALUE = "com.healthcoco.MODIFIED_VALUE";
    public static final String TAG_SELECTED_TREATMENT_OBJECT = "selectedTreatmentItemOrdinal";

    private ViewPager viewPager;
    private TabHost tabhost;
    private ArrayList<Fragment> fragmentsList;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvTotalCost;
    private TextView tvTotalDiscount;
    private TextView tvGrandTotal;
    private SelectedTreatmentsListFragment selectedTreatmentsListFragment;
    private boolean receiversRegistered;
    private Treatments treatment;
    BroadcastReceiver getModifiedValueOfTreatmentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            setModifiedValues();
        }
    };
    private DoctorProfile doctorProfile;
    private TreatmentListFragment treatmentListFragment;
    private TreatmentCustomListFragment customListFragment;
    private ScrollViewWithHeaderNewPrescriptionLayout svContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_treatment, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();

    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        tvTotalCost = (TextView) view.findViewById(R.id.tv_total_cost);
        tvTotalDiscount = (TextView) view.findViewById(R.id.tv_total_discount);
        tvGrandTotal = (TextView) view.findViewById(R.id.tv_grand_total);
        svContainer = (ScrollViewWithHeaderNewPrescriptionLayout) view.findViewById(R.id.sv_container);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initSaveButton(this);
        tabhost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);
    }

    private void initTabsFragmentsList() {
        tabhost.setup();
        fragmentsList = new ArrayList<>();

        // init fragment 1
        treatmentListFragment = new TreatmentListFragment(this);
        addFragment(treatmentListFragment, R.string.all, false);

//        if (doctorProfile.getSpecialities().contains("Dentist")) {
//        init fragment 2
//            customListFragment = new TreatmentCustomListFragment(this);
//            addFragment(customListFragment, R.string.featured, true);
//        } else {
//            tabhost.getTabWidget().setVisibility(View.GONE);
//
//        }
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
        initSelectedTreatmentsListFragment();
        initIntentData();
    }

    private void initIntentData() {
        Intent intent = mActivity.getIntent();
        treatment = Parcels.unwrap(intent.getParcelableExtra(PatientTreatmentDetailFragment.TAG_TREATMENT_DATA));
    }

    private void initDefaultData(Treatments treatment) {
        tvTotalCost.setText(Util.formatDoubleNumber(treatment.getTotalCost()));
        if (treatment.getTotalDiscount() != null)
            tvTotalDiscount.setText(Util.formatDoubleNumber(treatment.getTotalDiscount().getValue()));
        tvGrandTotal.setText(Util.formatDoubleNumber(treatment.getGrandTotal()));
    }

    private void initSelectedTreatmentsListFragment() {
        selectedTreatmentsListFragment = new SelectedTreatmentsListFragment(doctorProfile, this);
        mFragmentManager.beginTransaction().add(R.id.layout_selected_treatment_list_fragment, selectedTreatmentsListFragment, selectedTreatmentsListFragment.getClass().getSimpleName()).commit();
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
                doctorProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorProfileObject(user.getUniqueId());
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save:
                validateData();
                break;
            default:
                break;
        }
    }

    public void validateData() {
        int msgId = getBlankTreatmentMsg();
        if (msgId == 0) {
            setModifiedValues();
            addTreatment();
        } else {
            Util.showToast(mActivity, msgId);
        }
    }

    private void addTreatment() {
        TreatmentRequest treatmentRequest = new TreatmentRequest();
        treatmentRequest.setTreatments(selectedTreatmentsListFragment.getModifiedTreatmentsItemRequestList());
        treatmentRequest.setDoctorId(user.getUniqueId());
        treatmentRequest.setHospitalId(user.getForeignHospitalId());
        treatmentRequest.setPatientId(selectedPatient.getUserId());
        treatmentRequest.setLocationId(user.getForeignLocationId());
        treatmentRequest.setTotalCost(treatment.getTotalCost());
        treatmentRequest.setTotalDiscount(treatment.getTotalDiscount());
        treatmentRequest.setGrandTotal(treatment.getGrandTotal());
        treatmentRequest.setTotalDiscount(treatment.getTotalDiscount());
        if (treatment.getUniqueId() != null)
            treatmentRequest.setUniqueId(treatment.getUniqueId());


        if (treatment.getUniqueEmrId() != null)
            treatmentRequest.setUniqueEmrId(treatment.getUniqueEmrId());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addTreatment(Treatments.class, treatmentRequest, this, this);
    }

    public int getBlankTreatmentMsg() {
        int msgId = R.string.alert_add_treatment;
        if (!Util.isNullOrEmptyList(selectedTreatmentsListFragment.getModifiedTreatmentsItemList()))
            return 0;
        return msgId;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_ADD_TREATMENT) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_SELECTED_TREATMENT && data != null) {
                TreatmentService treatmentService = Parcels.unwrap(data.getParcelableExtra(TAG_SELECTED_TREATMENT_OBJECT));
                addTreatmentService(treatmentService);
            }
        }
    }

    private void addTreatmentService(TreatmentService treatmentService) {
        TreatmentItem treatmentItem = new TreatmentItem();
        treatmentItem.setTreatmentService(treatmentService);
        treatmentItem.setCost(treatmentService.getCost());
        Quantity quantity = new Quantity();
        quantity.setType(QuantityEnum.QTY);
        quantity.setValue(1);
        treatmentItem.setQuantity(quantity);
        treatmentItem.setFinalCost(treatmentService.getCost());
        treatmentItem.setCustomUniqueId(treatmentService.getUniqueId());
        treatmentItem.setTreatmentServiceId(treatmentService.getUniqueId());
        selectedTreatmentsListFragment.addTreatment(treatmentItem);
        Util.sendBroadcast(mApp, INTENT_GET_MODIFIED_VALUE);
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
                    initTabsFragmentsList();
                    initViewPagerAdapter();
//                    initActionPatientDetailActionBar(PatientProfileScreenType.IN_EMR_HEADER, view, selectedPatient);
                }
                break;
            case ADD_TREATMENT:
                if (response.getData() != null && response.getData() instanceof Treatments) {
                    Treatments treatment = (Treatments) response.getData();
                    LocalDataServiceImpl.getInstance(mApp).addTreatment(treatment);
                    Util.sendBroadcast(mApp, PatientTreatmentDetailFragment.INTENT_GET_TREATMENT_LIST_LOCAL);
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_NEW_TREATMENT, null);
                    ((CommonOpenUpActivity) mActivity).finish();
                }
                break;
            default:
                break;
        }
        mActivity.hideLoading();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!receiversRegistered) {
            //receiver for prescription list refresh from local
            IntentFilter filter2 = new IntentFilter();
            filter2.addAction(INTENT_GET_MODIFIED_VALUE);
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(getModifiedValueOfTreatmentReceiver, filter2);
            receiversRegistered = true;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(getModifiedValueOfTreatmentReceiver);
    }

    public void setModifiedValues() {
        selectedTreatmentsListFragment.modifyTreatmentsList();
        Double totalCost = selectedTreatmentsListFragment.getTotalCost();
        Double totalDiscount = selectedTreatmentsListFragment.getTotalDiscount();
        Double totalGrandTotal = selectedTreatmentsListFragment.getGrandTotalCost();
        if (treatment == null)
            treatment = new Treatments();
        //set Total Cost
        treatment.setTotalCost(totalCost);
        //set TotalDiscount Cost
        UnitValue unitTotalDiscount = new UnitValue();
        unitTotalDiscount.setValue(totalDiscount);
        unitTotalDiscount.setUnit(UnitType.INR);
        treatment.setTotalDiscount(unitTotalDiscount);
        //set GrandTotal Cost
        treatment.setGrandTotal(totalGrandTotal);
        initDefaultData(treatment);
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

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void ondrugItemClick(SelectDrugItemType drugItemType, Object object) {

    }

    @Override
    public void onTreatmentItemClick(Object object) {
        TreatmentService treatmentService = (TreatmentService) object;
        addTreatmentService(treatmentService);
        if (object != null && selectedTreatmentsListFragment != null) {
            svContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    svContainer.removeOnLayoutChangeListener(this);
                    Log.d(TAG, "list got updated, do what ever u want");
                    svContainer.requestChildFocus(selectedTreatmentsListFragment.getLastChildView(), selectedTreatmentsListFragment.getLastChildView());
                }
            });
        }
    }

    @Override
    public User getUser() {
        return user;
    }
}
