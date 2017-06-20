package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.ReportsListItemViewHolder;

import java.util.List;

/**
 * Created by neha on 19/03/16.
 */
public class ReportsListAdapter extends BaseAdapter {
    private CommonEMRItemClickListener listItemClickListener;
    private List<Records> list;
    private HealthCocoActivity mActivity;
    private ReportsListItemViewHolder holder;

    public ReportsListAdapter() {
    }

    public ReportsListAdapter(HealthCocoActivity activity, CommonEMRItemClickListener listItemClickListener) {
        this.mActivity = activity;
        this.listItemClickListener = listItemClickListener;
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
            holder = new ReportsListItemViewHolder(mActivity, listItemClickListener, true);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (ReportsListItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<Records> list) {
        this.list = list;
    }

}
