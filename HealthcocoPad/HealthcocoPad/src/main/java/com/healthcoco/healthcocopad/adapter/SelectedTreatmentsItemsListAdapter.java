package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentsListItemListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SelectedTreatmentsItemsListViewholder;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 18-07-2017.
 */

public class SelectedTreatmentsItemsListAdapter extends BaseAdapter {
    HealthCocoFragment mFragment;
    private ArrayList<TreatmentItem> list;
    private HealthCocoActivity mActivity;
    private SelectedTreatmentsItemsListViewholder holder;
    private SelectedTreatmentsListItemListener treatmentsListItemListener;

    public SelectedTreatmentsItemsListAdapter() {
    }

    public SelectedTreatmentsItemsListAdapter(HealthCocoActivity activity, SelectedTreatmentsListItemListener treatmentsListItemListener, HealthCocoFragment mFragment) {
        this.mActivity = activity;
        this.treatmentsListItemListener = treatmentsListItemListener;
        this.mFragment = mFragment;
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
            holder = new SelectedTreatmentsItemsListViewholder(mActivity, treatmentsListItemListener, mFragment);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (SelectedTreatmentsItemsListViewholder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(ArrayList<TreatmentItem> list) {
        this.list = list;
    }
}