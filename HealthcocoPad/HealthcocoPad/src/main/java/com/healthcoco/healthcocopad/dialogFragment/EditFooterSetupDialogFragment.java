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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.BottomTextStyle;
import com.healthcoco.healthcocopad.bean.server.FooterSetup;
import com.healthcoco.healthcocopad.bean.server.PrintSettings;
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
public class EditFooterSetupDialogFragment extends HealthCocoDialogFragment
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
    FooterSetup footerSetup;
    PrintSettings printSettings;
    private CustomAutoCompleteTextView autotvFontSize;
    private RadioGroup radioIncludeFooter;
    private RadioGroup radioIncludeSignature;
    private RadioGroup radioIncludeSignatureText;
    private EditText etBottomText;
    private EditText etSignatureText;
    private TextView tvTextStyleBold;
    private TextView tvTextStyleIttalic;
    private String yesTag = "yes";
    private String noTag = "no";
    private TextView tvTitle;
    private ImageButton btCross;
    private Button btSave;
    private AddPrintSettingsListener addPrintSettingsListener;

    public EditFooterSetupDialogFragment(AddPrintSettingsListener addPrintSettingsListener) {
        this.addPrintSettingsListener = addPrintSettingsListener;
        printSettings = (PrintSettings) addPrintSettingsListener.getPrintSettings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit_footer_setup, container, false);
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
        autotvFontSize = (CustomAutoCompleteTextView) view.findViewById(R.id.et_font_size);
        radioIncludeFooter = (RadioGroup) view.findViewById(R.id.rg_include_footer);
        radioIncludeSignature = (RadioGroup) view.findViewById(R.id.rg_include_signature);
        radioIncludeSignatureText = (RadioGroup) view.findViewById(R.id.rg_include_signature_text);
        etBottomText = (EditText) view.findViewById(R.id.et_bottom_text);
        etSignatureText = (EditText) view.findViewById(R.id.et_signature_text);
        tvTextStyleBold = (TextView) view.findViewById(R.id.tv_text_style_bold);
        tvTextStyleIttalic = (TextView) view.findViewById(R.id.tv_text_style_ittalic);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btCross = (ImageButton) view.findViewById(R.id.bt_cross);
        btSave = (Button) view.findViewById(R.id.bt_save);

        tvTitle.setText(R.string.footer);

        initAutoTvAdapter(autotvFontSize, AutoCompleteTextViewType.BLOOD_GROUP, fontSizeArrayList);

    }

    @Override
    public void initListeners() {
        btSave.setOnClickListener(this);
        btCross.setOnClickListener(this);
        tvTextStyleBold.setOnClickListener(this);
        tvTextStyleIttalic.setOnClickListener(this);
    }


    @Override
    public void initData() {
        if (printSettings.getFooterSetup() != null) {
            footerSetup = printSettings.getFooterSetup();

            if (!Util.isNullOrBlank(footerSetup.getBottomSignText()))
                etSignatureText.setText(footerSetup.getBottomSignText());

            if (footerSetup.getShowSignature()) {
                RadioButton radioButton = (RadioButton) radioIncludeSignature.findViewWithTag(yesTag.toUpperCase());
                if (radioButton != null)
                    radioButton.setChecked(true);
            } else {
                RadioButton radioButton = (RadioButton) radioIncludeSignature.findViewWithTag(noTag.toUpperCase());
                if (radioButton != null)
                    radioButton.setChecked(true);
            }

            if (footerSetup.getShowBottomSignText()) {
                RadioButton radioButton1 = (RadioButton) radioIncludeSignatureText.findViewWithTag(yesTag.toUpperCase());
                if (radioButton1 != null)
                    radioButton1.setChecked(true);
            } else {
                RadioButton radioButton1 = (RadioButton) radioIncludeSignatureText.findViewWithTag(noTag.toUpperCase());
                if (radioButton1 != null)
                    radioButton1.setChecked(true);
            }

            if (footerSetup.getCustomFooter()) {
                RadioButton radioButton1 = (RadioButton) radioIncludeFooter.findViewWithTag(yesTag.toUpperCase());
                if (radioButton1 != null)
                    radioButton1.setChecked(true);
            } else {
                RadioButton radioButton1 = (RadioButton) radioIncludeFooter.findViewWithTag(noTag.toUpperCase());
                if (radioButton1 != null)
                    radioButton1.setChecked(true);
            }

            if (!Util.isNullOrEmptyList(footerSetup.getBottomText())) {
                List<BottomTextStyle> bottomTextList = footerSetup.getBottomText();
                for (BottomTextStyle style : bottomTextList) {
                    if (!Util.isNullOrBlank(style.getText())) {
                        etBottomText.setText(style.getText());
                        if (!Util.isNullOrBlank(style.getFontSize()))
                            autotvFontSize.setText(style.getFontSize());
                        if (style.getFontStyle() != null) {
                            for (String s : style.getFontStyle()) {
                                if (s.equalsIgnoreCase(getString(R.string.bold)))
                                    tvTextStyleBold.setSelected(true);
                                if (s.equalsIgnoreCase(getString(R.string.italic)))
                                    tvTextStyleIttalic.setSelected(true);
                            }
                        }
                    }
                }
            }

        } else {
            footerSetup = new FooterSetup();
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
            case R.id.tv_text_style_bold:
                tvTextStyleBold.setSelected(!tvTextStyleBold.isSelected());
                break;
            case R.id.tv_text_style_ittalic:
                tvTextStyleIttalic.setSelected(!tvTextStyleIttalic.isSelected());
                break;
        }
    }

    private void validateData() {
        String fontSize = String.valueOf(autotvFontSize.getText());
        String bottomText = String.valueOf(etBottomText.getText());
        String signatureText = String.valueOf(etSignatureText.getText());


        footerSetup.setBottomSignText(signatureText);

        View checkedRadioButton = view.findViewById(radioIncludeFooter.getCheckedRadioButtonId());
        if (checkedRadioButton != null) {
            String showfooter = (String.valueOf(checkedRadioButton.getTag())).toUpperCase();
            if (showfooter.equalsIgnoreCase(yesTag))
                footerSetup.setCustomFooter(true);
            else
                footerSetup.setCustomFooter(false);
        } else
            footerSetup.setCustomFooter(false);

        View checkedRadioButton1 = view.findViewById(radioIncludeSignatureText.getCheckedRadioButtonId());
        if (checkedRadioButton1 != null) {
            String showSignatureText = (String.valueOf(checkedRadioButton1.getTag())).toUpperCase();
            if (showSignatureText.equalsIgnoreCase(yesTag))
                footerSetup.setShowBottomSignText(true);
            else
                footerSetup.setShowBottomSignText(false);
        } else
            footerSetup.setShowBottomSignText(false);

        View checkedRadioButton2 = view.findViewById(radioIncludeSignature.getCheckedRadioButtonId());
        if (checkedRadioButton2 != null) {
            String showSignature = (String.valueOf(checkedRadioButton2.getTag())).toUpperCase();
            if (showSignature.equalsIgnoreCase(yesTag))
                footerSetup.setShowSignature(true);
            else
                footerSetup.setShowSignature(false);
        } else
            footerSetup.setShowSignature(false);

        if (!Util.isNullOrBlank(bottomText)) {
            List<BottomTextStyle> styles = new ArrayList<>();
            BottomTextStyle bottomTextStyle = new BottomTextStyle();
            String[] fontStyle;

            bottomTextStyle.setText(bottomText);
            if (tvTextStyleIttalic.isSelected()) {
                if (tvTextStyleBold.isSelected())
                    fontStyle = new String[]{"BOLD", "ITALIC"};
                else
                    fontStyle = new String[]{"ITALIC"};
                bottomTextStyle.setFontStyle(fontStyle);
            } else if (tvTextStyleBold.isSelected()) {
                fontStyle = new String[]{"BOLD"};
                bottomTextStyle.setFontStyle(fontStyle);
            } else
                bottomTextStyle.setFontStyle(new String[]{});


            if (!fontSize.equals(getString(R.string.font_value)))
                bottomTextStyle.setFontSize(fontSize);

            styles.add(bottomTextStyle);
            footerSetup.setBottomText(styles);
        }

        printSettings.setFooterSetup(footerSetup);
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