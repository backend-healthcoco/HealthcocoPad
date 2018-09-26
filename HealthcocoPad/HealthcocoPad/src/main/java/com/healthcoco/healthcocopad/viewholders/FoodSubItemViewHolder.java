package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.server.Meal;
import com.healthcoco.healthcocopad.enums.KioskSubItemType;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Prashant on 23-06-18.
 */

public class FoodSubItemViewHolder extends HealthcocoComonRecylcerViewHolder {

    private HealthCocoActivity mActivity;
    private Meal mealItem;
    private TextView tvTitle;
    private TextView tvQuantity;
    private TextView tvCalarie;


    public FoodSubItemViewHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener) {
        super(mActivity, itemView, itemClickListener);
        this.mActivity = mActivity;
    }

    @Override
    public void initViews(View itemView) {

        tvTitle = (TextView) itemView.findViewById(R.id.tv_title_food);
        tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity_food);
        tvCalarie = (TextView) itemView.findViewById(R.id.tv_calarie_food);


        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(mealItem);
                }
            });

    }

    @Override
    public void applyData(Object object) {
        mealItem = (Meal) object;
        if (mealItem != null) {
            tvTitle.setText(mealItem.getName());

            if (mealItem.getQuantity() != null) {
                MealQuantity quantity = mealItem.getQuantity();

                String quantityType = quantity.getType().getQuantityType();
                if (!Util.isNullOrZeroNumber(quantity.getValue())) {
                    tvQuantity.setText(Util.getValidatedValue(quantity.getValue()) + quantityType);
                }
            } else {
                tvQuantity.setText("");
            }
            tvCalarie.setText("120Kcal");
        }
    }


}
