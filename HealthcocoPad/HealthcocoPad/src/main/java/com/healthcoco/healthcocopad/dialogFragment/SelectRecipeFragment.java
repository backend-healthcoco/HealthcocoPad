package com.healthcoco.healthcocopad.dialogFragment;

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
import com.healthcoco.healthcocopad.bean.server.Nutrients;
import com.healthcoco.healthcocopad.bean.server.RecipeResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.fragments.RecipeListFragment;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Prashant on 26/09/2018.
 */


public class SelectRecipeFragment extends HealthCocoFragment implements
        View.OnClickListener, SelectedTreatmentItemClickListener {
    public static final String INTENT_GET_MODIFIED_VALUE = "com.healthcoco.MODIFIED_VALUE";
    public static final String TAG_SELECTED_TREATMENT_OBJECT = "selectedTreatmentItemOrdinal";
    public static final String TAG_TREATMENT_ID = "treatmentId";
    private User user;

    private RecipeListFragment recipeListFragment;

    private TextView tvNoRecipeAdded;
    private RecyclerView selectedRecipeRecyclerView;
    private HealthcocoRecyclerViewAdapter mAdapter;
    private LinkedHashMap<String, RecipeResponse> recipeHashMap;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_fragment_select_food, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
     /*   if (bundle != null && bundle.containsKey(PatientTreatmentDetailFragment.TAG_TREATMENT_DATA)) {
            treatmentsList = Parcels.unwrap(bundle.getParcelable(PatientTreatmentDetailFragment.TAG_TREATMENT_DATA));
            if (!Util.isNullOrEmptyList(treatmentsList)) {
                for (Treatments treatments : treatmentsList) {
                    treatment = treatments;
                }
            }
        }
        if (bundle != null && bundle.containsKey(HealthCocoConstants.TAG_IS_FROM_VISIT)) {
            isFromVisit = Parcels.unwrap(bundle.getParcelable(HealthCocoConstants.TAG_IS_FROM_VISIT));
        }*/
        init();
    }

    @Override
    public void init() {
        initViews();
        initRecipeListSolrFragment();
        initListeners();
        initAdapter();
        notifyAdapter(recipeHashMap);
    }

    @Override
    public void initViews() {
        selectedRecipeRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_selected_recipe);
        tvNoRecipeAdded = (TextView) view.findViewById(R.id.tv_no_recipe);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initSaveButton(this);
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
            case R.id.bt_save:
                validateData();
                break;
            default:
                break;
        }
    }

    public void validateData() {
        int msgId = getBlankTreatmentMsg();
        if (msgId == 0) {
//            addTreatment();
        } else {
            Util.showToast(mActivity, msgId);
        }
    }


    public int getBlankTreatmentMsg() {
        int msgId = R.string.alert_add_treatment;
//        if (!Util.isNullOrEmptyList(selectedTreatmentsListFragment.getModifiedTreatmentsItemList()))
        return 0;
//        return msgIzd;
    }


    @Override
    public void onTreatmentItemClick(Object object) {
        RecipeResponse recipeResponse = (RecipeResponse) object;
        if (recipeResponse != null) {

            recipeHashMap.put(recipeResponse.getUniqueId(), getNutrientPerHundredUnit(recipeResponse));
        }
        notifyAdapter(recipeHashMap);
    }


    private void notifyAdapter(HashMap<String, RecipeResponse> mealHashMap) {
        ArrayList<RecipeResponse> list = new ArrayList<>(mealHashMap.values());

        if (!Util.isNullOrEmptyList(list)) {
            selectedRecipeRecyclerView.setVisibility(View.VISIBLE);
            tvNoRecipeAdded.setVisibility(View.GONE);
        } else {
            selectedRecipeRecyclerView.setVisibility(View.GONE);
            tvNoRecipeAdded.setVisibility(View.VISIBLE);
        }
        mAdapter.setListData((ArrayList<Object>) (Object) list);
        mAdapter.notifyDataSetChanged();
        selectedRecipeRecyclerView.smoothScrollToPosition(mAdapter.getItemCount());
    }

    @Override
    public User getUser() {
        return null;
    }


    private RecipeResponse getNutrientPerHundredUnit(RecipeResponse recipeResponse) {

        DietPlanRecipeItem dietPlanRecipeItem = new DietPlanRecipeItem();

        dietPlanRecipeItem.setUniqueId(recipeResponse.getUniqueId());
        dietPlanRecipeItem.setName(recipeResponse.getName());
        dietPlanRecipeItem.setQuantity(recipeResponse.getQuantity());
        dietPlanRecipeItem.setIngredients(recipeResponse.getIngredients());
        dietPlanRecipeItem.setNutrients(recipeResponse.getNutrients());
        dietPlanRecipeItem.setDirection(recipeResponse.getDirection());
        dietPlanRecipeItem.setCalaries(recipeResponse.getCalaries());


        MealQuantity quantity = recipeResponse.getQuantity();
        QuantityType type = quantity.getType();
        double value = quantity.getValue();
        double currentValue = 1;
        MealQuantity currentQuantity = new MealQuantity();
        for (EquivalentQuantities equivalentQuantities : recipeResponse.getEquivalentMeasurements()) {
            if (type == equivalentQuantities.getServingType()) {
                double equivalentQuantitiesValue = equivalentQuantities.getValue();
                currentValue = value * equivalentQuantitiesValue;
                currentQuantity.setValue(currentValue);
                currentQuantity.setType(type);
                recipeResponse.setCurrentQuantity(currentQuantity);
            }
        }

        for (Nutrients nutrients : recipeResponse.getNutrients()) {
            nutrients.setValue((nutrients.getValue() / currentValue) * 100);
        }
        recipeResponse.setNutrientPerHundredUnit(recipeResponse.getNutrients());


        if (recipeResponse.getCalaries() != null) {
            dietPlanRecipeItem.setCalaries(recipeResponse.getCalaries());

            recipeResponse.setCalariesPerHundredUnit((recipeResponse.getCalaries().getValue() / currentValue) * 100);
        }


        return recipeResponse;
    }
}
