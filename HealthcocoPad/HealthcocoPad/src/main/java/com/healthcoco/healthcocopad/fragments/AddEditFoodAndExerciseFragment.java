package com.healthcoco.healthcocopad.fragments;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.adapter.UIPermissionItemGridAdapter;
import com.healthcoco.healthcocopad.bean.server.Exercise;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.DietPlanRecipeItem;
import com.healthcoco.healthcocopad.bean.server.FoodCravingRequest;
import com.healthcoco.healthcocopad.bean.server.Ingredient;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.MealTimeAndPattern;
import com.healthcoco.healthcocopad.bean.server.PatientFoodAndExcercise;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddExerciseDialogFragment;
import com.healthcoco.healthcocopad.enums.CommonOpenUpFragmentType;
import com.healthcoco.healthcocopad.enums.ExerciseType;
import com.healthcoco.healthcocopad.enums.FoodPreferenceType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.MealTimeType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonUiPermissionsListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by Prashant on 14/07/2018.
 */

public class AddEditFoodAndExerciseFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener, CommonUiPermissionsListener {

    private final int EDIT_ID = 100;
    private final int DELETE_ID = 101;
    public static final String TIME_FORMAT = "hh:mm aaa";
    public static final int DEFAULT_TIME_INTERVAL = 15;
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    TimePickerDialog datePickerDialog;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;

    private TextView tvTimeBreakfast;
    private LinearLayout layoutAddFoodBreakfast;
    private CheckBox cbBreakfast;
    private LinearLayout layoutBreakfast;
    private LinearLayout layoutTimeBreakfast;
    private LinearLayout containerFoodBreakfast;

    private TextView tvTimeEarlyMorning;
    private LinearLayout layoutAddFoodEarlyMorning;
    private CheckBox cbEarlyMorning;
    private LinearLayout layoutEarlyMorning;
    private LinearLayout layoutTimeEarlyMorning;
    private LinearLayout containerFoodEarlyMorning;

    private TextView tvTimeMidMorning;
    private LinearLayout layoutAddFoodMidMorning;
    private CheckBox cbMidMorning;
    private LinearLayout layoutMidMorning;
    private LinearLayout layoutTimeMidMorning;
    private LinearLayout containerFoodMidMorning;

    private TextView tvTimeLunch;
    private LinearLayout layoutAddFoodLunch;
    private CheckBox cbLunch;
    private LinearLayout layoutLunch;
    private LinearLayout layoutTimeLunch;
    private LinearLayout containerFoodLunch;

    private TextView tvTimeEveningSnacks;
    private LinearLayout layoutAddFoodEveningSnacks;
    private CheckBox cbEveningSnacks;
    private LinearLayout layoutEveningSnacks;
    private LinearLayout layoutTimeEveningSnacks;
    private LinearLayout containerFoodEveningSnacks;

    private TextView tvTimeDinner;
    private LinearLayout layoutAddFoodDinner;
    private CheckBox cbDinner;
    private LinearLayout layoutDinner;
    private LinearLayout layoutTimeDinner;
    private LinearLayout containerFoodDinner;

    private LinearLayout layoutAddFoodCravings;
    private LinearLayout containerFoodCravings;

    private LinearLayout layoutAddExercise;
    private LinearLayout containerExercise;

    private GridView gvFoodPreference;
    private UIPermissionItemGridAdapter adapter;
    private ArrayList<String> selectedFoodPreference = new ArrayList<>();
    private ArrayList<String> allFoodPreference;

    private LinkedHashMap<MealTimeType, MealTimeAndPattern> mealTimeAndPatternList = new LinkedHashMap<>();
    private LinkedHashMap<ExerciseType, Exercise> exerciseHashMap = new LinkedHashMap<>();

    private String assessmentId;
    private String patientId;
    private PatientFoodAndExcercise patientFoodAndExcercise;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_food_and_exercise, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();


