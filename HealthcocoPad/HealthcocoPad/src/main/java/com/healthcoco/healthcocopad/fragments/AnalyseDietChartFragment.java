package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.DietplanAddItem;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Nutrients;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.NutrientCategoryType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by Prashant on 26/09/2018.
 */

public class AnalyseDietChartFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener {

    public static final String TAG_DIET_PLAN_DATA = "dietPlanData";
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

    private DietplanAddItem dietplanAddItemReceived;
    private DietPlanRecipeItem recipeItemRecived;
    private int postion = 0;
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
        dietplanAddItemReceived = Parcels.unwrap(intent.getParcelableExtra(AnalyseDietChartFragment.TAG_DIET_PLAN_DATA));
        recipeItemRecived = Parcels.unwrap(intent.getParcelableExtra(AnalyseDietChartFragment.TAG_RECIPE_DATA));
//        LocalDataServiceImpl.getInstance(mApp).addNutrientValueByGroup();

        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
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
        if (recipeItemRecived != null) {
            isEndOfListAchieved = true;
            initRecipeData(recipeItemRecived);
        }

        if (dietplanAddItemReceived != null && (!Util.isNullOrEmptyList(dietplanAddItemReceived.getRecipes()))) {
            getNutrientDataForRecipe();
        }
    }

    private void getNutrientDataForRecipe() {
        if (postion <= dietplanAddItemReceived.getRecipes().size()) {
            if (postion == dietplanAddItemReceived.getRecipes().size())
                isEndOfListAchieved = true;

            initRecipeData(dietplanAddItemReceived.getRecipes().get(postion));
            postion++;
        }
    }

    private void initRecipeData(DietPlanRecipeItem recipeItem) {
        recipeItemRecived = recipeItem;
        if (Util.isNullOrEmptyList(recipeItem.getGeneralNutrients())) {
            getRecipe(recipeItem.getUniqueId());
        } else
            getNutruentList(recipeItem);
    }


    private void getRecipe(String uniqueId) {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getRecipe(DietPlanRecipeItem.class, WebServiceType.GET_RECIPE, uniqueId, this, this);
    }

    private void initAdapter() {
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        String errorMsg = errorMessage;
        if (volleyResponseBean != null && !Util.isNullOrBlank(volleyResponseBean.getErrMsg())) {
            errorMsg = volleyResponseBean.getErrMsg();
        }
        mActivity.hideLoading();
        Util.showToast(mActivity, errorMsg);
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    break;
                case GET_RECIPE:
                    if (response.getData() != null && response.getData() instanceof DietPlanRecipeItem) {
                        DietPlanRecipeItem dietPlanRecipeItem = (DietPlanRecipeItem) response.getData();
                        getNutruentList(dietPlanRecipeItem);
//                        mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_DIET, null);
//                        ((CommonOpenUpActivity) mActivity).finish();
                    }
                    break;
                default:
                    break;
            }
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
                if (doctor != null && doctor.getUser() != null) {
                    user = doctor.getUser();
                    selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
                }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }

    private void getNutruentList(DietPlanRecipeItem recipeItemRecived) {

        MealQuantity quantity = recipeItemRecived.getQuantity();
        QuantityType type = quantity.getType();
        double value = quantity.getValue();
        double currentValue = 1;
        MealQuantity currentQuantity = new MealQuantity();
        if (!Util.isNullOrEmptyList(recipeItemRecived.getEquivalentMeasurements()))
            for (EquivalentQuantities equivalentQuantities : recipeItemRecived.getEquivalentMeasurements()) {
                if (type == equivalentQuantities.getServingType()) {
                    double equivalentQuantitiesValue = equivalentQuantities.getValue();
                    currentValue = value * equivalentQuantitiesValue;
                    currentQuantity.setValue(currentValue);
                    currentQuantity.setType(type);
                    recipeItemRecived.setCurrentQuantity(currentQuantity);
                }
            }

        if (!Util.isNullOrEmptyList(recipeItemRecived.getGeneralNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(getDataFormList(recipeItemRecived.getGeneralNutrients(), recipeItemRecived.getCurrentQuantity().getValue(), NutrientCategoryType.GENERAL));
        }
        if (!Util.isNullOrEmptyList(recipeItemRecived.getCarbNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(getDataFormList(recipeItemRecived.getCarbNutrients(), recipeItemRecived.getCurrentQuantity().getValue(), NutrientCategoryType.CARBOHYDRATE));
        }
        if (!Util.isNullOrEmptyList(recipeItemRecived.getLipidNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(getDataFormList(recipeItemRecived.getLipidNutrients(), recipeItemRecived.getCurrentQuantity().getValue(), NutrientCategoryType.LIPIDS));
        }
        if (!Util.isNullOrEmptyList(recipeItemRecived.getProteinAminoAcidNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(getDataFormList(recipeItemRecived.getProteinAminoAcidNutrients(), recipeItemRecived.getCurrentQuantity().getValue(), NutrientCategoryType.PROTEIN_AMINOACIDS));
        }
        if (!Util.isNullOrEmptyList(recipeItemRecived.getVitaminNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(getDataFormList(recipeItemRecived.getVitaminNutrients(), recipeItemRecived.getCurrentQuantity().getValue(), NutrientCategoryType.VITAMINS));
        }
        if (!Util.isNullOrEmptyList(recipeItemRecived.getMineralNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(getDataFormList(recipeItemRecived.getMineralNutrients(), recipeItemRecived.getCurrentQuantity().getValue(), NutrientCategoryType.MINERALS));
        }
        if (!Util.isNullOrEmptyList(recipeItemRecived.getOtherNutrients())) {
            LocalDataServiceImpl.getInstance(mApp).addNutrientList(getDataFormList(recipeItemRecived.getOtherNutrients(), recipeItemRecived.getCurrentQuantity().getValue(), NutrientCategoryType.OTHERS));
        }

        if (isEndOfListAchieved)
            updateNutrientView();
        else
            getNutrientDataForRecipe();

    }

    private List<Nutrients> getDataFormList(List<Nutrients> nutrientsList, double value, NutrientCategoryType categoryType) {
        for (Nutrients nutrients : nutrientsList) {
            nutrients.setValue((nutrients.getValue() * recipeItemRecived.getCurrentQuantity().getValue()) / value);
            nutrients.setCategoryType(categoryType);
        }
        return nutrientsList;
    }

    private void updateNutrientView() {

        LocalDataServiceImpl.getInstance(mApp).addNutrientValueByGroup();

        generalNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.GENERAL);
        carbNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.CARBOHYDRATE);
        lipidNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.LIPIDS);
        proteinAminoAcidNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.PROTEIN_AMINOACIDS);
        vitaminNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.VITAMINS);
        mineralNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.MINERALS);
        otherNutrients = LocalDataServiceImpl.getInstance(mApp).getNutrientList(NutrientCategoryType.OTHERS);


        if (!Util.isNullOrEmptyList(generalNutrients))
            updateDietPlan(new ArrayList<Nutrients>(generalNutrients), R.string.protein_colon, parentCategoryOne);

        if (!Util.isNullOrEmptyList(carbNutrients))
            updateDietPlan(new ArrayList<Nutrients>(carbNutrients), R.string.protein_colon, parentCategoryOne);

        if (!Util.isNullOrEmptyList(lipidNutrients))
            updateDietPlan(new ArrayList<Nutrients>(lipidNutrients), R.string.protein_colon, parentCategoryOne);

        if (!Util.isNullOrEmptyList(proteinAminoAcidNutrients))
            updateDietPlan(new ArrayList<Nutrients>(proteinAminoAcidNutrients), R.string.protein_colon, parentCategoryOne);

        if (!Util.isNullOrEmptyList(vitaminNutrients))
            updateDietPlan(new ArrayList<Nutrients>(vitaminNutrients), R.string.protein_colon, parentCategoryTwo);

        if (!Util.isNullOrEmptyList(mineralNutrients))
            updateDietPlan(new ArrayList<Nutrients>(mineralNutrients), R.string.protein_colon, parentCategoryTwo);

        if (!Util.isNullOrEmptyList(otherNutrients))
            updateDietPlan(new ArrayList<Nutrients>(otherNutrients), R.string.protein_colon, parentCategoryTwo);
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
            tvValue.setText(Util.getValidatedValue(nutrient.getValue()) + nutrient.getType().getUnit());

            containerNutrient.addView(layoutNutrient);
        }
        linearLayout.addView(parentNutrient);
    }


}
