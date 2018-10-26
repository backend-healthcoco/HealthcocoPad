package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.EquivalentQuantities;
import com.healthcoco.healthcocopad.bean.MealQuantity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.IngredientRequest;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Nutrients;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddEquivalentMeasurementDialogFragment;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.PopupWindowType;
import com.healthcoco.healthcocopad.enums.QuantityType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.popupwindow.PopupWindowListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Prashant on 27-08-2018.
 */
public class AddEditIngradientFragment extends HealthCocoFragment implements View.OnClickListener,
        GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean>, LocalDoInBackgroundListenerOptimised, PopupWindowListener {

    private final int EDIT_ID = 100;
    private final int DELETE_ID = 101;
    private User user;
    private TextView tvTypeQuantity;
    private TextView tvTypeProtein;
    private TextView tvTypeFat;
    private TextView tvTypeFiber;
    private TextView tvTypeCarbs;
    private TextView tvTypeCal;
    private TextView tvAddEquivalentMeasurement;
    private TextView tvAddNutrients;

    private EditText editValueQuantity;
    private EditText editValueProtein;
    private EditText editValueFat;
    private EditText editValueFiber;
    private EditText editValueCarbs;
    private EditText editValueCal;
    private EditText editName;

    private LinearLayout containerEquivalentMeasurement;
    private LinearLayout containerNutrientsOne;
    private LinearLayout containerNutrientsTwo;
    private Ingredient ingredientNutrient;
    private LinkedHashMap<String, EquivalentQuantities> quantitiesLinkedHashMap = new LinkedHashMap<>();


    public AddEditIngradientFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_ingredient, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Intent intent = mActivity.getIntent();

        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapters();
        initData();
    }

    private void initAdapters() {
        mActivity.initPopupWindows(tvTypeQuantity, PopupWindowType.QUANTITY_TYPE, new ArrayList<Object>(quantitiesLinkedHashMap.values()), this);
        mActivity.initPopupWindows(tvTypeProtein, PopupWindowType.NUTRIENT_TYPE, PopupWindowType.NUTRIENT_TYPE.getList(), this);
        mActivity.initPopupWindows(tvTypeFat, PopupWindowType.NUTRIENT_TYPE, PopupWindowType.NUTRIENT_TYPE.getList(), this);
        mActivity.initPopupWindows(tvTypeFiber, PopupWindowType.NUTRIENT_TYPE, PopupWindowType.NUTRIENT_TYPE.getList(), this);
        mActivity.initPopupWindows(tvTypeCarbs, PopupWindowType.NUTRIENT_TYPE, PopupWindowType.NUTRIENT_TYPE.getList(), this);
        mActivity.initPopupWindows(tvTypeCal, PopupWindowType.NUTRIENT_TYPE, PopupWindowType.NUTRIENT_TYPE.getList(), this);
    }


    @Override
    public void initViews() {
        tvTypeQuantity = view.findViewById(R.id.tv_type_quantity);
        tvTypeProtein = view.findViewById(R.id.tv_type_protein);
        tvTypeFat = view.findViewById(R.id.tv_type_fat);
        tvTypeFiber = view.findViewById(R.id.tv_type_fiber);
        tvTypeCarbs = view.findViewById(R.id.tv_type_carb);
        tvTypeCal = view.findViewById(R.id.tv_type_calories);
        tvAddEquivalentMeasurement = view.findViewById(R.id.tv_add_equivalent_measurement);
        tvAddNutrients = view.findViewById(R.id.tv_add_nutrient);

        editValueQuantity = view.findViewById(R.id.edit_value_quantity);
        editValueProtein = view.findViewById(R.id.edit_value_protein);
        editValueFat = view.findViewById(R.id.edit_value_fat);
        editValueFiber = view.findViewById(R.id.edit_value_fiber);
        editValueCarbs = view.findViewById(R.id.edit_value_carb);
        editValueCal = view.findViewById(R.id.edit_value_calories);
        editName = view.findViewById(R.id.edit_ingredient_name);

        containerEquivalentMeasurement = view.findViewById(R.id.container_equivalent_measurement);
        containerNutrientsOne = view.findViewById(R.id.parent_layout_category_one);
        containerNutrientsTwo = view.findViewById(R.id.parent_layout_category_two);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);

        tvAddEquivalentMeasurement.setOnClickListener(this);
        tvAddNutrients.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                validateData();
                break;
            case R.id.tv_add_equivalent_measurement:
                openAddEquivalentMeasurementDialog(null);
                break;
            case R.id.tv_add_nutrient:
                openAddNutrientWindow();
                break;
            case EDIT_ID:
                EquivalentQuantities itemEquivalentQuantities = (EquivalentQuantities) v.getTag();
                if (itemEquivalentQuantities != null)
                    openAddEquivalentMeasurementDialog(itemEquivalentQuantities);
                break;
            case DELETE_ID:
                String tag = (String) v.getTag();
                if (!Util.isNullOrBlank(tag)) {
                    quantitiesLinkedHashMap.remove(tag);
                    addEquivalentMeasurement(new ArrayList<>(quantitiesLinkedHashMap.values()));
                }
                break;
        }
    }

    private void openAddNutrientWindow() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_NUTRIENT.ordinal());
