package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DietPlan;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.DietplanAddItem;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.DietItemClickListeners;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 05-07-2017.
 */

public class DietPlanListItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener
        , Response.Listener<VolleyResponseBean>, GsonRequest.ErrorListener {
    private DietPlan dietPlan;
    private TextView tvDate;
    private TextView tvInvoiceID;
    private TextView tvCreatedBy;
    private LinearLayout btEdit;
    private LinearLayout btPrint;
    private LinearLayout btDiscard;
    private LinearLayout layoutDiscarded;
    private LinearLayout btEmail;
    private LinearLayout containerDietItem;

    private HealthCocoActivity mActivity;
    private DietItemClickListeners dietItemClickListeners;

    private ArrayList<DietplanAddItem> earlyMorning;
    private ArrayList<DietplanAddItem> breakfast;
    private ArrayList<DietplanAddItem> midMorning;
    private ArrayList<DietplanAddItem> lunch;
    private ArrayList<DietplanAddItem> postLunch;
    private ArrayList<DietplanAddItem> eveningSnacks;
    private ArrayList<DietplanAddItem> dinner;
    private ArrayList<DietplanAddItem> postDinner;
    private ArrayList<DietplanAddItem> midNight;

    public DietPlanListItemViewHolder(HealthCocoActivity mActivity, DietItemClickListeners dietItemClickListeners) {
        super(mActivity);
        this.mActivity = mActivity;
        this.dietItemClickListeners = dietItemClickListeners;
    }

    @Override
    public void setData(Object object) {
        this.dietPlan = (DietPlan) object;
    }

    @Override
    public void applyData() {
        if (dietPlan.getCreatedTime() != null)
            tvDate.setText(DateTimeUtil.getFormatedDate(dietPlan.getCreatedTime()));

        if (!Util.isNullOrBlank(dietPlan.getUniquePlanId())) {
            tvInvoiceID.setVisibility(View.VISIBLE);
            tvInvoiceID.setText(mActivity.getResources().getString(R.string.diet_plan_id) + dietPlan.getUniquePlanId());
        } else tvInvoiceID.setVisibility(View.GONE);
        if (!Util.isNullOrBlank(dietPlan.getCreatedBy()))
            tvCreatedBy.setText(dietPlan.getCreatedBy());
        else
            tvCreatedBy.setText("");

        applyDataToDietPlanItems();

        checkDiscarded(dietPlan.getDiscarded());
    }

    private void checkDiscarded(Boolean isDiscarded) {
        if (isDiscarded != null && isDiscarded) {
            layoutDiscarded.setVisibility(View.VISIBLE);
        } else layoutDiscarded.setVisibility(View.GONE);
    }


    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.list_item_diet_plan, null);
        initViews(contentView);
        initListeners();
        return contentView;
    }

    private void initViews(View contentView) {
        tvDate = (TextView) contentView.findViewById(R.id.tv_date);
        tvInvoiceID = (TextView) contentView.findViewById(R.id.tv_invoice_id);
        tvCreatedBy = (TextView) contentView.findViewById(R.id.tv_created_by);
        btEmail = (LinearLayout) contentView.findViewById(R.id.bt_email);
        containerDietItem = (LinearLayout) contentView.findViewById(R.id.container_invoice);
        initBottomButtonViews(contentView);
    }

    private void initBottomButtonViews(View contentView) {
        btEdit = (LinearLayout) contentView.findViewById(R.id.bt_edit);
        btPrint = (LinearLayout) contentView.findViewById(R.id.bt_print);
        btDiscard = (LinearLayout) contentView.findViewById(R.id.bt_discard);
        layoutDiscarded = (LinearLayout) contentView.findViewById(R.id.layout_discarded);
    }

    private void initListeners() {
        btEdit.setOnClickListener(this);
        btEmail.setOnClickListener(this);
        btPrint.setOnClickListener(this);
        btDiscard.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit:
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    dietItemClickListeners.onEditDietClicked(dietPlan);
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_discard:
                LogUtils.LOGD(TAG, "Discard");
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    int msgId = R.string.confirm_discard_clinical_notes_message;
                    int titleId = R.string.confirm_discard_invoice_title;
                    showConfirmationAlert(v.getId(), mActivity.getResources().getString(titleId), mActivity.getResources().getString(msgId));
                } else onNetworkUnavailable(null);
                break;
            case R.id.bt_print:
            case R.id.tv_print:
                LogUtils.LOGD(TAG, "Print");
                Util.checkNetworkStatus(mActivity);
                if (HealthCocoConstants.isNetworkOnline) {
                    mActivity.showLoading(false);
//                    WebDataServiceImpl.getInstance(mApp).getPdfUrl(String.class, WebServiceType.GET_INVOICE_PDF_URL, invoice.getUniqueId(), this, this);
                } else onNetworkUnavailable(null);
                break;
            case R.id.tv_email:
            case R.id.bt_email:
                Util.checkNetworkStatus(mActivity);
