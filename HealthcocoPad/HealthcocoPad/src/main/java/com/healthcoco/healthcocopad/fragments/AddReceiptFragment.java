package com.healthcoco.healthcocopad.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddReceiptRequest;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.ReceiptServerResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.InputFilterMinMax;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.ModeOfPaymentType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.ReceiptType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectInvoiceItemClickListener;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.InvoiceListItemViewHolder;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;


/**
 * Created by Prashant on 23/12/2017.
 */


public class AddReceiptFragment extends HealthCocoFragment implements LocalDoInBackgroundListenerOptimised,
        HealthcocoTextWatcherListener, View.OnClickListener, PopupWindowListener, SelectInvoiceItemClickListener {
    public static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    public static final String DATE_FORMAT_TO_SEND = "EEE, MMM dd,yyyy hh:mm aaa";
    private LinearLayout layoutInvoiceItem;
    private TextView tvInvoiceId;
    private TextView tvGrandTotalRupees;
    private TextView tvPaidRupees;
    private TextView tvDueRupees;
    private TextView btDelete;
    private TextView tvAvailableAdvanceAmount;
    private TextView tvInvoiceDueAmount;
    private LinearLayout layoutPayFromAdvance;
    private EditText editTextPayFromAdvance;
    private LinearLayout layoutDueAfterAdvance;
    private TextView tvTextDueAfterAdvance;
    private EditText editPayNow;
    private TextView tvCashMode;
    private TextView tvReceivedDate;
    private TextView tvAddedToPatientAdvance;
    private TextView tvAdvanceLeft;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private LinearLayout layoutInvoiceDue;
    private Invoice invoice;
    private SelectInvoiceFragment selectInvoiceFragment;
    private LinearLayout layoutAdvanceLeft;
    private LinearLayout btSave;
    private LinearLayout layoutNextReviewOn;
    private String modeOfPayment;
    private boolean isFromInvoiceScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_new_receipt, container, false);
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
        initSelectedTreatmentsListFragment();
        mActivity.initPopupWindows(tvCashMode, PopupWindowType.PAYMENT_TYPE, PopupWindowType.PAYMENT_TYPE.getList(), this);
    }

    private void getIntentData() {
        Intent intent = mActivity.getIntent();
        invoice = Parcels.unwrap(intent.getParcelableExtra(PatientInvoiceDetailFragment.TAG_INVOICE_DATA));
        isFromInvoiceScreen = intent.getBooleanExtra(PatientInvoiceDetailFragment.TAG_FROM_INVOICE_SCREEN, false);
        if (invoice != null) initInvoiceLayoutData(invoice);
    }

    @Override
    public void initViews() {
        layoutInvoiceItem = (LinearLayout) view.findViewById(R.id.layout_invoice_item);
        tvInvoiceId = (TextView) view.findViewById(R.id.tv_invoice_id);
        tvGrandTotalRupees = (TextView) view.findViewById(R.id.tv_grand_total_rupees);
        tvPaidRupees = (TextView) view.findViewById(R.id.tv_paid_rupees);
        tvDueRupees = (TextView) view.findViewById(R.id.tv_due_rupees);
        btDelete = (TextView) view.findViewById(R.id.bt_delete);
        tvAvailableAdvanceAmount = (TextView) view.findViewById(R.id.tv_available_advance_amount);
        tvInvoiceDueAmount = (TextView) view.findViewById(R.id.tv_invoice_due_amount);
        layoutPayFromAdvance = (LinearLayout) view.findViewById(R.id.layout_pay_from_advance);
        layoutInvoiceDue = (LinearLayout) view.findViewById(R.id.layout_invoice_due);
        editTextPayFromAdvance = (EditText) view.findViewById(R.id.editText_pay_from_advance);
        layoutDueAfterAdvance = (LinearLayout) view.findViewById(R.id.layout_due_after_advance);
        tvTextDueAfterAdvance = (TextView) view.findViewById(R.id.tv_text_due_after_advance);
        editPayNow = (EditText) view.findViewById(R.id.edit_pay_now);
        tvCashMode = (TextView) view.findViewById(R.id.tv_cash_mode);
        tvReceivedDate = (TextView) view.findViewById(R.id.tv_received_date);
        tvAddedToPatientAdvance = (TextView) view.findViewById(R.id.tv_added_to_patient_advance);
        tvAdvanceLeft = (TextView) view.findViewById(R.id.tv_advance_left);
        layoutAdvanceLeft = (LinearLayout) view.findViewById(R.id.layout_advance_left);
        layoutNextReviewOn = (LinearLayout) view.findViewById(R.id.layout_next_review);
        layoutNextReviewOn.setVisibility(View.INVISIBLE);
        layoutInvoiceItem.setVisibility(View.GONE);
        layoutInvoiceDue.setVisibility(View.GONE);
        layoutPayFromAdvance.setVisibility(View.GONE);
        layoutDueAfterAdvance.setVisibility(View.GONE);
        layoutAdvanceLeft.setVisibility(View.GONE);
        view.findViewById(R.id.tv_date).setVisibility(View.GONE);


        ((CommonOpenUpActivity) mActivity).initRightActionView(ActionbarLeftRightActionTypeDrawables.WITH_SAVE, view);
        btSave = ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);

    }

    @Override
    public void initListeners() {
        btDelete.setOnClickListener(this);
        tvReceivedDate.setOnClickListener(this);
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
        editTextPayFromAdvance.addTextChangedListener(new HealthcocoTextWatcher(editTextPayFromAdvance, this));
        editPayNow.addTextChangedListener(new HealthcocoTextWatcher(editPayNow, this));
    }

    private void initSelectedTreatmentsListFragment() {
        selectInvoiceFragment = new SelectInvoiceFragment(this);
        mFragmentManager.beginTransaction().add(R.id.layout_select_invoice_fragment, selectInvoiceFragment, selectInvoiceFragment.getClass().getSimpleName()).commit();
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
            case R.id.container_right_action:
                hideKeyboard(view);
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    validateData();
                else
                    Util.showToast(mActivity, getResources().getString(R.string.user_offline));
                break;
            case R.id.bt_delete:
                showConfirmDeleteInviceItem();
                break;
            case R.id.tv_received_date:
                openReceivedDatePickerDialog();
                break;
            default:
                break;
        }
    }

    private void validateData() {
        String msg = null;
        String amountPayNow = String.valueOf(editPayNow.getText());
        String usedAdvanceAmountToPay = editTextPayFromAdvance.getText().toString();
        if (Util.isNullOrBlank(amountPayNow) && Util.isNullOrBlank(usedAdvanceAmountToPay))
            msg = getResources().getString((R.string.please_enter_amount_to_pay));

        if (Util.isNullOrBlank(msg)) {
            addReceipt(amountPayNow, usedAdvanceAmountToPay);
        } else
            Util.showToast(mActivity, msg);
    }

    private void addReceipt(String amountPayNow, String usedAdvanceAmountToPay) {
        AddReceiptRequest addReceiptRequest = new AddReceiptRequest();
        addReceiptRequest.setDoctorId(user.getUniqueId());
        addReceiptRequest.setHospitalId(user.getForeignHospitalId());
        addReceiptRequest.setPatientId(selectedPatient.getUserId());
        addReceiptRequest.setLocationId(user.getForeignLocationId());
        String selectedDate = String.valueOf(tvReceivedDate.getText()).trim();
        addReceiptRequest.setReceivedDate(DateTimeUtil.getLongFromFormattedDayMonthYearFormatString(DATE_FORMAT_USED_IN_THIS_SCREEN, selectedDate));
        if (modeOfPayment != null)
            addReceiptRequest.setModeOfPayment(ModeOfPaymentType.valueOf(modeOfPayment));
        else addReceiptRequest.setModeOfPayment(ModeOfPaymentType.CASH);
        if (!Util.isNullOrBlank(usedAdvanceAmountToPay))
            addReceiptRequest.setUsedAdvanceAmount(Double.parseDouble(usedAdvanceAmountToPay));
        else addReceiptRequest.setUsedAdvanceAmount(0.0);
        if (!Util.isNullOrBlank(amountPayNow))
            addReceiptRequest.setAmountPaid(Double.parseDouble(amountPayNow));
        else addReceiptRequest.setAmountPaid(0.0);
        ArrayList<String> invoiceIds = new ArrayList<>();
        if (invoice != null) {
            invoiceIds.add(invoice.getUniqueId());
            addReceiptRequest.setInvoiceIds(invoiceIds);
            addReceiptRequest.setReceiptType(ReceiptType.INVOICE);
        } else addReceiptRequest.setReceiptType(ReceiptType.ADVANCE);
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addReceipt(ReceiptServerResponse.class, addReceiptRequest, this, this);
    }

    private void showConfirmDeleteInviceItem() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setMessage(R.string.confirm_remove_invoice);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                resetAllTextViews();
                layoutInvoiceItem.setVisibility(View.GONE);
                layoutAdvanceLeft.setVisibility(View.GONE);
                tvAddedToPatientAdvance.setVisibility(View.VISIBLE);
                layoutPayFromAdvance.setVisibility(View.GONE);
                layoutDueAfterAdvance.setVisibility(View.GONE);
                layoutInvoiceDue.setVisibility(View.GONE);
                invoice = null;
                editPayNow.setFilters(new InputFilter[]{});
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    private void openReceivedDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(DATE_FORMAT_USED_IN_THIS_SCREEN, calendar, tvReceivedDate);
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvReceivedDate.setText(DateTimeUtil.getFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN, year, monthOfYear, dayOfMonth, 0, 0, 0));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMaxDate(DateTimeUtil.getCurrentDateLong());
        datePickerDialog.show();
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
                    initActionPatientDetailActionBar(PatientProfileScreenType.IN_ADD_VISIT_HEADER, view, selectedPatient);
                    initData();
                    getIntentData();
                }
                break;
            case ADD_RECEIPT:
                if (response.getData() != null && response.getData() instanceof ReceiptServerResponse) {
                    ReceiptServerResponse receiptServerResponse = (ReceiptServerResponse) response.getData();
                    LocalDataServiceImpl.getInstance(mApp).addReceipt(receiptServerResponse.getDoctorPatientReceipt());
                    if (receiptServerResponse.getInvoice() != null)
                        LocalDataServiceImpl.getInstance(mApp).addInvoice(receiptServerResponse.getInvoice());
                    Util.sendBroadcasts(mApp, new ArrayList<String>() {{
                        add(PatientReceiptDetailFragment.INTENT_GET_RECEIPT_LIST_LOCAL);
                        add(PatientInvoiceDetailFragment.INTENT_GET_INVOICE_LIST_LOCAL);
                        add(CommonOpenUpPatientDetailFragment.INTENT_REFRESH_AMOUNT_DETAILS);
                    }});
                    String msg;
                    if (receiptServerResponse.getInvoice() != null)
                        msg = "Receipt Saved";
                    else msg = "Adavance Amount Added";

                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(HealthCocoConstants.TAG_RESULT_MESSAGE, msg);
                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_RECEIPT, resultIntent);
                    ((CommonOpenUpActivity) mActivity).finish();
                }
                break;
            default:
                break;
        }
        mActivity.hideLoading();
    }

    private void initData() {
        tvReceivedDate.setText(DateTimeUtil.getCurrentFormattedDate(DATE_FORMAT_USED_IN_THIS_SCREEN));
        /*
        if (selectedPatient.getTotalDueAmount() <= 0) {
            tvDueAdvanceAmount.setText(String.valueOf("\u20B9 " + Util.getFormattedDoubleNumber(selectedPatient.getTotalDueAmount()) + getResources().getString(R.string.advance)));
            tvDueAdvanceAmount.setTextColor(getResources().getColor(R.color.green_logo));
        } else if (selectedPatient.getTotalDueAmount() > 0) {
            tvDueAdvanceAmount.setText(String.valueOf("\u20B9 " + Util.getFormattedDoubleNumber(selectedPatient.getTotalDueAmount()) + getResources().getString(R.string.due)));
            tvDueAdvanceAmount.setTextColor(getResources().getColor(R.color.red_error));
        }
       */
        if (selectedPatient.getTotalRemainingAdvanceAmount() > 0)
            tvAvailableAdvanceAmount.setText(String.valueOf("\u20B9 " + Util.formatDoubleNumber(selectedPatient.getTotalRemainingAdvanceAmount())));
        else tvAvailableAdvanceAmount.setText(String.valueOf("\u20B9 " + 0));
    }

    private void initInvoiceLayoutData(Invoice invoice) {
        resetAllTextViews();
        layoutInvoiceItem.setVisibility(View.VISIBLE);
        layoutAdvanceLeft.setVisibility(View.VISIBLE);
        tvAddedToPatientAdvance.setVisibility(View.GONE);
        layoutPayFromAdvance.setVisibility(View.VISIBLE);
        layoutDueAfterAdvance.setVisibility(View.VISIBLE);
        layoutInvoiceDue.setVisibility(View.VISIBLE);
        tvInvoiceDueAmount.setText(" \u20B9 " + Util.formatDoubleNumber(invoice.getBalanceAmount()));
        tvTextDueAfterAdvance.setText(String.valueOf(Util.formatDoubleNumber(invoice.getBalanceAmount())));
        if (selectedPatient != null && selectedPatient.getTotalRemainingAdvanceAmount() > 0)
            tvAdvanceLeft.setText(" \u20B9 " + Util.formatDoubleNumber(selectedPatient.getTotalRemainingAdvanceAmount()));
        tvInvoiceId.setText(invoice.getUniqueInvoiceId());
        tvGrandTotalRupees.setText(String.valueOf(Util.formatDoubleNumber(invoice.getGrandTotal())));
        tvPaidRupees.setText(String.valueOf(Util.formatDoubleNumber(invoice.getUsedAdvanceAmount())));
        tvDueRupees.setText(String.valueOf(Util.formatDoubleNumber(invoice.getBalanceAmount())));
        editPayNow.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(invoice.getBalanceAmount()))});
        if (selectedPatient.getTotalRemainingAdvanceAmount() >= invoice.getBalanceAmount())
            editTextPayFromAdvance.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(invoice.getBalanceAmount()))});
        else
            editTextPayFromAdvance.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(selectedPatient.getTotalRemainingAdvanceAmount()))});
    }

    private void resetAllTextViews() {
        editPayNow.clearFocus();
        editPayNow.getText().clear();
        editTextPayFromAdvance.clearFocus();
        editTextPayFromAdvance.getText().clear();
    }

    @Override
    public void afterTextChange(View v, String s) {
        switch (v.getId()) {
            case R.id.edit_pay_now:
                if (!Util.isNullOrBlank(s)) {
                    if (s.length() == 1 && s.equals("0"))
                        editPayNow.setText("");
                    if (invoice != null) {
                        if (!Util.isNullOrBlank(editTextPayFromAdvance.getText().toString())) {
                            double v1 = Double.parseDouble(Util.getFormattedDoubleNumber(invoice.getBalanceAmount())) - Double.parseDouble(editTextPayFromAdvance.getText().toString());
                            if (selectedPatient.getTotalRemainingAdvanceAmount() >= invoice.getBalanceAmount())
                                editTextPayFromAdvance.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(invoice.getBalanceAmount()))});
                            else
                                editTextPayFromAdvance.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(selectedPatient.getTotalRemainingAdvanceAmount()))});
                            tvTextDueAfterAdvance.setText(String.valueOf(Util.formatDoubleNumber(v1)));
                            editPayNow.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(v1))});
                        } else {
                            editTextPayFromAdvance.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(invoice.getBalanceAmount()))});
                            editPayNow.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(invoice.getBalanceAmount()))});
                        }
                    }
                } else {
                    if (invoice != null && invoice.getBalanceAmount() > 0) {
                        if (!Util.isNullOrBlank(editTextPayFromAdvance.getText().toString())) {
                            double v1 = Double.parseDouble(Util.getFormattedDoubleNumber(invoice.getBalanceAmount())) - Double.parseDouble(editTextPayFromAdvance.getText().toString());
                            tvTextDueAfterAdvance.setText(String.valueOf(Util.formatDoubleNumber(v1)));
                        } else
                            tvTextDueAfterAdvance.setText(Util.formatDoubleNumber(invoice.getBalanceAmount()));
                        if (selectedPatient != null && selectedPatient.getTotalRemainingAdvanceAmount() > 0) {
                            tvAdvanceLeft.setText(" \u20B9 " + Util.formatDoubleNumber(selectedPatient.getTotalRemainingAdvanceAmount()));
                        }
                    }
                }
                break;
            case R.id.editText_pay_from_advance:
                if (!Util.isNullOrBlank(s)) {
                    if (s.length() == 1 && s.equals("0"))
                        editTextPayFromAdvance.setText("");
                    double intDueAfterAdvance = Double.parseDouble(Util.getFormattedDoubleNumber(invoice.getBalanceAmount())) - Double.parseDouble(s);
                    double intAdvanceLeft = Double.parseDouble(Util.getFormattedDoubleNumber(selectedPatient.getTotalRemainingAdvanceAmount())) - Double.parseDouble(s);
                    tvTextDueAfterAdvance.setText(String.valueOf(Util.formatDoubleNumber(intDueAfterAdvance)));
                    tvAdvanceLeft.setText(" \u20B9 " + Util.formatDoubleNumber(intAdvanceLeft));
                    if (!Util.isNullOrBlank(editPayNow.getText().toString())) {
                        if (selectedPatient.getTotalRemainingAdvanceAmount() >= invoice.getBalanceAmount())
                            editTextPayFromAdvance.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(invoice.getBalanceAmount()))});
                        else
                            editTextPayFromAdvance.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(selectedPatient.getTotalRemainingAdvanceAmount()))});
                        double v2 = Double.parseDouble(Util.getFormattedDoubleNumber(invoice.getBalanceAmount())) - Double.parseDouble(s);
                        editPayNow.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(v2))});
                    } else {
                        if (selectedPatient.getTotalRemainingAdvanceAmount() >= invoice.getBalanceAmount())
                            editTextPayFromAdvance.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(invoice.getBalanceAmount()))});
                        else
                            editTextPayFromAdvance.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(selectedPatient.getTotalRemainingAdvanceAmount()))});
                        editPayNow.setFilters(new InputFilter[]{new InputFilterMinMax("0", Util.getFormattedDoubleNumber(intDueAfterAdvance))});
                    }
                } else {
                    if (!Util.isNullOrBlank(editPayNow.getText().toString())) {
                        double v1 = Double.parseDouble(Util.getFormattedDoubleNumber(invoice.getBalanceAmount())) - Double.parseDouble(editPayNow.getText().toString());
                        tvTextDueAfterAdvance.setText(String.valueOf(Util.formatDoubleNumber(v1)));
                    } else
                        tvTextDueAfterAdvance.setText(Util.formatDoubleNumber(invoice.getBalanceAmount()));
                    tvAdvanceLeft.setText(" \u20B9 " + Util.formatDoubleNumber(selectedPatient.getTotalRemainingAdvanceAmount()));
                }
                break;
        }
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        modeOfPayment = (String) object;
    }

    @Override
    public void onEmptyListFound() {

    }

    @Override
    public void onInvoiceItemClick(Object object) {
        invoice = (Invoice) object;
        initInvoiceLayoutData(invoice);
    }

    @Override
    public User getUser() {
        return null;
    }
}
