package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.enums.UIPermissionsItemType;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SettingsItemViewHolder;

import java.util.List;

/**
 * Created by Shreshtha on 27-02-2017.
 */
public class SettingsUIPermissionListAdapter extends SettingsListAdapter {
    private HealthCocoActivity mActivity;
    private SettingsItemViewHolder holder;
    private List<UIPermissionsItemType> list;

    public SettingsUIPermissionListAdapter() {
    }

    public SettingsUIPermissionListAdapter(HealthCocoActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        if (!Util.isNullOrEmptyList(list))
            return list.size();
        return 0;
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new SettingsItemViewHolder(mActivity);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (SettingsItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setUIListData(List<UIPermissionsItemType> list) {
        this.list = list;
    }
}
