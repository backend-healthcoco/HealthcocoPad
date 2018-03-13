package com.healthcoco.healthcocopad.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.R;
import com.healthcoco.healthcocopad.bean.server.CalendarEvents;
import com.healthcoco.healthcocopad.viewholders.QueueItemViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant on 07/03/2018.
 */

public class QueueRecyclerViewAdapter extends RecyclerView.Adapter<QueueItemViewHolder> {
    private final HealthCocoActivity mActivity;
    private ArrayList<CalendarEvents> list;

    public QueueRecyclerViewAdapter(HealthCocoActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public QueueItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        QueueItemViewHolder holder = new QueueItemViewHolder(mActivity, LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.list_item_queue, parent, false));
        return null;

    }

    @Override
    public void onBindViewHolder(QueueItemViewHolder holder, int position) {
//        holder.setData(list.get(position));
//        holder.applyData();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public void setListData(ArrayList<CalendarEvents> list) {
        this.list = list;
    }


    public Object getItem(int position) {
        return list.get(position);
    }
}