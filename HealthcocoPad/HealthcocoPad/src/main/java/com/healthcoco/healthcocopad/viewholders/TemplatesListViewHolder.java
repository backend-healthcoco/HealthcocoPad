package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.listeners.TemplateListItemListener;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;
import com.healthcoco.healthcocopad.utilities.Util;

public class TemplatesListViewHolder extends HealthCocoViewHolder implements OnClickListener {

    private TemplateListItemListener templateListItemListener;
    private HealthCocoActivity mActivity;
    private TempTemplate template;
    private LinearLayout containerDoses;
    private TextView tvTemplateName;
    private TextView tvTemplateDefault;
    private LinearLayout btEdit;
    private LinearLayout btDelete;
    private LinearLayout containerTemplate;
    private LinearLayout layoutBottomButtons;
    private Boolean isFromSettingsScreen = false;

    public TemplatesListViewHolder(HealthCocoActivity mActivity, TemplateListItemListener templateListItemListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.templateListItemListener = templateListItemListener;
        this.isFromSettingsScreen = templateListItemListener.isFromSettingsScreen();
    }

    @Override
    public void setData(Object object) {
        this.template = (TempTemplate) object;
    }

    @Override
    public void applyData() {
        tvTemplateName.setText(template.getName());
        containerDoses.removeAllViews();
        for (DrugItem drug : template.getItems()) {
            DrugNameItemViewholder view = new DrugNameItemViewholder(mActivity);
            view.setData(drug);
            if (drug.getDrug() != null)
                containerDoses.addView(view);
        }
        if (template.getDefault() != null) {
            if (template.getDefault())
                tvTemplateDefault.setVisibility(View.VISIBLE);
            else
                tvTemplateDefault.setVisibility(View.GONE);
        } else
            tvTemplateDefault.setVisibility(View.GONE);

    }

    @Override
    public View getContentView() {
        View view = (View) inflater.inflate(R.layout.list_item_template, null);
        initViews(view);
        initListeners();
        return view;
    }

    private void initViews(View view) {
        tvTemplateName = (TextView) view.findViewById(R.id.tv_template_name);
        tvTemplateDefault = (TextView) view.findViewById(R.id.tv_template_default);
        containerDoses = (LinearLayout) view.findViewById(R.id.container_drug_names);
        containerTemplate = (LinearLayout) view.findViewById(R.id.container_template);
        btEdit = (LinearLayout) view.findViewById(R.id.bt_edit);
        btDelete = (LinearLayout) view.findViewById(R.id.bt_delete);
        layoutBottomButtons = (LinearLayout) view.findViewById(R.id.layout_bottom_buttons);
        if (!isFromSettingsScreen)
            layoutBottomButtons.setVisibility(View.GONE);
    }

    private void initListeners() {
        containerTemplate.setOnClickListener(this);
        btDelete.setOnClickListener(this);
        btEdit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.container_template:
                if (!isFromSettingsScreen)
                    templateListItemListener.onItemClicked(template);
                break;
            case R.id.bt_edit:
                templateListItemListener.onEditClicked(template);
                break;
            case R.id.bt_delete:
                templateListItemListener.onDeleteClicked(template);
                break;
            default:
                break;
        }
    }
}
