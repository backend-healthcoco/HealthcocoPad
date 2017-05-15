package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.viewholders.AddVisitSuggestionsListViewHolder;

import java.util.List;

/**
 * Created by neha on 15/04/17.
 */

public class AddVisitSuggestionsListAdapter extends BaseAdapter {
    private List<Object> list;
    private HealthCocoActivity mActivity;
    private AddVisitSuggestionsListViewHolder holder;

    public AddVisitSuggestionsListAdapter(HealthCocoActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new AddVisitSuggestionsListViewHolder(mActivity);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (AddVisitSuggestionsListViewHolder) convertView.getTag();
        holder.setData(list.get(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<Object> list) {
        this.list = list;
    }
}
