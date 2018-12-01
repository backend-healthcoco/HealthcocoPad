package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.DietPlan;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.DietplanAddItem;
import com.healthcoco.healthcocopad.bean.server.Nutrients;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.dialogFragment.SelectRecipeFragment;
import com.healthcoco.healthcocopad.enums.NutrientCategoryType;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Prashant on 26/09/2018.
 */

public class AnalyseDietChartFragment extends HealthCocoFragment implements View.OnClickListener {

    public static final String TAG_DIET_PLAN_DATA = "dietPlanData";
    public static final String TAG_DIET_PLAN_CHART = "dietPlanChart";
    public static final String TAG_RECIPE_DATA = "recipeData";

    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;

    private LinearLayout parentCategoryOne;
    private LinearLayout parentCategoryTwo;

    private List<Nutrients> generalNutrients;
    private List<Nutrients> carbNutrients;
    private List<Nutrients> lipidNutrients;
    private List<Nutrients> proteinAminoAcidNutrients;
    private List<Nutrients> vitaminNutrients;
    private List<Nutrients> mineralNutrients;
    private List<Nutrients> otherNutrients;

    private DietplanAddItem dietplanAddItem;
    private DietPlanRecipeItem recipeItem;
    private DietPlan dietPlan;
    private int postion = 0;
    private int listSize = 1;
    private boolean isEndOfListAchieved;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_analyse_diet_chart, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = mActivity.getIntent();
        dietplanAddItem = Parcels.unwrap(intent.getParcelableExtra(AnalyseDietChartFragment.TAG_DIET_PLAN_DATA));
        recipeItem = Parcels.unwrap(intent.getParcelableExtra(AnalyseDietChartFragment.TAG_RECIPE_DATA));
        dietPlan = Parcels.unwrap(intent.getParcelableExtra(AnalyseDietChartFragment.TAG_DIET_PLAN_CHART));

        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initData();
    }

    @Override
    public void initViews() {
        parentCategoryOne = (LinearLayout) view.findViewById(R.id.parent_layout_category_one);
        parentCategoryTwo = (LinearLayout) view.findViewById(R.id.parent_layout_category_two);
    }


    @Override
    public void initListeners() {
//        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);

    }

    public void initData() {
//        mActivity.showLoading(false);
        if (recipeItem != null) {
            initRecipeData();
        }

        if (dietplanAddItem != null) {
            initDietPlanData();
        }

        if (dietPlan != null) {
            initDietChartData();
        }
    }

    private void initDietPlanData() {
        if (!Util.isNullOrEmptyList(dietplanAddItem.getGeneralNutrients()))
            updateNutrientViewDietPlan();
    }


    private void initRecipeData() {
        if (!Util.isNullOrEmptyList(recipeItem.getGeneralNutrients()))
            updateNutrientViewRecipe();

    }

    private void initDietChartData() {
        if (!Util.isNullOrEmptyList(dietPlan.getItems())) {
            for (DietplanAddItem dietplanAddItem : dietPlan.getItems()) {
                if (Util.isNullOrEmptyList(dietplanAddItem.getGeneralNutrients())) {
                    addNutrientValue(dietplanAddItem);
                }
            }
            addNutrientArrayList(dietPlan);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private void updateNutrientViewDietPlan() {
        if (dietplanAddItem != null) {

            if (!Util.isNullOrEmptyList(dietplanAddItem.getGeneralNutrients()))
                generalNutrients = dietplanAddItem.getGeneralNutrients();
            if (!Util.isNullOrEmptyList(dietplanAddItem.getCarbNutrients()))
                carbNutrients = dietplanAddItem.getCarbNutrients();
            if (!Util.isNullOrEmptyList(dietplanAddItem.getLipidNutrients()))
                lipidNutrients = dietplanAddItem.getLipidNutrients();
            if (!Util.isNullOrEmptyList(dietplanAddItem.getProteinAminoAcidNutrients()))
                proteinAminoAcidNutrients = dietplanAddItem.getProteinAminoAcidNutrients();
            if (!Util.isNullOrEmptyList(dietplanAddItem.getVitaminNutrients()))
                vitaminNutrients = dietplanAddItem.getVitaminNutrients();
            if (!Util.isNullOrEmptyList(dietplanAddItem.getMineralNutrients()))
                mineralNutrients = dietplanAddItem.getMineralNutrients();
            if (!Util.isNullOrEmptyList(dietplanAddItem.getOtherNutrients()))
                otherNutrients = dietplanAddItem.getOtherNutrients();
        }
        updateNutrientView();
    }

    private void updateNutrientViewRecipe() {
        if (recipeItem != null) {
            if (!Util.isNullOrEmptyList(recipeItem.getGeneralNutrients()))
                generalNutrients = recipeItem.getGeneralNutrients();
            if (!Util.isNullOrEmptyList(recipeItem.getCarbNutrients()))
                carbNutrients = recipeItem.getCarbNutrients();
            if (!Util.isNullOrEmptyList(recipeItem.getLipidNutrients()))
                lipidNutrients = recipeItem.getLipidNutrients();
            if (!Util.isNullOrEmptyList(recipeItem.getProteinAminoAcidNutrients()))
                proteinAminoAcidNutrients = recipeItem.getProteinAminoAcidNutrients();
            if (!Util.isNullOrEmptyList(recipeItem.getVitaminNutrients()))
                vitaminNutrients = recipeItem.getVitaminNutrients();
            if (!Util.isNullOrEmptyList(recipeItem.getMineralNutrients()))
                mineralNutrients = recipeItem.getMineralNutrients();
            if (!Util.isNullOrEmptyList(recipeItem.getOtherNutrients()))
                otherNutrients = recipeItem.getOtherNutrients();
        }
        updateNutrientView();
    }

    private void updateNutrientView() {
//        mActivity.hideLoading();
        if (!Util.isNullOrEmptyList(generalNutrients))
            updateDietPlan(new ArrayList<Nutrients>(generalNutrients), R.string.general, parentCategoryOne);

        if (!Util.isNullOrEmptyList(carbNutrients))
            updateDietPlan(new ArrayList<Nutrients>(carbNutrients), R.string.carbohydrates, parentCategoryOne);

        if (!Util.isNullOrEmptyList(lipidNutrients))
            updateDietPlan(new ArrayList<Nutrients>(lipidNutrients), R.string.lipids, parentCategoryOne);

        if (!Util.isNullOrEmptyList(proteinAminoAcidNutrients))
            updateDietPlan(new ArrayList<Nutrients>(proteinAminoAcidNutrients), R.string.protein_and_amino_acids, parentCategoryOne);

        if (!Util.isNullOrEmptyList(vitaminNutrients))
            updateDietPlan(new ArrayList<Nutrients>(vitaminNutrients), R.string.vitamins, parentCategoryTwo);

        if (!Util.isNullOrEmptyList(mineralNutrients))
            updateDietPlan(new ArrayList<Nutrients>(mineralNutrients), R.string.minerals, parentCategoryTwo);

        if (!Util.isNullOrEmptyList(otherNutrients))
            updateDietPlan(new ArrayList<Nutrients>(otherNutrients), R.string.others, parentCategoryTwo);
    }


    private void updateDietPlan(ArrayList<Nutrients> nutrientsArrayList, int categoryTitle, LinearLayout linearLayout) {

        LinearLayout parentNutrient = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.container_nutrient_category, null);
        TextView tvCategory = (TextView) parentNutrient.findViewById(R.id.tv_category);
        LinearLayout containerNutrient = (LinearLayout) parentNutrient.findViewById(R.id.container_nutrient);
        tvCategory.setText(getString(categoryTitle));

        for (Nutrients nutrient : nutrientsArrayList) {
            LinearLayout layoutNutrient = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.sub_item_nutrient, null);
            TextView tvName = (TextView) layoutNutrient.findViewById(R.id.tv_name_nutrient);
            TextView tvValue = (TextView) layoutNutrient.findViewById(R.id.tv_value_nutrient);
            tvName.setText(nutrient.getName());
            tvValue.setText(Util.getValidatedValue(Util.round((nutrient.getValue() / listSize), 2)) + nutrient.getType().getUnit());

            containerNutrient.addView(layoutNutrient);
        }
        linearLayout.addView(parentNutrient);
    }


    public DietplanAddItem addNutrientValue(DietplanAddItem dietplanAddItem) {
        LocalDataServiceImpl.getInstance(mApp).deleteAllNutrient();
        for (DietPlanRecipeItem recipeItem : dietplanAddItem.getRecipes()) {
            addNutrientList(recipeItem);
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

    private void addNutrientList(DietPlanRecipeItem recipeItem) {

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

    public void addNutrientArrayList(DietPlan dietPlanItem) {
        LocalDataServiceImpl.getInstance(mApp).deleteAllNutrient();
        for (DietplanAddItem addItem : dietPlanItem.getItems()) {
            addNutrientList(addItem);
        }
        LocalDataServiceImpl.getInstance(mApp).addNutrientValueGroup();

        generalNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.GENERAL);
        carbNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.CARBOHYDRATE);
        lipidNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.LIPIDS);
        proteinAminoAcidNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.PROTEIN_AMINOACIDS);
        vitaminNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.VITAMINS);
        mineralNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.MINERALS);
        otherNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.OTHERS);

        listSize = dietPlanItem.getItems().size();
        updateNutrientView();
    }

    private void addNutrientList(DietplanAddItem addItem) {

        if (!Util.isNullOrEmptyList(addItem.getGeneralNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(addItem.getGeneralNutrients(), NutrientCategoryType.GENERAL);
        }
        if (!Util.isNullOrEmptyList(addItem.getCarbNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(addItem.getCarbNutrients(), NutrientCategoryType.CARBOHYDRATE);
        }
        if (!Util.isNullOrEmptyList(addItem.getLipidNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(addItem.getLipidNutrients(), NutrientCategoryType.LIPIDS);
        }
        if (!Util.isNullOrEmptyList(addItem.getProteinAminoAcidNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(addItem.getProteinAminoAcidNutrients(), NutrientCategoryType.PROTEIN_AMINOACIDS);
        }
        if (!Util.isNullOrEmptyList(addItem.getVitaminNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(addItem.getVitaminNutrients(), NutrientCategoryType.VITAMINS);
        }
        if (!Util.isNullOrEmptyList(addItem.getMineralNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(addItem.getMineralNutrients(), NutrientCategoryType.MINERALS);
        }
        if (!Util.isNullOrEmptyList(addItem.getOtherNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(addItem.getOtherNutrients(), NutrientCategoryType.OTHERS);
        }

    }


}
