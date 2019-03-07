package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.VaccineBrandResponse;
import com.healthcoco.healthcocopad.listeners.SelectedBrandListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.BrandGroupListViewHolder;

import java.util.List;

public class BrandGroupListAdapter extends BaseAdapter {
    private HealthCocoActivity mActivity;
    private SelectedBrandListener selectedBrandListener;
    private List<VaccineBrandResponse> list;
    private BrandGroupListViewHolder holder;

    public BrandGroupListAdapter(HealthCocoActivity mActivity, SelectedBrandListener selectedBrandListener) {
        this.mActivity = mActivity;
        this.selectedBrandListener = selectedBrandListener;
    }

    @Override
    public int getCount() {
        if (!Util.isNullOrEmptyList(list))
            return list.size();
        return 0;
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
            holder = new BrandGroupListViewHolder(mActivity, selectedBrandListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (BrandGroupListViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<VaccineBrandResponse> list) {
        this.list = list;
    }
}
