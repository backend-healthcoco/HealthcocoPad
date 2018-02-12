package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.LeftText;
import com.healthcoco.healthcocopad.bean.server.Style;
import com.healthcoco.healthcocopad.custom.AutoCompleteTextViewAdapter;
import com.healthcoco.healthcocopad.enums.AutoCompleteTextViewType;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.views.CustomAutoCompleteTextView;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 25-03-2017.
 */
public class HeaderLeftItemViewHolder extends HealthCocoViewHolder implements View.OnClickListener {

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


    private LeftText style;

    public HeaderLeftItemViewHolder(HealthCocoActivity mActivity) {
        super(mActivity);

    }

    @Override
    public void setData(Object object) {
        this.style = (LeftText) object;
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
        switch (v.getId()) {
            case R.id.tv_text_style_bold:
                tvTextStyleBold.setSelected(!tvTextStyleBold.isSelected());
                break;
            case R.id.tv_text_style_ittalic:
                tvTextStyleIttalic.setSelected(!tvTextStyleIttalic.isSelected());
                break;
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
