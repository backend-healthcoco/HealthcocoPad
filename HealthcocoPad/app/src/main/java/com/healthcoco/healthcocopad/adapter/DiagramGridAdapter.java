package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.Diagram;
import com.healthcoco.healthcocopad.listeners.DiagramsGridItemListener;
import com.healthcoco.healthcocopad.viewholders.DiagramGridViewHolder;

import java.util.ArrayList;


public class DiagramGridAdapter extends BaseAdapter {
    private DiagramsGridItemListener diagramsGridItemListener;
    private ArrayList<Diagram> list;
    private HealthCocoActivity mActivity;
    private DiagramGridViewHolder holder;

    public DiagramGridAdapter() {
    }

    public DiagramGridAdapter(HealthCocoActivity activity, DiagramsGridItemListener diagramsGridItemListener) {
        this.mActivity = activity;
        this.diagramsGridItemListener = diagramsGridItemListener;
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
        holder = new DiagramGridViewHolder(mActivity, diagramsGridItemListener);
        convertView = holder.getContentView();
        convertView.setTag(holder);
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(ArrayList<Diagram> list) {
        this.list = list;
    }

    public Diagram getObject(int position) {
        return list.get(position);
    }
}
