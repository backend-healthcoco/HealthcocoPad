package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.GenericName;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.Util;


public class DrugDoseItemViewHolder extends LinearLayout {

    private HealthCocoApplication mApp;
    private TextView tvName;
    private DrugItem drugItem;
    private TextView tvGenericName;
    private static final String GENERIC_NAME_SEPARATOR = ",";

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
        tvGenericName = (TextView) findViewById(R.id.tv_generic_name);
        tvName = (TextView) findViewById(R.id.tv_name);
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
            String genericNamesFormatted = "";
            if (!Util.isNullOrEmptyList(drug.getGenericNames())) {
                for (GenericName genericName : drug.getGenericNames()) {
                    int index = drug.getGenericNames().indexOf(genericName);
                    genericNamesFormatted = genericNamesFormatted + " " + genericName.getName();
                    if (index != drug.getGenericNames().size() - 1)
                        genericNamesFormatted = genericNamesFormatted + GENERIC_NAME_SEPARATOR;
                }
            }
            tvGenericName.setText(genericNamesFormatted);
        }
    }
}
