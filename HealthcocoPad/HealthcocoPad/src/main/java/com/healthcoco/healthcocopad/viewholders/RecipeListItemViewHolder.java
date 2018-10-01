package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.bean.server.Meal;
import com.healthcoco.healthcocopad.bean.server.RecipeResponse;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.List;

/**
 * Created by Prashant on 23-06-18.
 */

public class RecipeListItemViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener {

    private HealthCocoActivity mActivity;
    private RecipeResponse recipeItem;
    private TextView tvTitle;
    private TextView tvQuantity;
    private TextView tvServingType;
    private TextView tvProtein;
    private TextView tvFat;
    private TextView tvCarbs;
    private TextView tvFiber;
    private TextView tvTotalQuantity;
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

        tvTotalQuantity = (TextView) itemView.findViewById(R.id.tv_total_quantity);

        containerIngredients = (LinearLayout) itemView.findViewById(R.id.container_ingredient);

        btDelete = (TextViewFontAwesome) itemView.findViewById(R.id.bt_delete);


        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(recipeItem);
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
        recipeItem = (RecipeResponse) object;
        if (recipeItem != null) {
            tvTitle.setText(recipeItem.getName());

            if (recipeItem.getQuantity() != null) {
                MealQuantity quantity = recipeItem.getQuantity();

                String quantityType = quantity.getType().getQuantityType();
                if (!Util.isNullOrZeroNumber(quantity.getValue())) {
                    tvQuantity.setText(Util.getValidatedValue(quantity.getValue()));
                    tvServingType.setText(quantityType);
                }
            } else {
                tvQuantity.setText("");

            }

            addIngredients(recipeItem.getIngredients());
        }

    }

    private void addIngredients(List<Ingredient> ingredientList) {

        containerIngredients.removeAllViews();
        for (Ingredient ingredient : ingredientList) {
            if (ingredient != null) {
                LinearLayout layoutSubItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_sub_item_selected_recipe, null);
                TextView tvItemTitle = (TextView) layoutSubItemPermission.findViewById(R.id.tv_ingredient_name);
                TextView tvItemQuantity = (TextView) layoutSubItemPermission.findViewById(R.id.tv_quantity_ingredient);
                TextView tvItemCalarie = (TextView) layoutSubItemPermission.findViewById(R.id.tv_serving_ingredient);
                TextViewFontAwesome btItemDelete = (TextViewFontAwesome) layoutSubItemPermission.findViewById(R.id.bt_delete_ingredient);
                btItemDelete.setOnClickListener(this);
                btItemDelete.setVisibility(View.GONE);

                tvItemTitle.setText(ingredient.getName());

                String quantityType = ingredient.getType().getQuantityType();
                if (!Util.isNullOrZeroNumber(ingredient.getValue())) {
                    tvItemQuantity.setText(Util.getValidatedValue(ingredient.getValue()) + quantityType);
                }
                if (ingredient.getCalaries() != null) {
                    String calariesType = ingredient.getCalaries().getType().getQuantityType();
                    if (!Util.isNullOrZeroNumber(ingredient.getCalaries().getValue())) {
                        tvItemQuantity.setText(Util.getValidatedValue(ingredient.getCalaries().getValue()) + calariesType);
                    }
                }
                containerIngredients.addView(layoutSubItemPermission);
            }
        }
    }


    @Override
    public void onClick(View view) {
        String tag = (String) view.getTag();
    }
}
