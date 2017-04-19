package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.DrugItem;
import com.healthcoco.healthcocopad.fragments.AddPrescriptionVisitFragment;
import com.healthcoco.healthcocopad.fragments.AddVisitsFragment;
import com.healthcoco.healthcocopad.listeners.SelectedDrugsListItemListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SelectedPrescriptionDrugDoseItemsListViewHolder;
import com.healthcoco.healthcocopad.viewholders.SelectedTemplateDrugItemsListViewholder;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 17-04-2017.
 */

public class SelectedPrescriptionDrugItemsListAdapter extends BaseAdapter {
    private ArrayList<DrugItem> list;
    private HealthCocoActivity mActivity;
    private SelectedPrescriptionDrugDoseItemsListViewHolder holder;
    private SelectedDrugsListItemListener templateListener;
    private AddVisitsFragment addVisitsFragment;

    public SelectedPrescriptionDrugItemsListAdapter(HealthCocoActivity activity, AddPrescriptionVisitFragment templateListener, AddVisitsFragment addVisitsFragment) {
        this.mActivity = activity;
        this.templateListener = templateListener;
        this.addVisitsFragment = addVisitsFragment;
    }

    public SelectedPrescriptionDrugItemsListAdapter(HealthCocoActivity activity, SelectedDrugsListItemListener templateListener) {
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
            holder = new SelectedPrescriptionDrugDoseItemsListViewHolder(mActivity, templateListener,addVisitsFragment);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (SelectedPrescriptionDrugDoseItemsListViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(ArrayList<DrugItem> list) {
        this.list = list;
    }
}
