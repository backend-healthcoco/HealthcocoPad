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
import com.healthcoco.healthcocopad.bean.request.RecipeRequest;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.Nutrients;
import com.healthcoco.healthcocopad.bean.server.RecipeResponse;
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
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Prashant on 27-08-2018.
 */
public class AddEditRecipeFragment extends HealthCocoFragment implements View.OnClickListener,
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
    private TextView tvAddIngredient;

    private EditText editValueQuantity;
    private EditText editValueProtein;
    private EditText editValueFat;
    private EditText editValueFiber;
    private EditText editValueCarbs;
    private EditText editValueCal;
    private EditText editName;

    private LinearLayout containerEquivalentMeasurement;
    private LinearLayout containerIngredient;
    private LinearLayout containerNutrientsOne;
    private LinearLayout containerNutrientsTwo;
    private LinkedHashMap<QuantityType, EquivalentQuantities> quantitiesLinkedHashMap = new LinkedHashMap<>();
    private DietPlanRecipeItem dietPlanRecipeItem;
    private RecipeResponse recipeResponse;
    private boolean isViewMode;

    public AddEditRecipeFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_recipe, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = mActivity.getIntent();
        recipeResponse = Parcels.unwrap(intent.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
        isViewMode = intent.getBooleanExtra(HealthCocoConstants.TAG_IS_FROM_RECIPE, false);

        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        if (!isViewMode) {
            initListeners();
            initAdapters();
        }
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
        tvAddIngredient = view.findViewById(R.id.tv_add_ingredient);

        editValueQuantity = view.findViewById(R.id.edit_value_quantity);
        editValueProtein = view.findViewById(R.id.edit_value_protein);
        editValueFat = view.findViewById(R.id.edit_value_fat);
        editValueFiber = view.findViewById(R.id.edit_value_fiber);
        editValueCarbs = view.findViewById(R.id.edit_value_carb);
        editValueCal = view.findViewById(R.id.edit_value_calories);
        editName = view.findViewById(R.id.edit_recipe_name);

        containerEquivalentMeasurement = view.findViewById(R.id.container_equivalent_measurement);
        containerIngredient = view.findViewById(R.id.container_ingredient);
        containerNutrientsOne = view.findViewById(R.id.parent_layout_category_one);
        containerNutrientsTwo = view.findViewById(R.id.parent_layout_category_two);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);

        tvAddEquivalentMeasurement.setOnClickListener(this);
        tvAddNutrients.setOnClickListener(this);
        tvAddIngredient.setOnClickListener(this);
    }

    private void initData() {

        if (dietPlanRecipeItem != null) {
            editName.setText(dietPlanRecipeItem.getName());

            if (dietPlanRecipeItem.getQuantity() != null && (!Util.isNullOrZeroNumber(dietPlanRecipeItem.getQuantity().getValue()))) {
                editValueQuantity.setText(Util.getValidatedValue(dietPlanRecipeItem.getQuantity().getValue()));
                if (dietPlanRecipeItem.getQuantity().getType() != null) {
                    tvTypeQuantity.setText(dietPlanRecipeItem.getQuantity().getType().getUnit());
                    tvTypeQuantity.setTag(dietPlanRecipeItem.getQuantity().getType());
                }
            } else {
                editValueQuantity.setText("");
            }


            if (dietPlanRecipeItem.getCalories() != null) {
                editValueCal.setText(Util.getValidatedValue(Util.round(dietPlanRecipeItem.getCalories().getValue(), 2)));
                if (dietPlanRecipeItem.getCalories().getType() != null) {
                    tvTypeCal.setText(dietPlanRecipeItem.getCalories().getType().getUnit());
                    tvTypeCal.setTag(dietPlanRecipeItem.getCalories().getType());
                }
            } else {
                editValueCal.setText("");
            }

            if (dietPlanRecipeItem.getProtein() != null) {
                editValueProtein.setText(Util.getValidatedValue(Util.round(dietPlanRecipeItem.getProtein().getValue(), 2)));
                if (dietPlanRecipeItem.getProtein().getType() != null) {
                    tvTypeProtein.setText(dietPlanRecipeItem.getProtein().getType().getUnit());
                    tvTypeProtein.setTag(dietPlanRecipeItem.getProtein().getType());
                }
            } else {
                editValueProtein.setText("");
            }

            if (dietPlanRecipeItem.getFat() != null) {
                editValueFat.setText(Util.getValidatedValue(Util.round(dietPlanRecipeItem.getFat().getValue(), 2)));
                if (dietPlanRecipeItem.getFat().getType() != null) {
                    tvTypeFat.setText(dietPlanRecipeItem.getFat().getType().getUnit());
                    tvTypeFat.setTag(dietPlanRecipeItem.getFat().getType());
                }
            } else
                editValueFat.setText("");

            if (dietPlanRecipeItem.getCarbohydreate() != null) {
                editValueCarbs.setText(Util.getValidatedValue(Util.round(dietPlanRecipeItem.getCarbohydreate().getValue(), 2)));
                if (dietPlanRecipeItem.getCarbohydreate().getType() != null) {
                    tvTypeCarbs.setTag(dietPlanRecipeItem.getCarbohydreate().getType());
                    tvTypeCarbs.setText(dietPlanRecipeItem.getCarbohydreate().getType().getUnit());
                }
            } else
                editValueCarbs.setText("");

            if (dietPlanRecipeItem.getFiber() != null) {
                editValueFiber.setText(Util.getValidatedValue(Util.round(dietPlanRecipeItem.getFiber().getValue(), 2)));
                if (dietPlanRecipeItem.getFiber().getType() != null) {
                    tvTypeFiber.setTag(dietPlanRecipeItem.getFiber().getType());
                    tvTypeFiber.setText(dietPlanRecipeItem.getFiber().getType().getUnit());
                }
            } else
                editValueFiber.setText("");

            if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getEquivalentMeasurements())) {
                for (EquivalentQuantities quantity : dietPlanRecipeItem.getEquivalentMeasurements()) {
                    quantitiesLinkedHashMap.put(quantity.getServingType(), quantity);
                }
                addEquivalentMeasurement(new ArrayList<>(quantitiesLinkedHashMap.values()));
            }
            if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getGeneralNutrients())) {
                updateNutrientView(dietPlanRecipeItem);
            }

            if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getIngredients())) {
                addIngredients(dietPlanRecipeItem.getIngredients());
            } else {
                containerIngredient.setVisibility(View.GONE);
                containerIngredient.removeAllViews();
            }
        }

        if (isViewMode) {
            editValueQuantity.setEnabled(false);
            editValueProtein.setEnabled(false);
            editValueFat.setEnabled(false);
            editValueFiber.setEnabled(false);
            editValueCarbs.setEnabled(false);
            editValueCal.setEnabled(false);
            editName.setEnabled(false);
        }

    }


    private void getRecipeList() {

        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getRecipe(DietPlanRecipeItem.class,
                WebServiceType.GET_RECIPE, recipeResponse.getUniqueId(), this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_right_action:
                validateData();
                break;
            case R.id.tv_add_equivalent_measurement:
                openAddEquivalentMeasurementDialog();
                break;
            case R.id.tv_add_nutrient:
                onAddNutrientClick();
                break;
            case R.id.tv_add_ingredient:
                onAddIngredientClick();
                break;
            case DELETE_ID:
                QuantityType tag = (QuantityType) v.getTag();
                if (tag != null) {
                    quantitiesLinkedHashMap.remove(tag);
                    addEquivalentMeasurement(new ArrayList<>(quantitiesLinkedHashMap.values()));
                }
                break;
        }
    }

    private void onAddNutrientClick() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ADD_NUTRIENT.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_IS_FROM_RECIPE, true);
        if (dietPlanRecipeItem != null)
            intent.putExtra(HealthCocoConstants.TAG_RECIPE_DATA, Parcels.wrap(dietPlanRecipeItem));
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_ADD_NUTRIENT, null);
    }

    private void openAddEquivalentMeasurementDialog() {
        AddEquivalentMeasurementDialogFragment measurementDialogFragment = new AddEquivalentMeasurementDialogFragment();
        if (!Util.isNullOrEmptyList(quantitiesLinkedHashMap)) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(new ArrayList<>(quantitiesLinkedHashMap.values())));
            measurementDialogFragment.setArguments(bundle);
        }
        measurementDialogFragment.setTargetFragment(this, HealthCocoConstants.REQUEST_CODE_ADD_MEASUREMENT);
        measurementDialogFragment.show(mActivity.getSupportFragmentManager(),
                measurementDialogFragment.getClass().getSimpleName());
    }


    public void validateData() {
        String msg = null;
        if (dietPlanRecipeItem == null) {
            if (Util.isNullOrZeroNumber(Util.getValidatedValueOrNull(editValueQuantity)))
                msg = getResources().getString((R.string.please_enter_qauntity));
            if (Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeQuantity)))
                msg = getResources().getString((R.string.please_enter_qauntity));
        } else if (Util.isNullOrEmptyList(dietPlanRecipeItem.getIngredients())) {
            if (Util.isNullOrZeroNumber(Util.getValidatedValueOrNull(editValueQuantity)))
                msg = getResources().getString((R.string.please_enter_qauntity));
            if (Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeQuantity)))
                msg = getResources().getString((R.string.please_enter_qauntity));
        }

        if (Util.isNullOrBlank(Util.getValidatedValueOrNull(editName)))
            msg = getResources().getString((R.string.please_enter_name));

        if (Util.isNullOrBlank(msg)) {
            addEditRecipe();
        } else
            Util.showToast(mActivity, msg);
    }

    private void addEditRecipe() {
        mActivity.showLoading(false);

        RecipeRequest recipeRequest = new RecipeRequest();

        recipeRequest.setName(Util.getValidatedValueOrNull(editName));

        String valueQuantity = Util.getValidatedValueOrNull(editValueQuantity);
        if (!Util.isNullOrBlank(valueQuantity)) {
            MealQuantity mealQuantity = new MealQuantity();
            mealQuantity.setValue(Util.getValidatedDoubleValue(editValueQuantity));
            mealQuantity.setType((QuantityType) tvTypeQuantity.getTag());
            recipeRequest.setQuantity(mealQuantity);
        }

        if (!Util.isNullOrEmptyList(quantitiesLinkedHashMap))
            recipeRequest.setEquivalentMeasurements(new ArrayList<>(quantitiesLinkedHashMap.values()));

        String valueFat = Util.getValidatedValueOrNull(editValueFat);
        if (!Util.isNullOrBlank(valueFat)) {
            MealQuantity quantity = new MealQuantity();
            quantity.setValue(Double.parseDouble(valueFat));
            if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeFat)))
                quantity.setType((QuantityType) tvTypeFat.getTag());
            recipeRequest.setFat(quantity);
        }

        String valueProtein = Util.getValidatedValueOrNull(editValueProtein);
        if (!Util.isNullOrBlank(valueProtein)) {
            MealQuantity quantity = new MealQuantity();
            quantity.setValue(Double.parseDouble(valueProtein));
            if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeProtein)))
                quantity.setType((QuantityType) tvTypeProtein.getTag());
            recipeRequest.setProtein(quantity);
        }

        String valueCarbs = Util.getValidatedValueOrNull(editValueCarbs);
        if (!Util.isNullOrBlank(valueCarbs)) {
            MealQuantity quantity = new MealQuantity();
            quantity.setValue(Double.parseDouble(valueCarbs));
            if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeCarbs)))
                quantity.setType((QuantityType) tvTypeCarbs.getTag());
            recipeRequest.setCarbohydreate(quantity);
        }

        String valueFiber = Util.getValidatedValueOrNull(editValueFiber);
        if (!Util.isNullOrBlank(valueFiber)) {
            MealQuantity quantity = new MealQuantity();
            quantity.setValue(Double.parseDouble(valueFiber));
            if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeFiber)))
                quantity.setType((QuantityType) tvTypeFiber.getTag());
            recipeRequest.setFiber(quantity);
        }

        String valueCal = Util.getValidatedValueOrNull(editValueCal);
        if (!Util.isNullOrBlank(valueCal)) {
            MealQuantity quantity = new MealQuantity();
            quantity.setValue(Double.parseDouble(valueCal));
            if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(tvTypeCal)))
                quantity.setType((QuantityType) tvTypeCal.getTag());
            recipeRequest.setCalories(quantity);
        }

        if (dietPlanRecipeItem != null) {
            recipeRequest.setGeneralNutrients(dietPlanRecipeItem.getGeneralNutrients());
            recipeRequest.setCarbNutrients(dietPlanRecipeItem.getCarbNutrients());
            recipeRequest.setLipidNutrients(dietPlanRecipeItem.getLipidNutrients());
            recipeRequest.setProteinAminoAcidNutrients(dietPlanRecipeItem.getProteinAminoAcidNutrients());
            recipeRequest.setVitaminNutrients(dietPlanRecipeItem.getVitaminNutrients());
            recipeRequest.setMineralNutrients(dietPlanRecipeItem.getMineralNutrients());
            recipeRequest.setOtherNutrients(dietPlanRecipeItem.getOtherNutrients());

            if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getIngredients()))
                recipeRequest.setIngredients(dietPlanRecipeItem.getIngredients());
        }


        if (user != null) {
            recipeRequest.setLocationId(user.getForeignLocationId());
            recipeRequest.setHospitalId(user.getForeignHospitalId());
            recipeRequest.setDoctorId(user.getUniqueId());
        }

        WebDataServiceImpl.getInstance(mApp).addEditRecipe(RecipeRequest.class, recipeRequest, this, this);
    }

    private void onAddIngredientClick() {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.SELECT_INGREDIENT.ordinal());
        if (dietPlanRecipeItem != null)
            intent.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(dietPlanRecipeItem));
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_ADD_INGREDIENT
                , null);
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
                if (recipeResponse != null) {
                    if (isViewMode) {
                        try {
                            dietPlanRecipeItem = new DietPlanRecipeItem();
                            ReflectionUtil.copy(dietPlanRecipeItem, recipeResponse);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        initData();
                    } else {
                        getRecipeList();
                        return;
                    }
                }
                break;
            case ADD_EDIT_RECIPE:
                if (response.getData() != null && response.getData() instanceof RecipeRequest) {
                    RecipeRequest responseData = (RecipeRequest) response.getData();

                    mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_DIET, null);
                    ((CommonOpenUpActivity) mActivity).finish();
                }
                break;
            case GET_RECIPE:
                if (response.getData() != null && response.getData() instanceof DietPlanRecipeItem) {
                    dietPlanRecipeItem = (DietPlanRecipeItem) response.getData();
                    initData();
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
        switch (requestCode) {
            case HealthCocoConstants.REQUEST_CODE_ADD_MEASUREMENT:
                if (resultCode == mActivity.RESULT_OK)
                    if (data != null) {
                        List<EquivalentQuantities> quantitiesList = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        if (!Util.isNullOrEmptyList(quantitiesList)) {
                            quantitiesLinkedHashMap.clear();
                            for (EquivalentQuantities equivalentQuantities : quantitiesList) {
                                quantitiesLinkedHashMap.put(equivalentQuantities.getServingType(), equivalentQuantities);
                            }
                            addEquivalentMeasurement(new ArrayList<>(quantitiesLinkedHashMap.values()));
                        }
                    }
                break;
            case HealthCocoConstants.REQUEST_CODE_ADD_NUTRIENT:
                if (resultCode == mActivity.RESULT_OK)
                    if (data != null) {
                        dietPlanRecipeItem = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        if (dietPlanRecipeItem != null) {
                            updateNutrientView(dietPlanRecipeItem);
                        }
                    }
                break;
            case HealthCocoConstants.REQUEST_CODE_ADD_INGREDIENT:
                if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_INGREDIENT)
                    if (data != null) {
                        dietPlanRecipeItem = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        if (dietPlanRecipeItem != null) {
                            if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getIngredients()))
                                addIngredients(dietPlanRecipeItem.getIngredients());
                            updateNutrientView(dietPlanRecipeItem);
                            initNutrientData(dietPlanRecipeItem);
                        }
                    }
                break;
        }
    }

    private void initNutrientData(DietPlanRecipeItem dietPlanRecipeItem) {
        if (dietPlanRecipeItem.getFiber() != null) {
            if (!Util.isNullOrZeroNumber(dietPlanRecipeItem.getFiber().getValue()))
                editValueFiber.setText(Util.getValidatedValue(Util.round(dietPlanRecipeItem.getFiber().getValue(), 2)));
            if (dietPlanRecipeItem.getFiber().getType() != null) {
                tvTypeFiber.setText(dietPlanRecipeItem.getFiber().getType().getUnit());
                tvTypeFiber.setTag(dietPlanRecipeItem.getFiber().getType());
            }
        }


        if (dietPlanRecipeItem.getCarbohydreate() != null) {
            if (!Util.isNullOrZeroNumber(dietPlanRecipeItem.getCarbohydreate().getValue()))
                editValueCarbs.setText(Util.getValidatedValue(Util.round(dietPlanRecipeItem.getCarbohydreate().getValue(), 2)));
            if (dietPlanRecipeItem.getCarbohydreate().getType() != null) {
                tvTypeCarbs.setText(dietPlanRecipeItem.getCarbohydreate().getType().getUnit());
                tvTypeCarbs.setTag(dietPlanRecipeItem.getCarbohydreate().getType());
            }
        }

        if (dietPlanRecipeItem.getFat() != null) {
            if (!Util.isNullOrZeroNumber(dietPlanRecipeItem.getFat().getValue()))
                editValueFat.setText(Util.getValidatedValue(Util.round(dietPlanRecipeItem.getFat().getValue(), 2)));
            if (dietPlanRecipeItem.getFat().getType() != null) {
                tvTypeFat.setText(dietPlanRecipeItem.getFat().getType().getUnit());
                tvTypeFat.setTag(dietPlanRecipeItem.getFat().getType());
            }
        }

        if (dietPlanRecipeItem.getProtein() != null) {
            if (!Util.isNullOrZeroNumber(dietPlanRecipeItem.getProtein().getValue()))
                editValueProtein.setText(Util.getValidatedValue(Util.round(dietPlanRecipeItem.getProtein().getValue(), 2)));
            if (dietPlanRecipeItem.getProtein().getType() != null) {
                tvTypeProtein.setText(dietPlanRecipeItem.getProtein().getType().getUnit());
                tvTypeProtein.setTag(dietPlanRecipeItem.getProtein().getType());
            }
        }
        if (dietPlanRecipeItem.getCalories() != null) {
            if (!Util.isNullOrZeroNumber(dietPlanRecipeItem.getCalories().getValue()))
                editValueCal.setText(Util.getValidatedValue(Util.round(dietPlanRecipeItem.getCalories().getValue(), 2)));
            if (dietPlanRecipeItem.getCalories().getType() != null) {
                tvTypeCal.setText(dietPlanRecipeItem.getCalories().getType().getUnit());
                tvTypeCal.setTag(dietPlanRecipeItem.getCalories().getType());
            }
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

                btDelete.setId(DELETE_ID);
                btDelete.setTag(quantityItem.getServingType());
                btDelete.setOnClickListener(this);

                containerEquivalentMeasurement.addView(layoutEquivalentQuantity);
            }
        } else
            containerEquivalentMeasurement.setVisibility(View.GONE);
    }


    private void updateNutrientView(DietPlanRecipeItem dietPlanRecipeItem) {
        containerNutrientsOne.removeAllViews();
        containerNutrientsTwo.removeAllViews();
        if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getGeneralNutrients()))
            updateNutrientValue(dietPlanRecipeItem.getGeneralNutrients(), R.string.general, containerNutrientsOne);
        if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getCarbNutrients()))
            updateNutrientValue(dietPlanRecipeItem.getCarbNutrients(), R.string.carbohydrates, containerNutrientsOne);
        if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getLipidNutrients()))
            updateNutrientValue(dietPlanRecipeItem.getLipidNutrients(), R.string.lipids, containerNutrientsOne);
        if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getProteinAminoAcidNutrients()))
            updateNutrientValue(dietPlanRecipeItem.getProteinAminoAcidNutrients(), R.string.protein_and_amino_acids, containerNutrientsOne);
        if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getVitaminNutrients()))
            updateNutrientValue(dietPlanRecipeItem.getVitaminNutrients(), R.string.vitamins, containerNutrientsTwo);
        if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getMineralNutrients()))
            updateNutrientValue(dietPlanRecipeItem.getMineralNutrients(), R.string.minerals, containerNutrientsTwo);
        if (!Util.isNullOrEmptyList(dietPlanRecipeItem.getOtherNutrients()))
            updateNutrientValue(dietPlanRecipeItem.getOtherNutrients(), R.string.others, containerNutrientsTwo);
    }


    private void updateNutrientValue(List<Nutrients> nutrientsArrayList,
                                     int categoryTitle, LinearLayout linearLayout) {

        LinearLayout parentNutrient = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.container_nutrient_category, null);
        TextView tvCategory = (TextView) parentNutrient.findViewById(R.id.tv_category);
        LinearLayout containerNutrient = (LinearLayout) parentNutrient.findViewById(R.id.container_nutrient);
        tvCategory.setText(getString(categoryTitle));

        for (Nutrients nutrient : nutrientsArrayList) {
            LinearLayout layoutNutrient = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.sub_item_nutrient, null);
            TextView tvName = (TextView) layoutNutrient.findViewById(R.id.tv_name_nutrient);
            TextView tvValue = (TextView) layoutNutrient.findViewById(R.id.tv_value_nutrient);
            if (!Util.isNullOrBlank(nutrient.getName()))
                tvName.setText(nutrient.getName());
            if (nutrient.getType() != null)
                tvValue.setText(Util.getValidatedValue(Util.round(nutrient.getValue(), 2)) + " " + nutrient.getType().getUnit());

            containerNutrient.addView(layoutNutrient);
        }
        linearLayout.addView(parentNutrient);
    }

    private void addIngredients(List<Ingredient> ingredientList) {
        containerIngredient.setVisibility(View.VISIBLE);
        containerIngredient.removeAllViews();
        for (Ingredient ingredient : ingredientList) {
            if (ingredient != null) {
                LinearLayout layoutSubItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_sub_item_selected_ingredient, null);
                TextView tvItemTitle = (TextView) layoutSubItemPermission.findViewById(R.id.tv_ingredient_name);
                TextView tvItemQuantity = (TextView) layoutSubItemPermission.findViewById(R.id.tv_quantity_ingredient);
                TextView tvItemCalarie = (TextView) layoutSubItemPermission.findViewById(R.id.tv_serving_ingredient);
                TextViewFontAwesome btItemDelete = (TextViewFontAwesome) layoutSubItemPermission.findViewById(R.id.bt_delete_ingredient);
                btItemDelete.setOnClickListener(this);
                btItemDelete.setVisibility(View.GONE);

                tvItemTitle.setText(ingredient.getName());

                if (ingredient.getQuantity() != null) {
                    String quantityType = ingredient.getQuantity().getType().getUnit();
                    if (!Util.isNullOrZeroNumber(ingredient.getQuantity().getValue())) {
                        tvItemQuantity.setText(Util.getValidatedValue(Util.round(ingredient.getQuantity().getValue(), 2)) + quantityType);
                    }
                }
                if (ingredient.getCalories() != null) {
                    if (ingredient.getCalories().getType() != null) {
                        String calariesType = ingredient.getCalories().getType().getUnit();
                        if (!Util.isNullOrZeroNumber(ingredient.getCalories().getValue())) {
                            tvItemCalarie.setText(Util.getValidatedValue(Util.round(ingredient.getCalories().getValue(), 2)) + calariesType);
                        }
                    }
                }
                containerIngredient.addView(layoutSubItemPermission);
            }
        }
    }


}
