package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.BloodPressure;
import com.healthcoco.healthcocopad.bean.server.GrowthChartResponse;
import com.healthcoco.healthcocopad.listeners.GrowthChartListItemListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.Util;

public class GrowthChartViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener {
    private GrowthChartResponse objDta;
    private TextView tvDate;
    private TextView tvWeight;
    private TextView tvHeight;
    private TextView tvTemperature;
    private TextView tvBmi;
    private TextView tvBloodPressure;
    private TextView tvBloodSugarF;
    private TextView tvBloodSugarPP;
    private TextView tvBmd;
    private TextView tvHeadCircumfence;
    private LinearLayout btEdit;
    private LinearLayout btDiscard;
    private GrowthChartListItemListener growthChartListItemListener;

    public GrowthChartViewHolder(HealthCocoActivity mActivity, View convertView, HealthcocoRecyclerViewItemClickListener onItemClickListener, Object listenerObject) {
        super(mActivity, convertView, onItemClickListener, listenerObject);
        growthChartListItemListener = (GrowthChartListItemListener) listenerObject;
    }

    @Override
    public void initViews(View view) {
        tvDate = (TextView) view.findViewById(R.id.tv_date);
        tvWeight = (TextView) view.findViewById(R.id.tv_weight_kilograms);
        tvHeight = (TextView) view.findViewById(R.id.tv_height_cm);
        tvTemperature = (TextView) view.findViewById(R.id.tv_temp);
        tvHeadCircumfence = (TextView) view.findViewById(R.id.tv_head_circumference);
        tvBmi = (TextView) view.findViewById(R.id.tv_bmi);
        tvBloodPressure = (TextView) view.findViewById(R.id.tv_bp);
        tvBloodSugarF = (TextView) view.findViewById(R.id.tv_blood_sugar_f_mg_dl);
        tvBloodSugarPP = (TextView) view.findViewById(R.id.tv_blood_sugar_pp_mg_dl);
        tvBmd = (TextView) view.findViewById(R.id.tv_bmd);
        btEdit = (LinearLayout) view.findViewById(R.id.bt_edit_growth_chart);
        btDiscard = (LinearLayout) view.findViewById(R.id.bt_discard_growth_chart);

        btEdit.setOnClickListener(this);
        btDiscard.setOnClickListener(this);
    }

    @Override
    public void applyData(Object object) {
        objDta = (GrowthChartResponse) object;
        if (objDta != null) {
            if (objDta.getAge() != null)
                tvDate.setText(Util.getDOB(objDta.getAge()));
            if (objDta.getWeight() != null && objDta.getWeight() > 0)
                tvWeight.setText(":-" + Util.getFormattedDoubleNumber(objDta.getWeight()));
            else tvWeight.setText(R.string.no_text_dash);
            if (objDta.getWeight() != null && objDta.getWeight() > 0)
                tvHeight.setText(":-" + Util.getFormattedDoubleNumber(objDta.getHeight()));
            else tvHeight.setText(R.string.no_text_dash);
            if (objDta.getWeight() != null && objDta.getWeight() > 0)
                tvBmi.setText(":-" + Util.getFormattedDoubleNumber(objDta.getBmi()));
            else tvBmi.setText(R.string.no_text_dash);
            if (objDta.getSkullCircumference() != null && objDta.getSkullCircumference() > 0)
                tvHeadCircumfence.setText(":-" + Util.getFormattedDoubleNumber(objDta.getSkullCircumference()));
            else tvHeadCircumfence.setText(R.string.no_text_dash);
            if (!Util.isNullOrBlank(objDta.getTemperature()))
                tvTemperature.setText(":-" + objDta.getTemperature());
            else tvTemperature.setText(R.string.no_text_dash);
            if (!Util.isNullOrBlank(objDta.getBloodSugarF()))
                tvBloodSugarF.setText(":-" + objDta.getBloodSugarF());
            else tvBloodSugarF.setText(R.string.no_text_dash);
            if (!Util.isNullOrBlank(objDta.getBloodSugarPP()))
                tvBloodSugarPP.setText(":-" + objDta.getBloodSugarPP());
            else tvBloodSugarPP.setText(R.string.no_text_dash);
            if (!Util.isNullOrBlank(objDta.getBmd()))
                tvBmd.setText(":-" + objDta.getBmd());
            else tvBmd.setText(R.string.no_text_dash);
            BloodPressure bloodPressure = objDta.getBloodPressure();
            if (bloodPressure != null && !Util.isNullOrBlank(bloodPressure.getSystolic()) && !Util.isNullOrBlank(bloodPressure.getDiastolic())) {
                tvBloodPressure.setText(":-" + bloodPressure.getSystolic() + "/" + bloodPressure.getDiastolic());
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit_growth_chart:
                if (growthChartListItemListener != null)
                    growthChartListItemListener.editGrowthChart(objDta);
                break;
            case R.id.bt_discard_growth_chart:
                if (growthChartListItemListener != null)
                    growthChartListItemListener.discardGrowthChart(objDta);
                break;
        }
    }
}
