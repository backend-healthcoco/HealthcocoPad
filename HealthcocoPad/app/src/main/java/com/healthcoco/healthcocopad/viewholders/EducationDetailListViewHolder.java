package com.healthcoco.healthcocopad.viewholders;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.bean.server.Education;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.listeners.AutoCompleteTextViewListener;
import com.healthcoco.healthcocopad.listeners.EducationDetailItemListner;
import com.healthcoco.healthcocopad.utilities.EditTextTextViewErrorUtil;
import com.healthcoco.healthcocopad.utilities.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by neha on 03/02/16.
 */
public class EducationDetailListViewHolder extends LinearLayout implements View.OnClickListener, AdapterView.OnItemClickListener, AutoCompleteTextViewListener {

    private EducationDetailItemListner itemClickListener;
    private HealthCocoActivity mActivity;
    private Education objData;
    private View contentView;
    private TextView tvQualification;
    private TextView tvCollegeUniversity;
    private ImageButton btDelete;
    private HealthCocoApplication mApp;
    private AutoCompleteTextView autoTvYearOfPassing;

    public EducationDetailListViewHolder(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public EducationDetailListViewHolder(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public EducationDetailListViewHolder(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        this.mActivity = (HealthCocoActivity) context;
        this.mApp = (HealthCocoApplication) mActivity.getApplicationContext();
        inflate(context, R.layout.list_item_education_detail, this);
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
        tvQualification = (TextView) findViewById(R.id.tv_qualification);
        tvCollegeUniversity = (TextView) findViewById(R.id.tv_college_university);
        btDelete = (ImageButton) findViewById(R.id.bt_delete);
        autoTvYearOfPassing = (AutoCompleteTextView) findViewById(R.id.autotv_year_of_passing);
    }

    private void initListeners() {
        tvQualification.setOnClickListener(this);
        tvCollegeUniversity.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        autoTvYearOfPassing.setOnItemClickListener(this);
        autoTvYearOfPassing.setOnClickListener(this);
    }

    public void setData(Education education, EducationDetailItemListner itemClickListener, int position) {
        this.itemClickListener = itemClickListener;
        this.objData = education;
        autoTvYearOfPassing.setId(position);
//        itemClickListener.scrollToEnd(autoTvYearOfPassing);
        if (objData != null) {
            tvQualification.setText(Util.getValidatedValue(objData.getQualification()));
            tvCollegeUniversity.setText(Util.getValidatedValue(objData.getCollegeUniversity()));
            autoTvYearOfPassing.setText(Util.getValidatedValue(objData.getYearOfPassing()));
        }
        initAutoTvAdapter();
    }

    private void initAutoTvAdapter() {
        try {
            ArrayList<Object> list = (ArrayList<Object>) (ArrayList<?>) getYearsList();
            if (!Util.isNullOrEmptyList(list)) {
                AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, this, R.layout.spinner_drop_down_item_grey_background,
                        list, AutoCompleteTextViewType.YEAR_OF_PASSING);
                autoTvYearOfPassing.setThreshold(0);
                autoTvYearOfPassing.setAdapter(adapter);
                autoTvYearOfPassing.setDropDownAnchor(autoTvYearOfPassing.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Education getEducationDetail() {
        Education education = null;
        String qualification = Util.getValidatedValueOrNull(tvQualification);
        String collegeUniversity = Util.getValidatedValueOrNull(tvCollegeUniversity);
        Integer yearOfPassing = Util.getValidatedIntegerValue(autoTvYearOfPassing);
        if (!Util.isNullOrBlank(qualification)
                || !Util.isNullOrBlank(collegeUniversity)
                || yearOfPassing != null) {
            education = new Education();
            education.setQualification(qualification);
            education.setCollegeUniversity(collegeUniversity);
            education.setYearOfPassing(yearOfPassing);
        }
        return education;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_qualification:
                itemClickListener.onQualificationClicked(tvQualification, objData);
                break;
            case R.id.tv_college_university:
                itemClickListener.onCollegeUniversityClicked(tvCollegeUniversity, objData);
                break;
            case R.id.bt_delete:
                itemClickListener.onDeleteEducationDetailClicked(this, objData);
                break;
        }
    }

    private void clearPreviousAlerts() {
        tvQualification.setActivated(false);
        tvCollegeUniversity.setActivated(false);
        autoTvYearOfPassing.setActivated(false);
    }

    public Object getErrorMessageOrTrueIfValidated(boolean isOnSaveClick) {
        clearPreviousAlerts();
        ArrayList<View> errorViewList = new ArrayList<>();
        String msg = null;
        String qualification = Util.getValidatedValueOrNull(tvQualification);
        String collegeUniversity = Util.getValidatedValueOrNull(tvCollegeUniversity);
        String yearOfPassing = Util.getValidatedValueOrNull(autoTvYearOfPassing);
        if (isOnSaveClick && Util.isNullOrBlank(qualification) && Util.isNullOrBlank(collegeUniversity) && Util.isNullOrBlank(yearOfPassing)) {
            return true;
        } else if (Util.isNullOrBlank(qualification) && Util.isNullOrBlank(collegeUniversity) && Util.isNullOrBlank(yearOfPassing)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(tvQualification);
            errorViewList.add(tvCollegeUniversity);
            errorViewList.add(autoTvYearOfPassing);
        } else if (Util.isNullOrBlank(qualification)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(tvQualification);
        } else if (Util.isNullOrBlank(collegeUniversity)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(tvCollegeUniversity);
        } else if (Util.isNullOrBlank(yearOfPassing)) {
            msg = mActivity.getResources().getString(R.string.all_fields_are_mandatory);
            errorViewList.add(autoTvYearOfPassing);
        }
        if (Util.isNullOrBlank(msg)) {
            itemClickListener.addEducationDetailToList(getEducationDetail());
            return true;
        }
        EditTextTextViewErrorUtil.showErrorOnEditText(mActivity, errorViewList, msg);
        return msg;
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
