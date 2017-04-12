package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.bean.server.VisitDetails;
import com.healthcoco.healthcocopad.listeners.VisitDetailCombinedItemListener;
import com.healthcoco.healthcocopad.utilities.LogUtils;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.VisitDetailCombinedViewHolder;

import java.util.List;

/**
 * Created by Shreshtha on 07-03-2017.
 */
public class PatientDetailVisitAdapter
        extends BaseAdapter {
    private static final String TAG = PatientDetailVisitAdapter.class.getSimpleName();
    private VisitDetailCombinedItemListener listItemClickListener;
    private List<VisitDetails> list;
    private HealthCocoActivity mActivity;
    private HealthCocoViewHolder holder;
    private VisitDetailCombinedItemListener visitDetailCombinedItemListener;

    public PatientDetailVisitAdapter() {
    }

    public PatientDetailVisitAdapter(HealthCocoActivity activity, VisitDetailCombinedItemListener listItemClickListener) {
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
        LogUtils.LOGD(TAG, "notifyAdapter size " + position);
        if (convertView == null) {
            holder = new VisitDetailCombinedViewHolder(mActivity, listItemClickListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (VisitDetailCombinedViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<VisitDetails> list) {
        this.list = list;
    }
}

