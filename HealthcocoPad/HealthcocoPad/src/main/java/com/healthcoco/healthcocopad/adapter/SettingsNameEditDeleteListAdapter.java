package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.enums.SettingsItemType;
import com.healthcoco.healthcocopad.listeners.NameEditDeleteListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SettingsNameEditDeleteViewHolder;

import java.util.List;

/**
 * Created by neha on 15/01/16.
 */
public class SettingsNameEditDeleteListAdapter extends BaseAdapter {
    private SettingsItemType itemType;
    private List<?> list;
    private HealthCocoActivity mActivity;
    private SettingsNameEditDeleteViewHolder holder;
    private NameEditDeleteListener editDeleteListener;

    public SettingsNameEditDeleteListAdapter() {
    }

    public SettingsNameEditDeleteListAdapter(HealthCocoActivity activity, NameEditDeleteListener editDeleteListener) {
        this.mActivity = activity;
        this.editDeleteListener = editDeleteListener;
    }

    @Override
    public int getCount() {
        if (Util.isNullOrEmptyList(list))
            return 0;
        return list.size();
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
            holder = new SettingsNameEditDeleteViewHolder(mActivity, editDeleteListener, itemType);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (SettingsNameEditDeleteViewHolder) convertView.getTag();
        holder.setData(list.get(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(SettingsItemType itemType, List<?> list) {
        this.list = list;
        this.itemType = itemType;
    }

}
