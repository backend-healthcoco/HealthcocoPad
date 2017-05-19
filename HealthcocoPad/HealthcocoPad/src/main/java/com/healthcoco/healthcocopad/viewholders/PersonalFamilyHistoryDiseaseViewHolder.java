package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.Disease;
import com.healthcoco.healthcocopad.listeners.MedicalFamilyHistoryItemListener;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 11/12/15.
 */
public class PersonalFamilyHistoryDiseaseViewHolder extends HealthCocoViewHolder implements
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private MedicalFamilyHistoryItemListener itemListener;
    private Disease disease;
    private CheckBox cbCheckBox;

    public PersonalFamilyHistoryDiseaseViewHolder(HealthCocoActivity mActivity, MedicalFamilyHistoryItemListener itemListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.itemListener = itemListener;
    }

    @Override
    public void setData(Object object) {
        this.disease = (Disease) object;
    }

    @Override
    public void applyData() {
        boolean isDiseaseAdded = itemListener.isDiseaseAdded(disease.getUniqueId());
        if (isDiseaseAdded) {
            cbCheckBox.setChecked(true);
        } else {
            cbCheckBox.setChecked(false);
        }
        cbCheckBox.setText(Util.getValidatedValue(disease.getDisease()));
    }

    @Override
    public View getContentView() {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.grid_item_groups, null);
        cbCheckBox = (CheckBox) view.findViewById(R.id.ch_ui_permission);
        cbCheckBox.setOnCheckedChangeListener(this);
        view.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) itemListener.onAddDiseaseClicked(disease.getUniqueId());
        else
            itemListener.onRemoveDiseaseClicked(disease.getUniqueId());
        disease.setIsAssigned(isChecked);
    }

    @Override
    public void onClick(View v) {
        cbCheckBox.setChecked(!cbCheckBox.isChecked());
    }
}
