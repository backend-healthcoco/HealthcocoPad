package com.healthcoco.healthcocopad.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.activities.CommonOpenUpActivity;
import com.healthcoco.healthcocopad.bean.UserPermissionsResponse;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.BottomTextStyle;
import com.healthcoco.healthcocopad.bean.server.ContentSetup;
import com.healthcoco.healthcocopad.bean.server.FooterSetup;
import com.healthcoco.healthcocopad.bean.server.HeaderSetup;
import com.healthcoco.healthcocopad.bean.server.LeftText;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.PageSetup;
import com.healthcoco.healthcocopad.bean.server.PatientDetails;
import com.healthcoco.healthcocopad.bean.server.PrintSettings;
import com.healthcoco.healthcocopad.bean.server.RightText;
import com.healthcoco.healthcocopad.bean.server.Style;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.dialogFragment.AddEditGeneralNotesDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.EditContentSetupDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.EditFooterSetupDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.EditHeaderSetupDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.EditPageSetupDialogFragment;
import com.healthcoco.healthcocopad.dialogFragment.EditPatientDetailsSetupDialogFragment;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddPrintSettingsListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.TextViewFontAwesome;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Prashant on 05/02/2018.
 */

public class SettingPrintSetupFragment extends HealthCocoFragment implements GsonRequest.ErrorListener,
        LocalDoInBackgroundListenerOptimised, Response.Listener<VolleyResponseBean>, View.OnClickListener, AddPrintSettingsListener {

    public static final String TAG_HEADER_PRINT_SETTING_DATA = "printSetting";
    public static final String TAG_GENERAL_NOTES = "generalNotes";
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
    private LinearLayout layoutTopRightText;
    private LinearLayout layoutTopLeftText;
    private TextView tvIncludelogo;
    private TextView tvIncludeHeader;
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

    private PrintSettings printSettings;

    private User user;
    private boolean isInitialLoading = true;
    private TextView tvGeneralNotesText;
    private TextViewFontAwesome btEditGeneralNotes;
    private TextView tvShowFooterImage;
    private TextView tvShowHeaderImage;
    private TextView tvFooterHeight;
    private TextView tvHeaderHeight;
    private ImageView ivHeaderImage;
    private ImageView ivFooterImage;


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
        layoutTopLeftText = (LinearLayout) view.findViewById(R.id.layout_topleft_text);
        layoutTopRightText = (LinearLayout) view.findViewById(R.id.layout_topright_text);
        tvIncludelogo = (TextView) view.findViewById(R.id.tv_include_logo);
        tvIncludeHeader = (TextView) view.findViewById(R.id.tv_include_header);
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

        tvGeneralNotesText = (TextView) view.findViewById(R.id.tv_general_notes_text);
        tvShowFooterImage = (TextView) view.findViewById(R.id.tv_show_footer_image);
        tvShowHeaderImage = (TextView) view.findViewById(R.id.tv_show_header_image);
        tvFooterHeight = (TextView) view.findViewById(R.id.tv_footer_height);
        tvHeaderHeight = (TextView) view.findViewById(R.id.tv_header_height);
        tvIncludeHeader = (TextView) view.findViewById(R.id.tv_include_header);
        tvIncludeFooter = (TextView) view.findViewById(R.id.tv_include_footer);
        ivHeaderImage = (ImageView) view.findViewById(R.id.iv_header_image);
        ivFooterImage = (ImageView) view.findViewById(R.id.iv_footer_image);
        btEditGeneralNotes = (TextViewFontAwesome) view.findViewById(R.id.bt_edit_general_notes);
        ivHeaderImage.setVisibility(View.GONE);
        ivFooterImage.setVisibility(View.GONE);
    }

    @Override
    public void initListeners() {
        ((CommonOpenUpActivity) mActivity).initActionbarRightAction(this);

        btEditPageSetup.setOnClickListener(this);
        btEditContentSetup.setOnClickListener(this);
        btEditHeader.setOnClickListener(this);
        btEditFooter.setOnClickListener(this);
        btEditPatirntDetails.setOnClickListener(this);
        btEditGeneralNotes.setOnClickListener(this);
        ivHeaderImage.setOnClickListener(this);
        ivFooterImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_edit_page_setup:
                EditPageSetupDialogFragment editPageSetupDialogFragment = new EditPageSetupDialogFragment(this);
                editPageSetupDialogFragment.show(mActivity.getSupportFragmentManager(),
                        editPageSetupDialogFragment.getClass().getSimpleName());
                break;
            case R.id.bt_edit_content_setup:
                EditContentSetupDialogFragment editContentSetupDialogFragment = new EditContentSetupDialogFragment(this);
                editContentSetupDialogFragment.show(mActivity.getSupportFragmentManager(),
                        editContentSetupDialogFragment.getClass().getSimpleName());
                break;

            case R.id.bt_edit_header_setup:
                EditHeaderSetupDialogFragment editHeaderSetupDialogFragment = new EditHeaderSetupDialogFragment(this);
                editHeaderSetupDialogFragment.show(mActivity.getSupportFragmentManager(),
                        editHeaderSetupDialogFragment.getClass().getSimpleName());
                break;

            case R.id.bt_edit_footer_setup:
                EditFooterSetupDialogFragment editFooterSetupDialogFragment = new EditFooterSetupDialogFragment(this);
                editFooterSetupDialogFragment.show(mActivity.getSupportFragmentManager(),
                        editFooterSetupDialogFragment.getClass().getSimpleName());
                break;
            case R.id.bt_edit_patient_details:
                EditPatientDetailsSetupDialogFragment editPatientDetailsSetupDialogFragment = new EditPatientDetailsSetupDialogFragment(this);
                editPatientDetailsSetupDialogFragment.show(mActivity.getSupportFragmentManager(),
                        editPatientDetailsSetupDialogFragment.getClass().getSimpleName());
                break;
            case R.id.bt_edit_general_notes:
                Bundle args = new Bundle();
                args.putString(TAG_GENERAL_NOTES, printSettings.getGeneralNotes());
                AddEditGeneralNotesDialogFragment dialogFragment = new AddEditGeneralNotesDialogFragment();
                dialogFragment.setArguments(args);
                dialogFragment.setTargetFragment(this, HealthCocoConstants.REQUEST_CODE_ADD_EDIT_PRINT_SETTING);
                dialogFragment.show(mActivity.getSupportFragmentManager(), dialogFragment.getClass().getSimpleName());
                break;
            case R.id.container_right_action:
                addPrintSettings();
                break;
            case R.id.iv_header_image:
                if (printSettings != null &&
                        printSettings.getHeaderSetup() != null &&
                        !Util.isNullOrBlank(printSettings.getHeaderSetup().getHeaderImageUrl()))
                    mActivity.openEnlargedImageDialogFragment(printSettings.getHeaderSetup().getHeaderImageUrl());
                break;
            case R.id.iv_footer_image:
                if (printSettings != null &&
                        printSettings.getFooterSetup() != null &&
                        !Util.isNullOrBlank(printSettings.getFooterSetup().getFooterImageUrl()))
                    mActivity.openEnlargedImageDialogFragment(printSettings.getFooterSetup().getFooterImageUrl());
                break;
        }

    }

    private void addPrintSettings() {
        try {
            mActivity.showLoading(false);
            WebDataServiceImpl.getInstance(mApp).addPrintSettings(PrintSettings.class, printSettings, this, this);
        } catch (Exception e) {
            e.printStackTrace();
            mActivity.hideLoading();
        }
    }

    @Override
    public VolleyResponseBean doInBackground(VolleyResponseBean response) {
        VolleyResponseBean volleyResponseBean = new VolleyResponseBean();
        switch (response.getLocalBackgroundTaskType()) {
            case GET_FRAGMENT_INITIALISATION_DATA:
                volleyResponseBean = new VolleyResponseBean();
                volleyResponseBean.setWebServiceType(WebServiceType.FRAGMENT_INITIALISATION);
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null && doctor.getUser() != null && !Util.isNullOrBlank(doctor.getUser().getUniqueId())) {
                    user = doctor.getUser();
                }
                return volleyResponseBean;
            case ADD_PRINT_SETTINGS:
                LocalDataServiceImpl.getInstance(mApp).
                        addPrintSettings((ArrayList) response.getDataList());
            case GET_PRINT_SETTINGS:
                volleyResponseBean = LocalDataServiceImpl.getInstance(mApp).getPrintSettingsResponse(WebServiceType.GET_PRINT_SETTINGS, user.getUniqueId(), null, null);
                break;


        }
        return volleyResponseBean;
    }

    @Override
    public void onPostExecute(VolleyResponseBean aVoid) {

    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        super.onErrorResponse(volleyResponseBean, errorMessage);
        switch (volleyResponseBean.getWebServiceType()) {
            case GET_PRINT_SETTINGS:
//                WebDataServiceImpl.getInstance(mApp).getPrintSettings(PrintSettings.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
                break;
        }
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        if (response != null && response.getWebServiceType() != null) {
            switch (response.getWebServiceType()) {
                case FRAGMENT_INITIALISATION:
                    if (user != null) {
                        getPrintSetupFromLocal();
                        return;
                    }
                    break;
                case GET_PRINT_SETTINGS:
                    if (response.isDataFromLocal()) {
                        if (response.getData() != null) {
                            printSettings = (PrintSettings) response.getData();
                            initData(printSettings);
                        }
                        if (isInitialLoading && response.isUserOnline()) {
                            isInitialLoading = false;
                            WebDataServiceImpl.getInstance(mApp).getPrintSettings(PrintSettings.class, user.getUniqueId(), user.getForeignLocationId(), user.getForeignHospitalId(), this, this);
                            return;
                        }
                    } else {
                        isInitialLoading = false;
                        new LocalDataBackgroundtaskOptimised(mActivity, LocalBackgroundTaskType.ADD_PRINT_SETTINGS, this, this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, response);
                    }
                    break;
                case ADD_PRINT_SETTINGS:
                    LocalDataServiceImpl.getInstance(mApp).
                            addPrintSetting((PrintSettings) response.getData());
                    mActivity.finish();
                    break;
            }
        }
        mActivity.hideLoading();
    }

    private void initData(PrintSettings printSettings) {
        if (printSettings != null) {
            refreshPageSetupDetails(printSettings);
            refreshContentSetupDetails(printSettings);
            refreshHeaderDetails(printSettings);
            refreshFooterDetails(printSettings);
            refreshPatientDetails(printSettings);
            refreshGeneralNotesDetails(printSettings);
        }
    }
    private void refreshGeneralNotesDetails(PrintSettings printSettings) {
        if (!Util.isNullOrBlank(printSettings.getGeneralNotes()))
            tvGeneralNotesText.setText(Util.getValidatedValue(printSettings.getGeneralNotes()));
    }

    private void refreshPatientDetails(PrintSettings printSettings) {
        if (printSettings.getHeaderSetup() != null) {
            if (printSettings.getHeaderSetup().getPatientDetails() != null) {
                PatientDetails patientDetails = printSettings.getHeaderSetup().getPatientDetails();

                if (patientDetails.getShowName())
                    tvIncludePatientName.setText(R.string.yes);
                else
                    tvIncludePatientName.setText(R.string.no);

                if (patientDetails.getShowPID())
                    tvIncludePatientID.setText(R.string.yes);
                else
                    tvIncludePatientID.setText(R.string.no);

                if (patientDetails.getShowDOB())
                    tvIncludeAge.setText(R.string.yes);
                else
                    tvIncludeAge.setText(R.string.no);

                if (patientDetails.getShowGender())
                    tvIncludeGender.setText(R.string.yes);
                else
                    tvIncludeGender.setText(R.string.no);

                if (patientDetails.getShowMobileNumber())
                    tvIncludeMobileNumber.setText(R.string.yes);
                else
                    tvIncludeMobileNumber.setText(R.string.no);

                if (patientDetails.getShowHospitalId())
                    tvIncludeHospitalId.setText(R.string.yes);
                else
                    tvIncludeHospitalId.setText(R.string.no);

                if (patientDetails.getShowReferedBy())
                    tvIncludeRefferedBy.setText(R.string.yes);
                else
                    tvIncludeRefferedBy.setText(R.string.no);

                if (patientDetails.getShowBloodGroup())
                    tvIncludeBloodGroup.setText(R.string.yes);
                else
                    tvIncludeBloodGroup.setText(R.string.no);

                if (patientDetails.getShowResourceId())
                    tvIncludeRecordId.setText(R.string.yes);
                else
                    tvIncludeRecordId.setText(R.string.no);

                if (patientDetails.getShowDate())
                    tvIncludeDate.setText(R.string.yes);
                else
                    tvIncludeDate.setText(R.string.no);

                if (patientDetails.getShowCity())
                    tvIncludeCity.setText(R.string.yes);
                else
                    tvIncludeCity.setText(R.string.no);
            }
        }
    }

    private void refreshFooterDetails(PrintSettings printSettings) {
        if (printSettings.getFooterSetup() != null) {
            FooterSetup footerSetup = printSettings.getFooterSetup();
            if (footerSetup.getCustomFooter())
                tvIncludeFooter.setText(R.string.yes);
            else
                tvIncludeFooter.setText(R.string.no);

            if (footerSetup.getShowBottomSignText())
                tvIncludeSignatureText.setText(R.string.yes);
            else
                tvIncludeSignatureText.setText(R.string.no);

            if (footerSetup.getShowSignature())
                tvIncludeSignature.setText(R.string.yes);
            else
                tvIncludeSignature.setText(R.string.no);

            if (!Util.isNullOrBlank(footerSetup.getBottomSignText()))
                tvBottomSignatureText.setText(footerSetup.getBottomSignText());

            if (footerSetup.getShowImageFooter())
                tvShowFooterImage.setText(R.string.yes);
            else
                tvShowFooterImage.setText(R.string.no);
            if (!Util.isNullOrBlank(footerSetup.getFooterImageUrl())) {
                DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivFooterImage, footerSetup.getFooterImageUrl());
                ivFooterImage.setVisibility(View.VISIBLE);
            }
            tvFooterHeight.setText(Util.getFormattedDoubleNumber(footerSetup.getFooterHeight()));

            if (!Util.isNullOrEmptyList(footerSetup.getBottomText())) {
                List<BottomTextStyle> bottomTextList = footerSetup.getBottomText();
                for (BottomTextStyle style : bottomTextList) {
                    if (!Util.isNullOrBlank(style.getText())) {
                        tvBottomText.setText(style.getText());
                        if (!Util.isNullOrBlank(style.getFontSize())) {
                            int textsize = Integer.parseInt(style.getFontSize().substring(0, style.getFontSize().length() - 2));
                            tvBottomText.setTextSize(TypedValue.COMPLEX_UNIT_PT, textsize);
                        }
                        if (style.getFontStyle() != null) {
                            ArrayList<String> styleList = new ArrayList<String>(Arrays.asList(style.getFontStyle()));

                            if (styleList.contains(getString(R.string.bold))) {
                                if (styleList.contains(getString(R.string.italic)))
                                    tvBottomText.setTypeface(tvBottomText.getTypeface(), Typeface.BOLD_ITALIC);
                                else
                                    tvBottomText.setTypeface(tvBottomText.getTypeface(), Typeface.BOLD);
                            } else if (styleList.contains(getString(R.string.italic)))
                                tvBottomText.setTypeface(tvBottomText.getTypeface(), Typeface.ITALIC);
                            else
                                tvBottomText.setTypeface(tvBottomText.getTypeface(), Typeface.NORMAL);

                        }
                    }
                }
            }


        }

    }

    private void refreshHeaderDetails(PrintSettings printSettings) {

        if (printSettings.getHeaderSetup() != null) {
            HeaderSetup headerSetup = printSettings.getHeaderSetup();
            if (headerSetup.getCustomHeader())
                tvIncludeHeader.setText(R.string.yes);
            else
                tvIncludeHeader.setText(R.string.no);

            if (headerSetup.getCustomLogo())
                tvIncludelogo.setText(R.string.yes);
            else
                tvIncludelogo.setText(R.string.no);

            if (headerSetup.getShowHeaderImage())
                tvShowHeaderImage.setText(R.string.yes);
            else
                tvShowHeaderImage.setText(R.string.no);

            tvHeaderHeight.setText(Util.getFormattedDoubleNumber(headerSetup.getHeaderHeight()));

            if (!Util.isNullOrBlank(headerSetup.getHeaderImageUrl())) {
                DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivHeaderImage, headerSetup.getHeaderImageUrl());
                ivHeaderImage.setVisibility(View.VISIBLE);
            }

            layoutTopRightText.removeAllViews();
            layoutTopLeftText.removeAllViews();
            if (!Util.isNullOrEmptyList(headerSetup.getTopRightText())) {
                List<RightText> rightTextList = headerSetup.getTopRightText();
                for (RightText rightText : rightTextList) {
                    if (!Util.isNullOrBlank(rightText.getText())) {

                        LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.layout_item_top_right_text, null);
                        TextView tvTopText = (TextView) linearLayout.findViewById(R.id.tv_top_left_text);

                        tvTopText.setText(rightText.getText());
                        if (!Util.isNullOrBlank(rightText.getFontSize())) {
                            int textsize = Integer.parseInt(rightText.getFontSize().substring(0, rightText.getFontSize().length() - 2));
                            tvTopText.setTextSize(TypedValue.COMPLEX_UNIT_PT, textsize);
                        }
                        if (rightText.getFontStyle() != null) {
                            ArrayList<String> styleList = new ArrayList<String>(Arrays.asList(rightText.getFontStyle()));

                            if (styleList.contains(getString(R.string.bold))) {
                                if (styleList.contains(getString(R.string.italic)))
                                    tvTopText.setTypeface(tvTopText.getTypeface(), Typeface.BOLD_ITALIC);
                                else
                                    tvTopText.setTypeface(tvTopText.getTypeface(), Typeface.BOLD);
                            } else if (styleList.contains(getString(R.string.italic)))
                                tvTopText.setTypeface(tvTopText.getTypeface(), Typeface.ITALIC);
                            else
                                tvTopText.setTypeface(tvTopText.getTypeface(), Typeface.NORMAL);

                        }

                        layoutTopRightText.addView(linearLayout);
                    }
                }
            }


            if (!Util.isNullOrEmptyList(headerSetup.getTopLeftText())) {
                List<LeftText> leftTextList = headerSetup.getTopLeftText();
                for (LeftText leftText : leftTextList) {
                    if (!Util.isNullOrBlank(leftText.getText())) {

                        LinearLayout linearLayout = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.layout_item_top_right_text, null);
                        TextView tvTopText = (TextView) linearLayout.findViewById(R.id.tv_top_left_text);

                        tvTopText.setText(leftText.getText());
                        if (!Util.isNullOrBlank(leftText.getFontSize())) {
                            int textsize = Integer.parseInt(leftText.getFontSize().substring(0, leftText.getFontSize().length() - 2));
                            tvTopText.setTextSize(TypedValue.COMPLEX_UNIT_PT, textsize);
                        }
                        if (leftText.getFontStyle() != null) {
                            ArrayList<String> styleList = new ArrayList<String>(Arrays.asList(leftText.getFontStyle()));

                            if (styleList.contains(getString(R.string.bold))) {
                                if (styleList.contains(getString(R.string.italic)))
                                    tvTopText.setTypeface(tvTopText.getTypeface(), Typeface.BOLD_ITALIC);
                                else
                                    tvTopText.setTypeface(tvTopText.getTypeface(), Typeface.BOLD);
                            } else if (styleList.contains(getString(R.string.italic)))
                                tvTopText.setTypeface(tvTopText.getTypeface(), Typeface.ITALIC);
                            else
                                tvTopText.setTypeface(tvTopText.getTypeface(), Typeface.NORMAL);

                        }

                        layoutTopLeftText.addView(linearLayout);
                    }
                }
            }

        }
    }

    private void refreshContentSetupDetails(PrintSettings printSettings) {
        if (printSettings.getContentSetup() != null) {
            ContentSetup contentSetup = printSettings.getContentSetup();

            tvFontsize.setText(contentSetup.getFontSize());
            tvLineSpace.setText(printSettings.getContentLineSpace());
            tvShowInstructionAlign.setText(contentSetup.getInstructionAlign());
            if (contentSetup.getShowTreatmentcost())
                tvShowTreatmentCost.setText(R.string.yes);
            else
                tvShowTreatmentCost.setText(R.string.no);

            if (printSettings.getShowDrugGenericNames())
                tvShowDrugGenericNames.setText(R.string.yes);
            else
                tvShowDrugGenericNames.setText(R.string.no);

        }

    }

    private void refreshPageSetupDetails(PrintSettings printSettings) {
        if (printSettings.getPageSetup() != null) {
            PageSetup pageSetup = printSettings.getPageSetup();

            if (!Util.isNullOrBlank(pageSetup.getPageSize()))
                tvPageSize.setText(pageSetup.getPageSize());

            if (!Util.isNullOrZeroNumber(pageSetup.getBottomMargin()))
                tvBottomMargin.setText(pageSetup.getBottomMargin() + getString(R.string.px));

            if (!Util.isNullOrZeroNumber(pageSetup.getTopMargin()))
                tvTopMargin.setText(pageSetup.getTopMargin() + getString(R.string.px));

            if (!Util.isNullOrZeroNumber(pageSetup.getLeftMargin()))
                tvLeftMargin.setText(pageSetup.getLeftMargin() + getString(R.string.px));

            if (!Util.isNullOrZeroNumber(pageSetup.getRightMargin()))
                tvRightMargin.setText(pageSetup.getRightMargin() + getString(R.string.px));
        }
    }


    private void getPrintSetupFromLocal() {
        mActivity.showLoading(false);
        new LocalDataBackgroundtaskOptimised
                (mActivity, LocalBackgroundTaskType.GET_PRINT_SETTINGS, this,
                        this, this).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onSaveClicked(Object object) {
        printSettings = (PrintSettings) object;
        initData(printSettings);
    }

    @Override
    public Object getPrintSettings() {
        return printSettings;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == HealthCocoConstants.REQUEST_CODE_ADD_EDIT_PRINT_SETTING) {
            if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_GENERAL_NOTE) {
                String stringExtra = data.getStringExtra(TAG_GENERAL_NOTES);
                tvGeneralNotesText.setText(stringExtra);
                printSettings.setGeneralNotes(stringExtra);
            } else if (resultCode == HealthCocoConstants.RESULT_CODE_ADD_HEADER_SETUP) {
                PrintSettings printSettings = Parcels.unwrap(data.getParcelableExtra(TAG_HEADER_PRINT_SETTING_DATA));
                this.printSettings = printSettings;
                initData(this.printSettings);
            }
        }
    }
}
