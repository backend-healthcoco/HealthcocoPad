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
import com.healthcoco.healthcocopad.bean.server.DietplanAddItem;
import com.healthcoco.healthcocopad.bean.server.RecipeResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.fragments.RecipeListFragment;
import com.healthcoco.healthcocopad.listeners.SelectedRecipeItemClickListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Prashant on 26/09/2018.
 */


public class SelectRecipeFragment extends HealthCocoFragment implements
        View.OnClickListener, SelectedRecipeItemClickListener {
    public static final String INTENT_GET_MODIFIED_VALUE = "com.healthcoco.MODIFIED_VALUE";
    public static final String TAG_SELECTED_TREATMENT_OBJECT = "selectedTreatmentItemOrdinal";
    public static final String TAG_TREATMENT_ID = "treatmentId";
    private User user;

    private RecipeListFragment recipeListFragment;

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
        Bundle bundle = getArguments();

        Intent intent = mActivity.getIntent();
        ordinal = intent.getIntExtra(HealthCocoConstants.TAG_TAB_TYPE, 0);
        mealTimeType = MealTimeType.values()[ordinal];
        dietplanAddItemReceived = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
        init();
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
                        recipeHashMap.put(recipeItem.getUniqueId(), recipeItem);

                    notifyAdapter(recipeHashMap);
                    selectedRecipeRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
                }
            }
        }
    }

    @Override
    public void initViews() {
        selectedRecipeRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_selected_recipe);
        tvNoRecipeAdded = (TextView) view.findViewById(R.id.tv_no_recipe);

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
        recipeHashMap = new LinkedHashMap<>();

        LinearLayoutManager layoutManager = new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false);
        selectedRecipeRecyclerView.setLayoutManager(layoutManager);
        selectedRecipeRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mAdapter = new HealthcocoRecyclerViewAdapter(mActivity, AdapterType.RECIPE_ITEM, this);
