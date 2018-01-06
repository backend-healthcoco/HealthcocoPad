package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.listeners.InvoiceItemClickListeners;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.InvoiceListItemViewHolder;

import java.util.List;

/**
 * Created by Shreshtha on 04-07-2017.
 */

public class InvoiceListAdapter extends BaseAdapter {

    private final HealthCocoActivity mActivity;
    private final InvoiceItemClickListeners invoiceItemClickListeners;
    private List<Invoice> list;
    private InvoiceListItemViewHolder holder;

    public InvoiceListAdapter(HealthCocoActivity mActivity, InvoiceItemClickListeners invoiceItemClickListeners) {
        this.mActivity = mActivity;
        this.invoiceItemClickListeners = invoiceItemClickListeners;
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
            holder = new InvoiceListItemViewHolder(mActivity, invoiceItemClickListeners);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (InvoiceListItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }
}
