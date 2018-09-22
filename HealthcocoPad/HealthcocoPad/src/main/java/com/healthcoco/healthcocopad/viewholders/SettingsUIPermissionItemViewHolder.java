package com.healthcoco.healthcocopad.viewholders;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.listeners.CommonUiPermissionsListener;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 28-02-2017.
 */
public class SettingsUIPermissionItemViewHolder extends HealthCocoViewHolder implements CompoundButton.OnCheckedChangeListener {
    private CommonUiPermissionsListener commonUiPermissionsListener;
    private AppCompatCheckBox chUIPermission;
    private String permission;

    public SettingsUIPermissionItemViewHolder(HealthCocoActivity mActivity, CommonUiPermissionsListener commonUiPermissionsListener) {
        super(mActivity);
        this.commonUiPermissionsListener = commonUiPermissionsListener;
    }

    public void setData(Object object) {
        this.permission = (String) object;
    }

    public void applyData() {
        chUIPermission.setText(Util.toTitleCase(Util.getFormattedStringReplaceBySpace(permission)));
        chUIPermission.setChecked(commonUiPermissionsListener.isAssigned(permission));
    }

    public View getContentView() {
        LinearLayout view = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.grid_item_groups, null);
        chUIPermission = (AppCompatCheckBox) view.findViewById(R.id.ch_ui_permission);
        chUIPermission.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked)
            commonUiPermissionsListener.assignPermission(permission);
        else
            commonUiPermissionsListener.removePermission(permission);
    }
}
