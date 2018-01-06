package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoFragment;
import com.healthcoco.healthcocopad.bean.server.InvoiceItem;
import com.healthcoco.healthcocopad.bean.server.TreatmentItem;
import com.healthcoco.healthcocopad.listeners.SelectedInvoiceListItemListener;
import com.healthcoco.healthcocopad.listeners.SelectedTreatmentsListItemListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SelectedInvoiceItemsListViewholder;
import com.healthcoco.healthcocopad.viewholders.SelectedTreatmentsItemsListViewholder;

import java.util.ArrayList;

/**
 * Created by Shreshtha on 18-07-2017.
 */

public class SelectedInvoiceItemsListAdapter extends BaseAdapter {
    HealthCocoFragment mFragment;
    private ArrayList<InvoiceItem> list;
    private HealthCocoActivity mActivity;
    private SelectedInvoiceItemsListViewholder holder;
    private SelectedInvoiceListItemListener invoiceListItemListener;

    public SelectedInvoiceItemsListAdapter() {
    }

    public SelectedInvoiceItemsListAdapter(HealthCocoActivity activity, SelectedInvoiceListItemListener invoiceListItemListener) {
        this.mActivity = activity;
        this.invoiceListItemListener = invoiceListItemListener;
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
            holder = new SelectedInvoiceItemsListViewholder(mActivity, invoiceListItemListener);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (SelectedInvoiceItemsListViewholder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(ArrayList<InvoiceItem> list) {
        this.list = list;
    }
}