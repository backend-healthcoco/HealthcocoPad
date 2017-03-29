package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 29-03-2017.
 */
public class DrugDoseTemplateItemViewHolder extends LinearLayout implements View.OnClickListener {

    private HealthCocoApplication mApp;
    private TextView tvName;
    private TextView tvDirections;
    private TextView tvFrequency;
    private TextView tvDuration;
    private TextView tvInstruction;
    private Drug drug;
    private LinearLayout containerDirectionDosageDuration;

    public DrugDoseTemplateItemViewHolder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DrugDoseTemplateItemViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrugDoseTemplateItemViewHolder(Context context) {
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

    public void setData(Drug item) {
        this.drug = item;

        if (drug != null) {
            String drugName = Util.getValidatedValue(drug.getDrugName());
            if (drug.getDrugType() != null && !Util.isNullOrBlank(drug.getDrugType().getType()))
                drugName = drug.getDrugType().getType() + " " + drugName;
            tvName.setText(drugName);
        }
        if (!Util.isNullOrEmptyList(drug.getDirection())) {
            DrugDirection direction = drug.getDirection().get(0);
            tvDirections.setText(Util.getValidatedValue((direction.getDirection())));
        } else {
            tvDirections.setVisibility(View.GONE);
            tvDirections.setText("");
        }

        if (!Util.isNullOrBlank(drug.getDosage()))
            tvFrequency.setText(drug.getDosage());
        else {
            tvFrequency.setText("");
            tvFrequency.setVisibility(View.GONE);
        }

        if (drug.getDuration() != null && drug.getDuration().getDurationUnit() != null
                && !Util.isNullOrBlank(drug.getDuration().getValue())
                && !Util.isNullOrBlank(drug.getDuration().getDurationUnit().getUnit()))
            tvDuration.setText(
                    drug.getDuration().getValue() + " " + drug.getDuration().getDurationUnit().getUnit());
        else {
            tvDuration.setVisibility(View.GONE);
            tvDuration.setText("");
        }
        if (Util.isNullOrEmptyList(drug.getDirection()) && Util.isNullOrBlank(drug.getDosage()) && (drug.getDuration() == null || drug.getDuration().getDurationUnit() == null)) {
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

    @Override
    public void onClick(View v) {
    }
}
