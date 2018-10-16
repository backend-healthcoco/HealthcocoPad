package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.bean.server.IngredientResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.fragments.IngredientListFragment;
import com.healthcoco.healthcocopad.listeners.SelectedRecipeItemClickListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Prashant on 26/09/2018.
 */


public class SelectIngredientFragment extends HealthCocoFragment implements
        View.OnClickListener, SelectedRecipeItemClickListener {
    private User user;

    private IngredientListFragment ingredientListFragment;

    private TextView tvNoIngredientAdded;
    private RecyclerView selectedIngredientRecyclerView;
    private HealthcocoRecyclerViewAdapter mAdapter;
    private LinkedHashMap<String, Ingredient> ingredientHashMap;
    private int ordinal;
    private MealTimeType mealTimeType;
    private TextView tvTotalProtein;
    private TextView tvTotalFat;
    private TextView tvTotalCarbs;
    private TextView tvTotalFiber;
    private TextView tvTotalCalaries;
    private DietPlanRecipeItem dietPlanRecipeItemReceived;

    private double protein = 0;
    private double fat = 0;
    private double carbs = 0;
    private double fiber = 0;
    private double calaries = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_ingredients, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();

        Intent intent = mActivity.getIntent();
        dietPlanRecipeItemReceived = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
        init();
    }

    @Override
    public void init() {
        initViews();
        initIngredientListSolrFragment();
        initListeners();
        initAdapter();
        notifyAdapter(ingredientHashMap);
        initData();
    }

    private void initData() {
        if (dietPlanRecipeItemReceived != null) {
            if (!Util.isNullOrEmptyList(dietPlanRecipeItemReceived.getIngredients())) {

                for (Ingredient ingredient : dietPlanRecipeItemReceived.getIngredients()) {
                    if (!ingredientHashMap.containsKey(ingredient.getUniqueId()))
                        ingredientHashMap.put(ingredient.getUniqueId(), getNutrientPerHundredUnit(ingredient));

                    notifyAdapter(ingredientHashMap);
                    selectedIngredientRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                }
            }
        }
    }

    @Override
    public void initViews() {
        selectedIngredientRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_selected_ingradient);
        tvNoIngredientAdded = (TextView) view.findViewById(R.id.tv_no_ingredient);

        tvTotalCalaries = (TextView) view.findViewById(R.id.tv_total_cal);
        tvTotalProtein = (TextView) view.findViewById(R.id.tv_total_protein);
        tvTotalFat = (TextView) view.findViewById(R.id.tv_total_fat);
        tvTotalCarbs = (TextView) view.findViewById(R.id.tv_total_carbs);
        tvTotalFiber = (TextView) view.findViewById(R.id.tv_total_fiber);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);
    }


    private void initAdapter() {
        ingredientHashMap = new LinkedHashMap<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        selectedIngredientRecyclerView.setLayoutManager(layoutManager);
        selectedIngredientRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.INGREDIENT_ITEM, this);
