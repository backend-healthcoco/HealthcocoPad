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
import com.healthcoco.healthcocopad.bean.server.ContentSetup;
import com.healthcoco.healthcocopad.bean.server.PageSetup;
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
public class EditContentSetupDialogFragment extends HealthCocoDialogFragment
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
    public ArrayList<Object> lineSpaceArrayList = new ArrayList<Object>() {{
        add("SMALL");
        add("MEDIUM");
        add("LARGE");
    }};
    public ArrayList<Object> instructionAlignArrayList = new ArrayList<Object>() {{
        add("VERTICAL");
        add("HORIZONTAL");
    }};
    ContentSetup contentSetup = new ContentSetup();
    PrintSettings printSettings;
    private CustomAutoCompleteTextView autotvFontSize;
    private CustomAutoCompleteTextView autotvLineSpace;
    private CustomAutoCompleteTextView autotvInstructionAlign;
    private RadioGroup radioShowDrugGenericName;
    private RadioGroup radioShowTreatmentCost;
    private String yesTag = "yes";
    private String noTag = "no";
    private TextView tvTitle;
    private ImageButton btCross;
    private Button btSave;
    private AddPrintSettingsListener addPrintSettingsListener;

    public EditContentSetupDialogFragment(AddPrintSettingsListener addPrintSettingsListener) {
        this.addPrintSettingsListener = addPrintSettingsListener;
        printSettings = (PrintSettings) addPrintSettingsListener.getPrintSettings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit_content_setup, container, false);
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
        autotvLineSpace = (CustomAutoCompleteTextView) view.findViewById(R.id.et_line_space);
        autotvInstructionAlign = (CustomAutoCompleteTextView) view.findViewById(R.id.et_instruction_align);
        radioShowDrugGenericName = (RadioGroup) view.findViewById(R.id.rg_drug_generic_name);
        radioShowTreatmentCost = (RadioGroup) view.findViewById(R.id.rg_treatment_cost);

        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btCross = (ImageButton) view.findViewById(R.id.bt_cross);
        btSave = (Button) view.findViewById(R.id.bt_save);

        tvTitle.setText(R.string.content_setup);

        initAutoTvAdapter(autotvFontSize, AutoCompleteTextViewType.BLOOD_GROUP, fontSizeArrayList);
        initAutoTvAdapter(autotvLineSpace, AutoCompleteTextViewType.BLOOD_GROUP, lineSpaceArrayList);
        initAutoTvAdapter(autotvInstructionAlign, AutoCompleteTextViewType.BLOOD_GROUP, instructionAlignArrayList);

    }

    @Override
    public void initListeners() {
        btSave.setOnClickListener(this);
        btCross.setOnClickListener(this);
    }


    @Override
    public void initData() {
        if (printSettings.getContentSetup() != null) {
            contentSetup = printSettings.getContentSetup();

            if (!Util.isNullOrBlank(contentSetup.getFontSize()))
                autotvFontSize.setText(contentSetup.getFontSize());

            if (!Util.isNullOrBlank(contentSetup.getInstructionAlign()))
                autotvInstructionAlign.setText(contentSetup.getInstructionAlign());

            if (!Util.isNullOrBlank(printSettings.getContentLineSpace()))
                autotvLineSpace.setText(printSettings.getContentLineSpace());

            if (contentSetup.getShowTreatmentcost()) {
                RadioButton radioButton = (RadioButton) radioShowTreatmentCost.findViewWithTag(yesTag.toUpperCase());
                if (radioButton != null)
                    radioButton.setChecked(true);
            } else {
                RadioButton radioButton = (RadioButton) radioShowTreatmentCost.findViewWithTag(noTag.toUpperCase());
                if (radioButton != null)
                    radioButton.setChecked(true);
            }

            if (printSettings.getShowDrugGenericNames()) {
                RadioButton radioButton1 = (RadioButton) radioShowDrugGenericName.findViewWithTag(yesTag.toUpperCase());
                if (radioButton1 != null)
                    radioButton1.setChecked(true);
            } else {
                RadioButton radioButton1 = (RadioButton) radioShowDrugGenericName.findViewWithTag(noTag.toUpperCase());
                if (radioButton1 != null)
                    radioButton1.setChecked(true);
            }

        } else {
            contentSetup = new ContentSetup();
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
        String fontSize = String.valueOf(autotvFontSize.getText());
        String lineSpace = String.valueOf(autotvLineSpace.getText());
        String instructionAlign = String.valueOf(autotvInstructionAlign.getText());

        if (!fontSize.equals(getString(R.string.font_value)))
            contentSetup.setFontSize(fontSize);

        contentSetup.setInstructionAlign(instructionAlign);
        printSettings.setContentLineSpace(lineSpace);


        View checkedRadioButton = view.findViewById(radioShowDrugGenericName.getCheckedRadioButtonId());
        if (checkedRadioButton != null) {
            String showDrugName = (String.valueOf(checkedRadioButton.getTag())).toUpperCase();
            if (showDrugName.equalsIgnoreCase(yesTag))
                printSettings.setShowDrugGenericNames(true);
            else
                printSettings.setShowDrugGenericNames(false);
        } else
            printSettings.setShowDrugGenericNames(false);

        View checkedRadioButton1 = view.findViewById(radioShowTreatmentCost.getCheckedRadioButtonId());
        if (checkedRadioButton1 != null) {
            String showTreatmentCost = (String.valueOf(checkedRadioButton1.getTag())).toUpperCase();
            if (showTreatmentCost.equalsIgnoreCase(yesTag))
                contentSetup.setShowTreatmentcost(true);
            else
                contentSetup.setShowTreatmentcost(false);
        } else
            contentSetup.setShowTreatmentcost(false);

        printSettings.setContentSetup(contentSetup);
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