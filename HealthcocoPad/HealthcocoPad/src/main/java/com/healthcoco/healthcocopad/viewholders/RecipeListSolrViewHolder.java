package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.server.RecipeResponse;
import com.healthcoco.healthcocopad.listeners.SelectedRecipeItemClickListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 15-07-2017.
 */

public class RecipeListSolrViewHolder extends HealthcocoComonRecylcerViewHolder {

    private SelectedRecipeItemClickListener selectedRecipeItemClickListener;
    private HealthCocoActivity mActivity;
    private RecipeResponse objData;
    private TextView tvTitle;
    private TextView tvQuantity;
    private TextView tvCalarie;

    public RecipeListSolrViewHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener, Object listenerObject) {
        super(mActivity, itemView, itemClickListener);
        this.mActivity = mActivity;
        this.selectedRecipeItemClickListener = (SelectedRecipeItemClickListener) listenerObject;
    }

    @Override
    public void initViews(View contentView) {
        tvTitle = (TextView) contentView.findViewById(R.id.tv_title_food);
        tvQuantity = (TextView) contentView.findViewById(R.id.tv_quantity_food);
        tvCalarie = (TextView) contentView.findViewById(R.id.tv_calarie_food);

        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedRecipeItemClickListener.onRecipeItemClicked(objData);
            }
        });
    }

    @Override
    public void applyData(Object object) {
        objData = (RecipeResponse) object;
        if (objData != null) {
            tvTitle.setText(objData.getName());

            if (objData.getCalories() != null) {
                MealQuantity calaries = objData.getCalories();
//                if (calaries.getType() != null)
                tvCalarie.setText(Util.getValidatedValue(calaries.getValue()) + mActivity.getString(R.string.cal_orange) /*+ calaries.getType().getQuantityType()*/);
            }

            if (objData.getQuantity() != null) {
                MealQuantity quantity = objData.getQuantity();
                if (quantity.getType() != null)
                    tvQuantity.setText(Util.getValidatedValue(quantity.getValue()) + quantity.getType().getUnit());
            }
        }
    }
}