//        mAdapter.setListData();
        selectedIngredientRecyclerView.setAdapter(mAdapter);

    }

    private void initIngredientListSolrFragment() {

        ingredientListFragment = new IngredientListFragment(this);
        mFragmentManager.beginTransaction().add(R.id.layout_ingredient_list, ingredientListFragment,
                ingredientListFragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                validateData();
                break;
            default:
                break;
        }
    }

    public void validateData() {
        int msgId = getBlankDietMsg();
        if (msgId == 0) {
            addDietPlanItem();
        } else {
            Util.showToast(mActivity, msgId);
        }
    }


    public int getBlankDietMsg() {
        int msgId = R.string.alert_add_diet;
        if (!Util.isNullOrEmptyList(ingredientHashMap))
            return 0;
        return msgId;
    }


    private void notifyAdapter(HashMap<String, Ingredient> mealHashMap) {
        ArrayList<Ingredient> list = new ArrayList<>(mealHashMap.values());

        if (!Util.isNullOrEmptyList(list)) {
            selectedIngredientRecyclerView.setVisibility(View.VISIBLE);
            tvNoIngredientAdded.setVisibility(View.GONE);
        } else {
            selectedIngredientRecyclerView.setVisibility(View.GONE);
            tvNoIngredientAdded.setVisibility(View.VISIBLE);
        }
        mAdapter.setListData((ArrayList<Object>) (Object) list);
        mAdapter.notifyDataSetChanged();
        setTotalNutrientsValue(list);
    }

    private void setTotalNutrientsValue(ArrayList<Ingredient> list) {
        protein = 0;
        fat = 0;
        carbs = 0;
        fiber = 0;
        calaries = 0;
        if (!Util.isNullOrEmptyList(list)) {
            for (Ingredient ingredient : list) {
                if (ingredient.getCaloriesTemp() != null) {
                    calaries = calaries + ingredient.getCaloriesTemp().getValue();
                }
                if (ingredient.getFatTemp() != null) {
                    fat = fat + ingredient.getFatTemp().getValue();
                }
                if (ingredient.getProteinTemp() != null) {
                    protein = protein + ingredient.getProteinTemp().getValue();
                }
                if (ingredient.getCarbohydreateTemp() != null) {
                    carbs = carbs + ingredient.getCarbohydreateTemp().getValue();
                }
                if (ingredient.getFiberTemp() != null) {
                    fiber = fiber + ingredient.getFiberTemp().getValue();
                }
            }
        }
        tvTotalCalaries.setText(String.valueOf(calaries) + getString(R.string.cal_orange));
        tvTotalProtein.setText(String.valueOf(protein));
        tvTotalFat.setText(String.valueOf(fat));
        tvTotalCarbs.setText(String.valueOf(carbs));
        tvTotalFiber.setText(String.valueOf(fiber));
    }

    private Ingredient getNutrientPerHundredUnit(IngredientResponse ingredientResponse) {

        Ingredient ingredient = new Ingredient();

        ingredient.setUniqueId(ingredientResponse.getUniqueId());
        ingredient.setName(ingredientResponse.getName());
        ingredient.setType(ingredientResponse.getQuantity().getType());
        ingredient.setValue(ingredientResponse.getQuantity().getValue());
        ingredient.setCalories(ingredientResponse.getCalories());
        ingredient.setFat(ingredientResponse.getFat());
        ingredient.setProtein(ingredientResponse.getProtein());
        ingredient.setCarbohydreate(ingredientResponse.getCarbohydreate());
        ingredient.setFiber(ingredientResponse.getFiber());
        ingredient.setEquivalentMeasurements(ingredientResponse.getEquivalentMeasurements());

        MealQuantity quantity = ingredientResponse.getQuantity();
        QuantityType type = quantity.getType();
        double value = quantity.getValue();
        double currentValue = 1;
        MealQuantity currentQuantity = new MealQuantity();
        if (!Util.isNullOrEmptyList(ingredientResponse.getEquivalentMeasurements()))
            for (EquivalentQuantities equivalentQuantities : ingredientResponse.getEquivalentMeasurements()) {
                if (type == equivalentQuantities.getServingType()) {
                    double equivalentQuantitiesValue = equivalentQuantities.getValue();
                    currentValue = value * equivalentQuantitiesValue;
                    currentQuantity.setValue(currentValue);
                    currentQuantity.setType(equivalentQuantities.getType());
                    ingredient.setCurrentQuantity(currentQuantity);
                }
            }

        MealQuantity tempQuantity = new MealQuantity();
        if (ingredientResponse.getQuantity() != null) {
            tempQuantity.setValue(ingredientResponse.getQuantity().getValue());
            if (ingredientResponse.getQuantity().getType() != null)
                tempQuantity.setType(ingredientResponse.getQuantity().getType());
        }
        ingredient.setTempQuantity(tempQuantity);

        if (ingredientResponse.getCalories() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getCalories().getValue());
            if (ingredientResponse.getCalories().getType() != null)
                qty.setType(ingredientResponse.getCalories().getType());
            ingredient.setCaloriesTemp(qty);


            ingredient.setCalories(ingredientResponse.getCalories());
            ingredient.setCaloriesPerHundredUnit((ingredientResponse.getCalories().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getFat() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getFat().getValue());
            if (ingredientResponse.getFat().getType() != null)
                qty.setType(ingredientResponse.getFat().getType());
            ingredient.setFatTemp(qty);

            ingredient.setFat(ingredientResponse.getFat());
            ingredient.setFatPerHundredUnit((ingredientResponse.getFat().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getProtein() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getProtein().getValue());
            if (ingredientResponse.getProtein().getType() != null)
                qty.setType(ingredientResponse.getProtein().getType());
            ingredient.setProteinTemp(qty);


            ingredient.setProtein(ingredientResponse.getProtein());
            ingredient.setProteinPerHundredUnit((ingredientResponse.getProtein().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getCarbohydreate() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getCarbohydreate().getValue());
            if (ingredientResponse.getCarbohydreate().getType() != null)
                qty.setType(ingredientResponse.getCarbohydreate().getType());
            ingredient.setCarbohydreateTemp(qty);


            ingredient.setCarbohydreate(ingredientResponse.getCarbohydreate());
            ingredient.setCarbohydreatePerHundredUnit((ingredientResponse.getCarbohydreate().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getFiber() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getFiber().getValue());
            if (ingredientResponse.getFiber().getType() != null)
                qty.setType(ingredientResponse.getFiber().getType());
            ingredient.setFiberTemp(qty);


            ingredient.setFiber(ingredientResponse.getFiber());
            ingredient.setFiberPerHundredUnit((ingredientResponse.getFiber().getValue() / currentValue) * 100);
        }


        return ingredient;
    }

    private Ingredient getNutrientPerHundredUnit(Ingredient ingredientResponse) {

        QuantityType type = ingredientResponse.getType();
        double value = ingredientResponse.getValue();
        double currentValue = 1;
        MealQuantity currentQuantity = new MealQuantity();
        if (!Util.isNullOrEmptyList(ingredientResponse.getEquivalentMeasurements()))
            for (EquivalentQuantities equivalentQuantities : ingredientResponse.getEquivalentMeasurements()) {
                if (type == equivalentQuantities.getServingType()) {
                    double equivalentQuantitiesValue = equivalentQuantities.getValue();
                    currentValue = value * equivalentQuantitiesValue;
                    currentQuantity.setValue(currentValue);
                    currentQuantity.setType(equivalentQuantities.getType());
                    ingredientResponse.setCurrentQuantity(currentQuantity);
                }
            }

        MealQuantity tempQuantity = new MealQuantity();
        if (!Util.isNullOrZeroNumber(ingredientResponse.getValue())) {
            tempQuantity.setValue(ingredientResponse.getValue());
            if (ingredientResponse.getType() != null)
                tempQuantity.setType(ingredientResponse.getType());
        }
        ingredientResponse.setTempQuantity(tempQuantity);

        if (ingredientResponse.getCalories() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getCalories().getValue());
            if (ingredientResponse.getCalories().getType() != null)
                qty.setType(ingredientResponse.getCalories().getType());
            ingredientResponse.setCaloriesTemp(qty);
            ingredientResponse.setCaloriesPerHundredUnit((ingredientResponse.getCalories().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getFat() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getFat().getValue());
            if (ingredientResponse.getFat().getType() != null)
                qty.setType(ingredientResponse.getFat().getType());
            ingredientResponse.setFatTemp(qty);
            ingredientResponse.setFatPerHundredUnit((ingredientResponse.getFat().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getProtein() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getProtein().getValue());
            if (ingredientResponse.getProtein().getType() != null)
                qty.setType(ingredientResponse.getProtein().getType());
            ingredientResponse.setProteinTemp(qty);
            ingredientResponse.setProteinPerHundredUnit((ingredientResponse.getProtein().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getCarbohydreate() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getCarbohydreate().getValue());
            if (ingredientResponse.getCarbohydreate().getType() != null)
                qty.setType(ingredientResponse.getCarbohydreate().getType());
            ingredientResponse.setCarbohydreateTemp(qty);
            ingredientResponse.setCarbohydreatePerHundredUnit((ingredientResponse.getCarbohydreate().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getFiber() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getFiber().getValue());
            if (ingredientResponse.getFiber().getType() != null)
                qty.setType(ingredientResponse.getFiber().getType());
            ingredientResponse.setFiberTemp(qty);
            ingredientResponse.setFiberPerHundredUnit((ingredientResponse.getFiber().getValue() / currentValue) * 100);
        }


        return ingredientResponse;
    }

    @Override
    public void onRecipeItemClicked(Object object) {
        IngredientResponse ingredientResponse = (IngredientResponse) object;
        if (ingredientResponse != null) {
            if (!ingredientHashMap.containsKey(ingredientResponse.getUniqueId()))
                ingredientHashMap.put(ingredientResponse.getUniqueId(), getNutrientPerHundredUnit(ingredientResponse));
        }
        notifyAdapter(ingredientHashMap);
        selectedIngredientRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }

    @Override
    public void onIngredientItemClicked(Object object) {

    }

    @Override
    public void onAnalyseItemClicked(Object object) {

    }

    @Override
    public void onDeleteItemClicked(Object object) {
        Ingredient ingredientItem = (Ingredient) object;
        if (ingredientItem != null) {
            ingredientHashMap.remove(ingredientItem.getUniqueId());
        }
        notifyAdapter(ingredientHashMap);
    }

    @Override
    public void onQuantityChanged(Object object) {
        Ingredient ingredient = (Ingredient) object;
        if (ingredient != null) {
            ingredient.setType(ingredient.getTempQuantity().getType());
            ingredient.setValue(ingredient.getTempQuantity().getValue());

            if (ingredient.getCaloriesTemp() != null)
                ingredient.setCalories(ingredient.getCaloriesTemp());
            if (ingredient.getProteinTemp() != null)
                ingredient.setProtein(ingredient.getProteinTemp());
            if (ingredient.getFatTemp() != null)
                ingredient.setFat(ingredient.getFatTemp());
            if (ingredient.getCarbohydreateTemp() != null)
                ingredient.setCarbohydreate(ingredient.getCarbohydreateTemp());
            if (ingredient.getFiberTemp() != null)
                ingredient.setFiber(ingredient.getFiberTemp());

            ingredientHashMap.put(ingredient.getUniqueId(), ingredient);
        }
        setTotalNutrientsValue(new ArrayList<>(ingredientHashMap.values()));
    }


    private void addDietPlanItem() {
        DietPlanRecipeItem planRecipeItem = new DietPlanRecipeItem();
        if (dietPlanRecipeItemReceived != null)
            planRecipeItem = dietPlanRecipeItemReceived;


        if (planRecipeItem.getCalariesTemp() != null)
            planRecipeItem.getCalariesTemp().setValue(calaries);
        if (planRecipeItem.getProteinTemp() != null)
            planRecipeItem.getProteinTemp().setValue(protein);
        if (planRecipeItem.getFatTemp() != null)
            planRecipeItem.getFatTemp().setValue(fat);
        if (planRecipeItem.getCarbohydreateTemp() != null)
            planRecipeItem.getCarbohydreateTemp().setValue(carbs);
        if (planRecipeItem.getFiberTemp() != null) planRecipeItem.getFiberTemp().setValue(fiber);

        planRecipeItem.setNutrientValueAtRecipeLevel(true);
        planRecipeItem.setIngredients(new ArrayList<Ingredient>(ingredientHashMap.values()));


        Intent data = new Intent();
        data.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(planRecipeItem));
        getActivity().setResult(HealthCocoConstants.RESULT_CODE_ADD_INGREDIENT, data);
        getActivity().finish();
    }
}
