package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.SyncAll;
import com.healthcoco.healthcocopad.enums.SyncAllType;
import com.healthcoco.healthcocopad.listeners.SyncAllItemListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.SyncAllViewholder;

import java.util.List;

/**
 * Created by neha on 15/03/16.
 */
public class SyncAllAdapter extends BaseAdapter {
    private SyncAllItemListener syncAllItemListener;
    private List<SyncAll> list;
    private HealthCocoActivity mActivity;
    private SyncAllViewholder holder;
    private boolean isSyncAll;
    private int syncPosition;

    public SyncAllAdapter() {
    }

    public SyncAllAdapter(HealthCocoActivity activity, SyncAllItemListener syncAllItemListener) {
        this.mActivity = activity;
        this.syncAllItemListener = syncAllItemListener;
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
        SyncAll syncAll = (SyncAll) getItem(position);
        holder = new SyncAllViewholder(mActivity, syncAllItemListener);
        convertView = holder.getContentView();
        convertView.setTag(holder);
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<SyncAll> list) {
        this.list = list;
    }

    public void setSyncAll(boolean isSyncAll) {
        this.isSyncAll = isSyncAll;
    }

    public void setSyncPosition(int syncPosition) {
        this.syncPosition = syncPosition;
    }

    public void getPosition(SyncAllType syncAllType) {

    }
}
