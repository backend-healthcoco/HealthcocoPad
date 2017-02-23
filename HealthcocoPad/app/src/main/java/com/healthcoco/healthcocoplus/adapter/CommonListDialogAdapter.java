package com.healthcoco.healthcocoplus.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.enums.CommonListDialogType;
import com.healthcoco.healthcocoplus.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.viewholders.CommonListDialogViewHolder;

import java.util.List;

/**
 * Created by Shreshtha on 24-01-2017.
 */
public class CommonListDialogAdapter extends BaseAdapter {
    private CommonListDialogType popupType;
    private List<?> list;
    private HealthCocoActivity mActivity;
    private CommonListDialogViewHolder holder;
    private CommonListDialogItemClickListener commonListDialogItemClickListener;

    public CommonListDialogAdapter(HealthCocoActivity activity, CommonListDialogItemClickListener commonListDialogItemClickListener) {
        this.mActivity = activity;
        this.commonListDialogItemClickListener = commonListDialogItemClickListener;
    }

    @Override
    public int getCount() {
        if (Util.isNullOrEmptyList(list))
            return 0;
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            holder = new CommonListDialogViewHolder(mActivity, commonListDialogItemClickListener, popupType);
            convertView = holder.getContentView();
            convertView.setTag(holder);
        } else
            holder = (CommonListDialogViewHolder) convertView.getTag();
        holder.setData(list.get(position));
        holder.applyData();
        return convertView;
    }

    public void setListData(CommonListDialogType popupType, List<?> list) {
        this.list = list;
        this.popupType = popupType;
    }
}
