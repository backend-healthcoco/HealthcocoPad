package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.AssessmentPersonalDetail;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.listeners.DietItemClickListeners;
import com.healthcoco.healthcocopad.listeners.InvoiceItemClickListeners;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.AssessmentListItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.InvoiceListItemViewHolder;

import java.util.List;

/**
 * Created by Prashant on 23-11-2018.
 */

public class AssessmentListAdapter extends BaseAdapter {

    private final HealthCocoActivity mActivity;
    private DietItemClickListeners dietItemClickListeners;
    private List<AssessmentPersonalDetail> list;
    private AssessmentListItemViewHolder holder;

    public AssessmentListAdapter(HealthCocoActivity mActivity, DietItemClickListeners dietItemClickListeners) {
        this.mActivity = mActivity;
        this.dietItemClickListeners = dietItemClickListeners;
    }

    public void setListData(List<AssessmentPersonalDetail> list) {
        this.list = list;
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
            holder = new AssessmentListItemViewHolder(mActivity, dietItemClickListeners);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (AssessmentListItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }
}
