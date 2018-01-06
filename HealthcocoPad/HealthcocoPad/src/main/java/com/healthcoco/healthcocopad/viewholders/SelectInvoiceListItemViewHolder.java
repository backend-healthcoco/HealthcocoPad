package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.InvoiceItem;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.List;

/**
 * Created by Shreshtha on 08-07-2017.
 */

public class SelectInvoiceListItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener {
    private Invoice invoice;
    private TextView tvDate;
    private TextView tvInvoiceID;
    private LinearLayout containerInvoice;
    private TextView tvGrandTotal;
    private TextView tvPaidAmount;
    private TextView tvBalanceAmount;
    private HealthCocoActivity mActivity;
    private TextView tvTextInvoiceTypeName;

    public SelectInvoiceListItemViewHolder(HealthCocoActivity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
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
            tvInvoiceID.setText(mActivity.getResources().getString(R.string.invoice_id_select_invoice) + invoice.getUniqueInvoiceId());
        } else tvInvoiceID.setVisibility(View.GONE);

        applyDataToInvoiceItems();
        tvGrandTotal.setText(Util.getFormattedDoubleNumber(invoice.getGrandTotal()));

        double paidAmount = invoice.getGrandTotal() - invoice.getBalanceAmount();
        tvPaidAmount.setText(String.valueOf(Util.getFormattedDoubleNumber(paidAmount)));

        tvBalanceAmount.setText(Util.getFormattedDoubleNumber(invoice.getBalanceAmount()));
    }

    private void applyDataToInvoiceItems() {
        containerInvoice.removeAllViews();
        List<InvoiceItem> invoiceItemList = invoice.getInvoiceItems();
        if (!Util.isNullOrEmptyList(invoiceItemList)) {
            containerInvoice.setVisibility(View.VISIBLE);
            for (InvoiceItem invoiceItem : invoiceItemList) {
                View convertView = inflater.inflate(R.layout.sub_item_select_invoice, null);
                tvTextInvoiceTypeName = (TextView) convertView.findViewById(R.id.tv_text_invoice_type_name);
                tvTextInvoiceTypeName.setText(invoiceItem.getName());
                containerInvoice.addView(convertView);
            }
        } else {
            containerInvoice.setVisibility(View.GONE);
        }
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.list_item_select_invoice, null);
        initViews(contentView);
        initListeners();
        return contentView;
    }

    private void initViews(View contentView) {
        tvDate = (TextView) contentView.findViewById(R.id.tv_date);
        tvInvoiceID = (TextView) contentView.findViewById(R.id.tv_invoice_id);
        tvGrandTotal = (TextView) contentView.findViewById(R.id.tv_text_grand_total);
        tvPaidAmount = (TextView) contentView.findViewById(R.id.tv_text_paid_amount);
        tvBalanceAmount = (TextView) contentView.findViewById(R.id.tv_text_balance_amount);
        containerInvoice = (LinearLayout) contentView.findViewById(R.id.container_invoice);
    }

    private void initListeners() {

    }

    @Override
    public void onClick(View v) {

    }
}