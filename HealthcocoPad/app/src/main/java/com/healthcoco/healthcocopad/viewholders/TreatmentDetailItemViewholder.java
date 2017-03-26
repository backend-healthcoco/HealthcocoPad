package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by shreshtha on 3/26/2017.
 */
public class TreatmentDetailItemViewholder extends LinearLayout {

    private HealthCocoApplication mApp;
    private TextView tvName;
    private TextView tvQtyPerDay;
    private TextView tvCost;
    private Treatments treatments;
    private TextView tvDiscount;
    private TextView tvStatus;
    private View divider;
    private TextView tvTotalRupees;

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
    }

    public void setData(Treatments item) {
        this.treatments = item;
        TreatmentService treatmentService = null;
        if (treatments.getTreatmentServices() != null) {
            treatmentService = treatments.getTreatmentService();
            treatments.setId(treatmentService.getId());
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
        if (treatments.getQuantity().getValue() != 0)
            tvQtyPerDay.setText(treatments.getQuantity().getValue());
        else tvQtyPerDay.setText(getResources().getString(R.string.no_text_dash));

        //set Duration
        double cost = treatments.getCost();
        if (cost != 0) {
            tvCost.setText(Util.getIntValue(cost));
        } else tvCost.setText(getResources().getString(R.string.no_text_dash));

        //set direction
        String direction = null;
        if (treatments.getDiscount() != null && treatments.getDiscount().getValue() != 0
                && treatments.getDiscount().getUnit() != null)
            tvDiscount.setText(treatments.getDiscount().getUnit() + " " + treatments.getDiscount().getValue());
        else tvDiscount.setText(getResources().getString(R.string.no_text_dash));

        //set instruction
        PatientTreatmentStatus status = null;
        status = treatments.getStatus();
        if (status != null)
            tvStatus.setText(status.toString());
        else tvStatus.setText(getResources().getString(R.string.no_text_dash));

        //set Total Cost
        double finalCost = treatments.getFinalCost();
        if (finalCost != 0) {
            tvTotalRupees.setText(Util.getIntValue(finalCost));
        } else tvTotalRupees.setText(getResources().getString(R.string.no_text_dash));
    }
}
