package com.healthcoco.healthcocoplus.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.bean.server.UserGroups;
import com.healthcoco.healthcocoplus.listeners.AssignGroupListener;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.viewholders.GroupListViewHolder;

import java.util.List;

/**
 * Created by neha on 23/11/15.
 */
public class GroupsListViewAdapter extends BaseAdapter {
    private AssignGroupListener assignGroupListener;
    private List<UserGroups> list;
    private HealthCocoActivity mActivity;
    private GroupListViewHolder holder;

    public GroupsListViewAdapter() {
    }

    public GroupsListViewAdapter(HealthCocoActivity activity, AssignGroupListener assignGroupListener) {
        this.mActivity = activity;
        this.assignGroupListener = assignGroupListener;
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
            holder = new GroupListViewHolder(mActivity,assignGroupListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (GroupListViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<UserGroups> list) {
        this.list = list;
    }
}
