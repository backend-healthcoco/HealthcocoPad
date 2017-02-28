package com.healthcoco.healthcocoplus.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.enums.SettingsItemType;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.viewholders.SettingsItemViewHolder;

import java.util.List;

/**
 * Created by neha on 15/01/16.
 */
public class SettingsListAdapter extends BaseAdapter {
    private List<SettingsItemType> list;
    private HealthCocoActivity mActivity;
    private SettingsItemViewHolder holder;

    public SettingsListAdapter() {
    }

    public SettingsListAdapter(HealthCocoActivity activity) {
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

    public void setListData(List<SettingsItemType> list) {
        this.list = list;
    }

}
