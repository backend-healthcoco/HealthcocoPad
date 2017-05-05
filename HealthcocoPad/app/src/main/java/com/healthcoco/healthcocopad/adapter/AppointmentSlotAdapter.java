package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.AvailableTimeSlots;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.AppointmentTimeSlotViewHolder;

import java.util.List;

/**
 * Created by neha on 10/10/16.
 */
public class AppointmentSlotAdapter extends BaseAdapter {
    private static final String TAG = AppointmentSlotAdapter.class.getSimpleName();
    private List<AvailableTimeSlots> list;
    private HealthCocoActivity mActivity;
    private AppointmentTimeSlotViewHolder holder;
    private int selectedPosition = -1;

    public AppointmentSlotAdapter() {
    }

    public AppointmentSlotAdapter(HealthCocoActivity activity) {
        this.mActivity = activity;
    }

    @Override
    public int getCount() {
        if (Util.isNullOrEmptyList(list)) return 0;
        return list.size();
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
            holder = new AppointmentTimeSlotViewHolder(mActivity);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (AppointmentTimeSlotViewHolder) convertView.getTag();
        holder.setData(list.get(position));
        holder.applyData();

        if (position == selectedPosition) {
            LogUtils.LOGD(TAG, "Position selected true : " + position);
            holder.setSelected(true);
        } else {
            LogUtils.LOGD(TAG, "Position selected false : " + position);
            holder.setSelected(false);
        }
        return convertView;
    }

    public void setListData(List<AvailableTimeSlots> fragmentType) {
        this.list = fragmentType;
    }

    public void setSelected(int selected) {
        this.selectedPosition = selected;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
}
