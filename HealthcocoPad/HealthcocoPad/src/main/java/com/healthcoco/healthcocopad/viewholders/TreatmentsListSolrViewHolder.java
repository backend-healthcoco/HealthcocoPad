package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 15-07-2017.
 */

public class TreatmentsListSolrViewHolder extends HealthCocoViewHolder implements View.OnClickListener {

    private User user;
    private SelectedTreatmentItemClickListener selectedTreatmentItemClickListener;
    private HealthCocoActivity mActivity;
    private TreatmentService objData;
    private TextView tvTreatmentName;
    private TextView tvTreatmentCost;

    public TreatmentsListSolrViewHolder(HealthCocoActivity mActivity, SelectedTreatmentItemClickListener selectedTreatmentItemClickListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.selectedTreatmentItemClickListener = selectedTreatmentItemClickListener;
        user = selectedTreatmentItemClickListener.getUser();
    }

    @Override
    public void setData(Object object) {
        this.objData = (TreatmentService) object;
    }

    @Override
    public void applyData() {
        if (user != null) {
            String treatmentName = objData.getName();
            tvTreatmentName.setText(treatmentName);
            if (objData.getCost() > 0) {
                tvTreatmentCost.setVisibility(View.VISIBLE);
                tvTreatmentCost.setText("(" + "\u20B9 " + Util.formatDoubleNumber(objData.getCost()) + ")");
            } else tvTreatmentCost.setVisibility(View.GONE);
        }
    }

    @Override
    public View getContentView() {
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.list_item_solr_treatment_item, null);
        tvTreatmentName = (TextView) contentView.findViewById(R.id.tv_treatment_name);
        tvTreatmentCost = (TextView) contentView.findViewById(R.id.tv_treatment_cost);
        contentView.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onClick(View v) {
        selectedTreatmentItemClickListener.onTreatmentItemClick(objData);
    }
}
