package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.enums.SelectDrugItemType;
import com.healthcoco.healthcocopad.listeners.SelectDrugItemClickListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 17/12/15.
 */
public class SelectDrugListSolrViewHolder extends HealthCocoViewHolder implements View.OnClickListener {

    private SelectDrugItemClickListener drugItemClickListener;
    private HealthCocoActivity mActivity;
    private DrugsListSolrResponse objData;
    private TextView tvDrugName;

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
    }

    @Override
    public View getContentView() {
        tvDrugName = (TextView) inflater.inflate(R.layout.list_item_select_drug_item, null);
        tvDrugName.setOnClickListener(this);
        return tvDrugName;
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
