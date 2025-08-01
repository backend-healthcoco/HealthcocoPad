package com.healthcoco.healthcocopad.fragments;

import android.app.DatePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.ContactsDetailViewPagerAdapter;
import com.healthcoco.healthcocopad.bean.TotalTreatmentCostDiscountValues;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.InvoiceRequest;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.bean.server.DoctorClinicProfile;
import com.healthcoco.healthcocopad.bean.server.DoctorProfile;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.InvoiceItem;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Quantity;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.UnitValue;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.DummyTabFactory;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.InvoiceItemType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.QuantityEnum;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;
import com.healthcoco.healthcocopad.enums.UnitType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DiagnosticTestItemListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectDrugItemClickListener;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.ScrollViewWithHeaderNewPrescriptionLayout;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Prashant on 16/12/2017.
 */


public class AddInvoiceFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised,
        View.OnClickListener, TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener, SelectedTreatmentItemClickListener, SelectDrugItemClickListener, DiagnosticTestItemListener {
    public static final String INTENT_GET_MODIFIED_VALUE = "com.healthcoco.MODIFIED_VALUE";
    public static final String TAG_SELECTED_SERVICE_OBJECT = "selectedServiceItemOrdinal";
    public static final String TAG_INVOICE_ID = "invoiceId";
    private ViewPager viewPager;
    private TabHost tabhost;
    private ArrayList<Fragment> fragmentsList;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvTotalCost;
    private TextView tvTotalDiscount;
    private TextView tvGrandTotal;
    private TextView tvTotalTax;
    private LinearLayout btSave;
    private LinearLayout layoutNextReviewOn;
    private SelectedInvoiceListFragment selectedInvoiceListFragment;
    private boolean receiversRegistered;
    private Invoice invoice;
    private TextView tvDate;
    //    private Treatments treatment;
    private DoctorProfile doctorProfile;
    private TreatmentListFragment allTreatmentListFragment;
    private DiagnosticTestListFragment diagnosticTestListFragment;
    private DrugListFragment drugListFragment;
    private ScrollViewWithHeaderNewPrescriptionLayout svContainer;
    private boolean isFromVisit;
    private Boolean pidHasDate;
    private long selectedFromDateTimeMillis;
    private TreatmentCustomListFragment customTreatmentListFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_invoice, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = mActivity.getIntent();
        if (intent != null) {
            invoice = Parcels.unwrap(intent.getParcelableExtra(AddInvoiceFragment.TAG_INVOICE_ID));
//            treatment = Parcels.unwrap(intent.getParcelableExtra(PatientTreatmentDetailFragment.TAG_TREATMENT_DATA));
        }

        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initSelectedInvoiceListFragment();
    }

    @Override
    public void initViews() {
        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabhost = (TabHost) view.findViewById(android.R.id.tabhost);
        tvTotalCost = (TextView) view.findViewById(R.id.tv_total_cost);
        tvTotalDiscount = (TextView) view.findViewById(R.id.tv_total_discount);
        tvGrandTotal = (TextView) view.findViewById(R.id.tv_grand_total);
        tvTotalTax = (TextView) view.findViewById(R.id.tv_total_tax);
        svContainer = (ScrollViewWithHeaderNewPrescriptionLayout) view.findViewById(R.id.sv_container);
        layoutNextReviewOn = (LinearLayout) view.findViewById(R.id.layout_next_review);
        layoutNextReviewOn.setVisibility(View.INVISIBLE);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initSaveButton(this);
        tabhost.setOnTabChangedListener(this);
        viewPager.addOnPageChangeListener(this);
        tvDate.setOnClickListener(this);
        ((CommonOpenUpActivity) mActivity).initRightActionView(ActionbarLeftRightActionTypeDrawables.WITH_SAVE, view);
        btSave = ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }

    private void initTabsFragmentsList() {
        tabhost.setup();
        fragmentsList = new ArrayList<>();

        // init fragment 1
        allTreatmentListFragment = new TreatmentListFragment(this);
        addFragment(allTreatmentListFragment, R.string.treatment, false);
// init fragment 2
        if (doctorProfile.getSpecialities().contains("Dentist")) {
            // init fragment 2
            customTreatmentListFragment = new TreatmentCustomListFragment(this);
            addFragment(customTreatmentListFragment, R.string.featured, false);
        } else {
            tabhost.getTabWidget().setVisibility(View.GONE);
        }
        drugListFragment = new DrugListFragment(this);
        addFragment(drugListFragment, R.string.rx_text, false);

        diagnosticTestListFragment = new DiagnosticTestListFragment(this);
        addFragment(diagnosticTestListFragment, R.string.lab_test, false);
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

    BroadcastReceiver getModifiedValueOfInvoiceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            if (intent != null && intent.hasExtra(SelectedInvoiceListFragment.TAG_TOTAL_COST_DISCOUNT_DETAIL_VALUES)) {
                TotalTreatmentCostDiscountValues totalTreatmentCostDiscountValues = Parcels.unwrap(intent.getParcelableExtra(SelectedInvoiceListFragment.TAG_TOTAL_COST_DISCOUNT_DETAIL_VALUES));
                setModifiedValues(totalTreatmentCostDiscountValues);
            }
        }

    };

    private void initViewPagerAdapter() {
        viewPager.setOffscreenPageLimit(fragmentsList.size());
        ContactsDetailViewPagerAdapter viewPagerAdapter = new ContactsDetailViewPagerAdapter(getChildFragmentManager());
        viewPagerAdapter.setFragmentsList(fragmentsList);
        viewPager.setAdapter(viewPagerAdapter);
    }


    private void initDefaultData(Invoice invoice) {
        tvTotalCost.setText(Util.formatDoubleNumber(invoice.getTotalCost()));
        if (invoice.getTotalDiscount() != null)
            tvTotalDiscount.setText(Util.formatDoubleNumber(invoice.getTotalDiscount().getValue()));
        tvGrandTotal.setText(Util.formatDoubleNumber(invoice.getGrandTotal()));
        if (invoice.getTotalTax() != null)
            tvTotalTax.setText(Util.formatDoubleNumber(invoice.getTotalTax().getValue()));
        if (invoice != null && invoice.getCreatedTime() != null)
            tvDate.setText(DateTimeUtil.getFormatedDate(invoice.getCreatedTime()));
    }

    private void initSelectedInvoiceListFragment() {
        Bundle bundle = new Bundle();
        selectedInvoiceListFragment = new SelectedInvoiceListFragment(user);
        if (invoice != null)
            bundle.putParcelable(selectedInvoiceListFragment.TAG_INVOICE_ITEM_DETAIL, Parcels.wrap(invoice));
        bundle.putParcelable(SelectedTreatmentsListFragment.TAG_DOCTOR_PROFILE, Parcels.wrap(doctorProfile));
        selectedInvoiceListFragment.setArguments(bundle);
        mFragmentManager.beginTransaction().add(R.id.layout_selected_service_list_fragment, selectedInvoiceListFragment, selectedInvoiceListFragment.getClass().getSimpleName()).commit();
    }

  /*  private void initTreatmentData() {
        if (treatment != null) {
            List<TreatmentItem> treatments = treatment.getTreatments();
            for (TreatmentItem treatmentItem :
                    treatments) {
                TreatmentService treatmentService = treatmentItem.getTreatmentService();
                if (treatmentService != null)
                    onTreatmentItemClick(treatmentService);
            }
        }
    }*/


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
                DoctorClinicProfile doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
                if (doctorClinicProfile != null && doctorClinicProfile.getPidHasDate() != null)
                    pidHasDate = doctorClinicProfile.getPidHasDate();
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
        int id = v.getId();

        if (id == R.id.container_right_action) {
            validateData();
        } else if (id == R.id.tv_date) {
            openDatePickerDialog();
        } else {
            // default case: do nothing
        }
    }

    private void openDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(DateTimeUtil.DATE_FORMAT_DAY_MONTH_YEAR_SLASH, calendar, tvDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedFromDateTimeMillis = getSelectedFromDateTime(year, monthOfYear, dayOfMonth);
                tvDate.setText(DateTimeUtil.getFormatedDate(selectedFromDateTimeMillis));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private long getSelectedFromDateTime(int year, int month, int day) {
        Calendar calendar1 = DateTimeUtil.getCalendarInstance();
        calendar1.set(Calendar.YEAR, year);
        calendar1.set(Calendar.MONTH, month);
        calendar1.set(Calendar.DAY_OF_MONTH, day);
        return calendar1.getTimeInMillis();
    }

    private void validateData() {
        int msgId = getBlankInvoiceMsg();
        if (msgId == 0) {
            addInvoice();
        } else {
            Util.showToast(mActivity, msgId);
        }
    }

    private void addInvoice() {
        InvoiceRequest invoiceToSend = new InvoiceRequest();
        invoiceToSend.setInvoiceItems(selectedInvoiceListFragment.getModifiedInvoiceItemRequestList());
        invoiceToSend.setDoctorId(user.getUniqueId());
        invoiceToSend.setHospitalId(user.getForeignHospitalId());
        invoiceToSend.setPatientId(selectedPatient.getUserId());
        invoiceToSend.setLocationId(user.getForeignLocationId());
        invoiceToSend.setTotalCost(Double.parseDouble(Util.getFormattedDoubleNumber(invoice.getTotalCost())));
        invoiceToSend.setTotalDiscount(invoice.getTotalDiscount());
        invoiceToSend.setTotalTax(invoice.getTotalTax());
        invoiceToSend.setGrandTotal(invoice.getGrandTotal());
        if (!Util.isNullOrBlank(tvDate.getText().toString()))
            invoiceToSend.setCreatedTime(DateTimeUtil.getLongFromFormattedFormatString(DateTimeUtil.DATE_FORMAT_DAY_MONTH_YEAR_SLASH, String.valueOf(tvDate.getText())));
        else
            invoiceToSend.setCreatedTime(DateTimeUtil.getCurrentDateLong());
        if (invoice.getUniqueId() != null)
            invoiceToSend.setUniqueId(invoice.getUniqueId());
        if (invoice.getUniqueInvoiceId() != null)
            invoiceToSend.setUniqueInvoiceId(invoice.getUniqueInvoiceId());
        invoiceToSend.setInvoiceDate(DateTimeUtil.getCurrentDateLong());
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addInvoice(Invoice.class, invoiceToSend, this, this);
    }


    public int getBlankInvoiceMsg() {
        int msgId = R.string.alert_add_invoice;
        if (!Util.isNullOrEmptyList(selectedInvoiceListFragment.getModifiedInvoiceItemList()))
            return 0;
        return msgId;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_ADD_TREATMENT) {
//            if (resultCode == HealthCocoConstants.RESULT_CODE_SELECTED_TREATMENT && data != null) {
//                TreatmentService treatmentService = Parcels.unwrap(data.getParcelableExtra(TAG_SELECTED_TREATMENT_OBJECT));
//                addTreatmentService(treatmentService);
//            }
        }
    }

    private void addTreatmentService(TreatmentService treatmentService) {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setName(treatmentService.getName());
        invoiceItem.setDoctorId(user.getUniqueId());
        invoiceItem.setDoctorName(user.getFirstName());
        invoiceItem.setType(InvoiceItemType.SERVICE);
        Quantity quantity = new Quantity();
        quantity.setType(QuantityEnum.QTY);
        quantity.setValue(1);
        invoiceItem.setQuantity(quantity);
        invoiceItem.setCost(treatmentService.getCost());
        invoiceItem.setFinalCost(treatmentService.getCost());
        invoiceItem.setItemId(treatmentService.getUniqueId());
        selectedInvoiceListFragment.addInvoiceItem(invoiceItem);
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
//                    initTreatmentData();
                    init();
                    initTabsFragmentsList();
                    initViewPagerAdapter();
                    if (pidHasDate != null)
                        if (!pidHasDate && (!Util.isNullOrBlank(selectedPatient.getPnum())))
                            selectedPatient.setPid(Util.getValidatedValue(selectedPatient.getPnum()));
                    initActionPatientDetailActionBar(PatientProfileScreenType.IN_ADD_VISIT_HEADER, view, selectedPatient);
                }
                break;
            case ADD_INVOICE:
                if (response.getData() != null && response.getData() instanceof Invoice) {
                    Invoice invoice = (Invoice) response.getData();
                    LocalDataServiceImpl.getInstance(mApp).addInvoice(invoice);
                    Util.sendBroadcasts(mApp, new ArrayList<String>() {{
                        add(PatientReceiptDetailFragment.INTENT_GET_RECEIPT_LIST_LOCAL);
                        add(PatientInvoiceDetailFragment.INTENT_GET_INVOICE_LIST_LOCAL);
                        add(CommonOpenUpPatientDetailFragment.INTENT_REFRESH_AMOUNT_DETAILS);
                    }});
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_INVOICE, null);
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
            LocalBroadcastManager.getInstance(mActivity).registerReceiver(getModifiedValueOfInvoiceReceiver, filter2);
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
        LocalBroadcastManager.getInstance(mActivity).unregisterReceiver(getModifiedValueOfInvoiceReceiver);
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

    @Override
    public void onTreatmentItemClick(Object object) {
        TreatmentService treatmentService = (TreatmentService) object;
        addTreatmentService(treatmentService);
        if (object != null && selectedInvoiceListFragment != null) {
            svContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    svContainer.removeOnLayoutChangeListener(this);
                    Log.d(TAG, "list got updated, do what ever u want");
                    svContainer.requestChildFocus(selectedInvoiceListFragment.getLastChildView(), selectedInvoiceListFragment.getLastChildView());
                }
            });
        }
    }

    @Override
    public User getUser() {
        return user;
    }

    private void setModifiedValues(TotalTreatmentCostDiscountValues totalTreatmentCostDiscountValues) {
        Double totalTax = totalTreatmentCostDiscountValues.getTotalTax();
        Double totalCost = totalTreatmentCostDiscountValues.getTotalCost();
        Double totalGrandTotal = totalTreatmentCostDiscountValues.getTotalGrandTotal();
        Double totalDiscount = totalTreatmentCostDiscountValues.getTotalCost() - (totalTreatmentCostDiscountValues.getTotalGrandTotal() - totalTax);
        if (invoice == null)
            invoice = new Invoice();
        //set Total Cost
        invoice.setTotalCost(totalCost);
        //set TotalDiscount Cost
        UnitValue unitTotalDiscount = new UnitValue();
        unitTotalDiscount.setValue(totalDiscount);
        unitTotalDiscount.setUnit(UnitType.INR);
        invoice.setTotalDiscount(unitTotalDiscount);

        //set TotalTax
        UnitValue unitTotalTax = new UnitValue();
        unitTotalTax.setValue(totalTax);
        unitTotalTax.setUnit(UnitType.INR);
        invoice.setTotalTax(unitTotalTax);

        //set GrandTotal Cost
        invoice.setGrandTotal(totalGrandTotal);
        initDefaultData(invoice);
    }

    @Override
    public void ondrugItemClick(SelectDrugItemType drugItemType, Object object) {
        InvoiceItem invoiceItem = new InvoiceItem();
        DrugsListSolrResponse drugsSolr = (DrugsListSolrResponse) object;
        String drugName = drugsSolr.getDrugName();
        invoiceItem.setName(drugName);
        invoiceItem.setType(InvoiceItemType.PRODUCT);
        Quantity quantity1 = new Quantity();
        quantity1.setType(QuantityEnum.QTY);
        quantity1.setValue(1);
        invoiceItem.setQuantity(quantity1);
        invoiceItem.setDoctorName(user.getFirstName());
        invoiceItem.setDoctorId(user.getUniqueId());
        invoiceItem.setItemId(drugsSolr.getUniqueId());
        selectedInvoiceListFragment.addInvoiceItem(invoiceItem);
        Util.sendBroadcast(mApp, INTENT_GET_MODIFIED_VALUE);

        if (object != null && selectedInvoiceListFragment != null) {
            svContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    svContainer.removeOnLayoutChangeListener(this);
                    Log.d(TAG, "list got updated, do what ever u want");
                    svContainer.requestChildFocus(selectedInvoiceListFragment.getLastChildView(), selectedInvoiceListFragment.getLastChildView());
                }
            });
        }
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

    @Override
    public void onDiagnosticTestClicked(DiagnosticTest diagnosticTest) {
        InvoiceItem invoiceItem = new InvoiceItem();
        invoiceItem.setName(diagnosticTest.getTestName());
        invoiceItem.setType(InvoiceItemType.TEST);
        invoiceItem.setDoctorId(user.getUniqueId());
        invoiceItem.setDoctorName(user.getFirstName());
        Quantity quantity = new Quantity();
        quantity.setType(QuantityEnum.QTY);
        quantity.setValue(1);
        invoiceItem.setQuantity(quantity);
        invoiceItem.setItemId(diagnosticTest.getUniqueId());
        selectedInvoiceListFragment.addInvoiceItem(invoiceItem);
        Util.sendBroadcast(mApp, INTENT_GET_MODIFIED_VALUE);

        if (diagnosticTest != null && selectedInvoiceListFragment != null) {
            svContainer.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    svContainer.removeOnLayoutChangeListener(this);
                    Log.d(TAG, "list got updated, do what ever u want");
                    svContainer.requestChildFocus(selectedInvoiceListFragment.getLastChildView(), selectedInvoiceListFragment.getLastChildView());
                }
            });
        }
    }
}
