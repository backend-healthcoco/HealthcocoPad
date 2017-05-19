package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.UserGroups;
import com.healthcoco.healthcocopad.listeners.PatientRegistrationListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.GroupGridViewHolder;

import java.util.List;

/**
 * Created by neha on 23/11/15.
 */
public class GroupsGridViewAdapter extends BaseAdapter {
    private PatientRegistrationListener assignGroupListener;
    private List<UserGroups> list;
    private HealthCocoActivity mActivity;
    private GroupGridViewHolder holder;

    public GroupsGridViewAdapter() {
    }

    public GroupsGridViewAdapter(HealthCocoActivity activity, PatientRegistrationListener assignGroupListener) {
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
            holder = new GroupGridViewHolder(mActivity, assignGroupListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (GroupGridViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<UserGroups> list) {
        this.list = list;
    }

}
