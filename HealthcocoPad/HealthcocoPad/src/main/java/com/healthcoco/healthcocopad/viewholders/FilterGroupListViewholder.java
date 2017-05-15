package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.listeners.OnFilterItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by neha on 06/01/16.
 */
public class FilterGroupListViewholder extends HealthCocoViewHolder implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private OnFilterItemClickListener itemClickListener;
    private HealthCocoActivity mActivity;
    private UserGroups objData;
    private TextView tvGroupName;
    private View contentView;

    public FilterGroupListViewholder(HealthCocoActivity mActivity, OnFilterItemClickListener itemClickListener) {
        super(mActivity);
        this.mActivity = mActivity;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void setData(Object object) {
        this.objData = (UserGroups) object;
    }

    @Override
    public void applyData() {
        tvGroupName.setText(Util.getValidatedValue(objData.getName()));
    }

    @Override
    public View getContentView() {
        contentView = inflater.inflate(R.layout.list_item_filter_groups, null);
        tvGroupName = (TextView) contentView.findViewById(R.id.tv_group_name);
        contentView.setOnClickListener(this);
        return contentView;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        objData.setForeignIsAssignedAnyPatient(isChecked);
    }

    @Override
    public void onClick(View v) {
        itemClickListener.onGroupItemClicked(objData);
    }

    public void setSelected(boolean isSelected) {
        if (isSelected)
            tvGroupName.setTextColor(mActivity.getResources().getColor(R.color.blue_action_bar));
        else
            tvGroupName.setTextColor(mActivity.getResources().getColor(R.color.black));
    }
}
