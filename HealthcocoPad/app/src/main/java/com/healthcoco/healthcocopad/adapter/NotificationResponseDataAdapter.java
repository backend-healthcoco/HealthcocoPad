package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.HealthCocoViewHolder;
import com.healthcoco.healthcocopad.bean.NotificationResponse;
import com.healthcoco.healthcocopad.bean.server.Records;
import com.healthcoco.healthcocopad.listeners.CommonEMRItemClickListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.ReportsListItemViewHolder;

import java.util.List;

/**
 * Created by neha on 04/07/16.
 */
public class NotificationResponseDataAdapter extends BaseAdapter {
    private CommonEMRItemClickListener listItemClickListener;
    private List<NotificationResponse> list;
    private HealthCocoActivity mActivity;

    public NotificationResponseDataAdapter() {
    }

    public NotificationResponseDataAdapter(HealthCocoActivity activity, CommonEMRItemClickListener listItemClickListener) {
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
        HealthCocoViewHolder holder = null;
        NotificationResponse notificationResponse = ((NotificationResponse) getItem(position));
        switch (notificationResponse.getNotificationType()) {
            case REPORTS:
                Records record = (Records) notificationResponse.getData();
                holder = new ReportsListItemViewHolder(mActivity, listItemClickListener, true);
                convertView = holder.getContentView();
                holder.setData(record);
                holder.applyData();
                break;
        }
        return convertView;
    }

    public void setListData(List<NotificationResponse> list) {
        this.list = list;
    }

}
