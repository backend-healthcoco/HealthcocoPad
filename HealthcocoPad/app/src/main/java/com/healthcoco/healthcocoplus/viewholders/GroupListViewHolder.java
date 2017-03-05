package com.healthcoco.healthcocoplus.viewholders;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.HealthCocoViewHolder;
import com.healthcoco.healthcocoplus.bean.server.UserGroups;
import com.healthcoco.healthcocoplus.listeners.AssignGroupListener;
import com.healthcoco.healthcocoplus.utilities.Util;

/**
 * Created by neha on 23/11/15.
 */
public class GroupListViewHolder extends HealthCocoViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private AssignGroupListener assignGroupListener;
    private HealthCocoActivity mActivity;
    private UserGroups objData;
    private TextView tvGroupName;
    private CheckBox cbGrouped;

    public GroupListViewHolder(HealthCocoActivity mActivity, AssignGroupListener assignGroupListener) {
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
        tvGroupName.setText(Util.getValidatedValue(objData.getName()));
        if (objData.isForeignIsAssignedAnyPatient())
            cbGrouped.setChecked(true);
        else
            cbGrouped.setChecked(false);
    }

    @Override
    public View getContentView() {
        View contentView = inflater.inflate(R.layout.item_ui_permisssion, null);
        tvGroupName = (TextView) contentView.findViewById(R.id.tv_group_name);
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