//                if (HealthCocoConstants.isNetworkOnline)
//                    mActivity.openAddUpdateNameDialogFragment(WebServiceType.SEND_EMAIL_INVOICE, AddUpdateNameDialogType.EMAIL,
//                            invoice.getUniqueId(), invoice.getDoctorId(), invoice.getLocationId(), invoice.getHospitalId());
//
//                else onNetworkUnavailable(null);
                break;
        }
    }


    private void showConfirmationAlert(final int viewId, String title, String msg) {
        if (Util.isNullOrBlank(title))
            title = mActivity.getResources().getString(R.string.confirm);
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mActivity);
        alertBuilder.setTitle(title);
        alertBuilder.setMessage(msg);
        alertBuilder.setCancelable(false);
        alertBuilder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (viewId) {
                    case R.id.bt_discard:
//                        onDiscardedClicked(invoice);
                        break;
                }
            }
        });
        alertBuilder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        alertBuilder.create();
        alertBuilder.show();
    }

    public void onDiscardedClicked(Object object) {
        Invoice invoice = (Invoice) object;
        if (HealthCocoConstants.isNetworkOnline) {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).discardInvoice(Invoice.class, invoice.getUniqueId(), this, this);
        } else onNetworkUnavailable(null);
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response.getWebServiceType() != null) {
            LogUtils.LOGD(TAG, "Success " + String.valueOf(response.getWebServiceType()));
            switch (response.getWebServiceType()) {
                case GET_INVOICE_PDF_URL:
                    if (response.getData() != null && response.getData() instanceof String) {
                        mActivity.openEnlargedImageDialogFragment(true, (String) response.getData());
                    }
                    break;
                case DISCARD_INVOICE:
                    LogUtils.LOGD(TAG, "Success DISCARD_INVOICE");
//                    invoice.setDiscarded(!invoice.getDiscarded());
//                    applyData();
//                    LocalDataServiceImpl.getInstance(mApp).updateInvoice(invoice);
                    break;
            }
        }
        mActivity.hideLoading();
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
    }

    private void applyDataToDietPlanItems() {
        earlyMorning = new ArrayList<>();
        breakfast = new ArrayList<>();
        midMorning = new ArrayList<>();
        lunch = new ArrayList<>();
        postLunch = new ArrayList<>();
        eveningSnacks = new ArrayList<>();
        dinner = new ArrayList<>();
        postDinner = new ArrayList<>();
        midNight = new ArrayList<>();
        containerDietItem.removeAllViews();
        if (!Util.isNullOrEmptyList(dietPlan.getItems())) {
            for (DietplanAddItem dietplanAddItem : dietPlan.getItems()) {
                switch (dietplanAddItem.getMealTiming()) {
                    case EARLY_MORNING:
                        earlyMorning.add(dietplanAddItem);
                        break;
                    case BREAKFAST:
                        breakfast.add(dietplanAddItem);
                        break;
                    case MID_MORNING:
                        midMorning.add(dietplanAddItem);
                        break;
                    case LUNCH:
                        lunch.add(dietplanAddItem);
                        break;
                    case POST_LUNCH:
                        postLunch.add(dietplanAddItem);
                        break;
                    case EVENING_SNACK:
                        eveningSnacks.add(dietplanAddItem);
                        break;
                    case DINNER:
                        dinner.add(dietplanAddItem);
                        break;
                    case POST_DINNER:
                        postDinner.add(dietplanAddItem);
                        break;
                    case MID_NIGHT:
                        midNight.add(dietplanAddItem);
                        break;
                }
            }
            addDietPlan();
        } else {
            containerDietItem.setVisibility(View.GONE);
        }
    }

    private void addDietPlan() {
        if (!Util.isNullOrEmptyList(earlyMorning))
            addDiet(earlyMorning, MealTimeType.EARLY_MORNING.getMealTitle());
        if (!Util.isNullOrEmptyList(breakfast))
            addDiet(breakfast, MealTimeType.BREAKFAST.getMealTitle());
        if (!Util.isNullOrEmptyList(midMorning))
            addDiet(midMorning, MealTimeType.MID_MORNING.getMealTitle());
        if (!Util.isNullOrEmptyList(lunch))
            addDiet(lunch, MealTimeType.LUNCH.getMealTitle());
        if (!Util.isNullOrEmptyList(postLunch))
            addDiet(postLunch, MealTimeType.POST_LUNCH.getMealTitle());
        if (!Util.isNullOrEmptyList(eveningSnacks))
            addDiet(eveningSnacks, MealTimeType.EVENING_SNACK.getMealTitle());
        if (!Util.isNullOrEmptyList(dinner))
            addDiet(dinner, MealTimeType.DINNER.getMealTitle());
        if (!Util.isNullOrEmptyList(postDinner))
            addDiet(postDinner, MealTimeType.POST_DINNER.getMealTitle());
        if (!Util.isNullOrEmptyList(midNight))
            addDiet(midNight, MealTimeType.MID_NIGHT.getMealTitle());
    }

    private void addDiet(ArrayList<DietplanAddItem> itemArrayList, int dietTitle) {
        LinearLayout parentDietPlan = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_item_selected_diet, null);
        TextView title = (TextView) parentDietPlan.findViewById(R.id.container_title);
        LinearLayout containerDiet = (LinearLayout) parentDietPlan.findViewById(R.id.container_diet);
        title.setText(mActivity.getString(dietTitle));

        for (DietplanAddItem dietplanAddItem : itemArrayList) {
            LinearLayout layoutMeal = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_item_selected_diet_options, null);
            TextView tvOption = (TextView) layoutMeal.findViewById(R.id.tv_option);
            TextView tvTotalCalaries = (TextView) layoutMeal.findViewById(R.id.tv_total_cal);
            TextView tvTotalProtein = (TextView) layoutMeal.findViewById(R.id.tv_total_protein);
            TextView tvTotalFat = (TextView) layoutMeal.findViewById(R.id.tv_total_fat);
            TextView tvTotalCarbs = (TextView) layoutMeal.findViewById(R.id.tv_total_carbs);
            TextView tvTotalFiber = (TextView) layoutMeal.findViewById(R.id.tv_total_fiber);
            LinearLayout containerMeals = (LinearLayout) layoutMeal.findViewById(R.id.container_meal);
            tvOption.setText(mActivity.getString(R.string.diet_space) + (itemArrayList.indexOf(dietplanAddItem) + 1));

            double protein = 0;
            double fat = 0;
            double carbs = 0;
            double fiber = 0;
            double calaries = 0;

            for (DietPlanRecipeItem recipeItem : dietplanAddItem.getRecipes()) {
                LinearLayout layoutRecipe = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_sub_item_selected_recipe, null);
                TextView tvRecipeTitle = (TextView) layoutRecipe.findViewById(R.id.tv_ingredient_name);
                TextView tvRecipeQuantity = (TextView) layoutRecipe.findViewById(R.id.tv_quantity_ingredient);
                TextView tvRecipeCalarie = (TextView) layoutRecipe.findViewById(R.id.tv_serving_ingredient);
                LinearLayout containerIngredients = (LinearLayout) layoutRecipe.findViewById(R.id.container_ingredient);
                boolean isIngredientValue = false;

                tvRecipeTitle.setText(recipeItem.getName());

                if (recipeItem.getNutrientValueAtRecipeLevel() != null)
                    isIngredientValue = recipeItem.getNutrientValueAtRecipeLevel();
                if (isIngredientValue) {
                    tvRecipeQuantity.setVisibility(View.GONE);
                    tvRecipeCalarie.setVisibility(View.GONE);
                }

                if (recipeItem.getQuantity() != null) {
                    if (recipeItem.getQuantity().getType() != null) {
                        String quantityType = recipeItem.getQuantity().getType().getUnit();
                        if (!Util.isNullOrZeroNumber(recipeItem.getQuantity().getValue())) {
                            tvRecipeQuantity.setText(Util.getValidatedValue(recipeItem.getQuantity().getValue()) + quantityType);
                        }
                    }
                }
                if (recipeItem.getCalories() != null) {
                    if (!Util.isNullOrZeroNumber(recipeItem.getCalories().getValue())) {
                        tvRecipeCalarie.setText(Util.getValidatedValue(recipeItem.getCalories().getValue()) + mActivity.getString(R.string.cal_orange) /*+ calaries.getType().getQuantityType()*/);
                    }
                }

                containerIngredients.removeAllViews();
