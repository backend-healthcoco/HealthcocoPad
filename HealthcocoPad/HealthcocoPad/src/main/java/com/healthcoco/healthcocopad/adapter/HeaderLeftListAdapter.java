package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.LeftText;
import com.healthcoco.healthcocopad.bean.server.RightText;
import com.healthcoco.healthcocopad.bean.server.Style;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.HeaderItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.HeaderLeftItemViewHolder;

import java.util.List;

/**
 * Created by neha on 12/03/16.
 */
public class HeaderLeftListAdapter extends BaseAdapter {
    private List<LeftText> list;
    private HealthCocoActivity mActivity;
    private HeaderLeftItemViewHolder holder;

    public HeaderLeftListAdapter() {
    }

    public HeaderLeftListAdapter(HealthCocoActivity activity) {
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
            holder = new HeaderLeftItemViewHolder(mActivity);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (HeaderLeftItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<LeftText> list) {
        this.list = list;
    }

}
