package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.bean.server.DoctorRegistrationDetail;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.listeners.RegistrationDetailItemListener;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by neha on 03/02/16.
 */
public class RegistrationDetailListViewHolder extends LinearLayout implements View.OnClickListener {
    private RegistrationDetailItemListener itemClickListener;
    private HealthCocoActivity mActivity;
    private DoctorRegistrationDetail objData;
    private EditText editRegistrationId;
    private TextView tvMedicalCouncil;
    private ImageButton btDelete;
    private HealthCocoApplication mApp;
    private AutoCompleteTextView autoTvYearOfRegistration;

    public RegistrationDetailListViewHolder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public RegistrationDetailListViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RegistrationDetailListViewHolder(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mActivity = (HealthCocoActivity) context;
        this.mApp = (HealthCocoApplication) mActivity.getApplicationContext();
        inflate(context, R.layout.list_item_registration_detail, this);
        EditTextTextViewErrorUtil.setUpUI(mActivity, this);
        initViews();
        initListeners();
    }

    private void initViews() {
        editRegistrationId = (EditText) findViewById(R.id.edit_registration_id);
        tvMedicalCouncil = (TextView) findViewById(R.id.tv_medical_council);
        btDelete = (ImageButton) findViewById(R.id.bt_delete);
        autoTvYearOfRegistration = (AutoCompleteTextView) findViewById(R.id.autotv_year_of_registration);
    }

    private void initListeners() {
        tvMedicalCouncil.setOnClickListener(this);
        btDelete.setOnClickListener(this);
    }

    public void setData(DoctorRegistrationDetail registrationDetail, RegistrationDetailItemListener itemClickListener, int position) {
        this.itemClickListener = itemClickListener;
        this.objData = registrationDetail;
        autoTvYearOfRegistration.setId(position);
        initAutoTvAdapter();
        if (objData != null) {
            editRegistrationId.setText(Util.getValidatedValue(objData.getRegistrationId()));
            tvMedicalCouncil.setText(Util.getValidatedValue(objData.getMedicalCouncil()));
            autoTvYearOfRegistration.setText(Util.getValidatedValue(objData.getYearOfPassing()));
        }
    }

    private void initAutoTvAdapter() {
        try {
            ArrayList<Object> list = (ArrayList<Object>) (ArrayList<?>) getYearsList();
            if (!Util.isNullOrEmptyList(list)) {
                AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.spinner_drop_down_item_grey_background,
                        list, AutoCompleteTextViewType.YEAR_OF_PASSING);
                autoTvYearOfRegistration.setThreshold(0);
                autoTvYearOfRegistration.setAdapter(adapter);
                autoTvYearOfRegistration.setDropDownAnchor(autoTvYearOfRegistration.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<?> getYearsList() {
        ArrayList<String> yearsList = new ArrayList<String>();
        for (int i = 1900; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            yearsList.add(String.valueOf(i));
        }
        return yearsList;
    }

    public DoctorRegistrationDetail getEducationDetail() {
        DoctorRegistrationDetail registrationDetail = null;
        String registrationId = Util.getValidatedValueOrNull(editRegistrationId);
        String medicalCouncil = Util.getValidatedValueOrNull(tvMedicalCouncil);
        Integer year = Util.getValidatedIntegerValue(autoTvYearOfRegistration);
        if (!Util.isNullOrBlank(registrationId)
                || !Util.isNullOrBlank(medicalCouncil)
                || year != null) {
            registrationDetail = new DoctorRegistrationDetail();
            registrationDetail.setRegistrationId(registrationId);
            registrationDetail.setMedicalCouncil(medicalCouncil);
            registrationDetail.setYearOfPassing(year);
        }
        return registrationDetail;
    }

    @Override
    public void onClick(View v) {
        clearPreviousAlerts();
        switch (v.getId()) {
            case R.id.tv_medical_council:
                itemClickListener.onMedicalCouncilClicked(tvMedicalCouncil, objData);
                break;
            case R.id.bt_delete:
                itemClickListener.onDeleteRegistrationDetailClicked(this, objData);
        }
    }

    private void clearPreviousAlerts() {
        editRegistrationId.setActivated(false);
        tvMedicalCouncil.setActivated(false);
        autoTvYearOfRegistration.setActivated(false);
    }

    public Object getErrorMessageOrTrueIfValidated(boolean isOnSaveClick) {
        clearPreviousAlerts();
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String registrationId = Util.getValidatedValueOrNull(editRegistrationId);
        String medicalCouncil = Util.getValidatedValueOrNull(tvMedicalCouncil);
        String year = Util.getValidatedValueOrNull(autoTvYearOfRegistration);
        if (isOnSaveClick && Util.isNullOrBlank(registrationId) && Util.isNullOrBlank(medicalCouncil) && Util.isNullOrBlank(year)) {
            return true;
        } else if (Util.isNullOrBlank(registrationId) && Util.isNullOrBlank(medicalCouncil) && Util.isNullOrBlank(year)) {
            errorViewList.add(editRegistrationId);
            errorViewList.add(tvMedicalCouncil);
            errorViewList.add(autoTvYearOfRegistration);
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
        } else if (Util.isNullOrBlank(registrationId)) {
            errorViewList.add(editRegistrationId);
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
        } else if (Util.isNullOrBlank(medicalCouncil)) {
            errorViewList.add(tvMedicalCouncil);
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
        } else if (Util.isNullOrBlank(year)) {
            errorViewList.add(autoTvYearOfRegistration);
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
        }
        if (Util.isNullOrBlank(msg)) {
            itemClickListener.addRegistrationDetailToList(getEducationDetail());
            return true;
        }
        EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, errorViewList, msg);
        return msg;
    }

}
