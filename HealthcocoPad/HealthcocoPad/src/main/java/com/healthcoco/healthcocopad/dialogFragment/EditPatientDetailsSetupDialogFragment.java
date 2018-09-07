package com.healthcoco.healthcocopad.dialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.PatientDetails;
import com.healthcoco.healthcocopad.bean.server.PrintSettings;
import com.healthcoco.healthcocopad.bean.server.Style;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.listeners.AddPrintSettingsListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;

import java.util.ArrayList;

/**
 * Created by Prashant on 07-02-2018.
 */
public class EditPatientDetailsSetupDialogFragment extends HealthCocoDialogFragment
        implements View.OnClickListener {

    PrintSettings printSettings;
    PatientDetails patientDetails = new PatientDetails();
    private TextView tvIncludePatientName;
    private TextView tvIncludePatientId;
    private TextView tvIncludeRecordId;
    private RadioGroup radioPatientMobile;
    private RadioGroup radioAge;
    private RadioGroup radioGender;
    private RadioGroup radioDate;
    private RadioGroup radioHospitalId;
    private RadioGroup radioRefferedBy;
    private RadioGroup radioBloodGroup;
    private RadioGroup radioCity;
    private String yesTag = "yes";
    private String noTag = "no";
    private TextView tvTitle;
    private ImageButton btCross;
    private Button btSave;
    private AddPrintSettingsListener addPrintSettingsListener;

    public EditPatientDetailsSetupDialogFragment(AddPrintSettingsListener addPrintSettingsListener) {
        this.addPrintSettingsListener = addPrintSettingsListener;
        printSettings = (PrintSettings) addPrintSettingsListener.getPrintSettings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit_patient_details_setup, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        init();
        setWidthHeight(0.75, 0.65);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initData();
    }


    @Override
    public void initViews() {
        tvIncludePatientId = (TextView) view.findViewById(R.id.tv_include_patient_id);
        tvIncludePatientName = (TextView) view.findViewById(R.id.tv_include_patient_name);
        tvIncludeRecordId = (TextView) view.findViewById(R.id.tv_include_record_id);

        radioPatientMobile = (RadioGroup) view.findViewById(R.id.rg_include_mobile_number);
        radioAge = (RadioGroup) view.findViewById(R.id.rg_include_age);
        radioGender = (RadioGroup) view.findViewById(R.id.rg_include_gender);
        radioDate = (RadioGroup) view.findViewById(R.id.rg_include_date);
        radioHospitalId = (RadioGroup) view.findViewById(R.id.rg_include_hospital_id);
        radioRefferedBy = (RadioGroup) view.findViewById(R.id.rg_include_referred_by);
        radioBloodGroup = (RadioGroup) view.findViewById(R.id.rg_include_blood_group);
        radioCity = (RadioGroup) view.findViewById(R.id.rg_include_city);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btCross = (ImageButton) view.findViewById(R.id.bt_cross);
        btSave = (Button) view.findViewById(R.id.bt_save);

        tvTitle.setText(R.string.patient_details);

    }

    @Override
    public void initListeners() {
        btSave.setOnClickListener(this);
        btCross.setOnClickListener(this);
    }


    @Override
    public void initData() {
        if (printSettings.getHeaderSetup() != null)
            if (printSettings.getHeaderSetup().getPatientDetails() != null) {
                patientDetails = printSettings.getHeaderSetup().getPatientDetails();

                if (patientDetails.getShowMobileNumber()) {
                    RadioButton radioButton = (RadioButton) radioPatientMobile.findViewWithTag(yesTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                } else {
                    RadioButton radioButton = (RadioButton) radioPatientMobile.findViewWithTag(noTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                }

                if (patientDetails.getShowDOB()) {
                    RadioButton radioButton = (RadioButton) radioAge.findViewWithTag(yesTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                } else {
                    RadioButton radioButton = (RadioButton) radioAge.findViewWithTag(noTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                }

                if (patientDetails.getShowGender()) {
                    RadioButton radioButton = (RadioButton) radioGender.findViewWithTag(yesTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                } else {
                    RadioButton radioButton = (RadioButton) radioGender.findViewWithTag(noTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                }

                if (patientDetails.getShowDate()) {
                    RadioButton radioButton = (RadioButton) radioDate.findViewWithTag(yesTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                } else {
                    RadioButton radioButton = (RadioButton) radioDate.findViewWithTag(noTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                }

                if (patientDetails.getShowHospitalId()) {
                    RadioButton radioButton = (RadioButton) radioHospitalId.findViewWithTag(yesTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                } else {
                    RadioButton radioButton = (RadioButton) radioHospitalId.findViewWithTag(noTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                }

                if (patientDetails.getShowReferedBy()) {
                    RadioButton radioButton = (RadioButton) radioRefferedBy.findViewWithTag(yesTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                } else {
                    RadioButton radioButton = (RadioButton) radioRefferedBy.findViewWithTag(noTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                }

                if (patientDetails.getShowBloodGroup()) {
                    RadioButton radioButton = (RadioButton) radioBloodGroup.findViewWithTag(yesTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                } else {
                    RadioButton radioButton = (RadioButton) radioBloodGroup.findViewWithTag(noTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                }

                if (patientDetails.getShowCity()) {
                    RadioButton radioButton = (RadioButton) radioCity.findViewWithTag(yesTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                } else {
                    RadioButton radioButton = (RadioButton) radioCity.findViewWithTag(noTag.toUpperCase());
                    if (radioButton != null)
                        radioButton.setChecked(true);
                }
            }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_cross:
                dismiss();
                break;
            case R.id.bt_save:
                validateData();
                break;
        }
    }

    private void validateData() {
        if (patientDetails == null)
            patientDetails = new PatientDetails();
        patientDetails.setShowName(true);
        patientDetails.setShowPID(true);
        patientDetails.setShowResourceId(true);

        View checkedRadioButton = view.findViewById(radioPatientMobile.getCheckedRadioButtonId());
        if (checkedRadioButton != null) {
            String mobile = (String.valueOf(checkedRadioButton.getTag())).toUpperCase();
            if (mobile.equalsIgnoreCase(yesTag))
                patientDetails.setShowMobileNumber(true);
            else
                patientDetails.setShowMobileNumber(false);
        } else
            patientDetails.setShowMobileNumber(false);

        View checkedRadioButton1 = view.findViewById(radioAge.getCheckedRadioButtonId());
        if (checkedRadioButton1 != null) {
            String mobile = (String.valueOf(checkedRadioButton1.getTag())).toUpperCase();
            if (mobile.equalsIgnoreCase(yesTag))
                patientDetails.setShowDOB(true);
            else
                patientDetails.setShowDOB(false);
        } else
            patientDetails.setShowDOB(false);

        View checkedRadioButton2 = view.findViewById(radioGender.getCheckedRadioButtonId());
        if (checkedRadioButton2 != null) {
            String mobile = (String.valueOf(checkedRadioButton2.getTag())).toUpperCase();
            if (mobile.equalsIgnoreCase(yesTag))
                patientDetails.setShowGender(true);
            else
                patientDetails.setShowGender(false);
        } else
            patientDetails.setShowGender(false);

        View checkedRadioButton3 = view.findViewById(radioDate.getCheckedRadioButtonId());
        if (checkedRadioButton3 != null) {
            String mobile = (String.valueOf(checkedRadioButton3.getTag())).toUpperCase();
            if (mobile.equalsIgnoreCase(yesTag))
                patientDetails.setShowDate(true);
            else
                patientDetails.setShowDate(false);
        } else
            patientDetails.setShowDate(false);

        View checkedRadioButton4 = view.findViewById(radioHospitalId.getCheckedRadioButtonId());
        if (checkedRadioButton4 != null) {
            String mobile = (String.valueOf(checkedRadioButton4.getTag())).toUpperCase();
            if (mobile.equalsIgnoreCase(yesTag))
                patientDetails.setShowHospitalId(true);
            else
                patientDetails.setShowHospitalId(false);
        } else
            patientDetails.setShowHospitalId(false);

        View checkedRadioButton5 = view.findViewById(radioRefferedBy.getCheckedRadioButtonId());
        if (checkedRadioButton5 != null) {
            String mobile = (String.valueOf(checkedRadioButton5.getTag())).toUpperCase();
            if (mobile.equalsIgnoreCase(yesTag))
                patientDetails.setShowReferedBy(true);
            else
                patientDetails.setShowReferedBy(false);
        } else
            patientDetails.setShowReferedBy(false);

        View checkedRadioButton6 = view.findViewById(radioBloodGroup.getCheckedRadioButtonId());
        if (checkedRadioButton6 != null) {
            String mobile = (String.valueOf(checkedRadioButton6.getTag())).toUpperCase();
            if (mobile.equalsIgnoreCase(yesTag))
                patientDetails.setShowBloodGroup(true);
            else
                patientDetails.setShowBloodGroup(false);
        } else
            patientDetails.setShowBloodGroup(false);

        View checkedRadioButton7 = view.findViewById(radioCity.getCheckedRadioButtonId());
        if (checkedRadioButton7 != null) {
            String mobile = (String.valueOf(checkedRadioButton7.getTag())).toUpperCase();
            if (mobile.equalsIgnoreCase(yesTag))
                patientDetails.setShowCity(true);
            else
                patientDetails.setShowCity(false);
        } else
            patientDetails.setShowCity(false);


        printSettings.getHeaderSetup().setPatientDetails(patientDetails);
        addPrintSettingsListener.onSaveClicked(printSettings);
        dismiss();

    }


    private void initAutoTvAdapter(AutoCompleteTextView autoCompleteTextView, final AutoCompleteTextViewType autoCompleteTextViewType, ArrayList<Object> list) {
        try {
            if (!Util.isNullOrEmptyList(list)) {
                final AutoCompleteTextViewAdapter adapter = new AutoCompleteTextViewAdapter(mActivity, R.layout.spinner_drop_down_item_grey_background,
                        list, autoCompleteTextViewType);
                autoCompleteTextView.setThreshold(1);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setDropDownAnchor(autoCompleteTextView.getId());
                autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        switch (autoCompleteTextViewType) {

                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}