//        mAdapter.setListData();
        selectedRecipeRecyclerView.setAdapter(mAdapter);

    }

    private void initRecipeListSolrFragment() {

        recipeListFragment = new RecipeListFragment(this);
        mFragmentManager.beginTransaction().add(R.id.layout_recipe_list, recipeListFragment,
                recipeListFragment.getClass().getSimpleName()).commit();
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
                if (dietPlanRecipeItem.getCalariesTemp() != null) {
                    calaries = calaries + dietPlanRecipeItem.getCalariesTemp().getValue();
                }
                if (dietPlanRecipeItem.getFatTemp() != null) {
                    fat = fat + dietPlanRecipeItem.getFatTemp().getValue();
                }
                if (dietPlanRecipeItem.getProteinTemp() != null) {
                    protein = protein + dietPlanRecipeItem.getProteinTemp().getValue();
                }
                if (dietPlanRecipeItem.getCarbohydreateTemp() != null) {
                    carbs = carbs + dietPlanRecipeItem.getCarbohydreateTemp().getValue();
                }
                if (dietPlanRecipeItem.getFiberTemp() != null) {
                    fiber = fiber + dietPlanRecipeItem.getFiberTemp().getValue();
                }
            }
        }
        tvTotalCalaries.setText(String.valueOf(calaries) + getString(R.string.cal_orange));
        tvTotalProtein.setText(String.valueOf(protein));
        tvTotalFat.setText(String.valueOf(fat));
        tvTotalCarbs.setText(String.valueOf(carbs));
        tvTotalFiber.setText(String.valueOf(fiber));
    }

    private DietPlanRecipeItem getNutrientPerHundredUnit(RecipeResponse recipeResponse) {

        DietPlanRecipeItem dietPlanRecipeItem = new DietPlanRecipeItem();

        dietPlanRecipeItem.setUniqueId(recipeResponse.getUniqueId());
        dietPlanRecipeItem.setName(recipeResponse.getName());
        dietPlanRecipeItem.setQuantity(recipeResponse.getQuantity());
        dietPlanRecipeItem.setDirection(recipeResponse.getDirection());
        dietPlanRecipeItem.setCalaries(recipeResponse.getCalaries());
        dietPlanRecipeItem.setFat(recipeResponse.getFat());
        dietPlanRecipeItem.setProtein(recipeResponse.getProtein());
        dietPlanRecipeItem.setCarbohydreate(recipeResponse.getCarbohydreate());
        dietPlanRecipeItem.setFiber(recipeResponse.getFiber());
        dietPlanRecipeItem.setEquivalentMeasurements(recipeResponse.getEquivalentMeasurements());
        dietPlanRecipeItem.setNutrientValueAtRecipeLevel(recipeResponse.getNutrientValueAtRecipeLevel());

        MealQuantity quantity = recipeResponse.getQuantity();
        QuantityType type = quantity.getType();
        double value = quantity.getValue();
        double currentValue = 1;
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

        MealQuantity tempQuantity = new MealQuantity();
        if (recipeResponse.getQuantity() != null) {
            tempQuantity.setValue(recipeResponse.getQuantity().getValue());
            if (recipeResponse.getQuantity().getType() != null)
                tempQuantity.setType(recipeResponse.getQuantity().getType());
        }
        dietPlanRecipeItem.setTempQuantity(tempQuantity);

        if (recipeResponse.getCalaries() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeResponse.getCalaries().getValue());
            if (recipeResponse.getCalaries().getType() != null)
                qty.setType(recipeResponse.getCalaries().getType());
            dietPlanRecipeItem.setCalariesTemp(qty);


            dietPlanRecipeItem.setCalaries(recipeResponse.getCalaries());
            dietPlanRecipeItem.setCalariesPerHundredUnit((recipeResponse.getCalaries().getValue() / currentValue) * 100);
        }
        if (recipeResponse.getFat() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeResponse.getFat().getValue());
            if (recipeResponse.getFat().getType() != null)
                qty.setType(recipeResponse.getFat().getType());
            dietPlanRecipeItem.setFatTemp(qty);

            dietPlanRecipeItem.setFat(recipeResponse.getFat());
            dietPlanRecipeItem.setFatPerHundredUnit((recipeResponse.getFat().getValue() / currentValue) * 100);
        }
        if (recipeResponse.getProtein() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeResponse.getProtein().getValue());
            if (recipeResponse.getProtein().getType() != null)
                qty.setType(recipeResponse.getProtein().getType());
            dietPlanRecipeItem.setProteinTemp(qty);


            dietPlanRecipeItem.setProtein(recipeResponse.getProtein());
            dietPlanRecipeItem.setProteinPerHundredUnit((recipeResponse.getProtein().getValue() / currentValue) * 100);
        }
        if (recipeResponse.getCarbohydreate() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeResponse.getCarbohydreate().getValue());
            if (recipeResponse.getCarbohydreate().getType() != null)
                qty.setType(recipeResponse.getCarbohydreate().getType());
            dietPlanRecipeItem.setCarbohydreateTemp(qty);


            dietPlanRecipeItem.setCarbohydreate(recipeResponse.getCarbohydreate());
            dietPlanRecipeItem.setCarbohydreatePerHundredUnit((recipeResponse.getCarbohydreate().getValue() / currentValue) * 100);
        }
        if (recipeResponse.getFiber() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeResponse.getFiber().getValue());
            if (recipeResponse.getFiber().getType() != null)
                qty.setType(recipeResponse.getFiber().getType());
            dietPlanRecipeItem.setFiberTemp(qty);


            dietPlanRecipeItem.setFiber(recipeResponse.getFiber());
            dietPlanRecipeItem.setFiberPerHundredUnit((recipeResponse.getFiber().getValue() / currentValue) * 100);
        }


        return dietPlanRecipeItem;
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
    public void onDeleteIteClicked(Object object) {
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
            recipeItem.setQuantity(recipeItem.getTempQuantity());

            if (recipeItem.getCalariesTemp() != null)
                recipeItem.setCalaries(recipeItem.getCalariesTemp());
            if (recipeItem.getProteinTemp() != null)
                recipeItem.setProtein(recipeItem.getProteinTemp());
            if (recipeItem.getFatTemp() != null)
                recipeItem.setFat(recipeItem.getFatTemp());
            if (recipeItem.getCarbohydreateTemp() != null)
                recipeItem.setCarbohydreate(recipeItem.getCarbohydreateTemp());
            if (recipeItem.getFiberTemp() != null)
                recipeItem.setFiber(recipeItem.getFiberTemp());

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


        Intent data = new Intent();
        data.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(dietplanAddItem));
        getActivity().setResult(HealthCocoConstants.RESULT_CODE_ADD_MEAL, data);
        getActivity().finish();
    }
}
