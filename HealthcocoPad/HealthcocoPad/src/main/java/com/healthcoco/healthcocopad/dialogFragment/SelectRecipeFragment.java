package com.healthcoco.healthcocopad.dialogFragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.DietplanAddItem;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Nutrients;
import com.healthcoco.healthcocopad.bean.server.RecipeResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.enums.NutrientCategoryType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.fragments.AnalyseDietChartFragment;
import com.healthcoco.healthcocopad.fragments.RecipeListFragment;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.listeners.SelectedRecipeItemClickListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Prashant on 26/09/2018.
 */


public class SelectRecipeFragment extends HealthCocoFragment implements
        View.OnClickListener, SelectedRecipeItemClickListener, LocalDoInBackgroundListenerOptimised {
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;

    private TextView tvNoRecipeAdded;
    private RecyclerView selectedRecipeRecyclerView;
    private HealthcocoRecyclerViewAdapter mAdapter;
    private LinkedHashMap<String, DietPlanRecipeItem> recipeHashMap;
    private int ordinal;
    private MealTimeType mealTimeType;
    private TextView tvTotalProtein;
    private TextView tvTotalFat;
    private TextView tvTotalCarbs;
    private TextView tvTotalFiber;
    private TextView tvTotalCalaries;
    private Button btAnalyse;
    private TextView tvTitle;
    private DietplanAddItem dietplanAddItemReceived;

    private double protein = 0;
    private double fat = 0;
    private double carbs = 0;
    private double fiber = 0;
    private double calaries = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_select_recipes, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = mActivity.getIntent();
        ordinal = intent.getIntExtra(HealthCocoConstants.TAG_TAB_TYPE, 0);
        mealTimeType = MealTimeType.values()[ordinal];
        dietplanAddItemReceived = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
        init();

        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void init() {
        initViews();
        initRecipeListSolrFragment();
        initListeners();
        initAdapter();
        notifyAdapter(recipeHashMap);
        initData();
    }

    private void initData() {
        if (dietplanAddItemReceived != null) {
            if (!Util.isNullOrEmptyList(dietplanAddItemReceived.getRecipes())) {

                for (DietPlanRecipeItem recipeItem : dietplanAddItemReceived.getRecipes()) {
                    if (!recipeHashMap.containsKey(recipeItem.getUniqueId()))
                        recipeHashMap.put(recipeItem.getUniqueId(), getRecipePerHundredUnit(recipeItem));

                    notifyAdapter(recipeHashMap);
                    selectedRecipeRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                }
            }
        }
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
                    currentQuantity.setType(type);
                    recipeItem.setCurrentQuantity(currentQuantity);
                }
            }

        MealQuantity tempQuantity = new MealQuantity();
        if (recipeItem.getQuantity() != null) {
            tempQuantity.setValue(recipeItem.getQuantity().getValue());
            if (recipeItem.getQuantity().getType() != null)
                tempQuantity.setType(recipeItem.getQuantity().getType());
        }
        recipeItem.setQuantity(tempQuantity);
        recipeItem.setQuantityTemp(recipeItem.getQuantity());

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

    @Override
    public void initViews() {
        selectedRecipeRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_selected_recipe);
        tvNoRecipeAdded = (TextView) view.findViewById(R.id.tv_no_recipe);
        btAnalyse = (Button) view.findViewById(R.id.bt_analyse);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);

        tvTotalCalaries = (TextView) view.findViewById(R.id.tv_total_cal);
        tvTotalProtein = (TextView) view.findViewById(R.id.tv_total_protein);
        tvTotalFat = (TextView) view.findViewById(R.id.tv_total_fat);
        tvTotalCarbs = (TextView) view.findViewById(R.id.tv_total_carbs);
        tvTotalFiber = (TextView) view.findViewById(R.id.tv_total_fiber);

        tvTitle.setText(getString(R.string.select_recipe));
    }

    @Override
    public void initListeners() {
        btAnalyse.setOnClickListener(this);

        ((CommonOpenUpActivity) mActivity).initRightActionView(ActionbarLeftRightActionTypeDrawables.WITH_SAVE, view);
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);

    }


    private void initAdapter() {
        recipeHashMap = new LinkedHashMap<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        selectedRecipeRecyclerView.setLayoutManager(layoutManager);
        selectedRecipeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.RECIPE_ITEM, this);
