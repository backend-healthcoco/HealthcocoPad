package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.Invoice;
import com.healthcoco.healthcocopad.bean.server.ReceiptResponse;
import com.healthcoco.healthcocopad.listeners.InvoiceItemClickListeners;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.InvoiceListItemViewHolder;
import com.healthcoco.healthcocopad.viewholders.ReceiptListItemViewHolder;

import java.util.List;

/**
 * Created by Prashant on 22-12-2017.
 */

public class ReceiptListAdapter extends BaseAdapter {

    private final HealthCocoActivity mActivity;
    private List<ReceiptResponse> list;
    private ReceiptListItemViewHolder holder;

    public ReceiptListAdapter(HealthCocoActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void setListData(List<ReceiptResponse> list) {
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
            holder = new ReceiptListItemViewHolder(mActivity);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (ReceiptListItemViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }
}
