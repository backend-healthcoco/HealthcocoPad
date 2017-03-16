package com.healthcoco.healthcocopad.viewholders;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.utilities.Util;

/**
 * Created by Shreshtha on 28-02-2017.
 */
public class SettingsUIPermissionItemViewHolder implements CompoundButton.OnCheckedChangeListener {
    private final HealthCocoApplication mApp;
    private Object list;
    private HealthCocoActivity mActivity;
    private AppCompatCheckBox chUIPermission;

    public SettingsUIPermissionItemViewHolder(HealthCocoActivity mActivity) {
        this.mActivity = mActivity;
        this.mApp = (HealthCocoApplication) mActivity.getApplication();
    }

    public void setData(Object object) {
        this.list = object;
    }

    public void applyData(String position) {
        chUIPermission.setText(Util.getValidatedValueWithoutQuotes(position));
    }

    public View getContentView() {
        LinearLayout view = (LinearLayout) mActivity.getLayoutInflater().inflate(R.layout.item_ui_permisssion, null);
        chUIPermission = (AppCompatCheckBox) view.findViewById(R.id.ch_ui_permission);
        chUIPermission.setChecked(true);
        chUIPermission.setOnCheckedChangeListener(this);
        return view;
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (!isChecked) {
            CharSequence text = chUIPermission.getText();
            System.out.println("chUIPermission" + text);
//            UserPermissionsResponse userPermissionsResponse=new UserPermissionsResponse();
//            UIPermissions uiPermissions=new UIPermissions();
//            userPermissionsResponse.setUiPermissions(uiPermissions.);
        }
    }
}
