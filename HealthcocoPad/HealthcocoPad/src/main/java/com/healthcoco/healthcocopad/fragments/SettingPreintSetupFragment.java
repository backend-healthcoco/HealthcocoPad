package com.healthcoco.healthcocopad.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

/**
 * Created by Prashant on 05/02/2018.
 */

public class SettingPreintSetupFragment extends HealthCocoFragment implements GsonRequest.ErrorListener,
        LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, View.OnClickListener {

    private TextView tvPageSize;
    private TextView tvBottomMargin;
    private TextView tvTopMargin;
    private TextView tvLeftMargin;
    private TextView tvRightMargin;
    private TextView tvFontsize;
    private TextView tvShowDrugGenericNames;
    private TextView tvLineSpace;
    private TextView tvShowTreatmentCost;
    private TextView tvShowInstructionAlign;
    private TextView tvIncludeHeader;
    private TextView tvIncludeLogo;
    private TextView tvTopleftFirst;
    private TextView tvTopleftSecond;
    private TextView tvTopleftThird;
    private TextView tvTopleftFourth;
    private TextView tvTopRightFirst;
    private TextView tvTopRightSecond;
    private TextView tvTopRightThird;
    private TextView tvTopRightFourth;
    private TextView tvIncludeFooter;
    private TextView tvIncludeSignature;
    private TextView tvIncludeSignatureText;
    private TextView tvBottomSignatureText;
    private TextView tvBottomText;
    private TextView tvIncludePatientName;
    private TextView tvIncludePatientID;
    private TextView tvIncludeAge;
    private TextView tvIncludeGender;
    private TextView tvIncludeMobileNumber;
    private TextView tvIncludeHospitalId;
    private TextView tvIncludeRefferedBy;
    private TextView tvIncludeBloodGroup;
    private TextView tvIncludeRecordId;
    private TextView tvIncludeDate;
    private TextView tvIncludeCity;
    private TextViewFontAwesome btEditPageSetup;
    private TextViewFontAwesome btEditContentSetup;
    private TextViewFontAwesome btEditHeader;
    private TextViewFontAwesome btEditFooter;
    private TextViewFontAwesome btEditPatirntDetails;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting_print_set_up, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Util.checkNetworkStatus(mActivity);
        init();
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.GET_FRAGMENT_INITIALISATION_DATA, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public void initViews() {
        tvPageSize = (TextView) view.findViewById(R.id.tv_page_size);
        tvBottomMargin = (TextView) view.findViewById(R.id.tv_bottom_margin);
        tvTopMargin = (TextView) view.findViewById(R.id.tv_top_margin);
        tvLeftMargin = (TextView) view.findViewById(R.id.tv_left_margin);
        tvRightMargin = (TextView) view.findViewById(R.id.tv_right_margin);
        tvFontsize = (TextView) view.findViewById(R.id.tv_font_size);
        tvShowDrugGenericNames = (TextView) view.findViewById(R.id.tv_show_drug_generic_namesn);
        tvLineSpace = (TextView) view.findViewById(R.id.tv_line_space);
        tvShowTreatmentCost = (TextView) view.findViewById(R.id.tv_show_treatment_cost);
        tvShowInstructionAlign = (TextView) view.findViewById(R.id.tv_show_instruction_align);
        tvIncludeHeader = (TextView) view.findViewById(R.id.tv_include_header);
        tvIncludeLogo = (TextView) view.findViewById(R.id.tv_include_logo);
        tvTopleftFirst = (TextView) view.findViewById(R.id.tv_topleft_first);
        tvTopleftSecond = (TextView) view.findViewById(R.id.tv_topleft_second);
        tvTopleftThird = (TextView) view.findViewById(R.id.tv_topleft_third);
        tvTopleftFourth = (TextView) view.findViewById(R.id.tv_topleft_forth);
        tvTopRightFirst = (TextView) view.findViewById(R.id.tv_topright_first);
        tvTopRightSecond = (TextView) view.findViewById(R.id.tv_topright_second);
        tvTopRightThird = (TextView) view.findViewById(R.id.tv_topright_third);
        tvTopRightFourth = (TextView) view.findViewById(R.id.tv_topright_forth);
        tvIncludeFooter = (TextView) view.findViewById(R.id.tv_include_footer);
        tvIncludeSignature = (TextView) view.findViewById(R.id.tv_include_signature);
        tvIncludeSignatureText = (TextView) view.findViewById(R.id.tv_include_signature_text);
        tvBottomSignatureText = (TextView) view.findViewById(R.id.tv_bottom_signature_text);
        tvBottomText = (TextView) view.findViewById(R.id.tv_bottom_text);
        tvIncludePatientName = (TextView) view.findViewById(R.id.tv_include_patient_name);
        tvIncludePatientID = (TextView) view.findViewById(R.id.tv_include_patient_id);
        tvIncludeAge = (TextView) view.findViewById(R.id.tv_include_age);
        tvIncludeGender = (TextView) view.findViewById(R.id.tv_include_gender);
        tvIncludeMobileNumber = (TextView) view.findViewById(R.id.tv_include_mobile_number);
        tvIncludeHospitalId = (TextView) view.findViewById(R.id.tv_include_hospital_id);
        tvIncludeRefferedBy = (TextView) view.findViewById(R.id.tv_include_referred_by);
        tvIncludeBloodGroup = (TextView) view.findViewById(R.id.tv_include_blood_group);
        tvIncludeRecordId = (TextView) view.findViewById(R.id.tv_include_record_id);
        tvIncludeDate = (TextView) view.findViewById(R.id.tv_include_date);
        tvIncludeCity = (TextView) view.findViewById(R.id.tv_include_city);

        btEditPageSetup = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_page_setup);
        btEditContentSetup = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_content_setup);
        btEditHeader = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_header_setup);
        btEditFooter = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_footer_setup);
        btEditPatirntDetails = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_patient_details);
    }

    @Override
    public void initListeners() {
        btEditPageSetup.setOnClickListener(this);
        btEditContentSetup.setOnClickListener(this);
        btEditHeader.setOnClickListener(this);
        btEditFooter.setOnClickListener(this);
        btEditPatirntDetails.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit_page_setup:
                break;
            case R.id.bt_edit_content_setup:
                break;
            case R.id.bt_edit_header_setup:
                break;
            case R.id.bt_edit_footer_setup:
                break;
            case R.id.bt_edit_patient_details:
                break;
        }

    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        return null;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }


}