//        if (dietPlanRecipeItem != null)
//            intent.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(dietPlanRecipeItem));
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_ADD_NUTRIENT, null);
    }

    private void openAddEquivalentMeasurementDialog(EquivalentQuantities equivalentQuantities) {
        AddEquivalentMeasurementDialogFragment measurementDialogFragment = new AddEquivalentMeasurementDialogFragment();
        if (equivalentQuantities != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(equivalentQuantities));
            measurementDialogFragment.setArguments(bundle);
        }
        measurementDialogFragment.setTargetFragment(this, HealthCocoConstants.REQUEST_CODE_ADD_MEASUREMENT);
        measurementDialogFragment.show(mActivity.getSupportFragmentManager(),
                measurementDialogFragment.getClass().getSimpleName());
    }


    public void validateData() {
        String msg = null;

        if (Util.isNullOrBlank(Util.getValidatedValueOrNull(editName)))
            msg = getResources().getString((R.string.please_enter_name));
        if (Util.isNullOrZeroNumber(Util.getValidatedValueOrNull(editValueQuantity)))
            msg = getResources().getString((R.string.please_enter_qauntity));
        if (Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeQuantity)))
            msg = getResources().getString((R.string.please_enter_qauntity));

        if (Util.isNullOrBlank(msg)) {
            addIngredient();
        } else
            Util.showToast(mActivity, msg);
    }

    private void addIngredient() {
        mActivity.showLoading(false);
        IngredientRequest ingredientRequest = new IngredientRequest();

        ingredientRequest.setName(Util.getValidatedValueOrNull(editName));

        MealQuantity mealQuantity = new MealQuantity();
        mealQuantity.setValue(Util.getValidatedDoubleValue(editValueQuantity));
        mealQuantity.setType((QuantityType) tvTypeQuantity.getTag());
        ingredientRequest.setQuantity(mealQuantity);

        if (!Util.isNullOrEmptyList(quantitiesLinkedHashMap))
            ingredientRequest.setEquivalentMeasurements(new ArrayList<>(quantitiesLinkedHashMap.values()));

        String valueFat = Util.getValidatedValueOrNull(editValueFat);
        if (!Util.isNullOrBlank(valueFat)) {
            MealQuantity quantity = new MealQuantity();
            quantity.setValue(Double.parseDouble(valueFat));
            if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeFat)))
                quantity.setType((QuantityType) tvTypeFat.getTag());
            ingredientRequest.setFat(quantity);
        }

        String valueProtein = Util.getValidatedValueOrNull(editValueProtein);
        if (!Util.isNullOrBlank(valueProtein)) {
            MealQuantity quantity = new MealQuantity();
            quantity.setValue(Double.parseDouble(valueProtein));
            if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeProtein)))
                quantity.setType((QuantityType) tvTypeProtein.getTag());
            ingredientRequest.setProtein(quantity);
        }

        String valueCarbs = Util.getValidatedValueOrNull(editValueCarbs);
        if (!Util.isNullOrBlank(valueCarbs)) {
            MealQuantity quantity = new MealQuantity();
            quantity.setValue(Double.parseDouble(valueCarbs));
            if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeCarbs)))
                quantity.setType((QuantityType) tvTypeCarbs.getTag());
            ingredientRequest.setCarbohydreate(quantity);
        }

        String valueFiber = Util.getValidatedValueOrNull(editValueFiber);
        if (!Util.isNullOrBlank(valueFiber)) {
            MealQuantity quantity = new MealQuantity();
            quantity.setValue(Double.parseDouble(valueFiber));
            if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeFiber)))
                quantity.setType((QuantityType) tvTypeFiber.getTag());
            ingredientRequest.setFiber(quantity);
        }

        String valueCal = Util.getValidatedValueOrNull(editValueCal);
        if (!Util.isNullOrBlank(valueCal)) {
            MealQuantity quantity = new MealQuantity();
            quantity.setValue(Double.parseDouble(valueCal));
            if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeCal)))
                quantity.setType((QuantityType) tvTypeCal.getTag());
            ingredientRequest.setCalories(quantity);
        }

        if (ingredientNutrient != null) {
            ingredientRequest.setGeneralNutrients(ingredientNutrient.getGeneralNutrients());
            ingredientRequest.setCarbNutrients(ingredientNutrient.getCarbNutrients());
            ingredientRequest.setLipidNutrients(ingredientNutrient.getLipidNutrients());
            ingredientRequest.setProteinAminoAcidNutrients(ingredientNutrient.getProteinAminoAcidNutrients());
            ingredientRequest.setVitaminNutrients(ingredientNutrient.getVitaminNutrients());
            ingredientRequest.setMineralNutrients(ingredientNutrient.getMineralNutrients());
            ingredientRequest.setOtherNutrients(ingredientNutrient.getOtherNutrients());
        }

        if (user != null) {
            ingredientRequest.setLocationId(user.getForeignLocationId());
            ingredientRequest.setHospitalId(user.getForeignHospitalId());
            ingredientRequest.setDoctorId(user.getUniqueId());
        }

        WebDataServiceImpl.getInstance(mApp).addEditIngredient(Ingredient.class, ingredientRequest, this, this);

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
            case FRAGMENT_INITIALISATION:
                break;
            case ADD_EDIT_INGREDIENT:
                if (response.getData() != null && response.getData() instanceof Ingredient) {
                    Ingredient responseData = (Ingredient) response.getData();

                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_DIET, null);
                    ((CommonOpenUpActivity) mActivity).finish();
                }
                break;
        }
        mActivity.hideLoading();
    }


    /**
     * dont add break statement after add(since we need to call get statement also after add
     *
     * @param response
     * @return
     */

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
                }
                break;
        }
        if (volleyResponseBean == null)
            volleyResponseBean = new VolleyResponseBean();
        volleyResponseBean.setIsFromLocalAfterApiSuccess(response.isFromLocalAfterApiSuccess());
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean volleyResponseBean) {
    }

    @Override
    public void onItemSelected(PopupWindowType popupWindowType, Object object) {
        switch (popupWindowType) {
            case QUANTITY_TYPE:
                EquivalentQuantities equivalentQuantities = (EquivalentQuantities) object;
                tvTypeQuantity.setText(equivalentQuantities.getServingType().getUnit());
                tvTypeQuantity.setTag(equivalentQuantities.getServingType());
                break;
        }
    }

    @Override
    public void onEmptyListFound() {
        Util.showToast(mActivity, getString(R.string.please_add_equivalent_quantity_first));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == mActivity.RESULT_OK)
            switch (requestCode) {
                case HealthCocoConstants.REQUEST_CODE_ADD_MEASUREMENT:
                    if (data != null) {
                        EquivalentQuantities quantity = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        if (quantity != null) {
                            quantitiesLinkedHashMap.put(quantity.getServingType().getUnit(), quantity);
                            addEquivalentMeasurement(new ArrayList<>(quantitiesLinkedHashMap.values()));
                        }
                    }
                    break;
                case HealthCocoConstants.REQUEST_CODE_ADD_NUTRIENT:
                    if (data != null) {
                        ingredientNutrient = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        if (ingredientNutrient != null) {
                            updateNutrientView(ingredientNutrient);
                        }
                    }
                    break;
            }
    }

    private void addEquivalentMeasurement(ArrayList<EquivalentQuantities> equivalentQuantities) {
        mActivity.initPopupWindows(tvTypeQuantity, PopupWindowType.QUANTITY_TYPE, new ArrayList<Object>(equivalentQuantities), this);
        containerEquivalentMeasurement.removeAllViews();
        if (!Util.isNullOrEmptyList(equivalentQuantities)) {
            containerEquivalentMeasurement.setVisibility(View.VISIBLE);
            for (EquivalentQuantities quantityItem : equivalentQuantities) {

                LinearLayout layoutEquivalentQuantity = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_item_equivalent_measurement, null);
                TextView tvType = (TextView) layoutEquivalentQuantity.findViewById(R.id.tv_type);
                TextView tvServingType = (TextView) layoutEquivalentQuantity.findViewById(R.id.tv_serving_type);
                TextView tvValue = (TextView) layoutEquivalentQuantity.findViewById(R.id.tv_value);

                TextViewFontAwesome btDelete = (TextViewFontAwesome) layoutEquivalentQuantity.findViewById(R.id.bt_delete);
                TextViewFontAwesome btEdit = (TextViewFontAwesome) layoutEquivalentQuantity.findViewById(R.id.bt_edit);

                tvType.setText(quantityItem.getType().getUnit());
                tvServingType.setText(quantityItem.getServingType().getUnit());
                tvValue.setText(Util.getValidatedValue(quantityItem.getValue()));


                btEdit.setId(EDIT_ID);
                btEdit.setTag(quantityItem);
                btEdit.setOnClickListener(this);

                btDelete.setId(DELETE_ID);
                btDelete.setTag(quantityItem.getServingType().getUnit());
                btDelete.setOnClickListener(this);

                containerEquivalentMeasurement.addView(layoutEquivalentQuantity);
            }
        } else
            containerEquivalentMeasurement.setVisibility(View.GONE);
    }


    private void updateNutrientView(Ingredient ingredient) {

        if (!Util.isNullOrEmptyList(ingredient.getGeneralNutrients()))
            updateDietPlan(ingredient.getGeneralNutrients(), R.string.general, containerNutrientsOne);
        if (!Util.isNullOrEmptyList(ingredient.getCarbNutrients()))
            updateDietPlan(ingredient.getCarbNutrients(), R.string.carbohydrates, containerNutrientsOne);
        if (!Util.isNullOrEmptyList(ingredient.getLipidNutrients()))
            updateDietPlan(ingredient.getLipidNutrients(), R.string.lipids, containerNutrientsOne);
        if (!Util.isNullOrEmptyList(ingredient.getProteinAminoAcidNutrients()))
            updateDietPlan(ingredient.getProteinAminoAcidNutrients(), R.string.protein_and_amino_acids, containerNutrientsOne);
        if (!Util.isNullOrEmptyList(ingredient.getVitaminNutrients()))
            updateDietPlan(ingredient.getVitaminNutrients(), R.string.vitamins, containerNutrientsTwo);
        if (!Util.isNullOrEmptyList(ingredient.getMineralNutrients()))
            updateDietPlan(ingredient.getMineralNutrients(), R.string.minerals, containerNutrientsTwo);
        if (!Util.isNullOrEmptyList(ingredient.getOtherNutrients()))
            updateDietPlan(ingredient.getOtherNutrients(), R.string.others, containerNutrientsTwo);
    }


    private void updateDietPlan(List<Nutrients> nutrientsArrayList,
                                int categoryTitle, LinearLayout linearLayout) {

        LinearLayout parentNutrient = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.container_nutrient_category, null);
        TextView tvCategory = (TextView) parentNutrient.findViewById(R.id.tv_category);
        LinearLayout containerNutrient = (LinearLayout) parentNutrient.findViewById(R.id.container_nutrient);
        tvCategory.setText(getString(categoryTitle));

        for (Nutrients nutrient : nutrientsArrayList) {
            LinearLayout layoutNutrient = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.sub_item_nutrient, null);
            TextView tvName = (TextView) layoutNutrient.findViewById(R.id.tv_name_nutrient);
            TextView tvValue = (TextView) layoutNutrient.findViewById(R.id.tv_value_nutrient);
            tvName.setText(nutrient.getName());
            tvValue.setText(Util.getValidatedValue(nutrient.getValue()) + " " + nutrient.getType().getUnit());

            containerNutrient.addView(layoutNutrient);
        }
        linearLayout.addView(parentNutrient);
    }

}
