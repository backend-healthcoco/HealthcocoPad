package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.DrugsListSolrResponse;
import com.healthcoco.healthcocopad.listeners.LoadMorePageListener;
import com.healthcoco.healthcocopad.listeners.SelectDrugItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SelectDrugListSolrViewHolder;

import java.util.List;

/**
 * Created by neha on 17/12/15.
 */
public class SelectDrugListSolrAdapter extends BaseAdapter {
    private LoadMorePageListener loadMorePageListener;
    private SelectDrugItemClickListener drugItemClickListener;
    private List<DrugsListSolrResponse> list;
    private HealthCocoActivity mActivity;
    private SelectDrugListSolrViewHolder holder;

    public SelectDrugListSolrAdapter() {
    }

    public SelectDrugListSolrAdapter(HealthCocoActivity activity, SelectDrugItemClickListener drugItemClickListener, LoadMorePageListener loadMorePageListener) {
        this.mActivity = activity;
        this.drugItemClickListener = drugItemClickListener;
        this.loadMorePageListener = loadMorePageListener;
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
            holder = new SelectDrugListSolrViewHolder(mActivity, drugItemClickListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (SelectDrugListSolrViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<DrugsListSolrResponse> list) {
        this.list = list;
    }

}

