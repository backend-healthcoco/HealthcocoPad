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

    private boolean isFromAddNewPrescriptionScreen;
    private TemplateListItemListener templateListItemListener;
    private HealthCocoActivity mActivity;
    private TempTemplate template;
    private LinearLayout containerDoses;
    private TextView tvTemplateName;
    private LinearLayout btEdit;
    private LinearLayout btDelete;
    private LinearLayout containerTemplate;

    public TemplatesListViewHolder(HealthCocoActivity mActivity, TemplateListItemListener templateListItemListener, boolean isFromAddNewPrescriptionScreen) {
        super(mActivity);
        this.mActivity = mActivity;
        this.templateListItemListener = templateListItemListener;
        this.isFromAddNewPrescriptionScreen = isFromAddNewPrescriptionScreen;
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
            containerDoses.addView(view);
        }
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
        containerDoses = (LinearLayout) view.findViewById(R.id.container_drug_names);
        containerTemplate = (LinearLayout) view.findViewById(R.id.container_template);
    }

    private void initListeners() {
        containerTemplate.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (!Util.isNullOrEmptyList(template.getItems()))
            LocalDataServiceImpl.getInstance(mApp).addTemplate(template);
        template.save();
        templateListItemListener.onItemClicked(template);
    }
}
