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

public class EquivalentQuantityListItemViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener {

    public ArrayList<Object> quantityTypeList = new ArrayList<Object>() {{
        add(QuantityType.G);
        add(QuantityType.MILI_LITRE);
        add(QuantityType.MG);
        add(QuantityType.UG);
    }};
    private HealthCocoActivity mActivity;
    private EquivalentQuantities equivalentQuantity;

    private TextView tvType;
    private TextView tvServingType;
    private EditText editValue;

    public EquivalentQuantityListItemViewHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener, Object listenerObject) {
        super(mActivity, itemView, itemClickListener);
        this.mActivity = mActivity;
    }

    @Override
    public void initViews(View view) {

        tvType = view.findViewById(R.id.tv_type);
        tvServingType = view.findViewById(R.id.tv_serving_type);
        editValue = view.findViewById(R.id.edit_value);

        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(equivalentQuantity);
                }
            });
    }

    @Override
    public void applyData(Object object) {
        equivalentQuantity = (EquivalentQuantities) object;
        if (equivalentQuantity != null) {

            mActivity.initPopupWindows(tvType, PopupWindowType.NUTRIENT_TYPE, quantityTypeList, null);

            if (!Util.isNullOrZeroNumber(equivalentQuantity.getValue()))
                editValue.setText(Util.getValidatedValue(equivalentQuantity.getValue()));
            if (equivalentQuantity.getServingType() != null) {
                tvServingType.setText("1 " + equivalentQuantity.getServingType().getUnit());
                tvServingType.setTag(equivalentQuantity.getServingType());
            }
            if (equivalentQuantity.getType() != null) {
                tvType.setText(equivalentQuantity.getType().getUnit());
                tvType.setTag(equivalentQuantity.getType());
            }
        }

    }

    @Override
    public void onClick(View view) {
    }

}
