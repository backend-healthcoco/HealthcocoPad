package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.Util;


public class DrugDoseItemViewHolder extends LinearLayout {

    private HealthCocoApplication mApp;
    private TextView tvName;
    private TextView tvDirections;
    private TextView tvFrequency;
    private TextView tvDuration;
    private TextView tvInstruction;
    private DrugItem drugItem;
    private View instructionView;
    private PopupWindow instructionsPopupWindow;
    private LinearLayout containerDirectionDosageDuration;

    public DrugDoseItemViewHolder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DrugDoseItemViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrugDoseItemViewHolder(Context context) {
        super(context);
        this.mApp = ((HealthCocoApplication) context.getApplicationContext());
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.item_tablet_dose, this);
        initViews();
    }

    private void initViews() {
        tvInstruction = (TextView) findViewById(R.id.tv_instruction);
        tvName = (TextView) findViewById(R.id.tv_name);
        tvDirections = (TextView) findViewById(R.id.tv_directions);
        tvFrequency = (TextView) findViewById(R.id.tv_frequency);
        tvDuration = (TextView) findViewById(R.id.tv_duration);
        containerDirectionDosageDuration = (LinearLayout) findViewById(R.id.container_dosage_direction_duration);
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
        if (drug != null) {
            String drugName = Util.getValidatedValue(drug.getDrugName());
            if (drug.getDrugType() != null && !Util.isNullOrBlank(drug.getDrugType().getType()))
                drugName = drug.getDrugType().getType() + " " + drugName;
            tvName.setText(drugName);
        }
        if (!Util.isNullOrEmptyList(drugItem.getDirection())) {
            DrugDirection direction = drugItem.getDirection().get(0);
            tvDirections.setText(Util.getValidatedValue((direction.getDirection())));
        } else {
            tvDirections.setVisibility(View.GONE);
            tvDirections.setText("");
        }

        if (!Util.isNullOrBlank(drugItem.getDosage()))
            tvFrequency.setText(drugItem.getDosage());
        else {
            tvFrequency.setText("");
            tvFrequency.setVisibility(View.GONE);
        }

        if (drugItem.getDuration() != null && drugItem.getDuration().getDurationUnit() != null
                && !Util.isNullOrBlank(drugItem.getDuration().getValue())
                && !Util.isNullOrBlank(drugItem.getDuration().getDurationUnit().getUnit()))
            tvDuration.setText(
                    drugItem.getDuration().getValue() + " " + drugItem.getDuration().getDurationUnit().getUnit());
        else {
            tvDuration.setVisibility(View.GONE);
            tvDuration.setText("");
        }
        if (Util.isNullOrEmptyList(drugItem.getDirection()) && Util.isNullOrBlank(drugItem.getDosage()) && (drugItem.getDuration() == null || drugItem.getDuration().getDurationUnit() == null)) {
            containerDirectionDosageDuration.setVisibility(View.GONE);
        } else
            containerDirectionDosageDuration.setVisibility(View.VISIBLE);

        //initialising instructions popup
        if (!Util.isNullOrBlank(item.getInstructions())) {
            tvInstruction.setVisibility(View.VISIBLE);
            tvInstruction.setText(Util.getValidatedValue(item.getInstructions()));
        } else
            tvInstruction.setVisibility(View.GONE);
    }
}
