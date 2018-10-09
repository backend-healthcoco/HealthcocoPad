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
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DietPlan;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.DietplanAddItem;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.LinkedHashMap;


/**
 * Created by Prashant on 26/09/2018.
 */

public class AddEditDietChartFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener {

    private final int EDIT_ID = 100;
    private final int DELETE_ID = 101;

    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;

    private TextViewFontAwesome tvAddEarlyMorning;
    private LinearLayout containerFoodEarlyMorning;

    private TextViewFontAwesome tvAddBreakfast;
    private LinearLayout containerFoodBreakfast;

    private TextViewFontAwesome tvAddMidMorning;
    private LinearLayout containerFoodMidMorning;

    private TextViewFontAwesome tvAddLunch;
    private LinearLayout containerFoodLunch;

    private TextViewFontAwesome tvAddPostLunch;
    private LinearLayout containerFoodPostLunch;

    private TextViewFontAwesome tvAddEveningSnacks;
    private LinearLayout containerFoodEveningSnacks;

    private TextViewFontAwesome tvAddDinner;
    private LinearLayout containerFoodDinner;

    private TextViewFontAwesome tvAddPostDinner;
    private LinearLayout containerFoodPostDinner;

    private TextViewFontAwesome tvAddMidNight;
    private LinearLayout containerFoodMidNight;


    private LinkedHashMap<String, DietplanAddItem> earlyMorning = new LinkedHashMap<>();
    private LinkedHashMap<String, DietplanAddItem> breakfast = new LinkedHashMap<>();
    private LinkedHashMap<String, DietplanAddItem> midMorning = new LinkedHashMap<>();
    private LinkedHashMap<String, DietplanAddItem> lunch = new LinkedHashMap<>();
    private LinkedHashMap<String, DietplanAddItem> postLunch = new LinkedHashMap<>();
    private LinkedHashMap<String, DietplanAddItem> eveningSnacks = new LinkedHashMap<>();
    private LinkedHashMap<String, DietplanAddItem> dinner = new LinkedHashMap<>();
    private LinkedHashMap<String, DietplanAddItem> postDinner = new LinkedHashMap<>();
    private LinkedHashMap<String, DietplanAddItem> midNight = new LinkedHashMap<>();

    private LinkedHashMap<String, DietplanAddItem> dietplanHashMap = new LinkedHashMap<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_diet_chart, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
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
        tvAddEarlyMorning = (TextViewFontAwesome) view.findViewById(R.id.tv_add_early_morning);
        containerFoodEarlyMorning = (LinearLayout) view.findViewById(R.id.container_food_early_morning);

        tvAddBreakfast = (TextViewFontAwesome) view.findViewById(R.id.tv_add_breakfast);
        containerFoodBreakfast = (LinearLayout) view.findViewById(R.id.container_food_breakfast);

        tvAddMidMorning = (TextViewFontAwesome) view.findViewById(R.id.tv_add_morning_snacks);
        containerFoodMidMorning = (LinearLayout) view.findViewById(R.id.container_food_morning_snacks);

        tvAddLunch = (TextViewFontAwesome) view.findViewById(R.id.tv_add_lunch);
        containerFoodLunch = (LinearLayout) view.findViewById(R.id.container_food_lunch);

        tvAddPostLunch = (TextViewFontAwesome) view.findViewById(R.id.tv_add_post_lunch);
        containerFoodPostLunch = (LinearLayout) view.findViewById(R.id.container_food_post_lunch);

        tvAddEveningSnacks = (TextViewFontAwesome) view.findViewById(R.id.tv_add_evening_snack);
        containerFoodEveningSnacks = (LinearLayout) view.findViewById(R.id.container_food_evening_snack);

        tvAddDinner = (TextViewFontAwesome) view.findViewById(R.id.tv_add_dinner);
        containerFoodDinner = (LinearLayout) view.findViewById(R.id.container_food_dinner);

        tvAddPostDinner = (TextViewFontAwesome) view.findViewById(R.id.tv_add_post_dinner);
        containerFoodPostDinner = (LinearLayout) view.findViewById(R.id.container_food_post_dinner);

