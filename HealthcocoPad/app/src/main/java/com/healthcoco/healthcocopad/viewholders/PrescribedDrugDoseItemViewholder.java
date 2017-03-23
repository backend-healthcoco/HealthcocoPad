package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 23-03-2017.
 */
public class PrescribedDrugDoseItemViewholder extends LinearLayout {

    private HealthCocoApplication mApp;
    private TextView tvName;
    private TextView tvFrequency;
    private TextView tvDuration;
    private DrugItem drugItem;
    private TextView tvDirection;
    private TextView tvInstruction;

    public PrescribedDrugDoseItemViewholder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public PrescribedDrugDoseItemViewholder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PrescribedDrugDoseItemViewholder(Context context) {
        super(context);
        this.mApp = ((HealthCocoApplication) context.getApplicationContext());
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.item_prescribed_drug_dose, this);
        initViews();
    }

    private void initViews() {
        tvName = (TextView) findViewById(R.id.tv_drug_name);
        tvFrequency = (TextView) findViewById(R.id.tv_drug_frequency);
        tvDuration = (TextView) findViewById(R.id.tv_drug_duration);
        tvDirection = (TextView) findViewById(R.id.tv_drug_direction);
        tvInstruction = (TextView) findViewById(R.id.tv_drug_instruction);
    }

    public void setData(DrugItem item) {
        this.drugItem = item;
        Drug drug = null;
        if (drugItem.getDrug() != null) {
            drug = drugItem.getDrug();
            drugItem.setDrugId(drug.getUniqueId());
        } else if (!Util.isNullOrBlank(drugItem.getDrugId())) {
            drug = LocalDataServiceImpl.getInstance(mApp).getDrug(drugItem.getDrugId());
            drugItem.setDrug(drug);
        }
        //set Name
        if (drug != null) {
            String drugName = Util.getValidatedValue(drug.getDrugName());
            if (drug.getDrugType() != null && !Util.isNullOrBlank(drug.getDrugType().getType()))
                drugName = drug.getDrugType().getType() + " " + drugName;
            if (!Util.isNullOrBlank(drugName)) {
                tvName.setText(drugName);
            } else
                tvName.setText(getResources().getString(R.string.no_text_dash));
        }
        //set Frequency
        if (!Util.isNullOrBlank(drugItem.getDosage()))
            tvFrequency.setText(drugItem.getDosage());
        else tvFrequency.setText(getResources().getString(R.string.no_text_dash));

        //set Duration
        if (drugItem.getDuration() != null && drugItem.getDuration().getDurationUnit() != null
                && !Util.isNullOrBlank(drugItem.getDuration().getValue())
                && !Util.isNullOrBlank(drugItem.getDuration().getDurationUnit().getUnit()))
            tvDuration.setText(
                    drugItem.getDuration().getValue() + " " + drugItem.getDuration().getDurationUnit().getUnit());
        else tvDuration.setText(getResources().getString(R.string.no_text_dash));

        //set direction
        String direction = null;
        if (!Util.isNullOrEmptyList(drugItem.getDirection())) {
            direction = drugItem.getDirection().get(0).getDirection();
            tvDirection.setText(direction);
        } else tvDirection.setText(getResources().getString(R.string.no_text_dash));

        //set instruction
        String instruction = null;
        instruction = drugItem.getInstructions();
        if (!Util.isNullOrBlank(instruction))
            tvInstruction.setText(instruction);
        else tvInstruction.setText(getResources().getString(R.string.no_text_dash));
    }
}
