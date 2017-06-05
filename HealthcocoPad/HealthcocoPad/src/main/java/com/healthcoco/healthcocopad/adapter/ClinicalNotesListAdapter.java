package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.ClinicalNotes;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.ClinicalNotesListItemViewHolder;

import java.util.List;

/**
 * Created by neha on 19/03/16.
 */
public class ClinicalNotesListAdapter extends BaseAdapter {
    private CommonEMRItemClickListener listItemClickListener;
    private List<ClinicalNotes> list;
    private HealthCocoActivity mActivity;
    private ClinicalNotesListItemViewHolder holder;

    public ClinicalNotesListAdapter() {
    }

    public ClinicalNotesListAdapter(HealthCocoActivity activity, CommonEMRItemClickListener listItemClickListener) {
        this.mActivity = activity;
        this.listItemClickListener = listItemClickListener;
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
            holder = new ClinicalNotesListItemViewHolder(mActivity,listItemClickListener, true);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (ClinicalNotesListItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<ClinicalNotes> list) {
        this.list = list;
    }

}
