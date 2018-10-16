package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.listeners.SelectedRecipeItemClickListener;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoComonRecylcerViewHolder;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.ArrayList;

/**
 * Created by Prashant on 23-06-18.
 */

public class IngredientListItemViewHolder extends HealthcocoComonRecylcerViewHolder implements View.OnClickListener, PopupWindowListener {

    private HealthCocoActivity mActivity;
    private Ingredient ingredient;
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
    private EquivalentQuantities equivalentQuantities;
    private SelectedRecipeItemClickListener recipeItemClickListener;

    public IngredientListItemViewHolder(HealthCocoActivity mActivity, View itemView, HealthcocoRecyclerViewItemClickListener itemClickListener, Object listenerObject) {
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


        if (onItemClickListener != null)
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClicked(ingredient);
                }
            });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recipeItemClickListener.onDeleteItemClicked(ingredient);
            }
        });
    }

    @Override
    public void applyData(Object object) {
        ingredient = (Ingredient) object;
        if (ingredient != null) {
            tvTitle.setText(ingredient.getName());

            if (!Util.isNullOrZeroNumber(ingredient.getValue())) {
                tvQuantity.setText(Util.getValidatedValue(ingredient.getValue()));
                tvServingType.setText(ingredient.getType().getUnit());
                mActivity.initPopupWindows(tvQuantity, PopupWindowType.QUANTITY_TYPE, PopupWindowType.QUANTITY_TYPE.getList(), this);
            } else {
                tvQuantity.setText("");
            }

            if (!Util.isNullOrEmptyList(ingredient.getEquivalentMeasurements()))
                mActivity.initPopupWindows(tvServingType, PopupWindowType.SERVING_TYPE, (ArrayList<Object>) (Object) ingredient.getEquivalentMeasurements(), this);
            else {
                tvServingType.setText("");
            }

            if (ingredient.getCurrentQuantity() != null && ingredient.getCalories() != null)
                tvTotalQuantity.setText(ingredient.getCurrentQuantity().getValue() + ingredient.getCurrentQuantity().getType().getUnit() + " - " +
                        ingredient.getCalories().getValue() + mActivity.getString(R.string.cal_orange));
            else
                tvTotalQuantity.setText("");

            if (ingredient.getProtein() != null)
                tvProtein.setText(ingredient.getProtein().getValue()/* + ingredient.getProtein().getType().getUnit()*/ + "");
            else
                tvProtein.setText("");

            if (ingredient.getFat() != null)
                tvFat.setText(ingredient.getFat().getValue()/* + ingredient.getProtein().getType().getUnit()*/ + "");
            else
                tvFat.setText("");

            if (ingredient.getCarbohydreate() != null)
                tvCarbs.setText(ingredient.getCarbohydreate().getValue()/* + ingredient.getProtein().getType().getUnit()*/ + "");
            else
                tvCarbs.setText("");

            if (ingredient.getFiber() != null)
                tvFiber.setText(ingredient.getFiber().getValue()/* + ingredient.getProtein().getType().getUnit()*/ + "");
            else
                tvFiber.setText("");
            parentIngredients.setVisibility(View.GONE);
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
                    ingredient.getTempQuantity().setType(equivalentQuantities.getServingType());
                    ingredient.getCurrentQuantity().setValue(Util.getValidatedDoubleValue(tvQuantity) * equivalentQuantities.getValue());

                    break;
                }
            case QUANTITY_TYPE:
                if (object instanceof String) {
                    tvQuantity.setText((String) object);
                    if (equivalentQuantities != null) {
                        ingredient.getCurrentQuantity().setValue(Util.getValidatedDoubleValue(tvQuantity) * equivalentQuantities.getValue());
                        ingredient.getTempQuantity().setValue(Util.getValidatedDoubleValue(tvQuantity));
                    } else {
                        ingredient.getCurrentQuantity().setValue((Util.getValidatedDoubleValue(tvQuantity) * ingredient.getCurrentQuantity().getValue()) / ingredient.getTempQuantity().getValue());
                        ingredient.getTempQuantity().setValue(Util.getValidatedDoubleValue(tvQuantity));
                    }
                }
                break;
        }

        if (ingredient.getProteinTemp() != null) {
            ingredient.getProteinTemp().setValue((ingredient.getProteinPerHundredUnit() * ingredient.getCurrentQuantity().getValue()) / 100);
            tvProtein.setText(ingredient.getProteinTemp().getValue()/* + ingredient.getProtein().getType().getUnit()*/ + "");
        }
        if (ingredient.getFatTemp() != null) {
            ingredient.getFatTemp().setValue((ingredient.getFatPerHundredUnit() * ingredient.getCurrentQuantity().getValue()) / 100);
            tvFat.setText(ingredient.getFatTemp().getValue()/* + ingredient.getProtein().getType().getUnit()*/ + "");
        }
        if (ingredient.getCarbohydreateTemp() != null) {
            ingredient.getCarbohydreateTemp().setValue((ingredient.getCarbohydreatePerHundredUnit() * ingredient.getCurrentQuantity().getValue()) / 100);
            tvCarbs.setText(ingredient.getCarbohydreateTemp().getValue()/* + ingredient.getProtein().getType().getUnit()*/ + "");
        }
        if (ingredient.getFiberTemp() != null) {
            ingredient.getFiberTemp().setValue((ingredient.getFiberPerHundredUnit() * ingredient.getCurrentQuantity().getValue()) / 100);
            tvFiber.setText(ingredient.getFiberTemp().getValue()/* + ingredient.getProtein().getType().getUnit()*/ + "");
        }
        if (ingredient.getCaloriesTemp() != null) {
            ingredient.getCaloriesTemp().setValue((ingredient.getCaloriesPerHundredUnit() * ingredient.getCurrentQuantity().getValue()) / 100);
        }
        tvTotalQuantity.setText(ingredient.getCurrentQuantity().getValue() + ingredient.getCurrentQuantity().getType().getUnit() + " - " +
                ingredient.getCaloriesTemp().getValue() + mActivity.getString(R.string.cal_orange));
        recipeItemClickListener.onQuantityChanged(ingredient);
    }

    @Override
    public void onEmptyListFound() {

    }
}