//                if (!recipeItem.getNutrientValueAtRecipeLevel()) {
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
                                String type = ingredient.getType().getUnit();
                                if (!Util.isNullOrZeroNumber(ingredient.getValue())) {
                                    tvItemQuantity.setText(Util.getValidatedValue(ingredient.getValue()) + type);
                                }
                            }
                            if (ingredient.getCalories() != null) {
                                if (!Util.isNullOrZeroNumber(ingredient.getCalories().getValue())) {
                                    tvItemCalarie.setText(Util.getValidatedValue(ingredient.getCalories().getValue()) + mActivity.getString(R.string.cal_orange));
                                }
                            }
                            containerIngredients.addView(layoutSubItemPermission);
                        }
                    }
                } else
                    containerIngredients.setVisibility(View.GONE);
                /*} else {
                    containerIngredients.setVisibility(View.GONE);*/
//                }
                if (recipeItem.getCalories() != null) {
                    calaries = calaries + recipeItem.getCalories().getValue();
                }
                if (recipeItem.getFat() != null) {
                    fat = fat + recipeItem.getFat().getValue();
                }
                if (recipeItem.getProtein() != null) {
                    protein = protein + recipeItem.getProtein().getValue();
                }
                if (recipeItem.getCarbohydreate() != null) {
                    carbs = carbs + recipeItem.getCarbohydreate().getValue();
                }
                if (recipeItem.getFiber() != null) {
                    fiber = fiber + recipeItem.getFiber().getValue();
                }


                containerMeals.addView(layoutRecipe);
            }

            dietplanAddItem.setCalTotal(calaries);
            dietplanAddItem.setProteinTotal(protein);
            dietplanAddItem.setFatTotal(fat);
            dietplanAddItem.setCarbohydreateTotal(carbs);
            dietplanAddItem.setFiberTotal(fiber);

            tvTotalCalaries.setText(Util.round(dietplanAddItem.getCalTotal(), 2) + mActivity.getString(R.string.cal_orange));
            tvTotalProtein.setText(Util.round(dietplanAddItem.getFatTotal(), 2) + mActivity.getString(R.string.gm));
            tvTotalFat.setText(Util.round(dietplanAddItem.getProteinTotal(), 2) + mActivity.getString(R.string.gm));
            tvTotalCarbs.setText(Util.round(dietplanAddItem.getCarbohydreateTotal(), 2) + mActivity.getString(R.string.gm));
            tvTotalFiber.setText(Util.round(dietplanAddItem.getFiberTotal(), 2) + mActivity.getString(R.string.gm));


            containerDiet.addView(layoutMeal);
        }
        containerDietItem.addView(parentDietPlan);
    }
}
