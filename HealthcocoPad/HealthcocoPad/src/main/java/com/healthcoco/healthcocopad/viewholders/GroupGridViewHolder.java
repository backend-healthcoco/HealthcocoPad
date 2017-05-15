package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationListener;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class GroupGridViewHolder extends HealthCocoViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private PatientRegistrationListener assignGroupListener;
    private UserGroups objData;
    private CheckBox cbGrouped;

    public GroupGridViewHolder(HealthCocoActivity mActivity, PatientRegistrationListener assignGroupListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.assignGroupListener = assignGroupListener;
    }


    @Override
    public void setData(Object object) {
        this.objData = (UserGroups) object;
    }

    @Override
    public void applyData() {
        cbGrouped.setText(Util.getValidatedValue(objData.getName()));
        cbGrouped.setChecked(assignGroupListener.isGroupAssigned(objData.getUniqueId()));
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.grid_item_groups, null);
        cbGrouped = (CheckBox) contentView.findViewById(R.id.ch_ui_permission);
        cbGrouped.setOnCheckedChangeListener(this);
        contentView.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        objData.setForeignIsAssignedAnyPatient(isChecked);
        assignGroupListener.onAssignGroupCheckClicked(isChecked, objData);
    }

    @Override
    public void onClick(View v) {
        cbGrouped.setChecked(!cbGrouped.isChecked());
    }
}