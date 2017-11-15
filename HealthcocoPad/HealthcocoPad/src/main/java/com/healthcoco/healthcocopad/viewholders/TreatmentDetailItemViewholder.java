package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.TreatmentFields;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.List;

/**
 * Created by shreshtha on 3/26/2017.
 */
public class TreatmentDetailItemViewholder extends LinearLayout {

    private HealthCocoApplication mApp;
    private TextView tvName;
    private TextView tvQtyPerDay;
    private TextView tvCost;
    private TreatmentItem treatmentItem;
    private TextView tvDiscount;
    private TextView tvStatus;
    private View divider;
    private TextView tvTotalRupees;
    private LinearLayout layoutTreatmentToothNo;
    private LinearLayout layoutTreatmentMaterial;
    private TextView tvTreatmentToothNo;
    private TextView tvTreatmentMaterial;

    public TreatmentDetailItemViewholder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TreatmentDetailItemViewholder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TreatmentDetailItemViewholder(Context context) {
        super(context);
        this.mApp = ((HealthCocoApplication) context.getApplicationContext());
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.item_treatment_list, this);
        initViews();
    }

    private void initViews() {
        tvName = (TextView) findViewById(R.id.tv_treatment_name);
        tvQtyPerDay = (TextView) findViewById(R.id.tv_qty_per_day);
        tvCost = (TextView) findViewById(R.id.tv_cost);
        tvDiscount = (TextView) findViewById(R.id.tv_discount);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvTotalRupees = (TextView) findViewById(R.id.tv_total_rupees);
        tvTreatmentToothNo = (TextView) findViewById(R.id.tv_tooth_numbers);
        tvTreatmentMaterial = (TextView) findViewById(R.id.tv_tooth_material);
        layoutTreatmentToothNo = (LinearLayout) findViewById(R.id.layout_treatment_tooth_no);
        layoutTreatmentMaterial = (LinearLayout) findViewById(R.id.layout_treatment_material);
    }

    public void setData(TreatmentItem item) {
        this.treatmentItem = item;
        TreatmentService treatmentService = null;
        if (treatmentItem.getTreatmentServices() != null) {
            treatmentService = treatmentItem.getTreatmentService();
            treatmentItem.setId(treatmentService.getId());
        }
//        else if (!Util.isNullOrBlank(treatments.getDrugId())) {
//            treatmentService = LocalDataServiceImpl.getInstance(mApp).getDrug(treatments.getDrugId());
//            treatments.setDrug(treatmentService);
//        }
        //set Name
        if (treatmentService != null) {
            String treatmentName = Util.getValidatedValue(treatmentService.getName());
            if (!Util.isNullOrBlank(treatmentName)) {
                tvName.setText(treatmentName);
            } else
                tvName.setText(getResources().getString(R.string.no_text_dash));
        }
        //set Quantity Per Day
        if (treatmentItem.getQuantity().getValue() != 0)
            tvQtyPerDay.setText(treatmentItem.getQuantity().getValue() + "");
        else tvQtyPerDay.setText(getResources().getString(R.string.no_text_dash));

        //set Duration
        double cost = treatmentItem.getCost();
        if (cost != 0) {
            tvCost.setText(Util.getIntValue(cost) + "");
        } else tvCost.setText(getResources().getString(R.string.no_text_dash));

        //set direction
        String direction = null;
        if (treatmentItem.getDiscount() != null && treatmentItem.getDiscount().getValue() != 0
                && treatmentItem.getDiscount().getUnit() != null)
            tvDiscount.setText(treatmentItem.getDiscount().getUnit() + " " + treatmentItem.getDiscount().getValue());
        else tvDiscount.setText(getResources().getString(R.string.no_text_dash));

        //set instruction
        PatientTreatmentStatus status = null;
        status = treatmentItem.getStatus();
        if (status != null)
            tvStatus.setText(status.toString());
        else tvStatus.setText(getResources().getString(R.string.no_text_dash));

        //set Total Cost
        double finalCost = treatmentItem.getFinalCost();
        if (finalCost != 0) {
            tvTotalRupees.setText(Util.getIntValue(finalCost) + "");
        } else tvTotalRupees.setText(getResources().getString(R.string.no_text_dash));

        if (!Util.isNullOrEmptyList(treatmentItem.getTreatmentFields())) {
            List<TreatmentFields> treatmentFieldsList = treatmentItem.getTreatmentFields();
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
