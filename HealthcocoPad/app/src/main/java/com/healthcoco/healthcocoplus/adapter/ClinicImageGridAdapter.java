package com.healthcoco.healthcocoplus.adapter;

import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.healthcoco.healthcocoplus.HealthCocoActivity;
import com.healthcoco.healthcocoplus.bean.server.ClinicImage;
import com.healthcoco.healthcocoplus.listeners.AddEditClinicImageListener;
import com.healthcoco.healthcocoplus.viewholders.ClinicImageGridItemViewholder;

import java.util.List;

/**
 * Created by neha on 02/02/16.
 */
public class ClinicImageGridAdapter extends BaseAdapter {
    private AddEditClinicImageListener addEditClinicImageListener;
    private List<Object> list;
    private HealthCocoActivity mActivity;
    private ClinicImageGridItemViewholder holder;

    public ClinicImageGridAdapter() {
    }

    public ClinicImageGridAdapter(HealthCocoActivity activity, AddEditClinicImageListener addEditClinicImageListener) {
        this.mActivity = activity;
        this.addEditClinicImageListener = addEditClinicImageListener;
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
        Object item = getItem(position);
        holder = new ClinicImageGridItemViewholder(mActivity, addEditClinicImageListener);
        if (item instanceof ClinicImage || item instanceof Bitmap)
            convertView = holder.getContentView();
        else
            convertView = holder.getAddImageContentView();
        convertView.setTag(holder);
        holder.setData(item);
        return convertView;
    }

    public void setListData(List<Object> list) {
        this.list = list;
    }
}
