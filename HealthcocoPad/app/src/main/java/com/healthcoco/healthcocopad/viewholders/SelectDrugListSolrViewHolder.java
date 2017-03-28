package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.bean.server.GenericName;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;
import com.healthcoco.healthcocopad.listeners.SelectDrugItemClickListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 17/12/15.
 */
public class SelectDrugListSolrViewHolder extends HealthCocoViewHolder implements View.OnClickListener {

    private static final String GENERIC_NAME_SEPARATOR = "+";
    private SelectDrugItemClickListener drugItemClickListener;
    private HealthCocoActivity mActivity;
    private DrugsListSolrResponse objData;
    private TextView tvDrugName;
    private TextView tvGenericName;
    private LinearLayout containerDrugItem;

    public SelectDrugListSolrViewHolder(HealthCocoActivity mActivity, SelectDrugItemClickListener drugItemClickListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.drugItemClickListener = drugItemClickListener;
    }

    @Override
    public void setData(Object object) {
        this.objData = (DrugsListSolrResponse) object;
    }

    @Override
    public void applyData() {
        String drugName = objData.getDrugName();
        if (!Util.isNullOrBlank(objData.getDrugType()))
            drugName = objData.getDrugType() + " " + drugName;
        tvDrugName.setText(drugName);
        String genericNamesFormatted = "";
        if (!Util.isNullOrEmptyList(objData.getGenericNames())) {
            for (GenericName genericName : objData.getGenericNames()) {
                int index = objData.getGenericNames().indexOf(genericName);
                genericNamesFormatted = genericNamesFormatted + " " + genericName.getName();
                if (index != objData.getGenericNames().size() - 1)
                    genericNamesFormatted = genericNamesFormatted + GENERIC_NAME_SEPARATOR;
            }
            tvGenericName.setText(genericNamesFormatted);
        }
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.list_item_select_drug_item, null);
        initViews(contentView);
        initListeners();
        return contentView;
    }

    private void initListeners() {
        containerDrugItem.setOnClickListener(this);
    }

    private void initViews(View contentView) {
        containerDrugItem = (LinearLayout) contentView.findViewById(R.id.container_drug_item);
        tvDrugName = (TextView) contentView.findViewById(R.id.tv_drug_name);
        tvGenericName = (TextView) contentView.findViewById(R.id.tv_generic_name);
    }

    @Override
    public void onClick(View v) {
        if (!Util.isNullOrEmptyList(objData.getDirection()))
            LocalDataServiceImpl.getInstance(mApp).addDirection(objData.getDirection().get(0));
        if (objData.getDuration() != null && objData.getDuration().getDurationUnit() != null)
            LocalDataServiceImpl.getInstance(mApp).addDurationUnit(objData.getDuration().getDurationUnit());
        objData.save();
        drugItemClickListener.ondrugItemClick(SelectDrugItemType.ALL_DRUGS, objData);
    }
}
