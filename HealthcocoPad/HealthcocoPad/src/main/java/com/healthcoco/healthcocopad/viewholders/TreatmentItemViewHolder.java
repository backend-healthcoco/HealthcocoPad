package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.enums.PatientTreatmentStatus;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 24-03-2017.
 */
public class TreatmentItemViewHolder extends LinearLayout {

    private HealthCocoApplication mApp;
    private TextView tvName;
    private TextView tvQtyPerDay;
    private TextView tvCost;
    private Treatments treatments;
    private TextView tvDiscount;
    private TextView tvStatus;
    private TextView tvTotal;

    public TreatmentItemViewHolder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public TreatmentItemViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public TreatmentItemViewHolder(Context context) {
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
        tvTotal = (TextView) findViewById(R.id.tv_total_rupees);
    }

    public void setData(Treatments item) {
        this.treatments = item;

        //set Name
        if (treatments != null) {
            String drugName = Util.getValidatedValue(treatments.getTreatmentService().getName());
            if (!Util.isNullOrBlank(drugName)) {
                tvName.setText(drugName);
            } else
                tvName.setText(getResources().getString(R.string.no_text_dash));
        }

        //set QtyPerDay
        if (treatments.getQuantity() != null)
            tvQtyPerDay.setText(Util.getValidatedValue(treatments.getQuantity().getValue()));
        else tvQtyPerDay.setText(getResources().getString(R.string.no_text_dash));

        //set Cost
        if (treatments.getCost() != 0)
            tvCost.setText(Util.getIntValue(treatments.getCost()));
        else tvCost.setText(getResources().getString(R.string.no_text_dash));

        //set discount
        if (treatments.getDiscount() != null && treatments.getDiscount().getUnit() != null
                && treatments.getDiscount().getValue() != 0
                && treatments.getDiscount().getUnit() != null)
            tvDiscount.setText((getResources().getString(R.string.rupees) + " " + treatments.getDiscount().getValue()));
        else tvDiscount.setText(getResources().getString(R.string.no_text_dash));

        //set status
        PatientTreatmentStatus status = null;
        status = treatments.getStatus();
        if (status != null)
            tvStatus.setText(status.ordinal());
        else tvStatus.setText(getResources().getString(R.string.no_text_dash));

        //set Total Cost
        if (treatments.getFinalCost() != 0)
            tvTotal.setText(Util.getIntValue(treatments.getFinalCost()));
        else tvTotal.setText(getResources().getString(R.string.no_text_dash));
    }
}