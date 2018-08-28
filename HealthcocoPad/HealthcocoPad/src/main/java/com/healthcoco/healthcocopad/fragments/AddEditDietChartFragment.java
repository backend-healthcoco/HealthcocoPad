package com.healthcoco.healthcocopad.fragments;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

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
import com.healthcoco.healthcocopad.enums.ActionbarLeftRightActionTypeDrawables;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.enums.PatientProfileScreenType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;


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

    public static final String TIME_FORMAT = "hh:mm aaa";
    public static final int DEFAULT_TIME_INTERVAL = 15;
    TimePickerDialog datePickerDialog;

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

    private TextView tvTimeEarlyMorning;
    private TextView tvTimeBreakfast;
    private TextView tvTimeMidMorning;
    private TextView tvTimeLunch;
    private TextView tvTimePostLunch;
    private TextView tvTimeEveningSnacks;
    private TextView tvTimeDinner;
    private TextView tvTimePostDinner;
    private TextView tvTimeMidNight;

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
    private DietPlan dietPlanReceived;
    private Button btAnalyse;
    private TextView tvTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_diet_chart, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = mActivity.getIntent();
        dietPlanReceived = Parcels.unwrap(intent.getParcelableExtra(PatientDietPlanDetailFragment.TAG_DIET_PLAN_DATA));

        init();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
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

        tvTimeEarlyMorning = (TextView) view.findViewById(R.id.tv_time_early_morning);
        tvTimeBreakfast = (TextView) view.findViewById(R.id.tv_time_breakfast);
        tvTimeMidMorning = (TextView) view.findViewById(R.id.tv_time_mid_morning);
        tvTimeLunch = (TextView) view.findViewById(R.id.tv_time_lunch);
        tvTimePostLunch = (TextView) view.findViewById(R.id.tv_time_post_lunch);
        tvTimeEveningSnacks = (TextView) view.findViewById(R.id.tv_time_evening_snack);
        tvTimeDinner = (TextView) view.findViewById(R.id.tv_time_dinner);
        tvTimePostDinner = (TextView) view.findViewById(R.id.tv_time_post_dinner);
        tvTimeMidNight = (TextView) view.findViewById(R.id.tv_time_mid_night);
        btAnalyse = (Button) view.findViewById(R.id.bt_analyse);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        tvTitle.setText(getString(R.string.add_diet_chart));

        tvTimeEarlyMorning.setTag(0l);
        tvTimeBreakfast.setTag(0l);
        tvTimeMidMorning.setTag(0l);
        tvTimeLunch.setTag(0l);
        tvTimePostLunch.setTag(0l);
        tvTimeEveningSnacks.setTag(0l);
        tvTimeDinner.setTag(0l);
        tvTimePostDinner.setTag(0l);
        tvTimeMidNight.setTag(0l);
    }


    @Override
    public void initListeners() {
        btAnalyse.setOnClickListener(this);
        ((CommonOpenUpActivity) mActivity).initRightActionView(ActionbarLeftRightActionTypeDrawables.WITH_SAVE, view);
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

        view.findViewById(R.id.tv_analyse_early_morning).setOnClickListener(this);
        view.findViewById(R.id.tv_analyse_breakfast).setOnClickListener(this);
        view.findViewById(R.id.tv_analyse_morning_snacks).setOnClickListener(this);
        view.findViewById(R.id.tv_analyse_lunch).setOnClickListener(this);
        view.findViewById(R.id.tv_analyse_post_lunch).setOnClickListener(this);
        view.findViewById(R.id.tv_analyse_evening_snack).setOnClickListener(this);
        view.findViewById(R.id.tv_analyse_dinner).setOnClickListener(this);
        view.findViewById(R.id.tv_analyse_post_dinner).setOnClickListener(this);
        view.findViewById(R.id.tv_analyse_mid_night).setOnClickListener(this);

        tvTimeEarlyMorning.setOnClickListener(this);
        tvTimeBreakfast.setOnClickListener(this);
        tvTimeMidMorning.setOnClickListener(this);
        tvTimeLunch.setOnClickListener(this);
        tvTimePostLunch.setOnClickListener(this);
        tvTimeEveningSnacks.setOnClickListener(this);
        tvTimeDinner.setOnClickListener(this);
        tvTimePostDinner.setOnClickListener(this);
        tvTimeMidNight.setOnClickListener(this);
    }

    public void initData() {
        if (dietPlanReceived != null) {
            if (!Util.isNullOrEmptyList(dietPlanReceived.getItems()))
                for (DietplanAddItem dietplanAddItem : dietPlanReceived.getItems()) {
                    dietplanAddItem.setForeignDietId(Util.getValidatedValue(DateTimeUtil.getCurrentDateLong()));
                    updateDietPlan(dietplanAddItem, dietplanAddItem.getToTime());
                }
        }
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
                            initActionPatientDetailActionBar(PatientProfileScreenType.IN_ADD_VISIT_HEADER, view, selectedPatient);
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
                         */
                        mActivity.setResult(HealthCocoConstants.RESULT_CODE_ADD_DIET, null);
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

        updateMealTimings(new ArrayList<DietplanAddItem>(dietplanHashMap.values()));

        dietPlanToSend.setItems(new ArrayList<DietplanAddItem>(dietplanHashMap.values()));

        if (dietPlanReceived != null) {
            if (dietPlanReceived.getUniqueId() != null)
                dietPlanToSend.setUniqueId(dietPlanReceived.getUniqueId());
            if (dietPlanReceived.getUniquePlanId() != null)
                dietPlanToSend.setUniquePlanId(dietPlanReceived.getUniquePlanId());
        }

        WebDataServiceImpl.getInstance(mApp).addDietPlan(DietPlan.class, dietPlanToSend, this, this);
    }

    private void updateMealTimings(ArrayList<DietplanAddItem> dietplanAddItemArrayList) {
        for (DietplanAddItem dietplanAddItem : dietplanAddItemArrayList) {
            updateDietPlanMealTime(dietplanAddItem);
        }
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
            case R.id.tv_time_early_morning:
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.tv_time_breakfast:
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.tv_time_mid_morning:
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.tv_time_lunch:
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.tv_time_post_lunch:
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.tv_time_evening_snack:
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.tv_time_dinner:
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.tv_time_post_dinner:
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.tv_time_mid_night:
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.tv_analyse_early_morning:
                analyseDietChart(new ArrayList<DietplanAddItem>(earlyMorning.values()));
                break;
            case R.id.tv_analyse_breakfast:
                analyseDietChart(new ArrayList<DietplanAddItem>(breakfast.values()));
                break;
            case R.id.tv_analyse_morning_snacks:
                analyseDietChart(new ArrayList<DietplanAddItem>(midMorning.values()));
                break;
            case R.id.tv_analyse_lunch:
                analyseDietChart(new ArrayList<DietplanAddItem>(lunch.values()));
                break;
            case R.id.tv_analyse_post_lunch:
                analyseDietChart(new ArrayList<DietplanAddItem>(postLunch.values()));
                break;
            case R.id.tv_analyse_evening_snack:
                analyseDietChart(new ArrayList<DietplanAddItem>(eveningSnacks.values()));
                break;
            case R.id.tv_analyse_dinner:
                analyseDietChart(new ArrayList<DietplanAddItem>(dinner.values()));
                break;
            case R.id.tv_analyse_post_dinner:
                analyseDietChart(new ArrayList<DietplanAddItem>(postDinner.values()));
                break;
            case R.id.tv_analyse_mid_night:
                analyseDietChart(new ArrayList<DietplanAddItem>(midNight.values()));
                break;
            case R.id.container_right_action:
                validateData();
                break;
            case R.id.bt_analyse:
                onAnalysedietChartClicked();
                break;
        }
    }

    private void onAnalysedietChartClicked() {
        ArrayList<DietplanAddItem> addItemList = new ArrayList<>();

        addItemList.addAll(new ArrayList<DietplanAddItem>(earlyMorning.values()));
        addItemList.addAll(new ArrayList<DietplanAddItem>(breakfast.values()));
        addItemList.addAll(new ArrayList<DietplanAddItem>(midMorning.values()));
        addItemList.addAll(new ArrayList<DietplanAddItem>(lunch.values()));
        addItemList.addAll(new ArrayList<DietplanAddItem>(postLunch.values()));
        addItemList.addAll(new ArrayList<DietplanAddItem>(eveningSnacks.values()));
        addItemList.addAll(new ArrayList<DietplanAddItem>(dinner.values()));
        addItemList.addAll(new ArrayList<DietplanAddItem>(postDinner.values()));
        addItemList.addAll(new ArrayList<DietplanAddItem>(midNight.values()));

        analyseDietChart(addItemList);
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
                            updateDietPlan(dietplanAddItem, 0);
                        }
                    }
                    break;
            }
        }

    }


    private void updateDietPlan(DietplanAddItem dietplanAddItem, float mealTiming) {
        dietplanHashMap.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
        switch (dietplanAddItem.getMealTiming()) {
            case EARLY_MORNING:
                earlyMorning.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodEarlyMorning, new ArrayList<DietplanAddItem>(earlyMorning.values()));
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeEarlyMorning.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case BREAKFAST:
                breakfast.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodBreakfast, new ArrayList<DietplanAddItem>(breakfast.values()));
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeBreakfast.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case MID_MORNING:
                midMorning.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodMidMorning, new ArrayList<DietplanAddItem>(midMorning.values()));
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeMidMorning.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case LUNCH:
                lunch.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodLunch, new ArrayList<DietplanAddItem>(lunch.values()));
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeLunch.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case POST_LUNCH:
                postLunch.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodPostLunch, new ArrayList<DietplanAddItem>(postLunch.values()));
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimePostLunch.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case EVENING_SNACK:
                eveningSnacks.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodEveningSnacks, new ArrayList<DietplanAddItem>(eveningSnacks.values()));
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeEveningSnacks.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case DINNER:
                dinner.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodDinner, new ArrayList<DietplanAddItem>(dinner.values()));
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeDinner.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case POST_DINNER:
                postDinner.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodPostDinner, new ArrayList<DietplanAddItem>(postDinner.values()));
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimePostDinner.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case MID_NIGHT:
                midNight.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
                addDietplanItem(containerFoodMidNight, new ArrayList<DietplanAddItem>(midNight.values()));
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeMidNight.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
        }
    }

    private void analyseDietChart(ArrayList<DietplanAddItem> itemArrayList) {
        if (!Util.isNullOrEmptyList(itemArrayList)) {
            DietPlan dietPlan = new DietPlan();
            dietPlan.setItems(itemArrayList);

            Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
            intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.ANALYSE.ordinal());
            if (dietPlan != null)
                intent.putExtra(AnalyseDietChartFragment.TAG_DIET_PLAN_CHART, Parcels.wrap(dietPlan));
            startActivity(intent);
        } else
            Util.showToast(mActivity, R.string.alert_add_analyse_diet_plan);
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

            tvTotalCalaries.setText(Util.round(dietplanAddItem.getCalTotal(), 2) + getString(R.string.cal_orange));
            tvTotalProtein.setText(Util.round(dietplanAddItem.getFatTotal(), 2) + getString(R.string.gm));
            tvTotalFat.setText(Util.round(dietplanAddItem.getProteinTotal(), 2) + getString(R.string.gm));
            tvTotalCarbs.setText(Util.round(dietplanAddItem.getCarbohydreateTotal(), 2) + getString(R.string.gm));
            tvTotalFiber.setText(Util.round(dietplanAddItem.getFiberTotal(), 2) + getString(R.string.gm));


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

                            if (ingredient.getQuantity().getType() != null) {
                                String type = ingredient.getQuantity().getType().getUnit();
                                if (!Util.isNullOrZeroNumber(ingredient.getQuantity().getValue())) {
                                    tvItemQuantity.setText(Util.getValidatedValue(ingredient.getQuantity().getValue()) + type);
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
                   /* } else
                    containerIngredients.setVisibility(View.GONE);*/
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


    private void openTimePickerDialog(final String selectedFromTime, final TextView tvToTime) {
        String defaultPickerTime = selectedFromTime;
        String textTime = Util.getValidatedValueOrNull(tvToTime);
        boolean isTextShown = false;
        if (!Util.isNullOrBlank(textTime)) {
            isTextShown = true;
            defaultPickerTime = textTime;
        }
        final Calendar calendar = DateTimeUtil.getCalendarInstanceFromFormattedTime(TIME_FORMAT, defaultPickerTime, isTextShown, DEFAULT_TIME_INTERVAL);

        datePickerDialog = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                long selectedFromDateTimeMillis = getSelectedFromDateTime(hourOfDay, minute);
                int msg = 0;
                LogUtils.LOGD(TAG, "Time lesser");
                tvToTime.setText(DateTimeUtil.getFormattedDateTime(TIME_FORMAT, selectedFromDateTimeMillis));
                tvToTime.setTag(selectedFromDateTimeMillis);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        if (!datePickerDialog.isShowing()) {
            datePickerDialog.show();
        }
    }

    private long getSelectedFromDateTime(int hourOfDay, int minute) {
        Calendar calendar1 = DateTimeUtil.getCalendarInstance();
        calendar1.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar1.set(Calendar.MINUTE, minute);
        return calendar1.getTimeInMillis();
    }

    private void updateDietPlanMealTime(DietplanAddItem dietplanAddItem) {
        switch (dietplanAddItem.getMealTiming()) {
            case EARLY_MORNING:
                if (!Util.isNullOrZeroNumber((long) tvTimeEarlyMorning.getTag()))
                    dietplanAddItem.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeEarlyMorning.getTag()));
                break;
            case BREAKFAST:
                if (!Util.isNullOrZeroNumber((long) tvTimeBreakfast.getTag()))
                    dietplanAddItem.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeBreakfast.getTag()));
                break;
            case MID_MORNING:
                if (!Util.isNullOrZeroNumber((long) tvTimeMidMorning.getTag()))
                    dietplanAddItem.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeMidMorning.getTag()));
                break;
            case LUNCH:
                if (!Util.isNullOrZeroNumber((long) tvTimeLunch.getTag()))
                    dietplanAddItem.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeLunch.getTag()));
                break;
            case POST_LUNCH:
                if (!Util.isNullOrZeroNumber((long) tvTimePostLunch.getTag()))
                    dietplanAddItem.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimePostLunch.getTag()));
                break;
            case EVENING_SNACK:
                if (!Util.isNullOrZeroNumber((long) tvTimeEveningSnacks.getTag()))
                    dietplanAddItem.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeEveningSnacks.getTag()));
                break;
            case DINNER:
                if (!Util.isNullOrZeroNumber((long) tvTimeDinner.getTag()))
                    dietplanAddItem.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeDinner.getTag()));
                break;
            case POST_DINNER:
                if (!Util.isNullOrZeroNumber((long) tvTimePostDinner.getTag()))
                    dietplanAddItem.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimePostDinner.getTag()));
                break;
            case MID_NIGHT:
                if (!Util.isNullOrZeroNumber((long) tvTimeMidNight.getTag()))
                    dietplanAddItem.setToTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeMidNight.getTag()));
                break;
        }
        dietplanHashMap.put(dietplanAddItem.getForeignDietId(), dietplanAddItem);
    }


}
