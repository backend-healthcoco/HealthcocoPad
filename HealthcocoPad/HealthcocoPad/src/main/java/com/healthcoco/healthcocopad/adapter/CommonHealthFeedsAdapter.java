package com.healthcoco.healthcocopad.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocopad.HealthCocoActivity;
import com.healthcoco.healthcocopad.bean.server.HealthcocoBlogResponse;
import com.healthcoco.healthcocopad.viewholders.CommonHealthFeedsViewHolder;

import java.util.List;

/**
 * Created by Shreshtha on 23-Sep-17.
 */

public class CommonHealthFeedsAdapter extends BaseAdapter {
    private List<?> list;
    private HealthCocoActivity mActivity;
    private CommonHealthFeedsViewHolder holder;

    public CommonHealthFeedsAdapter(HealthCocoActivity mActivity) {
        this.mActivity = mActivity;
    }

    @Override
    public int getCount() {
        if (list == null)
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            holder = new CommonHealthFeedsViewHolder(mActivity);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (CommonHealthFeedsViewHolder) convertView.getTag();
        holder.setData(getItem(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(List<HealthcocoBlogResponse> list) {
        this.list = list;
    }

}
