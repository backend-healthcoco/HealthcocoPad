package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.server.Nutrients;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;

/**
 * Created by Prashant on 23-06-18.
 */

public class NutrientListItemViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener, PopupWindowListener {

    private HealthCocoActivity mActivity;
    private Nutrients nutrient;
    private TextView tvName;
    private EditText editValue;
    private TextView tvType;

    public NutrientListItemViewHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener, Object listenerObject) {
        super(mActivity, itemView, itemClickListener);
        this.mActivity = mActivity;
    }

    @Override
    public void initViews(View itemView) {

        tvName = itemView.findViewById(R.id.tv_name);
        editValue = itemView.findViewById(R.id.edit_value);
        tvType = itemView.findViewById(R.id.tv_type);

        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(nutrient);
                }
            });
    }

    @Override
    public void applyData(Object object) {
        nutrient = (Nutrients) object;
        if (nutrient != null) {

            mActivity.initPopupWindows(tvType, PopupWindowType.NUTRIENT_TYPE, PopupWindowType.MICRO_NUTRIENT_TYPE.getList(), this);
            if (!Util.isNullOrBlank(nutrient.getName()))
                tvName.setText(nutrient.getName());
            if (!Util.isNullOrZeroNumber(nutrient.getValue()))
                editValue.setText(Util.getValidatedValue(nutrient.getValue()));
            if (nutrient.getType() != null) {
                tvType.setText(nutrient.getType().getUnit());
                tvType.setTag(nutrient.getType());
            } else {
                tvType.setText(QuantityType.GM.getUnit());
                tvType.setTag(QuantityType.GM);
            }
            editValue.setTag(nutrient);
        }

    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
        }
    }

    @Override
    public void onEmptyListFound() {

    }
}
