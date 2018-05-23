package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.InvoiceItem;
import com.healthcoco.healthcocopad.bean.server.UnitValue;
import com.healthcoco.healthcocopad.custom.OptionsPopupWindow;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.InvoiceItemType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.InvoiceItemClickListeners;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.List;

/**
 * Created by Shreshtha on 05-07-2017.
 */

public class InvoiceListItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener
        , Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private Invoice invoice;
    private TextView tvDate;
    private TextView tvInvoiceID;
    private TextView tvCreatedBy;
    private LinearLayout btEdit;
    private LinearLayout btPay;
    private LinearLayout btPrint;
    private LinearLayout btDiscard;
    private LinearLayout layoutDiscarded;
    private LinearLayout containerInvoice;
    private TextView tvGrandTotal;
    private TextView tvTotalCost;
    private TextView tvTotalDiscount;
    private TextView tvTotalTax;
    private TextView tvPaidAmount;
    private TextView tvBalanceAmount;
    private OptionsPopupWindow popupWindow;
    private HealthCocoActivity mActivity;
    private InvoiceItemClickListeners invoiceItemClickListeners;

    public InvoiceListItemViewHolder(HealthCocoActivity mActivity, InvoiceItemClickListeners invoiceItemClickListeners) {
        super(mActivity);
        this.mActivity = mActivity;
        this.invoiceItemClickListeners = invoiceItemClickListeners;
    }

    @Override
    public void setData(Object object) {
        this.invoice = (Invoice) object;
    }

    @Override
    public void applyData() {
        if (invoice.getCreatedTime() != null)
            tvDate.setText(DateTimeUtil.getFormatedDate(invoice.getCreatedTime()));

        if (!Util.isNullOrBlank(invoice.getUniqueInvoiceId())) {
            tvInvoiceID.setVisibility(View.VISIBLE);
            tvInvoiceID.setText(mActivity.getResources().getString(R.string.invoice_id) + invoice.getUniqueInvoiceId());
        } else tvInvoiceID.setVisibility(View.GONE);
        if (!Util.isNullOrBlank(invoice.getCreatedBy()))
            tvCreatedBy.setText(invoice.getCreatedBy());
        else
            tvCreatedBy.setText("");

        applyDataToInvoiceItems();
//        if (invoice.getGrandTotal() > 0)
        tvGrandTotal.setText(String.valueOf(Util.formatDoubleNumber(invoice.getGrandTotal())));
//        else tvGrandTotal.setText(0);

//        if (invoice.getUsedAdvanceAmount() > 0)
        double paidAmount = invoice.getGrandTotal() - invoice.getBalanceAmount();
        tvPaidAmount.setText(String.valueOf(Util.getFormattedDoubleNumber(paidAmount)));

//        if (invoice.getBalanceAmount() > 0)
        tvBalanceAmount.setText(String.valueOf(Util.formatDoubleNumber(invoice.getBalanceAmount())));
//        else tvBalanceAmount.setText(0);
        tvTotalCost.setText(String.valueOf(Util.formatDoubleNumber(invoice.getTotalCost())));

        UnitValue totalDiscount = invoice.getTotalDiscount();
        if (totalDiscount != null) {
            double value = totalDiscount.getValue();
            if (totalDiscount != null && totalDiscount.getUnit() != null && value != 0)
                tvTotalDiscount.setText(Util.getIntValue(value) + "");
            else
                tvTotalDiscount.setText(mActivity.getResources().getString(R.string.no_text_dash));
        }
        UnitValue totalTax = invoice.getTotalTax();
        if (totalTax != null) {
            double valueTax = totalTax.getValue();
            if (totalTax != null && totalTax.getUnit() != null && valueTax != 0)
                tvTotalTax.setText(Util.getIntValue(valueTax) + "");
            else
                tvTotalTax.setText(mActivity.getResources().getString(R.string.no_text_dash));
        }

        if (invoice.getBalanceAmount() <= 0) {
            btPay.setVisibility(View.GONE);
        } else btPay.setVisibility(View.VISIBLE);

        if (invoice.getBalanceAmount() >= 0) {
            btEdit.setVisibility(View.VISIBLE);
        } else btEdit.setVisibility(View.GONE);
        checkDiscarded(invoice.getDiscarded());
    }

    private void checkDiscarded(Boolean isDiscarded) {
        if (isDiscarded != null && isDiscarded) {
            layoutDiscarded.setVisibility(View.VISIBLE);
        } else layoutDiscarded.setVisibility(View.GONE);
    }

    private void applyDataToInvoiceItems() {
        containerInvoice.removeAllViews();
        List<InvoiceItem> invoiceItemList = invoice.getInvoiceItems();
        if (!Util.isNullOrEmptyList(invoiceItemList)) {
            containerInvoice.setVisibility(View.VISIBLE);
            for (InvoiceItem invoiceItem : invoiceItemList) {
                InvoiceListSubItemViewHolder invoiceListSubItemViewHolder = new InvoiceListSubItemViewHolder(mActivity);
                View convertView = invoiceListSubItemViewHolder.getContentView();
                if (invoiceItem.getType() != null) {
                    if (invoiceItem.getType().equals(InvoiceItemType.PRODUCT)) {
                        invoiceListSubItemViewHolder.setData(invoiceItem);
                    } else if (invoiceItem.getType().equals(InvoiceItemType.TEST)) {
                        invoiceListSubItemViewHolder.setData(invoiceItem);
                    } else if (invoiceItem.getType().equals(InvoiceItemType.SERVICE)) {
                        invoiceListSubItemViewHolder.setData(invoiceItem);
                    }
                    invoiceListSubItemViewHolder.applyData();
                    containerInvoice.addView(convertView);
                }
            }
        } else {
            containerInvoice.setVisibility(View.GONE);
        }
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.list_item_invoice, null);
        initViews(contentView);
        initListeners();
        return contentView;
    }

    private void initViews(View contentView) {
        tvDate = (TextView) contentView.findViewById(R.id.tv_date);
        tvInvoiceID = (TextView) contentView.findViewById(R.id.tv_invoice_id);
        tvCreatedBy = (TextView) contentView.findViewById(R.id.tv_created_by);
        tvGrandTotal = (TextView) contentView.findViewById(R.id.tv_text_grand_total);
        tvPaidAmount = (TextView) contentView.findViewById(R.id.tv_text_paid_amount);
        tvBalanceAmount = (TextView) contentView.findViewById(R.id.tv_text_balance_amount);
        tvTotalCost = (TextView) contentView.findViewById(R.id.tv_text_total_cost);
        tvTotalDiscount = (TextView) contentView.findViewById(R.id.tv_text_total_discount);
        tvTotalTax = (TextView) contentView.findViewById(R.id.tv_text_total_tax);
        containerInvoice = (LinearLayout) contentView.findViewById(R.id.container_invoice);
        initBottomButtonViews(contentView);
    }

    private void initBottomButtonViews(View contentView) {
        btEdit = (LinearLayout) contentView.findViewById(R.id.bt_edit);
        btPay = (LinearLayout) contentView.findViewById(R.id.bt_pay);
        btPrint = (LinearLayout) contentView.findViewById(R.id.bt_print);
        btDiscard = (LinearLayout) contentView.findViewById(R.id.bt_discard);
        layoutDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded);
    }

    private void initListeners() {
        btEdit.setOnClickListener(this);
        btPay.setOnClickListener(this);
        btPrint.setOnClickListener(this);
        btDiscard.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    invoiceItemClickListeners.onEditInvoiceClicked(invoice);
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_pay:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    invoiceItemClickListeners.onPayInvoiceClicked(invoice);
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_options:
                popupWindow.showOptionsWindow(v);
                break;
            case R.id.bt_discard:
                LogUtils.LOGD(TAG, "Discard");
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    int msgId = R.string.confirm_discard_clinical_notes_message;
                    int titleId = R.string.confirm_discard_invoice_title;
                    showConfirmationAlert(v.getId(), mActivity.getResources().getString(titleId), mActivity.getResources().getString(msgId));
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_print:
            case R.id.tv_print:
                LogUtils.LOGD(TAG, "Print");
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).getPdfUrl(String.class, WebServiceType.GET_INVOICE_PDF_URL, invoice.getUniqueId(), this, this);
                } else onNetworkUnavailable(null);
                break;
            case R.id.tv_email:
            case R.id.bt_email:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    mActivity.openAddUpdateNameDialogFragment(WebServiceType.SEND_EMAIL_INVOICE, AddUpdateNameDialogType.EMAIL, invoice.getUniqueId());
                else onNetworkUnavailable(null);
                break;
        }
    }


    private void showConfirmationAlert(final int viewId, String title, String msg) {
        if (Util.isNullOrBlank(title))
            title = mActivity.getResources().getString(R.string.confirm);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(msg);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (viewId) {
                    case R.id.bt_discard:
                        onDiscardedClicked(invoice);
                        break;
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

    public void onDiscardedClicked(Object object) {
        Invoice invoice = (Invoice) object;
        if (HealthCocoConstants.isNetworkOnline) {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).discardInvoice(Invoice.class, invoice.getUniqueId(), this, this);
        } else onNetworkUnavailable(null);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case GET_INVOICE_PDF_URL:
                    if (response.getData() != null && response.getData() instanceof String) {
                        mActivity.openEnlargedImageDialogFragment(true, (String) response.getData());
                    }
                    break;
                case DISCARD_INVOICE:
                    LogUtils.LOGD(TAG, "Success DISCARD_INVOICE");
                    invoice.setDiscarded(!invoice.getDiscarded());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).updateInvoice(invoice);
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }
}
