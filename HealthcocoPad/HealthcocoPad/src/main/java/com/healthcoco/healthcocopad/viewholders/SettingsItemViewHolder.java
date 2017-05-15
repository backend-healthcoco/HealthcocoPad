package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.enums.SettingsItemType;
import com.healthcoco.healthcocopad.enums.UIPermissionsItemType;

/**
 * Created by neha on 15/01/16.
 */
public class SettingsItemViewHolder extends HealthCocoViewHolder {
    private HealthCocoActivity mActivity;
    private SettingsItemType itemType;
    private UIPermissionsItemType permissionsItemType;
    private TextView tvName;
    private TextView tvVersionName;

    public SettingsItemViewHolder(HealthCocoActivity mActivity) {
        super(mActivity);
        this.mActivity = mActivity;
    }

    @Override
    public void setData(Object object) {
        if (object instanceof SettingsItemType) {
            this.itemType = (SettingsItemType) object;
        } else if (object instanceof UIPermissionsItemType) {
            this.permissionsItemType = (UIPermissionsItemType) object;
        }
    }

    @Override
    public void applyData() {
        if (itemType instanceof SettingsItemType) {
            tvName.setText(itemType.getTitleId());
            switch (itemType) {
                case ABOUT:
                    tvVersionName.setVisibility(View.VISIBLE);
                    tvVersionName.setText(mActivity.getFormattedVersionName());
                    break;
                default:
                    tvVersionName.setVisibility(View.GONE);
                    break;
            }
        } else if (permissionsItemType instanceof UIPermissionsItemType) {
            tvName.setText(permissionsItemType.getTitleId());
        }
    }

    @Override
    public View getContentView() {
        LinearLayout view = (LinearLayout) inflater.inflate(R.layout.item_settings, null);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvVersionName = (TextView) view.findViewById(R.id.tv_version_name);
        return view;
    }
}
