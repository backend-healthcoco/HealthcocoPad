package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandResponse;
import com.healthcoco.healthcocopad.listeners.SelectedBrandListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;

public class SelectedBrandListViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener {

    private SelectedBrandListener selectedBrandListener;
    private ImageView ivCancelSelectedBrand;
    private VaccineBrandResponse vaccineBrandResponse;
    private TextView tvSelectedBrandName;

    public SelectedBrandListViewHolder(HealthCocoActivity mActivity, View convertView, HealthcocoRecyclerViewItemClickListener onItemClickListener, Object listenerObject) {
        super(mActivity, convertView, onItemClickListener, listenerObject);
        this.selectedBrandListener = (SelectedBrandListener) listenerObject;
    }

    @Override
    public void initViews(View itemView) {
        tvSelectedBrandName = (TextView) itemView.findViewById(R.id.tv_selected_brand_name);
        ivCancelSelectedBrand = (ImageView) itemView.findViewById(R.id.iv_cancel_selected_brand);
        ivCancelSelectedBrand.setOnClickListener(this);
    }

    @Override
    public void applyData(Object object) {
        vaccineBrandResponse = (VaccineBrandResponse) object;
        if (vaccineBrandResponse != null) {
            tvSelectedBrandName.setText(vaccineBrandResponse.getVaccineBrand().getName());

            if (vaccineBrandResponse.getVaccineBrand() != null && !Util.isNullOrBlank(vaccineBrandResponse.getVaccineBrand().getName()))
                tvSelectedBrandName.setText(Util.getValidatedValue(vaccineBrandResponse.getVaccineBrand().getName()));
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.iv_cancel_selected_brand) {
            selectedBrandListener.isBrandCancel(vaccineBrandResponse.getVaccineBrandId(), vaccineBrandResponse);
        }
    }
}
