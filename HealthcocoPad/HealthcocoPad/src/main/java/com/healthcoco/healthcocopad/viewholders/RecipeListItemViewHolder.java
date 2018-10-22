package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.listeners.SelectedRecipeItemClickListener;
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
    private TextViewFontAwesome iconAnalyse;
    private TextView tvAnalyse;
    private LinearLayout btAnalyse;
    private boolean isIngredientValue = false;
    private EquivalentQuantities equivalentQuantities;
    private SelectedRecipeItemClickListener recipeItemClickListener;

    public RecipeListItemViewHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener, Object listenerObject) {
        super(mActivity, itemView, itemClickListener);
        this.mActivity = mActivity;
        this.recipeItemClickListener = (SelectedRecipeItemClickListener) listenerObject;
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
        iconAnalyse = (TextViewFontAwesome) itemView.findViewById(R.id.icon_analyse);
        tvAnalyse = (TextView) itemView.findViewById(R.id.tv_analyse);
        btAnalyse = (LinearLayout) itemView.findViewById(R.id.bt_analyse);


        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(recipeItem);
                }
            });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recipeItemClickListener.onIngredientItemClicked(recipeItem);
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeItemClickListener.onDeleteItemClicked(recipeItem);
            }
        });

        btAnalyse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeItemClickListener.onAnalyseItemClicked(recipeItem);
            }
        });
    }

    @Override
    public void applyData(Object object) {
        recipeItem = (DietPlanRecipeItem) object;
        if (recipeItem != null) {
            tvTitle.setText(recipeItem.getName());

            if (recipeItem.getQuantity() != null && (!Util.isNullOrZeroNumber(recipeItem.getQuantity().getValue()))) {
                tvQuantity.setText(Util.getValidatedValue(recipeItem.getQuantity().getValue()));
                tvServingType.setText(recipeItem.getQuantity().getType().getUnit());
                mActivity.initPopupWindows(tvQuantity, PopupWindowType.QUANTITY_VALUE, PopupWindowType.QUANTITY_VALUE.getList(), this);
            } else {
                tvQuantity.setText("");
            }

            if (!Util.isNullOrEmptyList(recipeItem.getEquivalentMeasurements()))
                mActivity.initPopupWindows(tvServingType, PopupWindowType.SERVING_TYPE, (ArrayList<Object>) (Object) recipeItem.getEquivalentMeasurements(), this);
            else {
                tvServingType.setText("");
            }

            if (recipeItem.getNutrientValueAtRecipeLevel() != null)
                isIngredientValue = recipeItem.getNutrientValueAtRecipeLevel();

            if (isIngredientValue) {
                tvQuantity.setVisibility(View.GONE);
                tvServingType.setVisibility(View.GONE);
            }

            if (recipeItem.getCurrentQuantity() != null && recipeItem.getCalories() != null) {
                if (!isIngredientValue)
                    tvTotalQuantity.setText(Util.round(recipeItem.getCurrentQuantity().getValue(), 2) + recipeItem.getCurrentQuantity().getType().getUnit() + " - " +
                            Util.round(recipeItem.getCalories().getValue(), 2) + mActivity.getString(R.string.cal_orange));
                else
                    tvTotalQuantity.setText(Util.round(recipeItem.getCalories().getValue(), 2) + mActivity.getString(R.string.cal_orange));
            } else
                tvTotalQuantity.setText("");

            if (recipeItem.getProtein() != null)
                tvProtein.setText(Util.round(recipeItem.getProtein().getValue(), 2)/* + recipeItem.getProtein().getType().getUnit()*/ + "");
            else
                tvProtein.setText("");

            if (recipeItem.getFat() != null)
                tvFat.setText(Util.round(recipeItem.getFat().getValue(), 2)/* + recipeItem.getProtein().getType().getUnit()*/ + "");
            else
                tvFat.setText("");

            if (recipeItem.getCarbohydreate() != null)
                tvCarbs.setText(Util.round(recipeItem.getCarbohydreate().getValue(), 2)/* + recipeItem.getProtein().getType().getUnit()*/ + "");
            else
                tvCarbs.setText("");

            if (recipeItem.getFiber() != null)
                tvFiber.setText(Util.round(recipeItem.getFiber().getValue(), 2)/* + recipeItem.getProtein().getType().getUnit()*/ + "");
            else
                tvFiber.setText("");

            if (!Util.isNullOrEmptyList(recipeItem.getGeneralNutrients())) {
                tvAnalyse.setTextColor(mActivity.getResources().getColor(R.color.green_dark));
                iconAnalyse.setTextColor(mActivity.getResources().getColor(R.color.green_dark));
                iconAnalyse.setText(mActivity.getString(R.string.check_circle_analysed));
                tvAnalyse.setText(mActivity.getString(R.string.analysed));
            } else {
                tvAnalyse.setTextColor(mActivity.getResources().getColor(R.color.red_error));
                iconAnalyse.setTextColor(mActivity.getResources().getColor(R.color.red_error));
                iconAnalyse.setText(mActivity.getString(R.string.info_circle_analyse));
                tvAnalyse.setText(mActivity.getString(R.string.not_analysed));
            }

            if (!Util.isNullOrEmptyList(recipeItem.getIngredients()))
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

                String quantityType = ingredient.getType().getUnit();
                if (!Util.isNullOrZeroNumber(ingredient.getValue())) {
                    tvItemQuantity.setText(Util.getValidatedValue(ingredient.getValue()) + quantityType);
                }
                if (ingredient.getCalories() != null) {
                    if (ingredient.getCalories().getType() != null) {
                        String calariesType = ingredient.getCalories().getType().getQuantityType();
                        if (!Util.isNullOrZeroNumber(ingredient.getCalories().getValue())) {
                            tvItemCalarie.setText(Util.getValidatedValue(ingredient.getCalories().getValue()) + calariesType);
                        }
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
        switch (popupWindowType) {
            case SERVING_TYPE:
                if (object instanceof EquivalentQuantities) {
                    EquivalentQuantities equivalentQuantities = (EquivalentQuantities) object;
                    this.equivalentQuantities = equivalentQuantities;
                    tvServingType.setText(equivalentQuantities.getServingType().getUnit());
                    recipeItem.getTempQuantity().setType(equivalentQuantities.getServingType());
                    recipeItem.getCurrentQuantity().setValue(Util.getValidatedDoubleValue(tvQuantity) * equivalentQuantities.getValue());

                    break;
                }
            case QUANTITY_VALUE:
                if (object instanceof String) {
                    tvQuantity.setText((String) object);
                    if (equivalentQuantities != null) {
                        recipeItem.getCurrentQuantity().setValue(Util.getValidatedDoubleValue(tvQuantity) * equivalentQuantities.getValue());
                        recipeItem.getTempQuantity().setValue(Util.getValidatedDoubleValue(tvQuantity));
                    } else {
                        recipeItem.getCurrentQuantity().setValue((Util.getValidatedDoubleValue(tvQuantity) * recipeItem.getCurrentQuantity().getValue()) / recipeItem.getTempQuantity().getValue());
                        recipeItem.getTempQuantity().setValue(Util.getValidatedDoubleValue(tvQuantity));
                    }
                }
                break;
        }

        if (recipeItem.getProteinTemp() != null) {
            recipeItem.getProteinTemp().setValue((recipeItem.getProteinPerHundredUnit() * recipeItem.getCurrentQuantity().getValue()) / 100);
            tvProtein.setText(Util.round(recipeItem.getProteinTemp().getValue(), 2)/* + recipeItem.getProtein().getType().getUnit()*/ + "");
        }
        if (recipeItem.getFatTemp() != null) {
            recipeItem.getFatTemp().setValue((recipeItem.getFatPerHundredUnit() * recipeItem.getCurrentQuantity().getValue()) / 100);
            tvFat.setText(Util.round(recipeItem.getFatTemp().getValue(), 2)/* + recipeItem.getProtein().getType().getUnit()*/ + "");
        }
        if (recipeItem.getCarbohydreateTemp() != null) {
            recipeItem.getCarbohydreateTemp().setValue((recipeItem.getCarbohydreatePerHundredUnit() * recipeItem.getCurrentQuantity().getValue()) / 100);
            tvCarbs.setText(Util.round(recipeItem.getCarbohydreateTemp().getValue(), 2)/* + recipeItem.getProtein().getType().getUnit()*/ + "");
        }
        if (recipeItem.getFiberTemp() != null) {
            recipeItem.getFiberTemp().setValue((recipeItem.getFiberPerHundredUnit() * recipeItem.getCurrentQuantity().getValue()) / 100);
            tvFiber.setText(Util.round(recipeItem.getFiberTemp().getValue(), 2)/* + recipeItem.getProtein().getType().getUnit()*/ + "");
        }
        if (recipeItem.getCalariesTemp() != null) {
            recipeItem.getCalariesTemp().setValue((recipeItem.getCaloriesPerHundredUnit() * recipeItem.getCurrentQuantity().getValue()) / 100);
        }
        tvTotalQuantity.setText(Util.round(recipeItem.getCurrentQuantity().getValue(), 2) + recipeItem.getCurrentQuantity().getType().getUnit() + " - " +
                Util.round(recipeItem.getCalariesTemp().getValue(), 2) + mActivity.getString(R.string.cal_orange));
        recipeItemClickListener.onQuantityChanged(recipeItem);
    }

    @Override
    public void onEmptyListFound() {

    }
}
