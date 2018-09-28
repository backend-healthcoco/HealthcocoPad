package com.healthcoco.healthcocopad.fragments;

import android.app.TimePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.UIPermissionItemGridAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AdapterType;
import com.healthcoco.healthcocopad.enums.FoodPreferenceType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonUiPermissionsListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.recyclerview.HealthcocoRecyclerViewAdapter;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Prashant on 26/09/2018.
 */

public class AddEditDietChartFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener {

    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;

    private TextViewFontAwesome tvAddEarlyMorning;
    private RecyclerView containerFoodEarlyMorning;

    private TextViewFontAwesome tvAddMorningSnacks;
    private LinearLayout containerFoodMorningSnacks;

    private TextViewFontAwesome tvAddBreakfast;
    private LinearLayout containerFoodBreakfast;

    private TextViewFontAwesome tvAddPostBreakfast;
    private LinearLayout containerFoodPostBreakfast;

    private TextViewFontAwesome tvAddLunch;
    private LinearLayout containerFoodLunch;

    private TextViewFontAwesome tvAddPostLunch;
    private LinearLayout containerFoodPostLunch;

    private TextViewFontAwesome tvAddEveningSnacks;
    private LinearLayout containerFoodEveningSnacks;

    private TextViewFontAwesome tvAddDinner;
    private LinearLayout containerFoodDinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_edit_food_and_exercise, container, false);
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
        containerFoodEarlyMorning = (RecyclerView) view.findViewById(R.id.container_food_early_morning);

        tvAddBreakfast = (TextViewFontAwesome) view.findViewById(R.id.tv_add_breakfast);
        containerFoodBreakfast = (LinearLayout) view.findViewById(R.id.container_food_breakfast);

        tvAddMorningSnacks = (TextViewFontAwesome) view.findViewById(R.id.tv_add_morning_snacks);
        containerFoodMorningSnacks = (LinearLayout) view.findViewById(R.id.container_food_morning_snacks);

        tvAddLunch = (TextViewFontAwesome) view.findViewById(R.id.tv_add_lunch);
        containerFoodLunch = (LinearLayout) view.findViewById(R.id.container_food_lunch);

        tvAddEveningSnacks = (TextViewFontAwesome) view.findViewById(R.id.tv_add_evening_snack);
        containerFoodEveningSnacks = (LinearLayout) view.findViewById(R.id.container_food_evening_snack);

        tvAddDinner = (TextViewFontAwesome) view.findViewById(R.id.tv_add_dinner);
        containerFoodDinner = (LinearLayout) view.findViewById(R.id.container_food_dinner);

        tvAddPostLunch = (TextViewFontAwesome) view.findViewById(R.id.tv_add_post_lunch);
        containerFoodPostLunch = (LinearLayout) view.findViewById(R.id.container_food_post_lunch);

        tvAddPostBreakfast = (TextViewFontAwesome) view.findViewById(R.id.tv_add_post_breakfast);
        containerFoodPostBreakfast = (LinearLayout) view.findViewById(R.id.container_food_post_breakfast);
    }

    @Override
    public void initListeners() {
        tvAddEarlyMorning.setOnClickListener(this);
        tvAddBreakfast.setOnClickListener(this);
        tvAddMorningSnacks.setOnClickListener(this);
        tvAddLunch.setOnClickListener(this);
        tvAddEveningSnacks.setOnClickListener(this);
        tvAddDinner.setOnClickListener(this);
        tvAddPostBreakfast.setOnClickListener(this);
        tvAddPostLunch.setOnClickListener(this);
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
            case R.id.tv_add_early_morning:
//                addFood(containerFoodEarlyMorning);
                break;
            case R.id.tv_add_breakfast:
//                addFood(containerFoodBreakfast);
                break;
        }
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
//        String selectedDate = String.valueOf(tvSelectedDate.getText()).trim();

        if (Util.isNullOrBlank(msg)) {
            addLifeStyle();
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addLifeStyle() {

//        Util.getValidatedValueOrNull(tvTimeEarlyMorning);

    }


    private void addFood(LinearLayout containerLayout) {
//        containerLayout.removeAllViews();

        LinearLayout layoutSubItemPermission = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.sub_item_add_food, null);
        TextView tvTitle = (TextView) layoutSubItemPermission.findViewById(R.id.tv_title_food);
        TextView tvQuantity = (TextView) layoutSubItemPermission.findViewById(R.id.tv_quantity_food);
        TextView tvCalarie = (TextView) layoutSubItemPermission.findViewById(R.id.tv_calarie_food);
        tvTitle.setText("Milk");
        tvQuantity.setText("100ml");
        tvCalarie.setText("120Kcal");

        containerLayout.addView(layoutSubItemPermission);
    }


}
