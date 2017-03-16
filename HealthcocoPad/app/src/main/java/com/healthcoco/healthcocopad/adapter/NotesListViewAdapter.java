package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.listeners.NotesItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.NotesListItemViewHolder;

import java.util.List;

/**
 * Created by neha on 19/03/16.
 */
public class NotesListViewAdapter extends BaseAdapter {
    private NotesItemClickListener notesItemClickListener;
    private List<String> list;
    private HealthCocoActivity mActivity;
    private NotesListItemViewHolder holder;

    public NotesListViewAdapter() {
    }

    public NotesListViewAdapter(HealthCocoActivity activity, NotesItemClickListener notesItemClickListener) {
        this.mActivity = activity;
        this.notesItemClickListener = notesItemClickListener;
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
            holder = new NotesListItemViewHolder(mActivity, notesItemClickListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (NotesListItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<String> list) {
        this.list = list;
    }

}
