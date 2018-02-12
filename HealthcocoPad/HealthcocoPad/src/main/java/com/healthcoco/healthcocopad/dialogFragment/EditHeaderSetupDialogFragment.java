package com.healthcoco.healthcocopad.dialogFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.adapter.HeaderLeftListAdapter;
import com.healthcoco.healthcocopad.adapter.HeaderListAdapter;
import com.healthcoco.healthcocopad.adapter.TreatmentListAdapter;
import com.healthcoco.healthcocopad.bean.server.FooterSetup;
import com.healthcoco.healthcocopad.bean.server.HeaderSetup;
import com.healthcoco.healthcocopad.bean.server.LeftText;
import com.healthcoco.healthcocopad.bean.server.PrintSettings;
import com.healthcoco.healthcocopad.bean.server.RightText;
import com.healthcoco.healthcocopad.bean.server.Style;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.listeners.AddPrintSettingsListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant on 07-02-2018.
 */
public class EditHeaderSetupDialogFragment extends HealthCocoDialogFragment
        implements View.OnClickListener {

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
    private ListView lvTopLeftText;
    private ListView lvTopRightText;
    private String yesTag = "yes";
    private String noTag = "no";
    private TextView tvTitle;
    private ImageButton btCross;
    private Button btSave;
    private HeaderLeftListAdapter leftAdapter;
    private HeaderListAdapter rightAdapter;
    private AddPrintSettingsListener addPrintSettingsListener;

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

        rightAdapter = new HeaderListAdapter(mActivity);
        lvTopRightText.setAdapter(rightAdapter);

    }


    @Override
    public void initViews() {
        radioIncludeHeader = (RadioGroup) view.findViewById(R.id.rg_include_header);
        radioIncludeLogo = (RadioGroup) view.findViewById(R.id.rg_include_logo);

        tvTopLeftText = (TextView) view.findViewById(R.id.tv_top_left_text);
        tvTopRightText = (TextView) view.findViewById(R.id.tv_top_right_text);
        lvTopLeftText = (ListView) view.findViewById(R.id.lv_top_left_text);
        lvTopRightText = (ListView) view.findViewById(R.id.lv_top_right_text);

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

            leftAdapter.setListData(headerSetup.getTopLeftText());
            leftAdapter.notifyDataSetChanged();

            rightAdapter.setListData(headerSetup.getTopRightText());
            rightAdapter.notifyDataSetChanged();
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
        }
    }

    private void validateData() {


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


}