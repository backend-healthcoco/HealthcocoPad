package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SelectInvoiceListItemViewHolder;

import java.util.List;

/**
 * Created by Shreshtha on 08-07-2017.
 */

public class SelectInvoiceListAdapter extends BaseAdapter {

    private final HealthCocoActivity mActivity;
    private List<Invoice> list;
    private SelectInvoiceListItemViewHolder holder;

    public SelectInvoiceListAdapter(HealthCocoActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void setListData(List<Invoice> list) {
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
            holder = new SelectInvoiceListItemViewHolder(mActivity);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (SelectInvoiceListItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }
}
