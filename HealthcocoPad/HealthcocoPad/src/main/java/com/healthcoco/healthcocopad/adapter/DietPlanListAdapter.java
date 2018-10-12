package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.DietPlan;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.listeners.DietItemClickListeners;
import com.healthcoco.healthcocopad.listeners.InvoiceItemClickListeners;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.DietPlanListItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.InvoiceListItemViewHolder;

import java.util.List;

/**
 * Created by Shreshtha on 04-07-2017.
 */

public class DietPlanListAdapter extends BaseAdapter {

    private final HealthCocoActivity mActivity;
    private final DietItemClickListeners dietItemClickListeners;
    private List<DietPlan> list;
    private DietPlanListItemViewHolder holder;

    public DietPlanListAdapter(HealthCocoActivity mActivity, DietItemClickListeners dietItemClickListeners) {
        this.mActivity = mActivity;
        this.dietItemClickListeners = dietItemClickListeners;
    }

    public void setListData(List<DietPlan> list) {
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
            holder = new DietPlanListItemViewHolder(mActivity, dietItemClickListeners);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (DietPlanListItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }
}
