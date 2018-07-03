package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.PatientEducationVideo;
import com.healthcoco.healthcocopad.viewholders.PatientEducationVideosListViewHolder;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 01-08-2017.
 */

public class PatientEducationVideosListAdapter extends BaseAdapter {

    private final HealthCocoActivity mActivity;
    private ArrayList<PatientEducationVideo> list;
    private PatientEducationVideosListViewHolder holder;

    public PatientEducationVideosListAdapter(HealthCocoActivity activity) {
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
        holder = new PatientEducationVideosListViewHolder(mActivity);
        convertView = holder.getContentView();
        convertView.setTag(holder);
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
/*
        if (convertView == null) {
            holder = new DoctorVideosListViewHolder(mActivity);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (DoctorVideosListViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;*/
    }


    public void setListData(ArrayList<PatientEducationVideo> list) {
        this.list = list;
    }
}
