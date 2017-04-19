package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.DiagnosticTest;
import com.healthcoco.healthcocopad.listeners.DiagnosticTestItemListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.DiagnosticTestListViewHolder;

import java.util.List;

/**
 * Created by neha on 19/04/17.
 */

public class DiagnosticTestsListAdapter  extends BaseAdapter {
    private DiagnosticTestItemListener itemListener;
    private List<DiagnosticTest> list;
    private HealthCocoActivity mActivity;
    private DiagnosticTestListViewHolder holder;

    public DiagnosticTestsListAdapter() {
    }

    public DiagnosticTestsListAdapter(HealthCocoActivity activity, DiagnosticTestItemListener itemListener) {
        this.mActivity = activity;
        this.itemListener = itemListener;
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
            holder = new DiagnosticTestListViewHolder(mActivity, itemListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (DiagnosticTestListViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<DiagnosticTest> list) {
        this.list = list;
    }

}
