package com.healthcoco.healthcocopad.fragments;

import android.app.DatePickerDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.BloodPressure;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.GrowthChartRequest;
import com.healthcoco.healthcocopad.bean.server.GrowthChartResponse;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.HealthcocoTextWatcher;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.HealthcocoTextWatcherListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Calendar;

public class AddGrowthChartDialogFragment extends HealthCocoDialogFragment implements View.OnClickListener, HealthcocoTextWatcherListener,
        LocalDoInBackgroundListenerOptimised, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {
    private EditText editWeight;
    private EditText editHeight;
    private EditText editTemperature;
    private EditText editBmi;
    private EditText editSys;
    private EditText editDias;
    private EditText editBloodSugarF;
    private EditText editBloodSugarPP;
    private EditText editBmd;
    private EditText editHeadCircumfence;
    private GrowthChartResponse growthChartResponse;
    private User user;
    private RegisteredPatientDetailsUpdated selectedPatient;
    private TextView tvDate;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_growth_chart, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setWidthHeight(0.50, 0.80);
        init();
        Bundle bundle = getArguments();
        growthChartResponse = Parcels.unwrap(bundle.getParcelable(GrowthChartListFragment.TAG_GROWTH_CHART_DATA));
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
    }

    @Override
    public void initViews() {
        editWeight = (EditText) view.findViewById(R.id.edit_weight_kilograms);
        editHeight = (EditText) view.findViewById(R.id.edit_height_cm);
        editTemperature = (EditText) view.findViewById(R.id.edit_temp);
        editHeadCircumfence = (EditText) view.findViewById(R.id.edit_head_circumference);
        editBmi = (EditText) view.findViewById(R.id.edit_bmi);
        editSys = (EditText) view.findViewById(R.id.edit_sys);
        editDias = (EditText) view.findViewById(R.id.edit_dias);
        editBloodSugarF = (EditText) view.findViewById(R.id.edit_blood_sugar_f_mg_dl);
        editBloodSugarPP = (EditText) view.findViewById(R.id.edit_blood_sugar_pp_mg_dl);
        editBmd = (EditText) view.findViewById(R.id.edit_bmd);
        tvDate = (TextView) view.findViewById(R.id.tv_date);
    }

    @Override
    public void initListeners() {
        initSaveCancelButton(this);
        initActionbarTitle(getResources().getString(R.string.add_growth_chart));
        editHeight.addTextChangedListener(new HealthcocoTextWatcher(editHeight, this));
        editWeight.addTextChangedListener(new HealthcocoTextWatcher(editWeight, this));
        tvDate.setOnClickListener(this);
    }

    public void initData() {
        if (growthChartResponse != null) {
            if (growthChartResponse.getWeight() > 0)
                editWeight.setText(String.valueOf(Util.formatDoubleNumber(growthChartResponse.getWeight())));
            if (growthChartResponse.getHeight() > 0)
                editHeight.setText(String.valueOf(Util.formatDoubleNumber(growthChartResponse.getHeight())));
            if (growthChartResponse.getBmi() > 0)
                editBmi.setText(String.valueOf(Util.formatDoubleNumber(growthChartResponse.getBmi())));
            if (growthChartResponse.getSkullCircumference() > 0)
                editHeadCircumfence.setText(String.valueOf(Util.formatDoubleNumber(growthChartResponse.getSkullCircumference())));
            if (!Util.isNullOrBlank(growthChartResponse.getTemperature()))
                editTemperature.setText(Util.getValidatedValue(growthChartResponse.getTemperature()));
            if (!Util.isNullOrBlank(growthChartResponse.getBloodSugarF()))
                editBloodSugarF.setText(Util.getValidatedValue(growthChartResponse.getBloodSugarF()));
            if (!Util.isNullOrBlank(growthChartResponse.getBloodSugarPP()))
                editBloodSugarPP.setText(Util.getValidatedValue(growthChartResponse.getBloodSugarPP()));
            if (!Util.isNullOrBlank(growthChartResponse.getBmd()))
                editBmd.setText(Util.getValidatedValue(growthChartResponse.getBmd()));
            if (growthChartResponse.getAge() != null)
                tvDate.setText(Util.getDOB(growthChartResponse.getAge()));
            if (growthChartResponse.getBloodPressure() != null) {
                if (!Util.isNullOrBlank(growthChartResponse.getBloodPressure().getSystolic()))
                    editSys.setText(Util.getValidatedValue(growthChartResponse.getBloodPressure().getSystolic()));
                if (!Util.isNullOrBlank(growthChartResponse.getBloodPressure().getDiastolic()))
                    editDias.setText(Util.getValidatedValue(growthChartResponse.getBloodPressure().getDiastolic()));
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.bt_save) {
            validateData();
        } else if (id == R.id.tv_date) {
            openDatePickerDialog();
        }
    }

    private void openDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        calendar = DateTimeUtil.setCalendarDefaultvalue(calendar, DateTimeUtil.getCurrentFormattedDate(DateTimeUtil.DATE_FORMAT_DAY_MONTH_YEAR_SLASH));
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                tvDate.setText(DateTimeUtil.getBirthDateFormat(dayOfMonth, monthOfYear + 1, year));

            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void validateData() {
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String systolic = Util.getValidatedValueOrNull(editSys);
        String diastolic = Util.getValidatedValueOrNull(editDias);
        String weight = Util.getValidatedValueOrNull(editWeight);
        String height = Util.getValidatedValueOrNull(editHeight);
        if (Util.isNullOrBlank(weight))
            msg = getResources().getString(R.string.please_fill_weight);
        else if (Util.isNullOrBlank(height))
            msg = getResources().getString(R.string.please_fill_height);
        else if ((Util.isNullOrBlank(systolic) && !Util.isNullOrBlank(diastolic) || (!Util.isNullOrBlank(systolic) && Util.isNullOrBlank(diastolic))))
            msg = getResources().getString(R.string.please_add_systolic_and_diastolic_both);

        if (Util.isNullOrBlank(msg)) {
            addRequest();
        } else {
            EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, view, errorViewList, msg);
        }
    }

    private void addRequest() {
        mActivity.showLoading(false);
        GrowthChartRequest growthChartRequest = new GrowthChartRequest();
        growthChartRequest.setDoctorId(user.getUniqueId());
        growthChartRequest.setLocationId(user.getForeignLocationId());
        growthChartRequest.setHospitalId(user.getForeignHospitalId());
        growthChartRequest.setPatientId(selectedPatient.getUserId());
        if (growthChartResponse != null) {
            growthChartRequest.setUniqueId(growthChartResponse.getUniqueId());
        }
        growthChartRequest.setWeight(getValidatedDoubleValue(editWeight.getText().toString()));
        growthChartRequest.setHeight(getValidatedDoubleValue(editHeight.getText().toString()));
        growthChartRequest.setSkullCircumference(getValidatedDoubleValue(editHeadCircumfence.getText().toString()));
        growthChartRequest.setBmi(getValidatedDoubleValue(editBmi.getText().toString()));
        growthChartRequest.setTemperature(Util.getValidatedValueOrNull(editTemperature));
        growthChartRequest.setAge(DateTimeUtil.getDob(DateTimeUtil.DATE_FORMAT_DAY_MONTH_YEAR_SLASH, String.valueOf(tvDate.getText())));
        growthChartRequest.setBloodSugarF(Util.getValidatedValueOrNull(editBloodSugarF));
        growthChartRequest.setBloodSugarPP(Util.getValidatedValueOrNull(editBloodSugarPP));
        growthChartRequest.setBmd(Util.getValidatedValueOrNull(editBmd));
        if (!Util.isNullOrBlank(Util.getValidatedValueOrNull(editSys)) && !Util.isNullOrBlank(Util.getValidatedValueOrNull(editDias)))
            growthChartRequest.setBloodPressure(getBloodPressure(Util.getValidatedValueOrNull(editSys), Util.getValidatedValueOrNull(editDias)));
        WebDataServiceImpl.getInstance(mApp).addGrowthChart(GrowthChartResponse.class, WebServiceType.ADD_GROWTH_CHART, growthChartRequest, this, this);
    }

    private double getValidatedDoubleValue(String s) {
        double discount = 0;
        if (!Util.isNullOrBlank(s))
            discount = Double.parseDouble(s);
        else discount = 0;
        return discount;
    }

    private BloodPressure getBloodPressure(String systolic, String diastolic) {
        BloodPressure bloodPressure = new BloodPressure();
        bloodPressure.setSystolic(systolic);
        bloodPressure.setDiastolic(diastolic);
        return bloodPressure;
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
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    initData();
                    break;
                case ADD_GROWTH_CHART:
                    if (response.getData() != null) {
                        if (response.getData() instanceof GrowthChartResponse) {
                            GrowthChartResponse growthChartResponse = (GrowthChartResponse) response.getData();
                            LocalDataServiceImpl.getInstance(mApp).addGrowthChartResponse(growthChartResponse);
                            Util.sendBroadcast(mApp, GrowthChartListFragment.INTENT_REFRESH_REQUEST_LIST_FROM_SERVER);
                            getDialog().dismiss();
                        }
                    }
                    break;
            }
        }
        mActivity.hideLoading();
    }

    @Override
    public void afterTextChange(View v, String s) {
        int id = v.getId();

        if (id == R.id.edit_height_cm) {
            if (!Util.isNullOrBlank(s)
                    && !Util.isNullOrBlank(editWeight.getText().toString())
                    && !Util.isNullOrBlank(s)) {
                float weight = Float.parseFloat(editWeight.getText().toString());
                float height = Float.parseFloat(s);
                // BMI = weight in KG / square of (height in metre)
                float bmiValue = Util.calculateBMI(weight, height / 100f);
                editBmi.setText(Util.getFormattedFloatNumber(bmiValue));

                // BSA = square root of (weight X height / 3600)
                float bsaValue = Util.calculateBSA(weight, height);
                // You may want to use bsaValue somewhere
            } else {
                editBmi.setText("");
            }
        } else if (id == R.id.edit_weight_kilograms) {
            if (!Util.isNullOrBlank(s)
                    && !Util.isNullOrBlank(editHeight.getText().toString())
                    && !Util.isNullOrBlank(s)) {
                float weight = Float.parseFloat(s);
                float height = Float.parseFloat(editHeight.getText().toString());
                // BMI = weight in KG / square of (height in metre)
                float bmiValue = Util.calculateBMI(weight, height / 100f);
                editBmi.setText(Util.getFormattedFloatNumber(bmiValue));

                // BSA = square root of (weight X height / 3600)
                float bsaValue = Util.calculateBSA(weight, height);
                // You may want to use bsaValue somewhere
            } else {
                editBmi.setText("");
            }
        }
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = null;
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null)
                    user = doctor.getUser();
                selectedPatient = LocalDataServiceImpl.getInstance(mApp).getPatient(HealthCocoConstants.SELECTED_PATIENTS_USER_ID);
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
}
