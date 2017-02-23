package com.healthcoco.healthcocoplus.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.enums.CommonListDialogType;
import com.healthcoco.healthcocoplus.listeners.CommonListDialogItemClickListener;
import com.healthcoco.healthcocoplus.listeners.LoadMorePageListener;
import com.healthcoco.healthcocoplus.utilities.Util;
import com.healthcoco.healthcocoplus.viewholders.CommonListDialogViewHolder;

import java.util.List;

/**
 * Created by neha on 03/02/16.
 */
public class CommonListDialogSolrAdapter extends BaseAdapter {
    private LoadMorePageListener loadMorePageListener;
    private CommonListDialogType popupType;
    private List<?> list;
    private HealthCocoActivity mActivity;
    private CommonListDialogViewHolder holder;
    private CommonListDialogItemClickListener commonListDialogItemClickListener;

    public CommonListDialogSolrAdapter() {
    }

    public CommonListDialogSolrAdapter(HealthCocoActivity activity, CommonListDialogItemClickListener commonListDialogItemClickListener, LoadMorePageListener loadMorePageListener) {
        this.mActivity = activity;
        this.commonListDialogItemClickListener = commonListDialogItemClickListener;
        this.loadMorePageListener = loadMorePageListener;
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