//        mAdapter.setListData();
        selectedRecipeRecyclerView.setAdapter(mAdapter);

    }

    private void initRecipeListSolrFragment() {
        RecipeListFragment recipeListFragment = new RecipeListFragment(this);
        mFragmentManager.beginTransaction().add(R.id.layout_recipe_list, recipeListFragment,
                recipeListFragment.getClass().getSimpleName()).commit();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                validateData();
                break;
            case R.id.bt_analyse:
                if (!Util.isNullOrEmptyList(recipeHashMap))
                    onAnalysedClicked();
                else
                    Util.showToast(mActivity, R.string.alert_add_analyse_diet_plan);
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
        if (!Util.isNullOrEmptyList(recipeHashMap))
            return 0;
        return msgId;
    }


    private void notifyAdapter(HashMap<String, DietPlanRecipeItem> mealHashMap) {
        ArrayList<DietPlanRecipeItem> list = new ArrayList<>(mealHashMap.values());

        if (!Util.isNullOrEmptyList(list)) {
            selectedRecipeRecyclerView.setVisibility(View.VISIBLE);
            tvNoRecipeAdded.setVisibility(View.GONE);
        } else {
            selectedRecipeRecyclerView.setVisibility(View.GONE);
            tvNoRecipeAdded.setVisibility(View.VISIBLE);
        }
        mAdapter.setListData((ArrayList<Object>) (Object) list);
        mAdapter.notifyDataSetChanged();
        setTotalNutrientsValue(list);
    }

    private void setTotalNutrientsValue(ArrayList<DietPlanRecipeItem> list) {
        protein = 0;
        fat = 0;
        carbs = 0;
        fiber = 0;
        calaries = 0;
        if (!Util.isNullOrEmptyList(list)) {
            for (DietPlanRecipeItem dietPlanRecipeItem : list) {
                if (dietPlanRecipeItem.getCalories() != null) {
                    calaries = calaries + dietPlanRecipeItem.getCalories().getValue();
                }
                if (dietPlanRecipeItem.getFat() != null) {
                    fat = fat + dietPlanRecipeItem.getFat().getValue();
                }
                if (dietPlanRecipeItem.getProtein() != null) {
                    protein = protein + dietPlanRecipeItem.getProtein().getValue();
                }
                if (dietPlanRecipeItem.getCarbohydreate() != null) {
                    carbs = carbs + dietPlanRecipeItem.getCarbohydreate().getValue();
                }
                if (dietPlanRecipeItem.getFiber() != null) {
                    fiber = fiber + dietPlanRecipeItem.getFiber().getValue();
                }
            }
        }
        tvTotalCalaries.setText(Util.round(calaries, 2) + getString(R.string.cal_orange));
        tvTotalProtein.setText(Util.round(protein, 2) + getString(R.string.gm));
        tvTotalFat.setText(Util.round(fat, 2) + getString(R.string.gm));
        tvTotalCarbs.setText(Util.round(carbs, 2) + getString(R.string.gm));
        tvTotalFiber.setText(Util.round(fiber, 2) + getString(R.string.gm));
    }

    private DietPlanRecipeItem getNutrientPerHundredUnit(RecipeResponse recipeResponse) {

        DietPlanRecipeItem dietPlanRecipeItem = new DietPlanRecipeItem();

        dietPlanRecipeItem.setUniqueId(recipeResponse.getUniqueId());
        dietPlanRecipeItem.setName(recipeResponse.getName());
        dietPlanRecipeItem.setDirection(recipeResponse.getDirection());
        dietPlanRecipeItem.setEquivalentMeasurements(recipeResponse.getEquivalentMeasurements());
        dietPlanRecipeItem.setNutrientValueAtRecipeLevel(recipeResponse.getNutrientValueAtRecipeLevel());
        if (!Util.isNullOrEmptyList(recipeResponse.getIngredients()))
            dietPlanRecipeItem.setIngredients(recipeResponse.getIngredients());

        double currentValue = 1;
        if (recipeResponse.getQuantity() != null) {
            MealQuantity quantity = recipeResponse.getQuantity();
            QuantityType type = quantity.getType();
            double value = quantity.getValue();
            MealQuantity currentQuantity = new MealQuantity();
            if (!Util.isNullOrEmptyList(recipeResponse.getEquivalentMeasurements()))
                for (EquivalentQuantities equivalentQuantities : recipeResponse.getEquivalentMeasurements()) {
                    if (type == equivalentQuantities.getServingType()) {
                        double equivalentQuantitiesValue = equivalentQuantities.getValue();
                        currentValue = value * equivalentQuantitiesValue;
                        currentQuantity.setValue(currentValue);
                        currentQuantity.setType(type);
                        dietPlanRecipeItem.setCurrentQuantity(currentQuantity);
                    }
                }
        }

        MealQuantity tempQuantity = new MealQuantity();
        if (recipeResponse.getQuantity() != null) {
            tempQuantity.setValue(recipeResponse.getQuantity().getValue());
            if (recipeResponse.getQuantity().getType() != null)
                tempQuantity.setType(recipeResponse.getQuantity().getType());
            dietPlanRecipeItem.setQuantity(tempQuantity);
        }
        dietPlanRecipeItem.setQuantity(tempQuantity);
        dietPlanRecipeItem.setQuantityTemp(recipeResponse.getQuantity());

        if (recipeResponse.getCalories() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeResponse.getCalories().getValue());
            if (recipeResponse.getCalories().getType() != null)
                qty.setType(recipeResponse.getCalories().getType());

            dietPlanRecipeItem.setCalories(qty);
            dietPlanRecipeItem.setCaloriesPerHundredUnit((recipeResponse.getCalories().getValue() / currentValue) * 100);
        }
        if (recipeResponse.getFat() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeResponse.getFat().getValue());
            if (recipeResponse.getFat().getType() != null)
                qty.setType(recipeResponse.getFat().getType());

            dietPlanRecipeItem.setFat(qty);
            dietPlanRecipeItem.setFatPerHundredUnit((recipeResponse.getFat().getValue() / currentValue) * 100);
        }
        if (recipeResponse.getProtein() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeResponse.getProtein().getValue());
            if (recipeResponse.getProtein().getType() != null)
                qty.setType(recipeResponse.getProtein().getType());

            dietPlanRecipeItem.setProtein(qty);
            dietPlanRecipeItem.setProteinPerHundredUnit((recipeResponse.getProtein().getValue() / currentValue) * 100);
        }
        if (recipeResponse.getCarbohydreate() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeResponse.getCarbohydreate().getValue());
            if (recipeResponse.getCarbohydreate().getType() != null)
                qty.setType(recipeResponse.getCarbohydreate().getType());

            dietPlanRecipeItem.setCarbohydreate(qty);
            dietPlanRecipeItem.setCarbohydreatePerHundredUnit((recipeResponse.getCarbohydreate().getValue() / currentValue) * 100);
        }
        if (recipeResponse.getFiber() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeResponse.getFiber().getValue());
            if (recipeResponse.getFiber().getType() != null)
                qty.setType(recipeResponse.getFiber().getType());

            dietPlanRecipeItem.setFiber(qty);
            dietPlanRecipeItem.setFiberPerHundredUnit((recipeResponse.getFiber().getValue() / currentValue) * 100);
        }

        if (!Util.isNullOrEmptyList(recipeResponse.getGeneralNutrients()))
            dietPlanRecipeItem.setGeneralNutrients(getNutrientArrayList(recipeResponse.getGeneralNutrients()));
        if (!Util.isNullOrEmptyList(recipeResponse.getCarbNutrients()))
            dietPlanRecipeItem.setCarbNutrients(getNutrientArrayList(recipeResponse.getCarbNutrients()));
        if (!Util.isNullOrEmptyList(recipeResponse.getLipidNutrients()))
            dietPlanRecipeItem.setLipidNutrients(getNutrientArrayList(recipeResponse.getLipidNutrients()));
        if (!Util.isNullOrEmptyList(recipeResponse.getProteinAminoAcidNutrients()))
            dietPlanRecipeItem.setProteinAminoAcidNutrients(getNutrientArrayList(recipeResponse.getProteinAminoAcidNutrients()));
        if (!Util.isNullOrEmptyList(recipeResponse.getVitaminNutrients()))
            dietPlanRecipeItem.setVitaminNutrients(getNutrientArrayList(recipeResponse.getVitaminNutrients()));
        if (!Util.isNullOrEmptyList(recipeResponse.getMineralNutrients()))
            dietPlanRecipeItem.setMineralNutrients(getNutrientArrayList(recipeResponse.getMineralNutrients()));
        if (!Util.isNullOrEmptyList(recipeResponse.getOtherNutrients()))
            dietPlanRecipeItem.setOtherNutrients(getNutrientArrayList(recipeResponse.getOtherNutrients()));


        return dietPlanRecipeItem;
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

    @Override
    public void onRecipeItemClicked(Object object) {
        RecipeResponse recipeResponse = (RecipeResponse) object;
        if (recipeResponse != null) {
            if (!recipeHashMap.containsKey(recipeResponse.getUniqueId()))
                recipeHashMap.put(recipeResponse.getUniqueId(), getNutrientPerHundredUnit(recipeResponse));
        }
        notifyAdapter(recipeHashMap);
        selectedRecipeRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }

    @Override
    public void onDeleteItemClicked(Object object) {
        DietPlanRecipeItem recipeItem = (DietPlanRecipeItem) object;
        if (recipeItem != null) {
            recipeHashMap.remove(recipeItem.getUniqueId());
        }
        notifyAdapter(recipeHashMap);
    }

    @Override
    public void onQuantityChanged(Object object) {
        DietPlanRecipeItem recipeItem = (DietPlanRecipeItem) object;
        if (recipeItem != null) {

            recipeItem = updateNutrientValue(recipeItem, recipeItem.getQuantityTemp().getValue(), recipeItem.getQuantityTemp().getType());

            if (recipeItem.getCalories() != null)
                recipeItem.setCalories(recipeItem.getCalories());
            if (recipeItem.getProtein() != null)
                recipeItem.setProtein(recipeItem.getProtein());
            if (recipeItem.getFat() != null)
                recipeItem.setFat(recipeItem.getFat());
            if (recipeItem.getCarbohydreate() != null)
                recipeItem.setCarbohydreate(recipeItem.getCarbohydreate());
            if (recipeItem.getFiber() != null)
                recipeItem.setFiber(recipeItem.getFiber());

            recipeHashMap.put(recipeItem.getUniqueId(), recipeItem);
        }
        setTotalNutrientsValue(new ArrayList<>(recipeHashMap.values()));
    }


    private void addDietPlanItem() {
        DietplanAddItem dietplanAddItem = new DietplanAddItem();

        dietplanAddItem.setCalTotal(calaries);
        dietplanAddItem.setProteinTotal(protein);
        dietplanAddItem.setFatTotal(fat);
        dietplanAddItem.setCarbohydreateTotal(carbs);
        dietplanAddItem.setFiberTotal(fiber);

        dietplanAddItem.setMealTiming(mealTimeType);
        if (dietplanAddItemReceived != null)
            dietplanAddItem.setForeignDietId(dietplanAddItemReceived.getForeignDietId());
        else
            dietplanAddItem.setForeignDietId(DateTimeUtil.getCurrentDateLong() + "");
        dietplanAddItem.setRecipes(new ArrayList<DietPlanRecipeItem>(recipeHashMap.values()));

        dietplanAddItem = addNutrientValue(dietplanAddItem);

        Intent data = new Intent();
        data.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(dietplanAddItem));
        getActivity().setResult(HealthCocoConstants.RESULT_CODE_ADD_MEAL, data);
        getActivity().finish();
    }

    @Override
    public void onIngredientItemClicked(Object object) {
        DietPlanRecipeItem dietPlanRecipeItem = (DietPlanRecipeItem) object;
        onAddIngredientClicked(dietPlanRecipeItem);
    }

    @Override
    public void onAnalyseItemClicked(Object object) {
        DietPlanRecipeItem dietPlanRecipeItem = (DietPlanRecipeItem) object;

        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ANALYSE.ordinal());
        if (dietPlanRecipeItem != null)
            intent.putExtra(AnalyseDietChartFragment.TAG_RECIPE_DATA, Parcels.wrap(dietPlanRecipeItem));
        startActivity(intent);
    }


    public void onAddIngredientClicked(DietPlanRecipeItem dietPlanRecipeItem) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.SELECT_INGREDIENT.ordinal());
        if (dietPlanRecipeItem != null)
            intent.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(dietPlanRecipeItem));
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_ADD_INGREDIENT, null);
    }

    public void onAnalysedClicked() {
        DietplanAddItem dietplanAddItem = new DietplanAddItem();

        dietplanAddItem.setCalTotal(calaries);
        dietplanAddItem.setProteinTotal(protein);
        dietplanAddItem.setFatTotal(fat);
        dietplanAddItem.setCarbohydreateTotal(carbs);
        dietplanAddItem.setFiberTotal(fiber);

        dietplanAddItem.setMealTiming(mealTimeType);
        if (dietplanAddItemReceived != null)
            dietplanAddItem.setForeignDietId(dietplanAddItemReceived.getForeignDietId());
        else
            dietplanAddItem.setForeignDietId(Util.getValidatedValue(DateTimeUtil.getCurrentDateLong()));
        dietplanAddItem.setRecipes(new ArrayList<DietPlanRecipeItem>(recipeHashMap.values()));

        dietplanAddItem = addNutrientValue(dietplanAddItem);

        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ANALYSE.ordinal());
        if (dietplanAddItem != null)
            intent.putExtra(AnalyseDietChartFragment.TAG_DIET_PLAN_DATA, Parcels.wrap(dietplanAddItem));
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_ADD_INGREDIENT) {
            switch (resultCode) {
                case HealthCocoConstants.RESULT_CODE_ADD_INGREDIENT:
                    if (data != null) {
                        DietPlanRecipeItem dietPlanRecipeItem = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        if (dietPlanRecipeItem != null) {
                            onQuantityChanged(dietPlanRecipeItem);
                            notifyAdapter(recipeHashMap);
                        }
                    }
                    break;
            }
        }

    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        switch (response.getWebServiceType()) {
            case FRAGMENT_INITIALISATION:
                if (user != null && selectedPatient != null) {
                    initActionPatientDetailActionBar(PatientProfileScreenType.IN_ADD_VISIT_HEADER, view, selectedPatient);
                }
                break;
            default:
                break;
        }
        mActivity.hideLoading();
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null)
                    user = doctor.getUser();
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    private DietPlanRecipeItem updateNutrientValue(DietPlanRecipeItem recipeItem, double value, QuantityType type) {

        double currentValue = 1;
        if (!Util.isNullOrEmptyList(recipeItem.getEquivalentMeasurements()))
            for (EquivalentQuantities equivalentQuantities : recipeItem.getEquivalentMeasurements()) {
                if (type == equivalentQuantities.getServingType()) {
                    double equivalentQuantitiesValue = equivalentQuantities.getValue();
                    currentValue = value * equivalentQuantitiesValue;
                }
            }

        if (!Util.isNullOrEmptyList(recipeItem.getGeneralNutrients())) {
            recipeItem.setGeneralNutrients(changeNutrientValue(recipeItem.getGeneralNutrients(), recipeItem, currentValue, NutrientCategoryType.GENERAL));
        }
        if (!Util.isNullOrEmptyList(recipeItem.getCarbNutrients())) {
            recipeItem.setCarbNutrients(changeNutrientValue(recipeItem.getCarbNutrients(), recipeItem, currentValue, NutrientCategoryType.CARBOHYDRATE));
        }
        if (!Util.isNullOrEmptyList(recipeItem.getLipidNutrients())) {
            recipeItem.setLipidNutrients(changeNutrientValue(recipeItem.getLipidNutrients(), recipeItem, currentValue, NutrientCategoryType.LIPIDS));
        }
        if (!Util.isNullOrEmptyList(recipeItem.getProteinAminoAcidNutrients())) {
            recipeItem.setProteinAminoAcidNutrients(changeNutrientValue(recipeItem.getProteinAminoAcidNutrients(), recipeItem, currentValue, NutrientCategoryType.PROTEIN_AMINOACIDS));
        }
        if (!Util.isNullOrEmptyList(recipeItem.getVitaminNutrients())) {
            recipeItem.setVitaminNutrients(changeNutrientValue(recipeItem.getVitaminNutrients(), recipeItem, currentValue, NutrientCategoryType.VITAMINS));
        }
        if (!Util.isNullOrEmptyList(recipeItem.getMineralNutrients())) {
            recipeItem.setMineralNutrients(changeNutrientValue(recipeItem.getMineralNutrients(), recipeItem, currentValue, NutrientCategoryType.MINERALS));
        }
        if (!Util.isNullOrEmptyList(recipeItem.getOtherNutrients())) {
            recipeItem.setOtherNutrients(changeNutrientValue(recipeItem.getOtherNutrients(), recipeItem, currentValue, NutrientCategoryType.OTHERS));
        }
        return recipeItem;
    }

    private List<Nutrients> changeNutrientValue(List<Nutrients> nutrientsList, DietPlanRecipeItem recipeItem, double currentValue, NutrientCategoryType categoryType) {

        for (Nutrients nutrients : nutrientsList) {
            nutrients.setValue((nutrients.getValue() * recipeItem.getCurrentQuantity().getValue()) / currentValue);
            nutrients.setCategoryType(categoryType);
        }
        return nutrientsList;
    }

    public DietplanAddItem addNutrientValue(DietplanAddItem dietplanAddItem) {
        LocalDataServiceImpl.getInstance(mApp).deleteAllNutrient();
        for (DietPlanRecipeItem recipeItem : dietplanAddItem.getRecipes()) {
            addNutruentList(recipeItem);
        }
        LocalDataServiceImpl.getInstance(mApp).addNutrientValueGroup();

        dietplanAddItem.setGeneralNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.GENERAL));
        dietplanAddItem.setCarbNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.CARBOHYDRATE));
        dietplanAddItem.setLipidNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.LIPIDS));
        dietplanAddItem.setProteinAminoAcidNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.PROTEIN_AMINOACIDS));
        dietplanAddItem.setVitaminNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.VITAMINS));
        dietplanAddItem.setMineralNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.MINERALS));
        dietplanAddItem.setOtherNutrients(LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.OTHERS));

        return dietplanAddItem;
    }


    private void addNutruentList(DietPlanRecipeItem recipeItem) {

        if (!Util.isNullOrEmptyList(recipeItem.getGeneralNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(recipeItem.getGeneralNutrients(), NutrientCategoryType.GENERAL);
        }
        if (!Util.isNullOrEmptyList(recipeItem.getCarbNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(recipeItem.getCarbNutrients(), NutrientCategoryType.CARBOHYDRATE);
        }
        if (!Util.isNullOrEmptyList(recipeItem.getLipidNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(recipeItem.getLipidNutrients(), NutrientCategoryType.LIPIDS);
        }
        if (!Util.isNullOrEmptyList(recipeItem.getProteinAminoAcidNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(recipeItem.getProteinAminoAcidNutrients(), NutrientCategoryType.PROTEIN_AMINOACIDS);
        }
        if (!Util.isNullOrEmptyList(recipeItem.getVitaminNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(recipeItem.getVitaminNutrients(), NutrientCategoryType.VITAMINS);
        }
        if (!Util.isNullOrEmptyList(recipeItem.getMineralNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(recipeItem.getMineralNutrients(), NutrientCategoryType.MINERALS);
        }
        if (!Util.isNullOrEmptyList(recipeItem.getOtherNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(recipeItem.getOtherNutrients(), NutrientCategoryType.OTHERS);
        }

    }
}
