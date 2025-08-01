package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.DoctorExperienceDetail;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocopad.listeners.ExperienceDetailItemListener;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Shreshtha on 20-02-2017.
 */
public class ExperienceDetailListViewHolder extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener, AutoCompleteTextViewListener {
    private HealthCocoActivity mActivity;
    private HealthCocoApplication mApp;
    private EditText etClinicName;
    private EditText etCity;
    private ImageButton btDelete;
    private AutoCompleteTextView autotvStartYear;
    private AutoCompleteTextView autotvEndYear;
    private DoctorExperienceDetail objData;
    private ExperienceDetailItemListener experienceDetailItemListener;

    public ExperienceDetailListViewHolder(Context context) {
        super(context);
        init(context);
    }

    public ExperienceDetailListViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ExperienceDetailListViewHolder(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mActivity = (HealthCocoActivity) context;
        this.mApp = (HealthCocoApplication) mActivity.getApplicationContext();
        inflate(context, R.layout.list_item_experience_detail, this);
        EditTextTextViewErrorUtil.setUpUI(mActivity, this);
        initViews();
        initListeners();
    }

    private List<?> getYearsList() {
        ArrayList<String> yearsList = new ArrayList<String>();
        for (int i = 1900; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            yearsList.add(String.valueOf(i));
        }
        return yearsList;
    }

    private void initViews() {
        etClinicName = (EditText) findViewById(R.id.et_clinic_name);
        etCity = (EditText) findViewById(R.id.et_city);
        btDelete = (ImageButton) findViewById(R.id.bt_delete);
        autotvStartYear = (AutoCompleteTextView) findViewById(R.id.autotv_start_year);
        autotvEndYear = (AutoCompleteTextView) findViewById(R.id.autotv_end_year);
    }

    private void initListeners() {
        btDelete.setOnClickListener(this);
        autotvStartYear.setOnItemClickListener(this);
        autotvStartYear.setOnClickListener(this);
        autotvEndYear.setOnItemClickListener(this);
        autotvEndYear.setOnClickListener(this);
    }

    public void setData(DoctorExperienceDetail experienceDetail, ExperienceDetailItemListener itemClickListener, int position) {
        this.experienceDetailItemListener = itemClickListener;
        this.objData = experienceDetail;
        autotvStartYear.setId(position);
        autotvEndYear.setId(position);
        if (objData != null) {
            etClinicName.setText(Util.getValidatedValue(objData.getOrganization()));
            autotvStartYear.setText(Util.getValidatedValue(objData.getFromValue()));
            etCity.setText(Util.getValidatedValue(objData.getCity()));
            autotvEndYear.setText(Util.getValidatedValue(objData.getToValue()));
        }
        initAutoTvAdapter();
    }

    private void initAutoTvAdapter() {
        try {
            ArrayList<Object> list = (ArrayList<Object>) (ArrayList<?>) getYearsList();
            if (!Util.isNullOrEmptyList(list)) {
                AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, this, R.layout.spinner_drop_down_item_grey_background,
                        list, AutoCompleteTextViewType.YEAR_OF_PASSING);
                autotvStartYear.setThreshold(0);
                autotvStartYear.setAdapter(adapter);
                autotvStartYear.setDropDownAnchor(autotvStartYear.getId());
                autotvEndYear.setThreshold(0);
                autotvEndYear.setAdapter(adapter);
                autotvEndYear.setDropDownAnchor(autotvEndYear.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bt_delete) {
            experienceDetailItemListener.onDeleteExperienceDetailClicked(this, objData);
        }
    }

    private void clearPreviousAlerts() {
        etClinicName.setActivated(false);
        autotvStartYear.setActivated(false);
        etCity.setActivated(false);
        autotvEndYear.setActivated(false);
    }

    public Object getErrorMessageOrTrueIfValidated(boolean isOnSaveClick) {
        clearPreviousAlerts();
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String clinicName = Util.getValidatedValueOrNull(etClinicName);
        String startYear = Util.getValidatedValueOrNull(autotvStartYear);
        String city = Util.getValidatedValueOrNull(etCity);
        String endYear = Util.getValidatedValueOrNull(autotvEndYear);
        if (isOnSaveClick && Util.isNullOrBlank(clinicName) && Util.isNullOrBlank(startYear) && Util.isNullOrBlank(city) && Util.isNullOrBlank(endYear)) {
            return true;
        } else if (Util.isNullOrBlank(clinicName) && Util.isNullOrBlank(startYear) && Util.isNullOrBlank(city) && Util.isNullOrBlank(endYear)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(etClinicName);
            errorViewList.add(autotvStartYear);
            errorViewList.add(etCity);
            errorViewList.add(autotvEndYear);
        } else if (Util.isNullOrBlank(clinicName)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(etClinicName);
        } else if (Util.isNullOrBlank(startYear)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(autotvStartYear);
        } else if (Util.isNullOrBlank(city)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(etCity);
        } else if (Util.isNullOrBlank(endYear)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(autotvEndYear);
        }
        if (Util.isNullOrBlank(msg)) {
            experienceDetailItemListener.addExperienceDetailToList(getExperience());
            return true;
        }
        EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, errorViewList, msg);
        return msg;
    }

    private DoctorExperienceDetail getExperience() {
        DoctorExperienceDetail experienceDetail = null;
        String clinicName = Util.getValidatedValueOrNull(etClinicName);
        Integer startYear = Util.getValidatedIntegerValue(autotvStartYear);
        String city = Util.getValidatedValueOrNull(etCity);
        Integer endYear = Util.getValidatedIntegerValue(autotvEndYear);
        if (!Util.isNullOrBlank(clinicName) || startYear != null || !Util.isNullOrBlank(city) || endYear != null) {
            experienceDetail = new DoctorExperienceDetail();
            experienceDetail.setOrganization(clinicName);
            experienceDetail.setFromValue(startYear);
            experienceDetail.setCity(city);
            experienceDetail.setToValue(endYear);
        }
        return experienceDetail;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onEmptyListFound(AutoCompleteTextView autoCompleteTextView) {

    }

    @Override
    public void scrollToPosition(int position) {

    }
}