        tvAddMidNight = (TextViewFontAwesome) view.findViewById(R.id.tv_add_mid_night);
        containerFoodMidNight = (LinearLayout) view.findViewById(R.id.container_food_mid_night);
    }


    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);


        tvAddEarlyMorning.setOnClickListener(this);
        tvAddBreakfast.setOnClickListener(this);
        tvAddMidMorning.setOnClickListener(this);
        tvAddLunch.setOnClickListener(this);
        tvAddPostLunch.setOnClickListener(this);
        tvAddEveningSnacks.setOnClickListener(this);
        tvAddDinner.setOnClickListener(this);
        tvAddPostDinner.setOnClickListener(this);
        tvAddMidNight.setOnClickListener(this);

        view.findViewById(R.id.tv_add_food_early_morning).setOnClickListener(this);
        view.findViewById(R.id.tv_add_food_breakfast).setOnClickListener(this);
        view.findViewById(R.id.tv_add_food_morning_snacks).setOnClickListener(this);
        view.findViewById(R.id.tv_add_food_lunch).setOnClickListener(this);
        view.findViewById(R.id.tv_add_food_post_lunch).setOnClickListener(this);
        view.findViewById(R.id.tv_add_food_evening_snack).setOnClickListener(this);
        view.findViewById(R.id.tv_add_food_dinner).setOnClickListener(this);
        view.findViewById(R.id.tv_add_food_post_dinner).setOnClickListener(this);
        view.findViewById(R.id.tv_add_food_mid_night).setOnClickListener(this);

    }

    public void initData() {
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
                    if (user != null && !Util.isNullOrBlank(user.getUniqueId())) {
                        if (selectedPatient != null) {

                        }
                    }
                    break;
                case ADD_EDIT_DIET_PLAN:
                    if (response.getData() != null && response.getData() instanceof DietPlan) {
                        DietPlan plan = (DietPlan) response.getData();
                      /*  LocalDataServiceImpl.getInstance(mApp).addInvoice(invoice);
                        Util.sendBroadcasts(mApp, new ArrayList<String>() {{
                            add(PatientReceiptDetailFragment.INTENT_GET_RECEIPT_LIST_LOCAL);
                            add(PatientInvoiceDetailFragment.INTENT_GET_INVOICE_LIST_LOCAL);
                            add(CommonOpenUpPatientDetailFragment.INTENT_REFRESH_AMOUNT_DETAILS);
                        }});
                        mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_INVOICE, null);
                        */
                        ((CommonOpenUpActivity) mActivity).finish();
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


    private void validateData() {
        int msgId = getBlankDietPlanMsg();
        if (msgId == 0) {
            addDietPlan();
        } else {
            Util.showToast(mActivity, msgId);
        }
    }

    public int getBlankDietPlanMsg() {
        int msgId = R.string.alert_add_diet_plan;
        if (!Util.isNullOrEmptyList(dietplanHashMap))
            return 0;
        return msgId;
    }

    private void addDietPlan() {

        mActivity.showLoading(false);

        DietPlan dietPlanToSend = new DietPlan();
        dietPlanToSend.setDoctorId(user.getUniqueId());
        dietPlanToSend.setHospitalId(user.getForeignHospitalId());
        dietPlanToSend.setPatientId(selectedPatient.getUserId());
        dietPlanToSend.setLocationId(user.getForeignLocationId());
        dietPlanToSend.setItems(new ArrayList<DietplanAddItem>(dietplanHashMap.values()));

//        if (dietPlan.getUniqueId() != null)
//            dietPlanToSend.setUniqueId(dietPlan.getUniqueId());
//        if (dietPlan.getUniquePlanId() != null)
//            dietPlanToSend.setUniquePlanId(dietPlan.getUniquePlanId());
        dietPlanToSend.setUniquePlanId("DPLNGZ8VL1");
        dietPlanToSend.setUniqueId("5bbc54a4e4b0ca922e2b2379");


        WebDataServiceImpl.getInstance(mApp).addDietPlan(DietPlan.class, dietPlanToSend, this, this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add_early_morning:
            case R.id.tv_add_food_early_morning:
                onAddMealClicked(null, MealTimeType.EARLY_MORNING);
                break;
            case R.id.tv_add_breakfast:
            case R.id.tv_add_food_breakfast:
                onAddMealClicked(null, MealTimeType.BREAKFAST);
                break;
            case R.id.tv_add_morning_snacks:
            case R.id.tv_add_food_morning_snacks:
                onAddMealClicked(null, MealTimeType.MID_MORNING);
                break;
            case R.id.tv_add_lunch:
            case R.id.tv_add_food_lunch:
                onAddMealClicked(null, MealTimeType.LUNCH);
                break;
            case R.id.tv_add_post_lunch:
            case R.id.tv_add_food_post_lunch:
                onAddMealClicked(null, MealTimeType.POST_LUNCH);
                break;
            case R.id.tv_add_evening_snack:
            case R.id.tv_add_food_evening_snack:
                onAddMealClicked(null, MealTimeType.EVENING_SNACK);
                break;
            case R.id.tv_add_dinner:
            case R.id.tv_add_food_dinner:
                onAddMealClicked(null, MealTimeType.DINNER);
                break;
            case R.id.tv_add_post_dinner:
            case R.id.tv_add_food_post_dinner:
                onAddMealClicked(null, MealTimeType.POST_DINNER);
                break;
            case R.id.tv_add_mid_night:
            case R.id.tv_add_food_mid_night:
                onAddMealClicked(null, MealTimeType.MID_NIGHT);
                break;
            case EDIT_ID:
                DietplanAddItem dietplanAddItem = (DietplanAddItem) v.getTag();
                if (dietplanAddItem != null)
                    onAddMealClicked(dietplanAddItem, dietplanAddItem.getMealTiming());
                break;
            case DELETE_ID:
                String dietId = (String) v.getTag();
                if (!Util.isNullOrBlank(dietId)) {
                    removeItemDietPlan(dietId);
                }
                break;
            case R.id.container_right_action:
                validateData();
                break;
        }
    }


    public void onAddMealClicked(DietplanAddItem dietplanAddItem, MealTimeType mealTimeType) {
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.SELECT_RECIPES.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, mealTimeType.ordinal());
        if (dietplanAddItem != null)
            intent.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(dietplanAddItem));
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_ADD_MEAL, null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_ADD_MEAL) {
            switch (resultCode) {
                case HealthCocoConstants.RESULT_CODE_ADD_MEAL:
                    if (data != null) {
                        DietplanAddItem dietplanAddItem = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        if (dietplanAddItem != null) {
                            updateDietPlan(dietplanAddItem);
                        }
                    }
                    break;
            }
        }

    }

    private void updateDietPlan(DietplanAddItem dietplanAddItem) {
        dietplanHashMap.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
        switch (dietplanAddItem.getMealTiming()) {
            case EARLY_MORNING:
                earlyMorning.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodEarlyMorning, new ArrayList<DietplanAddItem>(earlyMorning.values()));
                break;
            case BREAKFAST:
                breakfast.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodBreakfast, new ArrayList<DietplanAddItem>(breakfast.values()));
                break;
            case MID_MORNING:
                midMorning.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodMidMorning, new ArrayList<DietplanAddItem>(midMorning.values()));
                break;
            case LUNCH:
                lunch.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodLunch, new ArrayList<DietplanAddItem>(lunch.values()));
                break;
            case POST_LUNCH:
                postLunch.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodPostLunch, new ArrayList<DietplanAddItem>(postLunch.values()));
                break;
            case EVENING_SNACK:
                eveningSnacks.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodEveningSnacks, new ArrayList<DietplanAddItem>(eveningSnacks.values()));
                break;
            case DINNER:
                dinner.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodDinner, new ArrayList<DietplanAddItem>(dinner.values()));
                break;
            case POST_DINNER:
                postDinner.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodPostDinner, new ArrayList<DietplanAddItem>(postDinner.values()));
                break;
            case MID_NIGHT:
                midNight.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodMidNight, new ArrayList<DietplanAddItem>(midNight.values()));
                break;
        }
    }


    private void addDietplanItem(LinearLayout containerFood, ArrayList<DietplanAddItem> itemArrayList) {
        containerFood.removeAllViews();
        for (DietplanAddItem dietplanAddItem : itemArrayList) {

            LinearLayout layoutMeal = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_item_selected_meal, null);
            TextView tvOption = (TextView) layoutMeal.findViewById(R.id.tv_option);
            TextView tvTotalCalaries = (TextView) layoutMeal.findViewById(R.id.tv_total_cal);
            TextView tvTotalProtein = (TextView) layoutMeal.findViewById(R.id.tv_total_protein);
            TextView tvTotalFat = (TextView) layoutMeal.findViewById(R.id.tv_total_fat);
            TextView tvTotalCarbs = (TextView) layoutMeal.findViewById(R.id.tv_total_carbs);
            TextView tvTotalFiber = (TextView) layoutMeal.findViewById(R.id.tv_total_fiber);
            LinearLayout containerMeals = (LinearLayout) layoutMeal.findViewById(R.id.container_meal);
            tvOption.setText(getString(R.string.option) + (itemArrayList.indexOf(dietplanAddItem) + 1));
            LinearLayout btRemove = (LinearLayout) layoutMeal.findViewById(R.id.bt_remove);
            LinearLayout btEdit = (LinearLayout) layoutMeal.findViewById(R.id.bt_edit);

            tvTotalCalaries.setText(dietplanAddItem.getCalTotal() + getString(R.string.cal_orange));
            tvTotalProtein.setText(dietplanAddItem.getFatTotal() + "");
            tvTotalFat.setText(dietplanAddItem.getProteinTotal() + "");
            tvTotalCarbs.setText(dietplanAddItem.getCarbohydreateTotal() + "");
            tvTotalFiber.setText(dietplanAddItem.getFiberTotal() + "");


            btEdit.setId(EDIT_ID);
            btEdit.setTag(dietplanAddItem);
            btEdit.setOnClickListener(this);

            btRemove.setId(DELETE_ID);
            btRemove.setTag(dietplanAddItem.getForeignDietId());
            btRemove.setOnClickListener(this);

            for (DietPlanRecipeItem recipeItem : dietplanAddItem.getRecipes()) {

                LinearLayout layoutRecipe = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_sub_item_selected_recipe, null);
                TextView tvRecipeTitle = (TextView) layoutRecipe.findViewById(R.id.tv_ingredient_name);
                TextView tvRecipeQuantity = (TextView) layoutRecipe.findViewById(R.id.tv_quantity_ingredient);
                TextView tvRecipeCalarie = (TextView) layoutRecipe.findViewById(R.id.tv_serving_ingredient);
                LinearLayout containerIngredients = (LinearLayout) layoutRecipe.findViewById(R.id.container_ingredient);

                tvRecipeTitle.setText(recipeItem.getName());

                if (recipeItem.getQuantity() != null) {
                    if (recipeItem.getQuantity().getType() != null) {
                        String quantityType = recipeItem.getQuantity().getType().getQuantityType();
                        if (!Util.isNullOrZeroNumber(recipeItem.getQuantity().getValue())) {
                            tvRecipeQuantity.setText(Util.getValidatedValue(recipeItem.getQuantity().getValue()) + quantityType);
                        }
                    }
                }
                if (recipeItem.getCalaries() != null) {
                    if (!Util.isNullOrZeroNumber(recipeItem.getCalaries().getValue())) {
                        tvRecipeCalarie.setText(Util.getValidatedValue(recipeItem.getCalaries().getValue()) + mActivity.getString(R.string.cal_orange) /*+ calaries.getType().getQuantityType()*/);
                    }
                }


                containerIngredients.removeAllViews();

                if (!recipeItem.getNutrientValueAtRecipeLevel()) {
                    if (!Util.isNullOrEmptyList(recipeItem.getIngredients())) {
                        containerIngredients.setVisibility(View.VISIBLE);
                        for (Ingredient ingredient : recipeItem.getIngredients()) {
                            if (ingredient != null) {
                                LinearLayout layoutSubItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_sub_item_selected_ingredient, null);
                                TextView tvItemTitle = (TextView) layoutSubItemPermission.findViewById(R.id.tv_ingredient_name);
                                TextView tvItemQuantity = (TextView) layoutSubItemPermission.findViewById(R.id.tv_quantity_ingredient);
                                TextView tvItemCalarie = (TextView) layoutSubItemPermission.findViewById(R.id.tv_serving_ingredient);
                                TextViewFontAwesome btItemDelete = (TextViewFontAwesome) layoutSubItemPermission.findViewById(R.id.bt_delete_ingredient);
                                btItemDelete.setOnClickListener(this);
                                btItemDelete.setVisibility(View.GONE);

                                tvItemTitle.setText(ingredient.getName());

                                if (ingredient.getType() != null) {
                                    String type = ingredient.getType().getQuantityType();
                                    if (!Util.isNullOrZeroNumber(ingredient.getValue())) {
                                        tvItemQuantity.setText(Util.getValidatedValue(ingredient.getValue()) + type);
                                    }
                                }
                                if (ingredient.getCalaries() != null) {
                                    if (!Util.isNullOrZeroNumber(ingredient.getCalaries().getValue())) {
                                        tvItemCalarie.setText(Util.getValidatedValue(ingredient.getCalaries().getValue()) + mActivity.getString(R.string.cal_orange));
                                    }
                                }
                                containerIngredients.addView(layoutSubItemPermission);
                            }
                        }
                    } else
                        containerIngredients.setVisibility(View.GONE);
                } else {
                    containerIngredients.setVisibility(View.GONE);
                }
                containerMeals.addView(layoutRecipe);
            }
            containerFood.addView(layoutMeal);
        }
    }


    private void removeItemDietPlan(String dietId) {
        DietplanAddItem dietplanAddItem = dietplanHashMap.get(dietId);
        dietplanHashMap.remove(dietId);
        switch (dietplanAddItem.getMealTiming()) {
            case EARLY_MORNING:
                earlyMorning.remove(dietId);
                addDietplanItem(containerFoodEarlyMorning, new ArrayList<DietplanAddItem>(earlyMorning.values()));
                break;
            case BREAKFAST:
                breakfast.remove(dietId);
                addDietplanItem(containerFoodBreakfast, new ArrayList<DietplanAddItem>(breakfast.values()));
                break;
            case MID_MORNING:
                midMorning.remove(dietId);
                addDietplanItem(containerFoodMidMorning, new ArrayList<DietplanAddItem>(midMorning.values()));
                break;
            case LUNCH:
                lunch.remove(dietId);
                addDietplanItem(containerFoodLunch, new ArrayList<DietplanAddItem>(lunch.values()));
                break;
            case POST_LUNCH:
                postLunch.remove(dietId);
                addDietplanItem(containerFoodPostLunch, new ArrayList<DietplanAddItem>(postLunch.values()));
                break;
            case EVENING_SNACK:
                eveningSnacks.remove(dietId);
                addDietplanItem(containerFoodEveningSnacks, new ArrayList<DietplanAddItem>(eveningSnacks.values()));
                break;
            case DINNER:
                dinner.remove(dietId);
                addDietplanItem(containerFoodDinner, new ArrayList<DietplanAddItem>(dinner.values()));
                break;
            case POST_DINNER:
                postDinner.remove(dietId);
                addDietplanItem(containerFoodPostDinner, new ArrayList<DietplanAddItem>(postDinner.values()));
                break;
            case MID_NIGHT:
                midNight.remove(dietId);
                addDietplanItem(containerFoodMidNight, new ArrayList<DietplanAddItem>(midNight.values()));
                break;
        }
    }

}
