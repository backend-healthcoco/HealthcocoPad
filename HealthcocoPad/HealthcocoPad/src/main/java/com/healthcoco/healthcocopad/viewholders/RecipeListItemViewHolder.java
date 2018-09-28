package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.server.Meal;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

/**
 * Created by Prashant on 23-06-18.
 */

public class RecipeListItemViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener {

    private HealthCocoActivity mActivity;
    private Meal mealItem;
    private TextView tvTitle;
    private TextView tvQuantity;
    private TextView tvServingType;
    private TextView tvProtein;
    private TextView tvFat;
    private TextView tvCarbs;
    private TextView tvFiber;
    private LinearLayout containerIngredients;
    private TextViewFontAwesome btDelete;


    public RecipeListItemViewHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener) {
        super(mActivity, itemView, itemClickListener);
        this.mActivity = mActivity;
    }

    @Override
    public void initViews(View itemView) {

        tvTitle = (TextView) itemView.findViewById(R.id.tv_recipe_name);
        tvQuantity = (TextView) itemView.findViewById(R.id.tv_quantity);
        tvServingType = (TextView) itemView.findViewById(R.id.tv_serving_type);

        tvProtein = (TextView) itemView.findViewById(R.id.tv_protein);
        tvFat = (TextView) itemView.findViewById(R.id.tv_fat);
        tvCarbs = (TextView) itemView.findViewById(R.id.tv_carbs);
        tvFiber = (TextView) itemView.findViewById(R.id.tv_fiber);

        containerIngredients = (LinearLayout) itemView.findViewById(R.id.container_ingredient);

        btDelete = (TextViewFontAwesome) itemView.findViewById(R.id.bt_delete);


        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(mealItem);
                }
            });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
            tvTitle.setText("fnafklmavmmalalmlamklsam");
            tvServingType.setText("120Kcal");
        }
        tvTitle.setText("fnafklmavmmalalmlamklsam");
        tvServingType.setText("Cup");

        containerIngredients.removeAllViews();
        addIngredients();
        addIngredients();
        addIngredients();
        addIngredients();
    }

    private void addIngredients() {
//        containerIngredients.removeAllViews();
//        containerIngredients.setVisibility(View.VISIBLE);

        LinearLayout layoutSubItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_sub_item_selected_recipe, null);
        TextView tvTitle = (TextView) layoutSubItemPermission.findViewById(R.id.tv_ingredient_name);
        TextView tvQuantity = (TextView) layoutSubItemPermission.findViewById(R.id.tv_quantity_ingredient);
        TextView tvCalarie = (TextView) layoutSubItemPermission.findViewById(R.id.tv_serving_ingredient);
        TextViewFontAwesome btDelete = (TextViewFontAwesome) layoutSubItemPermission.findViewById(R.id.bt_delete_ingredient);
        btDelete.setTag("Delete_Sub_item");
        btDelete.setOnClickListener(this);
        tvTitle.setText("Milk");
        tvQuantity.setText("100");
        tvCalarie.setText("ml");

        containerIngredients.addView(layoutSubItemPermission);
    }


    @Override
    public void onClick(View view) {
        String tag = (String) view.getTag();
    }
}
