package com.healthcoco.healthcocopad.dialogFragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.HeaderLeftListAdapter;
import com.healthcoco.healthcocopad.adapter.HeaderListAdapter;
import com.healthcoco.healthcocopad.bean.FileDetails;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.HeaderSetup;
import com.healthcoco.healthcocopad.bean.server.LeftText;
import com.healthcoco.healthcocopad.bean.server.PrintSettings;
import com.healthcoco.healthcocopad.bean.server.RightText;
import com.healthcoco.healthcocopad.custom.ExpandableHeightListView;
import com.healthcoco.healthcocopad.enums.DialogType;
import com.healthcoco.healthcocopad.enums.OptionsType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddPrintSettingsListener;
import com.healthcoco.healthcocopad.listeners.CommonOptionsDialogItemClickListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.BitmapUtil;
import com.healthcoco.healthcocopad.utilities.DownloadImageFromUrlUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.ImageUtil;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant on 07-02-2018.
 */
public class EditHeaderSetupDialogFragment extends HealthCocoDialogFragment
        implements View.OnClickListener, CommonOptionsDialogItemClickListener, GsonRequest.ErrorListener, Response.Listener<VolleyResponseBean> {

    public ArrayList<Object> fontSizeArrayList = new ArrayList<Object>() {{
        add("5pt");
        add("6pt");
        add("7pt");
        add("8pt");
        add("9pt");
        add("10pt");
        add("11pt");
        add("12pt");
        add("13pt");
        add("14pt");
        add("15pt");
    }};
    HeaderSetup headerSetup;
    PrintSettings printSettings;
    private RadioGroup radioIncludeHeader;
    private RadioGroup radioIncludeLogo;
    private TextView tvTopLeftText;
    private TextView tvTopRightText;
    private ExpandableHeightListView lvTopLeftText;
    private ExpandableHeightListView lvTopRightText;
    private String yesTag = "yes";
    private String noTag = "no";
    private TextView tvTitle;
    private ImageButton btCross;
    private Button btSave;
    private HeaderLeftListAdapter leftAdapter;
    private HeaderListAdapter rightAdapter;
    private AddPrintSettingsListener addPrintSettingsListener;
    private RadioGroup radioIncludeHeaderImage;
    private EditText etHeaderHeight;
    private LinearLayout btSelectHeaderImage;
    private ImageView ivHeaderImage;
    private Uri cameraImageUri;
    private String imageString;

    public EditHeaderSetupDialogFragment(AddPrintSettingsListener addPrintSettingsListener) {
        this.addPrintSettingsListener = addPrintSettingsListener;
        printSettings = (PrintSettings) addPrintSettingsListener.getPrintSettings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit_header_setup, container, false);
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
        initAdapter();
        initData();
    }

    private void initAdapter() {
        leftAdapter = new HeaderLeftListAdapter(mActivity);
        lvTopLeftText.setAdapter(leftAdapter);
        lvTopLeftText.setExpanded(true);
        rightAdapter = new HeaderListAdapter(mActivity);
        lvTopRightText.setAdapter(rightAdapter);
        lvTopRightText.setExpanded(true);
    }


    @Override
    public void initViews() {
        radioIncludeHeader = (RadioGroup) view.findViewById(R.id.rg_include_header);
        radioIncludeLogo = (RadioGroup) view.findViewById(R.id.rg_include_logo);
        radioIncludeHeaderImage = (RadioGroup) view.findViewById(R.id.rg_include_header_image);
        etHeaderHeight = (EditText) view.findViewById(R.id.et_header_height);
        btSelectHeaderImage = (LinearLayout) view.findViewById(R.id.bt_select_header_image);
        ivHeaderImage = (ImageView) view.findViewById(R.id.iv_header_image);
        ivHeaderImage.setVisibility(View.GONE);
        tvTopLeftText = (TextView) view.findViewById(R.id.tv_top_left_text);
        tvTopRightText = (TextView) view.findViewById(R.id.tv_top_right_text);
        lvTopLeftText = (ExpandableHeightListView) view.findViewById(R.id.lv_top_left_text);
        lvTopRightText = (ExpandableHeightListView) view.findViewById(R.id.lv_top_right_text);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btCross = (ImageButton) view.findViewById(R.id.bt_cross);
        btSave = (Button) view.findViewById(R.id.bt_save);
        tvTopLeftText.setSelected(true);

        tvTitle.setText(R.string.header);
    }

    @Override
    public void initListeners() {
        btSave.setOnClickListener(this);
        btCross.setOnClickListener(this);
        tvTopLeftText.setOnClickListener(this);
        tvTopRightText.setOnClickListener(this);
        btSelectHeaderImage.setOnClickListener(this);
        ivHeaderImage.setOnClickListener(this);
    }


    @Override
    public void initData() {
        if (printSettings.getHeaderSetup() != null) {
            headerSetup = printSettings.getHeaderSetup();

            if (headerSetup.getCustomHeader()) {
                RadioButton radioButton = (RadioButton) radioIncludeHeader.findViewWithTag(yesTag.toUpperCase());
                if (radioButton != null)
                    radioButton.setChecked(true);
            } else {
                RadioButton radioButton = (RadioButton) radioIncludeHeader.findViewWithTag(noTag.toUpperCase());
                if (radioButton != null)
                    radioButton.setChecked(true);
            }

            if (headerSetup.getCustomLogo()) {
                RadioButton radioButton1 = (RadioButton) radioIncludeLogo.findViewWithTag(yesTag.toUpperCase());
                if (radioButton1 != null)
                    radioButton1.setChecked(true);
            } else {
                RadioButton radioButton1 = (RadioButton) radioIncludeLogo.findViewWithTag(noTag.toUpperCase());
                if (radioButton1 != null)
                    radioButton1.setChecked(true);
            }
            if (headerSetup.getShowHeaderImage()) {
                RadioButton radioButton1 = (RadioButton) radioIncludeHeaderImage.findViewWithTag(yesTag.toUpperCase());
                if (radioButton1 != null)
                    radioButton1.setChecked(true);
            } else {
                RadioButton radioButton1 = (RadioButton) radioIncludeHeaderImage.findViewWithTag(noTag.toUpperCase());
                if (radioButton1 != null)
                    radioButton1.setChecked(true);
            }
            if (headerSetup.getHeaderHeight() != null)
                etHeaderHeight.setText(Util.getFormattedDoubleNumber(headerSetup.getHeaderHeight()));

            if (!Util.isNullOrBlank(headerSetup.getHeaderImageUrl())) {
                DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivHeaderImage, headerSetup.getHeaderImageUrl());
                ivHeaderImage.setVisibility(View.VISIBLE);
            }

            if (Util.isNullOrEmptyList(headerSetup.getTopLeftText()))
                addDefaultTopLeftText();
            if (Util.isNullOrEmptyList(headerSetup.getTopRightText()))
                addDefaultTopRightText();


            leftAdapter.setListData(headerSetup.getTopLeftText());
            leftAdapter.notifyDataSetChanged();

            rightAdapter.setListData(headerSetup.getTopRightText());
            rightAdapter.notifyDataSetChanged();
        } else {
            headerSetup = new HeaderSetup();

            addDefaultTopLeftText();
            addDefaultTopRightText();

            leftAdapter.setListData(headerSetup.getTopLeftText());
            leftAdapter.notifyDataSetChanged();

            rightAdapter.setListData(headerSetup.getTopRightText());
            rightAdapter.notifyDataSetChanged();
        }
    }

    private void addDefaultTopRightText() {
        List<RightText> topRightText = new ArrayList<>();
        topRightText.add(new RightText());
        topRightText.add(new RightText());
        topRightText.add(new RightText());
        topRightText.add(new RightText());
        headerSetup.setTopRightText(topRightText);

    }

    private void addDefaultTopLeftText() {
        List<LeftText> topLeftText = new ArrayList<>();
        topLeftText.add(new LeftText());
        topLeftText.add(new LeftText());
        topLeftText.add(new LeftText());
        topLeftText.add(new LeftText());
        headerSetup.setTopLeftText(topLeftText);

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
            case R.id.tv_top_left_text:
                tvTopLeftText.setSelected(true);
                tvTopRightText.setSelected(false);
                lvTopLeftText.setVisibility(View.VISIBLE);
                lvTopRightText.setVisibility(View.GONE);
                break;
            case R.id.tv_top_right_text:
                tvTopRightText.setSelected(true);
                tvTopLeftText.setSelected(false);
                lvTopLeftText.setVisibility(View.GONE);
                lvTopRightText.setVisibility(View.VISIBLE);
                break;
            case R.id.iv_header_image:
                if (!Util.isNullOrBlank(imageString))
                    mActivity.openEnlargedImageDialogFragment(imageString);
                else if (!Util.isNullOrBlank(headerSetup.getHeaderImageUrl()))
                    mActivity.openEnlargedImageDialogFragment(headerSetup.getHeaderImageUrl());
                break;
            case R.id.bt_select_header_image:
                openDialogFragment(DialogType.SELECT_IMAGE, this);
                break;
        }
    }

    private void validateData() {
        if (headerSetup == null)
            headerSetup = new HeaderSetup();
        View checkedRadioButton = view.findViewById(radioIncludeHeader.getCheckedRadioButtonId());
        if (checkedRadioButton != null) {
            String showheader = (String.valueOf(checkedRadioButton.getTag())).toUpperCase();
            if (showheader.equalsIgnoreCase(yesTag))
                headerSetup.setCustomHeader(true);
            else
                headerSetup.setCustomHeader(false);
        } else
            headerSetup.setCustomHeader(false);

        View checkedRadioButton1 = view.findViewById(radioIncludeLogo.getCheckedRadioButtonId());
        if (checkedRadioButton1 != null) {
            String showlogo = (String.valueOf(checkedRadioButton1.getTag())).toUpperCase();
            if (showlogo.equalsIgnoreCase(yesTag))
                headerSetup.setCustomLogo(true);
            else
                headerSetup.setCustomLogo(false);
        } else
            headerSetup.setCustomLogo(false);

        View checkedRadioButton2 = view.findViewById(radioIncludeHeaderImage.getCheckedRadioButtonId());
        if (checkedRadioButton2 != null) {
            String showlogo = (String.valueOf(checkedRadioButton2.getTag())).toUpperCase();
            if (showlogo.equalsIgnoreCase(yesTag))
                headerSetup.setShowHeaderImage(true);
            else
                headerSetup.setShowHeaderImage(false);
        } else
            headerSetup.setShowHeaderImage(false);
        if (!Util.isNullOrBlank(etHeaderHeight.getText().toString()))
            headerSetup.setHeaderHeight(Double.parseDouble(etHeaderHeight.getText().toString()));
        headerSetup.setHeaderImageUrl(imageString);

        if (!Util.isNullOrZeroNumber(lvTopLeftText.getChildCount()))
            headerSetup.setTopLeftText(getTopLeftText());
        if (!Util.isNullOrZeroNumber(lvTopRightText.getChildCount()))
            headerSetup.setTopRightText(getTopRightText());

        printSettings.setHeaderSetup(headerSetup);
        addPrintSettingsListener.onSaveClicked(printSettings);
        dismiss();

    }

    private List<LeftText> getTopLeftText() {
        List<LeftText> leftTextList = new ArrayList<>();

        TextView tvTextStyleBold;
        TextView tvTextStyleIttalic;
        CustomAutoCompleteTextView autotvFontSize;
        EditText etBottomText;
        View childView;

        int listLength = lvTopLeftText.getChildCount();
        for (int i = 0; i < listLength; i++) {
            childView = lvTopLeftText.getChildAt(i);
            autotvFontSize = (CustomAutoCompleteTextView) childView.findViewById(R.id.et_font_size);
            etBottomText = (EditText) childView.findViewById(R.id.et_bottom_text);
            tvTextStyleBold = (TextView) childView.findViewById(R.id.tv_text_style_bold);
            tvTextStyleIttalic = (TextView) childView.findViewById(R.id.tv_text_style_ittalic);

            LeftText leftText = new LeftText();
            String[] fontStyle;

            String fontSize = String.valueOf(autotvFontSize.getText());
            String bottomText = String.valueOf(etBottomText.getText());

            leftText.setText(bottomText);
            if (tvTextStyleIttalic.isSelected()) {
                if (tvTextStyleBold.isSelected())
                    fontStyle = new String[]{"BOLD", "ITALIC"};
                else
                    fontStyle = new String[]{"ITALIC"};
                leftText.setFontStyle(fontStyle);
            } else if (tvTextStyleBold.isSelected()) {
                fontStyle = new String[]{"BOLD"};
                leftText.setFontStyle(fontStyle);
            } else
                leftText.setFontStyle(new String[]{});


            if (!fontSize.equals(getString(R.string.font_value)))
                leftText.setFontSize(fontSize);

            leftTextList.add(leftText);
        }

        return leftTextList;
    }


    private List<RightText> getTopRightText() {
        List<RightText> rightTextList = new ArrayList<>();

        TextView tvTextStyleBold;
        TextView tvTextStyleIttalic;
        CustomAutoCompleteTextView autotvFontSize;
        EditText etBottomText;
        View childView;

        int listLength = lvTopRightText.getChildCount();
        for (int i = 0; i < listLength; i++) {
            childView = lvTopRightText.getChildAt(i);
            autotvFontSize = (CustomAutoCompleteTextView) childView.findViewById(R.id.et_font_size);
            etBottomText = (EditText) childView.findViewById(R.id.et_bottom_text);
            tvTextStyleBold = (TextView) childView.findViewById(R.id.tv_text_style_bold);
            tvTextStyleIttalic = (TextView) childView.findViewById(R.id.tv_text_style_ittalic);

            RightText rightText = new RightText();
            String[] fontStyle;

            String fontSize = String.valueOf(autotvFontSize.getText());
            String bottomText = String.valueOf(etBottomText.getText());

            rightText.setText(bottomText);
            if (tvTextStyleIttalic.isSelected()) {
                if (tvTextStyleBold.isSelected())
                    fontStyle = new String[]{"BOLD", "ITALIC"};
                else
                    fontStyle = new String[]{"ITALIC"};
                rightText.setFontStyle(fontStyle);
            } else if (tvTextStyleBold.isSelected()) {
                fontStyle = new String[]{"BOLD"};
                rightText.setFontStyle(fontStyle);
            } else
                rightText.setFontStyle(new String[]{});

            if (!fontSize.equals(getString(R.string.font_value)))
                rightText.setFontSize(fontSize);

            rightTextList.add(rightText);
        }

        return rightTextList;
    }

    @Override
    public void onOptionsItemSelected(Object object) {
        OptionsType optionsType = (OptionsType) object;
        if (optionsType != null) {
            LogUtils.LOGD(TAG, getResources().getString(optionsType.getStringId()));
            switch (optionsType) {
                case CAMERA:
                    cameraImageUri = mActivity.openCamera(this, "reportImage");
                    break;
                case GALLERY:
                    mActivity.openGallery(this);
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            try {
                if (requestCode == HealthCocoConstants.REQUEST_CODE_CAMERA && cameraImageUri != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), cameraImageUri);
                    if (bitmap != null) {
                        showImage(cameraImageUri.getPath(), bitmap);
                    }
                } else if (requestCode == HealthCocoConstants.REQUEST_CODE_GALLERY && data.getData() != null) {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver(), data.getData());
                    if (bitmap != null) {
                        cameraImageUri = data.getData();
                        showImage(cameraImageUri.getPath(), bitmap);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void showImage(String filePath, Bitmap originalBitmap) {
        originalBitmap = ImageUtil.getRotatedBitmapIfRequiredFromPath(filePath, originalBitmap);
        //croping bitmap to show on image as per its dimensions
        Bitmap bitmap = BitmapUtil.scaleCenterCrop(originalBitmap, Util.getValidatedWidth(ivHeaderImage.getWidth()), Util.getValidatedHeight(ivHeaderImage.getHeight()));
        if (bitmap != null) ivHeaderImage.setImageBitmap(bitmap);
        //passing original bitmap to server
        FileDetails fileDetails = getFileDetails(ImageUtil.DEFAULT_PRINT_SETTING_IMAGE_NAME, originalBitmap);
        mActivity.showLoading(false);
        WebDataServiceImpl.getInstance(mApp).addPrintSettingImageToServer(String.class, fileDetails, this, this);
    }

    private FileDetails getFileDetails(String fileName, Bitmap bitmap) {
        FileDetails fileDetails = new FileDetails();
        fileDetails.setFileEncoded(ImageUtil.encodeTobase64(bitmap));
        fileDetails.setFileExtension(ImageUtil.DEFAULT_IMAGE_EXTENSION);
        fileDetails.setFileName(fileName);
        fileDetails.setBitmap(bitmap);
        return fileDetails;
    }

    @Override
    public void onErrorResponse(VolleyResponseBean volleyResponseBean, String errorMessage) {
        LogUtils.LOGD(TAG, "ADD_RECORD Fail");
        mActivity.hideLoading();
    }

    @Override
    public void onNetworkUnavailable(WebServiceType webServiceType) {
        Util.showToast(mActivity, R.string.user_offline);
        mActivity.hideLoading();
    }

    @Override
    public void onResponse(VolleyResponseBean response) {
        LogUtils.LOGD(TAG, "ADD_RECORD Success");
        switch (response.getWebServiceType()) {
            case UPDATE_PRINT_SETTING_FILE:
                if (response.isValidData(response)) {
                    imageString = (String) response.getData();
                    if (!Util.isNullOrBlank(imageString)) {
                        ivHeaderImage.setVisibility(View.VISIBLE);
                        DownloadImageFromUrlUtil.loadImageUsingImageLoader(null, ivHeaderImage, imageString);
                    }
                }
                break;
        }
        mActivity.hideLoading();
    }

}