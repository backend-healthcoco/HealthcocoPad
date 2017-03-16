package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SettingsUIPermissionItemViewHolder;

import java.util.List;

/**
 * Created by Shreshtha on 28-02-2017.
 */
public class UIPermissionItemGridAdapter extends BaseAdapter {
    private HealthCocoActivity mActivity;
    private SettingsUIPermissionItemViewHolder holder;
    private List<String> list;

    public UIPermissionItemGridAdapter() {
    }

    public UIPermissionItemGridAdapter(HealthCocoActivity activity) {
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
            holder = new SettingsUIPermissionItemViewHolder(mActivity);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (SettingsUIPermissionItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData(list.get(position));
        return convertView;
    }

    public void setUIListData(List<String> list) {
        this.list = list;
    }
}
