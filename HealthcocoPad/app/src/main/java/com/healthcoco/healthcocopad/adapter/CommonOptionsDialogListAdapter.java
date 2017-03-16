package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.enums.OptionsType;
import com.healthcoco.healthcocopad.viewholders.CommonOptionsDialogViewHolder;

import java.util.ArrayList;

public class CommonOptionsDialogListAdapter extends BaseAdapter {

    private HealthCocoActivity mActivity;
    private ArrayList<OptionsType> list;
    private CommonOptionsDialogViewHolder holder;
    private int selectedPosition = -1;

    public CommonOptionsDialogListAdapter(HealthCocoActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
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
            holder = new CommonOptionsDialogViewHolder(mActivity);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else {
            holder = (CommonOptionsDialogViewHolder) convertView.getTag();
        }
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(ArrayList<OptionsType> list) {
        this.list = list;

    }
}
