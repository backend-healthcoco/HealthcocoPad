package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.Drug;
import com.healthcoco.healthcocopad.listeners.SelectedDrugsListItemListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SelectedDrugItemsListViewholder;
import com.healthcoco.healthcocopad.viewholders.SelectedTemplateDrugItemsListViewholder;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 29-03-2017.
 */
public class SelectedTemplateDrugItemsListAdapter extends BaseAdapter {
    private ArrayList<Drug> list;
    private HealthCocoActivity mActivity;
    private SelectedTemplateDrugItemsListViewholder holder;
    private SelectedDrugsListItemListener templateListener;

    public SelectedTemplateDrugItemsListAdapter() {
    }

    public SelectedTemplateDrugItemsListAdapter(HealthCocoActivity activity, SelectedDrugsListItemListener templateListener) {
        this.mActivity = activity;
        this.templateListener = templateListener;
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
            holder = new SelectedTemplateDrugItemsListViewholder(mActivity, templateListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (SelectedTemplateDrugItemsListViewholder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(ArrayList<Drug> list) {
        this.list = list;
    }
}

