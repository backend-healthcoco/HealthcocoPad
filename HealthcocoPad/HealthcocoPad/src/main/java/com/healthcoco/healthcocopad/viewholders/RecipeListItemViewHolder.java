package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant on 23-06-18.
 */

public class RecipeListItemViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener, PopupWindowListener {

    private HealthCocoActivity mActivity;
    private DietPlanRecipeItem recipeItem;
    private TextView tvTitle;
    private TextView tvQuantity;
    private TextView tvServingType;
    private TextView tvProtein;
    private TextView tvFat;
    private TextView tvCarbs;
    private TextView tvFiber;
    private TextView tvTotalQuantity;
    private LinearLayout containerIngredients;
    private LinearLayout parentIngredients;
    private TextViewFontAwesome btDelete;
    private boolean isIngredientValue = false;


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

        parentIngredients = (LinearLayout) itemView.findViewById(R.id.parent_ingredient);
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
        recipeItem = (DietPlanRecipeItem) object;
        if (recipeItem != null) {
            tvTitle.setText(recipeItem.getName());

            if (recipeItem.getQuantity() != null) {
                MealQuantity quantity = recipeItem.getQuantity();

                if (!Util.isNullOrZeroNumber(quantity.getValue())) {
                    tvQuantity.setText(Util.getValidatedValue(quantity.getValue()));
                    tvServingType.setText(quantity.getType().getUnit());
                }
            } else {
                tvQuantity.setText("");
            }

            if (!Util.isNullOrEmptyList(recipeItem.getEquivalentMeasurements()))
                mActivity.initPopupWindows(tvServingType, PopupWindowType.SERVING_TYPE, (ArrayList<Object>) (Object) recipeItem.getEquivalentMeasurements(), this);

            mActivity.initPopupWindows(tvQuantity, PopupWindowType.QUANTITY_TYPE, PopupWindowType.QUANTITY_TYPE.getList(), this);

            if (recipeItem.getNutrientValueAtRecipeLevel() != null)
                isIngredientValue = recipeItem.getNutrientValueAtRecipeLevel();

            if (isIngredientValue)
                addIngredients(recipeItem.getIngredients());
            else parentIngredients.setVisibility(View.GONE);
        }

    }

    private void addIngredients(List<Ingredient> ingredientList) {
        parentIngredients.setVisibility(View.VISIBLE);
        containerIngredients.removeAllViews();
        for (Ingredient ingredient : ingredientList) {
            if (ingredient != null) {
                LinearLayout layoutSubItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_sub_item_selected_ingredient, null);
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
                        tvItemCalarie.setText(Util.getValidatedValue(ingredient.getCalaries().getValue()) + calariesType);
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

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        if (object instanceof EquivalentQuantities) {
            EquivalentQuantities equivalentQuantities = (EquivalentQuantities) object;
            tvServingType.setText(equivalentQuantities.getServingType().getUnit());
        }
    }

    @Override
    public void onEmptyListFound() {

    }
}
