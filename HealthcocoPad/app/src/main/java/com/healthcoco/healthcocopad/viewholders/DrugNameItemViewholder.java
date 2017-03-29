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
 * Created by neha on 28/11/15.
 */
public class DrugNameItemViewholder extends LinearLayout {

    private HealthCocoApplication mApp;
    private DrugItem drugItem;
    private TextView tvDrugName;

    public DrugNameItemViewholder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public DrugNameItemViewholder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public DrugNameItemViewholder(Context context) {
        super(context);
        this.mApp = ((HealthCocoApplication) context.getApplicationContext());
        init(context);
    }

    private void init(Context context) {
        inflate(context, R.layout.item_drug_name, this);
        initViews();
    }

    private void initViews() {
        tvDrugName = (TextView) findViewById(R.id.tv_drug_name);
    }


    public void setData(DrugItem item) {
        this.drugItem = item;
        Drug drug = null;
        if (drugItem.getDrug() != null) {
            drug = drugItem.getDrug();
        } else if (!Util.isNullOrBlank(drugItem.getDrugId()))
            drug = LocalDataServiceImpl.getInstance(mApp).getDrug(drugItem.getDrugId());
        if (drug != null) {
            String drugName = Util.getValidatedValue(drug.getDrugName());
            if (drug.getDrugType() != null && !Util.isNullOrBlank(drug.getDrugType().getType()))
                drugName = drug.getDrugType().getType() + " " + drugName;
            tvDrugName.setText(drugName);
        }
    }
}
