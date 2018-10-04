package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.server.RecipeResponse;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 15-07-2017.
 */

public class RecipeListSolrViewHolder extends HealthCocoViewHolder implements View.OnClickListener {

    private User user;
    private SelectedTreatmentItemClickListener selectedTreatmentItemClickListener;
    private HealthCocoActivity mActivity;
    private RecipeResponse objData;
    private TextView tvTitle;
    private TextView tvQuantity;
    private TextView tvCalarie;

    public RecipeListSolrViewHolder(HealthCocoActivity mActivity, SelectedTreatmentItemClickListener selectedTreatmentItemClickListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.selectedTreatmentItemClickListener = selectedTreatmentItemClickListener;
        user = selectedTreatmentItemClickListener.getUser();
    }

    @Override
    public void setData(Object object) {
        this.objData = (RecipeResponse) object;
    }

    @Override
    public void applyData() {
        if (objData != null) {
            tvTitle.setText(objData.getName());

            if (objData.getCalaries() != null) {
                MealQuantity calaries = objData.getCalaries();
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

    @Override
    public View getContentView() {
        LinearLayout contentView = (LinearLayout) inflater.inflate(R.layout.sub_item_add_food, null);
        tvTitle = (TextView) contentView.findViewById(R.id.tv_title_food);
        tvQuantity = (TextView) contentView.findViewById(R.id.tv_quantity_food);
        tvCalarie = (TextView) contentView.findViewById(R.id.tv_calarie_food);

        contentView.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onClick(View v) {
        selectedTreatmentItemClickListener.onTreatmentItemClick(objData);
    }
}
