package com.healthcoco.healthcocopad.viewholders;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoApplication;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.LoginResponse;
import com.healthcoco.healthcocopad.bean.server.User;
import com.healthcoco.healthcocopad.enums.FragmentType;
import com.healthcoco.healthcocopad.enums.LocalTabelType;
import com.healthcoco.healthcocopad.services.impl.LocalDataServiceImpl;

public class MenuListViewHolder {
    private HealthCocoApplication mApp;
    private TextView tvItemName;
    private HealthCocoActivity mActivity;
    private ImageView ivMenuIcon;
    private TextView tvNotifNo;
    private View convertView;
    private LinearLayout container;

    public MenuListViewHolder(HealthCocoActivity activity) {
        this.mActivity = activity;
        this.mApp = (HealthCocoApplication) mActivity.getApplication();
    }

    public void applyData(FragmentType fragmentType) {
        ivMenuIcon.setBackgroundResource(fragmentType.getDrawableId());
        tvItemName.setText(fragmentType.getTitleId());
        tempMethod(fragmentType);
    }

    private void tempMethod(FragmentType fragmentType) {
        String notifNo = "";
        switch (fragmentType) {
            case CONTACTS:
                LoginResponse doctor = LocalDataServiceImpl.getInstance(mApp).getDoctor();
                if (doctor != null) {
                    User user = doctor.getUser();
                    notifNo = LocalDataServiceImpl.getInstance(mApp).getListCount(user) + "";
                }
                break;
            default:
                notifNo = "";
                break;
        }
        tvNotifNo.setText(notifNo);
    }

    public View getContentView(FragmentType fragmentType) {
        convertView = null;
//        switch (fragmentType.getMenuType()) {
//            case SEPARATOR:
        convertView = mActivity.getLayoutInflater().inflate(R.layout.item_menu_separator, null);
//                break;
//            case SUB_ITEM:
//                convertView = mActivity.getLayoutInflater().inflate(R.layout.item_menu_sub_item, null);
//                break;
//            default:
//                break;
//        }
        tvItemName = (TextView) convertView.findViewById(R.id.tv_menu_item_name);
        ivMenuIcon = (ImageView) convertView.findViewById(R.id.iv_menu_icon);
        tvNotifNo = (TextView) convertView.findViewById(R.id.tv_notif_no);
        container = (LinearLayout) convertView.findViewById(R.id.container);
        return convertView;
    }

    public void setSelected(boolean isSelected) {
        if (convertView != null)
            container.setSelected(isSelected);
    }
}
