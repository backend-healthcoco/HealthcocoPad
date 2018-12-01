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

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.RecipeRequest;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.bean.server.IngredientResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Nutrients;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.enums.NutrientCategoryType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.IngredientListFragment;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectedRecipeItemClickListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Prashant on 26/09/2018.
 */


public class SelectIngredientFragment extends HealthCocoFragment implements
        View.OnClickListener, SelectedRecipeItemClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
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
                    if (Util.isNullOrEmptyList(ingredient.getGeneralNutrients())) {
                        getRecipeList();
                        return;
                    }
                    if (!ingredientHashMap.containsKey(ingredient.getUniqueId()))
                        ingredientHashMap.put(ingredient.getUniqueId(), getNutrientPerHundredUnit(ingredient));
                }
                notifyAdapter(ingredientHashMap);
//                selectedIngredientRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
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

    private void getRecipeList() {

        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getRecipe(DietPlanRecipeItem.class,
                WebServiceType.GET_RECIPE, dietPlanRecipeItemReceived.getUniqueId(), this, this);
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
                if (ingredient.getCalories() != null) {
                    calaries = calaries + ingredient.getCalories().getValue();
                }
                if (ingredient.getFat() != null) {
                    fat = fat + ingredient.getFat().getValue();
                }
                if (ingredient.getProtein() != null) {
                    protein = protein + ingredient.getProtein().getValue();
                }
                if (ingredient.getCarbohydreate() != null) {
                    carbs = carbs + ingredient.getCarbohydreate().getValue();
                }
                if (ingredient.getFiber() != null) {
                    fiber = fiber + ingredient.getFiber().getValue();
                }
            }
        }
        tvTotalCalaries.setText(Util.round(calaries, 2) + getString(R.string.cal_orange));
        tvTotalProtein.setText(Util.round(protein, 2) + getString(R.string.gm));
        tvTotalFat.setText(Util.round(fat, 2) + getString(R.string.gm));
        tvTotalCarbs.setText(Util.round(carbs, 2) + getString(R.string.gm));
        tvTotalFiber.setText(Util.round(fiber, 2) + getString(R.string.gm));
    }

    private Ingredient getNutrientPerHundredUnit(IngredientResponse ingredientResponse) {

        Ingredient ingredient = new Ingredient();

        ingredient.setUniqueId(ingredientResponse.getUniqueId());
        ingredient.setName(ingredientResponse.getName());
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

        if (ingredientResponse.getQuantity() != null) {
            MealQuantity tempQuantity = new MealQuantity();
            tempQuantity.setValue(ingredientResponse.getQuantity().getValue());
            if (ingredientResponse.getQuantity().getType() != null)
                tempQuantity.setType(ingredientResponse.getQuantity().getType());
            ingredient.setQuantity(tempQuantity);
        }
        if (ingredientResponse.getQuantity() != null) {
            MealQuantity tempQuantity = new MealQuantity();
            tempQuantity.setValue(ingredientResponse.getQuantity().getValue());
            if (ingredientResponse.getQuantity().getType() != null)
                tempQuantity.setType(ingredientResponse.getQuantity().getType());
            ingredient.setQuantityTemp(tempQuantity);
        }

        if (ingredientResponse.getCalories() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getCalories().getValue());
            if (ingredientResponse.getCalories().getType() != null)
                qty.setType(ingredientResponse.getCalories().getType());
            ingredient.setCalories(qty);

            ingredient.setCaloriesPerHundredUnit((ingredientResponse.getCalories().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getFat() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getFat().getValue());
            if (ingredientResponse.getFat().getType() != null)
                qty.setType(ingredientResponse.getFat().getType());
            ingredient.setFat(qty);

            ingredient.setFatPerHundredUnit((ingredientResponse.getFat().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getProtein() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getProtein().getValue());
            if (ingredientResponse.getProtein().getType() != null)
                qty.setType(ingredientResponse.getProtein().getType());
            ingredient.setProtein(qty);

            ingredient.setProteinPerHundredUnit((ingredientResponse.getProtein().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getCarbohydreate() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getCarbohydreate().getValue());
            if (ingredientResponse.getCarbohydreate().getType() != null)
                qty.setType(ingredientResponse.getCarbohydreate().getType());
            ingredient.setCarbohydreate(qty);

            ingredient.setCarbohydreatePerHundredUnit((ingredientResponse.getCarbohydreate().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getFiber() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getFiber().getValue());
            if (ingredientResponse.getFiber().getType() != null)
                qty.setType(ingredientResponse.getFiber().getType());
            ingredient.setFiber(qty);

            ingredient.setFiberPerHundredUnit((ingredientResponse.getFiber().getValue() / currentValue) * 100);
        }

        if (!Util.isNullOrEmptyList(ingredientResponse.getGeneralNutrients()))
            ingredient.setGeneralNutrients(getNutrientArrayList(ingredientResponse.getGeneralNutrients()));
        if (!Util.isNullOrEmptyList(ingredientResponse.getCarbNutrients()))
            ingredient.setCarbNutrients(getNutrientArrayList(ingredientResponse.getCarbNutrients()));
        if (!Util.isNullOrEmptyList(ingredientResponse.getLipidNutrients()))
            ingredient.setLipidNutrients(getNutrientArrayList(ingredientResponse.getLipidNutrients()));
        if (!Util.isNullOrEmptyList(ingredientResponse.getProteinAminoAcidNutrients()))
            ingredient.setProteinAminoAcidNutrients(getNutrientArrayList(ingredientResponse.getProteinAminoAcidNutrients()));
        if (!Util.isNullOrEmptyList(ingredientResponse.getVitaminNutrients()))
            ingredient.setVitaminNutrients(getNutrientArrayList(ingredientResponse.getVitaminNutrients()));
        if (!Util.isNullOrEmptyList(ingredientResponse.getMineralNutrients()))
            ingredient.setMineralNutrients(getNutrientArrayList(ingredientResponse.getMineralNutrients()));
        if (!Util.isNullOrEmptyList(ingredientResponse.getOtherNutrients()))
            ingredient.setOtherNutrients(getNutrientArrayList(ingredientResponse.getOtherNutrients()));

        return ingredient;
    }

    private List<Nutrients> getNutrientArrayList(List<Nutrients> nutrientArrayList) {
        List<Nutrients> nutrientList = new ArrayList<>();
        for (Nutrients nutrient : nutrientArrayList) {
            Nutrients newNutrient = new Nutrients();
            newNutrient.setUniqueId(nutrient.getUniqueId());
            newNutrient.setName(nutrient.getName());
            newNutrient.setValue(nutrient.getValue());
            newNutrient.setType(nutrient.getType());
            newNutrient.setNote(nutrient.getNote());
            newNutrient.setNutrientCode(nutrient.getNutrientCode());
            nutrientList.add(newNutrient);
        }
        return nutrientList;
    }

    private Ingredient getNutrientPerHundredUnit(Ingredient ingredientResponse) {

        double currentValue = 1;
        if (ingredientResponse.getQuantity() != null) {
            QuantityType type = ingredientResponse.getQuantity().getType();
            double value = ingredientResponse.getQuantity().getValue();

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
        }
        if (ingredientResponse.getQuantity() != null) {
            MealQuantity tempQuantity = new MealQuantity();
            if (!Util.isNullOrZeroNumber(ingredientResponse.getQuantity().getValue())) {
                tempQuantity.setValue(ingredientResponse.getQuantity().getValue());
                if (ingredientResponse.getQuantity().getType() != null)
                    tempQuantity.setType(ingredientResponse.getQuantity().getType());
            }
            ingredientResponse.setQuantityTemp(tempQuantity);
        }
        if (ingredientResponse.getQuantity() != null) {
            MealQuantity tempQuantity = new MealQuantity();
            if (!Util.isNullOrZeroNumber(ingredientResponse.getQuantity().getValue())) {
                tempQuantity.setValue(ingredientResponse.getQuantity().getValue());
                if (ingredientResponse.getQuantity().getType() != null)
                    tempQuantity.setType(ingredientResponse.getQuantity().getType());
            }
            ingredientResponse.setQuantity(tempQuantity);
        }

        if (ingredientResponse.getCalories() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getCalories().getValue());
            if (ingredientResponse.getCalories().getType() != null)
                qty.setType(ingredientResponse.getCalories().getType());
            ingredientResponse.setCalories(qty);
            ingredientResponse.setCaloriesPerHundredUnit((ingredientResponse.getCalories().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getFat() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getFat().getValue());
            if (ingredientResponse.getFat().getType() != null)
                qty.setType(ingredientResponse.getFat().getType());
            ingredientResponse.setFat(qty);
            ingredientResponse.setFatPerHundredUnit((ingredientResponse.getFat().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getProtein() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getProtein().getValue());
            if (ingredientResponse.getProtein().getType() != null)
                qty.setType(ingredientResponse.getProtein().getType());
            ingredientResponse.setProtein(qty);
            ingredientResponse.setProteinPerHundredUnit((ingredientResponse.getProtein().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getCarbohydreate() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getCarbohydreate().getValue());
            if (ingredientResponse.getCarbohydreate().getType() != null)
                qty.setType(ingredientResponse.getCarbohydreate().getType());
            ingredientResponse.setCarbohydreate(qty);
            ingredientResponse.setCarbohydreatePerHundredUnit((ingredientResponse.getCarbohydreate().getValue() / currentValue) * 100);
        }
        if (ingredientResponse.getFiber() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(ingredientResponse.getFiber().getValue());
            if (ingredientResponse.getFiber().getType() != null)
                qty.setType(ingredientResponse.getFiber().getType());
            ingredientResponse.setFiber(qty);
            ingredientResponse.setFiberPerHundredUnit((ingredientResponse.getFiber().getValue() / currentValue) * 100);
        }


        return ingredientResponse;
    }

    @Override
    public void onRecipeItemClicked(Object object) {
        mActivity.hideSoftKeyboard();
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
            ingredient = updateNutrientValue(ingredient, ingredient.getQuantityTemp().getValue(), ingredient.getQuantityTemp().getType());


            if (ingredient.getCalories() != null)
                ingredient.setCalories(ingredient.getCalories());
            if (ingredient.getProtein() != null)
                ingredient.setProtein(ingredient.getProtein());
            if (ingredient.getFat() != null)
                ingredient.setFat(ingredient.getFat());
            if (ingredient.getCarbohydreate() != null)
                ingredient.setCarbohydreate(ingredient.getCarbohydreate());
            if (ingredient.getFiber() != null)
                ingredient.setFiber(ingredient.getFiber());

            if (ingredient.getQuantity() != null) {
                MealQuantity mealQty = new MealQuantity();
                mealQty.setValue(ingredient.getQuantity().getValue());
                if (ingredient.getQuantity().getType() != null)
                    mealQty.setType(ingredient.getQuantity().getType());
                ingredient.setQuantityTemp(mealQty);
            }


            ingredientHashMap.put(ingredient.getUniqueId(), ingredient);
        }
        setTotalNutrientsValue(new ArrayList<>(ingredientHashMap.values()));
    }

    private Ingredient updateNutrientValue(Ingredient ingredient, double value, QuantityType type) {

        double currentValue = 1;
        if (!Util.isNullOrEmptyList(ingredient.getEquivalentMeasurements()))
            for (EquivalentQuantities equivalentQuantities : ingredient.getEquivalentMeasurements()) {
                if (type == equivalentQuantities.getServingType()) {
                    double equivalentQuantitiesValue = equivalentQuantities.getValue();
                    currentValue = value * equivalentQuantitiesValue;
                }
            }

        if (!Util.isNullOrEmptyList(ingredient.getGeneralNutrients())) {
            ingredient.setGeneralNutrients(changeNutrientValue(ingredient.getGeneralNutrients(), ingredient, currentValue, NutrientCategoryType.GENERAL));
        }
        if (!Util.isNullOrEmptyList(ingredient.getCarbNutrients())) {
            ingredient.setCarbNutrients(changeNutrientValue(ingredient.getCarbNutrients(), ingredient, currentValue, NutrientCategoryType.CARBOHYDRATE));
        }
        if (!Util.isNullOrEmptyList(ingredient.getLipidNutrients())) {
            ingredient.setLipidNutrients(changeNutrientValue(ingredient.getLipidNutrients(), ingredient, currentValue, NutrientCategoryType.LIPIDS));
        }
        if (!Util.isNullOrEmptyList(ingredient.getProteinAminoAcidNutrients())) {
            ingredient.setProteinAminoAcidNutrients(changeNutrientValue(ingredient.getProteinAminoAcidNutrients(), ingredient, currentValue, NutrientCategoryType.PROTEIN_AMINOACIDS));
        }
        if (!Util.isNullOrEmptyList(ingredient.getVitaminNutrients())) {
            ingredient.setVitaminNutrients(changeNutrientValue(ingredient.getVitaminNutrients(), ingredient, currentValue, NutrientCategoryType.VITAMINS));
        }
        if (!Util.isNullOrEmptyList(ingredient.getMineralNutrients())) {
            ingredient.setMineralNutrients(changeNutrientValue(ingredient.getMineralNutrients(), ingredient, currentValue, NutrientCategoryType.MINERALS));
        }
        if (!Util.isNullOrEmptyList(ingredient.getOtherNutrients())) {
            ingredient.setOtherNutrients(changeNutrientValue(ingredient.getOtherNutrients(), ingredient, currentValue, NutrientCategoryType.OTHERS));
        }
        return ingredient;
    }

    private List<Nutrients> changeNutrientValue(List<Nutrients> nutrientsList, Ingredient ingredient, double currentValue, NutrientCategoryType categoryType) {

        for (Nutrients nutrients : nutrientsList) {
            nutrients.setValue((nutrients.getValue() * ingredient.getCurrentQuantity().getValue()) / currentValue);
            nutrients.setCategoryType(categoryType);
        }
        return nutrientsList;
    }


    private void addDietPlanItem() {
        DietPlanRecipeItem planRecipeItem = new DietPlanRecipeItem();
        if (dietPlanRecipeItemReceived != null)
            planRecipeItem = dietPlanRecipeItemReceived;

        if (planRecipeItem.getCalories() != null)
            planRecipeItem.getCalories().setValue(calaries);
        else {
            MealQuantity qty = new MealQuantity();
            qty.setValue(calaries);
            qty.setType(QuantityType.CAL);
            planRecipeItem.setCalories(qty);
        }
        if (planRecipeItem.getProtein() != null)
            planRecipeItem.getProtein().setValue(protein);
        else {
            MealQuantity qty = new MealQuantity();
            qty.setValue(protein);
            qty.setType(QuantityType.G);
            planRecipeItem.setProtein(qty);
        }
        if (planRecipeItem.getFat() != null)
            planRecipeItem.getFat().setValue(fat);
        else {
            MealQuantity qty = new MealQuantity();
            qty.setValue(fat);
            qty.setType(QuantityType.G);
            planRecipeItem.setFat(qty);
        }
        if (planRecipeItem.getCarbohydreate() != null)
            planRecipeItem.getCarbohydreate().setValue(carbs);
        else {
            MealQuantity qty = new MealQuantity();
            qty.setValue(carbs);
            qty.setType(QuantityType.G);
            planRecipeItem.setCarbohydreate(qty);
        }
        if (planRecipeItem.getFiber() != null)
            planRecipeItem.getFiber().setValue(fiber);
        else {
            MealQuantity qty = new MealQuantity();
            qty.setValue(fiber);
            qty.setType(QuantityType.G);
            planRecipeItem.setFiber(qty);
        }

        planRecipeItem.setNutrientValueAtRecipeLevel(true);
        planRecipeItem.setIngredients(new ArrayList<Ingredient>(ingredientHashMap.values()));

        planRecipeItem = addNutrientValue(planRecipeItem);

        Intent data = new Intent();
        data.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(planRecipeItem));
        getActivity().setResult(HealthCocoConstants.RESULT_CODE_ADD_INGREDIENT, data);
        getActivity().finish();
    }

    private DietPlanRecipeItem addNutrientValue(DietPlanRecipeItem planRecipeItem) {
        LocalDataServiceImpl.getInstance(mApp).deleteAllNutrient();
        for (Ingredient ingredient : planRecipeItem.getIngredients()) {
            addNutruentList(ingredient);
        }
        LocalDataServiceImpl.getInstance(mApp).addNutrientValueGroup();

        planRecipeItem.setGeneralNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.GENERAL));
        planRecipeItem.setCarbNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.CARBOHYDRATE));
        planRecipeItem.setLipidNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.LIPIDS));
        planRecipeItem.setProteinAminoAcidNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.PROTEIN_AMINOACIDS));
        planRecipeItem.setVitaminNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.VITAMINS));
        planRecipeItem.setMineralNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.MINERALS));
        planRecipeItem.setOtherNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.OTHERS));

        return planRecipeItem;
    }


    private void addNutruentList(Ingredient ingredient) {

        if (!Util.isNullOrEmptyList(ingredient.getGeneralNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(ingredient.getGeneralNutrients(), NutrientCategoryType.GENERAL);
        }
        if (!Util.isNullOrEmptyList(ingredient.getCarbNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(ingredient.getCarbNutrients(), NutrientCategoryType.CARBOHYDRATE);
        }
        if (!Util.isNullOrEmptyList(ingredient.getLipidNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(ingredient.getLipidNutrients(), NutrientCategoryType.LIPIDS);
        }
        if (!Util.isNullOrEmptyList(ingredient.getProteinAminoAcidNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(ingredient.getProteinAminoAcidNutrients(), NutrientCategoryType.PROTEIN_AMINOACIDS);
        }
        if (!Util.isNullOrEmptyList(ingredient.getVitaminNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(ingredient.getVitaminNutrients(), NutrientCategoryType.VITAMINS);
        }
        if (!Util.isNullOrEmptyList(ingredient.getMineralNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(ingredient.getMineralNutrients(), NutrientCategoryType.MINERALS);
        }
        if (!Util.isNullOrEmptyList(ingredient.getOtherNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(ingredient.getOtherNutrients(), NutrientCategoryType.OTHERS);
        }

    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();

            if (volleyResponseBean.getWebServiceType() != null) {
                mActivity.hideLoading();
                Util.showToast(mActivity, volleyResponseBean.getWebServiceType() + errorMsg);
                return;
            }
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
        switch (response.getWebServiceType()) {
            case GET_RECIPE:
                if (response.getData() != null && response.getData() instanceof DietPlanRecipeItem) {
                    dietPlanRecipeItemReceived = getRecipePerHundredUnit((DietPlanRecipeItem) response.getData());
                    initData();
                }
                break;
        }
        mActivity.hideLoading();
    }


    private DietPlanRecipeItem getRecipePerHundredUnit(DietPlanRecipeItem recipeItem) {

        MealQuantity quantity = recipeItem.getQuantity();
        QuantityType type = quantity.getType();
        double value = quantity.getValue();
        double currentValue = 1;
        MealQuantity currentQuantity = new MealQuantity();
        if (!Util.isNullOrEmptyList(recipeItem.getEquivalentMeasurements()))
            for (EquivalentQuantities equivalentQuantities : recipeItem.getEquivalentMeasurements()) {
                if (type == equivalentQuantities.getServingType()) {
                    double equivalentQuantitiesValue = equivalentQuantities.getValue();
                    currentValue = value * equivalentQuantitiesValue;
                    currentQuantity.setValue(currentValue);
                    currentQuantity.setType(equivalentQuantities.getType());
                    recipeItem.setCurrentQuantity(currentQuantity);
                }
            }

        if (recipeItem.getQuantity() != null) {
            MealQuantity tempQuantity = new MealQuantity();
            tempQuantity.setValue(recipeItem.getQuantity().getValue());
            if (recipeItem.getQuantity().getType() != null)
                tempQuantity.setType(recipeItem.getQuantity().getType());
            recipeItem.setQuantity(tempQuantity);
        }

        if (recipeItem.getQuantity() != null) {
            MealQuantity mealQty = new MealQuantity();
            mealQty.setValue(recipeItem.getQuantity().getValue());
            if (recipeItem.getQuantity().getType() != null)
                mealQty.setType(recipeItem.getQuantity().getType());
            recipeItem.setQuantityTemp(mealQty);
        }

        if (recipeItem.getCalories() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeItem.getCalories().getValue());
            if (recipeItem.getCalories().getType() != null)
                qty.setType(recipeItem.getCalories().getType());
            recipeItem.setCalories(qty);

            recipeItem.setCaloriesPerHundredUnit((recipeItem.getCalories().getValue() / currentValue) * 100);
        }
        if (recipeItem.getFat() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeItem.getFat().getValue());
            if (recipeItem.getFat().getType() != null)
                qty.setType(recipeItem.getFat().getType());
            recipeItem.setFat(qty);

            recipeItem.setFatPerHundredUnit((recipeItem.getFat().getValue() / currentValue) * 100);
        }
        if (recipeItem.getProtein() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeItem.getProtein().getValue());
            if (recipeItem.getProtein().getType() != null)
                qty.setType(recipeItem.getProtein().getType());
            recipeItem.setProtein(qty);

            recipeItem.setProteinPerHundredUnit((recipeItem.getProtein().getValue() / currentValue) * 100);
        }
        if (recipeItem.getCarbohydreate() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeItem.getCarbohydreate().getValue());
            if (recipeItem.getCarbohydreate().getType() != null)
                qty.setType(recipeItem.getCarbohydreate().getType());
            recipeItem.setCarbohydreate(qty);

            recipeItem.setCarbohydreatePerHundredUnit((recipeItem.getCarbohydreate().getValue() / currentValue) * 100);
        }
        if (recipeItem.getFiber() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeItem.getFiber().getValue());
            if (recipeItem.getFiber().getType() != null)
                qty.setType(recipeItem.getFiber().getType());
            recipeItem.setFiber(qty);

            recipeItem.setFiberPerHundredUnit((recipeItem.getFiber().getValue() / currentValue) * 100);
        }

        return recipeItem;
    }

}
