package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.TempTemplate;
import com.healthcoco.healthcocopad.listeners.TemplateListItemListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.TemplatesListViewHolder;

import java.util.ArrayList;

public class TemplatesListAdapter extends BaseAdapter {
    private boolean isFromAddNewPrescriptionScreen;
    private TemplateListItemListener templateListItemListener;
    private ArrayList<TempTemplate> list;
    private HealthCocoActivity mActivity;
    private TemplatesListViewHolder holder;

    public TemplatesListAdapter() {
    }

    public TemplatesListAdapter(HealthCocoActivity activity, TemplateListItemListener templateListItemListener) {
        this.mActivity = activity;
        this.templateListItemListener = templateListItemListener;
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
            holder = new TemplatesListViewHolder(mActivity, templateListItemListener, isFromAddNewPrescriptionScreen);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (TemplatesListViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(ArrayList<TempTemplate> list) {
        this.list = list;
    }
}
