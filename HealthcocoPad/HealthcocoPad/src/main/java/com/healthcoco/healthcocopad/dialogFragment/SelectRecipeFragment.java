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
import com.healthcoco.healthcocopad.bean.server.RecipeResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.MealTimeType;
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

/**
 * Created by Prashant on 26/09/2018.
 */


public class SelectRecipeFragment extends HealthCocoFragment implements
        View.OnClickListener, SelectedRecipeItemClickListener, LocalDoInBackgroundListenerOptimised {
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;

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
    private Button btAnalyse;
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
        recipeItem.setTempQuantity(tempQuantity);

        if (recipeItem.getCalories() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeItem.getCalories().getValue());
            if (recipeItem.getCalories().getType() != null)
                qty.setType(recipeItem.getCalories().getType());
            recipeItem.setCalariesTemp(qty);


            recipeItem.setCaloriesPerHundredUnit((recipeItem.getCalories().getValue() / currentValue) * 100);
        }
        if (recipeItem.getFat() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeItem.getFat().getValue());
            if (recipeItem.getFat().getType() != null)
                qty.setType(recipeItem.getFat().getType());
            recipeItem.setFatTemp(qty);

            recipeItem.setFatPerHundredUnit((recipeItem.getFat().getValue() / currentValue) * 100);
        }
        if (recipeItem.getProtein() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeItem.getProtein().getValue());
            if (recipeItem.getProtein().getType() != null)
                qty.setType(recipeItem.getProtein().getType());
            recipeItem.setProteinTemp(qty);

            recipeItem.setProteinPerHundredUnit((recipeItem.getProtein().getValue() / currentValue) * 100);
        }
        if (recipeItem.getCarbohydreate() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeItem.getCarbohydreate().getValue());
            if (recipeItem.getCarbohydreate().getType() != null)
                qty.setType(recipeItem.getCarbohydreate().getType());
            recipeItem.setCarbohydreateTemp(qty);

            recipeItem.setCarbohydreatePerHundredUnit((recipeItem.getCarbohydreate().getValue() / currentValue) * 100);
        }
        if (recipeItem.getFiber() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeItem.getFiber().getValue());
            if (recipeItem.getFiber().getType() != null)
                qty.setType(recipeItem.getFiber().getType());
            recipeItem.setFiberTemp(qty);

            recipeItem.setFiberPerHundredUnit((recipeItem.getFiber().getValue() / currentValue) * 100);
        }

        return recipeItem;
    }

    @Override
    public void initViews() {
        selectedRecipeRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_selected_recipe);
        tvNoRecipeAdded = (TextView) view.findViewById(R.id.tv_no_recipe);
        btAnalyse = (Button) view.findViewById(R.id.bt_analyse);

        tvTotalCalaries = (TextView) view.findViewById(R.id.tv_total_cal);
        tvTotalProtein = (TextView) view.findViewById(R.id.tv_total_protein);
        tvTotalFat = (TextView) view.findViewById(R.id.tv_total_fat);
        tvTotalCarbs = (TextView) view.findViewById(R.id.tv_total_carbs);
        tvTotalFiber = (TextView) view.findViewById(R.id.tv_total_fiber);
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
            case R.id.bt_analyse:
                onAnalysedClicked();
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
        dietPlanRecipeItem.setQuantity(recipeResponse.getQuantity());
        dietPlanRecipeItem.setDirection(recipeResponse.getDirection());
        dietPlanRecipeItem.setCalories(recipeResponse.getCalories());
        dietPlanRecipeItem.setFat(recipeResponse.getFat());
        dietPlanRecipeItem.setProtein(recipeResponse.getProtein());
        dietPlanRecipeItem.setCarbohydreate(recipeResponse.getCarbohydreate());
        dietPlanRecipeItem.setFiber(recipeResponse.getFiber());
        dietPlanRecipeItem.setEquivalentMeasurements(recipeResponse.getEquivalentMeasurements());
        dietPlanRecipeItem.setNutrientValueAtRecipeLevel(recipeResponse.getNutrientValueAtRecipeLevel());
        if (!Util.isNullOrEmptyList(recipeResponse.getIngredients()))
            dietPlanRecipeItem.setIngredients(recipeResponse.getIngredients());

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

        if (recipeResponse.getCalories() != null) {
            MealQuantity qty = new MealQuantity();
            qty.setValue(recipeResponse.getCalories().getValue());
            if (recipeResponse.getCalories().getType() != null)
                qty.setType(recipeResponse.getCalories().getType());
            dietPlanRecipeItem.setCalariesTemp(qty);


            dietPlanRecipeItem.setCalories(recipeResponse.getCalories());
            dietPlanRecipeItem.setCaloriesPerHundredUnit((recipeResponse.getCalories().getValue() / currentValue) * 100);
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
            recipeItem.setQuantity(recipeItem.getTempQuantity());

            if (recipeItem.getCalariesTemp() != null)
                recipeItem.setCalories(recipeItem.getCalariesTemp());
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
            dietplanAddItem.setForeignDietId(DateTimeUtil.getCurrentDateLong() + "");
        dietplanAddItem.setRecipes(new ArrayList<DietPlanRecipeItem>(recipeHashMap.values()));

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
}
