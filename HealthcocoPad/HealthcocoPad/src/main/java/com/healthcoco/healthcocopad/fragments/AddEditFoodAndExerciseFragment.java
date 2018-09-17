package com.healthcoco.healthcocopad.fragments;

import android.app.TimePickerDialog;
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
import com.healthcoco.healthcocopad.adapter.UIPermissionItemGridAdapter;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonUiPermissionsListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * Created by Prashant on 14/07/2018.
 */

public class AddEditFoodAndExerciseFragment extends HealthCocoFragment implements
        GsonRequest.ErrorListener, LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>,
        View.OnClickListener, CompoundButton.OnCheckedChangeListener, CommonUiPermissionsListener {

    public static final String TIME_FORMAT = "hh:mm aaa";
    public static final int DEFAULT_TIME_INTERVAL = 15;
    private static final String DATE_FORMAT_USED_IN_THIS_SCREEN = "EEE, MMM dd,yyyy";
    TimePickerDialog datePickerDialog;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;

    private TextView tvTimeEarlyMorning;
    private TextViewFontAwesome tvAddFoodEarlyMorning;
    private TextView tvTimeBreakfast;
    private TextViewFontAwesome tvAddFoodBreakfast;

    private CheckBox cbEarlyMorning;
    private CheckBox cbBreakfast;

    private LinearLayout layoutEarlyMorning;
    private LinearLayout layoutTimeEarlyMorning;
    private LinearLayout layoutBreakfast;
    private LinearLayout layoutTimeBreakfast;

    private LinearLayout containerFoodEarlyMorning;
    private LinearLayout containerFoodBreakfast;

    private GridView gvFoodPreference;
    private UIPermissionItemGridAdapter adapter;
    private ArrayList<String> selectedFoodPreference = new ArrayList<>();
    private ArrayList<String> allFoodPreference = new ArrayList<String>() {{
        add("Grain");
        add("Milk");
        add("Egg");
        add("Fish");
        add("Meat");
        add("Seafood");
        add("Honey");
    }};

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
    }

    @Override
    public void initViews() {
        gvFoodPreference = (GridView) view.findViewById(R.id.gv_food_preference);

        tvTimeEarlyMorning = (TextView) view.findViewById(R.id.tv_time_early_morning);
        tvAddFoodEarlyMorning = (TextViewFontAwesome) view.findViewById(R.id.tv_add_food_early_morning);
        cbEarlyMorning = (CheckBox) view.findViewById(R.id.cb_early_morning);

        layoutEarlyMorning = (LinearLayout) view.findViewById(R.id.layout_early_morning);
        layoutTimeEarlyMorning = (LinearLayout) view.findViewById(R.id.layout_time_early_morning);
        containerFoodEarlyMorning = (LinearLayout) view.findViewById(R.id.container_food_early_morning);

        tvTimeBreakfast = (TextView) view.findViewById(R.id.tv_time_breakfast);
        tvAddFoodBreakfast = (TextViewFontAwesome) view.findViewById(R.id.tv_add_food_breakfast);
        cbBreakfast = (CheckBox) view.findViewById(R.id.cb_breakfast);

        layoutBreakfast = (LinearLayout) view.findViewById(R.id.layout_breakfast);
        layoutTimeBreakfast = (LinearLayout) view.findViewById(R.id.layout_time_breakfast);
        containerFoodBreakfast = (LinearLayout) view.findViewById(R.id.container_food_breakfast);
    }

    @Override
    public void initListeners() {
        cbEarlyMorning.setOnCheckedChangeListener(this);
        cbBreakfast.setOnCheckedChangeListener(this);

        tvTimeEarlyMorning.setOnClickListener(this);
        tvAddFoodEarlyMorning.setOnClickListener(this);

        tvTimeBreakfast.setOnClickListener(this);
        tvAddFoodBreakfast.setOnClickListener(this);
    }

    public void initData() {
    }

    private void initAdapter() {
        adapter = new UIPermissionItemGridAdapter(mActivity, this);
        gvFoodPreference.setAdapter(adapter);
        notifyAdapter(allFoodPreference);
    }

    private void notifyAdapter(List<String> list) {
        adapter.setListData(list);
        adapter.notifyDataSetChanged();
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
                openTimePickerDialog(null, (TextView) v);
                break;
            case R.id.tv_add_food_early_morning:
                addFood(containerFoodEarlyMorning);
                break;
            case R.id.tv_add_food_breakfast:
                addFood(containerFoodBreakfast);
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

        Util.getValidatedValueOrNull(tvTimeEarlyMorning);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        switch (compoundButton.getId()) {
            case R.id.cb_early_morning:
                if (isChecked) {
                    layoutTimeEarlyMorning.setVisibility(View.VISIBLE);
                    Util.toggleLayoutView(mActivity, layoutEarlyMorning, true);
                } else {
                    layoutTimeEarlyMorning.setVisibility(View.GONE);
                    Util.toggleLayoutView(mActivity, layoutEarlyMorning, false);
                }
                break;
            case R.id.cb_breakfast:
                if (isChecked) {
                    layoutTimeBreakfast.setVisibility(View.VISIBLE);
                    Util.toggleLayoutView(mActivity, layoutBreakfast, true);
                } else {
                    layoutTimeBreakfast.setVisibility(View.GONE);
                    Util.toggleLayoutView(mActivity, layoutBreakfast, false);
                }
                break;
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
