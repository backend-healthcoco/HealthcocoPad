package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.InvoiceItem;
import com.healthcoco.healthcocopad.bean.server.TreatmentFields;
import com.healthcoco.healthcocopad.enums.InvoiceItemType;
import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;
import com.healthcoco.healthcocopad.enums.UnitType;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.List;

/**
 * Created by Shreshtha on 05-07-2017.
 */

public class InvoiceListSubItemViewHolder extends HealthCocoViewHolder {
    private InvoiceItem invoiceItem;
    private TextViewFontAwesome textviewFontawesomeInvoiceIcon;
    private TextView tvTextInvoiceTypeName;
    private TextView tvTextQtyPerDay;
    private TextView tvTextTotalRuppes;
    private HealthCocoActivity mActivity;
    private TextView tvTextInvoiceStatus;
    private TextView tvCost;
    private TextView tvDiscount;
    private TextView tvTax;
    private LinearLayout layoutTreatmentToothNo;
    private LinearLayout layoutTreatmentMaterial;
    private TextView tvTreatmentToothNo;
    private TextView tvTreatmentMaterial;

    public InvoiceListSubItemViewHolder(HealthCocoActivity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
    }

    @Override
    public void setData(Object object) {
        this.invoiceItem = (InvoiceItem) object;
    }

    @Override
    public void applyData() {
        if (invoiceItem.getType().equals(InvoiceItemType.PRODUCT)) {
            textviewFontawesomeInvoiceIcon.setText(R.string.fa_product);
        } else if (invoiceItem.getType().equals(InvoiceItemType.TEST)) {
            textviewFontawesomeInvoiceIcon.setText(R.string.fa_test);
        } else if (invoiceItem.getType().equals(InvoiceItemType.SERVICE)) {
            textviewFontawesomeInvoiceIcon.setText(R.string.fa_service);
        }
        tvTextInvoiceTypeName.setText(String.valueOf(invoiceItem.getName()));

        if (invoiceItem.getQuantity() != null) {
            String formattedQtyPerDay = null;
            if (invoiceItem.getCost() > 0)
                formattedQtyPerDay = String.valueOf(invoiceItem.getQuantity().getValue());
            else formattedQtyPerDay = String.valueOf(invoiceItem.getQuantity().getValue());
            tvTextQtyPerDay.setText(formattedQtyPerDay);
        } else
            tvTextQtyPerDay.setText("1");

        if (invoiceItem.getDiscount() != null) {
            String formattedDiscount = String.valueOf(Util.formatDoubleNumber(invoiceItem.getDiscount().getValue()));
            UnitType unitType = invoiceItem.getDiscount().getUnit();
            if (unitType.equals(UnitType.INR))
                tvDiscount.setText(formattedDiscount + " INR");
            else tvDiscount.setText(formattedDiscount + " %");
        } else tvDiscount.setText("0 %");

        if (invoiceItem.getTax() != null) {
            String formattedTaxText = String.valueOf(Util.formatDoubleNumber(invoiceItem.getTax().getValue()));
            UnitType unitType = invoiceItem.getTax().getUnit();
            if (unitType.equals(UnitType.INR))
                tvTax.setText(formattedTaxText + " INR");
            else tvTax.setText(formattedTaxText + "  %");
        } else tvTax.setText("0 %");

        tvCost.setText(String.valueOf("\u20B9 " + Util.formatDoubleNumber(invoiceItem.getCost())));

        if (invoiceItem.getFinalCost() > 0)
            tvTextTotalRuppes.setText(String.valueOf("\u20B9 " + Util.formatDoubleNumber(invoiceItem.getFinalCost())));
        else tvTextTotalRuppes.setText(String.valueOf("\u20B9 " + 0));

        if (invoiceItem.getType().equals(InvoiceItemType.SERVICE)) {
            if (invoiceItem.getStatus() != null && invoiceItem.getStatus() instanceof PatientTreatmentStatus) {
                PatientTreatmentStatus status = invoiceItem.getStatus();
                tvTextInvoiceStatus.setText(status.getTreamentStatus());
                tvTextInvoiceStatus.setVisibility(View.VISIBLE);
                switch (status) {
                    case NOT_STARTED:
                        tvTextInvoiceStatus.setTextColor(mActivity.getResources().getColor(R.color.grey_light_text));
                        break;
                    case IN_PROGRESS:
                        tvTextInvoiceStatus.setTextColor(mActivity.getResources().getColor(R.color.orange));
                        break;
                    case COMPLETED:
                        tvTextInvoiceStatus.setTextColor(mActivity.getResources().getColor(R.color.green_logo));
                        break;
                }
            } else
                tvTextInvoiceStatus.setText("--");

            if (!Util.isNullOrEmptyList(invoiceItem.getTreatmentFields())) {
                List<TreatmentFields> treatmentFieldsList = invoiceItem.getTreatmentFields();
                for (TreatmentFields treatmentField :
                        treatmentFieldsList) {
                    if (treatmentField.getKey().equals(HealthCocoConstants.TAG_TOOTH_NUMBER)
                            && !Util.isNullOrBlank(treatmentField.getValue())) {
                        tvTreatmentToothNo.setText(treatmentField.getValue());
                        layoutTreatmentToothNo.setVisibility(View.VISIBLE);
                    } else {
                        layoutTreatmentToothNo.setVisibility(View.GONE);
                        tvTreatmentToothNo.setText("");
                    }

                    if (treatmentField.getKey().equals(HealthCocoConstants.TAG_TOOTH_MATERIAL)
                            && !Util.isNullOrBlank(treatmentField.getValue())) {
                        if (treatmentField.getKey().equals(HealthCocoConstants.TAG_TOOTH_MATERIAL)) {
                            tvTreatmentMaterial.setText(treatmentField.getValue());
                            layoutTreatmentMaterial.setVisibility(View.VISIBLE);
                        } else {
                            layoutTreatmentMaterial.setVisibility(View.GONE);
                            tvTreatmentMaterial.setText("");
                        }
                    }
                }
            } else {
                layoutTreatmentToothNo.setVisibility(View.GONE);
                layoutTreatmentMaterial.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.sub_item_invoice, null);
        initViews(contentView);
        return contentView;
    }

    private void initViews(View contentView) {
        textviewFontawesomeInvoiceIcon = (TextViewFontAwesome) contentView.findViewById(R.id.textview_fontawesome_invoice_icon);
        tvTextInvoiceTypeName = (TextView) contentView.findViewById(R.id.tv_text_invoice_type_name);
        tvTextQtyPerDay = (TextView) contentView.findViewById(R.id.tv_qty_per_day);
        tvCost = (TextView) contentView.findViewById(R.id.tv_cost);
        tvDiscount = (TextView) contentView.findViewById(R.id.tv_discount);
        tvTax = (TextView) contentView.findViewById(R.id.tv_tax);
        tvTextTotalRuppes = (TextView) contentView.findViewById(R.id.tv_total_ruppes);
        tvTextInvoiceStatus = (TextView) contentView.findViewById(R.id.tv_text_invoice_status);
        tvTreatmentToothNo = (TextView) contentView.findViewById(R.id.tv_tooth_numbers);
        tvTreatmentMaterial = (TextView) contentView.findViewById(R.id.tv_tooth_material);
        layoutTreatmentToothNo = (LinearLayout) contentView.findViewById(R.id.layout_treatment_tooth_no);
        layoutTreatmentMaterial = (LinearLayout) contentView.findViewById(R.id.layout_treatment_material);
    }
}
