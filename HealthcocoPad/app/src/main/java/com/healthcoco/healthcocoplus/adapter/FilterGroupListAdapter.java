package com.healthcoco.healthcocoplus.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.bean.server.UserGroups;
import com.healthcoco.healthcocoplus.listeners.OnFilterItemClickListener;
import com.healthcoco.healthcocoplus.utilities.LogUtils;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.viewholders.FilterGroupListViewholder;

import java.util.List;

/**
 * Created by neha on 06/01/16.
 */
public class FilterGroupListAdapter extends BaseAdapter {
    private static final String TAG = FilterGroupListAdapter.class.getSimpleName();
    private OnFilterItemClickListener itemClickListener;
    private List<UserGroups> list;
    private HealthCocoActivity mActivity;
    private FilterGroupListViewholder holder;
    private String selectedGroupId = null;

    public FilterGroupListAdapter() {
    }

    public FilterGroupListAdapter(HealthCocoActivity activity, OnFilterItemClickListener itemClickListener) {
        this.mActivity = activity;
        this.itemClickListener = itemClickListener;
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
        UserGroups group = (UserGroups) getItem(position);
        if (convertView == null) {
            holder = new FilterGroupListViewholder(mActivity, itemClickListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (FilterGroupListViewholder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        if (!Util.isNullOrBlank(selectedGroupId) && group.getUniqueId().equals(selectedGroupId)) {
            LogUtils.LOGD(TAG, "Position " + position + " true ");
            holder.setSelected(true);
        } else {
            LogUtils.LOGD(TAG, "Position " + position + " false ");
            holder.setSelected(false);
        }
        return convertView;
    }

    public void setListData(List<UserGroups> list) {
        this.list = list;
    }

    public void setSelectedGroupId(String groupId) {
        this.selectedGroupId = groupId;
    }
}
