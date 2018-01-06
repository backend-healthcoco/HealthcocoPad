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
import com.healthcoco.healthcocopad.bean.server.ReceiptResponse;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.ReceiptType;
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
 * Created by Prashant on 22-12-2017.
 */

public class ReceiptListItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener
        , Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    public static final String TAG_INVOICE_DATA = "invoice_data";
    private TextView tvReceiptID;
    private TextView tvAmountPaid;
    private TextView tvPaymentMethod;
    private TextView tvUsedAdvanceAmount;

    private ReceiptResponse receiptResponse;
    private TextView tvDate;
    private TextView tvInvoiceID;
    private TextView tvCreatedBy;
    private LinearLayout btEmail;
    private LinearLayout btPrint;
    private LinearLayout btDiscard;
    private LinearLayout layoutDiscarded;
    private HealthCocoActivity mActivity;

    public ReceiptListItemViewHolder(HealthCocoActivity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
    }

    @Override
    public void setData(Object object) {
        this.receiptResponse = (ReceiptResponse) object;
    }

    @Override
    public void applyData() {
        if (receiptResponse.getReceivedDate() != null)
            tvDate.setText(DateTimeUtil.getFormatedDate(receiptResponse.getReceivedDate()));

        if (!Util.isNullOrBlank(receiptResponse.getUniqueReceiptId())) {
            tvReceiptID.setVisibility(View.VISIBLE);
            tvReceiptID.setText(mActivity.getResources().getString(R.string.receipt_id) + receiptResponse.getUniqueReceiptId());
        } else tvReceiptID.setVisibility(View.GONE);
        if (!Util.isNullOrBlank(receiptResponse.getCreatedBy()))
            tvCreatedBy.setText(receiptResponse.getCreatedBy());
        else
            tvCreatedBy.setText("");
        if (receiptResponse.getUsedAdvanceAmount() > 0) {
            tvUsedAdvanceAmount.setText(String.valueOf("\u20B9 " + Util.getFormattedDoubleNumber(receiptResponse.getUsedAdvanceAmount())));
        } else
            tvUsedAdvanceAmount.setText(String.valueOf("\u20B9 " + "0"));

        if (receiptResponse.getReceiptType() == ReceiptType.ADVANCE)
            tvAmountPaid.setText(String.valueOf("\u20B9 " + Util.getFormattedDoubleNumber(receiptResponse.getAmountPaid())) + "(added to advance)");
        else
            tvAmountPaid.setText(String.valueOf("\u20B9 " + Util.getFormattedDoubleNumber(receiptResponse.getAmountPaid())));

        tvPaymentMethod.setText(String.valueOf(receiptResponse.getModeOfPayment()));

        if (!Util.isNullOrBlank(receiptResponse.getUniqueInvoiceId())) {
            tvInvoiceID.setText(String.valueOf(receiptResponse.getUniqueInvoiceId()));
        }

        checkDiscarded(receiptResponse.getDiscarded());
    }

    private void checkDiscarded(Boolean isDiscarded) {
        if (isDiscarded != null && isDiscarded) {
            layoutDiscarded.setVisibility(View.VISIBLE);
        } else layoutDiscarded.setVisibility(View.GONE);
    }


    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.list_item_receipt, null);
        initViews(contentView);
        initListeners();
        return contentView;
    }

    private void initViews(View contentView) {
        tvDate = (TextView) contentView.findViewById(R.id.tv_date);
        tvReceiptID = (TextView) contentView.findViewById(R.id.tv_receipt_id);
        tvCreatedBy = (TextView) contentView.findViewById(R.id.tv_created_by);
        tvAmountPaid = (TextView) contentView.findViewById(R.id.tv_text_amount_paid);
        tvInvoiceID = (TextView) contentView.findViewById(R.id.tv_text_invoice_id);
        tvPaymentMethod = (TextView) contentView.findViewById(R.id.tv_text_payment_method);
        tvUsedAdvanceAmount = (TextView) contentView.findViewById(R.id.tv_text_used_advance_amount);
        initBottomButtonViews(contentView);

        initBottomButtonViews(contentView);
    }

    private void initBottomButtonViews(View contentView) {
        btPrint = (LinearLayout) contentView.findViewById(R.id.bt_print);
        btEmail = (LinearLayout) contentView.findViewById(R.id.bt_email);
        btDiscard = (LinearLayout) contentView.findViewById(R.id.bt_discard);
        layoutDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded);
    }

    private void initListeners() {
        btEmail.setOnClickListener(this);
        btPrint.setOnClickListener(this);
        btDiscard.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_discard:
                LogUtils.LOGD(TAG, "Discard");
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    int msgId = R.string.confirm_discard_clinical_notes_message;
                    int titleId = R.string.confirm_discard_receipt_title;
                    showConfirmationAlert(v.getId(), mActivity.getResources().getString(titleId), mActivity.getResources().getString(msgId));
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_email:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline)
                    mActivity.openAddUpdateNameDialogFragment(WebServiceType.SEND_EMAIL_RECEIPT, AddUpdateNameDialogType.EMAIL, receiptResponse.getUniqueId());
                else onNetworkUnavailable(null);
                break;
            case R.id.bt_print:
            case R.id.tv_print:
                LogUtils.LOGD(TAG, "Print");
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    mActivity.showLoading(false);
                    WebDataServiceImpl.getInstance(mApp).getPdfUrl(String.class, WebServiceType.GET_RECEIPT_PDF_URL, receiptResponse.getUniqueId(), this, this);
                } else onNetworkUnavailable(null);
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
                        onDiscardedClicked(receiptResponse);
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
        ReceiptResponse receiptResponse = (ReceiptResponse) object;
        if (HealthCocoConstants.isNetworkOnline) {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).discardReceipt(ReceiptResponse.class, receiptResponse.getUniqueId(), this, this);
        } else onNetworkUnavailable(null);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case GET_RECEIPT_PDF_URL:
                    if (response.getData() != null && response.getData() instanceof String) {
                        mActivity.openEnlargedImageDialogFragment(true, (String) response.getData());
                    }
                    break;
                case DISCARD_RECEIPT:
                    LogUtils.LOGD(TAG, "Success DISCARD_RECEIPT");
                    receiptResponse.setDiscarded(!receiptResponse.getDiscarded());
                    applyData();
                    LocalDataServiceImpl.getInstance(mApp).updateReceipt(receiptResponse);
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
