package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.enums.SettingsItemType;
import com.healthcoco.healthcocopad.enums.UIPermissionsItemType;
import com.healthcoco.healthcocopad.fragments.CalendarViewType;

/**
 * Created by neha on 15/01/16.
 */
public class SettingsItemViewHolder extends HealthCocoViewHolder {
    private HealthCocoActivity mActivity;
    private SettingsItemType itemType;
    private UIPermissionsItemType permissionsItemType;
    private CalendarViewType calendarViewType;
    private TextView tvViewType;
    private TextView tvName;
    private TextView tvVersionName;
    private RelativeLayout layoutItemSetting;
    private RelativeLayout layoutViewType;
    private View bottomView;

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
        } else if (object instanceof CalendarViewType) {
            this.calendarViewType = (CalendarViewType) object;
        }
    }

    @Override
    public void applyData() {
        layoutViewType.setVisibility(View.GONE);
        if (itemType instanceof SettingsItemType) {
            tvName.setText(itemType.getTitleId());
            switch (itemType) {
                case ABOUT:
                    tvVersionName.setVisibility(View.VISIBLE);
                    tvVersionName.setText(mActivity.getFormattedVersionName());
                    break;
                case TREATMENT:
                case PRINT:
                case SYNC:
                case HELP_IMPROVE:
                case SIGN_OUT:
                case GROUPS:
                    tvVersionName.setVisibility(View.GONE);
                    bottomView.setVisibility(View.GONE);
                    LayoutParams params = new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                    );
                    params.setMargins(0, 0, 0, 30);
                    layoutItemSetting.setLayoutParams(params);

                    break;
                case CLINICAL_NOTE:
                case HISTORY:
                case PRESCRIPTION:
                case TEMPLATES:
                case UI_PERMISSION:
                    tvVersionName.setVisibility(View.GONE);
                    bottomView.setVisibility(View.VISIBLE);
                    LayoutParams layoutParams = new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.WRAP_CONTENT
                    );
                    layoutParams.setMargins(0, 0, 0, 0);
                    layoutItemSetting.setLayoutParams(layoutParams);

                    break;
                default:
                    tvVersionName.setVisibility(View.GONE);
                    break;
            }
        } else if (permissionsItemType instanceof UIPermissionsItemType) {
            tvName.setText(permissionsItemType.getTitleId());
        } else if (calendarViewType instanceof CalendarViewType) {
            layoutItemSetting.setVisibility(View.GONE);
            layoutViewType.setVisibility(View.VISIBLE);
            tvViewType.setText(calendarViewType.getTitleId());
        }
    }

    @Override
    public View getContentView() {
        RelativeLayout view = (RelativeLayout) inflater.inflate(R.layout.item_settings, null);
        tvName = (TextView) view.findViewById(R.id.tv_name);
        tvViewType = (TextView) view.findViewById(R.id.tv_view_type);
        tvVersionName = (TextView) view.findViewById(R.id.tv_version_name);
        bottomView = view.findViewById(R.id.view_bottom);
        layoutItemSetting = (RelativeLayout) view.findViewById(R.id.layout_item_setting);
        layoutViewType = (RelativeLayout) view.findViewById(R.id.layout_item_view_type);


        return view;
    }
}
