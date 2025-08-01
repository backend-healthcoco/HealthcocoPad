package com.healthcoco.healthcocopad.dialogFragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.HealthCocoDialogFragment;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.Duration;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.request.AddDrugRequest;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.bean.server.DrugDirection;
import com.healthcoco.healthcocopad.bean.server.DrugDosage;
import com.healthcoco.healthcocopad.bean.server.DrugDurationUnit;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.DrugType;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.PageSetup;
import com.healthcoco.healthcocopad.bean.server.PrintSettings;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.custom.LocalDataBackgroundtaskOptimised;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.CommonListDialogType;
import com.healthcoco.healthcocopad.enums.LocalBackgroundTaskType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.AddNewDrugListener;
import com.healthcoco.healthcocopad.listeners.AddPrintSettingsListener;
import com.healthcoco.healthcocopad.listeners.LocalDoInBackgroundListenerOptimised;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.ReflectionUtil;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by Prashant on 07-02-2018.
 */
public class EditPageSetupDialogFragment extends HealthCocoDialogFragment
        implements View.OnClickListener {

    public ArrayList<Object> pageSizeArrayList = new ArrayList<Object>() {{
        add("A4");
        add("A5");
    }};
    PageSetup pageSetup = new PageSetup();
    PrintSettings printSettings;
    private CustomAutoCompleteTextView etPageSize;
    private EditText etBottomMargin;
    private EditText etTopMargin;
    private EditText etLeftMargin;
    private EditText etRightMargin;
    private TextView tvTitle;
    private ImageButton btCross;
    private Button btSave;
    private AddPrintSettingsListener addPrintSettingsListener;

    public EditPageSetupDialogFragment(AddPrintSettingsListener addPrintSettingsListener) {
        this.addPrintSettingsListener = addPrintSettingsListener;
        printSettings = (PrintSettings) addPrintSettingsListener.getPrintSettings();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.dialog_edit_page_setup, container, false);
        super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle arg0) {
        super.onActivityCreated(arg0);
        init();
        setWidthHeight(0.75, 0.50);
    }

    @Override
    public void init() {
        initViews();
        initListeners();
        initData();
    }


    @Override
    public void initViews() {
        etPageSize = (CustomAutoCompleteTextView) view.findViewById(R.id.et_page_size);
        etBottomMargin = (EditText) view.findViewById(R.id.et_bottom_margin);
        etTopMargin = (EditText) view.findViewById(R.id.et_top_margin);
        etLeftMargin = (EditText) view.findViewById(R.id.et_left_margin);
        etRightMargin = (EditText) view.findViewById(R.id.et_right_margin);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        btCross = (ImageButton) view.findViewById(R.id.bt_cross);
        btSave = (Button) view.findViewById(R.id.bt_save);

        tvTitle.setText(R.string.page_setup);

        initAutoTvAdapter(etPageSize, AutoCompleteTextViewType.BLOOD_GROUP, pageSizeArrayList);

    }

    @Override
    public void initListeners() {
        btSave.setOnClickListener(this);
        btCross.setOnClickListener(this);
    }


    @Override
    public void initData() {
        if (printSettings.getPageSetup() != null) {
            pageSetup = printSettings.getPageSetup();

            if (!Util.isNullOrBlank(pageSetup.getPageSize()))
                etPageSize.setText(pageSetup.getPageSize());

            if (!Util.isNullOrZeroNumber(pageSetup.getBottomMargin()))
                etBottomMargin.setText(Util.getValidatedValue(pageSetup.getBottomMargin()));

            if (!Util.isNullOrZeroNumber(pageSetup.getTopMargin()))
                etTopMargin.setText(Util.getValidatedValue(pageSetup.getTopMargin()));

            if (!Util.isNullOrZeroNumber(pageSetup.getLeftMargin()))
                etLeftMargin.setText(Util.getValidatedValue(pageSetup.getLeftMargin()));

            if (!Util.isNullOrZeroNumber(pageSetup.getRightMargin()))
                etRightMargin.setText(Util.getValidatedValue(pageSetup.getRightMargin()));
        } else
            pageSetup = new PageSetup();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.bt_cross) {
            dismiss();
        } else if (id == R.id.bt_save) {
            validateData();
        }
    }

    private void validateData() {
        String msg = null;
        String pageSize = String.valueOf(etPageSize.getText());
        String bottomMargin = String.valueOf(etBottomMargin.getText());
        String topMargin = String.valueOf(etTopMargin.getText());
        String leftMargin = String.valueOf(etLeftMargin.getText());
        String rightMargin = String.valueOf(etRightMargin.getText());

//        if (Util.isNullOrBlank(pageSize))
//            msg = getResources().getString(R.string.please_enter_drug_name);
        if (Util.isNullOrBlank(topMargin))
            msg = getResources().getString(R.string.please_enter_top_margin);
        if (Util.isNullOrBlank(bottomMargin))
            msg = getResources().getString(R.string.please_enter_bottom_margin);
        if (Util.isNullOrBlank(leftMargin))
            msg = getResources().getString(R.string.please_enter_left_margin);
        if (Util.isNullOrBlank(rightMargin))
            msg = getResources().getString(R.string.please_enter_right_margin);
        if (Util.isNullOrBlank(msg)) {

            if (pageSetup == null)
                pageSetup = new PageSetup();
            if (!pageSize.equals(getString(R.string.select)))
                pageSetup.setPageSize(pageSize);
            pageSetup.setBottomMargin(Integer.parseInt(bottomMargin));
            pageSetup.setLeftMargin(Integer.parseInt(leftMargin));
            pageSetup.setTopMargin(Integer.parseInt(topMargin));
            pageSetup.setRightMargin(Integer.parseInt(rightMargin));

            printSettings.setPageSetup(pageSetup);

            addPrintSettingsListener.onSaveClicked(printSettings);
            dismiss();

        } else {
            Util.showToast(mActivity, msg);
        }
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
                            case PAGE_SIZE:
                                break;
                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}