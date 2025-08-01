package com.healthcoco.healthcocopad.viewholders;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Response;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.VolleyResponseBean;
import com.healthcoco.healthcocopad.bean.server.AppointmentRequest;
import com.healthcoco.healthcocopad.bean.server.RegisteredPatientDetailsUpdated;
import com.healthcoco.healthcocopad.bean.server.RightText;
import com.healthcoco.healthcocopad.bean.server.Style;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.bean.server.Treatments;
import com.healthcoco.healthcocopad.bean.server.UnitValue;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.enums.AddUpdateNameDialogType;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.enums.RoleType;
import com.healthcoco.healthcocopad.enums.WebServiceType;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.listeners.TreatmentListItemClickListeners;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;
import com.healthcoco.healthcocopad.services.GsonRequest;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.services.impl.WebDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.DateTimeUtil;
import com.healthcoco.healthcocopad.utilities.HealthCocoConstants;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Shreshtha on 25-03-2017.
 */
public class HeaderItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener {

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

    private TextView tvTextStyleBold;
    private TextView tvTextStyleIttalic;
    private CustomAutoCompleteTextView autotvFontSize;
    private EditText etBottomText;


    private RightText style;

    public HeaderItemViewHolder(HealthCocoActivity mActivity) {
        super(mActivity);

    }

    @Override
    public void setData(Object object) {
        this.style = (RightText) object;
    }

    @Override
    public void applyData() {
        if (style != null) {
            if (!Util.isNullOrBlank(style.getText())) {
                etBottomText.setText(style.getText());
                if (!Util.isNullOrBlank(style.getFontSize()))
                    autotvFontSize.setText(style.getFontSize());
                if (style.getFontStyle() != null) {
                    for (String s : style.getFontStyle()) {
                        if (s.equalsIgnoreCase(mActivity.getString(R.string.bold)))
                            tvTextStyleBold.setSelected(true);
                        if (s.equalsIgnoreCase(mActivity.getString(R.string.italic)))
                            tvTextStyleIttalic.setSelected(true);
                    }
                }
            }

        }

    }


    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.layout_item_header_setup, null);
        initViews(contentView);
        initListeners();
        return contentView;
    }

    private void initViews(View view) {
        autotvFontSize = (CustomAutoCompleteTextView) view.findViewById(R.id.et_font_size);
        etBottomText = (EditText) view.findViewById(R.id.et_bottom_text);
        tvTextStyleBold = (TextView) view.findViewById(R.id.tv_text_style_bold);
        tvTextStyleIttalic = (TextView) view.findViewById(R.id.tv_text_style_ittalic);


        initAutoTvAdapter(autotvFontSize, AutoCompleteTextViewType.BLOOD_GROUP, fontSizeArrayList);
    }

    private void initListeners() {
        tvTextStyleBold.setOnClickListener(this);
        tvTextStyleIttalic.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.tv_text_style_bold) {
            tvTextStyleBold.setSelected(!tvTextStyleBold.isSelected());
        } else if (id == R.id.tv_text_style_ittalic) {
            tvTextStyleIttalic.setSelected(!tvTextStyleIttalic.isSelected());
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

                        }
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
