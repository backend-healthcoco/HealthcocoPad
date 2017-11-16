package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.TreatmentService;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.TreatmentsListSolrViewHolder;

import java.util.List;

/**
 * Created by Shreshtha on 15-07-2017.
 */

public class TreatmentsListSolrAdapter extends BaseAdapter {
    private boolean isFromAddNewPrescriptionScreen;
    private List<TreatmentService> list;
    private HealthCocoActivity mActivity;
    private TreatmentsListSolrViewHolder holder;
    private SelectedTreatmentItemClickListener selectedTreatmentItemClickListener;

    public TreatmentsListSolrAdapter() {
    }

    public TreatmentsListSolrAdapter(HealthCocoActivity activity, SelectedTreatmentItemClickListener selectedTreatmentItemClickListener) {
        this.mActivity = activity;
        this.selectedTreatmentItemClickListener = selectedTreatmentItemClickListener;
//        this.isFromAddNewPrescriptionScreen = mActivity.getIntent().getBooleanExtra(HealthCocoConstants.TAG_COMMON_OPENUP_INTENT_DATA, false);
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
            holder = new TreatmentsListSolrViewHolder(mActivity, selectedTreatmentItemClickListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (TreatmentsListSolrViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<TreatmentService> list) {
        this.list = list;
    }
}