        Intent intent = mActivity.getIntent();
        if (intent != null) {
            assessmentId = intent.getStringExtra(HealthCocoConstants.TAG_ASSESSMENT_ID);
            patientId = intent.getStringExtra(HealthCocoConstants.TAG_PATIENT_ID);
//            if (assessmentId != null)
//                measurementInfo = LocalDataServiceImpl.getInstance(mApp).getPatientMeasurementInfo(assessmentId);
//            if (measurementInfo != null)
//                initMeasurementInfo();
        }

        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initAdapter();
        initListData();
    }

    @Override
    public void initViews() {
        gvFoodPreference = (GridView) view.findViewById(R.id.gv_food_preference);

        tvTimeEarlyMorning = (TextView) view.findViewById(R.id.tv_time_early_morning);
        layoutAddFoodEarlyMorning = (LinearLayout) view.findViewById(R.id.layout_add_food_early_morning);
        cbEarlyMorning = (CheckBox) view.findViewById(R.id.cb_early_morning);
        layoutEarlyMorning = (LinearLayout) view.findViewById(R.id.layout_early_morning);
        layoutTimeEarlyMorning = (LinearLayout) view.findViewById(R.id.layout_time_early_morning);
        containerFoodEarlyMorning = (LinearLayout) view.findViewById(R.id.container_food_early_morning);

        tvTimeBreakfast = (TextView) view.findViewById(R.id.tv_time_breakfast);
        layoutAddFoodBreakfast = (LinearLayout) view.findViewById(R.id.layout_add_food_breakfast);
        cbBreakfast = (CheckBox) view.findViewById(R.id.cb_breakfast);
        layoutBreakfast = (LinearLayout) view.findViewById(R.id.layout_breakfast);
        layoutTimeBreakfast = (LinearLayout) view.findViewById(R.id.layout_time_breakfast);
        containerFoodBreakfast = (LinearLayout) view.findViewById(R.id.container_food_breakfast);

        tvTimeMidMorning = (TextView) view.findViewById(R.id.tv_time_mid_morning);
        layoutAddFoodMidMorning = (LinearLayout) view.findViewById(R.id.layout_add_food_mid_morning);
        cbMidMorning = (CheckBox) view.findViewById(R.id.cb_mid_morning);
        layoutMidMorning = (LinearLayout) view.findViewById(R.id.layout_mid_morning);
        layoutTimeMidMorning = (LinearLayout) view.findViewById(R.id.layout_time_mid_morning);
        containerFoodMidMorning = (LinearLayout) view.findViewById(R.id.container_food_mid_morning);

        tvTimeLunch = (TextView) view.findViewById(R.id.tv_time_lunch);
        layoutAddFoodLunch = (LinearLayout) view.findViewById(R.id.layout_add_food_lunch);
        cbLunch = (CheckBox) view.findViewById(R.id.cb_lunch);
        layoutLunch = (LinearLayout) view.findViewById(R.id.layout_lunch);
        layoutTimeLunch = (LinearLayout) view.findViewById(R.id.layout_time_lunch);
        containerFoodLunch = (LinearLayout) view.findViewById(R.id.container_food_lunch);

        tvTimeEveningSnacks = (TextView) view.findViewById(R.id.tv_time_evening_snack);
        layoutAddFoodEveningSnacks = (LinearLayout) view.findViewById(R.id.layout_add_food_evening_snack);
        cbEveningSnacks = (CheckBox) view.findViewById(R.id.cb_evening_snack);
        layoutEveningSnacks = (LinearLayout) view.findViewById(R.id.layout_evening_snack);
        layoutTimeEveningSnacks = (LinearLayout) view.findViewById(R.id.layout_time_evening_snack);
        containerFoodEveningSnacks = (LinearLayout) view.findViewById(R.id.container_food_evening_snack);

        tvTimeDinner = (TextView) view.findViewById(R.id.tv_time_dinner);
        layoutAddFoodDinner = (LinearLayout) view.findViewById(R.id.layout_add_food_dinner);
        cbDinner = (CheckBox) view.findViewById(R.id.cb_dinner);
        layoutDinner = (LinearLayout) view.findViewById(R.id.layout_dinner);
        layoutTimeDinner = (LinearLayout) view.findViewById(R.id.layout_time_dinner);
        containerFoodDinner = (LinearLayout) view.findViewById(R.id.container_food_dinner);

        layoutAddFoodCravings = (LinearLayout) view.findViewById(R.id.layout_add_food_cravings);
        containerFoodCravings = (LinearLayout) view.findViewById(R.id.container_food_cravings);

        layoutAddExercise = (LinearLayout) view.findViewById(R.id.layout_add_exercise);
        containerExercise = (LinearLayout) view.findViewById(R.id.container_exercise);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);

        cbEarlyMorning.setOnCheckedChangeListener(this);
        tvTimeEarlyMorning.setOnClickListener(this);
        layoutAddFoodEarlyMorning.setOnClickListener(this);


        cbBreakfast.setOnCheckedChangeListener(this);
        tvTimeBreakfast.setOnClickListener(this);
        layoutAddFoodBreakfast.setOnClickListener(this);

        cbMidMorning.setOnCheckedChangeListener(this);
        tvTimeMidMorning.setOnClickListener(this);
        layoutAddFoodMidMorning.setOnClickListener(this);

        cbLunch.setOnCheckedChangeListener(this);
        tvTimeLunch.setOnClickListener(this);
        layoutAddFoodLunch.setOnClickListener(this);

        cbEveningSnacks.setOnCheckedChangeListener(this);
        tvTimeEveningSnacks.setOnClickListener(this);
        layoutAddFoodEveningSnacks.setOnClickListener(this);

        cbDinner.setOnCheckedChangeListener(this);
        tvTimeDinner.setOnClickListener(this);
        layoutAddFoodDinner.setOnClickListener(this);

        layoutAddFoodCravings.setOnClickListener(this);
        layoutAddExercise.setOnClickListener(this);
    }

    public void initListData() {

        List<FoodPreferenceType> preferenceTypeList = (List<FoodPreferenceType>) Arrays.asList(FoodPreferenceType.values());

        allFoodPreference = new ArrayList<String>();
        for (FoodPreferenceType foodPreferenceType : preferenceTypeList) {
            allFoodPreference.add(foodPreferenceType.getFoodPreferenceType());
        }

        notifyAdapter(allFoodPreference);
    }

    private void initAdapter() {
        adapter = new UIPermissionItemGridAdapter(mActivity, this);
        gvFoodPreference.setAdapter(adapter);
    }

    private void notifyAdapter(List<String> list) {
        adapter.setListData(list);
        adapter.notifyDataSetChanged();
    }


    public void getPatientFoodAndExercise() {
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).getPatientAssessmentDetails(PatientFoodAndExcercise.class,
                WebServiceType.GET_PATIENT_FOOD_AND_EXERCISE, assessmentId, this, this);
    }

    private void initData() {
        if (patientFoodAndExcercise != null) {
            if (!Util.isNullOrEmptyList(patientFoodAndExcercise.getFoodPrefer())) {
                selectedFoodPreference.addAll(patientFoodAndExcercise.getFoodPrefer());
                notifyAdapter(allFoodPreference);
            }
            if (!Util.isNullOrEmptyList(patientFoodAndExcercise.getMealTimeAndPattern())) {
                for (MealTimeAndPattern mealTimeAndPattern : patientFoodAndExcercise.getMealTimeAndPattern()) {
                    updateDietPlan(mealTimeAndPattern, mealTimeAndPattern.getFromTime());
                }
            }
            if (!Util.isNullOrEmptyList(patientFoodAndExcercise.getExercise())) {
                for (Exercise exercise : patientFoodAndExcercise.getExercise()) {
                    exerciseHashMap.put(exercise.getType(), exercise);
                }
                addEquivalentMeasurement(new ArrayList<>(exerciseHashMap.values()));
            }
            if (!Util.isNullOrEmptyList(patientFoodAndExcercise.getFoodCravings())) {
                for (FoodCravingRequest cravingRequest : patientFoodAndExcercise.getFoodCravings()) {
                    MealTimeAndPattern timeAndPattern = new MealTimeAndPattern();
                    if (!Util.isNullOrEmptyList(timeAndPattern.getFood())) {
                        timeAndPattern.setFood(timeAndPattern.getFood());
                        timeAndPattern.setTimeType(MealTimeType.MID_NIGHT);
                        updateDietPlan(timeAndPattern, 0);
                    }
                }
                addEquivalentMeasurement(new ArrayList<>(exerciseHashMap.values()));
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
                        if (!Util.isNullOrBlank(assessmentId)) {
                            getPatientFoodAndExercise();
                        }
                    }
                    break;
                case GET_PATIENT_FOOD_AND_EXERCISE:
                    if (response.isValidData(response) && response.getData() instanceof PatientFoodAndExcercise) {
                        patientFoodAndExcercise = (PatientFoodAndExcercise) response.getData();
                        if (patientFoodAndExcercise != null) {
                            initData();
                        }
                    }
                    break;
                case ADD_PATIENT_FOOD_AND_EXERCISE:
                    if (response.isValidData(response) && response.getData() instanceof PatientFoodAndExcercise) {
                        PatientFoodAndExcercise data = (PatientFoodAndExcercise) response.getData();
                        LocalDataServiceImpl.getInstance(mApp).addPatientFoodAndExercise(data);
                        mActivity.hideLoading();

                        mActivity.setResult(HealthCocoConstants.RESULT_CODE_REGISTRATION);
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
//                    doctorClinicProfile = LocalDataServiceImpl.getInstance(mApp).getDoctorClinicProfile(user.getUniqueId(), user.getForeignLocationId());
//                    registeredDoctorProfileList = LocalDataServiceImpl.getInstance(mApp).getRegisterDoctorDetails(user.getForeignLocationId());
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
            case R.id.tv_time_early_morning:
            case R.id.tv_time_breakfast:
            case R.id.tv_time_mid_morning:
            case R.id.tv_time_lunch:
            case R.id.tv_time_evening_snack:
            case R.id.tv_time_dinner:
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.layout_add_food_early_morning:
                onAddMealClicked(MealTimeType.EARLY_MORNING);
                break;
            case R.id.layout_add_food_breakfast:
                onAddMealClicked(MealTimeType.BREAKFAST);
                break;
            case R.id.layout_add_food_mid_morning:
                onAddMealClicked(MealTimeType.MID_MORNING);
                break;
            case R.id.layout_add_food_lunch:
                onAddMealClicked(MealTimeType.LUNCH);
                break;
            case R.id.layout_add_food_evening_snack:
                onAddMealClicked(MealTimeType.EVENING_SNACK);
                break;
            case R.id.layout_add_food_dinner:
                onAddMealClicked(MealTimeType.DINNER);
                break;
            case R.id.layout_add_food_cravings:
                onAddMealClicked(MealTimeType.MID_NIGHT);
                break;
            case R.id.layout_add_exercise:
                openAddExerciseDialog(null);
                break;
            case EDIT_ID:
                Exercise exerciseObject = (Exercise) v.getTag();
                if (exerciseObject != null)
                    openAddExerciseDialog(exerciseObject);
                break;
            case DELETE_ID:
                ExerciseType tag = (ExerciseType) v.getTag();
                if (tag != null) {
                    exerciseHashMap.remove(tag);
                    addEquivalentMeasurement(new ArrayList<>(exerciseHashMap.values()));
                }
                break;
            case R.id.container_right_action:
                validateData();
                break;
        }
    }

    private void openAddExerciseDialog(Exercise exercise) {
        AddExerciseDialogFragment exerciseDialogFragment = new AddExerciseDialogFragment();
        if (exercise != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(exercise));
            exerciseDialogFragment.setArguments(bundle);
        }
        exerciseDialogFragment.setTargetFragment(this, HealthCocoConstants.REQUEST_CODE_ADD_EXERCISE);
        exerciseDialogFragment.show(mActivity.getSupportFragmentManager(),
                exerciseDialogFragment.getClass().getSimpleName());
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
//        String selectedDate = String.valueOf(tvSelectedDate.getText()).trim();

        if (Util.isNullOrBlank(msg)) {
            addFoodAndExercise();
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addFoodAndExercise() {

        mActivity.showLoading(false);

        PatientFoodAndExcercise foodAndExcercise = new PatientFoodAndExcercise();

        if (patientFoodAndExcercise != null) {
            foodAndExcercise.setPatientId(patientFoodAndExcercise.getPatientId());
            foodAndExcercise.setAssessmentId(patientFoodAndExcercise.getAssessmentId());
            foodAndExcercise.setDoctorId(patientFoodAndExcercise.getDoctorId());
            foodAndExcercise.setLocationId(patientFoodAndExcercise.getLocationId());
            foodAndExcercise.setHospitalId(patientFoodAndExcercise.getHospitalId());

        } else {
            if (!Util.isNullOrBlank(patientId))
                foodAndExcercise.setPatientId(patientId);
            if (!Util.isNullOrBlank(assessmentId))
                foodAndExcercise.setAssessmentId(assessmentId);
            foodAndExcercise.setDoctorId(user.getUniqueId());
            foodAndExcercise.setLocationId(user.getForeignLocationId());
            foodAndExcercise.setHospitalId(user.getForeignHospitalId());
        }
        addMealTimingType();

        foodAndExcercise.setFoodPrefer(selectedFoodPreference);
        foodAndExcercise.setFoodCravings(getSelectedFoodCravings());
        foodAndExcercise.setMealTimeAndPattern(new ArrayList<MealTimeAndPattern>(mealTimeAndPatternList.values()));
        foodAndExcercise.setExercise(new ArrayList<Exercise>(exerciseHashMap.values()));


        WebDataServiceImpl.getInstance(mApp).addPatientAssessmentInfo(PatientFoodAndExcercise.class,
                WebServiceType.ADD_PATIENT_FOOD_AND_EXERCISE, foodAndExcercise, this, this);


    }

    private void addMealTimingType() {
        if (!Util.isNullOrBlank(String.valueOf(tvTimeEarlyMorning.getText())))
            if (!Util.isNullOrZeroNumber((long) tvTimeEarlyMorning.getTag()) && mealTimeAndPatternList.containsKey(MealTimeType.EARLY_MORNING)) {
                mealTimeAndPatternList.get(MealTimeType.EARLY_MORNING)
                        .setFromTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeEarlyMorning.getTag()));
            }
        if (!Util.isNullOrBlank(String.valueOf(tvTimeBreakfast.getText())))
            if (!Util.isNullOrZeroNumber((long) tvTimeBreakfast.getTag()) && mealTimeAndPatternList.containsKey(MealTimeType.BREAKFAST)) {
                mealTimeAndPatternList.get(MealTimeType.BREAKFAST)
                        .setFromTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeBreakfast.getTag()));
            }
        if (!Util.isNullOrBlank(String.valueOf(tvTimeLunch.getText())))
            if (!Util.isNullOrZeroNumber((long) tvTimeLunch.getTag()) && mealTimeAndPatternList.containsKey(MealTimeType.LUNCH)) {
                mealTimeAndPatternList.get(MealTimeType.LUNCH)
                        .setFromTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeLunch.getTag()));
            }
        if (!Util.isNullOrBlank(String.valueOf(tvTimeMidMorning.getText())))
            if (!Util.isNullOrZeroNumber((long) tvTimeMidMorning.getTag()) && mealTimeAndPatternList.containsKey(MealTimeType.MID_MORNING)) {
                mealTimeAndPatternList.get(MealTimeType.MID_MORNING)
                        .setFromTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeMidMorning.getTag()));
            }
        if (!Util.isNullOrBlank(String.valueOf(tvTimeEveningSnacks.getText())))
            if (!Util.isNullOrZeroNumber((long) tvTimeEveningSnacks.getTag()) && mealTimeAndPatternList.containsKey(MealTimeType.EVENING_SNACK)) {
                mealTimeAndPatternList.get(MealTimeType.EVENING_SNACK)
                        .setFromTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeEveningSnacks.getTag()));
            }
        if (!Util.isNullOrBlank(String.valueOf(tvTimeDinner.getText())))
            if (!Util.isNullOrZeroNumber((long) tvTimeDinner.getTag()) && mealTimeAndPatternList.containsKey(MealTimeType.DINNER)) {
                mealTimeAndPatternList.get(MealTimeType.DINNER)
                        .setFromTime(DateTimeUtil.getMinutesFromFormattedTime((long) tvTimeDinner.getTag()));
            }
    }

    private List<FoodCravingRequest> getSelectedFoodCravings() {
        List<FoodCravingRequest> foodCravingRequestList = new ArrayList<>();
        if (!Util.isNullOrEmptyList(mealTimeAndPatternList)) {
            if (mealTimeAndPatternList.containsKey(MealTimeType.MID_NIGHT)) {
                MealTimeAndPattern mealTimeAndPattern = mealTimeAndPatternList.get(MealTimeType.MID_NIGHT);
                if (!Util.isNullOrEmptyList(mealTimeAndPattern.getFood())) {
                    for (DietPlanRecipeItem planRecipeItem : mealTimeAndPattern.getFood()) {
                        FoodCravingRequest foodCravingRequest = new FoodCravingRequest();
                        foodCravingRequest.setFood(planRecipeItem);
                        foodCravingRequestList.add(foodCravingRequest);
                    }
                }
                mealTimeAndPatternList.remove(MealTimeType.MID_NIGHT);
            }
        }
        return foodCravingRequestList;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.cb_early_morning:
                changeLayoutVisibility(isChecked, layoutTimeEarlyMorning, layoutEarlyMorning);
                break;
            case R.id.cb_breakfast:
                changeLayoutVisibility(isChecked, layoutTimeBreakfast, layoutBreakfast);
                break;
            case R.id.cb_mid_morning:
                changeLayoutVisibility(isChecked, layoutTimeMidMorning, layoutMidMorning);
                break;
            case R.id.cb_lunch:
                changeLayoutVisibility(isChecked, layoutTimeLunch, layoutLunch);
                break;
            case R.id.cb_evening_snack:
                changeLayoutVisibility(isChecked, layoutTimeEveningSnacks, layoutEveningSnacks);
                break;
            case R.id.cb_dinner:
                changeLayoutVisibility(isChecked, layoutTimeDinner, layoutDinner);
                break;

        }
    }

    private void changeLayoutVisibility(boolean isChecked, LinearLayout layoutTime, LinearLayout layoutFood) {
        if (isChecked) {
            layoutTime.setVisibility(View.VISIBLE);
            Util.toggleLayoutView(mActivity, layoutFood, true);
        } else {
            layoutTime.setVisibility(View.GONE);
            Util.toggleLayoutView(mActivity, layoutFood, false);
        }
    }

    @Override
    public boolean isAssigned(String permission) {
        return selectedFoodPreference.contains(permission);
    }

    @Override
    public void assignPermission(String permission) {
        if (!selectedFoodPreference.contains(permission))
            selectedFoodPreference.add(permission);
    }

    @Override
    public void removePermission(String permission) {
        if (selectedFoodPreference.contains(permission))
            selectedFoodPreference.remove(permission);
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

                if (msg == 0) {
                    LogUtils.LOGD(TAG, "Time lesser");
                    tvToTime.setText(DateTimeUtil.getFormattedDateTime(TIME_FORMAT, selectedFromDateTimeMillis));
                    tvToTime.setTag(selectedFromDateTimeMillis);
                } else {
                    openTimePickerDialog(selectedFromTime, tvToTime);
                    Util.showToast(mActivity, msg);
                    LogUtils.LOGD(TAG, "Time greater");
                }
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

    public void onAddMealClicked(MealTimeType mealTimeType) {
        MealTimeAndPattern mealTimeAndPattern = null;
        if (!Util.isNullOrEmptyList(mealTimeAndPatternList)) {
            mealTimeAndPattern = mealTimeAndPatternList.get(mealTimeType);
        }
        Intent intent = new Intent(mActivity, CommonOpenUpActivity.class);
        intent.putExtra(HealthCocoConstants.TAG_FRAGMENT_NAME, CommonOpenUpFragmentType.SELECT_RECIPES.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_TAB_TYPE, mealTimeType.ordinal());
        intent.putExtra(HealthCocoConstants.TAG_IS_FROM_ASSESSMENT, true);
        if (mealTimeAndPattern != null)
            intent.putExtra(HealthCocoConstants.TAG_INTENT_DATA, Parcels.wrap(mealTimeAndPattern));
        startActivityForResult(intent, HealthCocoConstants.REQUEST_CODE_ADD_MEAL, null);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case HealthCocoConstants.REQUEST_CODE_ADD_MEAL:
                if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_MEAL)
                    if (data != null) {
                        MealTimeAndPattern mealTimeAndPattern = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        if (mealTimeAndPattern != null) {
                            updateDietPlan(mealTimeAndPattern, 0);
                        }
                    }
                break;
            case HealthCocoConstants.REQUEST_CODE_ADD_EXERCISE:
                if (resultCode == mActivity.RESULT_OK)
                    if (data != null) {
                        Exercise exercise = Parcels.unwrap(data.getParcelableExtra(HealthCocoConstants.TAG_INTENT_DATA));
                        if (exercise != null) {
                            exerciseHashMap.put(exercise.getType(), exercise);
                            addEquivalentMeasurement(new ArrayList<>(exerciseHashMap.values()));
                        }
                    }
                break;

        }

    }


    private void updateDietPlan(MealTimeAndPattern mealTimeAndPattern, float mealTiming) {
        switch (mealTimeAndPattern.getTimeType()) {
            case EARLY_MORNING:
                addFood(containerFoodEarlyMorning, mealTimeAndPattern.getFood());
                cbEarlyMorning.setChecked(true);
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeEarlyMorning.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case BREAKFAST:
                addFood(containerFoodBreakfast, mealTimeAndPattern.getFood());
                cbBreakfast.setChecked(true);
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeBreakfast.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case MID_MORNING:
                addFood(containerFoodMidMorning, mealTimeAndPattern.getFood());
                cbMidMorning.setChecked(true);
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeMidMorning.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case LUNCH:
                addFood(containerFoodLunch, mealTimeAndPattern.getFood());
                cbLunch.setChecked(true);
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeLunch.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case EVENING_SNACK:
                addFood(containerFoodEveningSnacks, mealTimeAndPattern.getFood());
                cbEveningSnacks.setChecked(true);
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeEveningSnacks.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case DINNER:
                addFood(containerFoodDinner, mealTimeAndPattern.getFood());
                cbDinner.setChecked(true);
                if (!Util.isNullOrZeroNumber(mealTiming))
                    tvTimeDinner.setText(DateTimeUtil.getFormattedTime(0, Math.round((float) mealTiming)));
                break;
            case MID_NIGHT:
                addFood(containerFoodCravings, mealTimeAndPattern.getFood());
                break;
        }
        mealTimeAndPatternList.put(mealTimeAndPattern.getTimeType(), mealTimeAndPattern);
    }

    private void addFood(LinearLayout containerLayout, List<DietPlanRecipeItem> mealList) {
        containerLayout.removeAllViews();
        if (!Util.isNullOrEmptyList(mealList)) {
            for (DietPlanRecipeItem recipeItem : mealList) {

               /* LinearLayout layoutSubItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.sub_item_add_food, null);
                TextView tvTitle = (TextView) layoutSubItemPermission.findViewById(R.id.tv_title_food);
                TextView tvQuantity = (TextView) layoutSubItemPermission.findViewById(R.id.tv_quantity_food);
                TextView tvCalarie = (TextView) layoutSubItemPermission.findViewById(R.id.tv_calarie_food);
                tvTitle.setText("Milk");
                tvQuantity.setText("100ml");
                tvCalarie.setText("120Kcal");

                containerLayout.addView(layoutSubItemPermission);*/

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
                            if (ingredient.getQuantity() != null)
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
                containerLayout.addView(layoutRecipe);
            }
        }
    }

    private void addEquivalentMeasurement(ArrayList<Exercise> exerciseList) {
        containerExercise.removeAllViews();
        if (!Util.isNullOrEmptyList(exerciseList)) {
            containerExercise.setVisibility(View.VISIBLE);
            for (Exercise exercise : exerciseList) {

                LinearLayout layoutEquivalentQuantity = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.list_item_equivalent_measurement, null);
                TextView tvTimesPerWeek = (TextView) layoutEquivalentQuantity.findViewById(R.id.tv_type);
                TextView tvExerciseType = (TextView) layoutEquivalentQuantity.findViewById(R.id.tv_serving_type);
                TextView tvMinPerDay = (TextView) layoutEquivalentQuantity.findViewById(R.id.tv_value);

                TextViewFontAwesome btDelete = (TextViewFontAwesome) layoutEquivalentQuantity.findViewById(R.id.bt_delete);
                TextViewFontAwesome btEdit = (TextViewFontAwesome) layoutEquivalentQuantity.findViewById(R.id.bt_edit);

                tvExerciseType.setText(exercise.getType().getExcerciseType());
                tvMinPerDay.setText(Util.getValidatedValue(exercise.getMinPerDay()) + getString(R.string.mins_per_day));
                tvTimesPerWeek.setText(Util.getValidatedValue(exercise.getTimePerWeek()) + getString(R.string.per_week));


                btEdit.setId(EDIT_ID);
                btEdit.setTag(exercise);
                btEdit.setOnClickListener(this);

                btDelete.setId(DELETE_ID);
                btDelete.setTag(exercise.getType());
                btDelete.setOnClickListener(this);

                containerExercise.addView(layoutEquivalentQuantity);
            }
        } else
            containerExercise.setVisibility(View.GONE);
    }


}
