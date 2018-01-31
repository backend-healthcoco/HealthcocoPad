package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.enums.NameHideActivateType;
import com.healthcoco.healthcocopad.listeners.NameHideActivateListener;
import com.healthcoco.healthcocopad.utilities.Util;
import com.healthcoco.healthcocopad.viewholders.NameHideActivateViewholder;

import java.util.List;

/**
 * Created by neha on 15/01/16.
 */
public class NameHideActivateAdapter extends BaseAdapter {
    public static final int HIDDEN_LIST_ADAPTER = 1;
    public static final int ACTIVATED_LIST_ADAPTER = 2;
    private int adapterType;
    private NameHideActivateType nameHideActivateType;
    private List<?> list;
    private HealthCocoActivity mActivity;
    private NameHideActivateViewholder holder;
    private NameHideActivateListener hideActivateListener;

    public NameHideActivateAdapter() {
    }

    public NameHideActivateAdapter(HealthCocoActivity activity, NameHideActivateListener hideActivateListener, int adapterType) {
        this.mActivity = activity;
        this.hideActivateListener = hideActivateListener;
        this.adapterType = adapterType;
    }

    @Override
    public int getCount() {
        if (Util.isNullOrEmptyList(list))
            return 0;
        return list.size();
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
        try {
            if (convertView == null) {
                holder = new NameHideActivateViewholder(mActivity, hideActivateListener, nameHideActivateType, position, adapterType);
                convertView = holder.getContentView();
                convertView.setTag(holder);
            } else
                holder = (NameHideActivateViewholder) convertView.getTag();
            holder.setData(list.get(position));
            holder.applyData();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return convertView;
    }


    public void setListData(NameHideActivateType nameHideActivateType, List<?> list) {
        this.list = list;
        this.nameHideActivateType = nameHideActivateType;
    }

    public void removeItemAndNotify(int position) {
        list.remove(position);
        notifyDataSetChanged();
    }
}
